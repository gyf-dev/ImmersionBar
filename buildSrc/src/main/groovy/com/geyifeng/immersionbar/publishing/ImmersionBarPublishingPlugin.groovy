package com.geyifeng.immersionbar.publishing

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.bundling.Zip
import org.gradle.plugins.signing.SigningExtension

/**
 * 集中管理 ImmersionBar 的发布流程：
 *  - 为每个 Android library 模块配置 maven-publish / signing / 本地 staging 仓库
 *  - 在根项目注册 Central Portal Publisher API 上传任务
 *
 * 应用到根项目：id 'immersionbar.publishing'
 */
class ImmersionBarPublishingPlugin implements Plugin<Project> {

    // 库信息
    static final String LIB_GROUP_ID = "com.geyifeng.immersionbar"
    static final String LIB_SITE_URL = "https://github.com/gyf-dev/ImmersionBar"

    // 开源协议
    static final String LICENSE_NAME = 'The Apache License, Version 2.0'
    static final String LICENSE_URL = 'http://www.apache.org/licenses/LICENSE-2.0.txt'

    // 开发者信息
    static final String DEVELOPER_ID = 'gyf-dev'
    static final String DEVELOPER_NAME = 'gyf-dev'
    static final String DEVELOPER_EMAIL = 'gyf.dev@gmail.com'

    // SCM信息
    static final String SCM_CONNECTION = 'git@github.com:gyf-dev/ImmersionBar.git'
    static final String SCM_URL = "https://github.com/gyf-dev/ImmersionBar/tree/master"

    @Override
    void apply(Project rootProject) {
        if (rootProject != rootProject.rootProject) {
            throw new GradleException("immersionbar.publishing 插件应应用于根项目")
        }

        // 对每个 Android library 子模块配置发布
        rootProject.subprojects { Project project ->
            project.plugins.withId("com.android.library") {
                configureLibraryModule(project)
            }
        }

        // 注册 Central Portal 聚合任务
        rootProject.gradle.projectsEvaluated {
            configureCentralPortalTasks(rootProject)
        }
    }

    private void configureLibraryModule(Project project) {
        project.pluginManager.apply('maven-publish')
        project.pluginManager.apply('signing')

        // 从 local.properties 加载签名与凭据（保留原逻辑）
        loadSecretProps(project)

        project.afterEvaluate {
            def android = project.extensions.findByName('android')

            def sourcesJar = project.tasks.register('androidSourcesJar', Jar) {
                archiveClassifier.set('sources')
                if (android != null) {
                    from android.sourceSets.main.java.source
                }
                exclude "**/R.class"          //排除`R.class`
                exclude "**/BuildConfig.class" //排除`BuildConfig.class`
            }

            def javadocsJar = project.tasks.register('androidJavadocsJar', Jar) {
                archiveClassifier.set('javadoc')
            }

            def releaseComponent = project.components.findByName('release')
            if (releaseComponent == null) {
                throw new GradleException(
                        "模块 ${project.name} 缺少 release 组件，请在 android { publishing { singleVariant('release') } } 中声明")
            }

            def libVersion = project.rootProject.ext.immersionbar_version

            def publishing = project.extensions.getByType(PublishingExtension)
            publishing.publications.create('upload', MavenPublication) { publication ->
                println("publish-maven Log-------> LIB_GROUP_ID: ${LIB_GROUP_ID}; LIB_ARTIFACT_ID: ${project.name}; LIB_VERSION_NAME: ${libVersion}")

                publication.groupId = LIB_GROUP_ID
                publication.artifactId = project.name
                publication.version = libVersion.toString()

                publication.from releaseComponent
                publication.artifact sourcesJar.get()
                publication.artifact javadocsJar.get()

                publication.pom { pom ->
                    pom.name = project.name
                    pom.description = "Android " + project.name + " sdk"
                    pom.url = LIB_SITE_URL
                    pom.licenses { licenses ->
                        licenses.license { license ->
                            license.name = LICENSE_NAME
                            license.url = LICENSE_URL
                        }
                    }
                    pom.developers { developers ->
                        developers.developer { developer ->
                            developer.id = DEVELOPER_ID
                            developer.name = DEVELOPER_NAME
                            developer.email = DEVELOPER_EMAIL
                        }
                    }
                    pom.scm { scm ->
                        scm.connection = 'scm:' + SCM_CONNECTION
                        scm.developerConnection = 'scm:' + SCM_CONNECTION
                        scm.url = SCM_URL
                    }
                }
            }
            publishing.repositories.maven { repository ->
                repository.name = "localStaging"
                repository.url = project.rootProject.layout.buildDirectory.dir("central-staging").get().asFile.toURI()
            }

            def signing = project.extensions.getByType(SigningExtension)
            signing.sign publishing.publications
        }
    }

    private static void loadSecretProps(Project project) {
        project.ext["signing.keyId"] = ''
        project.ext["signing.password"] = ''
        project.ext["signing.secretKeyRingFile"] = ''
        project.ext["ossrhUsername"] = ''
        project.ext["ossrhPassword"] = ''

        File secretPropsFile = project.rootProject.file('local.properties')
        if (secretPropsFile.exists()) {
            println "Found secret props file, loading props"
            Properties p = new Properties()
            secretPropsFile.withInputStream { p.load(it) }
            p.each { name, value ->
                project.ext[name] = value
            }
        } else {
            println "No props file, loading env vars"
        }
    }

    private void configureCentralPortalTasks(Project rootProject) {
        def centralStagingDir = rootProject.layout.buildDirectory.dir("central-staging")
        def centralBundleDir = rootProject.layout.buildDirectory.dir("central-bundle")
        def libraryProjects = rootProject.subprojects.findAll { it.plugins.hasPlugin("com.android.library") }
        def stageTasks = libraryProjects.collect { project ->
            project.tasks.named("publishUploadPublicationToLocalStagingRepository")
        }
        def immersionbarVersion = rootProject.ext.immersionbar_version

        def cleanCentralStaging = rootProject.tasks.register("cleanCentralStaging", Delete) {
            delete centralStagingDir
        }

        stageTasks.each { taskProvider ->
            taskProvider.configure {
                mustRunAfter cleanCentralStaging
            }
        }

        def zipCentralPortalBundle = rootProject.tasks.register("zipCentralPortalBundle", Zip) {
            group = "publishing"
            description = "Builds a Maven Central Portal upload bundle from all library publications."
            dependsOn cleanCentralStaging
            dependsOn stageTasks
            from centralStagingDir
            destinationDirectory = centralBundleDir
            archiveFileName = "central-bundle-${immersionbarVersion}.zip"
            doFirst {
                if (immersionbarVersion.toString().endsWith("SNAPSHOT")) {
                    throw new GradleException("Central Portal Publisher API upload only supports release versions: ${immersionbarVersion}")
                }
            }
        }

        def publishToCentralPortal = rootProject.tasks.register("publishToCentralPortal") {
            group = "publishing"
            description = "Uploads the Maven Central Portal bundle using the Publisher API."
            dependsOn zipCentralPortalBundle
            doLast {
                def username = loadPublishProperty(rootProject, "ossrhUsername")
                def password = loadPublishProperty(rootProject, "ossrhPassword")
                if (!username || !password) {
                    throw new GradleException("Missing Central Portal token. Please set ossrhUsername and ossrhPassword in local.properties or Gradle properties.")
                }

                def publishingType = (rootProject.findProperty("publishingType") ?: "USER_MANAGED").toString().toUpperCase()
                if (!(publishingType in ["USER_MANAGED", "AUTOMATIC"])) {
                    throw new GradleException("Invalid publishingType: ${publishingType}. Supported values: USER_MANAGED, AUTOMATIC")
                }

                def bundleFile = zipCentralPortalBundle.get().archiveFile.get().asFile
                if (!bundleFile.exists()) {
                    throw new GradleException("Central Portal bundle does not exist: ${bundleFile}")
                }

                def token = Base64.encoder.encodeToString("${username}:${password}".getBytes("UTF-8"))
                def deploymentName = (rootProject.findProperty("deploymentName") ?: "ImmersionBar-${immersionbarVersion}").toString()
                def query = "publishingType=${URLEncoder.encode(publishingType, "UTF-8")}&name=${URLEncoder.encode(deploymentName, "UTF-8")}"
                def uri = URI.create("https://central.sonatype.com/api/v1/publisher/upload?${query}")
                def boundary = "----ImmersionBarCentralPortal${System.currentTimeMillis()}"
                def lineBreak = "\r\n"
                def body = new ByteArrayOutputStream()

                body.write("--${boundary}${lineBreak}".getBytes("UTF-8"))
                body.write("Content-Disposition: form-data; name=\"bundle\"; filename=\"${bundleFile.name}\"${lineBreak}".getBytes("UTF-8"))
                body.write("Content-Type: application/octet-stream${lineBreak}${lineBreak}".getBytes("UTF-8"))
                body.write(bundleFile.bytes)
                body.write("${lineBreak}--${boundary}--${lineBreak}".getBytes("UTF-8"))

                println "Uploading ${bundleFile.name} to Central Portal (${publishingType})..."
                def client = java.net.http.HttpClient.newHttpClient()
                def request = java.net.http.HttpRequest.newBuilder(uri)
                        .header("Authorization", "Bearer ${token}")
                        .header("Content-Type", "multipart/form-data; boundary=${boundary}")
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofByteArray(body.toByteArray()))
                        .build()
                def response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString())
                if (response.statusCode() != 201) {
                    throw new GradleException("Central Portal upload failed: HTTP ${response.statusCode()}\n${response.body()}")
                }
                println "Central Portal deployment id: ${response.body()}"
            }
        }

        rootProject.tasks.register("publishAllToCentralPortal") {
            group = "publishing"
            description = "Publishes all library modules to Maven Central Portal using the Publisher API."
            dependsOn publishToCentralPortal
        }
    }

    private static String loadPublishProperty(Project project, String key) {
        if (project.hasProperty(key)) {
            return project.property(key)?.toString()
        }
        def secretPropsFile = project.rootProject.file("local.properties")
        if (secretPropsFile.exists()) {
            Properties props = new Properties()
            secretPropsFile.withInputStream { props.load(it) }
            def value = props.getProperty(key)
            if (value != null && value.trim().length() > 0) {
                return value
            }
        }
        return System.getenv(key) ?: System.getenv(key.toUpperCase())
    }
}

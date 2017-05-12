# ImmersionBar -- android 4.4以上沉浸式实现 

## 直接看效果图，最下面有各个版本的效果图
<img width="300"  src="https://github.com/gyf-dev/ImmersionBar/blob/master/screenshots/Screenshot_6.0.gif"/>

## 使用 
android studio用户用法如下，2.x.x版本全新的Api，调用更方便

	compile 'com.gyf.barlibrary:barlibrary:2.1.2'
eclipse用户直接下载jar包

[barlibrary-2.1.2.jar](https://github.com/gyf-dev/ImmersionBar/blob/master/jar/barlibrary-2.1.2.jar) 

版本说明

>2.1.2
- 修复4.4旋转屏幕为横屏时右边布局被导航栏挡住的问题
- 删除调试信息

>2.1.1
- 修复4.4旋转屏幕为横屏时底部出现多余的导航栏
- 修复4.4初始化沉浸式有时候不能实现效果的问题

>2.1.0
- 全新的链式调用，更方便，删除了1.x.x版本Api
- 修复了4.4不能时刻改变颜色的bug
- 修复了4.4 activity全屏时底部被导航栏遮挡的bug
- 修复状态栏和布局重叠的问题，调用fitsSystemWindows(true)即可，必须指定状态栏的颜色statusBarColor()
- 增加了View支持变色功能
- 删除[SystemBarTint](https://github.com/jgilfelt/SystemBarTint)的库的依赖

### [1.x.x 用户请点击](https://github.com/gyf-dev/ImmersionBar/blob/master/README_1.x.md)
 
## 下载demo 
### [点我](https://github.com/gyf-dev/ImmersionBar/blob/master/apk/sample-debug.apk) 
  
## 用法 
### 初始化
- 基础用法（已经可以满足日常沉浸式）

    ```
    ImmersionBar.with(this).init();
    ```
- 高级用法(每个参数的意义)

    ```
     ImmersionBar.with(this)
                     .transparentStatusBar()  //透明状态栏，不写默认透明色
                     .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                     .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                     .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                     .navigationBarColor(R.color.colorPrimary) //导航栏颜色，不写默认黑色
                     .barColor(R.color.colorPrimary)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
                     .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
                     .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
                     .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
                     .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                     .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                     .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
                     .setViewSupportTransformColor(toolbar) //设置支持view变色，支持一个view，不指定颜色，默认和状态栏同色，还有两个重载方法
                     .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
                     .fitsSystemWindows(false)    //解决状态栏和布局重叠问题，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色
                     .statusBarColorTransform(R.color.orange)  //状态栏变色后的颜色
                     .navigationBarColorTransform(R.color.orange) //导航栏变色后的颜色
                     .barColorTransform(R.color.orange)  //状态栏和导航栏变色后的颜色
                     .removeSupportView()  //移除通过setViewSupportTransformColor()方法指定的view
                     .removeSupportView(toolbar)  //移除指定view支持
                     .removeSupportAllView() //移除全部view支持
                     .init();  //必须调用方可沉浸式
    ```
### 关闭销毁
- 在activity的onDestroy方法中执行

    ```
    ImmersionBar.with(this).destroy();
    ```
	
## 建议
- 建议在BaseActivity中初始化和销毁

    ```
    Ipublic class BaseActivity extends AppCompatActivity {
         @Override
         protected void onCreate(@Nullable Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             ImmersionBar.with(this).init();
         }
     
         @Override
         protected void onDestroy() {
             super.onDestroy();
             ImmersionBar.with(this).destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
         }
     }
    ```
## 状态栏与布局顶部重叠解决方案，三种方案任选其一
- ① 使用dimen自定义状态栏高度
    ```
        values-v19/dimens.xml
        
        <dimen name="status_bar_height">25dp</dimen>
        
        values/dimens.xml
        
        <dimen name="status_bar_height">0dp</dimen>
    ```
    然后在布局界面添加view标签，高度指定为status_bar_height
    ```
        <View
           android:layout_width="match_parent"
           android:layout_height="@dimen/status_bar_height" />
    ```
- ② 使用ImmersionBar的fitsSystemWindows()方法
    ```
        ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .fitsSystemWindows(true)  //使用该属性必须指定状态栏的颜色，不然状态栏透明，很难看
            .init();
    ```
    
- ③ 使用系统的fitsSystemWindows属性
   ```
       <?xml version="1.0" encoding="utf-8"?>
       <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
           android:layout_width="match_parent"
           android:layout_height="match_parent"
           android:orientation="vertical"
           android:fitsSystemWindows="true">
       </LinearLayout>
   ```
   然后使用ImmersionBar时候必须指定状态栏颜色
   ```
       ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .init();
   ```
- 注意：②和③的fitsSystemWindows方法和属性，不要一起使用
## 状态栏和导航栏其它方法
	
- public static boolean hasNavigationBar(Activity activity)
 
    判断是否存在导航栏
    
- public static int getNavigationBarHeight(Activity activity)
 
    获得导航栏的高度
 
- public static int getNavigationBarWidth(Activity activity)
 
    获得导航栏的宽度
    
- public static boolean isNavigationAtBottom(Activity activity)
 
    判断导航栏是否在底部
    
- public static int getStatusBarHeight(Activity activity)
 
    或得状态栏的高度
    
- public static int getActionBarHeight(Activity activity)
 
    或得ActionBar得高度

## 效果图 ##
#### 说明 ####
- 这是状态栏
![image](https://github.com/gyf-dev/ImmersionBar/blob/master/screenshots/StatusBar.png)
- 这是导航栏（有些手机没有导航栏）
![image](https://github.com/gyf-dev/ImmersionBar/blob/master/screenshots/NavigationBar.png)
#### 动态图 ####
- android 6.0 有导航栏效果
<img width="300"  src="https://github.com/gyf-dev/ImmersionBar/blob/master/screenshots/Screenshot_6.0.gif"/>

- android 4.4 有导航栏效果
<img width="300"  src="https://github.com/gyf-dev/ImmersionBar/blob/master/screenshots/Screenshot_4.4.gif"/>

- android 4.4 没有导航栏效果
<img width="300"  src="https://github.com/gyf-dev/ImmersionBar/blob/master/screenshots/Screenshot_4.4_no.gif"/>

## 联系我 ##
- QQ 969565471
- WeChat(微信)
<img width="300"  src="https://github.com/gyf-dev/ImmersionBar/blob/master/screenshots/wechat.JPG"/>
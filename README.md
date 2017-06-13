# ImmersionBar -- android 4.4以上沉浸式实现 

## 直接看效果图，最下面有各个版本的效果图
<img width="300"  src="https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/Screenshot_6.0.gif"/>

## 使用 
> android studio

   ```groovy
   compile 'com.gyf.barlibrary:barlibrary:2.2.3'
   ```

>eclipse

[barlibrary-2.2.3.jar](https://github.com/gyf-dev/ImmersionBar/blob/master/jar/barlibrary-2.2.3.jar) 

## 版本说明
### [点我](https://github.com/gyf-dev/ImmersionBar/wiki)

## 下载demo 
### [下载](https://github.com/gyf-dev/ImmersionBar/blob/master/apk/sample-2.2.3.apk) 
  
## 用法 
### 初始化
- 基础用法（已经可以满足日常沉浸式）

    ```java
    ImmersionBar.with(this).init();
    ```
- 高级用法(每个参数的意义)

    ```java
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
                 .flymeOSStatusBarFontColor(R.color.btn3)  //修改flyme OS状态栏字体颜色
                 .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                 .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
                 .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
                 .titleBar(view)    //解决状态栏和布局重叠问题，任选其一
                 .statusBarView(view)  //解决状态栏和布局重叠问题，任选其一
                 .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色
                 .supportActionBar(true) //支持ActionBar使用
                 .statusBarColorTransform(R.color.orange)  //状态栏变色后的颜色
                 .navigationBarColorTransform(R.color.orange) //导航栏变色后的颜色
                 .barColorTransform(R.color.orange)  //状态栏和导航栏变色后的颜色
                 .removeSupportView(toolbar)  //移除指定view支持
                 .removeSupportAllView() //移除全部view支持
                 .addTag("tag")  //给以上设置的参数打标记
                 .getTag("tag")  //根据tag获得沉浸式参数
                 .reset()  //重置所以沉浸式参数
                 .init();  //必须调用方可沉浸式
    ```
    
### 关闭销毁
- 在activity的onDestroy方法中执行

    ```java
    ImmersionBar.with(this).destroy(); //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    ```
	
## 建议
- 建议在BaseActivity中初始化和销毁

    ```java
    public class BaseActivity extends AppCompatActivity {
         @Override
         protected void onCreate(@Nullable Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             ImmersionBar.with(this).init();   //所有子类都将继承这些相同的属性
         }
     
         @Override
         protected void onDestroy() {
             super.onDestroy();
             ImmersionBar.with(this).destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
         }
     }
    ```

## 在Fragment中的用法（fragment+viewpager）
为了使每个fragment都可以设置不同的沉浸式样式，这里给出两种解决方式，这两种实现效果都一样的

-  ①使用viewpager的addOnPageChangeListener方法，代码如下

   ```java
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        ImmersionBar.with(oneFragment)
                                .navigationBarColor(R.color.btn4)
                                .init();
                        break;
                    case 1:
                        ImmersionBar.with(twoFragment)
                                .statusBarDarkFont(true)
                                .navigationBarColor(R.color.btn3)
                                .init();
                        break;
                    case 2:
                        ImmersionBar.with(threeFragment)
                                .navigationBarColor(R.color.btn13)
                                .init();
                        break;
                    case 3:
                        ImmersionBar.with(fourFragment)
                                .statusBarDarkFont(true)
                                .navigationBarColor(R.color.btn1)
                                .init();
                        break;
                }
            }
    
            @Override
            public void onPageSelected(int position) {
    
            }
    
            @Override
            public void onPageScrollStateChanged(int state) {
    
            }
        });
    ```
    
-  ②继承ImmersionFragment类，在immersionInit中初始化沉浸式，调用该方法必须保证加载Fragment的Activity先初始化,代码如下：

    ```java
    public class OneFragment extends ImmersionFragment {
         @Nullable
         @Override
            public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                return inflater.inflate(R.layout.fragment_one, container, false);
            }
            @Override
            protected void immersionInit() {
                ImmersionBar.with(this) 
                        .statusBarDarkFont(false)
                        .navigationBarColor(R.color.btn4)
                        .init();
            }
        }
    ```
- 如果想使当前Fragment不走immersionInit()方法，请子类重写immersionEnabled()方法，返回值设为false
   ```java
        @Override
        protected boolean immersionEnabled() {
            return false;
        }
   ```
- 如果你的Fragment页面沉浸式都一样的话，你完全可以把它放在activity里的onCreate方法中初始化

## 状态栏与布局顶部重叠解决方案，五种方案任选其一
- ① 使用dimen自定义状态栏高度

    在values-v19/dimens.xml文件下
    ```xml
        <dimen name="status_bar_height">25dp</dimen>
     ```
    
    在values/dimens.xml文件下     
    ```xml
        <dimen name="status_bar_height">0dp</dimen>
    ```
    
    然后在布局界面添加view标签，高度指定为status_bar_height
    ```xml
       <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
           xmlns:app="http://schemas.android.com/apk/res-auto"
           android:layout_width="match_parent"
           android:layout_height="match_parent"
           android:background="@color/darker_gray"
           android:orientation="vertical">
       
           <View
               android:layout_width="match_parent"
               android:layout_height="@dimen/status_bar_height"
               android:background="@color/colorPrimary" />
       
           <android.support.v7.widget.Toolbar
               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:background="@color/colorPrimary"
               app:title="方法一"
               app:titleTextColor="@android:color/white" />
       </LinearLayout>
    ```
  
- ② 使用系统的fitsSystemWindows属性，要慎用，会有意想不到的坑，比如界面会发生错位

   ```xml
       <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
           android:layout_width="match_parent"
           android:layout_height="match_parent"
           android:orientation="vertical"
           android:fitsSystemWindows="true">
       </LinearLayout>
   ```
   然后使用ImmersionBar时候必须指定状态栏颜色
   ```java
       ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .init();
   ```

- ③ 使用ImmersionBar的fitsSystemWindows(boolean fits)方法

    ```java
        ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .fitsSystemWindows(true)  //使用该属性必须指定状态栏的颜色，不然状态栏透明，很难看
            .init();
    ```
- ④ 使用ImmersionBar的statusBarView(View view)方法

    在标题栏的上方增加View标签，高度指定为0dp
    ```xml
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
               xmlns:app="http://schemas.android.com/apk/res-auto"
               android:layout_width="match_parent"
               android:layout_height="match_parent"
               android:background="@color/darker_gray"
               android:orientation="vertical">
           
               <View
                   android:layout_width="match_parent"
                   android:layout_height="0dp"
                   android:background="@color/colorPrimary" />
           
               <android.support.v7.widget.Toolbar
                   android:layout_width="match_parent"
                   android:layout_height="wrap_content"
                   android:background="@color/colorPrimary"
                   app:title="方法四"
                   app:titleTextColor="@android:color/white" />
        </LinearLayout>
     ```
      
    然后使用ImmersionBar的statusBarView方法，指定view就可以啦
    ```java
         ImmersionBar.with(this)
               .statusBarView(view)
               .init();
     ```   
- ⑤ 使用ImmersionBar的titleBar(View view)方法
    ```java
             ImmersionBar.with(this)
                   .titleBar(view) //指定标题栏view,xml里的标题的高度不能指定为warp_content
                   .init();
     ```
       
## 解决EditText和软键盘的问题
   ```java
         KeyboardPatch.patch(this, linearLayout).enable(); //解决底部EditText和软键盘的问题，linearLayout指的是当前布局的根节点
   ```
 
<img width="300"  src="https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/Screenshot_edit.gif"/>

## 当白色背景状态栏遇到不能改变状态栏字体为深色的设备时，解决方案
   ```java
         ImmersionBar.with(this)
                     .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                     .init();
   ```
<img width="300"  src="https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/whiteStatusBar.png"/>

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
    
- public static boolean isSupportStatusBarDarkFont()
 
    判断当前设备支不支持状态栏字体设置为黑色

## 效果图 ##
#### 说明 ####
- 这是状态栏
![image](https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/StatusBar.png)
- 这是导航栏（有些手机没有导航栏）
![image](https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/NavigationBar.png)
#### 动态图 ####
- android 6.0 有导航栏效果
<img width="300"  src="https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/Screenshot_6.0.gif"/>

- android 4.4 有导航栏效果
<img width="300"  src="https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/Screenshot_4.4.gif"/>

- android 4.4 没有导航栏效果
<img width="300"  src="https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/Screenshot_4.4_no.gif"/>

## 特别鸣谢 ##
* 感谢[zhangzhen92](https://github.com/zhangzhen92)远程协助测试

## 联系我 ##
- QQ群 314360549（沉浸式交流）
- WeChat(微信)
<img width="300"  src="https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/wechat.JPG"/>
![logo](https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/readme_head.png)
# ImmersionBar -- android 4.4以上沉浸式实现 

## 直接看效果图，最下面有各个版本的效果图
<img width="300"  src="https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/Screenshot_6.0.gif"/>

## 使用 
> android studio

   ```groovy
   compile 'com.gyf.barlibrary:barlibrary:2.3.0'
   ```

>eclipse

[barlibrary-2.3.0.jar](https://github.com/gyf-dev/ImmersionBar/blob/master/jar/barlibrary-2.3.0.jar) 

## 版本说明
### [点我](https://github.com/gyf-dev/ImmersionBar/wiki)

## 下载demo 
### [下载](https://github.com/gyf-dev/ImmersionBar/blob/master/apk/immersionBar-2.3.0.apk) 
  
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
                 .titleBarMarginTop(view)     //解决状态栏和布局重叠问题，任选其一
                 .statusBarView(view)  //解决状态栏和布局重叠问题，任选其一
                 .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
                 .supportActionBar(true) //支持ActionBar使用
                 .statusBarColorTransform(R.color.orange)  //状态栏变色后的颜色
                 .navigationBarColorTransform(R.color.orange) //导航栏变色后的颜色
                 .barColorTransform(R.color.orange)  //状态栏和导航栏变色后的颜色
                 .removeSupportView(toolbar)  //移除指定view支持
                 .removeSupportAllView() //移除全部view支持
                 .navigationBarEnable(true)   //是否可以修改导航栏颜色，默认为true
                 .navigationBarWithKitkatEnable(true)  //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
                 .fixMarginAtBottom(true)   //已过时，当xml里使用android:fitsSystemWindows="true"属性时,解决4.4和emui3.1手机底部有时会出现多余空白的问题，默认为false，非必须
                 .addTag("tag")  //给以上设置的参数打标记
                 .getTag("tag")  //根据tag获得沉浸式参数
                 .reset()  //重置所以沉浸式参数
                 .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                 .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
                 .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
                       @Override
                       public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                           LogUtils.e(isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
                       }
                  })
                 .init();  //必须调用方可沉浸式
    ```
    
### 关闭销毁
- 在activity的onDestroy方法中执行

    ```java
    ImmersionBar.with(this).destroy(); //必须调用该方法，防止内存泄漏
    ```
	
## 建议
- 建议在BaseActivity中初始化和销毁,可以参看demo中[BaseActivity](https://github.com/gyf-dev/ImmersionBar/blob/master/sample/src/main/java/com/gyf/immersionbar/activity/BaseActivity.java)

    ```java
    public class BaseActivity extends AppCompatActivity {
  
         private ImmersionBar mImmersionBar;
         @Override
         protected void onCreate(@Nullable Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
           mImmersionBar = ImmersionBar.with(this);
           mImmersionBar.init();   //所有子类都将继承这些相同的属性
            
         }
     
         @Override
         protected void onDestroy() {
             super.onDestroy();
             if (mImmersionBar != null)
                mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
         }
     }
    ```

## 在Fragment中实现沉浸式

注意：2.2.6版本已将[ImmersionFragment](https://github.com/gyf-dev/ImmersionBar/blob/master/barlibrary/src/main/java/com/gyf/barlibrary/ImmersionFragment.java)这个类标记为过时，请用户自行使用懒加载方式实现

- 在Fragment使用ImmersionBar
  #### 第一种，当结合viewpager使用的时候，请使用懒加载的形式，参考demo中的[BaseLazyFragment](https://github.com/gyf-dev/ImmersionBar/tree/master/sample/src/main/java/com/gyf/immersionbar/fragment/one/BaseLazyFragment.java)这个类
  #### 第二种，当使用show()和hide()来控制Fragment显示隐藏的时候，参考demo中的[BaseTwoFragment](https://github.com/gyf-dev/ImmersionBar/tree/master/sample/src/main/java/com/gyf/immersionbar/fragment/two/BaseTwoFragment.java)这个类
  注意：
  - 2.2.7版本以后别忘了在Fragment的onDestroy方法里销毁沉浸式了，2.2.7版本之前不需要调用
  
  ```java
     @Override
     protected void onDestroy() {
         super.onDestroy();
         if (mImmersionBar != null)
            mImmersionBar.destroy();  
     }
  ```
  - 以show()和hide()方式控制Fragment显示隐藏，别忘了重写onHiddenChanged方法，如下
  
  ```java
       @Override
       public void onHiddenChanged(boolean hidden) {
           super.onHiddenChanged(hidden);
           if (!hidden && mImmersionBar != null)
              mImmersionBar.init();
       }
    ```
- 在Activity使用ImmersionBar
  #### 第一种，当结合viewpager使用的时候，请使用viewpager的addOnPageChangeListener的方法监听沉浸式，参考demo中[FragmentThreeActivity](https://github.com/gyf-dev/ImmersionBar/blob/master/sample/src/main/java/com/gyf/immersionbar/activity/FragmentThreeActivity.java)这个类
  #### 第二种，当使用show()和hide()来控制Fragment显示隐藏的时候，请在tab切换的时候使用ImmersionBar，参考demo中[FragmentFourActivity](https://github.com/gyf-dev/ImmersionBar/blob/master/sample/src/main/java/com/gyf/immersionbar/activity/FragmentFourActivity.java)这个类
- 使用Fragment第三方框架[Fragmentation](https://github.com/YoKeyword/Fragmentation)实现沉浸式
  #### 参考demo中[FragmentFiveActivity](https://github.com/gyf-dev/ImmersionBar/blob/master/sample/src/main/java/com/gyf/immersionbar/activity/FragmentFiveActivity.java)和[BaseFiveFragment](https://github.com/gyf-dev/ImmersionBar/blob/master/sample/src/main/java/com/gyf/immersionbar/fragment/five/BaseFiveFragment.java)这个类

## 在Dialog中实现沉浸式，具体实现参考demo
- ①结合dialogFragment使用，可以参考demo中的[BaseDialogFragment](https://github.com/gyf-dev/ImmersionBar/blob/master/sample/src/main/java/com/gyf/immersionbar/fragment/dialog/BaseDialogFragment.java)这个类

   ```java
         ImmersionBar.with(this, dialog) .init();
      
   ```
- ②其他dialog
    ```java
            ImmersionBar.with(this, dialog, "flag")   //第三个参数是为当前Dialog加上标记，多个Dialog之间不可相同
                        .init();
         
    ```
    
   注意：在dialog使用，当销毁dialog同时，别忘了调用ImmersionBar的destroy方法了
   
<img width="300"  src="https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/Screenshot_dialog.gif"/>

## 状态栏与布局顶部重叠解决方案，六种方案任选其一
- ① 使用dimen自定义状态栏高度，不建议使用，因为设备状态栏高度并不是固定的

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
  
- ② 使用系统的fitsSystemWindows属性，使用该属性不会导致输入框与软键盘冲突问题，不要再Fragment使用该属性

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
   - 注意：ImmersionBar一定要在设置完布局以后使用，

- ③ 使用ImmersionBar的fitsSystemWindows(boolean fits)方法

    ```java
        ImmersionBar.with(this)
            .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
            .statusBarColor(R.color.colorPrimary)
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
         //或者
         //ImmersionBar.setStatusBarView(this,view);
     ```   
- ⑤ 使用ImmersionBar的titleBar(View view)方法，原理是设置paddingTop
    ```java
             ImmersionBar.with(this)
                   .titleBar(view) //可以为任意view，如果是自定义xml实现标题栏的话，最外层节点不能为RelativeLayout
                   .init();
             //或者
             //ImmersionBar.setTitleBar(this, view);
     ```
- ⑥ 使用ImmersionBar的titleBarMarginTop(View view)方法，原理是设置marginTop
    ```java
             ImmersionBar.with(this)
                   .titleBarMarginTop(view)  //可以为任意view
                   .statusBarColor(R.color.colorPrimary)  //指定状态栏颜色,根据情况是否设置
                   .init();
             //或者使用静态方法设置
             //ImmersionBar.setTitleBarMarginTop(this,view);
     ```
       
## 解决EditText和软键盘的问题

 - 第一种方案
   ```java
       ImmersionBar.with(this)
                   .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
               //  .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
               //                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //软键盘自动弹出
                   .init();
   ```
 - 第二种方案
   不使用keyboardEnable方法，只需要在布局的根节点（最外层节点）加上android:fitsSystemWindows="true"属性即可，只适合纯色状态栏
    
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
 
    获得状态栏的高度
    
- public static int getActionBarHeight(Activity activity)
 
    获得ActionBar的高度
    
- public static boolean isSupportStatusBarDarkFont()
 
    判断当前设备支不支持状态栏字体设置为黑色

- public static void hideStatusBar(Window window) 
 
    隐藏状态栏
    
## 混淆规则(proguard-rules.pro)
   ```
    -keep class com.gyf.barlibrary.* {*;} 
   ```

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
* 感谢[zhangzhen92](https://github.com/zhangzhen92)、 [yutouxiansheng](https://github.com/yutouxiansheng) 、[MrWhhh](https://github.com/MrWhhh)提供测试

## 联系我 ##
- QQ群 314360549（问题交流）
- WeChat(微信)
<img width="300"  src="https://github.com/gyf-dev/Screenshots/blob/master/ImmersionBar/wechat.JPG"/>
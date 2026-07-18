package com.gyf.immersionbar.sample.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.gyf.immersionbar.BarProperties
import com.gyf.immersionbar.ktx.checkFitsSystemWindows
import com.gyf.immersionbar.ktx.hideStatusBar
import com.gyf.immersionbar.ktx.immersionBar
import com.gyf.immersionbar.ktx.isSupportNavigationIconDark
import com.gyf.immersionbar.ktx.isSupportStatusBarDarkFont
import com.gyf.immersionbar.ktx.showStatusBar
import com.gyf.immersionbar.sample.R
import com.gyf.immersionbar.sample.databinding.ActivityParamsBinding

/**
 * @author geyifeng
 * @date 2019/3/27 7:20 PM
 */
class KotlinActivity : BaseKotlinActivity() {

    private var mIsHideStatusBar = false

    /**
     * Bar最新快照，由OnBarPropertiesChangedListener回调下发
     */
    private var mBarProperties: BarProperties? = null

    private val viewBinding by lazy {
        ActivityParamsBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    override fun initImmersionBar() {
        immersionBar {
            titleBar(viewBinding.toolbar)
            navigationBarColor(R.color.btn13)
            setOnStatusBarListener {
                val text = "状态栏${
                    if (it) {
                        "显示了"
                    } else {
                        "隐藏了"
                    }
                }"
                Toast.makeText(this@KotlinActivity, text, Toast.LENGTH_SHORT).show()
            }
            setOnNavigationBarListener { show, _ ->
                val text = "导航栏${
                    if (show) {
                        "显示了"
                    } else {
                        "隐藏了"
                    }
                }"
                Toast.makeText(this@KotlinActivity, text, Toast.LENGTH_SHORT).show()
            }
            addOnBarPropertiesChangedListener {
                Log.d("KotlinActivity", "onBarPropertiesChanged: $it")
                mBarProperties = it
                initView()
            }
        }
    }

    override fun initData() {
        super.initData()
        viewBinding.toolbar.title = intent.getCharSequenceExtra("title")
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        // 等待OnBarPropertiesChangedListener回调下发Bar快照后再刷新UI
        val p = mBarProperties ?: return
        viewBinding.apply {
            tvPortrait.text = "${tvPortrait.title}${p.isPortrait}".content()
            tvLandscapeLeft.text = "${tvLandscapeLeft.title}${p.isLandscapeLeft}".content()
            tvLandscapeRight.text = "${tvLandscapeRight.title}${p.isLandscapeRight}".content()
            tvStatus.text = "${tvStatus.title}${p.statusBarHeight}".content()
            tvStatusVisible.text = "${tvStatusVisible.title}${p.isStatusBarVisible}".content()
            tvHasNav.text = "${tvHasNav.title}${p.hasNavigationBar()}".content()
            tvNavVisible.text = "${tvNavVisible.title}${p.isNavigationBarVisible}".content()
            tvNavAtBottom.text = "${tvNavAtBottom.title}${p.isNavigationAtBottom}".content()
            tvNavType.text = "${tvNavType.title}${p.navigationBarType}".content()
            tvNav.text = "${tvNav.title}${p.navigationBarHeight}".content()
            tvNavWidth.text = "${tvNavWidth.title}${p.navigationBarWidth}".content()
            tvGesture.text = "${tvGesture.title}${p.isGestureNavigation}".content()
            tvHasNotch.text = "${tvHasNotch.title}${p.isNotchScreen}".content()
            tvNotchHeight.text = "${tvNotchHeight.title}${p.notchHeight}".content()
            tvAction.text = "${tvAction.title}${p.actionBarHeight}".content()
            // 以下信息不在BarProperties快照中，仍使用静态/扩展方法获取
            tvFits.text =
                "${tvFits.title}${findViewById<View>(android.R.id.content).checkFitsSystemWindows}".content()
            tvStatusDark.text = "${tvStatusDark.title}$isSupportStatusBarDarkFont".content()
            tvNavigationDark.text =
                "${tvNavigationDark.title}$isSupportNavigationIconDark".content()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setListener() {
        viewBinding.btnStatus.setOnClickListener {
            mIsHideStatusBar = if (!mIsHideStatusBar) {
                hideStatusBar()
                true
            } else {
                showStatusBar()
                false
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.tvInsets) { _, windowInsetsCompat ->
            viewBinding.tvInsets.text =
                "${viewBinding.tvInsets.title}${windowInsetsCompat.systemWindowInsetTop}".content()
            windowInsetsCompat.consumeSystemWindowInsets()
        }
    }

    private fun String.content(): SpannableString {
        val split = split("   ")
        return SpannableString(this).apply {
            val colorSpan =
                ForegroundColorSpan(ContextCompat.getColor(this@KotlinActivity, R.color.btn3))
            setSpan(
                colorSpan,
                this.length - split[1].length,
                this.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }

    private val TextView.title get() = text.toString().split("   ")[0] + "   "

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        initView()
    }
}

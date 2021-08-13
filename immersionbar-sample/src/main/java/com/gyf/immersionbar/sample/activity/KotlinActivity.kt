package com.gyf.immersionbar.sample.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.gyf.immersionbar.ktx.*
import com.gyf.immersionbar.sample.R
import com.gyf.immersionbar.sample.databinding.ActivityParamsBinding

/**
 * @author geyifeng
 * @date 2019/3/27 7:20 PM
 */
class KotlinActivity : BaseKotlinActivity() {

    private var mIsHideStatusBar = false

    private val viewBinding by lazy {
        ActivityParamsBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    override fun initImmersionBar() {
        immersionBar {
            titleBar(viewBinding.mToolbar)
            navigationBarColor(R.color.btn13)
            setOnNavigationBarListener { show, _ ->
                initView()
                val text = "导航栏${
                    if (show) {
                        "显示了"
                    } else {
                        "隐藏了"
                    }
                }"
                Toast.makeText(this@KotlinActivity, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun initData() {
        super.initData()
        viewBinding.mToolbar.title = intent.getCharSequenceExtra("title")
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        viewBinding.apply {
            mTvStatus.text = "${mTvStatus.title}$statusBarHeight".content()
            mTvHasNav.text = "${mTvHasNav.title}$hasNavigationBar".content()
            mTvNav.text = "${mTvNav.title}$navigationBarHeight".content()
            mTvNavWidth.text = "${mTvNavWidth.title}$navigationBarWidth".content()
            mTvAction.text = "${mTvAction.title}$actionBarHeight".content()
            mTvHasNotch.post { mTvHasNotch.text = "${mTvHasNotch.title}$hasNotchScreen".content() }
            mTvNotchHeight.post { mTvNotchHeight.text = "${mTvNotchHeight.title}$notchHeight".content() }
            mTvFits.text = "${mTvFits.title}${findViewById<View>(android.R.id.content).checkFitsSystemWindows}".content()
            mTvStatusDark.text = "${mTvStatusDark.title}$isSupportStatusBarDarkFont".content()
            mTvNavigationDark.text = "${mTvNavigationDark.title}$isSupportNavigationIconDark".content()
            mTvGesture.text = "${mTvGesture.title}$isGesture".content()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setListener() {
        viewBinding.mBtnStatus.setOnClickListener {
            mIsHideStatusBar = if (!mIsHideStatusBar) {
                hideStatusBar()
                true
            } else {
                showStatusBar()
                false
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.mTvInsets) { _, windowInsetsCompat ->
            viewBinding.mTvInsets.text = "${viewBinding.mTvInsets.title}${windowInsetsCompat.systemWindowInsetTop}".content()
            windowInsetsCompat.consumeSystemWindowInsets()
        }
    }

    private fun String.content(): SpannableString {
        val split = split("   ")
        return SpannableString(this).apply {
            val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this@KotlinActivity, R.color.btn3))
            setSpan(colorSpan, this.length - split[1].length, this.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }

    private val TextView.title get() = text.toString().split("   ")[0] + "   "

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        initView()
    }
}
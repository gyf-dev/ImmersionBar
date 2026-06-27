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
            titleBar(viewBinding.toolbar)
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
        viewBinding.toolbar.title = intent.getCharSequenceExtra("title")
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        viewBinding.apply {
            tvStatus.text = "${tvStatus.title}$statusBarHeight".content()
            tvHasNav.text = "${tvHasNav.title}$hasNavigationBar".content()
            tvNav.text = "${tvNav.title}$navigationBarHeight".content()
            tvNavWidth.text = "${tvNavWidth.title}$navigationBarWidth".content()
            tvAction.text = "${tvAction.title}$actionBarHeight".content()
            tvHasNotch.post { tvHasNotch.text = "${tvHasNotch.title}$hasNotchScreen".content() }
            tvNotchHeight.post { tvNotchHeight.text = "${tvNotchHeight.title}$notchHeight".content() }
            tvFits.text = "${tvFits.title}${findViewById<View>(android.R.id.content).checkFitsSystemWindows}".content()
            tvStatusDark.text = "${tvStatusDark.title}$isSupportStatusBarDarkFont".content()
            tvNavigationDark.text = "${tvNavigationDark.title}$isSupportNavigationIconDark".content()
            tvGesture.text = "${tvGesture.title}$isGesture".content()
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
            viewBinding.tvInsets.text = "${viewBinding.tvInsets.title}${windowInsetsCompat.systemWindowInsetTop}".content()
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

package com.gyf.immersionbar.sample.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.BarProperties
import com.gyf.immersionbar.ktx.barProperties
import com.gyf.immersionbar.ktx.immersionBar
import com.gyf.immersionbar.sample.AppManager
import com.gyf.immersionbar.sample.R

/**
 * 结合 Compose 使用
 */
class ComposeActivity : ComponentActivity() {

    private val mBarProperties by lazy { mutableStateOf(barProperties) }
    private var mIsHideStatusBar by mutableStateOf(false)
    private var mIsHideNavigationBar by mutableStateOf(false)
    private var mIsDarkFont by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.getInstance().addActivity(this)
        immersionBar {
            transparentStatusBar()
            navigationBarColor(R.color.btn13)
            statusBarDarkFont(mIsDarkFont)
            setOnBarListener {
                mBarProperties.value = it
            }
            setOnStatusBarListener {
                Toast.makeText(
                    this@ComposeActivity,
                    "状态栏${if (it) "显示了" else "隐藏了"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            setOnNavigationBarListener { show, _ ->
                Toast.makeText(
                    this@ComposeActivity,
                    "导航栏${if (show) "显示了" else "隐藏了"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        setContent {
            MaterialTheme {
                Surface(color = Color(0xFFF5F7FA)) {
                    ComposeDemoScreen(
                        barProperties = mBarProperties.value,
                        isHideStatusBar = mIsHideStatusBar,
                        isHideNavigationBar = mIsHideNavigationBar,
                        isDarkFont = mIsDarkFont,
                        onToggleStatusBar = ::toggleStatusBar,
                        onToggleNavigationBar = ::toggleNavigationBar,
                        onToggleStatusFont = ::toggleStatusFont
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.getInstance().removeActivity(this)
    }

    private fun toggleStatusBar() {
        mIsHideStatusBar = !mIsHideStatusBar
        updateBarVisibility()
    }

    private fun toggleNavigationBar() {
        mIsHideNavigationBar = !mIsHideNavigationBar
        updateBarVisibility()
    }

    private fun updateBarVisibility() {
        immersionBar {
            hideBar(
                when {
                    mIsHideStatusBar && mIsHideNavigationBar -> BarHide.FLAG_HIDE_BAR
                    mIsHideStatusBar -> BarHide.FLAG_HIDE_STATUS_BAR
                    mIsHideNavigationBar -> BarHide.FLAG_HIDE_NAVIGATION_BAR
                    else -> BarHide.FLAG_SHOW_BAR
                }
            )
        }
    }

    private fun toggleStatusFont() {
        mIsDarkFont = !mIsDarkFont
        immersionBar {
            statusBarDarkFont(mIsDarkFont)
        }
    }

    @Composable
    private fun ComposeDemoScreen(
        barProperties: BarProperties,
        isHideStatusBar: Boolean,
        isHideNavigationBar: Boolean,
        isDarkFont: Boolean,
        onToggleStatusBar: () -> Unit,
        onToggleNavigationBar: () -> Unit,
        onToggleStatusFont: () -> Unit
    ) {
        val statusBarHeight = barProperties.statusBarHeight
        val navigationBarHeight = barProperties.navigationBarHeight
        val statusBarHeightDp = with(LocalDensity.current) { statusBarHeight.toDp() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF5F7FA))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.colorPrimary))
                    .padding(top = statusBarHeightDp)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "结合 Compose 使用",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Compose 页面中同样可以通过 ImmersionBar 配置状态栏和导航栏。",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                InfoCard(
                    title = "Bar 信息",
                    rows = listOf(
                        "状态栏高度" to "$statusBarHeight px",
                        "状态栏是否显示" to "${barProperties.isStatusBarVisible}",
                        "是否有导航栏" to "${barProperties.hasNavigationBar()}",
                        "导航栏是否显示" to "${barProperties.isNavigationBarVisible}",
                        "导航栏高度" to "$navigationBarHeight px",
                        "导航栏位置在底部" to "${barProperties.isNavigationAtBottom}",
                        "刘海屏" to "${barProperties.isNotchScreen}",
                        "刘海高度" to "${barProperties.notchHeight} px"
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                InfoCard(
                    title = "操作演示",
                    rows = listOf(
                        "状态栏" to if (isHideStatusBar) "已隐藏" else "已显示",
                        "导航栏" to if (isHideNavigationBar) "已隐藏" else "已显示",
                        "状态栏字体" to if (isDarkFont) "深色" else "亮色"
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onToggleStatusBar,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.btn13),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = if (isHideStatusBar) "显示状态栏" else "隐藏状态栏")
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onToggleNavigationBar,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.colorPrimary),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = if (isHideNavigationBar) "显示导航栏" else "隐藏导航栏")
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onToggleStatusFont,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.btn3),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = if (isDarkFont) "切换为亮色状态栏字体" else "切换为深色状态栏字体")
                }
            }
        }
    }

    @Composable
    private fun InfoCard(title: String, rows: List<Pair<String, String>>) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    color = colorResource(id = R.color.colorPrimary),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                rows.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = row.first,
                            color = Color(0xFF555555),
                            fontSize = 14.sp
                        )
                        Text(
                            text = row.second,
                            color = colorResource(id = R.color.btn3),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

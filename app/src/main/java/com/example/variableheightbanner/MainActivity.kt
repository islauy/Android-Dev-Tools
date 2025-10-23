package com.example.variableheightbanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    BannerDemoScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerDemoScreen() {
    val items = remember {
        listOf(
            BannerItem(title = "秋季大促", color = Color(0xFFFF6F61), height = 190.dp),
            BannerItem(title = "数码会场", color = Color(0xFF42A5F5), height = 250.dp),
            BannerItem(title = "服饰上新", color = Color(0xFF66BB6A), height = 210.dp),
            BannerItem(title = "家居精选", color = Color(0xFFAB47BC), height = 280.dp),
            BannerItem(title = "超值秒杀", color = Color(0xFFFFA726), height = 160.dp)
        )
    }

    val pagerState = rememberPagerState(pageCount = { items.size })

    val containerHeight = rememberInterpolatedHeight(pagerState = pagerState) { pageIndex ->
        items[pageIndex].height
    }

    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxWidth().height(containerHeight)) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val item = items[page]
                BannerCard(item)
            }
        }
        Text(
            text = "左右滑动查看高度变化动画",
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun BannerCard(item: BannerItem) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(item.color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = item.title,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Derive container height as a pure function of pager offset, no side effects.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberInterpolatedHeight(
    pagerState: PagerState,
    getPageHeight: (Int) -> androidx.compose.ui.unit.Dp
): androidx.compose.ui.unit.Dp {
    val fraction = pagerState.currentPageOffsetFraction
    val current = pagerState.currentPage

    val goingToNext = fraction > 0f
    val targetPage = when {
        fraction == 0f -> current
        goingToNext -> (current + 1).coerceAtMost(pagerState.pageCount - 1)
        else -> (current - 1).coerceAtLeast(0)
    }

    val startHeight = getPageHeight(current).value
    val endHeight = getPageHeight(targetPage).value
    val t = abs(fraction)
    val value = (1f - t) * startHeight + t * endHeight
    return androidx.compose.ui.unit.Dp(value)
}

data class BannerItem(
    val title: String,
    val color: Color,
    val height: Dp
)

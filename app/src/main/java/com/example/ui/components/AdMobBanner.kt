package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.BuildConfig
import com.example.ui.theme.BorderOutline
import com.example.ui.theme.SurfaceContainerLow
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

// Production banner Ad Unit ID provided by user: ca-app-pub-1352153538954102/3746169237
// Official Google test banner unit id: ca-app-pub-3940256099942544/6300978111
private const val PROD_BANNER_AD_UNIT_ID = "ca-app-pub-1352153538954102/3746169237"
private const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

@Composable
fun AdMobBanner(
    modifier: Modifier = Modifier,
    useTestAdInDebug: Boolean = false // Direct user production ad unit
) {
    val adUnitId = if (useTestAdInDebug && BuildConfig.DEBUG) {
        TEST_BANNER_AD_UNIT_ID
    } else {
        PROD_BANNER_AD_UNIT_ID
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Subtle clean "Sponsored / Ad" tag conforming to Google AdMob layout guidelines
        Row(
            modifier = Modifier.padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF222736))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "SPONSORED",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceContainerLow)
                .border(1.dp, BorderOutline, RoundedCornerShape(12.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier.wrapContentSize(),
                factory = { ctx ->
                    AdView(ctx).apply {
                        setAdSize(AdSize.BANNER)
                        this.adUnitId = adUnitId
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
    }
}

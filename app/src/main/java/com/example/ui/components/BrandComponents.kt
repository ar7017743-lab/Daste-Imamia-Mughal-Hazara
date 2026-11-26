package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SiteSettingsEntity
import com.example.ui.AppLanguage
import com.example.ui.theme.BurnishedGold
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.IvoryWhite
import com.example.ui.theme.LightMutedText
import com.example.ui.theme.NastaliqUrduFontFamily
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.RegalDisplayFontFamily
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun IslamicGeometricPatternCanvas(
    modifier: Modifier = Modifier,
    lineColor: Color = BurnishedGold.copy(alpha = 0.14f)
) {
    Canvas(modifier = modifier) {
        val step = 52.dp.toPx()
        val cols = (size.width / step).toInt() + 2
        val rows = (size.height / step).toInt() + 2
        val r = step * 0.38f

        for (row in 0..rows) {
            for (col in 0..cols) {
                val cx = col * step
                val cy = row * step
                // 8-pointed Rub el Hizb double square
                drawOctagram(
                    center = Offset(cx, cy),
                    radius = r,
                    color = lineColor,
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawOctagram(
    center: Offset,
    radius: Float,
    color: Color,
    strokeWidth: Float
) {
    val square1 = Path()
    val square2 = Path()
    for (i in 0 until 4) {
        val a1 = Math.toRadians((i * 90 + 45).toDouble())
        val a2 = Math.toRadians((i * 90).toDouble())
        val p1 = Offset(
            center.x + (radius * cos(a1)).toFloat(),
            center.y + (radius * sin(a1)).toFloat()
        )
        val p2 = Offset(
            center.x + (radius * cos(a2)).toFloat(),
            center.y + (radius * sin(a2)).toFloat()
        )
        if (i == 0) {
            square1.moveTo(p1.x, p1.y)
            square2.moveTo(p2.x, p2.y)
        } else {
            square1.lineTo(p1.x, p1.y)
            square2.lineTo(p2.x, p2.y)
        }
    }
    square1.close()
    square2.close()
    drawPath(square1, color = color, style = Stroke(width = strokeWidth))
    drawPath(square2, color = color, style = Stroke(width = strokeWidth))
}

@Composable
fun IslamicGeometricDivider(
    modifier: Modifier = Modifier,
    color: Color = BurnishedGold
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(22.dp)
    ) {
        val cy = size.height / 2f
        val cx = size.width / 2f
        val starRadius = 8.dp.toPx()

        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Transparent, color.copy(alpha = 0.7f)),
                startX = 0f,
                endX = cx - starRadius * 2f
            ),
            start = Offset(16.dp.toPx(), cy),
            end = Offset(cx - starRadius * 1.8f, cy),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(color.copy(alpha = 0.7f), Color.Transparent),
                startX = cx + starRadius * 2f,
                endX = size.width
            ),
            start = Offset(cx + starRadius * 1.8f, cy),
            end = Offset(size.width - 16.dp.toPx(), cy),
            strokeWidth = 1.dp.toPx()
        )
        drawOctagram(
            center = Offset(cx, cy),
            radius = starRadius,
            color = color,
            strokeWidth = 1.3.dp.toPx()
        )
    }
}

/**
 * Prominent Golden Urdu Nastaliq Calligraphy Header on Obsidian Black
 * with clean English subtitle underneath.
 */
@Composable
fun UrduCalligraphyBrandHeader(
    settings: SiteSettingsEntity,
    currentLanguage: AppLanguage,
    isDarkTheme: Boolean,
    onLanguageSelected: (AppLanguage) -> Unit,
    onToggleTheme: () -> Unit,
    onOpenMenu: () -> Unit,
    onOpenAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = ObsidianBlack,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ObsidianBlack,
                            Color(0xFF14110B),
                            ObsidianBlack
                        )
                    )
                )
        ) {
            IslamicGeometricPatternCanvas(
                modifier = Modifier.matchParentSize(),
                lineColor = BurnishedGold.copy(alpha = 0.12f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top utility controls row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onOpenMenu,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("open_drawer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open navigation menu",
                            tint = ChampagneGold
                        )
                    }

                    // Language Switcher Pills (EN | اردو | العربية)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppLanguage.entries.forEach { lang ->
                            val selected = lang == currentLanguage
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (selected) BurnishedGold else Color(0xFF1D1A13),
                                border = BorderStroke(
                                    1.dp,
                                    if (selected) ChampagneGold else BurnishedGold.copy(alpha = 0.35f)
                                ),
                                modifier = Modifier
                                    .clickable { onLanguageSelected(lang) }
                                    .testTag("lang_${lang.name.lowercase()}")
                            ) {
                                Text(
                                    text = lang.nativeLabel,
                                    color = if (selected) ObsidianBlack else ChampagneGold,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onToggleTheme,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("theme_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle color theme",
                                tint = ChampagneGold
                            )
                        }
                        IconButton(
                            onClick = onOpenAdmin,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("admin_shortcut_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = "Open Admin CMS",
                                tint = BurnishedGold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Authentic Urdu Nastaliq Calligraphy Branding (Always Gold on Black, RTL)
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = settings.siteNameUrdu,
                        style = TextStyle(
                            fontFamily = NastaliqUrduFontFamily,
                            fontSize = 30.sp,
                            lineHeight = 56.sp,
                            brush = Brush.verticalGradient(
                                colors = listOf(ChampagneGold, BurnishedGold)
                            )
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .testTag("brand_urdu_calligraphy")
                    )
                }

                // Clean English Subtitle Underneath
                Text(
                    text = settings.siteSubtitleEnglish,
                    style = TextStyle(
                        fontFamily = RegalDisplayFontFamily,
                        fontSize = 11.sp,
                        letterSpacing = 2.6.sp,
                        fontWeight = FontWeight.Medium,
                        color = IvoryWhite.copy(alpha = 0.92f)
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(top = 2.dp, bottom = 6.dp)
                        .testTag("brand_english_subtitle")
                )

                IslamicGeometricDivider(
                    modifier = Modifier.padding(top = 2.dp),
                    color = BurnishedGold.copy(alpha = 0.75f)
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    urduTitle: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(18.dp)
                        .background(BurnishedGold, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            if (!urduTitle.isNullOrBlank()) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = urduTitle,
                        style = TextStyle(
                            fontFamily = NastaliqUrduFontFamily,
                            fontSize = 16.sp,
                            lineHeight = 32.sp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 11.dp)
                    )
                }
            }
        }

        if (actionLabel != null && onActionClick != null) {
            TextButton(
                onClick = onActionClick,
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Text(
                    text = actionLabel,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WebsiteFooter(
    settings: SiteSettingsEntity,
    onNavigate: (String) -> Unit,
    onOpenYouTube: () -> Unit,
    onOpenWhatsApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = ObsidianBlack,
        border = BorderStroke(1.dp, BurnishedGold.copy(alpha = 0.28f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IslamicGeometricPatternCanvas(
                modifier = Modifier.matchParentSize(),
                lineColor = BurnishedGold.copy(alpha = 0.08f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = settings.siteNameUrdu,
                        style = TextStyle(
                            fontFamily = NastaliqUrduFontFamily,
                            fontSize = 26.sp,
                            lineHeight = 48.sp,
                            color = ChampagneGold
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = settings.siteSubtitleEnglish,
                    style = TextStyle(
                        fontFamily = RegalDisplayFontFamily,
                        fontSize = 11.sp,
                        letterSpacing = 2.2.sp,
                        color = IvoryWhite.copy(alpha = 0.85f)
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(14.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val links = listOf(
                        "About Us" to "about",
                        "Noha Archive" to "videos",
                        "Majalis" to "majalis",
                        "Audio MP3" to "audio",
                        "Noha Lyrics" to "lyrics",
                        "Gallery" to "gallery",
                        "Articles" to "articles",
                        "Contact Us" to "contact",
                        "Admin CMS" to "admin"
                    )
                    links.forEach { (label, route) ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF191712),
                            border = BorderStroke(1.dp, BurnishedGold.copy(alpha = 0.25f)),
                            modifier = Modifier.clickable { onNavigate(route) }
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                color = ChampagneGold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onOpenYouTube,
                        border = BorderStroke(1.dp, BurnishedGold),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ChampagneGold),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("YouTube", style = MaterialTheme.typography.labelLarge)
                    }
                    OutlinedButton(
                        onClick = onOpenWhatsApp,
                        border = BorderStroke(1.dp, BurnishedGold),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ChampagneGold),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Text("WhatsApp", style = MaterialTheme.typography.labelLarge)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "TikTok: ${settings.tiktokHandle} • ${settings.contactEmail}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightMutedText,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "© 2026 ${settings.siteSubtitleEnglish} • All Rights Reserved",
                    style = MaterialTheme.typography.labelSmall,
                    color = LightMutedText.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

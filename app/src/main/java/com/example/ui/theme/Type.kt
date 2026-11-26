package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val NastaliqUrduFontFamily = FontFamily(
    Font(R.font.noto_nastaliq_urdu, FontWeight.Normal)
)

val ArabicClassicFontFamily = FontFamily(
    Font(R.font.amiri, FontWeight.Normal)
)

val RegalDisplayFontFamily = FontFamily(
    Font(R.font.cinzel, FontWeight.Normal)
)

val CleanSansFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = NastaliqUrduFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 56.sp
    ),
    displayMedium = TextStyle(
        fontFamily = RegalDisplayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 1.5.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = RegalDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.8.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = CleanSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),
    titleLarge = TextStyle(
        fontFamily = CleanSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = CleanSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = CleanSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 23.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = CleanSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.5.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = CleanSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.4.sp
    ),
    labelMedium = TextStyle(
        fontFamily = CleanSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.5.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.6.sp
    ),
    labelSmall = TextStyle(
        fontFamily = CleanSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.5.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.8.sp
    )
)

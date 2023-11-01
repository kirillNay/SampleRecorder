package nay.kirill.samplerecorder.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.W400,
        fontFamily = FontFamily.SansSerif,
        lineHeight = 42.sp,
    ),
    titleMedium = TextStyle(
        letterSpacing = 0.sp,
        fontSize = 24.sp,
        fontWeight = FontWeight.W700,
        fontFamily = FontFamily.SansSerif,
        lineHeight = 30.sp,
    ),
    titleSmall = TextStyle(
        letterSpacing = 0.sp,
        fontSize = 12.sp,
        fontWeight = FontWeight.W500,
        fontFamily = FontFamily.SansSerif,
        lineHeight = 16.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.W400,
        fontFamily = FontFamily.SansSerif,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.W300,
        fontFamily = FontFamily.SansSerif,
        lineHeight = 14.sp,
    ),
    headlineLarge = TextStyle(
        fontSize = 36.sp,
        fontWeight = FontWeight.W700,
        fontFamily = FontFamily.SansSerif,
        lineHeight = 30.sp,
    ),
    labelMedium = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.W400,
        fontFamily = FontFamily.SansSerif,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
)
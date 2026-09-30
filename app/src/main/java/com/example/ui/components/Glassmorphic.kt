package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.RatingYellow
import com.example.ui.theme.TagGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VipGoldEnd
import com.example.ui.theme.VipGoldStart

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = DarkSurface.copy(alpha = 0.85f),
    borderColor: Color = DarkBorder,
    borderWidth: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val clickableMod = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    Surface(
        modifier = clickableMod
            .clip(shape)
            .border(borderWidth, borderColor, shape),
        shape = shape,
        color = backgroundColor,
        tonalElevation = 2.dp
    ) {
        Box(content = content)
    }
}

@Composable
fun NeonTag(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = NeonCyan,
    backgroundColor: Color = color.copy(alpha = 0.15f),
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .then(clickModifier)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, color.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun MatchScoreBadge(score: Int, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(TagGreen.copy(alpha = 0.16f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(TagGreen, CircleShape)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "$score% Match",
            color = TagGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RatingBadge(rating: Double, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(RatingYellow.copy(alpha = 0.16f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = "★",
            color = RatingYellow,
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = rating.toString(),
            color = RatingYellow,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun QualityPill(
    text: String,
    modifier: Modifier = Modifier,
    isNeon: Boolean = false
) {
    val borderColor = if (isNeon) NeonCyan else Color.White.copy(alpha = 0.25f)
    val textColor = if (isNeon) NeonCyan else TextSecondary
    val bgColor = if (isNeon) NeonCyan.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.06f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun NeonPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    testTag: String = "neon_primary_button"
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = NeonCyan,
            contentColor = Color(0xFF001B20)
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        modifier = modifier
            .testTag(testTag)
            .drawBehind {
                drawCircle(
                    color = NeonCyanGlow,
                    radius = size.maxDimension * 0.7f,
                    center = center
                )
            }
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF001B20),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun VipGoldenBadge(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(VipGoldStart, VipGoldEnd)
                )
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = "VIP PLATINUM",
            color = Color(0xFF2B1D00),
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.8.sp
        )
    }
}

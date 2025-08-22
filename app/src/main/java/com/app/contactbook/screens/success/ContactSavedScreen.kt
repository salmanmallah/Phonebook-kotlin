package com.app.contactbook.screens.success

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun ContactSavedScreen(navController: NavHostController) {
    var showAnimation by remember { mutableStateOf(false) }
    
    // Animation for the check icon
    val scale by animateFloatAsState(
        targetValue = if (showAnimation) 1.2f else 0.8f,
        animationSpec = tween(500, easing = EaseOutBack),
        label = "scale"
    )
    
    // Auto redirect after 2 seconds
    LaunchedEffect(Unit) {
        delay(500) // Start animation after 500ms
        showAnimation = true
        delay(2000) // Wait 2 seconds
        navController.navigate("home") {
            popUpTo("home") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Animated Check Icon
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Success",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale),
                tint = MaterialTheme.colorScheme.primary
            )
            
            // Success Text
            Text(
                text = "Contact Saved!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            
            // Subtitle
            Text(
                text = "Your contact has been successfully added to your contact book.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            
            // Loading indicator
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )
        }
    }
}

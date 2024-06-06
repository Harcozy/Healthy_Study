package com.example.myapplication.ui

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.NetworkService
import com.example.myapplication.R
import com.example.myapplication.ui.HMSFontInfo.Companion.HMS
import com.example.myapplication.ui.HMSFontInfo.Companion.MS
import com.example.myapplication.ui.HMSFontInfo.Companion.S
import com.example.myapplication.ui.theme.purple200
import com.example.myapplication.ui.theme.teal200
import com.example.myapplication.ui.theme.typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

const val MY_PERMISSIONS_REQUEST_VIBRATE = 1

fun sendNotification(context: Context) {
    val channelId = "break_time_channel"
    val notificationId = 1

    // Create a notification channel for Android Oreo and above
    val name = "Session Ended Channel"
    val descriptionText = "Channel for Break Time notification"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelId, name, importance).apply {
        description = descriptionText
    }

    // Register the channel with the system
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)

    // Build the notification
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Session Ended!")
        .setContentText("It's time for a break!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    // Check if the VIBRATE permission is already granted
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
        // Permission is not granted
        if (context is Activity && ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.VIBRATE)) {
            // Show an explanation to the user. This is asynchronous and should not block.
            // After the user sees the explanation, try again to request the permission.
        } else {
            // No explanation needed, request the permission.
            if (context is Activity) {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.VIBRATE), MY_PERMISSIONS_REQUEST_VIBRATE)
            }
        }
    } else {
        // Permission has already been granted
        notificationManager.notify(notificationId, builder.build())
    }
}

@Composable
fun CountdownScreen(
    timeInSec: Int,
    context: Context,
    onCancel: () -> Unit
) {
    var remainingTime by remember { mutableStateOf(timeInSec) }
    var showDialog by remember { mutableStateOf(false) }
    var quote by remember { mutableStateOf("") }

    LaunchedEffect(timeInSec) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime--

            // Fetch a new quote every 10 seconds
            if (remainingTime % 5000 == 0) {
                val fetchedQuote = fetchQuote(context)
                quote = fetchedQuote
            }
        }
        showDialog = true
        sendNotification(context)
    }

    if (showDialog) {
        BreakTimeDialog(onDismiss = {
            showDialog = false
            onCancel()  // Navigate back to the input screen
        })
    }

    Column(
        Modifier
            .fillMaxHeight()
            .padding(start = 10.dp, end = 10.dp)
    ) {
        Box {
            AnimationElapsedTime(remainingTime)
            AnimationCircleCanvas(remainingTime, timeInSec)
        }

        Spacer(modifier = Modifier.size(55.dp))

        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(70.dp)
                .shadow(30.dp, shape = CircleShape)
                .clickable { onCancel() },
            imageVector = Icons.Default.Cancel,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
        )

        val customFont = FontFamily(Font(resId = R.font.merriweather))

        Text(
            text = quote,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 15.dp),
            style = typography.h6.copy(fontSize = 15.sp, fontFamily = customFont),
        )
    }
}

@Composable
fun BreakTimeDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Break Time!")
        },
        text = {
            Text("It's time for a break!")
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun BoxScope.AnimationElapsedTime(remainingTime: Int) {
    val (hou, min, sec) = remember(remainingTime) {
        val hou = remainingTime / 3600
        val min = (remainingTime % 3600) / 60
        val sec = remainingTime % 60
        Triple(hou, min, sec)
    }

    val onlySec = remember(hou, min) {
        hou == 0 && min == 0
    }

    val transition = rememberInfiniteTransition()

    val animatedFont by transition.animateFloat(
        initialValue = 1.5f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse)
    )

    val (size, labelSize, padding) = when {
        hou > 0 -> HMS
        min > 0 -> MS
        else -> S
    }

    Row(
        Modifier
            .align(Alignment.Center)
            .padding(start = padding, end = padding, top = 10.dp, bottom = 10.dp)
    ) {
        if (hou > 0) {
            DisplayTime(
                hou.formatTime(),
                "h",
                fontSize = size,
                labelSize = labelSize
            )
        }
        if (min > 0) {
            DisplayTime(
                min.formatTime(),
                "m",
                fontSize = size,
                labelSize = labelSize
            )
        }
        DisplayTime(
            if (onlySec) sec.toString() else sec.formatTime(),
            if (onlySec) "" else "s",
            fontSize = size * (if (onlySec && sec < 10) animatedFont else 1f),
            labelSize = labelSize,
            textAlign = if (onlySec) TextAlign.Center else TextAlign.End
        )
    }
}

@Composable
private fun BoxScope.AnimationCircleCanvas(remainingTime: Int, totalTime: Int) {
    val progress = remember(remainingTime) {
        1f - remainingTime / totalTime.toFloat()
    }

    val strokeRestart = Stroke(15f)
    val strokeReverse = Stroke(10f)
    val color = MaterialTheme.colors.primary
    val secondColor = MaterialTheme.colors.secondary

    val transition = rememberInfiniteTransition()
    val animatedRestart by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart)
    )
    val animatedReverse by transition.animateFloat(
        initialValue = 1.05f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse)
    )

    Canvas(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(16.dp)
            .size(350.dp)
    ) {
        val diameter = size.minDimension
        val radius = diameter / 2f
        val topLeftOffset = Offset(10f, 10f)
        val size = Size(radius * 2, radius * 2)

        drawArc(
            color = color,
            startAngle = animatedRestart,
            sweepAngle = 150f,
            topLeft = topLeftOffset,
            size = size,
            useCenter = false,
            style = strokeRestart,
        )

        drawCircle(
            color = secondColor,
            style = strokeReverse,
            radius = radius * animatedReverse
        )

        drawArc(
            startAngle = 270f,
            sweepAngle = progress * 360f,
            brush = Brush.radialGradient(
                radius = radius,
                colors = listOf(
                    purple200.copy(0.3f),
                    teal200.copy(0.2f),
                    Color.White.copy(0.3f)
                ),
            ),
            useCenter = true,
            style = Fill,
        )
    }
}

@Preview
@Composable
fun DisplayPreview() {
    val context = LocalContext.current
    CountdownScreen(1000, context) {}
}

private fun Int.formatTime() = String.format("%02d", this)

data class HMSFontInfo(val fontSize: TextUnit, val labelSize: TextUnit, val padding: Dp) {
    companion object {
        val HMS = HMSFontInfo(50.sp, 20.sp, 40.dp)
        val MS = HMSFontInfo(85.sp, 30.sp, 50.dp)
        val S = HMSFontInfo(150.sp, 50.sp, 55.dp)
    }
}

// Fetch a quote function
private suspend fun fetchQuote(context: Context): String {
    return try {
        val response = withContext(Dispatchers.IO) {
            NetworkService.quotesApi.getEducationQuotes()
        }
        if (response.isSuccessful && response.body() != null) {
            val quotes = response.body()!!
            val randomQuote = quotes.randomOrNull()
            randomQuote?.let {
                "\"${it.quote}\" - ${it.author ?: "Unknown"}"
            } ?: ""
        } else {
            ""
        }
    } catch (e: Exception) {
        ""
    }
}

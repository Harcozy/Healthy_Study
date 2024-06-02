package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class Clockie @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 5f
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val calendar = Calendar.getInstance()

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) - 20f

        // Draw the clock face
        paint.color = Color.parseColor("#FFF8E7") // Creamy color for the face
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw the speckles on the clock face
        paint.color = Color.parseColor("#CDCDCD") // Speckle color
        for (i in 1..200) { // Increase or decrease the number for more/less density
            val speckleX = Random.nextFloat() * (2 * radius) + (centerX - radius)
            val speckleY = Random.nextFloat() * (2 * radius) + (centerY - radius)
            canvas.drawCircle(speckleX, speckleY, 2f, paint)
        }

        // Draw the outer rim of the clock
        paint.color = Color.parseColor("#AEC6CF") // Blue-ish outer rim color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw hour marks (for simplicity, drawing lines for hour marks)
        paint.strokeWidth = 8f
        for (i in 0..11) {
            val angle = Math.toRadians((i * 30).toDouble())
            val startX = centerX + (radius - 30) * cos(angle).toFloat()
            val startY = centerY + (radius - 30) * sin(angle).toFloat()
            val stopX = centerX + (radius - 20) * cos(angle).toFloat()
            val stopY = centerY + (radius - 20) * sin(angle).toFloat()
            canvas.drawLine(startX, startY, stopX, stopY, paint)
        }

        // Draw hour, minute, and second hands
        drawHand(canvas, centerX, centerY, (calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE) / 60f) * 30, radius * 0.5f, 20f) // Hour
        drawHand(canvas, centerX, centerY, calendar.get(Calendar.MINUTE) * 6f, radius * 0.7f, 15f) // Minute
        paint.color = context.getColor(R.color.second_hand)
        drawHand(canvas, centerX, centerY, calendar.get(Calendar.SECOND) * 6f, radius * 0.9f, 10f) // Second

        // Draw the center circle of the clock hands
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, 12f, paint)

        // Schedule a view update
        postInvalidateOnAnimation()
    }

    private fun drawHand(canvas: Canvas, cx: Float, cy: Float, angle: Float, length: Float, strokeWidth: Float) {
        paint.strokeWidth = strokeWidth
        val handEnd = calculateHandEndPoint(cx, cy, angle, length)
        canvas.drawLine(cx, cy, handEnd.x, handEnd.y, paint)
    }

    private fun calculateHandEndPoint(cx: Float, cy: Float, angle: Float, length: Float): PointF {
        val angleRadians = Math.toRadians(angle.toDouble() - 90)
        val x = (cx + cos(angleRadians) * length).toFloat()
        val y = (cy + sin(angleRadians) * length).toFloat()
        return PointF(x, y)
    }
}

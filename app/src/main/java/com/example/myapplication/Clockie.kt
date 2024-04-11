package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class Clockie(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 5f
        strokeCap = Paint.Cap.ROUND
    }

    private val calendar = Calendar.getInstance()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) - 20f

        paint.strokeWidth = 1f
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(centerX, centerY, radius, paint)

        calendar.timeInMillis = System.currentTimeMillis()
        val hours = calendar.get(Calendar.HOUR_OF_DAY) % 12
        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)

        drawHand(canvas, centerX, centerY, (hours + minutes / 60f) * 30, radius * 0.5f, 15f)

        drawHand(canvas, centerX, centerY, minutes * 6f, radius * 0.7f, 10f)

        paint.color = context.getColor(R.color.second_hand)
        drawHand(canvas, centerX, centerY, minutes * 6f, radius * 0.9f, 5f)

        postInvalidateDelayed(1000)
    }

    private fun drawHand(canvas: Canvas, cx: Float, cy: Float, angle: Float, length: Float, strokeWidth:Float) {
        paint.strokeWidth = strokeWidth
        val handEnd = calculateHandEndPoint(cx, cy, angle, length)
        canvas.drawLine(cx, cy, handEnd.x, handEnd.y, paint)
    }

    private fun calculateHandEndPoint(cx: Float, cy: Float, angle: Float, length: Float): PointF {
        val angleRadians = Math.toRadians(angle.toDouble() - 90)
        val x = (cx + cos(angleRadians) * length).toFloat()
        val y = (cy + sin(angleRadians) * length).toFloat()
        return PointF(x,y)
    }
}
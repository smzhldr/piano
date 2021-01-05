package com.smzh.piano

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View

class WaveView : View {

    private val paint by lazy {
        Paint().apply {
            color = Color.GREEN
            strokeWidth = 4f
            isAntiAlias = true
            style = Paint.Style.STROKE
            textAlign = Paint.Align.CENTER
        }
    }

    private var data: ShortArray? = null


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        data?.let {
            val path: Path = Path()
            for (i in 0..it.size) {
                if (i * 5f >= width) {
                    break
                }
                if (i == 0) {
                    path.moveTo(0f, height / 2f - it[i].toFloat() / Short.MAX_VALUE * height / 2)
                } else {
                    path.lineTo(i * 5f, height / 2f - it[i].toFloat() / Short.MAX_VALUE * height / 2)
                }
            }
            canvas?.drawPath(path, paint)
        }
    }


    fun updateWave(data: ShortArray) {
        this.data = data
        invalidate()
    }
}
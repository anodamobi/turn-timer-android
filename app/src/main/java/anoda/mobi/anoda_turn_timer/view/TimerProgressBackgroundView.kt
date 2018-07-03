package anoda.mobi.anoda_turn_timer.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import anoda.mobi.anoda_turn_timer.R
import timber.log.Timber

class TimerProgressBackgroundView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    companion object {
        private val SWEEP_INC = 360f
        private val START_INC = 270f
    }

    private var paint: Paint = Paint()
    private lateinit var rectF: RectF
    private var angle: Float = 0f

    private var circleCenterX: Float = 0f
    private var circleCenterY: Float = 0f
    private var circleRadius: Float = 0f

    private var isRectFInitialized = false

    init {
        paint.isAntiAlias = true

        angle = SWEEP_INC
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        Timber.i("onMeasure spec $width, $height")
        if (isRectFInitialized.not()) {
            rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
            circleCenterX = width.toFloat() / 2
            circleCenterY = height.toFloat() / 2
            circleRadius = (circleCenterX + circleCenterY) / 2
            isRectFInitialized = true
        }
    }

    public fun update(newAngle: Float) {
        angle = newAngle
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.smallCircleColor)
        canvas.drawArc(rectF, START_INC, angle, true, paint)

        //draw outer circle
        paint.strokeWidth = 16f
        paint.style = Paint.Style.STROKE
        paint.color = ContextCompat.getColor(context, R.color.largeCircleColor)
        canvas.drawCircle(circleCenterX, circleCenterY, circleRadius, paint)
    }

}
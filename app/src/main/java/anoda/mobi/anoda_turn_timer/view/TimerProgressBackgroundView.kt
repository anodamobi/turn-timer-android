package anoda.mobi.anoda_turn_timer.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import anoda.mobi.anoda_turn_timer.R

class TimerProgressBackgroundView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val SWEEP_INC = 360f
        private const val START_INC = 270f
        private const val STROKE_WIDTH = 32f
        private const val LEFT_COORDINATE = 25f
        private const val RIGHT_COORDINATE = 25f
        private const val BOTTOM_SIDE_COEFFICIENT = 25f
        private const val RIGHT_SIDE_COEFFICIENT = 25f
        private const val RADIUS_COEFFICIENT = 25f
    }

    private lateinit var mCircleRectF: RectF

    private var mCircleBackgroundPaint: Paint = Paint()
    private var mCircleStrokePaint: Paint = Paint()
    private var mRectWidth: Int = 0
    private var mRectHeight: Int = 0
    private var mAngle: Float = 0f

    private var mCircleCenterX: Float = 0f
    private var mCircleCenterY: Float = 0f
    private var mCircleRadius: Float = 0f

    private var isRectFInitialized = false

    init {
        initCircleBackground()
        initCircleStroke()
        mAngle = SWEEP_INC
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mRectWidth = MeasureSpec.getSize(widthMeasureSpec)
        mRectHeight = MeasureSpec.getSize(heightMeasureSpec)
        createCircleProgressBarFrame()

    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mCircleBackgroundPaint)
        canvas.drawArc(mCircleRectF, START_INC, mAngle, false, mCircleStrokePaint)
    }

    fun update(newAngle: Float) {
        mAngle = newAngle
        invalidate()
    }

    private fun initCircleBackground() {
        mCircleBackgroundPaint.isAntiAlias = true
        mCircleBackgroundPaint.strokeWidth = STROKE_WIDTH
        mCircleBackgroundPaint.style = Paint.Style.STROKE
        mCircleBackgroundPaint.color = ContextCompat.getColor(context, R.color.circleBackgroundColor)
    }

    private fun initCircleStroke() {
        mCircleStrokePaint.isAntiAlias = true
        mCircleStrokePaint.style = Paint.Style.STROKE
        mCircleStrokePaint.strokeWidth = STROKE_WIDTH
        mCircleStrokePaint.strokeCap = Paint.Cap.ROUND
        mCircleStrokePaint.color = ContextCompat.getColor(context, R.color.circleStrokeColor)
    }

    private fun createCircleProgressBarFrame() {
        if (isRectFInitialized.not()) {
            mCircleRectF = RectF(LEFT_COORDINATE,
                    RIGHT_COORDINATE,
                    width.toFloat() - RIGHT_SIDE_COEFFICIENT,
                    height.toFloat() - BOTTOM_SIDE_COEFFICIENT)
            mCircleCenterX = width.toFloat() / 2
            mCircleCenterY = height.toFloat() / 2
            mCircleRadius = ((mCircleCenterX + mCircleCenterY) / 2) - RADIUS_COEFFICIENT
            isRectFInitialized = true
        }
    }
}
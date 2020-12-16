package com.example.android.myapplication

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.math.min
import kotlin.properties.Delegates

class LoadButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var valueAnimator = ValueAnimator()
    private lateinit var txtString: String
    private var width = 0f
    private var radius        = 0.0f
    private var btnColor      = 0
    private var txtColor      = 0
    private var xSpacing      = 200.0
    private var ySpacing      = 15.0
    private var paint         = Paint(Paint.ANTI_ALIAS_FLAG)

    private var switcher:testRun by Delegates.observable(testRun.complete){ _, _, newValue ->
        when(newValue){
            testRun.clicked -> showCircle = true
            testRun.download -> println("Downloading")
            testRun.complete -> println("Complete")
        }
    }

    private var showCircle:Boolean by Delegates.observable(false){ _, _, newValue ->
        if (newValue){
            valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
                valueAnimator.addUpdateListener {
                    width = it.animatedValue as Float
                    invalidate()
                }
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                duration = 5000
                start()
            }


            txtString   = "Downloading"
            //btnColor    = Color.BLUE
            invalidate()

        }
    }

    init {
        isClickable = true
        context.withStyledAttributes(
                attrs,
                R.styleable.LoadButton
        ){
            txtString = getString(R.styleable.LoadButton_txtShow).toString()
            txtColor  = getColor(R.styleable.LoadButton_txtColor, 0)
            btnColor  = getColor(R.styleable.LoadButton_btnBackground, 0)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = (min(w, h) / 2 * .6).toFloat()
    }

    override fun performClick(): Boolean {
        if (super.performClick())return true
        switcher = testRun.clicked
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawButton(canvas)
        drawText(canvas)
        if (showCircle){
            val progress = width*measuredWidth
            paint.color = Color.RED
            paint.style = Paint.Style.FILL
            canvas.drawColor(paint.color)
            canvas.drawRect(0f,0f,progress,measuredHeight.toFloat(), paint)
            drawText(canvas)
        drawCircle(canvas)
        }
    }

    private fun drawCircle(canvas: Canvas) {
        paint.apply {
            color = Color.YELLOW
            style = Paint.Style.FILL
        }
        canvas.drawCircle((measuredWidth.toFloat() / 2 + xSpacing).toFloat(), (measuredHeight.toFloat() / 2 - ySpacing).toFloat(), radius, paint)
    }

    private fun drawText(canvas: Canvas) {
        paint.apply {
            color     = txtColor
            textAlign = Paint.Align.CENTER
            textSize  = 40.0F
        }
        canvas.drawText(txtString, measuredWidth.toFloat() / 2, measuredHeight.toFloat() / 2, paint)
    }

    private fun drawButton(canvas: Canvas) {
        paint.color = btnColor
        paint.style = Paint.Style.FILL
        canvas.drawColor(paint.color)
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int    = resolveSizeAndState(minW, widthMeasureSpec, 1)
        val h: Int    = resolveSizeAndState(
                MeasureSpec.getSize(w),
                heightMeasureSpec,
                0
        )

        setMeasuredDimension(w, h)
    }
}
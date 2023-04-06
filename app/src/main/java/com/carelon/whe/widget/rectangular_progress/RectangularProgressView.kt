package com.carelon.whe.widget.rectangular_progress

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.carelon.whe.widget.rectangular_progress.CalculationUtil.convertDpToPx

class RectangularProgressView : View {
    var progress = 0.0
    private var progressBarPaint: Paint? = null
    private var outlinePaint: Paint? = null
    private var widthInDp = 10f
    private var strokewidth = 0f
    private var canvas: Canvas? = null
    var isOutline = true
        set(outline) {
            field = outline
            this.invalidate()
        }
    var isStartline = false
        set(startline) {
            field = startline
            this.invalidate()
        }
    var isShowProgress = false
        set(showProgress) {
            field = showProgress
            this.invalidate()
        }
    var isCenterline = false
        set(centerline) {
            field = centerline
            this.invalidate()
        }
    var isRoundedCorners = true
        private set
    private var roundedCornersRadius = 14f
    private var percentSettings = PercentStyle(
        Align.CENTER, 150f,
        true
    )
    var isClearOnHundred = false
        set(clearOnHundred) {
            field = clearOnHundred
            this.invalidate()
        }
    var isIndeterminate = false
        set(indeterminate) {
            field = indeterminate
            this.invalidate()
        }
    private val indeterminate_count = 1
    private val indeterminate_width = 20.0f

    constructor(context: Context) : super(context) {
        initializePaints(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initializePaints(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializePaints(context)
    }

    private fun initializePaints(context: Context) {
        progressBarPaint = Paint()
        progressBarPaint!!.color = context.resources.getColor(
            R.color.holo_green_dark
        )
        progressBarPaint!!.strokeWidth = convertDpToPx(
            widthInDp, getContext()
        ).toFloat()
        progressBarPaint!!.isAntiAlias = true
        progressBarPaint!!.style = Paint.Style.STROKE
        progressBarPaint!!.strokeCap = Paint.Cap.ROUND
        outlinePaint = Paint()
        outlinePaint!!.color = context.resources.getColor(
            R.color.darker_gray
        )
        outlinePaint!!.strokeWidth = convertDpToPx(
            widthInDp, getContext()
        ).toFloat()
        outlinePaint!!.isAntiAlias = true
        outlinePaint!!.style = Paint.Style.STROKE
        outlinePaint!!.strokeCap = Paint.Cap.ROUND
        setRoundedCorners(true, roundedCornersRadius)
    }

    override fun onDraw(canvas: Canvas) {
        this.canvas = canvas
        super.onDraw(canvas)
        strokewidth = convertDpToPx(widthInDp, context).toFloat()
        val cW = canvas.width
        val cH = canvas.height
        val scope = 2 * cW + 2 * cH - 4 * strokewidth
        val hSw = strokewidth / 2
        if (isOutline) {
            drawOutline()
        }
        if (isClearOnHundred && progress == 100.0 || progress <= 0.0) {
            return
        }
        val path = Path()
        val drawEnd = getDrawEnd(scope / 100 * java.lang.Float.valueOf(progress.toString()), canvas)
        if (drawEnd.place == Place.TOP) {
            if (drawEnd.location > cW / 2 && progress < 100.0) {
                path.moveTo((cW / 2).toFloat(), hSw)
                path.lineTo(drawEnd.location, hSw)
            } else {
                path.moveTo((cW / 2).toFloat(), hSw)
                path.lineTo(cW - hSw, hSw)
                path.lineTo(cW - hSw, cH - hSw)
                path.lineTo(hSw, cH - hSw)
                path.lineTo(hSw, hSw)
                path.lineTo(strokewidth, hSw)
                path.lineTo(drawEnd.location, hSw)
            }
            canvas.drawPath(path, progressBarPaint!!)
        }
        if (drawEnd.place == Place.RIGHT) {
            path.moveTo((cW / 2).toFloat(), hSw)
            path.lineTo(cW - hSw, hSw)
            path.lineTo(
                cW - hSw, 0
                        + drawEnd.location
            )
            canvas.drawPath(path, progressBarPaint!!)
        }
        if (drawEnd.place == Place.BOTTOM) {
            path.moveTo((cW / 2).toFloat(), hSw)
            path.lineTo(cW - hSw, hSw)
            path.lineTo(cW - hSw, cH - hSw)
            path.lineTo(cW - strokewidth, cH - hSw)
            path.lineTo(drawEnd.location, cH - hSw)
            canvas.drawPath(path, progressBarPaint!!)
        }
        if (drawEnd.place == Place.LEFT) {
            path.moveTo((cW / 2).toFloat(), hSw)
            path.lineTo(cW - hSw, hSw)
            path.lineTo(cW - hSw, cH - hSw)
            path.lineTo(hSw, cH - hSw)
            path.lineTo(hSw, cH - strokewidth)
            path.lineTo(hSw, drawEnd.location)
            canvas.drawPath(path, progressBarPaint!!)
        }
    }

    private fun drawOutline() {
        val margin = widthInDp + 2
        canvas!!.drawRect(
            margin,
            margin,
            canvas!!.width - margin,
            canvas!!.height - margin,
            outlinePaint!!
        )
    }

    fun setColor(color: Int) {
        progressBarPaint!!.color = color
        this.invalidate()
    }

    fun setWidthInDp(width: Int) {
        widthInDp = width.toFloat()
        progressBarPaint!!.strokeWidth = convertDpToPx(
            widthInDp, context
        ).toFloat()
        outlinePaint!!.strokeWidth = convertDpToPx(
            widthInDp, context
        ).toFloat()
        this.invalidate()
    }

    var percentStyle: PercentStyle
        get() = percentSettings
        set(percentSettings) {
            this.percentSettings = percentSettings
            this.invalidate()
        }

    fun getDrawEnd(percent: Float, canvas: Canvas): DrawStop {
        val drawStop = DrawStop()
        strokewidth = convertDpToPx(widthInDp, context).toFloat()
        val halfOfTheImage = (canvas.width / 2).toFloat()

        // top right
        if (percent > halfOfTheImage) {
            val second = percent - halfOfTheImage

            // right
            if (second > canvas.height - strokewidth) {
                val third = second - (canvas.height - strokewidth)

                // bottom
                if (third > canvas.width - strokewidth) {
                    val forth = third - (canvas.width - strokewidth)

                    // left
                    if (forth > canvas.height - strokewidth) {
                        val fifth = forth - (canvas.height - strokewidth)

                        // top left
                        if (fifth == halfOfTheImage) {
                            drawStop.place = Place.TOP
                            drawStop.location = halfOfTheImage
                        } else {
                            drawStop.place = Place.TOP
                            drawStop.location = strokewidth + fifth
                        }
                    } else {
                        drawStop.place = Place.LEFT
                        drawStop.location = canvas.height - strokewidth - forth
                    }
                } else {
                    drawStop.place = Place.BOTTOM
                    drawStop.location = canvas.width - strokewidth - third
                }
            } else {
                drawStop.place = Place.RIGHT
                drawStop.location = strokewidth + second
            }
        } else {
            drawStop.place = Place.TOP
            drawStop.location = halfOfTheImage + percent
        }
        return drawStop
    }

    fun setRoundedCorners(roundedCorners: Boolean, radius: Float) {
        isRoundedCorners = roundedCorners
        roundedCornersRadius = radius
        if (roundedCorners) {
            progressBarPaint!!.pathEffect = CornerPathEffect(roundedCornersRadius)
            outlinePaint!!.pathEffect = CornerPathEffect(roundedCornersRadius)
        } else {
            progressBarPaint!!.pathEffect = null
            outlinePaint!!.pathEffect = null
        }
        this.invalidate()
    }

    fun setOutlineColor(parseColor: Int) {
        outlinePaint!!.color = parseColor
        this.invalidate()
    }

    inner class DrawStop {
        var place: Place? = null
        var location = 0f
    }

    enum class Place {
        TOP, RIGHT, BOTTOM, LEFT
    }
}
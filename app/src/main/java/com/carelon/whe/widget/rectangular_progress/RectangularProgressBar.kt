package com.carelon.whe.widget.rectangular_progress

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.carelon.whe.R
import com.carelon.whe.widget.rectangular_progress.CalculationUtil.convertDpToPx

/**
 * The basic [RectangularProgressBar]. This class includes all the methods you
 * need to modify your [RectangularProgressBar].
 */
class RectangularProgressBar : RelativeLayout {
    /**
     * Returns the [TextView] that the progress gets drawn around.
     *
     * @return the main txtButton
     */
    var childView: TextView
        private set
    private val bar: RectangularProgressView

    /**
     * If opacity is enabled.
     *
     * @return true if opacity is enabled.
     */
    var isOpacity = false
        private set
    private val greyscale = false
    private var isFadingOnProgress = false
    /**
     * Returns a boolean if rounded corners is active or not.
     *
     * @return true if rounded corners is active.
     * @since 1.6.2
     */
    /**
     * Activates the drawing of rounded corners.
     *
     */
    var roundedCorners: Boolean
        get() = bar.isRoundedCorners
        set(useRoundedCorners) {
            bar.setRoundedCorners(useRoundedCorners, 10f)
        }

    /**
     * New SquareProgressBar.
     *
     * @param context
     * the [Context]
     * @param attrs
     * an [AttributeSet]
     * @param defStyle
     * a defined style.
     */
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val mInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mInflater.inflate(R.layout.rectangular_progressbar, this, true)
        bar = findViewById<View>(R.id.progress_bar) as RectangularProgressView
        childView = findViewById<View>(R.id.txtButton) as TextView
        bar.bringToFront()
    }

    /**
     * New SquareProgressBar.
     *
     * @param context
     * the [Context]
     * @param attrs
     * an [AttributeSet]
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val mInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mInflater.inflate(R.layout.rectangular_progressbar, this, true)
        bar = findViewById<View>(R.id.progress_bar) as RectangularProgressView
        childView = findViewById<View>(R.id.txtButton) as TextView
        bar.bringToFront()
    }

    /**
     * New SquareProgressBar.
     *
     * @param context the context
     */
    constructor(context: Context) : super(context) {
        val mInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mInflater.inflate(R.layout.rectangular_progressbar, this, true)
        bar = findViewById<View>(R.id.progress_bar) as RectangularProgressView
        childView = findViewById<View>(R.id.txtButton) as TextView
        bar.bringToFront()
    }

    /**
     * Sets the image of the [RectangularProgressBar]. Must be a valid
     * ressourceId.
     *
     * @param image
     * the image as a ressourceId
     */
    fun setImage(image: Int) {
        childView.setBackgroundResource(image)
    }

    /**
     * Sets the image of the [RectangularProgressBar]. Must be a valid
     * Drawable.
     *
     * @param imageDrawable the image as a Drawable
     */
    fun setImageDrawable(imageDrawable: Drawable?) {
        childView.setBackgroundDrawable(imageDrawable)
    }

    /**
     * Sets the colour of the [RectangularProgressBar] to a predefined android
     * color.
     * @param androidHoloColor color value
     */
    fun setColor(androidHoloColor: Int) {
        bar.setColor(context.resources.getColor(androidHoloColor))
    }

    fun setOutlineColor(androidHoloColor: Int) {
        bar.setOutlineColor(context.resources.getColor(androidHoloColor))
    }

    /**
     * Sets the colour of the [RectangularProgressBar]. YOu can give it a
     * hex-color string like *#C9C9C9*.
     *
     * @param colorString
     * the colour of the [RectangularProgressBar]
     */
    fun setColor(colorString: String?) {
        bar.setColor(Color.parseColor(colorString))
    }

    /**
     * Sets the colour of the outline of [RectangularProgressBar]. YOu can give it a
     * hex-color string like *#C9C9C9*.
     *
     * @param colorString
     * the colour of the outline of [RectangularProgressBar]
     */
    fun setOutlineColor(colorString: String?) {
        bar.setOutlineColor(Color.parseColor(colorString))
    }

    /**
     * This sets the width of the [RectangularProgressBar].
     *
     * @param width
     * in Dp
     */
    fun setWidth(width: Int) {
        val margin = convertDpToPx(width.toFloat(), context)
        val pm = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        pm.setMargins(margin, margin, margin, margin)
        childView.layoutParams = pm
        bar.setWidthInDp(width)
    }

    /**
     * This sets the alpha of the image in the view. Actually I need to use the
     * deprecated method here as the new one is only available for the API-level
     * 16. And the min API level of this library is 14.
     *
     * Use this only as private method.
     *
     * @param progress
     * the progress
     */
    private fun setOpacity(progress: Int) {
        childView.alpha = (2.55 * progress).toInt().toFloat()
    }

    /**
     * Switches the opacity state of the image. This forces the
     * SquareProgressBar to redraw with the current progress. As bigger the
     * progress is, then more of the image comes to view. If the progress is 0,
     * then you can't see the image at all. If the progress is 100, the image is
     * shown full.
     *
     * @param opacity
     * true if opacity should be enabled.
     */
    fun setOpacity(opacity: Boolean) {
        isOpacity = opacity
        progress = bar.progress
    }

    /**
     * Switches the opacity state of the image. This forces the
     * SquareProgressBar to redraw with the current progress. As bigger the
     * progress is, then more of the image comes to view. If the progress is 0,
     * then you can't see the image at all. If the progress is 100, the image is
     * shown full.
     *
     * You can also set the flag if the fading should get inverted so the image
     * disappears when the progress increases.
     *
     * @param opacity
     * true if opacity should be enabled.
     * @param isFadingOnProgress
     * default false. This changes the behavior the opacity works. If
     * the progress increases then the images fades. When the
     * progress reaches 100, then the image disappears.
     */
    fun setOpacity(opacity: Boolean, isFadingOnProgress: Boolean) {
        isOpacity = opacity
        this.isFadingOnProgress = isFadingOnProgress
        progress = bar.progress
    }

    /**
     * Draws an outline of the progressbar. Looks quite cool in some situations.
     *
     * @param drawOutline
     * true if it should or not.
     */
    fun drawOutline(drawOutline: Boolean) {
        bar.isOutline = drawOutline
    }

    /**
     * If outline is enabled or not.
     *
     * @return true if outline is enabled.
     */
    val isOutline: Boolean
        get() = bar.isOutline

    /**
     * Defines if the percent text should be shown or not. To modify the text
     *
     * @param showProgress
     * true if it should or not.
     */
    fun showProgress(showProgress: Boolean) {
        bar.isShowProgress = showProgress
    }

    /**
     * If the progress text inside of the image is enabled.
     *
     * @return true if it is or not.
     */
    val isShowProgress: Boolean
        get() = bar.isShowProgress
    /**
     * Returns the [PercentStyle] of the percent text. Maybe returns the
     * default value, check [.setPercentStyle] fo that.
     *
     * @return the percent style of the moment.
     */
    /**
     * Sets a custom percent style to the text inside the image. Make sure you
     * set [.showProgress] to true. Otherwise it doesn't shows.
     * The default settings are:
     * Text align: CENTER
     * Text size: 150 [dp]
     * Display percentsign: true
     * Custom text: %
     *
     * @param percentStyle the percent style
     */
    var percentStyle: PercentStyle?
        get() = bar.percentStyle
        set(percentStyle) {
            if (percentStyle != null) {
                bar.percentStyle = percentStyle
            }
        }
    /**
     * If the progressbar disappears when the progress reaches 100%.
     *
     * @return if "clearOnHundred" is enabled or not
     */
    /**
     * If the progress hits 100% then the progressbar disappears if this flag is
     * set to `true`. The default is set to false.
     *
     * @param clearOnHundred
     * if it should disappear or not.
     */
    var isClearOnComplete: Boolean
        get() = bar.isClearOnHundred
        set(clearOnHundred) {
            bar.isClearOnHundred = clearOnHundred
        }
    /**
     * Returns the status of the indeterminate mode. The default status is false.
     *
     * @return if "indeterminate" is enabled or not
     */
    /**
     * Set the status of the indeterminate mode. The default is false. You can
     * still configure colour, width and so on.
     *
     * @param indeterminate true to enable the indeterminate mode (default true)
     */
    var isIndeterminate: Boolean
        get() = bar.isIndeterminate
        set(indeterminate) {
            bar.isIndeterminate = indeterminate
        }
    /**
     * Returns the progress of the progressbar as a double.
     *
     * @return the current progress as double.
     */
    /**
     * Sets the progress of the [RectangularProgressBar]. If opacity is
     * selected then here it sets it. See [.setOpacity] for more
     * information.
     *
     * @param progress the progress
     */
    var progress: Double
        get() = bar.progress
        set(progress) {
            bar.progress = progress
            if (isOpacity) {
                if (isFadingOnProgress) {
                    setOpacity(100 - progress.toInt())
                } else {
                    setOpacity(progress.toInt())
                }
            } else {
                setOpacity(100)
            }
        }

    /**
     * Sets the progress as an integer value. This is mainly used for animations.
     *
     * @param progress as an integer value.
     */
    fun setProgress(progress: Int) {
        this.progress = progress.toDouble()
    }

    /**
     * Activates the drawing of rounded corners with a given radius.
     */
    fun setRoundedCorners(useRoundedCorners: Boolean, radius: Float) {
        bar.setRoundedCorners(useRoundedCorners, radius)
        val shape = GradientDrawable()
        shape.cornerRadius = radius
        shape.setColor(Color.WHITE)
        childView.background = shape
    }
}
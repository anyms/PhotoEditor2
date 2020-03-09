package app.spidy.photoeditor2.core

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.widget.TextView
import app.spidy.photoeditor2.OutlineSpan
import java.util.*


/**
 *
 *
 * This class is used to wrap the styles to apply on the TextView on [PhotoEditor.addText] and [PhotoEditor.editText]
 *
 *
 * @author [Christian Caballero](https://github.com/Sulfkain)
 * @since 14/05/2019
 */
class TextStyleBuilder {
    private val values: MutableMap<TextStyle, Any> = HashMap()

    fun getValues(): Map<TextStyle, Any> {
        return values
    }

    /**
     * Set this textSize style
     *
     * @param size Size to apply on text
     */
    fun withTextSize(size: Float): TextStyleBuilder {
        values[TextStyle.SIZE] = size
        return this
    }

    /**
     * Set this color style
     *
     * @param color Color to apply on text
     */
    fun withTextColor(color: Int): TextStyleBuilder {
        values[TextStyle.COLOR] = color
        return this
    }

    fun withTextOutline(color: Int, strokeSize: Int): TextStyleBuilder {
        val vals: ArrayList<Any> = ArrayList(2)
        vals.add(color)
        vals.add(strokeSize)
        values[TextStyle.OUTLINE_COLOR] = vals
        return this
    }

    fun withOpacity(opacity: Float): TextStyleBuilder {
        values[TextStyle.OPACITY] = opacity
        return this
    }

    /**
     * Set this [Typeface] style
     *
     * @param textTypeface TypeFace to apply on text
     */
    fun withTextFont(textTypeface: Typeface): TextStyleBuilder {
        values[TextStyle.FONT_FAMILY] = textTypeface
        return this
    }

    /**
     * Set this gravity style
     *
     * @param gravity Gravity style to apply on text
     */
    fun withGravity(gravity: Int): TextStyleBuilder {
        values[TextStyle.GRAVITY] = gravity
        return this
    }

    /**
     * Set this background color
     *
     * @param background Background color to apply on text, this method overrides the preview set on [TextStyleBuilder.withBackgroundDrawable]
     */
    fun withBackgroundColor(background: Int): TextStyleBuilder {
        values[TextStyle.BACKGROUND] = background
        return this
    }

    /**
     * Set this background [Drawable], this method overrides the preview set on [TextStyleBuilder.withBackgroundColor]
     *
     * @param bgDrawable Background drawable to apply on text
     */
    fun withBackgroundDrawable(bgDrawable: Drawable): TextStyleBuilder {
        values[TextStyle.BACKGROUND] = bgDrawable
        return this
    }

    /**
     * Set this textAppearance style
     *
     * @param textAppearance Text style to apply on text
     */
    fun withTextAppearance(textAppearance: Int): TextStyleBuilder {
        values[TextStyle.TEXT_APPEARANCE] = textAppearance
        return this
    }

    /**
     * Method to apply all the style setup on this Builder}
     *
     * @param textView TextView to apply the style
     */
    fun applyStyle(textView: TextView) {
        for ((key, value) in values) {
            when (key) {
                TextStyle.SIZE -> {
                    val size = value as Float
                    applyTextSize(textView, size)
                }
                TextStyle.COLOR -> {
                    val color = value as Int
                    applyTextColor(textView, color)
                }
                TextStyle.OUTLINE_COLOR -> {
                    val vals = value as List<*>
                    applyOutlineColor(textView, vals[0] as Int, vals[1] as Int)
                }
                TextStyle.OPACITY -> {
                    val opacity = value as Float
                    applyOpacity(textView, opacity)
                }
                TextStyle.FONT_FAMILY -> {
                    val typeface = value as Typeface
                    applyFontFamily(textView, typeface)
                }
                TextStyle.GRAVITY -> {
                    val gravity = value as Int
                    applyGravity(textView, gravity)
                }
                TextStyle.BACKGROUND -> {
                    if (value is Drawable) {
                        applyBackgroundDrawable(textView, value)
                    } else if (value is Int) {
                        applyBackgroundColor(textView, value)
                    }
                }
                TextStyle.TEXT_APPEARANCE -> {
                    if (value is Int) {
                        applyTextAppearance(textView, value)
                    }
                }
            }
        }
    }

    fun applyTextSize(textView: TextView, size: Float) {
        textView.textSize = size
    }

    fun applyTextColor(textView: TextView, color: Int) {
        textView.setTextColor(color)
    }

    fun applyOutlineColor(textView: TextView, color: Int, strokeSize: Int) {
        val outlineSpan = OutlineSpan(color, strokeSize.toFloat())
        val s = textView.text.toString()
        val spannable = SpannableString(s)
        spannable.setSpan(outlineSpan, 0, s.length, Spannable.SPAN_COMPOSING)
        textView.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    fun applyOpacity(textView: TextView, opacity: Float) {
        textView.alpha = opacity
    }

    fun applyFontFamily(textView: TextView, typeface: Typeface?) {
        textView.typeface = typeface
    }

    fun applyGravity(textView: TextView, gravity: Int) {
        textView.gravity = gravity
    }

    fun applyBackgroundColor(textView: TextView, color: Int) {
        textView.setBackgroundColor(color)
    }

    fun applyBackgroundDrawable(textView: TextView, bg: Drawable?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.background = bg
        } else {
            textView.setBackgroundDrawable(bg)
        }
    }

    fun applyTextAppearance(textView: TextView, styleAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(styleAppearance)
        } else {
            textView.setTextAppearance(textView.context, styleAppearance)
        }
    }

    /**
     * Enum to maintain current supported style properties used on on [PhotoEditor.addText] and [PhotoEditor.editText]
     */
    enum class TextStyle(val property: String) {
        SIZE("TextSize"), COLOR("TextColor"),
        OUTLINE_COLOR("OutlineColor"), OPACITY("Opacity"), GRAVITY("Gravity"),
        FONT_FAMILY("FontFamily"),
        BACKGROUND("Background"), TEXT_APPEARANCE("TextAppearance");

    }
}
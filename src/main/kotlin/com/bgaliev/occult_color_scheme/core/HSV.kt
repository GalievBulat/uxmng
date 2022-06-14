package com.bgaliev.occult_color_scheme.core

import org.apache.xmlgraphics.java2d.color.NamedColorSpace
import java.awt.Color
import java.awt.color.ColorSpace


class HSBColorSpace : ColorSpace(ColorSpace.TYPE_HSV, 3) {
    override fun toRGB(c: FloatArray): FloatArray {
        val rgb: Int = Color.HSBtoRGB(c[0], c[1], c[2])
        return floatArrayOf( //
            (rgb and 0xff0000 shr 16) / 255f,  //
            (rgb and 0xff00 shr 8) / 255f,  //
            (rgb and 0xff) / 255f //
        )
    }

    override fun fromRGB(rgbvalue: FloatArray): FloatArray {
        return Color.RGBtoHSB(
            (rgbvalue[0] * 255).toInt(),
            (rgbvalue[1] * 255).toInt(),
            (rgbvalue[2] * 255).toInt(),
            FloatArray(3)
        )
    }

    override fun toCIEXYZ(colorvalue: FloatArray): FloatArray {
        val rgb = toRGB(colorvalue)
        return ColorSpace.getInstance(CS_sRGB).toCIEXYZ(rgb)
    }

    override fun fromCIEXYZ(colorvalue: FloatArray): FloatArray {
        val sRGB: FloatArray = ColorSpace.getInstance(ColorSpace.CS_sRGB).fromCIEXYZ(colorvalue)
        return fromRGB(sRGB)
    }

    override fun getName(idx: Int): String {
        return when (idx) {
            0 -> "Hue"
            1 -> "Saturation"
            2 -> "Brightness"
            else -> throw IllegalArgumentException("index must be between 0 and 2:$idx")
        }
    }

    override fun getMaxValue(component: Int): Float {
        return 1f
    }

    override fun getMinValue(component: Int): Float {
        return 0f
    }

    override fun equals(o: Any?): Boolean {
        return o is HSBColorSpace
    }

    override fun hashCode(): Int {
        return javaClass.simpleName.hashCode()
    }

    val name: String
        get() = "HSB"

    companion object {
        var instance: HSBColorSpace? = null
            get() {
                if (field == null) {
                    field = HSBColorSpace()
                }
                return field
            }
            private set
    }
}
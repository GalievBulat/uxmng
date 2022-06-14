package com.bgaliev.occult_color_scheme.core

import java.awt.color.ColorSpace

class CIELab internal constructor() : ColorSpace(TYPE_Lab, 3) {

        fun fromRGB(rgb: ImageProcessing.RGBA): ColorLab =
            fromCIEXYZ(CIEXYZ.fromRGB(arrayOf(rgb.r, rgb.g, rgb.b, rgb.a).toFloatArray())).let {
                ColorLab(l = it[0].toDouble(), a = it[1].toDouble(),b=  it[2].toDouble())
            }
        fun fromRGB(rgb: ImageProcessing.RGB): ColorLab =
            fromCIEXYZ(CIEXYZ.fromRGB(arrayOf(rgb.r, rgb.g, rgb.b).toFloatArray())).let {
                ColorLab(l = it[0].toDouble(), a = it[1].toDouble(),b=  it[2].toDouble())
            }
        private fun fInv(x: Double): Double {
            return if (x > 6.0 / 29.0) {
                x * x * x
            } else {
                108.0 / 841.0 * (x - N)
            }
        }
        override fun toCIEXYZ(colorvalue: FloatArray): FloatArray {
            val i = (colorvalue[0] + 16.0) * (1.0 / 116.0)
            val X = fInv(i + colorvalue[1] * (1.0 / 500.0))
            val Y = fInv(i)
            val Z = fInv(i - colorvalue[2] * (1.0 / 200.0))
            return floatArrayOf(X.toFloat(), Y.toFloat(), Z.toFloat())
        }
        override fun toRGB(colorvalue: FloatArray): FloatArray {
            val xyz = toCIEXYZ(colorvalue)
            return CIEXYZ.toRGB(xyz)
        }
        override fun fromRGB(rgbvalue: FloatArray?): FloatArray =
            fromCIEXYZ(CIEXYZ.fromRGB(rgbvalue))
        override fun fromCIEXYZ(colorvalue: FloatArray): FloatArray {
            val l = f(colorvalue[1].toDouble())
            val L = 116.0 * l - 16.0
            val a = 500.0 * (f(colorvalue[0].toDouble()) - l)
            val b = 200.0 * (l - f(colorvalue[2].toDouble()))
            return floatArrayOf(L.toFloat(), a.toFloat(), b.toFloat())
        }

        private fun f(x: Double): Double {
            return if (x > 216.0 / 24389.0) {
                Math.cbrt(x)
            } else {
                841.0 / 108.0 * x + N
            }
        }
        private val N = 4.0 / 29.0
        private val CIEXYZ = getInstance(ColorSpace.CS_CIEXYZ)



}
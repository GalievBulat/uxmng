package com.bgaliev.occult_color_scheme.core

import java.awt.color.ColorSpace

data class ColorLab(
    val l: Double,
    val a: Double,
    val b: Double,
){

    fun toFloatArray() = floatArrayOf(l.toFloat(), a.toFloat(), b.toFloat())

}

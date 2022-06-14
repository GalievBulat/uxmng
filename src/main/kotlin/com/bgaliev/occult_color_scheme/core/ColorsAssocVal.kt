package com.bgaliev.occult_color_scheme.core

enum class ColorsAssocVal(val rgd: ImageProcessing.RGBA, val sinc: Double, val exc: Double, val comp: Double, val soph: Double, val rugg: Double) {
    GREEN(ImageProcessing.RGBA(0f/255,0f/255,255.0f/255, 0.0f), 0.32, 0.08, 0.075, -0.205, 0.278),
    BLUE(ImageProcessing.RGBA(118f/255,255f/255,122f/255, 0.0f), 0.394, -0.008, 0.3, 0.06205, 0.09278),
    PURPLE(ImageProcessing.RGBA(128f/255, 0f/255, 128f/255, 0.0f), 0.362, 0.04, -0.025, 0.275, -0.414),

}
package com.bgaliev.occult_color_scheme.core

import com.intellij.util.ui.UIUtil
import java.awt.Color
import java.awt.Graphics2D
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.lang.Math.pow
import javax.imageio.ImageIO
import kotlin.math.*
import kotlin.random.Random

const val MINCOLORDIF = 12
class ImageProcessing {
    fun loadImage(path: String): BufferedImage?{
        try {
            val img = File(path)
            return ImageIO.read(img)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    fun resize( originalImage: BufferedImage, targetSize: Pair<Int, Int>): BufferedImage {
        val resizedImage = UIUtil.createImage(targetSize.first, targetSize.second, BufferedImage.TYPE_INT_ARGB)
        val graphics2D: Graphics2D = resizedImage.createGraphics()
        graphics2D.drawImage(originalImage, 0, 0, targetSize.first, targetSize.second, null)
        graphics2D.dispose()
        return resizedImage
    }
    data class RGB(val r: Float, val g: Float, val b: Float){
        fun toFloatArray() = floatArrayOf(r,g,b)
    }

    data class RGBA(val r: Float, val g: Float, val b: Float, val a: Float){
        fun toFloatArray() = floatArrayOf(r,g,b,a)
        constructor(floatArray: FloatArray) : this(floatArray[0], floatArray[1],floatArray[2],
            (if (floatArray.size>3) floatArray[3] else 1.0F)
        )
        val hex: String
        get() {
            val color = Color(r, g, b)
            var hex = Integer.toHexString(color.rgb and 0xffffff)
            if (hex.length < 6) {
                hex = "0$hex"
            }
            return "#$hex"
        }

    }
    enum class HUE_CONTROL{
        COMPLEMENTARY, MONOCHROMATIC, NO_CONTROL
    }
    data class ColorFrequency(val color: RGBA, var freq: Int)

    private val hsv: ColorSpace = HSBColorSpace()
    private val lab: CIELab = CIELab()

    fun harmonizeColors(colors: List<RGBA>,
                        colorNormalizer: ColorNormalizer,
                        areas: List<Double>,
                        hueControl: HUE_CONTROL = HUE_CONTROL.COMPLEMENTARY
    ): MutableList<RGBA>  {

        val minHueAngle = when(hueControl){
            HUE_CONTROL.COMPLEMENTARY -> min((360/colors.size)/5.5,60.0).let{
                if (it< 12) 12 else it
            }.toDouble()
            HUE_CONTROL.MONOCHROMATIC -> 10.0
            HUE_CONTROL.NO_CONTROL -> 0.0
        }/360.0
        val brtAndSat = mutableMapOf<RGBA, Pair<Double,Double>>()
        for (color in colors) {
            val brt = lab.fromRGB(color).l/100
            val sat = (hsv.fromRGB(color.toFloatArray()))[1]
            brtAndSat[color] = Pair(brt, sat.toDouble())
        }
        //val sumBrtAndSat = brtAndSat.values.sumOf{ p ->
        //    p.first + p.second
        //}
        val maxBrtAndSat = 1.0
        val normBrtAndSat = brtAndSat.mapValues {
            (it.value.first  * it.value.second)/maxBrtAndSat
        }
        //val potentialBaseColors = normBrtAndSat.filter {
        //    it.value <= 1-baseArea
        //}
        //TODO if empty
        val transformedColors = mutableListOf<RGBA>()
        var i = 0
        var droped = 0
        while(i < areas.size) {
            val baseColor = colors.random()
            val baseColorValues = brtAndSat[baseColor]!!
            val hBaseColor = transformColor(baseColor, areas[i], baseColorValues.first, baseColorValues.second,
                normBrtAndSat[baseColor]!!,Strategy.MIXED, 0.05)
            var minColorDiff = Double.MAX_VALUE
            val baseHsv = hsv.fromRGB(baseColor.toFloatArray())
            var minHueAngleDiff = Double.MAX_VALUE
            for (other in transformedColors) {

                val otherHsv = hsv.fromRGB(other.toFloatArray())
                val colorDiff = colorNormalizer.compare(hBaseColor,other)
                if (colorDiff< minColorDiff){
                    minColorDiff = colorDiff
                }
                if (abs(otherHsv[0] - baseHsv[0])< minHueAngleDiff ){
                    minHueAngleDiff = abs(otherHsv[0] -  baseHsv[0]).toDouble()
                }
            }
            if (minColorDiff<MINCOLORDIF || minHueAngleDiff < minHueAngle) {
                val message = if (minColorDiff<MINCOLORDIF) "min color dif" else "hue angle";
                println("droped color $message")
                if(droped> 1400){
                    throw IllegalArgumentException("wrong image")
                }
                droped++
                continue
            }else {
                i++
                transformedColors.add(hBaseColor)
            }
        }
        return transformedColors

    }
    enum class Strategy{
        SAT, BRT, MIXED
    }
    private fun transformColor(color: RGBA, area: Double, brt: Double, sat: Double, norm: Double,
                               strategy: Strategy, threshold: Double, ): RGBA{
        val diff = (1 - area - norm)
        if (abs(diff) < threshold)
            return color
        return when(strategy){
            Strategy.SAT ->{
                val changedSat = (1 - area)/brt
                val hsvColor = hsv.fromRGB(color.toFloatArray())
                RGBA(hsv.toRGB(hsvColor.also { it[1] = changedSat.toFloat() }))
            }
            Strategy.BRT ->{
                val changedBrt = (1 - area)/sat
                val labColor = lab.fromRGB(color)
                RGBA(lab.toRGB(labColor.toFloatArray().also { it[0] = changedBrt.toFloat()*100 }))
            }
            Strategy.MIXED -> {
                val changedVal = (1 - area)
                val labColor = lab.fromRGB(color)
                val prop = Random.nextDouble(0.3, 0.7)
                val newLab = sqrt(changedVal)*prop
                val newHsv = sqrt(changedVal)*(1-prop)
                val labAppl = RGBA(lab.toRGB(labColor.toFloatArray().also { it[0] = newLab.toFloat()*100 }))
                val hsvColor = hsv.fromRGB(labAppl.toFloatArray())
                RGBA(hsv.toRGB(hsvColor.also { it[1] = newHsv.toFloat() }))
            }
            /*Strategy.MIXED -> {
                val changedVal = (1 - area).pow(1.3)
                val labColor = lab.fromRGB(color)
                val prop = Random.nextDouble(0.95, 1.0)
                val newLab = sqrt(changedVal)*(prop.pow(-1))
                val newHsv = sqrt(changedVal)*prop
                val labAppl = RGBA(lab.toRGB(labColor.toFloatArray().also {
                    val newArr = it
                    newArr[0] = newLab.toFloat()*100
                    //newArr[1] = (if(Random.nextBoolean()) 1F else -1F) * min(aBSum*prop2, 128.0).toFloat()
                    //newArr[2] = (if(Random.nextBoolean()) 1F else -1F) * min(aBSum*(1-prop2), 128.0).toFloat()
                }))
                val hsvColor = hsv.fromRGB(labAppl.toFloatArray())
                return RGBA(hsv.toRGB(hsvColor.also { it[1] = newHsv.toFloat() }))
            }*/
        }
    }

    fun dominantColorFrequencies(
        originalImage: BufferedImage,
        targetSize: Pair<Int, Int>,
        colorsCount: Int,
        maxNumberOfColors: Int = 400,
        colorDifferenceScoreThreshold: Double = 10.0,
        colorNormalizer: ColorNormalizer,
    ): List<ColorFrequency> {

        // ------
        // Step 1: Resize the image based on the requested quality
        // ------
        //TODO enum?
        val resizedImage = resize(originalImage, targetSize)
        val area = resizedImage.height * resizedImage.width
        val cfData = resizedImage.data


        // ------
        // Step 2: Add each pixel to a NSCountedSet. This will give us a count for each color.
        // ------

        val colorsCountedSet = HashMap<RGB, Int>()

        for (y in 0 until resizedImage.height) {
            for (x in 0 until resizedImage.width) {
                val data = cfData.getPixel(x, y, FloatArray(4))
                // Let's make sure there is enough alpha.
                if (data[3] <= 150) {
                    continue
                }

                val pixelColor = RGB(r = data[ 0], g = data[ 1], b = data[ 2])
                colorsCountedSet[pixelColor] = colorsCountedSet[pixelColor]?.let {
                     it+1
                }?: 1

            }
        }

        // ------
        // Step 3: Remove colors that are barely present on the image.
        // ------
        //TODO
        val minCountThreshold = area * (0.01 / 100.0)

        // ------
        // Step 4: Sort the remaning colors by frequency.
        // ------

        val filteredColorsCountMap =
            colorsCountedSet/*.filter {
                it.value > minCountThreshold
            }*/.map {
                val rgb = it.key
                ColorFrequency(
                    color = RGBA(
                        r = (rgb.r / 255.0).toFloat(),
                        g = (rgb.g / 255.0).toFloat(),
                        b = (rgb.b / 255.0).toFloat(),
                        a = 1.0f
                    ), freq = it.value
                )
            }.sortedByDescending {
                it.freq
            }
        // ------
        // Step 5: Only keep the most frequent colors.
        // ------

        var colorFrequencies = filteredColorsCountMap.take(maxNumberOfColors)

        // ------
        // Step 6: Combine similar colors together.
        // ------

        /// The main dominant colors on the picture.

        /// The score that needs to be met to consider two colors similar.


        // Combines colors that are similar.

        val omittedColors = arrayListOf<ColorFrequency>()
        val matches = arrayListOf<ColorFrequency>()
        for (colorFrequency in colorFrequencies) {
            if (colorFrequency !in omittedColors)
                for (frequency in filteredColorsCountMap) {
                    val differenceScore = colorNormalizer.compare(colorFrequency.color, frequency.color)
                    if (differenceScore < colorDifferenceScoreThreshold) {
                        matches.add(colorFrequency.apply { ++freq })
                        omittedColors.add(frequency)
                    }
                }
        }

        colorFrequencies = (colorFrequencies.filter {
            it !in omittedColors &&
                    it !in matches
        } + matches.distinct()).sortedByDescending { it.freq }

        // ------
        // Step 7: Again, limit the number of colors we keep, this time drastically.
        // ------

        // We only keep the first few dominant colors.

        // ------
        // Step 8: Sort again on frequencies because the order may have changed because we combined colors.
        // ------


        // ------
        // Step 9: Calculate the frequency of colors as a percentage.
        // ------

        /// The total count of colors
        //TODO

        return colorFrequencies.take(colorsCount)

    }
}
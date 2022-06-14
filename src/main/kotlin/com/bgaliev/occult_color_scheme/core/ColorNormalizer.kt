package com.bgaliev.occult_color_scheme.core

import java.awt.color.ColorSpace
import java.lang.Math.pow
import kotlin.math.*

class ColorNormalizer {
    private val lab: CIELab =CIELab()

    private fun deg2Rad(deg: Double): Double
    {
        return (deg * (PI / 180.0))
    }

    private fun rad2Deg(rad: Double): Double
    {
        return ((180.0 / PI) * rad)
    }

    fun compare(rgb1: ImageProcessing.RGB, rgb2: ImageProcessing.RGB): Double {
        return alg2000(lab.fromRGB(rgb1), lab.fromRGB(rgb2))
    }
    fun compare(rgb1: ImageProcessing.RGBA, rgb2: ImageProcessing.RGBA): Double {
        return alg2000(lab.fromRGB(rgb1), lab.fromRGB(rgb2))
    }
    fun alg2000(lab1: ColorLab, lab2: ColorLab): Double{
        val k_L = 1.0
        val k_C = 1.0
        val k_H = 1.0
        val deg360InRad = deg2Rad(360.0)
        val deg180InRad = deg2Rad(180.0)
        val pow25To7 = 25.0.pow(7.0) /* pow(25, 7) */
        /*
         * Step 1 
         */
        /* Equation 2 */
        val C1 = sqrt((lab1.a * lab1.a) + (lab1.b * lab1.b))
        val C2 = sqrt((lab2.a * lab2.a) + (lab2.b * lab2.b))
        /* Equation 3 */
        val barC = (C1 + C2) / 2.0
        /* Equation 4 */
        val G = 0.5 * (1 - sqrt(barC.pow(7.0) / (barC.pow(7.0) + pow25To7)))
        /* Equation 5 */
        val a1Prime = (1.0 + G) * lab1.a
        val a2Prime = (1.0 + G) * lab2.a
        /* Equation 6 */
        val CPrime1 = sqrt((a1Prime * a1Prime) + (lab1.b * lab1.b))
        val CPrime2 = sqrt((a2Prime * a2Prime) + (lab2.b * lab2.b))
        /* Equation 7 */
        var hPrime1: Double
        if (lab1.b == 0.0 && a1Prime == 0.0)
            hPrime1 = 0.0
        else {
            hPrime1 = atan2(lab1.b, a1Prime)
            /* 
             * This must be converted to a hue angle in degrees between 0 
             * and 360 by addition of 2􏰏 to negative hue angles.
             */
            if (hPrime1 < 0)
                hPrime1 += deg360InRad
        }
        var hPrime2: Double
        if (lab2.b == 0.0 && a2Prime == 0.0)
            hPrime2 = 0.0
        else {
            hPrime2 = atan2(lab2.b, a2Prime)
            /* 
             * This must be converted to a hue angle in degrees between 0 
             * and 360 by addition of 2􏰏 to negative hue angles.
             */
            if (hPrime2 < 0)
                hPrime2 += deg360InRad
        }

        /*
         * Step 2
         */
        /* Equation 8 */
        val deltaLPrime = lab2.l - lab1.l
        /* Equation 9 */
        val deltaCPrime = CPrime2 - CPrime1
        /* Equation 10 */
        var deltahPrime: Double

        val CPrimeProduct = CPrime1 * CPrime2
        if (CPrimeProduct == 0.0)
            deltahPrime = 0.0
        else {
            /* Avoid the fabs() call */
            deltahPrime = hPrime2 - hPrime1
            if (deltahPrime < -deg180InRad)
                deltahPrime += deg360InRad
            else if (deltahPrime > deg180InRad)
                deltahPrime -= deg360InRad
        }
        /* Equation 11 */
        val deltaHPrime = 2.0 * sqrt(CPrimeProduct) * sin(deltahPrime / 2.0)

        /*
         * Step 3
         */
        /* Equation 12 */
        val barLPrime = (lab1.l + lab2.l) / 2.0
        /* Equation 13 */
        val barCPrime = (CPrime1 + CPrime2) / 2.0
        /* Equation 14 */
        var barhPrime: Double
        val hPrimeSum = hPrime1 + hPrime2
        barhPrime = if (CPrime1 * CPrime2 == 0.0) {
            hPrimeSum
        } else {
            if (abs(hPrime1 - hPrime2) <= deg180InRad)
                hPrimeSum / 2.0
            else {
                if (hPrimeSum < deg360InRad)
                    (hPrimeSum + deg360InRad) / 2.0
                else
                    (hPrimeSum - deg360InRad) / 2.0
            }
        }
        /* Equation 15 */
        val T = 1.0 - (0.17 * cos(barhPrime - deg2Rad(30.0))) +
        (0.24 * cos(2.0 * barhPrime)) +
                (0.32 * cos((3.0 * barhPrime) + deg2Rad(6.0))) -
                (0.20 * cos((4.0 * barhPrime) - deg2Rad(63.0)))
        /* Equation 16 */
        val deltaTheta = deg2Rad(30.0) *
                exp(-((barhPrime - deg2Rad(275.0)) / deg2Rad(25.0)).pow(2.0))
        /* Equation 17 */
        val R_C = 2.0 * sqrt(
            barCPrime.pow(7.0) /
        (barCPrime.pow(7.0) + pow25To7))
        /* Equation 18 */
        val S_L = 1 + ((0.015 * (barLPrime - 50.0).pow(2.0)) /
        sqrt(20 + (barLPrime - 50.0).pow(2.0)))
        /* Equation 19 */
        val S_C = 1 + (0.045 * barCPrime)
        /* Equation 20 */
        val S_H = 1 + (0.015 * barCPrime * T)
        /* Equation 21 */
        val R_T = (-sin(2.0 * deltaTheta)) * R_C

        /* Equation 22 */
        val deltaE = sqrt(
                (deltaLPrime / (k_L * S_L)).pow(2.0) +
                        (deltaCPrime / (k_C * S_C)).pow(2.0) +
                        (deltaHPrime / (k_H * S_H)).pow(2.0) +
                        (R_T * (deltaCPrime / (k_C * S_C)) * (deltaHPrime / (k_H * S_H))))

        return (deltaE)
    }

}
package com.bgaliev.occult_color_scheme.core

import Dlg
import MainScreen
import java.awt.Color
import java.awt.EventQueue
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


class Test {
    @org.junit.Test
    fun main(){
        //print(ColorLab.fromRGB(floatArrayOf(126f, 126f, 126f)) )
        test2()
    }
    fun test5(){
        val dialog = Dlg();
        dialog.setBounds(100, 100, 730, 489);
        //dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
    fun test4() {
        EventQueue.invokeLater {
            try {
                val window = MainScreen()
                window.frame.isVisible =true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Thread.sleep(10000000)
    }
    fun test3(){
        print(CIELab().fromRGB(ImageProcessing.RGB(84/255f,84/255f, 46/255f)))
        print(ColorNormalizer().alg2000(ColorLab(32.578806727690086, -0.6565684279613015, 19.34038606314289),
            ColorLab(46.314837459627114, 0.780247087258712, 24.81258284198198)))
        print(ColorNormalizer().compare(ImageProcessing.RGB(84/255f,84/255f, 46/255f),
            ImageProcessing.RGB(168/255f,151/255f, 119/255f)))
    }
    fun test2(
    ){
        val path = "E:\\HDDStorage\\pictures\\depositphotos_127863786-stock-illustration-pomegranate-pattern-cute-seamless-pattern.jpg"
        val iP  =ImageProcessing()
        iP.loadImage(path)?.let {
            val colorNormalizer = ColorNormalizer()
            val colors =(iP.dominantColorFrequencies(it,Pair(400, 400), 5, colorNormalizer = colorNormalizer )).map { it.color }
            println(colors.map {
                it.run {
                    val color = Color(r, g, b)
                    var hex = Integer.toHexString(color.rgb and 0xffffff)
                    if (hex.length < 6) {
                        hex = "0$hex"
                    }
                    "#$hex"
                    //ImageProcessing.RGB(r * 255, g * 255, b * 255)
                }
            })
            val pair = iP.harmonizeColors(colors, colorNormalizer, listOf(0.5,0.2,0.1, 0.1)).map {
                it.run {
                    val color = Color(r, g, b)
                    var hex = Integer.toHexString(color.rgb and 0xffffff)
                    if (hex.length < 6) {
                        hex = "0$hex"
                    }
                    "#$hex"
                    //ImageProcessing.RGB(r * 255, g * 255, b * 255)
                }
            }
            println(pair)
        }
    }

    fun test1(){
        try {
            val img = File(
                "E:\\HDDStorage\\pictures\\depositphotos_127863786-stock-illustration-pomegranate-pattern-cute-seamless-pattern.jpg")
            val image: BufferedImage = ImageIO.read(img)
            println(image.data.getPixel(1,1, FloatArray(4)).joinToString())
            val newImg = ImageProcessing().resize(image, Pair(120, 120))
            print("${newImg.width}, ${newImg.height}")
            ImageIO.write(newImg, "jpg", File("rer.jpg"));
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
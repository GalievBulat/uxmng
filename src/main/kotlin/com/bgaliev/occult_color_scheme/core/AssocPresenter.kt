package com.bgaliev.occult_color_scheme.core

import view.ViewNavigator

class AssocPresenter(private val colorNormalizer: ColorNormalizer) {
    fun getAssoc(palette: ViewNavigator.ImagePalette): String{
        var sinc = 0.0
        var exc  = 0.0
        var comp = 0.0
        var soph = 0.0
        var rugg = 0.0
        val gr = ColorsAssocVal.GREEN
        val bl = ColorsAssocVal.BLUE
        val pr = ColorsAssocVal.PURPLE
        for (color in palette.colors) {
            val gDist = colorNormalizer.compare(color, gr.rgd)
            val bDist = colorNormalizer.compare(color, bl.rgd)
            val pDist = colorNormalizer.compare(color, pr.rgd)
            sinc += gDist * gr.sinc + bDist * bl.sinc + pDist * bl.sinc

            exc += gDist * gr.exc + bDist * bl.exc + pDist * bl.exc

            comp += gDist * gr.comp + bDist * bl.comp + pDist * bl.comp

            soph += gDist * gr.soph + bDist * bl.soph + pDist * bl.soph

            rugg += gDist * gr.rugg + bDist * bl.rugg + pDist * bl.rugg
        }
        if(sinc > exc && sinc > comp && sinc > soph && sinc > rugg)
            return "Sincere"
        if(exc > sinc && exc > comp && exc > soph && exc > rugg)
            return "Exciting"
        if(comp > exc && comp > sinc && comp > soph && comp > rugg)
            return "Competent"
        if(rugg > exc && rugg > comp && rugg > soph && rugg > sinc)
            return "Rugged"
        else return "Unknown"
    }
}
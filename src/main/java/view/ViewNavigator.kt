package view

import com.android.tools.idea.configurations.ConfigurationManager
import com.android.tools.idea.ui.resourcemanager.plugin.LayoutRenderer
import com.android.tools.idea.util.androidFacet
import com.bgaliev.occult_color_scheme.core.AssocPresenter
import com.bgaliev.occult_color_scheme.core.ColorNormalizer
import com.bgaliev.occult_color_scheme.core.ImageProcessing
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlFile
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import org.jetbrains.kotlin.idea.util.projectStructure.allModules
import java.awt.image.BufferedImage
import java.util.concurrent.CompletableFuture
import javax.swing.JPanel

class ViewNavigator(val toolWindow: ToolWindow, private val contentFactory: ContentFactory,val project: Project) {
    private var curImage: BufferedImage? = null
    private val imageProcessing = ImageProcessing()
    private val colorNormalizer = ColorNormalizer()
    private val assocPresenter = AssocPresenter(colorNormalizer)
    var curPalette: ImagePalette? = null
    var palettesCount = 3
    var quality = Quality.MED
    var hueSpacing = ImageProcessing.HUE_CONTROL.COMPLEMENTARY
    var areaType: AreaType = AreaType.TYPE_1
    private val colorsInPalette
        get() = areaType.areas.size
    enum class AreaType(val areas: List<Double>){
        TYPE_1(listOf(0.55, 0.2, 0.15, 0.1)),TYPE_2(listOf(0.75, 0.1, 0.15)), TYPE_3(listOf(0.65, 0.2, 0.15))
    }
    enum class Quality( val dim: Pair<Int, Int>){
       MED(Pair(800, 800)),  HIGH(Pair(1000, 1000)),  LOW(Pair(400, 400));
    }

    fun getAssoc(imagePalette: ImagePalette): String {
        return assocPresenter.getAssoc(imagePalette)
    }
    fun getPaletteByImage(image: BufferedImage, colCount: Int = 10): ImagePalette {
        val domFreq = imageProcessing.dominantColorFrequencies(
            image, quality.dim,
            colCount, colorNormalizer = colorNormalizer, colorDifferenceScoreThreshold = 10.0,
        )
        val sum = domFreq.sumOf { it.freq }
        val freqs = domFreq.takeWhile {
            it.freq>sum*0.005
        }
        return ImagePalette(freqs.map { it.color }, freqs.map { (it.freq.toDouble() / sum)})

    }
    fun navigateToScreen(screen: JPanel) {

        val content: Content =
            contentFactory.createContent(screen,
                "",
                false
            )
        toolWindow.getContentManager().apply {
            removeAllContents(false)
            addContent(content)
            setSelectedContent(content);
        }
    }
    fun colorDiff(rgba1: ImageProcessing.RGBA, rgba2: ImageProcessing.RGBA): Double {
        return colorNormalizer.compare(rgba1, rgba2)
    }
    fun comparePalettes(maxColDiff: Double,maxAreaDiff: Double, imagePalette1: ImagePalette, imagePalette2: ImagePalette): Boolean{
        for (i in imagePalette1.colors.indices) {
            val minDiff = imagePalette2.colors.minByOrNull {
                colorDiff(it, imagePalette1.colors[i])
            }
            if (minDiff == null || colorDiff(minDiff, imagePalette1.colors[i]) > maxColDiff){
               return false
            }
            val areaDiff = imagePalette2.areas[imagePalette2.colors.indexOf(minDiff)] - imagePalette1.areas[1]
            if ( areaDiff> maxAreaDiff){
                return false
            }
        }
        return true

    }
    fun loadImage(path: String): Boolean{
        curImage = imageProcessing.loadImage(path)
        return true
    }
    data class ImagePalette(val colors: List<ImageProcessing.RGBA>, val areas: List<Double>)

    fun getImagePalettes(): List<ImagePalette>{

        curImage?.let {
            val colList = mutableListOf<ImagePalette>()
            val colorNormalizer = ColorNormalizer()
            val colors =
                (imageProcessing.dominantColorFrequencies(it, quality.dim, colorsInPalette*5, colorNormalizer = colorNormalizer)).map { it.color }

            for (i in 0 until palettesCount) {
                    imageProcessing.harmonizeColors(colors, colorNormalizer, areaType.areas, hueSpacing).let { col ->
                        colList.add(ImagePalette(col, areaType.areas))
                    }
            }
            return colList
        } ?: return emptyList()
    }
    fun loadLayoutPreview(): MutableList<CompletableFuture<BufferedImage?>> {
        val layoutList = mutableListOf<CompletableFuture<BufferedImage?>>()
        val layoutDir = FilenameIndex.getVirtualFilesByName( "layout", GlobalSearchScope.projectScope(project)).first()
        val module = project.allModules().find {
            it.androidFacet != null
        } ?: throw java.lang.IllegalStateException("no facet module")
        for (layout in layoutDir.children){
            val config = ConfigurationManager.getConfigurationForModule(module)
            val psi = PsiManager.getInstance(project).findFile(layout) as XmlFile
            layoutList.add( LayoutRenderer.getInstance(module.androidFacet!!).getLayoutRender(psi, config))
        }
        return layoutList
    }
    fun showMessage(title: String, text: String){
        Messages.showMessageDialog(text, title, Messages.getInformationIcon())
    }
}
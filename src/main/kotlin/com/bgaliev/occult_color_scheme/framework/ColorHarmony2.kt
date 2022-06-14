package com.bgaliev.occult_color_scheme.framework

import com.android.tools.idea.configurations.Configuration
import com.android.tools.idea.configurations.ConfigurationManager
import com.android.tools.idea.ui.resourcemanager.plugin.DesignAssetRendererManager
import com.android.tools.idea.ui.resourcemanager.plugin.DrawableAssetRenderer
import com.android.tools.idea.ui.resourcemanager.plugin.LayoutRenderer
import com.android.tools.idea.util.androidFacet
import com.intellij.codeInsight.completion.AllClassesGetter
import com.intellij.codeInsight.completion.PlainPrefixMatcher
import com.intellij.designer.actions.DesignerActionPanel
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.roots.ContentIterator
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.ui.Messages
import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.RecursiveTreeElementVisitor
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlFile
import com.intellij.util.Processor
import org.jetbrains.kotlin.idea.util.findModule
import org.jetbrains.kotlin.idea.util.projectStructure.allModules
import java.awt.Dimension


class ColorHarmony2 : AnAction() {


    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getData(PlatformDataKeys.PROJECT)!!
        val layoutDir = FilenameIndex.getVirtualFilesByName( "drawable", GlobalSearchScope.projectScope(project)).first()
        for (layout in layoutDir.children){
            val module = project.allModules()[1]
            val config = ConfigurationManager.getConfigurationForModule(module)
            val psi = PsiManager.getInstance(project).findFile(layout) as XmlFile
            val render = LayoutRenderer.getInstance(module.androidFacet!!).getLayoutRender(psi, config)
            render.get()
        }
        Messages.showMessageDialog(project, "Hello from Kotlin!", "Greeting", Messages.getInformationIcon())
    }
}

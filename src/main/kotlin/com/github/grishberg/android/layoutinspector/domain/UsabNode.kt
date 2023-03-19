package com.github.grishberg.android.layoutinspector.domain

import com.android.layoutinspector.model.ViewNode
import com.intellij.grazie.utils.dropPostfix
import com.intellij.grazie.utils.dropPrefix
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.ceil
import kotlin.math.roundToInt

class UsabNode     // constructor
    (
// getter methods
    val viewClass: String,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val visible: Boolean,
    var children: MutableList<UsabNode>?,
)

object TreeTraverser {
    // recursive method
    fun traverseTree(root: ViewNode): UsabNode{

        val usabNode = UsabNode(root.name, root.locationOnScreenX, root.locationOnScreenY, root.displayInfo.width,
            root.displayInfo.height, root.isDrawn, null)
        if (root.children.isEmpty())
            usabNode.children = null
        else
            usabNode.children = mutableListOf()
        for (child in root.children) {
            usabNode.children!!.add(traverseTree(child))
        }
        return usabNode
    }
}
class TreeToNameArrayConverter(private val nameToNumberDict: Map<String, Pair<Int,Int>>) {
    fun convert(rootNode: UsabNode): Array<IntArray> {

        return convertToMatrix(rootNode)
    }
    fun convertToMatrix(root: UsabNode): Array<IntArray> {
        // Define the size of the resulting matrix
        val matrixWidth = 160
        val matrixHeight = 90

        // Create the matrix with default values of 0
        val matrix = Array(TARGET_WIDTH) { IntArray(TARGET_HEIGHT) }

        // Traverse the tree and update the matrix with element IDs
        traverseTree(root, matrix, root.width, root.height)

        return matrix
    }

    fun traverseTree(element: UsabNode, matrix: Array<IntArray>, givenWidth: Int, givenHeight: Int) {
        // Calculate the position and size of the element in the matrix
        val x = (element.x * matrix[0].size / givenWidth).coerceIn(0 until matrix[0].size)
        val y = (element.y * matrix.size / givenHeight).coerceIn(0 until matrix.size)
        val width = (element.width * matrix[0].size / givenWidth).coerceIn(0 until matrix[0].size - x+1)
        val height = (element.height * matrix.size / givenHeight).coerceIn(0 until matrix.size - y+1)

        val nodeNumber: Int? = getMostFrequentEntry(element.viewClass)
        // Fill the corresponding cells in the matrix with the element ID

        if( nodeNumber!=null && element.visible)
        for (i in y until y + height) {
            for (j in x until x + width) {
                matrix[i][j] = nodeNumber
            }
        }

        // Traverse the child elements recursively
        if((element.children?.isNotEmpty()) == true)
            for (child in element.children!!) {
                traverseTree(child, matrix, givenWidth,givenHeight)
            }
    }
    fun getMostFrequentEntry( className: String): Int? {
        var id = -1
        var freq = 0
        val viewName = className.split(".").last().dropPrefix("Compat").dropPrefix("Material").dropPrefix("AppCompat")

        nameToNumberDict.forEach { k, v ->
            val nameDict = k.split(".").last().dropPrefix("Compat").dropPrefix("Material").dropPrefix("AppCompat")
            if (nameDict == viewName && v.second>freq) {
                id = v.first
                freq = v.second
            }
        }
        return if (id==-1)
            null
        else
            id
    }

    companion object {
        const val TARGET_WIDTH = 160
        const val TARGET_HEIGHT = 90
    }
}


object DictionaryLoader {
    @Throws(IOException::class)
    fun load(filePath: String?): Map<String, Pair<Int,Int>> {
        val dictionary: MutableMap<String, Pair<Int,Int>> = HashMap()
        BufferedReader(FileReader(filePath)).use { reader ->
            var line: String
            while (reader.readLine().also {
                    line = it ?: ""
                } != null) {
                val fields =
                    line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (fields.size >= 4) {
                    val key = fields[1].trim { it <= ' ' }
                    val value = Integer.valueOf(fields[2].trim { it <= ' ' })
                    val value2 = Integer.valueOf(fields[3].trim { it <= ' ' })
                    dictionary[key] = Pair(value, value2)
                }
            }
        }
        return dictionary
    }
}



package com.iib.iadbe_server.service

import com.iib.iadbe_server.model.BtechBM
import com.iib.iadbe_server.model.MVTec3DBM
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class TableService {
  fun getBtechBM(): List<BtechBM> {
    val resource = ClassPathResource("rawtables/Btech_Image_AUROC.tex")
    val btechBMList = mutableListOf<BtechBM>()

    try {
        val fileContent = resource.file.readText()

        // Find the start and end indices of the table within the LaTeX content
        val tableStart = fileContent.indexOf("\\begin{tabular}")
        val tableEnd = fileContent.indexOf("\\end{tabular}")

        if (tableStart != -1 && tableEnd != -1) {
            // Extract the content of the table between \begin{tabular} and \end{tabular}
            val tableContent = fileContent.substring(tableStart, tableEnd)

            // Split the table content into rows
            val rows = tableContent
                .split("\\midrule", "\\toprule", "\\bottomrule")  // Handle LaTeX table formatting
                .flatMap { it.split("\\\\") }                     // Split by row terminator
                .map { it.trim() }
                .filter { it.isNotEmpty() && !it.startsWith("\\") }  // Filter out commands and empty rows

            // Skip the first row (header) and process the rest
            rows.drop(1).forEach { row ->
                val columns = row.split("&").map { it.trim().replace("\\textbf{", "").replace("}", "") }
                if (columns.size == 5) {
                    val model = columns[0]
                    val category01 = columns[1].toDoubleOrNull() ?: 0.0
                    val category02 = columns[2].toDoubleOrNull() ?: 0.0
                    val category03 = columns[3].toDoubleOrNull() ?: 0.0
                    val average = columns[4].toDoubleOrNull() ?: 0.0

                    btechBMList.add(BtechBM(model, category01, category02, category03, average))
                } else {
                    println("Skipping malformed row: ${columns.joinToString(" | ")}")
                }
            }

//            println("Total models parsed: ${btechBMList.size}")
        } else {
            println("Table content not found")
        }
        } catch (e: Exception) {
            println("Error reading LaTeX file: ${e.message}")
            e.printStackTrace()
        }

        return btechBMList
    }

 fun getMVTec3DBM(): List<MVTec3DBM> {
    val resource = ClassPathResource("rawtables/MVTec3D_Image_AUROC.tex")
    val mvtec3DBMList = mutableListOf<MVTec3DBM>()

    try {
        val fileContent = resource.file.readText()

        // Find the start and end indices of the table within the LaTeX content
        val tableStart = fileContent.indexOf("\\begin{tabular}")
        val tableEnd = fileContent.indexOf("\\end{tabular}")

        if (tableStart != -1 && tableEnd != -1) {
            // Extract the content of the table between \begin{tabular} and \end{tabular}
            val tableContent = fileContent.substring(tableStart + "\\begin{tabular}".length, tableEnd).trim()

            // Clean up LaTeX formatting and split the table content into rows
            val rawRows = tableContent
                .replace("\\midrule", "")
                .replace("\\toprule", "")
                .replace("\\bottomrule", "")
                .replace("\\textbf{", "")  // Remove bold text formatting
                .replace("}", "")          // Remove closing braces
                .replace("\\\\", "\n")    // Ensure rows are split by new lines
                .trim()
                .split("\n")
                .map { it.trim() }
                .filter { it.isNotEmpty() && !it.startsWith("\\") }

            // Skip the header row and parse each remaining row
            val rows = rawRows.drop(1) // Skip header row
            rows.forEachIndexed { index, row ->
                val columns = row.split("&").map { it.trim() }

                if (columns.size == 12) { // Ensure the number of columns matches
                    val model = columns[0]
                    val bagel = columns[1].toDoubleOrNull() ?: 0.0
                    val cableGland = columns[2].toDoubleOrNull() ?: 0.0
                    val carrot = columns[3].toDoubleOrNull() ?: 0.0
                    val cookie = columns[4].toDoubleOrNull() ?: 0.0
                    val dowel = columns[5].toDoubleOrNull() ?: 0.0
                    val foam = columns[6].toDoubleOrNull() ?: 0.0
                    val peach = columns[7].toDoubleOrNull() ?: 0.0
                    val potato = columns[8].toDoubleOrNull() ?: 0.0
                    val rope = columns[9].toDoubleOrNull() ?: 0.0
                    val tire = columns[10].toDoubleOrNull() ?: 0.0
                    val average = when {
                        columns[11].isEmpty() || columns[11] == "-" -> 0.0
                        else -> columns[11].toDoubleOrNull() ?: 0.0
                    }

                    // Round average to two decimal places
                    val roundedAverage = String.format("%.2f", average).toDouble()

                    // Skip empty model names
                    if (model.isNotEmpty()) {
                        mvtec3DBMList.add(MVTec3DBM(model, bagel, cableGland, carrot, cookie, dowel, foam, peach, potato, rope, tire, roundedAverage))
                    } else {
                        println("Skipping entry with empty model name")
                    }
                } else {
                    println("Skipping malformed row at index $index: ${columns.joinToString(" | ")}")
                }
            }

            println("Total models parsed: ${mvtec3DBMList.size}")
        } else {
            println("Table content not found")
        }
    } catch (e: Exception) {
        println("Error reading LaTeX file: ${e.message}")
        e.printStackTrace()
    }

    return mvtec3DBMList
}












}
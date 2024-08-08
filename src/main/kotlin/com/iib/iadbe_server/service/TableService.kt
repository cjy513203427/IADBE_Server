package com.iib.iadbe_server.service

import com.iib.iadbe_server.model.BtechBM
import com.iib.iadbe_server.model.MVTec3DBM
import com.iib.iadbe_server.model.MVTecBM
import com.iib.iadbe_server.model.VisABM
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
                            mvtec3DBMList.add(
                                MVTec3DBM(
                                    model,
                                    bagel,
                                    cableGland,
                                    carrot,
                                    cookie,
                                    dowel,
                                    foam,
                                    peach,
                                    potato,
                                    rope,
                                    tire,
                                    roundedAverage
                                )
                            )
                        } else {
//                        println("Skipping entry with empty model name")
                        }
                    } else {
                        println("Skipping malformed row at index $index: ${columns.joinToString(" | ")}")
                    }
                }

//            println("Total models parsed: ${mvtec3DBMList.size}")
            } else {
                println("Table content not found")
            }
        } catch (e: Exception) {
            println("Error reading LaTeX file: ${e.message}")
            e.printStackTrace()
        }

        return mvtec3DBMList
    }

    fun getMVTecBM(): List<MVTecBM> {
        val resource = ClassPathResource("rawtables/MVTec_Image_AUROC.tex")
        val mvtecBMList = mutableListOf<MVTecBM>()

        try {
            val fileContent = resource.file.readText()

            // Find the start and end of the table
            val tableStart = fileContent.indexOf("\\begin{tabular}")
            val tableEnd = fileContent.indexOf("\\end{tabular}")

            if (tableStart != -1 && tableEnd != -1) {
                // Extract the content of the table
                val tableContent = fileContent.substring(tableStart + "\\begin{tabular}".length, tableEnd)

                // Clean up LaTeX formatting and split the table content into rows
                val rawRows = tableContent
                    .replace("\\midrule", "")
                    .replace("\\toprule", "")
                    .replace("\\bottomrule", "")
                    .replace("\\textbf{", "")  // Remove bold text formatting
                    .replace("}", "")          // Remove closing braces
                    .replace("\\\\", "")       // Remove trailing `\\`
                    .trim()
                    .split("\n")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() && !it.startsWith("\\") }

                // Skip only the first row
                val rows = rawRows.drop(1)
                rows.forEachIndexed { index, row ->
                    val columns = row.split("&").map { it.trim() }

                    if (columns.size == 17) { // Ensure the number of columns match
                        val model = columns[0]
                        val screw = columns[1].toDoubleOrNull() ?: 0.0
                        val pill = columns[2].toDoubleOrNull() ?: 0.0
                        val capsule = columns[3].toDoubleOrNull() ?: 0.0
                        val carpet = columns[4].toDoubleOrNull() ?: 0.0
                        val grid = columns[5].toDoubleOrNull() ?: 0.0
                        val tile = columns[6].toDoubleOrNull() ?: 0.0
                        val wood = columns[7].toDoubleOrNull() ?: 0.0
                        val zipper = columns[8].toDoubleOrNull() ?: 0.0
                        val cable = columns[9].toDoubleOrNull() ?: 0.0
                        val toothbrush = columns[10].toDoubleOrNull() ?: 0.0
                        val transistor = columns[11].toDoubleOrNull() ?: 0.0
                        val metalNut = columns[12].toDoubleOrNull() ?: 0.0
                        val bottle = columns[13].toDoubleOrNull() ?: 0.0
                        val hazelnut = columns[14].toDoubleOrNull() ?: 0.0
                        val leather = columns[15].toDoubleOrNull() ?: 0.0
                        val average = columns[16].replace("\\", "").toDoubleOrNull() ?: 0.0

                        // Add the parsed data to the list
                        if (model.isNotEmpty()) {
                            mvtecBMList.add(
                                MVTecBM(
                                    model,
                                    screw,
                                    pill,
                                    capsule,
                                    carpet,
                                    grid,
                                    tile,
                                    wood,
                                    zipper,
                                    cable,
                                    toothbrush,
                                    transistor,
                                    metalNut,
                                    bottle,
                                    hazelnut,
                                    leather,
                                    average
                                )
                            )
                        } else {
                            println("Skipping entry with empty model name")
                        }
                    } else {
                        println("Skipping malformed row at index $index: ${columns.joinToString(" | ")}")
                    }
                }

                //println("Total models parsed: ${mvtecBMList.size}")
            } else {
                println("Table content not found")
            }
        } catch (e: Exception) {
            println("Error reading LaTeX file: ${e.message}")
            e.printStackTrace()
        }

        return mvtecBMList
    }

    fun getVisABM(): List<VisABM> {
        val resource = ClassPathResource("rawtables/VisA_Image_AUROC.tex")
        val visABMList = mutableListOf<VisABM>()

        try {
            val fileContent = resource.file.readText()

            // Find the start and end of the table
            val tableStart = fileContent.indexOf("\\begin{tabular}")
            val tableEnd = fileContent.indexOf("\\end{tabular}")

            if (tableStart != -1 && tableEnd != -1) {
                val tableContent = fileContent.substring(tableStart + "\\begin{tabular}{l*{13}{c}}".length, tableEnd)

                // Clean up LaTeX format and split table content into rows
                val rawRows = tableContent
                    .replace("\\midrule", "")
                    .replace("\\toprule", "")
                    .replace("\\bottomrule", "")
                    .replace("\\textbf{", "")
                    .replace("}", "")
                    .trim()
                    .split("\n")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() && !it.startsWith("\\") }

                // Skip header row and parse remaining rows
                val rows = rawRows.drop(1)
                rows.forEachIndexed { index, row ->
                    val columns = row.split("&").map { it.trim() }

                    if (columns.size == 14) { // Ensure the number of columns matches
                        val model = columns[0]
                        val candle = columns[1].toDoubleOrNull() ?: 0.0
                        val capsules = columns[2].toDoubleOrNull() ?: 0.0
                        val cashew = columns[3].toDoubleOrNull() ?: 0.0
                        val chewinggum = columns[4].toDoubleOrNull() ?: 0.0
                        val fryum = columns[5].toDoubleOrNull() ?: 0.0
                        val macaroni1 = columns[6].toDoubleOrNull() ?: 0.0
                        val macaroni2 = columns[7].toDoubleOrNull() ?: 0.0
                        val pcb1 = columns[8].toDoubleOrNull() ?: 0.0
                        val pcb2 = columns[9].toDoubleOrNull() ?: 0.0
                        val pcb3 = columns[10].toDoubleOrNull() ?: 0.0
                        val pcb4 = columns[11].toDoubleOrNull() ?: 0.0
                        val pipeFryum = columns[12].toDoubleOrNull() ?: 0.0
                        val average = columns[13].replace("\\", "").toDoubleOrNull() ?: 0.0

                        // Add parsed data to the list
                        if (model.isNotEmpty()) {
                            visABMList.add(
                                VisABM(
                                    model,
                                    candle,
                                    capsules,
                                    cashew,
                                    chewinggum,
                                    fryum,
                                    macaroni1,
                                    macaroni2,
                                    pcb1,
                                    pcb2,
                                    pcb3,
                                    pcb4,
                                    pipeFryum,
                                    average
                                )
                            )
                        } else {
                            println("Skipping entry with empty model name")
                        }
                    } else {
                        println("Skipping malformed row at index $index: ${columns.joinToString(" | ")}")
                    }
                }

                //println("Total models parsed: ${visABMList.size}")
            } else {
                println("Table content not found")
            }
        } catch (e: Exception) {
            println("Error reading LaTeX file: ${e.message}")
            e.printStackTrace()
        }

        return visABMList
    }


}
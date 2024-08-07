package com.iib.iadbe_server

import com.iib.iadbe_server.model.BtechBM
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import java.io.File

@SpringBootTest
class tableProcessingTest {
    @Test
    fun readFile() {
        // Load the LaTeX file from the classpath resources
        val resource = ClassPathResource("rawtables/MVTec3D_Image_AUROC.tex")

        // Read the file content as a string
        val fileContent = resource.file.readText()

        // Find the start and end indices of the table within the LaTeX content
        val tableStart = fileContent.indexOf("\\begin{tabular}")
        val tableEnd = fileContent.indexOf("\\end{tabular}")

        if (tableStart != -1 && tableEnd != -1) {
            // Extract the content of the table between \begin{tabular} and \end{tabular}
            val tableContent = fileContent.substring(tableStart, tableEnd)

            // Split the table content into rows and clean up LaTeX commands
            val rows = tableContent.split("\\midrule", "\\toprule", "\\bottomrule")
                .flatMap { it.split("\\\\") }  // Split by line endings (\\)
                .map { it.trim() }              // Trim whitespace
                .filter { it.isNotEmpty() && !it.startsWith("\\") }  // Remove empty lines and commands

            // Print the parsed table rows, separating columns with a pipe (|)
            rows.forEach { row ->
                val columns = row.split("&").map { it.trim() }
                println(columns.joinToString(" | "))  // Print columns separated by '|'
            }
        } else {
            println("Table content not found")
        }
    }

    @Test
    fun serialize() {
        val btechBM = BtechBM("ModelX", 10.0, 20.0, 30.0, 20.0)

        // Serialize to JSON
        val jsonString = Json.encodeToString(btechBM)
        println(jsonString) // {"model":"ModelX","01":10.0,"02":20.0,"03":30.0,"average":20.0}

        // Deserialize from JSON
        val decodedBtechBM = Json.decodeFromString<BtechBM>(jsonString)
        println(decodedBtechBM) // BtechBM(model=ModelX, catogery01=10.0, catogery02=20.0, catogery03=30.0, average=20.0)
    }

}
package com.iib.iadbe_server

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import java.io.File

@SpringBootTest
class tableProcessingTest {
     @Test
    fun readFile() {
        // Load the LaTeX file from the classpath resources
        val resource = ClassPathResource("rawtables/Btech_Image_AUROC.tex")

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
}
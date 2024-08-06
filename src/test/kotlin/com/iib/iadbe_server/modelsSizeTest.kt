package com.iib.iadbe_server

import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import java.io.IOException
import java.io.InputStreamReader

@SpringBootTest
class modelsSizeTest {

@Test
    fun readCSV() {
        val resource = ClassPathResource("models_filesize/models_filesize.csv")

        try {
            InputStreamReader(resource.inputStream).use { reader ->
                val csvReader = CSVReader(reader)
                val lines = csvReader.readAll()

                // Skip header if present
                val header = lines.firstOrNull()
                if (header != null && header.size == 2 && header[0] == "model" && header[1] == "file size") {
                    lines.removeAt(0)
                }

                // Print each line
                for (line in lines) {
                    val model = line[0]
                    val size = line[1]
                    println("Model: $model, File Size: $size")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: CsvException) {
            e.printStackTrace()
        }
    }
}
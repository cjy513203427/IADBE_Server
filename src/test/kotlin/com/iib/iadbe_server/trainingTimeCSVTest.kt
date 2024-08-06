package com.iib.iadbe_server

import com.iib.iadbe_server.model.LineData
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import java.io.IOException
import java.io.InputStreamReader
import kotlin.test.Test

@SpringBootTest
class trainingTimeCSVTest {
    @Test
    fun readCSV() {
        val filenames = listOf("btech.csv", "kolektor.csv", "mvtec.csv", "mvtec3d.csv", "visa.csv")

        filenames.forEach { filename ->
            val resource = ClassPathResource("training_time/$filename")
            try {
                InputStreamReader(resource.inputStream).use { reader ->
                    val csvReader = CSVReader(reader)
                    val lines = csvReader.readAll()

                    // Skip header
                    lines.drop(1).forEach { line ->
                        val model = line[0]
                        val trainingTime = line[1].removeSuffix("s").toInt()
                        val imageAurocValue = line[2].toDouble()

                        val trainingData = LineData(model, trainingTime, imageAurocValue)
                        println(trainingData)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: CsvException) {
                e.printStackTrace()
            }
        }
    }
}
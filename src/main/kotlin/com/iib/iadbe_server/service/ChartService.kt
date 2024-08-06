package com.iib.iadbe_server.service

import com.iib.iadbe_server.model.BarData
import com.iib.iadbe_server.model.LineData
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.IOException
import java.io.InputStreamReader

@Service
class ChartService {

    fun getBarDataFromCSV(): List<BarData> {
        val resource = ClassPathResource("models_filesize/models_filesize.csv")
        val barDataList = mutableListOf<BarData>()

        try {
            InputStreamReader(resource.inputStream).use { reader ->
                val csvReader = CSVReader(reader)
                val lines = csvReader.readAll()

                // Skip header if present
                val header = lines.firstOrNull()
                if (header != null && header.size == 2 && header[0] == "model" && header[1] == "file size") {
                    lines.removeAt(0)
                }

                // Map lines to BarData
                for (line in lines) {
                    val modelName = line[0]
                    val fileSize = line[1]
                    barDataList.add(BarData(modelName, fileSize))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: CsvException) {
            e.printStackTrace()
        }

        return barDataList
    }

fun getLineDataFromCSV(): Map<String, Any> {
    val filenames = listOf("btech.csv", "kolektor.csv", "mvtec.csv", "mvtec3d.csv", "visa.csv")
    val dataMap = mutableMapOf<String, MutableList<LineData>>()
    val allModels = mutableSetOf<String>()

    filenames.forEach { filename ->
        val resource = ClassPathResource("training_time/$filename")
        InputStreamReader(resource.inputStream).use { reader ->
            val csvReader = CSVReader(reader)
            val lines = csvReader.readAll()
            val datasetKey = filename.split(".")[0] // e.g., "btech", "kolektor"
            val dataList = dataMap.getOrPut(datasetKey) { mutableListOf() }

            lines.drop(1).forEach { line ->
                val model = line[0]
                val trainingTime = line[1].removeSuffix("s").toInt()
                val imageAurocValue = line[2].toDouble()

                dataList.add(LineData(model, trainingTime, imageAurocValue))
                allModels.add(model)
            }
        }
    }

    // Prepare labels and datasets
    val labels = allModels.toList().sorted() // Ensure labels are sorted if needed
    val datasets = filenames.map { filename ->
        val datasetKey = filename.split(".")[0]
        val dataList = dataMap[datasetKey] ?: emptyList()

        // Map dataList to training times with models as labels
        val data = labels.map { model ->
            dataList.find { it.model == model }?.trainingTime ?: 0
        }

        mapOf(
            "label" to datasetKey,
            "data" to data
        )
    }

    return mapOf(
        "labels" to labels,
        "datasets" to datasets
    )
}
}
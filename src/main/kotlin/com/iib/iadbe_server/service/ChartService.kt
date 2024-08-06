package com.iib.iadbe_server.service

import com.iib.iadbe_server.model.BarData
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
}
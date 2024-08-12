package com.iib.iadbe_server.controller

import com.iib.iadbe_server.model.*
import com.iib.iadbe_server.service.ChartService
import com.iib.iadbe_server.service.TableService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ChartController(private val chartService: ChartService) {

    @GetMapping("/api/pie-data")
    fun getPieData(): List<PieData> {
        return listOf(
            PieData(5354, "MVTec", 15),
            PieData(5312, "MVTec3D", 10),
            PieData(2830, "Btech", 3),
            PieData(399, "Kolektor", 1),
            PieData(10821, "visa", 12)
        )
    }

    @GetMapping("/api/bar-data")
    fun getBarData(): List<BarData> {
        return chartService.getBarDataFromCSV()
    }

    @GetMapping("/api/line-data")
    fun getLineData(): Map<String, Any> {
        return chartService.getLineDataFromCSV()
    }

    @GetMapping("/api/radar-data")
    fun getRadarData(): List<Map<String, Any>> {
        // static data
        val data = listOf(
            RadarData("PatchCore", 32, 86, 96.36),
            RadarData("YOLOv9", 78, 35, 85.71)
        )

        // convert RadarData
        return data.map { radarData ->
            mapOf(
                "label" to radarData.modelName,
                "data" to listOf(radarData.trainingTime, radarData.modelSize, radarData.f1Score)
            )
        }
    }

    @GetMapping("/api/advance-pie-dataset")
    fun getAdvancePieDatasetData(): List<AdvancePieData> {
       return listOf(
            AdvancePieData(name = "Standard Datasets", value = 5),
            AdvancePieData(name = "Custom Datasets", value = 3)
        )
    }

    @GetMapping("/api/advance-pie-model")
    fun getAdvancePieModelData(): List<AdvancePieData> {
       return listOf(
            AdvancePieData(name = "Anomalib Models", value = 16),
            AdvancePieData(name = "YOLO Models", value = 3)
        )
    }


}
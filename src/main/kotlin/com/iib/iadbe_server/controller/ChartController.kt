package com.iib.iadbe_server.controller

import com.iib.iadbe_server.model.BarData
import com.iib.iadbe_server.model.LineData
import com.iib.iadbe_server.model.PieData
import com.iib.iadbe_server.service.ChartService
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
}
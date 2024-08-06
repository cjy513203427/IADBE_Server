package com.iib.iadbe_server.controller

import com.iib.iadbe_server.model.PieData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ChartController {

    @GetMapping("/api/pie-data")
    fun getPieData(): List<PieData> {
        return listOf(
            PieData(335, "Germany"),
            PieData(310, "France"),
            PieData(234, "Canada"),
            PieData(135, "Russia"),
            PieData(1548, "USA")
        )
    }
}
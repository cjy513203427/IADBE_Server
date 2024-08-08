package com.iib.iadbe_server.controller

import com.iib.iadbe_server.model.BtechBM
import com.iib.iadbe_server.model.MVTec3DBM
import com.iib.iadbe_server.model.MVTecBM
import com.iib.iadbe_server.model.VisABM
import com.iib.iadbe_server.service.TableService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TableController(private val tableService: TableService) {

    @GetMapping("/api/btech-benchmark")
    fun getBtechBenchmarkResults(): List<BtechBM> {
        return tableService.getBtechBM();
    }

    @GetMapping("/api/mvtec3d-benchmark")
    fun getMVTec3DBenchmarkResults(): List<MVTec3DBM> {
        return tableService.getMVTec3DBM();
    }

    @GetMapping("/api/mvtec-benchmark")
    fun getMVTecBenchmarkResults(): List<MVTecBM> {
        return tableService.getMVTecBM();
    }

    @GetMapping("/api/visa-benchmark")
    fun getVisABenchmarkResults(): List<VisABM> {
        return tableService.getVisABM();
    }

}
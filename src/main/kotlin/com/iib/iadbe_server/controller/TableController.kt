package com.iib.iadbe_server.controller

import com.iib.iadbe_server.model.BtechBM
import com.iib.iadbe_server.model.MVTec3DBM
import com.iib.iadbe_server.service.TableService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TableController (private val tableService: TableService){

    @GetMapping("/api/btech-bechmark")
    fun getBtechTrainingTime(): List<BtechBM>{
        return tableService.getBtechBM();
    }

    @GetMapping("/api/mvtec3d-bechmark")
    fun getMVTec3DTrainingTime(): List<MVTec3DBM>{
        return tableService.getMVTec3DBM();
    }
}
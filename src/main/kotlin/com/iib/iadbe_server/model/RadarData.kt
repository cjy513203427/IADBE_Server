package com.iib.iadbe_server.model

data class RadarData(
    val modelName: String,
    val trainingTime: Int,
    val modelSize: Int,
    val f1Score: Double
)
package com.iib.iadbe_server.model

data class RawLog(
    var imageAUROC: Double,
    var imagePRO: Double,
    var pixelAUROC: Double,
    var pixelPRO: Double,
    var trainingTime: String
)

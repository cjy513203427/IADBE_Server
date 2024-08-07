package com.iib.iadbe_server.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BtechBM(
    val model: String,
    @SerialName("01") val catogery01: Double,
    @SerialName("02") val catogery02: Double,
    @SerialName("03") val catogery03: Double,
    val average: Double
)
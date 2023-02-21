package com.app.nitro.domain.usecase.models

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class GraphDataModel(
    val data: GraphData? = null,
    val status: Boolean,
    val message: String? = null
) : Serializable

@JsonClass(generateAdapter = true)
data class GraphData(
    val day: List<Day>? = null,
    val month: List<Month>? = null,
    val week: List<Week>? = null,
    val year: List<Year>? = null
) : Serializable

@JsonClass(generateAdapter = true)
data class Day(
    val hour: Float,
    val value: Double
) : Serializable

@JsonClass(generateAdapter = true)
data class Month(
    val day: String,
    val value: Double
) : Serializable

@JsonClass(generateAdapter = true)
data class Week(
    val day: String,
    val value: Double
) : Serializable

@JsonClass(generateAdapter = true)
data class Year(
    val month: Int,
    val value: Double
) : Serializable

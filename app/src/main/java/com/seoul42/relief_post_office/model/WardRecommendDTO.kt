package com.seoul42.relief_post_office.model

import java.io.Serializable

data class WardRecommendDTO(
    val timeGap: Int,
    val safetyId: String,
    var resultId: String?,
    val safetyDTO: SafetyDTO
) : Serializable
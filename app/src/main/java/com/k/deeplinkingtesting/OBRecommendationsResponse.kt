package com.k.deeplinkingtesting

import com.google.gson.annotations.SerializedName

data class OBRecommendationsResponse(
    @SerializedName("response") val response: Response
)

data class Response(
    @SerializedName("recommendations") val recommendations: List<Recommendation>
)

data class Recommendation(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String
    // Add other fields as necessary
    )

package com.k.deeplinkingtesting.responses

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val response: Response
)

@Serializable
data class Response(
    val documents: Documents
)

@Serializable
data class Documents(
    val total_count: Int,
    val count: Int,
    val doc: List<Doc>
)

@Serializable
data class Doc(
    val `on-viewed`: List<String>,
    val source_name: String,
    val same_source: String,
    val source_display_name: String,
    val pc_id: Long,
    val adv_name: String,
    val publish_date: String,
    val orig_url: String,
    val ads_type: Int,
    val rec_en_did: String,
    val url: String,
    val author: String,
    val content: String,
    val pc_cmp_id: Long,
    val pos: String,
    val ttid: Int,
    val advId: String,
    val predictedRpm: String,
    val creativeFormat: String,
    val recLang: String,
    val thumbnail: Thumbnail
)

@Serializable
data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int,
    val imageImpressionType: String,
    val isAutoGen: Boolean
)

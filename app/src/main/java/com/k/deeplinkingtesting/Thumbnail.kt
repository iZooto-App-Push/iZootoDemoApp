package com.k.deeplinkingtesting

import kotlinx.serialization.Serializable
@Serializable
data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int,
    val imageImpressionType: String,
    val uuid: String,
    val isAutoGen: Boolean
)
@Serializable
data class Doc(
    val onViewed: List<String>,
    val sourceName: String,
    val sameSource: String,
    val sourceDisplayName: String,
    val pcId: Int,
    val advName: String,
    val publishDate: String,
    val origUrl: String,
    val recCats: List<Int>,
    val adsType: Int,
    val recEnDid: String,
    val url: String,
    val author: String,
    val content: String,
    val pcCmpId: Int,
    val pixels: List<String>,
    val pos: String,
    val ttid: Int,
    val clickTrackers: List<String>,
    val jsTrackers: List<String>,
    val advId: String,
    val cta: String,
    val creativeFormat: String,
    val recLang: String,
    val iabCategories: String,
    val thumbnail: Thumbnail
)
@Serializable
data class Documents(
    val totalCount: Int,
    val count: Int,
    val doc: List<Doc>
)
@Serializable
data class Root(
    val response: Response
)
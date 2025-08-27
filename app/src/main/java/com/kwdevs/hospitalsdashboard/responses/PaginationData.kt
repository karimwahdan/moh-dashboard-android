package com.kwdevs.hospitalsdashboard.responses

import com.squareup.moshi.Json

data class PaginationData<T>(
    @Json(name = "current_page")
    var currentPage:Int,

    @Json(name = "data") val data: List<T>,

    @Json(name = "first_page_url")
    var firstPageUrl:String,

    @Json(name = "from")
    var from:Int?,

    @Json(name = "last_page")
    var lastPage:Int,

    @Json(name = "last_page_url")
    var lastPageUrl:String,

    @Json(name = "next_page_url")
    var nexPageUrl:String?,

    @Json(name = "path")
    var path:String,

    @Json(name = "per_page")
    var perPage:Int,

    @Json(name = "prev_page_url")
    var previousPageUrl:String?,

    @Json(name = "to")
    var to:Int?,

    @Json(name = "total")
    var total:Int?,


)

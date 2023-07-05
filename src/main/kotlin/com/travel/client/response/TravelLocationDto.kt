package com.travel.client.response

data class TravelLocationDto(
    var code: String,
    var name: String? = null,
    var type: String,
    var latitude: Int,
    var longitude: Int,
    var description: String? = null,
    var parentCode: String? = null,
    var parentType: String? = null
)
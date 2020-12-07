package com.trapezoidlimited.groundforce.model.response

data class PageMetaData(
    val page: Int,
    val perPage: Int,
    val total: Int,
    val totalPages: Int
)
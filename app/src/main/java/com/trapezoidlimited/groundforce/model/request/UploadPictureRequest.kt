package com.trapezoidlimited.groundforce.model.request

data class UploadPictureRequest(
    val avatarUrl: String,
    val publicId: String
)
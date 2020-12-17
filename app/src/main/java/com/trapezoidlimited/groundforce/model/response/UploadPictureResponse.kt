package com.trapezoidlimited.groundforce.model.response

data class UploadPictureResponse(
    val avatarUrl: String,
    val publicId: String
) : ParentResponse
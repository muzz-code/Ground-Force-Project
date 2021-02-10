package com.trapezoidlimited.groundforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GeoPoints(
    val latitude: Double,
    val longitude: Double
): Parcelable
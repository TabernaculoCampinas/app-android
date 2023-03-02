package br.org.tabernaculocampinas.model.tabernacle

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Response(
    @SerializedName("erro")
    val error: Boolean,
    @SerializedName("message")
    val message: String
) : Parcelable
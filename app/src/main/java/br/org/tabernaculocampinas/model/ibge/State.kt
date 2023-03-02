package br.org.tabernaculocampinas.model.ibge

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class State(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nome")
    val name: String,
    @SerializedName("sigla")
    val acronym: String
) : Parcelable
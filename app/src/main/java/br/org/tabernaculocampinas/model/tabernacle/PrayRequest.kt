package br.org.tabernaculocampinas.model.tabernacle

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PrayRequest(
    @SerializedName("nome")
    val name: String,
    @SerializedName("fone")
    val phone: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("cidade")
    val city: String,
    @SerializedName("uf")
    val state: String,
    @SerializedName("pedido")
    val prayRequest: String
) : Parcelable
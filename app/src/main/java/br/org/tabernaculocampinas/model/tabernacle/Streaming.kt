package br.org.tabernaculocampinas.model.tabernacle

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Streaming(
    @SerializedName("aovivo")
    val live: Boolean,
    @SerializedName("video_id")
    val videoId: String,
    @SerializedName("status_go")
    val statusGo: String,
    @SerializedName("radio_url")
    val radioUrl: String,
    @SerializedName("local")
    val local: String,
    @SerializedName("youtubeURL")
    val youtubeURL: String,
    @SerializedName("flgWebRadio")
    val flagWebRadio: String,
    @SerializedName("flgAudiencia")
    val flagAudience: String,
    @SerializedName("flgGmeet")
    val flagGoogleMeet: String,
    @SerializedName("arquivo_leitura")
    val dailyReadingFile: String
) : Parcelable
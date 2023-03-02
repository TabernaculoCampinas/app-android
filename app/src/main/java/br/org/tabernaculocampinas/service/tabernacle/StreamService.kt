package br.org.tabernaculocampinas.service.tabernacle

import br.org.tabernaculocampinas.model.tabernacle.Streaming
import retrofit2.Call
import retrofit2.http.GET

interface StreamService {
    @GET("aovivo.php")
    fun getStream() : Call<Streaming>?
}
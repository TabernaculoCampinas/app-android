package br.org.tabernaculocampinas.service.tabernacle

import br.org.tabernaculocampinas.model.tabernacle.PrayRequest
import br.org.tabernaculocampinas.model.tabernacle.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PrayRequestService {
    @POST("pedidooracao/create.php")
    fun addPrayRequest(
        @Body request: PrayRequest, @Header("Authorization") token: String
    ): Call<Response>
}
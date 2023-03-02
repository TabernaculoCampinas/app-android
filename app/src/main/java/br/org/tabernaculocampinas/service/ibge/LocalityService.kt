package br.org.tabernaculocampinas.service.ibge

import br.org.tabernaculocampinas.model.ibge.State
import retrofit2.Call
import retrofit2.http.GET

interface LocalityService {
    @GET("localidades/estados")
    fun getStates(): Call<List<State>>
}
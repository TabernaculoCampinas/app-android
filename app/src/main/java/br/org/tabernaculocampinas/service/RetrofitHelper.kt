package br.org.tabernaculocampinas.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object {
        fun createRetrofitInstanceTabernacle(): Retrofit {
            val path = "http://tabernaculocampinas.org.br/api/"

            return createRetrofitInstance(path)
        }
        fun createRetrofitInstanceIbge(): Retrofit {
            val path = "https://servicodados.ibge.gov.br/api/v1/"

            return createRetrofitInstance(path)
        }

        private fun createRetrofitInstance(path: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
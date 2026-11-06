package org.ukrida.triemas4018week10.data.remote

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

class RemoteDataSource {
    suspend fun fetchRawProducts(): String {
        val response = HttpClientProvider.client.get(
            "${APIConfig.BASE_URL}/rest/v1/tbl_products"
        ) {
            header("apikey", APIConfig.API_KEY)
            parameter("select", "*")
            parameter("order", "product_name.asc")
        }
        return response.body<String>()
    }
}
package org.ukrida.triemas4018week10.data.remote

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import org.ukrida.triemas4018week10.data.models.Product
import org.ukrida.triemas4018week10.data.remote.APIConfig.API_KEY

class ProductRemoteDataSource {
    suspend fun fetchProducts(): List<Product> {
        return try {
            val response = HttpClientProvider.client.get(
                "${APIConfig.BASE_URL}/rest/v1/tbl_products"
            ) {
                header("apikey", API_KEY)
                header(HttpHeaders.Accept, "application/json")
                parameter(
                    "select",
                    "product_id, category_id, product_name, price, stock, image, created_at"
                )
                parameter("order", "product_name.asc")
            }
            response.body<List<Product>>()
        } catch (e: Exception) {
            throw e
        }
    }
}
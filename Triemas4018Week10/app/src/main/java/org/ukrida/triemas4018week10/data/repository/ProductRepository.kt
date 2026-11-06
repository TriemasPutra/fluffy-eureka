package org.ukrida.triemas4018week10.data.repository

import org.ukrida.triemas4018week10.data.models.Product
import org.ukrida.triemas4018week10.data.remote.ProductRemoteDataSource

class ProductRepository (
    private val remoteDataSource: ProductRemoteDataSource =
        ProductRemoteDataSource()
) {
    suspend fun getProducts(): List<Product>
    {
        return remoteDataSource.fetchProducts()
    }
}
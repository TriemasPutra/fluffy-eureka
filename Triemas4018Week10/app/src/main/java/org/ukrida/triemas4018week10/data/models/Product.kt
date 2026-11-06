package org.ukrida.triemas4018week10.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("product_id")
    val productId: Int,

    @SerialName("category_id")
    val categoryId: Int,

    @SerialName("product_name")
    val productName: String,

    @SerialName("price")
    val price: Double,

    @SerialName("stock")
    val stock: Int = 0,

    @SerialName("image")
    val image: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null
)

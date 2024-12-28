package com.calcmarket.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.calcmarket.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDAO {

    @Insert
    fun saveProduct(buyEntity: ProductEntity): Long

    @Insert
    suspend fun saveProducts(buyEntity: List<ProductEntity>): LongArray

    @Query("SELECT * FROM product WHERE product_name LIKE '%' || :queryString || '%' ")
    fun getProductsByName(queryString: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product WHERE product_name LIKE '%' || :queryString || '%' ")
    suspend fun getProductByName(queryString: String): ProductEntity?

}
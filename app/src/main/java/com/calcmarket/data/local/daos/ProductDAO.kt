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

    @Query("SELECT * FROM product WHERE product_name LIKE '%' || :queryString || '%' ")
    fun getProductsByName(queryString: String): Flow<List<ProductEntity>>

}
package com.calcmarket.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.calcmarket.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDAO {

    @Insert
    fun saveProduct(buyEntity: ProductEntity)

    @Query("SELECT product_name FROM product WHERE product_name LIKE '%' || :queryString || '%' ")
    fun getNameProducts(queryString: String): Flow<List<String>>

}
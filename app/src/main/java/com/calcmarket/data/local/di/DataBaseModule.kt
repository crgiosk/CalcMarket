package com.calcmarket.data.local.di

import android.content.Context
import androidx.room.Room
import com.calcmarket.data.local.daos.BuyDAO
import com.calcmarket.data.local.daos.ProductDAO
import com.calcmarket.data.local.db.CalcMarketDataBase
import com.calcmarket.data.local.db.Migrations.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    private const val DATABASE_NAME = "calk_market_database"


    @Singleton
    @Provides
    fun getDatabaseInstance(@ApplicationContext context: Context): CalcMarketDataBase {
        return Room
            .databaseBuilder(context, CalcMarketDataBase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Singleton
    @Provides
    fun getBuyDAOInstance(dataBase: CalcMarketDataBase): BuyDAO = dataBase.getBuyDAO()

    @Singleton
    @Provides
    fun getProductDAO(dataBase: CalcMarketDataBase): ProductDAO = dataBase.getProductDAO()


}
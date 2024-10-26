package com.calcmarket.data.network.di

import com.calcmarket.data.network.FirebaseProductsService
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseDBModule {

    @Provides
    @Singleton
    fun providesFirebaseDatabaseReference(): DatabaseReference {
        return Firebase.database.reference
    }

    @Provides
    @Singleton
    fun providesFirebaseChatService(databaseReference: DatabaseReference): FirebaseProductsService {
        return FirebaseProductsService(databaseReference)
    }

}
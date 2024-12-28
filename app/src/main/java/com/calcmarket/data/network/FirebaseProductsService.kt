package com.calcmarket.data.network

import android.util.Log
import com.calcmarket.BuildConfig
import com.calcmarket.data.network.model.ProductsFBResponse
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import com.calcmarket.data.network.dto.ProductsFRBDTO
import com.google.firebase.database.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirebaseProductsService @Inject constructor(
    private val firebaseDatabaseReference: DatabaseReference
) {

    companion object {
        private const val PATH_PRODUCTS = "productos"
    }

    fun saveNewProductToFirebase(productsFBRDTO: ProductsFRBDTO): Flow<Result<Unit>> = callbackFlow {
        val productsObject = getPathProducts().push()
        productsObject.setValue(productsFBRDTO)
            .addOnSuccessListener {
                trySend(Result.success(Unit))
                close()
            }
            .addOnFailureListener { exception ->
                trySend(Result.failure(exception))
                close(exception)
            }
        awaitClose {
        /* in here
        * 	•	Remover listeners en Firebase.
	        •	Cerrar conexiones.
	        •	Detener operaciones en curso.
        * */
        /* No necesitamos limpiar recursos explícitos aquí */
        }

    }

    fun subscribeAndGetToProducts(): Flow<List<ProductsFBResponse>> {
        val products = getPathProducts().snapshots.map { snapShot ->
            snapShot.children.mapNotNull {
                val product = it.getValue<ProductsFBResponse>()!!
                product.id = it.key!!
                product
            }
        }
        return products.catch { e ->
            if (BuildConfig.DEBUG) {
                Log.e("FirebaseError", "Error in Firebase subscription", e)
            }
        }
    }

    private fun getPathProducts(): DatabaseReference {
        return firebaseDatabaseReference.child(PATH_PRODUCTS)
    }

}
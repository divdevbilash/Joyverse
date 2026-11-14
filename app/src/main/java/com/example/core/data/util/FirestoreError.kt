package com.example.core.data.util

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException

enum class OperationType {
    CREATE,
    READ,
    UPDATE,
    DELETE,
    LIST
}

fun handleFirestoreError(e: Exception, operationType: OperationType, path: String) {
    val errorDetails = if (e is FirebaseFirestoreException) {
        "Code: ${e.code}, Message: ${e.message}"
    } else {
        e.localizedMessage ?: "Unknown error"
    }
    Log.e("FirestoreError", "Operation $operationType on $path failed: $errorDetails", e)
}

package com.flash.locationsharing_job3.repo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.flash.locationsharing_job3.model.AppUser
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener { result ->
                val userId = result.user?.uid
                if (userId == null) {
                    onComplete(false, "Could not obtain user id")
                    return@addOnSuccessListener
                }
                val userName = email.substringBefore("@")
                val user = AppUser(
                    userId = userId,
                    userName = userName,
                    email = email
                )
                db.collection("AppUsers").document(userId).set(user)
                    .addOnSuccessListener {
                        onComplete(true,"Stored Successfully")
                    }
                    .addOnFailureListener {error ->
                        onComplete(false,error.message)
                    }
            }
            .addOnFailureListener { error ->
                onComplete(false, error.message)
            }
    }

    fun loginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener { result ->
                onComplete(true, "Login Successful")
            }
            .addOnFailureListener { error ->
                onComplete(false, error.message)
            }
    }

    fun getAllUsers(onComplete: (List<AppUser>) -> Unit) {
        db.collection("AppUsers").get()
            .addOnSuccessListener { snapshots ->
                val list = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(AppUser::class.java)
                }
                onComplete(list)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun getCurrentUserEmail(): String? = auth.currentUser?.email

    fun getUserById(userId: String, callback: (AppUser?) -> Unit) {
        db.collection("AppUsers").document(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject(AppUser::class.java)
                callback(user)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun updateLocation(userId: String, lat: Double, lng: Double, onComplete: (Boolean, String?) -> Unit) {
        db.collection("AppUsers").document(userId).update(
            mapOf(
                "latitude" to lat,
                "longitude" to lng
            )
        )
            .addOnSuccessListener {
                onComplete(true, "Location Updated")
            }
            .addOnFailureListener { error ->
                onComplete(false, error.message)
            }

    }

    fun updateLocationAuto(context: Context, onComplete: (Boolean) -> Unit) {

        val fused = LocationServices.getFusedLocationProviderClient(context)

        val userId = getCurrentUserId() ?: return

        val hasFine = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            onComplete(false)
            return
        }

        val request = CurrentLocationRequest.Builder()
            .setPriority(if (hasFine) Priority.PRIORITY_HIGH_ACCURACY else Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .setDurationMillis(10000)
            .build()

        fused.getCurrentLocation(request, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    updateLocation(userId, location.latitude, location.longitude) { success, _ ->
                        onComplete(success)
                    }
                } else {
                    onComplete(false)
                }
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun updateUsername(userId: String, newName: String, onComplete: (Boolean) -> Unit) {
        db.collection("AppUsers").document(userId)
            .update("userName", newName)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }


}
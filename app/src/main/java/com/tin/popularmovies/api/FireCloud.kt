package com.tin.popularmovies.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FireCloud @Inject constructor() {

    // This is a Singleton. It doesn't need to be added to a Dagger Module
    private val fireBaseAuthInstance = FirebaseAuth.getInstance()
    private val fireBaseFireStoreInstance = FirebaseFirestore.getInstance()

    fun isUserLoggedIn(): Boolean = fireBaseAuthInstance.currentUser != null

    private val userUid = fireBaseAuthInstance.currentUser?.uid

    val auth = fireBaseAuthInstance

    val moviesCollection = fireBaseFireStoreInstance
        .document("$USERS_COLLECTION_KEY/$userUid")
        .collection(MOVIES_COLLECTION_KEY)

    fun getMovieDocument(movieUid: Int): DocumentReference = fireBaseFireStoreInstance.document("$USERS_COLLECTION_KEY/$userUid/${MOVIES_COLLECTION_KEY}/$movieUid")

    companion object {
        const val USERS_COLLECTION_KEY = "Users"
        const val MOVIES_COLLECTION_KEY = "Movies"
    }
}
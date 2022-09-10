package com.wingsmight.pushwords.data.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wingsmight.pushwords.data.User;

import java.util.HashMap;
import java.util.Map;

public final class CloudDatabase {
    private static final String TAG = "CloudDatabase";
    private static final String USERS_COLLECTION_PATH = "users";


    public static void addUser(User user) {
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

        dataBase.collection(USERS_COLLECTION_PATH)
                .add(user.getMapData())
                .addOnSuccessListener(documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.w(TAG, "Error adding document", e));
    }
    public static void getUser(String email, Consumer<User> onSuccess) {
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

        dataBase.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            onSuccess.accept(new User(snapshot));
                        }
                    } else {
                        Log.w("CloudDatabase", "Error getting documents.", task.getException());
                    }
                });
    }
}

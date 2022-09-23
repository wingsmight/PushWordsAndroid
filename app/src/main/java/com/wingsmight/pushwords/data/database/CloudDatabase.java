package com.wingsmight.pushwords.data.database;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.wingsmight.pushwords.data.User;
import com.wingsmight.pushwords.handlers.InternalStorage;
import com.wingsmight.pushwords.ui.dictionaryTab.CategoryButton;
import com.wingsmight.pushwords.ui.dictionaryTab.DictionaryTab;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CloudDatabase {
    private static final String LEARNING_CATEGORIES_FOLDER_NAME = "LearningCategories";
    private static final Long LEARNING_CATEGORIES_FILE_MAX_SIZE = 32 * 1024 * 1024L;
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
    public static void updateUser(User user) {
        if (user == null
                || user.getDocumentID() == null
                || user.getDocumentID().isEmpty()) {
            Log.i(TAG, "user.documentID is empty");

            return;
        }

        Log.i(TAG, "updateUserData started");

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference reference = database
                .collection("users")
                .document(user.getDocumentID());

        reference.set(user.getMapData());
    }
    public static void getData(FirebaseUser user, Consumer<User> onSuccess) {
        String email = user.getEmail();
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
    public static void loadLearningCategories(Context context,
                                              Consumer<File> onSuccess) {
        StorageReference reference = FirebaseStorage.getInstance()
                .getReference()
                .child(LEARNING_CATEGORIES_FOLDER_NAME);

        Task<ListResult> files = reference.listAll();
        files.addOnSuccessListener(listResult -> {
            List<StorageReference> categoryFiles = listResult.getItems();
            for (StorageReference categoryFile : categoryFiles) {
                categoryFile.getBytes(LEARNING_CATEGORIES_FILE_MAX_SIZE)
                        .addOnSuccessListener(bytes -> {
                            String fileName = InternalStorage.LEARNING_CATEGORIES_DIRECTORY
                                    + "/" + categoryFile.getName();
                            File file = InternalStorage
                                    .writeFile(context, fileName, bytes);

                            onSuccess.accept(file);
                        });
            }
        });
    }


    public class FileInfo {
        private String fileName;
        private byte[] content;


        public FileInfo(String fileName, byte[] content) {
            this.fileName = fileName;
            this.content = content;
        }


        public String getFileName() {
            return fileName;
        }
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        public byte[] getContent() {
            return content;
        }
        public void setContent(byte[] content) {
            this.content = content;
        }
    }
}

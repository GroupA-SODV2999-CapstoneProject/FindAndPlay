package com.hfad.findandplayA;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Test {
    FirebaseFirestore db= FirebaseFirestore.getInstance();
//    DocumentReference Categories= db.collection("Categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//        @Override
//        public void onComplete(@NonNull Task<QuerySnapshot> task)
//        {
//            if(task.isSuccessful())
//            {
//
//            }
//            else
//            {
//                Log.i("info", "Error getting documents: ", task.getException());
//            }
//        }
//    })
}

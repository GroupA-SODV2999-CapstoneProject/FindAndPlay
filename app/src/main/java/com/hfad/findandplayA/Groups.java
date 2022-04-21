package com.hfad.findandplayA;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Groups {
    static ArrayList<String> children;
    public ArrayList<String> childGroups = new ArrayList<>();

// function to return child groups
    public ArrayList<String> getGroups(){
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("All Groups", document.getId());

                                childGroups.add(document.getId());
                            }
                        }else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        return childGroups;
    }


//function to return all children from a group
    public ArrayList<String> getChildren(){
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    ArrayList<String> groups = new ArrayList<>();
    db.collection("Groups")
            .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()) {
                     for (QueryDocumentSnapshot document : task.getResult()) {
                       Log.d("something" , document.getId() + " => " + document.getData());
                       groups.add(document.getId());
                   }
            } else {
                Log.w("TAG", "Error getting documents.", task.getException());
                   }
                                                                               }
                           });

    for(String group : groups){
        db.collection("Groups").document(group).collection("Children")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("something" , document.getId() + " => " + document.getData());
                                children.add(document.getId());

                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    return children;
}



     public boolean deleteDoc(DocumentSnapshot child) {
         FirebaseFirestore db = FirebaseFirestore.getInstance();
         Map<String, Object> data = child.getData();
         for (Map.Entry<String, Object> entry : data.entrySet()) {
             String Name = entry.getKey();
             if (Name.equals("age")) {
                 if (entry.getValue().equals(3)) {
                     db.collection("groups").document("group1").collection("children").document(child.getId()).delete();
                     return true;

                 } else if (entry.getValue().equals(4)) {
                     db.collection("groups").document("group2").collection("children").document(child.getId()).delete();
                     return true;
                 } else if (entry.getValue().equals(5)) {
                     db.collection("groups").document("group3").collection("children").document(child.getId()).delete();
                     return true;
                 }

             }
         }
         ;
     return false;
     }
     public static void AddGroup(String GroupName){
         FirebaseFirestore db = FirebaseFirestore.getInstance();
         db.collection("groups").add(GroupName);
     }

     public static void AddChild(String GroupName,String ChildName, HashMap<String,String> ChildInfo){
         FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("groups").document(GroupName).collection("Children").document(ChildName).set(ChildInfo);

     }



}





package com.hfad.findandplayA.viewmodels;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
  * to create a new user, create an instance passing
  * all 4 fields of first consturctor, pass default values for unavailable
  * data e.g photoUri (null or Uri.EMPTY), then call user.save(listener)
  *
  * to update the authenticated user profile, modify the public props:
  *   user.displayName = ?
  *   user.photoUri = ?
  *   user.save(listener) // save
  *
  * you can also get that same user from Auth.getCurrentUser() e.g:
  *   Auth.getCurrentUser().displayName = "Stewie Griffin";
  *   Auth.getCurrentUser().save(ok -> {});
  */

public class User {
    private FirebaseUser link;
    private String uid;
    private String email;
    public Uri photoUri;
    public String displayName;
    private String password;

    public User( String email, String password, String displayName, Uri photoUri )
    {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.photoUri = photoUri;
    }

    public User ( FirebaseUser link )
    {
        this.link = link;
        refreshPropertiesFromAuth();
    }

    public User ()
    {
    }

    private void refreshPropertiesFromAuth()
    {
        uid = link.getUid();
        email = link.getEmail();
        photoUri = link.getPhotoUrl();
        displayName = link.getDisplayName();
    }

    // a bunch of getters for easier data reads
    public FirebaseUser getLink() { return link; }
    public String getUid() { return uid; }
    public String getEmail() { return email; }
    public Uri getPhotoUri() { return photoUri; }
    public String getDisplayName() { return displayName; }

    public void save( Consumer<Boolean> then ) {
        if (link != null) { // update existing user
            update(then);
        } else { // insert new user
            create(then);
        }
    }

    private void create( Consumer<Boolean> then )
    {
        Auth.getFirebaseAuthInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new Executor() {
                    @Override
                    public void execute(Runnable runnable) {
                        runnable.run();
                    }
                }, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( ! task.isSuccessful() ) {
                            then.accept(false);
                            return;
                        }

                        // refresh current object state as we now have a valid link
                        // since task is successful
                        link = Objects.requireNonNull(task.getResult()).getUser();
                        refreshPropertiesFromAuth();
                        then.accept(true);
                    }
                });
    }

    private void update( Consumer<Boolean> then )
    {
        link.updateProfile(new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(photoUri)
                .build()
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if ( task.isSuccessful() ) {
                    then.accept(false);
                    return;
                }

                then.accept(true);
            }
        });
    }
}

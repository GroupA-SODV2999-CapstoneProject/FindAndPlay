package com.hfad.findandplayA.viewmodels;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class Auth {
    private static FirebaseAuth auth;
    private static User currentUser;

    public static FirebaseAuth getFirebaseAuthInstance()
    {
        if ( null == auth ) {
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }

    // returns a logged in user object
    public static User getCurrentUser()
    {
        if ( null != currentUser )
            return currentUser;

        FirebaseUser userObj = getFirebaseAuthInstance().getCurrentUser();

        if ( userObj != null && ! Strings.isEmptyOrWhitespace(userObj.getUid()) ) {
            User user = new User(userObj.getUid());
            user.setEmail(userObj.getEmail()); // @todo rm
            return (currentUser = null != user.getEmail() ? user : new User(null));
        } else { // a default user object
            return (currentUser = new User(null));
        }
    }

    public static boolean isLoggedIn()
    {
        return getCurrentUser().getUid() != null;
    }

    public static void login( String email, String password, Consumer<Boolean> then )
    {
        getFirebaseAuthInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new Executor() {
                    @Override
                    public void execute(Runnable runnable) {
                        runnable.run();
                    }
                }, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        currentUser = null; // reload session info on next getCurrentUser() call
                        then.accept(task.isSuccessful());
                    }
                });
    }

    public static void signOut()
    {
        try {
            getFirebaseAuthInstance().signOut();
        } catch (Exception e) {}

        currentUser = null;
    }
}

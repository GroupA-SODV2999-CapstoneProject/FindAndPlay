package com.hfad.findandplayA.viewmodels;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class User {
    private String uid;
    private String email;
    private String password;

    public User( String email, String password )
    {
        this.email = email;
        this.password = password;
    }

    public User ( String uid )
    {
        this.uid = uid;
        // @todo load firebase user by uid
        // the next step requires the setup of firebase admin sdk
        // all @todo items will be handled using admin API
        // @see https://github.com/GroupA-SODV2999-CapstoneProject/FindAndPlay/issues/21
    }

    // @todo delete this method once SDK is setup
    public void setEmail(String email) { this.email = email; }

    public String getEmail()
    {
        return email;
    }

    public String getUid()
    {
        return uid;
    }

    public void save( Consumer<Boolean> then )
    {
        if ( uid != null ) { // update existing user
            // update(then); @todo
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
                        // can get uid from task.getResult().getUser().getUid() if needed
                        then.accept(task.isSuccessful());
                    }
                });
    }
}

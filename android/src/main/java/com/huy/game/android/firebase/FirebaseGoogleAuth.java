package com.huy.game.android.firebase;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseGoogleAuth {

    public static AuthCredential verifyGoogleId(Context context, String idToken) {
        if (idToken != null) {
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            return firebaseCredential;
        }else {
            Toast.makeText(
                context,
                "No idToken",
                Toast.LENGTH_SHORT
            ).show();
            return null;
        }
    }
}

// app/src/main/java/com/example/cityxpertzz/auth/AuthUtils.java
package com.example.cityxpertzz.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthUtils {

    public static boolean isLoggedIn() {
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        return u != null;
    }
}

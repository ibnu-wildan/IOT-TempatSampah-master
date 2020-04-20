package com.pratamatechnocraft.smarttempatsampah.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.pratamatechnocraft.smarttempatsampah.LoginActivity;
import com.pratamatechnocraft.smarttempatsampah.MainActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferencesLogin;
    public SharedPreferences.Editor editorLogin;
    public Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String USER_ID = "USER_ID";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferencesLogin =context.getSharedPreferences( PREF_NAME, PRIVATE_MODE );
        editorLogin = sharedPreferencesLogin.edit();
    }


    public void createSessionLogin(String user_id){
        editorLogin.putBoolean(LOGIN, true);
        editorLogin.putString(USER_ID, user_id );
        editorLogin.apply();
    }

    public boolean isLoggin(){
        return sharedPreferencesLogin.getBoolean( LOGIN, false );
    }

    public void checkLogin(){
        if (!this.isLoggin()){
            Intent intent = new Intent( context, LoginActivity.class );
            context.startActivity( intent );
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getDataLogin(){
        HashMap<String, String> user = new HashMap<>(  );
        user.put(USER_ID, sharedPreferencesLogin.getString(USER_ID, null ) );
        return user;
    }

    public void logout(){
        editorLogin.clear();
        editorLogin.commit();
        Intent intent = new Intent( context, LoginActivity.class );
        context.startActivity( intent );
        ((MainActivity) context).finish();
    }
}

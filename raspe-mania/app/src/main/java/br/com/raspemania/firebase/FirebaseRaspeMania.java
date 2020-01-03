package br.com.raspemania.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class FirebaseRaspeMania {


    /*--------------------------------------------------------------------------------------------*/
    public static final FirebaseFirestore getDatabase() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        db.setFirestoreSettings(settings);

        return db;
    }

    /*--------------------------------------------------------------------------------------------*/
    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    /*--------------------------------------------------------------------------------------------*/
    public static final boolean isConnected() {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            if (user.getUid() != null) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    /*--------------------------------------------------------------------------------------------*/
    public static final String getUsuarioNumero() {

        try {

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            return "myPhone_" + user.getPhoneNumber()  ;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /*--------------------------------------------------------------------------------------------*/
    public static final String getUidUsuario() {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            return user.getUid();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /*--------------------------------------------------------------------------------------------*/
    public static final String getEMailUsuario() {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            return user.getEmail();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /*--------------------------------------------------------------------------------------------*/
}

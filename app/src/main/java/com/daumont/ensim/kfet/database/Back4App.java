package com.daumont.ensim.kfet.database;

import android.content.Context;
import android.util.Log;

import com.parse.Parse;

/**
 * Created by jojo- on 16/08/2017.
 */

public class Back4App {


    /**
     * Connexion of database online
     * @param context context of activity
     */
    public Back4App(Context context){
        try {
            Parse.initialize(new Parse.Configuration.Builder(context)
                    .applicationId("485uuQEORScsLeBdVPCUt1aX7jPrUj3sUnYLY0aW")
                    .clientKey("ThUUPROoCemiucEipYzLJLeheMoLQbiBUcD6LRmR")
                    .server("https://parseapi.back4app.com")
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

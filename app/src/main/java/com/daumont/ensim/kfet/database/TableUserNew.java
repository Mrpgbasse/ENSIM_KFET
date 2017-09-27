package com.daumont.ensim.kfet.database;


import android.content.Context;
import android.util.Log;

import com.daumont.ensim.kfet.modele.User_new;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * Created by jojo- on 13/09/2017.
 */

public class TableUserNew extends Back4App {

    private String TABLE_USER = "TABLE_USER";
    private String NOM = "NOM";
    private String NUM_ETU = "NUM_ETU";
    private String RANG = "RANG";
    private String CREDIT = "CREDIT";
    private String MDP = "MDP";


    public TableUserNew(Context context) {
        super(context);
    }

    public void add_user(User_new user) {

        ParseObject parse_code = new ParseObject(TABLE_USER);
        parse_code.put(NOM, user.getNom());
        parse_code.put(NUM_ETU, user.getNum_etu());
        parse_code.put(RANG, user.getRang());
        parse_code.put(CREDIT, "" + user.getCredit());
        parse_code.put(MDP, "" + user.getMdp());
        parse_code.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e != null) {
                    Log.d("ONLINE", "ERROR TableUser - add_user()");
                } else {
                    Log.d("ONLINE : ", "OK TableUser - add_user() ");

                }
            }
        });
    }

    public User_new get_user(String num_etu) {

        ParseQuery query = ParseQuery.getQuery(TABLE_USER);
        query.whereEqualTo(NUM_ETU, num_etu);
        ParseObject parseObject = null;
        User_new user = null;
        try {
            parseObject = query.getFirst();

            user = new User_new(
                    parseObject.get(NOM).toString(),
                    parseObject.get(NUM_ETU).toString(),
                    parseObject.get(RANG).toString(),
                    Float.parseFloat(parseObject.get(CREDIT).toString()),
                    parseObject.get(MDP).toString()
            );
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("ONLINE", "Problème pour get_user() : " + e);
        }


        return user;
    }
}

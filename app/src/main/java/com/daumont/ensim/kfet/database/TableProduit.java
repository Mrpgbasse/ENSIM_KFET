package com.daumont.ensim.kfet.database;

import android.content.Context;
import android.util.Log;

import com.daumont.ensim.kfet.modele.Produit;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by jojo- on 13/09/2017.
 */

public class TableProduit extends Back4App{

    private String TABLE_PRODUIT = "TABLE_PRODUIT";
    private String ID_PRODUIT = "ID_PRODUIT";
    private String NOM = "NOM";
    private String PRIX_VENTE = "PRIX_VENTE";
    private String STOCK = "STOCK";
    private String PRIX_ACHAT = "PRIX_ACHAT";




    public TableProduit(Context context) {
        super(context);
    }

    public int add_produit(Produit produit) {

        int id_produit = generate_id();

        ParseObject parse_code = new ParseObject(TABLE_PRODUIT);
        parse_code.put(ID_PRODUIT, id_produit);
        parse_code.put(NOM, produit.getNom());
        parse_code.put(PRIX_VENTE, ""+produit.getPrix_vente());
        parse_code.put(STOCK, produit.getStock());
        parse_code.put(PRIX_ACHAT, ""+produit.getPrix_achat());

        parse_code.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e != null) {
                    Log.d("ONLINE", "ERROR TableProduit - add_produit()");
                } else {
                    Log.d("ONLINE : ", "OK TableProduit - add_produit() ");

                }
            }
        });
        return id_produit;
    }

    public int generate_id() {
        int qrcode=0;
        ParseQuery query = ParseQuery.getQuery(TABLE_PRODUIT);
        List<ParseObject> parseObject = null;
        Produit produit=null;
        try {
            parseObject = query.find();

            if(parseObject.size()==0){
                qrcode = 1;
            }else{
                qrcode =  (int)parseObject.get(parseObject.size()-1).get(ID_PRODUIT)+1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return qrcode;
    }

    public Produit get_produit(int id_produit) {
        ParseQuery query = ParseQuery.getQuery(TABLE_PRODUIT);
        query.whereEqualTo(ID_PRODUIT, id_produit);
        ParseObject parseObject = null;
        Produit produit = null;
        try {
            parseObject = query.getFirst();

            produit = new Produit(
                    (int) parseObject.get(ID_PRODUIT),
                    parseObject.get(NOM).toString(),
                    Float.parseFloat(parseObject.get(PRIX_VENTE).toString()),
                    Integer.parseInt(parseObject.get(STOCK).toString()),
                    Float.parseFloat(parseObject.get(PRIX_ACHAT).toString())
            );
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("ONLINE", "Problème pour get_user() : " + e);
        }
        return produit;
    }
}

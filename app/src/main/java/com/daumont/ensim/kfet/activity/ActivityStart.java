package com.daumont.ensim.kfet.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daumont.ensim.kfet.Methodes;
import com.daumont.ensim.kfet.R;


/**
 * JONATHAN DAUMONT
 * PRESENTE L'APPPLICATION
 * ET
 * VERIFIE SI IL Y A UNE CONNEXION INTERNET DE DISPONIBLE
 */
public class ActivityStart extends AppCompatActivity {

    //TODO COMMENTAIRE
    //TODO PROPOSITION
    //TODO MODE HORS LIGNE

    /**
     * Atributs
     */

    /**
     * Création de l'activité
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //TODO mode en ligne et local

        //TableUserNew tableUserNew = new TableUserNew(this.getBaseContext());

       // tableUserNew.add_user(new User_new("toto","e152611","admin", 20.02f));

        //User_new user = tableUserNew.get_user("e152611");

        //Toast.makeText(this, "->"+user.getNom(), Toast.LENGTH_SHORT).show();

        //TableProduit tableProduit = new TableProduit(this.getBaseContext());
        //tableProduit.add_produit(new Produit("café",0.4f,100,0.2f));


        //On test si il y a une connexion internet de disponible
        if (Methodes.internet_diponible(this)) {
            //réveil de la base de donnéese BACK4APP

            //Table_emprunt table_emprunt = new Table_emprunt(this);
            // table_emprunt.add_emprunt(new Emprunt(100,101,101));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ActivityStart.this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                    finish();
                }
            }, 2000);
        } else {


            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityStart.this);
            builder.setCancelable(false);
            builder.setMessage("Internet n'est pas activé\nVeuillez l'activer.")
                    .setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ActivityStart.this, ActivityLogin.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.create();
            builder.show();


        }


    }




}

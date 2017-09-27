package com.daumont.ensim.kfet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daumont.ensim.kfet.database.Table_user_online;
import com.daumont.ensim.kfet.R;


/**
 * JONATHAN DAUMONT
 * PERMET DE VERIFIER SI L'IDENTIFIANT EXISTE
 */
public class Activity_identifiant extends AppCompatActivity {

    /**
     * ATTRIBUTS
     */
    //elements visuels
    private Button button_suivant;
    private EditText editText_login;
    private LinearLayout layout_chargement;
    private ProgressDialog mProgressDialog;
    //BDD
    private Table_user_online table_user_online;
    //autres
    private Activity activity;
    private String identifiant;
    private boolean identifiant_ok;

    /**
     * Création de l'activité
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Recuperation des elements visuels
        setContentView(R.layout.activity_identifiant);
        button_suivant = (Button) findViewById(R.id.button_suivant);
        editText_login = (EditText) findViewById(R.id.editText_login);
        layout_chargement = (LinearLayout) findViewById(R.id.layout_chargement);


        //Initialisation variables
        table_user_online = new Table_user_online(this);
        activity = this;

        //Listener sur bouton
        button_suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                identifiant = editText_login.getText().toString();
                new Chargement().execute();

            }
        });
    }


    private class Chargement extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setTitle("Veuillez patienter");
            mProgressDialog.setMessage("Chargement en cours...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            if(table_user_online.utilisateur_present(identifiant)){
                identifiant_ok=true;
            }else{//nexiste pas
                identifiant_ok = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(identifiant_ok ==true){
                Intent intent = new Intent(Activity_identifiant.this, Activity_mot_de_passe.class);
                Bundle objetbunble = new Bundle();
                objetbunble.putString("identifiant", identifiant);
                intent.putExtras(objetbunble);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                finish();
                mProgressDialog.hide();
            }else{
                mProgressDialog.hide();
                info_dialog("L'identifiant n'existe pas");
            }

        }

    }

    private void info_dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_identifiant.this);
        builder.setMessage(message)
                .setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
    }
}

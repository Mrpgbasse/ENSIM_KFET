package com.daumont.ensim.kfet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.daumont.ensim.kfet.R;
import com.daumont.ensim.kfet.database.Table_user_online;
import com.daumont.ensim.kfet.modele.User;

/**
 * JONATHAN DAUMONT
 * PERMET DE VERIFIER SI LE MOTE DE PASSE DE L'IDENTIFIANT
 */
public class Activity_mot_de_passe extends AppCompatActivity {

    /**
     * ATTRIBUTS
     */
    //elements visuels
    private Button button_connect;
    private EditText editText_mot_de_passe;
    private TextView textView_identifiant;
    private LinearLayout layout_chargement;
    private ProgressDialog mProgressDialog;
    //BDD
    private Table_user_online table_user_online;

    private String identifiant,mdp;
    private int nb_essai_mot_de_passe = 0;
    private int id_user;
    private Activity activity;

    /**
     * Création de l'activité
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Recuperation des elements visuels
        setContentView(R.layout.activity_mot_de_passe);
        button_connect = (Button) findViewById(R.id.button_connect);
        editText_mot_de_passe = (EditText) findViewById(R.id.editText_mot_de_passe);
        textView_identifiant = (TextView) findViewById(R.id.textView_identifiant);
        layout_chargement = (LinearLayout) findViewById(R.id.layout_chargement);

        //Initialisation variables
        table_user_online = new Table_user_online(this);
        activity = this;
        //Recuperation parametres
        Bundle objetbunble = this.getIntent().getExtras();
        if (objetbunble != null) {
            identifiant = objetbunble.getString("identifiant");
            textView_identifiant.setText("Bienvenue " + identifiant);
        } else {
            retour();
        }

        //Listener sur bouton
        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdp =  editText_mot_de_passe.getText().toString();
                new Chargement().execute();
            }
        });
    }

    public void retour() {
        Intent intent = new Intent(Activity_mot_de_passe.this, Activity_identifiant.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_return, R.anim.push_out_return);
        finish();
    }

    /**
     * Detection de la touche retour
     */
    @Override
    public void onBackPressed() {
        retour();
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
            id_user = table_user_online.connexion_user(identifiant, mdp);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //Recuperation de l'id ou non de l'utilisateur

            if (id_user != -1) {//si on recupere un id pour l'utilisateur
                mProgressDialog.hide();
                User user = table_user_online.get_user(id_user);
                if (user.getType().equals("admin")) {//on charge une session admi
                    Intent intent = new Intent(Activity_mot_de_passe.this, Activity_administrateur.class);
                    Bundle objetbunble = new Bundle();
                    //On passe en parametre l'id utilisateur
                    objetbunble.putString("id_user", "" + id_user);
                    objetbunble.putString("notification", "yes");
                    intent.putExtras(objetbunble);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                    finish();
                } else {//on charge une session classique
                    Intent intent = new Intent(Activity_mot_de_passe.this, Activity_utilisateur.class);
                    //On passe en parametre l'id utilisateur
                    Bundle objetbunble = new Bundle();
                    objetbunble.putString("id_user", "" + id_user);
                    objetbunble.putString("notification", "yes");
                    intent.putExtras(objetbunble);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                    finish();
                }
            } else {//n'exisste pas on incremente le nombre d'essai
                mProgressDialog.hide();
                nb_essai_mot_de_passe++;
                info_dialog("Mot de passe incorect");
                if (nb_essai_mot_de_passe > 3) {
                    retour();
                }
            }

        }

    }

    private void info_dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_mot_de_passe.this);
        builder.setMessage(message)
                .setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
    }
}

package com.daumont.ensim.kfet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.daumont.ensim.kfet.modele.Emprunt;
import com.daumont.ensim.kfet.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * JONATHAN DAUMONT
 * Menu principal pour un admin
 * permet de voir les albums de la CDTheque et de la modifier
 * permet de voir les utilisateurs et de les modifier
 */
public class Activity_administrateur extends AppCompatActivity {

    /**
     * Declaration variables
     */
    //Elements graphiques
    public ListView listView;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private LinearLayout linearLayout_menu;
    private Button button_emprunter_un_cd, button_rendre_cd, button_ajouter_cd, button_voir_demande_emprunt;
    private AlertDialog alertDialog;
    private ProgressDialog mProgressDialog;

    //tableaux & lists
    private HashMap<String, String> map_user, map_cd, map_cd_user, map_demande_emprunt;
    private ArrayList<HashMap<String, String>> listItem_cd = new ArrayList<>();
    private ArrayList<HashMap<String, String>> listItem_user = new ArrayList<>();
    private ArrayList<HashMap<String, String>> listItem_cd_user = new ArrayList<>();
    private ArrayList<HashMap<String, String>> listItem_demande_emprunt = new ArrayList<>();
    private ArrayList<Emprunt> list_demande_emprunt;


    //base de données

    //autres
    private int position_vue;
    private String string_id_user;
    private Activity activity;
    private String etat_notif;


    /**
     * Listener sur le bouton d'onglet
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            listView.setAdapter(null);
            switch (item.getItemId()) {
                case R.id.navigation_cdtheque:
                    load_cdtheque();
                    return true;
                case R.id.navigation_list_user:
                    load_user();
                    return true;
                case R.id.navigation_mon_profil:
                    load_profil();
                    return true;
                case R.id.navigation_mes_cd:
                    mes_cd();
                    return true;
            }
            return false;
        }

    };

    /**
     * Création de l'activité
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Recuperation des elements visuels
        setContentView(R.layout.activity_administrateur);
        listView = (ListView) findViewById(R.id.listView);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_admin);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fab = (FloatingActionButton) findViewById(R.id.fab_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        linearLayout_menu = (LinearLayout) findViewById(R.id.linearLayout_menu);
        button_emprunter_un_cd = (Button) findViewById(R.id.button_emprunter_un_cd);
        button_rendre_cd = (Button) findViewById(R.id.button_rendre_cd);
        button_ajouter_cd = (Button) findViewById(R.id.button_ajouter_cd);
        button_voir_demande_emprunt = (Button) findViewById(R.id.button_voir_demande_emprunt);
        toolbar.inflateMenu(R.menu.menu_profil);


        //Recuperation parametres
        Bundle objetbunble = this.getIntent().getExtras();
        if (objetbunble != null) {
            string_id_user = objetbunble.getString("id_user");
            if (objetbunble.getString("notification") != null) {
                etat_notif = objetbunble.getString("notification");
            } else {
                etat_notif = "rien";
            }
        }

        //Initialisation bdd

        //Initialisation variables
        listView.setNestedScrollingEnabled(true);
        linearLayout_menu.setNestedScrollingEnabled(true);
        activity = this;
        list_demande_emprunt = new ArrayList<>();
        listItem_cd_user = new ArrayList<>();

        new Chargement().execute();






        //Listener sur FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position_vue == 2 || position_vue == 3) {
                   /* Intent i = new Intent(Activity_administrateur.this, Activity_rechercher_artiste.class);
                    Bundle objetbunble = new Bundle();
                    objetbunble.putString("id_user", string_id_user);
                    i.putExtras(objetbunble);
                    Activity_administrateur.this.startActivity(i);
                    overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                    finish();*/
                } else {
                    if (position_vue == 4) {
                        /*Intent i = new Intent(Activity_administrateur.this, Activity_ajouter_utilisateur.class);
                        Bundle objetbunble = new Bundle();
                        objetbunble.putString("id_user", string_id_user);
                        i.putExtras(objetbunble);
                        Activity_administrateur.this.startActivity(i);
                        overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                        finish();*/
                    } else {
                        affichage_demande_emprunt();
                    }
                }
            }
        });

        //Listener sur la listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                HashMap<String, String> map = (HashMap<String, String>) listView
                        .getItemAtPosition(position);
                if(!map.get("id").equals("null")){
                    if (position_vue == 2 || position_vue == 3) {
                       /* Intent i = new Intent(Activity_administrateur.this, Activity_details_cd.class);
                        Bundle objetbunble = new Bundle();
                        objetbunble.putString("id_cd", map.get("id"));
                        objetbunble.putString("id_user", "" + string_id_user);
                        i.putExtras(objetbunble);
                        Activity_administrateur.this.startActivity(i);
                        overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                        finish();*/
                    } else {
                        /*Intent i = new Intent(Activity_administrateur.this, Activity_details_utilisateur.class);
                        Bundle objetbunble = new Bundle();
                        objetbunble.putString("id_user_select", "" + map.get("id"));
                        objetbunble.putString("id_user", "" + string_id_user);
                        i.putExtras(objetbunble);
                        Activity_administrateur.this.startActivity(i);
                        overridePendingTransition(R.anim.pull_in, R.anim.push_out);*/
                    }
                }

            }

        });

        //LISTENER sur le menu haut/droit
        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_modifier_mot_de_passe:
                       /* Intent i = new Intent(Activity_administrateur.this, Activity_modifier_profil.class);
                        Bundle objetbunble = new Bundle();
                        objetbunble.putString("id_user", string_id_user);
                        i.putExtras(objetbunble);
                        Activity_administrateur.this.startActivity(i);
                        overridePendingTransition(R.anim.pull_in, R.anim.push_out);*/
                        return true;
                    default:
                        return false;
                }
            }
        });



        //LISTENER BOUTONS
        button_emprunter_un_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(Activity_administrateur.this, Activity_emprunter_cd.class);
                Bundle objetbunble = new Bundle();
                objetbunble.putString("id_user", string_id_user);
                i.putExtras(objetbunble);
                Activity_administrateur.this.startActivity(i);
                overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                finish();*/
            }
        });
        button_rendre_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Veuillez scanner l'album");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
        button_ajouter_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(Activity_administrateur.this, Activity_rechercher_artiste.class);
                Bundle objetbunble = new Bundle();
                objetbunble.putString("id_user", string_id_user);
                i.putExtras(objetbunble);
                Activity_administrateur.this.startActivity(i);
                overridePendingTransition(R.anim.pull_in, R.anim.push_out);*/
            }
        });
        button_voir_demande_emprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                affichage_demande_emprunt();
            }
        });
    }

    /**
     * Permet de charger la vue de l'onglet utilisateurs
     */
    public void load_user() {
        position_vue = 4;
        listView.setVisibility(View.VISIBLE);
        linearLayout_menu.setVisibility(View.GONE);
        fab.setImageResource(R.drawable.icon_add_user);

        SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(),
                listItem_user, R.layout.layout_user, new String[]{"info"}, new int[]{R.id.textView_info_user}) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return view;
            }
        };
        listView.setAdapter(mSchedule);
    }


    /**
     * Permet de charger la vue de l'onglet CDTheque
     */
    public void load_cdtheque() {
        fab.setImageResource(R.drawable.icon_disk_add);
        position_vue = 3;
        listView.setVisibility(View.VISIBLE);
        linearLayout_menu.setVisibility(View.GONE);
        fab.setImageResource(R.drawable.icon_disk_add);
        SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(),
                listItem_cd, R.layout.layout_cd, new String[]{"info"}, new int[]{R.id.textView_info_cd}) {
            public View getView(int position, View convertView, ViewGroup parent) {
                HashMap<String, String> map = (HashMap<String, String>) listView
                        .getItemAtPosition(position);
                final String url_image = map.get("image");
                View view = super.getView(position, convertView, parent);
                ImageView image_view_cd = (ImageView) view.findViewById(R.id.image_view_cd);
                if(map.get("id").equals("null")){
                    image_view_cd.setVisibility(View.GONE);
                }
                Picasso.with(image_view_cd.getContext()).load(url_image).centerCrop().fit().into(image_view_cd);
                return view;
            }
        };
        listView.setAdapter(mSchedule);
    }

    /**
     * Charge la list des CD emprunter
     */
    public void mes_cd() {
        fab.setImageResource(R.drawable.icon_disk_add);
        position_vue = 2;
        listView.setVisibility(View.VISIBLE);
        linearLayout_menu.setVisibility(View.GONE);
        SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(),
                listItem_cd_user, R.layout.layout_cd, new String[]{"info"}, new int[]{R.id.textView_info_cd}) {
            public View getView(int position, View convertView, ViewGroup parent) {
                HashMap<String, String> map = (HashMap<String, String>) listView
                        .getItemAtPosition(position);
                final String url_image = map.get("image");
                View view = super.getView(position, convertView, parent);
                ImageView image_view_cd = (ImageView) view.findViewById(R.id.image_view_cd);
                if(map.get("id").equals("null")){
                    image_view_cd.setVisibility(View.GONE);
                }
                Picasso.with(image_view_cd.getContext()).load(url_image).centerCrop().fit().into(image_view_cd);
                return view;
            }
        };
        listView.setAdapter(mSchedule);
    }

    /**
     * Permet de charger la vue profil de l'admin
     */
    public void load_profil() {
        fab.setImageResource(R.drawable.icon_info);
        position_vue = 1;
        listView.setVisibility(View.GONE);
        linearLayout_menu.setVisibility(View.VISIBLE);
    }

    /**
     * Gestion du bouton retour
     */
    @Override
    public void onBackPressed() {
        retour();
    }

    public void retour() {
        /**ON DEMANDE CONFIRMATION*****************************************/
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_administrateur.this);
        builder.setMessage("Vous allez être déconnecté. Voulez-vous continuer ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*Intent intent = new Intent(Activity_administrateur.this, Activity_identifiant.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.pull_in_return, R.anim.push_out_return);
                        finish();*/
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
        builder.show();
    }

    public void affichage_demande_emprunt() {


    }

    /**
     * Afficher un dialog de confirmation
     *
     * @param p_id_emprunt
     * @param alertDialog
     */
    public void confirmation(final String p_id_emprunt, final AlertDialog alertDialog) {

    }

    /**
     * Afficher un dialog d'informations
     *
     * @param message
     */
    private void info_dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_administrateur.this);
        builder.setMessage(message)
                .setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
    }

    /**
     * Methodes pour le QRCODE
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

                info_dialog("Aucun QRCode détecté");
            } else {


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            //TODO mettre dans la base de données


        }
    }

    private class Chargement extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setTitle("Veuillez patienter");
            mProgressDialog.setMessage("Chargement en cours...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {



          /*  user = table_user_online.get_user(Integer.parseInt(string_id_user));
            list_cd_utilisateur = table_cd_online.list_cd_utilistaeur(Integer.parseInt(string_id_user));
            list_demande_emprunt = table_emprunt.demande_emprunt(Integer.parseInt(string_id_user));
            list_user = table_user_online.list_user();
            list_cd = table_cd_online.list_cd();*/
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
           /* mProgressDialog.hide();
            toolbar.setTitle("Bonjour " + user.getIdentifiant());
            //Génération des list d'items pour les différentes vues
            for (int i = 0; i < list_user.size(); i++) {
                map_user = new HashMap<>();
                map_user.put("id", "" + list_user.get(i).getId_user());
                map_user.put("info", list_user.get(i).getNom() + "\n" + list_user.get(i).getPrenom());//champ id
                listItem_user.add(map_user);
            }

            if( list_cd.size()>0){
                for (int i = 0; i < list_cd.size(); i++) {
                    map_cd = new HashMap<>();
                    map_cd.put("id", "" + list_cd.get(i).getId_cd());
                    map_cd.put("info", list_cd.get(i).getNom_artist() + "\n" + list_cd.get(i).getNom_album());//champ id
                    map_cd.put("image", list_cd.get(i).getImage());//champ id
                    listItem_cd.add(map_cd);
                }
            }else{
                map_cd = new HashMap<>();
                map_cd.put("id", "null");
                map_cd.put("info","Il n'y a aucun CD");//champ id
                listItem_cd.add(map_cd);
            }

            if(list_cd_utilisateur.size()>0){
                for (int i = 0; i < list_cd_utilisateur.size(); i++) {
                    map_cd_user = new HashMap<>();
                    map_cd_user.put("id", "" + list_cd_utilisateur.get(i).getId_cd());
                    map_cd_user.put("info", list_cd_utilisateur.get(i).getNom_artist() + "\n" + list_cd_utilisateur.get(i).getNom_album());//champ id
                    map_cd_user.put("image", list_cd_utilisateur.get(i).getImage());//champ id
                    listItem_cd_user.add(map_cd_user);
                }
            }else{
                map_cd = new HashMap<>();
                map_cd.put("id", "null");
                map_cd.put("info","Il n'y a aucun CD");//champ id
                listItem_cd_user.add(map_cd);
            }

            for (int i = 0; i < list_demande_emprunt.size(); i++) {
                map_demande_emprunt = new HashMap<>();
                map_demande_emprunt.put("id", "" + list_demande_emprunt.get(i).getId_emprunt());
                User user_demandeur = table_user_online.get_user(list_demande_emprunt.get(i).getId_emprunteur());
                CD cd_tmp = table_cd_online.get_cd(list_demande_emprunt.get(i).getQr_code());
                map_demande_emprunt.put("titre", cd_tmp.getNom_artist() + " - " + cd_tmp.getNom_album());
                map_demande_emprunt.put("demandeur", "Demande par " + user_demandeur.getIdentifiant());
                map_demande_emprunt.put("image", cd_tmp.getImage());
                listItem_demande_emprunt.add(map_demande_emprunt);
            }


            load_profil();//chargement de la première vue

            //on regarde si l'application vient d'etre lancer pour afficher ou non les demandes d'emprunt
            if (!etat_notif.equals("rien")) {
                if (list_demande_emprunt.size() != 0) {
                    affichage_demande_emprunt();
                }

            }*/
        }

    }

}

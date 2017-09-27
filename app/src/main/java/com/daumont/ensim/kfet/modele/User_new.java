package com.daumont.ensim.kfet.modele;

/**
 * Created by jojo- on 13/09/2017.
 */

public class User_new {

    private String nom;
    private String num_etu;
    private String rang;
    private String mdp;
    private float credit;

    //Constructor
    public User_new(String nom,String num_etu,String rang,float credit,String mdp){
        this.nom=nom;
        this.num_etu = num_etu;
        this.rang = rang;
        this.mdp = mdp;
        this.credit = credit;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNum_etu() {
        return num_etu;
    }

    public void setNum_etu(String num_etu) {
        this.num_etu = num_etu;
    }

    public String getRang() {
        return rang;
    }

    public void setRang(String rang) {
        this.rang = rang;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
}

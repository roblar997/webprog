package com.s349967.oblig3s349967.model;

public class LoginData {

    private String brukernavn;
    private String passord;

    public LoginData(String brukernavn, String passord){
        this.brukernavn = brukernavn;
        this.passord    = passord;
    }
    public LoginData(){

    }


    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }
}

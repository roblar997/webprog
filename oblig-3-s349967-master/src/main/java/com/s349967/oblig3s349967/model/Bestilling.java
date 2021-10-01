package com.s349967.oblig3s349967.model;

import java.io.Serializable;
import java.util.Objects;

public class Bestilling implements Serializable {


    private String filmValg;
    private Integer antall;
    private String fornavn;
    private String etternavn;
    private String telefon;
    private String epost;

    public Bestilling(String filmValg, Integer antall, String fornavn, String etternavn, String telefon, String epost){
        this.filmValg   = filmValg;
        this.antall     = antall;
        this.fornavn    = fornavn;
        this.etternavn  = etternavn;
        this.telefon    = telefon;
        this.epost      = epost;
    }
    public Bestilling(){

    }


    public String getFilmValg() {
        return filmValg;
    }

    public void setFilmValg(String filmValg) {
        this.filmValg = filmValg;
    }

    public Integer getAntall() {
        return antall;
    }

    public void setAntall(Integer antall) {
        this.antall = antall;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }



    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bestilling)) return false;
        Bestilling that = (Bestilling) o;
        return Objects.equals(filmValg, that.filmValg) && Objects.equals(antall, that.antall) && Objects.equals(fornavn, that.fornavn) && Objects.equals(etternavn, that.etternavn) && Objects.equals(telefon, that.telefon) && Objects.equals(epost, that.epost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filmValg, antall, fornavn, etternavn, telefon, epost);
    }
}

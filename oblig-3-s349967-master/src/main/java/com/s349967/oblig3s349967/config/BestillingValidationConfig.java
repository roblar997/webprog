package com.s349967.oblig3s349967.config;
//
public class BestillingValidationConfig {

    private static String  patternAntall = "^[1-9][0-9]*{4,}$";  //Å bestill 0 antall filmer gir ikke mening.
    private static Boolean  requiredAntall = true;

    private static Integer  antRunderBcryptSalt = 15;

    public static Integer getAntRunderBcryptSalt(){
        return antRunderBcryptSalt;
    }
    public static void setAntRunderBcryptSalt(Integer antRunderBcryptSaltL){
        antRunderBcryptSalt = antRunderBcryptSaltL;
    }
    private static Boolean  requiredFornavn = true;
    private static String  patternFornavn = "^[a-zA-Z]+{1,15}$";


    public static String getPatternAntall() {
        return patternAntall;
    }

    private static  Boolean requiredEtternavn = true;
    private static  String patternEtternavn  = "^[a-zA-Z]+{1,15}$";
    private static  String patternEpost  = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(([a-zA-Z]+)|([a-zA-z0-9]+[a-zA-z]+)|([a-zA-z]+[a-zA-Z0-9]+))((.|[a-zA-Z0-9-]+)([a-zA-Z0-9])+)*{3,25}$";


    private static  Boolean requiredTelefon = true;
    private static  String patternTelefon  = "^(\\+[0-9]+)?[0-9]+{5,15}$";

    public static void setPatternAntall(String patternAntallL) {
        patternAntall = patternAntallL;
    }

    public static Boolean getRequiredAntall() {
        return requiredAntall;
    }

    public static void setRequiredAntall(Boolean requiredAntallL) {
        requiredAntall = requiredAntallL;
    }

    public static  Boolean getRequiredFornavn() {
        return requiredFornavn;
    }

    public static  void setRequiredFornavn(Boolean requiredFornavnL) {
        requiredFornavn = requiredFornavnL;
    }

    public static String getPatternFornavn() {
        return patternFornavn;
    }

    public static void setPatternFornavn(String patternFornavnL) {
        patternFornavn = patternFornavnL;
    }

    public static Boolean getRequiredEtternavn() {
        return requiredEtternavn;
    }

    public static void setRequiredEtternavn(Boolean requiredEtternavnL) {
        requiredEtternavn = requiredEtternavnL;
    }

    public static String getPatternEtternavn() {
        return patternEtternavn;
    }

    public static void setPatternEtternavn(String patternEtternavnL) {
        patternEtternavn = patternEtternavnL;
    }

    public static Boolean getRequiredEpost() {
        return requiredEpost;
    }

    public static void setRequiredEpost(Boolean requiredEpostL) {
       requiredEpost = requiredEpostL;
    }

    public static Boolean getRequiredTelefon() {
        return requiredTelefon;
    }

    public static void setRequiredTelefon(Boolean requiredTelefonL) {
        requiredTelefon = requiredTelefonL;
    }

    public static String getPatternTelefon() {
        return patternTelefon;
    }

    public static void setPatternTelefon(String patternTelefonL) {
        patternTelefon = patternTelefonL;
    }




    private static Boolean requiredEpost = true;

    static public String getPatternEpost() {
        return patternEpost;
    }

    static  public void setPatternEpost(String patternEpostL) {
         patternEpost = patternEpostL;
    }



}

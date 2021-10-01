package com.s349967.oblig3s349967.ServiceInterfaces;

import org.mindrot.jbcrypt.BCrypt;

public interface IKryptoService {

    public boolean kryptoCompare(String passord, String hashPassord);

    public String genererPassord(String passord,int antRunder);
}

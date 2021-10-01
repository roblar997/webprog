package com.s349967.oblig3s349967.RepositoryInterfaces;

import com.s349967.oblig3s349967.model.LoginData;
import org.springframework.stereotype.Component;

@Component
public interface IBrukerRepository {


    public String hentHashedDbPassord(String brukernavn);

    /**
     * Registrer en bruker
     * @param loginData
     */
    public boolean registrerBruker(LoginData loginData);
}

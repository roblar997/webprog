package com.s349967.oblig3s349967.DAOInterfaces;

import com.s349967.oblig3s349967.model.LoginData;
import org.springframework.stereotype.Component;

@Component
public interface IBrukerDAO {

    /**
     * valider bruker
     * @param loginData
     * @return
     */
    public String hentHashedDbPassord(String brukernavn);

    /**
     * Registrer en bruker
     * @param loginData
     */
    public void registrerBruker(LoginData loginData);
}


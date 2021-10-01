package com.s349967.oblig3s349967.repository;

import com.s349967.oblig3s349967.DAOInterfaces.IBrukerDAO;
import com.s349967.oblig3s349967.DAOInterfaces.IFilmDAO;
import com.s349967.oblig3s349967.RepositoryInterfaces.IBrukerRepository;
import com.s349967.oblig3s349967.model.LoginData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BrukerRepository implements IBrukerRepository {

    @Autowired
    private IBrukerDAO brukerDAO;

    @Override
    public String hentHashedDbPassord(String brukernavn) {

        try {
            String result = brukerDAO.hentHashedDbPassord(brukernavn);
            return result;
        }
        catch (Exception ex){
            return null;
        }

    }

    @Override
    public boolean registrerBruker(LoginData loginData) {
        try {
            brukerDAO.registrerBruker(loginData);
            return true;
        }
        catch(Exception ex) {
                return false;
        }
    }
}

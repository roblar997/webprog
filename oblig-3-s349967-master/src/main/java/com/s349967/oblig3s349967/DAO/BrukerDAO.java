package com.s349967.oblig3s349967.DAO;

import com.s349967.oblig3s349967.DAOInterfaces.IBrukerDAO;
import com.s349967.oblig3s349967.model.LoginData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BrukerDAO implements IBrukerDAO {

    @Autowired
    private JdbcTemplate db;

    @Override
    public String hentHashedDbPassord(String brukernavn) {
        String sql = " SELECT passord FROM main.bruker WHERE brukernavn=?";
        String hashedPassord = db.queryForObject(sql,String.class,brukernavn);
        return hashedPassord;
    }

    @Override
    public void registrerBruker(LoginData loginData) {
        String sql = " INSERT INTO main.bruker (brukernavn,passord) VALUES(?,?)";
        db.update(sql,loginData.getBrukernavn(),loginData.getPassord());
    }
}

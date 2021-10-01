package com.s349967.oblig3s349967.DAO;

import com.s349967.oblig3s349967.DAOInterfaces.IBestillingDAO;
import com.s349967.oblig3s349967.model.Bestilling;
import com.s349967.oblig3s349967.model.BestillingDbModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class BestillingDAO implements IBestillingDAO {
    @Autowired
    private JdbcTemplate db;


    public void createSchemaInstance(String schema) {

          String sql = "CREATE SCHEMA " + schema;
          db.update(sql);

          sql = "CREATE TABLE  " + schema + ".bestilling  " +
                  "(id serial primary key," +
                  "filmvalg varchar(255)," +
                  "antall int," +
                  "fornavn varchar(255)," +
                  "etternavn varchar(255),"+
                  "telefon varchar(255)," +
                  "epost varchar(255))";

          db.update(sql);

    }

    //
    public Integer addBestilling(Bestilling bestilling, String dbToUse){

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO " + dbToUse + ".bestilling (filmValg,antall, fornavn, etternavn,telefon,epost) VALUES(?,?,?,?,?,?)";

        db.update(con -> {
             PreparedStatement query = con.prepareStatement(sql, new String[]{"id"});
             query.setString(1, bestilling.getFilmValg() );
             query.setInt(   2, bestilling.getAntall());
             query.setString(3, bestilling.getFornavn());
             query.setString(4, bestilling.getEtternavn());
             query.setString(5,bestilling.getTelefon());
             query.setString(6,bestilling.getEpost());
             return query;
        },keyHolder);


       return  keyHolder.getKey().intValue();

    }

    public List<BestillingDbModel> getBestillinger(String dbToUse){
        String sql =  "SELECT * FROM " + dbToUse + ".bestilling";
        List<BestillingDbModel> bestillinger = db.query(sql, new BeanPropertyRowMapper(BestillingDbModel.class));
        return bestillinger;
    }
    public void slettBestillinger(String dbToUse){
        String sql= "DELETE FROM " + dbToUse + ".bestilling";
        db.update(sql);
    }

    public void slettBestilling(BestillingDbModel bestilling,String dbToUse){
        String sql = "DELETE FROM "  + dbToUse +  ".bestilling WHERE id = ?";
        db.update(sql,bestilling.getId());

    }


    public void endreBestilling(Integer id, Bestilling bestilling, String dbToUse){
        String sql = "UPDATE " + dbToUse + ".bestilling SET filmValg=?,antall=?, fornavn=?,etternavn=?,telefon = ?, epost=? WHERE id=?";
        db.update(sql,bestilling.getFilmValg(),bestilling.getAntall(),bestilling.getFornavn(),bestilling.getEtternavn(),
                bestilling.getTelefon(),bestilling.getEpost(),id);
    }
}

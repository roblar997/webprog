package com.s349967.oblig3s349967.DAO;

import com.s349967.oblig3s349967.DAOInterfaces.IFilmDAO;
import com.s349967.oblig3s349967.model.Film;
import com.s349967.oblig3s349967.model.FilmDbModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilmDAO implements IFilmDAO{
    @Autowired
    private JdbcTemplate db;

    public List<Film> getFilmValg(){

        String sql =  "SELECT * FROM main.film";
        List<Film> filmer = db.query(sql, new BeanPropertyRowMapper(FilmDbModel.class));

        return filmer;
    }
}

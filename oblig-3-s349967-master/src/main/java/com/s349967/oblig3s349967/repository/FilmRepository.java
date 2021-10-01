package com.s349967.oblig3s349967.repository;

import com.s349967.oblig3s349967.DAOInterfaces.IFilmDAO;
import com.s349967.oblig3s349967.RepositoryInterfaces.IFilmRepository;
import com.s349967.oblig3s349967.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FilmRepository implements IFilmRepository {

    private ArrayList<Film> filmValg;

    @Autowired
    private IFilmDAO filmDAO;

    @PostConstruct
    public void init(){

        filmValg  = (ArrayList<Film>) filmDAO.getFilmValg();



    }
//
    public List<Film> getFilmValg() {
        return this.filmValg;
    }
}

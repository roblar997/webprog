package com.s349967.oblig3s349967.service;

import com.s349967.oblig3s349967.RepositoryInterfaces.IFilmRepository;
import com.s349967.oblig3s349967.ServiceInterfaces.IFilmService;
import com.s349967.oblig3s349967.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService implements IFilmService{

    @Autowired
    IFilmRepository filmRepository;

    public List<Film> getFilmValg(){
        return filmRepository.getFilmValg();
    }

}

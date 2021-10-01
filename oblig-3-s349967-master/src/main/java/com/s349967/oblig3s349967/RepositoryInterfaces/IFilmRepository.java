package com.s349967.oblig3s349967.RepositoryInterfaces;

import com.s349967.oblig3s349967.model.Film;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
/**
 * Lagrings plass for filmer
 */
public interface IFilmRepository{

    /**
     * Hent liste over filmer
     * @return
     */
    public List<Film> getFilmValg();
}

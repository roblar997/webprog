package com.s349967.oblig3s349967.DAOInterfaces;

import com.s349967.oblig3s349967.model.Film;

import java.util.List;


/**
 * Interface for DAO som snakker med databasen angående filmer
 */
public interface IFilmDAO {

    /**
     * Henter alle filmer
     * @return alle filmer
     */
    public List<Film> getFilmValg();
}

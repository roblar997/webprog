package com.s349967.oblig3s349967.ServiceInterfaces;

import com.s349967.oblig3s349967.RepositoryInterfaces.IFilmRepository;
import com.s349967.oblig3s349967.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
/**
 * Service for håndtering av film data
 */
public interface IFilmService {

    /**
     * Hent alle filmer
     * @return
     */
    public List<Film> getFilmValg();
}

package com.s349967.oblig3s349967.model;

import java.util.Objects;

public class FilmDbModel extends Film{

    private Integer id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilmDbModel)) return false;
        FilmDbModel that = (FilmDbModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public FilmDbModel(Integer id, String filmTittel){
        super(filmTittel);
        this.id         = id;

    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public FilmDbModel(){

    }
}

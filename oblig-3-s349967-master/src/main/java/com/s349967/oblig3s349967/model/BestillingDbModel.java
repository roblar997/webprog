package com.s349967.oblig3s349967.model;

import java.io.Serializable;
import java.util.Objects;

public class BestillingDbModel extends Bestilling implements Serializable {

    Integer id;

    public BestillingDbModel(Integer id, String filmValg, Integer antall, String fornavn, String etternavn, String telefon, String epost){
        super(filmValg,antall,fornavn,etternavn,telefon,epost);
        this.id = id;
    }
    public BestillingDbModel(){

    }
    public Integer getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BestillingDbModel)) return false;
        if (!super.equals(o)) return false;
        BestillingDbModel that = (BestillingDbModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    public void setId(Integer id){
        this.id = id;
    }
}

package com.s349967.oblig3s349967.DAOInterfaces;

import com.s349967.oblig3s349967.model.Bestilling;
import com.s349967.oblig3s349967.model.BestillingDbModel;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Interface for DAO som snakker med databasen angående bestillinger
 */
@Component
public interface IBestillingDAO {

    public void createSchemaInstance(String schema);
    /**
     * Lgger til en bestilling
     * @param bestilling
     * @return
     */
    public Integer addBestilling(Bestilling bestilling, String dbToUse);

    /**
     * Henter bestillinger
     * @return
     */
    public List<BestillingDbModel> getBestillinger(String dbToUse);

    /**
     * Sletter alle bestillinger
     */
    public void slettBestillinger(String dbToUse);

    /**
     * Slette en bestilling
     * @param bestillingDbModel
     */
    public void slettBestilling(BestillingDbModel bestillingDbModel, String dbToUse);

    /**
     * Endre en bestilling git en id.
     * @param id
     * @param bestilling
     */
    public void endreBestilling(Integer id, Bestilling bestilling, String dbToUse);
}

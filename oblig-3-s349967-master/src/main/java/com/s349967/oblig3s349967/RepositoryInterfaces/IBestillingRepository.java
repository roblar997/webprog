package com.s349967.oblig3s349967.RepositoryInterfaces;

import com.s349967.oblig3s349967.model.Bestilling;
import com.s349967.oblig3s349967.model.BestillingDbModel;
import com.s349967.oblig3s349967.model.SortingStrategyModel;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public interface IBestillingRepository {


    /**
     * Henter variabel med variabler som definerer hvordan bestillingsdataene skal holdes sortert
     * på web-serveren (ikke på database serveren)
     * @return
     */
    public SortingStrategyModel getSortingStrategyModel();
    public void createSchemaInstance(String schema);
    /**
     * Henter alle bestillingsdataene som er på serveren
     * @return liste over bestillingsdataene
     */
    public List<BestillingDbModel> getBestillingStorage(String schemaToUse);

    /**
     * Legg til en bestilling
     * @param bestilling
     * @return
     * @throws SQLException
     */
    public BestillingDbModel addDbBestilling(Bestilling bestilling,String schemaToUse) throws SQLException;

    /**
     * Hent, hvis mulig, en BestillingDbModell < Bestilling, hvor denne
     * BestillingDbModell kan kastes til en bestilling lik den i funksjonkallet.
     * @param bestilling
     * @return
     */
    public Optional<BestillingDbModel> findFirst(Bestilling  bestilling,String schemaToUse);

    /**
     * Slett en bestilling
     * @param bestilling
     * @return
     * @throws SQLException
     */
    public Optional<BestillingDbModel> slettDbBestilling(Bestilling  bestilling, String schemaToUse) throws SQLException;

    /**
     *  Endre en bestilling
     *
     * @param toFind
     * @param postBestilling
     * @return
     * @throws SQLException
     */
    public Optional<BestillingDbModel> endreDbBestilling(Bestilling  toFind, Bestilling  postBestilling,String schemaToUse) throws SQLException;

    /**
     * Slett alle bestillinger
     * @throws SQLException
     */
    public void slettDbBestillinger(String schmeaToUse) throws SQLException;
}

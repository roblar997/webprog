package com.s349967.oblig3s349967.repository;

import com.s349967.oblig3s349967.DAOInterfaces.IBestillingDAO;
import com.s349967.oblig3s349967.RepositoryInterfaces.IBestillingRepository;
import com.s349967.oblig3s349967.model.Bestilling;
import com.s349967.oblig3s349967.model.BestillingDbModel;
import com.s349967.oblig3s349967.model.SortingStrategyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;


@Repository
public class BestillingRepository implements IBestillingRepository {

    private TreeMap<String,List<BestillingDbModel>> bestillingStorage;
    private final Logger repLogger = LoggerFactory.getLogger(BestillingRepository.class);


    @Autowired
    private IBestillingDAO bestillingDAO;

    private SortingStrategyModel sortingStrategyModel;


    public SortingStrategyModel getSortingStrategyModel(){
        return this.sortingStrategyModel;
    }
    @PostConstruct
    public void init() {
        bestillingStorage = new TreeMap<String,List<BestillingDbModel>>();
        sortingStrategyModel = new SortingStrategyModel(3,1);

    }

    public void createSchemaInstance(String schema){
        bestillingDAO.createSchemaInstance(schema);

    }

    public List<BestillingDbModel> getBestillingStorage(String schemaToUse) {
        if(!bestillingStorage.containsKey(schemaToUse))
            bestillingStorage.put(schemaToUse,bestillingDAO.getBestillinger(schemaToUse));

        return this.bestillingStorage.get(schemaToUse);
    }

    public BestillingDbModel addDbBestilling(Bestilling bestilling,String schemaToUse) throws SQLException {
        Integer id = null;
        try {
            id = bestillingDAO.addBestilling(bestilling,schemaToUse);
        } catch (Exception ex) {
            repLogger.error("Kunne ikke legge til bestilling i bestillingDAO" + ex);
            throw new SQLException("Kunne ikke legge til bestilling i bestillingDAO");
        }

        BestillingDbModel bestilling2 = new BestillingDbModel(id, bestilling.getFilmValg(), bestilling.getAntall(), bestilling.getFornavn(), bestilling.getEtternavn(), bestilling.getTelefon(), bestilling.getEpost());
        return bestilling2;

    }

    public Optional<BestillingDbModel> findFirst(Bestilling bestilling,String schemaToUse) {


        //Kan gjøre det slikt.
        return getBestillingStorage(schemaToUse).stream().filter((x) -> {
            return bestilling.equals(x);
        }).findFirst();
    }

    public Optional<BestillingDbModel> slettDbBestilling(Bestilling bestilling,String schemaToUse) throws SQLException {
        Optional<BestillingDbModel> bestillingDbModelOpt = this.findFirst(bestilling,schemaToUse);
        if (bestillingDbModelOpt.isPresent()) {
            BestillingDbModel bestillingDbModel = bestillingDbModelOpt.get();

            try {
                bestillingDAO.slettBestilling(bestillingDbModel,schemaToUse);
            } catch (Exception ex) {
                repLogger.error("Kunne utføre slett alle bestillinger i bestillingDAO" + ex);
                throw new SQLException("Kunne utføre slett alle bestillinger i bestillingDAO");
            }

        }
        return bestillingDbModelOpt;

    }

    public Optional<BestillingDbModel> endreDbBestilling(Bestilling toFind, Bestilling postBestilling,String schemaToUse) throws SQLException {
        Optional<BestillingDbModel> bestillingDbModelOpt = this.findFirst(toFind,schemaToUse);
        if (bestillingDbModelOpt.isPresent()) {
            BestillingDbModel bestillingDbModel = bestillingDbModelOpt.get();
            try {
                bestillingDAO.endreBestilling(bestillingDbModel.getId(), postBestilling,schemaToUse);
            } catch (Exception ex) {
                repLogger.error("Kunne ikke endre bestilling i bestillingDAO" + ex);
                throw new SQLException("Kunne ikke endre bestilling i bestillingDAO");
            }
        }

        return bestillingDbModelOpt;
    }

    public void slettDbBestillinger(String schemaToUse) throws SQLException {
        try {
            bestillingDAO.slettBestillinger(schemaToUse);
        } catch (Exception ex) {
            repLogger.error("Kunne ikke slette  bestilling i bestillingDAO" + ex);
            throw new SQLException("Kunne ikke slette  bestilling i bestillingDAO");
        }

    }
}
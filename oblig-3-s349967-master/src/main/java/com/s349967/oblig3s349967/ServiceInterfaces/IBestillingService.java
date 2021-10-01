package com.s349967.oblig3s349967.ServiceInterfaces;

import com.s349967.oblig3s349967.RepositoryInterfaces.IBestillingRepository;
import com.s349967.oblig3s349967.RepositoryInterfaces.ITimeRepository;
import com.s349967.oblig3s349967.model.Bestilling;
import com.s349967.oblig3s349967.model.BestillingDbModel;
import com.s349967.oblig3s349967.model.ChangeTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
/**
 * Håndtering og manipulering av bestillings data
 */
public interface IBestillingService {
    public void createSchemaInstance(String schema);
    public void sorterCurrentBestillingerWrapper(String schemaToUse);
    /**
     * Hent alle bestillinger
     * @return
     */
    public List<BestillingDbModel> getBestillingStorage(String schemaToUse);

    /**
     * Hent en sub-del av alle bestillinger fra en liste, gitt øvre og nedre grense
     * @param from
     * @param to
     * @return
     */
    public List<Bestilling> getSubBestillinger(int from, int to,String schemaToUse);

    /**
     * Hent timestamp for endring i database
     * @return
     */
    public ChangeTime getChangeTime();

    /**
     * Slett en bestilling
     * @param bestilling
     * @return
     * @throws SQLException
     */
    public long slettBestilling(Bestilling bestilling,String schemaToUse) throws SQLException;

    /**
     * Sorter bestillinger, med strategi basert på variabler i funksjonskallet.
     * @param id
     * @param isDesc
     * @return
     */
    public long sorterBestillingerWrapper(int id, int isDesc,String schemaToUse);

    /**
     * Slett alle bestillinger
     * @return
     * @throws SQLException
     */
    public long slettBestillinger(String schemaToUse) throws SQLException;

    /**
     * Lagre en bestilling
     * @param bestilling
     * @return
     * @throws SQLException
     */
    public long lagreBestilling(Bestilling bestilling,String schemaToUse) throws SQLException;


    /**
     * Endre en bestilling gitt liste med 2 variabler - 1 som representerer
     * det som skal endres, og en som representerer ønsket tilstand.
     * @param bestillingsListe
     * @return
     * @throws SQLException
     */
    public long endreBestilling(List<Bestilling> bestillingsListe,String schemaToUse) throws SQLException;
}

package com.s349967.oblig3s349967.ServiceInterfaces;

import com.s349967.oblig3s349967.RepositoryInterfaces.IBrukerRepository;
import com.s349967.oblig3s349967.config.BestillingValidationConfig;
import com.s349967.oblig3s349967.model.Bestilling;
import com.s349967.oblig3s349967.model.LoginData;
import com.s349967.oblig3s349967.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
/**
 * Service som validerer data
 */
public interface IValidationService {



    /**
     * validerer en bruker
     * @param loginData
     * @return
     */
    public boolean validerBruker(LoginData loginData);

    /**
     * Registrer en bruker
     * @param loginData
     */
    public boolean    registrerBruker(LoginData loginData);
    /**
     * Valider forespørsel fra subBestilling request
     * @param from
     * @param to
     * @param bestillingStorage
     * @return
     */
    public boolean validateSubBestillingerRequest(int from, int to, List<Bestilling> bestillingStorage);

    /**
     * Valider forespørsel fra endre bestilling request
     * @param bestillingsListe
     * @return
     */
    public boolean validateEndreBestillingerRequest(List<Bestilling> bestillingsListe);

    /**
     *  Valider bestillingsdata
     * @param bestilling
     * @return
     */
    public  boolean validateBestilling(Bestilling bestilling);
}

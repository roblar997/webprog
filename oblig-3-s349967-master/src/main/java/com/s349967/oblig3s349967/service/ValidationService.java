package com.s349967.oblig3s349967.service;

import com.s349967.oblig3s349967.RepositoryInterfaces.IBrukerRepository;
import com.s349967.oblig3s349967.ServiceInterfaces.IKryptoService;
import com.s349967.oblig3s349967.ServiceInterfaces.IValidationService;
import com.s349967.oblig3s349967.config.BestillingValidationConfig;
import com.s349967.oblig3s349967.model.Bestilling;
import com.s349967.oblig3s349967.model.LoginData;
import com.s349967.oblig3s349967.repository.BestillingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService implements IValidationService{

    private final Logger validationLogger = LoggerFactory.getLogger(ValidationService .class);

    @Autowired
    IBrukerRepository brukerRepository;

    @Autowired
    IKryptoService kryptoService;

    @Override
    public boolean validerBruker(LoginData loginData) {

        String hashedPassord = brukerRepository.hentHashedDbPassord(loginData.getBrukernavn());

        //Hvis brukernavnet ikke finnes
        if(hashedPassord == null) return false;

        boolean loginOk = kryptoService.kryptoCompare(loginData.getPassord(),hashedPassord);
        if(!loginOk) validationLogger.error("Ugyldig innlogging" );
        return loginOk;
    }

    @Override
    public boolean registrerBruker(LoginData loginData) {
            String hashPassord = kryptoService.genererPassord(loginData.getPassord(), BestillingValidationConfig.getAntRunderBcryptSalt());
            loginData.setPassord(hashPassord);
            if(brukerRepository.registrerBruker(loginData)) return true;
            return false;
    }

    public boolean validateSubBestillingerRequest(int from, int to, List<Bestilling> bestillingStorage){

        int realTo = Math.min(to,bestillingStorage.size());

        if (from < 0 ||  realTo  < 0 || from >  realTo || realTo  > bestillingStorage.size() || from > bestillingStorage.size()-1 || bestillingStorage.subList(from, realTo ).isEmpty()){
            validationLogger.error("Server validering tilknyttet validateSubBestillingerRequest");
            return false;
        }
        return true;
    }

    public boolean validateEndreBestillingerRequest(List<Bestilling> bestillingsListe){
        return validateBestilling(bestillingsListe.get(1));
    }


    public  boolean validateBestilling(Bestilling bestilling){

        boolean isValid = true;

        isValid &= bestilling.getAntall().toString().matches(BestillingValidationConfig.getPatternAntall());
        isValid &= bestilling.getFornavn().matches(BestillingValidationConfig.getPatternFornavn());
        isValid &= bestilling.getEtternavn().matches(BestillingValidationConfig.getPatternEtternavn());
        isValid &= bestilling.getTelefon().matches(BestillingValidationConfig.getPatternTelefon());
        isValid &= bestilling.getEpost().matches(BestillingValidationConfig.getPatternEpost());
        if(!isValid) validationLogger.error("Ugyldig bestilling" );
        return isValid;
    }
}

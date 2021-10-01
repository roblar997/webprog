package com.s349967.oblig3s349967.service;

import com.s349967.oblig3s349967.RepositoryInterfaces.IBestillingRepository;
import com.s349967.oblig3s349967.RepositoryInterfaces.ITimeRepository;
import com.s349967.oblig3s349967.ServiceInterfaces.IBestillingService;
import com.s349967.oblig3s349967.model.BestillingDbModel;
import com.s349967.oblig3s349967.model.Bestilling;
import com.s349967.oblig3s349967.model.ChangeTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class BestillingService implements IBestillingService {

    @Autowired
    IBestillingRepository bestillingRepository;

    @Autowired
    ITimeRepository timeRepository;

    @PostConstruct
    public void init() {
            //Sorter etter etternavn i starten i sykene orden
            //Henter variabler direkte pga repository kan skal ikke kunne endre, og pga
            //problematikk knyttet til Integer

    }
    public List<BestillingDbModel> getBestillingStorage(String schemaToUse){
        return this.bestillingRepository.getBestillingStorage(schemaToUse);
    }
    public List<Bestilling> getSubBestillinger(int from, int to,String schemaToUse)  {

        int realTo = Math.min(to,bestillingRepository.getBestillingStorage(schemaToUse).size());


        return bestillingRepository.getBestillingStorage(schemaToUse).subList(from, realTo ).stream().map((x)->{return (Bestilling)x;}).collect(Collectors.toList());
    }
    public ChangeTime getChangeTime(){
        return timeRepository.getChangeTime();
    }

    public long slettBestilling(Bestilling bestilling,String schemaToUse) throws SQLException {

        Optional<BestillingDbModel> bestillingDbModelOpt = bestillingRepository.slettDbBestilling(bestilling,schemaToUse);
        if(bestillingDbModelOpt.isPresent()){
            BestillingDbModel bestillingDbModel = bestillingDbModelOpt.get();
            timeRepository.getChangeTime().setChangeTime(new Date().getTime());
            bestillingRepository.getBestillingStorage(schemaToUse).remove(bestillingDbModel);
        }

        return timeRepository.getChangeTime().getChangeTime();

    }
   public void sorterCurrentBestillingerWrapper(String schemaToUse){
       this.sorterBestillinger(bestillingRepository.getSortingStrategyModel().getFieldNmb(), bestillingRepository.getSortingStrategyModel().getIsDesc(),schemaToUse);
   }
    private void sorterBestillinger(int id, int isDesc,String schemaToUse){
        switch(id) {
            case 0:
                if(isDesc == 0){
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling a, Bestilling b) {


                            return a.getFilmValg().compareTo(b.getFilmValg());

                        } });
                }

                else {
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling b, Bestilling a) {


                            return a.getFilmValg().compareTo(b.getFilmValg());

                        } });
                }

                break;
            case 1:
                if(isDesc == 0) {
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling a, Bestilling b) {

                            return a.getAntall() - b.getAntall();


                        }
                    });
                }
                else {
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling b, Bestilling a) {

                            return a.getAntall() - b.getAntall();


                        }
                    });
                }
                break;

            case 2:
                if(isDesc == 0) {
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling a, Bestilling b) {

                            return a.getFornavn().compareTo(b.getFornavn());



                        } });}
                else {
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling b, Bestilling a) {

                            return a.getFornavn().compareTo(b.getFornavn());



                        } });
                }
                break;


            case 3:
                if(isDesc == 0) {
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling a, Bestilling b) {

                            return a.getEtternavn().compareTo(b.getEtternavn());

                        }
                    });
                }
                else {
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling b, Bestilling a) {

                            return a.getEtternavn().compareTo(b.getEtternavn());

                        }
                    });
                }
                break;

            case 4:
                if(isDesc == 0) {
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling a, Bestilling b) {

                            return a.getTelefon().compareTo(b.getTelefon());


                        }

                    });}
                else {
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling b, Bestilling a) {

                            return a.getTelefon().compareTo(b.getTelefon());


                        }

                    });
                }
                break;
            case 5:
                if(isDesc == 0) {
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling a, Bestilling b) {

                            return a.getEpost().compareTo(b.getEpost());

                        }
                    });}
                else {
                    bestillingRepository.getBestillingStorage(schemaToUse).sort(new Comparator<Bestilling>() {
                        @Override
                        public int compare(Bestilling b, Bestilling a) {

                            return a.getEpost().compareTo(b.getEpost());

                        }
                    });
                }
                break;



        }
    }
    public long sorterBestillingerWrapper(int id, int isDesc,String schemaToUse){

        if(id < 0 || id > 6) return timeRepository.getChangeTime().getChangeTime();
        if(isDesc < 0 || isDesc > 1) return timeRepository.getChangeTime().getChangeTime();
        sorterBestillinger(id,isDesc,schemaToUse);
        bestillingRepository.getSortingStrategyModel().setFieldNmb(id);
        bestillingRepository.getSortingStrategyModel().setIsDesc(isDesc);

        return timeRepository.getChangeTime().getChangeTime();

    }



    public long slettBestillinger(String schemaToUse) throws SQLException {
        bestillingRepository.slettDbBestillinger(schemaToUse);
        bestillingRepository.getBestillingStorage(schemaToUse).clear();

        timeRepository.getChangeTime().setChangeTime(new Date().getTime());
        return timeRepository.getChangeTime().getChangeTime();
    }


    public void createSchemaInstance(String schema){
        bestillingRepository.createSchemaInstance(schema);
    }
    public long lagreBestilling(Bestilling bestilling,String schemaToUse) throws SQLException {



        BestillingDbModel bestilling2 = bestillingRepository.addDbBestilling(bestilling,schemaToUse);
        bestillingRepository.getBestillingStorage(schemaToUse).add(bestilling2);
        //Sorter pga brudd i ordning
        this.sorterBestillinger(bestillingRepository.getSortingStrategyModel().getFieldNmb(), bestillingRepository.getSortingStrategyModel().getIsDesc(),schemaToUse);

        timeRepository.getChangeTime().setChangeTime(new Date().getTime());
        return timeRepository.getChangeTime().getChangeTime();
    }


    public long endreBestilling(List<Bestilling> bestillingsListe,String schemaToUse) throws SQLException {

        Bestilling toFind = bestillingsListe.get(0);
        Bestilling postBestilling = bestillingsListe.get(1);

        Optional<BestillingDbModel> muligBestilling = bestillingRepository.endreDbBestilling(toFind,postBestilling,schemaToUse);


        if(muligBestilling.isPresent()){

            BestillingDbModel toChange = muligBestilling.get();

            toChange.setFilmValg(postBestilling.getFilmValg());
            toChange.setAntall(postBestilling.getAntall());
            toChange.setFornavn(postBestilling.getFornavn());
            toChange.setEtternavn(postBestilling.getEtternavn());
            toChange.setTelefon(postBestilling.getTelefon());
            toChange.setEpost(postBestilling.getEpost());

            //Sorter pga brudd i ordning
            this.sorterBestillinger(bestillingRepository.getSortingStrategyModel().getFieldNmb(), bestillingRepository.getSortingStrategyModel().getIsDesc(),schemaToUse);

        }


        timeRepository.getChangeTime().setChangeTime(new Date().getTime());
        return timeRepository.getChangeTime().getChangeTime();
    }
}


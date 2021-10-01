package com.s349967.oblig3s349967.controller;

import com.s349967.oblig3s349967.ServiceInterfaces.IBestillingService;
import com.s349967.oblig3s349967.ServiceInterfaces.IFilmService;
import com.s349967.oblig3s349967.ServiceInterfaces.IValidationService;
import com.s349967.oblig3s349967.model.*;
import com.s349967.oblig3s349967.service.BestillingService;
import com.s349967.oblig3s349967.service.FilmService;
import com.s349967.oblig3s349967.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

//
@RestController
public class RegistrationController {


    @Autowired
    private IBestillingService bestillingService;

    @Autowired
    private IFilmService filmService;

    @Autowired
    private IValidationService validationService;

    @Autowired
    HttpSession session;



    @PostMapping("/getSubBestillinger/{from}/{to}")
    @Transactional
    public List<Bestilling> getSubBestillinger(@PathVariable("from") int from, @PathVariable("to") int to, HttpServletResponse response) throws IOException {
        if(session.getAttribute("innlogget") == null)       response.sendError(HttpStatus.FORBIDDEN.value());
        if (!validationService.validateSubBestillingerRequest(from, to, bestillingService.getBestillingStorage((String)session.getAttribute("innlogget")).stream().map((x) -> {
            return (Bestilling) x;
        }).collect(Collectors.toList()))) {
            response.sendError(HttpStatus.NOT_ACCEPTABLE.value());
            return null;
        }

        return bestillingService.getSubBestillinger(from, to,(String)session.getAttribute("innlogget"));

    }

    @GetMapping("/getFilmValg")
    @Transactional
    public List<Film> getFilmValg( HttpServletResponse response) throws IOException {

        if(session.getAttribute("innlogget") == null)         response.sendError(HttpStatus.FORBIDDEN.value());
        return filmService.getFilmValg();
    }

    @PostMapping("/slettBestilling")
    @Transactional
    public long slettBestilling(Bestilling bestilling, HttpServletResponse response) throws IOException {
        if(session.getAttribute("innlogget") == null)         response.sendError(HttpStatus.FORBIDDEN.value());
        if (!validationService.validateBestilling(bestilling)) {
            return bestillingService.getChangeTime().getChangeTime();
        }

        try {
            long toReturn = bestillingService.slettBestilling(bestilling,(String)session.getAttribute("innlogget"));
            return toReturn;
        } catch (SQLException throwables) {
            response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return -1;
        }

    }

    @GetMapping("/sorterBestillinger/{id}/{isDesc}")
    @Transactional
    public long sorterBestillinger(@PathVariable("id") int id, @PathVariable("isDesc") int isDesc,HttpServletResponse response) throws IOException {
        if(session.getAttribute("innlogget") == null)         response.sendError(HttpStatus.FORBIDDEN.value());
        return bestillingService.sorterBestillingerWrapper(id, isDesc,(String)session.getAttribute("innlogget"));

    }


    @GetMapping("/getChangeTime")
    @Transactional
    public ChangeTime getChangeTime(HttpServletResponse response) throws IOException {

        if(session.getAttribute("innlogget") == null)        response.sendError(HttpStatus.FORBIDDEN.value());
        return bestillingService.getChangeTime();
    }

    @PostMapping("/slettBestillinger")
    @Transactional
    public long slettBestillinger(HttpServletResponse response) throws IOException {

        if(session.getAttribute("innlogget") == null)        response.sendError(HttpStatus.FORBIDDEN.value());
        try {
            long toReturn = bestillingService.slettBestillinger((String)session.getAttribute("innlogget"));
            return toReturn;
        } catch (SQLException throwables) {
            response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return -1;
        }
    }

    @PostMapping("/login")
    @Transactional
    public boolean login(LoginData loginData){

        if(validationService.validerBruker(loginData)){
            session.setAttribute("innlogget",loginData.getBrukernavn());
            bestillingService.sorterCurrentBestillingerWrapper((String)session.getAttribute("innlogget"));
            return true;
        }
        return false;
    }

    @PostMapping("/registrer")
    @Transactional
    public boolean registrer(LoginData loginData){
        if(validationService.registrerBruker(loginData)){
            bestillingService.createSchemaInstance(loginData.getBrukernavn());
            return true;
        }
        else  return false;

    }

    @PostMapping("/logout")
    @Transactional
    public void logOut(){
        session.removeAttribute("innlogget");

    }


    @PostMapping("/lagreBestilling")
    @Transactional
    public long lagreBestilling(Bestilling bestilling, HttpServletResponse response) throws IOException {
        if(session.getAttribute("innlogget") == null)        response.sendError(HttpStatus.FORBIDDEN.value());
        //Ingenting endrer seg hvis ugyldig. Endringsdatoen blir den samme.

        if (!validationService.validateBestilling(bestilling)) {
            response.sendError(HttpStatus.NOT_ACCEPTABLE.value());
            return -1;
        }

        try {
            long toReturn = bestillingService.lagreBestilling(bestilling,(String)session.getAttribute("innlogget"));
            return toReturn;
        } catch (SQLException throwables) {
            response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return -1;
        }

    }

    @PostMapping("/endreBestilling")
    @Transactional
    public long endreBestilling(@RequestBody List<Bestilling> bestillingsListe, HttpServletResponse response) throws IOException {
        if(session.getAttribute("innlogget") == null)        response.sendError(HttpStatus.FORBIDDEN.value());
        //Ingenting endrer seg hvis ugyldig. Endringsdatoen blir den samme.
        if (!validationService.validateEndreBestillingerRequest(bestillingsListe)) {
            response.sendError(HttpStatus.NOT_ACCEPTABLE.value());
            return -1;

        }
        try {
            long toReturn = bestillingService.endreBestilling(bestillingsListe,(String)session.getAttribute("innlogget"));
            return toReturn;
        } catch (SQLException throwables) {
            response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return -1;
        }


    }
}
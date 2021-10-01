package com.s349967.oblig3s349967.repository;

import com.s349967.oblig3s349967.RepositoryInterfaces.ITimeRepository;
import com.s349967.oblig3s349967.model.ChangeTime;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class TimeRepository implements ITimeRepository {

    private ChangeTime changeTime;


    @PostConstruct
    public void init(){

        changeTime = new ChangeTime (0l);

    }

    public ChangeTime getChangeTime(){
        return changeTime;
    }

}

package com.f12s.playdatenow08.services;

import com.f12s.playdatenow08.models.StateterritoryMdl;
import com.f12s.playdatenow08.repositories.StateterritoryRpo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateterritorySrv {

    @Autowired
    StateterritoryRpo stateterritoryRpo;

    // returns all
    public List<StateterritoryMdl> returnAll(){
        return stateterritoryRpo.findAll();
    }

}

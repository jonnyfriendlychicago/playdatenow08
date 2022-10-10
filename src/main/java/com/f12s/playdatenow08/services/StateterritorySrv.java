package com.f12s.playdatenow08.services;

//public class StateterritorySrv {
//}

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.f12s.playdatenow08.models.StateterritoryMdl;
import com.f12s.playdatenow08.repositories.StateterritoryRpo;

@Service
public class StateterritorySrv {

    // adding the playdate repository as a dependency
//	private final StateterritoryRpo stateterritoryRpo;
//	public StateterritorySrv(StateterritoryRpo stateterritoryRpo) {this.stateterritoryRpo = stateterritoryRpo;}
    // above replaced by below
    @Autowired
    StateterritoryRpo stateterritoryRpo;

    // returns all stateterritory
    public List<StateterritoryMdl> returnAll(){
        return stateterritoryRpo.findAll();
    }

}

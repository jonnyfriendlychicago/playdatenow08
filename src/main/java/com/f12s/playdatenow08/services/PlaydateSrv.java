package com.f12s.playdatenow08.services;

import com.f12s.playdatenow08.models.PlaydateMdl;
import com.f12s.playdatenow08.repositories.PlaydateRpo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaydateSrv {

    // adding the playdate repository as a dependency
//	private final PlaydateRpo playdateRpo;
//	public PlaydateSrv(PlaydateRpo playdateRpo) {this.playdateRpo = playdateRpo;}
    // above replaced by below
    @Autowired
    PlaydateRpo playdateRpo;

    // creates one
    public PlaydateMdl create(PlaydateMdl x) {
        return playdateRpo.save(x);
    }

    // update one
    public PlaydateMdl update(PlaydateMdl x) {
        return playdateRpo.save(x);
    }

    // delete by id
    public void delete(PlaydateMdl x) {
        playdateRpo.delete(x);
    }

    // returns one by id
    public PlaydateMdl findById(Long id) {
        Optional<PlaydateMdl> optionalPlaydate = playdateRpo.findById(id);
        if(optionalPlaydate.isPresent()) {
            return optionalPlaydate.get();
        }else {
            return null;
        }
    }

    // returns all
    public List<PlaydateMdl> returnAll(){
        return playdateRpo.findAll();
    }

    public List<PlaydateMdl> userHostedPlaydateListCurrentPlus(Long x) {
        return playdateRpo.userHostedPlaydateListCurrentPlus(x);
    }

    public List<PlaydateMdl> userHostedPlaydateListPast(Long x) {
        return playdateRpo.userHostedPlaydateListPast(x);
    }

// end srv
}

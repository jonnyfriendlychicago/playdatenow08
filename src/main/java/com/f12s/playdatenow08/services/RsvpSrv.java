package com.f12s.playdatenow08.services;

//public class RsvpSrv {
//}

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f12s.playdatenow08.models.RsvpMdl;
import com.f12s.playdatenow08.pojos.PlaydateUserUnionRsvpUser;
import com.f12s.playdatenow08.models.PlaydateMdl;
import com.f12s.playdatenow08.repositories.RsvpRpo;

@Service
public class RsvpSrv {

    // adding the rsvp repository as a dependency
//	private final RsvpRpo rsvpRpo;
//	public RsvpSrv(RsvpRpo rsvpRpo) {this.rsvpRpo = rsvpRpo;}
    // above replaced by below
    @Autowired
    RsvpRpo rsvpRpo;

    // creates one rsvp
    public RsvpMdl create(RsvpMdl x) {
        return rsvpRpo.save(x);
    }

    // updates one rsvp
    public RsvpMdl update(RsvpMdl x) {
        return rsvpRpo.save(x);
    }

    // delete rsvp by id
    public void delete(RsvpMdl x) {
        rsvpRpo.delete(x);
    }

    // returns one rsvp by id
    public RsvpMdl findById(Long id) {
        Optional<RsvpMdl> optionalRsvp = rsvpRpo.findById(id);
        if(optionalRsvp.isPresent()) {
            return optionalRsvp.get();
        }else {
            return null;
        }
    }

    // returns all rsvp
    public List<RsvpMdl> returnAll(){
        return rsvpRpo.findAll();
    }

    // get all joined playdate
    public List<RsvpMdl> returnAllRsvpForPlaydate(PlaydateMdl x){
        return rsvpRpo.findAllByPlaydateMdl(x);
    }

    public List<PlaydateUserUnionRsvpUser> playdateRsvpList(Long x) {
        return rsvpRpo.playdateRsvpList(x);
    }

// end srv
}

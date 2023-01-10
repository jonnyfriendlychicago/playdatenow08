package com.f12s.playdatenow08.services;

import com.f12s.playdatenow08.models.RsvpMdl;
import com.f12s.playdatenow08.pojos.PlaydateUserUnionRsvpUser;
import com.f12s.playdatenow08.models.PlaydateMdl;
import com.f12s.playdatenow08.repositories.RsvpRpo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RsvpSrv {

    @Autowired
    RsvpRpo rsvpRpo;

    // creates one
    public RsvpMdl create(RsvpMdl x) {
        return rsvpRpo.save(x);
    }

    // updates one
    public RsvpMdl update(RsvpMdl x) {
        return rsvpRpo.save(x);
    }

    // delete  by id
    public void delete(RsvpMdl x) {
        rsvpRpo.delete(x);
    }

    // returns one  by id
    public RsvpMdl findById(Long id) {
        Optional<RsvpMdl> optionalRsvp = rsvpRpo.findById(id);
        if(optionalRsvp.isPresent()) {
            return optionalRsvp.get();
        }else {
            return null;
        }
    }

    // returns all
    public List<RsvpMdl> returnAll(){
        return rsvpRpo.findAll();
    }

    // get all joined playdate
    public List<RsvpMdl> returnAllRsvpForPlaydate(PlaydateMdl x){
        return rsvpRpo.findAllByPlaydateMdl(x);
    }

    public List<PlaydateUserUnionRsvpUser> playdateRsvpList(Long playdateId) {
        return rsvpRpo.playdateRsvpList(playdateId);
    }

} // end srv

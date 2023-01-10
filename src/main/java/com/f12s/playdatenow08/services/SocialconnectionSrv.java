package com.f12s.playdatenow08.services;

import com.f12s.playdatenow08.models.SocialconnectionMdl;
import com.f12s.playdatenow08.repositories.SocialconnectionRpo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SocialconnectionSrv {

    @Autowired
    SocialconnectionRpo socialconnectionRpo;

    // creates one
    public SocialconnectionMdl create(SocialconnectionMdl x) { return socialconnectionRpo.save(x);}

    // updates one
    public SocialconnectionMdl update(SocialconnectionMdl x) { return socialconnectionRpo.save(x);}

    // trying to get the accept thing working
    // returns one by id
    public SocialconnectionMdl findById(Long id) {
        Optional<SocialconnectionMdl> optionalSocialconnection = socialconnectionRpo.findById(id);
        if(optionalSocialconnection.isPresent()) {
            return optionalSocialconnection.get();
        }else {
            return null;
        }
    }

} // end srv

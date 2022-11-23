package com.f12s.playdatenow08.repositories;


import com.f12s.playdatenow08.models.SocialconnectionMdl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialconnectionRpo extends CrudRepository<SocialconnectionMdl, Long> {

}

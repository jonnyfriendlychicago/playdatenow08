package com.f12s.playdatenow08.repositories;

import com.f12s.playdatenow08.models.SocialconnectionhistoryMdl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialconnectionhistoryRpo extends CrudRepository<SocialconnectionhistoryMdl, Long> {

} // end rpo

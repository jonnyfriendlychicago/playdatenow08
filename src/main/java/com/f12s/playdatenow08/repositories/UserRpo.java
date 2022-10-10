package com.f12s.playdatenow08.repositories;

//public interface UserRpo {
//}

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.f12s.playdatenow08.models.UserMdl;

@Repository
public interface UserRpo extends CrudRepository<UserMdl, Long> {

    UserMdl findByEmail(String email);

    UserMdl findByUserName(String userName);

    List<UserMdl> findAll();

    UserMdl findByIdIs(Long id);

// end rpo
}

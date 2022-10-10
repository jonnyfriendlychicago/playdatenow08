package com.f12s.playdatenow08.repositories;

//public interface RoleRpo {
//}

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.f12s.playdatenow08.models.RoleMdl;

@Repository
public interface RoleRpo extends CrudRepository<RoleMdl, Long> {

    List<RoleMdl> findAll();

    List<RoleMdl> findByName(String name);
}
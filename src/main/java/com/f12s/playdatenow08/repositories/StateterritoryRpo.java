package com.f12s.playdatenow08.repositories;

//public interface StateterritoryRpo {
//}

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.f12s.playdatenow08.models.StateterritoryMdl;

@Repository
public interface StateterritoryRpo extends CrudRepository<StateterritoryMdl, Long> {

    List<StateterritoryMdl> findAll();

// end rpo
}

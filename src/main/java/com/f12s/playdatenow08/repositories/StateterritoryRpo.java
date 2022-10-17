package com.f12s.playdatenow08.repositories;

import com.f12s.playdatenow08.models.StateterritoryMdl;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateterritoryRpo extends CrudRepository<StateterritoryMdl, Long> {

    List<StateterritoryMdl> findAll();

// end rpo
}

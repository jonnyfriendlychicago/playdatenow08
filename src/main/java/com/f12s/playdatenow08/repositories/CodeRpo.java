package com.f12s.playdatenow08.repositories;

import com.f12s.playdatenow08.models.CodeMdl;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRpo extends CrudRepository<CodeMdl, Long> {

    List<CodeMdl> findAll();
}

package com.f12s.playdatenow08.repositories;

import com.f12s.playdatenow08.models.CodeMdl;
import java.util.List;

import com.f12s.playdatenow08.models.PlaydateMdl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRpo extends CrudRepository<CodeMdl, Long> {

    List<CodeMdl> findAll();


    @Query(
            value= "SELECT c.* FROM playdatenow08.code c where c.codecategory_id = :keyword"
            , nativeQuery = true)
    List<CodeMdl> locationTypeList(Long keyword);

} // end rpo

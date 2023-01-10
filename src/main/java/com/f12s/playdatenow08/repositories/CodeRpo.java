package com.f12s.playdatenow08.repositories;

import com.f12s.playdatenow08.models.CodeMdl;
import java.util.List;

import com.f12s.playdatenow08.models.CodecategoryMdl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRpo extends CrudRepository<CodeMdl, Long> {

    List<CodeMdl> findAll();

    CodeMdl findByIdIs(Long id); // I think this line is junk.  12/1

    CodeMdl findCodeMdlByCode(String code);

//    @Query(
//            value= "SELECT c.* FROM playdatenow08.code c where c.codecategory = :keyword order by c.gui_display_order"
//            , nativeQuery = true)
//    List<CodeMdl> targetedCodeList(Long keyword);

    //  great example here of how above native query can be readily replaced by simple RPO language (not sure what it's called)
    List<CodeMdl> findCodeMdlsByCodecategory(CodecategoryMdl codecategory); // jan 8 playaround

} // end rpo


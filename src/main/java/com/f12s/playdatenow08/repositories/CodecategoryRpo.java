package com.f12s.playdatenow08.repositories;

import com.f12s.playdatenow08.models.CodecategoryMdl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodecategoryRpo extends CrudRepository<CodecategoryMdl, Long> {

    CodecategoryMdl findCodecategoryMdlByCodeType(String codeType);

}

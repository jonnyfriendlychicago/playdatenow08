package com.f12s.playdatenow08.services;

import com.f12s.playdatenow08.models.CodeMdl;
import com.f12s.playdatenow08.models.CodecategoryMdl;
import com.f12s.playdatenow08.models.PlaydateMdl;
import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.repositories.CodeRpo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeSrv {

    @Autowired
    CodeRpo codeRpo;

    // returns all
    public List<CodeMdl> returnAll() {return codeRpo.findAll(); }

    public List<CodeMdl> targetedCodeList(Long x) {
        return codeRpo.targetedCodeList(x);
    }

    // jan 8 mess around
    public List<CodeMdl> targetedCodeListTwo(CodecategoryMdl x) {
        return codeRpo.findCodeMdlsByCodecategory(x);
    }


    public CodeMdl findCodeMdlByCode(
            String code
    ) {
        return codeRpo.findCodeMdlByCode(code);
    }


} // end CodeSrv

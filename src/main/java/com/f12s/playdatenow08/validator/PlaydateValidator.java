package com.f12s.playdatenow08.validator;

//import com.f12s.playdatenow08.models.UserMdl;
// above replaced by below
import com.f12s.playdatenow08.models.PlaydateMdl;
import com.f12s.playdatenow08.repositories.PlaydateRpo;
import com.f12s.playdatenow08.repositories.UserRpo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PlaydateValidator implements Validator {

//    @Autowired
//    private UserRpo userRpo;
//    // above replaced by below
    @Autowired
    private PlaydateRpo playdateRpo;

//    @Override
//    public boolean supports(Class<?> clazz) {
//        return UserMdl.class.equals(clazz);
//    }
    // above replaced by below
    @Override
    public boolean supports(Class<?> clazz) {
        return PlaydateMdl.class.equals(clazz);
    }

    @Override
    public void validate(
            Object object
            , Errors errors
    ) {

//        UserMdl userMdl = (UserMdl) object;
        // above replaced by below
        PlaydateMdl playdateMdl = (PlaydateMdl) object;

        // below is JRF first attempt at this error function syntax
        if (playdateMdl.getEventName().length() == 0 ) {
            errors.rejectValue("eventName", "Size");
        }

        // all of below commented out, does not seem essential
//        Optional<UserMdl> userObjWithSameEmail = Optional.ofNullable(userRpo.findByEmail(userMdl.getEmail()));
//
////        Optional<UserMdl> userObjWithSameUserName = Optional.ofNullable(userRpo.findByUserName(userMdl.getUserName()));
//
//        // Reject if email exists in db
//        if(userObjWithSameEmail.isPresent()) {
//            errors.rejectValue("email", "Match");
//        }
//
//        // moved here
//        Optional<UserMdl> userObjWithSameUserName = Optional.ofNullable(userRpo.findByUserName(userMdl.getUserName()));
//
//        // Reject if username exists in db
//        if(userObjWithSameUserName.isPresent()) {
//            errors.rejectValue("userName", "Match");
//        }
//
//        // JRF here trying to add a validation for userName size
//
//        if (userMdl.getUserName().length() < 3) {
//            errors.rejectValue("userName", "Size");
//        }


    } // end validate function

// end validator class
}


package com.f12s.playdatenow08.validator;

//public class UserValidator {
//}

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.repositories.UserRpo;

@Component
public class UserValidator implements Validator {


    @Autowired
    private UserRpo userRpo;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserMdl.class.equals(clazz);
    }

    @Override
    public void validate(
            Object object
            , Errors errors
    ) {

        UserMdl userMdl = (UserMdl) object;

        if (!userMdl.getPasswordConfirm().equals(userMdl.getPassword())) {
            errors.rejectValue("passwordConfirm", "Match");
        }

        Optional<UserMdl> userObjWithSameEmail = Optional.ofNullable(userRpo.findByEmail(userMdl.getEmail()));

        Optional<UserMdl> userObjWithSameUserName = Optional.ofNullable(userRpo.findByUserName(userMdl.getUserName()));

        // Reject if email exists in db
        if(userObjWithSameEmail.isPresent()) {
            errors.rejectValue("email", "Match");
        }

        // Reject if username exists in db
        if(userObjWithSameUserName.isPresent()) {
            errors.rejectValue("userName", "Match");
        }
    }

// end validator class
}


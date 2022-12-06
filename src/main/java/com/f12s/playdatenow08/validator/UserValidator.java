package com.f12s.playdatenow08.validator;

import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.repositories.UserRpo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserRpo userRpo;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserMdl.class.equals(clazz);
    } // 2022.12.05: to this day, I'm still befuddled as to how this method works.  Same confusion regarding the "@Override" stuff in method below

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

//        Optional<UserMdl> userObjWithSameUserName = Optional.ofNullable(userRpo.findByUserName(userMdl.getUserName()));

        // Reject if email exists in db
        if(userObjWithSameEmail.isPresent()) {
            errors.rejectValue("email", "Match");
        }

        // moved here
        Optional<UserMdl> userObjWithSameUserName = Optional.ofNullable(userRpo.findByUserName(userMdl.getUserName()));

        // Reject if username exists in db
        if(userObjWithSameUserName.isPresent()) {
            errors.rejectValue("userName", "Match");
        }

        // JRF here trying to add a validation for userName size

        if (userMdl.getUserName().length() < 3) {
            errors.rejectValue("userName", "Size");
        }
    }

// end validator class
}


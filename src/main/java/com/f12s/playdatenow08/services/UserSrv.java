package com.f12s.playdatenow08.services;

import com.f12s.playdatenow08.models.RoleMdl;
import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.pojos.UserSocialConnectionPjo;
import com.f12s.playdatenow08.repositories.RoleRpo;
import com.f12s.playdatenow08.repositories.UserRpo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

@Service
public class UserSrv{

    @Autowired
    private UserRpo userRpo;

    @Autowired
    private RoleRpo roleRpo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void newUser(
            UserMdl usermdl
            , String role  // note: this is just a string variable called 'role'... not the roleMdl
    ) {
        usermdl.setPassword(bCryptPasswordEncoder.encode(usermdl.getPassword()));
        usermdl.setRoleMdl(roleRpo.findByName(role));
        userRpo.save(usermdl);
    }

    // JRF: temporarily disabling this updateUser program.
//    public void updateUser(
//    		UserMdl usermdl
//    		) {
//    	userRpo.save(usermdl);
//	}

    public UserMdl findByEmail(
            String email
    ) {
        return userRpo.findByEmail(email);
    }

    public List<UserMdl> allUsers(){
        return userRpo.findAll();
    }


    // below disabled from springSec for now
//	public UserMdl upgradeUser(
//			UserMdl usermdl
//			) {
//		usermdl.setRoleMdl(roleRpo.findByName("ROLE_ADMIN"));
//		return userRpo.save(usermdl);
//	}

    // below disabled from springSec for now
//	public void deleteUser(
//			UserMdl usermdl
//			) {
//		userRpo.delete(usermdl);
//	}

    // below used on every page beginning with /home.  Delivers user object of logged in user, so as to display various user attributes.
    public UserMdl findById(
            Long id
    ) {
        Optional<UserMdl> potentialUser = userRpo.findById(id);

        if(!potentialUser.isPresent()) {
            return null;}

        return potentialUser.get();

    }

    // returns all
    public List<UserMdl> returnAll(){
        return userRpo.findAll();
    }

    public UserMdl updateUserProfile(
            UserMdl sketchedUpdatedUserMdl
            , Errors errors
    ) {

        // try to find a user record with the proposed email addy
        Optional<UserMdl> userRecordWithMatchingEmailAddy = Optional.ofNullable(userRpo.findByEmail(sketchedUpdatedUserMdl.getEmail()));

        // Reject if record found in db with that email and the userID of such record is NOT the user we mging here.
        if(
                userRecordWithMatchingEmailAddy.isPresent()
                        &&
                        userRecordWithMatchingEmailAddy.get().getId() != sketchedUpdatedUserMdl.getId()

        ) {
            errors.rejectValue("email", "Match");
        }

        Optional<UserMdl> userRecordWithMatchingUserName = Optional.ofNullable(userRpo.findByUserName(sketchedUpdatedUserMdl.getUserName()));

        // Reject if record found in db with that userName and the userID of such record is NOT the user we mging here.
        if(
                userRecordWithMatchingUserName.isPresent()
                        &&
                        userRecordWithMatchingUserName.get().getId() != sketchedUpdatedUserMdl.getId()

        ) {
            errors.rejectValue("userName", "Match");
        }

        if(errors.hasErrors()) {
            return null; // Exit the method and go back to the controller to handle the response
        }

        return userRpo.save(sketchedUpdatedUserMdl);
    }

    // (1) all users list
//    public List<UserSocialConnectionPjo> userSocialConnectionList(Long x) {return userRpo.userSocialConnectionList(x);}
    public List<UserSocialConnectionPjo> userSocialConnectionList(Long authUserId) {return userRpo.userSocialConnectionList( authUserId );}

    // (2) relations to authUser: blocked
//    public List<UserSocialConnectionPjo> userSocialConnectionListBlocked(Long x) {return userRpo.userSocialConnectionListBlocked(x);}
    public List<UserSocialConnectionPjo> userSocialConnectionListBlocked(Long authUserId ) {return userRpo.userSocialConnectionListBlocked( authUserId );}

    // (3) relations to authUser: sent requests
    public List<UserSocialConnectionPjo> userSocialConnectionListSent(Long authUserId) {return userRpo.userSocialConnectionListSent(authUserId);}

    // (4) relations to authUser: received requests
    public List<UserSocialConnectionPjo> userSocialConnectionListReceived(Long authUserId) {return userRpo.userSocialConnectionListReceived( authUserId );}

    // (5) relations to authUser: friends
    public List<UserSocialConnectionPjo> userSocialConnectionListFriends(Long authUserId) {return userRpo.userSocialConnectionListFriends(authUserId);}



    // determination of whether auth user is admin or not; this combination of new variable instantiation and list iteration is required b/c of the many:many set-up between user and role
    public int authUserIsAdmin(UserMdl authUserObj) {
        int authUserIsAdmin = 0;
        // 6b: get list of roleMdl values, b/c that's how this many:many table set-up works
        List<RoleMdl> authUserObjRoleMdlList = authUserObj.getRoleMdl();
        // 6c: iterate through the list, looking for values where role id is 2 or 3, i.e. the admin roles; and yes, there is literally only one record in the list, so this iteration is "one and done"
        int a = 0; // instantiate variable for iteration
        for (a=0; a < authUserObjRoleMdlList.size(); a++ ) {
            System.out.println("item from service: " + a );
            System.out.println("authUserObjRoleMdlList.get(a): " + authUserObjRoleMdlList.get(a));
//            System.out.println("authUserObjRoleMdlList.get(a).getId() service: " + authUserObjRoleMdlList.get(a).getId() );
            if (

                    authUserObjRoleMdlList.get(a).getId().equals(Long.valueOf(2))
                            // replace above with a new line below so it's no longer get id... it using object
                            ||

                            authUserObjRoleMdlList.get(a).getId().equals(Long.valueOf(3))
                // replace above with a new line below so it's no longer get id... it using object
            ) authUserIsAdmin = 1;
//            System.out.println("authUserIsAdmin service: " + authUserIsAdmin  + "\n" );
        }
        return authUserIsAdmin;
    }



    // (6) getting friends list on playdate page
    public List<UserMdl> userFriendForPlaydateInviteDropdownList(Long x) {
        return userRpo.userFriendForPlaydateInviteDropdownList(x);
    }

} // end srv



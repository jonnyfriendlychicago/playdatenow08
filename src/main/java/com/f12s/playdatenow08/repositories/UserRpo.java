package com.f12s.playdatenow08.repositories;

import com.f12s.playdatenow08.models.UserMdl;
import java.util.List;

import com.f12s.playdatenow08.pojos.UserSocialConnectionPjo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRpo extends CrudRepository<UserMdl, Long> {

    UserMdl findByEmail(String email);

    UserMdl findByUserName(String userName);

    UserMdl findByIdIs(Long id);

    List<UserMdl> findAll();

    // begin socialCon stuff
    @Query(
            value=
                    "select case when u.id = :keyword then 0 when userFriend.userfriend is null then 1 else 2 end as statusRelationToAuthUser, u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName from playdatenow08.user u "
                            + "left join "
                            + "(select sc.usertwo as userfriend from playdatenow08.socialconnection sc where userone = :keyword union all select sc.userone as userfriend from playdatenow08.socialconnection sc where  usertwo  = :keyword ) userFriend on u.id = userFriend.userfriend"
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionList (Long keyword);


// end rpo
}


//package com.f12s.playdatenow08.repositories;
//
//import com.f12s.playdatenow08.models.UserMdl;
//import java.util.List;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface UserRpo extends CrudRepository<UserMdl, Long> {
//
//    UserMdl findByEmail(String email);
//
//    UserMdl findByUserName(String userName);
//
//    List<UserMdl> findAll();
//
//    UserMdl findByIdIs(Long id);
//
//// end rpo
//}

package com.f12s.playdatenow08.repositories;

import com.f12s.playdatenow08.models.CodeMdl;
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

//    UserMdl findById(Long id); // this line results in redSquig with msg: "'findById(Long)' in 'com.f12s.playdatenow08.repositories.UserRpo' clashes with 'findById(ID)' in 'org.springframework.data.repository.CrudRepository'; attempting to use incompatible return type"

    List<UserMdl> findAll();

    // (1) all users list
    // note: excludes any user records that join a soCon record on userRecord+AuthUser where the blocker column has a value.
    // the union selection 'ur' is identical to 'ur2' except that 'ur2' contains only those records with a blocker value
    // this mega query is boilerplate for all other mega queries in this Rpo
    @Query(
            value =
                    "select "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
                            + " , case "
                            + " when u.id = :authUserId then 'authUserRecord' "
                            + " when (ur.relatedUserId is null or code.code = 'soConReset') then 'noRelation' "
                            + " when (code.code = 'requestPending' and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
                            + " when (code.code = 'requestPending' and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
                            + " when code.code = 'friends' then 'friends'  "
                            + " when code.code = 'blocked' then 'blocked'  "
                            + " else  'soConError' end as soconStatusEnhanced, "
                            + " ur.socialconnectionId as socialconnectionId "
                            + " from playdatenow08.user u "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " left join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
                            + " ) ur on u.id = ur.relatedUserId"
                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "

                            + " left join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId and sc.blocker_user_id is not null"
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId and sc.blocker_user_id is not null"
                            + " ) ur2 on u.id = ur2.relatedUserId"
                            + " where ur2.socialconnectionId is null"

                            + " order by u.id desc"
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionList(Long authUserId);

    // (2) relations to authUser: blockedBy

    // difference between all users query and this query: (a) ur is a join, not a left join (b) ur includes where clause: blocked by is authUser (c) b/c aforementioned, no need to fail-join with a ur2
    @Query(
            value =
                    "select "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
                            + " , case "
                            + " when u.id = :authUserId then 'authUserRecord' "
                            + " when (ur.relatedUserId is null or code.code = 'soConReset') then 'noRelation' "
                            + " when (code.code = 'requestPending' and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
                            + " when (code.code = 'requestPending' and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
                            + " when code.code = 'friends' then 'friends'  "
                            + " when code.code = 'blocked' then 'blocked'  "
                            + " else  'soConError' end as soconStatusEnhanced, "
                            + " ur.socialconnectionId as socialconnectionId "
                            + " from playdatenow08.user u "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId and sc.blocker_user_id = :authUserId"
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId and sc.blocker_user_id = :authUserId"
                            + " ) ur on u.id = ur.relatedUserId"
                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
                            + " order by u.id desc"
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionListBlocked(Long authUserId);

    // below is old, above is refactor attempt
//    // difference between all users query and this query: (a) ur2 has blocker = auth user and (b) left join to ur2 followed by a 'where jr2 join occurs'
//    @Query(
//            value =
//                    "select "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//                            + " , case "
//                            + " when u.id = :authUserId then 'authUserRecord' "
//                            + " when (ur.relatedUserId is null or code.code = 'soConReset') then 'noRelation' "
//                            + " when (code.code = 'requestPending' and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
//                            + " when (code.code = 'requestPending' and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
//                            + " when code.code = 'friends' then 'friends'  "
//                            + " when code.code = 'blocked' then 'blocked'  "
//                            + " else  'soConError' end as soconStatusEnhanced, "
//                            + " ur.socialconnectionId as socialconnectionId "
//                            + " from playdatenow08.user u "
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join "
//                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
//                            + " union all "
//                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
//                            + " ) ur on u.id = ur.relatedUserId"
//                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
//
//                            + " left join "
//                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId and sc.blocker_user_id = :authUserId"
//                            + " union all "
//                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId and sc.blocker_user_id = :authUserId"
//                            + " ) ur2 on u.id = ur2.relatedUserId"
//                            + " where ur2.socialconnectionId is not null"
//
//                            + " order by u.id desc"
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionListBlocked(Long authUserId);

    // (3) relations to authUser: sent requests
    // diff between this and blocked: (a) in ur, where clause targets initiator, not blocker (b) first-tier where clause points to requestPending (in contrast, blocker query doesn't care about that)
    @Query(
            value =
                    "select "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
                            + " , case "
                            + " when u.id = :authUserId then 'authUserRecord' "
                            + " when (ur.relatedUserId is null or code.code = 'soConReset') then 'noRelation' "
                            + " when (code.code = 'requestPending' and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
                            + " when (code.code = 'requestPending' and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
                            + " when code.code = 'friends' then 'friends'  "
                            + " when code.code = 'blocked' then 'blocked'  "
                            + " else  'soConError' end as soconStatusEnhanced, "
                            + " ur.socialconnectionId as socialconnectionId "
                            + " from playdatenow08.user u "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId and sc.initiator_user_id = :authUserId"
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId and sc.initiator_user_id = :authUserId"
                            + " ) ur on u.id = ur.relatedUserId"
                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
                            + " where code.code = 'requestPending' "
                            + " order by u.id desc"
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionListSent(Long authUserId);

//    @Query(
//            value =
//                    "select "
//                            + " ur.socialconnectionId as socialconnectionId, ur.relatedUser as relatedUser, ur.blockerUser as blockerUser, ur.initiatorUser as initiatorUser, "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//
//                            + " from (select sc.id as socialconnectionId, sc.userone as authUser, sc.usertwo as relatedUser, sc.soconstatus_code as soconStatusCodeId , sc.blocker_user_id as blockerUser, sc.initiator_user_id as initiatorUser  from playdatenow08.socialconnection sc where userone = :keyword union all select sc.id as socialconnectionId, sc.userone as relatedUser, sc.usertwo as authUser , sc.soconstatus_code as soconStatusCodeId , sc.blocker_user_id as blockerUser, sc.initiator_user_id as initiatorUser  from playdatenow08.socialconnection sc where  usertwo  = :keyword ) ur "
//                            + " join playdatenow08.user u on ur.relatedUser = u.id "
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join playdatenow08.code c on ur.soconStatusCodeId = c.id "
//                            + " where c.code = 'requestPending' and ur.initiatorUser = :keyword  "
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionListSent(Long keyword);

//    @Query(
//            value =
//                    "select "
//                            + " ur.socialconnectionId as socialconnectionId, ur.initiatorUserId as initiatorUser, "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//                            + " from playdatenow08.user u "
//                            + " join "
//                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
//                            + " union all "
//                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
//                            + " ) ur on u.id = ur.relatedUserId"
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join playdatenow08.code c on ur.soconStatusCodeId = c.id "
//                            + " where c.code = 'requestPending' "
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionListSent(Long authUserId);



    // (1) sent list
//    @Query(
//            value =
//                    "select "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//                            + " , case "
//                            + " when u.id = :authUserId then 'authUserRecord' "
//                            + " when (ur.relatedUserId is null or ur.soconStatusCodeId = 12) then 'noRelation' "
//                            + " when (ur.soconStatusCodeId = 6 and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
//                            + " when (ur.soconStatusCodeId = 6 and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
//                            + " when ur.soconStatusCodeId = 9 then 'friends'  "
//                            + " when code.code is not null then code.code "
//                            + " else  'soConError' end as soconStatusEnhanced, "
//                            + " ur.socialconnectionId as socialconnectionId "
//                            + " from playdatenow08.user u "
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join "
//                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
//                            + " union all "
//                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
//                            + " ) ur on u.id = ur.relatedUserId"
//                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
//                            + " where ur.soconStatusCodeId = 6 and :authUserId = ur.initiatorUserId "
//                            + " order by u.id desc"
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionListSent(Long authUserId);



//    how to fix above:
//
//    the union should say:
//    both groups get InitiatorUserId, ResponderUserId, soConStatusCodeId, a field called "relatedUserId"
//    relatedUserId is the value that is NOT the authUserID
//    group 1 is where initiator = authUserId; in this situation, relatedUserId is the Responder
//    group 2 reverses this: responder = authUserId; in this situation, relatedUserId is the initiator
//
//            once above is done, you can join user table to this unioned table: u.id = relatedUser.relatedUserId
//
//    Then, we can do logic in the case statement, e.g.,
//            if soConStatusCodeId = 12, then 'noRelation'; who's initiator/responder doesn't matter.  alt, we could make the 6/9/11 statements, and otherwise it's a default: 'noRelation
//            if status code = 6 and authUserId = initiatorUserId, then 'authUserSentRequest'
//            if status code = 6 and authUserId = ResponderUserId, then 'authUserReceivedRequest'
//            if status code = 9 then 'friends' who's initiator/responder doesn't matter
//            if status code = 11 and authUserId = BlockerUserId, then 'blockedByAuthUser'
//
//    okay, also need the following:
//
//    somehow the union statement needs to exclude records where ( blocker is  null or (blocker is not null and blocker = authUserID)


// Old is below

//    // (1) all users list
//    @Query(
//            value =
//                    "select "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//                            + " , case "
//                            + " when u.id = :authUserId then 'authUserRecord' "
//                            + " when ur.relatedUser is null then 'noRelation' "
//                            + " when code.code is not null then code.code "
//                            + " else  'soConError' end as soconStatusEnhanced, "
//                            + " ur.socialconnectionId as socialconnectionId, ur.relationInitiator as relationInitiator"
//                            + " from playdatenow08.user u "
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join "
//                            + "(select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.usertwo as relatedUser, sc.soconstatus_code_id as soconStatusCodeId from playdatenow08.socialconnection sc where userone = :authUserId union all select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.userone as relatedUser, sc.soconstatus_code_id as soconStatusCodeId from playdatenow08.socialconnection sc where  usertwo  = :authUserId ) ur on u.id = ur.relatedUser"
//                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
//                            + " order by u.id desc"
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionList(Long authUserId);

    // (4) relations to authUser: received requests

    // diff between this and sent: (a) in ur, where clause targets responder... that's it! and yes, the where clauses totally redundant, will clean up later
    @Query(
            value =
                    "select "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
                            + " , case "
                            + " when u.id = :authUserId then 'authUserRecord' "
                            + " when (ur.relatedUserId is null or code.code = 'soConReset') then 'noRelation' "
                            + " when (code.code = 'requestPending' and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
                            + " when (code.code = 'requestPending' and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
                            + " when code.code = 'friends' then 'friends'  "
                            + " when code.code = 'blocked' then 'blocked'  "
                            + " else  'soConError' end as soconStatusEnhanced, "
                            + " ur.socialconnectionId as socialconnectionId "
                            + " from playdatenow08.user u "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId and sc.responder_user_id = :authUserId"
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId and sc.responder_user_id = :authUserId"
                            + " ) ur on u.id = ur.relatedUserId"
                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
                            + " where code.code = 'requestPending' "
                            + " order by u.id desc"
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionListReceived(Long authUserId);

    // below super old
//    @Query(
//            value =
//                    "select "
//                            + " case when u.id = :keyword then 'authUserRecord' when ur.relatedUser is null then 'noRelation' when c.code is not null then c.code else  'soConError' end as soconStatusEnhanced, "
//                            + " ur.socialconnectionId as socialconnectionId, ur.relationInitiator as relationInitiator, "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//
//                            + " from (select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.usertwo as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where userone = :keyword union all select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.userone as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where usertwo  = :keyword ) ur "
//                            + " join playdatenow08.user u on ur.relatedUser = u.id "
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join playdatenow08.code c on ur.soconStatusCodeId = c.id "
//                            + " where c.code = 'requestPending' and ur.relationInitiator <> :keyword  "
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionListReceived(Long keyword);

    // (5) relations to authUser: friends
    @Query(
            value =
                    "select "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
                            + " , case "
                            + " when u.id = :authUserId then 'authUserRecord' "
                            + " when (ur.relatedUserId is null or code.code = 'soConReset') then 'noRelation' "
                            + " when (code.code = 'requestPending' and :authUserId = ur.initiatorUserId) then 'authUserSentRequest'  "
                            + " when (code.code = 'requestPending' and :authUserId = ur.ResponderUserId) then 'authUserReceivedRequest'  "
                            + " when code.code = 'friends' then 'friends'  "
                            + " when code.code = 'blocked' then 'blocked'  "
                            + " else  'soConError' end as soconStatusEnhanced, "
                            + " ur.socialconnectionId as socialconnectionId "
                            + " from playdatenow08.user u "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " join "
                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
                            + " union all "
                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
                            + " ) ur on u.id = ur.relatedUserId"
                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
                            + " where code.code = 'friends' "
                            + " order by u.id desc"
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionListFriends(Long authUserId);

    // below super old
//    @Query(
//            value =
//                    "select "
//                            + " case when u.id = :keyword then 'authUserRecord' when ur.relatedUser is null then 'noRelation' when c.code is not null then c.code else  'soConError' end as soconStatusEnhanced, "
//                            + " ur.socialconnectionId as socialconnectionId, ur.relationInitiator as relationInitiator, "
//                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "
//
//                            + " from (select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.usertwo as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where userone = :keyword union all select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.userone as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where usertwo  = :keyword ) ur "
//                            + " join playdatenow08.user u on ur.relatedUser = u.id "
//                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
//                            + " left join playdatenow08.code c on ur.soconStatusCodeId = c.id "
//                            + " where c.code = 'friends' "
//            , nativeQuery = true)
//    List<UserSocialConnectionPjo> userSocialConnectionListFriends(Long keyword);

    // (4) relations to authUser: cancelled requests

    @Query(
            value =
                    "select "
                            + " ur.socialconnectionId as socialconnectionId, ur.relationInitiator as relationInitiator, "
                            + " u.id as id, u.about_me as aboutMe, u.address_line1 as addressLine1, u.address_line2 as addressLine2, u.city as city, u.created_at as createdAt, u.email as email, u.first_name as firstName, u.last_name as lastName, u.user_name as userName, u.zip_code as zipCode, u.home_name as homeName, st.full_name as stateName "

                            + " from (select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.usertwo as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where userone = :keyword union all select sc.id as socialconnectionId, sc.userone as relationInitiator, sc.userone as relatedUser, sc.soconstatus_code as soconStatusCodeId from playdatenow08.socialconnection sc where usertwo  = :keyword ) ur "
                            + " join playdatenow08.user u on ur.relatedUser = u.id "
                            + " left join playdatenow08.stateterritory st on u.stateterritory_id = st.id "
                            + " left join playdatenow08.code c on ur.soconStatusCodeId = c.id "
                            + " where c.code = 'requestCancelled' "
            , nativeQuery = true)
    List<UserSocialConnectionPjo> userSocialConnectionListRequestCancelled(Long keyword);

    // (5) user records/objects: is a friend of authUser
//
//    @Query(
//            value= "SELECT u.* FROM playdatenow08.user u where u.id = :keyword "
//            , nativeQuery = true)
//    List<UserMdl> userFriendForPlaydateList(Long keyword);

//    @Query(
//            value=
//                    "SELECT "
//                            + "u.* "
//                            + "FROM playdatenow08.user u "
//                            + " join "
//                            + "(select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
//                            + " union all "
//                            + " select sc.id as socialconnectionId, sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as initiatorUserId, sc.responder_user_id as responderUserId , sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
//                            + " ) ur on u.id = ur.relatedUserId"
//                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
//                            + " where code.code = 'friends' "
//
//            , nativeQuery = true)
//    List<UserMdl> userFriendForPlaydateList(Long authUserId);

    @Query(
            value=
                    "SELECT "
                            + "u.* "
                            + "FROM playdatenow08.user u "
                            + " join "
                            + "(select sc.soconstatus_code_id as soconStatusCodeId, sc.initiator_user_id as relatedUserId from playdatenow08.socialconnection sc where responder_user_id = :authUserId "
                            + " union all "
                            + " select sc.soconstatus_code_id as soconStatusCodeId, sc.responder_user_id as relatedUserId from playdatenow08.socialconnection sc where initiator_user_id = :authUserId "
                            + " ) ur on u.id = ur.relatedUserId"
                            + " left join playdatenow08.code code on ur.soconStatusCodeId = code.id "
                            + " where code.code = 'friends' "

            , nativeQuery = true)
    List<UserMdl> userFriendForPlaydateInviteDropdownList(Long authUserId);



}


package com.f12s.playdatenow08.repositories;

import com.f12s.playdatenow08.models.RsvpMdl;
import com.f12s.playdatenow08.pojos.PlaydateUserUnionRsvpUser;
import com.f12s.playdatenow08.models.PlaydateMdl;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RsvpRpo extends CrudRepository<RsvpMdl, Long> {

    List<RsvpMdl> findAll();

    RsvpMdl findByIdIs(Long id);

    List<RsvpMdl> findAllByPlaydateMdl(PlaydateMdl playdateMdl);

//    @Query(
//            value=
//                    "select u.first_name as firstName, u.user_name as userName, r.kid_count as kidCount, r.adult_count as adultCount, r.rsvp_status as rsvpStatus, r.comment as comment, u.id as userId "
//                    + " from playdatenow08.rsvp r left join playdatenow08.user u on r.createdby_id = u.id  where r.playdate_id = :keyword"
//                    + " union all "
//                    + "select u.first_name as firstName, u.user_name as userName, p.kid_count as kidCount, p.adult_count as adultCount, p.rsvp_status as rsvpStatus, p.comment as comment, u.id as userId "
//                    + " from playdatenow08.playdate p left join playdatenow08.user u on p.createdby_id = u.id  where p.id = :keyword"
//            , nativeQuery = true)
//    List<PlaydateUserUnionRsvpUser> playdateRsvpList(Long keyword);

    @Query(
            value=
                    "select c.code as playdateOrganizerRsvpStatus , 0 as rsvpId, u.first_name as firstName, u.user_name as userName, p.kid_count as kidCount, p.adult_count as adultCount, p.rsvp_status as rsvpStatus, p.comment as comment, u.id as userId "
                            + " from playdatenow08.playdate p "
                            + " left join playdatenow08.user u on p.createdby_id = u.id "
                            + " left join playdatenow08.code c on p.playdate_organizer_rsvp_status_code_id = c.id"
                            + " where p.id = :keyword"
            , nativeQuery = true)
    List<PlaydateUserUnionRsvpUser> playdateRsvpList(Long keyword);


//    @Query(
//            value=
//                    "select c.code as playdateOrganizerRsvpStatus ,  r.id as rsvpId, u.first_name as firstName, u.user_name as userName, r.kid_count as kidCount, r.adult_count as adultCount, r.rsvp_status as rsvpStatus, r.comment as comment, u.id as userId "
//                    + " from playdatenow08.rsvp r "
//                    + " left join playdatenow08.user u on r.createdby_id = u.id "
//                    + " left join playdatenow08.code c on r.playdate_organizer_rsvp_status_code_id = c.id"
//
//
//                    + " where r.playdate_id = :keyword"
//
//
//                    + " union all "
//
//
//                    + "select c.code as playdateOrganizerRsvpStatus , 0 as rsvpId, u.first_name as firstName, u.user_name as userName, p.kid_count as kidCount, p.adult_count as adultCount, p.rsvp_status as rsvpStatus, p.comment as comment, u.id as userId "
//                    + " from playdatenow08.playdate p "
//                    + " left join playdatenow08.user u on p.createdby_id = u.id "
//                    + " left join playdatenow08.code c on \p.playdate_organizer_rsvp_status_code_id = c.id"
//                    + " where p.id = :keyword"
//            , nativeQuery = true)
//    List<PlaydateUserUnionRsvpUser> playdateRsvpList(Long keyword);

// end rpo
}
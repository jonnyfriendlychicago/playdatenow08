package com.f12s.playdatenow08.repositories;

import java.util.Date;
import java.util.List;

import com.f12s.playdatenow08.models.PlaydateMdl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaydateRpo extends CrudRepository<PlaydateMdl, Long> {

    List<PlaydateMdl> findAll();

//    PlaydateMdl findByIdIs(Long id);

    @Query(
            value= "SELECT p.* FROM playdatenow08.playdate p WHERE p.createdBy_id LIKE :keyword and event_date >= curdate() order by p.event_date desc"
            , nativeQuery = true)
    List<PlaydateMdl> userHostedPlaydateListCurrentPlus(Long keyword);

    @Query(
            value= "SELECT p.* FROM playdatenow08.playdate p WHERE p.createdBy_id LIKE :keyword and event_date < curdate() order by p.event_date desc"
            , nativeQuery = true)
    List<PlaydateMdl> userHostedPlaydateListPast(Long keyword);

    List<PlaydateMdl> findPlaydateMdlsByEventDateGreaterThanEqualOrderByEventDateDesc(Date todayDate); // try this out... see if works
    // random thought here: I think one of the issues we need to address here is that these event object/fields go to the millisec, and we actually
    // want to be working merely in whole dates.  For example, if today is Jan 1 and event is Jan 1 sometime, we want that to show here.
    // but if today is Jan 1 12pm and playdate is listed as Jan 1 11am, it will not show up in this list, it will be in the past list.
    // all of this is before we take into account timezone differences between user and aws server. sigh.  lots of further discovery here.

} // end rpo
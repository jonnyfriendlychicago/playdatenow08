package com.f12s.playdatenow08.pojos;

public interface PlaydateUserUnionRsvpUser {
    // note: make sure all the 'getters' below share the same naming structure as the 'select blankety.blank as xyx' in the native query in the rpo
    String getFirstName();
    String getUserName();
    Integer getKidCount();
    Integer getAdultCount();
    String getRsvpStatus();
    String getComment();
//    Integer getUserId();
    Long getUserId();
    Long getRsvpId(); // new
//    String getPlaydateOrganizerRsvpStatus(); // new
    String getRsvpStatusTwo();
    String getRsvpStatusTwoCode();


}


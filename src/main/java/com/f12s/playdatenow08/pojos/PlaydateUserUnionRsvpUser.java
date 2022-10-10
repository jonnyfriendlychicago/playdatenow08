package com.f12s.playdatenow08.pojos;

//public interface PlaydateUserUnionRsvpUser {
//}

public interface PlaydateUserUnionRsvpUser {

    // note: make sure all the 'getters' below share the same naming structure as the 'select blankety.blank as xyx' in the native query in the rpo
    String getUserName();
    Integer getKidCount();
    Integer getAdultCount();
    String getComment();
    Integer getUserId();
    String getRsvpStatus();
    String getFirstName();
}
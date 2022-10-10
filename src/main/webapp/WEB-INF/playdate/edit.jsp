<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/head.jsp" />
<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />

<c:if test="${validationErrorMsg != null}">
  <div class="alert alert-danger" role="alert">
      ${validationErrorMsg}</div>
</c:if>
<div id="playdateCard" class="card p-3 d-md-flex justify-content-start">
  <div id="creatorOrganizerTopButton"class="d-flex justify-content-between">
    <div id="creatorOrganizer" class="card p-2 border-0">
      <p class="m-0 text-secondary" style="font-size: 0.8rem;">Created by
        <c:choose>
          <c:when test="${playdateCreatedById == authUser.id}">
            you (${playdateCreatedByUserName})
          </c:when>
          <c:otherwise>
            ${playdateCreatedByUserName}
          </c:otherwise>
        </c:choose>
        on <fmt:formatDate value="${playdateCreatedAt}" pattern="EEEE" />, <fmt:formatDate value="${playdateCreatedAt}" pattern="MMMM dd" />, <fmt:formatDate value="${playdateCreatedAt}" pattern="yyyy" />, <fmt:formatDate value="${playdateCreatedAt}" pattern="h:mm a" />
      </p>
      <c:if test="${playdateCreatedById == authUser.id}">
        <p class="m-0 text-secondary">You are the organizer of this event.</p>
      </c:if>
    </div> <!-- end creatorOrganizer -->

    <a href="/playdate/${playdate.id}"><button class="btn btn-secondary mb-2">Cancel</button></a>
  </div> <!-- end creatorOrganizerTopButton -->

  <form:form action='/playdate/edit' method='post' modelAttribute='playdate'>

    <div id="eventDeetsAndRsvpRow" class="row mt-3">
      <div id="playdateInfoCol" class="col">
        <form:input type="hidden" path="id" />

        <div class="form-floating mb-3">
          <form:select
                  path="eventStatus"
                  class="form-control"
                  id="eventStatus"
                  placeholder="eventStatus">
            <form:option value="It's on" path="eventStatus">It's on</form:option>
            <form:option value="Canceled" path="eventStatus">Canceled</form:option>
            <form:option value="Pending" path="eventStatus">Pending</form:option>
          </form:select>
          <form:label path="eventStatus" for="eventStatus">Playdate status</form:label>
          <p class="text-danger"><form:errors path="eventStatus" />
        </div>

        <div class="form-floating mb-3">
          <form:input
                  path="eventName"
                  type="text"
                  class="form-control"
                  id="eventName"
                  placeholder="eventName" />
          <form:label path="eventName" for="eventName">Playdate Name</form:label>
          <div id="eventNameHelp" class="form-text">If left blank, your event will be listed as "Playdate." Suggestion: enter a descriptive name (e.g., arts & crafts, bike ride, scooters, legos, etc.).</div>
          <p class="text-danger"><form:errors path="eventName" /></p>
        </div>

        <div class="form-floating mb-3">
          <form:input
                  path="locationName"
                  type="text"
                  class="form-control"
                  id="locationName"
                  placeholder="locationName" />
          <form:label path="locationName" for="locationName">Location Name</form:label>
          <div id="locationHelp" class="form-text">Examples: Our Home or Central Park or Gertie's Ice Cream, etc.</div>
          <p class="text-danger"><form:errors path="locationName" /></p>
        </div>

        <div class="form-floating mb-3">
          <form:input
                  path="eventDate"
                  type="date"
                  class="form-control"
                  id="eventDate"
                  placeholder="eventDate" />
          <form:label path="eventDate" for="eventDate">Event Date</form:label>
          <p class="text-danger"><form:errors path="eventDate" /></p>
        </div>

        <div class="form-floating mb-3">
          <form:select
                  path="startTimeTxt"
                  class="form-control"
                  id="startTimeTxt"
                  placeholder="startTimeTxt">
            <c:forEach items="${startTimeList}" var="record">
              <c:choose>
                <c:when test="${record == playdate.startTimeTxt}">
                  <form:option value="${record}" path="startTimeTxt"
                               selected="true">${record}</form:option>
                </c:when>
                <c:otherwise>
                  <form:option value="${record}" path="startTimeTxt">${record}</form:option>
                </c:otherwise>
              </c:choose>
            </c:forEach>

          </form:select>
          <form:label path="startTimeTxt" for="startTimeTxt">Start Time:</form:label>
          <p class="text-danger"><form:errors path="startTimeTxt" />
        </div>


        <div class="form-floating mb-3">
          <form:input
                  path="locationAddy"
                  type="text"
                  class="form-control"
                  id="locationAddy"
                  placeholder="locationAddy" />
          <form:label path="locationAddy" for="locationAddy">Location Address</form:label>
          <p class="text-danger"><form:errors path="locationAddy" />
        </div>

        <div class="form-floating mb-3">
          <form:textarea
                  path="eventDescription"
                  type="eventDescription"
                  class="form-control"
                  id="eventDescription"
                  placeholder="eventDescription"
                  style="height: 10rem;" />
          <form:label path="eventDescription" for="eventDescription">Playdate Description:</form:label>
          <p class="text-danger"><form:errors path="eventDescription" />
        </div>

        <div class="form-floating mb-3">
          <form:input
                  path="maxCountKids"
                  type="number"
                  class="form-control"
                  id="maxCountKids"
                  placeholder="maxCountKids"
                  min="1"
                  step="1" />
          <form:label path="maxCountKids" for="maxCountKids">Max. Kids Number</form:label>
          <p class="text-danger"><form:errors path="maxCountKids" />
        </div>
      </div><!-- end playdateInfoCol -->

      <div id="rsvpEtcCol" class="col">
        <div id="rsvpTrackingCard" class="card p-3 d-md-flex justify-content-start mb-3">
          <p class="m-0 text-secondary text-center">RSVP Tracking</p>
          <table class="table table-responsive mt-2 table-borderless table-sm">
            <thead class="table-light align-top">
            <tr>
              <th scope="col">RSVPs</th>
              <th scope="col">RSVPed Adults</th>
              <th scope="col">Max Kids</th>
              <th scope="col">RSVPed Kids</th>
              <th scope="col">Open Spots</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <td>${rsvpCount}</td>
              <td>${aggAdultsCount}</td>
              <td>${playdate.maxCountKids}</td>
              <td>${aggKidsCount}</td>
              <td>${openKidsSpots}</td>
            </tr>
            </tbody>
          </table>
        </div><!-- end rsvpTrackingCard -->

        <div id="rsvpCard" class="card p-3 d-md-flex justify-content-start">
          <p class="m-0 text-center" style="font-size: 1.25rem;">Your RSVP</p>
          <div class="form-floating mb-3">
            <form:select
                    path="rsvpStatus"
                    class="form-control"
                    id="rsvpStatus"
                    placeholder="rsvpStatus">
              <form:option value="In" path="rsvpStatus">In</form:option>
              <form:option value="Maybe" path="rsvpStatus">Maybe</form:option>
              <form:option value="Out" path="rsvpStatus">Out</form:option>
            </form:select>
            <form:label path="rsvpStatus" for="rsvpStatus">Status</form:label>
            <p class="text-danger"><form:errors path="rsvpStatus" />
          </div>

          <div class="form-floating mb-3">
            <form:input
                    path="kidCount"
                    type="number"
                    class="form-control"
                    id="kidCount"
                    placeholder="kidCount"
                    min="1"
                    step="1" />
            <form:label path="kidCount" for="kidCount"># of Kids</form:label>
            <p class="text-danger"><form:errors path="kidCount" />
          </div>

          <div class="form-floating mb-3">
            <form:input
                    path="adultCount"
                    type="number"
                    class="form-control"
                    id="adultCount"
                    placeholder="adultCount"
                    min="1"
                    step="1" />
            <form:label path="adultCount" for="adultCount"># of Adults</form:label>
            <p class="text-danger"><form:errors path="adultCount" />
          </div>

          <div class="form-floating mb-3">
            <form:textarea
                    path="comment"
                    type="text"
                    class="form-control"
                    id="comment"
                    placeholder="comment"
                    style="height: 10rem;" />
            <form:label path="comment" for="comment">Comment</form:label>
            <p class="text-danger"><form:errors path="comment" />
          </div>
        </div> <!-- end rsvpCard -->
      </div> <!-- end rsvpEtcCol-->
    </div><!-- end eventDeetsAndRsvpRow -->

    <div id="bottomButtonTray" class="d-flex justify-content-center">
      <div>
        <button type="submit" class="btn btn-primary">Update</button>
      </div>
    </div><!-- end bottomButtonTray -->
  </form:form> <!-- end playdateEditForm -->

  <div id="rsvpListRow" class="row m-1">
    <table class="table table-striped table-hover table-responsive mt-2 caption-top">
      <caption class="text-dark" style="font-size: 1.5rem;">Rsvp List</caption>
      <thead class="border-top-0">
      <tr>
        <th scope="col">Name</th>
        <th scope="col">Status</th>
        <th scope="col"># of Kids</th>
        <th scope="col"># of Adults</th>
        <th scope="col">Comment</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="record" items="${playdateRsvpList}">
        <tr>
          <td><a class="text-decoration-none" href="/profile/${record.userId}">${record.userName}</a></td>
          <td>${record.rsvpStatus}</td>
          <td>${record.kidCount}</td>
          <td>${record.adultCount}</td>
          <td>${record.comment}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

    <div id="otherActionsCard" class="card mt-3 p-3 d-flex justify-content-end">
      <c:choose>
        <c:when test="${not hasOneOrMoreRsvp}">
          <div class="card p-2 border-0">
            <p>Other Actions:</p>
          </div>
          <div>
              <%--
              <form action="/playdate/${playdate.id}" method="post">
                  <!-- <input type="hidden" name="_method" value="delete"> -->
                  <button class="btn btn-danger">Delete this Playdate</button>
              </form>
              --%>
            <!-- 9/27: not entirely sure how/why started using this "form" approach for delete; trying href below instead;  -->
            <a href="/playdate/delete/${playdate.id}"><button class="btn btn-danger mb-2">Delete Playdate</button></a>
          </div>
        </c:when>
        <c:otherwise>
          <p>Got rsvps. Can't delete.</p>
        </c:otherwise>
      </c:choose>
    </div><!-- end otherActionsCard -->
  </div><!-- end rsvpListRow -->
</div><!-- end playdateCard -->

<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />
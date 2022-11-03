<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/headCommon.jsp" />
</head>
<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />

<c:if test="${permissionErrorMsg != null}">
  <div class="alert alert-warning" role="alert">
      ${permissionErrorMsg}</div>
</c:if>
<c:if test="${successMsg != null}">
  <div class="alert alert-success" role="success">${successMsg}</div>
</c:if>
<div id="playdateCard" class="card p-3 d-md-flex justify-content-start mt-3">
  <div id="creationOrganizerButtons" class="d-flex justify-content-between">

    <div id="creatorOrganizer" class="card p-2 border-0">
      <p class="m-0 text-secondary" style="font-size: 0.8rem;">
        Created by
        <c:choose>
          <c:when test="${playdate.userMdl.id == authUser.id}">
            you (${playdate.userMdl.userName})
          </c:when>
          <c:otherwise>
            ${playdate.userMdl.userName}
          </c:otherwise>
        </c:choose>
        on <fmt:formatDate value="${playdate.createdAt}" pattern="EEEE" />, <fmt:formatDate value="${playdate.createdAt}" pattern="MMMM dd" />, <fmt:formatDate value="${playdate.createdAt}" pattern="yyyy" />, <fmt:formatDate value="${playdate.createdAt}" pattern="h:mm a" />
      </p>
      <c:if test="${playdate.userMdl.id == authUser.id}">
        <p class="m-0 text-secondary">You are the organizer of this event.</p>
      </c:if>
    </div>
    <div>
      <c:if test="${playdate.userMdl.id == authUser.id}">
        <a href="/playdate/${playdate.id}/edit"><button class="btn btn-primary mb-2">Edit Playdate</button></a>
      </c:if>
    </div>
  </div><!-- end creationOrganizerButtons -->

  <div class="row mt-3">
    <div id="playdateInfoCol" class="col">
      <div class="card p-2 m-0 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Event Status</p>
        <p class="m-0">${playdate.eventStatus}</p>
      </div>

      <c:if test="${playdate.eventName.length() > 0}">
        <div class="card p-2 m-0 border-0">
          <p class="m-0 text-secondary" style="font-size: 0.8rem;">Playdate Name</p>
          <p class="m-0" style="font-size: 2rem;">${playdate.eventName}</p>
        </div>
      </c:if>

      <div class="card p-2 m-0 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Location</p>
        <p class="m-0" style="font-size: 2rem;">${playdate.locationName}</p>
      </div>

      <div class="card p-2 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Event Date</p>
        <p class="m-0">
          <fmt:formatDate value="${playdate.eventDate}" pattern="EEEE" />, <fmt:formatDate value="${playdate.eventDate}" pattern="MMMM dd" />, <fmt:formatDate value="${playdate.eventDate}" pattern="yyyy" />
        </p>
      </div>

      <div class="card p-2 m-0 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Start Time</p>
        <p class="m-0">${playdate.startTimeTxt}</p>
      </div>

      <div class="card p-2 m-0 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Address</p>
        <p class="m-0">${playdate.locationAddy}</p>
      </div>

      <c:choose>
        <c:when test="${playdate.userMdl.id != authUser.id}">
          <div class="card p-2 m-0 border-0">
            <p class="m-0 text-secondary" style="font-size: 0.8rem;">Organizer</p>
            <p class="m-0">${playdate.userMdl.userName}</p>
          </div>
        </c:when>
        <c:otherwise>
        </c:otherwise>
      </c:choose>

      <div class="card p-2 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Description</p>
        <pre style="white-space: pre-wrap">${playdate.eventDescription}</pre>
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
            <th scope="col">Open Kid Spots</th>
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
      </div>

      <div id="rsvpCard" class="card p-3 d-md-flex justify-content-start">

        <c:choose>
          <c:when test="${authUser.id == playdate.userMdl.id}">
            <p class="m-0 text-center" style="font-size: 1.25rem;">Your RSVP</p>
            <div class="card p-2 m-0 border-0">
              <p class="m-0 text-secondary" style="font-size: 0.8rem;">Status</p>
              <p class="m-0">${playdate.rsvpStatus}</p>
            </div>

            <div class="card p-2 border-0">
              <p class="m-0 text-secondary" style="font-size: 0.8rem;"># of Kids</p>
              <p class="m-0">${playdate.kidCount}</p>
            </div>

            <div class="card p-2 border-0">
              <p class="m-0 text-secondary" style="font-size: 0.8rem;"># of Adults</p>
              <p class="m-0">${playdate.adultCount}</p>
            </div>

            <div class="card p-2 border-0">
              <p class="m-0 text-secondary" style="font-size: 0.8rem;">Comment</p>
              <pre style="white-space: pre-wrap" class="m-0">${playdate.comment}</pre>
            </div>
          </c:when>
          <c:when test="${rsvpExistsCreatedByAuthUser}">
            <div class="row">
              <div class="col"></div>
              <div class="col">
                <p class="m-0 text-center" style="font-size: 1.25rem;">Your
                  RSVP</p>
              </div>

              <div class="col d-flex justify-content-end">
                <a
                        href="/playdate/${playdate.id}/rsvp/${rsvpObjForAuthUser.id}/edit"><button
                        class="btn btn-primary mb-2">Edit</button></a>
              </div>
            </div>

            <div class="card p-2 m-0 border-0">
              <p class="m-0 text-secondary" style="font-size: 0.8rem;">Status</p>
              <p class="m-0">${rsvpObjForAuthUser.rsvpStatus}</p>
            </div>

            <div class="card p-2 border-0">
              <p class="m-0 text-secondary" style="font-size: 0.8rem;"># of Kids</p>
              <p class="m-0">${rsvpObjForAuthUser.kidCount}</p>
            </div>

            <div class="card p-2 border-0">
              <p class="m-0 text-secondary" style="font-size: 0.8rem;"># of Adults</p>
              <p class="m-0">${rsvpObjForAuthUser.adultCount}</p>
            </div>

            <div class="card p-2 border-0">
              <p class="m-0 text-secondary" style="font-size: 0.8rem;">Comment</p>
              <pre style="white-space: pre-wrap" class="m-0">${rsvpObjForAuthUser.comment}</pre>
            </div>

            <div class="card p-2 border-0">
              <p class="m-0 text-secondary" style="font-size: 0.8rem;">
                You RSVPed on <fmt:formatDate value="${rsvpObjForAuthUser.createdAt}" pattern="EEEE" />, <fmt:formatDate value="${rsvpObjForAuthUser.createdAt}" pattern="MMMM dd" />, <fmt:formatDate value="${rsvpObjForAuthUser.createdAt}" pattern="yyyy" />, <fmt:formatDate value="${rsvpObjForAuthUser.createdAt}" pattern="h:mm a" />
              </p>
            </div>

          </c:when>
          <c:otherwise>
            <div class="card p-2 border-0 d-md-flex justify-content-center bg-light mb-2">
              <p class="m-0 text-center text-secondary">You have not yet RSVPed for this event.</p>
            </div>

            <form:form action='/playdate/${playdate.id}/rsvp/create'
                       method='post' modelAttribute='rsvp'>
              <div class="form-floating mb-3">
                <form:select
                        path="rsvpStatus"
                        class="form-control"
                        id="rsvpStatus" placeholder="rsvpStatus">
                  <form:option value="In" path="rsvpStatus">In</form:option>
                  <form:option value="Maybe" path="rsvpStatus">Maybe</form:option>
                  <form:option value="Out" path="rsvpStatus">Out</form:option>
                </form:select>
                <form:label path="rsvpStatus" for="rsvpStatus">Status</form:label>
              </div>

              <div class="form-floating mb-3">
                <form:input
                        path="kidCount"
                        type="number"
                        class="form-control"
                        id="kidCount"
                        placeholder="kidCount"
                        min="1"
                        step="1"
                        value="1" />
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
                        step="1"
                        value="1" />
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

              <div>
                <button type="submit" class="btn btn-primary w-100">Save My Rsvp</button>
              </div>
            </form:form>

          </c:otherwise>
        </c:choose>
      </div> <!-- end rsvpCard -->
    </div><!-- end rsvpEtcCol -->
  </div><!-- end row -->

  <div id="rsvpListRow" class="row m-1">

    <table
            class="table table-striped table-hover table-responsive mt-2 caption-top">
      <caption class="text-dark" style="font-size: 1.5rem;">Rsvp
        List</caption>
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
          <td><a class="text-decoration-none"
                 href="/profile/${record.userId}">${record.userName}</a></td>
          <td>${record.rsvpStatus}</td>
          <td>${record.kidCount}</td>
          <td>${record.adultCount}</td>
          <td><pre style="white-space: pre-wrap" class="m-0">${record.comment}</pre></td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

  </div><!-- end rsvpListRow -->
</div><!-- end playdateCard -->

<jsp:include page="/WEB-INF/include/pageLayoutBottomCommon.jsp" />



</body>
</html>
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
      ${validationErrorMsg}
  </div>
</c:if>
<div id="playdateCard" class="card p-3 d-md-flex justify-content-start">
  <div id="creationOrganizerButtons" class="d-flex justify-content-between">
    <div class="card p-2 border-0">
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
    </div>
    <div></div> <!-- why is this div here? -->
  </div><!-- end creationOrganizerButtons -->

  <div class="row mt-3">
    <div id="playdateInfoCol" class="col">

<%--      <div class="card p-2 m-0 border-0">--%>
<%--        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Event Status</p>--%>
<%--        <p class="m-0">${playdate.eventStatus}</p>--%>
<%--      </div>--%>

      <div class="card p-2 m-0 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Playdate Status</p>
        <p class="m-0">${playdate.playdateStatus.displayValue}</p>
      </div>

      <c:if test="${playdate.eventName.length() > 0}">
        <div class="card p-2 m-0 border-0">
          <p class="m-0 text-secondary" style="font-size: 0.8rem;">Playdate Name</p>
          <p class="m-0" style="font-size: 2rem;">${playdate.eventName}</p>
        </div>
      </c:if>

      <div class="card p-2 m-0 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Location Type</p>
        <p class="m-0">${playdate.locationType.displayValue}</p>
      </div>

      <div class="card p-2 m-0 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Location</p>
        <p class="m-0" style="font-size: 2rem;">${playdate.locationName}</p>
      </div>

      <div class="card p-2 m-0 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Address</p>
        <p class="m-0">${playdate.locationAddy}</p>
      </div>

      <div class="card p-2 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Event Date</p>
        <p class="m-0"><fmt:formatDate value="${playdate.eventDate}" pattern="EEEE" />, <fmt:formatDate value="${playdate.eventDate}" pattern="MMMM dd" />, <fmt:formatDate value="${playdate.eventDate}" pattern="yyyy" />
        </p>
      </div>

      <div class="card p-2 m-0 border-0">
        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Start Time</p>
        <p class="m-0">${playdate.startTimeTxt}</p>
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

<%--      <div class="card p-2 border-0">--%>
<%--        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Max. Number kids</p>--%>
<%--        <p class="m-0">${playdate.maxCountKids}</p>--%>
<%--      </div>--%>

<%--      <div class="card p-2 border-0">--%>
<%--        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Count Rsvp</p>--%>
<%--        <p class="m-0">${rsvpCount}</p>--%>
<%--      </div>--%>

<%--      <div class="card p-2 border-0">--%>
<%--        <p class="m-0 text-secondary" style="font-size: 0.8rem;">Sum Rsvp - rsvpInt</p>--%>
<%--        <p class="m-0">${sumRsvpDotRsvpInt}</p>--%>
<%--      </div>--%>

    </div><!-- end playdateInfoCol -->

    <div id="rsvpCol" class="col">
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
      </div>

      <div id="rsvpCard" class="card p-3 d-md-flex justify-content-start">
        <div class="row">
          <!-- need ids on this, plus spacing cleanup -->
          <div class="col"></div>
          <div class="col">
            <p class="m-0 text-center" style="font-size: 1.25rem;">Your RSVP</p>
          </div>

          <div class="col d-flex justify-content-end">
            <a href="/playdate/${playdate.id}"><button class="btn btn-secondary  mb-2">Cancel</button></a>
          </div>
        </div>

        <form:form action='/rsvp/edit' method='post' modelAttribute='rsvp'>
          <form:input type="hidden" path="id" />

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

<%--          <div class="form-floating mb-3">--%>
<%--            <form:textarea--%>
<%--                    path="comment"--%>
<%--                    type="text"--%>
<%--                    class="form-control"--%>
<%--                    id="comment"--%>
<%--                    placeholder="comment"--%>
<%--                    style="height: 10rem;" />--%>
<%--            <form:label path="comment" for="comment">Comment</form:label>--%>
<%--            <p class="text-danger"><form:errors path="comment" />--%>
<%--          </div>--%>

          <div>
            <button type="submit" class="btn btn-primary w-100">Update</button>
          </div>
        </form:form>

        <div class="d-flex justify-content-center mt-3">
          <%--
          <form action="/rsvp/${rsvp.id}" method="post">
              <input type="hidden" name="_method" value="delete">
              <button class="btn btn-danger">Delete your RSVP</button>
          </form>
          --%>
          <a href="/rsvp/delete/${rsvp.id}"><button class="btn btn-danger mb-2">Delete RSVP</button></a>
        </div>
      </div> <!-- end rsvpCard -->
    </div><!-- end col -->
  </div><!-- end row -->

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
          <td><pre style="white-space: pre-wrap" class="m-0">${record.comment}</pre></td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

  </div><!-- end rsvpListRow -->
</div><!-- end playdateCard -->
<%--<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />--%>
<jsp:include page="/WEB-INF/include/pageLayoutBottomCommon.jsp" />

</body>
</html>
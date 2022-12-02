<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/head.jsp" />
<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />

<%--<h3>sections to do: My Friends; Sent Requests; Invitations; Blocked List;</h3>--%>
<c:if test="${permissionErrorMsg != null}">
    <div class="alert alert-warning mt-3" role="alert">${permissionErrorMsg}</div>
</c:if>

<div id = "profileCardArrayNew" class="container bg-info">
    <h2>Sent Requests</h2>
    <c:forEach var="record" items="${userSocialConnectionListSent}">
        <div id="userCard" class="card m-2">
            <div id="cardbody1" class="card-body">
                <div id="container" class="container">
                    <div class="row">
                        <div class="col-sm-9">
                            <h5 class="card-title fw-bold">
                                <a class="text-decoration-none link-dark" href="/profile/${record.id}">
                                    <c:choose>
                                        <c:when test="${record.firstName.length() > 0 || record.lastName.length() >0}">
                                            ${record.firstName} ${record.lastName} (${record.userName})
                                        </c:when>
                                        <c:otherwise>
                                            ${record.userName}
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </h5>
                            <p>soconStatusEnhanced: ${record.soconStatusEnhanced}</p>
                            <p>relationInitiator: ${record.relationInitiator}</p>


                            <c:if test="${record.city.length() > 0}">
                            <p>${record.city}, ${record.stateName}</p>
                            </c:if>
                        </div>
                        <div class="col-sm-3 d-flex justify-content-end">
                            <c:choose>
                                <c:when test="${record.soconStatusEnhanced == 'authUserRecord'}">
                                    <p>This is your profile.</p>
                                </c:when>
                                <c:when test="${record.soconStatusEnhanced == 'requestPending'}">
                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>
                                        <%--                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value--%>
                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>
                                        <button type="submit" class="btn btn-primary disabled">Add Friend</button>
                                        <p>Friend request awaits response.</p>
                                    </form:form>
                                </c:when>
                                <c:otherwise>
                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>
                                        <%--                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value--%>
                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>
                                        <button type="submit" class="btn btn-primary">Add Friend</button>
                                        <p>(This is the default!)</p>
                                    </form:form>
                                </c:otherwise>
                            </c:choose>
<%--                            <p>some</p>--%>
                        </div>
                    </div> <%-- end row--%>
               </div> <%-- end container--%>
            </div> <%--end cardbody1--%>
        </div> <%-- end userCard --%>
    </c:forEach>
</div>

<div id = "profileCardArrayNew" class="container bg-warning">
    <h2>Received Requests</h2>
    <c:forEach var="record" items="${userSocialConnectionListReceived}">
        <div id="userCard" class="card m-2">
            <div id="cardbody1" class="card-body">
                <div id="container" class="container">
                    <div class="row">
                        <div class="col-sm-9">
                            <h5 class="card-title fw-bold">
                                <a class="text-decoration-none link-dark" href="/profile/${record.id}">
                                    <c:choose>
                                        <c:when test="${record.firstName.length() > 0 || record.lastName.length() >0}">
                                            ${record.firstName} ${record.lastName} (${record.userName})
                                        </c:when>
                                        <c:otherwise>
                                            ${record.userName}
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </h5>
                            <p>soconStatusEnhanced: ${record.soconStatusEnhanced}</p>
                            <p>relationInitiator: ${record.relationInitiator}</p>
                            <p>socialconnectionId: ${record.socialconnectionId}</p>

                            <c:if test="${record.city.length() > 0}">
                                <p>${record.city}, ${record.stateName}</p>
                            </c:if>
                        </div>
                        <div class="col-sm-3 d-flex justify-content-end">
                            <form:form action='/connection/accept/${record.socialconnectionId}' method='post' modelAttribute='soConObj'>
                                <button type="submit" class="btn btn-primary me-2">Accept</button>
                            </form:form>
                            <form:form action='/connection/decline/${record.socialconnectionId}' method='post' modelAttribute='soConObj'>
                                <button type="submit" class="btn btn-secondary">Decline</button>
                            </form:form>
                        </div>
                    </div> <%-- end row--%>
                </div> <%-- end container--%>
            </div> <%--end cardbody1--%>
        </div> <%-- end userCard --%>
    </c:forEach>
</div>

<div id = "profileCardArrayNew" class="container bg-success">
    <h2>Friends</h2>
    <c:forEach var="record" items="${userSocialConnectionListFriends}">
        <div id="userCard" class="card m-2">
            <div id="cardbody1" class="card-body">
                <div id="container" class="container">
                    <div class="row">
                        <div class="col-sm-9">
                            <h5 class="card-title fw-bold">
                                <a class="text-decoration-none link-dark" href="/profile/${record.id}">
                                    <c:choose>
                                        <c:when test="${record.firstName.length() > 0 || record.lastName.length() >0}">
                                            ${record.firstName} ${record.lastName} (${record.userName})
                                        </c:when>
                                        <c:otherwise>
                                            ${record.userName}
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </h5>
                            <p>soconStatusEnhanced: ${record.soconStatusEnhanced}</p>
                            <p>relationInitiator: ${record.relationInitiator}</p>


                            <c:if test="${record.city.length() > 0}">
                                <p>${record.city}, ${record.stateName}</p>
                            </c:if>
                        </div>
                        <div class="col-sm-3 d-flex justify-content-end">
                            <c:choose>
                                <c:when test="${record.soconStatusEnhanced == 'authUserRecord'}">
                                    <p>This is your profile.</p>
                                </c:when>
                                <c:when test="${record.soconStatusEnhanced == 'requestPending'}">
                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>
                                        <%--                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value--%>
                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>
                                        <button type="submit" class="btn btn-primary disabled">Add Friend</button>
                                        <p>Friend request awaits response.</p>
                                    </form:form>
                                </c:when>
                                <c:otherwise>
                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>
                                        <%--                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value--%>
                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>
                                        <button type="submit" class="btn btn-primary">Add Friend</button>
                                        <p>(This is the default!)</p>
                                    </form:form>
                                </c:otherwise>
                            </c:choose>
                                <%--                            <p>some</p>--%>
                        </div>
                    </div> <%-- end row--%>
                </div> <%-- end container--%>
            </div> <%--end cardbody1--%>
        </div> <%-- end userCard --%>
    </c:forEach>
</div>


<div id = "profileCardArrayNew" class="container bg-danger">
    <h2>Blocked Users</h2>
    <c:forEach var="record" items="${userSocialConnectionListFriends}">
        <div id="userCard" class="card m-2">
            <div id="cardbody1" class="card-body">
                <div id="container" class="container">
                    <div class="row">
                        <div class="col-sm-9">
                            <h5 class="card-title fw-bold">
                                <a class="text-decoration-none link-dark" href="/profile/${record.id}">
                                    <c:choose>
                                        <c:when test="${record.firstName.length() > 0 || record.lastName.length() >0}">
                                            ${record.firstName} ${record.lastName} (${record.userName})
                                        </c:when>
                                        <c:otherwise>
                                            ${record.userName}
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </h5>
                            <p>soconStatusEnhanced: ${record.soconStatusEnhanced}</p>
                            <p>relationInitiator: ${record.relationInitiator}</p>


                            <c:if test="${record.city.length() > 0}">
                                <p>${record.city}, ${record.stateName}</p>
                            </c:if>
                        </div>
                        <div class="col-sm-3 d-flex justify-content-end">
                            <c:choose>
                                <c:when test="${record.soconStatusEnhanced == 'authUserRecord'}">
                                    <p>This is your profile.</p>
                                </c:when>
                                <c:when test="${record.soconStatusEnhanced == 'requestPending'}">
                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>
                                        <%--                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value--%>
                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>
                                        <button type="submit" class="btn btn-primary disabled">Add Friend</button>
                                        <p>Friend request awaits response.</p>
                                    </form:form>
                                </c:when>
                                <c:otherwise>
                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>
                                        <%--                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value--%>
                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>
                                        <button type="submit" class="btn btn-primary">Add Friend</button>
                                        <p>(This is the default!)</p>
                                    </form:form>
                                </c:otherwise>
                            </c:choose>
                                <%--                            <p>some</p>--%>
                        </div>
                    </div> <%-- end row--%>
                </div> <%-- end container--%>
            </div> <%--end cardbody1--%>
        </div> <%-- end userCard --%>
    </c:forEach>
</div>

<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />


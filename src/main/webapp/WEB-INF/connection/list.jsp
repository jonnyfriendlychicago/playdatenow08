<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/head.jsp" />
<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />

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
<%--                            <p>initiatorUser: ${record.initiatorUser}</p>--%>
<%--                            <p>socialconnectionId: ${record.socialconnectionId}</p>--%>

                            <c:if test="${record.city.length() > 0}">
                            <p>${record.city}, ${record.stateName}</p>
                            </c:if>
                        </div>
                        <div class="col-sm-3 d-flex justify-content-end">
<%--                            <form:form action='/connection/cancel/${record.socialconnectionId}' method='post' modelAttribute='soConObj'>--%>
<%--                            <form:form action='/connection/cancel/${record.socialconnectionId}' method='post' >--%>
<%--                            note: there is no modelAttribute needed here, b/c there is no object being generated/received/sent; just passing the id--%>
<%--                                starting over here...--%>
                            <form:form action='/socialconnection/cancel' method='post' modelAttribute='soConObjForm'>
                                <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>
                                <form:input type="hidden" path="objectOrigin" value="connectionList"/>
                                <button type="submit" class="btn btn-primary me-2">Cancel Request</button>
                            </form:form>
                        </div>
                    </div> <%-- end row--%>
               </div> <%-- end container--%>
            </div> <%--end cardbody1--%>
        </div> <%-- end userCard --%>
    </c:forEach>
</div>

<%--<div id = "profileCardArrayNew" class="container bg-info">--%>
<%--    <h2>Blocked Users</h2>--%>
<%--    <c:forEach var="record" items="${userSocialConnectionListBlocked}">--%>
<%--        <div id="userCard" class="card m-2">--%>
<%--            <div id="cardbody1" class="card-body">--%>
<%--                <div id="container" class="container">--%>
<%--                    <div class="row">--%>
<%--                        <div class="col-sm-9">--%>
<%--                            <h5 class="card-title fw-bold">--%>
<%--                                <a class="text-decoration-none link-dark" href="/profile/${record.id}">--%>
<%--                                    <c:choose>--%>
<%--                                        <c:when test="${record.firstName.length() > 0 || record.lastName.length() >0}">--%>
<%--                                            ${record.firstName} ${record.lastName} (${record.userName})--%>
<%--                                        </c:when>--%>
<%--                                        <c:otherwise>--%>
<%--                                            ${record.userName}--%>
<%--                                        </c:otherwise>--%>
<%--                                    </c:choose>--%>
<%--                                </a>--%>
<%--                            </h5>--%>
<%--                            <p>initiatorUser: ${record.initiatorUser}</p>--%>
<%--                            <p>socialconnectionId: ${record.socialconnectionId}</p>--%>

<%--                            <c:if test="${record.city.length() > 0}">--%>
<%--                                <p>${record.city}, ${record.stateName}</p>--%>
<%--                            </c:if>--%>
<%--                        </div>--%>
<%--                        <div class="col-sm-3 d-flex justify-content-end">--%>
<%--                            <form:form action='/socialconnection/unblock' method='post' modelAttribute='soConObjForm'>--%>
<%--                                <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>--%>
<%--                                <form:input type="hidden" path="objectOrigin" value="profileRecord"/>--%>
<%--                                <button type="submit" class="btn btn-secondary me-2">Unblock</button>--%>
<%--                            </form:form>--%>
<%--                        </div>--%>
<%--                    </div> &lt;%&ndash; end row&ndash;%&gt;--%>
<%--                </div> &lt;%&ndash; end container&ndash;%&gt;--%>
<%--            </div> &lt;%&ndash;end cardbody1&ndash;%&gt;--%>
<%--        </div> &lt;%&ndash; end userCard &ndash;%&gt;--%>
<%--    </c:forEach>--%>
<%--</div>--%>

<div id = "profileCardArrayNew" class="container bg-info">
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
<%--                            <p>initiatorUser: ${record.initiatorUser}</p>--%>
<%--                            <p>socialconnectionId: ${record.socialconnectionId}</p>--%>

                            <c:if test="${record.city.length() > 0}">
                                <p>${record.city}, ${record.stateName}</p>
                            </c:if>
                        </div>
                        <div class="col-sm-3 d-flex justify-content-end">
<%--                            --%>
<%--                            <form:form action='/socialconnection/unblock' method='post' modelAttribute='soConObjForm'>--%>
<%--                                <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>--%>
<%--                                <button type="submit" class="btn btn-secondary me-2">Unblock</button>--%>
<%--                            </form:form>--%>

                            <form:form action='/socialconnection/accept' method='post' modelAttribute='soConObjForm'>
                                <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>
                                <form:input type="hidden" path="objectOrigin" value="connectionList"/>
                                <button type="submit" class="btn btn-primary me-2">Accept</button>
                            </form:form>

                            <form:form action='/socialconnection/decline' method='post' modelAttribute='soConObjForm'>
                                <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>
                                <form:input type="hidden" path="objectOrigin" value="connectionList"/>
                                <button type="submit" class="btn btn-secondary me-2">Decline</button>
                            </form:form>

                        </div>
                    </div> <%-- end row--%>
                </div> <%-- end container--%>
            </div> <%--end cardbody1--%>
        </div> <%-- end userCard --%>
    </c:forEach>
</div>


<div id = "profileCardArrayNew" class="container bg-info">
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
<%--                            <p>initiatorUser: ${record.initiatorUser}</p>--%>
<%--                            <p>socialconnectionId: ${record.socialconnectionId}</p>--%>

                            <c:if test="${record.city.length() > 0}">
                                <p>${record.city}, ${record.stateName}</p>
                            </c:if>
                        </div>
                        <div class="col-sm-3 d-flex justify-content-end">

<%--                            <form:form action='/socialconnection/unblock' method='post' modelAttribute='soConObjForm'>--%>
<%--                                <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>--%>
<%--                                <button type="submit" class="btn btn-secondary me-2">Unblock</button>--%>
<%--                            </form:form>--%>

<%--                                <form:form action='/socialconnection/unfriend' method='post' modelAttribute='soConObjForm'>--%>
<%--                                    <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>--%>
<%--                                    <button type="submit" class="btn btn-info me-2">Unfriend</button>--%>
<%--                                </form:form>--%>

                        </div>
                    </div> <%-- end row--%>
                </div> <%-- end container--%>
            </div> <%--end cardbody1--%>
        </div> <%-- end userCard --%>
    </c:forEach>
</div>


<%--<div id = "profileCardArrayNew" class="container bg-warning">--%>
<%--    <h2> Received Requests</h2>--%>
<%--    <c:forEach var="record" items="${userSocialConnectionListReceived}">--%>
<%--        <div id="userCard" class="card m-2">--%>
<%--            <div id="cardbody1" class="card-body">--%>
<%--                <div id="container" class="container">--%>
<%--                    <div class="row">--%>
<%--                        <div class="col-sm-9">--%>
<%--                            <h5 class="card-title fw-bold">--%>
<%--                                <a class="text-decoration-none link-dark" href="/profile/${record.id}">--%>
<%--                                    <c:choose>--%>
<%--                                        <c:when test="${record.firstName.length() > 0 || record.lastName.length() >0}">--%>
<%--                                            ${record.firstName} ${record.lastName} (${record.userName})--%>
<%--                                        </c:when>--%>
<%--                                        <c:otherwise>--%>
<%--                                            ${record.userName}--%>
<%--                                        </c:otherwise>--%>
<%--                                    </c:choose>--%>
<%--                                </a>--%>
<%--                            </h5>--%>
<%--                            <p>initiatorUser: ${record.initiatorUser}</p>--%>
<%--                            <p>socialconnectionId: ${record.socialconnectionId}</p>--%>

<%--                            <c:if test="${record.city.length() > 0}">--%>
<%--                                <p>${record.city}, ${record.stateName}</p>--%>
<%--                            </c:if>--%>
<%--                        </div>--%>
<%--                        <div class="col-sm-3 d-flex justify-content-end">--%>
<%--                            <form:form action='/socialconnection/decline' method='post' modelAttribute='soConObjForm'>--%>
<%--                                <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>--%>
<%--                                <button type="submit" class="btn btn-primary me-2">Decline Request</button>--%>
<%--                            </form:form>--%>
<%--                        </div>--%>
<%--                    </div> &lt;%&ndash; end row&ndash;%&gt;--%>
<%--                </div> &lt;%&ndash; end container&ndash;%&gt;--%>
<%--            </div> &lt;%&ndash;end cardbody1&ndash;%&gt;--%>
<%--        </div> &lt;%&ndash; end userCard &ndash;%&gt;--%>
<%--    </c:forEach>--%>
<%--</div>--%>


<%--<div id = "profileCardArrayNew" class="container bg-success">--%>
<%--    <h2>Friends</h2>--%>
<%--    <c:forEach var="record" items="${userSocialConnectionListFriends}">--%>
<%--        <div id="userCard" class="card m-2">--%>
<%--            <div id="cardbody1" class="card-body">--%>
<%--                <div id="container" class="container">--%>
<%--                    <div class="row">--%>
<%--                        <div class="col-sm-9">--%>
<%--                            <h5 class="card-title fw-bold">--%>
<%--                                <a class="text-decoration-none link-dark" href="/profile/${record.id}">--%>
<%--                                    <c:choose>--%>
<%--                                        <c:when test="${record.firstName.length() > 0 || record.lastName.length() >0}">--%>
<%--                                            ${record.firstName} ${record.lastName} (${record.userName})--%>
<%--                                        </c:when>--%>
<%--                                        <c:otherwise>--%>
<%--                                            ${record.userName}--%>
<%--                                        </c:otherwise>--%>
<%--                                    </c:choose>--%>
<%--                                </a>--%>
<%--                            </h5>--%>
<%--                                &lt;%&ndash;                            <p>soconStatusEnhanced: ${record.soconStatusEnhanced}</p>&ndash;%&gt;--%>
<%--                                &lt;%&ndash;                            <p>relationInitiator: ${record.relationInitiator}</p>&ndash;%&gt;--%>


<%--                            <c:if test="${record.city.length() > 0}">--%>
<%--                                <p>${record.city}, ${record.stateName}</p>--%>
<%--                            </c:if>--%>
<%--                        </div>--%>
<%--                        <div class="col-sm-3 d-flex justify-content-end">--%>
<%--                            <c:choose>--%>
<%--                                <c:when test="${record.soconStatusEnhanced == 'authUserRecord'}">--%>
<%--                                    <p>This is your profile.</p>--%>
<%--                                </c:when>--%>
<%--                                <c:when test="${record.soconStatusEnhanced == 'requestPending'}">--%>
<%--                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>--%>
<%--                                        &lt;%&ndash;                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value&ndash;%&gt;--%>
<%--                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>--%>
<%--                                        <button type="submit" class="btn btn-primary disabled">Add Friend</button>--%>
<%--                                        <p>Friend request awaits response.</p>--%>
<%--                                    </form:form>--%>
<%--                                </c:when>--%>
<%--                                <c:otherwise>--%>
<%--                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>--%>
<%--                                        &lt;%&ndash;                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value&ndash;%&gt;--%>
<%--                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>--%>
<%--                                        <button type="submit" class="btn btn-primary">Unfriend</button>--%>
<%--                                    </form:form>--%>
<%--                                </c:otherwise>--%>
<%--                            </c:choose>--%>
<%--                                &lt;%&ndash;                            <p>some</p>&ndash;%&gt;--%>
<%--                        </div>--%>
<%--                    </div> &lt;%&ndash; end row&ndash;%&gt;--%>
<%--                </div> &lt;%&ndash; end container&ndash;%&gt;--%>
<%--            </div> &lt;%&ndash;end cardbody1&ndash;%&gt;--%>
<%--        </div> &lt;%&ndash; end userCard &ndash;%&gt;--%>
<%--    </c:forEach>--%>
<%--</div>--%>

<%--<div id = "profileCardArrayNew" class="container bg-secondary">--%>
<%--    <h2>Cancelled Requests</h2>--%>
<%--    <c:forEach var="record" items="${userSocialConnectionListRequestCancelled}">--%>
<%--        <div id="userCard" class="card m-2">--%>
<%--            <div id="cardbody1" class="card-body">--%>
<%--                <div id="container" class="container">--%>
<%--                    <div class="row">--%>
<%--                        <div class="col-sm-9">--%>
<%--                            <h5 class="card-title fw-bold">--%>
<%--                                <a class="text-decoration-none link-dark" href="/profile/${record.id}">--%>
<%--                                    <c:choose>--%>
<%--                                        <c:when test="${record.firstName.length() > 0 || record.lastName.length() >0}">--%>
<%--                                            ${record.firstName} ${record.lastName} (${record.userName})--%>
<%--                                        </c:when>--%>
<%--                                        <c:otherwise>--%>
<%--                                            ${record.userName}--%>
<%--                                        </c:otherwise>--%>
<%--                                    </c:choose>--%>
<%--                                </a>--%>
<%--                            </h5>--%>
<%--&lt;%&ndash;                            <p>soconStatusEnhanced: ${record.soconStatusEnhanced}</p>&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <p>relationInitiator: ${record.relationInitiator}</p>&ndash;%&gt;--%>
<%--                            <p>socialconnectionId: ${record.socialconnectionId}</p>--%>

<%--                            <c:if test="${record.city.length() > 0}">--%>
<%--                                <p>${record.city}, ${record.stateName}</p>--%>
<%--                            </c:if>--%>
<%--                        </div>--%>
<%--                        <div class="col-sm-3 d-flex justify-content-end">--%>
<%--                            <form:form action='/connection/reactivaterequest/${record.socialconnectionId}' method='post' >--%>
<%--                                <button type="submit" class="btn btn-primary me-2">Reactivate Request</button>--%>
<%--                            </form:form>--%>

<%--                        </div>--%>
<%--                    </div> &lt;%&ndash; end row&ndash;%&gt;--%>
<%--                </div> &lt;%&ndash; end container&ndash;%&gt;--%>
<%--            </div> &lt;%&ndash;end cardbody1&ndash;%&gt;--%>
<%--        </div> &lt;%&ndash; end userCard &ndash;%&gt;--%>
<%--    </c:forEach>--%>
<%--</div>--%>


<%--<div id = "profileCardArrayNew" class="container bg-primary">--%>
<%--    <h2>Declined Requests -- placeholder</h2>--%>
<%--    <c:forEach var="record" items="${userSocialConnectionListFriends}">--%>
<%--        <div id="userCard" class="card m-2">--%>
<%--            <div id="cardbody1" class="card-body">--%>
<%--                <div id="container" class="container">--%>
<%--                    <div class="row">--%>
<%--                        <div class="col-sm-9">--%>
<%--                            <h5 class="card-title fw-bold">--%>
<%--                                <a class="text-decoration-none link-dark" href="/profile/${record.id}">--%>
<%--                                    <c:choose>--%>
<%--                                        <c:when test="${record.firstName.length() > 0 || record.lastName.length() >0}">--%>
<%--                                            ${record.firstName} ${record.lastName} (${record.userName})--%>
<%--                                        </c:when>--%>
<%--                                        <c:otherwise>--%>
<%--                                            ${record.userName}--%>
<%--                                        </c:otherwise>--%>
<%--                                    </c:choose>--%>
<%--                                </a>--%>
<%--                            </h5>--%>
<%--                                &lt;%&ndash;                            <p>soconStatusEnhanced: ${record.soconStatusEnhanced}</p>&ndash;%&gt;--%>
<%--                                &lt;%&ndash;                            <p>relationInitiator: ${record.relationInitiator}</p>&ndash;%&gt;--%>


<%--                            <c:if test="${record.city.length() > 0}">--%>
<%--                                <p>${record.city}, ${record.stateName}</p>--%>
<%--                            </c:if>--%>
<%--                        </div>--%>
<%--                        <div class="col-sm-3 d-flex justify-content-end">--%>
<%--                            <c:choose>--%>
<%--                                <c:when test="${record.soconStatusEnhanced == 'authUserRecord'}">--%>
<%--                                    <p>This is your profile.</p>--%>
<%--                                </c:when>--%>
<%--                                <c:when test="${record.soconStatusEnhanced == 'requestPending'}">--%>
<%--                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>--%>
<%--                                        &lt;%&ndash;                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value&ndash;%&gt;--%>
<%--                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>--%>
<%--                                        <button type="submit" class="btn btn-primary disabled">Add Friend</button>--%>
<%--                                        <p>Friend request awaits response.</p>--%>
<%--                                    </form:form>--%>
<%--                                </c:when>--%>
<%--                                <c:otherwise>--%>
<%--                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>--%>
<%--                                        &lt;%&ndash;                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value&ndash;%&gt;--%>
<%--                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>--%>
<%--                                        <button type="submit" class="btn btn-primary">Unfriend</button>--%>
<%--                                    </form:form>--%>
<%--                                </c:otherwise>--%>
<%--                            </c:choose>--%>
<%--                                &lt;%&ndash;                            <p>some</p>&ndash;%&gt;--%>
<%--                        </div>--%>
<%--                    </div> &lt;%&ndash; end row&ndash;%&gt;--%>
<%--                </div> &lt;%&ndash; end container&ndash;%&gt;--%>
<%--            </div> &lt;%&ndash;end cardbody1&ndash;%&gt;--%>
<%--        </div> &lt;%&ndash; end userCard &ndash;%&gt;--%>
<%--    </c:forEach>--%>
<%--</div>--%>




<%--<div id = "profileCardArrayNew" class="container bg-danger">--%>
<%--    <h2>Blocked Users</h2>--%>
<%--    <c:forEach var="record" items="${userSocialConnectionListFriends}">--%>
<%--        <div id="userCard" class="card m-2">--%>
<%--            <div id="cardbody1" class="card-body">--%>
<%--                <div id="container" class="container">--%>
<%--                    <div class="row">--%>
<%--                        <div class="col-sm-9">--%>
<%--                            <h5 class="card-title fw-bold">--%>
<%--                                <a class="text-decoration-none link-dark" href="/profile/${record.id}">--%>
<%--                                    <c:choose>--%>
<%--                                        <c:when test="${record.firstName.length() > 0 || record.lastName.length() >0}">--%>
<%--                                            ${record.firstName} ${record.lastName} (${record.userName})--%>
<%--                                        </c:when>--%>
<%--                                        <c:otherwise>--%>
<%--                                            ${record.userName}--%>
<%--                                        </c:otherwise>--%>
<%--                                    </c:choose>--%>
<%--                                </a>--%>
<%--                            </h5>--%>
<%--                            <p>soconStatusEnhanced: ${record.soconStatusEnhanced}</p>--%>
<%--                            <p>relationInitiator: ${record.relationInitiator}</p>--%>


<%--                            <c:if test="${record.city.length() > 0}">--%>
<%--                                <p>${record.city}, ${record.stateName}</p>--%>
<%--                            </c:if>--%>
<%--                        </div>--%>
<%--                        <div class="col-sm-3 d-flex justify-content-end">--%>
<%--                            <c:choose>--%>
<%--                                <c:when test="${record.soconStatusEnhanced == 'authUserRecord'}">--%>
<%--                                    <p>This is your profile.</p>--%>
<%--                                </c:when>--%>
<%--                                <c:when test="${record.soconStatusEnhanced == 'requestPending'}">--%>
<%--                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>--%>
<%--                                        &lt;%&ndash;                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value&ndash;%&gt;--%>
<%--                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>--%>
<%--                                        <button type="submit" class="btn btn-primary disabled">Add Friend</button>--%>
<%--                                        <p>Friend request awaits response.</p>--%>
<%--                                    </form:form>--%>
<%--                                </c:when>--%>
<%--                                <c:otherwise>--%>
<%--                                    <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>--%>
<%--                                        &lt;%&ndash;                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value&ndash;%&gt;--%>
<%--                                        <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>--%>
<%--                                        <button type="submit" class="btn btn-primary">Add Friend</button>--%>
<%--                                        <p>(This is the default!)</p>--%>
<%--                                    </form:form>--%>
<%--                                </c:otherwise>--%>
<%--                            </c:choose>--%>
<%--                                &lt;%&ndash;                            <p>some</p>&ndash;%&gt;--%>
<%--                        </div>--%>
<%--                    </div> &lt;%&ndash; end row&ndash;%&gt;--%>
<%--                </div> &lt;%&ndash; end container&ndash;%&gt;--%>
<%--            </div> &lt;%&ndash;end cardbody1&ndash;%&gt;--%>
<%--        </div> &lt;%&ndash; end userCard &ndash;%&gt;--%>
<%--    </c:forEach>--%>
<%--</div>--%>

<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />


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

<div id = "profileCardArrayNew" class="container bg-warning">
    <h2>New soCon List</h2>

    <c:forEach var="record" items="${userSocialConnectionList}">
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

<%--                            <c:choose>--%>
<%--                                <c:when test="${record.soconStatusEnhanced == 'authUserRecord' || record.soconStatusEnhanced == 'blocked' }">--%>
<%--                                </c:when>--%>
<%--                                <c:otherwise>--%>
<%--                                    <form:form action='/socialconnection/block' method='post' modelAttribute='soConObjForm'>--%>
<%--                                        <form:input type="hidden" path="responderUser" value="${record.id}"/>--%>
<%--                                        <button type="submit" class="btn btn-danger">Block</button>--%>
<%--                                    </form:form>--%>
<%--                                </c:otherwise>--%>
<%--                            </c:choose>--%>

<%--                            <c:if test="${record.soconStatusEnhanced != 'blocked' || record.soconStatusEnhanced != 'authUserRecord'}">--%>
<%--                                <form:form action='/socialconnection/block' method='post' modelAttribute='soConObjForm'>--%>
<%--                                    <form:input type="hidden" path="responderUser" value="${record.id}"/>--%>
<%--                                    <button type="submit" class="btn btn-danger">Block</button>--%>
<%--                                </form:form>--%>
<%--                            </c:if>--%>

                            <c:choose>
<%--                                <c:when test="${record.id == authUser.id}">--%>
                                <c:when test="${record.soconStatusEnhanced == 'authUserRecord'}">
                                    <p class="text-secondary">This is your profile.</p>
                                </c:when>
                                <c:when test="${record.soconStatusEnhanced == 'authUserSentRequest'}">
<%--                                    <form:form action='/socialconnection/request' method='post' modelAttribute='soConObjForm'>--%>
<%--                                        <form:input type="hidden" path="responderUser" value="${record.id}"/>--%>
<%--                                        <button type="submit" class="btn btn-primary disabled">Add Friend</button>--%>
<%--                                        <p>Friend request awaits response.</p>--%>
<%--                                    </form:form>--%>
                                    <form:form action='/socialconnection/cancel' method='post' modelAttribute='soConObjForm'>
                                        <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>
                                        <button type="submit" class="btn btn-secondary me-2">Cancel Request</button>
                                    </form:form>
                                </c:when>
                                <c:when test="${record.soconStatusEnhanced == 'authUserReceivedRequest'}">
                                    <form:form action='/socialconnection/accept' method='post' modelAttribute='soConObjForm'>
                                        <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>
                                        <button type="submit" class="btn btn-primary me-2">Accept</button>
                                    </form:form>

                                    <form:form action='/socialconnection/decline' method='post' modelAttribute='soConObjForm'>
                                        <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>
                                        <button type="submit" class="btn btn-secondary me-2">Decline</button>
                                    </form:form>
                                </c:when>

                                <c:when test="${record.soconStatusEnhanced == 'friends'}">
<%--                                    <form:form action='/socialconnection/unfriend' method='post' modelAttribute='soConObjForm'>--%>
<%--                                        <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>--%>
<%--                                        <button type="submit" class="btn btn-info me-2">Unfriend</button>--%>
<%--                                    </form:form>--%>
                                    <p class="text-secondary">You are friends.</p>
                                </c:when>

                                <c:when test="${record.soconStatusEnhanced == 'blocked'}">
                                    <form:form action='/socialconnection/unblock' method='post' modelAttribute='soConObjForm'>
                                        <form:input type="hidden" path="id" value = "${record.socialconnectionId}"/>
                                        <button type="submit" class="btn btn-warning me-2">Unblock</button>
                                    </form:form>
                                </c:when>

                                <c:otherwise>
                                    <form:form action='/socialconnection/request' method='post' modelAttribute='soConObjForm'>
                                        <form:input type="hidden" path="responderUser" value="${record.id}"/>
                                        <button type="submit" class="btn btn-primary">Add Friend</button>
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


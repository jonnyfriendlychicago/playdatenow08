<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/head.jsp" />
<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />


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
<%--                            <p>statusRelationToAuthUser:  ${record.statusRelationToAuthUser}</p>--%>
<%--                            <p>soconStatusCodeId: ${record.soconStatusCodeId}</p>--%>
<%--                            <p>soconStatus: ${record.soconStatus}</p>--%>
                            <p>soconStatusEnhanced: ${record.soconStatusEnhanced}</p>
                            <p>relationInitiator: ${record.relationInitiator}</p>
                            <c:if test="${record.city.length() > 0}">
                            <p>${record.city}, ${record.stateName}</p>
                            </c:if>
                        </div>
                        <div class="col-sm-3 d-flex justify-content-end">
                            <c:choose>
<%--                                <c:when test="${record.id == authUser.id}">--%>
                                <c:when test="${record.soconStatusEnhanced == 'authUserRecord'}">
                                    <p>(This is your profile.)</p>
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


<div id="profileCardArray" class="container bg-info ">
    <h2>Old List</h2>
    <c:forEach var="record" items="${profileList}">
        <%--        <div class="card m-1" style="width: 18rem;">--%>
        <div class="card m-1" >
            <div class="card-body">
                <h5 class="card-title">${record.userName}</h5> <a class="text-decoration-none" href="/profile/${record.id}">View Full Profile</a>
                <p class="card-text">${record.firstName}${record.lastName}</p>
                <pre style="white-space: pre-wrap; max-height: 10rem; overflow: ellipsis;">${record.aboutMe}</pre>
            </div>
            <ul class="list-group list-group-flush">
                <li class="list-group-item">Joined in <fmt:formatDate value="${record.createdAt}" pattern="MMMM" /> <fmt:formatDate value="${record.createdAt}" pattern="yyyy" /></li>
                <li class = "list-group-item">
                    <c:choose>
                        <c:when test="${record.id == authUser.id}">
                            <p>(This is your profile.)</p>
                        </c:when>
                        <c:otherwise>
                            <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>
<%--                                when we got a min, let's put the record.id in the path, so we can remove the hidden input.  just cleaner, it seems.  Also, need added ctl validation for the value--%>
                                <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>
                                <button type="submit" class="btn btn-primary w-100">Add Friend</button>
                            </form:form>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </div>
    </c:forEach>
</div> <!-- end profileCardArray -->
<%--    begin boilerplate for form copied from rsvp--%>


<%--        WORKING ON THIS FORM NOW!!--%>

<div class = "container p-3 bg-success">
    <div class="row m-0 p-0">
        <div class="form-floating col-sm me-5 p-0 ">
            <h2>Drop-down Form</h2>
            <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>

                <form:select
                        path="usertwoUserMdl"
                        class="form-control"
                        id="usertwoUserMdl"
                        placeholder="usertwoUserMdl">
                    <%--                        <form:option value="0" path="stateterritoryMdl">(none)</form:option>--%>
                    <c:forEach items="${profileList}" var="record">
                        <%--                            <c:choose>--%>
                        <%--                                <c:when test="${record == playdate.stateterritoryMdl}">--%>
                        <%--                                    &lt;%&ndash;                            <c:when test="${record == intendedStateTerritoryObj}">&ndash;%&gt;--%>
                        <%--                                    &lt;%&ndash;                                above replaced by below&ndash;%&gt;--%>
                        <%--                                    &lt;%&ndash;                                <c:when test="${record == userProfileObj.stateterritoryMdl  }">&ndash;%&gt;--%>
                        <%--                                    <form:option value="${record.id}" path="stateterritoryMdl" selected="true">${record.abbreviation}</form:option>--%>
                        <%--                                </c:when>--%>
                        <%--                                <c:otherwise>--%>
                        <form:option value="${record.id}" path="usertwoUserMdl">${record.userName}</form:option>
                        <%--                                </c:otherwise>--%>
                        <%--                            </c:choose>--%>
                    </c:forEach>
                </form:select>

                <div>
                    <button type="submit" class="btn btn-primary w-100">Save soCon</button>
                </div>

            </form:form>
        </div>
    </div> <!-- end row -->
</div> <!-- end container -->




<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />

<%--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>
<%--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>--%>
<%--<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>--%>

<%--<jsp:include page="/WEB-INF/include/head.jsp" />--%>
<%--<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />--%>
<%--<jsp:include page="/WEB-INF/include/header.jsp" />--%>
<%--<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />--%>

<%--&lt;%&ndash;<div id="profileCardArray" class="container d-flex flex-wrap">&ndash;%&gt;--%>
<%--<div id="profileCardArray" class="container ">--%>
<%--    <c:forEach var="record" items="${profileList}">--%>
<%--&lt;%&ndash;        <div class="card m-1" style="width: 18rem;">&ndash;%&gt;--%>
<%--        <div class="card m-1" >--%>
<%--            <div class="card-body">--%>
<%--                <h5 class="card-title">${record.userName}</h5> <a class="text-decoration-none" href="/profile/${record.id}">View Full Profile</a>--%>
<%--                <p class="card-text">${record.firstName}${record.lastName}</p>--%>
<%--                <pre style="white-space: pre-wrap; max-height: 10rem; overflow: ellipsis;">${record.aboutMe}</pre>--%>
<%--            </div>--%>
<%--                <ul class="list-group list-group-flush">--%>

<%--&lt;%&ndash;                    <li class="list-group-item">&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <c:choose>&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <c:when test="${record.homeName.length() > 0}">&ndash;%&gt;--%>
<%--&lt;%&ndash;                                Home name: "${record.homeName}"&ndash;%&gt;--%>
<%--&lt;%&ndash;                            </c:when>&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <c:otherwise>&ndash;%&gt;--%>
<%--&lt;%&ndash;                                (No home name declared.)&ndash;%&gt;--%>
<%--&lt;%&ndash;                            </c:otherwise>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        </c:choose>&ndash;%&gt;--%>
<%--&lt;%&ndash;                    </li>&ndash;%&gt;--%>

<%--                    <li class="list-group-item">Joined in <fmt:formatDate value="${record.createdAt}" pattern="MMMM" /> <fmt:formatDate value="${record.createdAt}" pattern="yyyy" /></li>--%>
<%--&lt;%&ndash;                    <li class="list-group-item"><a class="text-decoration-none" href="/profile/${record.id}">View Full Profile</a></li>&ndash;%&gt;--%>
<%--                    <li class = "list-group-item">--%>
<%--                        <c:choose>--%>
<%--                            <c:when test="${record.id == authUser.id}">--%>
<%--                                <p>can't friend yoself, fool!</p>--%>
<%--                            </c:when>--%>
<%--                            <c:otherwise>--%>
<%--                                <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>--%>
<%--                                    <form:input type="hidden" path="usertwoUserMdl" value="${record.id}"/>--%>
<%--                                    <button type="submit" class="btn btn-primary w-100">Add Parent Circle</button>--%>
<%--                                </form:form>--%>
<%--                            </c:otherwise>--%>
<%--                        </c:choose>--%>
<%--                    </li>--%>
<%--                </ul>--%>
<%--        </div>--%>
<%--    </c:forEach>--%>
<%--</div> <!-- end profileCardArray -->--%>
<%--&lt;%&ndash;    begin boilerplate for form copied from rsvp&ndash;%&gt;--%>


<%--&lt;%&ndash;        WORKING ON THIS FORM NOW!!&ndash;%&gt;--%>

<%--<div class = "container m-3 p-3">--%>
<%--    <div class="row m-0 p-0">--%>
<%--        <div class="form-floating col-sm me-5 p-0 ">--%>

<%--            <form:form action='/socialconnection/create' method='post' modelAttribute='soConObj'>--%>

<%--                <form:select--%>
<%--                        path="usertwoUserMdl"--%>
<%--                        class="form-control"--%>
<%--                        id="usertwoUserMdl"--%>
<%--                        placeholder="usertwoUserMdl">--%>
<%--    &lt;%&ndash;                        <form:option value="0" path="stateterritoryMdl">(none)</form:option>&ndash;%&gt;--%>
<%--                    <c:forEach items="${profileList}" var="record">--%>
<%--    &lt;%&ndash;                            <c:choose>&ndash;%&gt;--%>
<%--    &lt;%&ndash;                                <c:when test="${record == playdate.stateterritoryMdl}">&ndash;%&gt;--%>
<%--    &lt;%&ndash;                                    &lt;%&ndash;                            <c:when test="${record == intendedStateTerritoryObj}">&ndash;%&gt;&ndash;%&gt;--%>
<%--    &lt;%&ndash;                                    &lt;%&ndash;                                above replaced by below&ndash;%&gt;&ndash;%&gt;--%>
<%--    &lt;%&ndash;                                    &lt;%&ndash;                                <c:when test="${record == userProfileObj.stateterritoryMdl  }">&ndash;%&gt;&ndash;%&gt;--%>
<%--    &lt;%&ndash;                                    <form:option value="${record.id}" path="stateterritoryMdl" selected="true">${record.abbreviation}</form:option>&ndash;%&gt;--%>
<%--    &lt;%&ndash;                                </c:when>&ndash;%&gt;--%>
<%--    &lt;%&ndash;                                <c:otherwise>&ndash;%&gt;--%>
<%--                                <form:option value="${record.id}" path="usertwoUserMdl">${record.userName}</form:option>--%>
<%--    &lt;%&ndash;                                </c:otherwise>&ndash;%&gt;--%>
<%--    &lt;%&ndash;                            </c:choose>&ndash;%&gt;--%>
<%--                    </c:forEach>--%>
<%--                </form:select>--%>

<%--                <div>--%>
<%--                    <button type="submit" class="btn btn-primary w-100">Save soCon</button>--%>
<%--                </div>--%>

<%--            </form:form>--%>
<%--        </div>--%>
<%--    </div> <!-- end row -->--%>
<%--</div> <!-- end container -->--%>




<%--<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />--%>
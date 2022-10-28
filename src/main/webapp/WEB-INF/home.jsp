<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/headCommon.jsp" />
<%--without this polyfill, the map won't load, which will then put you on an endless downward spiral of plug/chug to make it work.  stop the insanity.--%>
<script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script>
<script
        type = "module"
        src="/js/map.js"
<%--async    this async is not in google documentation, but the program still works.  leave?--%>
></script>

</head>

<jsp:include page="/WEB-INF/include/bodyDesign.jsp" />
<jsp:include page="/WEB-INF/include/header.jsp" />
<jsp:include page="/WEB-INF/include/pageLayoutTop.jsp" />

<div id="playdateList" class="container-sm my-5 table-responsive">

    <c:if test="${successMsg != null}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${successMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <h2>This is the home screen. Oct 25</h2>
    <h3>We are about to map some awesome stuff.</h3>
    <h4>I have nothing more to say! :-)</h4>

    <div id="map" class="card p-2 m-2 border-0" style="height: 25rem"></div>

</div><!-- end playdateList -->

<jsp:include page="/WEB-INF/include/pageLayoutBottomCommon.jsp" />

<script
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBcebr3h87oaEoYNm0ix80FMxuoBzh7nMI&callback=initMap"
        defer
></script>

</body>
</html>
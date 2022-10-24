<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/head.jsp" />
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

    <h2>This is the home screen.</h2>
    <h3>We are about to map some awesome stuff.</h3>
    <h4>I have nothing more to say! :-)</h4>

    <div id="mapInfo" class="card p-2 m-2 border-0 bg-info">
        <p>map info</p>
    </div>

    <div id="map" class="card p-2 m-2 border-0 bg-info" style="height: 25rem">
<%--        <p>map itself</p>--%>
    </div>

    <script>
        var map;
        function initMap() {
            map = new google.maps.Map(document.getElementById("map"), {
                center: {lat: 45.334120, lng:   -121.69868},
                zoom: 12
            });
        }
    </script>
    <script
            src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBcebr3h87oaEoYNm0ix80FMxuoBzh7nMI&callback=initMap"  async defer
    ></script>

</div><!-- end playdateList -->
<jsp:include page="/WEB-INF/include/pageLayoutBottom.jsp" />
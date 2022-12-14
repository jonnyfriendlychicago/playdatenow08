<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/headCommon.jsp" />

<script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script> <%--without this polyfill, the map won't load.--%>
<script src="https://unpkg.com/axios@1.1.2/dist/axios.min.js"></script> <%-- this enables the geocode that's embedded in initMap function--%>

<script>
    function initMap(callback) {

        // (1) instantiate internal js object composed of kvp, with numbers in the var position, for consumption in downstream activities.
        const latLongObj = {lat: 1, lng: 1}; //

        axios
            // (2) get geocoded values from incoming java variable addy
            .get('https://maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: "${staticAddy}",
                    key: 'AIzaSyBcebr3h87oaEoYNm0ix80FMxuoBzh7nMI'
                }
            }) // end get

            .then(function(response) {
                console.log(response) // log full gma results for analysis

                // (3) display geocode values on the page
                const formattedAddy = response.data.results[0].formatted_address;

                const addyComp = response.data.results[0].address_components[0].long_name; // start of additional analysis/dev we're gonna do in a little bit
                console.log ("addyComp: ", addyComp);

                const formattedAddyOutput =
                "<p class = 'm-0 text-danger text-center'>" + formattedAddy + "</p>" // this works, yay!

                // below is an interesting take on doing this, that will not work, to hell with it!
                <%--`<div class="hello">--%>
                <%--    <p> ${formattedAddy} </p>--%>
                <%--</div>--%>
                <%--`--%>
                document.getElementById('validatedAddyCard2').innerHTML =  formattedAddyOutput;

                // // begin: below is the ol-skool way of doing it, but why would you do that do yourself now that above method is (finally) working??
                // const newEl = document.createElement('p');
                // newEl.setAttribute('id','validatedAddy');
                // newEl.setAttribute('class', "m-0 text-secondary text-center");
                // const newText = document.createTextNode(formattedAddy);
                // newEl.appendChild(newText);
                // const position = document.getElementById('validatedAddyCard');
                // // console.log("position:", position)
                // position.appendChild(newEl);

                // (4) update the js object (creatd in step 1 above), then execute function that builds the map, passing in the js object.  The full function is below axios, and contains the display-on-page functionality.
                latLongObj.lat = response.data.results[0].geometry.location.lat;
                latLongObj.lng = response.data.results[0].geometry.location.lng;
                buildMapFromGeo(latLongObj, formattedAddy);

            }) // end then

            .catch(function(error) {
                console.log(error)
            }) // end catch

        function buildMapFromGeo(latLong, formattedAddy, formattedAddyOutput) {

            // const locationTitle = "OurHome";

            // prework: prepare HTML elements for insertion into map's info window; hopefully we find a better way to do this, and discard this step in the near future.
            // const infoWindowContent = document.createElement('h6');
            // infoWindowContent.setAttribute('id','xyz');
            // infoWindowContent.setAttribute('class', "m-0 text-center");
            // const newText = document.createTextNode(formattedAddy);
            // infoWindowContent.appendChild(newText);
            //
            // const lineBreak1 = document.createElement('br');
            // const position = infoWindowContent;
            // position.appendChild(lineBreak1);

            // const newEl2 = document.createElement('p');
            // newEl2.setAttribute('id','validatedAddy');
            // newEl2.setAttribute('class', "m-0 text-secondary text-center");
            // const newText2 = document.createTextNode(formattedAddy);
            //
            // const position2 = lineBreak1;
            // position2.appendChild(newEl2);

            // (4a) instantiate new map object
            const map1 = new google.maps.Map(document.getElementById(
                "mapCard1"),
                {zoom: 12, center: latLong,}
            );

            // (4b) instantiate/populate array of marker objects
            const markerObjList = [
                {coords: latLong, content: "<p class = 'm-0 text-success text-center'>" + formattedAddy + "</p>" },
                // {coords: latLong, content: "<h2>" + formattedAddy + "</h2>" },
                // {coords: latLong, content: formattedAddyOutput}, // this doesn't work
                // put more items in here as needed.
            ];

            // (4c) run the addMarker function on all objects in the marker object array
            for (let i= 0; i < markerObjList.length; i++ ) {
                addMarkers(markerObjList[i])
            };

            function addMarkers (props) {
                const marker = new google.maps.Marker ( {
                    map: map1,
                    position: props.coords,
                    content: props.content,
                })

                if(props.content) {
                    const infoWindow = new google.maps.InfoWindow({
                        content: props.content
                    });

                    marker.addListener('click', function () {
                        infoWindow.open(map1, marker);
                    });
                } // end if-props
            };  // end function addMarkers
        } // end function: buildMapFromGeo
    }  // end function: initMap
</script>

<script>
    function myFunction() {
        document.getElementById("changeThis").innerHTML = "Paragraph changed.";
    }
</script>


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

    <h4>Whut the fuck you do?</h4>

    <div id = "validatedAddyCard2" class="card p-2 m-2 border-0" style="height: 5rem">
    </div>

    <div id="mapCard1" class="card p-2 m-2 border-0" style="height: 20rem"></div>

    <div class="card">
        <div id="cardBody1" class="card-body">
          <h5 class="card-title">Your Home</h5>
          <p id="changeThis" class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
            <button type="button" onclick="myFunction()">Try it</button>

        </div>
                                            <%--        <img src="/img/usa.jpeg" class="card-img-top" alt="...">--%>
                                            <%--        <div id="mapCard1" class="card-img-top" ></div>--%>
<%--        <div id="mapCard1" class="card p-2 m-2 border-0" style="height: 20rem"></div>--%>
                                            <%--        <ul class="list-group list-group-flush">--%>
                                            <%--          <li class="list-group-item">An item</li>--%>
                                            <%--          <li class="list-group-item">A second item</li>--%>
                                            <%--          <li class="list-group-item">A third item</li>--%>
                                            <%--        </ul>--%>
                                            <%--        <div class="card-body">--%>
                                            <%--          <a href="#" class="card-link">Card link</a>--%>
                                            <%--          <a href="#" class="card-link">Another link</a>--%>
                                            <%--        </div>--%>
    </div>

</div> <!-- end playdateList -->

<jsp:include page="/WEB-INF/include/pageLayoutBottomCommon.jsp" />

<script
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBcebr3h87oaEoYNm0ix80FMxuoBzh7nMI&callback=initMap"
        defer
></script>

</body>
</html>
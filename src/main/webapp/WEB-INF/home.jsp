<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/include/headCommon.jsp" />
<%--without this polyfill, the map won't load, which will then put you on an endless downward spiral of plug/chug to make it work.  stop the insanity.--%>
<script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script>

<%--<script--%>
<%--        type = "module"--%>
<%--        src="/js/map.js"--%>
<%--></script>--%>

<script src="https://unpkg.com/axios@1.1.2/dist/axios.min.js"></script>

<script>


    // begin: second mapping stuff
    function initMap(callback) {

        let incomingAddy = "xyz";
        incomingAddy = "${staticAddy}";

        // let latBaby = response.data.results[0].geometry.location.lat;
        let latBaby = 1;
        console.log ("latBaby:" + latBaby);

        // let lngBaby = response.data.results[0].geometry.location.lng;
        let lngBaby = 2;
        console.log ("lngBaby:" + lngBaby);

        const latLongObj = {lat: 1, lng: 1};

        axios
            .get('https://maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: incomingAddy,
                    key: 'AIzaSyBcebr3h87oaEoYNm0ix80FMxuoBzh7nMI'
                }
            })

            .then(function(response) {
                // log results
                console.log(response)

                const formattedAddy = response.data.results[0].formatted_address;
                console.log ("formattedAddy:" + formattedAddy);

                const addyComp = response.data.results[0].address_components[0].long_name;
                console.log ("addyComp:" + addyComp);

                latBaby = response.data.results[0].geometry.location.lat;
                console.log ("latBaby UPDATED BY GOOGLE:" + latBaby);

                lngBaby = response.data.results[0].geometry.location.lng;
                console.log ("lngBaby UPDATED BY GOOGLE:" + lngBaby);

                latLongObj.lat = response.data.results[0].geometry.location.lat;
                latLongObj.lng = response.data.results[0].geometry.location.lng;

                console.log("latLongNateSol: ", latLongObj)

                buildMapFromGeo(latLongObj);

                // const latBaby = response.data.results[0].geometry.location.lat;
                // console.log ("latBaby:" + latBaby);
                //
                // const lngBaby = response.data.results[0].geometry.location.lng;
                // console.log ("lngBaby:" + lngBaby);

                // below is supposed to be the "right" way, but it doesn't work. WTF?  so, this entire thing is replaced by dom manipulation approach that follows
                <%--const formattedAddyOutput = --%>
                <%--    `--%>
                <%--    <div class="hello">--%>
                <%--        <p>${formattedAddy} and hello to you</p>--%>
                <%--    </div>--%>
                <%--     `;--%>
                <%-- //'<div class="hello">' + '<p>' + '${formattedAddy} and hello to you' + '</p>' + '</div>' --%>
                // document.getElementById('formattedAddyOnScreenContainer2').innerHTML =  formattedAddyOutput;

                // begin: below is the ol-skool way of doing it, but it works.
                const newEl = document.createElement('p');
                newEl.setAttribute('id','validatedAddy');
                newEl.setAttribute('class', "m-0 text-secondary text-center");
                const newText = document.createTextNode("formattedAddy: " + formattedAddy);
                newEl.appendChild(newText);
                const position = document.getElementById('validatedAddyCard');
                position.appendChild(newEl);





            })

            .catch(function(error) {
                console.log(error)
            })


        // console.log("theDigits: " + axios.return()); // this doesn't work

// // all of below is working spendidly to produce a simple oregon map
//
//         // set map position and zoom level
//         // const locoCenter = { lat: 45.334120, lng: -121.69868 };
//         const locoCenter = { lat: latBaby, lng: lngBaby  };
//
//         const mapDeets = {
//             zoom: 12,
//             center: locoCenter,
//         }
//
//         // const map = new google.maps.Map(document.getElementById(
//         const locoMap = new google.maps.Map(document.getElementById(
//                 "map2"),
//             mapDeets
//         );
//
//         const markerList = [
//             // {coords: {lat: 45.30447594851857, lng: -121.75421673233924}, content: '<h1>My Hometown</h1>'},
//             {coords: {lat: latBaby, lng: lngBaby}, content: '<h1>Joni and Chachi</h1>'},
//             // {coords: {lat: 45.33231072131942, lng: -121.66490406283796}, content: '<h2>Eat here!</h2>'}
//         ];
//
//         console.log("markerList: ",  markerList)
//         for (let i= 0; i < markerList.length; i++ ) {
//             addMarkerz(markerList[i])
//         };
//
//         function addMarkerz (props) {
//             const marker = new google.maps.Marker ( {
//                 map: locoMap,
//                 position: props.coords,
//                 content: props.content,
//             })
//
//             if(props.content) {
//
//                 const infoWindow = new google.maps.InfoWindow({
//                     content: props.content
//                 });
//
//                 marker.addListener('click', function () {
//                     // marker.addEventListener('click', function () {
//                     // infoWindow.open(map, marker);
//                     infoWindow.open(locoMap, marker);
//                 });
//             }
//
//         };  // end function addMarkerz

        function buildMapFromGeo(latLong) {
            // all of below is working splendidly to produce a simple oregon map

            // set map position and zoom level
            // const locoCenter = { lat: 45.334120, lng: -121.69868 };
            // const locoCenter = { lat: latBaby, lng: lngBaby  };
            const locoCenter = latLong;

            const mapDeets = {
                zoom: 12,
                center: locoCenter,
            }

            // const map = new google.maps.Map(document.getElementById(
            const locoMap = new google.maps.Map(document.getElementById(
                    "map2"),
                mapDeets
            );

            const markerList = [
                // {coords: {lat: 45.30447594851857, lng: -121.75421673233924}, content: '<h1>My Hometown</h1>'},
                {coords: {lat: latBaby, lng: lngBaby}, content: '<h1>Joni and Chachi</h1>'},
                // {coords: {lat: 45.33231072131942, lng: -121.66490406283796}, content: '<h2>Eat here!</h2>'}
            ];

            console.log("markerList: ",  markerList)
            for (let i= 0; i < markerList.length; i++ ) {
                addMarkerz(markerList[i])
            };

            function addMarkerz (props) {
                const marker = new google.maps.Marker ( {
                    map: locoMap,
                    position: props.coords,
                    content: props.content,
                })

                if(props.content) {

                    const infoWindow = new google.maps.InfoWindow({
                        content: props.content
                    });

                    marker.addListener('click', function () {
                        // marker.addEventListener('click', function () {
                        // infoWindow.open(map, marker);
                        infoWindow.open(locoMap, marker);
                    });
                }

            };  // end function addMarkerz


        }

    }  // end: initMap


</script>

<%--<script--%>
<%--&lt;%&ndash;        this works!&ndash;%&gt;--%>
<%--        type = "module"--%>
<%--        src="/js/geocode.js"--%>
<%--></script>--%>

<%--<script src="https://unpkg.com/axios@1.1.2/dist/axios.min.js"></script>--%>


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

    <h4>Welcome to home screen. I have nothing more to say right now! :-)</h4>

<%--    <div id="map1" class="card p-2 m-2 border-0" style="height: 20rem"></div>--%>

    <div id = "validatedAddyCard" class="card p-2 m-2 border-0" style="height: 5rem">
        <p>StaticAddy delivered from controller: ${staticAddy}</p>

    </div>

    <div id="map2" class="card p-2 m-2 border-0" style="height: 20rem"></div>

<%--    see comments below about this div--%>
<%--    <div id = "formattedAddyOnScreenContainer2"></div>--%>

</div><!-- end playdateList -->

<jsp:include page="/WEB-INF/include/pageLayoutBottomCommon.jsp" />

<script
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBcebr3h87oaEoYNm0ix80FMxuoBzh7nMI&callback=initMap"
        defer
></script>

</body>
</html>
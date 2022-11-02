//
// function initMap() {
//     // set map position and zoom level
//     const loco = { lat: 45.334120, lng: -121.69868 }; // the mere inclusion of this line makes the script fail.  why????
//
//     const map = new google.maps.Map(document.getElementById("map"), {
//         zoom: 12,
//         center: loco,
//         // center: {lat: 45.334120, lng: -121.69868},
//     });
//
//     // add a marker, syntax 1
//     const marker = new google.maps.Marker({
//         position: loco,
//         // position: {lat: 45.334120, lng:   -121.69868},
//         map: map
//         // title: "hello!" // this line does NOT work
//     });
//
//     // // add a marker, syntax 2 -- this doensn't work ?!?
//     // new google.maps.Marker({
//     //     position: {lat: 45.334120, lng:   -121.69868},
//     //     map,
//     //     title: "Hello World!",
//     // });
//
//
// }  // end function = initMap
//
// // window.initMap = initMap();
// window.initMap = initMap;

////////////// above 100 working, trying below now instead


function initMap(callback) {

    // // begin: geocode section
    //
    // const incomingAddy = "2834 W Palmer Chicago IL";
    // // let incomingAddy = "xyz";
    // // incomingAddy === ${staticAddy};
    //
    // axios
    //     .get('https://maps.googleapis.com/maps/api/geocode/json', {
    //         params: {
    //             address: incomingAddy,
    //             key: 'AIzaSyBcebr3h87oaEoYNm0ix80FMxuoBzh7nMI'
    //         }
    //     })
    //
    //     .then(function(response) {
    //         // log results
    //         console.log(response)
    //
    //         const formattedAddy = response.data.results[0].formatted_address;
    //         console.log ("formattedAddy:" + formattedAddy);
    //
    //         const addyComp = response.data.results[0].address_components[0].long_name;
    //         console.log ("addyComp:" + addyComp);
    //
    //         const lat2 = response.data.results[0].geometry.location.lat;
    //         console.log ("lat2:" + lat2);
    //
    //         const lng2 = response.data.results[0].geometry.location.lng;
    //         console.log ("lng2:" + lng2);
    //
    //         // below is supposed to be the "right" way, but it doesn't work. WTF?  so, this entire thing is replaced by dom manipulation approach that follows
    //         // <%--const formattedAddyOutput =--%>
    //         //     <%--    '<div class="hello">' + '<p>' + '${formattedAddy} and hello to you' + '</p>' + '</div>'--%>
    //         // document.getElementById('formattedAddyOnScreenContainer2').innerHTML =  formattedAddyOutput;
    //
    //         const newEl = document.createElement('p');
    //         newEl.setAttribute('id','validatedAddy');
    //         newEl.setAttribute('class', "m-0 text-secondary text-center");
    //         const newText = document.createTextNode(formattedAddy);
    //         newEl.appendChild(newText);
    //         const position = document.getElementById('validatedAddyCard');
    //         position.appendChild(newEl);
    //
    //     })
    //
    //     .catch(function(error) {
    //         console.log(error)
    //     })
    //
    // // end: geocode section


    // all of below is working spendidly to produce a simple oregon map

    // set map position and zoom level
    const locoCenter = { lat: 45.334120, lng: -121.69868 }; // the mere inclusion of this line makes the script fail.  why????

    const mapDeets = {
        zoom: 12,
        center: locoCenter,
    }

    // const map = new google.maps.Map(document.getElementById(
    const locoMap = new google.maps.Map(document.getElementById(
        "map1"),
        mapDeets
    );

    // // add a marker, syntax 1
    // const marker = new google.maps.Marker({
    //     position: locoCenter,
    //     // map: map
    //     map: locoMap
    //
    //     })
    //
    // const infoWindow = new google.maps.InfoWindow({
    //         content: '<h1> Hola, Friend!</h1>'
    // });
    //
    // marker.addListener('click', function () {
    // // marker.addEventListener('click', function () {
    // infoWindow.open(map, marker);
    // });

    // below = first take on multi marker

    // addMarkerz( {lat: 45.30447594851857, lng: -121.75421673233924} );
    //
    // addMarkerz( {lat: 45.33231072131942, lng: -121.66490406283796} );
    //
    // function addMarkerz (coords) {
    //     const marker = new google.maps.Marker ( {
    //         position: coords,
    //         map: locoMap,
    //     })
    // };  // end function addMarkerz


    // below NEXT take on multimarker
    // addMarkerz({coords: {lat: 45.30447594851857, lng: -121.75421673233924}, content: '<h1> Hola, Friend!</h1>'});
    // addMarkerz({coords: {lat: 45.33231072131942, lng: -121.66490406283796}, content: '<h2> Eat here!</h2>'});

    // above replaced by below

    const markerList = [
        {coords: {lat: 45.30447594851857, lng: -121.75421673233924}, content: '<h1>Huck it!</h1>'},
        {coords: {lat: 45.33231072131942, lng: -121.66490406283796}, content: '<h2>Eat here!</h2>'}
    ]

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

}  // end function = initMap
window.initMap = initMap;

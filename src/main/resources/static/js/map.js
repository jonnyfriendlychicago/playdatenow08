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


function initMap() {
    // set map position and zoom level
    const locoCenter = { lat: 45.334120, lng: -121.69868 }; // the mere inclusion of this line makes the script fail.  why????

    const mapDeets = {
        zoom: 12,
        center: locoCenter,
    }

    // const map = new google.maps.Map(document.getElementById(
    const locoMap = new google.maps.Map(document.getElementById(
        "map"),
        mapDeets
    );

    // add a marker, syntax 1
    const marker = new google.maps.Marker({
        position: locoCenter,
        // map: map
        map: locoMap

        })

    const infoWindow = new google.maps.InfoWindow({
            content: '<h1> Hola, Friend!</h1>'
    });

    marker.addListener('click', function() {
        infoWindow.open(map, marker);
    });


}  // end function = initMap

window.initMap = initMap;

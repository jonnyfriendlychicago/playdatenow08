
function initMap() {
    // set map position and zoom level
    const youLoco = { lat: 45.334120, lng: -121.69868 }; // the mere inclusion of this line makes the script fail.  why????

    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 12,
        center: youLoco,
        // center: {lat: 45.334120, lng: -121.69868},
    });

    // add a marker, syntax 1
    const marker = new google.maps.Marker({
        position: youLoco,
        // position: {lat: 45.334120, lng:   -121.69868},
        map: map
        // title: "hello!" // this line does NOT work
    });

    // // add a marker, syntax 2 -- this doensn't work ?!?
    // new google.maps.Marker({
    //     position: {lat: 45.334120, lng:   -121.69868},
    //     map,
    //     title: "Hello World!",
    // });


}  // end function = initMap

// window.initMap = initMap();
window.initMap = initMap;
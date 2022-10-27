function initMap() {
    const map = new google.maps.Map(document.getElementById("map"), {
        center: {lat: 45.334120, lng:   -121.69868},
        zoom: 12
    });
};

// window.initMap = initMap();
window.initMap = initMap;
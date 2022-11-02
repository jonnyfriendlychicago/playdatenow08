geocode();

function geocode() {
    let incomingAddy = "433 N Lafayette Griffith IN";
    axios
        .get('https://maps.googleapis.com/maps/api/geocode/json', {
            params: {
                address: incomingAddy,
                key: 'AIzaSyBcebr3h87oaEoYNm0ix80FMxuoBzh7nMI'
            }
        })
        .then(function(response) {
            console.log(response)
        })

        .catch(function(error) {
                console.log(error);
            }
        );
}
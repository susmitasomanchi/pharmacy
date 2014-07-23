function loadgooglemap(latt, lngt) {

	var map_canvas = document.getElementById('map_canvas');
	var map_options = {
		center : new google.maps.LatLng(latt, lngt),
		zoom : 17,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(map_canvas, map_options);

	var marker = new google.maps.Marker({
		position : new google.maps.LatLng(latt, lngt),
		map : map
	});

	google.maps.event.addDomListener(window, 'load', initialize);
}

function initialize() {
	var map_canvas = document.getElementById('map_canvas');
	var map_options = {
		center : new google.maps.LatLng(17.445735,78.349828),
		zoom : 18,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(map_canvas, map_options);

	var marker = new google.maps.Marker({
		position : new google.maps.LatLng(17.445735,78.349828),
		map : map
	});
}

google.maps.event.addDomListener(window, 'load', initialize); 
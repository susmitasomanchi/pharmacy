function initialize() {
	var map_canvas = document.getElementById('map_canvas');
	var map_options = {
		center : new google.maps.LatLng(17.445735,78.349828),
		zoom : 12,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(map_canvas, map_options);

	var marker1 = new google.maps.Marker({
		position : new google.maps.LatLng(17.445735,78.349828),
		map : map
	});
	

	
	
	/*var marker2 = new google.maps.Marker({
		position : new google.maps.LatLng(17.4512756,78.3940295),
		map : map
	});
	
	var marker3 = new google.maps.Marker({
		position : new google.maps.LatLng(17.443563,78.3397746),
		map : map
	});*/
}

function initialize1(){
	var map_canvas = document.getElementById('map_canvas');
	var map_options = {
		center : new google.maps.LatLng(17.445735,78.349828),
		zoom : 12,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(map_canvas, map_options);

	var marker1 = new google.maps.Marker({
		position : new google.maps.LatLng(17.4512756,78.3940295),
		map : map
	});
	

}

function initialize2(){
	var map_canvas = document.getElementById('map_canvas');
	var map_options = {
		center : new google.maps.LatLng(17.445735,78.349828),
		zoom : 12,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(map_canvas, map_options);

	var marker1 = new google.maps.Marker({
		position : new google.maps.LatLng(17.443563,78.3397746),
		map : map
	});
	

}

google.maps.event.addDomListener(window, 'load', initialize); 
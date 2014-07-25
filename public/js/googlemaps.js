function initialize() {
	
	var map_canvas = document.getElementById('map_canvas');
	var map_options = {
		center : new google.maps.LatLng(17.445735,78.349828),
		zoom : 12,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(map_canvas, map_options);

	var marker = new google.maps.Marker({
		position : new google.maps.LatLng(17.445735,78.349828),
		map : map
	});
	
	var map_canvas2 = document.getElementById('map_canvas2');
	var map_options2 = {
		center : new google.maps.LatLng(17.443563,78.3397746),
		zoom : 12,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var map2 = new google.maps.Map(map_canvas2, map_options2);

	var marker2 = new google.maps.Marker({
		position : new google.maps.LatLng(17.443563,78.3397746),
		map : map2
	});
	
	var map_canvas3 = document.getElementById('map_canvas3');
	var map_options3 = {
		center : new google.maps.LatLng(17.4512756,78.3940295),
		zoom : 12,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var map3 = new google.maps.Map(map_canvas3, map_options3);

	var marker3 = new google.maps.Marker({
		position : new google.maps.LatLng(17.4512756,78.3940295),
		map : map3
	});
	
	var map_canvas4 = document.getElementById('map_canvas4');
	var map_options4 = {
		center : new google.maps.LatLng(17.445735,78.349828),
		zoom : 12,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var map4 = new google.maps.Map(map_canvas4, map_options4);

	var marker4 = new google.maps.Marker({
		position : new google.maps.LatLng(17.445735,78.349828),
		map : map4
	});
	
	var map_canvas5 = document.getElementById('map_canvas5');
	var map_options5 = {
		center : new google.maps.LatLng(17.443563,78.3397746),
		zoom : 12,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var map5 = new google.maps.Map(map_canvas5, map_options5);

	var marker5 = new google.maps.Marker({
		position : new google.maps.LatLng(17.443563,78.3397746),
		map : map5
	});
	
	var map_canvas6 = document.getElementById('map_canvas6');
	var map_options6 = {
		center : new google.maps.LatLng(17.4512756,78.3940295),
		zoom : 12,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var map6 = new google.maps.Map(map_canvas6, map_options6);

	var marker6 = new google.maps.Marker({
		position : new google.maps.LatLng(17.4512756,78.3940295),
		map : map6
	});
}

google.maps.event.addDomListener(window, 'load', initialize); 
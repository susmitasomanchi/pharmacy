// This is a manifest file that'll be compiled into application.js, which will include all the files
// listed below.
//
// Any JavaScript/Coffee file within this directory, lib/assets/javascripts, vendor/assets/javascripts,
// or vendor/assets/javascripts of plugins, if any, can be referenced here using a relative path.
//
// It's not advisable to add code directly here, but if you do, it'll appear at the bottom of the
// compiled file.
//
// Read Sprockets README (https://github.com/sstephenson/sprockets#sprockets-directives) for details
// about supported directives.
//
//= require jquery
jquery_ujs;
//= require turbolinks
//= require_tree .


function runtopcarousel(offset){
	var modelcount = $("#top-carousel>table").size();
	$("#top-carousel").carouFredSel({
		items : 4,
		circular : false,
		infinite : false,
		auto : false,
		prev : {
			button : "#top-carousel_prev",
		},
		next : {
			button : "#top-carousel_next",
		},
		scroll : {
			items : 1,
			duration : 1000,
		},
		items :{
			start: offset,
		}
	}); 
}




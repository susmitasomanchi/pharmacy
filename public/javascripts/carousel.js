$(document).ready(function() {

				$("#banner-carousel").carouFredSel({
					items : 1,
					circular : true,
					infinite : true,
					auto : {
						items : 1,
						play : true,
						timeoutDuration : 4000,
					},

					scroll : {
						items : 1,
						fx : "crossfade",
						duration : 1000,
						pauseOnHover : true
					}

					/*
					 prev	: {
					 button	: "#models-carousel_prev",
					 key		: "left"
					 },
					 next	: {
					 button	: "#models-carousel_next",
					 key		: "right"
					 },
					 */

				});

				$("#models-carousel").carouFredSel({
					items : 4,
					circular : false,
					infinite : false,
					auto : false,
					prev : {
						button : "#models-carousel_prev",
						//key : "left"
					},
					next : {
						button : "#models-carousel_next",
						//key : "right"
					},
					scroll : {
						duration : 2000,
					}
				});

				$("#platforms-carousel").carouFredSel({
					items : 4,
					circular : false,
					infinite : false,
					auto : false,
					prev : {
						button : "#platforms-carousel_prev",
						//key : "left"
					},
					next : {
						button : "#platforms-carousel_next",
						//key : "right"
					},
					scroll : {
						duration : 2000,
					}
				});

				$("#our-work-carousel").carouFredSel({
					items : 4,
					circular : false,
					infinite : false,
					auto : false,
					prev : {
						button : "#our-work-carousel_prev",
						//key : "left"
					},
					next : {
						button : "#our-work-carousel_next",
						//key : "right"
					},
					scroll : {
						duration : 2000,
					}
				});

				$("#about-us-carousel").carouFredSel({
					items : 4,
					circular : false,
					infinite : false,
					auto : false,
					prev : {
						button : "#about-us-carousel_prev",
						//key : "left"
					},
					next : {
						button : "#about-us-carousel_next",
						//key : "right"
					},
					scroll : {
						duration : 2000,
					}
				});

			});
/**
 * 
 */
window.onload=function(){
	$(".chzn").chosen();
	$(".boottip").tooltip();
	$('.datepicker').datepicker();
	$("[data-toggle=popover]").popover();
	
	var nowTemp = new Date();
	var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
	$('.datepicker-past').datepicker({
		  onRender: function(date) {
			    return date.valueOf() > now.valueOf() ? 'disabled' : '';
		  }
	}).on('changeDate', function(ev) {
		$(this).datepicker('hide');
	});
}


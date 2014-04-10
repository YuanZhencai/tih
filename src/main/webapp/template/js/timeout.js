$(document).ready(function(){
  $("#ajax").click(function(){
    $.ajaxSetup({
    	contentType:"application/x-www-form-urlencoded;charset=utf-8",
    	complete:function(xhr,status){
    		if (xhr.getResponseHeader("sessionstatus") == "timeout") {
    			window.location.replace(document.URL); 
    		}
    	}
    });
    $.ajax();
  });
});

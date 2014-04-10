var fileUploadStyle={
				addlistener :function(id){
					$("#"+id).live('change',this,function(b){
						var changeClassHtml=document.getElementsByClassName("name","td");
						for(var i=0;i <changeClassHtml.length;i++){
							var changeHtml=changeClassHtml[i].html();
							changeClassHtml[i].empty();
							changeClassHtml[i].html("<div  style='width:100px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;-icab-text-overflow: ellipsis;-khtml-text-overflow: ellipsis;-moz-text-overflow: ellipsis;-webkit-text-overflow: ellipsis;'><a href='javascript:void(0);' title='"+changeHtml+"' >"+changeHtml+"</a></div>");
						}
					});
				}
		}
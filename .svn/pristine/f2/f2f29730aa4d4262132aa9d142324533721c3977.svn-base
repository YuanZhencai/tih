
//菜单栏效果
 jQuery(document).ready(function () {
	/*此代码执行后的DOM完全加载*/
	
	/*获取系统当前时间*/
	jQuery('#down_right_timer').timer({ format: "yy\u5e74mm\u6708dd\u65e5 W HH:MM:ss" });
    
	/*改变thedefault缓动效果 - 将影响到/ slideDown方法效果基本show：*/
    jQuery.easing.def = "easeOutBounce";
	//jQuery.easing.def = "easeOutQuad";
            
	/*单击事件处理程序绑定到链接：*/       
	jQuery('li.button a').click(function (e) {
		/*下拉列表中找到对应于当前部分：*/
        var dropDown = jQuery(this).parent().next();
		
		/*关闭所有其他降下来的部分，除了目前的 */
        jQuery('.dropdown').not(dropDown).slideUp('slow');
        dropDown.slideToggle('slow');
		
		/*防止违约事件（这将是导航到链接的地址浏览器）*/
        e.preventDefault();
    })
			
	/*模拟鼠标点击事件--> 设置选中一级菜单*/
	//
    if(jQuery("#menuTwo").val()!=null&&jQuery("#menuTwo").val()!=""&&jQuery("#menuOne").val()!=null&&jQuery("#menuOne").val()!=""){
	//jQuery("#"+jQuery("#menuOne").val()).trigger("click");
    var url=jQuery("#requestContextPath").val();
	jQuery("#"+jQuery("#menuOne").val()).css("background-image","url("+url+"/images/nav-a-1.png)");
	jQuery("#"+jQuery("#menuOne").val()).css("color","#FFFFFF");
	/*-> 设置选中二级菜单样式*/
	jQuery("#"+jQuery("#menuTwo").val()).css("background-image","url("+url+"/images/nav-a-2.png)");
    }
	
	/*页面加载时设置按钮提示框*/
	jQuery("button").tooltip({
		left: 5,top:20
	});
	/*页面加载时设置按钮提示框*/
	jQuery(":text").tooltip({
		left: 20,top:-10
	});
	
	jQuery("select").tooltip({
		left: 20,top:-10
	});
	
	//jQuery("a").tooltip({
	//	showURL: false
	//});
	//jQuery("a").each(function() {
	//	alert(this.type);
	//	if(this.tooltipText==null){
	//		
	//		jQuery("a").tooltip({
	//			showURL: false
	//		});}
	// });

});
 
 /*页面局部刷新时设置按钮提示框*/
function showTooltip(){
	jQuery("button").each(function() {
		if(this.tooltipText==null){
		//if(this.title!=null && this.title.length>0 && jQuery(this).text()!="ui-button"){
		jQuery(this).tooltip({
			left: 5,top:20
		});}
	 });
	jQuery(":text").each(function() {
		if(this.tooltipText==null){
			jQuery(this).tooltip({
			left: 20,top:-10
		});}
	 });
	jQuery("select").each(function() {
		if(this.tooltipText==null){
			jQuery(this).tooltip({
			left: 20,top:-10
		});}
	 });

 }
        
//鼠标停留效果
function LiStyleOver(obj) {
	obj.style.border = "1px solid #7cb9dd";
}

 //鼠标离开效果
function LiStyleOut(obj) {
	obj.style.border = "1px solid #d5e8f6";
}
		
/*--获取网页传递的参数--*/
function request(paras){ 
	var url = location.href; 
	var paraString = url.substring(url.indexOf("?")+1,url.length).split("&"); 
	var paraObj = {} 
 	for (i=0; j=paraString[i]; i++){ 
		paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=")+1,j.length); 
    } 
	var returnValue = paraObj[paras.toLowerCase()]; 
	if(typeof(returnValue)=="undefined"){ 
    	return ""; 
    }else{ 
        return returnValue; 
    } 
}

/*--菜单栏隐藏--*/
//var arrowpic1="/images/control_left.gif";//最好用服务器的绝对路径
//var arrowpic2="/images/control_right.gif";

//--------------------- 状态初始 ----------------------
//var MENU_SWITCH=1;

//------------------ 点击时切换菜单栏状态 ---------------------
function enter_menu_ctrl()
{
	if(getCookie("MENU_SWITCH")==null){
		SetCookie("MENU_SWITCH","1");
	}
	if(getCookie("MENU_SWITCH")==0)
    {
		jQuery("#menu_id").css("display","block");
		jQuery("#control_id").css("left","172px");
		jQuery("#main_id").css("left","180px");
        SetCookie("MENU_SWITCH","1");
        jQuery("#menu_ctrl_img_left").css("display","block");
        jQuery("#menu_ctrl_img_right").css("display","none");
		//document.getElementById("menu_ctrl_img_left").src=arrowpic1;
		//document.getElementById("menu_ctrl_img_right").src=arrowpic2;
		document.getElementById("menu_ctrl_tit").title="\u5173\u95ed\u83dc\u5355";
     }
     else
     {
		jQuery("#menu_id").css("display","none");
		jQuery("#control_id").css("left","0px");
		jQuery("#main_id").css("left","8px");
       	SetCookie("MENU_SWITCH","0");
       	jQuery("#menu_ctrl_img_left").css("display","none");
        jQuery("#menu_ctrl_img_right").css("display","block");
       	//document.getElementById("menu_ctrl_img_left").src=arrowpic1;
		//document.getElementById("menu_ctrl_img_right").src=arrowpic2;
		document.getElementById("menu_ctrl_tit").title="\u6253\u5f00\u83dc\u5355";
     }
}

//------------------ 默认时记忆中的菜单栏状态 ---------------------
function ready_menu_ctrl()
{
	if(getCookie("MENU_SWITCH")==null){
		SetCookie("MENU_SWITCH","1");
	}
	if(getCookie("MENU_SWITCH")==1)
    {
		jQuery("#menu_id").css("display","block");
 		jQuery("#control_id").css("left","172px");
 		jQuery("#main_id").css("left","180px");
         jQuery("#menu_ctrl_img_left").css("display","block");
         jQuery("#menu_ctrl_img_right").css("display","none");
 		//document.getElementById("menu_ctrl_img_left").src=arrowpic1;
 		//document.getElementById("menu_ctrl_img_right").src=arrowpic2;
 		document.getElementById("menu_ctrl_tit").title="\u5173\u95ed\u83dc\u5355";
     }
     else
     {
    	jQuery("#menu_id").css("display","none");
 		jQuery("#control_id").css("left","0px");
 		jQuery("#main_id").css("left","8px");
 		jQuery("#menu_ctrl_img_left").css("display","none");
        jQuery("#menu_ctrl_img_right").css("display","block");
        //document.getElementById("menu_ctrl_img_left").src=arrowpic1;
 		//document.getElementById("menu_ctrl_img_right").src=arrowpic2;
 		document.getElementById("menu_ctrl_tit").title="\u6253\u5f00\u83dc\u5355";
     }
}

/*
 * 关闭验证消息框
 * id 消息ID
 */
function closeMsg(id){
	jQuery("#"+id).css("z-index","0");
	jQuery("#"+id).css("position","absolute");
	//jQuery("#"+id).css("float","right");
	jQuery("#"+id).css("left","400px");
	jQuery("#"+id).css("top","-10px");
	 setTimeout(function(){
		 jQuery("#"+id).hide('slow');
		 jQuery("#"+id).css("display","none");
		 }, 3000);
	//var intervalID = window.setTimeout(jQuery("#"+id).css("display","none"),5000);
	//var intervalID2 = window.setTimeout(jQuery("#"+id).css("display","block"),5000);
}

//写cookies函数
function SetCookie(name, value)//两个参数，一个是cookie的名子，一个是值
{
    delCookie(name);
    var Days = 30; //此 cookie 将被保存 30 天
    var exp = new Date();    //new Date("December 31, 9998");
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}
function getCookie(name)//取cookies函数        
{
    var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|jQuery)"));
    if (arr != null) return unescape(arr[2]); return "";

}
function delCookie(name)//删除cookie
{
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

//------------------------------------------------------------

//源系统物料代码自动根据源代码赋值
function updateByMaterialCode(vars) {
	var sysCode = document.getElementById("material_addOriginalVendorNo");
	sysCode.value=vars.value;
}

//关闭添加panel
function handleAddComplete(args) {
	if(args.validationFailed){
  } else {
  	addPanel.hide();
  }
}
//关闭修改panel
function handleUpdateComplete(args) {
	if(args.validationFailed){
  } else {
  	updatePanel.hide();
  }
}
/*
* 弹出层验证事件
* panel 需要关闭的层
* args
*/
function handleComplete(panel,args){
	 if(args.validationFailed){
	    } else {
	    	panel.hide();
	    }
}

/**
* 弹出非Ajax数据提交等待层
* 用法：<h:form id="packingMaterialInputForm" prependId="false" onsubmit="statusComplete();">
*/
function statusComplete(){
	 jQuery("#submitDiv").css("display","");
}


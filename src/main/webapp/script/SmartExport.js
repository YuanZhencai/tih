//全局intervalID
var intervalID;

// 只要刷新页面就要执行
setInerval();

// 判断是否要周期执行
function setInerval() {
	var intId = sessionStorage.getItem('intervalID');
	if (intId != null) {
		interval();
	}
}

// 周期执行
function interval() {
	intervalID = window.clearInterval(intervalID);
	intervalID = window.setInterval(getExportStatus, 1000);
	sessionStorage.setItem('intervalID', intervalID);
}

function getExportStatus() {
	exportStatus();
}

function stopInterval(xhr, status, args) {
	console.info("stopInterval");
	if (args.status == "1") {
		window.clearInterval(intervalID);
		sessionStorage.clear();
		exportNociceVar.show();
		console.info("success");
	}
}
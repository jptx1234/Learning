function loadXML(file) {
	var xmlDox;
	try 
	{
		var xmlhttp = new window.XMLHttpRequest();
		xmlhttp.open("GET", file, false);
		xmlhttp.send(null);
		xmlDoc = xmlhttp.responseXML.documentElement;
	} catch (e) {
		alert("XML加载失败");
	}
	return xmlDoc;
}
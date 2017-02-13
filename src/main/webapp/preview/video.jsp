<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>视频文件</title>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	<link rel="stylesheet" type="text/css" href="../media/css/preview.css" />
    <script type="text/javascript" src="../media/js/jquery-1.9.0.js"></script>
    <script type="text/javascript" src="../media/plugins/html5media/html5media.min.js"></script>
</head>

<body>
	<div class="title">
		<span class="text">${file == null ? "文件" : file.name }</span>
		<span class="time">上传时间：${file == null ? "未知" : file.uploadTimeStr }</span>
	</div>
	<div class="main">
        <video src="${path }" width="1000" height="550" style="margin:0 auto;" controls preload></video>
	</div>
</body>
</html>

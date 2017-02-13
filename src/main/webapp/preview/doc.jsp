<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>文档查看</title>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	<link rel="stylesheet" type="text/css" href="../media/css/preview.css" />
</head>

<body>
	<div class="title">
		<span class="text">${file == null ? "文件" : file.name }</span>
		<span class="time">上传时间：${file == null ? "未知" : file.uploadTimeStr }</span>
	</div>
	<div class="main">
		<iframe id="file_iframe" src="${path }" frameborder="0" marginwidth="0" marginheight="0" width="100%"></iframe>
	</div>
</body>
</html>

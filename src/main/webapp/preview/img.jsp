<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>ͼƬ�鿴</title>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	<link rel="stylesheet" type="text/css" href="../media/css/preview.css" />
</head>

<body>
	<div class="title">
		<span class="text">${file == null ? "�ļ�" : file.name }</span>
		<span class="time">�ϴ�ʱ�䣺${file == null ? "δ֪" : file.uploadTimeStr }</span>
	</div>
	<div class="main">
		<img src="${path }" width="100%" height="100%"/>
	</div>
</body>
</html>
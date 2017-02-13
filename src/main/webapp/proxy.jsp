<%@page language="java" pageEncoding="GBK"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
</head>
<body>
<form id="proxy" action="${url }" method="post">
    <textarea style="display:none;" name="json">${data }</textarea>
</form>
<script type="text/javascript">
document.getElementById("proxy").submit();
</script>
</body>
</html>

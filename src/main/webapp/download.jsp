<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page import="java.io.*" %>
<%
String fileName = (String)request.getAttribute("fileName");
String filePath = (String)request.getAttribute("filePath");

fileName = new String(fileName.getBytes("GBK"),"ISO8859-1");
response.reset();    //����reset�����������ļ�������
response.setContentType( "application/octet-stream;charset=UTF-8");
response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
OutputStream os = response.getOutputStream();
FileInputStream inStream = new FileInputStream(filePath);
byte[] b = new byte[inStream.available()];
int len = 0;
//ѭ��ȡ�����е����� 
while ((len = inStream.read(b)) != -1) {
    os.write(b, 0, len);
}
inStream.close();
os.flush();
out.clear();
out = pageContext.pushBody();
%>
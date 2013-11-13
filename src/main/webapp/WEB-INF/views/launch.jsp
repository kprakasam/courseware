<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<title>Assignment Applets</title>
<meta http-equiv="Content-Type" content="text/html; utf-8">
</head>
<body>
  <noscript>A browser with JavaScript enabled is required for this page to operate properly.</noscript>
  <script src="http://www.java.com/js/deployJava.js"></script>
  <script>
  	var attributes = {};
  	var parameters = { jnlp_href: 'jars/applet.jnlp', 
  	        		   jnlp_embedded: '<c:out value="${jnlp}"/>' };

	deployJava.runApplet(attributes, parameters, '1.7');
  </script>
</body>
</html>
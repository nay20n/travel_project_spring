<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>에러 발생</title>
	<style>
		body {
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        body>* {
            max-width: 100%;
            height: auto;
        }
	</style>
</head>
<body>
	<img src="<c:url value='/resources/img/page404.png'/>" alt="404 에러" onclick="goHome()"/>
	<script>
        function goHome() {
            window.location.href = "<c:url value='/'/>";
        }
    </script>
</body>
</html>
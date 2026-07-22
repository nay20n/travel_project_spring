<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>일정표 - 메일(월)</title>
	<link rel="stylesheet" href="../../resources/css/MainMonth.css"/>
	<link rel="stylesheet" href="../../resources/css/Header.css"/>
	<link rel="stylesheet" href="../../resources/css/BoardTitle.css"/>
	<script type="text/javascript" src="../../resources/js/jquery-4.0.0.min.js"></script>
	<script type="text/javascript" src="../../resources/js/MainMonth.js"></script>
	<script type="text/javascript" src="../../resources/js/Header.js"></script>
	<script type="text/javascript" src="../../resources/js/BoardTitle.js"></script>
	
	<script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
	
	<script>
		$(function() {
			clickInput();
		});
	</script>
</head>
<body>
	<%@ include file="HeaderEditBoard.jsp"%>
	<%@ include file="BoardTitle.jsp"%>
	<div id="daterange" data-start="${startDate}" data-end="${endDate}"></div>
	<div id="main" data-bno="${bno}">
		<div>
			<h2 class="date fl">2026년 5월</h2>
			<div class="changeView fl bs" >
				<span>일</span><span>주</span><span>월</span>
			</div>
         </div>
         <div class="bs"> 
         	<input type="text" id="calender"/>
		</div>
		<button class="bs">일정 확정하기</button>
	</div>
</body>
</html>
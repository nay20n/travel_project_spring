<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>여행 기간은?</title>
		<link rel="styleSheet" href="../resources/css/Date.css"/>
		<script type="text/javascript" src="../resources/js/jquery-4.0.0.min.js"></script>
		<script type="text/javascript" src="../resources/js/Date.js"></script>
		
		<script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
		<!-- <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script> -->
		<script type="text/javascript" src="../resources/js/daterangepicker.js"></script>
		<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
	</head>
	<body onload="clickInput()">
		<div id="header">
			<img class = "fl" src="//assets.triple.guide/images/btn-com-back@4x.png"/>
		</div>
		<div id="banner" data-lat="${lat}" data-lng="${lng}">
			<img src="https://triple.guide/trips/static/icons/ico_schedule-emoji.svg"/>
			<div>여행 기간은?</div>
			<div>원하는 기간을 선택해 주세요.</div>
		</div>
		<div id="selectDate">
			<input type="text" readonly/>
		</div>
		<div id="nextBtn">
			<div class="bs">다음</div>
		</div>
	</body>
</html>
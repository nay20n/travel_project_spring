<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>떠나고 싶은 도시는?</title>
		<link rel="stylesheet" href="../resources/css/ArrPlace.css"/>
		<script type="text/javascript" src="../resources/js/jquery-4.0.0.min.js"></script>
		<script type="text/javascript" src="../resources/js/ArrPlace.js"></script>
	</head>
	<body>
		<div id="header">
			<img class = "fl" src="//assets.triple.guide/images/btn-com-back@4x.png"/>
			<div class="fr">2/3</div>
		</div>
		<div id="banner">
			<img src="https://triple.guide/trips/static/icons/ico_earth-emoji.svg"/>
			<div>떠나고 싶은 도시는?</div>
			<div>도시 1곳을 선택해 주세요.</div>
		</div>
		<div id="recomendPlace">
			<div>추천 여행지</div>
			<div>
				<c:forEach var="list" items="${recommendedPlace}">
					<span data-placeId="${list.placeId}">${list.spot}</span>
				</c:forEach>
			</div>
		</div>
		<div id="searchPlace">
			<div>직접 여행지 찾기</div>
			<img class="bs" src = "../resources/img/searchPlaceImg.png"> 
		</div>
		<div id="aiPlace">
			<div>사진으로 여행지 찾기</div>
			<img class="bs" src = "../resources/img/AIPlaceImg.png"> 
		</div>
		<div id="nextBtn">
			<div class="bs">다음</div>
		</div>
	</body>
</html>
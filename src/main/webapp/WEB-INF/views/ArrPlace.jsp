<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>떠나고 싶은 도시는?</title>
		<script>
            // prettier-ignore
            (g=>{var h,a,k,p="The Google Maps JavaScript API",c="google",l="importLibrary",q="__ib__",m=document,b=window;b=b[c]||(b[c]={});var d=b.maps||(b.maps={}),r=new Set,e=new URLSearchParams,u=()=>h||(h=new Promise(async(f,n)=>{await (a=m.createElement("script"));e.set("libraries",[...r]+"");for(k in g)e.set(k.replace(/[A-Z]/g,t=>"_"+t[0].toLowerCase()),g[k]);e.set("callback",c+".maps."+q);a.src=`https://maps.\${c}apis.com/maps/api/js?`+e;d[q]=f;a.onerror=()=>h=n(Error(p+" could not load."));a.nonce=m.querySelector("script[nonce]")?.nonce||"";m.head.append(a)}));d[l]?console.warn(p+" only loads once. Ignoring:",g):d[l]=(f,...n)=>r.add(f)&&u().then(()=>d[l](f,...n))})({
                key: "${googleApiKey}"
            });
        </script>
		<link rel="stylesheet" href="../resources/css/ArrPlace.css"/>
		<script type="text/javascript" src="../resources/js/jquery-4.0.0.min.js"></script>
		<script type="text/javascript" src="../resources/js/ArrPlace.js"></script>
		<script type="module" src="../resources/js/mapIndex.js"></script>
	</head>
	<body>
		<div id="header">
			<img class = "fl" src="//assets.triple.guide/images/btn-com-back@4x.png"/>
		</div>
		<div id="banner">
			<img src="https://triple.guide/trips/static/icons/ico_earth-emoji.svg"/>
			<div>떠나고 싶은 도시는?</div>
			<div>도시 1곳을 선택해 주세요.<br/>빨간 마커를 클릭 시 지워집니다.</div>
		</div>
		<div id="recomendPlace">
			<div>추천 여행지</div>
			<div>
				<c:forEach var="list" items="${recommendedPlace}">
					<span data-placeid="${list.arrPlaceId}" data-lat="${list.LAT}" data-lng="${list.LNG}">${list.spot}</span>
				</c:forEach>
			</div>
		</div>
		<div id="searchPlace">
			<div>직접 여행지 찾기</div>
			
			<div id="map" class="bs">
	 			<gmp-map
			    center="35.76, 127.03"
			    zoom="7"
			    map-id="MAP_1_ID">
				</gmp-map>
			    <div>
				    <gmp-place-autocomplete id="autocomplete" placeholder="장소 검색"></gmp-place-autocomplete>
	            </div>
			</div>
 
 
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
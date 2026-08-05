<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
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
        <link rel="icon" href="../resources/img/favicon.ico"/>
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
			<input type="file" name="file"/>
			<img class="bs" src = "../resources/img/장소대체이미지.png"> 
			<div>
				<!-- <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
					<path stroke-linecap="round" stroke-linejoin="round" d="m2.25 15.75 5.159-5.159a2.25 2.25 0 0 1 3.182 0l5.159 5.159m-1.5-1.5 1.409-1.409a2.25 2.25 0 0 1 3.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 0 0 1.5-1.5V6a1.5 1.5 0 0 0-1.5-1.5H3.75A1.5 1.5 0 0 0 2.25 6v12a1.5 1.5 0 0 0 1.5 1.5Zm10.5-11.25h.008v.008h-.008V8.25Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z" />
				</svg> -->
				<span>파일 업로드</span>
			</div>
			<div>이미지 분석하기</div>
		</div>
		<div id="nextBtn">
			<div class="bs">다음</div>
		</div>
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>떠나고 싶은 도시는?</title>
 		<!-- <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD36qsa9SClnegFZiQBM1jAA9YJC9j4F3s&libraries=maps,marker" defer></script> -->
		<!--  AIzaSyD36qsa9SClnegFZiQBM1jAA9YJC9j4F3s -->

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
			<div>도시 1곳을 선택해 주세요.</div>
		</div>
		<div id="recomendPlace">
			<div>추천 여행지</div>
			<div>
				<c:forEach var="list" items="${recommendedPlace}">
					<span data-placeid="${list.arrPlaceId}" data-lat="${list.LAT}" data-lng="${list.LNG}">${list.spot}</span>
				</c:forEach>
			</div>
		</div>
<style>
	#map {
		position:relative; 
		height: 600px; 
		width: 100%;
	}
	#map gmp-map {
		height:600px; 
		position: absolute; 
		top:0; 
		left:0; 
		bottom:0; 
		right:0;
	}
	#map > div {
		position: absolute; 
		top: 10px; 
		left: 10px; 
		z-index:1;
	}
	#map gmp-place-autocomplete{
	    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2), 0 -1px 0px rgba(0, 0, 0, 0.02);
	    border: none;
	    border-radius: 16px;
	}
</style>		
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
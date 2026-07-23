<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>출발지는?</title>
		<script>
            // prettier-ignore
            (g=>{var h,a,k,p="The Google Maps JavaScript API",c="google",l="importLibrary",q="__ib__",m=document,b=window;b=b[c]||(b[c]={});var d=b.maps||(b.maps={}),r=new Set,e=new URLSearchParams,u=()=>h||(h=new Promise(async(f,n)=>{await (a=m.createElement("script"));e.set("libraries",[...r]+"");for(k in g)e.set(k.replace(/[A-Z]/g,t=>"_"+t[0].toLowerCase()),g[k]);e.set("callback",c+".maps."+q);a.src=`https://maps.\${c}apis.com/maps/api/js?`+e;d[q]=f;a.onerror=()=>h=n(Error(p+" could not load."));a.nonce=m.querySelector("script[nonce]")?.nonce||"";m.head.append(a)}));d[l]?console.warn(p+" only loads once. Ignoring:",g):d[l]=(f,...n)=>r.add(f)&&u().then(()=>d[l](f,...n))})({
                key: "${googleApiKey}"
            });
        </script>
		<link rel="styleSheet" href="../resources/css/StartPlace.css"/>
		<script type="text/javascript" src="../resources/js/jquery-4.0.0.min.js"></script>
		<script type="text/javascript" src="../resources/js/StartPlace.js"></script>
		
		<script type="module" src="../resources/js/mapIndex.js"></script>
	</head>
	<body>
		<div id="header">
			<img class = "fl" src="//assets.triple.guide/images/btn-com-back@4x.png"/>
		</div>
		<div id="banner">
			<img src = "https://triple.guide/trips/static/icons/ico_map-emoji.svg"/>
			<div>출발지는?</div>
			<div>도시 1곳을 선택해 주세요.<br/>빨간 마커를 클릭 시 지워집니다.</div>
		</div>
		<div id="searchPlace" class="bs">
			<div id="map" class="bs">
	 			<gmp-map
			    center="35.76, 127.03"
			    zoom="7"
			    map-id="MAP_2_ID">
				</gmp-map>
			    <div>
				    <gmp-place-autocomplete id="autocomplete" placeholder="장소 검색"></gmp-place-autocomplete>
	            </div>
			</div>
		</div>
		<div id="nextBtn">
			<div class="bs">다음</div>
		</div>
	</body>
</html>
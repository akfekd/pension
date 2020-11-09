<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<style type="text/css">
.imgLayout {
	width: 190px;
	height: 205px;
	padding: 10px 5px 10px;
	margin: 5px;
	border: 1px solid #dad9ff;
	cursor: ponter;
}



input:focus {outline:none;}

.roomName {
	width: 180px;
	height: 25px;
	line-height: 25px;
	margin: 5px auto;
	border-top: 1px solid #dad9ff;
	display: inline-block;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	cursor: pointer;
}
</style>

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>

<script type="text/javascript">

function infoReserve(roomId, roomName, guestnum, price) {
	var f=document.reserveForm;
	
	f.roomId.value=roomId;
	f.guestnum.value=guestnum;
	document.getElementById("spanRoomName").innerHTML=roomName;
	f.price.value=price;
}

function reserveOk() {
	var f = document.reserveForm;
	
	f.action="${pageContext.request.contextPath}/reserve/${mode}_ok.do";
	
	f.submit();
	
}

</script>


</head>
<body>

<!-- 헤더 부분 시작 -->
<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
<!-- 헤더 부분 종료 -->

<div class="container">
    <div class="body-container" style="width: 700px;">
    <div class="body-title">
    	<h3>예약화면</h3>
    </div>
    
    <p> 원하는 방을 골라주세요. </p>
    
    <div>
    	<table style="width: 700px; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
    	<c:forEach var="dto" items="${listRoom}" varStatus="status">
    		<c:if test="${status.index==0}">
    			<tr>
    		</c:if>
    		<c:if test="${status.index!=0 && status.index%4==0}">
    			<c:out value="</tr><tr>" escapeXml="false"/>
    		</c:if>
    		<td width="300" align="center" >
    			<div class="imgLayout" onclick="infoReserve('${dto.roomId}','${dto.roomName}','${dto.guestnum}','${dto.price}');">
    				<img src="${pageContext.request.contextPath}/uploads/roominfo/${dto.saveFilename}" width="180" height="180" border="0">
    				<span class="roomName">${dto.roomName}</span>
    			</div>
    	</c:forEach>
    	<c:set var="n" value="${list.size()}"/>
		<c:if test="${n>0 && n%4!=0}">
			<c:forEach var="i" begin="${n%4+1}" end="5">
				<td width="300">
					<div class="imgLayout" style="cursor: default;">&nbsp;</div>
				</td>
			</c:forEach>
		</c:if>
    	</table>
    
    
    </div>
    
    
    
    
    
		<form name="reserveForm" method="post">
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse; table-layout: fixed;">
				<tr>	
					<td width="150px">
						<input type="text" name="guestnum" readonly="readonly" style="width: 15px; border: none;">인 기준
					</td>
				</tr>
				<tr>
					<td>
					<input type="hidden" name="roomId" readonly="readonly" style=" width:50px; border: none;">
					선택하신 방 : <span id="spanRoomName"></span>
					</td>
				</tr>
				<tr>
					<td>
					가격 : <input type="text" name="price" readonly="readonly" style="width: 50px; border: none;">원
					</td>
				</tr>


				<tr>
					<td>숙박 예정일</td>
					<td>
						<input type="date" name="rsvtStart">-<input type="date" name="rsvtEnd">
						<button type="button" class="btn" onclick="dateCheck()">날짜확인</button>
					</td>
				</tr>
				<tr>
					<td></td>
					<td></td>
				</tr>
			</table>
			
			<button class="btn" onclick="reserveOk();">예약 완료</button>
		</form>
	</div>
</div>
</body>
</html>
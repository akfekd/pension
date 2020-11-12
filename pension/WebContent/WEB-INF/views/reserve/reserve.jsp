<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>하늘숲 글램핑 파크</title> <!-- 김동현 -->
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">


<style type="text/css">
@import url(http://fonts.googleapis.com/earlyaccess/jejuhallasan.css);
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
	
	$('#reservebox').attr("style", "visibility:visible");
}

function reserveOk() {
	var f = document.reserveForm;
	
	var str = f.rsvtStart.value;
   	var str2 = f.rsvtEnd.value;
    if(!str2) {
        alert("날짜를 선택해 주세요. ");
        f.rsvtEnd.focus();
        return;
    }
    
    var sdt = new Date(str);
    var edt = new Date(str2);
    var dateDiff = Math.ceil((edt.getTime()-sdt.getTime())/(1000*3600*24));
	var multi;
	
	var rprice=f.price.value.replace(",","");
	
	
    multi=rprice*dateDiff;
    f.price.value=multi;
    
    
	f.action="${pageContext.request.contextPath}/reserve/${mode}_ok.do";
	
	f.submit();
	
}

function getInputDateFormat(date) {
    return date.toISOString().split('T')[0];
}


function validDate() {
	
    var today = new Date();
    today.setDate(today.getDate() + 1);
    today.setHours(today.getHours()+9);
    
    var maxDate = new Date();
    maxDate.setDate(maxDate.getDate() + 14);
    maxDate.setHours(maxDate.getHours()+9);

    document.getElementsByName("rsvtStart")[0].setAttribute('min', getInputDateFormat(today));
    document.getElementsByName("rsvtStart")[0].setAttribute('max', getInputDateFormat(maxDate));

}

function changeDate(obj) {
	
	var today = new Date(obj.value.substr(0,4),obj.value.substr(5,2)-1,obj.value.substr(8,2));
	today.setDate(today.getDate() + 1);
	today.setHours(today.getHours()+9);
	
	var maxDate = new Date(obj.value.substr(0,4),obj.value.substr(5,2)-1,obj.value.substr(8,2));
	maxDate.setDate(maxDate.getDate() + 7);
    maxDate.setHours(maxDate.getHours()+9);
	
	document.getElementsByName("rsvtEnd")[0].setAttribute('min', getInputDateFormat(today));
	document.getElementsByName("rsvtEnd")[0].setAttribute('max', getInputDateFormat(maxDate));

	$('#rsvtEnd').attr("disabled", false);
	$("input[type=date][name=rsvtEnd]").val("");

}
</script>


</head>
<body onload="validDate()">

<!-- 헤더 부분 시작 -->
<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
<!-- 헤더 부분 종료 -->

<div class="container" style="background: #e8e8e8;">
    <div class="body-container" style="width: 850px; margin: 0px auto; background: white;">
    <div class="body-title" style="border: none;">
    	&nbsp;&nbsp;&nbsp;<i class="fas fa-campground" style="font-size: 24px; padding-bottom: 8px; color: orange;"></i><h3 style="min-width: 800px; border-bottom: 0px; font-family: 'Jeju Hallasan', serif; font-weight: 100;">&nbsp;예약 정보 안내</h3>
    	<hr style="color: orange; height: 3px; background: orange; border: none;">
    </div>
    
    <p> <h2> &nbsp;&nbsp;예약하고자 하는 방을 클릭해 주세요. </h2> </p> 
    
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
    			<div class="imgLayout" style="cursor: pointer;" onclick="infoReserve('${dto.roomId}','${dto.roomName}','${dto.guestnum}','${dto.price}');">
    				<img src="${pageContext.request.contextPath}/uploads/roominfo/${dto.saveFilename}" width="180" height="180" border="0" style="border-radius: 12px;">
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
    
    <div></div>
		<form name="reserveForm" method="post">
			<table id="reservebox" style=" width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse; table-layout: fixed;  visibility: hidden;">
				<tr>	
					<td width="200px" style="font-size: 26px; padding: 20px 0px;">
						&nbsp;&nbsp;<i class="fas fa-users" style="color: tomato;"></i>&nbsp;&nbsp;<input type="text" name="guestnum" readonly="readonly" style="width: 20px; border: none; font-size: 26px">인 기준
					</td>
				</tr>
				<tr>
					<td style="font-size: 26px;">
					<input type="hidden" name="roomId" readonly="readonly" style=" width:50px; border: none;">
					&nbsp;&nbsp;<i class="fas fa-door-open" style="color: tomato;"></i>&nbsp;&nbsp;선택하신 방 : <span id="spanRoomName"></span>
					</td>
				</tr>
				<tr>
					<td style="font-size: 26px; padding: 20px 0px;">
					&nbsp;&nbsp;<i class="fas fa-bed" style="color: tomato;"></i>&nbsp;&nbsp;1박 가격 : <input type="text" name="price" readonly="readonly" style="width: 100px; border: none; font-size: 26px;">원
					</td>
				</tr>


				<tr>
					<td style="font-size: 26px; padding: 0px;">&nbsp;&nbsp;<i class="fas fa-calendar-check" style="color: tomato;"></i>&nbsp;&nbsp;&nbsp;숙박 예정일&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="date" name="rsvtStart" onchange="changeDate(this)" style="height: 32px; padding-top: 0px; vertical-align: bottom; font-size: 18px; border-radius: 10px;">
						<h3 style="display: inline-block;">～</h3>&nbsp;<input type="date" id="rsvtEnd" name="rsvtEnd" disabled="disabled" style="height: 32px; padding-top: 0px; vertical-align: bottom; font-size: 18px; border-radius: 10px;">
					</td>
				</tr>
				<tr>
				</tr>
			</table>			
		</form>
		<br>
		<br>
		<div style="text-align: center;">
			<span style="color: red; font-size: 32px;">${message}</span>
		</div>	
		<div style="text-align: center; position: static; margin-top: 50px; height: 50px; ">
			<button class="btn" onclick="reserveOk();">예약 완료</button>
		</div>
	</div>
</div>


<div class="footer" style="margin-top: 0px;">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>



</body>
</html>
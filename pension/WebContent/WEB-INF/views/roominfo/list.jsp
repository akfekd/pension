<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>roominfo</title>
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
	cursor: pointer;
}

.subject {
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

.roomimg {
	width: 60%;
	padding-bottom: 10px;
	float: left;
	clear:both;
}

.roominfo {
	float: left;
	padding: 10px;
	margin: 20px;
	width: 300px;
	text-align: center;
}

p {
	margin: 5px;
	word-wrap: break-word;
}

.commentary {
	font-family: fantasy;
	font-size: 15px;
	font-style: italic;
}

.morebtn {
	padding: 5px;
	border-radius: 3px;
	border: 1px solid;
	background: #6FA869;
	color: white;
}

.point {
	font-size: 30px;
	color: #6FA869;
}

.selectroom {
	text-align: center;
	margin: 10px;
	padding: 10px;
}

.selectbtn {
	border: 0;
	padding: 5px;
	background: white;
}

.selectbtn:active, .selectbtn:hover,  .selectbtn:focus {
	border-bottom : 1px solid #6FA869;
	background: white;
	outline: none;
	font-weight: bold;
}

</style>


<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>

<script type="text/javascript">

function article(roomId) {
	var url="${articleUrl}&roomId="+roomId;
	location.href=url;
}

function searchList(value) {
	var f=document.searchForm;
	f.submit();
}
</script>


</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 1000px;">
        <div class="body-title">
            <form name="searchForm" class="selectroom">
        		<button value="all" type="submit" class="selectbtn" onclick="${pageContext.request.contextPath}/roominfo/list.do">전체보기</button>
        		<button name="keyword" value="cab" type="submit" class="selectbtn" onclick="searchList('${value}')">카바나</button>
        		<button name="keyword" value="ope" type="submit" class="selectbtn" onclick="searchList('${value}')">오페라</button>
        		<button name="keyword" value="fam" type="submit" class="selectbtn" onclick="searchList('${value}')">패밀리</button>
            </form>
        </div>
        
        <div>
        	
	        <c:forEach var="dto" items="${list}" varStatus="status">
			<div class="roomlist">
	      		<div class="room">
	       			<a href="${articleUrl}&roomId=${dto.roomId}">
	          			<img class="roomimg" src="${pageContext.request.contextPath}/uploads/roominfo/${dto.saveFilename}" alt="">
	        		</a>
	     		</div>
	     		
		      	<div class="roominfo">
		        	<p style="font-size: 25px; font-weight: bold;">${dto.roomName}</p><br><br>
		           	<p>${dto.guestnum}인 기준</p>
		           	<p>요금 : ${dto.price}</p>
		        	<br>
		        	<p class="commentary"><span class="point">"</span>${dto.ment}<span class="point">"</span></p>
		        	<br><br><br>
		        		<button type="submit" class="morebtn" onclick="article('${dto.roomId}')">자세히 보기</button>
		        		<button type="submit" class="morebtn" onclick="">예약하기</button>
	      		</div>
	   	 	</div>
	   	 	
			</c:forEach>		
			
	
			 
			<table style="width: 100%; border-spacing: 0;">
			   <tr height="35">
				<td align="center">
			        ${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</td>
			   </tr>
			</table>
			
			
			
			<table style="width: 100%; margin-top: 10px; border-spacing: 0;">
			   <tr height="40">
			      <td align="left" width="100">
			          &nbsp;
			      </td>
			      <td align="center">
			          &nbsp;
			      </td>
			      <c:if test="${sessionScope.member.userId=='admin'}"> 
					<td align="right" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/roominfo/created.do';">등록하기</button>
			        </td>			        
			      </c:if>
			      
			   </tr>
			</table>

        </div>
    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- // 김다현 -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>후기</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<style type="text/css">
.reviewLayout {
	width: 1000px;
	margin: 40px auto;
}

.reviewLayout table {
	width: 100%;
	border-spacing: 0;
	border-collapse: collapse;
}

.selectroom {
	text-align: center;
	margin: 10px;
	padding: 10px;
}

.starLayout {
    color:#F2CB61;
}

.selectbtn {
	border: 0;
	padding: 5px;
	background: white;
}

.selectbtn:active, .selectbtn:hover,  .selectbtn:focus {
	border-bottom: 1px solid #6FA869;
	background: white;
	outline: none;
	font-weight: bold;
}

.reviewheader {
	background: #6FA869;
	color: white;
	padding: 5px;
}

</style>
<script type="text/javascript">

function deleteReview(rsvtNum) {
	if(confirm("게시물을 삭제 하시겠습니까 ?")) {
		var url="${pageContext.request.contextPath}/review/delete.do?page=${page}&rsvtNum="+rsvtNum;
		location.href=url;
	}
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
	
<form name="listForm">		
<c:forEach var="dto" items="${list}">
	<table class="reviewLayout" style="border-collapse: collapse; border-spacing: 0;">

		<tr class="reviewheader" height="40" style="border-radius: 10px;">
			<td style="padding-left: 10px; font-weight: bold; width: 18%">${dto.roomName}</td>
			<td class="starLayout" style="width: 8%">			
			<c:if test="${dto.star==1}">★</c:if>			
			<c:if test="${dto.star==2}">★★</c:if>			
			<c:if test="${dto.star==3}">★★★</c:if>
			<c:if test="${dto.star==4}">★★★★</c:if>
			<c:if test="${dto.star==5}">★★★★★</c:if>
			</td>
			<td style="width: 8%;">/ ${dto.userName} </td>
			<td colspan="2" style="width=50%; padding-right: 20px; text-align: right;">${dto.created} </td>
		</tr>
		<tr>
			<td style="padding: 10px;" colspan="4">${dto.content}</td>			
			<c:if test="${sessionScope.member.userId==dto.userId}">
			<td align="left" width="100">
				<button type="button" class="selectbtn" onclick="javascript:location.href='${pageContext.request.contextPath}/review/update.do?page=${page}&rsvtNum=${dto.rsvtNum}'">수정</button> | 
				<button type="button" class="selectbtn" onclick="deleteReview('${dto.rsvtNum}');">삭제</button>
			</td></c:if>
		</tr>
	</table>
</c:forEach>
</form>
	
<div>

		<div style="height: 35px; line-height: 35px; text-align: center;">
			${dataCount==0?"등록된 게시물이 없습니다.":paging}
		</div>	
		
		<table style="width: 100%; margin-top: 10px; border-spacing: 0;">
			   <tr height="40">
			      <td align="left" width="100">
			          &nbsp;
			      </td>
			      <td align="center">
			          &nbsp;
			      </td>
			      <c:if test="${sessionScope.member.userId!=null}"> 
					<td align="right" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/review/created.do';">등록하기</button>
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
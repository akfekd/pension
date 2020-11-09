<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>review</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<style type="text/css">
.reviewLayout {
	width: 1000px;
	margin: 30px auto;
}

.reviewLayout table {
	width: 100%;
	border-spacing: 0;
	border-collapse: collapse;
}

.selectroom {
	text-align: right;
	margin: 10px;
	padding: 10px;
}

.selectbtn {
	border: 0;
	padding: 5px;
	border-radius: 5px;
	background: white;
}

.selectbtn:active, .selectbtn:hover,  .selectbtn:focus {
	border: 1px solid #6FA869;
	background: white;
	outline: none;
}
</style>
<script type="text/javascript">

</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 1000px;">
	<div class="body-title">
		 <h3><span style="font-family: Webdings">2</span>&nbsp;Review </h3>
	</div>
	
<form name="listForm">
	<table class="reviewLayout" style="border-collapse: collapse; border-spacing: 0;">
		<c:forEach var="dto" items="${list}">
		<tr height="40" style="border: 1px solid #ccc;" bgcolor="#eee" >
			<td style="padding-left: 10px; font-weight: bold;">${dto.roomName}</td>
			<td style="padding-left: 10px;" width="70%">(${dto.userName})</td>
			<td>${dto.created} </td>
			<td align="left" width="100">
				<button type="button" style="border: 0; outline: 0;" onclick=""> 수정</button> | 
				<button type="button" style="border: 0; outline: 0;" onclick="">삭제</button>
			</td>
		</tr>
		<tr>
			<td style="padding: 10px;" colspan="4">${dto.content}</td>
			<td>${dto.star}</td>
		</tr>
		</c:forEach>
	</table>
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
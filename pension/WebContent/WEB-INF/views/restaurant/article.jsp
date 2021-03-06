﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 김성원 -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>하늘숲 글램핑 파크</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript">
function deleteBoard(num) {
<c:if test="${sessionScope.member.userId=='admin' || sessionScope.member.userId==dto.userId}">
	if(confirm("게시물을 삭제 하시겠습니까 ?")) {
		var url="${pageContext.request.contextPath}/restaurant/delete.do";
		location.href=url+"?${query}&num="+num;
	}
</c:if>	

<c:if test="${sessionScope.member.userId!='admin' && sessionScope.member.userId!=dto.userId}">
	alert('게시글을 삭제할 수 있는 권한이 없습니다.');
</c:if>
}
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 700px;">
        <div class="body-title">
              <h3><span style="font-family: Webdings">2</span> <span style="color:#6799FF;">주변</span><span style="color: #FFC19E;">맛집</span><span style="color: #AEC184">정보</span></h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="center">
				   ${dto.subject}
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td width="50%" align="left" style="padding-left: 5px;">
			        ${dto.userName}
			    </td>
			    <td width="50%" align="right" style="padding-right: 5px;">
			         ${dto.created} ㅣ ${dto.hitCount}
			    </td>
			</tr>
			
		<c:if test="${not empty dto.imageFilename}">
			<tr>
            <td colspan="2" style="padding: 10px 5px;">
               <img src="${pageContext.request.contextPath}/uploads/restaurant/${dto.imageFilename}"
                   style="max-width: 100%; height: auto; resize: both;">
            </td>
         	</tr> 
		</c:if>
			
			<tr style="border-bottom: 1px solid #cccccc;">
			  <td colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
			      ${dto.content}
			   </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       이전글 :
				<c:if test="${not empty preDto}">
       				<a href="${pageContext.request.contextPath}/restaurant/article.do?${query}&num=${preDto.num}">${preDto.subject}</a>
      		 	</c:if>
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       다음글 :
				<c:if test="${not empty nextDto}">
      				 <a href="${pageContext.request.contextPath}/restaurant/article.do?${query}&num=${nextDto.num}">${nextDto.subject}</a>
      			</c:if>
			    </td>
			</tr>
			<tr height="45">
			    <td>
			    <c:if test="${sessionScope.member.userId == dto.userId}">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/restaurant/update.do?page=${page}&num=${dto.num}';">수정</button>
			    </c:if>
			          <button type="button" class="btn" onclick="deleteBoard('${dto.num}');">삭제</button>
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/restaurant/list.do?${query}';">리스트</button>
			    </td>
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
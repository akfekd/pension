<%@ page contentType="text/html; charset=UTF-8" %>
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

<style type="text/css">
.imgLayout{
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
	overflow:hidden;
	text-overflow: ellipsis;
	cursor: pointer;
}
</style>

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>

<script type="text/javascript">
function article(num){
	var url="${articleUrl}&num="+num;
	location.href=url;
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
            <h3><span style="font-family: Webdings">2</span> 편의시설 </h3>
        </div>
        
        <div>

	<table style="width: 630px; margin: 20px auto 0px; border-spacing: 0px;">
		<c:forEach var="dto" items="${list}" varStatus="status">
			<c:if test="${status.index==0}">
				<tr>
			</c:if>
			<c:if test="${status.index!=0 && status.index%2==0}">
				<c:out value="</tr><tr>" escapeXml="false"/>
			</c:if>
			<td width="210" align="center">
				<div class="imgLayout" onclick="article('${dto.num}');">
					<img  src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}" width="180" height="180" border="0">
					<span class="subject">${dto.subject}</span>
				</div>
			</td>
		</c:forEach> 
		<c:set var="n" value="${list.size()}"/>
		<c:if test="${n>0 && n%2!=0}">
			<c:forEach var="i" begin="${n%2+1}" end="2">
				<td width="210">
					<div class="imgLayout">&nbsp;</div>
				</td>
			</c:forEach>
		</c:if>   
		<c:if test="${n!=0}">
			<c:out value="</tr" escapeXml="false"/>
		</c:if>
	</table>
		 
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
			          
			      </td>
			      <td align="center">
			       
			      </td>
			      <td align="right" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/photo/created.do';">사진올리기</button>
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
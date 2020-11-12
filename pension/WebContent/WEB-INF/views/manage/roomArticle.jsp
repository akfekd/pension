<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript">

//권철안
	function searchList() {
		var f=document.searchForm;
		f.submit();
	}
	function deleteRaservation(rsvtNum, userId) {
		<c:if test="${sessionScope.member.userId=='admin' || sessionScope.member.userId==dto.userId}">
			if(confirm("예약을 취소하시겠습니까 ?")) {
				var url="${pageContext.request.contextPath}/manage/delete_rsvt.do";
				location.href=url+"?rsvtNum="+rsvtNum+"&userId="+userId;
			}
		</c:if>

		<c:if test="${sessionScope.member.userId!='admin' && sessionScope.member.userId!=dto.userId}">
			alert('예약을을 취소할 수 있는 권한이 없습니다.');
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
            <h3><span style="font-family: Webdings">2</span> 예약 확인/취소 </h3>
        </div>
        
        <div>
			<table style="width: 100%; margin-top: 20px; border-spacing: 0;">
			   <tr height="35">
			      <td align="left" width="50%">
			          ${dataCount}개(${page}/${total_page} 페이지)
			      </td>
			      <td align="right">
			          &nbsp;
			      </td>
			   </tr>
			</table>
			
			<table style="width: 100%; border-spacing: 0; border-collapse: collapse;">
			  <tr align="center" bgcolor="#B7F0B1" height="50" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <th width="150" style="color: #787878;">방이름</th>
			      <th width="100" style="color: #787878;">인원수</th>
			      <th width="150" style="color: #787878;">입실일</th>
			      <th width="100" style="color: #787878;">퇴실일</th>
			      <th width="100" style="color: #787878;">예약일</th>
			      <th width="100" style="color: #787878;">요금</th>
			      <th width="100" style="color: #787878;">예약취소</th>
			  </tr>
			 
			 <c:forEach var="dto" items="${list}">
			  <tr align="center" height="35" style="border-bottom: 1px solid #cccccc;"> 
			      <td style="padding-left: 10px;">
			          ${dto.roomId }
			      </td>
			      <td>${dto.guestNum}</td>
			      <td>${dto.rsvtStart}</td>
			      <td>${dto.rsvtEnd}</td>
			      <td>${dto.created}</td>
			      <td>${dto.rsvtPrice}</td>
			      <td>
			          <button type="button" class="btn" onclick="deleteRaservation('${dto.rsvtNum}','${dto.userId}');">예약취소</button>
			    </td>
			  </tr>
			 </c:forEach>

			</table>
			 
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			   <tr height="35">
				<td align="center">
			        ${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/manage/roomList.do';">새로고침</button>
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
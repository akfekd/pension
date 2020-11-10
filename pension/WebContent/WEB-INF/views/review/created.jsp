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

.starLayout {font-size:0; letter-spacing:-4px;}

.starLayout a {
    font-size:22px;
    letter-spacing:0;
    display:inline-block;
    margin-left:3px;
    color:#cccccc;
    text-decoration:none;
}
.starLayout a:first-child {margin-left:0;}
.starLayout a.on {color:#F2CB61;}

</style>
<script type="text/javascript">
function sendReview() {
	var f=document.reviewForm;
	
	var m="${mode}";
	if (m!="update" && ! f.rsvtNum.value) {
		alert("예약번호를 선택하세요.");
		f.rsvtNum.focus();
		return;
	}
	
	if(! f.content.value) {
		f.content.focus();
		return;
	}
	
	f.action="${pageContext.request.contextPath}/review/${mode}_ok.do";
	f.submit();
}

</script>

<script type="text/javascript">
$(function(){
	$( ".starLayout a" ).click(function() {
		var b=$(this).hasClass("on");
	    $(this).parent().children("a").removeClass("on");
	    $(this).addClass("on").prevAll("a").addClass("on");
	    if(b) 	$(this).removeClass("on");
	    
	    var s=$(".starLayout .on").length;
	    $("#star").val(s);
	});
});
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
	
	<div class="reviewLayout">
		<form name="reviewForm" method="post">
		<table style="margin-top: 20px;">
			<tr height="40" style="border-top: 1px solid #ccc; border-bottom: 1px solid #ccc;">
				<td align="center" width="100" bgcolor="#eee">작성자</td>
				<td style="padding-left: 10px;">
					${sessionScope.member.userName}
				</td>
				<td align="center" width="100" bgcolor="#eee">예약번호</td>
				<td style="padding-left: 10px;">
					<select name="rsvtNum" style="width: 100px" onchange="selectRsv();" ${mode=='update'?"disabled='disabled'":""}>
						<option value="">예약번호</option>
					<c:forEach var="vo" items="${listRsv}">
						<option value="${vo.rsvtNum}" ${dto.rsvtNum==vo.rsvtNum?"selected='selected'":""}>${vo.roomName}(${vo.rsvtNum})</option>
					</c:forEach>
					</select>
				</td>
			</tr>
			
			<tr height="40" style="border-top: 1px solid #ccc; border-bottom: 1px solid #ccc;">
				<td align="center" width="100" bgcolor="#eee">평점</td>
				<td style="padding-left: 10px; width: 50%">
						<p class="starLayout">
							    <a href="#">★</a>
							    <a href="#">★</a>
							    <a href="#">★</a>
							    <a href="#">★</a>
							    <a href="#">★</a>
						</p>
					<input type="hidden" name="star" id="star" value="0">
				</td>
			</tr>
			
			
			<tr height="100" style="border-top: 1px solid #ccc; border-bottom: 1px solid #ccc;">
				<td align="center" width="100" bgcolor="#eee" valign="top" style="padding-top: 5px;">내 용</td>
				<td colspan="3" valign="top" style="padding: 5px 0 5px 10px;">
					<textarea name="content" class="boxTA" style="width: 95%;">${dto.content}</textarea>
				</td>

			</tr>
			
			<tr height="40">
				<td colspan="4" align="right">
				<c:if test="${mode=='update'}">
					<input type="hidden" name="rsvtNum" value="${dto.rsvtNum}">
					<input type="hidden" name="page" value="${page}">
				</c:if>
				<button type="button" class="btn" onclick="sendReview();">${mode=='update'?'수정완료':'등록하기'}</button>
				<button type="reset" class="btn">다시입력</button>
			    <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/review/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>

			</tr>	
		</table>
	</form>
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
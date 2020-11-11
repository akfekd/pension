<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>하늘숲 글램핑 파크</title>
<style type="text/css">
.a-box{
	position: relative;
	left: 50px; top: -750px;	
	font-weight: bold; text-align: center;
	line-height: 65px;
	border: 1;
	border-collapse: collapse;
}
.b-box{
	position: relative;
	left: 50px; top: -650px;	
	font-weight: bold; text-align: center;
	line-height: 65px;
	border: 1;
	border-collapse: collapse;
}
.c-box{
	position: relative;
	left: 1380px; top: -1250px;
	font-weight: bold; text-align: center;
	line-height: 65px;
	border: 1;
	border-collapse: collapse;
}

</style>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>

</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="body-container" style="width:700px ">
        <div style="padding-top: 15px;">
         	   <div style="overflow:hidden">
         	   		<div class="section">
         	   			<div class="inner">
         	   				<img src="${pageContext.request.contextPath}/resource/images/camp1.jpg">
         	   			</div>
         	   			<div class="inner">
         	   				<img src="${pageContext.request.contextPath}/resource/images/camp2.jpg">
         	   			</div>
         	   			<div class="inner">
         	   				<img src="${pageContext.request.contextPath}/resource/images/camp3.jpg">
         	   			</div>
         	   			<div class="inner">
         	   				<img src="${pageContext.request.contextPath}/resource/images/camp4.jpg">
         	   			</div>
         	   			<div class="inner">
         	   				<img src="${pageContext.request.contextPath}/resource/images/camp5.jpg">
         	   			</div>
         	   		</div>
         	   </div>
	</div>
</div>
	<div style="text-align: center; ">
		<button class="but1">1</button>
		<button class="but2">2</button>
		<button class="but3">3</button>
		<button class="but4">4</button>
		<button class="but5">5</button>
	</div>

<table class="a-box">	
			<tr align="center" height="35" style="border-bottom: 1px solid #cccccc;">
			      <th width="60" style="color: #787878;">번호</th>
			      <th style="color: #787878;">제목</th>
			      <th width="100" style="color: #787878;">작성자</th>
			      <th width="80" style="color: #787878;">작성일</th>
			      <th width="60" style="color: #787878;">조회수</th>
			 </tr>
		<c:forEach var="dto" items="${list2}">
			<tr> 
			      <td style="color: #FF5A5A">HOT</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
			      		<c:if test="${dto.gap<1}">
			      			<img src="${pageContext.request.contextPath}/resource/images/new.gif">
			      		</c:if>
			      </td>
			      <td>${dto.userName}</td>
			      <td>${dto.getCreated().substring(0,10)}</td>
			      <td>${dto.hitCount}</td>
			  </tr>
		</c:forEach>
</table>	
<table class="b-box">
			<tr align="center" height="35" style="border-bottom: 1px solid #cccccc;"> 
			      <th width="60" style="color: #787878;">번호</th>
			      <th style="color: #787878;">제목</th>
			      <th width="100" style="color: #787878;">작성자</th>
			      <th width="80" style="color: #787878;">작성일</th>
			      <th width="60" style="color: #787878;">조회수</th>
			</tr>
		<c:forEach var="dto" items="${list3}">
			<tr> 
			      <td style="color: #FF5A5A">HOT</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
			      		<c:if test="${dto.gap<1}">
			      			<img src="${pageContext.request.contextPath}/resource/images/new.gif">
			      		</c:if>
			      </td>
			      <td>${dto.userName}</td>
			      <td>${dto.getCreated().substring(0,10)}</td>
			      <td>${dto.hitCount}</td>
			  </tr>
		</c:forEach>
</table>				
<table class="c-box">
			<tr align="center" height="35" style="border-bottom: 1px solid #cccccc;"> 
			      <th width="60" style="color: #787878;">번호</th>
			      <th style="color: #787878;">제목</th>
			      <th width="100" style="color: #787878;">작성자</th>
			      <th width="80" style="color: #787878;">작성일</th>
			      <th width="60" style="color: #787878;">조회수</th>
			</tr>
		<c:forEach var="dto" items="${list4}">
			<tr> 
			      <td style="color: #FF5A5A">HOT</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
			      		<c:if test="${dto.gap<1}">
			      			<img src="${pageContext.request.contextPath}/resource/images/new.gif">
			      		</c:if>
			      </td>
			      <td>${dto.userName}</td>
			      <td>${dto.getCreated().substring(0,10)}</td>
			      <td>${dto.hitCount}</td>
			 </tr>
		</c:forEach>
</table>
<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>
<script>
	document.querySelector(".but1").addEventListener('click',function(){
		document.querySelector('.section').style.transform = 'translate(0vw)';
	})
	document.querySelector(".but2").addEventListener('click',function(){
		document.querySelector('.section').style.transform = 'translate(-100vw)';
	})
	document.querySelector(".but3").addEventListener('click',function(){
		document.querySelector('.section').style.transform = 'translate(-200vw)';
	})
	document.querySelector(".but4").addEventListener('click',function(){
		document.querySelector('.section').style.transform = 'translate(-300vw)';
	})
	document.querySelector(".but5").addEventListener('click',function(){
		document.querySelector('.section').style.transform = 'translate(-400vw)';
	})
</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>
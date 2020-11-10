<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>하늘숲 글램핑 파크</title>

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
	
<div class="body-container" style="width: 700px;">
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
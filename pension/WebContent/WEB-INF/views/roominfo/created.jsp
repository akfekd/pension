<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- // 김다현 -->
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
    function sendOk() {
        var f = document.boardForm;

    	var str = f.roomName.value;
        if(!str) {
            alert("방이름을 입력하세요. ");
            f.roomName.focus();
            return;
        }
        
        var str = f.guestnum.value;
        if(!str) {
            alert("인원수를 입력하세요. ");
            f.guestnum.focus();
            return;
        }
        

        var str = f.price.value;
        if(!str) {
            alert("가격을 입력하세요. ");
            f.price.focus();
            return;
        }


    	str = f.content.value;
        if(!str) {
            alert("내용을 입력하세요. ");
            f.content.focus();
            return;
        }
        
        var mode="${mode}";
        if(mode=="created" && ! f.selectFile.value) {
        	alert("이미지 파일을 선택하세요.");
        	f.selectFile.focus();
        	return;
        }
        

    	f.action="${pageContext.request.contextPath}/roominfo/${mode}_ok.do";

        f.submit();
    }
    
    <c:if test="%{mode=='update'}">
    	$(function () {
			$("#myroominfo").click(function () {
				var viewer=$("#imageLayout");
				var s="<img src='${pageContext.request.contextPath}/uploads/roominfo/${dto.saveFilename}' width='570' height='450'>";
					viewer.html(s);
				
				$("#roominfoDialog").dialog({
					title:"이미지",
					width:600,
					height:520,
					modal:true
				})
			});
		});
    </c:if>
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 700px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> 방 정보관리 </h3>
        </div>
        
        <div>
			<form name="boardForm" method="post" enctype="multipart/form-data">
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">방이름</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="roomName" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.roomName}">
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">코드</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="roomId" maxlength="100" class="boxTF" style="width: 20%;" value="${dto.roomId}" 
			          ${mode=="update" ? "readonly='readonly' ":""} > (카바나:cab/오페라:ope/패밀리:fam)
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">인원수</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="guestnum" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.guestnum}">
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">가격</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="price" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.price}">
			      </td>
			  </tr>

			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">작성자</td>
			      <td style="padding-left:10px;"> 
			          ${member.userName}
			      </td>
			  </tr>
			  
			   <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">코멘트</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="ment" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.ment}">
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center; padding-top:5px;" valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:5px 0px 5px 10px;"> 
			          <textarea name="content" rows="12" class="boxTA" style="width: 95%;">${dto.content}</textarea>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">이미지</td>
			      <td style="padding-left:10px;"> 
			          <input type="file" name="selectFile" accept="image/*" class="boxTF" size="53" style="height: 25px;">
			       </td>
			  </tr> 
			  
			  <c:if test="${mode=='update'}">
				   <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
				      <td width="100" bgcolor="#eeeeee" style="text-align: center;">미리보기</td>
				      <td style="padding-left:10px;"> 
				   		<img id="myroominfo" src="${pageContext.request.contextPath}/uploads/roominfo/${dto.saveFilename}" width="30" height="30" style="cursor: pointer;">
				       </td>
				   </tr> 			  
			  
			  </c:if>
			  
			  </table>
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			      	<c:if test="${mode=='update'}">
			      		<input type="hidden" name="page" value="${page}">
			      		<input type="hidden" name="roomId" value="${dto.roomId}">
			      		<input type="hidden" name="saveFilename" value="${dto.saveFilename}">
			      	</c:if>
			      	
			        <button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/roominfo/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>

				  </td>
			    </tr>
			  </table>
			</form>
        </div>

    </div>
</div>

<div id="roominfoDialog">
	<div id="imageLayout">
	</div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>
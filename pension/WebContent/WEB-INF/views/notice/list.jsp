<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
	function searchList() {
		var f=document.searchForm;
		f.submit();
	}
	
	function listBoard() {
	    var f=document.boardListForm;
	    f.page.value="1";
	    f.action="${pageContext.request.contextPath}/notice/list.do";
	    f.submit();
	}
	
	<c:if test="${sessionScope.member.userId=='admin'}">
	$(function(){
		$("#chkAll").click(function(){
			if($(this).is(":checked")) {
				$("input[name=nums]").prop("checked", true);
			} else {
				$("input[name=nums]").prop("checked", false);
			}
		});
		
		$("#btnDeleteList").click(function(){
			var cnt=$("input[name=nums]:checked").length;
			if(cnt==0) {
				alert("삭제할 게시물을 먼저 선택하세요.");
				return false;
			}
			
			var filename, input;
			$("input[name=nums]:checked").each(function(index) {
				filename=$(this).attr("data-filename");
				if(filename != "") {
					input="<input type='hidden' name='filenames' value='"+filename+"'>";
					$("form[name=noticeListForm]").append(input);
				}
			});
			
			if(confirm("선택한 게시물을 삭제 하시겠습니까 ?")) {
				var f=document.noticeListForm;
				f.action="${pageContext.request.contextPath}/notice/deleteList.do";
				f.submit();
			}
			
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
    <div class="body-container" style="width: 750px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> ★이용시 필독사항★ </h3>
        </div>
        
        <div>
        	<form name="noticeListForm" method="post">
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px;">
			   <tr height="35">
			      <td align="left" width="50%">
			      	  <c:if test="${sessionScope.member.userId=='admin'}">
			          	<button type="button" class="btn" id="btnDeleteList">삭제</button>
			          </c:if>
			      	  <c:if test="${sessionScope.member.userId!='admin'}">
			          	${dataCount}개(${page}/${total_page} 페이지)
			          </c:if>
			      </td>
			      <td align="right">
					<c:if test="${dataCount!=0 }">
					      <select name="rows" class="selectField" onchange="listBoard();">
					         <option value="5"  ${rows==5 ? "selected='selected' ":""}>5개씩 출력</option>
					         <option value="10" ${rows==10 ? "selected='selected' ":""}>10개씩 출력</option>
					         <option value="20" ${rows==20 ? "selected='selected' ":""}>20개씩 출력</option>
					         <option value="30" ${rows==30 ? "selected='selected' ":""}>30개씩 출력</option>
					         <option value="50" ${rows==50 ? "selected='selected' ":""}>50개씩 출력</option>
					      </select>
					  </c:if>
					  <input type="hidden" name="page" value="${page}">
					  <input type="hidden" name="condition" value="${condition}">
					  <input type="hidden" name="keyword" value="${keyword}">
			      </td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="center" bgcolor="#eeeeee" height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
			  	  <c:if test="${sessionScope.member.userId=='admin'}">
				  	  <th width="40" style="color: #787878;">
				  	  	<input type="checkbox" name="chkAll" id="chkAll" style="margin-top: 3px;">
				  	  </th>
			  	  </c:if> 
			      <th width="60" style="color: #787878;">번호</th>
			      <th style="color: #787878;">제목</th>
			      <th width="100" style="color: #787878;">작성자</th>
			      <th width="80" style="color: #787878;">작성일</th>
			      <th width="60" style="color: #787878;">조회수</th>
			      <th width="50" style="color: #787878;">첨부</th>
			  </tr>

			 <c:forEach var="dto" items="${listNotice}">
			  <tr align="center" bgcolor="#ffffff" height="35" style="border-bottom: 1px solid #cccccc;"> 
			  	  <c:if test="${sessionScope.member.userId=='admin'}">
			  	     <td>
			  	  		<input type="checkbox" name="nums" value="${dto.num}" style="margin-top: 3px;" data-filename="${dto.saveFilename}">
			  	  	 </td>
			  	  </c:if>
			      <td>
			           <span style="display: inline-block;padding:1px 3px; background: #ED4C00;color: #FFFFFF; ">공지</span>
			      </td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
			      </td>
			      <td>${dto.userName}</td>
			      <td>${dto.created}</td>
			      <td>${dto.hitCount}</td>
			      <td>
						<c:if test="${not empty dto.saveFilename}">
						      <a href="${pageContext.request.contextPath}/notice/download.do?num=${dto.num}"><img src="${pageContext.request.contextPath}/resource/images/disk.gif" border="0" style="margin-top: 1px;"></a>
						</c:if>
			      </td>
			  </tr>
			</c:forEach> 

			 <c:forEach var="dto" items="${list}">
			  <tr align="center" bgcolor="#ffffff" height="35" style="border-bottom: 1px solid #cccccc;">
			   	  <c:if test="${sessionScope.member.userId=='admin'}">
			   	     <td>
			   	  		<input type="checkbox" name="nums" value="${dto.num}" style="margin-top: 3px;" data-filename="${dto.saveFilename}">
			   	  	 </td>
			  	  </c:if>
			      <td>${dto.listNum}</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
			           <c:if test="${dto.gap<1}"><img src="${pageContext.request.contextPath}/resource/images/new.gif"></c:if>
			      </td>
			      <td>${dto.userName}</td>
			      <td>${dto.created}</td>
			      <td>${dto.hitCount}</td>
			      <td>
						<c:if test="${not empty dto.saveFilename}">
						      <a href="${pageContext.request.contextPath}/notice/download.do?num=${dto.num}"><img src="${pageContext.request.contextPath}/resource/images/disk.gif" border="0" style="margin-top: 1px;"></a>
						</c:if>
			      </td>
			  </tr>
			</c:forEach> 
			</table>
			</form>
			 
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			   <tr height="35">
				<td align="center">
					${dataCount!=0?paging:"등록된 게시물이 없습니다."}
				</td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/notice/list.do';">새로고침</button>
			      </td>
			      <td align="center">
			          <form name="searchForm" action="${pageContext.request.contextPath}/notice/list.do" method="post">
			              <select name="condition" class="selectField">
			                  <option value="subject"     ${condition=="subject"?"selected='selected'":"" }>제목</option>
			                  <option value="userName" ${condition=="userName"?"selected='selected'":"" }>작성자</option>
			                  <option value="content"     ${condition=="content"?"selected='selected'":"" }>내용</option>
			                  <option value="created"     ${condition=="created"?"selected='selected'":"" }>등록일</option>
			            </select>
			            <input type="text" name="keyword" class="boxTF" value="${keyword}">
			            <input type="hidden" name="rows" value="${rows}">
			            <button type="button" class="btn" onclick="searchList()">검색</button>
			        </form>
			      </td>
			      <td align="right" width="100">
			          <c:if test="${sessionScope.member.userId=='admin'}">
			              <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/notice/created.do?rows=${rows}';">글올리기</button>
			          </c:if>
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
﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
//엔터 처리
$(function(){
	   $("input").not($(":button")).keypress(function (evt) {
	        if (evt.keyCode == 13) {
	            var fields = $(this).parents('form,body').find('button,input,textarea,select');
	            var index = fields.index(this);
	            if ( index > -1 && ( index + 1 ) < fields.length ) {
	                fields.eq( index + 1 ).focus();
	            }
	            return false;
	        }
	     });
});
function deleteMember(userId) {
		if(confirm("회원을 탈퇴하시겠습니까 ?")) {
			var url="${pageContext.request.contextPath}/member/delete.do";
			location.href=url+"?&userId="+userId;
			return;
		}

	<c:if test="${sessionScope.member.userId!=sessionScope.member.userId}">
		alert('게시글을 삭제할수 있는 권한이 없습니다.');
	</c:if>
	}

</script>
<ul style="list-style: none; margin: 0px; padding: 0px; background-color: #FFD393" >
    <li style="position:relative; width: 300px; height: 70px; margin-left: 30px">
        <p style="margin: 2px;">
            <a href="${pageContext.request.contextPath}/" style="text-decoration: none;">
 			<img style="position:relative; width: 200px; height: 70;  left: 0; " alt="logo" src="${pageContext.request.contextPath}/resource/images/LOGO.png">

            </a>
        </p>
    </li>
<li class="header-top">
    <div class="header-right">
        <div style="padding-top: 20px;  float: right;">
            <c:if test="${empty sessionScope.member}">
                <a href="${pageContext.request.contextPath}/member/login.do">로그인</a>
                    &nbsp;|&nbsp;
                <a href="${pageContext.request.contextPath}/member/member.do">회원가입</a>
            </c:if>
            <c:if test="${not empty sessionScope.member}">
                <span style="color:blue;">${sessionScope.member.userName}</span>님
                    &nbsp;|&nbsp;
                    <a href="${pageContext.request.contextPath}/member/logout.do">로그아웃</a>
            </c:if>
           		
        </div>
    </div>
</li>

<li class="menu">
    <ul class="nav">
        <li>
            <a href="${pageContext.request.contextPath}/reserve/reserve.do">예약</a>
         
        </li>
			
        <li>
            <a href="#">숙소정보</a>
           <ul>
                <li><a href="${pageContext.request.contextPath}/roominfo/list.do">숙소목록</a></li>
                <li><a href="${pageContext.request.contextPath}/review/list.do">리뷰</a></li>
            </ul>
        </li>

        <li>
            <a href="#">펜션안내</a>
             <ul>
                <li><a href="${pageContext.request.contextPath}/resource/road/road.jsp">찾아오는 길</a></li>
                <li><a href="${pageContext.request.contextPath}/photo/list.do">편의시설</a></li>
                <li><a href="${pageContext.request.contextPath}/notice/list.do">★이용전 필독사항★</a></li> 
                <li><a href="${pageContext.request.contextPath}/faq/list.do">자주하는 질문</a><li>
                <li><a href="${pageContext.request.contextPath}/qna/list.do">1:1 질문</a><li>               
            </ul>
        </li>

        <li>
            <a href="#">숙소 주변 즐길거리</a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/festival/list.do">주변 축제 정보</a></li>
                <li><a href="${pageContext.request.contextPath}/restaurant/list.do">주변 맛집 정보</a></li>
                <li><a href="${pageContext.request.contextPath}/spot/list.do">주변 관광지 정보</a></li>
            </ul>
        </li>
       
   
        

        <c:if test="${not empty sessionScope.member && sessionScope.member.userId !='admin'}">
        <li>
            <a href="#">마이페이지</a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/manage/list.do">예약확인</a></li>
                <li><a href="${pageContext.request.contextPath}/member/update.do">정보수정</a></li>
                <li><a href="${pageContext.request.contextPath}/member/delete.do">회원탈퇴</a></li>
            </ul>
        </li>
        </c:if>
        
         <c:if test="${sessionScope.member.userId=='admin'}">
        <li>
            <a href="#">관리</a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/member/list.do">회원목록</a></li>
                <li><a href="${pageContext.request.contextPath}/manage/roomList.do">숙소별 예약현황</a></li>
                          
            </ul>
        </li>
        </c:if>

        <li style="float: right;"><a href="${pageContext.request.contextPath}/main.do">전체보기</a></li>

    </ul>      
</li>
</ul>
<div class="navigation">
</div>
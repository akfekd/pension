<%@ page contentType="text/html; charset=UTF-8" %>
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
</script>

<div class="header-top">
    <div class="header-left">
        <p style="margin: 2px;">
            <a href="${pageContext.request.contextPath}/" style="text-decoration: none;">
                <span style="width: 200px; height: 70; position: relative; left: 0; top:20px; color: #6FA869; filter: mask(color=red) shadow(direction=135) chroma(color=red);font-style: italic; font-family: arial black; font-size: 30px; font-weight: bold;">PENSION!!!</span>
            </a>
        </p>
    </div>
    <div class="header-right">
        <div style="padding-top: 20px;  float: right;">
            <c:if test="${empty sessionScope.member}">
                <a href="${pageContext.request.contextPath}/member/login.do">로그인</a>
                    &nbsp;|&nbsp;
                <a href="${pageContext.request.contextPath}/member/member.do">회원가입</a>
           		<a href="${pageContext.request.contextPath}">정보수정</a>
            </c:if>
            <c:if test="${not empty sessionScope.member}">
                <span style="color:blue;">${sessionScope.member.userName}</span>님
                    &nbsp;|&nbsp;
                    <a href="${pageContext.request.contextPath}/member/logout.do">로그아웃</a>
            </c:if>
        </div>
    </div>
</div>

<div class="menu">
    <ul class="nav">
        <li>
            <a href="${pageContext.request.contextPath}/reserve/reserve.do">예약</a>
            <!-- <ul>
                <li><a href="#"></a></li>
                <li><a href="#"></a></li>
                <li><a href="#"></a></li>
                <li><a href="#"></a></li>
                <li><a href="#"></a></li>
            </ul>
             -->
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
                <li><a href="${pageContext.request.contextPath}/manage/list.do">정보확인</a></li>
            </ul>
        </li>
        </c:if>
        
         <c:if test="${sessionScope.member.userId=='admin'}">
        <li>
            <a href="#">관리</a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/member/list.do">회원목록</a></li>
                <li><a href="${pageContext.request.contextPath}/manage/roomList.do">숙소별 예약현황</a></li>
                <li><a href="#">매출관리</a></li>              
            </ul>
        </li>
        </c:if>

        <li style="float: right;"><a href="#">전체보기</a></li>

    </ul>      
</div>

<div class="navigation">
</div>
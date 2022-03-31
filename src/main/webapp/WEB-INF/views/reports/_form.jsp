<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>


<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br>
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}"/> <br>
        </c:forEach>
    </div>
</c:if>

<fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />
    <!-- value==>日付データに変換する文字列　　var==>変換した文字列を格納する変数　　type==>日付データの種別3種から指定 -->

<label for="${AttributeConst.REP_DATE.getValue()}">日付</label><br>
    <!-- for==>関連付けたいフォーム部品(今回はinput)のidかname属性を指定。 -->

<input type="date" name="${AttributeConst.REP_DATE.getValue()}" value="<fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd'/>"/>
    <!-- type==>dateはカレンダーを表示　　value==>カレンダーを選ぶ前の初期値 name==>パラメータに送られる日付-->

<br><br>


<label for="name">氏名</label><br>
<c:out value="${sessionScope.login_employee.name}" />
<br><br>

<label for="${AttributeConst.REP_TITLE.getValue()}">タイトル</label><br>
<input type="text" name="${AttributeConst.REP_TITLE.getValue()}" value="${report.title}" />
<br><br>

<label for="${AttributeConst.REP_CONTENT.getValue()}">内容</label><br />
<textarea name="${AttributeConst.REP_CONTENT.getValue()}" rows="10" cols="50">${report.content}</textarea>
<br /><br />

<input type="hidden" name="${AttributeConst.REP_ID.getValue()}" value="${report.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>

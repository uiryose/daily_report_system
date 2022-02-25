<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>


<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdit" value="${ForwardConst.CMD_EDIT.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name ="content">

        <h2>従業員　新規登録ページ</h2>

        <form method="POST" action="<c:url value='?action=${action}&command=${commCrt}'/>">
            <c:import url="_form.jsp" />
        </form>

        <p><a href="<c:url value='?action=${action}&command=${commIdx}'/>">一覧に戻る</a></p>

    </c:param>
</c:import>



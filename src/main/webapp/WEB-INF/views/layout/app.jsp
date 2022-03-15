<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actTop" value="${ForwardConst.ACT_TOP.getValue()}" />
<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />     <!-- Employee -->
<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />     <!-- Report -->
<c:set var="actAuth" value="${ForwardConst.ACT_AUTH.getValue()}" />   <!-- Auth 認証の意味-->

<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commOut" value="${ForwardConst.CMD_LOGOUT.getValue()}" />

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title><c:out value="日報管理システム" /></title>
    <link rel="stylesheet" href="<c:url value='/css/reset.css' />">
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
    <div id="wrapper">
        <div id="header">
            <div id="header_menu">
                <h1><a href="<c:url value='?action=${actTop}&command=${commIdx}' />">日報管理システム</a></h1>&nbsp;&nbsp;&nbsp;

                <c:if test="${sessionScope.login_employee != null}">

       <!-- ログインした従業員の権限によって表示メニューを変える。管理者フラグが管理者1で一致していたら。［従業員管理］を表示 -->
                    <!-- sessionScope.login_employee.adminFlagで.の連結が不安 -->
                    <c:if test="${sessionScope.login_employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}">
                        <a href="<c:url value='?action=${actEmp}&command=${commIdx}' />">従業員管理</a>&nbsp;
                    </c:if>
       <!-- 共通で[日報管理]を表示。action="Report" command="index" -->
                    <a href="<c:url value='?action=${actRep}&command=${commIdx}' />">日報管理</a>&nbsp;

                </c:if>

            </div>
    <!-- ログイン(セッションスコープにlogin_employeeの情報がある)していれば、ヘッダーの右上に名前とログアウトボタンを設置 -->
            <c:if test="${sessionScope.login_employee != null}">
                <div id="employee_name">
                    <c:out value="${sessionScope.login_employee.name}" />&nbsp;さん&nbsp;&nbsp;&nbsp;
                    <a href="<c:url value='?action=${actAuth}&command=${commOut}' />">ログアウト</a>

                </div>
            </c:if>
        </div>
    </div>
    <div id="content">${param.content}</div>
    <div id="footer">by 未経験エンジニア</div>
</body>
</html>
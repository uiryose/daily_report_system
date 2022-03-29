<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">


        <h2>お気に入りの従業員</h2>
        <table id="follow_list">
            <tbody>
                <tr>
                    <th class="follow_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">最近の日報</th>
                    <th class="follow_action">操作</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />

                    <tr class="row${status.count % 2}">
                        <td class="follow_name"><a href="<c:url value='?action=${actEmp}&command=${commShow}&id=${follow.followEmployee.id}' />"><c:out value="${follow.followEmployee.name}" /></a></td>
                        <td class="report_date"><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="follow_action"><a href="<c:url value='?action=${actFol}&command=${commRem}&id=${employee.id}' />">フォロー外す</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>


    </c:param>
</c:import>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.AttributeConst"%>
<%@ page import="constants.ForwardConst"%>

<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commAll" value="${ForwardConst.CMD_ALL.getValue()}" />
<c:set var="actFol" value="${ForwardConst.ACT_FOL.getValue()}" />
<c:set var="commFol" value="${ForwardConst.CMD_FOLLOW.getValue()}" />
<c:set var="commRem" value="${ForwardConst.CMD_FOLLOW_REMOVE.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush !=null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <h2>フォロー従業員一覧</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>役職</th>
                    <th>フォロー</th>
                </tr>
                <c:forEach var="follow" items="${follows}" varStatus="status">
                    <tr class="row${status.count %2 }">
                        <td><c:out value="${follow.followEmployee.code}" /></td>
                        <td><a href="<c:url value='?action=${actEmp}&command=${commShow}&id=${employee.id}' />" >
                            <c:out value="${follow.followEmployee.name}" /></a></td>
                        <td>
                            <c:choose>
                                <c:when
                                    test="${follow.followEmployee.positionFlag == AttributeConst.POSITION_TOP.getIntegerValue()}">役員</c:when>
                                <c:when
                                    test="${follow.followEmployee.positionFlag == AttributeConst.POSITION_MID.getIntegerValue()}">部長</c:when>
                                <c:otherwise>社員</c:otherwise>
                            </c:choose>
                        </td>
                        <td><a  href="<c:url value='?action=${actFol}&command=${commRem}&id=${follow.followEmployee.id}&display=fav' />">フォローを外す</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="follow-count"> (全 ${follow_count} 件)</div>
    </c:param>
</c:import>
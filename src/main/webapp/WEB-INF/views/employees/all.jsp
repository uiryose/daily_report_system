<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

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

        <h2>従業員　在籍一覧</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>役職</th>
                    <th>フォロー</th>
                </tr>
                <c:forEach var="employee" items="${employees}" varStatus="status">
                   <tr class="row${status.count %2 }">
                       <td><c:out value="${employee.code}"/></td>
                       <td><c:out value="${employee.name}"/></td>
                       <td><c:choose>
                            <c:when test="${employee.positionFlag == AttributeConst.POSITION_TOP.getIntegerValue()}">役員</c:when>
                            <c:when test="${employee.positionFlag == AttributeConst.POSITION_MID.getIntegerValue()}">部長</c:when>
                            <c:otherwise>社員</c:otherwise>
                        </c:choose>
                       </td>
                       <td class="follow_action"><a href="<c:url value='?action=${actFol}&command=${commFol}&id=${employee.id}' />">フォローする</a>
                                                 <a href="<c:url value='?action=${actFol}&command=${commRem}&id=${employee.id}' />">フォロー外す</a>
                       </td>

                   </tr>
                </c:forEach>
            </tbody>
        </table>


         <div id="pagination">
            (全 ${employees_count} 件)<br/>
            <c:forEach var="i" begin="1" end="${((employees_count - 1) / maxRow) +1 }" step="1">
                <c:choose>
                    <c:when test="${i==page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actEmp}&command=${commAll}&page=${i}'/>"><c:out value="${i}"/></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </c:param>
</c:import>
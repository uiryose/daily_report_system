<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commSrh" value="search" />


<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush !=null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <c:if test="${call_method == 'index' }">
                <h2>従業員　一覧</h2>
        </c:if>
        <c:if test="${call_method == 'search'}">
                <h2>従業員　検索結果</h2>
        </c:if>

        <form method="POST" action="<c:url value='?action=${actEmp}&command=${commSrh}' />">

            <label for="${AttributeConst.EMP_CODE.getValue()}">社員番号:</label>
                <input type="text" name="${AttributeConst.EMP_CODE.getValue()}">&nbsp;&nbsp;
            <label for="${AttributeConst.EMP_NAME.getValue()}">氏名:</label>
                <input type="text" name="${AttributeConst.EMP_NAME.getValue()}">&nbsp;&nbsp;
            <label for="${AttributeConst.EMP_ADMIN_FLG.getValue()}">権限:</label>
            <select name="${AttributeConst.EMP_ADMIN_FLG.getValue()}">
                    <option selected value="9" >未選択</option>
                    <option value="${AttributeConst.ROLE_GENERAL.getIntegerValue()}">一般</option>
                    <option value="${AttributeConst.ROLE_ADMIN.getIntegerValue()}">管理者</option>
            </select> &nbsp;&nbsp;&nbsp;
            <input type="submit" value="  検索  ">
        </form>
        <br>


        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>操作</th>
                </tr>
                <c:forEach var="employee" items="${employees_search}" varStatus="status">
                   <tr class="row${status.count %2 }">
                       <td><c:out value="${employee.code}"/></td>
                       <td><c:out value="${employee.name}"/></td>
                       <td>
                           <c:choose>
                               <c:when test="${employee.deleteFlag == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()}">
                                   (削除済み)
                               </c:when>
                               <c:otherwise>
                                    <a href="<c:url value='?action=${actEmp}&command=${commShow}&id=${employee.id}' />">詳細を見る</a>
                               </c:otherwise>
                           </c:choose>
                       </td>
                   </tr>
                </c:forEach>
            </tbody>
        </table>
        <table>
            <tbody>
                 <c:forEach var="employee" items="${employees}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${employee.code}" /></td>
                        <td><c:out value="${employee.name}" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${employee.deleteFlag == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()}">
                                    （削除済み）
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value='?action=${actEmp}&command=${commShow}&id=${employee.id}' />">詳細を見る</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <c:if test="${employees_search.size() == 0}">
            <p>該当しませんでした。</p>
        </c:if>

        <c:if test="${call_method == 'index' }">
            <div id="pagination">
                (全 ${employees_count} 件)<br/>
                <c:forEach var="i" begin="1" end="${((employees_count - 1) / maxRow) +1 }" step="1">
                    <c:choose>
                        <c:when test="${i==page}">
                            <c:out value="${i}" />&nbsp;
                        </c:when>
                        <c:otherwise>
                            <a href="<c:url value='?action=${actEmp}&command=${commIdx}&page=${i}'/>"><c:out value="${i}"/></a>&nbsp;
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </c:if>
        <p><a href="<c:url value='?action=${actEmp}&command=${commNew}' />">新規従業員登録</a></p>
    </c:param>
</c:import>
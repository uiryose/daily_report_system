<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="actCom" value="${ForwardConst.ACT_COM.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdt" value="${ForwardConst.CMD_EDIT.getValue()}" />
<c:set var="commCre" value="${ForwardConst.CMD_CREATE.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

    <h2>日報 詳細ページ</h2>
<c:if test="${errors != null}">
    <div id="flush_error">
        コメントが未入力です。<br>
    </div>
</c:if>
   <c:if test="${flush != null}">
       <div id="flush_success">
           <c:out value="${flush}"></c:out>
       </div>
   </c:if>
    <table>
        <tbody>
            <tr>
                <th>氏名</th>
                <td><c:out value="${report.employee.name}"/></td>    <!-- reportとAttributeConst.REPとの違い…？ -->
            </tr>
            <tr>
                <th>日付</th>     <!-- fmt:***は設定のみ？もう一度確認 -->
                <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />
                <td><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
            </tr>
            <tr>
                <th>内容</th>
                <td><pre><c:out value="${report.content}"/></pre></td> <!-- <pre>によって、元の改行も反映される -->
            </tr>
            <tr>
                <th>登録日時</th>
                <fmt:parseDate value="${report.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createDay" type="date" />
                <td><fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
                <th>更新日時</th>
                <fmt:parseDate value="${report.updatedAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="updateDay" type="date" />
                <td><fmt:formatDate value="${updateDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
        </tbody>
    </table>

<!-- ログインIDとレポートのIDが一致していたら修正のリンクを表示する -->
    <c:if test="${sessionScope.login_employee.id==report.employee.id}">
        <p>
            <a href="<c:url value='?action=${actRep}&command=${commEdt}&id=${report.id}'/>">この日報を修正する</a>
        </p>
    </c:if>
    <p>
        <a href="<c:url value='?action=${actRep}&command=${commIdx}'/>">一覧に戻る</a>
    </p>

<h3>日報へのコメント</h3>
        <div class="comment">
            <c:forEach var="comment" items="${comments}">
                <div class="balloon">
                    <div class="balloon-image-left">
                        <c:out value="${comment.employee.name}" />
                    </div>
                    <div class="balloon-text-right">
                        <p class="kaiwa-text">
                        <c:choose>
                            <c:when test="${comment.deleteFlag == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()}">
                               (このコメントは削除されました)
                            </c:when>
                            <c:otherwise> <c:out value="${comment.content}" /></c:otherwise>
                        </c:choose>
                        </p>
                    </div>
                    <div class="balloon-date-left">
                        <fmt:parseDate value="${comment.updatedAt}"
                            pattern="yyyy-MM-dd'T'HH:mm:ss" var="day" type="date" />
                        <fmt:formatDate value="${day}" pattern="(M/d)" />
                        <c:choose>
                            <c:when test="${comment.deleteFlag == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()}"></c:when>
                            <c:when test="${sessionScope.login_employee.id == comment.employee.id}">
                            <a class="edit" href="<c:url value='?action=${actCom}&command=${commEdt}&id=${comment.id}'/>">編集</a>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </div>


        <form method="POST" action="<c:url value='?action=${actCom}&command=${commCre}&id=${report.id}'/>">

            <label for="${AttributeConst.COM_CONTENT.getValue()}">COMMENT</label><br />
            <textarea name="${AttributeConst.COM_CONTENT.getValue()}" rows="3" cols="60">${comment.content}</textarea>
            <br /><br />

            <input type="hidden" name="${AttributeConst.REP_ID.getValue()}" value="${report.id}" />
            <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />

            <button type="submit">送信</button>

        </form>



    </c:param>
</c:import>



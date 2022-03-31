<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>


<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commUpd" value="${ForwardConst.CMD_UPDATE.getValue()}" />
<c:set var="actCom" value="${ForwardConst.ACT_COM.getValue()}" />
<c:set var="commDes" value="${ForwardConst.CMD_DESTROY.getValue()}" />


<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <h2>コメント 編集ページ</h2>

<!-- 編集用のjsp -->
        <form method="POST" action="<c:url value='?action=${actCom}&command=${commUpd}&id=${comment.id}'/>">

            <label for="${AttributeConst.COM_CONTENT.getValue()}">COMMENT</label><br />
            <textarea name="${AttributeConst.COM_CONTENT.getValue()}" rows="3" cols="60">${comment.content}</textarea>
            <br /><br />

            <input type="hidden" name="${AttributeConst.COM_REP_ID.getValue()}" value="${comment.report.id}" />
            <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />

            <button type="submit">修正</button>
        </form>


<!-- 削除用のjsp -->
        <p>
            <a href="#" onclick="confirmDestroy();">このコメントを削除する</a>
        </p>

        <form method="POST" action="<c:url value='?action=${actCom}&command=${commDes}&id=${comment.id}'/>">
            <input type="hidden" name="${AttributeConst.COM_REP_ID.getValue()}" value="${comment.report.id}"/>
            <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}"/>
        </form>

        <script>
            function confirmDestroy(){
                if(confirm("本当に削除してよろしいですか？")){
                    document.forms[1].submit();
                }
            }
        </script>


        <p>
            <a href="<c:url value='?action=Report&command=show&id=${comment.report.id}' />">日報に戻る</a>
        </p>



    </c:param>
</c:import>
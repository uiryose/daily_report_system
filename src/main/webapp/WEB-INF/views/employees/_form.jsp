<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="action" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />


<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br>
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}" /><br>
        </c:forEach>
    </div>
</c:if>


<label for="${AttributeConst.EMP_CODE.getValue()}">社員番号</label><br>
<input type="text" name="${AttributeConst.EMP_CODE.getValue()}" value="${employee.code}" />
<br/><br/>

<label for="${AttributeConst.EMP_NAME.getValue()}">氏名</label><br>
<input type="text" name="${AttributeConst.EMP_NAME.getValue()}" value="${employee.name}" />
<br/><br/>

<label for="${AttributeConst.EMP_PASS.getValue()}">パスワード</label><br>
<input type="password" name="${AttributeConst.EMP_PASS.getValue()}" />
<br><br>

<label for="${AttributeConst.EMP_ADMIN_FLG.getValue()}">権限</label><br>
<select name="${AttributeConst.EMP_ADMIN_FLG.getValue()}" >
    <option value="${AttributeConst.ROLE_GENERAL.getIntegerValue()}"
        <c:if test="${employee.adminFlag==AttributeConst.ROLE_GENERAL.getIntegerValue()}"> selected</c:if>>
        一般
    </option>
<!--     **value=""はjavaに送る信号。  ROLE_GENERAL=0、 ROLE_ADMIN=1  if文は更新時にFlagが責任者ROLE_ADMIN=1と一致したらselected状態にする -->
    <option value="${AttributeConst.ROLE_ADMIN.getIntegerValue()}"
        <c:if test="${employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}"> selected</c:if>>
        管理者
    </option>
</select>

<br><br>
<label for="${AttributeConst.EMP_POSITION_FLG.getValue()}">役職</label><br>
<select name="${AttributeConst.EMP_POSITION_FLG.getValue()}" >
    <option value="${AttuributeConst.POSITION_LOW.getIntegerValue()}"
        <c:if test="${employee.positionFlag==AttributeConst.POSITION_LOW.getIntegerValue()}"> selected</c:if>>
        社員
    </option>
    <option value="${AttributeConst.POSITION_MID.getIntegerValue()}"
        <c:if test="${employee.positionFlag == AttributeConst.POSITION_MID.getIntegerValue()}"> selected</c:if>>
        部長
    </option>
    <option value="${AttributeConst.POSITION_TOP.getIntegerValue()}"
        <c:if test="${employee.positionFlag == AttributeConst.POSITION_TOP.getIntegerValue()}"> selected</c:if>>
        役員
    </option>
</select>
<br>

<p><input type="file" name="${AttributeConst.EMP_PROFILE_URL.getValue()}"/></p>

<br><br>
<input type="hidden" name="${AttributeConst.EMP_ID.getValue()}" value="${employee.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>


<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<%@ taglib prefix="t"   tagdir="/WEB-INF/tags"  %>
<!doctype html>
<html lang="${empty lang ? 'vi' : lang}">
<head>
  <meta charset="utf-8">
  <title><fmt:message key="app.title"/></title>
</head>
<body>

<fmt:setLocale value="${empty lang ? 'vi' : lang}"/>
<fmt:setBundle basename="app_i18n.messages"/>
<fmt:message var="phSearch" key="search.placeholder"/>
<%@ include file="/WEB-INF/views/_layout.jspf" %>

<div class="container my-4">
  <a class="btn btn-link px-0" href="<t:urlWithLang value='/home?lang=${lang}'/>">← <fmt:message key="btn.back.home"/></a>

  <c:choose>
    <c:when test="${p == null}">
      <div class="alert alert-danger"><fmt:message key="product.notfound"/></div>
    </c:when>
    <c:otherwise>
      <div class="card p-4">
        <h3 class="mb-1">${p.name}</h3>
        <div class="text-secondary mb-3">
          <fmt:message key="product.category"/>: ${p.category}
          &nbsp;|&nbsp;
          <fmt:message key="product.code"/>: #${p.id}
        </div>
        <p class="mb-3"><c:out value="${p.description}"/></p>
        <div>
          <strong><fmt:message key="product.price"/>:</strong> ₫${p.price}
          &nbsp;|&nbsp;
          <strong><fmt:message key="product.weight"/>:</strong> ${p.weight} g
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>

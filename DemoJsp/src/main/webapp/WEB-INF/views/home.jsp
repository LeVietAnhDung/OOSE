<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<%@ taglib prefix="t"   tagdir="/WEB-INF/tags"  %>
<fmt:setLocale value="${empty lang ? 'vi' : lang}"/>
<fmt:setBundle basename="app_i18n.messages"/><fmt:message var="phSearch" key="search.placeholder"/>
<!doctype html>
<html lang="${empty lang ? 'vi' : lang}">
<head><meta charset="utf-8"><title><fmt:message key="app.title"/></title></head>
<body>
<%@ include file="/WEB-INF/views/_layout.jspf" %>

<div class="container my-4">
  <div class="row g-4">
    <div class="col-lg-3">
      <div class="bg-white p-3 rounded-4 shadow-sm">
        <h6 class="mb-3"><fmt:message key="home.categories"/></h6>
        <div class="list-group list-group-flush">
          <c:forEach var="citem" items="${categories}">
            <a class="list-group-item ${citem.id == activeCatId ? 'active' : ''}"
               href="<t:urlWithLang value='/home?cat=${citem.id}'/>">
              <div class="d-flex justify-content-between">
                <span>${citem.name}</span>
                <span class="badge text-bg-secondary">${citem.productCount}</span>
              </div>
            </a>
          </c:forEach>
        </div>
      </div>
    </div>

    <div class="col-lg-9">
      <h5 class="mb-3"><fmt:message key="home.title"/></h5>
      <c:choose>
        <c:when test="${empty products}">
          <div class="alert alert-warning">No items.</div>
        </c:when>
        <c:otherwise>
          <div class="row g-4">
            <c:forEach var="p" items="${products}">
              <div class="col-md-6 col-xl-4">
                <div class="card h-100">
                  <div class="card-body">
                    <h6 class="card-title mb-1">${p.name}</h6>
                    <div class="muted small mb-2">#${p.id}</div>
                    <p class="muted" style="min-height:40px;"><c:out value="${p.description}"/></p>
                    <div class="d-flex justify-content-between">
                      <strong>₫${p.price}</strong>
                      <span class="badge text-bg-light">⚖ ${p.weight} g</span>
                    </div>
                  </div>
                  <div class="card-footer bg-white">
                    <a class="btn btn-sm btn-primary w-100"
                       href="<t:urlWithLang value='/products/detail?id=${p.id}'/>">
                      <fmt:message key="btn.details"/>
                    </a>
                  </div>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>
</body></html>

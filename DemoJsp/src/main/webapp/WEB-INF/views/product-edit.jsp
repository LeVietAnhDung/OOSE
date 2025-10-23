<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<%@ taglib prefix="t"   tagdir="/WEB-INF/tags"  %>
<!doctype html><html lang="${empty lang?'vi':lang}">
<head><meta charset="utf-8"><title>Edit</title></head>
<body>
<fmt:setLocale value="${empty lang?'vi':lang}"/><fmt:setBundle basename="app_i18n.messages"/><fmt:message var="phSearch" key="search.placeholder"/>
<%@ include file="/WEB-INF/views/_layout.jspf" %>

<div class="container my-4">
  <h4>Sửa sản phẩm #${p.id}</h4>
  <form method="post" action="<t:urlWithLang value='/products/edit'/>" class="row g-3">
    <input type="hidden" name="id" value="${p.id}"/>

    <div class="col-md-4">
      <label class="form-label">Giá</label>
      <input class="form-control" name="price" value="${p.price}">
    </div>
    <div class="col-md-4">
      <label class="form-label">Khối lượng</label>
      <input class="form-control" name="weight" value="${p.weight}">
    </div>
    <div class="col-md-4">
      <label class="form-label">Danh mục</label>
      <select class="form-select" name="categoryId">
        <c:forEach var="c" items="${categories}">
          <option value="${c.id}" ${c.name==p.category?'selected':''}>${c.name}</option>
        </c:forEach>
      </select>
    </div>

    <hr/>

    <div class="col-md-4">
      <label class="form-label">Tên (VI)</label>
      <input class="form-control" name="name_vi" value="${p.name}">
      <label class="form-label mt-2">Mô tả (VI)</label>
      <textarea class="form-control" name="desc_vi"><c:out value="${p.description}"/></textarea>
    </div>
    <div class="col-md-4">
      <label class="form-label">Name (EN)</label>
      <input class="form-control" name="name_en">
      <label class="form-label mt-2">Description (EN)</label>
      <textarea class="form-control" name="desc_en"></textarea>
    </div>
    <div class="col-md-4">
      <label class="form-label">名前 (JA)</label>
      <input class="form-control" name="name_ja">
      <label class="form-label mt-2">説明 (JA)</label>
      <textarea class="form-control" name="desc_ja"></textarea>
    </div>

    <div class="col-12 d-flex gap-2 mt-3">
      <button class="btn btn-primary">Lưu</button>
      <a class="btn btn-secondary" href="<t:urlWithLang value='/products/detail?id=${p.id}'/>">Huỷ</a>

      <form method="post" action="<t:urlWithLang value='/products/delete'/>" onsubmit="return confirm('Xoá?');">
        <input type="hidden" name="id" value="${p.id}"/>
        <button class="btn btn-danger">Xoá</button>
      </form>
    </div>
  </form>
</div>
</body></html>

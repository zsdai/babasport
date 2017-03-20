<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Cache-Control" content="max-age=300" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${query} - 商品搜索 - 淘淘</title>
<meta name="Keywords" content="java,淘淘java" />
<meta name="description" content="在淘淘中找到了29910件java的类似商品，其中包含了“图书”，“电子书”，“教育音像”，“骑行运动”等类型的java的商品。" />
<link rel="stylesheet" type="text/css" href="/css/base.css" media="all" />
<link rel="stylesheet" type="text/css" href="/css/psearch20131008.css" media="all" />
<link rel="stylesheet" type="text/css" href="/css/psearch.onebox.css" media="all" />
<link rel="stylesheet" type="text/css" href="/css/pop_compare.css" media="all" />
<link rel="stylesheet" type="text/css" href="/css/page.css" />
<style type="text/css">
.ulList
{
display:block;
white-space:nowrap;
overflow:auto;
}
.ulList li
{
margin:10px 20px;
display:inline-block;
}
.ulList li a{
color: blue;
}
</style>
<script type="text/javascript" src="/js/jquery-1.6.4.js"></script>
<script type="text/javascript">
var modelkeyword='${keyword}';
var modelprice='${price}';
var modelbrandId='${brandId}';
var url="/Search.do";
//过滤品牌
 function fqBrand(brandId) {
	
	//过滤品牌是 判断价格是否已经过滤
	if(null!=modelprice && ''!=modelprice){
		url=url+"?keyword="+modelkeyword+"&brandId="+brandId+"&price="+modelprice;
	}else{
		url=url+"?keyword="+modelkeyword+"&brandId="+brandId;
	}
	window.location.href=url;
}
//过滤价格
 function fqPrice(priceRange) {
	//过滤价格时 如果modelbrandId不为空 也添加上
	 if(null!=modelbrandId && ''!=modelbrandId){
		 url=url+"?keyword="+modelkeyword+"&price="+priceRange+"&brandId="+modelbrandId;
	}else{
		url=url+"?keyword="+modelkeyword+"&price="+priceRange;
	}
	window.location.href=url;
}
//改变或删除过滤条件
function changefq(thisObj,removeName){
	if(removeName=="价格"){
		url=url+"?brandId="+modelbrandId;
	}else{//品牌
		url=url+"?price="+modelprice;
	}
	$(thisObj).remove();
	window.location.href=url;
}
</script>
<script type="text/javascript" src="/js/base-2011.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/jquery.hashchange.js"></script>
<script type="text/javascript" src="/js/search_main.js"></script>
</head>
<body>
<!-- header start -->
<jsp:include page="commons/header.jsp" />
<!-- header end -->
<div class="w main">
	<div class="crumb">全部结果&nbsp;&gt;&nbsp;<strong>${keyword}</strong>
		<c:if test="${fn:length(map)>0}">
			<div class="ulList">
			<ul>
			<strong>已选条件:</strong>
			<c:forEach items="${ map}" var="m">
			<a href="javascript:void(0);" onclick="changefq(this,'${m.key }')"><li>${m.key }:<font color="red">${m.value }</font></li></a>
			</c:forEach>
			</ul>
		</div>
		</c:if>
	</div>
		<c:if test="${empty brandId }">
		<div class="ulList">
			<ul>
			<strong>品牌:</strong>
			<c:forEach items="${ brandList}" var="brand">
			<li><a href="javascript:void(0);" onclick="fqBrand('${brand.id}')" >${brand.name }</a></li>
			</c:forEach>
			</ul>
		</div>
			</c:if>
		<c:if test="${empty price }">
			<div class="ulList">
				<ul>
					<strong>价格:</strong>
					<li><a onclick="fqPrice('0-99')" href="javascript:void(0);">0-99</a></li>
					<li><a onclick="fqPrice('100-299')" href="javascript:void(0);">100-299</a></li>
					<li><a onclick="fqPrice('300-599')" href="javascript:void(0);">300-599</a></li>
					<li><a onclick="fqPrice('600-999')" href="javascript:void(0);">600-999</a></li>
					<li><a onclick="fqPrice('1000-1599')" href="javascript:void(0);">1000-1599</a></li>
					<li><a onclick="fqPrice('1600-*')" href="javascript:void(0);">1600以上</a></li>
				</ul>
			</div>
		</c:if>
<div class="page">
	<span class="r inb_a page_b">
		<c:forEach items="${pagination.pageView }" var="page">
			${page}
		</c:forEach>
	</span>
</div>

<div class="m psearch " id="plist">
<ul class="list-h clearfix" tpl="2">
<c:forEach items="${pagination.list}" var="item">
<li class="item-book" bookid="11078102">
	<div class="p-img">
		<%-- <a target="_blank" href="/item/${item.id }.html"> --%>
		<a href="javascript:void(0);" onclick="window.open('/product/detail?id=${item.id }')">
			<img width="160" height="160" data-img="1" data-lazyload="${item.images[0]}" />
		</a>
	</div>
	<div class="p-name">
		<a href="http://127.0.0.1:8084/html/product/${item.id }.html">
			${item.name}
		</a>
	</div>
	<div class="p-price">
		<i>淘淘价：</i>
		<strong>￥<fmt:formatNumber groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" value="${item.price }"/></strong>
	</div>
	<div class="service">由 淘淘 发货</div>
	<div class="extra">
		<span class="star"><span class="star-white"><span class="star-yellow h5">&nbsp;</span></span></span>
	</div>
</li>
</c:forEach>
</ul></div>
</div>
<!-- footer start -->
<jsp:include page="commons/footer.jsp" />
<!-- footer end -->

</body>
</html>
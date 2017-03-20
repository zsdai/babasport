<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>babasport-list</title>
<script type="text/javascript">
function updateSku(skuId){
	//如果被点击的行已经是可编辑状态
	if(!$("#m"+skuId).attr("disabled")){
		return;
	}
	//遍历所有 将之前可编辑的全部设置为不可编辑
	var id;
	$("input[name='ids']").each(function(){
		id=$(this).val();
		if(!$("#m"+id).attr("disabled")){
			$("#m"+id).attr("disabled","disabled");
			$("#p"+id).attr("disabled","disabled");
			$("#s"+id).attr("disabled","disabled");
			$("#l"+id).attr("disabled","disabled");
			$("#f"+id).attr("disabled","disabled");
		}
	});
	//<th>市场价格</th>
	$("#m"+skuId).attr("disabled",false);
	//<th>销售价格</th>
	$("#p"+skuId).attr("disabled",false);
	//<th>库存</th>
	$("#s"+skuId).attr("disabled",false);
	//<th>购买限制</th>
	$("#l"+skuId).attr("disabled",false);
	//<th>运 费</th>
	$("#f"+skuId).attr("disabled",false);
}
function addSku(skuId){
	var m=$("#m"+skuId).attr("disabled","disabled").val();
	var p=$("#p"+skuId).attr("disabled","disabled").val();
	var s=$("#s"+skuId).attr("disabled","disabled").val();
	var l=$("#l"+skuId).attr("disabled","disabled").val();
	var f=$("#f"+skuId).attr("disabled","disabled").val();
	$.post(
		"/sku/update.do",
		{
			"id":skuId,
			"marketPrice":m,
			"price":p,
			"stock":s,
			"upperLimit":l,
			"deliveFee":f
		},
		function(data){
			//alert(data.message)
		},
		"json"
	);
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 库存管理 - 列表</div>
	<div class="clear"></div>
</div>
<div class="body-box">
<form method="post" id="tableForm">
<table cellspacing="1" cellpadding="0" border="0" width="100%" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="Pn.checkbox('ids',this.checked)"/></th>
			<th>商品编号</th>
			<th>商品颜色</th>
			<th>商品尺码</th>
			<th>市场价格</th>
			<th>销售价格</th>
			<th>库       存</th>
			<th>购买限制</th>
			<th>运       费</th>
			<th>是否赠品</th>
			<th>操       作</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
	<c:forEach items="${skuList}" var="sku">
			<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
				<td><input type="checkbox" name="ids" value="${sku.id}"/></td>
				<td>${sku.productId}</td>
				<td align="center">${sku.color.name}</td>
				<td align="center">${sku.size}</td>
				<td align="center"><input value="${sku.marketPrice}" type="text" id="m${sku.id}"  disabled="disabled" size="10"/></td>
				<td align="center"><input value="${sku.price}" type="text" id="p${sku.id}"  disabled="disabled" size="10"/></td>
				<td align="center"><input value="${sku.stock}" type="text" id="s${sku.id}"  disabled="disabled" size="10"/></td>
				<td align="center"><input value="${sku.upperLimit}" type="text" id="l${sku.id}"  disabled="disabled" size="10"/></td>
				<td align="center"><input value="${sku.deliveFee}" type="text" id="f${sku.id}" disabled="disabled" size="10"/></td>
				<td align="center">否</td>
				<td align="center"><a href="javascript:updateSku(${sku.id})" class="pn-opt">修改</a> | <a href="javascript:addSku(${sku.id})" class="pn-opt">保存</a></td>
			</tr>
	</c:forEach>
		
	</tbody>
</table>
</form>
</div>
</body>
</html>
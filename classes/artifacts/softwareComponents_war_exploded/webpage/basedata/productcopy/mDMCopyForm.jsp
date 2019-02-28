<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>产品结构复制管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					jp.loading();
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/productcopy/mDMCopy"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="mDMCopy" action="${ctx}/productcopy/mDMCopy/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>源产品：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/mdmproductbom/mdmProductBom/data" id="source" name="source.id" value="${mDMCopy.source.id}" labelName="source.productItemName" labelValue="${mDMCopy.source.productItemName}"
							 title="选择源" cssClass="form-control required" fieldLabels="产品名称|零件名称" fieldKeys="productItemName|itemName" searchLabels="产品名称|零件名称" searchKeys="productItemName|itemName" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>目的产品：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/mdmproductbom/mdmProductBom/data" id="target" name="target.id" value="${mDMCopy.target.id}" labelName="target.productItemName" labelValue="${mDMCopy.target.productItemName}"
							 title="选择目标" cssClass="form-control required" fieldLabels="产品名称|零件名称" fieldKeys="productItemName|itemName" searchLabels="产品名称|零件名称" searchKeys="productItemName|itemName" ></sys:gridselect>
					</div>
				</div>
		<c:if test="${fns:hasPermission('productcopy:mDMCopy:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="复制成功">复制</button>
		                 </div>
		             </div>
		        </div>
		</c:if>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
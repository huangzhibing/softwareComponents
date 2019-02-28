<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>科目定义管理</title>
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
		
		function autoNumbering(){
			var id = $("#fatherIdId").val();
			$.ajax({
				url:"${ctx}/cositem/cosItem/autoNumberingRight?id="+id,
				type: "GET",
				cache:false,
				dataType: "text",
				success:function(data){
					$("#itemCode").val(data)
				}
			});
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/cositem/cosItem"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="cosItem" action="${ctx}/cositem/cosItem/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">科目编码：</label>
					<div class="col-sm-10">
						<form:input path="itemCode" htmlEscape="false"    class="form-control required" readonly = "true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">科目名称：</label>
					<div class="col-sm-10">
						<form:input path="itemName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">科目级别：</label>
					<div class="col-sm-10">
						<%-- <form:input path="itemClass" htmlEscape="false"    class="form-control "/> --%>
						<form:select path="itemClass" class="form-control" disabled = "true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('itemClass')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">是否为叶子科目：</label>
					<div class="col-sm-10">
						<form:select path="itemFinish" class="form-control" disabled = "true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('itemFinish')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">上级科目编号：</label>
					<div class="col-sm-10">
						<sys:treeselectwithevent id="fatherId" name="fatherId.id" value="${cosItem.fatherId.id}" labelName="fatherId.name" labelValue="${cosItem.fatherId.name}"
							title="上级科目编号" url="/cositem/cosItemLeft/treeData" extId="${cosItem.id}" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                 </div>
		             </div>
		        </div>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
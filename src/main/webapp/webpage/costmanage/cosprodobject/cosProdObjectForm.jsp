<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>核算对象定义(右表）管理</title>
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
				<a class="panelButton" href="${ctx}/cosprodobject/cosProdObject"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="cosProdObject" action="${ctx}/cosprodobject/cosProdObject/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">核算对象编码：</label>
					<div class="col-sm-10">
					<sys:gridselect-allsuitable url="${ctx}/purinvcheckmain/invCheckMain/dataRequest" id="prodId" name="prodId" value="${cosProdObject.prodId}" labelName="item.code" labelValue="${cosProdObject.prodId}"
							 title="选择物料编号" cssClass="form-control required " targetId="prodName|prodCatalog" targetField="name|className"  fieldLabels="物料编码|物料名称|物料类别" isMultiSelected="true" fieldKeys="code|name|className" searchLabels="物料编码|物料名称|物料类别" searchKeys="code|name|className" ></sys:gridselect-allsuitable>
						<%-- <form:input path="prodId" htmlEscape="false"    class="form-control "/> --%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">核算对象名称：</label>
					<div class="col-sm-10">
						<form:input path="prodName" htmlEscape="false"    class="form-control " readonly = "true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">核算对象类别：</label>
					<div class="col-sm-10">
						<form:input path="prodCatalog" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">是否为叶子节点：</label>
					<div class="col-sm-10">
						<form:select path="prodFinish" class="form-control " disabled = "true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('itemFinish')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">上级核算对象：</label>
					<div class="col-sm-10">
						<sys:treeselectwitheventObj id="fatherId" name="fatherId.id" value="${cosProdObject.fatherId.id}" labelName="fatherId.name" labelValue="${cosProdObject.fatherId.name}"
							title="上级核算对象" url="/cosprodobject/cosProdObjectLeft/treeData" extId="${cosProdObject.id}" cssClass="form-control " allowClear="true"/>
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工艺路线工序管理</title>
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
				<a class="panelButton" href="${ctx}/routingwork/routingWork"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="routingWork" action="${ctx}/routingwork/routingWork/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	

				<div class="form-group">
					<label class="col-sm-2 control-label">工艺编码：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/routing/routing/data" id="routing" name="routing.id" value="${routingWork.routing.id}" labelName="routing.routingName" labelValue="${routingWork.routing.routingName}"
							 title="选择工艺编码" cssClass="form-control required" fieldLabels="工艺编码" fieldKeys="routingName" searchLabels="工艺编码" searchKeys="routingName" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工序编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify url="${ctx}/workprocedure/workProcedure/data" id="workProcedure" name="workProcedure.id" value="${routingWork.workProcedure.id}" labelName="workProcedure.id" labelValue="${routingWork.workProcedure.id}"
							 title="选择工序编码" cssClass="form-control required" targetId="workProcedureName" targetField="workProcedureName" fieldLabels="工序编码|工序名称" fieldKeys="id|workProcedureName" searchLabels="工序编码|工序名称" searchKeys="id|workProcedureName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工序名称：</label>
					<div class="col-sm-10">
						<form:input path="workProcedureName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">负责人编码：</label>
					<div class="col-sm-10">
						<form:input path="workPersonCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">负责人名称：</label>
					<div class="col-sm-10">
						<form:input path="workPersonName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工序职能说明：</label>
					<div class="col-sm-10">
						<form:input path="workProcedureDesc" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">定额工时：</label>
					<div class="col-sm-10">
						<form:input path="workTime" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('routingwork:routingWork:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
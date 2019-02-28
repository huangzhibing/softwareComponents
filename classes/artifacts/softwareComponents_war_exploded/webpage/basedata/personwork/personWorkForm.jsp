<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>人员工种管理</title>
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
				<a class="panelButton" href="${ctx}/personwork/personWork"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="personWork" action="${ctx}/personwork/personWork/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>人员编码：</label>
				<div class="col-sm-10">
					<sys:userselect-modify targetId="personWorkName" id="user" name="user.id" value="${personWork.user.id}" labelName="user.name" labelValue="${personWork.user.no}"
										   cssClass="form-control required"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">人员名称：</label>
				<div class="col-sm-10">
					<form:input path="personWorkName" htmlEscape="false" maxlength="50"  readonly="true"  class="form-control "/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>工种编码：</label>
				<div class="col-sm-10">
					<sys:gridselect-modify url="${ctx}/worktype/workType/data" id="workTypeCode" name="workTypeCode.id" value="${personWork.workTypeCode.workTypeCode}" labelName="workTypeCode.workTypeCode" labelValue="${personWork.workTypeCode.workTypeName}"
										   title="选择工种编码" targetId="workTypeName" targetField="workTypeName" cssClass="form-control required" fieldLabels="工种编码|工种名称" fieldKeys="workTypeCode|workTypeName" searchLabels="工种编码|工种名称" searchKeys="workTypeCode|workTypeName" ></sys:gridselect-modify>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">工种名称：</label>
				<div class="col-sm-10">
					<form:input path="workTypeName" htmlEscape="false" maxlength="50"  readonly="true"  class="form-control "/>
				</div>
			</div>

		<c:if test="${fns:hasPermission('personwork:personWork:edit') || isAdd}">
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
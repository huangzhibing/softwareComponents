<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库人员管理</title>
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
				<a class="panelButton" href="${ctx}/employee/employee"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="employee" action="${ctx}/employee/employee/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input hidden value="${type}" name="type">
		<input hidden value="${typeid}" name="typeid">
		<sys:message content="${message}"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>人员编码：</label>
					<div class="col-sm-10">
						<sys:userselect-modify targetId="empName" id="user" name="user.no" value="${employee.user.no}" labelName="user.nos" labelValue="${employee.user.no}"
							    cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>人员姓名：</label>
					<div class="col-sm-10">
						<form:input path="empName" htmlEscape="false" maxlength="20"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input path="note" htmlEscape="false" maxlength="50"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('employee:employee:edit') || isAdd}">
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
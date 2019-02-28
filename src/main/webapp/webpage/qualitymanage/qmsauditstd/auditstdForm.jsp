<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>稽核标准管理</title>
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
				<a class="panelButton" href="${ctx}/qmsauditstd/auditstd"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="auditstd" action="${ctx}/qmsauditstd/auditstd/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>稽核流程ID：</label>
					<div class="col-sm-10">
						<form:input path="auditpCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>稽核流程名称：</label>
					<div class="col-sm-10">
						<form:input path="auditpName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>稽核标准要素编码：</label>
					<div class="col-sm-10">
						<form:input path="auditItemCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>稽核标准要素名称：</label>
					<div class="col-sm-10">
						<form:input path="auditItemName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>稽核部门编号：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify url="${ctx}/applymain/applyMain/officeData" id="office" name="office.id" value="${auditstd.office.id}" labelName="office.code" labelValue="${auditstd.office.code}"
							 title="部门" 
							 targetId="auditDeptName" targetField="name" 
							 cssClass="form-control required" fieldLabels="稽核部门编号|稽核部门名称" fieldKeys="code|name" searchLabels="稽核部门编号|稽核部门名称" searchKeys="code|name" ></sys:gridselect-modify>
			</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>稽核部门名称：</label>
					<div class="col-sm-10">
						<form:input path="auditDeptName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('qmsauditstd:auditstd:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <input type="button" class="btn btn-primary" value="提交" onclick="sub()">
		                 </div>
		             </div>
		        </div>
		</c:if>
		
		<script type="text/javascript">
			function sub(){
				
				$.ajax({
					type:'POST',
					datatype:'text',
					url:"${ctx}/qmsauditstd/auditstd/ajax",
					data:$("form").serializeArray(),
					contentType:"application/x-www-form-urlencoded",
					success:function(data){
						if(data=="1"){
							top.layer.msg("不能提交", {icon: "${ctype=='success'? 1:2}"});
							
						}
						if(data=="0"){
							$("form").submit();
							top.layer.msg("保存成功", {icon: "${ctype=='success'? 1:1}"});
						
						}
					}
					
				})
				
				
				
			}
		</script>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
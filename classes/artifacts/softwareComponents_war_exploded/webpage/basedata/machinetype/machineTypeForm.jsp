<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备类别定义管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			if($('#machineTypeName').val()){
				$('#machineTypeCode').attr("readonly","true");
			}
			$("#inputForm").validate({
				submitHandler: function(form){
					var index=jp.loading();
					var isAdd='${isAdd}';
					var code;
					$.ajax({
						url:'${ctx}/common/chkCode',
						data:{
							tableName:"mdm_machinetype",
							fieldName:"machine_type_code",
							fieldValue:$('#machineTypeCode').val(),
						},
						success:function(res){
							if(res==='true')
								code="true";
						}
					});
					$.ajax({
						url:'${ctx}/common/chkCode',
						data:{
							tableName:"mdm_machinetype",
							fieldName:"machine_type_name",
							fieldValue:$('#machineTypeName').val(),
						},
						success:function(res){
							if(isAdd===''){
								form.submit();
							}else{
								if(res==='true'&&code=="true"){
									form.submit();
								}else{
									code='';
									jp.warning("设备类别编号或名称已存在!");
									return false;
								}
							}
						}
					})
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
				<a class="panelButton" href="${ctx}/machinetype/machineType"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="machineType" action="${ctx}/machinetype/machineType/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>设备类别代码：</label>
					<div class="col-sm-10">
						<form:input path="machineTypeCode"  htmlEscape="false" maxlength="2"  minlength="2"   class="form-control required number"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>设备类别名称：</label>
					<div class="col-sm-10">
						<form:input path="machineTypeName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('machinetype:machineType:edit') || isAdd}">
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
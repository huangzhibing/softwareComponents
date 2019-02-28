<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验数据验证管理</title>
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
				<a class="panelButton" href="${ctx}/verifyqcnorm/verifyQCNorm"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="verifyQCNorm" action="${ctx}/verifyqcnorm/verifyQCNorm/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">检验单编号：</label>
					<div class="col-sm-10">
						<form:input path="reportId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验对象序列号：</label>
					<div class="col-sm-10">
						<form:input path="objSn" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验对象编码：</label>
					<div class="col-sm-10">
						<form:input path="objCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验对象名称：</label>
					<div class="col-sm-10">
						<form:input path="objName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">规则ID：</label>
					<div class="col-sm-10">
						<form:input path="ruleId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">测试项目：</label>
					<div class="col-sm-10">
						<form:input path="checkprj" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验标准号：</label>
					<div class="col-sm-10">
						<form:input path="qcnormId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验标准名称：</label>
					<div class="col-sm-10">
						<form:input path="qcnormName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">是否检验完成：</label>
					<div class="col-sm-10">
					<form:select path="isfisished" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('isFinished')}" itemLabel="label"
										  itemValue="value" htmlEscape="false"/>
						</form:select>
				
						<%--<form:radiobuttons path="isfisished" items="${fns:getDictList('TrueORFalse')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>--%>
					</div>
				</div>
		<c:if test="${fns:hasPermission('verifyqcnorm:verifyQCNorm:edit') || isAdd}">
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
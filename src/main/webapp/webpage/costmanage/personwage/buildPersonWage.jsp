<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>人工工资成本生成</title>
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
				<%-- <a class="panelButton" href="${ctx}/cosprodobject/cosProdObject"><i class="ti-angle-left"></i> 返回</a> --%>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="settleAccount" action="${ctx}/personwage/personWage/save" method="post" class="form-horizontal">
		
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">当前日期：</label>
					<div class="col-sm-10">
						<input type="text" name="currentDate"   htmlEscape="false"    class="form-control " readonly = "true" value="<fmt:formatDate value = "${settleAccount.currentDate}" pattern="yyyy-MM-dd"/>"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">当前核算期：</label>
					<div class="col-sm-10">
						<form:input path="periodId.periodId" value = "${settleAccount.periodId.periodId}" htmlEscape="false"    class="form-control " readonly = "true"/>
					</div>
				</div>
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在生成...">自动生成工资成本</button>
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
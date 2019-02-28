<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>contractmodel管理</title>
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
				<a class="panelButton" href="${ctx}/contractmodel/ctrItemsModel"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="ctrItemsModel" action="${ctx}/contractmodel/ctrItemsModel/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>合同模板编码：</label>
					<div class="col-sm-10">
						<form:input id="itemOrder" onblur="chk(this.value)" path="itemOrder" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>合同模板名称：</label>
					<div class="col-sm-10">
						<form:input path="itemName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>合同文本：</label>
					<div class="col-sm-10">
						<form:textarea path="itemContents" htmlEscape="false" rows="4"    class="form-control required"/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('contractmodel:ctrItemsModel:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button id="submitbtn" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
<script type="text/javascript">
var flag=1;
$(document).ready(function(){
	if("${isAdd}"=="true"){
		flag=0;
	}else{
		document.getElementById("itemOrder").disabled="true";
	}
})
function chk(a){
	if(flag==0){
		var tableName="sal_ctr_items_model";
		var fieldName="item_order";
		var tableValue=a
		var par={tableName:tableName,fieldName:fieldName,fieldValue:tableValue};
		var url="${ctx}/common/chkCode";
		$.post(url,par,function(data){console.log(data);
			if(data=="false"){
				layer.alert("编码已存在")
				document.getElementById("submitbtn").disabled="disabled";
			}else{
				document.getElementById("submitbtn").disabled="";
			}
		})
	}
	
}
</script>
</body>
</html>
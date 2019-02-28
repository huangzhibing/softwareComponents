<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>制单规则管理</title>
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
		
		function checkCorType(){
			$('#inputForm').submit();
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
				<a class="panelButton" href="${ctx}/billingrule/cosBillRule"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="cosBillRule" action="${ctx}/billingrule/cosBillRule/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>规则编号：</label>
					<div class="col-sm-10">
						<form:input path="ruleId" htmlEscape="false"    class="form-control required" readonly = "true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>规则名称：</label>
					<div class="col-sm-10">
						<form:input path="ruleName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
			<!--  	<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>规则描述：</label>
					<div class="col-sm-10">
						<form:input path="ruleDesc" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>  -->
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据类别                                                                                                                                                                                                    ：</label>
					<div class="col-sm-10">
						<form:select path="billCatalog" htmlEscape="false"    class="form-control required">
						<form:option value="" label=""/>
							<form:options items="${fns:getDictList('billcat')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>借贷方向：</label>
					<div class="col-sm-10">
						<form:select path="blFlag" htmlEscape="false"    class="form-control required">
							<form:options items="${fns:getDictList('blflag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>科目计算规则：</label>
					<div class="col-sm-10">
						<form:select path="itemRule" htmlEscape="false"    class="form-control required">
							<form:options items="${fns:getDictList('itemrule')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>科目规则内容：</label>
					<div class="col-sm-10">
						<%-- <form:input path="itemNotes" htmlEscape="false"    class="form-control required"/> --%>
						<sys:gridselect-allsuitable url="${ctx}/cositem/cosItem/data" id="itemNotes" name="itemNotes" value="${cosBillRule.itemNotes}" 
						labelName="itemCode" labelValue="${cosBillRule.itemNotes}"
							 title="选择科目编号" cssClass="form-control required " targetId="" targetField=""  fieldLabels="科目编码|科目名称" isMultiSelected="true" fieldKeys="itemCode|itemName" searchLabels="科目编码|科目名称" searchKeys="itemCode|itemName" ></sys:gridselect-allsuitable>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>核算对象计算规则：</label>
					<div class="col-sm-10">
						<form:select path="prodRule" htmlEscape="false"    class="form-control required">
							<form:options items="${fns:getDictList('prodRule')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>核算对象计算内容：</label>
					<div class="col-sm-10">
						<%-- <form:input path="resNotes" htmlEscape="false"    class="form-control required"/> --%>
						<sys:gridselect-allsuitable url="${ctx}/cosprodobject/cosProdObject/data" id="resNotes" name="resNotes" value="${cosBillRule.resNotes}" 
						labelName="prodId" labelValue="${cosBillRule.resNotes}"
							 title="选择核算对象编号" cssClass="form-control required " targetId="" targetField=""  fieldLabels="核算对象编码|核算对象名称" isMultiSelected="true" fieldKeys="prodId|prodName" searchLabels="核算对象编码|核算对象名称" searchKeys="prodId|prodName" ></sys:gridselect-allsuitable>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>是否使用制单规则：</label>
					<div class="col-sm-10">
						<form:select path="ruleType" htmlEscape="false"    class="form-control required">
						<form:options items="${fns:getDictList('ruleType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>对应的原始单据类型：</label>
					<div class="col-sm-10">
						<form:select path="corType" htmlEscape="false"    class="form-control required">
							<form:options items="${fns:getDictList('corType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
		<c:if test="${fns:hasPermission('billingrule:cosBillRule:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <input type = "button" onclick = "checkCorType()" id = "sub" name = "sub" value = "提交" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">
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
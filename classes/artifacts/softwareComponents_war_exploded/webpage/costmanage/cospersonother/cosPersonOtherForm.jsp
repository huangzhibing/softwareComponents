<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>
		<c:if test="${empty flag}">其它工资单据录入</c:if>
		<c:if test="${flag=='jihe'}">其它工资单据稽核</c:if>
		<c:if test="${flag=='chaxun'}">其它工资单据查询</c:if>
		<c:if test="${flag=='chexiao'}">其它工资单据撤销</c:if>
	</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			<c:if test="${!isAdd}">
				$("#billNum").attr("readonly",true)
			</c:if>
		   	<c:if test="${not empty flag}">
				$("input").attr("readonly",true)
            </c:if>
			$("#inputForm").validate({
				submitHandler: function(form){
					jp.loading();
					var isAdd="${isAdd}"
					console.log(isAdd)
					if (isAdd){
                        $.ajax({
                            async:false,
                            url: '${ctx}/common/chkCode',
                            data: {
                                tableName: "cos_personother",
                                fieldName: "bill_num",
                                fieldValue: $('#billNum').val()
                            },
                            success: function (res) {
								if (res === 'true') {
									form.submit();
								} else {
									jp.warning("单据号码已存在");
									return false;
								}
                            },
						});
                    }else {
                        form.submit();
                    }
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
				<a class="panelButton" href="${ctx}/cospersonother/cosPersonOther?flag=${flag}"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="cosPersonOther" action="${ctx}/cospersonother/cosPersonOther/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据号码：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>核算期：</label>
					<div class="col-sm-10">
						<form:input readOnly="readOnly" path="periodID" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>班组编码：</label>
					<div class="col-sm-10">
						<c:if test="${not empty flag}">
							<sys:gridselect-item disabled="disabled" url="${ctx}/team/team/data" id="teamcode" name="teamcode.teamCode" value="" labelName="personCode.teamCode" labelValue="${cosPersonOther.personCode.teamCode}"
											title="选择计划班组" cssClass="form-control required" fieldLabels="班组代码|班组名称" fieldKeys="teamCode|teamName" searchLabels="班组代码|班组名称" searchKeys="teamCode|teamName"
							extraField="personName:teamName"></sys:gridselect-item>
						</c:if>
						<c:if test="${empty flag}">
							<sys:gridselect-item url="${ctx}/team/team/data" id="teamcode" name="teamcode.teamCode" value="" labelName="personCode.teamCode" labelValue="${cosPersonOther.personCode.teamCode}"
											title="选择计划班组" cssClass="form-control required" fieldLabels="班组代码|班组名称" fieldKeys="teamCode|teamName" searchLabels="班组代码|班组名称" searchKeys="teamCode|teamName"
							 extraField="personName:teamName"></sys:gridselect-item>
						</c:if>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>班组名称：</label>
					<div class="col-sm-10">
						<form:input path="personName" htmlEscape="false"  readOnly="readOnly"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>科目编码：</label>
					<div class="col-sm-10">
						<c:if test="${not empty flag}">
							<sys:gridselect-item disabled="disabled" title="选择科目" url="${ctx}/cositem/cosItem/data"  extraField="itemName:itemName" id="itemId" name="itemId.id" value="${cosPersonOther.itemId.id}" labelName="itemId.itemCode" labelValue="${cosPersonOther.itemId.itemCode}"
												  cssClass="form-control" fieldKeys="itemCode|itemName" fieldLabels="科目编码|科目名称" searchKeys="itemCode|itemName" searchLabels="科目编码|科目名称"/>
						</c:if>
						<c:if test="${empty flag}">
							<sys:gridselect-item title="选择科目" url="${ctx}/cositem/cosItem/data"  extraField="itemName:itemName" id="itemId" name="itemId.id" value="${cosPersonOther.itemId.id}" labelName="itemId.itemCode" labelValue="${cosPersonOther.itemId.itemCode}"
												 cssClass="form-control" fieldKeys="itemCode|itemName" fieldLabels="科目编码|科目名称" searchKeys="itemCode|itemName" searchLabels="科目编码|科目名称"/>
						</c:if>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>科目名称：</label>
					<div class="col-sm-10">
						<form:input path="itemName" readonly="true" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>工资：</label>
					<div class="col-sm-10">
						<form:input path="itemSum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
		<c:if test="${(fns:hasPermission('cospersonother:cosPersonOther:edit') || isAdd) && flag!='chaxun'}">
				<c:if test="${empty flag}">
					
						<div class="col-lg-3"></div>
						<div class="col-lg-6">
							<div class="form-group text-center">
								<div>
									<button id="save" name="flag" value="save" class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在提交...">
										保存
									</button>
									<button name="flag" value="${flag}" class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在提交...">
									 	提交
								 	</button>
								</div>
							</div>
						</div>
					
				</c:if>
				<c:if test="${flag=='jihe' || flag=='chexiao'}">
					<div class="col-lg-3"></div>
					<div class="col-lg-6">
						 <div class="form-group text-center">
							 <div>
								 <button name="flag" value="${flag}" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">
									 <c:choose>
										 <c:when test="${flag=='jihe'}">稽核</c:when>
										 <c:when test="${flag=='chexiao'}">撤销</c:when>
									 </c:choose>
								 </button>
							 </div>
						 </div>
					</div>
					
				</c:if>
		</c:if>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
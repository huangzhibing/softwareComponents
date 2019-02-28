<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>保存成功管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

	$(document).ready(function() {
		$("#inputForm").validate({
			submitHandler: function(form){
				 var index=jp.loading();
                    var isAdd='${isAdd}';
                    $.ajax({
                        url:'${ctx}/common/chkCode',
                        data:{
                            tableName:"qms_verifyrule",
                            fieldName:"rule_id",
                            fieldValue:$('#ruleId').val(),
                        },
                        success:function(res){
                        	if(isAdd){
                        		if (res==='true'){
                        			form.submit();
                        		}else {
                        			jp.warning("规则编码已存在!");
                                    return false;
                        		}
                        	}else{
                        		form.submit();
                        	}
                        }
                           
                    })
                },
  //              function setUsername(idx,value){
 //       			var id=idx.split("_")[0];
  //      			var ids="#"+id+"_buyername";
  //      			$(ids).val(value);
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
				<a class="panelButton" href="${ctx}/verifyrule/verifyRule"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="verifyRule" action="${ctx}/verifyrule/verifyRule/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		     <c:if test="${isAdd!='true'}">	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>规则编码：</label>
					<div class="col-sm-10">
						<form:input path="ruleId" htmlEscape="false"  readOnly="true"  class="form-control required"/>
					</div>
				</div>
			 </c:if>
			 <c:if test="${isAdd=='true'}">	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>规则编码：</label>
					<div class="col-sm-10">
						<form:input path="ruleId" htmlEscape="false"   class="form-control required"/>
					</div>
				</div>
			</c:if>		
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>测试项目：</label>
					<div class="col-sm-10">
						<form:select path="checkprj" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('CheckPrj')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验标准号：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify url="${ctx}/qmsqcnorm/qCNorm/data" id="qcnorm" name="qcnorm.id" value="${verifyRule.qcnorm.id}" 
						labelName="qcnorm.qcnormId" labelValue="${verifyRule.qcnorm.qcnormId}"
							 title="选择检验标准号" cssClass="form-control required" targetId="qcnormName" targetField="qcnormName" fieldLabels="检验标准号|检验标准名称" fieldKeys="qcnormId|qcnormName" searchLabels="检验标准号|检验标准名称" searchKeys="qcnormId|qcnormName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验标准名称：</label>
					<div class="col-sm-10">
						<form:input path="qcnormName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<%-- <div class="form-group">
					<label class="col-sm-2 control-label">岗位编码：</label>
					<div class="col-sm-10">
						<form:input path="roleCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div> --%>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>岗位名称：</label>
					<div class="col-sm-10">

						<%--<form:input path="roleName" htmlEscape="false"    class="form-control "/>--%>
					<sys:gridselect-pursup  url="${ctx}/verifyrule/verifyRule/sysRole" id="role" name="role.id"
											value="${verifyRule.role.id}"
						labelName="role.name" labelValue="${verifyRule.role.name}"
							 title="选择岗位名称" cssClass="form-control required"
							 fieldLabels="岗位名称" fieldKeys="name" searchLabels="岗位名称" searchKeys="name" 
							 targetId = "roleName" targetField="name">
					</sys:gridselect-pursup>
					<form:hidden path="roleName" htmlEscape="false"    class="form-control "/>
					<%--<sys:gridselect-modify url="${ctx}/sys/role/data" id="role" name="role.id" value="${verifyRule.role.id}" labelName="role.id" labelValue="${verifyRule.role.id}"
							 title="选择岗位名称" cssClass="form-control required" targetId="roleName" targetField="name" fieldLabels="岗位名称" fieldKeys="roleName" searchLabels="岗位名称" searchKeys="roleName" ></sys:gridselect-modify>--%>		
					</div>			
				</div>
		<c:if test="${fns:hasPermission('verifyrule:verifyRule:edit') || isAdd}">
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>审核表管理</title>
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
			
	        $('#justifyDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
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
				<a class="panelButton" href="${ctx}/adtdetail/adtDetail"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="adtDetail" action="${ctx}/adtdetail/adtDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据类型编号：</label>
					<div class="col-sm-10">
						<form:input path="billTypeCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据类型名称：</label>
					<div class="col-sm-10">
						<form:input path="billTypeName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">审核结论：</label>
					<div class="col-sm-10">
						<form:select path="justifyResult" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('justifyResult')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">审核意见：</label>
					<div class="col-sm-10">
						<form:input path="justifyRemark" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>审核人代码：</label>
					<div class="col-sm-10">
						<form:input path="jpersonCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">审核人：</label>
					<div class="col-sm-10">
						<form:input path="justifyPerson" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>审核部门编码：</label>
					<div class="col-sm-10">
						<form:input path="jdeptCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>审核部门名称：</label>
					<div class="col-sm-10">
						<form:input path="jdeptName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>审核岗位编码：</label>
					<div class="col-sm-10">
						<form:input path="jpositionCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>审核岗位名称：</label>
					<div class="col-sm-10">
						<form:input path="jpositionName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>审核日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='justifyDate'>
			                    <input type='text'  name="justifyDate" class="form-control"  value="<fmt:formatDate value="${adtDetail.justifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>模型编号：</label>
					<div class="col-sm-10">
						<form:input path="modelCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">当前审核步骤是否完成标志：</label>
					<div class="col-sm-10">
						<form:select path="isFinished" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('isFinished')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">审核步骤序号：</label>
					<div class="col-sm-10">
						<form:input path="step" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">审核流程结束标志：</label>
					<div class="col-sm-10">
						<form:select path="finishFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('finishFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
		<c:if test="${fns:hasPermission('adtdetail:adtDetail:edit') || isAdd}">
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
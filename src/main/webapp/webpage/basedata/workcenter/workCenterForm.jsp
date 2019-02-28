<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工作中心定义管理</title>
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
				<a class="panelButton" href="${ctx}/workcenter/workCenter"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="workCenter" action="${ctx}/workcenter/workCenter/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">工作中心代码：</label>
					<div class="col-sm-10">
						<form:input id="centerCode" onblur="chk(this.value)" path="centerCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>工作中心名称：</label>
					<div class="col-sm-10">
						<form:input path="centerName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>部门编码：</label>
					<div class="col-sm-10">
						<sys:treeselect-modify targetId="deptName" targetField="code" id="deptCode" name="deptCode.id" value="${workCenter.deptCode.id}" labelName="deptCode.code" labelValue="${workCenter.deptCode.code}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">部门名称：</label>
					<div class="col-sm-10">
						<form:input readOnly="readOnly" id="deptName" path="deptName" htmlEscape="false"    class="form-control "/>
					</div>
				</div> 
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>核算类别：</label>
					<div class="col-sm-10">
						<form:select path="centerType" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('centerType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>关键工作中心：</label>
					<div class="col-sm-10">
						<form:select path="centerKey" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('centerKey')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">人员数：</label>
					<div class="col-sm-10">
						<form:input path="personNum" htmlEscape="false"    class="form-control digits" id="personNum"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">设备数：</label>
					<div class="col-sm-10">
						<form:input path="machineNum" htmlEscape="false"    class="form-control digits" id="machineNum" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">班次数：</label>
					<div class="col-sm-10">
						<form:input path="teamNum" htmlEscape="false"    class="form-control digits" id="teamNum" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工作中心日额定能力：</label>
					<div class="col-sm-10">
						<form:input path="centerRate" htmlEscape="false"    class="form-control digits" id="centerRate" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">设备日额定能力：</label>
					<div class="col-sm-10">
						<form:input path="machineRate" htmlEscape="false"    class="form-control digits" id="machineRate" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">额定班次：</label>
					<div class="col-sm-10">
						<form:input path="teamRate" htmlEscape="false"    class="form-control digits" id="teamRate" />
					</div>
				</div>
				<%-- <div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>工作中心设备类别编码：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/machinetype/machineType/data" id="machineTypeCode" name="machineTypeCode.id" value="${workCenter.machineTypeCode.id}" labelName="machineTypeCode.machineTypeName" labelValue="${workCenter.machineTypeCode.machineTypeName}"
							 title="选择工作中心设备类别编码" cssClass="form-control required" fieldLabels="工作中心设备类别" fieldKeys="machineTypeName" searchLabels="工作中心设备类别" searchKeys="machineTypeName" ></sys:gridselect>
					</div>
				</div> --%>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>工作中心设备类别编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify url="${ctx}/machinetype/machineType/data" id="machineTypeCode" name="machineTypeCode.id" value="${workCenter.machineTypeCode.id}" labelName="machineTypeCode.machineTypeCode" labelValue="${workCenter.machineTypeCode.machineTypeCode}"
							 title="选择工作中心设备类别编码" cssClass="form-control required" fieldLabels="工作中心设备类别" fieldKeys="machineTypeName" searchLabels="工作中心设备类别" searchKeys="machineTypeName" targetId="machineId" targetField="machineTypeName" ></sys:gridselect-modify>
					</div>
				</div>
				 <div class="form-group">
					<label class="col-sm-2 control-label">工作中心设备类别名称：</label>
					<div class="col-sm-10">
						<form:input readOnly="readOnly" id="machineId" path="machineTypeName" htmlEscape="false"    class="form-control "/>
					</div>
				</div> 
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:textarea path="note" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('workcenter:workCenter:edit') || isAdd}">
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
	var personNum = document.getElementById("personNum");
	var machineNum = document.getElementById("machineNum");
	var teamNum = document.getElementById("teamNum");
	var centerRate = document.getElementById("centerRate");
	var machineRate = document.getElementById("machineRate");
	var teamRate = document.getElementById("teamRate");
	if(personNum.value == '') personNum.value=0; 
	if(machineNum.value == '') machineNum.value=0; 
	if(teamNum.value == '') teamNum.value=0; 
	if(centerRate.value == '') centerRate.value=0; 
	if(machineRate.value == '') machineRate.value=0; 
	if(teamRate.value == '') teamRate.value=0; 
</script>
<script type="text/javascript">
	var flag=1;
	$(document).ready(function(){
		if("${isAdd}"=="true"){
			flag=0;
		}else{
			document.getElementById("centerCode").disabled="true";
		}
	})
	function chk(a){
		if(flag==0){
			var tableName="mdm_workcenter";
			var fieldName="center_code";
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
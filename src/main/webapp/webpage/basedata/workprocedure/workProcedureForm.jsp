<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工序定义管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
            var isAdd = '${isAdd}'
            if(isAdd === ''){
                $('#workProcedureId').attr('readOnly',true)
            }
            $("#inputForm").validate({
                submitHandler: function (form) {
                    var isAdd = '${isAdd}'
                    var index = jp.loading();
                    $.ajax({
                        url: '${ctx}/common/chkCode',
                        data: {
                            tableName: "mdm_workprocedure",
                            fieldName: "work_procedure_id",
                            fieldValue: $('#workProcedureId').val()
                        },
                        success:function(res){
                            if(isAdd === ''){
                                form.submit();
                            }else{
                                if(res === 'true'){
                                    form.submit();
                                }else {
                                    jp.warning("编号已存在");
                                    return false;
                                }
                            }
                        },
                    });
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
        // function selectUserNameRu(obj) {
        // userNameRu = document.getElementById("userNameRu");
        // var val=obj.value;
        // userNameRu.value=val;
        // }
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/workprocedure/workProcedure"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="workProcedure" action="${ctx}/workprocedure/workProcedure/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
				<div class="form-group">
					<label class="col-sm-2 control-label disabled"><font color="red">*</font>工序编码：</label>
					<div class="col-sm-10">
						<form:input path="workProcedureId" htmlEscape="false"  class="form-control required" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>工序名称：</label>
					<div class="col-sm-10">
						<form:input path="workProcedureName" htmlEscape="false" maxlength="50"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工序描述：</label>
					<div class="col-sm-10">
						<form:input path="workProcedureDesc" htmlEscape="false" maxlength="100"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>部门编码：</label>
					<div class="col-sm-10">
						<sys:treeselect-modify targetId="deptName" targetField="code" id="deptCode" name="deptCode.code" value="${workProcedure.deptCode.code}" labelName="deptCode.codes" labelValue="${workProcedure.deptCode.code}"
											   title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">部门名称：</label>
					<div class="col-sm-10">
						<form:input id="deptName" path="deptName" htmlEscape="false" maxlength="50"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>负责人编码：</label>
					<div class="col-sm-10">
						<sys:userselect-modify targetId="userNameRu" id="user" name="user.no" value="${workProcedure.user.no}" labelName="user.no" labelValue="${workProcedure.user.no}"
						  cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">负责人名称：</label>
					<div class="col-sm-10">
						<form:input path="userNameRu" htmlEscape="false" maxlength="50"  readonly="true"  class="form-control "/>
					</div>
				</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">定额工时：</label>
				<div class="col-sm-10">
					<form:input path="workTime" htmlEscape="false" maxlength="10"  id="workTime"  class="form-control "/>
				</div>
			</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>工作中心设备类别编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify url="${ctx}/machinetype/machineType/data" id="machineTypeCode" name="machineTypeCode.machineTypeCode" value="${workProcedure.machineTypeCode.machineTypeCode}" labelName="machineTypeCode.machineTypeCode" labelValue="${workProcedure.machineTypeCode.machineTypeCode}"
							 title="选择工作中心设备类别编码" targetId="machineId" targetField="machineTypeName" cssClass="form-control required" fieldLabels="工作中心设备类别名称" fieldKeys="machineTypeName" searchLabels="工作中心设备名称" searchKeys="machineTypeName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工作中心设备类别名称：</label>
					<div class="col-sm-10">
						<form:input path="machineTypeName" id="machineId" htmlEscape="false" maxlength="50"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">是否转序：</label>
					<div class="col-sm-10">
						<form:select path="isConvey" class="form-control ">
							<form:options items="${fns:getDictList('is_convey')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
		<c:if test="${fns:hasPermission('workprocedure:workProcedure:edit') || isAdd}">
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
<script>
	var workTime = document.getElementById("workTime");
	if (workTime.value == "") workTime.value=0;
</script>
<script type="text/javascript">
    var flag=1;
    $(document).ready(function(){
        if("${isAdd}"=="true"){
            flag=0;
        }else{
            document.getElementById("workProcedureId").disabled="true";
        }
    })
    function chk(a){
        if(flag==0){
            var tableName="mdm_workprocedure";
            var fieldName="work_procedure_id";
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
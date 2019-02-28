<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>岗位维护表管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
                submitHandler: function (form) {
                    var index = jp.loading();
                    var isAdd = '${isAdd}'
                    $.ajax({
                        url: '${ctx}/common/chkCode',
                        data: {
                            tableName: "mdm_pos",
                            fieldName: "code",
                            fieldValue: $('#code').val()
                        },
                        success: function (res) {
                            if (isAdd === '') {
                                form.submit();
                            } else {
                                if (res === 'true') {
                                    form.submit();
                                } else {
                                    jp.warning("岗位编号已存在");
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/mdmpos/pos"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="pos" action="${ctx}/mdmpos/pos/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>岗位编码：</label>
					<div class="col-sm-10">
						<form:input path="code" htmlEscape="false" maxlength="20"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>岗位名称：</label>
					<div class="col-sm-10">
						<form:input path="name" htmlEscape="false" maxlength="50"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>所属部门编号：</label>
					<div class="col-sm-10">
						<sys:treeselect-modify targetId="orgzName" targetField="code" 
												id="orgzCode" 
												name="orgzCode.id" value="${pos.orgzCode.id}"
												labelName="orgzCode.name" labelValue="${pos.orgzCode.code}"
												title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" 
												allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>所属部门名称：</label>
					<div class="col-sm-10">
						
						<form:input id="orgzName" path="orgzName" 
						htmlEscape="false" maxlength="50"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">岗位职能说明：</label>
					<div class="col-sm-10">
						<form:textarea path="note" htmlEscape="false" rows="4" maxlength="100"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('mdmpos:pos:edit') || isAdd}">
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
$("#orgzCodeName").val("${pos.orgzCode.code}");
</script>
</body>
</html>
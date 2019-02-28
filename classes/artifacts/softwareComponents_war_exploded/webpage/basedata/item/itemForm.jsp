<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>物料定义管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			var isAdd = '${isAdd}';
            if(isAdd === ''){
                $('#code').attr('readOnly',true)
            }
			$("#inputForm").validate({
				submitHandler: function(form){
					var index = jp.loading();
                    var isAdd = '${isAdd}'
                    $.ajax({
                        url: '${ctx}/common/chkCode',
                        data: {
                            tableName: "mdm_item",
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
                                    jp.warning("物料编号已存在");
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
		$(function(){  
			  
			$('#classCodeName').bind('input propertychange onchange', function() {  
			   alert("Ss");
			   document.getElementById("classCodeName").value = "123";
			});  
			  
			})  
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/item/item"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="item" action="${ctx}/item/item/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>物料类型编码：</label>
					<div class="col-sm-10" onpropertychange="changeInfo()">
					<sys:treeselect-modify-code targetId="className" targetField="classId" id="classCode" name="classCode.id" value="${item.classCode.id}" labelName="classCode.classId" labelValue="${item.classCode.classId}"
							title="物料类型编码" url="/item/itemClassNew/treeData"  cssClass="form-control required" allowClear="true" />
					
						
					</div>
				</div>
				<div class="form-group" >
					<label class="col-sm-2 control-label"><font color="red">*</font>物料类型名称：</label>
					<div class="col-sm-10" readonly>
						<form:input path="className" htmlEscape="false"   id="className" class="form-control required" disabled="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>物料编码：</label>
					<div class="col-sm-10">
						<form:input path="code" htmlEscape="false" minlength="12" maxlength="12"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>物料名称：</label>
					<div class="col-sm-10">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>规格型号：</label>
					<div class="col-sm-10">
						<form:input path="specModel" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">材质：</label>
					<div class="col-sm-10">
						<form:input path="texture" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">国标码：</label>
					<div class="col-sm-10">
						<form:input path="gb" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">图号：</label>
					<div class="col-sm-10">
						<form:input path="drawNO" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计量单位代码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify-code targetId="unit" targetField="unitName" url="${ctx}/unit/unit/data" id="unitCode" name="unitCode.unitCode" value="${item.unitCode.unitCode}" labelName="unitCode.unitCode" labelValue="${item.unitCode.unitCode}"
							 title="选择计量单位编码" cssClass="form-control required" fieldLabels="计量单位名称" fieldKeys="unitName" searchLabels="计量单位名称" searchKeys="unitName" ></sys:gridselect-modify-code>
					</div>
				</div>
				<div class="form-group" readonly>
					<label class="col-sm-2 control-label"><font color="red">*</font>计量单位名称：</label>
					<div class="col-sm-10" >
						<form:input path="unit" htmlEscape="false"    class="form-control required" disabled="true" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">安全库存量：</label>
					<div class="col-sm-10">
						<form:input path="safetyQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">采购提前期：</label>
					<div class="col-sm-10">
						<form:input path="leadTime" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">最大库存：</label>
					<div class="col-sm-10">
						<form:input path="maxStorage" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">最小库存：</label>
					<div class="col-sm-10">
						<form:input path="minStorage" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划价格：</label>
					<div class="col-sm-10">
						<form:input path="planPrice" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>系统标识：</label>
					<div class="col-sm-10">
						<sys:checkbox id="systemSign" name="systemSign" items="${fns:getDictList('systemSign')}" values="${item.systemSign}" cssClass="i-checks required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">关键件标识：</label>
					<div class="col-sm-10">
						<form:select path="isKey" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('is_key')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">采购批量：</label>
					<div class="col-sm-10">
						<form:input path="itemBatch" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">整机标识：</label>
					<div class="col-sm-10">
						<form:select path="isInMotor" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('is_in_motor')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
                    <label class="col-sm-2 control-label">库存检验周期：</label>
                    <div class="col-sm-10">
                        <form:input path="cycleTime" htmlEscape="false"    class="form-control "/>
                    </div>
                </div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>是否半成品：</label>
					<div class="col-sm-10">
						<form:select path="isPart"  class="form-control required">
							<form:option value="" label=""/>
							<form:option value="y" label="是"/>
							<form:option value="n" label="否"/>
						</form:select>
					</div>
				</div>
		<c:if test="${fns:hasPermission('item:item:edit') || isAdd}">
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
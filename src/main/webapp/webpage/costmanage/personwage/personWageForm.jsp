<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>人工工资单据管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		
		function getPlanWage() {
			var planWage = $("#itemUnit").val()*$("#itemQty").val();
			$("#wagePlan").val(planWage);
        }

        function getAssignWage() {
            var assignWage = $("#wageUnit").val()*$("#itemQty").val();
            $("#wageAssign").val(assignWage);
            var planWage = $("#wagePlan").val();
			assignWage = parseFloat(assignWage);
			planWage = parseFloat(planWage)

            var payWage = assignWage+planWage;
            $("#wagePay").val(payWage);
        }

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
			
	        $('#checkDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#makeDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });


            $("#wagePlan").val($("#itemUnit").val()*$("#itemQty").val());

            $("#wageAssign").val($("#wageUnit").val()*$("#itemQty").val());

            $("#wagePay").val(parseFloat($("#wagePlan").val())+parseFloat($("#wageAssign").val()));


            if("${flag}" === "jihe" ||"${flag}" === "cexiao"||"${flag}" === "chaxun"){
                $('input').attr('readonly',true);
                $("#workProcedureButton").attr('disabled',true);
                $("#personButton").attr('disabled',true);
                $("#itemCodeButton").attr('disabled',true);
            }
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
				<a class="panelButton" href="${ctx}/personwage/personWage?flag=${flag}"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="personWage" action="${ctx}/personwage/personWage/save?flag=${flag}" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>核算期：</label>
					<div class="col-sm-10">
						<form:input readonly="true" path="periodId" htmlEscape="false"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>工序代码：</label>
					<div class="col-sm-10">
						<sys:gridselect-item url="${ctx}/workprocedure/workProcedure/data" id="workProcedure" name="workCode.id" value="${personWage.workCode.id}" labelName="workCode.workProcedureId" labelValue="${personWage.workCode.workProcedureId}"
											 title="选择工序代码" cssClass="form-control required" targetId="workProcedureName" targetField="workProcedureName" fieldLabels="工序代码|工序名称" fieldKeys="workProcedureId|workProcedureName" searchLabels="工序代码|工序名称" searchKeys="workProcedureId|workProcedureName" ></sys:gridselect-item>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>工序名称：</label>
					<div class="col-sm-10">
						<input id="workProcedureName" name="workProcedureName" readonly="true" value="${personWage.workCode.workProcedureName}" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>人员代码：</label>
					<div class="col-sm-10">
						<sys:userselect-item   targetId="personName" id="person" name="personCode.id" value="${personWage.personCode.id}" labelName="personCode.no" labelValue="${personWage.personCode.no}"
											  cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>人员名称：</label>
					<div class="col-sm-10">
						<input readonly="true" id="personName" name="personName" htmlEscape="false" value="${personWage.personCode.name}"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>产品代码：</label>
					<div class="col-sm-10">
						<sys:gridselect-item url="${ctx}/product/product/data" id="itemCode" name="itemCode.item.id" value="${personWage.itemCode.item.id}" labelName="itemCode.item.code" labelValue="${personWage.itemCode.item.code}"
											 title="选择产品代码" cssClass="form-control required" fieldLabels="产品编码|产品名称" fieldKeys="item.code|itemNameRu" searchLabels="产品编码|产品名称" searchKeys="item.code|itemNameRu"
                                              extraField="itemCodeNames:item.code;productName:itemNameRu" ></sys:gridselect-item>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>产品名称：</label>
					<div class="col-sm-10">
						<input readonly="true" id="productName" name="productName" htmlEscape="false"  value="${personWage.itemCode.item.name}" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单位计件工资：</label>
					<div class="col-sm-10">
						<form:input path="itemUnit" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>工作量：</label>
					<div class="col-sm-10">
						<form:input path="itemQty" htmlEscape="false"  onchange="getPlanWage()"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计件工资：</label>
					<div class="col-sm-10">
						<input readonly="true" id="wagePlan" htmlEscape="false"  class="form-control required"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单位分配工资：</label>
					<div class="col-sm-10">
						<form:input path="wageUnit" htmlEscape="false" readOnly="true" value="0" onchange="getAssignWage()" class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>分配工资：</label>
					<div class="col-sm-10">
						<input readonly="true" id="wageAssign" htmlEscape="false"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>应付工资：</label>
					<div class="col-sm-10">
						<input readonly="true" id="wagePay" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">单据状态：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:radiobuttons path="billStatus" items="${fns:getDictList('billStatus')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">单据类型：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:radiobuttons path="billMode" items="${fns:getDictList('billMode')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">稽核人代码：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="checkId" htmlEscape="false"    class="form-control required"/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">稽核日期：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<p class="input-group">--%>
							<%--<div class='input-group form_datetime' id='checkDate'>--%>
			                    <%--<input type='text'  name="checkDate" class="form-control required"  value="<fmt:formatDate value="${personWage.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>--%>
			                    <%--<span class="input-group-addon">--%>
			                        <%--<span class="glyphicon glyphicon-calendar"></span>--%>
			                    <%--</span>--%>
			                <%--</div>						            --%>
			            <%--</p>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">稽核人姓名：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="checkName" htmlEscape="false"    class="form-control required"/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">录入人代码：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="makeId" htmlEscape="false"    class="form-control required"/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">录入日期：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<p class="input-group">--%>
							<%--<div class='input-group form_datetime' id='makeDate'>--%>
			                    <%--<input type='text'  name="makeDate" class="form-control required"  value="<fmt:formatDate value="${personWage.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>--%>
			                    <%--<span class="input-group-addon">--%>
			                        <%--<span class="glyphicon glyphicon-calendar"></span>--%>
			                    <%--</span>--%>
			                <%--</div>						            --%>
			            <%--</p>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">录入人姓名：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="makeName" htmlEscape="false"    class="form-control required"/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">成本单据号：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="cosBillNum" htmlEscape="false"    class="form-control required"/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">对应单据号：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="corBillNum" htmlEscape="false"    class="form-control required"/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">对应的序号：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="corSeqNo" htmlEscape="false"    class="form-control required"/>--%>
					<%--</div>--%>
				<%--</div>--%>
		<c:if test="${fns:hasPermission('personwage:personWage:edit') || isAdd}">
				<div class="col-lg-3"></div>
            <c:if test="${empty flag}">
                <div class="col-lg-3">
                    <div class="form-group text-center">
                        <div>
                            <button name="saveBtn" value="true" class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在保存...">保  存</button>
                            <button name="submitBtn" value="true" class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${flag == 'jihe'}">
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在稽核...">
								 稽 核
							 </button>
		                 </div>
		             </div>
		        </div>
		    </c:if>
			<c:if test="${flag == 'cexiao'}">
				<div class="col-lg-6">
					<div class="form-group text-center">
						<div>
							<button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在撤销...">
								撤 销
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
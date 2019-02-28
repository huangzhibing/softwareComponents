<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>滚动计划管理</title>
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
			
	        $('#requestDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#planArrivedate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#purStartDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#purArriveDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#makeDate').datetimepicker({
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
				<a class="panelButton" href="${ctx}/rollplannewquery/rollPlanNew"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="rollPlanNew" action="${ctx}/rollplannewquery/rollPlanNew/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">单据编号：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据类型：</label>
					<div class="col-sm-10">
						<form:input path="billType" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">序号：</label>
					<div class="col-sm-10">
						<form:input path="serialNum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">物料编号：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/item/item/data" id="itemCode" name="itemCode.id" value="${rollPlanNew.itemCode.id}" labelName="itemCode.code" labelValue="${rollPlanNew.itemCode.code}"
							 title="选择物料编号" cssClass="form-control required" fieldLabels="物料编号|物料名称" fieldKeys="code|name" searchLabels="物料编号|物料名称" searchKeys="code|name" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">物料名称：</label>
					<div class="col-sm-10">
						<form:input path="itemName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">物料规格：</label>
					<div class="col-sm-10">
						<form:input path="itemSpecmodel" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计量单位：</label>
					<div class="col-sm-10">
						<form:input path="unitName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">需求数量：</label>
					<div class="col-sm-10">
						<form:input path="applyQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划价：</label>
					<div class="col-sm-10">
						<form:input path="planPrice" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划总额：</label>
					<div class="col-sm-10">
						<form:input path="planSum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">需求日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='requestDate'>
			                    <input type='text'  name="requestDate" class="form-control"  value="<fmt:formatDate value="${rollPlanNew.requestDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划到达日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='planArrivedate'>
			                    <input type='text'  name="planArrivedate" class="form-control"  value="<fmt:formatDate value="${rollPlanNew.planArrivedate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">说明：</label>
					<div class="col-sm-10">
						<form:input path="notes" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">质量要求：</label>
					<div class="col-sm-10">
						<form:input path="massRequire" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">需求说明：</label>
					<div class="col-sm-10">
						<form:input path="applyQtyNotes" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">采购提前期：</label>
					<div class="col-sm-10">
						<form:input path="batchLt" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">采购开始日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='purStartDate'>
			                    <input type='text'  name="purStartDate" class="form-control"  value="<fmt:formatDate value="${rollPlanNew.purStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划到货日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='purArriveDate'>
			                    <input type='text'  name="purArriveDate" class="form-control"  value="<fmt:formatDate value="${rollPlanNew.purArriveDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">采购数量：</label>
					<div class="col-sm-10">
						<form:input path="purQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库存数量：</label>
					<div class="col-sm-10">
						<form:input path="invQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">安全库存：</label>
					<div class="col-sm-10">
						<form:input path="safetyQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">实际数量：</label>
					<div class="col-sm-10">
						<form:input path="realQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">在途量：</label>
					<div class="col-sm-10">
						<form:input path="roadQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='makeDate'>
			                    <input type='text'  name="makeDate" class="form-control"  value="<fmt:formatDate value="${rollPlanNew.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">需求部门编码：</label>
					<div class="col-sm-10">
						<sys:treeselect id="applyDeptId" name="applyDeptId.id" value="${rollPlanNew.applyDeptId.id}" labelName="applyDeptId.code" labelValue="${rollPlanNew.applyDeptId.code}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">需求部门名称：</label>
					<div class="col-sm-10">
						<form:input path="applyDept" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人代码：</label>
					<div class="col-sm-10">
						<sys:userselect id="makeEmpid" name="makeEmpid.id" value="${rollPlanNew.makeEmpid.id}" labelName="makeEmpid.no" labelValue="${rollPlanNew.makeEmpid.no}"
							    cssClass="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人名称：</label>
					<div class="col-sm-10">
						<form:input path="makeEmpname" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">来源：</label>
					<div class="col-sm-10">
						<form:input path="sourseFlag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">操作标志：</label>
					<div class="col-sm-10">
						<form:input path="opFlag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划号：</label>
					<div class="col-sm-10">
						<form:input path="planNum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">移动价：</label>
					<div class="col-sm-10">
						<form:input path="costPrice" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">物料材质：</label>
					<div class="col-sm-10">
						<form:input path="itemTexture" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('rollplannewquery:rollPlanNew:edit') || isAdd}">
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
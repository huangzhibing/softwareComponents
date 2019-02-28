<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>物料需求计划管理</title>
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
			
	        $('#planBdate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#planEDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#makeDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#confirmDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#auditDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#deliveryDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#closeDate').datetimepicker({
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
				<a class="panelButton" href="${ctx}/mrpplanquery/mrpPlanQuery"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="mrpPlanQuery" action="${ctx}/mrpplanquery/mrpPlanQuery/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">MRP计划号：</label>
					<div class="col-sm-3">
						<form:input path="MRPPlanID" htmlEscape="false"  readonly="true"    class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">mps计划号：</label>
					<div class="col-sm-3">
						<form:input path="MPSplanid" htmlEscape="false"  readonly="true"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">物料编码：</label>
					<div class="col-sm-3">
						<form:input path="itemCode" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">物料名称：</label>
						<div class="col-sm-3">
							<form:input path="itemName" htmlEscape="false"  readonly="true"    class="form-control "/>
						</div>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">计划数量：</label>
					<div class="col-sm-3">
						<form:input path="planQty" htmlEscape="false"  readonly="true"    class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">计划状态：</label>
					<div class="col-sm-3">
						<form:select path="planStatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('mps_planstatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">计划开始日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
							<div class='input-group form_datetime' id='planBdate'>
			                    <input type='text'  name="planBdate" class="form-control"  readonly="true"  value="<fmt:formatDate value="${mrpPlanQuery.planBdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>

					<label class="col-sm-2 control-label">计划结束日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
						<div class='input-group form_datetime' id='planEDate'>
							<input type='text'  name="planEDate" class="form-control" readonly="true"   value="<fmt:formatDate value="${mrpPlanQuery.planEDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
						</div>
						</p>
					</div>
				</div>



				<div class="form-group">
					<label class="col-sm-2 control-label">计划制定人：</label>
					<div class="col-sm-3">
						<form:input path="makePID" htmlEscape="false"  readonly="true"    class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">计划制定日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
						<div class='input-group form_datetime' id='makeDate'>
							<input type='text'  name="makeDate" class="form-control"  readonly="true"  value="<fmt:formatDate value="${mrpPlanQuery.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
						</div>
						</p>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label">审批意见：</label>
					<div class="col-sm-3">
						<form:input path="auditComment" htmlEscape="false"   readonly="true"   class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-3">
						<form:input path="remarks" htmlEscape="false"   readonly="true"   class="form-control "/>
					</div>
				</div>

		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
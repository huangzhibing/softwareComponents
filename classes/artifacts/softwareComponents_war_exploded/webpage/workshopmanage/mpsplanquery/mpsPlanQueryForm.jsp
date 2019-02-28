<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>主生产计划管理</title>
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
			
	        $('#planBDate').datetimepicker({
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
				<a class="panelButton" href="${ctx}/mpsplanquery/mpsPlanQuery"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="mpsPlanQuery" action="${ctx}/mpsplanquery/mpsPlanQuery/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>主生产计划号：</label>
					<div class="col-sm-3">
						<form:input path="mpsPlanid" htmlEscape="false"  readonly="true"   class="form-control required"/>
					</div>

					<label class="col-sm-2 control-label">内部订单号：</label>
					<div class="col-sm-3">
						<form:input path="reqID" htmlEscape="false"   readonly="true"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">产品编码：</label>
					<div class="col-sm-3">
						<form:input path="prodCode" htmlEscape="false"   readonly="true"   class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">产品名称：</label>
					<div class="col-sm-3">
						<form:input path="prodName" htmlEscape="false"    readonly="true"  class="form-control "/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">计划数量：</label>
					<div class="col-sm-3">
						<form:input path="planQty" htmlEscape="false"    readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">计划状态：</label>
					<div class="col-sm-3">
						<form:select path="planStatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('mps_planstatus')}"  disabled="true" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label">计划开始日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
						<div class='input-group form_datetime' id='planBDate'>
							<input type='text'  name="planBDate" class="form-control"  readonly="true"  value="<fmt:formatDate value="${mpsPlanQuery.planBDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
			                    <input type='text'  name="planEDate" class="form-control"  readonly="true"  value="<fmt:formatDate value="${mpsPlanQuery.planEDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>


				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">计划制定日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
							<div class='input-group form_datetime' id='makeDate'>
			                    <input type='text'  name="makeDate" class="form-control"  readonly="true"  value="<fmt:formatDate value="${mpsPlanQuery.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>

					<label class="col-sm-2 control-label">审核意见：</label>
					<div class="col-sm-3">
						<form:input path="auditComment" htmlEscape="false"   readonly="true"  class="form-control "/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-8">
						<form:textarea path="remarks" htmlEscape="false" rows="2"   readonly="true"  class="form-control "/>
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
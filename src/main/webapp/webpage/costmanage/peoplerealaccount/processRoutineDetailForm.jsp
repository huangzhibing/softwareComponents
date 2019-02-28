<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>加工路线单明细</title>
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
			
	        $('#planbdate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#planedate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#makedate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#confirmdate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#deliverydate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });

	        $('input').attr("readOnly",true);
		});
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="processRoutineDetail" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	

				<div class="form-group">
					<label class="col-sm-2 control-label">加工路线单号：</label>
					<div class="col-sm-10">
						<form:input path="routineBillNo" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">生产序号：</label>
					<div class="col-sm-10">
						<form:input path="produceNo" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工作中心代码：</label>
					<div class="col-sm-10">
						<form:input path="centerCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划生产数量：</label>
					<div class="col-sm-10">
						<form:input path="planQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划生产日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='planbdate'>
			                    <input type='text'  name="planBData" class="form-control"  value="<fmt:formatDate value="${processRoutineDetail.planBData}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">实际生产数量：</label>
					<div class="col-sm-10">
						<form:input path="realQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">实际生产日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='planedate'>
			                    <input type='text'  name="planEDate" class="form-control"  value="<fmt:formatDate value="${processRoutineDetail.planEDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">负责人：</label>
					<div class="col-sm-10">
						<form:input path="personIncharge" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划班组：</label>
					<div class="col-sm-10">
						<form:input path="teamCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">班次：</label>
					<div class="col-sm-10">
						<form:input path="shiftname" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划工时：</label>
					<div class="col-sm-10">
						<form:input path="workhour" htmlEscape="false"    class="form-control "/>
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
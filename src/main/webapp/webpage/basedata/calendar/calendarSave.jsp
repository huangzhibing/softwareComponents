<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>企业日历定义管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
				    console.log($('#curDate2').val())
					var index=jp.loading();
					var isAdd='${isAdd}';
					$.ajax({
						url:'${ctx}/calendar/calendar/chkCode',
						data:{
							tableName:"mdm_calendar",
							fieldName:"cur_date",
							sfieldValue:$('#curDate2').val(),
							efieldValue:$('#endDate2').val(),
						},
						success:function(res){
							if(isAdd===''){
								form.submit();
							}else{
								if(res==='true'){
									form.submit();
								}else{
									jp.warning("该日期已存在!");
									return false;
								}
							}
						}
					})
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
			
	        $('#curDate,#endDate').datetimepicker({
				 format: "YYYY-MM-DD",
				 ignoreReadonly: true,
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
				<a class="panelButton" href="${ctx}/calendar/calendar"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="calendar" action="${ctx}/calendar/calendar/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div  class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>起始日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='curDate'>
			                    <input type='text' id="curDate2"  readonly="readonly" name="curDate"  class="form-control"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>终止日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='endDate'>
			                    <input type='text' id="endDate2"   name="endDate" readonly="readonly" class="form-control"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>工作标准工时：</label>
					<div class="col-sm-10">
						<input name="workTime"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>休息标准工时：</label>
					<div class="col-sm-10">
						<input name="restTime"    class="form-control required"/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('calendar:calendar:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">生成企业日历</button>
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>加工路线单明细管理</title>
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
				<a class="panelButton" href="${ctx}/processroutinedetail/processRoutineDetail"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="processRoutineDetail" action="${ctx}/processroutinedetail/processRoutineDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">车间计划作业号：</label>
					<div class="col-sm-10">
						<form:input path="processbillno" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">批次序号：</label>
					<div class="col-sm-10">
						<form:input path="batchno" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">加工路线单号：</label>
					<div class="col-sm-10">
						<form:input path="routinebillno" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">生产序号：</label>
					<div class="col-sm-10">
						<form:input path="produceno" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工艺编码：</label>
					<div class="col-sm-10">
						<form:input path="routingcode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工序编码：</label>
					<div class="col-sm-10">
						<form:input path="workprocedurecode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工作中心代码：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/workcenter/workCenter/data" id="centercode" name="centercode.centerCode" value="${processRoutineDetail.centercode.centerCode}" labelName="centercode.centerCode" labelValue="${processRoutineDetail.centercode.centerCode}"
							 title="选择工作中心代码" cssClass="form-control required" fieldLabels="工作中心代码|工作中心名称" fieldKeys="centerCode|centerName" searchLabels="工作中心代码|工作中心名称" searchKeys="centerCode|centerName" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">末道工艺标志：</label>
					<div class="col-sm-10">
						<form:input path="islastrouting" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">产品编码：</label>
					<div class="col-sm-10">
						<form:input path="prodcode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划生产数量：</label>
					<div class="col-sm-10">
						<form:input path="planqty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划生产日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='planbdate'>
			                    <input type='text'  name="planbdate" class="form-control"  value="<fmt:formatDate value="${processRoutineDetail.planbdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
						<form:input path="realqty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">实际生产日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='planedate'>
			                    <input type='text'  name="planedate" class="form-control"  value="<fmt:formatDate value="${processRoutineDetail.planedate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-10">
						<form:input path="billstate" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">派工状态：</label>
					<div class="col-sm-10">
						<form:input path="assignedstate" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制定人：</label>
					<div class="col-sm-10">
						<form:input path="makepid" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">实作工作中心：</label>
					<div class="col-sm-10">
						<form:input path="actualcentercode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>负责人：</label>
					<div class="col-sm-10">
						<sys:userselect id="personincharge" name="personincharge" value="${processRoutineDetail.personincharge}" labelName="" labelValue="${processRoutineDetail.}"
							    cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计划班组：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/team/team/data" id="teamcode" name="teamcode.teamCode" value="${processRoutineDetail.teamcode.teamCode}" labelName="teamcode.teamCode" labelValue="${processRoutineDetail.teamcode.teamCode}"
							 title="选择计划班组" cssClass="form-control required" fieldLabels="班组代码|班组名称" fieldKeys="teamCode|teamName" searchLabels="班组代码|班组名称" searchKeys="teamCode|teamName" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">实作班组：</label>
					<div class="col-sm-10">
						<form:input path="actualteamcode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>班次：</label>
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
				<div class="form-group">
					<label class="col-sm-2 control-label">实作工时：</label>
					<div class="col-sm-10">
						<form:input path="actualworkhour" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制定日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='makedate'>
			                    <input type='text'  name="makedate" class="form-control"  value="<fmt:formatDate value="${processRoutineDetail.makedate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">提交人：</label>
					<div class="col-sm-10">
						<form:input path="confirmpid" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">提交日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='confirmdate'>
			                    <input type='text'  name="confirmdate" class="form-control"  value="<fmt:formatDate value="${processRoutineDetail.confirmdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">下达人：</label>
					<div class="col-sm-10">
						<form:input path="deliverypid" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">下达日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='deliverydate'>
			                    <input type='text'  name="deliverydate" class="form-control"  value="<fmt:formatDate value="${processRoutineDetail.deliverydate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">产品名称：</label>
					<div class="col-sm-10">
						<form:input path="prodname" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工艺名称：</label>
					<div class="col-sm-10">
						<form:input path="routingname" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单件序号：</label>
					<div class="col-sm-10">
						<form:input path="seqno" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('processroutinedetail:processRoutineDetail:edit') || isAdd}">
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
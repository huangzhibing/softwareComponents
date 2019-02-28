<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>主生产计划编制</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
				    if($('#planBDateInput').val()>$('#planEDateInput').val()){//判断计划开始时间是否大于计划结束时间
				        jp.warning("开始时间不能大于结束时间！");
                        $('#planEDateInput').focus();
				        return;
					}
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
				 format: "YYYY-MM-DD"
		    });
	        $('#planEDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
			//非审核不通过状态不显示审核不通过意见
            if($('#planStatus').val()!="U"){
                $("#comment").hide();
            }

		});


        /*
    	保存按钮事件
    	 */
        function button_save(){
            $('#makeDate').val(jp.dateFormat(new Date(),"YYYY-MM-dd hh:mm:ss"));//修改制单日期时间
            $('#confirmDate').attr("disabled","true");//禁用提交日期字段（默认为空串“”，日期插入""后台会报错）;
            $('#inputForm').submit();//提交表单
        }

		/*
		提交按钮事件
		 */
		function button_submit(){
            jp.confirm("确认提交？",function () {
                $('#planStatus').val('C');//更改计划状态
                $('#confirmPID').val('${fns:getUser().id}');//记录提交人id
                $('#makeDate').val(jp.dateFormat(new Date(),"YYYY-MM-dd hh:mm:ss"));//修改制单日期时间
                $('#confirmDate').val(jp.dateFormat(new Date(),"YYYY-MM-dd hh:mm:ss"));//记录提交日期时间
                $('#inputForm').submit();//提交表单
            });

		}




	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/ppc/mpsPlan"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="mpsPlan" action="${ctx}/ppc/mpsPlan/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="planStatus"/>
		<form:hidden path="confirmPID"/>
		<form:hidden path="confirmDate"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>主生产计划号：</label>
					<div class="col-sm-10">
						<form:input path="mpsPlanid" htmlEscape="false" readonly="true" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>销售订单号：</label>
					<div class="col-sm-10">
						<form:input path="reqID" htmlEscape="false" readonly="true" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>产品编码：</label>
					<div class="col-sm-10">
						<form:input path="prodCode" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>产品名称：</label>
					<div class="col-sm-10">
						<form:input path="prodName" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计划数量：</label>
					<div class="col-sm-10">
						<form:input path="planQty" htmlEscape="false"    class="form-control required number"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计划开始日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='planBDate'>
			                    <input type='text' id='planBDateInput' name="planBDate" class="form-control"  value="<fmt:formatDate value="${mpsPlan.planBDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计划结束日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='planEDate'>
			                    <input type='text' id='planEDateInput' name="planEDate" class="form-control"  value="<fmt:formatDate value="${mpsPlan.planEDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划制定人：</label>
					<div class="col-sm-10">
						<form:input path="makeName" htmlEscape="false"  readonly="true"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划制定日期：</label>
					<div class="col-sm-10">
						<input type='text' id="makeDate" name="makeDate" class="form-control" readonly="true" value="<fmt:formatDate value="${mpsPlan.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>

					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>

			<div id="comment">
				<div class="form-group">
					<label class="col-sm-2 control-label">审核不通过意见：</label>
					<div class="col-sm-10">
						<form:textarea path="auditComment" htmlEscape="false" rows="4"  disabled="true"  class="form-control "/>
					</div>
				</div>
			</div>
		<c:if test="${fns:hasPermission('ppc:mpsPlan:edit') || isAdd}">
				<div class="col-lg-3"></div>
				<div class="col-lg-2">
					<div class="form-group text-center">
						<div>
							<input type="button" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="button_save()" data-loading-text="正在保存..." value="保 存"></input>

						</div>
					</div>
				</div>
				<div class="col-lg-2"></div>
		        <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
							 <input type="button" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="button_submit()" data-loading-text="正在提交..." value="提 交"></input>
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>企业核算期定义管理</title>
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

            $('#periodBegin').datetimepicker({
                format: "YYYY-MM-DD"
            });
            $('#periodEnd').datetimepicker({
                format: "YYYY-MM-DD"
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
						<a class="panelButton" href="${ctx}/period/period"><i class="ti-angle-left"></i> 返回</a>
					</h3>
				</div>
				<div class="panel-body">
					<form:form id="inputForm" modelAttribute="period" action="${ctx}/period/period/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<sys:message content="${message}"/>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>核算期编码：</label>
							<div class="col-sm-10">
								<form:input path="periodId" htmlEscape="false" maxlength="6"  minlength="6"   class="form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>核算期开始日期：</label>
							<div class="col-sm-10">
								<p class="input-group">
								<div class='input-group form_datetime' id='periodBegin'>
									<input type='text'  name="periodBegin" class="form-control"  value="<fmt:formatDate value="${period.periodBegin}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
									<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
								</div>
								</p>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>核算期结束日期：</label>
							<div class="col-sm-10">
								<p class="input-group">
								<div class='input-group form_datetime' id='periodEnd'>
									<input type='text'  name="periodEnd" class="form-control"  value="<fmt:formatDate value="${period.periodEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
									<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
								</div>
								</p>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>起始时间：</label>
							<div class="col-sm-10">
								<form:input path="beginHour" htmlEscape="false"    class="form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>截止时间：</label>
							<div class="col-sm-10">
								<form:input path="endHour" htmlEscape="false"    class="form-control required"/>
							</div>
						</div>
						<c:if test="${fns:hasPermission('period:period:edit') || isAdd}">
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
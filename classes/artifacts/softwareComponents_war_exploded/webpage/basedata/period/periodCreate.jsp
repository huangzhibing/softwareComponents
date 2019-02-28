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
                    var index = jp.loading();
                    $.ajax({
                        url: '${ctx}/common/chkCode',
                        data: {
                            tableName: "mdm_period",
                            fieldName: "year",
                            fieldValue: $('input[name="periodYear"]').val()
                        },
                        success: function (res) {
                                if (res === 'true') {
                                    form.submit();
                                } else {
                                    jp.warning("该年核算期已存在");
                                    return false;
                                }
                            	jp.close();
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
					<form:form id="inputForm" modelAttribute="period" action="${ctx}/period/period/created" method="post" class="form-horizontal">
					<form:hidden path="id"/>
					<sys:message content="${message}"/>

					<div class="form-group">
						<label class="col-sm-2 control-label"><font color="red">*</font>年份：</label>
						<div class="col-sm-10">
							<input name="periodYear"  class="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><font color="red">*</font>结束日期所在月：</label>
						<div class="col-sm-10">
							<input name="endMonth" placeholder="1代表本月 0代表下月" class="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><font color="red">*</font>结束日期：</label>
						<div class="col-sm-10">
							<input name="endDate"  class="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">时间点：</label>
						<div class="col-sm-10">
							<input name="endtime" placeholder="默认为00:00:00，如需修改请改为整点，如17表示下午五点" class="form-control"/>
						</div>
					</div>

					<div class="col-lg-3"></div>
					<div class="col-lg-6">
						 <div class="form-group text-center">
							 <div>
								 <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">生成核算期</button>
							 </div>
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
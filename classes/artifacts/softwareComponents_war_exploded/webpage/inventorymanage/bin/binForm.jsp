<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>货区管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if(validateForm.form()){
                $table = table;
                $topIndex = index;
                $("#inputForm").submit();
                return true;
            }

            return false;
        }
		$(document).ready(function() {
            validateForm = $("#inputForm").validate({
				submitHandler: function(form){
                    jp.loading();
//					form.submit();

                    $.post("${ctx}/bin/bin/save",$('#inputForm').serialize(),function(data){

                        if(data.success){
                            $table.bootstrapTable('refresh');

                            var pro=parent.$("iframe[src$='/softwareComponents/a/warehouse/wareHouse']")[0]
//                            console.log(pro)
                            pro.contentWindow.refresh()
                            jp.success(data.msg);

                        }else{
                            jp.error(data.msg);
                        }

                        jp.close($topIndex);//关闭dialog
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
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="bin" action="${ctx}/bin/bin/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库房号：</label>
					<div class="col-sm-10">
						<form:input  readonly="true" path="wareId" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>货区号：</label>
					<div class="col-sm-10">
						<form:input  readonly="true" path="binId" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">货区名称：</label>
					<div class="col-sm-10">
						<form:input  path="binDesc" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input path="note" htmlEscape="false"    class="form-control "/>
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
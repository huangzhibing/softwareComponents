<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>企业类型表单管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

        $(document).ready(function() {
            $("#inputForm").validate({
                submitHandler: function(form){
                    var index=jp.loading();
                    var isAdd='${isAdd}';
                    $.ajax({
                        url:'${ctx}/common/chkCode',
                        data:{
                            tableName:"mdm_accounttype",
                            fieldName:"acc_type_code",
                            fieldValue:$('#accTypeCode').val(),
                        },
                        success:function(res){
                            if(isAdd===''){
                                form.submit();
                            }else{
                                if(res==='true'){
                                    form.submit();
                                }else{
                                    jp.warning("企业类型编码已存在!");
                                    return false;
                                }
                            }
                        }
                    })
				},
                    submitHandler: function(form){
                        var index=jp.loading();
                        var isAdd='${isAdd}';
                    $.ajax({
                        url:'${ctx}/common/chkCode',
                        data:{
                            tableName:"mdm_accounttype",
                            fieldName:"acc_type_name",
                            fieldValue:$('#accTypeName').val(),
                        },
                        success:function(res){
                            if(isAdd===''){
                                form.submit();
                            }else{
                                if(res==='true'){
                                    form.submit();
                                }else{
                                    jp.warning("企业类型名称已存在!");
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
				<a class="panelButton" href="${ctx}/accounttype/accountType"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="accountType" action="${ctx}/accounttype/accountType/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>企业类型编码：</label>
					<div class="col-sm-10">
						<form:input path="accTypeCode" htmlEscape="false"  maxlength="2" minlength="2" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>企业类型名称：</label>
					<div class="col-sm-10">
						<form:input path="accTypeName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('accounttype:accountType:edit') || isAdd}">
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
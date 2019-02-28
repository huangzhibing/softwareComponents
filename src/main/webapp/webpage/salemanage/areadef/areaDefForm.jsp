<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>销售地区定义管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			if(!'${isAdd}'){
				$('#areaCode').attr("readonly","true");
			}
			$("#inputForm").validate({
				submitHandler: function(form){
					jp.loading();
					var isAdd='${isAdd}';
					$.ajax({
						url:'${ctx}/common/chkCode',
						data:{
							tableName:"sal_areadef",
							fieldName:"area_code",
							fieldValue:$('#areaCode').val(),
						},
						success:function(res){
							if(isAdd===''){
								form.submit();
							}else{
								if(res==='true'){
									form.submit();
								}else{
									jp.warning("地区编码已存在!");
									return false;
								}
							}
						}
					})
				},
                submitHandler: function(form){
                    jp.loading();
                    var isAdd='${isAdd}';
                    $.ajax({
                        url:'${ctx}/common/chkCode',
                        data:{
                            tableName:"sal_areadef",
                            fieldName:"area_name",
                            fieldValue:$('#areaName').val(),
                        },
                        success:function(res){
                            if(isAdd===''){
                                form.submit();
                            }else{
                                if(res==='true'){
                                    form.submit();
                                }else{
                                    jp.warning("地区名称已存在!");
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
				<a class="panelButton" href="${ctx}/areadef/areaDef"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="areaDef" action="${ctx}/areadef/areaDef/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>地区编码：</label>
					<div class="col-sm-10">
						<form:input path="areaCode" htmlEscape="false" maxlength="2"  minlength="2"   class="form-control required number"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>地区名称：</label>
					<div class="col-sm-10">
						<form:input path="areaName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input path="notes" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('areadef:areaDef:edit') || isAdd}">
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
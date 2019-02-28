<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>保存成功管理</title>
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
                            tableName:"pur_areacode",
                            fieldName:"areacode",
                            fieldValue:$('#areacode').val(),
                        },
                        success:function(res){
                        	if(isAdd){
                        		if (res==='true'){
                        			form.submit();
                        		}else {
                        			jp.warning("地区编码已存在!");
                                    return false;
                        		}
                        	}else{
                        		form.submit();
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
				<a class="panelButton" href="${ctx}/areacode/areaCode"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="areaCode" action="${ctx}/areacode/areaCode/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/>
		<sys:message content="${message}"/>
		    <c:if test="${isAdd!='true'}">		
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>地区编码：</label>
					<div class="col-sm-10">
						<form:input path="areacode" htmlEscape="false"  readOnly="true"  class="form-control required"/>
					</div>
				</div>
			</c:if>
			<c:if test="${isAdd=='true'}">		
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>地区编码：</label>
					<div class="col-sm-10">
						<form:input path="areacode" htmlEscape="false"   class="form-control required"/>
					</div>
				</div>
			</c:if>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>地区名称：</label>
					<div class="col-sm-10">
						<form:input path="areaname" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input path="notes" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('areacode:areaCode:edit') || isAdd}">
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>产品维护管理</title>
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
				<a class="panelButton" href="${ctx}/product/product"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="product" action="${ctx}/product/product/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>产品编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify url="${ctx}/item/item/data2" id="code"
                                            name="item.code" value="${product.item.code}"
                                            labelName="item.code"
                                            labelValue="${product.item.code}"
                                            title="选择产品编码" cssClass="form-control required"
                                            targetId="itemNameRu" targetField="name"
                                            fieldLabels="物料编码|物料名称" fieldKeys="code|name" searchLabels="物料编码|物料名称"
                                            searchKeys="code|name"
                                            extraField="itemPdn:drawNO;itemSpec:specModel;itemMeasure:unit"
                        ></sys:gridselect-modify>

                    </div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>产品名称：</label>
					<div class="col-sm-10">
						<form:input readonly="true" path="itemNameRu" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">产品图号：</label>
					<div class="col-sm-10">
						<form:input readonly="true" path="itemPdn" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">产品规格：</label>
					<div class="col-sm-10">
						<form:input readonly="true" path="itemSpec" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单位：</label>
					<div class="col-sm-10">
						<form:input readonly="true" path="itemMeasure" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>装配提前期：</label>
					<div class="col-sm-10">
						<form:input path="leadTimeAssemble" htmlEscape="false"  value="0"   class="form-control required digits"/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('product:product:edit') || isAdd}">
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>产品结构维护管理</title>
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
						<a class="panelButton" href="${ctx}/productbomtree/productBom"><i class="ti-angle-left"></i> 返回</a>
					</h3>
				</div>
				<div class="panel-body">
					<form:form id="inputForm" modelAttribute="productBom" action="${ctx}/productbomtree/productBom/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<sys:message content="${message}"/>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>产品编码：</label>
							<div class="col-sm-10">
								<sys:gridselect-modify url="${ctx}/product/product/data" id="product" name="product.id" value="${productBom.product.id}" labelName="product.id" labelValue="${productBom.product.id}"
													   title="选择产品编码" cssClass="form-control required" targetId="productItemName" targetField="itemNameRu" fieldLabels="产品编码|产品名称" fieldKeys="id|itemNameRu" searchLabels="产品编码|产品名称" searchKeys="id|itemNameRu" ></sys:gridselect-modify>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>产品名称：</label>
							<div class="col-sm-10">
								<form:input path="productItemName" htmlEscape="false" maxlength="50"  readonly="true"   class="form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>零件编码：</label>
							<div class="col-sm-10">
								<sys:gridselect-modify url="${ctx}/item/item/data"
													   id="item" name="item.id"
													   value="${productBom.item.id}"
													   labelName="item.id" labelValue="${productBom.item.id}"
													   title="选择零件编码" cssClass="form-control required"
													   targetId="itemName" targetField="name"
													   fieldLabels="物料编码|物料名称|零件图号|零件规格|零件单位"
													   fieldKeys="id|name|drawNO|specModel|unit"
													   searchLabels="物料编码|物料名称"
													   searchKeys="id|name" extraField="itemPdn:drawNO;itemSpec:specModel;itemMeasure:unit">
								</sys:gridselect-modify>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>零件名称：</label>
							<div class="col-sm-10">
								<form:input path="itemName" htmlEscape="false" maxlength="50"  readonly="true" class="form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">零件图号：</label>
							<div class="col-sm-10">
								<form:input path="itemPdn" htmlEscape="false" maxlength="50"    class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">零件规格：</label>
							<div class="col-sm-10">
								<form:input path="itemSpec" htmlEscape="false" maxlength="50"    class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>零件单位：</label>
							<div class="col-sm-10">
								<form:input path="itemMeasure" htmlEscape="false" maxlength="50"    class="form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>装配提前期：</label>
							<div class="col-sm-10">
								<form:input path="leadTimeAssemble" htmlEscape="false" maxlength="10"    class="form-control required digits"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">在父项中的数量：</label>
							<div class="col-sm-10">
								<form:input path="numInFather" htmlEscape="false" maxlength="18"    class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>父零件编码：</label>
							<div class="col-sm-10">
								<sys:gridselect-modify url="${ctx}/item/item/data"
													   id="fatherItem" name="fatherItem.id"
													   value="${productBom.fatherItem.id}"
													   labelName="fatherItem.id" labelValue="${productBom.fatherItem.id}"
													   title="选择父零件编码" cssClass="form-control required"
													   targetId="fatherItemName" targetField="name"
													   fieldLabels="物料编码|物料名称" fieldKeys="id|name"
													   searchLabels="物料编码|物料名称" searchKeys="id|name"
													   extraField="fatherItemPdn:drawNO;fatherItemSpec:specModel">
								</sys:gridselect-modify>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>父零件名称：</label>
							<div class="col-sm-10">
								<form:input path="fatherItemName" htmlEscape="false" maxlength="50"    class="form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">父零件图号：</label>
							<div class="col-sm-10">
								<form:input path="fatherItemPdn" htmlEscape="false" maxlength="50"    class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">父零件规格：</label>
							<div class="col-sm-10">
								<form:input path="fatherItemSpec" htmlEscape="false" maxlength="50"    class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">产品类型：</label>
							<div class="col-sm-10">
								<sys:treeselect id="productType" name="productType.id" value="${productBom.productType.id}" labelName="productType.name" labelValue="${productBom.productType.name}"
												title="产品类型" url="/productbomtree/productBomFatherClazz/treeData" extId="${productBom.id}" cssClass="form-control " allowClear="true"/>
							</div>
						</div>
						<c:if test="${fns:hasPermission('productbomtree:productBom:edit') || isAdd}">
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
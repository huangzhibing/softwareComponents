<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>产品结构维护管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="productBomList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">产品结构维护列表</h3>
	</div>
		<div class="panel-body">

					<sys:message content="${message}"/>

					<!-- 搜索 -->
					<div class="accordion-group">
						<div id="collapseTwo" class="accordion-body collapse">
							<div class="accordion-inner">
								<form:form id="searchForm" modelAttribute="productBom"
										   class="form form-horizontal well clearfix">
									<div class="col-xs-12 col-sm-6 col-md-4">
										<label class="label-item single-overflow pull-left" title="产品编码：">产品编码：</label>
										<%--<sys:gridselect-modify targetId="productItemName" targetField="itemNameRu" url="${ctx}/product/product/data" id="product" name="product.id"
														value="${productBom.product.id}" labelName="product.itemCode"
														labelValue="${productBom.product.item.code}"
														title="选择产品编码" cssClass="form-control required"
														fieldLabels="产品编码|产品名称" fieldKeys="item.code|itemNameRu"
														searchLabels="产品编码|产品名称"
														searchKeys="item.code|itemNameRu"></sys:gridselect-modify>--%>
										<sys:gridselect-product targetField="item.name" targetId="productItemName" url="${ctx}/product/product/data" id="product" name="product.item.code" value="${productBom.product.item.code}" labelName="item.code" labelValue="${productBom.product.item.code}"
																title="选择产品编码" cssClass="form-control" fieldLabels="产品编码|产品名称" fieldKeys="item.code|itemNameRu" searchLabels="产品编码|产品名称" searchKeys="itemCode|itemNameRu" ></sys:gridselect-product>

									</div>
									<div class="col-xs-12 col-sm-6 col-md-4">
										<label class="label-item single-overflow pull-left" title="产品名称：">产品名称：</label>
										<form:input path="productItemName" htmlEscape="false" maxlength="64"
													class=" form-control"/>
									</div>
									<div class="col-xs-12 col-sm-6 col-md-4">
										<label class="label-item single-overflow pull-left" title="零件编码：">零件编码：</label>
										<sys:gridselect-modify targetId="itemName" targetField="name" url="${ctx}/item/item/data" id="item" name="item.id"
														value="${productBom.item.id}" labelName="item.code"
														labelValue="${productBom.item.name}"
														title="选择零件编码" cssClass="form-control required"
														fieldLabels="物料编码|物料名称" fieldKeys="code|name"
														searchLabels="物料编码|物料名称" searchKeys="code|name"></sys:gridselect-modify>
									</div>
									<div class="col-xs-12 col-sm-6 col-md-4">
										<label class="label-item single-overflow pull-left" title="零件名称：">零件名称：</label>
										<form:input path="itemName" htmlEscape="false" maxlength="64"
													class=" form-control"/>
									</div>
									<div class="col-xs-12 col-sm-6 col-md-4">
										<label class="label-item single-overflow pull-left" title="零件图号：">零件图号：</label>
										<form:input path="itemPdn" htmlEscape="false" maxlength="64"
													class=" form-control"/>
									</div>
									<div class="col-xs-12 col-sm-6 col-md-4">
										<label class="label-item single-overflow pull-left" title="零件规格：">零件规格：</label>
										<form:input path="itemSpec" htmlEscape="false" maxlength="64"
													class=" form-control"/>
									</div>
									<div class="col-xs-12 col-sm-6 col-md-4">
										<label class="label-item single-overflow pull-left"
											   title="在父项中的数量：">在父项中的数量：</label>
										<form:input path="numInFather" htmlEscape="false" maxlength="64"
													class=" form-control"/>
									</div>
									<div class="col-xs-12 col-sm-6 col-md-4">
										<label class="label-item single-overflow pull-left"
											   title="父零件编码：">父零件编码：</label>
										<sys:gridselect-modify targetField="name" targetId="fatherItemName" url="${ctx}/item/item/data" id="fatherItem" name="fatherItem.id"
														value="${productBom.fatherItem.id}" labelName="fatherItem.code"
														labelValue="${productBom.fatherItem.name}"
														title="选择父零件编码" cssClass="form-control required"
														fieldLabels="物料编码|物料名称" fieldKeys="code|name"
														searchLabels="物料编码|物料名称" searchKeys="code|name"></sys:gridselect-modify>
									</div>
									<div class="col-xs-12 col-sm-6 col-md-4">
										<label class="label-item single-overflow pull-left"
											   title="父零件名称：">父零件名称：</label>
										<form:input path="fatherItemName" htmlEscape="false" maxlength="64"
													class=" form-control"/>
									</div>
									<div class="col-xs-12 col-sm-6 col-md-4">
										<label class="label-item single-overflow pull-left"
											   title="父零件图号：">父零件图号：</label>
										<form:input path="fatherItemPdn" htmlEscape="false" maxlength="64"
													class=" form-control"/>
									</div>
									<div class="col-xs-12 col-sm-6 col-md-4">
										<label class="label-item single-overflow pull-left"
											   title="父零件规格：">父零件规格：</label>
										<form:input path="fatherItemSpec" htmlEscape="false" maxlength="64"
													class=" form-control"/>
									</div>
									<div class="col-xs-12 col-sm-6 col-md-4">
										<div style="margin-top:26px">
											<a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
													class="fa fa-search"></i> 查询</a>
											<a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
													class="fa fa-refresh"></i> 重置</a>
										</div>
									</div>
								</form:form>
							</div>
						</div>
					</div>

					<!-- 工具栏 -->
					<div id="toolbar">
						<shiro:hasPermission name="mdmproductbom:productBom:add">
							<a id="add" class="btn btn-primary" href="${ctx}/mdmproductbom/productBom/form"
							   title="产品结构维护"><i class="glyphicon glyphicon-plus"></i> 新建</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="mdmproductbom:productBom:edit">
							<button id="edit" class="btn btn-success" disabled onclick="edit()">
								<i class="glyphicon glyphicon-edit"></i> 修改
							</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="mdmproductbom:productBom:del">
							<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
								<i class="glyphicon glyphicon-remove"></i> 删除
							</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="mdmproductbom:productBom:import">
							<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
							<div id="importBox" class="hide">
								<form id="importForm" action="${ctx}/mdmproductbom/productBom/import" method="post"
									  enctype="multipart/form-data"
									  style="padding-left:20px;text-align:center;"><br/>
									<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　


								</form>
							</div>
						</shiro:hasPermission>
						<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2"
						   href="#collapseTwo">
							<i class="fa fa-search"></i> 检索
						</a>
					</div>

					<!-- 表格 -->
					<table id="productBomTable" data-toolbar="#toolbar"></table>

					<!-- context menu -->
					<ul id="context-menu" class="dropdown-menu">
						<shiro:hasPermission name="mdmproductbom:productBom:edit">
							<li data-item="edit"><a>编辑</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="mdmproductbom:productBom:del">
							<li data-item="delete"><a>删除</a></li>
						</shiro:hasPermission>
						<li data-item="action1"><a>取消</a></li>
					</ul>

		</div>
	</div>
</body>
</html>
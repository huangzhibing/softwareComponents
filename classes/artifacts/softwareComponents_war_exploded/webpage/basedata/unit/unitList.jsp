<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>计量单位定义管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="unitList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">计量单位定义列表</h3>
		</div>
		<div class="panel-body">
			<sys:message content="${message}"/>

			<!-- 搜索 -->
			<div class="accordion-group">
				<div id="collapseTwo" class="accordion-body collapse">
					<div class="accordion-inner">
						<form:form id="searchForm" modelAttribute="unit" class="form form-horizontal well clearfix">
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="计量单位类型：">计量单位类型：</label>
								<sys:gridselect url="${ctx}/unittype/unitType/data" id="unittype" name="unittype.id" value="${unit.unittype.id}" labelName="unittype.unitTypeName" labelValue="${unit.unittype.unitTypeName}"
												title="选择计量单位类型" cssClass="form-control required" fieldLabels="计量单位类别编码|计量单位类别名称" fieldKeys="unitTypeCode|unitTypeName" searchLabels="计量单位类别编码|计量单位类别名称" searchKeys="unitTypeCode|unitTypeName"></sys:gridselect>
						    </div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="计量单位编码：">计量单位代码：</label>
								<form:input path="unitCode" htmlEscape="false" maxlength="20"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="计量单位名称：">计量单位名称：</label>
								<form:input path="unitName" htmlEscape="false" maxlength="30"  class=" form-control"/>
							</div>

							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="是否标准：">是否标准：</label>
								<form:select path="isStand" class="form-control required">
									<option value="" selected></option>
									<form:option value="是" label="是"/>
									<form:option value="否" label="否"/>
									<form:options items="${fns:getDictList('isStand')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="标准计量单位编码：">标准计量单位编码：</label>
									<form:select path="standUnitCode" htmlEscape="false"  class="form-control ">
										<option value="" selected></option>
										<c:forEach items="${standUnitType}" var="var">
											<form:option value="${var.unitCode}" sid="${var.unitName}">${var.unitCode}</form:option>
										</c:forEach>
									</form:select>
										<%--<form:input path="standUnitCode" htmlEscape="false"    class="form-control "/>--%>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="标准计量单位名称：">标准计量单位名称：</label>
								<form:input path="standUnitName" htmlEscape="false" maxlength="50"  id="standunitname"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<div style="margin-top:26px">
									<a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
									<a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>

			<!-- 工具栏 -->
			<div id="toolbar">
				<shiro:hasPermission name="unit:unit:add">
					<a id="add" class="btn btn-primary" href="${ctx}/unit/unit/form" title="计量单位定义"><i class="glyphicon glyphicon-plus"></i> 新建</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="unit:unit:edit">
					<button id="edit" class="btn btn-success" disabled onclick="edit()">
						<i class="glyphicon glyphicon-edit"></i> 修改
					</button>
				</shiro:hasPermission>
				<shiro:hasPermission name="unit:unit:del">
					<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
						<i class="glyphicon glyphicon-remove"></i> 删除
					</button>
				</shiro:hasPermission>
				<shiro:hasPermission name="unit:unit:import">
					<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
					<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/unit/unit/import" method="post" enctype="multipart/form-data"
							  style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　


						</form>
					</div>
				</shiro:hasPermission>
				<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
			</div>

			<!-- 表格 -->
			<table id="unitTable"   data-toolbar="#toolbar"></table>

			<!-- context menu -->
			<ul id="context-menu" class="dropdown-menu">
				<shiro:hasPermission name="unit:unit:edit">
					<li data-item="edit"><a>编辑</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="unit:unit:del">
					<li data-item="delete"><a>删除</a></li>
				</shiro:hasPermission>
				<li data-item="action1"><a>取消</a></li>
			</ul>
		</div>
	</div>
</div>
</body>
</html>
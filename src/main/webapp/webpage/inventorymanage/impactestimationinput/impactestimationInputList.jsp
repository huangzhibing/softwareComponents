<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>冲估单据管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="impactestimationInputList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">冲估单据列表</h3>
		</div>
		<div class="panel-body">
			<sys:message content="${message}"/>

			<!-- 搜索 -->
			<div class="accordion-group">
				<div id="collapseTwo" class="accordion-body collapse">
					<div class="accordion-inner">
						<form:form id="searchForm" modelAttribute="billMain" class="form form-horizontal well clearfix">
							<div class="col-xs-12 col-sm-6 col-md-3">
								<label class="label-item single-overflow pull-left" title="单据号：">单据号：</label>
								<form:input path="billNum" htmlEscape="false" maxlength="20" class=" form-control"/>
							</div>

							<div class="col-xs-12 col-sm-6 col-md-3">
								<label class="label-item single-overflow pull-left" title="物料代码：">物料代码：</label>
								<sys:gridselect-modify url="${ctx}/item/item/data2" id="item" name="item.id" value="${billDetail.item.id}" labelName="item.code" labelValue="${billDetail.item.code}"
													   title="选择物料代码" cssClass="form-control  required" fieldLabels="物料代码|物料名称|规格型号" fieldKeys="code|name|specModel" searchLabels="物料代码|物料名称|规格型号" searchKeys="code|name|specModel"
													   targetId="itemName" targetField="name" extraField="itemSpec:specModel">
								</sys:gridselect-modify>
								<%--<input type='text' name="item.id" class=" form-control"/>--%>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-3">
								<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
								<input type='text' id="itemName" name="itemName"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-3">
								<label class="label-item single-overflow pull-left" title="规格型号：">规格型号：</label>
								<input  type='text' id="itemSpec" name="itemSpec"  class=" form-control"/>
							</div>
							<div class="row">
								<div class="col-xs-12 col-sm-6 col-md-6">
									<div class="form-group">
										<label class="label-item single-overflow pull-left" title="入库日期：">&nbsp;入库日期：</label>
										<div class="col-xs-12">
											<div class="col-xs-12 col-sm-6 col-md-5">
												<div class='input-group date' id='beginBillDate' style="left: 0px;" >
													<input type='text'  name="beginBillDate" class="form-control"  />
													<span class="input-group-addon">
													   <span class="glyphicon glyphicon-calendar"></span>
												   </span>
												</div>
											</div>
											<div class="col-xs-12 col-sm-6 col-sm-1">
												~
											</div>
											<div class="col-xs-12 col-sm-6 col-md-5">
												<div class='input-group date' id='endBillDate' style="left: -10px;" >
													<input type='text'  name="endBillDate" class="form-control" />
													<span class="input-group-addon">
													   <span class="glyphicon glyphicon-calendar"></span>
												   </span>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="col-xs-12 col-sm-6 col-md-3">
									<label class="label-item single-overflow pull-left" title="估价入库单号：">估价入库单号：</label>
									<sys:gridselect url="${ctx}/outsourcinginput/outsourcingInput?type=post/data" id="billMain" name="billMain.billNum" value="${billMain.billNum}"
													labelName="billMain.billNum" labelValue="${billMain.billNum}" title="选择估价入库单号" cssClass="form-control required" fieldLabels="估价入库单号" fieldKeys="billNum"
													searchLabels="估价入库单号" searchKeys="billNum"></sys:gridselect>
								</div>
								<div class="col-xs-12 col-sm-6 col-md-3">
									<label class="label-item single-overflow pull-left" title="库房号：">仓库编码：</label>
									<sys:gridselect-modify url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${billMain.ware.id}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
														   title="选择仓库编码" cssClass="form-control required" fieldLabels="仓库编码|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库编码|仓库名称" searchKeys="wareID|wareName"
														   targetId="wareName" targetField="wareName" ></sys:gridselect-modify>
								</div>
								<div class="col-xs-12 col-sm-6 col-md-3">
									<label class="label-item single-overflow pull-left" title="库房名称：">仓库名称：</label>
									<form:input path="wareName" htmlEscape="false" maxlength="100" readonly="true" class=" form-control"/>
								</div>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-3">
								<label class="label-item single-overflow pull-left" title="经办人：">经办人编码：</label>
								<sys:userselect id="billPerson" name="billPerson.id" value="${billMain.billPerson.id}" labelName="billPerson.no" labelValue="${billMain.billPerson.loginName}"
												cssClass="form-control "/>
							</div>
							<%--<div class="col-xs-12 col-sm-6 col-md-3">--%>
								<%--<label class="label-item single-overflow pull-left" title="经办人名称：">经办人名称：</label>--%>
								<%--<form:input path="billPersonName" htmlEscape="false" maxlength="100" readonly="true" class=" form-control"/>--%>
							<%--</div>--%>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="库管员代码：">库管员代码：</label>
								<sys:gridselect-modify url="${ctx}/employee/employee/data" id="wareEmp" name="wareEmp.id" value="${billMain.wareEmp.id}" labelName="wareEmp.empID" labelValue="${billMain.wareEmp.empID}"
													   title="选择库管员代码" cssClass="form-control required" fieldLabels="库管员代码|库管员名称" fieldKeys="empID|empName" searchLabels="库管员代码|库管员名称" searchKeys="empID|empName"
													   targetId="wareEmpname" targetField="empName"></sys:gridselect-modify>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="库管员名称：">库管员名称：</label>
								<form:input path="wareEmpname" htmlEscape="false" maxlength="10"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-3">
								<label class="label-item single-overflow pull-left" title="部门编码：">供应商编码：</label>
								<sys:treeselect-modify targetId="deptname" targetField="code" id="dept" name="dept.id" value="${billMain.dept.id}" labelName="dept.code" labelValue="${billMain.dept.code}"
													   title="部门" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-3">
								<label class="label-item single-overflow pull-left" title="部门名称：">供应商名称：</label>
								<form:input path="deptName" htmlEscape="false" maxlength="30"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-3">
								<label class="label-item single-overflow pull-left" title="核算期间：">核算期间：</label>
								<input type='text' name="period"  maxlength="10"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-3">
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
			<c:if test="${type eq 'Query'}">
				<div id="toolbar">
					<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2"
					   href="#collapseTwo">
						<i class="fa fa-search"></i> 检索
					</a>
				</div>
			</c:if>
			<c:if test="${type eq 'Input'}">
				<div id="toolbar">
					<shiro:hasPermission name="billmain:billMain:add">
						<a id="add" class="btn btn-primary" href="${ctx}/impactestimationinput/impactestimationInput/form?type=Input" title="单据"><i
								class="glyphicon glyphicon-plus"></i> 新建</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="billmain:billMain:del">
						<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
							<i class="glyphicon glyphicon-remove"></i> 删除
						</button>
					</shiro:hasPermission>
					<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2"
					   href="#collapseTwo">
						<i class="fa fa-search"></i> 检索
					</a>
				</div>
			</c:if>
			<c:if test="${type eq 'Post'}">
				<div id="toolbar">
					<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2"
					   href="#collapseTwo">
						<i class="fa fa-search"></i> 检索
					</a>
				</div>
			</c:if>

			<!-- 表格 -->
			<table id="billMainTable"   data-toolbar="#toolbar"></table>

			<!-- context menu -->
			<ul id="context-menu" class="dropdown-menu">
				<shiro:hasPermission name="billmain:billMain:edit">
					<li data-item="edit"><a>编辑</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="billmain:billMain:del">
					<li data-item="delete"><a>删除</a></li>
				</shiro:hasPermission>
				<li data-item="action1"><a>取消</a></li>
			</ul>
		</div>
	</div>
</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>人工工资单据管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="personWageList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">人工工资单据列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="personWage" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="核算期：">核算期：</label>
				<form:input path="periodId" htmlEscape="false" maxlength="6"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="产品代码：">工序代码：</label>
				<sys:gridselect-item url="${ctx}/workprocedure/workProcedure/data" id="workCode" name="workCode.id" value="${personWage.workCode.id}" labelName="workCode.workProcedureId" labelValue="${personWage.workCode.workProcedureId}"
									 title="选择工序编码" cssClass="form-control" targetId="workProcedureName" targetField="workProcedureName" fieldLabels="工序编码|工序名称" fieldKeys="workProcedureId|workProcedureName" searchLabels="工序编码|工序名称" searchKeys="workProcedureId|workProcedureName" ></sys:gridselect-item>
				</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="产品名称：">工序名称：</label>
				<input id="workProcedureName" name="workProcedureName" htmlEscape="false"    class="form-control "/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="人员代码：">人员代码：</label>
				<sys:userselect-item  targetId="personName" id="personCode" name="personCode.id" value="${personWage.personCode.id}" labelName="personCode.no" labelValue="${personWage.personCode.no}"
										  cssClass="form-control "/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="人员名称：">人员名称：</label>
				<input id="personName" name="personName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="产品代码：">产品代码：</label>
				<sys:gridselect-item url="${ctx}/product/product/data" id="itemCode" name="itemCode.id" value="${personWage.itemCode.id}" labelName="itemCode.item.code" labelValue="${personWage.itemCode.item.code}"
									 title="选择产品代码" cssClass="form-control" fieldLabels="产品代码|产品名称" fieldKeys="item.code|itemNameRu" searchLabels="产品代码|产品名称" searchKeys="item.code|itemNameRu"
									 extraField="itemCodeNames:item.code;productName:itemNameRu"></sys:gridselect-item>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="产品名称：">产品名称：</label>
				<input id="productName" name="productName" htmlEscape="false" maxlength="64"  class=" form-control"/>
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
		<c:if test="${empty flag}">
			<shiro:hasPermission name="personwage:personWage:add">
				<a id="add" class="btn btn-primary" href="${ctx}/personwage/personWage/form" title="人工工资单据录入"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="personwage:personWage:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="personwage:personWage:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="personwage:personWage:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/personwage/personWage/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission>
		</c:if>
		<c:if test="${flag == 'jihe'}">
			<button id="edit" class="btn btn-success" disabled onclick="edit('jihe')">
				<i class="glyphicon glyphicon-edit"></i> 稽核
			</button>
		</c:if>
		<c:if test="${flag == 'cexiao'}">
			<button id="edit" class="btn btn-success" disabled onclick="edit('cexiao')">
				<i class="glyphicon glyphicon-edit"></i> 撤销
			</button>
		</c:if>
		<c:if test="${flag == 'zhidan'}">
			<button id="edit" class="btn btn-success" disabled onclick="edit('zhidan')">
				<i class="glyphicon glyphicon-edit"></i> 制单
			</button>
		</c:if>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="personWageTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="personwage:personWage:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="personwage:personWage:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工作中心定义管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="workCenterList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">工作中心定义列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="workCenter" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工作中心代码：">工作中心代码：</label>
				<form:input path="centerCode" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工作中心名称：">工作中心名称：</label>
				<form:input path="centerName" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="部门编码：">部门编码：</label>
				<sys:treeselect-modify targetId="deptName" targetField="code" id="deptCode" name="deptCode.id" value="${workCenter.deptCode.id}" labelName="deptCode.name" labelValue="${workCenter.deptCode.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
			</div>
			
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="部门名称">部门名称：</label>
					
						<form:input readOnly="readOnly" id="deptName" path="deptName" maxlength="50" htmlEscape="false"    class="form-control "/>
					
				</div> 
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="核算类别：">核算类别：</label>
				<form:select path="centerType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('centerType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="关键工作中心：">关键工作中心：</label>
				<form:select path="centerKey"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('centerKey')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工作中心设备类别编码：">工作中心设备类别编码：</label>
				<sys:gridselect-modify  url="${ctx}/machinetype/machineType/data" id="machineTypeCode" name="machineTypeCode.id" value="${workCenter.machineTypeCode.id}" labelName="machineTypeCode.machineTypeCode" labelValue="${workCenter.machineTypeCode.machineTypeCode}"
					title="选择工作中心设备类别编码" cssClass="form-control required" fieldLabels="工作中心设备类别" fieldKeys="machineTypeName" searchLabels="工作中心设备类别" searchKeys="machineTypeName" targetId="machineId" targetField="machineTypeName"></sys:gridselect-modify>
			</div>
		
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="工作中心设备类别名称">工作中心设备类别名称：</label>
					
						<form:input id="machineId" path="machineTypeName" htmlEscape="false" maxlength="50"  class=" form-control" readOnly="readOnly"/>
					
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
			<shiro:hasPermission name="workcenter:workCenter:add">
				<a id="add" class="btn btn-primary" href="${ctx}/workcenter/workCenter/form" title="工作中心定义"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="workcenter:workCenter:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="workcenter:workCenter:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="workcenter:workCenter:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/workcenter/workCenter/import" method="post" enctype="multipart/form-data"
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
	<table id="workCenterTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="workcenter:workCenter:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="workcenter:workCenter:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
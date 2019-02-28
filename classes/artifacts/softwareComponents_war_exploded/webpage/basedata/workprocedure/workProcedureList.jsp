<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工序定义管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="workProcedureList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">工序定义列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="workProcedure" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="工序编码：">工序编码：</label>
				<form:input path="workProcedureId" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="工序名称：">工序名称：</label>
				<form:input path="workProcedureName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="所属部门编码：">部门编码：</label>
				 <sys:treeselect-modify targetId="deptName" targetField="code" id="deptCode" name="deptCode.code" value="${workProcedure.deptCode.code}" labelName="deptCode.name" labelValue="${workProcedure.deptCode.name}"
										title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
			 </div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="所属部门名称：">部门名称：</label>
				<form:input path="deptName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="负责人编码：">负责人编码：</label>
				 <sys:userselect-modify targetId="userNameRu" id="user" name="user.no" value="${workProcedure.user.no}" labelName="user.name" labelValue="${workProcedure.user.no}"
										cssClass="form-control required"/>
			 </div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="负责人名称：">负责人名称：</label>
				 <form:input path="userNameRu" htmlEscape="false" maxlength="50"  readonly="true"  class="form-control "/>
			 </div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				 <label class="label-item single-overflow pull-left" title="工作中心设备类别编码：">工作中心设备类别编码：</label>
				 <sys:gridselect-modify url="${ctx}/machinetype/machineType/data" id="machineTypeCode" name="machineTypeCode.machineTypeCode" value="${workProcedure.machineTypeCode.machineTypeCode}" labelName="machineTypeCode.machineTypeCode" labelValue="${workProcedure.machineTypeCode.machineTypeCode}"
										title="选择工作中心设备类别编码" targetId="machineId" targetField="machineTypeName" cssClass="form-control required" fieldLabels="工作中心设备类别名称" fieldKeys="machineTypeName" searchLabels="工作中心设备名称" searchKeys="machineTypeName" ></sys:gridselect-modify>
			 </div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="工作中心设备类别名称：">工作中心设备类别名称：</label>
				<form:input path="machineTypeName" id="machineId" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="是否转序：">是否转序：</label>
				<form:select path="isConvey"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('is_convey')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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
			<shiro:hasPermission name="workprocedure:workProcedure:add">
				<a id="add" class="btn btn-primary" href="${ctx}/workprocedure/workProcedure/form" title="工序定义"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="workprocedure:workProcedure:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="workprocedure:workProcedure:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<%--<shiro:hasPermission name="workprocedure:workProcedure:import">--%>
				<%--<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>--%>
				<%--<div id="importBox" class="hide">--%>
						<%--<form id="importForm" action="${ctx}/workprocedure/workProcedure/import" method="post" enctype="multipart/form-data"--%>
							 <%--style="padding-left:20px;text-align:center;" ><br/>--%>
							<%--<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　--%>
							<%----%>
							<%----%>
						<%--</form>--%>
				<%--</div>--%>
			<%--</shiro:hasPermission>--%>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="workProcedureTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="workprocedure:workProcedure:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="workprocedure:workProcedure:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
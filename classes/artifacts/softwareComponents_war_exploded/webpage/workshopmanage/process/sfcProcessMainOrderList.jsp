<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车间作业计划管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="sfcProcessMainOrderList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">车间作业计划下达列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<%--@elvariable id="mpsPlan" type="MpsPlan"--%>
			<form:form id="searchForm" modelAttribute="mpsPlan" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="主生产计划号：">主生产计划号：</label>
				<form:input path="mpsPlanid" htmlEscape="false" maxlength="12"  class=" form-control"/>
			</div>

				<div class="col-xs-12 col-sm-6 col-md-4">
					<div class="input-group" style="width: 100%">
						<label class="label-item single-overflow pull-left" title="产品名称：">产品名称：</label>
						<form:input path="prodName" htmlEscape="false" maxlength="64"  class=" form-control"/>
						<span class="input-group-btn">
						 <button type="button"  id="prodButton" class="btn btn-primary" style="top: 11px;"><i class="fa fa-search"></i></button>
						 <button type="button" id="prodDelButton" class="close" data-dismiss="alert" style="position: absolute; top: 30px; right: 53px; z-index: 999; display: block;">×</button>
       		 		</span>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6 col-md-4">
					<div class="form-group">
						<label class="label-item single-overflow pull-left col-md-6" title="计划开始日期：">&nbsp;计划开始日期：</label>
						<label class="label-item single-overflow " title="至：">&nbsp;至：</label>
						<div class="col-xs-12 col-sm-6">
							<div class='input-group date' id='beginPlanBDate' >
								<input type='text'  name="beginPlanBDate" class="form-control"  />
								<span class="input-group-addon">
			                       <span class="glyphicon glyphicon-calendar"></span>
			                   </span>
							</div>
						</div>

						<div class="col-xs-12 col-sm-6">
							<div class='input-group date' id='endPlanBDate' >
								<input type='text'  name="endPlanBDate" class="form-control"  />
								<span class="input-group-addon">
			                       <span class="glyphicon glyphicon-calendar"></span>
			                </span>
							</div>
						</div>
					</div>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<div class="form-group">
						<label class="label-item single-overflow pull-left col-md-6" title="计划结束日期：">&nbsp;计划结束日期：</label>
						<label class="label-item single-overflow " title="至：">&nbsp;至：</label>

						<div class="col-xs-12 col-sm-6">
							<div class='input-group date' id='beginPlanEDate' >
								<input type='text'  name="beginPlanEDate" class="form-control"  />
								<span class="input-group-addon">
			                       <span class="glyphicon glyphicon-calendar"></span>
			                   </span>
							</div>
						</div>

						<div class="col-xs-12 col-sm-6">
							<div class='input-group date' id='endPlanEDate' >
								<input type='text'  name="endPlanEDate" class="form-control"  />
								<span class="input-group-addon">
			                       <span class="glyphicon glyphicon-calendar"></span>
			                   </span>
							</div>
						</div>

					</div>
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
			<shiro:hasPermission name="process:sfcProcessMain:edit">
				<button id="edit" class="btn btn-success" disabled onclick="edit()">
					<i class="glyphicon glyphicon-edit"></i> 下达车间作业计划
				</button>
			</shiro:hasPermission>
			<!--<shiro:hasPermission name="process:sfcProcessMain:add">
				<a id="add" class="btn btn-primary" href="${ctx}/process/sfcProcessMain/form" title="车间作业计划"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>

			<shiro:hasPermission name="process:sfcProcessMain:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="process:sfcProcessMain:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/process/sfcProcessMain/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission> -->
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="sfcProcessMainTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="process:sfcProcessMain:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="process:sfcProcessMain:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
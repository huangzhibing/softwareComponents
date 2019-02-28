<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>主生产计划管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="mpsPlanQueryList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">主生产计划列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="mpsPlanQuery" class="form form-horizontal well clearfix">
		<div class="row">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="主生产计划号：">主生产计划号：</label>
				<form:input path="mpsPlanid" htmlEscape="false" maxlength="64"  class=" form-control"/>
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
				<label class="label-item single-overflow pull-left" title="销售订单号：">内部订单号：</label>
				<form:input path="reqID" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
		</div>
	<div class="row">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="计划开始日期：">&nbsp;计划开始日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginPlanBDate' style="left: -10px;" >
					                   <input type='text'  name="beginPlanBDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endPlanBDate' style="left: -10px;" >
					                   <input type='text'  name="endPlanBDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="计划结束日期：">&nbsp;计划结束日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginPlanEDate' style="left: -10px;" >
					                   <input type='text'  name="beginPlanEDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endPlanEDate' style="left: -10px;" >
					                   <input type='text'  name="endPlanEDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="计划状态：">计划状态：</label>
				<form:select path="planStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mps_planstatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
	</div>
	<div class="row">
		 <div class="col-xs-12 col-sm-6 col-md-4">
			<div style="margin-top:26px">
			  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
			 </div>
	    </div>

	</div>
	</form:form>
	</div>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<%--<shiro:hasPermission name="mpsplanquery:mpsPlanQuery:add">
				<a id="add" class="btn btn-primary" href="${ctx}/mpsplanquery/mpsPlanQuery/form" title="主生产计划"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>--%>
			<shiro:hasPermission name="mpsplanquery:mpsPlanQuery:edit">
			    <button id="edit" class="btn btn-warning" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-file"></i> 详情
	        	</button>
			</shiro:hasPermission>
			<%--<shiro:hasPermission name="mpsplanquery:mpsPlanQuery:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="mpsplanquery:mpsPlanQuery:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/mpsplanquery/mpsPlanQuery/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission>--%>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="mpsPlanQueryTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="mpsplanquery:mpsPlanQuery:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="mpsplanquery:mpsPlanQuery:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
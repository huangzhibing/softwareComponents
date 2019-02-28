<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>稽核任务管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="audittaskList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">稽核任务列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="audittask" class="form form-horizontal well clearfix">
			  <div class="row">
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="稽核任务编号：">稽核任务编号：</label>
					<form:input path="audittId" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="稽核任务名称：">稽核任务名称：</label>
					<form:input path="audittName" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="审核组长编号：">审核组长编号：</label>
	                  <sys:userselect-update id="auditGrouper" name="auditGrouper.id" value="${audittask.auditGrouper.id}" labelName="auditGrouper.no" labelValue="${audittask.auditGrouper.no}"
								    cssClass="form-control required"/>
				</div>
			</div>
			 <div class="row">
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="审核组长名称：">审核组长名称：</label>
					<form:input path="auditGlName" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="审核完成时间：">&nbsp;审核完成时间：</label>
				     <div class="col-xs-12">   
						        	  <div class='input-group date' id='beginAuditDate'  >
						                   <input type='text'  name="beginAuditDate" class="form-control"  />
						                   <span class="input-group-addon">
						                       <span class="glyphicon glyphicon-calendar"></span>
						                   </span>
						             </div>	
						</div>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="审核完成时间：">&nbsp;至：</label>
				     <div class="col-xs-12">   
						        	  <div class='input-group date' id='endAuditDate'  >
						                   <input type='text'  name="endAuditDate" class="form-control"  />
						                   <span class="input-group-addon">
						                       <span class="glyphicon glyphicon-calendar"></span>
						                   </span>
						             </div>	
						</div>
				</div>
				
			</div>
			<div class="row">
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="审核目的：">审核目的：</label>
					<form:input path="auditGoal" htmlEscape="false"  class=" form-control"/>
				</div>
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="审核范围：">审核范围：</label>
					<form:input path="auditScope" htmlEscape="false"  class=" form-control"/>
				</div>
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="审核结果综述：">审核结果综述：</label>
					<form:input path="auditResult" htmlEscape="false"  class=" form-control"/>
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
			<shiro:hasPermission name="qmsaudittask:audittask:add">
				<a id="add" class="btn btn-primary" href="${ctx}/qmsaudittask/audittask/form" title="稽核任务"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="qmsaudittask:audittask:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="qmsaudittask:audittask:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="qmsaudittask:audittask:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/qmsaudittask/audittask/import" method="post" enctype="multipart/form-data"
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
	<table id="audittaskTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="qmsaudittask:audittask:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="qmsaudittask:audittask:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
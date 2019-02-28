<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="purReportListAsse.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">检验单列表</h3>
	</div>
	<div class="panel-body">
	<sys:message content="${message}"/>	
	
	<!-- 搜索 -->
	<div class="form-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="purReport" class="form form-horizontal well clearfix">
			
			<div class="row">
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="检验单编号：">检验单编号：</label>
					<form:input path="reportId" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="全检/抽检：">全检/抽检：</label>
					<form:select path="rndFul"  class="form-control m-b">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('checkType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="检验结论：">检验结论：</label>
					<form:select path="checkResult"  class="form-control m-b">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('checkResult')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			
			
			<div class="row">
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<div class="form-group">
						<label class="label-item single-overflow pull-left" title="检验日期范围：">&nbsp;检验日期范围：</label>
						<div class="col-xs-12">
							<div class='input-group date' id='checkDate' >
				                   <input type='text'  name="checkDate" class="form-control"  />
				                   <span class="input-group-addon">
				                       <span class="glyphicon glyphicon-calendar"></span>
				                   </span>
				            </div>	
						</div>
					</div>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<div class="form-group">
						<label class="label-item single-overflow pull-left" title="至：">&nbsp;至：</label>
						<div class="col-xs-12">
							<div class='input-group date' id='justifyDate' >
				                   <input type='text'  name="justifyDate" class="form-control"  />
				                   <span class="input-group-addon">
				                       <span class="glyphicon glyphicon-calendar"></span>
				                   </span>
				            </div>	
						</div>
					</div>
				</div>
				
				
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="是否审核：">是否审核：</label>
					<form:select path="isAudit"  class="form-control m-b">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('isAudit')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="检验类型名称：">检验类型名称：</label>
					<form:select path="checktName"  class="form-control m-b">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('checkNameZ')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			
			
			
			
			
			
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
			<shiro:hasPermission name="purreport:purReport:addAsse">
				<a id="add" class="btn btn-primary" href="${ctx}/purreport/purReport/formAsse" title="检验单"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="purreport:purReport:editAsse">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="purreport:purReport:delAsse">
				<button id="remove" class="btn btn-danger" disabled onclick="mydelete()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			
			<shiro:hasPermission name="purreport:purReport:viewAsse">
				<button id="detail" class="btn btn-primary"  onclick="detail()">
	            	<i class="fa fa-folder-open-o"></i> 详情
	        	</button>
	        </shiro:hasPermission>
			<shiro:hasPermission name="purreport:purReport:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/purreport/purReport/import" method="post" enctype="multipart/form-data"
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
	<table id="purReportTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="purreport:purReport:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="purreport:purReport:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
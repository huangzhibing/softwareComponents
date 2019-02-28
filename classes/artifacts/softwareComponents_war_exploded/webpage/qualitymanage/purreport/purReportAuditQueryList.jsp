<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单审核管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="purReportAuditQueryList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">检验单审核查询列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="purReport" class="form form-horizontal well clearfix">
			<div class="col-xs-12 col-sm-6 col-md-4">
						<div class="col-sm-2" style = "display:none">
						<form:input path="justifyPerson" htmlEscape="false"  class="form-control"/> 
						<form:input path="jpositionName" htmlEscape="false"  class="form-control"/>
						<form:input path="jdeptName" htmlEscape="false"  class="form-control"/> 
						</div>
				<%-- <label class="label-item single-overflow pull-left" title="审核人：">审核人：</label>
				<sys:gridselect url="${ctx}/sys/user/list" id="user" name="user.id" value="${user.id}" 
						labelName="user.name" labelValue="${user.name}"
							 title="选择审核人" cssClass="form-control required" 
							 fieldLabels="审核人" fieldKeys="name" searchLabels="审核人名称" searchKeys="name" >
				</sys:gridselect> --%>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<%-- <label class="label-item single-overflow pull-left" title="审核岗位名称：">审核岗位名称：</label>
					<sys:gridselect url="${ctx}/sys/role/data" id="role" name="role.id" value="${role.id}" 
						labelName="role.name" labelValue="${role.name}"
							 title="选择审核岗位" cssClass="form-control required" 
							 fieldLabels="审核岗位" fieldKeys="name" searchLabels="审核岗位名称" searchKeys="name" >
					</sys:gridselect> --%>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<%-- <label class="label-item single-overflow pull-left" title="审核部门名称：">审核部门名称：</label>
				<sys:treeselect id="office" name="office.id" value="${purReport.office.id}" labelName="office.name" labelValue="${purReport.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/> --%>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验单编号：">检验单编号：</label>
				<form:input path="reportId" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验类型名称：">检验类型名称：</label>
				<form:select path="checktName"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkNameAll')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="全检/抽检：">全检/抽检：</label>
				<form:select path="rndFul"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="检验日期：">&nbsp;检验日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginCheckDate' style="left: -10px;" >
					                   <input type='text'  name="beginCheckDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endCheckDate' style="left: -10px;" >
					                   <input type='text'  name="endCheckDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验结论：">检验结论：</label>
				<form:select path="checkResult"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkRelsult_L')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="单据状态：">单据状态：</label>
				<form:select path="state"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			<%-- <shiro:hasPermission name="purreport:purReport:add">
				<a id="add" class="btn btn-primary" href="${ctx}/purreport/purReport/form" title="检验单"><i class="glyphicon glyphicon-plus"><建</a>
			</shiro:hasPermission> --%>
			<%-- <%-- <shiro:hasPermission name="purreport:purReport:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button> 
			</shiro:hasPermission> --%>
			<%-- <shiro:hasPermission name="purreport:purReport:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission> --%>
			<%-- <shiro:hasPermission name="purreport:purReport:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/purreport/purReport/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission> --%>
			
			<button id="showDetail" class="btn btn-warning" onclick="showDetail()">
	            	 <i class="glyphicon glyphicon-folder-open"></i>  详情
	        </button>
          <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
				<i class="fa fa-search"></i> 检索
			</a>
	    </div>
		
	<!-- 表格 -->
	<table id="purReportTable"   data-toolbar="#toolbar"></table>

    
	</div>
	</div>
	</div>
</body>
</html>
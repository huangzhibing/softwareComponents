<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>保存成功管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="verifyRuleList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">验证规则列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="verifyRule" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="规则编码：">规则编码：</label>
				<form:input path="ruleId" htmlEscape="false" maxlength="10"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="测试项目：">测试项目：</label>
				<form:select path="checkprj"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('CheckPrj')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <!--<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验标准号：">检验标准号：</label>
				<sys:gridselect url="${ctx}/qcnorm/qCNorm/data" id="qcnorm" name="qcnorm.id" value="${verifyRule.qcnorm.id}" labelName="qcnorm.qcnormId" labelValue="${verifyRule.qcnorm.qcnormId}"
					title="选择检验标准号" cssClass="form-control required" fieldLabels="检验标准号|检验标准名称" fieldKeys="qcnormId|qcnormName" searchLabels="检验标准号|检验标准名称" searchKeys="qcnormId|qcnormName" ></sys:gridselect>
			</div> -->
			
					<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="检验标准号：">检验标准号：</label>
						<sys:gridselect-modify url="${ctx}/qmsqcnorm/qCNorm/data" id="qcnorm" name="qcnorm.id" value="${verifyRule.qcnorm.id}" 
						labelName="qcnorm.qcnormId" labelValue="${verifyRule.qcnorm.qcnormId}"
							 title="选择检验标准号" cssClass="form-control required" targetId="qcnormName" targetField="qcnormName" fieldLabels="检验标准号|检验标准名称" fieldKeys="qcnormId|qcnormName" searchLabels="检验标准号|检验标准名称" searchKeys="qcnormId|qcnormName" ></sys:gridselect-modify>
					</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验标准名称：">检验标准名称：</label>
				<form:input path="qcnormName" htmlEscape="false" maxlength="40"  class=" form-control"/>
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
			<shiro:hasPermission name="verifyrule:verifyRule:add">
				<a id="add" class="btn btn-primary" href="${ctx}/verifyrule/verifyRule/form" title="保存成功"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="verifyrule:verifyRule:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="verifyrule:verifyRule:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="verifyrule:verifyRule:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/verifyrule/verifyRule/import" method="post" enctype="multipart/form-data"
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
	<table id="verifyRuleTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="verifyrule:verifyRule:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="verifyrule:verifyRule:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
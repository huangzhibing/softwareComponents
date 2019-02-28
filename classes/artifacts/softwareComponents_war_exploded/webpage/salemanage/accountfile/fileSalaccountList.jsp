<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>客户档案维护管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="fileSalaccountList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">客户档案维护列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="fileSalaccount" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="企业编码：">客户编码：</label>
				<form:input path="accountCode" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="企业名称：">客户名称：</label>
				<form:input path="accountName" htmlEscape="false" maxlength="100"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="地区编码：">所属地区编码：</label>
				 <sys:gridselect-modify url="${ctx}/areadef/areaDef/data" id="area" name="areaDef.id" value="${fileSalaccount.areaDef.id}" labelName="areaDef.areaCode" labelValue="${fileSalaccount.areaDef.areaCode}"
										title="选择地区" cssClass="form-control required" fieldLabels="地区编码|地区名称" fieldKeys="areaCode|areaName" searchLabels="地区编码|地区名称" searchKeys="areaCode|areaName"
										targetId="areaName" targetField="areaName"></sys:gridselect-modify>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="地区名称：">所属地区名称：</label>
				<form:input path="areaName" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="类别编码：">客户类别编码：</label>
				 <sys:gridselect-modify url="${ctx}/accounttype/accountType2/data" id="subType" name="accountType2.id" value="${fileSalaccount.accountType2.id}" labelName="accountType2.accTypeCode" labelValue="${fileSalaccount.accountType2.accTypeCode}"
										title="选择客户类别" cssClass="form-control required" fieldLabels="客户类别编码|客户类别名称" fieldKeys="accTypeCode|accTypeName" searchLabels="客户类别编码|客户类别名称" searchKeys="accTypeCode|accTypeName"
										targetId="subTypeName" targetField="accTypeName"></sys:gridselect-modify>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="类别名称：">客户类别名称：</label>
				<form:input path="subTypeName" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="性质：">企业性质：</label>
					<form:input path="accountProp" htmlEscape="false" maxlength="50"  class=" form-control"/>
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
			<shiro:hasPermission name="accountfile:fileSalaccount:add">
				<a id="add" class="btn btn-primary" href="${ctx}/accountfile/fileSalaccount/form" title="客户档案维护"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="accountfile:fileSalaccount:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="accountfile:fileSalaccount:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="accountfile:fileSalaccount:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/accountfile/fileSalaccount/import" method="post" enctype="multipart/form-data"
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
	<table id="fileSalaccountTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="accountfile:fileSalaccount:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="accountfile:fileSalaccount:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
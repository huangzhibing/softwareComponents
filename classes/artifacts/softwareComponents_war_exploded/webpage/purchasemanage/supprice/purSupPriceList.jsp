<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>供应商价格维护管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="purSupPriceList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">供应商价格维护列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="purSupPrice" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="供应商代码：">供应商代码：</label>
							<sys:gridselect-sup url="${ctx}/account/account/data" id="account" name="account.id" value="${purSupPrice.account.id}" labelName="account.accountCode" labelValue="${purSupPrice.account.accountCode}"
							 title="选择供应商代码" cssClass="form-control required" 
							 targetId="supName|areaCode|areaName|prop|address" targetField="accountName|areaCode|areaName|accountProp|accountAddr" 
							 fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" ></sys:gridselect-sup>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="供应商名称：">供应商名称：</label>
				<form:input path="supName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="所属地区编码：">所属地区编码：</label>
				<form:input path="areaCode" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<br><br><br><br>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="所属地区名称：">所属地区名称：</label>
				<form:input path="areaName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="供应商类别编码：">供应商类别编码：</label>
										<sys:gridselect-sup url="${ctx}/suptype/supType/data" id="supType" name="supType.id" value="${purSupPrice.supType.id}" labelName="supType.suptypeId" labelValue="${purSupPrice.supType.suptypeId}"
							 title="选择供应商类别编码" cssClass="form-control required" 
							 targetId="supTypeName" targetField="suptypeName" 
							 fieldLabels="供应商类别编码|供应商类别名称" fieldKeys="suptypeId|suptypeName" searchLabels="供应商类别编码|供应商类别名称" searchKeys="suptypeId|suptypeName" ></sys:gridselect-sup>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="供应商类别名称：">供应商类别名称：</label>
				<form:input path="supTypeName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<br><br><br><br>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="企业性质：">企业性质：</label>
				<form:input path="prop" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="地址：">地址：</label>
				<form:input path="address" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料编码：">物料编码：</label>
				<sys:gridselect-sup url="${ctx}/item/item/data" id="item" name="item.id" value="${purSupPrice.item.id}" labelName="item.code" labelValue="${purSupPrice.item.code}"
							 title="选择物料编码" cssClass="form-control  " 
							targetId="itemName" targetField="name" 
							fieldLabels="物料编码|物料名称" fieldKeys="code|name" searchLabels="物料编码|物料名称" searchKeys="code|name" ></sys:gridselect-sup>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
				<form:input path="itemName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			
			
			<div class="col-xs-12 col-sm-6 col-md-4">
				<div class="form-group">
					<label class="label-item single-overflow pull-left" title="时间点：">&nbsp;时间点：</label>
					<div class="col-xs-12">
						<div class='input-group date' id='date' >
			                   <input type='text'  name="date" class="form-control"  />
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
			<shiro:hasPermission name="supprice:purSupPrice:add">
				<a id="add" class="btn btn-primary" href="${ctx}/supprice/purSupPrice/form" title="供应商价格维护"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="supprice:purSupPrice:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="supprice:purSupPrice:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="supprice:purSupPrice:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/supprice/purSupPrice/import" method="post" enctype="multipart/form-data"
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
	<table id="purSupPriceTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="supprice:purSupPrice:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="supprice:purSupPrice:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
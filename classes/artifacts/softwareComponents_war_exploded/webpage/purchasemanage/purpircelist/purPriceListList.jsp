<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>价格清单管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="purPriceListList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">价格清单列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="purPriceList" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="供应商编码：">供应商编码：</label>
				<form:hidden path="supId" htmlEscape="false" maxlength="64"  class=" form-control"/>
					<sys:gridselect-pursup url="${ctx}/account/account/data" id="sup" name="sup" value="${purInvCheckPunctual.supId}" 
					labelName="account.accountCode" labelValue="${purInvCheckPunctual.supId}"
						 title="选择供应商编号" cssClass="form-control required" 
						 fieldLabels="供应商编码|供应商名称"  
						 targetId="supName|supAddress"  targetField="accountName|accountAddr" 
						 fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" 
						 searchKeys="accountCode|accountName" ></sys:gridselect-pursup>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="供应商名称：">供应商名称：</label>
				<form:input path="supName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料编码：">物料编码：</label>
				<form:hidden path="itemCode" htmlEscape="false" maxlength="64"  class=" form-control"/>
				<sys:gridselect-pursup url="${ctx}/purinvcheckmain/invCheckMain/dataRequest" id="itemCode" name="itemCode" 
				value="${purInvCheckPunctual.itemCode}" labelName="itemCode" labelValue="${purInvCheckPunctual.itemCode}"
				title="选择物料编号" cssClass="form-control required " 
				targetId="itemName|itemCode|itemCodeNames" targetField="name|code|code"  
				fieldLabels="物料编码|物料名称" isMultiSelected="false" fieldKeys="code|name" 
				searchLabels="物料编码|物料名称" searchKeys="code|name" ></sys:gridselect-pursup>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
				<form:input path="itemName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="创建日期：">&nbsp;创建日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginCreatDateItem' style="left: -10px;" >
					                   <input type='text'  name="beginCreatDateItem" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endCreatDateItem' style="left: -10px;" >
					                   <input type='text'  name="endCreatDateItem" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
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
	<div  id="printContent">
	</div> 
	</div>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<shiro:hasPermission name="purpircelist:purPriceList:add">
				<a id="add" class="btn btn-primary" href="${ctx}/purpircelist/purPriceList/form" title="价格清单"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="purpircelist:purPriceList:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="purpircelist:purPriceList:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="purpircelist:purPriceList:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/purpircelist/purPriceList/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission>
				<a id="showDetail" class="btn btn-success" onclick="print()">
	            	 <i class="glyphicon glyphicon-folder-open"></i>  打印
	            </a>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="purPriceListTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="purpircelist:purPriceList:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="purpircelist:purPriceList:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购计划明细表管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="purPlanDetailStatisList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">采购计划明细表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="purPlanDetailStatis" class="form form-horizontal well clearfix">
			 <%--<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="单据编号：">单据编号：</label>
				<form:input path="billNum" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="单据类型：">单据类型：</label>
				<form:input path="billType" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="序号：">序号：</label>
				<form:input path="serialNum" htmlEscape="false"  class=" form-control"/>
			</div>--%>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="物料编码：">物料编码：</label>
				<sys:gridselect-pursup url="${ctx}/item/item/data" id="itemCode" name="itemCode.id" value="${purPlanDetailStatis.itemCode.id}" labelName="itemCode.code" labelValue="${purPlanDetailStatis.itemCode.code}"
					title="选择物料编码" cssClass="form-control required" fieldLabels="物料编号|物料名称" fieldKeys="code|name" searchLabels="物料编号|物料名称" searchKeys="code|name" targetId="itemName" targetField="name" ></sys:gridselect-pursup>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
				<form:input path="itemName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
<%--			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料规格：">物料规格：</label>
				<form:input path="itemSpecmodel" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="计量单位：">计量单位：</label>
				<form:input path="unitName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="计划采购数量：">计划采购数量：</label>
				<form:input path="planQty" htmlEscape="false"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="需求数量：">需求数量：</label>
				<form:input path="purQty" htmlEscape="false"  class=" form-control"/>
			</div>--%>
			 <div class="col-xs-12 col-sm-6 col-md-6">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left col-sm-6" title="计划日期：">&nbsp;计划日期：</label>
					 <label class="label-item single-overflow " title="至：">&nbsp;至：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-6">
					        	  <div class='input-group date' id='beginPlanDate' style="left: -10px;" >
					                   <input type='text'  name="beginPlanDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-6">
					          	<div class='input-group date' id='endPlanDate' style="left: -10px;" >
					                   <input type='text'  name="endPlanDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>

			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="采购员编号：">采购员编号：</label>
				<sys:userselect-modify id="buyerId" name="buyerId.id" value="${purPlanDetailStatis.buyerId.id}" labelName="buyerId.no" labelValue="${purPlanDetailStatis.buyerId.no}" targetId="buyerName"
							    cssClass="form-control "/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="采购员：">采购员：</label>
				<form:input path="buyerName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="供应商编码：">供应商编码：</label>
					<sys:gridselect-pursup url="${ctx}/account/account/data" id="supId" name="supId.id" value="${purPlanDetailStatis.supId.id}" labelName="supId.accountCode" labelValue="${purPlanDetailStatis.supId.accountCode}"
									title="选择供应商编码" cssClass="form-control required" fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" targetId="supName" targetField="accountName" ></sys:gridselect-pursup>
				</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="供应商名称：">供应商名称：</label>
				<form:input path="supName" htmlEscape="false" maxlength="64"  class=" form-control"/>
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

		<button id="print" class="btn btn-success"  onclick="print()">
			打印
		</button>
			<%--<shiro:hasPermission name="purplanstatis:purPlanDetailStatis:add">
				<a id="add" class="btn btn-primary" href="${ctx}/purplanstatis/purPlanDetailStatis/form" title="采购计划明细表"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="purplanstatis:purPlanDetailStatis:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="purplanstatis:purPlanDetailStatis:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="purplanstatis:purPlanDetailStatis:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/purplanstatis/purPlanDetailStatis/import" method="post" enctype="multipart/form-data"
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
	<table id="purPlanDetailStatisTable"   data-toolbar="#toolbar"></table>

	<!--打印-->
	<div  id="printContent">
	</div>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="purplanstatis:purPlanDetailStatis:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="purplanstatis:purPlanDetailStatis:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
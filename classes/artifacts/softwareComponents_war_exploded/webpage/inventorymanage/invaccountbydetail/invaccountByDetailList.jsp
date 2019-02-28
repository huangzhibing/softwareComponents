<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>库存帐明细查询</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="invaccountByDetailList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">库存帐明细查询</h3>
	</div>

	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="invaccountByDetail" class="form form-horizontal well clearfix">
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="库房号：">仓库编码：</label>
					<sys:gridselect-item url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.wareID" value="${billMain.ware.wareID}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
										   title="选择仓库编码" cssClass="form-control required" fieldLabels="仓库编码|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库编码|仓库名称" searchKeys="wareID|wareName"
										   targetId="wareName" targetField="wareName" ></sys:gridselect-item>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="库房名称：">仓库名称：</label>
					<input id="wareName" name="wareName" htmlEscape="false" maxlength="100" class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="核算期间：">核算期起：</label>
					<input type='text' id="periodStart" name="period.periodID"  maxlength="10"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="核算期间：">核算期起：</label>
					<input type='text' id="periodEnd" name="period.periodID"  maxlength="10"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="物料代码：">物料代码：</label>
					<sys:gridselect-item url="${ctx}/item/item/data2" id="item" name="itemCode" value="${billDetail.item.id}" labelName="item.code" labelValue="${billDetail.item.code}"
										   title="选择物料代码" cssClass="form-control  required" fieldLabels="物料代码|物料名称|规格型号" fieldKeys="code|name|specModel" searchLabels="物料代码|物料名称|规格型号" searchKeys="code|name|specModel"
										   targetId="itemName" targetField="name" extraField="itemSpec:specModel">
					</sys:gridselect-item>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
					<input type='text' id="itemName" name="itemName"   class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="规格型号：">规格型号：</label>
					<input type='text' id="itemSpec" name="itemSpec"   class=" form-control"/>
				</div>
		 <div class="col-xs-12 col-sm-6 col-md-3">
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
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="invaccountByDetailTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="invaccountbyitem:invaccountByItem:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="invaccountbyitem:invaccountByItem:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
	</div>
</body>
</body>
</html>
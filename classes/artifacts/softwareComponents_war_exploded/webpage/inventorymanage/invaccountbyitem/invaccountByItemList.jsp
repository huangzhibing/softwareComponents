<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>库存帐按物料查询</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="invaccountByItemList.js" %>
	<%@include file="invaccountByItemTreeList.js"%>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">库存帐按物料查询</h3>
	</div>
	<div class="panel-body">
		<div class="row">
			<%--树--%>
			<div class="col-sm-4 col-md-3">
				<div class="form-group">
					<div class="row">
						<div class="col-sm-10">
							<div class="input-search">
								<button type="submit" class="input-search-btn">
									<i class="fa fa-search" aria-hidden="true"></i></button>
								<input id="search_q" type="text" class="form-control input-sm" name=""
									   placeholder="查找...">

							</div>
						</div>
					</div>
				</div>
				<div id="itemClassTree"></div>
			</div>
				<div class="col-sm-8 col-md-9 animated fadeInRight">
			<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="invaccountByItem" class="form form-horizontal well clearfix">
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="核算期间：">核算期：</label>
					<input type='text' id="period" name="period.periodID"  maxlength="10"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="物料代码：">物料代码：</label>
					<sys:gridselect-item url="${ctx}/item/item/data2" id="item" name="item.id" value="${billDetail.item.id}" labelName="item.code" labelValue="${billDetail.item.code}"
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
					<label class="label-item single-overflow pull-left" title="库房号：">仓库编码：</label>
					<sys:gridselect-item url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${billMain.ware.id}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
										   title="选择仓库编码" cssClass="form-control required" fieldLabels="仓库编码|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库编码|仓库名称" searchKeys="wareID|wareName"
										   targetId="wareName" targetField="wareName" ></sys:gridselect-item>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="库房名称：">仓库名称：</label>
					<input id="wareName" name="wareName" htmlEscape="false" maxlength="100" class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="laber-item pull-left single-overflow" title="物料类编码：">物料类编码：</label>
					<sys:treeselect-modify targetId="className" targetField="classId" id="classCode" name="classCode.id" value="${item.classCode.id}" labelName="classCode.classId" labelValue="${item.classCode.classId}"
										   title="物料类型编码" url="/item/itemClassNew/treeData"  cssClass="form-control required" allowClear="true" />
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3" >
					<label class="label-item single-overflow pull-left" title="物料类名称：">物料类名称：</label>
					<input name="className" htmlEscape="false"   id="className" class="form-control " />
				</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="货区编号：">货区编号：</label>
				<sys:gridselect-item targetId="binName" targetField="binDesc" url="${ctx}/bin/bin/data" id="bin" name="bin.id" value="${invaccountByItem.bin.id}" labelName="bin.binId" labelValue="${invaccountByItem.bin.binId}"
					title="选择货区编号" cssClass="form-control required" fieldLabels="货区编号|货区名称" fieldKeys="binId|binDesc" searchLabels="货区编号|货区名称" searchKeys="binId|binDesc" ></sys:gridselect-item>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="货区名称：">货区名称：</label>
					<input id="binName" htmlEscape="false" maxlength="20"  class=" form-control"/>
				</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="货位编号：">货位编号：</label>
				<sys:gridselect-item targetId="locName" targetField="locDesc" url="${ctx}/location/location/data" id="loc" name="loc.id" value="${invaccountByItem.loc.id}" labelName="loc.locId" labelValue="${invaccountByItem.loc.locId}"
					title="选择货位编号" cssClass="form-control required" fieldLabels="货位编号|货位名称" fieldKeys="locId|locDesc" searchLabels="货位编号|货位名称" searchKeys="locId|locDesc" ></sys:gridselect-item>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="货位名称：">货位名称：</label>
					<input id="locName" htmlEscape="false" maxlength="20"  class=" form-control"/>
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
	<table id="invaccountByItemTable"   data-toolbar="#toolbar"></table>

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
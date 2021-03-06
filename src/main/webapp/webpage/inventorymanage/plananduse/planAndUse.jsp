<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>计划领用对照</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="planAndUse.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">库存资金占用</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="sortCount" class="form form-horizontal well clearfix">
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left">核算期间：</label>
					<input  name="period.periodId"    class="form-control required"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="部门编码：">部门编码：</label>
					<sys:treeselect-modify id="dept" name="dept.id" value="${billMain.dept.id}" labelName="dept.code" labelValue="${billMain.dept.name}"
									title="部门" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"
									targetId="deptname" targetField="code"></sys:treeselect-modify>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="部门名称：">部门名称：</label>
					<input id="deptname" name="deptName" htmlEscape="false" maxlength="30"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="物料代码：">物料编码：</label>
					<sys:gridselect-modify urlParamId="classCodeName" urlParamName="classCode.classId" url="${ctx}/item/item/data2" id="item" name="item.id" value="${billDetail.item.id}" labelName="item.code" labelValue="${billDetail.item.code}"
										   title="选择物料代码" cssClass="form-control  required" fieldLabels="物料代码|物料名称|规格型号" fieldKeys="code|name|specModel" searchLabels="物料代码|物料名称|规格型号" searchKeys="code|name|specModel"
										   targetId="itemName" targetField="name" extraField="itemSpec:specModel">
					</sys:gridselect-modify>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
					<input type='text' id="itemName" name="itemName"   class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="规格型号：">规格型号：</label>
					<input type='text' id="itemSpec" name="itemSpec"   class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-6">
					<label class="label-item single-overflow pull-left" title="日期：">&nbsp;日期：</label>
					<div class="col-xs-12">
						<div class="col-xs-12 col-sm-6 col-md-5">
							<div class='input-group date' id='beginBillDate' style="left: 0px;" >
								<input type='text'  name="beginBillDate" class="form-control"  />
								<span class="input-group-addon">
								   <span class="glyphicon glyphicon-calendar"></span>
							   </span>
							</div>
						</div>
						<div class="col-xs-12 col-sm-6 col-sm-1">
							~
						</div>
						<div class="col-xs-12 col-sm-6 col-md-5">
							<div class='input-group date' id='endBillDate' style="left: -10px;" >
								<input type='text'  name="endBillDate" class="form-control" />
								<span class="input-group-addon">
								   <span class="glyphicon glyphicon-calendar"></span>
							   </span>
							</div>
						</div>
					</div>
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
	<table id="myTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="invaccount:invAccount:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="invaccount:invAccount:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
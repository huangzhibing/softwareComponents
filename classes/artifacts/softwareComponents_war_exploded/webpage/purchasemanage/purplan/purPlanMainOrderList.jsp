<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购计划管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="purPlanMainOrderList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">采购计划列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<%--@elvariable id="purPlanMain" type="com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain"--%>
			<form:form id="searchForm" modelAttribute="purPlanMain" class="form form-horizontal well clearfix">
		<div class="row">
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="采购计划编号：">单据编号：</label>
				<form:input path="billNum" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			
			
			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="物料编码：">物料编码：</label>
										<sys:gridselect-pursup url="${ctx}/item/item/data2" id="itemCode" name="itemCode.id" value="${purPlanMain.itemCode}" labelName="itemCode.code" labelValue="${purPlanMain.itemCode}"
							 title="选择物料编码" cssClass="form-control" fieldLabels="物料编码|物料名称" fieldKeys="code|name" searchLabels="物料编码|物料名称" searchKeys="code|name" targetId="itemName|specModel" targetField="name|specModel" ></sys:gridselect-pursup>
			</div>
			
			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
				<form:input path="itemName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			
			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="规格型号：">规格型号：</label>
				<form:input path="specModel" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="制单人编号：">制单人编号：</label>
				<sys:userselect-update id="makeEmpid" name="makeEmpid.id" value="${purPlanMain.makeEmpid.id}" labelName="makeEmpid.no" labelValue="${purPlanMain.makeEmpid.no}"
							    cssClass="form-control "  />
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="制单人名称：">制单人名称：</label>
				<form:input path="makeEmpname" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="计划类型编码：">计划类型编码：</label>
										<sys:gridselect-pursup url="${ctx}/plantype/planType/data" id="planTypeCode" name="planTypeCode.id" value="${purPlanMain.planTypeCode.id}" labelName="planTypeCode.plantypeId" labelValue="${purPlanMain.planTypeCode.plantypeId}"
							 title="选择计划类型编码" cssClass="form-control required" fieldLabels="计划类型编码|计划类型名称" fieldKeys="plantypeId|plantypename" searchLabels="计划类型编码|计划类型名称" searchKeys="plantypeId|plantypename" targetId="planTypeName" targetField="plantypename"></sys:gridselect-pursup>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="计划类型名称：">计划类型名称：</label>
				<form:input path="planTypeName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
		</div>

		<div class="row">
			 <div class="col-xs-12 col-sm-6 col-md-6">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left col-md-6" title="计划日期：">计划日期：</label>
					<label class="label-item single-overflow " title="至：">&nbsp;至：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-6">
					        	  <div class='input-group date' id='beginPlanDate' style="left: -10px" >
					                   <input type='text'  name="beginPlanDate" class="form-control"   />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>

					        <div class="col-xs-12 col-sm-6">
					          	<div class='input-group date' id='endPlanDate'>
					                   <input type='text'  name="endPlanDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-6">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left col-md-6" title="制单日期：">&nbsp;制单日期：</label>
					<label class="label-item single-overflow " title="至：">&nbsp;至：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-6">
					        	  <div class='input-group date' id='beginMakeDate'  >
					                   <input type='text'  name="beginMakeDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-6">
					          	<div class='input-group date' id='endMakeDate'>
					                   <input type='text'  name="endMakeDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
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
		<%--	<shiro:hasPermission name="purplan:purPlanMainOrder:add">
				<a id="add" class="btn btn-primary" href="${ctx}/purplan/purPlanMainOrder/form" title="采购计划"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>--%>
			<shiro:hasPermission name="purplan:purPlanMainOrder:list">
			    <button id="detail" class="btn btn-warning" onclick="detail()">
	            	 <i class="glyphicon glyphicon-folder-open"></i>  详情
	        	</button>
			</shiro:hasPermission>
<%--			<shiro:hasPermission name="purplan:purPlanMainOrder:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="purplan:purPlanMainOrder:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>--%>
			
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="purPlanMainTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="purplan:purPlanMainOrder:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="purplan:purPlanMainOrder:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
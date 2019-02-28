<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车间物料换件处理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="processRoutineDetailChangeList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">车间物料已安装明细列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="processRoutineDetail" class="form form-horizontal well clearfix">
			<div class="row">
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="机器序列号：">机器序列号：</label>
					<form:input path="mserialno" htmlEscape="false" maxlength="20"  class=" form-control"/>
				</div>

			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="加工路线单号：">加工路线单号：</label>
				<form:input path="routinebillno" htmlEscape="false" maxlength="20"  class=" form-control"/>
			</div>
			<%--<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="产品编码：">产品编码：</label>
				<form:input path="prodcode" htmlEscape="false" maxlength="20"  class=" form-control"/>
			</div>--%>

			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="单件序号：">单件序号：</label>
				<form:input path="seqno" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="产品名称：">产品名称：</label>
				<form:input path="prodname" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>

			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工艺编码：">工艺编码：</label>
				<form:input path="routingcode" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>


			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工作中心代码：">工作中心代码：</label>
				<form:input path="centercode.centerCode" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>
		</div>
		<div class="row">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="计划生产日期：">&nbsp;生产日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginPlanBDate' style="left: -10px;" >
					                   <input type='text'  name="beginPlanbdate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endPlanBDate' style="left: -10px;" >
					                   <input type='text'  name="endPlanbdate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>

			<%--<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="是否已完成工艺/扫码：">工艺/扫码是否已完成：</label>
				<form:select path="billstate" class="form-control " >
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sfc_processmaterial_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>--%>

			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="是否有零件品质异常：">是否有零件品质异常：</label>
				<form:select path="hasQualityPro" class="form-control " >
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('itemFinish')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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

				<button id="changeDeal" class="btn btn-success" disabled onclick="changeDeal()">
					<i class="glyphicon glyphicon-edit"></i> 换件处理
				</button>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="processRoutineDetailTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<%--<shiro:hasPermission name="processmaterialdetail:processRoutineDetail:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="processmaterialdetail:processRoutineDetail:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>--%>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
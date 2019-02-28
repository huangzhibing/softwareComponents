<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车间作业计划分批管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="sfcProcessDetailList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">车间作业计划分批列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="sfcProcessDetail" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="作业计划编号：">作业计划编号：</label>
				<form:input path="processBillNo" htmlEscape="false" maxlength="20"  class=" form-control"/>
			</div>
			<%-- <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="主生产计划号：">主生产计划号：</label>
				<form:input path="mpsPlanId" htmlEscape="false" maxlength="12"  class=" form-control"/>
			</div>--%>


				<div class="col-xs-12 col-sm-6 col-md-3">
					<div class="input-group" style="width: 100%">
						<label class="label-item single-overflow pull-left" title="产品名称：">产品名称：</label>
						<form:input path="prodName" htmlEscape="false" maxlength="64"  class=" form-control"/>
						<span class="input-group-btn">
									 <button type="button"  id="prodButton" class="btn btn-primary" style="top: 11px;"><i class="fa fa-search"></i></button>
									 <button type="button" id="prodDelButton" class="close" data-dismiss="alert" style="position: absolute; top: 30px; right: 53px; z-index: 999; display: block;">×</button>
								</span>
					</div>
				</div>


			 <div class="col-xs-12 col-sm-6 col-md-6">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="计划生产日期：">&nbsp;计划生产日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginPlanBdate' style="left: -10px;" >
					                   <input type='text'  name="beginPlanBdate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endPlanBdate' style="left: -10px;" >
					                   <input type='text'  name="endPlanBdate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>

			 <%--<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="制定人：">制定人：</label>
				<sys:userselect id="makePID" name="makePID" value="${sfcProcessDetail.makePID}" labelName="" labelValue="${sfcProcessDetail.}"
							    cssClass="form-control "/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="制定日期：">&nbsp;制定日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginMakeDate' style="left: -10px;" >
					                   <input type='text'  name="beginMakeDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endMakeDate' style="left: -10px;" >
					                   <input type='text'  name="endMakeDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>--%>


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

			<shiro:hasPermission name="processbatch:sfcProcessDetail:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 车间作业计划分批
	        	</button>
			</shiro:hasPermission>

	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="sfcProcessDetailTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="processbatch:sfcProcessDetail:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="processbatch:sfcProcessDetail:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
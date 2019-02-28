<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>人工单据管理管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="realAccountList.js" %>
	<%@include file="realAccountEditList.js"%>
	<%@include file="realAccountCheckList.js"%>
	<%@include file="realAccountSearchList.js"%>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">人工凭证管理列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="realAccount" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="凭证号：">凭证号：</label>
				<form:input path="billNum" htmlEscape="false" maxlength="15"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="原始凭证号：">原始凭证号：</label>
				<form:input path="oriBillId" htmlEscape="false" maxlength="18"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<div class="form-group">
					<label class="label-item single-overflow pull-left" title="原始单据日期：">&nbsp;原始单据日期：</label>
					<div class="col-xs-12">
						<div class='input-group date' id='oriBillDate' >
			                   <input type='text'  name="oriBillDate" class="form-control"  />
			                   <span class="input-group-addon">
			                       <span class="glyphicon glyphicon-calendar"></span>
			                   </span>
			            </div>	
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="会计期间：">会计期间：</label>
				<form:input path="periodId" htmlEscape="false" maxlength="8"  class=" form-control"/>
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
		<c:if test="${flag == 'check'}">
			<button id="jihe" class="btn btn-success" disabled onclick="jihe('check')">
				<i class="glyphicon glyphicon-edit"></i> 稽核
			</button>
		</c:if>
		<c:if test="${flag == 'edit'}">
			<button id="xiugai" class="btn btn-success" disabled onclick="xiugai('edit')">
				<i class="glyphicon glyphicon-edit"></i> 修改
			</button>
			<shiro:hasPermission name="realaccount:realAccount:del">
				<button id="removebyzhidan" class="btn btn-danger" onclick="deleteAllbyxiugai()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
		</c:if>
	     <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
		 </a>
	</div>
		
	<!-- 表格 -->
		<c:if test="${flag == 'zhidan'}">
			<table id="realAccountTable"   data-toolbar="#toolbar"></table>
		</c:if>
		<c:if test="${flag == 'edit'}">
			<table id="realAccountEditTable"   data-toolbar="#toolbar"></table>
		</c:if>
		<c:if test="${flag == 'check'}">
			<table id="realAccountCheckTable"   data-toolbar="#toolbar"></table>
		</c:if>
		<c:if test="${flag == 'view'}">
			<table id="realAccountSearchTable"   data-toolbar="#toolbar"></table>
		</c:if>
    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="realaccount:realAccount:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="realaccount:realAccount:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>质量问题处理结果管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="qmReportDealList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">质量问题处理结果列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<%--@elvariable id="qmReportQuery" type="com.hqu.modules.qualitymanage.qmreportquery.entity.QmReportQuery"--%>
			<form:form id="searchForm" modelAttribute="qmReportQuery" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="问题处理报告单编号：">问题处理报告单编号：</label>
				<form:input path="qmreportId" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验单编号：">检验单编号：</label>
				<form:input path="purreportId" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="问题处理人：">问题处理人：</label>
				<sys:userselect id="qmPerson" name="qmPerson.id" value="${qmReportQuery.qmPerson.id}" labelName="qmPerson.name" labelValue="${qmReportQuery.qmPerson.name}"
							    cssClass="form-control required"/>
			</div>

			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="问题处理意见：">问题处理意见：</label>
				<sys:gridselect url="${ctx}/matterhandling/matterHandling/data" id="mhandlingName" name="mhandlingName.id" value="${qmReportQuery.mhandlingName.id}" labelName="mhandlingName.mhandlingname" labelValue="${qmReportQuery.mhandlingName.mhandlingname}"
								title="选择问题处理意见" cssClass="form-control required" fieldLabels="处理意见|意见描述" fieldKeys="mhandlingname|mhandlingdes" searchLabels="处理意见|意见描述" searchKeys="mhandlingname|mhandlingdes" ></sys:gridselect>
			</div>

			 <div class="col-xs-12 col-sm-6 col-md-8">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left col-md-6" title="问题处理日期：">&nbsp;问题处理日期：</label>
					 <label class="label-item single-overflow pull-left col-md-6" title="至：">&nbsp;至：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-6">
					        	  <div class='input-group date' id='beginQmDate' >
					                   <input type='text'  name="beginQmDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-6">
					          	<div class='input-group date' id='endQmDate'  >
					                   <input type='text'  name="endQmDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>

		<div class="row">
			<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="检验类型名称：">检验类型名称：</label>
					<form:select path="qmType"  class="form-control m-b">
						<form:option value="整机" label="整机"/>
						<%--<form:options items="${fns:getDictList('checkNameALL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
					</form:select>
			</div>

			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="单据状态：">单据状态：</label>
				<form:select path="state"  class="form-control m-b">
					<form:option value="已提交" label="已提交"/>
					<%--<form:options items="${fns:getDictList('done_or_not')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
				</form:select>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="是否已处理：">是否已处理：</label>
				<form:select path="isDeal"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>

		 <div class=" col-xs-12 col-sm-6 col-md-3"  style="margin-top:20px">
			  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
	    </div>

	</form:form>
	</div>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<%-- <shiro:hasPermission name="qmreportquery:qmReportDeal:add">
				<a id="add" class="btn btn-primary" href="${ctx}/qmreportquery/qmReportQuery/form" title="质量问题报告"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="qmreportquery:qmReportDeal:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="qmreportquery:qmReportDeal:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="qmreportquery:qmReportDeal:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/qmreportquery/qmReportQuery/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission> --%>
			<shiro:hasPermission name="qmreport:qmReport:list">
			    <button id="item_detail" class="btn btn-warning" onclick="itemDetail()">
	            	<i class="glyphicon glyphicon-folder-open"></i> 详情
	        	</button>
			</shiro:hasPermission>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
				
		    </div>
		
	<!-- 表格 -->
	<table id="qmReportQueryTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="qmreportquery:qmReportDeal:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="qmreportquery:qmReportDeal:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
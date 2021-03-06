<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单发送</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="purReportSubmitQueryList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">检验单发送查询列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="purReport" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验单编号：">检验单编号：</label>
				<form:input path="reportId" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验类型名称：">检验类型名称：</label>
			<%--	<form:input path="checktName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			 --%>	<form:select path="checktName"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkTypeNameNew')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			
			</div>
			
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验结论：">检验结论：</label>
				<!--<form:input path="checkResult" htmlEscape="false" maxlength="64"  class=" form-control"/>-->
				<form:select path="checkResult"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkRelsult_L')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="全检/抽检：">全检/抽检：</label>
				<form:select path="rndFul"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="全检/抽检：">发送状态：</label>
				<form:select path="sendState"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sendState')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>			 
			  <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验单发送人：">检验单发送人：</label>
				<sys:userselect id="cPerson" name="cpersonCode" value="${purReport.cpersonCode}" labelName="checkPerson" labelValue="${purReport.checkPerson}"
							    cssClass="form-control required"/>
			</div>
			 
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="检验日期：">&nbsp;检验日期：</label>
					<div class="col-xs-12">   
					        	  <div class='input-group date' id='beginCheckDate'  >
					                   <input type='text'  name="beginCheckDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					</div>
				</div>
			</div>
					
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="检验日期：">&nbsp;至：</label>
					<div class="col-xs-12">					   	      
					          	<div class='input-group date' id='endCheckDate'  >
					                   <input type='text'  name="endCheckDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
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
	</div>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			   <shiro:hasPermission name="purreport:purReportSubmit:list">			
			    <button id="view" class="btn btn-success" disabled onclick="view()">
	            	<i class="glyphicon glyphicon-view"></i> 详情
	        	</button>
	        	</shiro:hasPermission>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="purReportTable"   data-toolbar="#toolbar"></table>

    <!-- context menu 
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="purreport:purReport:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="purreport:purReport:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
    -->
	</div>
	</div>
	</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单查询管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="purReportQueryList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">检验单查询列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="purReportQuery" class="form form-horizontal well clearfix">
			 
			 <div class="col-xs-12 col-sm-6 col-md-4" style="display:none">
				<label class="label-item single-overflow pull-left" title="所辖部门：">所辖部门：</label>
				<form:select path="office.id" class="form-control "  >
							<form:option value="" label=""/>
							<form:options items="${officeList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
				
				<%-- 
				<form:input path="underOffice" htmlEscape="false" maxlength="64"  class=" form-control"/>
				--%>
				
				</div>
			<div class="col-xs-12 col-sm-6 col-md-4" style="display:none">
				<label class="label-item single-overflow pull-left" title="所辖人员：">所辖人员：</label>
				
				
				
				<form:select path="cpersonCode" class="form-control "  >
							<form:option value="" label=""/>
							<form:options items="${userList}" itemLabel="name" itemValue="no" htmlEscape="false"/>
				</form:select>
				<%-- 
				<form:input path="underUser" htmlEscape="false" maxlength="64"  class=" form-control"/>
				--%>
			
			
			</div>
			 
			 
			 
		 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验单编号：">检验单编号：</label>
				<form:input path="reportId" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="采购报检单编号：">采购报检单编号：</label>
									
				<sys:gridselect url="${ctx}/purreport/purReport/invCheckMainData" id="invCheckMain" name="invCheckMain.id" value="${purReport.invCheckMain.id}" labelName="invCheckMain.billnum" labelValue="${purReport.invCheckMain.billnum}"
							 title="选择采购报检单编号" cssClass="form-control required" fieldLabels="单据编号|供应商名称|制单日期|到货日期|采购员" 
							 fieldKeys="billnum|supName|makeDate|arriveDate|buyerName" 
							 searchLabels="单据编号|供应商名称|制单日期|到货日期|采购员" 
							 searchKeys="billnum|supName|makeDate|arriveDate|buyerName" >
				</sys:gridselect>
			</div>
			 
			
			 <div class="col-xs-12 col-sm-6 col-md-4">
									
					<label class="label-item single-overflow pull-left">完工入库单编号：</label>
					
						<sys:gridselect url="${ctx}/purreport/purReport/SFCData" id="sfcInvCheckMain" name="sfcInvCheckMain.id" value="${purReport.sfcInvCheckMain.id}" labelName="sfcInvCheckMain.billNo" labelValue="${purReport.sfcInvCheckMain.billNo}"
							 title="选择完工入库单编号" cssClass="form-control required" fieldLabels="完工入库单编号|单据类型|制单人名称" fieldKeys="billNo|billType|makepName" searchLabels="完工入库单编号|单据类型|制单人名称" searchKeys="billNo|billType|makepName" ></sys:gridselect>										
					
			</div>
			
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left">超期复验单编号：</label>
					
						<sys:gridselect url="${ctx}/purreport/purReport/reInvData" id="reinvCheckMain" name="reinvCheckMain.id" value="${purReport.reinvCheckMain.id}" labelName="reinvCheckMain.billNum" labelValue="${purReport.reinvCheckMain.billNum}"
							 title="选择超期复验单编号" cssClass="form-control required" fieldLabels="超期复验单据号|核算期|制单日期|制单人名称" fieldKeys="billNum|periodId|makeDate|makeEmpname" searchLabels="超期复验单据号|核算期|制单日期|制单人名称" searchKeys="billNum|periodId|makeDate|makeEmpname" ></sys:gridselect>
					
			</div>
			
			
		
		
		
		
		
		
			
			
			
			
			<br><br><br><br>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验结论：">检验结论：</label>
				<form:select path="checkResult"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkResultChinese')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="全检/抽检：">全检/抽检：</label>
				<form:select path="rndFul"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkTypeChinese')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			
			
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验类型名称：">检验类型名称：</label>
				<form:select path="checktName"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkNameALL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			
		
		
		
			<div class="col-xs-12 col-sm-6 col-md-4">
				<div class="form-group">
					<label class="label-item single-overflow pull-left" title="检验日期范围：">&nbsp;检验日期范围：</label>
					<div class="col-xs-12">
						<div class='input-group date' id='beginCheckDate' >
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
					<label class="label-item single-overflow pull-left" title="至：">&nbsp;至：</label>
					<div class="col-xs-12">
						<div class='input-group date' id='endCheckDate' >
			                   <input type='text'  name="endCheckDate" class="form-control"  />
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
			
			<shiro:hasPermission name="purreportquery:purReportQuery:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 详情
	        	</button>
			</shiro:hasPermission>
			<%-- 
			<shiro:hasPermission name="purreportquery:purReportQuery:add">
				<a id="add" class="btn btn-primary" href="${ctx}/purreportquery/purReportQuery/form" title="检验单查询"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="purreportquery:purReportQuery:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="purreportquery:purReportQuery:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/purreportquery/purReportQuery/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
				
			</shiro:hasPermission>
			--%>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="purReportQueryTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="purreportquery:purReportQuery:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="purreportquery:purReportQuery:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
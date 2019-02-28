<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="purReportSelectList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">

	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<%--@elvariable id="purReport" type="PurReport"--%>
			<form:form id="searchForm" modelAttribute="purReport" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验单编号：">检验单编号：</label>
				<form:input path="reportId" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
<%--			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="采购报检单编号：">采购报检单编号：</label>
										<sys:gridselect url="${ctx}/purinvcheckmain/invCheckMain/data" id="invCheckMain" name="invCheckMain.id" value="${purReport.invCheckMain.id}" labelName="invCheckMain.billnum" labelValue="${purReport.invCheckMain.billnum}"
							 title="选择采购报检单编号" cssClass="form-control required" fieldLabels="采购报检单编号" fieldKeys="billnum" searchLabels="采购报检单编号" searchKeys="billnum" ></sys:gridselect>
			</div>--%>
			<%-- <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验类型名称：">检验类型名称：</label>
				<form:select path="checktName"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checktName_zhang')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>--%>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验结论：">检验结论：</label>
				<form:select path="checkResult"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkResult_yang')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验类型：">检验类型：</label>
				<form:select path="rndFul"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>

<!-- 			 <div class="col-xs-12 col-sm-6 col-md-4">
				<div class="form-group">
					<label class="label-item single-overflow pull-left" title="检验日期：">&nbsp;检验日期：</label>
					<div class="col-xs-12">
						<div class='input-group date' id='checkDate' >
			                   <input type='text'  name="checkDate" class="form-control"  />
			                   <span class="input-group-addon">
			                       <span class="glyphicon glyphicon-calendar"></span>
			                   </span>
			            </div>	
					</div>
				</div>
			</div> 
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="审核结论：">审核结论：</label>
				<input path="justifyResult" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>-->
				
		 <div class="col-xs-12 col-sm-6 col-md-4">
			<div style="margin-top:26px">
			  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
			 </div>
	    </div>	
			
<!-- 			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="审核人：">审核人：</label>
				<input path="justifyPerson" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="审核岗位名称：">审核岗位名称：</label>
				<input path="jpositionName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<div class="form-group">
					<label class="label-item single-overflow pull-left" title="审核日期：">&nbsp;审核日期：</label>
					<div class="col-xs-12">
						<div class='input-group date' id='justifyDate' >
			                   <input type='text'  name="justifyDate" class="form-control"  />
			                   <span class="input-group-addon">
			                       <span class="glyphicon glyphicon-calendar"></span>
			                   </span>
			            </div>	
					</div>
				</div>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="备注：">备注：</label>
				<input path="memo" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="执行标准：">执行标准：</label>
				<input path="qualityNorm" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验岗位编码：">检验岗位编码：</label>
				<input path="cpositionCode" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div> -->

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
	<table id="purReportTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
 
	</div>
	</div>
	</div>
</body>
</html>
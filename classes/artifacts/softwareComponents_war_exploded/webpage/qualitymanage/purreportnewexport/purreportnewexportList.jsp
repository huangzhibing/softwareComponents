<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购检验IQC导出管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="purreportnewexportList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">采购检验IQC导出列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="purreportnewexport" class="form form-horizontal well clearfix">
		 
		 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="报告单编号：">报告单编号：</label>
				<form:input path="reportId" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="采购单号：">采购单号：</label>
										<sys:gridselect-sup url="${ctx}/purreport/purReport/invCheckMainData2" id="inv" name="inv.id" value="${purReportNew.inv.id}" labelName="inv.billnum" labelValue="${purReportNew.inv.billnum}"
							 title="选择采购报检单编号" cssClass="form-control required" 
							  targetId="icode|itemName|itemSpecmodel|supId|supname1|itemArriveDate|unitCode|realPriceTaxed" 
							  targetField="itemCode|itemName|itemSpecmodel|supId|supName|itemArriveDate|unitCode|realPriceTaxed" 
							 fieldLabels="单据编号|物料编码|物料名称|物料规格|供应商编码|供应商名称" 
							 fieldKeys="billnum|itemCode|itemName|itemSpecmodel|supId|supName" 
							 searchLabels="单据编号|物料编码|物料名称|物料规格|供应商编码|供应商名称" 
							 searchKeys="billnum|itemCode|itemName|itemSpecmodel|supId|supName" >
						</sys:gridselect-sup>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="料号：">料号：</label>
										<form:input path="icode" htmlEscape="false"    class="form-control "/>
					</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
				<form:input path="itemName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="供应商名称：">供应商名称：</label>
										<form:input path="supname1" htmlEscape="false"    class="form-control "/></div>
			 <!--  
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="来料数量：">来料数量：</label>
				<form:input path="itemCount" htmlEscape="false"  class=" form-control"/>
			</div>
			-->
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="送检编号：">送检编号：</label>
				<form:input path="inspectionNum" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="来料日期：">&nbsp;来料日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginItemArriveDate' style="left: -10px;" >
					                   <input type='text'  name="beginItemArriveDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endItemArriveDate' style="left: -10px;" >
					                   <input type='text'  name="endItemArriveDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="检验日期：">&nbsp;检验日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginCheckDate' style="left: -10px;" >
					                   <input type='text'  name="beginCheckDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endCheckDate' style="left: -10px;" >
					                   <input type='text'  name="endCheckDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验员：">检验员：</label>
				<sys:userselect id="user" name="user.id" value="${purReportNew.user.id}" labelName="user.name" labelValue="${purReportNew.user.name}"
							    cssClass="form-control "/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="全检抽检：">全检抽检：</label>
				<form:select path="rndFul"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="收货标准：">收货标准：</label>
				<form:select path="aql"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('AQL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验结果：">检验结果：</label>
				<form:select path="checkResult"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('checkResultNew')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="检验处理：">检验处理：</label>
				<form:select path="handleResult"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('handleresult')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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
			
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="purreportnewexportTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="purreportnewexport:purreportnewexport:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="purreportnewexport:purreportnewexport:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
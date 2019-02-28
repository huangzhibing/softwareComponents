<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>销售合同管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="contractEndList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">销售合同列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="contract" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="合同编码：">合同编码：</label>
				<form:input path="contractCode" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="签订日期：">&nbsp;签订日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginSignDate' style="left: -10px;" >
					                   <input type='text'  name="beginSignDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endSignDate' style="left: -10px;" >
					                   <input type='text'  name="endSignDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="合同类型编码：">合同类型编码：</label>
				<sys:gridselect-modify targetId="ctrTypeNamed" targetField="ctrTypeName" extraField="ctrTypeCoded:ctrTypeCode" url="${ctx}/contract/ctrTypeDef/data" id="contractType" name="contractType.id" value="${contract.contractType.id}" labelName="contractType.ctrTypeCode" labelValue="${contract.contractType.ctrTypeCode}"
							 title="选择合同类型编码" cssClass="form-control required" fieldLabels="类型编码|类型名称" fieldKeys="ctrTypeCode|ctrTypeName" searchLabels="类型编码|类型名称" searchKeys="ctrTypeCode|ctrTypeName" ></sys:gridselect-modify>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="合同类型名称：">合同类型名称：</label>
				<form:input readOnly="true" id="ctrTypeNamed" path="" htmlEscape="false" maxlength="100"  class=" form-control"/>
			</div>
			<input style="display:none;" id="ctrTypeCoded" name="contractType.ctrTypeCode" />
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="客户编码：">客户编码：</label>
										<sys:gridselect-modify targetId="accountNamed" targetField="accountName" url="${ctx}/account/account/data?accTypeNameRu=客户" id="account" name="account.id" value="${contract.account.id}" labelName="account.accountCode" labelValue="${contract.account.accountCode}"
							 title="选择客户编码" cssClass="form-control required" fieldLabels="客户编码|客户名称" fieldKeys="accountCode|accountName" searchLabels="客户编码|客户名称" searchKeys="accountCode|accountName" ></sys:gridselect-modify>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="客户名称：">客户名称：</label>
				<form:input readOnly="true" id="accountNamed" path="accountName" htmlEscape="false" maxlength="100"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="销售人员编码：">销售人员编码：</label>
				<sys:userselect-modify targetId="personNamed" extraId="personNo" id="person" name="person.id" value="${contract.person.id}" labelName="" labelValue="${contract.person.no}"
							    cssClass="form-control "/>
			</div>
			<input id="personNo" style="display:none;" name="person.no"/>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="销售人员姓名：">销售人员姓名：</label>
				<form:input readOnly="true" id="personNamed" path="personName" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="纸制合同号：">纸制合同号：</label>
				<form:input path="paperCtrCode" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
			<label class="label-item single-overflow pull-left" title="产品代码：">产品代码：</label>
						<sys:gridselect-contract
							targetId="" targetField="" 
							extraField="prodName:itemNameRu;prodSpec:itemSpec" 
							url="${ctx}/product/product/data" id="prodCode" name="prodCode.id" value="" labelName="prodCode.item.code" labelValue=""
							 title="选择产品代码" cssClass="form-control required " fieldLabels="产品代码|产品名称|产品规格" fieldKeys="item.code|itemNameRu|itemSpec" searchLabels="产品代码|产品名称" searchKeys="item.code|itemNameRu" ></sys:gridselect-contract>
					</div>
					
					<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="产品名称：">产品名称：</label>
						<input readOnly="readOnly" id="prodName" name="prodName" type="text" value=""    class="form-control "/>
					</div>
					
					
					<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="产品名称：">产品名称：</label>
						<input readOnly="readOnly" id="prodSpec" name="prodSpec" type="text" value=""    class="form-control "/>
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
			<%-- <shiro:hasPermission name="salcontract:contract:add">
				<a id="add" class="btn btn-primary" href="${ctx}/salcontract/contract/form" title="销售合同"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission> --%>
			<%-- <shiro:hasPermission name="salcontract:contract:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission> --%>
			<%-- <shiro:hasPermission name="salcontract:contract:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission> --%>
			<%-- <shiro:hasPermission name="salcontract:contract:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/salcontract/contract/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission> --%>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="contractTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="salcontract:contract:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="salcontract:contract:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
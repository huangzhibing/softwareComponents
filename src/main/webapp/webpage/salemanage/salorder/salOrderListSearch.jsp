<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>内部订单管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="salOrderListSearch.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">内部订单列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="salOrder" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="合同编码：">合同编码：</label>
										<sys:gridselect url="${ctx}/salcontract/contract/data" id="contract" name="contract.id" value="${salOrder.contract.id}" labelName="contract.contractCode" labelValue="${salOrder.contract.contractCode}"
							 title="选择合同编码" cssClass="form-control required" fieldLabels="合同编码|签订日期" fieldKeys="contractCode|signDate" searchLabels="合同编码|签订日期" searchKeys="contractCode|signDate" ></sys:gridselect>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="订单编码：">订单编码：</label>
				<form:input path="orderCode" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>
			<%-- <div class="col-xs-12 col-sm-6 col-md-4">
			<label class="label-item single-overflow pull-left" title="产品编码：">产品编码：</label>
						<sys:gridselect-contract 
							targetId="" targetField="" 
							extraField="ctrItemList{{idx}}_prodName:itemNameRu;ctrItemList{{idx}}_prodSpec:itemSpec;ctrItemList{{idx}}_unitName:itemMeasure" 
							url="${ctx}/product/product/data" id="ctrItemList{{idx}}_prodCode" name="ctrItemList{{idx}}_prodCode.id" value="{{row.prodCode.id}}" labelName="ctrItemList{{idx}}.prodCode.item.code" labelValue="{{row.prodCode.item.code}}"
							 title="选择产品代码" cssClass="form-control required " fieldLabels="产品代码|产品名称|产品规格|单位" fieldKeys="item.code|itemNameRu|itemSpec|itemMeasure" searchLabels="产品代码|产品名称" searchKeys="item.code|itemNameRu" ></sys:gridselect-contract>
					</div> --%>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="产品编码：">产品编码：</label>
				<form:input path="product.item.code" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="产品名称：">产品名称：</label>
				<form:input path="product.itemNameRu" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="产品规格：">产品规格：</label>
				<form:input path="product.itemSpec" htmlEscape="false" maxlength="64"  class=" form-control"/>
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
				<label class="label-item single-overflow pull-left" title="客户编码：">客户编码：</label>
				<form:input path="accountCode" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="客户名称：">客户名称：</label>
				<form:input path="accountName" htmlEscape="false" maxlength="100"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="交货日期：">&nbsp;交货日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginSendDate' style="left: -10px;" >
					                   <input type='text'  name="beginSendDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endSendDate' style="left: -10px;" >
					                   <input type='text'  name="endSendDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
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
			
			
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="salOrderTable"   data-toolbar="#toolbar"></table>
    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="salorder:salOrder:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="salorder:salOrder:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
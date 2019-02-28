<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购合同管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="contractMainWarningList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">采购订单交期预警列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="contractDetailWarning" class="form form-horizontal well clearfix">
			
			<div class="row">
				<div class="col-xs-12 col-sm-6 col-md-3">
						<label class="label-item single-overflow pull-left" title="下单日期：">下单日期：</label>
						<div class="col-xs-12">   
						        	  <div class='input-group date' id='createBeginDate'  >
						                   <input type='text'  name="createBeginDate" class="form-control"  />
						                   <span class="input-group-addon">
						                       <span class="glyphicon glyphicon-calendar"></span>
						                   </span>
						             </div>	
						</div>
				</div>					
				 <div class="col-xs-12 col-sm-6 col-md-3">
						<label class="label-item single-overflow pull-left" title="下单日期：">至：</label>
						<div class="col-xs-12">					   	      
						          	<div class='input-group date' id='createEndDate'  >
						                   <input type='text'  name="createEndDate" class="form-control" />
						                   <span class="input-group-addon">
						                       <span class="glyphicon glyphicon-calendar"></span>
						                   </span>
						            	</div>	 
						</div>
				</div>
				
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="物料代码：">物料代码：</label>
				   <sys:gridselect-pursup url="${ctx}/item/item/data2" id="item" name="item.id" value="${item.id}" labelName="item.code" labelValue="${contractMain.item.code}"
								 title="选择物料编号" cssClass="form-control required " targetId="itemName" targetField="name"  fieldLabels="物料编码|物料名称" fieldKeys="code|name" searchLabels="物料编码|物料名称" searchKeys="code|name" ></sys:gridselect-pursup>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
					<form:input path="itemName" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>	
			</div>	 
			
			
			<div class="row">
				 <div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="供应商编号：">供应商编号：</label>
				      	<sys:gridselect-pursup url="${ctx}/account/account/data" id="account" name="account.id" value="${contractMain.account.id}" labelName="account.accountCode" labelValue="${contractMain.account.accountCode}"
								 title="选择供应商" cssClass="form-control required" fieldLabels="供应商编码|供应商名称"  targetId="accountName"  targetField="accountName" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" ></sys:gridselect-pursup>
				</div>
				
				 <div class="col-xs-12 col-sm-6 col-md-3">
					<label class="label-item single-overflow pull-left" title="供应商名称：">供应商名称：</label>
					<form:input path="accountName" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
                
                <div class="col-xs-12 col-sm-6 col-md-3">
						<label class="label-item single-overflow pull-left" title="交货日期：">交货日期：</label>
						<div class="col-xs-12">   
						        	  <div class='input-group date' id='arriveBeginDate'  >
						                   <input type='text'  name="arriveBeginDate" class="form-control"  />
						                   <span class="input-group-addon">
						                       <span class="glyphicon glyphicon-calendar"></span>
						                   </span>
						             </div>	
						</div>
				</div>					
				 <div class="col-xs-12 col-sm-6 col-md-3">
						<label class="label-item single-overflow pull-left" title="交货日期：">至：</label>
						<div class="col-xs-12">					   	      
						          	<div class='input-group date' id='arriveEndDate'  >
						                   <input type='text'  name="arriveEndDate" class="form-control" />
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
						
				<shiro:hasPermission name="contractmainwarning:contractMain:list">
					<button id="print" class="btn btn-success" onclick="print()">
		            	<i class="glyphicon glyphicon-view"></i> 打印
		        	</button>
				</shiro:hasPermission>
				
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
	
	<div  id="printContent">
	</div> 
		
	<!-- 表格 -->
	<table id="contractMainTable"   data-toolbar="#toolbar"></table>
        <!-- 创建一个空的iframe，因为如果每次请求都生成PDF，那么是不必要的。 -->
        <iframe style="display:none" id="printIframe"></iframe>
	</div>
	</div>
	</div>
</body>
</html>
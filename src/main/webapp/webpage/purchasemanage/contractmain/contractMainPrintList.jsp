<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购合同管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="contractMainPrintList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">采购合同查询列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="contractMain" class="form form-horizontal well clearfix">
			<div class="row">
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="单据号：">合同号：</label>
					<form:input path="billNum" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
			  	<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="采购员名称：">物料代码：</label>
				   <sys:gridselect-pursup url="${ctx}/item/item/data2" id="item" name="item.id" value="${item.id}" labelName="item.code" labelValue="${contractMain.item.code}"
								 title="选择物料编号" cssClass="form-control required " targetId="itemName|itemModel" targetField="name|specModel"  fieldLabels="物料编码|物料名称" fieldKeys="code|name" searchLabels="物料编码|物料名称" searchKeys="code|name" ></sys:gridselect-pursup>
					
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="采购员名称：">物料名称：</label>
					<form:input path="itemName" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="采购员名称：">规格型号：</label>
					<form:input path="itemModel" htmlEscape="false" maxlength="64"  class=" form-control"/>
		 		</div>
			  
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="供应商编号：">供应商编号：</label>
				      <%--					<form:input path="account.accountCode" htmlEscape="false" maxlength="64"  class=" form-control"/>
					  --%>	<sys:gridselect-pursup url="${ctx}/account/account/data" id="account" name="account.id" value="${contractMain.account.id}" labelName="account.accountCode" labelValue="${contractMain.account.accountCode}"
								 title="选择供应商" cssClass="form-control required" fieldLabels="供应商编码|供应商名称"  targetId="supName"  targetField="accountName" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" ></sys:gridselect-pursup>
				 
				</div>
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="供应商名称：">供应商名称：</label>
					<form:input path="supName" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
			</div>
			<div class="row">
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="采购员编号：">采购员编号：</label>
		            <%-- <form:input path="groupBuyer.user.id" htmlEscape="false" maxlength="64"  class=" form-control"/>					
					--%>
						<sys:gridselect-purbuyer url="${ctx}/group/groupQuery/buyersdata" id="groupBuyer" name="groupBuyer.id" value="${contractMain.groupBuyer.id}" labelName="groupBuyer.user.no" labelValue="${contractMain.groupBuyer.user.no}"
								 title="选择采购员" cssClass="form-control required" fieldLabels="采购员编码|采购员名称" targetId="buyerName" targetField="buyername" fieldKeys="user.no|buyername" searchLabels="采购员编码|采购员名称" searchKeys="user.no|buyername" ></sys:gridselect-purbuyer>
					
				</div>
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="采购员名称：">采购员名称：</label>
					<form:input path="buyerName" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>			 
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="合同类型名称：">合同类型名称：</label>
					<sys:gridselect url="${ctx}/contracttype/contractType/data" id="contractType" name="contractType.id" value="${contractMain.contractType.id}" labelName="contractType.contypename" labelValue="${contractMain.contractType.contypename}"
					 title="选择合同类型代码" cssClass="form-control required" fieldLabels="合同类型代码|合同类型名称" fieldKeys="contypeid|contypename" searchLabels="合同类型代码|合同类型名称" searchKeys="contypeid|contypename" ></sys:gridselect>				
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="采购员名称：">单据状态：</label>
	                     <form:select path="billStateFlag" class="form-control" >
								<form:option value="" label=""/>
								<form:options items="${fns:getDictList('ContractStateFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					     </form:select>	 		
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
						<label class="label-item single-overflow pull-left" title="签订日期：">&nbsp;签订日期：</label>
						<div class="col-xs-12">   
						        	  <div class='input-group date' id='beginBdate'  >
						                   <input type='text'  name="beginBdate" class="form-control"  />
						                   <span class="input-group-addon">
						                       <span class="glyphicon glyphicon-calendar"></span>
						                   </span>
						             </div>	
						</div>
				</div>					
				 <div class="col-xs-12 col-sm-6 col-md-4">
						<label class="label-item single-overflow pull-left" title="签订日期：">&nbsp;至：</label>
						<div class="col-xs-12">					   	      
						          	<div class='input-group date' id='endBdate'  >
						                   <input type='text'  name="endBdate" class="form-control" />
						                   <span class="input-group-addon">
						                       <span class="glyphicon glyphicon-calendar"></span>
						                   </span>
						            	</div>	 
						</div>
				</div>	
			</div>			
			<div class="row">
				<div class="col-xs-12 col-sm-6 col-md-4">
						<label class="label-item single-overflow pull-left" title="采购员名称：">单据类型：</label>
		                     <form:select path="contrState" class="form-control" >
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('contrState')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						     </form:select>	 		
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="供应商名称：">计划单号：</label>
					<form:input path="planBillNum" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<%--<div class="col-xs-12 col-sm-6 col-md-4">--%>
					<%--<label class="label-item single-overflow pull-left" title="打印：">打印：</label>--%>
					<%--<form:select path="isPrint" class="form-control" >--%>
						<%--<form:option value="" label=""/>--%>
						<%--<form:options items="${fns:getDictList('contractIsPrint')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
					<%--</form:select>--%>
				<%--</div>--%>
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
			<shiro:hasPermission name="contractmainprint:contractMain:list">
			    <button id="detail" class="btn btn-success" onclick="print()">
	            	 <i class="glyphicon glyphicon-folder-open"></i>  打印
	        	</button>
			</shiro:hasPermission>			
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
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
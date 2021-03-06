<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>领料单查询</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="sfcMaterialOrderQueryList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">领料单列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="sfcMaterialOrder" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="单据号：">单据号：</label>
				<form:input path="materialOrder" htmlEscape="false" maxlength="20"  class=" form-control"/>
			</div>

			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="单据类型：">单据类型：</label>
				<form:select path="receiveType" class="form-control"  >
					<form:option value="" label=""/>
					<form:option value="C" label="系统生成"/>
					<form:option value="M" label="手工录入"/>
				</form:select>
			</div>
<div class="row">
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工作中心编码：">工作中心编码：</label>
				<sys:gridselect-modify-code  url="${ctx}/workcenter/workCenter/data" id="operCode" name="operCode" value="${sfcMaterialOrder.operCode}" labelName=".centerCode" labelValue="${sfcMaterialOrder.operCode}"
											 title="选择工作中心" cssClass="form-control required" fieldLabels="工作中心编号|工作中心名称" fieldKeys="centerCode|centerName" searchLabels="工作中心编号|工作中心名称" searchKeys="centerCode|centerName"
											 extraField="operName:centerName;operCodeId:operCode"   targetField="" targetId=""></sys:gridselect-modify-code>
			<%--<form:input path="operCode" htmlEscape="false" maxlength="20"  class=" form-control"/>--%>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工作中心名称：">工作中心名称：</label>
				<form:input path="operName" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>
</div>
			<%--<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="领料单类型：">领料单类型：</label>
				<form:select path="billType" class="form-control " >
					&lt;%&ndash;<form:option value="" label=""/>&ndash;%&gt;
					<form:options items="${fns:getDictList('sfc_material_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>--%>

			<%--<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="领料人代码：">领料人代码：</label>
				<form:input path="responser" htmlEscape="false" maxlength="10"  class=" form-control"/>
			</div>--%>
<div class="row">
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="领料人姓名：">领料人姓名：</label>
				<form:input path="respName" htmlEscape="false" maxlength="20"  class=" form-control"/>
			</div>
<%--			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="库房代码：">库房代码：</label>
				<form:input path="wareId" htmlEscape="false" maxlength="10"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="库房名称：">库房名称：</label>
				<form:input path="wareName" htmlEscape="false" maxlength="20"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="核算期：">核算期：</label>
				<form:input path="periodId" htmlEscape="false" maxlength="10"  class=" form-control"/>
			</div>--%>
			 <%--<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="部门代码：">部门代码：</label>
				<form:input path="shopId" htmlEscape="false" maxlength="10"  class=" form-control"/>
			</div>--%>
		<%--	 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="部门名称：">部门名称：</label>
				<form:input path="shopName" htmlEscape="false" maxlength="50"  class=" form-control"/>
			</div>--%>

			 <%--<div class="col-xs-12 col-sm-6 col-md-4">
				<div class="form-group">
					<label class="label-item single-overflow pull-left" title="制单日期：">&nbsp;制单日期：</label>
					<div class="col-xs-12">
						<div class='input-group date' id='editDate' >
			                   <input type='text'  name="editDate" class="form-control"  />
			                   <span class="input-group-addon">
			                       <span class="glyphicon glyphicon-calendar"></span>
			                   </span>
			            </div>	
					</div>
				</div>
			</div>--%>

			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="领料单类型：">领料单类型：</label>
				<form:select path="billType" class="form-control " >
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sfc_material_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>

			<%--<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="是否缺料：">是否缺料：</label>
				<form:select path="isLack" class="form-control " >
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sfc_is_lack')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>--%>

<%--

			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="库管员编码：">库管员编码：</label>
				<form:input path="wareEmpid" htmlEscape="false" maxlength="10"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="库管员名称：">库管员名称：</label>
				<form:input path="wareEmpname" htmlEscape="false" maxlength="20"  class=" form-control"/>
			</div>
--%>

			<div class="col-xs-12 col-sm-6 col-md-4">
				<div class="form-group">
					<label class="label-item single-overflow pull-left" title="业务日期：">&nbsp;领料日期：</label>
					<div class="col-xs-12">
						<div class="col-xs-12 col-sm-5">
							<div class='input-group date' id='beginOperDate' style="left: -10px;" >
								<input type='text'  name="beginOperDate" class="form-control"  />
								<span class="input-group-addon">
									   <span class="glyphicon glyphicon-calendar"></span>
								   </span>
							</div>
						</div>
						<div class="col-xs-12 col-sm-1">
							~
						</div>
						<div class="col-xs-12 col-sm-5">
							<div class='input-group date' id='endOperDate' style="left: -10px;" >
								<input type='text'  name="endOperDate" class="form-control" />
								<span class="input-group-addon">
									   <span class="glyphicon glyphicon-calendar"></span>
								   </span>
							</div>
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

				<%--<button id="edit" class="btn btn-success" onclick="edit()">
					<i class="glyphicon glyphicon-warning-sign"></i> 缺料检查
				</button>--%>

				<a id="showDetail" class="btn btn-success" onclick="print()">
					<i class="glyphicon glyphicon-folder-open"></i>  打印
				</a>

	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>


		    </div>
		
	<!-- 表格 -->
	<table id="sfcMaterialOrderTable"   data-toolbar="#toolbar"></table>

		<iframe style="display:none" id="printIframe"></iframe>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="materialorder:sfcMaterialOrder:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="materialorder:sfcMaterialOrder:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
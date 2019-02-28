<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工资表核算表</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="accountList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">工资表核算表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="personWage" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="供货单位：">内部订单号：</label>
				<input id="orderId" name="orderId" htmlEscape="false" maxlength="10"  class=" form-control"/>
			</div>
                <div class="col-xs-12 col-sm-6 col-md-6">
                    <div class="form-group">
                        <label class="label-item single-overflow pull-left" title="日期：">&nbsp;日期：</label>
                        <div class="col-xs-12">
                            <div class="col-xs-12 col-sm-6 col-md-5">
                                <div class='input-group date' id='beginDate' style="left: 0px;" >
                                    <input type='text'  name="beginDate" class="form-control"  />
                                    <span class="input-group-addon">
													   <span class="glyphicon glyphicon-calendar"></span>
												   </span>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-sm-1">
                                ~
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-5">
                                <div class='input-group date' id='endDate' style="left: -10px;" >
                                    <input type='text'  name="endDate" class="form-control" />
                                    <span class="input-group-addon">
													   <span class="glyphicon glyphicon-calendar"></span>
												   </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="入库单号：">车间：</label>
				<input id="officeName" name="officeName" htmlEscape="false" maxlength="30"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="入库类别：">工位号：</label>
				<input id="workCode.workProcedureId" name="workCode.workProcedureId" htmlEscape="false" maxlength="30"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="所入仓库：">姓名：</label>
				<input id="personCode.name" name="personCode.name" htmlEscape="false" maxlength="30"  class=" form-control"/>
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
			<%-- <shiro:hasPermission name="accounttype:accountType:add">
				<a id="add" class="btn btn-primary" href="${ctx}/accounttype/accountType/form" title="企业类型表单"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="accounttype:accountType:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="accounttype:accountType:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission> --%>
			<%-- <shiro:hasPermission name="accounttype:accountType:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/accounttype/accountType/import" method="post" enctype="multipart/form-data"
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
	<table id="accountTypeTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="accounttype:accountType:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="accounttype:accountType:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
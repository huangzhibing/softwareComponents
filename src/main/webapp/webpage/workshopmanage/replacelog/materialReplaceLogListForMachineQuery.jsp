<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>换件记录管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="materialReplaceLogListForMachineQuery.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">	<a class="panelButton" href="${ctx}//mserialnoprint/mSerialNoPrint/machinelist"><i class="ti-angle-left"></i> 返回</a><span style="padding-left: 40%;">换件记录</span></h3>

	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="materialReplaceLog" class="form form-horizontal well clearfix">

				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="加工路线单号：">机器序列号：</label>
					<form:input path="serialno" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<%--  <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="加工路线单号：">加工路线单号：</label>
                    <form:input path="routinebillno" htmlEscape="false" maxlength="64"  classrm-control"/>
                </div>
                <%-- <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="单件序号：">单件序号：</label>
                    <form:input path="seqno" htmlEscape="false" maxlength="64"  class=" form-control"/>
                </div>
                 <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="工艺编码：">工艺编码：</label>
                    <form:input path="routingcode" htmlEscape="false" maxlength="64"  class=" form-control"/>
                </div>
                 <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="物料编码：">物料编码：</label>
                    <form:input path="itemcode" htmlEscape="false" maxlength="64"  class=" form-control"/>
                </div>--%>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料二维码：">物料二维码：</label>
				<form:input path="itemsn" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<%-- <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工作中心代码：">工作中心代码：</label>
				<form:input path="centercode" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工艺名称：">工艺名称：</label>
				<form:input path="routingname" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>--%>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
				<form:input path="itemname" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <%--<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="数量：">数量：</label>
				<form:input path="assembleqty" htmlEscape="false" maxlength="11"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="已安装数量：">已安装数量：</label>
				<form:input path="finishedqty" htmlEscape="false" maxlength="11"  class=" form-control"/>
			</div>--%>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工作中心名称：">工作中心名称：</label>
				<form:input path="centername" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<%-- <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="装配人员ID：">装配人员ID：</label>
				<form:input path="assemblepersonid" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>--%>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="装配人员名字：">装配人员：</label>
				<form:input path="assemblepersonname" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="装配时间：">&nbsp;装配时间：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginAssembletime' style="left: -10px;" >
					                   <input type='text'  name="beginAssembletime" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endAssembletime' style="left: -10px;" >
					                   <input type='text'  name="endAssembletime" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			<%-- <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="实作班组：">实作班组：</label>
				<form:input path="actualteamcode" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="是否有品质异常问题：">是否有品质异常问题：</label>
				<form:input path="hasqualitypro" htmlEscape="false" maxlength="1"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="异常类型：">异常类型：</label>
				<form:input path="qualityproblem" htmlEscape="false" maxlength="255"  class=" form-control"/>
			</div>--%>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="原表中的ID：">原表中的ID：</label>
				<form:input path="originalid" htmlEscape="false" maxlength="64"  class=" form-control"/>
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
	<%--<div id="toolbar">
			&lt;%&ndash;<shiro:hasPermission name="replacelog:materialReplaceLog:add">
				<a id="add" class="btn btn-primary" href="${ctx}/replacelog/materialReplaceLog/form" title="换件记录"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="replacelog:materialReplaceLog:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="replacelog:materialReplaceLog:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="replacelog:materialReplaceLog:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/replacelog/materialReplaceLog/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission>&ndash;%&gt;
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>--%>
		
	<!-- 表格 -->
	<table id="materialReplaceLogTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="replacelog:materialReplaceLog:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="replacelog:materialReplaceLog:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>机器序列号打印管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="mSerialNoPrintList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">综合查询</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body">
		<div class="accordion-inner">
			<%--@elvariable id="mSerialNoPrint" type="com.hqu.modules.workshopmanage.mserialnoprint.entity.MSerialNoPrint"--%>
			<form:form id="searchForm" modelAttribute="mSerialNoPrint" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="机器序列号：">机器序列号：</label>
				<form:input path="mserialno" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="二维码：">整机二维码：</label>
				<form:input path="objsn" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>

			<div class="col-xs-12 col-sm-6 col-md-6">
					<div class="form-group">
						<label class="label-item single-overflow pull-left col-md-6" title="生产日期：">&nbsp;生产日期：</label>
						<label class="label-item single-overflow " title="至：">&nbsp;至：</label>
						<div class="col-xs-12">
							<div class="col-xs-12 col-sm-6">
								<div class='input-group date' id='beginProdDate'  >
									<input type='text'  name="beginProdDate" class="form-control"  />
									<span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
								</div>
							</div>

							<div class="col-xs-12 col-sm-6">
								<div class='input-group date' id='endProdDate'>
									<input type='text'  name="endProdDate" class="form-control" />
									<span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
								</div>
							</div>
						</div>
					</div>
				</div>

			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="主生产计划号：">主生产计划号：</label>
				<form:input path="mpsplanid" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>



			<%-- <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="车间作业计划号：">车间作业计划号：</label>
				<form:input path="processbillno" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="车间作业计划分批号：">车间作业计划分批号：</label>
				<form:input path="batchno" htmlEscape="false" maxlength="11"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="加工路线单号：">加工路线单号：</label>
				<form:input path="routinebillno" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>--%>
			 <%--<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工艺编码：">工艺编码：</label>
				<form:input path="routingcode" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="加工路线单单件序号：">加工路线单单件序号：</label>
				<form:input path="seqno" htmlEscape="false" maxlength="11"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="已分配标志：">已分配标志：</label>
				<form:input path="isassigned" htmlEscape="false" maxlength="1"  class=" form-control"/>
			</div>--%>
			<%-- <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="产品编码：">产品编码：</label>
				<form:input path="prodcode" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>--%>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				 <div class="input-group" style="width: 100%">
					 <label class="label-item single-overflow pull-left" title="产品名称：">产品名称：</label>

					<form:input path="prodname" htmlEscape="false" maxlength="64"  class=" form-control"/>
					 <span class="input-group-btn">
										 <button type="button"  id="prodButton" class="btn btn-primary" style="top: 11px;"><i class="fa fa-search"></i></button>
										 <button type="button" id="prodDelButton" class="close" data-dismiss="alert" style="position: absolute; top: 30px; right: 53px; z-index: 999; display: block;">×</button>
					</span>
				 </div>
			</div>

				<div class="col-xs-12 col-sm-6 col-md-3">
					<div class="input-group" style="width: 100%">
						<label class="label-item single-overflow pull-left" title="生产班组：">生产班组：</label>

						<sys:gridselect-label1 url="${ctx}/team/team/data" id="teamCode"
											   labelName="teamCode" labelValue=""  labelNameForValue=".teamCode"
											   title="选择计划班组" cssClass="form-control"
											   fieldLabels="班组代码|班组名称" fieldKeys="teamCode|teamName" searchLabels="班组代码|班组名称"
											   searchKeys="teamCode|teamName" allowInput="false" targetField="" targetId=""></sys:gridselect-label1>


						<%--<form:input path="teamCode" htmlEscape="false" maxlength="64"  class=" form-control"/>
						<span class="input-group-btn">
										 <button type="button"  id="teamButton" class="btn btn-primary" style="top: 11px;"><i class="fa fa-search"></i></button>
										 <button type="button" id="teamDelButton" class="close" data-dismiss="alert" style="position: absolute; top: 30px; right: 53px; z-index: 999; display: block;">×</button>
					</span>--%>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6 col-md-3">
					<div class="input-group" style="width: 100%">
						<label class="label-item single-overflow pull-left" title="负责人：">负责人：</label>
						<sys:userselect id="personIncharge" name="personInchargeId"  labelName="personIncharge"
										cssClass="form-control required"/>
						<%--<form:input path="personIncharge" htmlEscape="false" maxlength="64"  class=" form-control"/>--%>

					</div>
				</div>

				<div class="col-xs-12 col-sm-6 col-md-3">
					<div class="input-group" style="width: 100%">
						<label class="label-item single-overflow pull-left" title="完工通知单：">装配完工单号：</label>

						<form:input path="invcheckNo" htmlEscape="false" maxlength="64"  class=" form-control"/>
						<%--<span class="input-group-btn">
										 <button type="button"  id="invcheckButton" class="btn btn-primary" style="top: 11px;"><i class="fa fa-search"></i></button>
										 <button type="button" id="invcheckDelButton" class="close" data-dismiss="alert" style="position: absolute; top: 30px; right: 53px; z-index: 999; display: block;">×</button>
					</span>--%>
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

		<button id="bt1" class="btn btn-success" disabled onclick="edit1()">
			<i class="glyphicon glyphicon-edit"></i> 生产记录详情
		</button>
		<button id="bt2" class="btn btn-success" disabled onclick="edit2()">
			<i class="glyphicon glyphicon-edit"></i> 换件记录详情
		</button>
		<button id="bt3" class="btn btn-success" disabled onclick="edit3()">
			<i class="glyphicon glyphicon-edit"></i> 品质记录详情
		</button>
		<button id="bt4" class="btn btn-success" disabled onclick="edit4()">
			<i class="glyphicon glyphicon-edit"></i> 检验记录详情
		</button>
			<%--<shiro:hasPermission name="mserialnoprint:mSerialNoPrint:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="mserialnoprint:mSerialNoPrint:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/mserialnoprint/mSerialNoPrint/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission>--%>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="mSerialNoPrintTable1"   data-toolbar="#toolbar"></table>



		<div  id="printContent">

		</div>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="mserialnoprint:mSerialNoPrint:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="mserialnoprint:mSerialNoPrint:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
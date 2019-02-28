<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购需求管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="applyMainListCheck.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">采购需求审核列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="applyMain" class="form form-horizontal well clearfix">
			
			
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="单据编号：">单据编号：</label>
				<form:input path="billNum" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			
			
			
			
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料代码">物料代码：</label>
				<sys:gridselect-pursup url="${ctx}/applymain/applyMain/itemData" id="item" name="item.id" value="${applyMain.item.id}" labelName="item.code" labelValue="${applyMain.item.code}"
							 title="选择需求物料代码" 
							 targetId="itemName|itemSpecmodel" targetField="name|specModel"
							 cssClass="form-control required" fieldLabels="物料代码|物料名称|规格型号" fieldKeys="code|name|specModel" searchLabels="物料代码|物料名称|规格型号" searchKeys="code|name|specModel" ></sys:gridselect-pursup>
			</div>
			
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
				<form:input path="itemName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="规格型号：">规格型号：</label>
				<form:input path="itemSpecmodel" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="制定日期：">&nbsp;制定日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginMakeDate' style="left: -10px;" >
					                   <input type='text'  name="beginMakeDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endMakeDate' style="left: -10px;" >
					                   <input type='text'  name="endMakeDate" class="form-control" />
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
					<label class="label-item single-overflow pull-left" title="业务日期：">&nbsp;业务日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginApplyDate' style="left: -10px;" >
					                   <input type='text'  name="beginApplyDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endApplyDate' style="left: -10px;" >
					                   <input type='text'  name="endApplyDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="需求类别编码：">需求类别编码：</label>
				<sys:gridselect-modify url="${ctx}/applytype/applyType/data" id="applyType" name="applyType.id" value="${applyMain.applyType.id}" labelName="applyType.applytypeid" labelValue="${applyMain.applyType.applytypeid}"
							 title="选择需求类别编码" 
							 targetId="applyName" targetField="applytypename" 
							 cssClass="form-control required" fieldLabels="需求类别编码|需求类别名称" fieldKeys="applytypeid|applytypename" searchLabels="需求类别编码|需求类别名称" searchKeys="applytypeid|applytypename" ></sys:gridselect-modify>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="需求类别名称：">需求类别名称：</label>
				<form:input path="applyName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<!-- 
			<sys:treeselect id="office" name="office.id" value="${applyMain.office.id}" labelName="office.code" labelValue="${applyMain.office.code}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
			
			
			 -->
			
			
			
			
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="需求部门编码：">需求部门编码：</label>
				<sys:gridselect-modify url="${ctx}/applymain/applyMain/officeData" id="office" name="office.id" value="${applyMain.office.id}" labelName="office.code" labelValue="${applyMain.office.code}"
							 title="部门" 
							 targetId="applyDept" targetField="name" 
							 cssClass="form-control required" fieldLabels="需求部门编码|需求部门名称" fieldKeys="code|name" searchLabels="需求部门编码|需求部门名称" searchKeys="code|name" ></sys:gridselect-modify>
				
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="需求部门名称：">需求部门名称：</label>
				<form:input path="applyDept" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			
			
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="需求人员编码：">需求人员编码：</label>
				<sys:gridselect-modify url="${ctx}/applymain/applyMain/userData" id="user" name="user.id" value="${applyMain.user.id}" labelName="user.no" labelValue="${applyMain.user.no}"
						 title="人员" 
						 targetId="makeEmpname" targetField="name" 
						 cssClass="form-control required" fieldLabels="需求人员编码|需求人员名称" fieldKeys="no|name" searchLabels="需求人员编码|需求人员名称" searchKeys="no|name" ></sys:gridselect-modify>
		
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="需求人员名称：">需求人员名称：</label>
				<form:input path="makeEmpname" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="登陆人所在部门：">登陆人所在部门：</label>
				<form:input path="userDeptCode" htmlEscape="false" maxlength="64"  class=" form-control"/>
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
	
	<script type="text/javascript">
	
	
	
	
	
	</script>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<shiro:hasPermission name="applymain:applyMain:add">
				<a id="add" class="btn btn-primary" href="${ctx}/applymain/applyMain/form" title="采购需求"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="applymain:applyMain:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="applymain:applyMain:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteA()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			
			<shiro:hasPermission name="applymain:applyMain:edit">
			    <button id="detail" class="btn btn-primary"  disabled onclick="detail1()">
	            	<i class="fa fa-folder-open-o"></i> 详情
	        	</button>
			</shiro:hasPermission>
			
				
			
			
			<shiro:hasPermission name="applymain:applyMain:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/applymain/applyMain/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="applyMainTableCheck"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="applymain:applyMain:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="applymain:applyMain:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
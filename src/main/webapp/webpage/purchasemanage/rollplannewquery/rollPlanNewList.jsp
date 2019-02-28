<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>滚动计划管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="rollPlanNewList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">滚动计划列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<%--@elvariable id="rollPlanNew" type="RollPlanNew"--%>
			<form:form id="searchForm" modelAttribute="rollPlanNew" class="form form-horizontal well clearfix">

		<div class="row">
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="单据编号：">单据编号：</label>
				<form:input path="billNum" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>


			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="物料编号：">物料编号：</label>
				<sys:gridselect-pursup url="${ctx}/item/item/data2" id="itemCode" name="itemCode.id" value="${rollPlanNew.itemCode.id}" labelName="itemCode.code" labelValue="${rollPlanNew.itemCode.code}"
					title="选择物料编号" cssClass="form-control required" fieldLabels="物料编号|物料名称" fieldKeys="code|name" searchLabels="物料编号|物料名称" searchKeys="code|name"  targetId="itemName|itemSpecmodel" targetField="name|specModel" ></sys:gridselect-pursup>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
				<form:input path="itemName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="物料规格：">规格型号：</label>
				<form:input path="itemSpecmodel" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
		</div>

		<div class="row">
			 <div class="col-xs-12 col-sm-6 col-md-6">
				 <div class="riqi">
					<label class="label-item single-overflow pull-left col-md-6 col-xs-12" title="需求日期：">需求日期：</label>
					 <label class="label-item  single-overflow col-md-6" title="至：">至:</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-6">
					        	  <div class='input-group date' id='beginRequestDate' style="left: -10px;" >
					                   <input type='text'  name="beginRequestDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-6">
					          	<div class='input-group date' id='endRequestDate' >
					                   <input type='text'  name="endRequestDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>

			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="需求部门编码：">需求部门编码：</label>
				<sys:treeselect-modify targetId="applyDept" targetField="code" id="applyDeptId" name="applyDeptId.id" value="${rollPlanNew.applyDeptId.id}" labelName="applyDeptId.code" labelValue="${rollPlanNew.applyDeptId.code}"
									   title="部门" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="需求部门名称：">需求部门名称：</label>
				<form:input path="applyDept" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>

	<%--		<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left">物料类型编码：</label>
				<sys:treeselect-modify targetId="itemClassName" targetField="classId" id="itemClassCode" name="itemClassCode.id" value="${rollPlanNew.itemClassCode.id}" labelName="itemClassCode.classId" labelValue="${rollPlanNew.itemClassCode.classId}"
								title="物料类型编码" url="/item/itemClassNew/treeData"  cssClass="form-control required" allowClear="true" />
			</div>

			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="物料类型名称：">物料类型名称：</label>
				<form:input path="itemClassName" htmlEscape="false"   maxlength="64" class="form-control"/>
			</div>--%>
		</div>

		<div class="row">
			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="需求类别编码：">需求类别编码：</label>
				<sys:gridselect-pursup url="${ctx}/applytype/applyType/data" id="applyTypeId" name="applyTypeId.id" value="${rollPlanNew.applyTypeId.id}" labelName="applyTypeId.applytypeid" labelValue="${rollPlanNew.applyTypeId.applytypeid}"
								title="选择需求类别编码" cssClass="form-control required" fieldLabels="需求类别编码|需求类别名称" fieldKeys="applytypeid|applytypename" searchLabels="需求类别编码|需求类别名称" searchKeys="applytypeid|applytypename" targetId="applyTypeName" targetField="applytypename" ></sys:gridselect-pursup>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="需求类别名称：">需求类别名称：</label>
				<form:input path="applyTypeName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>

			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="需求来源：">需求来源：</label>
				<form:select cssClass="form-control "   path="sourseFlag">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('rollplan_source_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>

			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="需求状态：">需求状态：</label>
				<form:select cssClass="form-control "   path="opFlag">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('rollplan_op_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>

<%--
		<div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="采购开始日期：">&nbsp;采购开始日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginPurStartDate' style="left: -10px;" >
					                   <input type='text'  name="beginPurStartDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endPurStartDate' style="left: -10px;" >
					                   <input type='text'  name="endPurStartDate" class="form-control" />
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
					<label class="label-item single-overflow pull-left" title="制单日期：">&nbsp;制单日期：</label>
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
			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="单据类型：">单据类型：</label>
				<form:input path="billType" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="制单人代码：">制单人代码：</label>
				<sys:userselect id="makeEmpid" name="makeEmpid.id" value="${rollPlanNew.makeEmpid.id}" labelName="makeEmpid.no" labelValue="${rollPlanNew.makeEmpid.no}"
							    cssClass="form-control "/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="制单人名称：">制单人名称：</label>
				<form:input path="makeEmpname" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="计划到货日期：">&nbsp;计划到货日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginPurArriveDate' style="left: -10px;" >
					                   <input type='text'  name="beginPurArriveDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endPurArriveDate' style="left: -10px;" >
					                   <input type='text'  name="endPurArriveDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>--%>


		</div>

		<div class="row">

			 <div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="计划号：">计划编号：</label>
				<form:input path="planNum" htmlEscape="false" maxlength="64"  class=" form-control"/>
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
		<%--	<shiro:hasPermission name="rollplannewquery:rollPlanNew:add">
				<a id="add" class="btn btn-primary" href="${ctx}/rollplannewquery/rollPlanNew/form" title="滚动计划"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
		<shiro:hasPermission name="rollplannewquery:rollPlanNew:edit">
            <button id="edit" class="btn btn-success" disabled onclick="edit()">
                <i class="glyphicon glyphicon-edit"></i> 修改
            </button>
        </shiro:hasPermission>
        <shiro:hasPermission name="rollplannewquery:rollPlanNew:del">
            <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
                <i class="glyphicon glyphicon-remove"></i> 删除
            </button>
        </shiro:hasPermission>
        <shiro:hasPermission name="rollplannewquery:rollPlanNew:import">
            <button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
            <div id="importBox" class="hide">
                    <form id="importForm" action="${ctx}/rollplannewquery/rollPlanNew/import" method="post" enctype="multipart/form-data"
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
	<table id="rollPlanNewTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
<%--    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="rollplannew:rollplannewquery:rollPlanNew:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="rollplannew:rollplannewquery:rollPlanNew:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  --%>
	</div>
	</div>
	</div>
</body>
</html>
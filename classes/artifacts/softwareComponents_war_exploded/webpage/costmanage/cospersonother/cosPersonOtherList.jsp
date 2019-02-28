<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>
		<c:if test="${empty flag}">其它工资单据录入</c:if>
		<c:if test="${flag=='jihe'}">其它工资单据稽核</c:if>
		<c:if test="${flag=='chaxun'}">其它工资单据查询</c:if>
		<c:if test="${flag=='chexiao'}">其它工资单据撤销</c:if>
	</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="cosPersonOtherList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">
			<c:if test="${empty flag}">其它工资单据录入列表</c:if>
			<c:if test="${flag=='jihe'}">其它工资单据稽核列表</c:if>
			<c:if test="${flag=='chaxun'}">其它工资单据查询列表</c:if>
			<c:if test="${flag=='chexiao'}">其它工资单据撤销列表</c:if>
		</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="cosPersonOther" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="核算期：">核算期：</label>
				<form:input path="periodID" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="人员代码：">人员代码：</label>
				<sys:userselect-item  targetId="personName" id="person" name="personCode.id" value="${cosPersonOther.personCode.id}" labelName="personCode.no" labelValue="${cosPersonOther.personCode.no}"
									   cssClass="form-control "/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="人员名称：">人员名称：</label>
				<form:input readOnly="readOnly" path="personName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="科目编码：">科目编码：</label>
				 <sys:gridselect-item title="选择科目" url="${ctx}/cositem/cosItem/data"  extraField="itemName:itemName" id="itemId" name="itemId.id" value="${cosPersonOther.itemId.id}" labelName="itemId.itemCode" labelValue="${cosPersonOther.itemId.itemCode}"
									  cssClass="form-control" fieldKeys="itemCode|itemName" fieldLabels="科目编码|科目名称" searchKeys="itemCode|itemName" searchLabels="科目编码|科目名称"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="科目名称：">科目名称：</label>
				<form:input readonly="true" path="itemName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工资：">工资：</label>
				<form:input path="itemSum" htmlEscape="false" maxlength="64"  class=" form-control"/>
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
			<c:if test="${empty flag}">
				<shiro:hasPermission name="cospersonother:cosPersonOther:add">
					<a id="add" class="btn btn-primary" href="${ctx}/cospersonother/cosPersonOther/form" title="其它工资单据录入"><i class="glyphicon glyphicon-plus"></i> 新建</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="cospersonother:cosPersonOther:edit">
					<button id="edit" class="btn btn-success" disabled onclick="edit('')">
						<i class="glyphicon glyphicon-edit"></i> 修改
					</button>
				</shiro:hasPermission>
				<shiro:hasPermission name="cospersonother:cosPersonOther:del">
					<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
						<i class="glyphicon glyphicon-remove"></i> 删除
					</button>
				</shiro:hasPermission>
				<shiro:hasPermission name="cospersonother:cosPersonOther:import">
					<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
					<div id="importBox" class="hide">
							<form id="importForm" action="${ctx}/cospersonother/cosPersonOther/import" method="post" enctype="multipart/form-data"
								 style="padding-left:20px;text-align:center;" ><br/>
								<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　


							</form>
					</div>
				</shiro:hasPermission>
			</c:if>
			<c:if test="${flag=='jihe'}">
				<button id="edit" class="btn btn-success" disabled onclick="edit('jihe')">
					<i class="glyphicon glyphicon-edit"></i> 稽核
				</button>
			</c:if>
			<c:if test="${flag=='chexiao'}">
				<button id="edit" class="btn btn-success" disabled onclick="edit('chexiao')">
					<i class="glyphicon glyphicon-edit"></i> 撤销
				</button>
			</c:if>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="cosPersonOtherTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="cospersonother:cosPersonOther:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="cospersonother:cosPersonOther:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>
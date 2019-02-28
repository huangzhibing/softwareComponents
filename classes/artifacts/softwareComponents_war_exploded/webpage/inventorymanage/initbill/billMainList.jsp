<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>单据管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="billMainList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">单据列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="billMain" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="单据号：">单据号：</label>
				<form:input path="billNum" htmlEscape="false" maxlength="20"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="出入库日期：">&nbsp;出入库日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginBillDate' style="left: -10px;" >
					                   <input type='text'  name="beginBillDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endBillDate' style="left: -10px;" >
					                   <input type='text'  name="endBillDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="部门编码：">部门编码：</label>
				<sys:treeselect-modify targetId="deptd" targetField="code" id="dept" name="dept.id" value="${billMain.dept.id}" labelName="dept.code" labelValue="${billMain.dept.code}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
			</div>
			
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="部门名称：">部门名称：</label>
				<form:input readOnly="readOnly" id="deptd" path="deptName" htmlEscape="false" maxlength="30"  class=" form-control"/>
			</div>
			<div style="display:none;">
				<form:input  id="code" path="dept.code" htmlEscape="false"    class="form-control required"/>
				</div> 
			
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="库房号：">库房号：</label>
				<sys:gridselect-modify extraField="wareIDd:wareID" targetId="wared" targetField="wareName"  url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${billMain.ware.id}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
							 title="选择库房号" cssClass="form-control required" fieldLabels="库房号|库房名称" fieldKeys="wareID|wareName" searchLabels="库房号|库房名称" searchKeys="wareID|wareName" ></sys:gridselect-modify>
			</div>
			<input id="wareIDd" name="ware.wareID" style="display:none"/>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="库房名称：">库房名称：</label>
				<form:input readOnly="readOnly" id="wared" path="wareName" htmlEscape="false" maxlength="100"  class=" form-control"/>
			</div>
			<%--  <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="经办人：">经办人：</label>
				<sys:userselect id="billPerson" name="billPerson.id" value="${billMain.billPerson.id}" labelName="billPerson.no" labelValue="${billMain.billPerson.no}"
							    cssClass="form-control "/>
			</div> --%>
			  <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left">制单人编码：</label>
						<form:input placeHolder="点击获取制单人" onclick="Test()" readOnly="true"  id="billPersonId"  value="${billMain.billEmp.no}" path="billEmp.no"
							    cssClass="form-control required"/>
				</div> 
				<form:input path="billEmp.id" style="display:none;"  id="billPerson" name="billEmp.id" value="${billMain.billEmp.id}" labelName="billEmp.no" labelValue="${billMain.billEmp.no}"
							    cssClass="form-control required"/>
				 <div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left">制单人名字：</label>
						<form:input  readonly="true" id="billPersond" path="billEmp.name" htmlEscape="false"    class="form-control required"/>
				</div> 
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="库管员代码：">库管员代码：</label>
				<sys:gridselect-modify labelField="user.no" targetId="wareEmpname" targetField="empName" url="${ctx}/employee/employee/data" id="wareEmp" name="wareEmp.id" value="${billMain.wareEmp.id}" labelName="wareEmp.user.no" labelValue="${billMain.wareEmp.user.no}"
							 title="选择库管员代码" cssClass="form-control required" fieldLabels="库管员代码|库管员名称" fieldKeys="user.no|empName" searchLabels="库管员代码|库管员名称" searchKeys="user.no|empName" ></sys:gridselect-modify>
					</div>
					<input id="wareEmpd" name="wareEmp.user.no" style="display:none;" />
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="库管员名称：">库管员名称：</label>
						<form:input readonly="true" path="wareEmpname" htmlEscape="false"  value="${billMain.wareEmp.empName}"   class="form-control required"/>
			</div>
			
			 <%-- <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="用途/车间班组代码：">用途代码：</label>
				<sys:gridselect-modify targetId="used" targetField="useDesc" url="${ctx}/use/use/data" id="invuse" name="invuse.id" value="${billMain.invuse.id}" labelName="invuse.useId" labelValue="${billMain.invuse.useId}"
							 title="选择用途/车间班组代码" cssClass="form-control required" fieldLabels="用途/车间班组代码|用途/车间班组名称" fieldKeys="useId|useDesc" searchLabels="用途/车间班组代码|用途/车间班组名称" searchKeys="useId|useDesc" ></sys:gridselect-modify>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="用途/车间班组名称：">用途名称：</label>
				<form:input readOnly="readOnly" id="used" path="useName" htmlEscape="false" maxlength="40"  class=" form-control"/>
			</div> --%>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="核算期：">核算期：</label>
				<form:input  path="period.periodId" htmlEscape="false" maxlength="40"  class=" form-control"/>
			</div>
			<!-- id还有问题 -->
					<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="物料代码：">物料代码：</label>
						<sys:gridselect-again
							targetId="" targetField="" 
							extraField="itemNamed:item.name;itemSpec:item.specModel;" 
							url="${ctx}/invaccount/invAccount/data" id="item" name="itemCode" value="" labelName="item.code" labelValue=""
							 title="选择物料代码" cssClass="form-control required " fieldLabels="物料代码|物料名称|物料规格|数量" fieldKeys="item.code|item.name|item.specModel|realQty" searchLabels="物料代码|物料名称|货区号|货区名|货位号|货位名" searchKeys="item.code|item.name|bin.binId|bin.binDesc|loc.locId|locDesc" ></sys:gridselect-again>
					</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
				<form:input readOnly="true" id="itemNamed"  path="" htmlEscape="false" maxlength="40"  class=" form-control"/>
			</div>	
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="物料规格：">物料规格：</label>
				<form:input readOnly="true" id="itemSpec"  path="" htmlEscape="false" maxlength="40"  class=" form-control"/>
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
			<shiro:hasPermission name="billmain:billMain:add">
				<a id="add" class="btn btn-primary" href="${ctx}/initbill/initBill/form" title="单据"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<%-- <shiro:hasPermission name="billmain:billMain:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission> --%>
			<shiro:hasPermission name="billmain:billMain:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<%-- <shiro:hasPermission name="billmain:billMain:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/billmain/billMain/import" method="post" enctype="multipart/form-data"
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
	<table id="billMainTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="billmain:billMain:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="billmain:billMain:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
	<script type="text/javascript">
	function Test(){
		top.layer.open({
		    type: 2, 
		    area: ['900px', '560px'],
		    title:"选择用户",
		    auto:true,
		    maxmin: true, //开启最大化最小化按钮
		    content: ctx+"/sys/user/userSelect?isMultiSelect="+false,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		    	var ids = layero.find("iframe")[0].contentWindow.getIdSelections();
		    	var names = layero.find("iframe")[0].contentWindow.getNameSelections();
		    	var loginNames = layero.find("iframe")[0].contentWindow.getLoginNameSelections();
		    	var nos = layero.find("iframe")[0].contentWindow.getNoSelections();
		        console.log(nos)
		    	if(ids.length ==0){
		    		
					jp.warning("请选择至少一个用户!");
					return;
				}
		        console.log(ids+" "+nos);
		    	// 执行保存
		    	$("#billPerson").val(ids.join(",").replace(/u_/ig,""));
		    	$("#billPersond").val(names.join(","));
		    	$("#billPersonId").val(nos.join(","));
		    	//yesFuc(ids.join(","), names.join(","),nos.join(","),loginNames.join(","));
		    	
		    	top.layer.close(index);
			  },
			  cancel: function(index){ 
				  //取消默认为空，如需要请自行扩展。
				  top.layer.close(index);
		        }
		}); 
	}

	</script>
</body>
</html>
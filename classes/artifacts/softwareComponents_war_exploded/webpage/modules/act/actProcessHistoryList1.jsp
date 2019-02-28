<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>已结束的流程</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
		});
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>已结束的流程 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm"  action="${ctx}/act/process/historyList/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<label>流程实例ID：</label><input type="text" id="procInsId" name="procInsId" value="${procInsId}" class="form-control"/>
			<label>流程定义Key：</label><input type="text" id="procDefKey" name="procDefKey" value="${procDefKey}" class="form-control"/>
		</div>	
	</form:form>
	<br/>
	</div>
	</div>
	
		<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	
		<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th>流程发起人</th>
				<th>执行ID</th>
				<th>流程实例ID</th>
				<th>流程定义ID</th>
				<th>流程启动时间</th>
				<th>流程结束时间</th>
				<th>流程状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="procIns">
				<tr>
					<td>${procIns.startUserId}</td>
					<td>${procIns.id}</td>
					<td>${procIns.processInstanceId}</td>
					<td>${procIns.processDefinitionId}</td>
					<td><fmt:formatDate value="${procIns.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><fmt:formatDate value="${procIns.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><c:if test="${procIns.deleteReason != null}">[流程作废]原因：${procIns.deleteReason}</c:if>  <c:if test="${procIns.deleteReason == null}">[正常结束]</c:if></td>
					<td>
						<a  href="javascript:openDialog('查看流程历史','${ctx}/act/task/trace/photo/${procIns.processDefinitionId}/${procIns.id}','1000px', '600px')">历史</a>
						
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
			<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>
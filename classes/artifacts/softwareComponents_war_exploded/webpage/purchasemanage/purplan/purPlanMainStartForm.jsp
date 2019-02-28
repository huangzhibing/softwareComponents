<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>采购计划流程启动</title>
<meta name="decorator" content="ani" />


</head>
<body>

<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">
				<a class="panelButton" onclick="history.go(-1);"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">

			<div class="col-lg-3 col-md-2 col-sm-2 col-xs-2"></div>
			<div class="col-lg-3 col-md-3 col-sm-3" >
				<form:form id="inputForm" modelAttribute="purPlanMain" action="${ctx}/purplan/purPlanMain/form" method="post" class="form-horizontal">
					<%--工作流涉及的变量--%>
				<form:hidden path="act.taskId"/>
				<form:hidden path="act.taskName"/>
				<form:hidden path="act.taskDefKey"/>
				<form:hidden path="act.procInsId"/>
				<form:hidden path="act.procDefId"/>
				<form:hidden id="flag" path="act.flag"/>
				<button type="submit" class="btn btn-primary btn-lg">启动采购计划录入流程</button>
				</form:form>
			</div>
			<div class="col-lg-1 col-md-2 col-sm-2 col-xs-2"></div>
			<div class="col-lg-3 col-md-3 col-sm-3">
				<form:form id="inputForm" modelAttribute="purPlanMain" action="${ctx}/purplan/purPlanMainDraft/form" method="post" class="form-horizontal">
					<%--工作流涉及的变量--%>
				<form:hidden path="act.taskId"/>
				<form:hidden path="act.taskName"/>
				<form:hidden path="act.taskDefKey"/>
				<form:hidden path="act.procInsId"/>
				<form:hidden path="act.procDefId"/>
				<form:hidden id="flag" path="act.flag"/>
				<button type="submit" class="btn btn-primary btn-lg">启动采购计划制定流程</button>
				</form:form>
			</div>


		</div>
	</div>
</div>


</body>
</html>
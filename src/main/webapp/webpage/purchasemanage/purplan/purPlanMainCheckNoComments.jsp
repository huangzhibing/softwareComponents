<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>指定下一个审核人</title>
<meta name="decorator" content="ani" />
<script type="text/javascript">


	function getMySelect() {
        $("#form").submit();
	    return $("#deptId").val();

    }
</script>

</head>
<body>
<form:form id="form" modelAttribute="nextUser" action="${ctx}/purplan/purPlanMainCheckComment/passSave" method="post" >



	<div class="wrapper wrapper-content">
		<div class="panel panel-primary">

			<div class="panel-body">
					<form:hidden path="billId" />
					<div class="form-group">
						<div class=" col-xs-1 col-sm-1"></div>
						<label class=" col-xs-3 col-sm-3 control-label"><font color="red">*</font>下一个审核人:</label>
						<div class=" col-xs-5 col-sm-5">
						<form:select class=" form-control " id="deptId" path="deptId" items="${nextCheckers}" itemLabel="name" itemValue="deptId" />
					    </div>
					</div>

			</div>
		</div>
	</div>
</form:form>
</body>
</html>
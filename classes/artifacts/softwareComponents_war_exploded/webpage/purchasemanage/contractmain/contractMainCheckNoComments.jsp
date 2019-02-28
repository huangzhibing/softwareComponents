<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>指定下一个审核人</title>
<meta name="decorator" content="ani" />
<script type="text/javascript">

	function test() {
		var mydata=new Array(1);
	      mydata[0]=$("#deptId").val();
	   return mydata;
	}
</script>

</head>
<body>
<form:form modelAttribute="nextUser" action="${ctx}/contractmain/contractMainCheckComment/passSave" method="post" >
	  <form:hidden path="billId"/>
	<div class="wrapper wrapper-content">
		<div class="form-horizontal">

					<div class="form-group">
						<div class=" col-xs-1 col-sm-1"></div>
						<label class=" col-xs-3 col-sm-3 control-label"><font color="red">*</font>下一个审核人:</label>
						<div class=" col-xs-5 col-sm-5">
						<form:select class=" form-control " path="deptId" items="${nextCheckers}" itemLabel="name" itemValue="deptId" />
					    </div>
					</div>
	<%--	<div class="row">
				<div class="col-md-12 col-xs-12">
					<div class="col-md-5 col-xs-5"></div>
					<div class="col-md-2 col-xs-2">
						<div class="form-group text-center">
							<div>
								<button class="btn btn-primary btn-block btn-md btn-parsley"
									data-loading-text="正在提交..." onclick="test()">提 交</button>
							</div>
						</div>
					</div>
					<div class="col-md-3 col-xs-3"></div>
				</div>
			</div>
	 --%>		
		</div>
	</div>
</form:form>
</body>
</html>
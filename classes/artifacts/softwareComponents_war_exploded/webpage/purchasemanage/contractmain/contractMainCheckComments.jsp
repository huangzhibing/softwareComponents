<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>审核意见</title>
<meta name="decorator" content="ani" />
<script type="text/javascript">

	function test() {
		var mydata=new Array(2);
		    mydata[0]=$("#deptId").val();
		    mydata[1]=$("#note").val();
		return mydata;
	}
	
	
</script>

</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		
		<div class="panel-body">
		<form:form id="inputForm1" modelAttribute="nextUser" action="${ctx}/contractmain/contractMainCheckComment/unPassSave" method="post" class="form-horizontal">
		     <form:hidden path="billId"/>
	      <%--  <form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/>
	    --%>  <sys:message content="${message}"/>	
				<%--
				<div class="form-group">
					<label class="col-sm-1 control-label"><font color="red">*</font>下一审核人：</label>
					<div class="col-sm-1">
					    <form:select path="deptId" class="form-control ">
						 	<form:options items="${nextCheckers}" itemLabel="name" itemValue="deptId" htmlEscape="false"/>
						</form:select>
					</div>					
				</div>
				 --%>				
	 			<div class="form-group">
					<label class="col-sm-2 control-label">意见说明：</label>
					<div class="col-sm-10">
						<form:textarea path="note" htmlEscape="false" rows="8" maxlength="2147483647"    class="form-control "/>
					</div>
				</div>			   		   		   
		  <%--    <div class="row">
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
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
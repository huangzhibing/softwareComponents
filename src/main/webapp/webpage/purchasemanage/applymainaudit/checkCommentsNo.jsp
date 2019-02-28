<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>审核意见</title>
<meta name="decorator" content="ani" />
<script type="text/javascript">

	function windowclose() {
	
		jp.close();
		//top.layer.msg("提交成功", {icon: "${ctype=='success'? 1:1}"});
	}
</script>

</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="adtDetail" action="${ctx}/applymainaudit/applyMainAudit/subSaveAdtNo" method="post" class="form-horizontal">
		<%--<form:hidden path="id"/>
	        <form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/>
	 --%>		<sys:message content="${message}"/>	
	 			<form:hidden path="billNum" value="${applyMainAudit.billNum }"/>
	 			
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label"><font color="red">*</font>下一审核人：</label>
					<div class="col-sm-10">
					    <form:select path="jpositionCode" class="form-control ">
							<form:option value="" label=""/>
						 	<form:options items="${roleList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
						</form:select>
					</div>
					
				</div>				
	 			<div class="form-group">
					<label class="col-sm-2 control-label">审核不同意说明：</label>
					<div class="col-sm-10">
						<form:textarea path="justifyRemark" htmlEscape="false" rows="4" maxlength="2147483647"    class="form-control "/>
					</div>
				</div>
	<%--	<c:if test="${fns:hasPermission('contractnotesmodel:contractNotesModel:edit') || isAdd}">
		   <div class="col-md-2 col-xs-2 form-group">
	             
		           <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交..." onclick="windowclose()">提 交</button>
		                 </div>
		             </div>
		            </div>
		            </div>
		     --%>       
		            
		           <div class="row">
				<div class="col-md-12 col-xs-12">
					<div class="col-md-5 col-xs-5"></div>
					<div class="col-md-2 col-xs-2">
						<div class="form-group text-center">
							<div>
								<button class="btn btn-primary btn-block btn-md btn-parsley"
									data-loading-text="正在提交..." onclick="windowclose()">提 交</button>
							</div>
						</div>
					</div>
					<div class="col-md-3 col-xs-3"></div>
				</div>
		   	   </div> 
		            
		            
		            
	<%-- 	</c:if> --%>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
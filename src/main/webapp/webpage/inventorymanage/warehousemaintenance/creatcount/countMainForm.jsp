<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>生成盘点单管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					jp.loading();
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});

	        $('#checkDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });

	        $('#wareId').on('change',function(){
				$('#binName').val('')
	            $('#binDelButton').click()
				$('#locName').val('')
	            $('#locDelButton').click()
			})
			$('#binId').on('change',function(){
				$('#locName').val('')
	            $('#locDelButton').click()
			})

		});
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">

		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="countMain" action="${ctx}/creatcount/creatCount/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>盘点日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='checkDate'>
			                    <input type='text'  name="checkDate" class="form-control"  value="<fmt:formatDate value="${countMain.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>盘点单号：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false" readonly="true"   class="form-control required"/>
					</div>
				</div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><font color="red">*</font>仓库代码：</label>
                <div class="col-sm-10">
                    <sys:gridselect-modify targetId="wareName" targetField="wareName" url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${countMain.ware.id}" labelName="ware.wareID" labelValue="${countMain.ware.wareID}"
                                           title="选择仓库代码" cssClass="form-control required" fieldLabels="仓库代码|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库代码|仓库名称" searchKeys="wareID|wareName" ></sys:gridselect-modify>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><font color="red">*</font>仓库名称：</label>
                <div class="col-sm-10">
                    <form:input readonly="true" path="wareName" htmlEscape="false"    class="form-control required"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">货区代码：</label>
                <div class="col-sm-10">
                    <sys:gridselect-modify targetId="binName" targetField="binDesc" urlParamId="wareNames" urlParamName="wareId" url="${ctx}/bin/bin/data" id="bin" name="bin.id" value="${countMain.bin.id}" labelName="bin.binId" labelValue="${countMain.bin.binId}"
                                           title="选择货区代码" cssClass="form-control" fieldLabels="货区代码|货区名称" fieldKeys="binId|binDesc" searchLabels="货区代码|货区名称" searchKeys="binId|binDesc" ></sys:gridselect-modify>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">货区名称：</label>
                <div class="col-sm-10">
                    <form:input readonly="true" path="binName" htmlEscape="false"    class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">货位代码：</label>
                <div class="col-sm-10">
                    <sys:gridselect-modify targetId="locName" targetField="locDesc" urlParamId="binNames" urlParamName="binId" url="${ctx}/location/location/data" id="loc" name="loc.id" value="${countMain.loc.id}" labelName="loc.locId" labelValue="${countMain.loc.locId}"
                                           title="选择货位代码" cssClass="form-control" fieldLabels="货位代码|货位名称" fieldKeys="locId|locDesc" searchLabels="货位代码|货位名称" searchKeys="locId|locDesc" ></sys:gridselect-modify>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">货位名称：</label>
                <div class="col-sm-10">
                    <form:input readonly="true" path="locName" htmlEscape="false"    class="form-control "/>
                </div>
            </div>
		<c:if test="${fns:hasPermission('creatcount:countMain:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">设置 盘点</button>
		                 </div>
		             </div>
		        </div>
		</c:if>
		</form:form>
		</div>
	</div>
	</div>
</div>
</div>
</body>
</html>
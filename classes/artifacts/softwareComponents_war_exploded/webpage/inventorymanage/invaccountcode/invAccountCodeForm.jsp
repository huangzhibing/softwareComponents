<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>库存帐扫码管理</title>
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
			
		});
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/invaccountcode/invAccountCode"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="invAccountCode" action="${ctx}/invaccountcode/invAccountCode/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">仓库编号：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify targetId="aaa" targetField="aaa" url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.wareID" value="${invAccountCode.ware.wareID}" labelName="ware.wareID" labelValue="${invAccountCode.ware.wareID}"
							 title="选择仓库编号" cssClass="form-control required" fieldLabels="仓库编号|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库编号|仓库名称" searchKeys="wareID|wareName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">物料编号：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify targetField="aaa" targetId="aaa" url="${ctx}/item/item/data" id="item" name="item.code" value="${invAccountCode.item.code}" labelName="item.code" labelValue="${invAccountCode.item.code}"
							 title="选择物料编号" cssClass="form-control required" fieldLabels="物料编号|物料名称" fieldKeys="code|name" searchLabels="物料编号|物料名称" searchKeys="code|name" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">会计年度：</label>
					<div class="col-sm-10">
						<form:input path="year" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">会计期间：</label>
					<div class="col-sm-10">
						<form:input path="period" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">货区编号：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify targetId="aaa" targetField="aaa" url="${ctx}/bin/bin/data" id="bin" name="bin.binId" value="${invAccountCode.bin.binId}" labelName="bin.binId" labelValue="${invAccountCode.bin.binId}"
							 title="选择货区编号" cssClass="form-control required" fieldLabels="货区编号|货区名称" fieldKeys="binId|binDesc" searchLabels="货区编号|货区名称" searchKeys="binId|binDesc" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">货位编号：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify targetField="aaa" targetId="aaa" url="${ctx}/location/location/data" id="loc" name="loc.locId" value="${invAccountCode.loc.locId}" labelName="loc.locId" labelValue="${invAccountCode.loc.locId}"
							 title="选择货位编号" cssClass="form-control required" fieldLabels="货位编号|货位名称" fieldKeys="locId|locDesc" searchLabels="货位编号|货位名称" searchKeys="locId|locDesc" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">批次号：</label>
					<div class="col-sm-10">
						<form:input path="itemBatch" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">物料二维码：</label>
					<div class="col-sm-10">
						<form:input path="itemBarcode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">供应商二维码：</label>
					<div class="col-sm-10">
						<form:input path="supBarcode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('invaccountcode:invAccountCode:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
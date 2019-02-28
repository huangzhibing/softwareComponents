<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>库存帐管理</title>
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
				<a class="panelButton" href="${ctx}/invaccount/invAccount"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="invAccount" action="${ctx}/invaccount/invAccount/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>仓库编号：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${invAccount.ware.id}" labelName="ware.wareID" labelValue="${invAccount.ware.wareID}"
							 title="选择仓库编号" cssClass="form-control required" fieldLabels="仓库号|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库号|仓库名称" searchKeys="wareID|wareName" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>物料编号：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/item/item/data" id="item" name="item.id" value="${invAccount.item.id}" labelName="item.code" labelValue="${invAccount.item.code}"
							 title="选择物料编号" cssClass="form-control required" fieldLabels="物料编号|物料名称" fieldKeys="code|name" searchLabels="物料编号|物料名称" searchKeys="code|name" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>会计年度：</label>
					<div class="col-sm-10">
						<form:input path="year" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>会计期间：</label>
					<div class="col-sm-10">
						<form:input path="period" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">期初数量：</label>
					<div class="col-sm-10">
						<form:input path="beginQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">期初金额：</label>
					<div class="col-sm-10">
						<form:input path="beginSum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">本期入库量：</label>
					<div class="col-sm-10">
						<form:input path="currInQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">入库金额：</label>
					<div class="col-sm-10">
						<form:input path="currInSum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">本期出库量：</label>
					<div class="col-sm-10">
						<form:input path="currOutQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">出库金额：</label>
					<div class="col-sm-10">
						<form:input path="currOutSum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">现有量：</label>
					<div class="col-sm-10">
						<form:input path="nowQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">现有金额：</label>
					<div class="col-sm-10">
						<form:input path="nowSum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单价：</label>
					<div class="col-sm-10">
						<form:input path="costPrice" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">差异率：</label>
					<div class="col-sm-10">
						<form:input path="costTax" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">期初差异：</label>
					<div class="col-sm-10">
						<form:input path="beginSub" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">收入差异：</label>
					<div class="col-sm-10">
						<form:input path="currInSub" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">发出差异：</label>
					<div class="col-sm-10">
						<form:input path="currOutSub" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">结存差异：</label>
					<div class="col-sm-10">
						<form:input path="nowSub" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">最小入库单价：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="minCost" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">最大入库单价：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="maxCost" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">公司机构类码：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="companyCode" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">调整入库：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="tinSum" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">调整出库：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="toutSum" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">调拨入库数量：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="dinQty" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">调拨入库金额：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="dinSum" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">调拨出库数量：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="doutQty" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">调拨出库金额：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="doutSum" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">盘点入库数量：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="pinQty" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">盘点入库金额：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="pinSum" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">盘点出库数量：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="poutQty" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">盘点出库金额：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="poutSum" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">其他入库数量：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="qinQty" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">其他入库金额：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="qinSum" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">其他出库数量：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="qoutQty" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">其他出库金额：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="qoutSum" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">盘点标志：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:select path="checkFlag" class="form-control ">--%>
							<%--<form:option value="" label=""/>--%>
							<%--<form:options items="${fns:getDictList('checkFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
						<%--</form:select>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">工具标志：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:select path="toolFlag" class="form-control ">--%>
							<%--<form:option value="" label=""/>--%>
							<%--<form:options items="${fns:getDictList('toolFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
						<%--</form:select>--%>
					<%--</div>--%>
				<%--</div>--%>
				<div class="form-group">
					<label class="col-sm-2 control-label">货区编号：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/bin/bin/data" id="bin" name="bin.id" value="${invAccount.bin.id}" labelName="bin.binId" labelValue="${invAccount.bin.binId}"
							 title="选择货区编号" cssClass="form-control required" fieldLabels="货区编号|货区名称" fieldKeys="binId|binDesc" searchLabels="货区编号|货区名称" searchKeys="binId|binDesc" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">货位编号：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/location/location/data" id="loc" name="loc.id" value="${invAccount.loc.id}" labelName="loc.locId" labelValue="${invAccount.loc.locId}"
							 title="选择货位编号" cssClass="form-control required" fieldLabels="货位编号|货位名称" fieldKeys="locId|locDesc" searchLabels="货位编号|货位名称" searchKeys="locId|locDesc" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">批次号：</label>
					<div class="col-sm-10">
						<form:input path="itemBatch" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">供应商编号：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="corId" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">部门编号：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="deptId" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">需求号：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="applyCode" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">订单号：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="orderCode" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">工序号：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:input path="operNo" htmlEscape="false"    class="form-control "/>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-2 control-label">自制外协标记：</label>--%>
					<%--<div class="col-sm-10">--%>
						<%--<form:select path="sourceFlag" class="form-control ">--%>
							<%--<form:option value="" label=""/>--%>
							<%--<form:options items="${fns:getDictList('sourceFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
						<%--</form:select>--%>
					<%--</div>--%>
				<%--</div>--%>
				<div class="form-group">
					<label class="col-sm-2 control-label">待发货数量：</label>
					<div class="col-sm-10">
						<form:input path="salQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">待入库数量：</label>
					<div class="col-sm-10">
						<form:input path="purQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">调拨在途数量：</label>
					<div class="col-sm-10">
						<form:input path="dinTranQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">调拨待发数量：</label>
					<div class="col-sm-10">
						<form:input path="doutTranQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划备料数量：</label>
					<div class="col-sm-10">
						<form:input path="sfcQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">不合格数量：</label>
					<div class="col-sm-10">
						<form:input path="qmsQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">可用数量：</label>
					<div class="col-sm-10">
						<form:input path="realQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('invaccount:invAccount:edit') || isAdd}">
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
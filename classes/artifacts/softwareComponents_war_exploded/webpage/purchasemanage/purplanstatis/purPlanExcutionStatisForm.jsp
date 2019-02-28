<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购计划执行情况表管理</title>
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
			
	        $('#planDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
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
				<a class="panelButton" href="${ctx}/purplanstatis/purPlanExcutionStatis"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="purPlanExcutionStatis" action="${ctx}/purplanstatis/purPlanExcutionStatis/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="物料编码：">物料编码：</label>
				<sys:gridselect-pursup url="${ctx}/item/item/data" id="itemCode" name="itemCode.id" value="${purPlanDetailStatis.itemCode.id}" labelName="itemCode.code" labelValue="${purPlanDetailStatis.itemCode.code}"
									   title="选择物料编码" cssClass="form-control required" fieldLabels="物料编号|物料名称" fieldKeys="code|name" searchLabels="物料编号|物料名称" searchKeys="code|name" targetId="itemName" targetField="name" ></sys:gridselect-pursup>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-3">
				<label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
				<form:input path="itemName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>


				<div class="form-group">
					<label class="col-sm-2 control-label">计划日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='planDate'>
			                    <input type='text'  name="planDate" class="form-control"  value="<fmt:formatDate value="${purPlanExcutionStatis.planDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单价：</label>
					<div class="col-sm-10">
						<form:input path="planPrice" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">预算金额：</label>
					<div class="col-sm-10">
						<form:input path="planSum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">采购员编号：</label>
					<div class="col-sm-10">
						<sys:userselect id="buyerId" name="buyerId.id" value="${purPlanExcutionStatis.buyerId.id}" labelName="buyerId.no" labelValue="${purPlanExcutionStatis.buyerId.no}"
							    cssClass="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">采购员：</label>
					<div class="col-sm-10">
						<form:input path="buyerName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">供应商编码：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/account/account/data" id="supId" name="supId.id" value="${purPlanExcutionStatis.supId.id}" labelName="supId.accountCode" labelValue="${purPlanExcutionStatis.supId.accountCode}"
							 title="选择供应商编码" cssClass="form-control required" fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">供应商名称：</label>
					<div class="col-sm-10">
						<form:input path="supName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">下单日期：</label>
					<div class="col-sm-10">
						<form:input path="bdate" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">订单号：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">订单数量：</label>
					<div class="col-sm-10">
						<form:input path="itemQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">交货日期：</label>
					<div class="col-sm-10">
						<form:input path="conArriveDate" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">实际交货日期：</label>
					<div class="col-sm-10">
						<form:input path="arriveDate" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">实际交货数量：</label>
					<div class="col-sm-10">
						<form:input path="checkQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">质量状态：</label>
					<div class="col-sm-10">
						<form:select path="qmsFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('qmsFlagSal')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('purplanstatis:purPlanExcutionStatis:edit') || isAdd}">
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
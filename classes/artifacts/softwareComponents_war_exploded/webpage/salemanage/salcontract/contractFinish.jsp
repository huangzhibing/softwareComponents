<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>销售合同管理</title>
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
			
	        $('#signDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#inputDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#endDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
		
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
			$(list+idx).find(".form_datetime").each(function(){
				 $(this).datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
			    });
			});
			$(list+idx+"_itemCode").val(idx);
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
	<script type="text/javascript">
		$(document).ready(function(){
			if('${isAdd}'=='true'){
				document.getElementById("contractStatid").value="正在录入";
				document.getElementById("contractStat").value="W";
			}else{
				document.getElementById("contractStatid").value="修改";
				document.getElementById("contractStat").value='${contract.contractStat}';
			}
		})
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/salcontract/contract/contractFinish"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="contract" action="" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<%-- <div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div> --%>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>合同编码：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="contractCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">合同状态：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" id="contractStatid" path="" htmlEscape="false"    class="form-control "/>
					</div>
					<div class="col-sm-10" style="display:none;">
						<form:input id="contractStat" path="contractStat" value="" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">签订日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='signDate' >
			                    <input readOnly="true" type='text'  name="signDate" class="form-control "  value="<fmt:formatDate value="${contract.signDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">交货日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='deliveryDate' >
			                    <input readOnly="true" type='text'  name="deliveryDate" class="form-control "  value="<fmt:formatDate value="${contract.deliveryDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">合同类型编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disabled="disabled" targetId="contractTypeNamed" targetField="ctrTypeName" url="${ctx}/contract/ctrTypeDef/data" id="contractType" name="contractType.id" value="${contract.contractType.id}" labelName="contractType.ctrTypeCode" labelValue="${contract.contractType.ctrTypeCode}"
							 title="选择合同类型编码" cssClass="form-control required" fieldLabels="类型编码|类型名称" fieldKeys="ctrTypeCode|ctrTypeName" searchLabels="类型编码|类型名称" searchKeys="ctrTypeCode|ctrTypeName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">合同类型名称：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" id="contractTypeNamed" path="contractTypeName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">客户编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disabled="disabled"  targetId="accountNamed" targetField="accountName" url="${ctx}/account/account/data?accTypeNameRu=客户" id="account" name="account.id" value="${contract.account.id}" labelName="account.accountCode" labelValue="${contract.account.accountCode}"
							 title="选择客户编码" cssClass="form-control required" fieldLabels="客户编码|客户名称" fieldKeys="accountCode|accountName" searchLabels="客户编码|客户名称" searchKeys="accountCode|accountName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">客户名称：</label>
					<div class="col-sm-10">
						<form:input id="accountNamed" readOnly="true" path="accountName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">结算方式编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disabled="disabled" targetId="blncTypeNamed" targetField="blncTypeName" url="${ctx}/blnctypedef/blncTypeDef/data" id="blncType" name="blncType.id" value="${contract.blncType.id}" labelName="blncType.blncTypeCode" labelValue="${contract.blncType.blncTypeCode}"
							 title="选择结算方式编码" cssClass="form-control required" fieldLabels="结算编码|结算名称" fieldKeys="blncTypeCode|blncTypeName" searchLabels="结算编码|结算名称" searchKeys="blncTypeCode|blncTypeName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">结算方式名称：</label>
					<div class="col-sm-10">
						<form:input  id="blncTypeNamed" readOnly="true" path="blncTypeName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">销售人员编码：</label>
					<div class="col-sm-10">
						<sys:userselect-modify disabled="disabled" targetId="personNamed" id="person" name="person.id" value="${contract.person.id}" labelName="person.no" labelValue="${contract.person.no}"
							    cssClass="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">销售人员姓名：</label>
					<div class="col-sm-10">
						<form:input  id="personNamed" readOnly="true" path="personName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='inputDate'>
			                    <input readOnly="true" type='text'  name="inputDate" class="form-control "  value="<fmt:formatDate value="${contract.inputDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<%-- <div class="form-group">
					<label class="col-sm-2 control-label">制单人编码：</label>
					<div class="col-sm-10">
						<sys:userselect id="inputPerson" name="inputPerson.id" value="${contract.inputPerson.id}" labelName="inputPerson.name" labelValue="${contract.inputPerson.name}"
							    cssClass="form-control "/>
					</div>
				</div> --%>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人编码：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="inputPerson.no" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div  style="display:none;">
					<form:input readOnly="true" path="inputPerson.id" htmlEscape="false"    class="form-control "/>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人名称：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="inputPerson.name" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">结案原因：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="endReason" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">结案人编码：</label>
					<div class="col-sm-10">
						<sys:userselect-modify disabled="disabled" targetId="endPsnNamed" id="endPerson" name="endPerson.id" value="${contract.endPerson.id}" labelName="endPerson.name" labelValue="${contract.endPerson.name}"
							    cssClass="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">结案人姓名：</label>
					<div class="col-sm-10">
						<form:input id="endPsnNamed" readOnly="true" path="endPsnName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">结案日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='endDate' disabled="disabled">
			                    <input readOnly="true" type='text'  name="endDate" class="form-control "  value="<fmt:formatDate value="${contract.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">付款方式编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disabled="disabled" targetId="payTypeNamed" targetField="typeName" url="${ctx}/paytypedef/payTypeDef/data" id="payType" name="payType.id" value="${contract.payType.id}" labelName="payType.typeCode" labelValue="${contract.payType.typeCode}"
							 title="选择付款方式编码" cssClass="form-control required" fieldLabels="付款方式编码|付款方式名称" fieldKeys="typeCode|typeName" searchLabels="付款方式编码|付款方式名称" searchKeys="typeCode|typeName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">付款方式名称：</label>
					<div class="col-sm-10">
						<form:input  id="payTypeNamed" readOnly="true" path="payTypeName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<%-- <div class="form-group">
					<label class="col-sm-2 control-label">税率：</label>
					<div class="col-sm-10">
						<form:input path="taxRatio" htmlEscape="false"    class="form-control  isFloatGteZero"/>
					</div>
				</div> --%>
				<div class="form-group">
					<label class="col-sm-2 control-label">税率编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify  disabled="disabled" targetId="taxRatiod" targetField="salTaxRatio" url="${ctx}/saltaxratio/saleTaxRatio/data" id="saleTaxRatio" name="saleTaxRatio.id" value="${contract.saleTaxRatio.id}" labelName="saleTaxRatio.salTaxCode" labelValue="${contract.saleTaxRatio.salTaxCode}"
							 title="选择税率" cssClass="form-control required" fieldLabels="税率编码|税率" fieldKeys="salTaxCode|salTaxRatio" searchLabels="税率编码|税率" searchKeys="salTaxCode|salTaxRatio" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">税率：</label>
					<div class="col-sm-10">
						<form:input id="taxRatiod" readOnly="true" path="saleTaxRatio.salTaxRatio" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">运输承担方：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="tranpricePayer" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">运费总额：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="transfeeSum" htmlEscape="false"    class="form-control  isFloatGteZero"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">不含税总额：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="goodsSum" htmlEscape="false"    class="form-control  isFloatGteZero"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">含税总额：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="goodsSumTaxed" htmlEscape="false"    class="form-control  isFloatGteZero"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">合同文本：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="itemContents" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">纸制合同号：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="paperCtrCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="praRemark" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">合同模板编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disabled="disabled" targetId="ctrItemsModeld" targetField="itemName" extraField="modelContent:itemContents" url="${ctx}/contractmodel/ctrItemsModel/data" id="ctrItemsModel" name="ctrItemsModel.id" value="${contract.ctrItemsModel.id}" labelName="ctrItemsModel.itemOrder" labelValue="${contract.ctrItemsModel.itemOrder}"
							 title="选择合同模板编码" cssClass="form-control" fieldLabels="合同模板编码|合同模板名称" fieldKeys="itemOrder|itemName" searchLabels="合同模板编码|合同模板名称" searchKeys="itemOrder|itemName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">合同模板名称：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" id="ctrItemsModeld" path="ctrItemsModel.itemName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">合同说明：</label>
					<div class="col-sm-10">
						<form:textarea readOnly="true" id="modelContent" path="ctrItemsModel.itemContents" htmlEscape="false" rows="4"    class="form-control"/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
            	<li class="active"><a data-toggle="tab" href="#tab-2" aria-expanded="false">订单完成明细：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-1" aria-expanded="true">合同附件：</a>
                </li>
				
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade ">
			<a class="btn btn-white btn-sm" onclick="addRow('#ctrFIleList', ctrFIleRowIdx, ctrFIleTpl);ctrFIleRowIdx = ctrFIleRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<div class="table-responsive" style="max-height:500px">
			<table class="table table-striped table-bordered table-condensed" style="min-width:1350px;">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>附件编号</th>
						<th>附件名称</th>
						<th>附件路径</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="ctrFIleList">
				</tbody>
			</table>
			</div>
			<script type="text/template" id="ctrFIleTpl">//<!--
				<tr id="ctrFIleList{{idx}}">
					<td class="hide">
						<input id="ctrFIleList{{idx}}_id" name="ctrFIleList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="ctrFIleList{{idx}}_delFlag" name="ctrFIleList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td style="display:none;">
						<input readOnly="true" id="ctrFIleList{{idx}}_fileCode" name="ctrFIleList[{{idx}}].fileCode" type="text" value="{{row.fileCode}}"    class="form-control "/>
					</td>
					
					
					<td style="display:none;">
						<input readOnly="true" id="ctrFIleList{{idx}}_fileName" name="ctrFIleList[{{idx}}].fileName" type="text" value="{{row.fileName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrFIleList{{idx}}_filePath" name="ctrFIleList[{{idx}}].filePath" type="hidden" value="{{row.filePath}}" maxlength="200" class="form-control"/>
						<sys:ckfinder readonly="true" input="ctrFIleList{{idx}}_filePath" type="files" uploadPath="/salcontract/contract" selectMultiple="true"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#ctrFIleList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var ctrFIleRowIdx = 0, ctrFIleTpl = $("#ctrFIleTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(contract.ctrFIleList)};
					for (var i=0; i<data.length; i++){
						addRow('#ctrFIleList', ctrFIleRowIdx, ctrFIleTpl, data[i]);
						ctrFIleRowIdx = ctrFIleRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-2" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#ctrItemList', ctrItemRowIdx, ctrItemTpl);ctrItemRowIdx = ctrItemRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<div class="table-responsive" style="max-height:500px">
                <table class="table table-striped table-bordered table-condensed" style="min-width:1350px">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>订单编号</th>
						<th style="width:180px;"><font color="red">*</font>产品编码</th>
						<th style="width:180px;"><font color="red">*</font>产品名称</th>
						<th style="width:130px;"><font color="red">*</font>规格型号</th>
						<th>单位名称</th>
						<th><font color="red">*</font>签订量</th>
						<th><font color="red">*</font>无税单价</th>
						<th><font color="red">*</font>无税总额</th>
						<th><font color="red">*</font>含税单价	</th>
						<th><font color="red">*</font>含税金额</th>
						<th>发货量</th>
						<th>运费价格</th>
						<th>运费金额</th>
						<th>订单期间</th>
						<th>备注</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="ctrItemList">
				</tbody>
			</table>
			</div>
			<script type="text/template" id="ctrItemTpl">//<!--
				<tr id="ctrItemList{{idx}}">
					<td class="hide">
						<input id="ctrItemList{{idx}}_id" name="ctrItemList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="ctrItemList{{idx}}_delFlag" name="ctrItemList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_itemCode" name="ctrItemList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"    class="form-control required"/>
					</td>
					<td>
						<sys:gridselect-contract disabled="disabled"
							targetId="" targetField="" 
							extraField="ctrItemList{{idx}}_prodName:itemNameRu;ctrItemList{{idx}}_prodSpec:itemSpec;ctrItemList{{idx}}_unitName:itemMeasure" 
							url="${ctx}/product/product/data" id="ctrItemList{{idx}}_prodCode" name="ctrItemList{{idx}}_prodCode.id" value="{{row.prodCode.id}}" labelName="ctrItemList{{idx}}.prodCode.item.code" labelValue="{{row.productCode}}"
							 title="选择产品代码" cssClass="form-control required " fieldLabels="产品代码|产品名称|产品规格|单位" fieldKeys="item.code|itemNameRu|itemSpec|itemMeasure" searchLabels="产品代码|产品名称" searchKeys="item.code|itemNameRu" ></sys:gridselect-contract>
					</td>
					
					<td style="display:none;">
						<input id="ctrItemList{{idx}}_prodCode" name="ctrItemList[{{idx}}].prodCode" type="text" value="{{row.prodCode}}"    class="form-control "/>
					</td>
					<td style="display:none;">
						<input id="ctrItemList{{idx}}_productCode" name="ctrItemList[{{idx}}].productCode" type="text" value="{{row.productCode}}"    class="form-control "/>
					</td>
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_prodName" name="ctrItemList[{{idx}}].prodName" type="text" value="{{row.prodName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_prodSpec" name="ctrItemList[{{idx}}].prodSpec" type="text" value="{{row.prodSpec}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_unitName" name="ctrItemList[{{idx}}].unitName" type="text" value="{{row.unitName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_prodQty" name="ctrItemList[{{idx}}].prodQty" type="text" value="{{row.prodQty}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_prodPrice" name="ctrItemList[{{idx}}].prodPrice" type="text" value="{{row.prodPrice}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_prodSum" name="ctrItemList[{{idx}}].prodSum" type="text" value="{{row.prodSum}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_prodPriceTaxed" name="ctrItemList[{{idx}}].prodPriceTaxed" type="text" value="{{row.prodPriceTaxed}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_prodSumTaxed" name="ctrItemList[{{idx}}].prodSumTaxed" type="text" value="{{row.prodSumTaxed}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_pickQty" name="ctrItemList[{{idx}}].pickQty" type="text" value="{{row.pickQty}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_transPrice" name="ctrItemList[{{idx}}].transPrice" type="text" value="{{row.transPrice}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_transSum" name="ctrItemList[{{idx}}].transSum" type="text" value="{{row.transSum}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_periodId" name="ctrItemList[{{idx}}].periodId" type="text" value="{{row.periodId}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="true" id="ctrItemList{{idx}}_remark" name="ctrItemList[{{idx}}].remark" type="text" value="{{row.remark}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#ctrItemList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var ctrItemRowIdx = 0, ctrItemTpl = $("#ctrItemTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(contract.ctrItemList)};
					for (var i=0; i<data.length; i++){
						addRow('#ctrItemList', ctrItemRowIdx, ctrItemTpl, data[i]);
						ctrItemRowIdx = ctrItemRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('salcontract:contract:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                 	<a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/salcontract/contract/contractFinish">返回</a>
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
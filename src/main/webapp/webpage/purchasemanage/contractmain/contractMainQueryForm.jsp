<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购合同管理管理</title>
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
			
	        $('#bdate').datetimepicker({
				// format: "YYYY-MM-DD"
	        	format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#advanceDate').datetimepicker({
				// format: "YYYY-MM-DD"
	        	format: "YYYY-MM-DD HH:mm:ss"
			});
	        $('#endDate').datetimepicker({
				// format: "YYYY-MM-DD"
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
					 format: "YYYY-MM-DD"
			    });
			});
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
		
		
		//在具体的使用中需替换'#applyDetailList', applyDetailRowIdx, applyDetailTpl这三个值
		function setOtherValue(items,obj,targetField,targetId,nam,labNam){
			var da=items;
			 for (var i=1; i<da.length; i++){
				    //增加一行
				    addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl);				
					//为所增加的行进行赋值
				    addRowModify('#contractDetailList', contractDetailRowIdx, contractDetailTpl, da[i],obj,targetField,targetId,nam,labNam);		
				    contractDetailRowIdx = contractDetailRowIdx + 1;
				}
		}
        		
		function addRowModify(list, idx, tpl, row, obj,targetField,targetId,nam,labNam){		 
			//给gridselect-modify1标签的显示input标签赋值，后台所传显示
			$(list+idx+"_"+obj+"Names").val(row[labNam]);
		    $(list+idx+"_"+obj+"Names").trigger("change");		    
			//为gridselect-modify1隐含的标签赋值,提交时传给后台
			$(list+idx+"_"+obj+"Id").val(row[nam]);		
		    $(list+idx+"_"+obj+"Id").trigger("change");
			//table标签的其他字段赋值
			//给各标签赋值
			for(var i=0;i<targetField.length;i++){
				//获取标签id
				var ind=targetField[i];
				//获取对象所填充的属性
				var tId=targetId[i];
				$(list+idx+"_"+tId).val(row[ind]);
			    $(list+idx+"_"+tId).trigger("change");	
			}						
		}
		
		function submitForm(){
			//修改表单提交的action
		 //   $("#inputForm").attr('action','${ctx}/contractmain/contractMain/submit');    
		//	$("#inputForm").submit();
		$.post("${ctx}/contractmain/contractMainQuery/save",$('#inputForm').serialize(),function(data){			
			jp.success(data.msg);
		});
		}
		
		function setRatio(ratio){
		  var rowl=contractDetailRowIdx;
		 // alertr(ratio);
		  for(var i=1;i<rowl;i++){
			    var itemPrice=$("#contractDetailList"+i+"_itemPrice").val();
			    //修改含税单价
				$("#contractDetailList"+i+"_itemPriceTaxed").val(itemPrice*(1+1*ratio));
				//获取签订的合同量
				var itemQtyValue=$("#contractDetailList"+i+"_itemQty").val();
				//修改含税金额
			    $("#contractDetailList"+i+"_itemSumTaxed").val(itemPrice*(1+1*ratio)*itemQtyValue);
			    
		  }
		}
		function selectPlan(buyerId){}
		
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="#" onclick="history.go(-1)"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="contractMain" action="${ctx}/contractmain/contractMainQuery/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		
		        <div class="form-group">				   
					<label class="col-sm-2 control-label">合同号：</label>
					<div class="col-sm-3">
						<form:input path="billNum" htmlEscape="false"   readOnly="true"  class="form-control required"/>
					</div>
					<label class="col-sm-2 control-label">合同状态：</label>
				     <form:input path="billStateFlag" htmlEscape="false" type="hidden" readOnly="true"  class="form-control"/>					     
					<div class="col-sm-3">
						<form:select path="billStateFlag" class="form-control "  disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('ContractStateFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
		
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人编号：</label>
					<div class="col-sm-3">
					    <form:input path="user.id" htmlEscape="false" type="hidden" readOnly="true"  class="form-control"/>					    
						<form:input path="user.no" htmlEscape="false"  readOnly="true"  class="form-control required"/>
					</div>
					<label class="col-sm-2 control-label">制单人名称：</label>
					<div class="col-sm-3">
						<form:input path="makeEmpname" htmlEscape="false" readOnly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">供应商编号：</label>
					<div class="col-sm-3">
						        <form:input path="account.id" htmlEscape="false" readOnly="true"  class="form-control hidden"/>										            
								<form:input path="account.accountCode" htmlEscape="false" readOnly="true"  class="form-control required"/>					
				
					</div>
					<label class="col-sm-2 control-label">供应商名称：</label>
					<div class="col-sm-3">
						<form:input path="supName" htmlEscape="false"  readOnly="true"  class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
				   <label class="col-sm-2 control-label">供应商联系人：</label>
					<div class="col-sm-3">
						<form:input path="accountLinkMam" htmlEscape="false"  readOnly="true"  class="form-control"/>
					</div>
					<label class="col-sm-2 control-label">税率：</label>
					<div class="col-sm-3">
					   <%-- <sys:gridselect-purratio url="${ctx}/taxratio/taxRatio/data" id="taxRatio" name="taxRatio" value="${contractMain.taxRatio}" labelName="taxRatio" labelValue="${contractMain.taxRatio}"
							 title="选择税率" cssClass="form-control required" targetId="" targetField="" fieldLabels="税率编号|税率值" fieldKeys="taxratioId|taxRatio" searchLabels="税率编号|税率值" searchKeys="taxratioId|taxRatio" ></sys:gridselect-purratio>
						--%>
						 <input id="taxRatio" name="taxRatioNew" htmlEscape="false" readOnly="true"  class="form-control required" onchange="setRatioNew(this)" value="<fmt:formatNumber value="${contractMain.taxRatio}" maxIntegerDigits="4" type="percent"/>" />			    
					</div>
					
				   <%--
					<label class="col-sm-2 control-label">承送单号：</label>
					<div class="col-sm-3">
						<form:input path="transContractNum" htmlEscape="false"    class="form-control"/>
					</div>
				    --%>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">采购员编号：</label>
					<div class="col-sm-3">
				             <form:input path="groupBuyer.id" htmlEscape="false" readOnly="true"  class="form-control hidden"/>										            
						     <form:input path="groupBuyer.user.no" htmlEscape="false" readOnly="true"  class="form-control required"/>					
				
				    </div>
					<label class="col-sm-2 control-label">采购员名称：</label>
					<div class="col-sm-3">
						<form:input path="buyerName" htmlEscape="false" readOnly="true"   class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">采购员电话：</label>
					<div class="col-sm-3">
					  <input id="buyerPhoneNum" name="buyerPhoneNum" readOnly="true"  class="form-control" />
					</div>
				    <label class="col-sm-2 control-label">采购员传真：</label>
					<div class="col-sm-3">
						<form:input path="buyerFaxNum" htmlEscape="false"  readOnly="true"  class="form-control"/>
					</div>
				</div>
				<div class="form-group">
				<label class="col-sm-2 control-label">合同类型：</label>
					<div class="col-sm-3">
					            <form:input path="contractType.id" htmlEscape="false" readOnly="true"  class="form-control hidden"/>										            
								<form:input path="contypeName" htmlEscape="false" readOnly="true"  class="form-control required"/>					
					</div>
					<label class="col-sm-2 control-label">合同保证金：</label>
					<div class="col-sm-3">
						<input id="contractNeedSum" name="contractNeedSum" readOnly="true" class="form-control  number" value="<fmt:formatNumber value="${contractMain.contractNeedSum}" pattern="#.00"/>" />
					</div>
					
				</div>
				<div class="form-group">
					
					<label class="col-sm-2 control-label">签订日期：</label>
					<div class="col-sm-3">						
							<div class='input-group form_datetime' id='bdate'>
			                    <input type='text'  name="bdate" class="form-control required"  readOnly="true" value="<fmt:formatDate value="${contractMain.bdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            			          
					</div>
					<label class="col-sm-2 control-label">付款方式：</label>
							<form:input path="payMode.id" htmlEscape="false" type="hidden" readOnly="true"  class="form-control"/>					     					
					<div class="col-sm-3">
						<form:select path="payMode.id" disabled="true" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${payList}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
					
				</div>
				
			<%-- 	<div class="form-group">
					<label class="col-sm-2 control-label">付款说明：</label>
					<div class="col-sm-3">
						<form:input path="paymentNotes" htmlEscape="false"  readOnly="true"  class="form-control "/>
					</div>
					<label class="col-sm-2 control-label">预付额款：</label>
					<div class="col-sm-3">
						<input id="advancePay" name="advancePay"  readOnly="true"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.advancePay}" pattern="#.00"/>" />
					</div>
				</div>
			--%>
				<div class="form-group">
					<label class="col-sm-2 control-label">预付日期：</label>
					<div class="col-sm-3">
					<div class='input-group form_datetime' id='advanceDate'>
			                    <input type='text'  name="advanceDate" class="form-control" readOnly="true"  value="<fmt:formatDate value="${contractMain.advanceDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>	
					</div>
					<label class="col-sm-2 control-label">截止日期：</label>
					<div class="col-sm-3">
					<div class='input-group form_datetime' id='endDate'>
			                    <input type='text'  name="endDate" class="form-control" readOnly="true" value="<fmt:formatDate value="${contractMain.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>	
					</div>
				</div>
			<%--	<div class="form-group">
					<label class="col-sm-2 control-label">运输方式：</label>
						<form:input path="transType.id" htmlEscape="false" type="hidden" readOnly="true"  class="form-control"/>					     					
					<div class="col-sm-3">
						<form:select path="transType.id" disabled="true" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${transList}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
					<label class="col-sm-2 control-label">运费承担方：</label>
					<div class="col-sm-3">
						<form:input path="tranpricePayer" htmlEscape="false" readOnly="true"   class="form-control "/>
					</div>
				</div>
			 
				<div class="form-group">
					
					<label class="col-sm-2 control-label">承送单号：</label>
					<div class="col-sm-3">
						<form:input path="transContractNum" htmlEscape="false"  readOnly="true"  class="form-control"/>
					</div>
				</div>
				--%>
				<div class="form-group">
					<label class="col-sm-2 control-label">含税总额：</label>
					<div class="col-sm-3">
						<input id="goodsSumTaxed" name="goodsSumTaxed"  readOnly="true"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.goodsSumTaxed}" pattern="#.00"/>" />
					</div>
					<label class="col-sm-2 control-label">不含税总额：</label>
					<div class="col-sm-3">
					    <input id="goodsSum" name="goodsSum"  readOnly="true"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.goodsSum}" pattern="#.00"/>" />
					</div>
				</div>
				
				<div class="form-group">	
					<label class="col-sm-2 control-label">运费总额：</label>
					<div class="col-sm-3">
						<input id="tranprice" name="tranprice"  readOnly="true"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.tranprice}" pattern="#.00"/>" />
					</div>
					<label class="col-sm-2 control-label">单据说明：</label>
					<div class="col-sm-3">
						<form:input path="billNotes" htmlEscape="false" readOnly="true"   class="form-control "/>
					</div>
				</div>
				
		        <div class="form-group">
					<label class="col-sm-2 control-label">供应商地址：</label>
					<div class="col-sm-8">
						<form:input path="supAddress" htmlEscape="false" readOnly="true"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">送货地址：</label>
					<div class="col-sm-8">
						<form:input path="deliveryAddress" htmlEscape="false" readOnly="true"   class="form-control "/>
					</div>
				</div>
				
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">合同信息</a>
                </li>
                <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">合同说明</a>
                </li>
                <li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">需求信息</a>
                </li>
                <li class=""><a data-toggle="tab" href="#tab-4" aria-expanded="false">计划信息</a>
                </li>
                <li class=""><a data-toggle="tab" href="#tab-5" aria-expanded="false">到货信息</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
		 <div class="table-responsive" style="max-height:500px">
			<table class="table table-striped table-bordered table-condensed"  style="min-width: 2430px">
				<thead>
					<tr>
						<th class="hide"></th>
						<th width="80px">编号</th>
						<th width="200px">物料编码</th>
						<th width="250px">物料名称</th>
						<th width="250px">物料规格</th>
						<th width="250px">物料材质</th>
						<th width="220px">计划到货日期</th>
						<th width="150px">签合同量</th>
						<th width="150px">无税单价</th>
						<th width="150px">无税总额</th>
						<th width="150px">含税单价</th>
						<th width="150px">含税金额</th>						
						<th width="100px">单位</th>
						<th width="150px">单位运费（含）</th>
						<th width="150px">运费金额（含）</th>
						<th width="250px">质量要求</th>
						<th width="10px">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="contractDetailList">
				</tbody>
			</table>
	     </div>	
			
			<script type="text/template" id="contractDetailTpl">//<!--
				<tr id="contractDetailList{{idx}}">
					<td class="hide">
						<input id="contractDetailList{{idx}}_id" name="contractDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="contractDetailList{{idx}}_delFlag" name="contractDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
						<input id="contractDetailList{{idx}}_itemId" name="contractDetailList[{{idx}}].item.id" type="hidden" value="{{row.item.id}}"/>
                    </td>

					<td>
						<input id="contractDetailList{{idx}}_serialNum" name="contractDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"  readOnly="true"  class="form-control "/>
					</td>
					
					
					<td>
					     <input id="contractDetailList{{idx}}_item" name="contractDetailList[{{idx}}].item.code" readOnly="true" type="text" value="{{row.item.code}}" class="form-control required"/>
                	</td>					
					
					<td>
						<input id="contractDetailList{{idx}}_itemName" name="contractDetailList[{{idx}}].itemName" type="text" readOnly="true" value="{{row.itemName}}"    class="form-control required"/>
					</td>

	                <td>
						<input id="contractDetailList{{idx}}_itemModel" name="contractDetailList[{{idx}}].itemModel" type="text" value="{{row.itemModel}}" readOnly="true"   class="form-control required"/>
					</td>
					
					<td>
						<input id="contractDetailList{{idx}}_itemTexture" name="contractDetailList[{{idx}}].itemTexture" type="text" value="{{row.itemTexture}}"  readOnly="true"  class="form-control"/>
					</td>
					
                    <td>
					    <div class='input-group form_datetime' id="contractDetailList{{idx}}_arriveDate">
		                    <input type='text'  name="contractDetailList[{{idx}}].arriveDate"  readOnly="true" value="{{row.arriveDate}}" class="form-control"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>	
                    </td>

					<td>
						<input id="contractDetailList{{idx}}_itemQty" name="contractDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"  onchange="setSumValue(this)"  readOnly="true" class="form-control number required"/>
					</td>
					
					
					<td>
						<input id="contractDetailList{{idx}}_itemPrice" name="contractDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"  onchange="setItemPrice(this)" readOnly="true" class="form-control number required"/>
					</td>
					
					
					<td>
						<input id="contractDetailList{{idx}}_itemSum" name="contractDetailList[{{idx}}].itemSum"  type="text" value="{{row.itemSum}}"  readOnly="true"  class="form-control number required"/>
					</td>
					
					
					<td>
						<input id="contractDetailList{{idx}}_itemPriceTaxed" name="contractDetailList[{{idx}}].itemPriceTaxed" type="text" value="{{row.itemPriceTaxed}}" onchange="setItemPriceTaxed(this)" readOnly="true"  class="form-control number required"/>
					</td>
										
					<td>
						<input id="contractDetailList{{idx}}_itemSumTaxed" name="contractDetailList[{{idx}}].itemSumTaxed" type="text" value="{{row.itemSumTaxed}}" readOnly="true"   class="form-control number required"/>
					</td>
													
					<td>
						<input id="contractDetailList{{idx}}_measUnit" name="contractDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"  readOnly="true"  class="form-control "/>
					</td>
				
					
					<td>
						<input id="contractDetailList{{idx}}_transPrice" name="contractDetailList[{{idx}}].transPrice" type="text" value="{{row.transPrice}}" readOnly="true" onchange="setTransPrice(this)"  class="form-control number"/>
					</td>
					
					
					<td>
						<input id="contractDetailList{{idx}}_transSum" name="contractDetailList[{{idx}}].transSum" type="text" value="{{row.transSum}}"  readOnly="true"  class="form-control number"/>
					</td>
					
		             <td>
						<input id="contractDetailList{{idx}}_massRequire" name="contractDetailList[{{idx}}].massRequire" type="text" value="{{row.massRequire}}"  readOnly="true"  class="form-control "/>
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var contractDetailRowIdx =0, contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(contractMain.contractDetailList)};
					
					for (var i=0; i<data.length; i++){
						addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl, data[i]);
						//alert(data[i].arriveDate);
						contractDetailRowIdx = contractDetailRowIdx + 1;
					}	
				});
				//通过合同量录入修改无税金额、含税金额、运费金额
				function setSumValue(obj){
					//获取当前点击所在的行的信息
					var preTagId=obj.id.split("_")[0];
					//获取无税单价
					var itemPriceId=preTagId+"_itemPrice";
					//修改无税总额
					var itemSumId=preTagId+"_itemSum";
					//获取无税单价
					var itemSumValue=$("#"+itemPriceId).val();
					//修改无税总额
					$("#"+itemSumId).val(obj.value*itemSumValue);
					//获取含税单价
					var itemPriceTaxedValue=$("#"+preTagId+"_itemPriceTaxed").val();
					//修改含税金额
					$("#"+preTagId+"_itemSumTaxed").val(itemPriceTaxedValue*obj.value);
					//获取运费单价
					var transPriceValue=$("#"+preTagId+"_transPrice").val();
					//修改运费金额
					$("#"+preTagId+"_transSum").val(transPriceValue*obj.value);
				}
				//通过无税单价修改无税金额、含税单价、含税金额
				function setItemPrice(obj){
					//获取当前点击所在的行的信息
					var preTagId=obj.id.split("_")[0];
					//获取签订的合同量
					var itemQtyValue=$("#"+preTagId+"_itemQty").val();
					//修改无税总额
					$("#"+preTagId+"_itemSum").val(itemQtyValue*obj.value);
					//获取税率的值
					var taxRatio=$("#taxRatioNames").val();
					//修改含税单价
					$("#"+preTagId+"_itemPriceTaxed").val(obj.value*(1+1*taxRatio));
					//修改含税金额
				    $("#"+preTagId+"_itemSumTaxed").val(obj.value*(1+1*taxRatio)*itemQtyValue);
				    //
				}
				//通过含税单价修改含税金额、无税单价、无税金额
				function setItemPriceTaxed(obj){
					//获取当前点击所在的行的信息
					var preTagId=obj.id.split("_")[0];
					//获取签订的合同量
					var itemQtyValue=$("#"+preTagId+"_itemQty").val();
					//修改含税金额
					$("#"+preTagId+"_itemSumTaxed").val(itemQtyValue*obj.value);			
					//获取税率的值
					var taxRatio=$("#taxRatioNames").val();
					//修改无税单价
					$("#"+preTagId+"_itemPrice").val(obj.value/(1+1*taxRatio));
					//修改无税金额
					$("#"+preTagId+"_itemSum").val((obj.value/(1+1*taxRatio))*itemQtyValue);
				}
				//通过运费单价修改运费金额
				function setTransPrice(obj){
					//获取当前点击所在的行的信息
					var preTagId=obj.id.split("_")[0];
					//获取签订的合同量
					var itemQtyValue=$("#"+preTagId+"_itemQty").val();
					$("#"+preTagId+"_transSum").val(obj.value*itemQtyValue);
				}
				
			</script>
			</div>
			
			<div id="tab-2" class="tab-pane fade">			   
			       <br/>  
			       <div class="form-group">				   
					<label class="col-sm-2 control-label">合同模板编码：</label>
					<div class="col-sm-3">
					     	    <form:input path="contractNotesModel.id" htmlEscape="false" readOnly="true"  class="form-control hidden"/>										            
								<form:input path="contractNotesModel.contractId" htmlEscape="false" readOnly="true"  class="form-control required"/>					
					
					</div>
					<label class="col-sm-2 control-label">合同模板名称：</label>
					<div class="col-sm-3">
						<form:input path="contractModelName" htmlEscape="false"  readOnly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">合同说明：</label>
					<div class="col-sm-8">
						<form:textarea path="dealNote" htmlEscape="false" rows="15" maxlength="2147483647"  readonly="true"  class="form-control "/>
					</div>
				</div>						   
			<script type="text/javascript">
				$(document).ready(function() {
				   
				});
			</script>
			</div>			
			
			<div id="tab-3" class="tab-pane fade">
			<div class="table-responsive" style="max-height:500px">
			<table class="table table-striped table-bordered table-condensed"  style="min-width: 1840px">
				<thead>
					<tr>
						<th class="hide" ></th>
						<th>需求单号</th>
						<th>物料编号</th>
						<th>物料名称</th>
						<th>规格型号</th>
						<th>需求日期</th>
						<th>需求数量</th>
						<th>库存量</th>
						<th>单位</th>
						<th>移动平均价</th>
						<th>金额</th>						
						<th>需求状态</th>
					</tr>
				</thead>
				<tbody id="applyDetailList">
				</tbody>
			</table>
	     </div>
			
		<script type="text/template" id="applyDetailTpl">//<!--
				<tr id="applyDetailList{{idx}}">
					<td class="hide">
						<input id="applyDetailList{{idx}}_id" name="applyDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="applyDetailList{{idx}}_delFlag" name="applyDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
                    <td width="90px">
						<input id="applyDetailList{{idx}}_billNum" name="applyDetailList[{{idx}}].applyMain.billNum" type="text"   value="{{row.applyMain.billNum}}" readonly="true" class="form-control "/>
					</td>					
					
					<td width="90px">
						<input id="applyDetailList{{idx}}_item" name="applyDetailList[{{idx}}].item.code" type="text" value="{{row.item.code}}"  readonly="true"  class="form-control "/>
					</td>					
					
					<td width="150px">
						<input id="applyDetailList{{idx}}_itemName" name="applyDetailList[{{idx}}].itemName" type="text" readonly="true" value="{{row.itemName}}"    class="form-control "/>
					</td>
					
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_itemSpecmodel" name="applyDetailList[{{idx}}].itemSpecmodel" type="text" readonly="true" value="{{row.itemSpecmodel}}"    class="form-control "/>
					</td>
					
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_requestDate" name="applyDetailList[{{idx}}].requestDate" type="text" readonly="true" value="{{row.requestDate}}"    class="form-control "/>
					</td>
					
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_applyQty" name="applyDetailList[{{idx}}].applyQty" type="text" value="{{row.applyQty}}"  readonly="true"  class="form-control  required number"/>
					</td>
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_nowSum" name="applyDetailList[{{idx}}].nowSum" type="text" value="{{row.nowSum}}"  readonly="true"  class="form-control "/>
					</td>
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_unitName" name="applyDetailList[{{idx}}].unitName" type="text" value="{{row.unitName}}"  readonly="true"  class="form-control "/>
					</td>

					<td width="70px">
						<input id="applyDetailList{{idx}}_costPrice" name="applyDetailList[{{idx}}].costPrice" type="text" value="{{row.costPrice}}" readonly="true" onchange="costPriceChange(this)"  class="form-control "/>
					</td>

					<td width="70px">
						<input id="applyDetailList{{idx}}_applySum" name="applyDetailList[{{idx}}].applySum" type="text" value="{{row.applySum}}" readonly="true"   class="form-control number"/>
					</td>
					<td width="70px">
					     <select id="applyDetailList{{idx}}_applyMain" name="applyDetailList[{{idx}}].applyMain.billStateFlag" disabled="true" data-value="{{row.applyMain.billStateFlag}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('applyState')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						 </select>
                    </td>
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var applyDetailRowIdx = 0, applyDetailTpl = $("#applyDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(contractMain.applyDetailList)};
					for (var i=0; i<data.length; i++){
						data[i].requestDate=data[i].requestDate.split(" ")[0];
						addRow('#applyDetailList', applyDetailRowIdx, applyDetailTpl, data[i]);
						applyDetailRowIdx = applyDetailRowIdx + 1;
					}					
				});				
			</script>
			</div>	
			
			
			<div id="tab-4" class="tab-pane fade">
		<div class="table-responsive" style="max-height:500px">
			<table class="table table-striped table-bordered table-condensed"  style="min-width:3290px" >
				<thead>
					<tr>
						<th class="hide"></th>
						
						<th width="180px">计划单号</th>
						<th width="150px">物料编码</th>
						<th width="250px">物料名称</th>
						<th width="250px">物料规格</th>
		                <th width="100px">单位</th>
						<th width="150px">采购数量</th>
						<th width="150px">用料部门</th>
						<th width="150px">金额</th>
						<th width="150px">需求日期</th>
						<th width="150px">需求数量</th>
						<th width="150px">库存量</th>	
						<th width="150px">到货量</th>		
						<th width="150px">移动平均价</th>	
						<th width="150px">采购员</th>	
						<th width="150px">制单日期</th>
						<th width="150px">制单人</th>	
						<th width="150px">单据类型</th>	
						<th width="150px">单据说明</th>	
						<th width="250px">数量修改历史</th>	
						<th width="10px">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="purPlanDetailList">
				</tbody>
			</table>
	     </div>
			 
			 
			<script type="text/template" id="purPlanDetailTpl">//<!--
				<tr id="purPlanDetailList{{idx}}">
					<td class="hide">
						<input id="purPlanDetailList{{idx}}_id" name="purPlanDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purPlanDetailList{{idx}}_delFlag" name="purPlanDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
						<input id="purPlanDetailList{{idx}}_itemTexture" name="purPlanDetailList[{{idx}}].itemTexture" type="text" readOnly="true" value="{{row.itemTexture}}"    class="form-control "/>									
                    	<input id="purPlanDetailList{{idx}}_conQty" name="purPlanDetailList[{{idx}}].conQty" type="hidden" value="{{row.conQty}}"/>
						<input id="purPlanDetailList{{idx}}_conQtyOrion" name="purPlanDetailList[{{idx}}].conQty" type="hidden" value="{{row.conQty}}"/>
                    </td>

					<td>
						<input id="purPlanDetailList{{idx}}_billNum" name="purPlanDetailList[{{idx}}].billNum.billNum" type="text" readOnly="true" value="{{row.billNum.billNum}}"    class="form-control "/>
					</td>
										
					<td>
                        <input id="purPlanDetailList{{idx}}_itemCode" name="purPlanDetailList[{{idx}}].itemCode.code" type="text" readOnly="true" value="{{row.itemCode.code}}"    class="form-control "/>
					</td>
					
					<td class="hide">
                        <input id="purPlanDetailList{{idx}}_itemId" name="purPlanDetailList[{{idx}}].itemCode.id" type="text" readOnly="true" value="{{row.itemCode.id}}"    class="form-control "/>
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_itemName" name="purPlanDetailList[{{idx}}].itemName" type="text" readOnly="true" value="{{row.itemName}}"    class="form-control "/>
					</td>
					
					<td>
						<input id="purPlanDetailList{{idx}}_itemSpecmodel" name="purPlanDetailList[{{idx}}].itemSpecmodel"  readOnly="true" type="text" value="{{row.itemSpecmodel}}"    class="form-control "/>
					</td>
					
					<td>
						<input id="purPlanDetailList{{idx}}_unitName" name="purPlanDetailList[{{idx}}].unitName" readOnly="true" type="text" value="{{row.unitName}}"    class="form-control "/>
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_planQty" name="purPlanDetailList[{{idx}}].planQty" readOnly="true" type="text" value="{{row.planQty}}"    class="form-control "/>
					</td>
										
					<td>
						<input id="purPlanDetailList{{idx}}_planDeptName" name="purPlanDetailList[{{idx}}].billNum.planDeptName" readOnly="true" type="text" value="{{row.billNum.planDeptName}}"    class="form-control "/>
					</td>
	
					<td>
						<input id="purPlanDetailList{{idx}}_planSum" name="purPlanDetailList[{{idx}}].planSum" readOnly="true" type="text" value="{{row.planSum}}"    class="form-control "/>
					</td>
															
					<td>
		                 <input type='text' id="purPlanDetailList{{idx}}_requestDate"  name="purPlanDetailList[{{idx}}].requestDate" readOnly="true" class="form-control "  value="{{row.requestDate}}"/>		                   					            
					</td>
																				
					<td>
						<input id="purPlanDetailList{{idx}}_purQty" name="purPlanDetailList[{{idx}}].purQty" readOnly="true" type="text" value="{{row.purQty}}"    class="form-control "/>
					</td>
                    <td>
						<input id="purPlanDetailList{{idx}}_invQty" name="purPlanDetailList[{{idx}}].invQty" readOnly="true" type="text" value="{{row.invQty}}"    class="form-control "/>
					</td>
                    <td>
						<input id="purPlanDetailList{{idx}}_checkQty" name="purPlanDetailList[{{idx}}].checkQty" readOnly="true" type="text" value="{{row.checkQty}}"    class="form-control "/>
					</td>
                    <td>
						<input id="purPlanDetailList{{idx}}_costPrice" name="purPlanDetailList[{{idx}}].costPrice" readOnly="true" type="text" value="{{row.costPrice}}"    class="form-control "/>
					</td>	
                    
                    <td>
						<input id="purPlanDetailList{{idx}}_buyerName" name="purPlanDetailList[{{idx}}].buyerName" readOnly="true" type="text" value="{{row.buyerName}}"    class="form-control "/>
					</td>	
                    <td>
						<input id="purPlanDetailList{{idx}}_makeDate" name="purPlanDetailList[{{idx}}].billNum.makeDate" readOnly="true" type="text" value="{{row.billNum.makeDate}}"    class="form-control "/>
					</td>
                    <td>
						<input id="purPlanDetailList{{idx}}_makeEmpname" name="purPlanDetailList[{{idx}}].billNum.makeEmpname" readOnly="true" type="text" value="{{row.billNum.makeEmpname}}"    class="form-control "/>
					</td>
                    <td>
						<input id="purPlanDetailList{{idx}}_billType" name="purPlanDetailList[{{idx}}].billNum.billType" readOnly="true" type="text" value="{{row.billNum.billType}}"    class="form-control "/>
					</td>
                    <td>
						<input id="purPlanDetailList{{idx}}_makeNotes" name="purPlanDetailList[{{idx}}].billNum.makeNotes" readOnly="true" type="text" value="{{row.billNum.makeNotes}}"    class="form-control "/>
					</td>
                    <td>
						<input id="purPlanDetailList{{idx}}_log" name="purPlanDetailList[{{idx}}].log" readOnly="true" type="text" value="{{row.log}}"    class="form-control "/>
					</td>									
				</tr>//-->
			</script>
			
			<script type="text/javascript">			  
				var purPlanDetailRowIdx = 0, purPlanDetailTpl = $("#purPlanDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				var purPlanData = ${fns:toJson(contractMain.purPlanDetailList)};
				$(document).ready(function() {
				//  var buyerId=$("#groupBuyer.id").val();
				var data = ${fns:toJson(contractMain.purPlanDetailList)};
					for (var i=0; i<data.length; i++){
						data[i].requestDate=data[i].requestDate.split(" ")[0];
						data[i].billNum.makeDate=data[i].billNum.makeDate.split(" ")[0];
						addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, data[i]);
						purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
					}
				});
			</script>
			</div>
			
			<div id="tab-5" class="tab-pane fade">
		<div class="table-responsive" style="max-height:500px">
			<table class="table table-striped table-bordered table-condensed"  style="min-width: 2000px">
				<thead>
					<tr>
						<th class="hide"></th>
						<th width="150px">到货单号</th>
						<th width="150px">物料编号</th>
						<th width="250px">物料名称</th>
						<th width="250px">物料规格</th>
						<th width="100px">单位</th>
						<th width="100px">到货数量</th>
						<th width="100px">实际单价</th>
						<th width="100px">实际金额</th>
						<th width="150px">到货单状态</th>
						<th width="150px">合同单号</th>
						<th width="150px">计划单号</th>
						<th width="150px">到货日期</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="invCheckDetailList">
				</tbody>
			</table>
	     </div>
			
			<script type="text/template" id="invCheckDetailTpl">//<!--
				<tr id="invCheckDetailList{{idx}}">
					<td class="hide">
						<input id="invCheckDetailList{{idx}}_id" name="invCheckDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="invCheckDetailList{{idx}}_delFlag" name="invCheckDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<input id="invCheckDetailList{{idx}}_invCheckMain" name="invCheckDetailList[{{idx}}].invCheckMain.billnum" type="text" value="{{row.invCheckMain.billnum}}"    class="form-control " readonly="true" />
					</td>					
					
					<td>
						<input id="invCheckDetailList{{idx}}_item" name="invCheckDetailList[{{idx}}].item.code" type="text" value="{{row.item.code}}"  readonly="true"  class="form-control "/>
                	</td>
					
					
					<td>
						<input id="invCheckDetailList{{idx}}_itemName" name="invCheckDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control " readonly="true"/>
					</td>
					
                    <td>
						<input id="invCheckDetailList{{idx}}_itemSpecmodel" name="invCheckDetailList[{{idx}}].itemSpecmodel" type="text" value="{{row.itemSpecmodel}}"    class="form-control " readonly="true"/>
					</td>
					
					<td>
						<input id="invCheckDetailList{{idx}}_unitCode" name="invCheckDetailList[{{idx}}].unitCode" type="text" value="{{row.unitCode}}"    class="form-control " readonly="true"/>
					</td>
					
					<td>
						<input id="invCheckDetailList{{idx}}_checkQty" name="invCheckDetailList[{{idx}}].checkQty" type="text" value="{{row.checkQty}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
						<input id="invCheckDetailList{{idx}}_realPrice" name="invCheckDetailList[{{idx}}].realPrice" type="text" value="{{row.realPrice}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
						<input id="invCheckDetailList{{idx}}_realSum" name="invCheckDetailList[{{idx}}].realSum" type="text" value="{{row.realSum}}"    class="form-control " readonly="true"/>
					</td>
					
					<td>
                        <select id="invCheckDetailList{{idx}}_billStateFlag" name="invCheckDetailList[{{idx}}].billStateFlag" disabled="disabled" data-value="{{row.billStateFlag}}"  class="form-control m-b ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('ContractStateFlag')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					<td>
						<input id="invCheckDetailList{{idx}}_conBillNum" name="invCheckDetailList[{{idx}}].conBillNum" type="text" value="{{row.conBillNum}}"    class="form-control " readonly="true"/>
					</td>

					<td>
						<input id="invCheckDetailList{{idx}}_planBillNum" name="invCheckDetailList[{{idx}}].planBillNum" type="text" value="{{row.planBillNum}}"    class="form-control " readonly="true"/>
					</td>					
	                <td>
						<input id="invCheckDetailList{{idx}}_arriveDate" name="invCheckDetailList[{{idx}}].arriveDate" type="text" value="{{row.arriveDate}}"    class="form-control "  readonly="true"/>
					</td>
					<td class="text-center" width="10">
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var invCheckDetailRowIdx = 0, invCheckDetailTpl = $("#invCheckDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {								
					var data = ${fns:toJson(contractMain.invCheckDetailList)};
					for (var i=0; i<data.length; i++){
						console.log(data[i]);
						//alert(data[i]);
						addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl, data[i]);
						invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
					}
				});	
			</script>
			</div>
			
			
		</div>
		</div>
		<c:if test="${fns:hasPermission('contractmainquery:contractMain:edit') || isAdd}">
			<%-- 	<div class="col-lg-2"></div>
		        <div class="col-lg-3">
		             <div class="form-group text-center">
		                 <div>
		                 	<button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">结案</button>   		                 
		                 </div>
		             </div>
		        </div>
		        --%>
		        <div class="col-lg-3"></div>
		        <div class="col-lg-6">
		        <div class="form-group text-center">
		                 <div>
		                 	<a href= "${ctx}/contractmain/contractMainQuery" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在保存...">返回</a>                  
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
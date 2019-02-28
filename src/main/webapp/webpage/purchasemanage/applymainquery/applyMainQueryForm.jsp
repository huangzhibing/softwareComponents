<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购需求查询管理</title>
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
			
	        $('#makeDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#applyDate').datetimepicker({
				 format: "YYYY-MM-DD"
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/applymainquery/applyMainQuery"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="applyMainQuery" action="${ctx}/applymainquery/applyMainQuery/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
					<div class="col-sm-3">
						<form:input path="billNum" htmlEscape="false" readonly="true"   class="form-control required"/>
					</div>
				<label class="col-sm-2 control-label"><font color="red">*</font>单据状态：</label>
				<div class="col-sm-3">
					<form:input path="billStateFlag" htmlEscape="false"  readonly="true"  class="form-control required"/>
				</div>
			</div>


				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制定日期：</label>
					<div class="col-sm-3">
							<div class='input-group form_datetime' id='makeDate'>
			                    <input type='text'  name="makeDate" class="form-control required" readonly="true" value="<fmt:formatDate value="${applyMainQuery.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>业务日期：</label>
					<div class="col-sm-3">
							<div class='input-group form_datetime' id='applyDate'>
			                    <input type='text'  name="applyDate" class="form-control required" readonly="true" value="<fmt:formatDate value="${applyMainQuery.applyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
					</div>
				</div>
				<div class="form-group" >
					<label class="col-sm-2 control-label"><font color="red">*</font>需求类别编码：</label>
					<div class="col-sm-3">
						<form:input path="applyType.applytypeid" value="${applyMainQuery.applyType.applytypeid}"  htmlEscape="false"  readonly="true"  class="form-control required"/>
						</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>需求类别名称：</label>
					<div class="col-sm-3">
						<form:input path="applyName" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>需求部门编码：</label>
					<div class="col-sm-3">
					<form:input path="office.code" value="${applyMainQuery.office.code}"  htmlEscape="false"  readonly="true"  class="form-control required"/>
						</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>需求部门名称：</label>
					<div class="col-sm-3">
						<form:input path="applyDept" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>需求人员编码：</label>
					<div class="col-sm-3">
						<form:input path="user.no" value="${applyMainQuery.user.no}"  htmlEscape="false"  readonly="true"  class="form-control required"/>
						</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>需求人员名称：</label>
					<div class="col-sm-3">
						<form:input path="makeEmpname" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">需求说明：</label>
					<div class="col-sm-8">
						<form:input path="makeNotes" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">登陆人所在部门：</label>
					<div class="col-sm-8">
						<form:input path="userDeptCode" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">采购需求单查询：</a>
                </li>
                <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">采购计划单查询：</a>
                </li>
                <li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">采购合同单查询：</a>
                </li>
                <li class=""><a data-toggle="tab" href="#tab-4" aria-expanded="false">采购到货单查询：</a>
                </li>
                
            </ul>
            <div style="width:100%;  overflow-x:scroll; ">	
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<table class="table table-striped table-bordered table-condensed" style="min-width:200%;">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>序号</th>
						<th>物料编号</th>
						<th>物料名称</th>
						<th>规格型号</th>
						<th>需求日期</th>
						<th>需求数量</th>
						<th>库存量</th>
						<th>计量单位</th>
						<th>移动平均价</th>
						<th>金额</th>
						<th>数量修改历史</th>
						<th>物料材质</th>
						<th>说明</th>
						
					</tr>
				</thead>
				<tbody id="applyDetailQueryList">
				</tbody>
			</table>
			<script type="text/template" id="applyDetailQueryTpl">//<!--
				<tr id="applyDetailQueryList{{idx}}">
					<td class="hide">
						<input id="applyDetailQueryList{{idx}}_id" name="applyDetailQueryList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="applyDetailQueryList{{idx}}_delFlag" name="applyDetailQueryList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="applyDetailQueryList{{idx}}_serialNum" name="applyDetailQueryList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_item" name="applyDetailQueryList{{idx}}.item.code" type="text" value="{{row.item.code}}"  readonly="true"   class="form-control "/>
					
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_itemName" name="applyDetailQueryList[{{idx}}].itemName" type="text" value="{{row.itemName}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_itemSpecmodel" name="applyDetailQueryList[{{idx}}].itemSpecmodel" type="text" value="{{row.itemSpecmodel}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_requestDate" name="applyDetailQueryList[{{idx}}].requestDate" type="text" value="{{row.requestDate}}"  readonly="true"   class="form-control "/>
					
										            
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_applyQty" name="applyDetailQueryList[{{idx}}].applyQty" type="text" value="{{row.applyQty}}"  readonly="true"   class="form-control  isIntGteZero"/>
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_nowSum" name="applyDetailQueryList[{{idx}}].nowSum" type="text" value="{{row.nowSum}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_unitName" name="applyDetailQueryList[{{idx}}].unitName" type="text" value="{{row.unitName}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_costPrice" name="applyDetailQueryList[{{idx}}].costPrice" type="text" value="{{row.costPrice}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_applySum" name="applyDetailQueryList[{{idx}}].applySum" type="text" value="{{row.applySum}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_log" name="applyDetailQueryList[{{idx}}].log" type="text" value="{{row.log}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_itemTexture" name="applyDetailQueryList[{{idx}}].itemTexture" type="text" value="{{row.itemTexture}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailQueryList{{idx}}_notes" name="applyDetailQueryList[{{idx}}].notes" type="text" value="{{row.notes}}"  readonly="true"  class="form-control "/>
					</td>
					
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var applyDetailQueryRowIdx = 0, applyDetailQueryTpl = $("#applyDetailQueryTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(applyMainQuery.applyDetailQueryList)};
					for (var i=0; i<data.length; i++){
						addRow('#applyDetailQueryList', applyDetailQueryRowIdx, applyDetailQueryTpl, data[i]);
						applyDetailQueryRowIdx = applyDetailQueryRowIdx + 1;
					}
				});
			</script>
			</div>
				
				
				
				
				<!-- 2222222222222222222222222222222222222222222222222222222222222 -->
					<div id="tab-2" class="tab-pane fade">
			<table class="table table-striped table-bordered table-condensed" style="min-width:200%;">
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
						<th width="150px">数量修改历史</th>	
						<th width="150px">采购员</th>	
						<th width="150px">制单日期</th>
						<th width="150px">制单人</th>	
						<th width="150px">单据类型</th>	
						<th width="150px">单据说明</th>				
						<th width="10px">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="purPlanDetailList">
				</tbody>
			</table>
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
						<input id="purPlanDetailList{{idx}}_log" name="purPlanDetailList[{{idx}}].log" readOnly="true" type="text" value="{{row.log}}"    class="form-control "/>
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
				</tr>//-->
			</script>
			<script type="text/javascript">			  
				var purPlanDetailRowIdx = 0, purPlanDetailTpl = $("#purPlanDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				var purPlanData = ${fns:toJson(contractMain.purPlanDetailList)};
				$(document).ready(function() {
				//  var buyerId=$("#groupBuyer.id").val();
				var data = ${fns:toJson(applyMainQuery.purPlanDetailList)};
					for (var i=0; i<data.length; i++){
						data[i].requestDate=data[i].requestDate.split(" ")[0];
						data[i].billNum.makeDate=data[i].billNum.makeDate.split(" ")[0];
						addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, data[i]);
						purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
					}
				});
			</script>
			</div>
				
				
				<!-- 3333333333333333333333333333333333333333333333333333333333333333333333 -->
					<div id="tab-3" class="tab-pane fade">
			<table class="table table-striped table-bordered table-condensed" style="min-width:200%;">
				<thead>
					<tr>
						<th class="hide"></th>
						
						<th width="200px"><font color="red">*</font>物料编码</th>
						<th width="250px"><font color="red">*</font>物料名称</th>
						<th width="250px"><font color="red">*</font>物料规格</th>
						<th width="250px">物料材质</th>
						<th width="150px"><font color="red">*</font>签合同量</th>
						<th width="150px"><font color="red">*</font>无税单价</th>
						<th width="150px"><font color="red">*</font>无税总额</th>
						<th width="150px"><font color="red">*</font>含税单价</th>
						<th width="150px"><font color="red">*</font>含税金额</th>						
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
			<script type="text/template" id="contractDetailTpl">//<!--
				<tr id="contractDetailList{{idx}}">
					<td class="hide">
						<input id="contractDetailList{{idx}}_id" name="contractDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="contractDetailList{{idx}}_delFlag" name="contractDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
						<input id="contractDetailList{{idx}}_itemId" name="contractDetailList[{{idx}}].item.id" type="hidden" value="{{row.item.id}}"/>
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
					var data = ${fns:toJson(applyMainQuery.contractDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl, data[i]);
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
				
				
				<!-- 444444444444444444444444444444444444444444444444444444 -->
					<div id="tab-4" class="tab-pane fade">
			<table class="table table-striped table-bordered table-condensed" style="min-width:200%;">
				<thead>
					<tr>
						<th class="hide"></th>
						
						<th>物料编号</th>
						<th>物料名称</th>
						<th>物料规格</th>
						<th>单位</th>
						<th>到货数量</th>
						<th>实际单价</th>
						<th>实际金额</th>
						<th>日期</th>
						<th>到货单号</th>
						<th>到货状态</th>
						<th>合同号</th>
						<th>计划号</th>
						
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="invCheckDetailList">
				</tbody>
			</table>
			<script type="text/template" id="invCheckDetailTpl">//<!--
				<tr id="invCheckDetailList{{idx}}">
					<td class="hide">
						<input id="invCheckDetailList{{idx}}_id" name="invCheckDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="invCheckDetailList{{idx}}_delFlag" name="invCheckDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					
					
					
					<td>
						<sys:gridselect-purmul url="${ctx}/item/item/data" id="invCheckDetailList{{idx}}_item" name="invCheckDetailList[{{idx}}].item.id" value="{{row.item.id}}" labelName="invCheckDetailList{{idx}}.item.code" labelValue="{{row.item.code}}"
							 title="选择物料编号" cssClass="form-control required " targetId="itemName|itemSpecmodel|unitCode" targetField="name|specModel|unit"  fieldLabels="物料编码|物料名称" isMultiSelected="true" fieldKeys="code|name" searchLabels="物料编码|物料名称" searchKeys="code|name" ></sys:gridselect-purmul>
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
						<input id="invCheckDetailList{{idx}}_checkQty" name="invCheckDetailList[{{idx}}].checkQty" type="text" value="{{row.checkQty}}"    class="form-control " onchange="priceCalculate()"/>
					</td>
					
					
					<td>
						<input id="invCheckDetailList{{idx}}_realPrice" name="invCheckDetailList[{{idx}}].realPrice" type="text" value="{{row.realPrice}}"    class="form-control " onchange="priceCalculate()"/>
					</td>
					
					
					<td>
						<input id="invCheckDetailList{{idx}}_realSum" name="invCheckDetailList[{{idx}}].realSum" type="text" value="{{row.realSum}}"    class="form-control " readonly="true"/>
					</td>
					
                    <td>
						<input id="invCheckDetailList{{idx}}_arriveDate" name="invCheckDetailList[{{idx}}].arriveDate" type="text" value="{{row.arriveDate}}"    class="form-control "  onchange="priceCalculate()"/>
					</td>
					
					<td>
						<input id="invCheckDetailList{{idx}}_InvCheckMain" name="invCheckDetailList[{{idx}}].InvCheckMain.billnum" type="text" value="{{row.InvCheckMain.billnum}}"    class="form-control " readonly="true" />
					</td>
					
					
					<td>
						<input id="invCheckDetailList{{idx}}_billStateFlag" name="invCheckDetailList[{{idx}}].billStateFlag" type="text" value="{{row.billStateFlag}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
						<input id="invCheckDetailList{{idx}}_conBillNum" name="invCheckDetailList[{{idx}}].conBillNum" type="text" value="{{row.conBillNum}}"    class="form-control " onchange="priceCalculate()"/>
					</td>

					<td>
						<input id="invCheckDetailList{{idx}}_planBillNum" name="invCheckDetailList[{{idx}}].planBillNum" type="text" value="{{row.planBillNum}}"    class="form-control " onchange="priceCalculate()"/>
					</td>					
	
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#invCheckDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var invCheckDetailRowIdx = 0, invCheckDetailTpl = $("#invCheckDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {								
					var data = ${fns:toJson(applyMainQuery.invCheckDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl, data[i]);
						invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
					}
				});	
			</script>
			</div>
			
				
				
				
				
				
				
				
			</div>
		</div>
		<c:if test="${fns:hasPermission('applymainquery:applyMainQuery:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
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
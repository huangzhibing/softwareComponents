<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>单据管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			
	        $('#pbillDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#pickDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });

            if($('#contract\\.contractCode').val()){
                var loading = jp.loading("加载中");
                $.get("${ctx}/salcontract/contract/getDetail?id="+$('#contract\\.contractCode').val(), function(contract){
                    console.log(contract)
                    $('#contract\\.contractCode').val(contract.id)
                    console.log($('#contract\\.id').val())
                    $('#contract').val(contract.contractCode)

                    for(var i = 0;i < contract.ctrItemList.length;i++){
                        addRow('#ctrItemList',i+1,ctrItemTpl,contract.ctrItemList[i])
                    }
                    jp.close(loading)
                })
                jp.close(loading)
            }
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
				<a class="panelButton" href="${ctx}/pickbill/pickBill"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="pickBill" action="${ctx}/pickbill/pickBill/save" method="post" class="form-horizontal">
			<fieldset>
				<legend>${pickBill.act.taskName}</legend>
		<form:hidden path="id"/>
		<sys:message-type content="${message}"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>发货单编码：</label>
					<div class="col-sm-10">
						<form:input path="pbillCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">合同编码：</label>
					<div class="col-sm-10 ">

						<form:input readonly="true" cssStyle="display: none" path="contract.contractCode" htmlEscape="false"    class="form-control "/>
						<input type="text" readonly id="contract" class="form-control required" value="${pickBill.contract.contractCode}" placeholder="请点击选择合同"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='pbillDate'>
			                    <input type='text'  name="pbillDate" class="form-control required"  value="<fmt:formatDate value="${pickBill.pbillDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人编码：</label>
					<div class="col-sm-10">
						<sys:userselect id="pbillPerson" name="pbillPerson.id" value="${pickBill.pbillPerson.id}" labelName="pbillPerson.no" labelValue="${pickBill.pbillPerson.no}"
							    cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人姓名：</label>
					<div class="col-sm-10">
						<form:input path="pbillPsnName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">收货客户编码：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/account/account/data" id="rcvAccount" name="rcvAccount.id" value="${pickBill.rcvAccount.id}" labelName="rcvAccount.accountCode" labelValue="${pickBill.rcvAccount.accountCode}"
							 title="选择收货客户编码" cssClass="form-control required" fieldLabels="客户编码|客户名称" fieldKeys="accountCode|accountName" searchLabels="客户编码|客户名称" searchKeys="accountCode|accountName" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">收货客户名称：</label>
					<div class="col-sm-10">
						<form:input path="rcvAccountName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">收货地址：</label>
					<div class="col-sm-10">
						<form:input path="rcvAddr" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">收货人：</label>
					<div class="col-sm-10">
						<form:input path="rcvPerson" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">收货人电话：</label>
					<div class="col-sm-10">
						<form:input path="rcvTel" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>发货单状态：</label>
					<div class="col-sm-10">
						<form:select path="pbillStat" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('pbillStat')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">承运人：</label>
					<div class="col-sm-10">
						<form:input path="transAccount" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">发货日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='pickDate'>
			                    <input type='text'  name="pickDate" class="form-control "  value="<fmt:formatDate value="${pickBill.pickDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">质检标志：</label>
					<div class="col-sm-10">
						<form:select path="qmsFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('qmsFlagSal')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">仓库编号：</label>
					<div class="col-sm-10">
						<form:input path="ware.id" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">仓库名称：</label>
					<div class="col-sm-10">
						<form:input path="wareName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">出库类型：</label>
					<div class="col-sm-10">
						<form:input path="io.id" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">出库类型描述：</label>
					<div class="col-sm-10">
						<form:input path="iodesc" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">生成出库单标记：</label>
					<div class="col-sm-10">
						<form:select path="passFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('passFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">出货单编码：</label>
					<div class="col-sm-10">
						<form:input path="outBillCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">销售发货单明细：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">销售合同明细：</a>
				</li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#pickBillItemList', pickBillItemRowIdx, pickBillItemTpl);pickBillItemRowIdx = pickBillItemRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
					<div class="table-responsive" style="max-height:500px">
						<table class="table table-striped table-bordered table-condensed" style="min-width:1350px">                                        <thead>

						<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>序号</th>
						<th>订单编号</th>
						<th>产品编码</th>
						<th>产品名称</th>
						<th>产品规格</th>
						<th>单位</th>
						<th>发货量</th>
						<th>税率</th>
						<th>无税单价</th>
						<th>无税总额</th>
						<th>含税单价</th>
						<th>含税总额</th>
						<th>运费价格</th>
						<th>运费总额</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="pickBillItemList">
				</tbody>
			</table>
					</div>
			<script type="text/template" id="pickBillItemTpl">//<!--
				<tr id="pickBillItemList{{idx}}">
					<td class="hide">
						<input id="pickBillItemList{{idx}}_id" name="pickBillItemList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="pickBillItemList{{idx}}_delFlag" name="pickBillItemList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="pickBillItemList{{idx}}_seqId" name="pickBillItemList[{{idx}}].seqId" type="text" value="{{row.seqId}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_itemCode" name="pickBillItemList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<sys:gridselect url="${ctx}/product/product/data" id="pickBillItemList{{idx}}_prodCode" name="pickBillItemList[{{idx}}].prodCode.id" value="{{row.prodCode.id}}" labelName="pickBillItemList{{idx}}.prodCode.item.code" labelValue="{{row.prodCode.item.code}}"
							 title="选择产品编码" cssClass="form-control  " fieldLabels="产品编码" fieldKeys="item.code" searchLabels="产品编码" searchKeys="item.code" ></sys:gridselect>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_prodName" name="pickBillItemList[{{idx}}].prodName" type="text" value="{{row.prodName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_prodSpec" name="pickBillItemList[{{idx}}].prodSpec" type="text" value="{{row.prodSpec}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_unitName" name="pickBillItemList[{{idx}}].unitName" type="text" value="{{row.unitName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_pickQty" name="pickBillItemList[{{idx}}].pickQty" type="text" value="{{row.pickQty}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_taxRatio" name="pickBillItemList[{{idx}}].taxRatio" type="text" value="{{row.taxRatio}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_prodPrice" name="pickBillItemList[{{idx}}].prodPrice" type="text" value="{{row.prodPrice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_prodSum" name="pickBillItemList[{{idx}}].prodSum" type="text" value="{{row.prodSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_prodPriceTaxed" name="pickBillItemList[{{idx}}].prodPriceTaxed" type="text" value="{{row.prodPriceTaxed}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_prodSumTaxed" name="pickBillItemList[{{idx}}].prodSumTaxed" type="text" value="{{row.prodSumTaxed}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_transPrice" name="pickBillItemList[{{idx}}].transPrice" type="text" value="{{row.transPrice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="pickBillItemList{{idx}}_transSum" name="pickBillItemList[{{idx}}].transSum" type="text" value="{{row.transSum}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#pickBillItemList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var pickBillItemRowIdx = 0, pickBillItemTpl = $("#pickBillItemTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(pickBill.pickBillItemList)};
					for (var i=0; i<data.length; i++){
						addRow('#pickBillItemList', pickBillItemRowIdx, pickBillItemTpl, data[i]);
						pickBillItemRowIdx = pickBillItemRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-2" class="tab-pane fade">
					<%--<a class="btn btn-white btn-sm" onclick="addRow('#ctrItemList', ctrItemRowIdx, ctrItemTpl);ctrItemRowIdx = ctrItemRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
					<table class="table table-striped table-bordered table-condensed">
						<thead>
						<tr>
							<th class="hide"></th>
							<th>订单编号</th>
							<th><font color="red">*</font>产品编码</th>
							<th><font color="red">*</font>产品名称</th>
							<th><font color="red">*</font>规格型号</th>
							<th>单位</th>
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
					<script type="text/template" id="ctrItemTpl">//<!--
				<tr id="ctrItemList{{idx}}">
					<td class="hide">
						<input id="ctrItemList{{idx}}_id" name="ctrItemList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="ctrItemList{{idx}}_delFlag" name="ctrItemList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<input id="ctrItemList{{idx}}_itemCode" name="ctrItemList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"    class="form-control required"/>
					</td>
					<%--<td>--%>
						<%--<sys:gridselect-contract--%>
							<%--targetId="" targetField=""--%>
							<%--extraField="ctrItemList{{idx}}_prodCode:id;ctrItemList{{idx}}_prodName:itemNameRu;ctrItemList{{idx}}_prodSpec:itemSpec;ctrItemList{{idx}}_unitName:itemMeasure"--%>
							<%--url="${ctx}/product/product/data" id="ctrItemList{{idx}}_prodCode" name="ctrItemList{{idx}}.prodCode" value="{{row.prodCode.id}}" labelName="ctrItemList{{idx}}.prodCode.item.code" labelValue="{{row.prodCode.item.code}}"--%>
							 <%--title="选择产品代码" cssClass="form-control required " fieldLabels="产品代码|产品名称|产品规格|单位" fieldKeys="item.code|itemNameRu|itemSpec|itemMeasure" searchLabels="产品代码|产品名称" searchKeys="item.code|itemNameRu" ></sys:gridselect-contract>--%>
					<%----%>
					<%--</td>--%>

					<td>
						<input id="ctrItemList{{idx}}_prodCode" name="ctrItemList[{{idx}}].prodCode" type="text" value="{{row.prodCode}}"    class="form-control "/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_prodName" name="ctrItemList[{{idx}}].prodName" type="text" value="{{row.prodName}}"    class="form-control "/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_prodSpec" name="ctrItemList[{{idx}}].prodSpec" type="text" value="{{row.prodSpec}}"    class="form-control "/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_unitName" name="ctrItemList[{{idx}}].unitName" type="text" value="{{row.unitName}}"    class="form-control "/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_prodQty" name="ctrItemList[{{idx}}].prodQty" type="text" value="{{row.prodQty}}"    class="form-control  isFloatGteZero"/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_prodPrice" name="ctrItemList[{{idx}}].prodPrice" type="text" value="{{row.prodPrice}}"    class="form-control  isFloatGteZero"/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_prodSum" name="ctrItemList[{{idx}}].prodSum" type="text" value="{{row.prodSum}}"    class="form-control  isFloatGteZero"/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_prodPriceTaxed" name="ctrItemList[{{idx}}].prodPriceTaxed" type="text" value="{{row.prodPriceTaxed}}"    class="form-control  isFloatGteZero"/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_prodSumTaxed" name="ctrItemList[{{idx}}].prodSumTaxed" type="text" value="{{row.prodSumTaxed}}"    class="form-control  isFloatGteZero"/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_pickQty" name="ctrItemList[{{idx}}].pickQty" type="text" value="{{row.pickQty}}"    class="form-control  isFloatGteZero"/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_transPrice" name="ctrItemList[{{idx}}].transPrice" type="text" value="{{row.transPrice}}"    class="form-control  isFloatGteZero"/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_transSum" name="ctrItemList[{{idx}}].transSum" type="text" value="{{row.transSum}}"    class="form-control  isFloatGteZero"/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_periodId" name="ctrItemList[{{idx}}].periodId" type="text" value="{{row.periodId}}"    class="form-control "/>
					</td>


					<td>
						<input id="ctrItemList{{idx}}_remark" name="ctrItemList[{{idx}}].remark" type="text" value="{{row.remark}}"    class="form-control "/>
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
				<act:flowChart procInsId="${pickBill.act.procInsId}"/>
				<act:histoicFlow procInsId="${pickBill.act.procInsId}"/>

			</fieldset>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
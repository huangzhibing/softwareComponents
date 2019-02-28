<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>内部订单管理</title>
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
	        $('#sendDate').datetimepicker({
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
				document.getElementById("orderCodeName").value="正在录入";
			}else{
				document.getElementById("orderCodeName").value="修改";
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
				<a class="panelButton" href="${ctx}/salorder/salOrder"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="salOrder" action="${ctx}/salorder/salOrder/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>订单编码：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="orderCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>订单状态：</label>
					<%-- <div class="col-sm-10">
						<form:select path="orderStat" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('orderStat')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div> --%>
					<div  class="col-sm-10">
						<form:input id="orderCodeName"  readOnly="true" path="" htmlEscape="false"    class="form-control required"/>
					</div>
					<div style="display:none;" class="col-sm-10">
						<form:input id="orderStat" value="W" readOnly="true" path="orderStat" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>合同编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-salorder targetId="" targetField="" extraField="signDated:signDate;accountCoded:account.accountCode;accountNamed:account.accountName;deliveryDated:deliveryDate" 
						url="${ctx}/salcontract/contract/data?status=E" id="contract" name="contract.id" value="${salOrder.contract.id}" labelName="contract.contractCode" labelValue="${salOrder.contract.contractCode}"
							 title="选择合同编码" cssClass="form-control required" fieldLabels="合同编码|签订日期" fieldKeys="contractCode|signDate" searchLabels="合同编码|签订日期" searchKeys="contractCode|signDate" ></sys:gridselect-salorder>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>签订日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='signDate'>
			                    <input type='text' id="signDated" name="signDate" class="form-control required"  value="<fmt:formatDate value="${salOrder.signDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>客户编码：</label>
					<div class="col-sm-10">
						<form:input readOnly="true"  id="accountCoded" path="accountCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>客户名称：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" id="accountNamed" path="accountName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='inputDate'>
			                    <input readOnly="true" type='text'  name="inputDate" class="form-control required"  value="<fmt:formatDate value="${salOrder.inputDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
						<form:input readOnly="true" path="inputPerson.no" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div style="display:none;">
				<form:input readOnly="true" path="inputPerson.id" htmlEscape="false"    class="form-control required"/>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人姓名：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="inputPerson.name" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>交货日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='sendDate'>
			                    <input  type='text' id="deliveryDated"  name="sendDate" class="form-control required"  value="<fmt:formatDate value="${salOrder.sendDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">内部定单会签表：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">内部订单子表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#salOrderAuditList', salOrderAuditRowIdx, salOrderAuditTpl);salOrderAuditRowIdx = salOrderAuditRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<!-- <th>备注信息</th> -->
						<th><font color="red">*</font>会签编码</th>
						<th><font color="red">*</font>会签人</th>
						<th><font color="red">*</font>会签日期</th>
						<th><font color="red">*</font>会签意见</th>
						<th>状态</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="salOrderAuditList">
				</tbody>
			</table>
			<script type="text/template" id="salOrderAuditTpl">//<!--
				<tr id="salOrderAuditList{{idx}}">
					<td class="hide">
						<input id="salOrderAuditList{{idx}}_id" name="salOrderAuditList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="salOrderAuditList{{idx}}_delFlag" name="salOrderAuditList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<!--<td>
						<textarea id="salOrderAuditList{{idx}}_remarks" name="salOrderAuditList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>-->
					
					
					<td>
						<input  id="salOrderAuditList{{idx}}_auditCode" name="salOrderAuditList[{{idx}}].auditCode" type="text" value="{{row.auditCode}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="salOrderAuditList{{idx}}_auditPerson" name="salOrderAuditList[{{idx}}].auditPerson" type="text" value="{{row.auditPerson}}"    class="form-control  required"/>
					</td>
					
					
					<td>
						<div class='input-group form_datetime' id="salOrderAuditList{{idx}}_auditDate">
		                    <input type='text'  name="salOrderAuditList[{{idx}}].auditDate" class="form-control  required"  value="{{row.auditDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>
					
					
					<td>
						<input id="salOrderAuditList{{idx}}_auditNote" name="salOrderAuditList[{{idx}}].auditNote" type="text" value="{{row.auditNote}}"    class="form-control  required"/>
					</td>
					
					
					<td>
						<select disabled="disabled" id="salOrderAuditList{{idx}}_auditStat" name="salOrderAuditList[{{idx}}].auditStat" data-value="0" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('auditStat')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#salOrderAuditList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var salOrderAuditRowIdx = 0, salOrderAuditTpl = $("#salOrderAuditTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(salOrder.salOrderAuditList)};
					for (var i=0; i<data.length; i++){
						addRow('#salOrderAuditList', salOrderAuditRowIdx, salOrderAuditTpl, data[i]);
						salOrderAuditRowIdx = salOrderAuditRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-2" class="tab-pane fade">
			<!-- <a class="btn btn-white btn-sm" onclick="addRow('#salOrderItemList', salOrderItemRowIdx, salOrderItemTpl);salOrderItemRowIdx = salOrderItemRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a> -->
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>序号</th>
						<th>产品编码</th>
						<th>产品名称</th>
						<th>规格型号</th>
						<th>单位名称</th>
						<th>签订量</th>
						<th>无税单价</th>
						<th>无税总额</th>
						<th>含税单价</th>
						<th>含税金额</th>
						<!-- <th width="10">&nbsp;</th> -->
					</tr>
				</thead>
				<tbody id="contractChild-2-List">
						</tbody>
			</table>
			<script type="text/template" id="salOrderItemTpl">//<!--
				<tr id="salOrderItemList{{idx}}">
					<td class="hide">
						<input id="salOrderItemList{{idx}}_id" name="salOrderItemList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="salOrderItemList{{idx}}_delFlag" name="salOrderItemList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input readOnly="true" id="salOrderItemList{{idx}}_seqId" name="salOrderItemList[{{idx}}].seqId" type="text" value="{{row.seqId}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="true" id="salOrderItemList{{idx}}_prodCode" name="salOrderItemList[{{idx}}].prodCode.id" type="text" value="{{row.prodCode.id}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="true" id="salOrderItemList{{idx}}_prodName" name="salOrderItemList[{{idx}}].prodName" type="text" value="{{row.prodName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="true" id="salOrderItemList{{idx}}_prodSpec" name="salOrderItemList[{{idx}}].prodSpec" type="text" value="{{row.prodSpec}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="true" id="salOrderItemList{{idx}}_unitName" name="salOrderItemList[{{idx}}].unitName" type="text" value="{{row.unitName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input  id="salOrderItemList{{idx}}_prodQty" name="salOrderItemList[{{idx}}].prodQty" type="text" value="{{row.prodQty}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input  id="salOrderItemList{{idx}}_prodPrice" name="salOrderItemList[{{idx}}].prodPrice" type="text" value="{{row.prodPrice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input  id="salOrderItemList{{idx}}_prodSum" name="salOrderItemList[{{idx}}].prodSum" type="text" value="{{row.prodSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input  id="salOrderItemList{{idx}}_prodPriceTaxed" name="salOrderItemList[{{idx}}].prodPriceTaxed" type="text" value="{{row.prodPriceTaxed}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input  id="salOrderItemList{{idx}}_prodSumTaxed" name="salOrderItemList[{{idx}}].prodSumTaxed" type="text" value="{{row.prodSumTaxed}}"    class="form-control "/>
					</td>
					
					<!--<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#salOrderItemList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>-->
				</tr>//-->
			</script>
			<script type="text/javascript">
				var salOrderItemRowIdx = 0, salOrderItemTpl = $("#salOrderItemTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(salOrder.salOrderItemList)};
					console.log(data);
					for (var i=0; i<data.length; i++){
						addRow('#contractChild-2-List', salOrderItemRowIdx, salOrderItemTpl, data[i]);
						salOrderItemRowIdx = salOrderItemRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('salorder:salOrder:edit') || isAdd}">
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
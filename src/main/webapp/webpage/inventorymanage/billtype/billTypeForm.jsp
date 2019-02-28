<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>单据类型定义管理</title>
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
</head>
<body onload="load()">
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/billtype/billType"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="billType" action="${ctx}/billtype/billType/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>出入库类型：</label>
					<div class="col-sm-10">
						<form:input id="iotype" path="ioType" htmlEscape="false"    class="form-control required" onblur="chk(this.value)"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>出入库标识：</label>
					<div class="col-sm-10">
						<form:select path="ioFlag" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('billType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>描述：</label>
					<div class="col-sm-10">
						<form:input path="ioDesc" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响现有量：</label>
					<div class="col-sm-10">
						<form:select path="currQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响总现有量：</label>
					<div class="col-sm-10">
						<form:select path="currTotalQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响期初量：</label>
					<div class="col-sm-10">
						<form:select path="beginQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响累计入库：</label>
					<div class="col-sm-10">
						<form:select path="inQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响累计出库：</label>
					<div class="col-sm-10">
						<form:select path="outQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响废品量：</label>
					<div class="col-sm-10">
						<form:select path="wasteQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响待验量：</label>
					<div class="col-sm-10">
						<form:select path="waitQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响已分配量：</label>
					<div class="col-sm-10">
						<form:select path="assignQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响车间任务：</label>
					<div class="col-sm-10">
						<form:select path="wshopQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响车间领用：</label>
					<div class="col-sm-10">
						<form:select path="wshopUse" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响生产成本：</label>
					<div class="col-sm-10">
						<form:select path="wshopCost" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响采购任务：</label>
					<div class="col-sm-10">
						<form:select path="purAssign" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响销售订单：</label>
					<div class="col-sm-10">
						<form:select path="sellOrder" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响移动平均价：</label>
					<div class="col-sm-10">
						<form:select path="costPrice" class="form-control required">
							 
							<form:options items="${fns:getDictList('influenceCostPrice')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">科目代码：</label>
					<div class="col-sm-10">
						<form:input path="accountId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">科目名称：</label>
					<div class="col-sm-10">
						<form:input path="accountName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响调拨入库：</label>
					<div class="col-sm-10">
						<form:select path="dinQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响调拨出库：</label>
					<div class="col-sm-10">
						<form:select path="doutQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响盘盈入库：</label>
					<div class="col-sm-10">
						<form:select path="pinQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响盘亏出库：</label>
					<div class="col-sm-10">
						<form:select path="poutQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响调整入库：</label>
					<div class="col-sm-10">
						<form:select path="tinQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>影响调整出库：</label>
					<div class="col-sm-10">
						<form:select path="toutQty" class="form-control required">
							 
							<form:options items="${fns:getDictList('billInfluence')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:textarea path="note" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">单据类型与仓库关系表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#billTypeWareHouseList', billTypeWareHouseRowIdx, billTypeWareHouseTpl);billTypeWareHouseRowIdx = billTypeWareHouseRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>库房号</th>
						<th>库房名称</th>
						<th>管理标识</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="billTypeWareHouseList">
				</tbody>
			</table>
			<script type="text/template" id="billTypeWareHouseTpl">//<!--
				<tr id="billTypeWareHouseList{{idx}}">
					<td class="hide">
						<input id="billTypeWareHouseList{{idx}}_id" name="billTypeWareHouseList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="billTypeWareHouseList{{idx}}_delFlag" name="billTypeWareHouseList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<sys:gridselect-new targetId="billTypeWareHouseList{{idx}}_wareName" targetField="wareName"
							 extraField="billTypeWareHouseList{{idx}}_adminMode:adminMode" 
							 url="${ctx}/warehouse/wareHouse/data" id="billTypeWareHouseList{{idx}}_wareHouse" name="billTypeWareHouseList[{{idx}}].wareHouse.wareID" value="{{row.wareHouse.wareID}}" labelName="billTypeWareHouseList{{idx}}.wareHouse.wareID" labelValue="{{row.wareHouse.wareID}}"
							 title="选择库房号" cssClass="form-control  " fieldLabels="库房号|库房名称|管理标识" fieldKeys="wareID|wareName|adminMode" searchLabels="库房号|库房名称" searchKeys="wareID|wareName" ></sys:gridselect-new>
					</td>
					
					<input id="billTypeWareHouseList{{idx}}_wareName" name="billTypeWareHouseList{{idx}}_wareName" value="{{row.wareHouse.wareID}}"/>
					<td>
						<input readOnly="true" id="billTypeWareHouseList{{idx}}_wareName" name="billTypeWareHouseList[{{idx}}].wareName" type="text" value="{{row.wareName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="true" id="billTypeWareHouseList{{idx}}_adminMode" name="billTypeWareHouseList[{{idx}}].adminMode" type="text" value="{{row.adminMode}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#billTypeWareHouseList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var billTypeWareHouseRowIdx = 0, billTypeWareHouseTpl = $("#billTypeWareHouseTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(billType.billTypeWareHouseList)};
					for (var i=0; i<data.length; i++){
						addRow('#billTypeWareHouseList', billTypeWareHouseRowIdx, billTypeWareHouseTpl, data[i]);
						billTypeWareHouseRowIdx = billTypeWareHouseRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('billtype:billType:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button id="submitbtn" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
<script type="text/javascript">
	var flag=1;
	$(document).ready(function(){
		if("${isAdd}"=="true"){
			flag=0;
		}else{
			document.getElementById("iotype").disabled="true";
		}
	})
	function chk(a){
		if(flag==0){
			var tableName="inv_bill_type";
			var fieldName="io_type";
			var tableValue=a
			var par={tableName:tableName,fieldName:fieldName,fieldValue:tableValue};
			var url="${ctx}/common/chkCode";
			$.post(url,par,function(data){console.log(data);
				if(data=="false"){
					layer.alert("出入库类型已存在")
					document.getElementById("submitbtn").disabled="disabled";
				}else{
					document.getElementById("submitbtn").disabled="";
				}
			})
		}
		
	}
</script>
</body>
</html>
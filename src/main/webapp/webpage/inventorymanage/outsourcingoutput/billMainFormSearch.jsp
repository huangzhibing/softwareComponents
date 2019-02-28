<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>单据管理</title>
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
			
	        $('#billDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#recDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#operDate').datetimepicker({
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
	
		function getSum(value,id){
			var index=id.indexOf('_');
			var pre=id.substring(0,index);
			var resid=pre+"_itemSum";
			var priceid=pre+"_itemPrice";
			var itemSum=document.getElementById(resid);
			var price=document.getElementById(priceid).value;
			itemSum.value=price*value;
		}
		function calSum(value,id){
			var index=id.indexOf('_');
			var pre=id.substring(0,index);
			var resid=pre+"_itemSum";
			var priceid=pre+"_itemQty";
			var itemSum=document.getElementById(resid);
			var price=document.getElementById(priceid).value;
			itemSum.value=price*value;
		}
		var flag=1;
		$(document).ready(function(){
			if("${isAdd}"=="true"){
				flag=0;
			}else{
				
				//document.getElementById("iotype").disabled="true";
			}
		})
		function chk(a){
			if(flag==0){
				var tableName="inv_billdetail";
				var fieldName="serial_num";
				var tableValue=a
				var par={tableName:tableName,fieldName:fieldName,fieldValue:tableValue};
				var url="${ctx}/common/chkCode";
				$.post(url,par,function(data){console.log(data);
					if(data=="false"){
						layer.alert("序号已存在")
						document.getElementById("submitBtn").disabled="disabled";
					}else{
						document.getElementById("submitBtn").disabled="";
					}
				})
			}
			
		}
		var isMoved=0;
	function move(){
		if(flag==1){
			var arrs=document.getElementsByTagName("input");
			for(var i=0;i<arrs.length;i++){
				if(arrs[i].id.indexOf("serialNum")!=-1){
					
					arrs[i].disabled=true;
				}
			}
			if(isMoved==0){
				isMoved=1;
				
			}
			
		}
		
	}
		function isAllow(value,id){
			var subFlag=document.getElementById("subFlag").value;
			var qty=document.getElementById(id+"1").value;
			console.log(qty);
			if(subFlag==0){
				if(value>qty){
					layer.alert("不允许超过可用量");
					document.getElementById("submitBtn").disabled="true";
				}else{console.log("SS")
					document.getElementById("submitBtn").disabled="";
				}
			}
		}
		function getSum(id){
			var price=id+"_itemPrice";
			var itemqty=id+"_itemQty";
			var price=document.getElementById(price).value;
			var qty=document.getElementById(itemqty).value;
			document.getElementById(id+"_itemSum").value=price*qty;
		}
</script>
</head>
<body >
<div class="wrapper wrapper-content" onmousemove="move()">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/outsourcingoutput/outsourcingOutput/searchList"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="billMain" action="" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据号：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="billNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据状态：</label>
					<div class="col-sm-10">
						<form:select path="billFlag" class="form-control required" disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('billFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据来源：</label>
					<div class="col-sm-10">
					<c:if test = "${billMain.billSource eq 'L'  }">
						<input  class="form-control" name="billSource" readOnly="true" value="自动生成"/>
					</c:if>
					<c:if test = "${billMain.billSource eq 'M' || billMain.billSource eq null }">
						<input  class="form-control" name="billSource" readOnly="true" value="手动输入"/>
					</c:if>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>出库日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='billDate'>
			                    <input readOnly="true" type='text'  name="billDate" class="form-control required"  value="<fmt:formatDate value="${billMain.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>出库部门编码：</label>
					<div class="col-sm-10">
						<sys:treeselect-modify disabled="disabled" targetId="deptd" targetField="code" id="dept" name="dept.id" value="${billMain.dept.id}" labelName="dept.code" labelValue="${billMain.dept.code}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required"  allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>出库部门名称：</label>
					<div class="col-sm-10">
						<form:input readonly="true" id="deptd" path="deptName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库房号：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disabled="disabled" targetId="wared" targetField="wareName" 
						extraField="subFlag:subFlag"
						url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${billMain.ware.id}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
							 title="选择库房号" cssClass="form-control required" fieldLabels="库房号|库房名称" fieldKeys="wareID|wareName" searchLabels="库房号|库房名称" searchKeys="wareID|wareName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库房名称：</label>
					<div class="col-sm-10">
						<form:input readonly="true" id="wared" path="wareName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<input id="subFlag" style="display:none;"/>
				 <div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>经办人编码：</label>
					<div class="col-sm-10">
						<%-- <sys:userselect-modify targetId="billPersond" id="billPerson" name="billPerson.id" value="${billMain.billPerson.id}" labelName="billPerson.no" labelValue="${billMain.billPerson.no}"
							    cssClass="form-control required"/> --%>
						<form:input placeHolder="点击获取经办人"  readOnly="true"  id="billPersonId"  value="${billMain.billPerson.no}" path="billPerson.no"
							    cssClass="form-control required"/>
					</div>
				</div> 
				<form:input path="billPerson.id" style="display:none;"  id="billPerson" name="billPerson.id" value="${billMain.billPerson.id}" labelName="billPerson.no" labelValue="${billMain.billPerson.no}"
							    cssClass="form-control required"/>
				 <div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>经办人名字：</label>
					<div class="col-sm-10">
						<form:input  readonly="true" id="billPersond" path="billPerson.name" htmlEscape="false"    class="form-control required"/>
					</div>
				</div> 
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库管员代码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disabled="disabled" labelField="user.no" targetId="wareEmpname" targetField="empName" url="${ctx}/employee/employee/data" id="wareEmp" name="wareEmp.id" value="${billMain.wareEmp.id}" labelName="wareEmp.user.no" labelValue="${billMain.wareEmp.user.no}"
							 title="选择库管员代码" cssClass="form-control required" fieldLabels="库管员代码|库管员名称" fieldKeys="user.no|empName" searchLabels="库管员代码|库管员名称" searchKeys="user.no|empName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库管员名称：</label>
					<div class="col-sm-10">
						<form:input readonly="true" path="wareEmpname" htmlEscape="false"  value="${billMain.wareEmp.empName}"   class="form-control required"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>用途代码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disabled="disabled" targetId="used" targetField="useDesc" url="${ctx}/use/use/data" id="invuse" name="invuse.id" value="${billMain.invuse.id}" labelName="invuse.useId" labelValue="${billMain.invuse.useId}"
							 title="选择用途/车间班组代码" cssClass="form-control required" fieldLabels="用途/车间班组代码|用途/车间班组名称" fieldKeys="useId|useDesc" searchLabels="用途/车间班组代码|用途/车间班组名称" searchKeys="useId|useDesc" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>用途名称：</label>
					<div class="col-sm-10">
						<form:input readonly="true" id="used" path="useName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>核算期间：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="period.periodId" htmlEscape="false"    class="form-control required"/>
					</div>
					<form:input style="display:none;" readOnly="true" path="period.id" htmlEscape="false"    class="form-control required"/>
				</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">是否是良品：</label>
				<div class="col-sm-10">
					<form:select path="isGood" class="form-control" disabled="true">
						<form:option value="" label=""/>
						<form:option value="0" label="不良品"/>
						<form:option value="1" label="良品"/>
					</form:select>
				</div>
			</div>
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">外购件出库单据明细：</a>
                </li>
                <li><a data-toggle="tab" href="#tab-2" aria-expanded="true">二维码信息表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<!-- <a class="btn btn-white btn-sm" onclick="addRow('#billDetailList', billDetailRowIdx, billDetailTpl);billDetailRowIdx = billDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a> -->
			<div class="table-responsive" style="max-height:500px">
			<table class="table table-striped table-bordered table-condensed"  style="min-width:1350px">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>序号</th>
						<th style="width:180px;"><font color="red">*</font>物料代码</th>
						<th style="width:180px;"><font color="red">*</font>物料名称</th>
						<th>货区号</th>
						<th>货区名称</th>
						<th>货位号</th>
						<th>货位名称</th>
						

						<th><font color="red">*</font>物料规格</th>
						<th>批次号</th>
						
						<th>计量单位</th>
						<th>实际单价</th>
						<th><font color="red">*</font>数量</th>
						
						<th>实际金额</th>
						
						
						<th>计划单价</th>
						
						
						
						
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="billDetailList">
				</tbody>
			</table>
			</div>
			<script type="text/template" id="billDetailTpl">//<!--
				<tr id="billDetailList{{idx}}" onmousemove="getSum(this.id)">
					<td class="hide">
						<input id="billDetailList{{idx}}_id" name="billDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="billDetailList{{idx}}_delFlag" name="billDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input readOnly="true" onblur="chk(this.value)" id="billDetailList{{idx}}_serialNum" name="billDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>
					
					<td>
						<sys:gridselect-again disabled="disabled"
							targetId="" targetField="" 
							extraField="billDetailList{{idx}}_accountId:id;billDetailList{{idx}}_itemBatch:itemBatch;billDetailList{{idx}}_item:item.id;billDetailList{{idx}}_itemName:item.name;billDetailList{{idx}}_planPrice:item.planPrice;billDetailList{{idx}}_itemPrice:costPrice;billDetailList{{idx}}_itemQty:realQty;billDetailList{{idx}}_itemQty1:realQty;billDetailList{{idx}}_itemSpec:item.specModel;billDetailList{{idx}}_measUnit:item.unitCode.unitName;billDetailList{{idx}}_planPrice:item.planPrice;billDetailList{{idx}}_bin:bin.id;billDetailList{{idx}}_binId:bin.binId;billDetailList{{idx}}_binName:bin.binDesc;billDetailList{{idx}}_loc:loc.id;billDetailList{{idx}}_locId:loc.locId;billDetailList{{idx}}_locName:loc.locDesc" 
							url="${ctx}/invaccount/invAccount/data" id="billDetailList{{idx}}_item" name="billDetailList{{idx}}.item.id" value="{{row.item.id}}" labelName="billDetailList{{idx}}.item.code" labelValue="{{row.item.id}}"
							 title="选择物料代码" cssClass="form-control required " fieldLabels="物料代码|物料名称|物料规格|数量" fieldKeys="item.code|item.name|item.specModel|realQty" searchLabels="物料代码|物料名称|货区号|货区名|货位号|货位名" searchKeys="item.code|item.name|bin.binId|bin.binDesc|loc.locId|locDesc" ></sys:gridselect-again>
					</td>
					<td>
                        <input readOnly="readOnly" id="billDetailList{{idx}}_itemName" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
                    </td>
					<td style="display:none;">
						<input  readOnly="readOnly" id="billDetailList{{idx}}_accountId" name="billDetailList[{{idx}}].accountId" class="form-control  " value="{{row.accountId}}" />
					</td>
					<td>
						<input  readOnly="readOnly" id="billDetailList{{idx}}_binId" name="billDetailList[{{idx}}].bin.binId" class="form-control  " value="{{row.bin.id}}" />
					</td>
					
					<td style="display:none;">
						<input  readOnly="readOnly" id="billDetailList{{idx}}_bin" name="billDetailList[{{idx}}].bin.id" class="form-control  " value="{{row.bin.id}}" />
					</td>
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_binName" name="billDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_locId" name="billDetailList[{{idx}}].loc.locId" type="text" value="{{row.loc.id}}"    class="form-control "/>
					</td>
					<td style="display:none;">
						<input readOnly="readOnly" id="billDetailList{{idx}}_loc" name="billDetailList[{{idx}}].loc.id" type="text" value="{{row.loc.id}}"    class="form-control "/>
					</td>
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_locName" name="billDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"    class="form-control "/>
					</td>
					
					
					
					
					

					<td style="display:none;">
						<input readOnly="readOnly" id="billDetailList{{idx}}_item" name="billDetailList[{{idx}}].item.id" type="text" value="{{row.item.id}}"    class="form-control "/>
					</td>
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_itemSpec" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_itemBatch" name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>
					
					
					
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_measUnit" name="billDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="true" onkeyup="calSum(this.value,this.id)"  id="billDetailList{{idx}}_itemPrice" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"    class="form-control"/>
					</td>
					
					<td>
						<input readOnly="true" onkeyup="isAllow(this.value,this.id)" onkeyup="getSum(this.value,this.id)" id="billDetailList{{idx}}_itemQty" name="billDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control required"/>
					</td>
					<td style="display:none">
						<input   id="billDetailList{{idx}}_itemQty1" name="billDetailList[{{idx}}].itemQty1" type="text" value="{{row.itemQty1}}"    class="form-control "/>
					</td>
					
					
					
					
					
					<td>
						<input  readOnly="true" id="billDetailList{{idx}}_itemSum" name="billDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control "/>
					</td>
					
					
					
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_planPrice" name="billDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"    class="form-control "/>
					</td>
					
					
					
					
					
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#billDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var billDetailRowIdx = 0, billDetailTpl = $("#billDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(billMain.billDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#billDetailList', billDetailRowIdx, billDetailTpl, data[i]);
						billDetailRowIdx = billDetailRowIdx + 1;
					}
				});
			</script>
			</div>
			<div id="tab-2" class="tab-pane fade in ">
			<div class="table-responsive" style="max-height:500px">
			<table class="table table-striped table-bordered table-condensed" style="min-width:1350px;">
				<thead>
					<tr>
						<th class="hide"></th>
						<th width="100px">序号</th>
						<th width="400px"><font color="red">*</font>二维码</th>
						<th width="200px">货区编号</th>
						<th width="200px">货区名称</th>
						<th width="200px">货位编号</th>
						<th width="200px">货位名称</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="billDetailCodeList">
				</tbody>
			</table>
			</div>
			<script type="text/template" id="ctrItemTpl">//<!--
				<tr id="billDetailCodeList{{idx}}">
					<td class="hide">
						<input id="billDetailCodeList{{idx}}_id" name="billDetailCodeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="billDetailCodeList{{idx}}_delFlag" name="billDetailCodeList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input readOnly="true" id="billDetailCodeList{{idx}}_serialNum" name="billDetailCodeList[{{idx}}].serialNum" type="text" value="{{idx}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="true" id="billDetailCodeList{{idx}}_itemBarcode" name="billDetailCodeList[{{idx}}].itemBarcode" type="text" value="{{row.itemBarcode}}"    class="form-control required"/>
					</td>
					<td>
						<input readOnly="true" id="billDetailCodeList{{idx}}_bin.binId" name="billDetailCodeList[{{idx}}].bin.binId" type="text" value="{{row.bin.binId}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="true" id="billDetailCodeList{{idx}}_bin.binDesc" name="billDetailCodeList[{{idx}}].bin.binDesc" type="text" value="{{row.bin.binDesc}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="true" id="billDetailCodeList{{idx}}_loc.locId" name="billDetailCodeList[{{idx}}].loc.locId" type="text" value="{{row.loc.locId}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="true" id="billDetailCodeList{{idx}}_loc.locDesc" name="billDetailCodeList[{{idx}}].loc.locDesc" type="text" value="{{row.loc.locDesc}}"    class="form-control "/>
					</td>
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#billDetailCodeList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var ctrItemRowIdx = 0, ctrItemTpl = $("#ctrItemTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(billMain.billDetailCodeList)};
					console.log(data);
					for (var i=0; i<data.length; i++){
						addRow('#billDetailCodeList', ctrItemRowIdx, ctrItemTpl, data[i]);
						ctrItemRowIdx = ctrItemRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<%-- <c:if test="${fns:hasPermission('billmain:billMain:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button id="submitBtn" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                 </div>
		             </div>
		        </div>
		</c:if> --%>
		<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                 	<a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/outsourcingoutput/outsourcingOutput/searchList">返 回</a>
		                 </div>
		             </div>
		        </div>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
<script type="text/javascript">
function Test(){
	top.layer.open({
	    type: 2, 
	    area: ['900px', '560px'],
	    title:"选择用户",
	    auto:true,
	    maxmin: true, //开启最大化最小化按钮
	    content: ctx+"/sys/user/userSelect?isMultiSelect="+false,
	    btn: ['确定', '关闭'],
	    yes: function(index, layero){
	    	var ids = layero.find("iframe")[0].contentWindow.getIdSelections();
	    	var names = layero.find("iframe")[0].contentWindow.getNameSelections();
	    	var loginNames = layero.find("iframe")[0].contentWindow.getLoginNameSelections();
	    	var nos = layero.find("iframe")[0].contentWindow.getNoSelections();
	        console.log(nos)
	    	if(ids.length ==0){
	    		
				jp.warning("请选择至少一个用户!");
				return;
			}
	        console.log(ids+" "+nos);
	    	// 执行保存
	    	$("#billPerson").val(ids.join(",").replace(/u_/ig,""));
	    	$("#billPersond").val(names.join(","));
	    	$("#billPersonId").val(nos.join(","));
	    	//yesFuc(ids.join(","), names.join(","),nos.join(","),loginNames.join(","));
	    	
	    	top.layer.close(index);
		  },
		  cancel: function(index){ 
			  //取消默认为空，如需要请自行扩展。
			  top.layer.close(index);
	        }
	}); 
}

</script>
</body>
</html>
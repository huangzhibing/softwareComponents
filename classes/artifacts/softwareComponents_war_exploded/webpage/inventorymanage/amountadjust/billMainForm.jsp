<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>单据管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
	function iniItemNum(){
		$("[id$='serialNum']").each(function(index,element){
			$(element).val(index+1);
		});

	}
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
			iniItemNum();
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
			iniItemNum();
		}
	</script>
	<script type="text/javascript">
	
		
		var flag=1,rqty;//获得进去页面时的可用量
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
	function cal(id){
		var batchFlag=document.getElementById("batchFlag").value;
		if(batchFlag==''||batchFlag==null) return;
		var itemBatch=document.getElementById(id+"_itemBatch");
		if(batchFlag=='N'){
			itemBatch.value=null;
			itemBatch.disabled="disabled";
		}
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
				<a class="panelButton" href="${ctx}/amountadjustment/amountAdjustment"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="billMain" action="${ctx}/amountadjustment/amountAdjustment/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="billNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据类型：</label>
					<div class="col-sm-10">
					
						<sys:gridselect-modify extraField="ioTypee:ioType;ioDescc:ioDesc" targetId="ioCode" targetField="ioType" url="${ctx}/billtype/billType/data?ioType=T" id="io" name="io.id" value="${billMain.io.id}" labelName="io.ioDesc" labelValue="${billMain.io.ioDesc}"
							 title="选择单据类型" cssClass="form-control required" fieldLabels="单据类型|单据名称" fieldKeys="ioType|ioDesc" searchLabels="单据名称" searchKeys="ioDesc" ></sys:gridselect-modify>
					</div>
				</div>
				<input style="display:none;" id="ioTypee" name="io.ioType" value="${billMain.io.ioType }"/>
				<input style="display:none;" id="ioDescc" name="io.ioDesc" value="${billMain.io.ioDesc }"/>
				<div style="display:none;">
					<input id="ioCode"/>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据状态：</label>
					<div class="col-sm-10">
<%-- 						<form:select path="billFlag" class="form-control required" disabled="true">
 --%>							<input value="待过账" readOnly="true"     class="form-control required"/>
								<form:input  style="display:none;" value="N" path="billFlag" htmlEscape="false"    class="form-control" />
						<%-- </form:select> --%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>调整日期：</label>
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
					<label class="col-sm-2 control-label"><font color="red">*</font>部门编码：</label>
					<div class="col-sm-10">
						<sys:treeselect-modify disabled="disabled" targetId="deptd" targetField="code" id="dept" name="dept.id" value="${billMain.dept.id}" labelName="dept.code" labelValue="${billMain.dept.code}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>部门名称：</label>
					<div class="col-sm-10">
						<form:input readonly="true" id="deptd" path="deptName" htmlEscape="false" value="${billMain.deptName }"   class="form-control required"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库房号：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify targetId="wared" targetField="wareName" 
						extraField="batchFlag:batchFlag;wareIDd:wareID"
						url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${billMain.ware.id}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
							 title="选择库房号" cssClass="form-control required" fieldLabels="库房号|库房名称" fieldKeys="wareID|wareName" searchLabels="库房号|库房名称" searchKeys="wareID|wareName" ></sys:gridselect-modify>
					</div>
				</div>
				<input style="display:none;" value="${billMain.ware.wareID }" id="wareIDd" name="ware.wareID"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库房名称：</label>
					<div class="col-sm-10">
						<form:input readonly="true" id="wared" path="wareName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<input id="batchFlag" style="display:none;"/>
				
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
					<label class="col-sm-2 control-label"><font color="red">*</font>核算期间：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="period.periodId" htmlEscape="false"    class="form-control required"/>
					</div>
					<input style="display:none;" readOnly="true" name="period.id" value="${billMain.period.periodId }"    class="form-control required"/>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input path="note" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				
				
				
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li id="t1" class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">出入库单据子表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onmousedown="check()" onmouseup="addRow('#billDetailList', billDetailRowIdx, billDetailTpl);billDetailRowIdx = billDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>编号</th>
						<th><font color="red">*</font>物料代码</th>
						<th>货区号</th>
						<th>货区名称</th>
						<th>货位号</th>
						<th>货位名称</th>
						
						<th><font color="red">*</font>物料名称</th>
						<th><font color="red">*</font>物料规格</th>
						<th>批次号</th>
						
						<th>单位</th>
						<th><font color="red">*</font>调整金额</th>
						
						
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="billDetailList">
				</tbody>
			</table>
			<script type="text/template" id="billDetailTpl">//<!--
				<tr id="billDetailList{{idx}}" onmousemove="cal(this.id);">
					<td class="hide">
						<input id="billDetailList{{idx}}_id" name="billDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="billDetailList{{idx}}_delFlag" name="billDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					<td>
						<input  readOnly="true" id="billDetailList{{idx}}_serialNum" name="billDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<sys:gridselect-again
							targetId="" targetField="" 
							extraField="billDetailList{{idx}}_accountId:id;billDetailList{{idx}}_itemBatch:itemBatch;billDetailList{{idx}}_item:item.id;billDetailList{{idx}}_itemName:item.name;billDetailList{{idx}}_itemPrice:costPrice;billDetailList{{idx}}_itemQty:realQty;billDetailList{{idx}}_itemQty1:realQty;billDetailList{{idx}}_itemSpec:item.specModel;billDetailList{{idx}}_measUnit:item.unitCode.unitName" 
							url="${ctx}/invaccount/invAccount/data" id="billDetailList{{idx}}_item" name="billDetailList{{idx}}.item.id" value="{{row.item.id}}" labelName="billDetailList{{idx}}.item.code" labelValue="{{row.item.code}}"
							 title="选择物料代码" cssClass="form-control required " fieldLabels="物料代码|物料名称|物料规格|数量" fieldKeys="item.code|item.name|item.specModel|realQty" searchLabels="物料代码|物料名称|货区号|货区名|货位号|货位名" searchKeys="item.code|item.name|bin.binId|bin.binDesc|loc.locId|locDesc" ></sys:gridselect-again>
					</td>
					<td style="display:none;">
						<input  readOnly="readOnly" id="billDetailList{{idx}}_accountId" name="billDetailList[{{idx}}].accountId" class="form-control  " value="{{row.accountId}}" />
					</td>
					
					<td>
						<input  readOnly="readOnly" id="billDetailList{{idx}}_binId" name="billDetailList[{{idx}}].bin.binId" class="form-control  " value="{{row.bin.binId}}" />
					</td>
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_binName" name="billDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_locId" name="billDetailList[{{idx}}].loc.locId" type="text" value="{{row.loc.locId}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_locName" name="billDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"    class="form-control "/>
					</td>
					
					
					
					
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_itemName" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>
					<td style="display:none;">
						<input readOnly="readOnly" id="billDetailList{{idx}}_item" name="billDetailList[{{idx}}].item.id" type="text" value="{{row.item.id}}"    class="form-control "/>
					</td>
					<td style="display:none;">
						<input  readOnly="readOnly" id="billDetailList{{idx}}_bin" name="billDetailList[{{idx}}].bin.id" class="form-control  " value="{{row.bin.id}}" />
					</td>
					<td style="display:none;">
						<input readOnly="readOnly" id="billDetailList{{idx}}_loc" name="billDetailList[{{idx}}].loc.id" type="text" value="{{row.loc.id}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_itemSpec" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="true" id="billDetailList{{idx}}_itemBatch" name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>
					
					
					
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_measUnit" name="billDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"    class="form-control "/>
					</td>
					<td>
						<input   id="billDetailList{{idx}}_itemPrice" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"    class="form-control required"/>
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
			
		</div>
		</div>
		<c:if test="${fns:hasPermission('billmain:billMain:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button id="submitBtn" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
	var code;
	function check(){
		code=document.getElementById("ioCode").value;
		if(code==null||code==""){
			layer.alert("请先选择单据类型");
			return;
		}
	}
	
</script>
</body>
</html>
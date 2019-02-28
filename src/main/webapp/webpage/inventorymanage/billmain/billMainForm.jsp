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
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/billmain/billMain"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="billMain" action="${ctx}/billmain/billMain/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据号：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>出入库日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='billDate'>
			                    <input type='text'  name="billDate" class="form-control required"  value="<fmt:formatDate value="${billMain.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">部门编码：</label>
					<div class="col-sm-10">
						<sys:treeselect id="dept" name="dept.code" value="${billMain.dept.code}" labelName="dept.code" labelValue="${billMain.dept.code}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">部门名称：</label>
					<div class="col-sm-10">
						<form:input path="deptName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据类型：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/billtype/billType/data" id="io" name="io.ioType" value="${billMain.io.ioType}" labelName="io.ioType" labelValue="${billMain.io.ioType}"
							 title="选择单据类型" cssClass="form-control required" fieldLabels="单据类型|单据名称" fieldKeys="ioType|ioDesc" searchLabels="单据类型|单据名称" searchKeys="ioType|ioDesc" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据名称：</label>
					<div class="col-sm-10">
						<form:input path="ioDesc" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据标记：</label>
					<div class="col-sm-10">
						<form:select path="ioFlag" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('ioFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库房号：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.wareID" value="${billMain.ware.wareID}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
							 title="选择库房号" cssClass="form-control required" fieldLabels="库房号|库房名称" fieldKeys="wareID|wareName" searchLabels="库房号|库房名称" searchKeys="wareID|wareName" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库房名称：</label>
					<div class="col-sm-10">
						<form:input path="wareName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">经办人：</label>
					<div class="col-sm-10">
						<sys:userselect id="billPerson" name="billPerson.id" value="${billMain.billPerson.id}" labelName="billPerson.no" labelValue="${billMain.billPerson.no}"
							    cssClass="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库管员代码：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/employee/employee/data" id="wareEmp" name="wareEmp.user.no" value="${billMain.wareEmp.user.no}" labelName="wareEmp.user.no" labelValue="${billMain.wareEmp.user.no}"
							 title="选择库管员代码" cssClass="form-control required" fieldLabels="库管员代码|库管员名称" fieldKeys="wareEmpid|wareEmpname" searchLabels="库管员代码|库管员名称" searchKeys="wareEmpid|wareEmpname" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库管员名称：</label>
					<div class="col-sm-10">
						<form:input path="wareEmpname" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">对应单据号：</label>
					<div class="col-sm-10">
						<form:input path="corBillNum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">来往单位代码：</label>
					<div class="col-sm-10">
						<form:input path="corId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">来往单位名称：</label>
					<div class="col-sm-10">
						<form:input path="corName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">用途/车间班组代码：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/use/use/data" id="invuse" name="invuse.useId" value="${billMain.invuse.useId}" labelName="invuse.useId" labelValue="${billMain.invuse.useId}"
							 title="选择用途/车间班组代码" cssClass="form-control required" fieldLabels="用途/车间班组代码|用途/车间班组名称" fieldKeys="use_id|use_name" searchLabels="用途/车间班组代码|用途/车间班组名称" searchKeys="use_id|use_name" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">用途/车间班组名称：</label>
					<div class="col-sm-10">
						<form:input path="useName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>过账标志：</label>
					<div class="col-sm-10">
						<form:select path="billFlag" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('billFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">估价标志：</label>
					<div class="col-sm-10">
						<form:select path="estimateFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('estimateFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">核算期间：</label>
					<div class="col-sm-10">
						<form:input path="period.id" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据说明：</label>
					<div class="col-sm-10">
						<form:input path="billDesc" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">记账人编码：</label>
					<div class="col-sm-10">
						<sys:userselect id="recEmp" name="recEmp.no" value="${billMain.recEmp.no}" labelName="recEmp.no" labelValue="${billMain.recEmp.no}"
							    cssClass="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">记账人：</label>
					<div class="col-sm-10">
						<form:input path="recEmpname" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">记账日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='recDate'>
			                    <input type='text'  name="recDate" class="form-control "  value="<fmt:formatDate value="${billMain.recDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
						<sys:userselect id="billEmp" name="billEmp.no" value="${billMain.billEmp.no}" labelName="billEmp.no" labelValue="${billMain.billEmp.no}"
							    cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人：</label>
					<div class="col-sm-10">
						<form:input path="billEmpname" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input path="note" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>接口序号：</label>
					<div class="col-sm-10">
						<form:input path="orderCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">税率：</label>
					<div class="col-sm-10">
						<form:input path="taxRatio" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">财务凭证号：</label>
					<div class="col-sm-10">
						<form:input path="inoId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">科目代码：</label>
					<div class="col-sm-10">
						<form:input path="classId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">科目名称：</label>
					<div class="col-sm-10">
						<form:input path="classDesc" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">传财务标志：</label>
					<div class="col-sm-10">
						<form:select path="cflag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cflag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">发票号：</label>
					<div class="col-sm-10">
						<form:input path="fnumber" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">发票内码：</label>
					<div class="col-sm-10">
						<form:input path="finvoiceID" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">冲估类型：</label>
					<div class="col-sm-10">
						<form:select path="billType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cgType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">红单标志：</label>
					<div class="col-sm-10">
						<form:input path="redFlag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">冲估标志：</label>
					<div class="col-sm-10">
						<form:input path="cgFlag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">金额合计：</label>
					<div class="col-sm-10">
						<form:input path="totalSum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">业务日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='operDate'>
			                    <input type='text'  name="operDate" class="form-control "  value="<fmt:formatDate value="${billMain.operDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据来源：</label>
					<div class="col-sm-10">
						<form:select path="billSource" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('billSource')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">冲估单号：</label>
					<div class="col-sm-10">
						<form:input path="cgNum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工位代码：</label>
					<div class="col-sm-10">
						<form:input path="workPoscode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工位名称：</label>
					<div class="col-sm-10">
						<form:input path="wordPosname" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">出入库单据子表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#billDetailList', billDetailRowIdx, billDetailTpl);billDetailRowIdx = billDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>序号</th>
						<th>货区号</th>
						<th>货区名称</th>
						<th>货位号</th>
						<th>货位名称</th>
						<th><font color="red">*</font>物料代码</th>
						<th>物料名称</th>
						<th>物料规格</th>
						<th>物料图号</th>
						<th>物料条码</th>
						<th>定单号</th>
						<th>计划号</th>
						<th>计量单位</th>
						<th>数量</th>
						<th>请领数量</th>
						<th>实际单价</th>
						<th>实际金额</th>
						<th>物料批号</th>
						<th>父项代码</th>
						<th>父项订单号</th>
						<th>工程号</th>
						<th>采购入库通知单号</th>
						<th>提货标志</th>
						<th>估价标志</th>
						<th>备注</th>
						<th>MRP标志</th>
						<th>计划单价</th>
						<th>税额</th>
						<th>销售传来的车号</th>
						<th>销售传来的到站</th>
						<th>销售传来的客户</th>
						<th>成本标志位</th>
						<th>接口序号</th>
						<th>运费</th>
						<th>工序号</th>
						<th>自制外协标记</th>
						<th>工序名称</th>
						<th>计划金额</th>
						<th>对应序号</th>
						<th>保留单价</th>
						<th>冲估数量</th>
						<th>冲估金额</th>
						<th>成本差额</th>
						<th>开票数量</th>
						<th>开票金额</th>
						<th>开票差额</th>
						<th>不含税金额</th>
						<th>含税金额</th>
						<th>税率</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="billDetailList">
				</tbody>
			</table>
			<script type="text/template" id="billDetailTpl">//<!--
				<tr id="billDetailList{{idx}}">
					<td class="hide">
						<input id="billDetailList{{idx}}_id" name="billDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="billDetailList{{idx}}_delFlag" name="billDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="billDetailList{{idx}}_serialNum" name="billDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<sys:gridselect url="${ctx}/bin/bin/data" id="billDetailList{{idx}}_bin" name="billDetailList[{{idx}}].bin.id" value="{{row.bin.id}}" labelName="billDetailList{{idx}}.bin.binId" labelValue="{{row.bin.binId}}"
							 title="选择货区号" cssClass="form-control  " fieldLabels="货区号|货区名称" fieldKeys="binId|binDesc" searchLabels="货区号|货区名称" searchKeys="binId|binDesc" ></sys:gridselect>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_binName" name="billDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<sys:gridselect url="${ctx}/location/location/data" id="billDetailList{{idx}}_loc" name="billDetailList[{{idx}}].loc.id" value="{{row.loc.id}}" labelName="billDetailList{{idx}}.loc.locId" labelValue="{{row.loc.locId}}"
							 title="选择货位号" cssClass="form-control  " fieldLabels="货位号|货位名称" fieldKeys="locId|locDesc" searchLabels="货位号|货位名称" searchKeys="locId|locDesc" ></sys:gridselect>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_locName" name="billDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<sys:gridselect url="${ctx}/item/item/data" id="billDetailList{{idx}}_item" name="billDetailList[{{idx}}].item.id" value="{{row.item.id}}" labelName="billDetailList{{idx}}.item.code" labelValue="{{row.item.code}}"
							 title="选择物料代码" cssClass="form-control  required" fieldLabels="物料代码|物料名称|规格型号" fieldKeys="code|name|specModel" searchLabels="物料代码|物料名称|规格型号" searchKeys="code|name|specModel" ></sys:gridselect>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemName" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemSpec" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemPdn" name="billDetailList[{{idx}}].itemPdn" type="text" value="{{row.itemPdn}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_barCode" name="billDetailList[{{idx}}].barCode" type="text" value="{{row.barCode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_orderNum" name="billDetailList[{{idx}}].orderNum" type="text" value="{{row.orderNum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_planNum" name="billDetailList[{{idx}}].planNum" type="text" value="{{row.planNum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_measUnit" name="billDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemQty" name="billDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemRqty" name="billDetailList[{{idx}}].itemRqty" type="text" value="{{row.itemRqty}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemPrice" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemSum" name="billDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemBatch" name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_fitemCode" name="billDetailList[{{idx}}].fitemCode" type="text" value="{{row.fitemCode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_forderNum" name="billDetailList[{{idx}}].forderNum" type="text" value="{{row.forderNum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_contractNum" name="billDetailList[{{idx}}].contractNum" type="text" value="{{row.contractNum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_corBillNum" name="billDetailList[{{idx}}].corBillNum" type="text" value="{{row.corBillNum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<select id="billDetailList{{idx}}_pickFlag" name="billDetailList[{{idx}}].pickFlag" data-value="{{row.pickFlag}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('pickFlag')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<select id="billDetailList{{idx}}_estimateFlag" name="billDetailList[{{idx}}].estimateFlag" data-value="{{row.estimateFlag}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('estimateFlag')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_note" name="billDetailList[{{idx}}].note" type="text" value="{{row.note}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_mrpFlag" name="billDetailList[{{idx}}].mrpFlag" type="text" value="{{row.mrpFlag}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_planPrice" name="billDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_taxSum" name="billDetailList[{{idx}}].taxSum" type="text" value="{{row.taxSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_vehicleCode" name="billDetailList[{{idx}}].vehicleCode" type="text" value="{{row.vehicleCode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_revStation" name="billDetailList[{{idx}}].revStation" type="text" value="{{row.revStation}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_revAccount" name="billDetailList[{{idx}}].revAccount" type="text" value="{{row.revAccount}}"    class="form-control "/>
					</td>
					
					
					<td>
						<select id="billDetailList{{idx}}_cosFlag" name="billDetailList[{{idx}}].cosFlag" data-value="{{row.cosFlag}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('cosFlag')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_orderCodes" name="billDetailList[{{idx}}].orderCodes" type="text" value="{{row.orderCodes}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_trafficCost" name="billDetailList[{{idx}}].trafficCost" type="text" value="{{row.trafficCost}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_operNo" name="billDetailList[{{idx}}].operNo" type="text" value="{{row.operNo}}"    class="form-control "/>
					</td>
					
					
					<td>
						<select id="billDetailList{{idx}}_sourceFlag" name="billDetailList[{{idx}}].sourceFlag" data-value="{{row.sourceFlag}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('sourceFlag')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_operName" name="billDetailList[{{idx}}].operName" type="text" value="{{row.operName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_planSum" name="billDetailList[{{idx}}].planSum" type="text" value="{{row.planSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_corSerialnum" name="billDetailList[{{idx}}].corSerialnum" type="text" value="{{row.corSerialnum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_oldPrice" name="billDetailList[{{idx}}].oldPrice" type="text" value="{{row.oldPrice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_cgQty" name="billDetailList[{{idx}}].cgQty" type="text" value="{{row.cgQty}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_cgSum" name="billDetailList[{{idx}}].cgSum" type="text" value="{{row.cgSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_subSum" name="billDetailList[{{idx}}].subSum" type="text" value="{{row.subSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_vouchQty" name="billDetailList[{{idx}}].vouchQty" type="text" value="{{row.vouchQty}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_vouchSum" name="billDetailList[{{idx}}].vouchSum" type="text" value="{{row.vouchSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_vouchSub" name="billDetailList[{{idx}}].vouchSub" type="text" value="{{row.vouchSub}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_realSum" name="billDetailList[{{idx}}].realSum" type="text" value="{{row.realSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_realSumTaxed" name="billDetailList[{{idx}}].realSumTaxed" type="text" value="{{row.realSumTaxed}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_taxRatio" name="billDetailList[{{idx}}].taxRatio" type="text" value="{{row.taxRatio}}"    class="form-control "/>
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
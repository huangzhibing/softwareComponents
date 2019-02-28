<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单管理</title>
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
			
	        $('#checkDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#justifyDate').datetimepicker({
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
				<a class="panelButton" href="${ctx}/purreport/purReport/completeCheck"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="purReport" action="${ctx}/purreport/purReport/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验单编号：</label>
					<div class="col-sm-10">
						<form:input path="reportId" htmlEscape="false"  readonly="true"   class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>完工单编号：</label>
					<div class="col-sm-10">
					
					<form:input path="sfcInvCheckMain.billNo" htmlEscape="false"  readonly="true"   class="form-control required"/>
					<%-- 
						<sys:gridselect url="${ctx}/purinvcheckmain/invCheckMain/data" id="invCheckMain" name="invCheckMain.id" value="${purReport.invCheckMain.id}" labelName="invCheckMain.billNum" labelValue="${purReport.invCheckMain.billNum}"
							 title="选择采购报检单编号" cssClass="form-control required" fieldLabels="采购报检单编号" fieldKeys="billNum" searchLabels="采购报检单编号" searchKeys="billNum" ></sys:gridselect>
					--%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验类型名称：</label>
					<div class="col-sm-10">
						<form:input path="checktName" htmlEscape="false"  value="采购" readonly="true" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">全检/抽检：</label>
					<div class="col-sm-10">
					<form:input path="rndFul" htmlEscape="false"  readonly="true"   class="form-control required"/>
					
					<%-- 
						<form:select path="rndFul" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('checkType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
						--%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">执行标准：</label>
					<div class="col-sm-10">
					<form:input path="qualityNorm" htmlEscape="false"  readonly="true"   class="form-control required"/>
					
					<%-- 
					<form:select path="qualityNorm" class="form-control "  >
							<form:option value="" label=""/>
							<form:options items="${qualityNormList}" itemLabel="qnormname" itemValue="qnormname" htmlEscape="false"/>
					</form:select>
					--%>
					<%-- 
						//原内容
						<form:input path="qualityNorm" htmlEscape="false"    class="form-control "/>
					--%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验部门名称：</label>
					<div class="col-sm-10">
					<form:input path="office.name" htmlEscape="false"  readonly="true"   class="form-control required"/>
					
					<%-- 
						<sys:treeselect id="office" name="office.id" value="${purReport.office.id}" labelName="office.name" labelValue="${purReport.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					--%>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">检验岗位编码：</label>
					<div class="col-sm-10">
						<form:input path="cpositionCode" htmlEscape="false"  readonly="true"   class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">检验岗位名称：</label>
					<div class="col-sm-10">
					
					<form:input path="cpositionName" htmlEscape="false"  readonly="true"   class="form-control "/>
					<%--
					
					<form:select path="cpositionName" class="form-control "  >
							<form:option value="" label=""/>
							<form:options items="${roleList}" itemLabel="name" itemValue="name" htmlEscape="false"/>
					</form:select>
					 --%>
					
					<%-- 	<form:input path="cpositionName" htmlEscape="false"    class="form-control "/>--%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验日期：</label>
					<div class="col-sm-10">
						<input type='text' id='checkDate1' name="checkDate" class="form-control " readonly="true" value="${fns:getMyDate()}"/>
			                 
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验时间：</label>
					<div class="col-sm-10">
						<form:input path="checkTime" htmlEscape="false" readonly="true" value="${fns:getMyTime()}"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验人名称：</label>
					<div class="col-sm-10">
						<form:input path="checkPerson" htmlEscape="false"  value='${fns:getUser().name}' readonly="true" class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核部门名称：</label>
					<div class="col-sm-10">
						<form:input path="" htmlEscape="false"   readonly="true" class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核岗位名称：</label>
					<div class="col-sm-10">
						<form:input path="" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核日期：</label>
					<div class="col-sm-10">
						<form:input path="" htmlEscape="false"   readonly="true" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核人：</label>
					<div class="col-sm-10">
						<form:input path="" htmlEscape="false"   readonly="true" class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核结论：</label>
					<div class="col-sm-10">
						<form:input path="justifyResult" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核意见：</label>
					<div class="col-sm-10">
						<form:textarea path="justifyRemark" htmlEscape="false" rows="4" readonly="true"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input path="memo" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-10">
					<%-- 
						<form:select path="state" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					--%>
						<form:input path="state" htmlEscape="false"  readonly="true" value="未审核"  class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
			<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">安规测试追溯：</a>
				</li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">老化测试追溯：</a>
				</li>
				<li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">激活测试追溯：</a>
				</li>
				<li class=""><a data-toggle="tab" href="#tab-4" aria-expanded="false">包装测试追溯：</a>
				</li>
			</ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
		
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验对象序列号</th>
						<th>机台序列码</th>
						<th>检验对象编码</th>
						<th>检验对象名称</th>
						<th>规则ID</th>
						<th>测试项目</th>
						<th>检验标准号</th>
						<th>检验是否完成</th>


					</tr>
				</thead>
				<tbody id="purReportANList">
				</tbody>
			</table>
			<script type="text/template" id="purReportRSnTpl">//<!--
				<tr id="purReportANList{{idx}}">
					<td class="hide">
						<input id="purReportANList{{idx}}_id" name="purReportANList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purReportANList{{idx}}_delFlag" name="purReportANList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="purReportANList{{idx}}_objSn" name="purReportANList[{{idx}}].objSn" readonly="true" type="text" value="{{row.objSn}}"    class="form-control "/>
					</td>

<td>
						<input id="purReportANList{{idx}}_mserialno" name="purReportANList[{{idx}}].mserialno" readonly="true" type="text" value="{{row.mserialno}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportANList{{idx}}_objCode" name="purReportANList[{{idx}}].objCode" type="text" value="{{row.objCode}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportANList{{idx}}_objName" name="purReportANList[{{idx}}].objName" readonly="true" type="text" value="{{row.objName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportANList{{idx}}_ruleId" name="purReportANList[{{idx}}].ruleId" readonly="true" type="text" value="{{row.ruleId}}"    class="form-control "/>
					

					
					</td>
					


                       <td>
						<input id="purReportANList{{idx}}_checkprj" name="purReportANList[{{idx}}].checkprj" readonly="true" type="text" value="{{row.checkprj}}"    class="form-control "/>
					</td>
					<td>
						<input id="purReportANList{{idx}}_qcnormId" name="purReportANList[{{idx}}].qcnormId" readonly="true" type="text" value="{{row.qcnormId}}"    class="form-control "/>
					</td>
					<td>
						<input id="purReportANList{{idx}}_isfisished" name="purReportANList[{{idx}}].isfisished" readonly="true" type="text" value="{{row.isfisished}}"    class="form-control "/>
					</td>
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purReportRSnRowIdx = 0, purReportRSnTpl = $("#purReportRSnTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(purReport.aList)};
					for (var i=0; i<data.length; i++){
						addRow('#purReportANList', purReportRSnRowIdx, purReportRSnTpl, data[i]);
						purReportRSnRowIdx = purReportRSnRowIdx + 1;
					}
				});
			</script>
			</div>
		
		
		
		
		
				<div id="tab-2" class="tab-pane fade">
		
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验对象序列号</th>
						<th>机台序列码</th>
						<th>检验对象编码</th>
						<th>检验对象名称</th>
						<th>规则ID</th>
						<th>测试项目</th>
						<th>检验标准号</th>
						<th>检验是否完成</th>


					</tr>
				</thead>
				<tbody id="purReportLHList">
				</tbody>
			</table>
			<script type="text/template" id="purReportLHListTpl">//<!--
				<tr id="purReportLHList{{idx}}">
					<td class="hide">
						<input id="purReportLHList{{idx}}_id" name="purReportLHList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purReportLHList{{idx}}_delFlag" name="purReportLHList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="purReportLHList{{idx}}_objSn" name="purReportLHList[{{idx}}].objSn" readonly="true" type="text" value="{{row.objSn}}"    class="form-control "/>
					</td>

					<td>
						<input id="purReportLHList{{idx}}_mserialno" name="purReportLHList[{{idx}}].mserialno" readonly="true" type="text" value="{{row.mserialno}}"    class="form-control "/>
					</td>

					
					
					<td>
						<input id="purReportLHList{{idx}}_objCode" name="purReportLHList[{{idx}}].objCode" type="text" value="{{row.objCode}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportLHList{{idx}}_objName" name="purReportLHList[{{idx}}].objName" readonly="true" type="text" value="{{row.objName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportLHList{{idx}}_ruleId" name="purReportLHList[{{idx}}].ruleId" readonly="true" type="text" value="{{row.ruleId}}"    class="form-control "/>
					

					
					</td>
					


                       <td>
						<input id="purReportLHList{{idx}}_checkprj" name="purReportLHList[{{idx}}].checkprj" readonly="true" type="text" value="{{row.checkprj}}"    class="form-control "/>
					</td>
					<td>
						<input id="purReportLHList{{idx}}_qcnormId" name="purReportLHList[{{idx}}].qcnormId" readonly="true" type="text" value="{{row.qcnormId}}"    class="form-control "/>
					</td>
					<td>
						<input id="purReportLHList{{idx}}_isfisished" name="purReportLHList[{{idx}}].isfisished" readonly="true" type="text" value="{{row.isfisished}}"    class="form-control "/>
					</td>
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purReportRSnRowLHIdx = 0, purReportLHListTpl = $("#purReportLHListTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(purReport.lList)};
					for (var i=0; i<data.length; i++){
						addRow('#purReportLHList', purReportRSnRowLHIdx, purReportLHListTpl, data[i]);
						purReportRSnRowLHIdx = purReportRSnRowLHIdx + 1;
					}
				});
			</script>
			</div>
		
		
		<div id="tab-3" class="tab-pane fade">
		
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验对象序列号</th>
						<th>机台序列码</th>
						<th>检验对象编码</th>
						<th>检验对象名称</th>
						<th>规则ID</th>
						<th>测试项目</th>
						<th>检验标准号</th>
						<th>检验是否完成</th>


					</tr>
				</thead>
				<tbody id="purReportJHList">
				</tbody>
			</table>
			<script type="text/template" id="purReportRSnJHTpl">//<!--
				<tr id="purReportJHList{{idx}}">
					<td class="hide">
						<input id="purReportJHList{{idx}}_id" name="purReportJHList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purReportJHList{{idx}}_delFlag" name="purReportJHList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="purReportJHList{{idx}}_objSn" name="purReportJHList[{{idx}}].objSn" readonly="true" type="text" value="{{row.objSn}}"    class="form-control "/>
					</td>


<td>
						<input id="purReportJHList{{idx}}_mserialno" name="purReportJHList[{{idx}}].mserialno" readonly="true" type="text" value="{{row.mserialno}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportJHList{{idx}}_objCode" name="purReportJHList[{{idx}}].objCode" type="text" value="{{row.objCode}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportJHList{{idx}}_objName" name="purReportJHList[{{idx}}].objName" readonly="true" type="text" value="{{row.objName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportJHList{{idx}}_ruleId" name="purReportJHList[{{idx}}].ruleId" readonly="true" type="text" value="{{row.ruleId}}"    class="form-control "/>
					

					
					</td>
					


                       <td>
						<input id="purReportJHList{{idx}}_checkprj" name="purReportJHList[{{idx}}].checkprj" readonly="true" type="text" value="{{row.checkprj}}"    class="form-control "/>
					</td>
					<td>
						<input id="purReportJHList{{idx}}_qcnormId" name="purReportJHList[{{idx}}].qcnormId" readonly="true" type="text" value="{{row.qcnormId}}"    class="form-control "/>
					</td>
					<td>
						<input id="purReportJHList{{idx}}_isfisished" name="purReportJHList[{{idx}}].isfisished" readonly="true" type="text" value="{{row.isfisished}}"    class="form-control "/>
					</td>
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purReportRSnRowJHIdx = 0, purReportRSnJHTpl = $("#purReportRSnJHTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(purReport.jList)};
					for (var i=0; i<data.length; i++){
						addRow('#purReportJHList', purReportRSnRowJHIdx, purReportRSnJHTpl, data[i]);
						purReportRSnRowJHIdx = purReportRSnRowJHIdx + 1;
					}
				});
			</script>
			</div>
		
		
		
		
		<div id="tab-4" class="tab-pane fade">
		
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验对象序列号</th>
						<th>机台序列码</th>
						<th>检验对象编码</th>
						<th>检验对象名称</th>
						<th>规则ID</th>
						<th>测试项目</th>
						<th>检验标准号</th>
						<th>检验是否完成</th>


					</tr>
				</thead>
				<tbody id="purReportBZList">
				</tbody>
			</table>
			<script type="text/template" id="purReportRSnBZTpl">//<!--
				<tr id="purReportBZList{{idx}}">
					<td class="hide">
						<input id="purReportBZList{{idx}}_id" name="purReportBZList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purReportBZList{{idx}}_delFlag" name="purReportBZList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="purReportBZList{{idx}}_objSn" name="purReportBZList[{{idx}}].objSn" readonly="true" type="text" value="{{row.objSn}}"    class="form-control "/>
					</td>

<td>
						<input id="purReportBZList{{idx}}_mserialno" name="purReportBZList[{{idx}}].mserialno" readonly="true" type="text" value="{{row.mserialno}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportBZList{{idx}}_objCode" name="purReportBZList[{{idx}}].objCode" type="text" value="{{row.objCode}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportBZList{{idx}}_objName" name="purReportBZList[{{idx}}].objName" readonly="true" type="text" value="{{row.objName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportBZList{{idx}}_ruleId" name="purReportBZList[{{idx}}].ruleId" readonly="true" type="text" value="{{row.ruleId}}"    class="form-control "/>
					

					
					</td>
					


                       <td>
						<input id="purReportBZList{{idx}}_checkprj" name="purReportBZList[{{idx}}].checkprj" readonly="true" type="text" value="{{row.checkprj}}"    class="form-control "/>
					</td>
					<td>
						<input id="purReportBZList{{idx}}_qcnormId" name="purReportBZList[{{idx}}].qcnormId" readonly="true" type="text" value="{{row.qcnormId}}"    class="form-control "/>
					</td>
					<td>
						<input id="purReportBZList{{idx}}_isfisished" name="purReportBZList[{{idx}}].isfisished" readonly="true" type="text" value="{{row.isfisished}}"    class="form-control "/>
					</td>
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purReportRSnRowBZIdx = 0, purReportRSnBZTpl = $("#purReportRSnBZTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(purReport.bList)};
					for (var i=0; i<data.length; i++){
						addRow('#purReportBZList', purReportRSnRowBZIdx, purReportRSnBZTpl, data[i]);
						purReportRSnRowBZIdx = purReportRSnRowBZIdx + 1;
					}
				});
			</script>
			</div>
		
		</div>
		</div>
		
		
		
		
		</div>
		<c:if test="${fns:hasPermission('purreport:purReport:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                    
		                    <%-- 
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                --%>
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
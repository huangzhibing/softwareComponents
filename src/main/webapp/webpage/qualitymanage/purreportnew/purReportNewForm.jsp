<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>IQC来料检验报告管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			
		/////
			var handleResult=$("#handleResult");
			var backBillNum=$("#rf");
			backBillNum.css("display","none");
			handleResult.change(function(){
				if(handleResult.val()=="退货"){
					backBillNum.css("display","block");  
				}else{
					backBillNum.css("display","none")
				}
			}
		
			)
		////
			
			
			
			
			
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
			}
			
			
			
			
			);
			
	        $('#itemArriveDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#checkDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#backDate').datetimepicker({
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
		
		
		function change(o){
			var id = o.id;
			var head = id.split("_")[0];
			var crNumId = head+"_crNum";
			var majNumid = head+"_majNum";
			var crNumVal = $("#"+crNumId).val();
			var majNumVal = $("#"+majNumid).val();
			if(crNumVal==""){
				crNumVal=0;
			}
			if(majNumVal==""){
				majNumVal=0;
			}
			
			var sum = parseInt(crNumVal)+parseInt(majNumVal);
			$("#"+head+"_sum").val(sum);
			
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
		
		function changef(o){
			var vl = o.val();
			alert(vl);
			if(vl=="退货"){
				var a = $("#rf").setInterval("f()",1);
				window.clearInterval(a);
			}
			
		}
		function f(){}
		
		function changeNum(h){
			var v = $("#backQty").val();
			var price = $("#realPriceTaxed").val();
			if(price==""){
				price=0;
			}
			$("#realSumTaxed").val(parseInt(v)*parseInt(price));
			
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
				<a class="panelButton" href="${ctx}/purreportnew/purReportNew"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="purReportNew" action="${ctx}/purreportnew/purReportNew/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>报告单编号：</label>
					<div class="col-sm-3">
						<form:input path="reportId" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				
					<label class="col-sm-2 control-label"><font color="red">*</font>采购单号：</label>
					<div class="col-sm-3">
						<sys:gridselect-sup url="${ctx}/purreport/purReport/invCheckMainData2" id="inv" name="inv.id" value="${purReportNew.inv.id}" labelName="inv.billnum" labelValue="${purReportNew.inv.billnum}"
							 title="选择采购报检单编号" cssClass="form-control required" 
							  targetId="icode|itemName|itemSpecmodel|supId|supname1|itemArriveDate|unitCode|realPriceTaxed|itemCount" 
							  targetField="itemCode|itemName|itemSpecmodel|supId|supName|itemArriveDate|unitCode|realPriceTaxed|itemCount" 
							 fieldLabels="单据编号|物料编码|物料名称|物料规格|供应商编码|供应商名称" 
							 fieldKeys="billnum|itemCode|itemName|itemSpecmodel|supId|supName" 
							 searchLabels="单据编号|物料编码|物料名称|物料规格|供应商编码|供应商名称" 
							 searchKeys="billnum|itemCode|itemName|itemSpecmodel|supId|supName" >
						</sys:gridselect-sup>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>料号：</label>
					<div class="col-sm-3">
						<form:input path="icode" htmlEscape="false"    class="form-control "/>
					<!--  
						<sys:gridselect-sup url="${ctx}/objectdef/objectDef/data" id="obf" name="obf.id" value="${purReportNew.obf.id}" labelName="obf.objCode" labelValue="${purReportNew.obf.objCode}"
							 title="选择料号" 
							 targetId="itemName" targetField="objName" 
							 cssClass="form-control required" fieldLabels="检验对象名称|检验对象编码" fieldKeys="objName|objCode" searchLabels="检验对象名称|检验对象编码" searchKeys="objName|objCode" >
						</sys:gridselect-sup>
						-->
					</div>
				
					<label class="col-sm-2 control-label"><font color="red">*</font>物料名称：</label>
					<div class="col-sm-3">
						<form:input path="itemName" htmlEscape="false"    class="form-control required"/>
					</div>
					
					
					
				</div>
				
					<div class="form-group">
					<label class="col-sm-2 control-label">物料规格：</label>
					<div class="col-sm-3">
						<form:input path="itemSpecmodel" htmlEscape="false"    class="form-control "/>
					</div>
				
					<label class="col-sm-2 control-label">供应商编号：</label>
					<div class="col-sm-3">
						<form:input path="supId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
					<div class="col-sm-3">
						<form:input path="supname1" htmlEscape="false"    class="form-control "/>
					<!--  
						<sys:gridselect url="${ctx}/accountfile/fileSalaccount/data" id="filesala" name="filesala.id" value="${purReportNew.filesala.id}" labelName="filesala.accountName" labelValue="${purReportNew.filesala.accountName}"
							 title="选择供应商名称" cssClass="form-control required" fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName" searchLabels="" searchKeys="" ></sys:gridselect>
					-->
					</div>
				
					<label class="col-sm-2 control-label"><font color="red">*</font>来料数量：</label>
					<div class="col-sm-3">
						<form:input path="itemCount" htmlEscape="false"    class="form-control required isIntGteZero"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">送检编号：</label>
					<div class="col-sm-3">
						<form:input path="inspectionNum" htmlEscape="false"    class="form-control "/>
					</div>
				
					<label class="col-sm-2 control-label">来料日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
							<div class='input-group form_datetime' id='itemArriveDate'>
			                    <input type='text'  name="itemArriveDate" class="form-control "  value="<fmt:formatDate value="${purReportNew.itemArriveDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">检验日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
							<div class='input-group form_datetime' id='checkDate'>
			                    <input type='text'  name="checkDate" class="form-control "  value="<fmt:formatDate value="${purReportNew.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				
					<label class="col-sm-2 control-label">检验员：</label>
					<div class="col-sm-3">
						<sys:userselect id="user" name="user.id" value="${purReportNew.user.id}" labelName="user.name" labelValue="${purReportNew.user.name}"
							    cssClass="form-control "/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">全检抽检：</label>
					<div class="col-sm-3">
						<form:select path="rndFul" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('checkType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				
					<label class="col-sm-2 control-label">收货标准：</label>
					<div class="col-sm-3">
						<form:select path="aql" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('AQL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">致命缺陷允收数量：</label>
					<div class="col-sm-3">
						<form:input path="crAc" htmlEscape="false"    class="form-control  isIntGteZero"/>
					</div>
				
					<label class="col-sm-2 control-label">严重缺陷允收数量：</label>
					<div class="col-sm-3">
						<form:input path="majAc" htmlEscape="false"    class="form-control  isIntGteZero"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">轻微缺陷允收数量：</label>
					<div class="col-sm-3">
						<form:input path="minAc" htmlEscape="false"    class="form-control  isIntGteZero"/>
					</div>
				
					<label class="col-sm-2 control-label">致命缺陷拒收数量：</label>
					<div class="col-sm-3">
						<form:input path="crRe" htmlEscape="false"    class="form-control  isIntGteZero"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">严重缺陷拒收数量：</label>
					<div class="col-sm-3">
						<form:input path="majRe" htmlEscape="false"    class="form-control  isIntGteZero"/>
					</div>
				
					<label class="col-sm-2 control-label">轻微缺陷拒收数量：</label>
					<div class="col-sm-3">
						<form:input path="minRe" htmlEscape="false"    class="form-control  isIntGteZero"/>
					</div>
				</div>
				
				
			
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验结果：</label>
					<div class="col-sm-3">
						<form:select path="checkResult" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('checkResultNew')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				
					<label class="col-sm-2 control-label"><font color="red">*</font>检验处理：</label>
					<div class="col-sm-3">
						<form:select path="handleResult" class="form-control required" >
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('handleresult')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				
				
				<div id="rf">
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">退货单号：</label>
					<div class="col-sm-3">
						<form:input path="backBillNum" htmlEscape="false"    class="form-control "/>
					</div>
				
					
					<label class="col-sm-2 control-label">退货日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
							<div class='input-group form_datetime' id='backDate'>
			                    <input type='text'  name="backDate" class="form-control "  value="<fmt:formatDate value="${purReportNew.backDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">退货数量：</label>
					<div class="col-sm-3">
						<form:input path="backQty" htmlEscape="false"  onchange="changeNum(this)"  class="form-control  isFloatGteZero"/>
					</div>
				
					<label class="col-sm-2 control-label">单位：</label>
					<div class="col-sm-3">
						<form:input path="unitCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">单价：</label>
					<div class="col-sm-3">
						<form:input path="realPriceTaxed" htmlEscape="false"  onchange="changeNum(this)"   class="form-control  isFloatGteZero"/>
					</div>
				
					<label class="col-sm-2 control-label">总金额：</label>
					<div class="col-sm-3">
						<form:input path="realSumTaxed" htmlEscape="false"    class="form-control  isFloatGteZero"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">退货原因：</label>
					<div class="col-sm-10">
						<textarea id="backReason" name="backReason" class="form-control" rows="3"></textarea>
					</div>
				</div>
				
				
				</div>
				
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">IQC来料检验报告功能检查缺陷描述：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">IQC来料检验报告外观检查缺陷描述：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">IQC来料检验报告产品尺寸缺陷描述：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#purreportfundetailList', purreportfundetailRowIdx, purreportfundetailTpl);purreportfundetailRowIdx = purreportfundetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>产品功能缺陷描述</th>
						<th>致命缺陷产品数量</th>
						<th>严重缺陷产品数量</th>
						<th>轻微缺陷产品数量</th>
						<th>不良品数量</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="purreportfundetailList">
				</tbody>
			</table>
			<script type="text/template" id="purreportfundetailTpl">//<!--
				<tr id="purreportfundetailList{{idx}}">
					<td class="hide">
						<input id="purreportfundetailList{{idx}}_id" name="purreportfundetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purreportfundetailList{{idx}}_delFlag" name="purreportfundetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<sys:gridselect url="${ctx}/purreportnew/purReportNew/data1" id="purreportfundetailList{{idx}}_matterType" name="purreportfundetailList[{{idx}}].matterType.id" value="{{row.matterType.id}}" labelName="purreportfundetailList{{idx}}.matterType.mattername" labelValue="{{row.matterType.mattername}}"
							 title="选择产品功能缺陷描述" cssClass="form-control  required" fieldLabels=" 问题类型编码|问题类型名称" fieldKeys="mattercode|mattername" searchLabels=" 问题类型编码|问题类型名称" searchKeys="mattercode|mattername" ></sys:gridselect>
					</td>
					
					
					<td>
						<input id="purreportfundetailList{{idx}}_crNum" name="purreportfundetailList[{{idx}}].crNum" type="text" value="{{row.crNum}}" onchange="change(this)"   class="form-control  isIntGteZero"/>
					</td>
					
					
					<td>
						<input id="purreportfundetailList{{idx}}_majNum" name="purreportfundetailList[{{idx}}].majNum" type="text" value="{{row.majNum}}"   onchange="change(this)" class="form-control  isIntGteZero"/>
					</td>
					
					
					<td>
						<input id="purreportfundetailList{{idx}}_minNum" name="purreportfundetailList[{{idx}}].minNum" type="text" value="{{row.minNum}}"    class="form-control  isIntGteZero"/>
					</td>
					
					
					<td>
						<input id="purreportfundetailList{{idx}}_sum" name="purreportfundetailList[{{idx}}].sum" type="text" value="{{row.sum}}"    class="form-control  isIntGteZero"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#purreportfundetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purreportfundetailRowIdx = 0, purreportfundetailTpl = $("#purreportfundetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(purReportNew.purreportfundetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#purreportfundetailList', purreportfundetailRowIdx, purreportfundetailTpl, data[i]);
						purreportfundetailRowIdx = purreportfundetailRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-2" class="tab-pane fade">
			<a class="btn btn-white btn-sm" onclick="addRow('#purreportnewsnList', purreportnewsnRowIdx, purreportnewsnTpl);purreportnewsnRowIdx = purreportnewsnRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>产品外观缺陷描述</th>
						<th>致命缺陷产品数量</th>
						<th>严重缺陷产品数量</th>
						<th>轻微缺陷产品数量</th>
						<th>不良品数量</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="purreportnewsnList">
				</tbody>
			</table>
			<script type="text/template" id="purreportnewsnTpl">//<!--
				<tr id="purreportnewsnList{{idx}}">
					<td class="hide">
						<input id="purreportnewsnList{{idx}}_id" name="purreportnewsnList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purreportnewsnList{{idx}}_delFlag" name="purreportnewsnList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<sys:gridselect url="${ctx}/purreportnew/purReportNew/data2" id="purreportnewsnList{{idx}}_matterType" name="purreportnewsnList[{{idx}}].matterType.id" value="{{row.matterType.id}}" labelName="purreportnewsnList{{idx}}.matterType.mattername" labelValue="{{row.matterType.mattername}}"
							 title="选择产品外观缺陷描述" cssClass="form-control  required" fieldLabels="问题类型编码|问题类型名称" fieldKeys="mattercode|mattername" searchLabels="问题类型编码|问题类型名称" searchKeys="mattercode|mattername" ></sys:gridselect>
					</td>
					
					
					<td>
						<input id="purreportnewsnList{{idx}}_crNum" name="purreportnewsnList[{{idx}}].crNum" type="text" value="{{row.crNum}}"  onchange="change(this)"  class="form-control  isIntGteZero"/>
					</td>
					
					
					<td>
						<input id="purreportnewsnList{{idx}}_majNum" name="purreportnewsnList[{{idx}}].majNum" type="text" value="{{row.majNum}}"  onchange="change(this)"  class="form-control  isIntGteZero"/>
					</td>
					
					
					<td>
						<input id="purreportnewsnList{{idx}}_minNum" name="purreportnewsnList[{{idx}}].minNum" type="text" value="{{row.minNum}}"    class="form-control  isIntGteZero"/>
					</td>
					
					
					<td>
						<input id="purreportnewsnList{{idx}}_sum" name="purreportnewsnList[{{idx}}].sum" type="text" value="{{row.sum}}"    class="form-control  isIntGteZero"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#purreportnewsnList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purreportnewsnRowIdx = 0, purreportnewsnTpl = $("#purreportnewsnTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(purReportNew.purreportnewsnList)};
					for (var i=0; i<data.length; i++){
						addRow('#purreportnewsnList', purreportnewsnRowIdx, purreportnewsnTpl, data[i]);
						purreportnewsnRowIdx = purreportnewsnRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-3" class="tab-pane fade">
			<a class="btn btn-white btn-sm" onclick="addRow('#purreportsizedetailList', purreportsizedetailRowIdx, purreportsizedetailTpl);purreportsizedetailRowIdx = purreportsizedetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>产品尺寸缺陷描述</th>
						<th>致命缺陷产品数量</th>
						<th>严重缺陷产品数量</th>
						<th>轻微缺陷产品数量</th>
						<th>不良品数量</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="purreportsizedetailList">
				</tbody>
			</table>
			<script type="text/template" id="purreportsizedetailTpl">//<!--
				<tr id="purreportsizedetailList{{idx}}">
					<td class="hide">
						<input id="purreportsizedetailList{{idx}}_id" name="purreportsizedetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purreportsizedetailList{{idx}}_delFlag" name="purreportsizedetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<sys:gridselect url="${ctx}/purreportnew/purReportNew/data3" id="purreportsizedetailList{{idx}}_matterType" name="purreportsizedetailList[{{idx}}].matterType.id" value="{{row.matterType.id}}" labelName="purreportsizedetailList{{idx}}.matterType.mattername" labelValue="{{row.matterType.mattername}}"
							 title="选择产品尺寸缺陷描述" cssClass="form-control  required" fieldLabels="问题类型编码|问题类型名称" fieldKeys="mattercode|mattername" searchLabels="问题类型编码|问题类型名称" searchKeys="mattercode|mattername" ></sys:gridselect>
					</td>
					
					
					<td>
						<input id="purreportsizedetailList{{idx}}_crNum" name="purreportsizedetailList[{{idx}}].crNum" type="text" value="{{row.crNum}}"  onchange="change(this)"  class="form-control  isIntGteZero"/>
					</td>
					
					
					<td>
						<input id="purreportsizedetailList{{idx}}_majNum" name="purreportsizedetailList[{{idx}}].majNum" type="text" value="{{row.majNum}}"  onchange="change(this)"  class="form-control  isIntGteZero"/>
					</td>
					
					
					<td>
						<input id="purreportsizedetailList{{idx}}_minNum" name="purreportsizedetailList[{{idx}}].minNum" type="text" value="{{row.minNum}}"    class="form-control  isIntGteZero"/>
					</td>
					
					
					<td>
						<input id="purreportsizedetailList{{idx}}_sum" name="purreportsizedetailList[{{idx}}].sum" type="text" value="{{row.sum}}"    class="form-control  isIntGteZero"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#purreportsizedetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purreportsizedetailRowIdx = 0, purreportsizedetailTpl = $("#purreportsizedetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(purReportNew.purreportsizedetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#purreportsizedetailList', purreportsizedetailRowIdx, purreportsizedetailTpl, data[i]);
						purreportsizedetailRowIdx = purreportsizedetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('purreportnew:purReportNew:edit') || isAdd}">
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
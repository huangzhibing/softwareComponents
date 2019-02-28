<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购计划查询</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		var numberLog=new Map();//记录数量是否修改 用于记录数量历史修改字段；MAP数据结构为  『修改字段id，数量}
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					jp.loading("正在处理...");
					numberLog.forEach(function (item, key, mapObj) {
					    var str=$("#"+key).val()+" "+"${fns:getUser().name}"+"-"+formatDateToString()+"-"+item;
					    $("#"+key).val(str);
					});
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					jp.close();
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
	        $('#planDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#makeDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        
	        
	        
		});
		
	/*	function button_save(){
			$('#billStateFlag').val('A');
			jp.loading("正在处理...");
			form=$('#inputForm');
			jp.post("${ctx}/common/chkCode",{tableName:"pur_planmain",fieldName:"bill_num",fieldValue:$('#billNum').val()},function(data){if("${isAdd}"==""){form.submit()}else{if(data=='true'){form.submit();}else{jp.warning("计划编号已存在！");return false;}}});
			jp.close();
		}
		function button_submit(){
			$('#billStateFlag').val('W');
			jp.loading("正在处理...");
			form=$('#inputForm');
			jp.post("${ctx}/common/chkCode",{tableName:"pur_planmain",fieldName:"bill_num",fieldValue:$('#billNum').val()},function(data){if("${isAdd}"==""){form.submit()}else{if(data=='true'){form.submit();}else{jp.warning("计划编号已存在！");return false;}}});
			jp.close();
		}*/
		
		
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
					 // , widgetPositioning:{vertical :"bottom"}

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
			 for (var i=1; i<items.length; i++){
					//addRowModify('#applyDetailList', applyDetailRowIdx, applyDetailTpl, items[i]);
				    addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl);
				    addRowModify('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, items[i],obj,targetField,targetId,nam,labNam);		
				    purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
				}
		}
        		
		function addRowModify(list, idx, tpl, row, obj,targetField,targetId,nam,labNam){
		/*	$(list).append(Mustache.render(tpl, {
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
			});*/
			//给gridselect-modify1标签的显示input标签赋值，后台所传显示
			$(list+idx+"_"+obj+"Names").val(row[labNam]);
			//为gridselect-modify1隐含的标签赋值,提交时传给后台
			$(list+idx+"_"+obj+"Id").val(row[nam]);
			//table标签的其他字段赋值
		  //  $(list+idx+"_"+targetField[0]).val(row.name);
			//给各标签赋值
			for(var i=0;i<targetField.length;i++){
				//获取标签id
				var ind=targetField[i];
				//获取对象所填充的属性
				var tId=targetId[i];
				$(list+idx+"_"+tId).val(row[ind]);
			}						
		}
		
		
		function qtySum(obj){
			var listId= obj.id.split("_")[0];
			var priceId=listId+"_planPrice";
			var sumId=listId+"_planSum";
			$('#'+sumId).val($('#'+priceId).val()*$(obj).val());
			numberLog.set(listId+"_log",$(obj).val());
			
		}
		function priceSum(obj){
			var listId= obj.id.split("_")[0];
			var qtyId=listId+"_planQty";
			var sumId=listId+"_planSum";
			$('#'+sumId).val($('#'+qtyId).val()*$(obj).val());
			
		}
		
		/** 
		 * 格式化日期 
		 * @param date 
		 * @returns {String} 
		 */  
		function formatDateToString(){  
		    
		    date = new Date();  
		   
		    var year = date.getFullYear();  
		    var month = date.getMonth()+1;  
		    var day = date.getDate();  
		    if(month<10) month = "0"+month;  
		    if(day<10) day = "0"+day;  
		    return year+month+day;  
		}  
	
	</script>
	<style type="text/css">
		.bootstrap-datetimepicker-widget{
			/*z-index: 99;*/
			/*position: absolute;*/
			/*position: sticky;*/
			/*z-index: 999;*/

		}
	</style>
</head>
<body >
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" onclick="history.go(-1)"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="purPlanMain" action="${ctx}/purplan/purPlanMainQuery/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="billStateFlag" />
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>采购计划编号：</label>
					<div class="col-sm-3">
						<form:input path="billNum" htmlEscape="false"  readonly="true" value="${billNum}"  class="form-control "/>
					</div>
					
					<label class="col-sm-2 control-label"><font color="red">*</font>单据状态：</label>
					<div class="col-sm-3">
						<form:select path="billStateFlag" class="form-control "  disabled="true" >
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('purplan_state_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>


				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计划日期：</label>
					<div class="col-sm-3">
							<div class='input-group form_datetime' id='planDate'>
			                    <input type='text'  name="planDate" class="form-control required"  readonly="true" value="<fmt:formatDate value="${purPlanMain.planDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>制单日期：</label>
					<div class="col-sm-3">
							<div class='input-group form_datetime' id='makeDate'>
			                    <input type='text'  name="makeDate" class="form-control " cssClass="form-control required" readonly="true"  value="<fmt:formatDate value="${purPlanMain.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
			            
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人编号：</label>
					<div class="col-sm-3">
						<form:input path="makeEmpid.no" htmlEscape="false"  readonly="true"     class="form-control "/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人名称：</label>
					<div class="col-sm-3">
						<form:input path="makeEmpname" htmlEscape="false"  readonly="true"     class="form-control "/>
					</div>
					
				</div>
				
				<div class="form-group">
					
					<label class="col-sm-2 control-label"><font color="red">*</font>计划类型编码：</label>
					<div class="col-sm-3">
						<sys:gridselect-pursup url="${ctx}/plantype/planType/data" id="planTypeCode" name="planTypeCode.id" value="${purPlanMain.planTypeCode.id}" labelName="planTypeCode.plantypeId" labelValue="${purPlanMain.planTypeCode.plantypeId}" disabled="disabled"
							 title="选择计划类型编码" cssClass="form-control required" fieldLabels="计划类型编码|计划类型名称" fieldKeys="plantypeId|plantypename" searchLabels="计划类型编码|计划类型名称" searchKeys="plantypeId|plantypename" targetId="planTypeName" targetField="plantypename" ></sys:gridselect-pursup>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>计划类型名称：</label>
					<div class="col-sm-3">
						<form:input path="planTypeName" htmlEscape="false" readonly="true"   class="form-control required"/>
					</div>
				</div>



			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>供应商编码：</label>
				<div class="col-sm-3">
					<sys:gridselect-pursup url="${ctx}/account/account/data" id="supId" name="supId.id" value="${purPlanMain.supId.id}" labelName="supId.accountCode" labelValue="${purPlanMain.supId.accountCode}" disabled="disabled"
										   title="选择供应商编号" cssClass="form-control  " fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" targetId="supName" targetField="accountName" ></sys:gridselect-pursup>

				</div>

				<label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
				<div class="col-sm-3">
					<form:input path="supName" htmlEscape="false" readonly="true"  class="form-control "/>
				</div>

			</div>


			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>税率：</label>
				<div class="col-sm-3">
					<input id="taxRatio" htmlEscape="false" readonly="true" value="<fmt:formatNumber value="${purPlanMain.taxRatio}" maxIntegerDigits="4" type="percent"/>" class="form-control "/>
				</div>

				<label class="col-sm-2 control-label">单据说明：</label>
				<div class="col-sm-3">
					<form:textarea path="makeNotes" htmlEscape="false" readonly="true"  rows="4"   class="form-control "/>
				</div>

			</div>



		<div class="tabs-container" >
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">计划信息</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">需求信息</a>
				</li>
				<li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">合同信息</a>
				</li>
				<li class=""><a data-toggle="tab" href="#tab-4" aria-expanded="false">到货信息</a>
				</li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active"  >
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl);purPlanDetailRowIdx = purPlanDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
<div class="table-responsive" style="max-height: 500px">
			<table class="table table-striped table-bordered table-condensed " style="min-width: 3000px"   >

				<thead>
					<tr>
						<th class="hide"></th>
						<th>序号</th>
						<th>物料编号</th>
						<th>物料名称</th>
						<th>物料规格</th>
						<th>材质</th>
						<th>需求日期</th>
						<th>数量</th>
						<th>单位</th>
						<th>数量修改历史</th>
						<th>价格</th>
						<th>总额</th>
						<th>库存数量</th>
						<th>移动平均价</th>
						<th>安全库存量</th>
						<%--<th>采购员编码</th>
						<th>采购员名称</th>
						<th>供应商编号</th>
						<th>供应商名称</th>--%>
						<th>备注</th>
					</tr>
				</thead>
				<tbody id="purPlanDetailList"  >
				</tbody>
			</table>
</div>
			<script type="text/template" id="purPlanDetailTpl">//<!--
				<tr id="purPlanDetailList{{idx}}">
					<td class="hide">
						<input id="purPlanDetailList{{idx}}_id" name="purPlanDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purPlanDetailList{{idx}}_delFlag" name="purPlanDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="purPlanDetailList{{idx}}_serialNum" name="purPlanDetailList[{{idx}}].serialNum" readonly="true" type="text"  value="{{row.serialNum}}"  style="width:50px"   class="form-control number"/>
					</td>
					
					
					<td>
						<sys:gridselect-purmul url="${ctx}/purplan/purPlanMain/itemsdata" id="purPlanDetailList{{idx}}_itemCode" name="purPlanDetailList[{{idx}}].itemCode.id" value="{{row.itemCode.id}}" labelName="purPlanDetailList{{idx}}.itemCode.code" labelValue="{{row.itemCode.code}}"  disabled="disabled"
							 title="选择物料编号" cssClass="form-control required " fieldLabels="物料编号|物料名称" fieldKeys="code|name" searchLabels="物料编号|物料名称" searchKeys="code|name" isMultiSelected="true" targetId="itemName|itemSpecmodel|unitName|planPrice|safetyQtys|invQty|costPrice|itemTexture" targetField="name|specModel|unit|planPrice|safetyQty|nowQty|costPrice|texture" ></sys:gridselect-purmul>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_itemName" name="purPlanDetailList[{{idx}}].itemName" readonly="true" type="text" value="{{row.itemName}}"  readonly="true"  class="form-control required "/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_itemSpecmodel" name="purPlanDetailList[{{idx}}].itemSpecmodel" readonly="true" type="text" value="{{row.itemSpecmodel}}"    class="form-control required"/>
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_itemTexture" name="purPlanDetailList[{{idx}}].itemTexture" type="text" value="{{row.itemTexture}}"  readonly="true"  class="form-control "/>
					</td>

					<td>
						<div class='input-group form_datetime' id="purPlanDetailList{{idx}}_requestDate">
		                    <input type='text'  name="purPlanDetailList[{{idx}}].requestDate" class="form-control required" readonly="true"  value="{{row.requestDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_planQty" name="purPlanDetailList[{{idx}}].planQty" type="text" readonly="true" value="{{row.planQty}}" onchange="qtySum(this)"   class="form-control required number"/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_unitName" name="purPlanDetailList[{{idx}}].unitName" type="text" readonly="true" value="{{row.unitName}}"    class="form-control "/>
					</td>
					
					<td>
						<input id="purPlanDetailList{{idx}}_log" name="purPlanDetailList[{{idx}}].log" type="text" value="{{row.log}}" readonly="true"   class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_planPrice" name="purPlanDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}" readonly="true"  onchange="priceSum(this)"  class="form-control required number"/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_planSum" name="purPlanDetailList[{{idx}}].planSum" type="text" value="{{row.planSum}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_invQty" name="purPlanDetailList[{{idx}}].invQty" type="text" value="{{row.invQty}}"  readonly="true"  class="form-control number"/>
					</td>
					
					<td>
						<input id="purPlanDetailList{{idx}}_costPrice" name="purPlanDetailList[{{idx}}].costPrice" type="text" value="{{row.costPrice}}"  readonly="true"  class="form-control "/>
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_safetyQtys" name="purPlanDetailList[{{idx}}].safetyQtys" type="text" value="{{row.safetyQtys}}"  readonly="true"  class="form-control number"/>
					</td>

				<%--	<td>
						<sys:gridselect-pursup-new url="${ctx}/group/groupQuery/buyersdata" id="purPlanDetailList{{idx}}_buyerId" name="purPlanDetailList[{{idx}}].buyerId.id" value="{{row.buyerId.id}}" labelName="purPlanDetailList{{idx}}.buyerId.user.no" labelValue="{{row.buyerId.user.no}}" disabled="disabled"
							 title="选择采购员编码" cssClass="form-control  " fieldLabels="采购员编码|采购员名称" fieldKeys="user.no|buyername" searchLabels="采购员编码|采购员名称" searchKeys="user.no|buyername" targetId="buyerName" targetField="buyername" ></sys:gridselect-pursup-new>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_buyerName" name="purPlanDetailList[{{idx}}].buyerName" type="text" value="{{row.buyerName}}"  readonly="true"  class="form-control "/>
					</td>
					

					
					<td>
						<sys:gridselect-pursup-new url="${ctx}/account/account/data" id="purPlanDetailList{{idx}}_supId" name="purPlanDetailList[{{idx}}].supId.id" value="{{row.supId.id}}" labelName="purPlanDetailList{{idx}}.supId.accountCode" labelValue="{{row.supId.accountCode}}" disabled="disabled"
							 title="选择供应商编号" cssClass="form-control  " fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" targetId="supName" targetField="accountName" ></sys:gridselect-pursup-new>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_supName" name="purPlanDetailList[{{idx}}].supName" type="text" value="{{row.supName}}"  readonly="true"   class="form-control "/>
					</td>--%>
					
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_notes" name="purPlanDetailList[{{idx}}].notes" type="text" value="{{row.notes}}" readonly="true"   class="form-control "/>
					</td>
					
					
					

				</tr>//-->
			</script>
			<script type="text/javascript">
				var purPlanDetailRowIdx = 0, purPlanDetailTpl = $("#purPlanDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					
					var data = ${fns:toJson(purPlanMain.purPlanDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, data[i]);
						purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
					}
				});
			</script>
			</div>

				<div id="tab-2" class="tab-pane fade">
					<div class="table-responsive"  style="max-height: 500px">
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
								<th>需求部门</th>
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
						<input id="applyDetailList{{idx}}_billNum" name="applyDetailList[{{idx}}].billNum" type="text"   value="{{row.billNum}}" readonly="true" class="form-control "/>
					</td>




					<td width="90px">
						<input id="applyDetailList{{idx}}_item" name="applyDetailList[{{idx}}].item.code" type="text" value="{{row.itemCode.code}}"  readonly="true"  class="form-control "/>
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
						<input id="applyDetailList{{idx}}_nowSum" name="applyDetailList[{{idx}}].invQty" type="text" value="{{row.invQty}}"  readonly="true"  class="form-control "/>
					</td>

					<td width="70px">
						<input id="applyDetailList{{idx}}_unitName" name="applyDetailList[{{idx}}].unitName" type="text" value="{{row.unitName}}"  readonly="true"  class="form-control "/>
					</td>

					<td width="70px">
						<input id="applyDetailList{{idx}}_costPrice" name="applyDetailList[{{idx}}].costPrice" type="text" value="{{row.costPrice}}" readonly="true" onchange="costPriceChange(this)"  class="form-control "/>
					</td>

					<td width="70px">
						<input id="applyDetailList{{idx}}_applySum" name="applyDetailList[{{idx}}].planSum" type="text" value="{{row.planSum}}" readonly="true"   class="form-control number"/>
					</td>

					<td width="70px">
						<input id="applyDetailList{{idx}}_DeptName" name="applyDetailList[{{idx}}].applyDept" type="text" value="{{row.applyDept}}" readonly="true"   class="form-control"/>
					</td>
					<td width="70px">
					     <select id="applyDetailList{{idx}}_opFlag" name="applyDetailList[{{idx}}].opFlag" disabled="true" data-value="{{row.opFlag}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('rollplan_op_flag')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						 </select>
                    </td>

				</tr>//-->
					</script>
					<script type="text/javascript">
                        var applyDetailRowIdx = 0, applyDetailTpl = $("#applyDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                        $(document).ready(function() {
                            var data = ${fns:toJson(purPlanMain.rollPlanNewList)};//console.log(data);
                            for (var i=0; i<data.length; i++){

                                data[i].requestDate=data[i].requestDate.split(" ")[0];
                                addRow('#applyDetailList', applyDetailRowIdx, applyDetailTpl, data[i]);
                                applyDetailRowIdx = applyDetailRowIdx + 1;
                            }
                        });
					</script>
				</div>

				<div id="tab-3" class="tab-pane fade ">
					<div class="table-responsive"  style="max-height: 500px">
						<table class="table table-striped table-bordered table-condensed"  style="min-width: 2410px">
							<thead>
							<tr>
								<th class="hide"></th>
								<th width="200px">合同号</th>
								<th width="200px">合同状态</th>
								<th width="200px">物料编码</th>
								<th width="250px">物料名称</th>
								<th width="250px">物料规格</th>
								<th width="250px">物料材质</th>
								<th width="150px">签合同量</th>
								<th width="150px">无税单价</th>
								<th width="150px">无税总额</th>
								<th width="150px">含税单价</th>
								<th width="150px">含税金额</th>
								<th width="100px">单位</th>
								<th width="150px">单位运费（含）</th>
								<th width="150px">运费金额（含）</th>
								<th width="250px">质量要求</th>
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
						<input id="contractDetailList{{idx}}_billNum" name="contractDetailList[{{idx}}].serialNum" type="text" value="{{row.contractMain.billNum}}"  readOnly="true"  class="form-control "/>
					</td>


					<td>
                        <select id="contractDetailList{{idx}}_billStateFlag" name="contractDetailList[{{idx}}].billStateFlag" disabled="disabled" data-value="{{row.contractMain.billStateFlag}}"  class="form-control m-b ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('ContractStateFlag')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						<lect>
					</td>


					<%--<td>
						<input id="contractDetailList{{idx}}_serialNum" name="contractDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"  readOnly="true"  class="form-control "/>
					</td>--%>


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
                            var data = ${fns:toJson(purPlanMain.contractDetailList)};
                            //console.log(data);
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

				<div id="tab-4" class="tab-pane fade">
					<div class="table-responsive"  style="max-height: 500px">
						<table class="table table-striped table-bordered table-condensed"  style="min-width: 150%">
							<thead>
							<tr>
								<th class="hide"></th>
								<th>到货单号</th>
								<th>物料编号</th>
								<th>物料名称</th>
								<th>物料规格</th>
								<th width="100">单位</th>
								<th width="150">到货数量</th>
								<th width="150">实际单价</th>
								<th width="150">实际金额</th>
								<th>日期</th>
								<th width="200">单据状态</th>
								<th>合同号</th>

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

					<%--<td>
						<input id="invCheckDetailList{{idx}}_serialnum" name="invCheckDetailList[{{idx}}].serialnum" type="text" readonly='true' value="{{row.serialnum}}"    class="form-control "/>
					</td>--%>


					<td>
						<sys:gridselect-purmul url="${ctx}/item/item/data" id="invCheckDetailList{{idx}}_item" name="invCheckDetailList[{{idx}}].item.id" value="{{row.item.id}}" labelName="invCheckDetailList{{idx}}.item.code" labelValue="{{row.item.code}}" disabled="disabled"
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
						<input id="invCheckDetailList{{idx}}_checkQty" name="invCheckDetailList[{{idx}}].checkQty" type="text" value="{{row.checkQty}}"   readonly='true' class="form-control " onchange="priceCalculate()"/>
					</td>


					<td>
						<input id="invCheckDetailList{{idx}}_realPrice" name="invCheckDetailList[{{idx}}].realPrice" type="text" value="{{row.realPrice}}"  readonly='true'  class="form-control " onchange="priceCalculate()"/>
					</td>


					<td>
						<input id="invCheckDetailList{{idx}}_realSum" name="invCheckDetailList[{{idx}}].realSum" type="text" value="{{row.realSum}}"    class="form-control " readonly="true"/>
					</td>

                    <td>
						<input id="invCheckDetailList{{idx}}_arriveDate" name="invCheckDetailList[{{idx}}].arriveDate" type="text" value="{{row.arriveDate}}"  readonly='true'  class="form-control "  onchange="priceCalculate()"/>
					</td>





					<td>
                        <select id="invCheckDetailList{{idx}}_billStateFlag" name="invCheckDetailList[{{idx}}].billStateFlag" disabled="disabled" data-value="{{row.invCheckMain.billStateFlag}}"  class="form-control m-b ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('ContractStateFlag')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						<lect>
					</td>

					<td>
						<input id="invCheckDetailList{{idx}}_conBillNum" name="invCheckDetailList[{{idx}}].conBillNum" type="text" value="{{row.conBillNum}}"  readonly='true'  class="form-control " onchange="priceCalculate()"/>
					</td>

					<%--<td>
						<input id="invCheckDetailList{{idx}}_planBillNum" name="invCheckDetailList[{{idx}}].planBillNum" type="text" value="{{row.planBillNum}}"  readonly='true'  class="form-control " onchange="priceCalculate()"/>
					</td>--%>


				</tr>//-->
					</script>
					<script type="text/javascript">
                        var invCheckDetailRowIdx = 0, invCheckDetailTpl = $("#invCheckDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                        $(document).ready(function() {
                            var data = ${fns:toJson(purPlanMain.invCheckDetailList)};
                            console.log(data);
                            for (var i=0; i<data.length; i++){
                                addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl, data[i]);
                                invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
                            }
                        });
					</script>
				</div>


		</div>
		</div>
		<br><br><br>
      <div class="row">

				<div class="col-lg-4"></div>
		       
		         <div class="col-lg-4">
		             <div class="form-group text-center">
		                 <div>
		                     <a  class="btn btn-primary btn-block btn-lg btn-parsley"  onclick="history.go(-1)" data-loading-text="正在返回..." >返 回</a>
		                 </div>
		             </div>
		        </div>

      </div>
            <c:if test="${not empty purPlanMain.act.procInsId}">
            <div class="row">
                <act:flowChart procInsId="${purPlanMain.act.procInsId}"/>
                <act:histoicFlow procInsId="${purPlanMain.act.procInsId}" />
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
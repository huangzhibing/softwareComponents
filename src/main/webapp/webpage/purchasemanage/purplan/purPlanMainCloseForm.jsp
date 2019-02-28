<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购计划管理</title>
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
			jp.post("${ctx}/common/chkCode",{tableName:"pur_planmain",fieldName:"bill_num",fieldValue:$('#billNum').val()},function(data){if("${isAdd}"==""){form.submit()}else{if(data=='true'){form.submit();}else{jp.warning("问题处理报告单编号已存在！");return false;}}});
			jp.close();
		}*/
		function button_submit(){
			$('#billStateFlag').val('O');
			jp.loading("正在处理...");
			form=$('#inputForm');
			jp.post("${ctx}/common/chkCode",{tableName:"pur_planmain",fieldName:"bill_num",fieldValue:$('#billNum').val()},function(data){if("${isAdd}"==""){form.submit()}else{if(data=='true'){form.submit();}else{jp.warning("问题处理报告单编号已存在！");return false;}}});
			jp.close();
		}
		
		
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
		<form:form id="inputForm" modelAttribute="purPlanMain" action="${ctx}/purplan/purPlanMainClose/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="billStateFlag" />
		<%--工作流涉及的变量--%>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
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
			                    <input type='text'  name="planDate" class="form-control required" readonly="true"  value="<fmt:formatDate value="${purPlanMain.planDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
						<form:input path="makeEmpname" htmlEscape="false"  readonly="true"    class="form-control "/>
					</div>
					
				</div>
				
				<div class="form-group">
					
					<label class="col-sm-2 control-label"><font color="red">*</font>计划类型编码：</label>
					<div class="col-sm-3">
						<sys:gridselect-pursup url="${ctx}/plantype/planType/data" id="planTypeCode" name="planTypeCode.id" value="${purPlanMain.planTypeCode.id}" labelName="planTypeCode.plantypeId" labelValue="${purPlanMain.planTypeCode.plantypeId}"  disabled="disabled"
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
					<form:input path="supName" htmlEscape="false"  readonly="true" class="form-control "/>
				</div>

			</div>


			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>税率：</label>
				<div class="col-sm-3">
					<form:input path="taxRatio" htmlEscape="false" readonly="true"  class="form-control "/>
				</div>

				<label class="col-sm-2 control-label">单据说明：</label>
				<div class="col-sm-3">
					<form:textarea path="makeNotes" htmlEscape="false" readonly="true"  rows="3"   class="form-control "/>
				</div>

			</div>


		<div class="tabs-container" >
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">采购计划子表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active"  >
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl);purPlanDetailRowIdx = purPlanDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
<div class="table-responsive">
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
						<input id="purPlanDetailList{{idx}}_serialNum" name="purPlanDetailList[{{idx}}].serialNum" readonly="true" type="text" value="{{row.serialNum}}"    style="width:50px"   class="form-control number"/>
					</td>
					
					
					<td>
						<sys:gridselect-purmul url="${ctx}/purplan/purPlanMain/itemsdata" id="purPlanDetailList{{idx}}_itemCode" name="purPlanDetailList[{{idx}}].itemCode.id" value="{{row.itemCode.id}}" labelName="purPlanDetailList{{idx}}.itemCode.code" labelValue="{{row.itemCode.code}}" disabled="disabled"
							 title="选择物料编号" cssClass="form-control required " fieldLabels="物料编号|物料名称" fieldKeys="code|name" searchLabels="物料编号|物料名称" searchKeys="code|name" isMultiSelected="true" targetId="itemName|itemSpecmodel|unitName|planPrice|safetyQtys|invQty|costPrice|itemTexture" targetField="name|specModel|unit|planPrice|safetyQty|nowQty|costPrice|texture" ></sys:gridselect-purmul>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_itemName" name="purPlanDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"  readonly="true"  class="form-control required "/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_itemSpecmodel" name="purPlanDetailList[{{idx}}].itemSpecmodel" type="text" value="{{row.itemSpecmodel}}"  readonly="true"  class="form-control required"/>
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_itemTexture" name="purPlanDetailList[{{idx}}].itemTexture" type="text" value="{{row.itemTexture}}"  readonly="true"  class="form-control "/>
					</td>

					<td>
						<div class='input-group form_datetime' id="purPlanDetailList{{idx}}_requestDate">
		                    <input type='text'  name="purPlanDetailList[{{idx}}].requestDate" class="form-control required" readonly="true" value="{{row.requestDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_planQty" name="purPlanDetailList[{{idx}}].planQty" type="text" value="{{row.planQty}}" onchange="qtySum(this)" readonly="true"  class="form-control required number"/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_unitName" name="purPlanDetailList[{{idx}}].unitName" type="text" value="{{row.unitName}}" readonly="true"   class="form-control "/>
					</td>
					
					<td>
						<input id="purPlanDetailList{{idx}}_log" name="purPlanDetailList[{{idx}}].log" type="text" value="{{row.log}}" readonly="true"   class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_planPrice" name="purPlanDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}" readonly="true" onchange="priceSum(this)"  class="form-control required number"/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_planSum" name="purPlanDetailList[{{idx}}].planSum" type="text" value="{{row.planSum}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_invQty" name="purPlanDetailList[{{idx}}].invQty" type="text" value="{{row.invQty}}"  readonly="true"  class="form-control number"/>
					</td>
					
					<td>
						<input id="purPlanDetailList{{idx}}_costPrice" name="purPlanDetailList[{{idx}}].costPrice" type="text" value="{{row.costPrice}}" readonly="true"   class="form-control "/>
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_safetyQtys" name="purPlanDetailList[{{idx}}].safetyQtys" type="text" value="{{row.safetyQtys}}"  readonly="true"  class="form-control number"/>
					</td>

					<%--<td>
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
						<input id="purPlanDetailList{{idx}}_supName" name="purPlanDetailList[{{idx}}].supName" type="text" value="{{row.supName}}"  readonly="true"  class="form-control "/>
					</td>--%>
					
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_notes" name="purPlanDetailList[{{idx}}].notes" type="text" value="{{row.notes}}"  readonly="true"  class="form-control "/>
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
		</div>
		</div>
		<br><br><br>
	<div class="row">
		<c:choose>
		<c:when test="${isEdit||isAdd}">
				<%--<div class="col-lg-2"></div>--%>
		        <div class="col-lg-2">
		             <%--<div class="form-group text-center">
		                 <div>
		                     <input type="button" id="save_btn" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="button_save()" data-loading-text="正在保存..." value="保 存"></input>
		                 </div>
		             </div>--%>
		        </div>
		        <div class="col-lg-1"></div>
		        <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                     <input type="button" id="submit_btn" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="button_submit()" data-loading-text="正在结案..." value="结 案"></input>
		                 </div>
		             </div>
		        </div>
		        <div class="col-lg-1"></div>
		         <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                     <a  class="btn btn-primary btn-block btn-lg btn-parsley"  onclick="history.go(-1)" data-loading-text="正在返回..." >返 回</a>
		                 </div>
		             </div>
		        </div>
		</c:when>
		<c:otherwise>
				<div class="col-lg-4"></div>
		       
		         <div class="col-lg-4">
		             <div class="form-group text-center">
		                 <div>
		                     <a  class="btn btn-primary btn-block btn-lg btn-parsley" onclick="history.go(-1)" data-loading-text="正在返回..." >返 回</a>
		                 </div>
		             </div>
		        </div>
		</c:otherwise>
		</c:choose>
	</div>
			<div class="row">
				<act:flowChart procInsId="${purPlanMain.act.procInsId}"/>
				<act:histoicFlow procInsId="${purPlanMain.act.procInsId}" />
			</div>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
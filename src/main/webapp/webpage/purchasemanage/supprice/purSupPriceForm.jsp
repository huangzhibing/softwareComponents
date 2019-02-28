<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>供应商价格维护管理</title>
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
		
		//在具体的使用中需替换'#purSupPriceDetailList', purSupPriceDetailRowIdx, purSupPriceDetailTpl这三个值
	      function setOtherValue(items,obj,targetField,targetId,nam,labNam){
			//alert("wq");	
			for (var i=1; i<items.length; i++){
						addRow('#purSupPriceDetailList', purSupPriceDetailRowIdx, purSupPriceDetailTpl);	
					    addRowModify('#purSupPriceDetailList', purSupPriceDetailRowIdx, purSupPriceDetailTpl, items[i],obj,targetField,targetId,nam,labNam);		
						purSupPriceDetailRowIdx = purSupPriceDetailRowIdx + 1;
					}
			}
	        		
			function addRowModify(list, idx, tpl, row, obj,targetField,targetId,nam,labNam){			
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
					
					
					
					if(ind=="costPrice"&&row[ind]=="null"){
						$(list+idx+"_"+tId).val("");
					}else{
						$(list+idx+"_"+tId).val(row[ind]);
					}
					
				}						
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
				<a class="panelButton" href="${ctx}/supprice/purSupPrice"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="purSupPrice" action="${ctx}/supprice/purSupPrice/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商代码：</label>
					<div class="col-sm-10">
						<sys:gridselect-sup url="${ctx}/account/account/data" id="account" name="account.id" value="${purSupPrice.account.id}" labelName="account.accountCode" labelValue="${purSupPrice.account.accountCode}"
							 title="选择供应商代码" cssClass="form-control required" 
							 targetId="supName|areaCode|areaName|prop|address" targetField="accountName|areaCode|areaName|accountProp|accountAddr" 
							 fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" ></sys:gridselect-sup>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
					<div class="col-sm-10">
						<form:input path="supName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>所属地区编码：</label>
					<div class="col-sm-10">
						<form:input path="areaCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>所属地区名称：</label>
					<div class="col-sm-10">
						<form:input path="areaName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商类别编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-sup url="${ctx}/suptype/supType/data" id="supType" name="supType.id" value="${purSupPrice.supType.id}" labelName="supType.suptypeId" labelValue="${purSupPrice.supType.suptypeId}"
							 title="选择供应商类别编码" cssClass="form-control required" 
							 targetId="supTypeName" targetField="suptypeName" 
							 fieldLabels="供应商类别编码|供应商类别名称" fieldKeys="suptypeId|suptypeName" searchLabels="供应商类别编码|供应商类别名称" searchKeys="suptypeId|suptypeName" ></sys:gridselect-sup>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商类别名称：</label>
					<div class="col-sm-10">
						<form:input path="supTypeName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>企业性质：</label>
					<div class="col-sm-10">
						<form:input path="prop" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>地址：</label>
					<div class="col-sm-10">
						<form:input path="address" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				
		<div style="width:100%;  overflow-x:scroll; ">			
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">供应商价格维护明细：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#purSupPriceDetailList', purSupPriceDetailRowIdx, purSupPriceDetailTpl);purSupPriceDetailRowIdx = purSupPriceDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed" style="min-width:200%;">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>物料编码</th>
						<th><font color="red">*</font>物料名称</th>
						<th>物料规格</th>
						<th>物料单位</th>
						<th><font color="red">*</font>开始时间</th>
						<th><font color="red">*</font>结束时间</th>
						<th><font color="red">*</font>最小数量</th>
						<th><font color="red">*</font>最大数量</th>
						<th><font color="red">*</font>价格</th>
						<th>备注</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="purSupPriceDetailList">
				</tbody>
			</table>
			<script type="text/template" id="purSupPriceDetailTpl">//<!--
				<tr id="purSupPriceDetailList{{idx}}">
					<td class="hide">
						<input id="purSupPriceDetailList{{idx}}_id" name="purSupPriceDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purSupPriceDetailList{{idx}}_delFlag" name="purSupPriceDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<sys:gridselect-purmul url="${ctx}/item/item/data" id="purSupPriceDetailList{{idx}}_item" name="purSupPriceDetailList[{{idx}}].item.id" value="{{row.item.id}}" labelName="purSupPriceDetailList{{idx}}.item.code" labelValue="{{row.item.code}}"
							 title="选择物料编码" cssClass="form-control  required" 
							targetId="itemName|itemSpecmodel|unitName" targetField="name|specModel|unit" 
							fieldLabels="物料编码|物料名称" fieldKeys="code|name" searchLabels="物料编码|物料名称" searchKeys="code|name" ></sys:gridselect-purmul>
					</td>
					
					
					<td>
						<input id="purSupPriceDetailList{{idx}}_itemName" name="purSupPriceDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control required"/>
					</td>
					

					<td>
						<input id="purSupPriceDetailList{{idx}}_itemSpecmodel" name="purSupPriceDetailList[{{idx}}].itemSpecmodel" type="text" value="{{row.itemSpecmodel}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purSupPriceDetailList{{idx}}_unitName" name="purSupPriceDetailList[{{idx}}].unitName" type="text" value="{{row.unitName}}"    class="form-control "/>
					</td>

					
					<td>
						<div class='input-group form_datetime' id="purSupPriceDetailList{{idx}}_startDate">
		                    <input type='text'  name="purSupPriceDetailList[{{idx}}].startDate" class="form-control required" onchange="changeTime(this)" value="{{row.startDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>
					
					
					<td>
						<div class='input-group form_datetime' id="purSupPriceDetailList{{idx}}_endDate">
		                    <input type='text'  name="purSupPriceDetailList[{{idx}}].endDate" class="form-control required"  onchange="changeTime(this)" value="{{row.endDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>
					
					
					<td>
						<input id="purSupPriceDetailList{{idx}}_minQty" name="purSupPriceDetailList[{{idx}}].minQty" type="text" onchange="change(this)" value="{{row.minQty}}"    class="form-control required number"/>
					</td>
					
					
					<td>
						<input id="purSupPriceDetailList{{idx}}_maxQty" name="purSupPriceDetailList[{{idx}}].maxQty" type="text" onchange="change(this)" value="{{row.maxQty}}"    class="form-control required number"/>
					</td>
					
					
					<td>
						<input id="purSupPriceDetailList{{idx}}_supPrice" name="purSupPriceDetailList[{{idx}}].supPrice" type="text" value="{{row.supPrice}}"    class="form-control required number"/>
					</td>
					
					
					<td>
						<input id="purSupPriceDetailList{{idx}}_notes" name="purSupPriceDetailList[{{idx}}].notes" type="text" value="{{row.notes}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#purSupPriceDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purSupPriceDetailRowIdx = 0, purSupPriceDetailTpl = $("#purSupPriceDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(purSupPrice.purSupPriceDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#purSupPriceDetailList', purSupPriceDetailRowIdx, purSupPriceDetailTpl, data[i]);
						purSupPriceDetailRowIdx = purSupPriceDetailRowIdx + 1;
					}
				});
				
				function change(o){
					var id = o.id;
					
					var name = id.split("_")[0];
					
					var min = $("#"+name+"_minQty").val();
					var max = $("#"+name+"_maxQty").val();
				
					if(min!=""&&max!=""&&parseFloat(min)>parseFloat(max)){
						jp.alert("最小数量、最大数量有误");
					}
				}
				
				function changeTime(o){
					var id = o.id;
					//alert(id);
					var name = id.split("_")[0];
					
					var min = $("#"+name+"_startDate").val();
					var max = $("#"+name+"_endDate").val();
					//alert(min);
					//alert(max);
					if(min!=undefined&&max!=undefined&&min>max){
						//jp.alert("最小数量、最大数量有误");
					}
				}
			</script>
			</div>
		</div>
		</div>
		</div>
		
		<c:if test="${fns:hasPermission('supprice:purSupPrice:edit') || isAdd}">
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购退货管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
	
	function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
        return currentdate;
    }

		function changeSubmit(){
			var billState = "录入完毕";
			$("#billStateFlag").val(billState);

			var newUrl = "${ctx}/purinvcheckmain/invCheckMain/changeStateSave"
			$("#inputForm").attr('action', newUrl).submit();//修改表单action
		}
    
	    function inputResult(){
            var wareid = $("#wareNames").val();
            console.log(wareid);
            $("#wareId").val(wareid);

 			var supid = $("#supNames").val();
			$("#supId").val(supid);
			
			var buyerid = $("#buyerIdNames").val();
			$("#buyerId").val(buyerid);
			
			var iotype = $("#ioTypeNames").val();
			$("#ioType").val(iotype);
	    }
    
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
				if(invCheckDetailRowIdx == 0){
					jp.warning("请输入物料子表信息！");
				}else{
					jp.loading();
					form.submit();
					}
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
			
	        $('#arriveDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#awayDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
		    
		    var currentdate = getNowFormatDate();
		    $("#makeDate").val(currentdate);
		    $("#bdate").val(currentdate);
		    
		    
                   
		});
		
		//在具体的使用中需替换'#applyDetailList', applyDetailRowIdx, applyDetailTpl这三个值
		function setOtherValue(items,obj,targetField,targetId,nam,labNam){
			 for (var i=1; i<items.length; i++){
				    //增加一行
				    addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl);
				    addRowModify('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl, items[i],obj,targetField,targetId,nam,labNam);
				    var preID = '#invCheckDetailList'+invCheckDetailRowIdx;
				    findQmsFlag(items[i].code, preID);
				    invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
				}
		}
        		
		function addRowModify(list, idx, tpl, row, obj,targetField,targetId,nam,labNam){		 
			//给gridselect-modify1标签的显示input标签赋值，后台所传显示
			$(list+idx+"_"+obj+"Names").val(row[labNam]);
		    $(list+idx+"_"+obj+"Names").trigger("change");		    
			//为gridselect-modify1隐含的标签赋值,提交时传给后台
			$(list+idx+"_"+obj+"Id").val(row[nam]);		
		    $(list+idx+"_"+obj+"Id").trigger("change");
			//table标签的其他字段赋值
			//给各标签赋值
			for(var i=0;i<targetField.length;i++){
				//获取标签id
				var ind=targetField[i];
				//获取对象所填充的属性
				var tId=targetId[i];
				$(list+idx+"_"+tId).val(row[ind]);
			    $(list+idx+"_"+tId).trigger("change");	
			}						
		}
		
		
		function addRow(list, idx, tpl, row){
		    /*if(idx == 1 || idx > 1){
		        jp.alert('只能选择一个物料！');
		        return;
            }*/
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
			    });
			});
		}
		function delRow(obj, prefix){
		     // /*减去小记金额*/
		     // var priceSumSubtotal=$("#priceSumSubtotal").val();
		     // var realSumTaxed= $(prefix+"_realSumTaxed").val();
		     // $("#priceSumSubtotal").val(priceSumSubtotal-1*realSumTaxed);
		     // /*减去运费合计*/
		     // var tranpriceSum=$("#tranpriceSum").val();
		     // var transSumTaxed= $(prefix+"_transSumTaxed").val();
		     // $("#tranpriceSum").val(tranpriceSum-1*transSumTaxed);
		     // /*减去金额合计*/
		     // var priceSum=$("#priceSum").val();
		     // $("#priceSum").val(priceSum - (1*realSumTaxed + 1*transSumTaxed));
		     //$(obj).parent().parent().remove();
            var id = $(prefix+"_id");
            var delFlag = $(prefix+"_delFlag");
            if (id.val() == ""){
                invCheckDetailRowIdx=invCheckDetailRowIdx-1;
                $(obj).parent().parent().remove();
            }else if(delFlag.val() == "1"){
                //invCheckDetailRowIdx=invCheckDetailRowIdx-1;
                // $(obj).html("&divide;").attr("title", "撤销删除");
                // $(obj).parent().parent().addClass("error");
            }else if(delFlag.val() == "0"){
                invCheckDetailRowIdx=invCheckDetailRowIdx-1;
                $(obj).parent().parent().remove();
                // $(obj).html("&times;").attr("title", "删除");
                // $(obj).parent().parent().removeClass("error");
            }
		}
		
/* 		function check(obj){ 
			if (isNaN(obj.value)) {
				jp.warning("请输入数字！"); 
				obj.value="";
			}
		}  */
		
		
		
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/purinvcheckmain/invCheckMain/listBackNew"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="invCheckMain" action="${ctx}/purinvcheckmain/invCheckMain/save" method="post" onsubmit="return inputResult()" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="col-sm-3" style="display:none">
		    <form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/> 
			<form:hidden path="wareId" htmlEscape="false"  class="form-control"/> 
			<form:hidden path="supAddress" htmlEscape="false"    class="form-control "/>
			<form:hidden path="supId" htmlEscape="false"    class="form-control "/>
			<form:hidden path="buyerId" htmlEscape="false"  class="form-control"/> 
			
			<form:input path="priceSumSubtotal" htmlEscape="false"    class="form-control " readonly="true"/>
			<form:input path="tranpriceSum" htmlEscape="false"    class="form-control " readonly="true"/>
			<form:input path="priceSum" htmlEscape="false"    class="form-control " readonly="true"/>
			<form:select path="qmsFlag" class="form-control required">
				<form:option value="" label=""/>
				<form:options items="${fns:getDictList('qmsFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		    </form:select>
		</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
					<div class="col-sm-3">
						<form:input path="billnum" htmlEscape="false"    class="form-control" readonly= "true"/>
					</div>
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-3">
						<form:input path="billStateFlag" htmlEscape="false"    class="form-control " readonly= "true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单日期：</label>
					<div class="col-sm-3">
						<form:input path="makeDate" htmlEscape="false"    class="form-control" readonly= "true"/>
					</div>
					<label class="col-sm-2 control-label">业务日期：</label>
					<div class="col-sm-3">
						<form:input path="bdate" htmlEscape="false"    class="form-control" readonly= "true"/>
					</div>
				</div>
				<div class="form-group">
				    <label class="col-sm-2 control-label"><font color="red">*</font>供应商编码：</label>
					<div class="col-sm-3">
						<sys:gridselect-pursup url="${ctx}/account/account/data" id="sup" name="sup" value="${invCheckMain.supId}" 
						labelName="account.accountCode" labelValue="${invCheckMain.supId}"
							 title="选择供应商编号" cssClass="form-control required" 
							 fieldLabels="供应商编码|供应商名称|税率"  
							 targetId="supName|supAddress|taxRatio"  targetField="accountName|accountAddr|taxRatio" 
							 fieldKeys="accountCode|accountName|taxRatio" searchLabels="供应商编码|供应商名称|税率" 
							 searchKeys="accountCode|accountName|taxRatio" ></sys:gridselect-pursup>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
					<div class="col-sm-3">
						<form:input path="supName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>税率：</label>
					<div class="col-sm-3">
						<input id="taxRatio" name="taxRatioNew" htmlEscape="false"    class="form-control required" value="<fmt:formatNumber value="${invCheckMain.taxRatio}" maxIntegerDigits="4" type="percent"/>" placeholder="由供应商信息带入"/>	
					</div>
				</div>
				<div class="form-group">
				    <label class="col-sm-2 control-label"><font color="red">*</font>接收仓库编码：</label>
					<div class="col-sm-3">
					<sys:gridselect-pursupnew url="${ctx}/purinvcheckmain/invCheckMain/waredata" id="ware" name="ware" value="${invCheckMain.wareId}"
					         labelName="wareID" labelValue="${invCheckMain.wareId}"
							 title="选择仓库" cssClass="form-control required" fieldLabels="仓库编号|仓库名称" 
							 fieldKeys="wareID|wareName"
							 searchLabels="仓库编号|仓库名称" searchKeys="wareID|wareName" 
							 targetId="wareName" targetField = "wareName"
							 ></sys:gridselect-pursupnew>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>接收仓库名称：</label>
					<div class="col-sm-3">
						<form:input path="wareName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">单据类型：</label>
					<div class="col-sm-3">
						<form:input path="billType" htmlEscape="false"    class="form-control " readonly= "true" />
					</div>
					<label class="col-sm-2 control-label">估价标记：</label>
					<div class="col-sm-3">
					<form:select path="thFlag" class="form-control ">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('estimateFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				    </form:select>
					</div>
				</div>
				<div class="form-group">
				    <label class="col-sm-2 control-label"><font color="red">*</font>采购员编码：</label>
					<div class="col-sm-3">
					<sys:gridselect-pursupforbuyer url="${ctx}/group/groupQuery/buyersdata" id="buyerId" name="buyerId.user.id" value="${invCheckMain.buyerId}" 
					   labelName=".user.no" labelValue="${invCheckMain.buyerId}"
							 title="选择采购员编号" cssClass="form-control required" 
							 fieldLabels="采购员编码|采购员名称" fieldKeys="user.no|buyername" 
							 searchLabels="采购员编码|采购员名称" searchKeys="user.no|buyername" 
							 targetId="buyerName|remarks" targetField = "buyername|id">
							 </sys:gridselect-pursupforbuyer>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>采购员名称：</label>
					<div class="col-sm-3">
						<form:input path="buyerName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>到货日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
							<div class='input-group form_datetime' id='arriveDate'>
			                    <input type='text'  name="arriveDate" class="form-control required"  value="<fmt:formatDate value="${invCheckMain.arriveDate}" pattern="yyyy-MM-dd"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
					<label class="col-sm-2 control-label">发货日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
							<div class='input-group form_datetime' id='awayDate'>
			                    <input type='text'  name="awayDate" class="form-control "  value="<fmt:formatDate value="${invCheckMain.awayDate}" pattern="yyyy-MM-dd"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
				    <label class="col-sm-2 control-label"><font color="red">*</font>制单员编码：</label>
					<div class="col-sm-3">
						<form:input path="makeEmpid" value="${fns:getUser().no}" htmlEscape="false"    class="form-control" readonly="true"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>制单员名称：</label>
					<div class="col-sm-3">
						<form:input path="makeEmpname" value="${fns:getUser().name}" htmlEscape="false"  class="form-control" readonly="true"/>
					</div>
				</div>
				
				<div class="form-group">
				    <label class="col-sm-2 control-label"><font color="red">*</font>入库类型编码：</label>
					<div class="col-sm-3">
					<sys:gridselect-pursupforparam url="${ctx}/purinvcheckmain/invCheckMain/databilltype" id="ioType" name="billType.ioType" value="${invCheckMain.ioType}" 
					labelName="ioType" labelValue="${invCheckMain.ioType}" param1 = "wareID" param1value = "wareNames"
							 title="选择入库标志" cssClass="form-control required" fieldLabels="入库类型编码|入库类型名称" fieldKeys="ioType|ioDesc" 
							 searchLabels="入库类型编码|入库类型名称" searchKeys="ioType|ioDesc" 
							 targetId="ioDesc" targetField="ioDesc" ></sys:gridselect-pursupforparam>
						<form:hidden path="ioType" htmlEscape="false"    class="form-control "/> 
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>入库类型名称：</label>
					<div class="col-sm-3">
						<form:input path="ioDesc" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				
				<div class="form-group">
				    <label class="col-sm-2 control-label">发票号：</label>
					<div class="col-sm-3">
						<form:input path="invoiceNum" htmlEscape="false"    class="form-control "/>
					</div>
					<label class="col-sm-2 control-label">承运商：</label>
					<div class="col-sm-3">
						<form:input path="carrierName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				
				<div class="form-group">
				    <label class="col-sm-2 control-label">承运发票号：</label>
					<div class="col-sm-3">
						<form:input path="invoiceNumCar" htmlEscape="false"    class="form-control "/>
					</div>
					<label class="col-sm-2 control-label">单据说明：</label>
					<div class="col-sm-3">
						<form:input path="billNotes" htmlEscape="false"    class="form-control "/>
					</div>

				</div>
		<!-- <div style="width:100%;  overflow-x:scroll;">	 -->
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">到货信息：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="if(addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl)!=0){invCheckDetailRowIdx = invCheckDetailRowIdx + 1;}else{jp.warning(invCheckDetailRowIdx);}" title="新增"><i class="fa fa-plus"></i> 新增</a>
			
			<table class="table table-striped table-bordered table-condensed" style="min-width:100%;">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>物料编号</th>
						<th><font color="red">*</font>物料名称</th>
						<th><font color="red">*</font>物料规格</th>
						<th><font color="red">*</font>计量单位</th>
                        <th><font color="red">*</font>实际单价</th>
						<th><font color="red">*</font>到货数量</th>
                        <th><font color="red">*</font>总价</th>
						<th>说明</th>
						<th width="10px">&nbsp;</th>
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
						<input id="invCheckDetailList{{idx}}_realPrice" name="invCheckDetailList[{{idx}}].realPrice" type="text" value="{{row.realPrice}}"    class="form-control required isFloatGtZero" onchange="priceCalculate()"/>
						<input id="invCheckDetailList{{idx}}_realSum" name="invCheckDetailList[{{idx}}].realSum" type="text" value="{{row.realSum}}"    class="form-control " readonly="true"/>
						<input id="invCheckDetailList{{idx}}_taxRatio" name="invCheckDetailList[{{idx}}].taxRatio" type="text" value="{{row.taxRatio}}"    class="form-control required isFloatGteZero"  onchange="priceCalculate()"/>
						<input id="invCheckDetailList{{idx}}_transSum" name="invCheckDetailList[{{idx}}].transSum" type="text" value="{{row.transSum}}"    class="form-control isFloatGtZero" onchange="priceCalculate()"/>
						<input id="invCheckDetailList{{idx}}_transRatio" name="invCheckDetailList[{{idx}}].transRatio" type="text" value="{{row.transRatio}}"    class="form-control isFloatGteZero" onchange="priceCalculate()"/>
						<input id="invCheckDetailList{{idx}}_transSumTaxed" name="invCheckDetailList[{{idx}}].transSumTaxed" type="text" value="{{row.transSumTaxed}}"    class="form-control " readonly="true" />
						<input id="invCheckDetailList{{idx}}_taxSum" name="invCheckDetailList[{{idx}}].taxSum" type="text" value="{{row.taxSum}}"    class="form-control " readonly="true"/>


					</td>

					<td>
						<sys:gridselect-purmulnew url="${ctx}/purinvcheckmain/invCheckMain/dataInvAccount" id="invCheckDetailList{{idx}}_itemInvAccount" name="invCheckDetailList[{{idx}}].itemInvAccountId" value="{{row.itemInvAccountId}}" labelName="invCheckDetailList{{idx}}.itemInvAccountCode" labelValue="{{row.itemInvAccountCode}}"
							 title="选择物料编号" cssClass="form-control required " targetId="itemName|itemSpecmodel|unitCode|itemInvAccountCode|itemInvAccountId" targetField="item.name|item.specModel|item.unitCode.unitName|item.code|item.id"  fieldLabels="物料编码|物料名称" isMultiSelected="true" fieldKeys="item.code|item.name" searchLabels="物料编码|物料名称" searchKeys="code|name" ></sys:gridselect-purmulnew>
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
					    <input id="invCheckDetailList{{idx}}_realPriceTaxed" name="invCheckDetailList[{{idx}}].realPriceTaxed" type="number" value="{{row.realPriceTaxed}}"  onchange="priceCalculate(this.id)"  class="form-control " />
					</td>

					<td>
						<input id="invCheckDetailList{{idx}}_checkQty" name="invCheckDetailList[{{idx}}].checkQty" type="number"" value="{{row.checkQty}}" onchange="priceCalculate(this.id)" class="form-control required"/>
					</td>

					<td>
					    <input id="invCheckDetailList{{idx}}_realSumTaxed" name="invCheckDetailList[{{idx}}].realSumTaxed" type="text" value="{{row.realSumTaxed}}"   class="form-control " readonly="true"/>
					</td>

					<td>
						<input id="invCheckDetailList{{idx}}_itemNotes" name="invCheckDetailList[{{idx}}].itemNotes" type="text" value="{{row.itemNotes}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10px">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#invCheckDetailList{{idx}}');" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var invCheckDetailRowIdx = 0, invCheckDetailTpl = $("#invCheckDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(invCheckMain.invCheckDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl, data[i]);
						invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
					}
				});

                function priceCalculate(id){
                    var strPrefix = "#" + id.split('_')[0];
                    var realPriceTaxed = $(strPrefix+"_realPriceTaxed").val();
                    var checkQty = $(strPrefix+"_checkQty").val();
                    var sum = (1*realPriceTaxed)*(1*checkQty).toFixed(2);
                    $(strPrefix+"_realSumTaxed").val(sum);
                }
				    var sumTotal = 0;
				    var transSumTotal = 0;
					function priceCalculateforbackup(){
			        sumTotal = 0;
			        transSumTotal = 0;
					var index = invCheckDetailRowIdx;
					for(var j = 0; j< index; j++){
					  var tagcheckQty = 0;
					  var tagrealPrice = 0;
					  var tagrealSum = 0;
					  var tagtaxRatio = 0;
					  var tagrealPriceTaxed = 0;
					  var tagrealSumTaxed = 0; 
					  var tagtransSum = 0;
					  var tagtransRatio = 0;
					  var tagtransSumTaxed = 0;
					  var tagtaxSum = 0;
					  tagtaxRatio = $("#invCheckDetailList"+j+"_taxRatio").val();//税率
					  tagcheckQty = $("#invCheckDetailList"+j+"_checkQty").val();//数量
					  tagrealPrice = $("#invCheckDetailList"+j+"_realPrice").val();//不含税单价
					  tagtransSum = $("#invCheckDetailList"+j+"_transSum").val();//不含税运费
					  tagtransRatio = $("#invCheckDetailList"+j+"_transRatio").val();//运费税率

					  /*金额计算*/
					  tagrealSum = (1*tagcheckQty) * (1*tagrealPrice);//不含税金额
					  tagrealPriceTaxed = (1+(1*tagtaxRatio))* (1*tagrealPrice);//含税单价					  
					  tagrealSumTaxed = (1*tagcheckQty)*(1*tagrealPriceTaxed);//含税金额
					  tagtransSumTaxed = (1+(1*tagtransRatio))*(1*tagtransSum);//含税运费
					  tagtaxSum = 1*tagtransSumTaxed - 1*tagtransSum + 1*tagrealSumTaxed - 1*tagrealSum;//税额
					  /*表单赋值*/
					  $("#invCheckDetailList"+j+"_realSum").val(tagrealSum);
					  $("#invCheckDetailList"+j+"_realPriceTaxed").val(tagrealPriceTaxed.toFixed(2));
					  $("#invCheckDetailList"+j+"_realSumTaxed").val(tagrealSumTaxed.toFixed(2));
					  $("#invCheckDetailList"+j+"_transSumTaxed").val(tagtransSumTaxed.toFixed(2));					  
					  $("#invCheckDetailList"+j+"_taxSum").val(tagtaxSum.toFixed(2));
					  sumTotal = sumTotal + 1*tagrealSumTaxed;//小记金额
					  transSumTotal = transSumTotal + 1*tagtransSumTaxed;//运费合计
					}
					var s = (transSumTotal + sumTotal).toFixed(2);
					$("#priceSumSubtotal").val(sumTotal.toFixed(2));
					$("#tranpriceSum").val(transSumTotal.toFixed(2));
					$("#priceSum").val(s);
					}
					
					
					function findQmsFlag(itemCode, preID){
					$.ajax({
						url:"${ctx}/purinvcheckmain/invCheckMain/qmsByItemCode?itemCode="+itemCode,
						type: "GET",
						cache:false,
						dataType: "json",
						success:function(data){
						if(data=="1"){
							$(preID+"_qmsFlag").val("质检");
						}else if(data=="0"){
							$(preID+"_qmsFlag").val("不质检");
						}else{
							$(preID+"_qmsFlag").val("未定义");
							}
						}
					});
					}
			</script>
			</div>
			<div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('purinvcheckmain:invCheckMain:edit') || isAdd}">
				<div class="col-lg-1"></div>
		        <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                     <input type ="submit" name="Submit" class="btn btn-primary btn-block btn-lg btn-parsley" value = "保存" data-loading-text="提交保存中..."/>
		                 </div>
		             </div>
		        </div>
			<div id = "submitDisplay">
				<div class="col-lg-1"></div>
				<div class="col-lg-2">
					<div class="form-group text-center">
						<div>
							<input type ="submit" name="Submit" class="btn btn-primary btn-block btn-lg btn-parsley" value = "提交" onclick="changeSubmit()" data-loading-text="提交采购退货单中..."/>
						</div>
					</div>
				</div>
			</div>
			<script>
				var id = "${invCheckMain.id}";
				if(id.length == 0){
				    document.getElementById("submitDisplay").style.display = "none";
				}
			</script>
		         <div class="col-lg-1"></div>
		        <div class="col-lg-2">
		        <div class="form-group text-center">
		                 <div>
		                      <a href= "${ctx}/purinvcheckmain/invCheckMain/listBackNew" class="btn btn-primary btn-block btn-lg btn-parsley">返回</a>
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
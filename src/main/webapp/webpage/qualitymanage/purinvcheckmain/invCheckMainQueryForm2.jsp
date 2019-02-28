<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购到货生成</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
	var buyerIdforbeginSetPlan = null;
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
    
	    function inputResult(){
		    var wareid = $("#wareNames").val();
			$("#wareId").val(wareid);
			
			var supid = $("#supNames").val();
			$("#supId").val(supid);	
			
			var buyerid = $("#buyerIdNames").val();
			$("#buyerId").val(buyerid);
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
					addRow('#applyDetailList', applyDetailRowIdx, applyDetailTpl);	
				    addRowModify('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl, items[i],obj,targetField,targetId,nam,labNam);		
				    invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
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
				$(list+idx+"_"+tId).val(row[ind]);
			}						
		}
		
		
		
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
	/*		$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
	*/
			$(list+idx).find(".form_datetime").each(function(){
				 $(this).datetimepicker({
					 format: "YYYY-MM-DD"
			    });
			});
		}
		function delRow(obj, prefix){
		     /*减去小记金额*/
		     var priceSumSubtotal=$("#priceSumSubtotal").val();
		     var realSumTaxed= $(prefix+"_realSumTaxed").val();
		     $("#priceSumSubtotal").val(priceSumSubtotal-1*realSumTaxed);
		     /*减去运费合计*/
		     var tranpriceSum=$("#tranpriceSum").val();
		     var transSumTaxed= $(prefix+"_transSumTaxed").val();
		     $("#tranpriceSum").val(tranpriceSum-1*transSumTaxed);
		     /*减去金额合计*/
		     var priceSum=$("#priceSum").val();
		     $("#priceSum").val(priceSum - (1*realSumTaxed + 1*transSumTaxed));
		     
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
		
		//下达给采购员的计划
		var purPlanData={};
		
		function selectPlan(buyerId){
			//alert(2);
			buyerIdforbeginSetPlan = buyerId;
			$.ajax({
				url:"${ctx}/purinvcheckmain/invCheckMain/selectPlan?buyerId="+buyerId,
				type: "GET",
				cache:false,
				dataType: "json",
				success:function(data){
					// alert(3);
					 //console.log(data);
					 purPlanData=data;
					 purPlanDetailRowIdx = 0;
					 purPlanDetailTpl = $("#purPlanDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");					
						var planData=data.rows ;
						for (var i=0; i<planData.length; i++){
							addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, planData[i]);
							purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
						}			
				}
			});
		}
		
		//下达给采购员的合同
		var contractDetailData = {};
		
		function selectContract(buyerId){
		$.ajax({
			url:"${ctx}/purinvcheckmain/invCheckMain/selectContract?buyerId="+buyerId,
			type: "GET",
			cache:false,
			dataType: "json",
			success:function(data){
				//console.log(data);
				contractDetailData = data;
				contractDetailRowIdx = 0;
				//console.log(1);
				contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				//console.log(111);
				    var contractData=data.rows;
				    console.log(contractData.length);
					for (var i=0; i<contractData.length; i++){
					console.log('loop');
						addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl, contractData[i]);
						contractDetailRowIdx = contractDetailRowIdx + 1;
					}			
					
			}
		})
		}
		
		function beginSetPlan(){
			$.ajax({
				url:"${ctx}/purinvcheckmain/invCheckMain/selectPlan?buyerId="+buyerIdforbeginSetPlan,
				type: "GET",
				cache:false,
				dataType: "json",
				success:function(data){
					// alert(3);
					//console.log(data);
				     //清除原有的
				   var namebox = $("input[name^='boxName']");  //获取name值为boxs的所有input
                    for(i = 0; i < namebox.length; i++) {                      
                        $(namebox[i]).parent().parent().remove(); 
                        purPlanDetailRowIdx--;
                    }
				     //重新加载数据
                     purPlanData=data;
					 purPlanDetailRowIdx = 0;
					 purPlanDetailTpl = $("#purPlanDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");					
				   var planData=data.rows ;
				   for (var i=0; i<planData.length; i++){
							addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, planData[i]);
							purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
				   }			
				}
			});		
		}
		
		function beginSetContract(){
			$.ajax({
				url:"${ctx}/purinvcheckmain/invCheckMain/selectContract?buyerId="+buyerIdforbeginSetPlan,
				type: "GET",
				cache:false,
				dataType: "json",
				success:function(data){
					// alert(3);
					//console.log(data);
				     //清除原有的
				   var namebox = $("input[name^='boxName']");  //获取name值为boxs的所有input
                    for(i = 0; i < namebox.length; i++) {                      
                        $(namebox[i]).parent().parent().remove(); 
                        contractDetailRowIdx--;
                    }
				     //重新加载数据
                     contractDetailData=data;
					 contractDetailRowIdx = 0;
					 contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");					
				   var contractDetailData=data.rows ;
				   for (var i=0; i<contractDetailData.length; i++){
							addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl, contractDetailData[i]);
							contractDetailRowIdx = contractDetailRowIdx + 1;
				   }			
				}
			});		
		}
		
		
		//点击选择框则全选
		function allcheckBox(){
			//建议用name属性值操作，因为id属性在每一个页面中默认是唯一的，而name属性则可以取到属性值相同的所有
			var nn = $("#allboxName").is(":checked"); //判断th中的checkbox是否被选中，如果被选中则nn为true，反之为false
                if(nn == true) {
                    var namebox = $("input[name^='boxName']");  //获取name值为boxs的所有input
                    for(i = 0; i < namebox.length; i++) {
                        namebox[i].checked=true;    //js操作选中checkbox
                    }
                }
                if(nn == false) {
                    var namebox = $("input[name^='boxName']");
                    for(i = 0; i < namebox.length; i++) {
                        namebox[i].checked=false;
                    }
                }
		}
		
		function addToCheck(){
			var namebox = $("input[name^='boxName']");
				for(i = 0; i < namebox.length; i++) {   
				  // alert(namebox[i].checked);
				  console.log(namebox);
				   //已被选择
				   if(namebox[i].checked){
					   //获取计划数据
					   var boxId=namebox[i].id;
					       itId=boxId.split("_")[0];
					      
	                   var itemCode=$("#"+itId+"_itemCode").val();
	                   var itemId=$("#"+itId+"_itemId").val();
	                   var itemName=$("#"+itId+"_itemName").val();
	                   var itemModel=$("#"+itId+"_itemSpecmodel").val();	                  
	                   var unitName=$("#"+itId+"_unitName").val();
	                   var itemTexture=$("#"+itId+"_itemTexture").val();
	                   var planQty=$("#"+itId+"_planQty").val();
	                   var itemPrice=$("#"+itId+"_planPrice").val();
	                   var itemSum=$("#"+itId+"_planSum").val();
	                   console.log($("#"+itId+"_itemName").val());
	                     //填充到到货数据
	                     //添加一行空白的
					   addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl);
					   //计划的数据填充到到货
	                   var preID="#invCheckDetailList"+invCheckDetailRowIdx;
	                   $(preID+"_itemNames").val(itemCode);//物料编号
	                   $(preID+"_itemName").val(itemName);//物料名称
	                   $(preID+"_itemSpecmodel").val(itemModel);//物料规格
	                   $(preID+"_unitCode").val(unitName);//
	                   $(preID+"_itemTexture").val(itemTexture);
	                   $(preID+"_itemId").val(itemId);
	                   $(preID+"_checkQty").val(planQty);
	                   $(preID+"_realPrice").val(itemPrice);
	                   $(preID+"_realSum").val(itemSum);
	                   invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
	                   
                 }
			   }  
		}
		
		function contractAddToCheck(){
			var namebox = $("input[name^='boxName']");
				for(i = 0; i < namebox.length; i++) {   
				  // alert(namebox[i].checked);
				  console.log(namebox);
				   //已被选择
				   if(namebox[i].checked){
					   //获取合同数据
					   var boxId=namebox[i].id;
					       itId=boxId.split("_")[0];
					      
	                   var itemCode=$("#"+itId+"_itemCode").val();
	                   var itemId=$("#"+itId+"_itemId").val();
	                   var itemName=$("#"+itId+"_itemName").val();
	                   var itemModel=$("#"+itId+"_itemModel").val();	                  
	                   var measUnit=$("#"+itId+"_measUnit").val();
	                   var itemPrice=$("#"+itId+"_itemPrice").val();
	                   var itemQty=$("#"+itId+"_itemQty").val();
	                   var itemSum=$("#"+itId+"_itemSum").val();
	                   console.log($("#"+itId+"_itemName").val());
	                     //填充到到货数据
	                     //添加一行空白的
					   addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl);
					   //计划的数据填充到到货
	                   var preID="#invCheckDetailList"+invCheckDetailRowIdx;
	                   $(preID+"_itemNames").val(itemCode);//物料编号
	                   $(preID+"_itemName").val(itemName);//物料名称
	                   $(preID+"_itemSpecmodel").val(itemModel);//物料规格
	                   $(preID+"_unitCode").val(measUnit);
	                   //$(preID+"_itemTexture").val(itemTexture);
	                   $(preID+"_itemId").val(itemId);
	                   $(preID+"_checkQty").val(itemQty);
	                   $(preID+"_realPrice").val(itemPrice);
	                   $(preID+"_realSum").val(itemSum);
	                   invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
                 }
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
				<a class="panelButton" href="${ctx}/purinvcheckmain/invCheckMain/listQuery"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="invCheckMain" action="${ctx}/purinvcheckmain/invCheckMain/saveCreat" method="post" onsubmit="return inputResult()" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="col-sm-2" style="display:none">
		    <form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/> 
			<form:hidden path="wareId" htmlEscape="false"  class="form-control"/> 
			<form:hidden path="supCode" htmlEscape="false"    class="form-control "/>
			<form:hidden path="buyerId" htmlEscape="false"  class="form-control"/>
			<form:hidden path="contractId" htmlEscape="false"  class="form-control"/> 
			<form:hidden path="planId" htmlEscape="false"  class="form-control"/>  
		</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
					<div class="col-sm-3">
						<form:input path="billnum" htmlEscape="false"    class="form-control " readonly= "true"/>
					</div>
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-3">
						<form:input path="billStateFlag" htmlEscape="false"    class="form-control " readonly= "true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单日期：</label>
					<div class="col-sm-3">
						<input type='text'  name="makeDate" class="form-control "  value="<fmt:formatDate value="${invCheckMain.makeDate}" pattern="yyyy-MM-dd"/>"
	                    readonly = "true"/>
					</div>
					<label class="col-sm-2 control-label">业务日期：</label>
					<div class="col-sm-3">
						<input type='text'  name="bdate" class="form-control "  value="<fmt:formatDate value="${invCheckMain.bdate}" pattern="yyyy-MM-dd"/>"
	                    readonly = "true"/>
					</div>
				</div>
				<div class="form-group">
				    <label class="col-sm-2 control-label"><font color="red">*</font>供应商编码：</label>
					<div class="col-sm-3">
						 <form:input path="supId" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
					<div class="col-sm-3">
						<form:input path="supName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
				    <label class="col-sm-2 control-label"><font color="red">*</font>接收仓库编码：</label>
					<div class="col-sm-3">
						 <form:input path="wareId" htmlEscape="false"    class="form-control " readonly="true"/>
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
					<form:input path="thFlag" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
				    <label class="col-sm-2 control-label"><font color="red">*</font>采购员编码：</label>
					<div class="col-sm-3">
						<form:input path="buyerId" htmlEscape="false"    class="form-control " readonly= "true"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>采购员名称：</label>
					<div class="col-sm-3">
						<form:input path="buyerName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">到货日期：</label>
					<div class="col-sm-3">
						<input type='text'  name="arriveDate" class="form-control "  value="<fmt:formatDate value="${invCheckMain.arriveDate}" pattern="yyyy-MM-dd"/>"
	                    readonly = "true"/>
					</div>
					<label class="col-sm-2 control-label">发货日期：</label>
					<div class="col-sm-3">
						<input type='text'  name="awayDate" class="form-control "  value="<fmt:formatDate value="${invCheckMain.awayDate}" pattern="yyyy-MM-dd"/>"
	                    readonly = "true"/>
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
						<form:input path="ioType" htmlEscape="false"    class="form-control " readonly= "true"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>入库类型名称：</label>
					<div class="col-sm-3">
						<form:input path="ioDesc" htmlEscape="false"    class="form-control " readonly= "true"/>
					</div>
				</div>
				
				<div class="form-group">
				    <label class="col-sm-2 control-label">发票号：</label>
					<div class="col-sm-3">
						<form:input path="invoiceNum" htmlEscape="false"    class="form-control " readonly= "true"/>
					</div>
					<label class="col-sm-2 control-label">承运商：</label>
					<div class="col-sm-3">
						<form:input path="carrierName" htmlEscape="false"    class="form-control " readonly= "true"/>
					</div>
				</div>
				
				<div class="form-group">
				    <label class="col-sm-2 control-label">承运发票号：</label>
					<div class="col-sm-3">
						<form:input path="invoiceNumCar" htmlEscape="false"    class="form-control " readonly= "true"/>
					</div>
					<label class="col-sm-2 control-label">单据说明：</label>
					<div class="col-sm-3">
						<form:input path="billNotes" htmlEscape="false"    class="form-control " readonly= "true"/>
					</div>
				</div>
		 
		<div class="tabs-container">
                <ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">到货信息</a>
	                </li>
	                <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">需求信息</a>
	                </li>
					<li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">合同信息</a>
	                </li>
					<li class=""><a data-toggle="tab" href="#tab-4" aria-expanded="false">计划信息</a>
	                </li>
                </ul>
        </div>
           	
            <div class="tab-content">
			<div id="tab-1" class="tab-pane fade in  active">	
			<div class="table-responsive" style="max-height:500px">
			<table class="table table-striped table-bordered table-condensed" style="min-width: 2260px">
				<thead>
					<tr>
						<th class="hide"></th>
						<th width="200">序号</th>
						<th width="200"><font color="red">*</font>物料编号</th>
						<th width="200"><font color="red">*</font>物料名称</th>
						<th width="200"><font color="red">*</font>物料规格</th>
						<th width="200"><font color="red">*</font>计量单位</th>
						<th width="200"><font color="red">*</font>到货数量</th>
						<th width="200"><font color="red">*</font>质检与否</th>
						<th width="200">说明</th>
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
						<input id="invCheckDetailList{{idx}}_serialnum" name="invCheckDetailList[{{idx}}].serialnum" type="text" value="{{row.serialnum}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
						<input id="invCheckDetailList{{idx}}_itemCode" name="invCheckDetailList[{{idx}}].item.code" type="text" value="{{row.item.code}}"    class="form-control " readonly="true"/>
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
						<input id="invCheckDetailList{{idx}}_checkQty" name="invCheckDetailList[{{idx}}].checkQty" type="text" value="{{row.checkQty}}"    class="form-control " onchange="priceCalculate()" readonly= "true"/>
					</td>

					<td>
						<input id="invCheckDetailList{{idx}}_qmsFlag" name="invCheckDetailList[{{idx}}].qmsFlag" type="text" value="{{row.qmsFlag}}"    class="form-control " readonly="true"/>
					</td>

					<td>
						<input id="invCheckDetailList{{idx}}_itemNotes" name="invCheckDetailList[{{idx}}].itemNotes" type="text" value="{{row.itemNotes}}"    class="form-control " readonly= "true"/>
					</td>
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var invCheckDetailRowIdx = 0, invCheckDetailTpl = $("#invCheckDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
				//$("#billStateFlag").val("正在录入");
				//$("#billType").val("生成");
				
				
					var data = ${fns:toJson(invCheckMain.invCheckDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl, data[i]);
						invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
					}
				});
				
				//点击选择框则全选
				function allcheck(){
					//建议用name属性值操作，因为id属性在每一个页面中默认是唯一的，而name属性则可以取到属性值相同的所有
					var nn = $("#allboxs").is(":checked"); //判断th中的checkbox是否被选中，如果被选中则nn为true，反之为false
	                 if(nn == true) {
	                     var namebox = $("input[name^='boxs']");  //获取name值为boxs的所有input
	                     for(i = 0; i < namebox.length; i++) {
	                         namebox[i].checked=true;    //js操作选中checkbox
	                     }
	                 }
	                 if(nn == false) {
	                     var namebox = $("input[name^='boxs']");
	                     for(i = 0; i < namebox.length; i++) {
	                         namebox[i].checked=false;
	                     }
	                 }
				}
				
				function deleteRow(){
                    var namebox = $("input[name^='boxs']");  //获取name值为boxs的所有input
                    for(i = 0; i < namebox.length; i++) {
                        if(namebox[i].checked){    //js操作选中checkbox                    
                            var boxId=namebox[i].id;
                                boxId=boxId.split("_")[0];
                                $("#"+boxId+"_delFlag").val(1);
                                $(namebox[i]).parent().parent().hide();
                        }
                    }
				}
					
				    var sumTotal = 0;
				    var transSumTotal = 0;
					function priceCalculate(){
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
					var s = transSumTotal + sumTotal;
					$("#priceSumSubtotal").val(sumTotal.toFixed(2));
					$("#tranpriceSum").val(transSumTotal.toFixed(2));
					$("#priceSum").val(s.toFixed(2));
					
					}
				//根据主表的字段对计划或者合同的子表进行显示
				var plan = $("#planId").val();
				var contra = $("#contractId").val();
				console.log($("#contractId").val());
				if((plan != "")&&(plan != null)){
					selectPlanOnlyShowDetail(plan);
				}else if(($("#contractId").val()!= "")&&($("#contractId").val()!= null)){
					selectContractOnlyShowDetail();
				}
				
		var contractIdforCn = null;		
		function selectContractOnlyShowDetail(){
		contractIdforCn = $("#contractId").val();
		$.ajax({
			url:"${ctx}/purinvcheckmain/invCheckMain/selectContract?contractId="+contractIdforCn,
			type: "GET",
			cache:false,
			dataType: "json",
			success:function(data){
				contractDetailData = data;
				contractDetailRowIdx = 0;
				contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				var invCheckdata = ${fns:toJson(invCheckMain.invCheckDetailList)};
				var contractData=data.rows;
				for (var i=0; i<contractData.length; i++){
					for (var j=0; j<invCheckdata.length; j++){
						if(contractData[i].serialNum == invCheckdata[j].frontSerialNum){
							addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl, contractData[i]);
							contractDetailRowIdx = contractDetailRowIdx + 1;
						}
					}
				}		
			}
		})
		}
		
		function selectPlanOnlyShowDetail(planId){
			var buyerId = $("#buyerId").val();
			$.ajax({
				url:"${ctx}/purinvcheckmain/invCheckMain/showplandatabyAjax?planId="+planId+"&buyerId="+buyerId,
				type: "GET",
				cache:false,
				dataType: "json",
				success:function(data){
					 purPlanData=data;
					 purPlanDetailRowIdx = 0;
					 purPlanDetailTpl = $("#purPlanDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");					
						var planData=data.rows;
						var invCheckdata = ${fns:toJson(invCheckMain.invCheckDetailList)};
						for (var i=0; i<planData.length; i++){
							for (var j=0; j<invCheckdata.length; j++){
								if(planData[i].serialNum == invCheckdata[j].frontSerialNum){
									addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, planData[i]);
									purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
								}
							}
						}			
				}
			});
		}
			</script>
			</div>
			
			<div id="tab-2" class="tab-pane fade">
			
			 <table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>序号</th>
						<th>物料编号</th>
						<th>物料名称</th>
						<th>物料规格</th>
						<th>日期</th>
						<th>需求单号</th>
						<th>需求状态</th>
						<th>单位</th>
						<th>数量</th>
						<th>单价</th>
						<th>金额</th>
						<th>库存量</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="applyDetailList">
				</tbody>
			</table>
			
			<script type="text/template" id="applyDetailTpl">//<!--
				<tr id="applyDetailList{{idx}}">
					<td class="hide">
						<input id="applyDetailList{{idx}}_id" name="applyDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="applyDetailList{{idx}}_delFlag" name="applyDetailList[{{idx}}].delFlag" type="hidden" value="0"/>

					</td>

                  <td>
						<input id="applyDetailList{{idx}}_serialNum" name="applyDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control " readonly="true"/>
					</td>					
					
					<td>
						<input id="applyDetailList{{idx}}_itemCode" name="applyDetailList[{{idx}}].item.code" type="text" value="{{row.item.code}}"    class="form-control required" readonly="true"/>

                    </td>					
					
					<td>
						<input id="applyDetailList{{idx}}_itemName" name="applyDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control required" readonly="true"/>
					</td>

	                <td>
						<input id="applyDetailList{{idx}}_itemSpecmodel" name="applyDetailList[{{idx}}].itemSpecmodel" type="text" value="{{row.itemSpecmodel}}"    class="form-control required" readonly="true"/>
					</td>
					
					<td>
						<input id="applyDetailList{{idx}}_requestDate" name="applyDetailList[{{idx}}].requestDate" type="text" value="{{row.requestDate}}"    class="form-control " readonly="true"/>
					</td>

					<td>
						<input id="applyDetailList{{idx}}_billnum" name="applyDetailList[{{idx}}].billnum" type="text" value="{{row.applyMain.billNum}}"    class="form-control " readonly="true"/>
					</td>

					<td>
						<input id="applyDetailList{{idx}}_applyState" name="applyDetailList[{{idx}}].applyState" type="text" value="{{row.applyMain.billStateFlag}}"    class="form-control " readonly="true"/>
					</td>

					<td>
						<input id="applyDetailList{{idx}}_unitName" name="applyDetailList[{{idx}}].unitName" type="text" value="{{row.unitName}}"    class="form-control " readonly="true"/>
					</td>


					<td>
						<input id="applyDetailList{{idx}}_applyQty" name="applyDetailList[{{idx}}].applyQty" type="text" value="{{row.applyQty}}"  onchange="setSumValue(this)"   class="form-control number required" readonly="true"/>
					</td>

					<td>
						<input id="applyDetailList{{idx}}_applyPrice" name="applyDetailList[{{idx}}].applyPrice" type="text" value="{{row.applyPrice}}"  onchange="setItemPrice(this)"  class="form-control number required" readonly="true"/>
					</td>
					
					<td>
						<input id="applyDetailList{{idx}}_applySum" name="applyDetailList[{{idx}}].applySum"  type="text" value="{{row.applySum}}"  readOnly="true"  class="form-control number required" readonly="true"/>
					</td>

					<td>
						<input id="applyDetailList{{idx}}_nowSum" name="applyDetailList[{{idx}}].nowSum"  type="text" value="{{row.nowSum}}"  readOnly="true"  class="form-control number required" readonly="true"/>
					</td>			
				</tr>//-->
			</script>
			</div>
			
			<script type="text/javascript">			  
				var applyDetailRowIdx = 0, applyDetailTpl = $("#applyDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
				var billnum = $("#planId").val();
				console.log(billnum);
				if(billnum){
					$.ajax({
						url:"${ctx}/purinvcheckmain/invCheckMain/appDatabyRollPlan?billn="+billnum,
						type: "GET",
						cache:false,
						dataType: "json",
						success:function(data){
							 applyData=data;
							 applyDetailRowIdx = 0;
							 applyDetailTpl = $("#applyDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");					
								var applyData=data.rows;
								for (var i=0; i<applyData.length; i++){
									addRow('#applyDetailList', applyDetailRowIdx, applyDetailTpl, applyData[i]);
									applyDetailRowIdx = applyDetailRowIdx + 1;
								}
						}			
						
					});
				}
			    });
			</script>
			
			
			
			<div id="tab-3" class="tab-pane fade">
			
			 <table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>序号</th>
						<th>合同单号</th>
						<th>物料编号</th>
						<th>物料名称</th>
						<th>物料规格</th>
						<th>计量单位</th>
						<th>合同数量</th>
						<th>采购单价</th>
						<th>采购总额</th>	
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="contractDetailList">
				</tbody>
			</table>
			
			<script type="text/template" id="contractDetailTpl">//<!--
				<tr id="purPlanDetailList{{idx}}">
					<td class="hide">
						<input id="contractDetailList{{idx}}_id" name="contractDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="contractDetailList{{idx}}_delFlag" name="contractDetailList[{{idx}}].delFlag" type="hidden" value="0"/>

					</td>

                  <td>
						<input id="contractDetailList{{idx}}_serialNum" name="contractDetailList[{{idx}}].serialNum" readOnly="true" type="text" value="{{row.serialNum}}"    class="form-control " readonly="true"/>
					</td>
					<td>
						<input id="contractDetailList{{idx}}_billNum" name="contractDetailList[{{idx}}].contractMain.billNum" type="text" readOnly="true" value="{{row.contractMain.billNum}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
						<input id="contractDetailList{{idx}}_itemCode" name="contractDetailList[{{idx}}].item.code" type="text" value="{{row.item.code}}"    class="form-control required" readonly="true"/>

                    </td>					
					
					<td>
						<input id="contractDetailList{{idx}}_itemName" name="contractDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control required" readonly="true"/>
					</td>

	                <td>
						<input id="contractDetailList{{idx}}_itemModel" name="contractDetailList[{{idx}}].itemModel" type="text" value="{{row.itemModel}}"    class="form-control required" readonly="true"/>
					</td>
					
					<td>
						<input id="contractDetailList{{idx}}_measUnit" name="contractDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"    class="form-control " readonly="true"/>
					</td>

					<td>
						<input id="contractDetailList{{idx}}_itemQty" name="contractDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"  onchange="setSumValue(this)"   class="form-control number required" readonly="true"/>
					</td>

					<td>
						<input id="contractDetailList{{idx}}_itemPrice" name="contractDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"  onchange="setItemPrice(this)"  class="form-control number required" readonly="true"/>
					</td>
					
					<td>
						<input id="contractDetailList{{idx}}_itemSum" name="contractDetailList[{{idx}}].itemSum"  type="text" value="{{row.itemSum}}"  readOnly="true"  class="form-control number required" readonly="true"/>
					</td>			
				</tr>//-->
			</script>
			</div>
			
			<script type="text/javascript">			  
				var contractDetailRowIdx = 0, contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
				});
			</script>
			
			
			<div id="tab-4" class="tab-pane fade">
			
			 <table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>序号</th>
						<th>计划单号</th>
						<th>物料编号</th>
						<th>物料名称</th>
						<th>物料规格</th>
						<th>计量单位</th>
						<th>计划数量</th>
						<th>采购单价</th>
						<th>采购总额</th>	
						<th>需求日期</th>					
						<th>需求数量</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="purPlanDetailList">
				</tbody>
			</table>
			
			<script type="text/template" id="purPlanDetailTpl">//<!--
				<tr id="purPlanDetailList{{idx}}">
					<td class="hide">
						<input id="purPlanDetailList{{idx}}_id" name="purPlanDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purPlanDetailList{{idx}}_delFlag" name="purPlanDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
						<input id="purPlanDetailList{{idx}}_itemId" name="purPlanDetailList[{{idx}}].itemCode.id" type="hidden" value="{{row.itemCode.id}}"/>
					</td>

                   <td>
						<input id="purPlanDetailList{{idx}}_serialNum" name="purPlanDetailList[{{idx}}].serialNum" readOnly="true" type="text" value="{{row.serialNum}}"    class="form-control " readonly="true"/>
					</td>
					<td>
						<input id="purPlanDetailList{{idx}}_billNum" name="purPlanDetailList[{{idx}}].billNum.billNum" type="text" readOnly="true" value="{{row.billNum.billNum}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
                        <input id="purPlanDetailList{{idx}}_itemCode" name="purPlanDetailList[{{idx}}].itemCode.code" type="text" readOnly="true" value="{{row.itemCode.code}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_itemName" name="purPlanDetailList[{{idx}}].itemName" type="text" readOnly="true" value="{{row.itemName}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_itemSpecmodel" name="purPlanDetailList[{{idx}}].itemSpecmodel"  readOnly="true" type="text" value="{{row.itemSpecmodel}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_unitName" name="purPlanDetailList[{{idx}}].unitName" readOnly="true" type="text" value="{{row.unitName}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_planQty" name="purPlanDetailList[{{idx}}].planQty" readOnly="true" type="text" value="{{row.planQty}}"    class="form-control " readonly="true"/>
					</td>
										
					<td>
						<input id="purPlanDetailList{{idx}}_planPrice" name="purPlanDetailList[{{idx}}].planPrice" readOnly="true" type="text" value="{{row.planPrice}}"    class="form-control " readonly="true"/>
					</td>
	
					<td>
						<input id="purPlanDetailList{{idx}}_planSum" name="purPlanDetailList[{{idx}}].planSum" readOnly="true" type="text" value="{{row.planSum}}"    class="form-control " readonly="true"/>
					</td>															
					<td>
		                 <input type='text' id="purPlanDetailList{{idx}}_requestDate"  name="purPlanDetailList[{{idx}}].requestDate" readOnly="true" class="form-control "  value="{{row.requestDate}}" readonly="true"/>		                   					            
					</td>																				
					<td>
						<input id="purPlanDetailList{{idx}}_purQty" name="purPlanDetailList[{{idx}}].purQty" readOnly="true" type="text" value="{{row.purQty}}"    class="form-control " readonly="true"/>
					</td>									
				</tr>//-->
			</script>
			
			<script type="text/javascript">			  
				var purPlanDetailRowIdx = 0, purPlanDetailTpl = $("#purPlanDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
				//  var buyerId=$("#groupBuyer.id").val();
				    
				    /*
				var data = ${fns:toJson(contractMain.purPlanDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, data[i]);
						purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
					}
					*/
				});
			</script>
		</div>
		</div>
		</div>
	        <div class="col-lg-3"></div>
	        <div class="col-lg-4">
	        <div class="form-group text-center">
	                 <div>
	                      <a href= "${ctx}/purinvcheckmain/invCheckMain/listQuery" class="btn btn-primary btn-block btn-lg btn-parsley">返回</a>
	                 </div>
	             </div>
	        </div>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
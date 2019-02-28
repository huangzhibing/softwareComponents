<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购到货管理</title>
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
    
	    function inputResult(){
		    var wareid = $("#wareNames").val();
			$("#wareId").val(wareid);
			
			var supid = $("#supNames").val();
			$("#supId").val(supid);	
			
			var buyerid = $("#buyerIdNames").val();
			$("#buyerId").val(buyerid);
	    }
    
		$(document).ready(function() {
		//中文单据状态
		if($("#billStateFlag").val()=='A'){
			$("#billStateFlag").val("正在录入");
		}
		
		//中文单据类型
		if($("#billType").val()=='M'){
			$("#billType").val("录入")
		}else if($("#billType").val()=='C'){
			$("#billType").val("生成")
		}else if($("#billType").val()=='V'){
			$("#billType").val("作废单据")
		}
		
		
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
		    
                   
		});
		
		//在具体的使用中需替换'#applyDetailList', applyDetailRowIdx, applyDetailTpl这三个值
		function setOtherValue(items,obj,targetField,targetId,nam,labNam){
			 for (var i=1; i<items.length; i++){
					//addRowModify('#applyDetailList', applyDetailRowIdx, applyDetailTpl, items[i]);	
				    addRowModify('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl, items[i],obj,targetField,targetId,nam,labNam);		
				    invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
				}
		}
        		
		function addRowModify(list, idx, tpl, row, obj,targetField,targetId,nam,labNam){
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
/* 			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			}); */
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
		
		function changeState(){
		var qms = $("#qmsFlag").val();
			if(qms == "不质检")
			{
				$("#qmsFlag").val("3");
			}else if(qms == "未质检"){
				$("#qmsFlag").val("0");
				console.log($("#qmsFlag").val());
			} 
		}
		
		
		
		//点击选择框则全选
		function allcheckBox(){
			//建议用name属性值操作，因为id属性在每一个页面中默认是唯一的，而name属性则可以取到属性值相同的所有
			var nn = $("#allboxName").is(":checked"); //判断th中的checkbox是否被选中，如果被选中则nn为true，反之为false
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
		
		
		function print(){
            var obj = getItemQRCode();//物料二维码字符串，多个用逗号隔开：例如:1234556698725544,879357349525298
            if(obj.str==""||obj.str==null){
				jp.alert("请选择需要打印的物料二维码！");
				return;
			}
			var codeNames = obj.stn.split(",");
            $.ajax({
                type: "POST",
                url: '${ctx}/tools/TwoDimensionCodeController/createBatchTwoDimensionCode',
                data: {encoderContent:obj.str,tm:new Date().getTime()},
                dataType:'json',
                cache: false,
                success: function(data){
                     if(data.success){
                        $("#printContent").html("");
                        var names = data.body.names.split(",");
                        var item = "<table>";
                        for(var i = 0;i<names.length;i++){
                            var file = data.body.fartherFilePath+names[i]+".png";
                            var itemname = codeNames[i];
                            item += '<tr><td><label style="font-size: 1px;">'+itemname+'</label><br/><img cache="false"  width="65px" height="65px"  class="block" style="vertical-align: middle" src="'+file+'"/></td></tr>'
                        }
                        document.getElementById("printContent").style.visibility="visible";
                        $("#printContent").html(item+"</table>");
                        $("#printContent").jqprint();
                        $("#printContent").html("");
                        document.getElementById("printContent").style.visibility="hidden";
                     }else{
                         jp.alert('后台读取出错！');
                         return;
                     }
                }
            })
        };
        function getItemQRCode(){
		    var row = {};
        	var str = "";
        	var stn = "";
        	var namebox = $("input[name^='boxs']");  //获取name值为boxs的所有input
        	for(i = 0; i < invCheckDetailCodeRowIdx; i++){                
                if(namebox[i].checked==true)//js操作选择被选中的checkbox
                {
                	if(str.length==0){
                		str = $("#invCheckDetailCodeList"+i+"_itemBarcode").val();
                		stn = $("#invCheckDetailCodeList"+i+"_itemName").val().substring(0,5);
                	}else{
                		str = str + "," + $("#invCheckDetailCodeList"+i+"_itemBarcode").val();
                		stn = stn + "," + $("#invCheckDetailCodeList"+i+"_itemName").val().substring(0,5);
                	}
                }
        	}
        	row.str = str;
        	row.stn = stn;
        	return row;
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
				<a class="panelButton" href="${ctx}/purinvcheckmain/invCheckMain/listWarehousing"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="invCheckMain" action="${ctx}/purinvcheckmain/invCheckMain/saveQRcode" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="col-sm-3" style="display:none">
		    <form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/> 
			<form:hidden path="supCode" htmlEscape="false"    class="form-control "/>
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
					<input type="text" name="makeDate" htmlEscape="false" class="form-control required" readonly ="true"
						value="<fmt:formatDate value="${invCheckMain.makeDate}" pattern="yyyy-MM-dd"/>"/>
					</div>
					<label class="col-sm-2 control-label">业务日期：</label>
					<div class="col-sm-3">
					<input type="text" name="bdate" htmlEscape="false" class="form-control required" readonly ="true"
						value="<fmt:formatDate value="${invCheckMain.bdate}" pattern="yyyy-MM-dd"/>"/>
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
					   <form:input path="thFlag" htmlEscape="false"    class="form-control " readonly= "true" />
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
					<label class="col-sm-2 control-label"><font color="red">*</font>到货日期：</label>
					<div class="col-sm-3">
						<input type='text'  name="arriveDate" class="form-control required"  value="<fmt:formatDate value="${invCheckMain.arriveDate}" pattern="yyyy-MM-dd"/>"
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
						<form:input path="makeEmpid"  htmlEscape="false"    class="form-control" readonly="true"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>制单员名称：</label>
					<div class="col-sm-3">
						<form:input path="makeEmpname"  htmlEscape="false"  class="form-control" readonly="true"/>
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
                <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="true">物料二维码：</a>
                </li>
            </ul>
            <div class="tab-content">
            <div id="tab-1" class="tab-pane fade in  active">
			<div class="table-responsive" style="max-height:500px">
			<table class="table table-striped table-bordered table-condensed" style="min-width: 2260px">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>序号</th>
						<th><font color="red">*</font>物料编号</th>
						<th><font color="red">*</font>物料名称</th>
						<th><font color="red">*</font>物料规格</th>
						<th><font color="red">*</font>计量单位</th>
						<th><font color="red">*</font>到货数量</th>
						<th><font color="red">*</font>质检与否</th>
						<th>说明</th>
						<th width="10">&nbsp;</th>
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
						<input id="invCheckDetailList{{idx}}_serialnum" name="invCheckDetailList[{{idx}}].serialnum" type="text" value="{{row.serialnum}}"    class="form-control "readonly= "true"/>
					</td>
					
					
					<td>
						<input id="invCheckDetailList{{idx}}_itemCode" name="invCheckDetailList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"    class="form-control " readonly="true"/>
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
					var data = ${fns:toJson(invCheckMain.invCheckDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl, data[i]);
						invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
					}
				});
					
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
					
			</script>
			</div>
			<div id="tab-2" class="tab-pane fade">
			 <table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>
					    	<input id="allboxName" onclick="allcheckBox()" type="checkbox"/>
						</th>
						<th width="100">序号</th>
						<th>物料编码</th>
						<th>物料名称</th>
						<th>物料二维码</th>
						<th>供应商二维码</th>
					</tr>
				</thead>
				<tbody id="invCheckDetailCodeList">
				</tbody>
			</table>
			
			<script type="text/template" id="invCheckDetailCodeTpl">//<!--
				<tr id="invCheckDetailCodeList{{idx}}">
					<td class="hide">
						<input id="invCheckDetailCodeList{{idx}}_id" name="invCheckDetailCodeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="invCheckDetailCodeList{{idx}}_delFlag" name="invCheckDetailCodeList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
                        <input id="invCheckDetailCodeList{{idx}}_checkbox" name="boxs" type="checkbox" value="${row.id}"/>
                    </td>

                    <td>
						<input id="invCheckDetailCodeList{{idx}}_serialnum" name="invCheckDetailCodeList[{{idx}}].serialnum" readOnly="true" type="text" value="{{row.serialnum}}"    class="form-control "/>
					</td>	
					<td>
						<input id="invCheckDetailCodeList{{idx}}_itemCode" name="invCheckDetailCodeList[{{idx}}].itemCode" type="text" readOnly="true" value="{{row.itemCode}}"    class="form-control "/>
					</td>	
					<td>
						<input id="invCheckDetailCodeList{{idx}}_itemName" name="invCheckDetailCodeList[{{idx}}].itemName" type="text" value="{{row.itemName}}"  readOnly="true"  class="form-control required"/>
					</td>
					<td>
						<input id="invCheckDetailCodeList{{idx}}_itemBarcode" name="invCheckDetailCodeList[{{idx}}].itemBarcode" type="text" value="{{row.itemBarcode}}"  readOnly="true"  class="form-control required"/>
					</td>
					<td>
						<input id="invCheckDetailCodeList{{idx}}_supBarcode" name="invCheckDetailCodeList[{{idx}}].supBarcode" type="text" value="{{row.supBarcode}}"  class="form-control"/>
					</td>
	
				</tr>//-->
			</script>
			<script>
			var invCheckDetailCodeRowIdx = 0, invCheckDetailCodeTpl = $("#invCheckDetailCodeTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(invCheckMain.invCheckDetailCodeList)};
					for (var i=0; i<data.length; i++){
						addRow('#invCheckDetailCodeList', invCheckDetailCodeRowIdx, invCheckDetailCodeTpl, data[i]);
						invCheckDetailCodeRowIdx = invCheckDetailCodeRowIdx + 1;
					}
				});
			</script>
			</div>
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
		        <div class="col-lg-1"></div>
		        <div class="col-lg-2">
		        <div class="form-group text-center">
		                 <div>
		                      <a href= "#" onclick = "print()" class="btn btn-primary btn-block btn-lg btn-parsley">打印物料二维码</a>
		                 </div>
		             </div>
		        </div>
		        <div class="col-lg-1"></div>
		        <div class="col-lg-2">
		        <div class="form-group text-center">
		                 <div>
		                      <a href= "${ctx}/purinvcheckmain/invCheckMain/listWarehousing" class="btn btn-primary btn-block btn-lg btn-parsley">返回</a>
		                 </div>
		             </div>
		        </div>
		</c:if>
		</form:form>
		<div  id="printContent">

        </div>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>

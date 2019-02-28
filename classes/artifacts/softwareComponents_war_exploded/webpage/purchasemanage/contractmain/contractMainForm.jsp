<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>采购合同管理</title>
	<meta name="decorator" content="ani"/> 
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					if(currentDetailRow==0){
						jp.warning("请输入合同信息");
					}else{
						jp.loading("正在处理...");
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
			
	        $('#bdate').datetimepicker({
				// format: "YYYY-MM-DD"
	        	format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#advanceDate').datetimepicker({
				// format: "YYYY-MM-DD"
	        	format: "YYYY-MM-DD HH:mm:ss"
			});
	        $('#endDate').datetimepicker({
				 //format: "YYYY-MM-DD"
	    	    format: "YYYY-MM-DD HH:mm:ss"
			});
	      
		});
		
		function addRow(list, idx, tpl, row){
			currentDetailRow=currentDetailRow+1;
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
					// format: "YYYY-MM-DD",
					 format: "YYYY-MM-DD HH:mm:ss",
					 widgetPositioning:{vertical :"bottom"}
			    });
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){			
				currentDetailRow=currentDetailRow-1;
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				currentDetailRow=currentDetailRow-1;
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				currentDetailRow=currentDetailRow+1;
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
			//计算含税总额，不含税总额、运费总额
			calculateSumPrice();
		}
		
		
		//在具体的使用中需替换'#applyDetailList', applyDetailRowIdx, applyDetailTpl这三个值
		function setOtherValue(items,obj,targetField,targetId,nam,labNam){
			var da=items;
			 for (var i=1; i<da.length; i++){
				 //console.log(da[i]);
				    //增加一行
				    addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl);				
					//为所增加的行进行赋值
				    addRowModify('#contractDetailList', contractDetailRowIdx, contractDetailTpl, da[i],obj,targetField,targetId,nam,labNam);		
				    contractDetailRowIdx = contractDetailRowIdx + 1;
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
		
		function saveForm(){
			//修改表单提交的action
			//jp.loading("正在保存...");
		    $("#inputForm").attr('action','${ctx}/contractmain/contractMain/save');    
			$("#inputForm").submit();
	//	$.post("${ctx}/contractmain/contractMain/save",$('#inputForm').serialize(),function(data){			
	//		$("#id").val(data.id);
	//		jp.success(data.msg);
	//	});
		}
		function submitForm(){
			//修改表单提交的action
			//jp.loading("正在提交...");
		    $("#inputForm").attr('action','${ctx}/contractmain/contractMain/submit');    
			$("#inputForm").submit();	
		}
		function setRatio(ratio){
		  var rowl=contractDetailRowIdx;
		  for(var i=0;i<rowl;i++){
			    var itemPrice=$("#contractDetailList"+i+"_itemPrice").val();
			    //修改含税单价
				$("#contractDetailList"+i+"_itemPriceTaxed").val((itemPrice*(1+1*ratio)).toFixed(2));
				//获取签订的合同量
				var itemQtyValue=$("#contractDetailList"+i+"_itemQty").val();
				//修改含税金额
			    $("#contractDetailList"+i+"_itemSumTaxed").val((itemPrice*(1+1*ratio)*itemQtyValue).toFixed(2));
			    
		  }
		}
		function setRatioNew(obj){
			  //  var  ratio=obj.value;
			     //获取含税单价
			  var ratio=$("#taxRatio").val(); //获取税率
				  ratio=(ratio.split('%')[0])/100.00;
			  var rowl=contractDetailRowIdx;
			  for(var i=0;i<rowl;i++){
				    var itemPrice=$("#contractDetailList"+i+"_itemPrice").val();
				    //修改含税单价
					$("#contractDetailList"+i+"_itemPriceTaxed").val((itemPrice*(1+1*ratio)).toFixed(2));
					//获取签订的合同量
					var itemQtyValue=$("#contractDetailList"+i+"_itemQty").val();
					//修改含税金额
				    $("#contractDetailList"+i+"_itemSumTaxed").val((itemPrice*(1+1*ratio)*itemQtyValue).toFixed(2));
			  }
		    	//计算含税总额，不含税总额、运费总额
				calculateSumPrice();
			}
		function selectPlan(buyerId){}
		//计算含税总额、不含税总额、运费总额
		function calculateSumPrice(){
			var rowNum=contractDetailRowIdx;
			var goodsSumTaxed=0;
			var goodsSum=0;
			var tranprice=0;
			for(var i=0;i<rowNum;i++){
				var delFlag=$("#contractDetailList"+i+"_delFlag").val();
				if(delFlag=='0'){//删除标志的不累加
					//计算含税总额
					var curItemSumTaxed=$("#contractDetailList"+i+"_itemSumTaxed").val();
				    if(curItemSumTaxed==undefined||curItemSumTaxed==null||""==curItemSumTaxed){
				    	curItemSumTaxed=0;
				    }
					goodsSumTaxed=goodsSumTaxed*1+curItemSumTaxed*1;
					//计算不含税总额
					var curItemSum=$("#contractDetailList"+i+"_itemSum").val();
					if(curItemSum==undefined||curItemSum==null||""==curItemSum){
						curItemSum=0;
				    }
					goodsSum=goodsSum*1+curItemSum*1;
					//计算运费总额
					var curTransSum=$("#contractDetailList"+i+"_transSum").val();
					if(curTransSum==undefined||curTransSum==null||""==curTransSum){
						curTransSum=0;
				    }
					tranprice=tranprice*1+curTransSum*1;
				}
			}
			$("#goodsSumTaxed").val(goodsSumTaxed.toFixed(2));
			$("#goodsSum").val(goodsSum.toFixed(2));
			$("#tranprice").val(tranprice.toFixed(2));
		}
		//在合同信息中选择更改物料时，选中所在行的数量，价格等数据设置为空
		function setPrice(index){
			$(index+"_arriveDate").val("");
			$(index+"_itemQty").val("");
			$(index+"_itemPrice").val("");
			$(index+"_itemSum").val("");
			$(index+"_itemPriceTaxed").val("");
			$(index+"_itemSumTaxed").val("");
			$(index+"_transPrice").val("");
			$(index+"_transSum").val("");
			//计算含税总额，不含税总额、运费总额
			calculateSumPrice();
		}
		
		
	//数字按千分位进行分割显示（逗号分隔）	
		function commafy(value)
		{
		    var tmp=value.replace(/[,]/g,'').split('').reverse().join('');
		    tmp=tmp.replace(/(\d{3})(?=\d)/g,'$1,');
		    return tmp.split('').reverse().join('');
		     
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
				<a class="panelButton" href="#" onclick="history.go(-1)"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="contractMain" action="${ctx}/contractmain/contractMain/submit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		    <form:hidden path="act.taskId"/>
			<form:hidden path="act.taskName"/>
			<form:hidden path="act.taskDefKey"/>
			<form:hidden path="act.procInsId"/>
			<form:hidden path="act.procDefId"/>
			<form:hidden id="flag" path="act.flag"/>	
		<form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/>
		<sys:message content="${message}"/>	
		
		        <div class="form-group">				   
					<label class="col-sm-2 control-label"><font color="red">*</font>合同号：</label>
					<div class="col-sm-3">
						<form:input path="billNum" htmlEscape="false"   readOnly="true"  class="form-control required"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>合同状态：</label>
				     <form:input path="billStateFlag" htmlEscape="false" type="hidden"  class="form-control"/>					     
					<div class="col-sm-3">
						<form:select path="billStateFlag" class="form-control "  disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('ContractStateFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
		
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人编号：</label>
					<div class="col-sm-3">
					    <form:input path="user.id" htmlEscape="false" type="hidden" readOnly="true"  class="form-control"/>					    
						<form:input path="user.no" htmlEscape="false"  readOnly="true"  class="form-control required"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人名称：</label>
					<div class="col-sm-3">
						<form:input path="makeEmpname" htmlEscape="false" readOnly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商编号：</label>
					<div class="col-sm-3">
						<sys:gridselect-pursup url="${ctx}/contractmain/contractMain/accountData" id="account" name="account.id" value="${contractMain.account.id}" labelName="account.accountCode" labelValue="${contractMain.account.accountCode}"
							 title="选择供应商编号" cssClass="form-control required" fieldLabels="供应商编码|供应商名称"  targetId="supName|supAddress|accountTelNum|accountFaxNum|accountLinkMam|taxRatio"  targetField="accountName|accountAddr|telNum|faxNum|accountLinkMam|taxRatio" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" ></sys:gridselect-pursup>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
					<div class="col-sm-3">
						<form:input path="supName" htmlEscape="false"  readOnly="true"  class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">供应商电话：</label>
					<div class="col-sm-3">
					 <%--  <input id="accountTelNum" name="accountTelNum"   class="form-control" readOnly="true" value="<fmt:formatNumber value="${contractMain.tranprice}" pattern="#.00"/>" />
					 --%>
					 <input id="accountTelNum" name="accountTelNum"   class="form-control" readOnly="true" value="${contractMain.accountTelNum}" />
					</div>
				    <label class="col-sm-2 control-label">供应商传真：</label>
					<div class="col-sm-3">
						<form:input path="accountFaxNum" htmlEscape="false"  readOnly="true"  class="form-control"/>
					</div>
				</div>
				
				<div class="form-group">
				   <label class="col-sm-2 control-label">供应商联系人：</label>
					<div class="col-sm-3">
						<form:input path="accountLinkMam" htmlEscape="false"  readOnly="true"  class="form-control"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>税率：</label>
					<div class="col-sm-3">
					   <%-- <sys:gridselect-purratio url="${ctx}/taxratio/taxRatio/data" id="taxRatio" name="taxRatio" value="${contractMain.taxRatio}" labelName="taxRatio" labelValue="${contractMain.taxRatio}"
							 title="选择税率" cssClass="form-control required" targetId="" targetField="" fieldLabels="税率编号|税率值" fieldKeys="taxratioId|taxRatio" searchLabels="税率编号|税率值" searchKeys="taxratioId|taxRatio" ></sys:gridselect-purratio>
						--%>
					  <%--  <input id="taxRatio" name="taxRatioNew"   class="form-control" value="<fmt:formatNumber value="${contractMain.taxRatio}" pattern="#%"/>" />
				      --%>
						  <input id="taxRatio" name="taxRatioNew" htmlEscape="false"    class="form-control required" onchange="setRatioNew(this)" value="<fmt:formatNumber value="${contractMain.taxRatio}" maxIntegerDigits="4" type="percent"/>" />			    
                   </div>
				   <%--
					<label class="col-sm-2 control-label">承送单号：</label>
					<div class="col-sm-3">
						<form:input path="transContractNum" htmlEscape="false"    class="form-control"/>
					</div>
				    --%>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>采购员编号：</label>
					<div class="col-sm-3">
						<sys:gridselect-purbuyer url="${ctx}/group/groupQuery/buyersdata" id="groupBuyer" name="groupBuyer.id" value="${contractMain.groupBuyer.id}" labelName="groupBuyer.user.no" labelValue="${contractMain.groupBuyer.user.no}"
							 title="选择采购员编号" cssClass="form-control required" fieldLabels="采购员编码|采购员名称" targetId="buyerName|buyerPhoneNum|buyerFaxNum|deliveryAddress" targetField="buyername|buyerPhoneNum|buyerFaxNum|deliveryAddress" fieldKeys="user.no|buyername" searchLabels="采购员编码|采购员名称" searchKeys="user.no|buyername" ></sys:gridselect-purbuyer>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>采购员名称：</label>
					<div class="col-sm-3">
						<form:input path="buyerName" htmlEscape="false" readOnly="true"   class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">采购员电话：</label>
					<div class="col-sm-3">
					  <input id="buyerPhoneNum" name="buyerPhoneNum" readOnly="true"  class="form-control" />
					</div>
				    <label class="col-sm-2 control-label">采购员传真：</label>
					<div class="col-sm-3">
						<form:input path="buyerFaxNum" htmlEscape="false"  readOnly="true"  class="form-control"/>
					</div>
				</div>
				
				<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>合同类型：</label>
					<div class="col-sm-3">
					<sys:gridselect-pursup url="${ctx}/contracttype/contractType/data" id="contractType" name="contractType.id" value="${contractMain.contractType.id}" labelName="contractType.contypename" labelValue="${contractMain.contypeName}"
							 title="选择合同类型代码" cssClass="form-control required" fieldLabels="合同类型代码|合同类型名称"  targetId="" targetField="" fieldKeys="contypeid|contypename" searchLabels="合同类型代码|合同类型名称" searchKeys="contypeid|contypename" ></sys:gridselect-pursup>
					</div>
					<label class="col-sm-2 control-label">合同保证金：</label>
					<div class="col-sm-3">
							<input id="contractNeedSum" name="contractNeedSum"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.contractNeedSum}" pattern="#.00"/>" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>签订日期：</label>
					<div class="col-sm-3">						
							<div class='input-group form_datetime' id='bdate'>
			                    <input type='text' id="condate" name="bdate" class="form-control required"  value="<fmt:formatDate value="${contractMain.bdate}" pattern="yyyy-MM-dd"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            			          
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>付款方式：</label>
					<div class="col-sm-3">
						<form:select path="payMode.id" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${payList}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<%-- 
					<div class="form-group">
						<label class="col-sm-2 control-label">付款说明：</label>
						<div class="col-sm-3">
							<form:input path="paymentNotes" htmlEscape="false"    class="form-control "/>
						</div>
						<label class="col-sm-2 control-label">预付额款：</label>
						<div class="col-sm-3">
								<input id="advancePay" name="advancePay"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.advancePay}" pattern="#.00"/>" />
						</div>
					</div>
				--%>
				<div class="form-group">
					<label class="col-sm-2 control-label">预付日期：</label>
					<div class="col-sm-3">
					<div class='input-group form_datetime' id='advanceDate'>
			                    <input type='text'  name="advanceDate" class="form-control"  value="<fmt:formatDate value="${contractMain.advanceDate}" pattern="yyyy-MM-dd"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>	
					</div>
					<label class="col-sm-2 control-label">截止日期：</label>
					<div class="col-sm-3">
					<div class='input-group form_datetime' id='endDate'>
			                    <input type='text'  name="endDate" class="form-control"  value="<fmt:formatDate value="${contractMain.endDate}" pattern="yyyy-MM-dd"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>	
					</div>
				</div>
			<%-- 
				<div class="form-group">
					<label class="col-sm-2 control-label">运输方式：</label>
					<div class="col-sm-3">
						<form:select path="transType.id" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${transList}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
					<label class="col-sm-2 control-label">运费承担方：</label>
					<div class="col-sm-3">
						<form:input path="tranpricePayer" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
			--%>
				
				<div class="form-group">
					
					<label class="col-sm-2 control-label">不含税总额：</label>
					<div class="col-sm-3">
					    <input id="goodsSum" name="goodsSum"   class="form-control" value="<fmt:formatNumber value="${contractMain.goodsSum}" pattern="#.00"/>" />
						<%-- <form:input path="goodsSum" htmlEscape="false"    class="form-control  number"/>
					    --%>
					</div>
					<label class="col-sm-2 control-label">含税总额：</label>
					<div class="col-sm-3">
						<input id="goodsSumTaxed" name="goodsSumTaxed"  class="form-control" value="<fmt:formatNumber value="${contractMain.goodsSumTaxed}" pattern="#.00"/>" />
					 <%-- 	<form:input path="goodsSumTaxed" htmlEscape="false" maxlength="16"   max="1000000000000000"   class="form-control  number"/>
					 --%>
					</div>
				</div>
				
				<div class="form-group">	
					<label class="col-sm-2 control-label">运费总额：</label>
					<div class="col-sm-3">
					  <input id="tranprice" name="tranprice"   class="form-control  number" value="<fmt:formatNumber value="${contractMain.tranprice}" pattern="#.00"/>" />
					</div>					
					<label class="col-sm-2 control-label">单据说明：</label>
					<div class="col-sm-3">
						<form:input path="billNotes" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">供应商地址：</label>
					<div class="col-sm-8">
						<form:input path="supAddress" htmlEscape="false" readOnly="true"   class="form-control "/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">送货地址：</label>
					<div class="col-sm-8">
						<form:input path="deliveryAddress" htmlEscape="false" readOnly="true"   class="form-control "/>
					</div>
				</div>
				
			<%-- 
		    <c:if test="${contractMain.billStateFlag=='B'}">   		  
		       <div class="form-group">			
					<label class="col-sm-2 control-label">审核说明：</label>
					<div class="col-sm-3">
						<form:textarea path="justifyRemark" htmlEscape="false" rows="4" maxlength="2147483647"  readonly="true"  class="form-control "/>			
					</div>
			   </div>
		    </c:if>	
		    --%>	
		    <%-- 
		    <div class="form-group">
					<label class="col-sm-2 control-label">单据类型：</label>
					<div class="col-sm-10">
						<form:hidden id="billType" path="billType" htmlEscape="false" maxlength="64" class="form-control"/>
						<sys:ckfinder input="billType" type="files" uploadPath="/contractmain/contractMain" selectMultiple="true"/>
					</div>
			</div>
			--%>    
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">合同信息</a>
                </li>
                <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">合同说明</a>
                </li>
                <%-- 
                <li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">合同附件</a>
                </li> 
                --%>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl);contractDetailRowIdx = contractDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
		<div class="table-responsive" style="max-height:500px; min-height: 400px">
			<table class="table table-striped table-bordered table-condensed"  style="min-width: 2480px">
				<thead>
					<tr>
						<th class="hide"></th>
						<%-- <th width="150px">编号</th> --%>
						<th width="200px"><font color="red">*</font>物料编码</th>
						<th width="250px"><font color="red">*</font>物料名称</th>
						<th width="250px"><font color="red">*</font>物料规格</th>
						<th width="250px">物料材质</th>
						<th width="220px"><font color="red">*</font>计划到货日期</th>
						<th width="150px"><font color="red">*</font>签合同量</th>
						<th width="150px"><font color="red">*</font>无税单价</th>
						<th width="150px"><font color="red">*</font>无税总额</th>
						<th width="150px"><font color="red">*</font>含税单价</th>
						<th width="150px"><font color="red">*</font>含税金额</th>						
						<th width="100px">单位</th>
						<th width="150px">单位运费（含）</th>
						<th width="150px">运费金额（含）</th>
						<th width="250px">质量要求</th>
						<th width="10px">&nbsp;</th>
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
						<input id="contractDetailList{{idx}}_serialNum" name="contractDetailList[{{idx}}].serialNum" type="text" value="{{idx}}"  readOnly="true"  class="form-control "/>
                    </td>

					<td>
						<sys:gridselect-purmulcons url="${ctx}/contractmain/contractMain/itemData" id="contractDetailList{{idx}}_item" name="contractDetailList[{{idx}}].item.id" value="{{row.item.id}}" labelName="contractDetailList{{idx}}.item.code" labelValue="{{row.item.code}}"
							 title="选择物料编号" cssClass="form-control required " targetId="itemName|itemModel|itemTexture|measUnit" targetField="name|specModel|texture|unit"  fieldLabels="物料编码|物料名称" isMultiSelected="true" fieldKeys="code|name" searchLabels="物料编码|物料名称" searchKeys="code|name" ></sys:gridselect-purmulcons>
					</td>
					
					
					<td>
						<input id="contractDetailList{{idx}}_itemName" name="contractDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"  readonly="true"  class="form-control required"/>
					</td>

	                <td>
						<input id="contractDetailList{{idx}}_itemModel" name="contractDetailList[{{idx}}].itemModel" type="text" value="{{row.itemModel}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="contractDetailList{{idx}}_itemTexture" name="contractDetailList[{{idx}}].itemTexture" type="text" value="{{row.itemTexture}}"  readonly="true"  class="form-control"/>
					</td>
					
                    <td>
					    <div class='input-group form_datetime' id="contractDetailList{{idx}}_arriveDate">
		                    <input type='text'  name="contractDetailList[{{idx}}].arriveDate" class="form-control required"  value="{{row.arriveDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>	
                    </td>
 
					<td>
						<input id="contractDetailList{{idx}}_itemQty" name="contractDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"  onblur="setSumValue(this)"   class="form-control number isFloatGtZero  required"/>
                    </td>
					
					<td>
						<input id="contractDetailList{{idx}}_itemPrice" name="contractDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"  onchange="setItemPrice(this)"  class="form-control number required"/>
						
                    </td>
					
					<td>
						<input id="contractDetailList{{idx}}_itemSum" name="contractDetailList[{{idx}}].itemSum"  type="text" value="{{row.itemSum}}"  readOnly="true"  class="form-control number required"/>
					</td>
					
					<td>
						<input id="contractDetailList{{idx}}_itemPriceTaxed" name="contractDetailList[{{idx}}].itemPriceTaxed" type="text" value="{{row.itemPriceTaxed}}" onchange="setItemPriceTaxed(this)"   class="form-control number required"/>
					    <span id="contractDetailList{{idx}}_itemPriceSpan"></span>
					</td>
					
					<td>
						<input id="contractDetailList{{idx}}_itemSumTaxed" name="contractDetailList[{{idx}}].itemSumTaxed" type="text" value="{{row.itemSumTaxed}}" readOnly="true"  class="form-control number required"/>
					</td>
					
					<td>
						<input id="contractDetailList{{idx}}_measUnit" name="contractDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"    class="form-control "/>
					</td>
				
					<td>
						<input id="contractDetailList{{idx}}_transPrice" name="contractDetailList[{{idx}}].transPrice" type="text" value="{{row.transPrice}}"  onchange="setTransPrice(this)"  class="form-control number"/>
					</td>
					
					<td>
						<input id="contractDetailList{{idx}}_transSum" name="contractDetailList[{{idx}}].transSum" type="text" value="{{row.transSum}}"  readOnly="true"  class="form-control number"/>
					</td>
					
		            <td>
						<input id="contractDetailList{{idx}}_massRequire" name="contractDetailList[{{idx}}].massRequire" type="text" value="{{row.massRequire}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#contractDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
			    //用于记录合同信息的条数，验证合同信息不为空
			    var currentDetailRow=0;
				var contractDetailRowIdx =0, contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(contractMain.contractDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl, data[i]);
						contractDetailRowIdx = contractDetailRowIdx + 1;
					}
					
					
			/*		var form = layui.form;
					form.verify({
						  username: function(value, item){ //value：表单的值、item：表单的DOM对象
						    alert("username验证");
						  return "username验证";
						  }
						});      
					*/			
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
				//	var itemSumValue=$("#"+itemPriceId).val();
					//获取供应商id
					var accountId=$("#accountId").val();
					   //获取物料ID
					   var itemId=$("#"+preTagId+"_itemId").val();
					   //依据物料id、物料数量获取物料单价
			/*		   var itemSumValue=getPurSupPrice(itemId,obj.value,accountId);
					   alert("itemSumValue"+itemSumValue);
					   //设置界面的物料单价
					$("#"+itemPriceId).val(itemSumValue);
					//修改无税总额
					$("#"+itemSumId).val((obj.value*itemSumValue).toFixed(2));
					//获取含税单价
					var itemPriceTaxedValue=$("#"+preTagId+"_itemPriceTaxed").val();
					//修改含税金额
					$("#"+preTagId+"_itemSumTaxed").val((itemPriceTaxedValue*obj.value).toFixed(2));
					//获取运费单价
					var transPriceValue=$("#"+preTagId+"_transPrice").val();
					//修改运费金额
					$("#"+preTagId+"_transSum").val((transPriceValue*obj.value).toFixed(2));
			*/         //依据物料id、物料数量获取物料单价
			           var itemNum=obj.value;
			         //  alert("--itemId:"+itemId+"--itemNum:"+itemNum+"--accountId:"+accountId);	
			           var condate=$("#condate").val();
			     /*     if(accountId=""){
			        	   jp.warning("请选择供应商");
			           }else
			        	if(condate==""){
			        	   jp.warning("请输入签订日期");
			           }
			           else
			           if(itemId=""){
			        	   jp.warning("请选择物料");
			           }else 
			           if(itemNum=""){
			        	   jp.warning("请输入合同数量");
			           }
			           else
			           {		 	            
					 */    $.ajax({
							url:"${ctx}/contractmain/contractMain/getItemPrice",
							type:"POST",
						//	async: true,
							cache:false,
							dataType: "json",
							data: { "itemId": itemId, "itemNum": itemNum, "accountId": accountId, "condate": condate },
							success:function(data){
								//console.log(data);
							    var itemSumValue=Number(data.body.sumPrice);//获取含税总额，供应商价格维护中的价格是含税单价	
							  
							    var itemUnitPrice=Number(data.body.supPrice);//获取含税单价，供应商价格维护中的价格是含税单价	
							    
								if(0==itemSumValue){	
									var info=data.body.info;
									if(info!=undefined){
 										if(info=='lack'){
  									        $("#"+preTagId+"_itemPriceSpan").html("<font color='red'>供应商价格数据缺失</font>");
 										}
 										else{
 											jp.warning(data.body.info);
 	 									    $("#"+preTagId+"_itemPriceSpan").html("<font color='red'>价格数据获取失败</font>");
 										}
									}
									$("#"+preTagId+"_itemQty").val("");
								    $("#"+itemSumId).val("");
								    $("#"+itemPriceId).val("");
								    $("#"+preTagId+"_itemPriceTaxed").val("");
								    $("#"+preTagId+"_itemSumTaxed").val("");
								}else{
								    $("#"+preTagId+"_itemPriceSpan").html("");
									//var itemSumValue=data;//获取含税总额
									//填写含税总额
									$("#"+preTagId+"_itemSumTaxed").val(itemSumValue.toFixed(2));
							//		alert(commafy(itemSumValue.toFixed(2)));
									//填写含税平均单价
								//	var avgItemPrice=(itemSumValue/(1*itemNum)).toFixed(2);  //阶梯价计算方法
							        var avgItemPrice=itemUnitPrice;
									$("#"+preTagId+"_itemPriceTaxed").val(avgItemPrice);
								//	$("#"+itemPriceId).val(itemSumValue.toFixed(2));
									//修改无税总额
								//	$("#"+itemSumId).val((obj.value*itemSumValue).toFixed(2));
									//获取含税单价
									var taxRatio=$("#taxRatio").val(); //获取税率
									taxRatio=(taxRatio.split('%')[0])/100.00;
									//填写含税平均价格
									//var avgItemPriceTaxed=(avgItemPrice*(1+taxRatio)).toFixed(2);
									//$("#"+preTagId+"_itemPriceTaxed").val(avgItemPriceTaxed);
									//填写无税总额
									//$("#"+preTagId+"_itemSumTaxed").val(((itemUnitPrice/(1+taxRatio))*itemNum).toFixed(2));
//									$("#"+itemSumId).val((itemSumValue/(1+taxRatio)).toFixed(2));
                                    $("#"+itemSumId).val(((itemUnitPrice/(1+taxRatio))*itemNum).toFixed(2));
									//填写无税单价
									$("#"+itemPriceId).val(((itemUnitPrice/(1+taxRatio))).toFixed(2));
//									$("#"+itemPriceId).val(((itemSumValue/(1+taxRatio))/(1*itemNum)).toFixed(2));
									//var itemPriceTaxedValue=$("#"+preTagId+"_itemPriceTaxed").val();
								//	var itemPriceTaxedValue=(itemSumValue*(1+1*taxRatio)).toFixed(2);
								//	$("#"+preTagId+"_itemPriceTaxed").val(itemPriceTaxedValue);
									//修改含税金额
								//	$("#"+preTagId+"_itemSumTaxed").val((itemPriceTaxedValue*obj.value).toFixed(2));
									//获取运费单价
									var transPriceValue=$("#"+preTagId+"_transPrice").val();
									//修改运费金额
									$("#"+preTagId+"_transSum").val((transPriceValue*itemNum).toFixed(2));
									//计算含税总额，不含税总额、运费总额
									calculateSumPrice();
							  }
				  		   }
						});
					   
			       //   }
				}
				
				//依据物料id、物料数量获取物料单价
			/**	function getPurSupPrice(itemId,itemNum,accountId){
					var itemPrice=0;
				//	alert("--itemId:"+itemId+"--itemNum:"+itemNum+"--accountId:"+accountId);
					$.ajax({
						url:"${ctx}/contractmain/contractMain/getItemPrice?itemId="+itemId+"&itemNum="+itemNum+"&accountId="+accountId,
						type: "GET",
						cache:false,
						dataType: "json",
						success:function(data){
							//console.log(data);
							itemPrice=data;
						}
					});
					//alert('itemPrice:'+itemPrice);
					return itemPrice;
		 	   }
			*/	
				//通过无税单价修改无税金额、含税单价、含税金额
				function setItemPrice(obj){
					//获取当前点击所在的行的信息
					var preTagId=obj.id.split("_")[0];
					//获取签订的合同量
					var itemQtyValue=$("#"+preTagId+"_itemQty").val();
					//修改无税总额
					$("#"+preTagId+"_itemSum").val((itemQtyValue*obj.value).toFixed(2));
					//获取税率的值
					//var taxRatio=$("#taxRatioNames").val();
					var taxRatio=$("#taxRatio").val();
					taxRatio=(taxRatio.split('%')[0])/100.00;
					//修改含税单价
					$("#"+preTagId+"_itemPriceTaxed").val((obj.value*(1+1*taxRatio)).toFixed(2));
					//修改含税金额
				    $("#"+preTagId+"_itemSumTaxed").val((obj.value*(1+1*taxRatio)*itemQtyValue).toFixed(2));
				    
				    //计算含税总额，不含税总额
					calculateSumPrice();					
				}
				//通过含税单价修改含税金额、无税单价、无税金额
				function setItemPriceTaxed(obj){
					//获取当前点击所在的行的信息
					var preTagId=obj.id.split("_")[0];
					//获取签订的合同量
					var itemQtyValue=$("#"+preTagId+"_itemQty").val();
					//修改含税金额
					$("#"+preTagId+"_itemSumTaxed").val((itemQtyValue*obj.value).toFixed(2));			
					//获取税率的值
					//var taxRatio=$("#taxRatioNames").val();
					var taxRatio=$("#taxRatio").val();
					taxRatio=(taxRatio.split('%')[0])/100.00;
					//修改无税单价
					$("#"+preTagId+"_itemPrice").val((obj.value/(1+1*taxRatio)).toFixed(2));
					//修改无税金额
					$("#"+preTagId+"_itemSum").val(((obj.value/(1+1*taxRatio))*itemQtyValue).toFixed(2));
					
					//计算含税总额，不含税总额
					calculateSumPrice();
					
				}
				//通过运费单价修改运费金额
				function setTransPrice(obj){
					//获取当前点击所在的行的信息
					var preTagId=obj.id.split("_")[0];
					//获取签订的合同量
					var itemQtyValue=$("#"+preTagId+"_itemQty").val();
					$("#"+preTagId+"_transSum").val((obj.value*itemQtyValue).toFixed(2));
					
					//计算运费总额
					calculateSumPrice();
				}
				
			</script>
			</div>
			
			
			
			
			<div id="tab-2" class="tab-pane fade">			   
			       <br/>  
			       <div class="form-group">				   
					<label class="col-sm-2 control-label">合同模板编码：</label>
					<div class="col-sm-3">
						<sys:gridselect-pursup url="${ctx}/contractnotesmodel/contractNotesModel/data" id="contractModel" name="contractNotesModel.id" value="${contractMain.contractNotesModel.id}" labelName="contractNotesModel.contractId" labelValue="${contractMain.contractNotesModel.contractId}"
							 title="选择合同模板" cssClass="form-control" targetId="contractModelName|dealNote" targetField="contractName|contractNotes" fieldLabels="合同模板编码|合同模板名称" fieldKeys="contractId|contractName" searchLabels="合同模板编码|合同模板名称" searchKeys="contractId|contractName" ></sys:gridselect-pursup>
					</div>
					<label class="col-sm-2 control-label">合同模板名称：</label>
					<div class="col-sm-3">
						<form:input path="contractModelName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">合同说明：</label>
					<div class="col-sm-8">
						 <form:textarea path="dealNote" htmlEscape="false" rows="15" maxlength="2147483647"    class="form-control "/>
					</div>
				</div>						   
			<script type="text/javascript">
				$(document).ready(function() {
				   
				});
			</script>
			</div>
			
			 <div id="tab-3" class="tab-pane fade">			   
			       <div class="form-group">
			       	  <br>			       			  
					  <label class="col-sm-2 control-label">上传附件：</label>
					  <div class="col-sm-8">
					    <br>
                        <form:hidden path="billType" htmlEscape="false" maxlength="150" class="input-xlarge"/>
                        <sys:ckfinder input="billType" type="files" selectMultiple="true" readonly="false" uploadPath="/合同fu/contractMain"/>                    					    					    
					  </div>
			       </div>			
			<script type="text/javascript">
				$(document).ready(function() {	
				});
			</script>
			</div>
		  
		</div>
		</div>
		<c:if test="${fns:hasPermission('contractmain:contractMain:edit') || isAdd}">
		   <%--  <div class="row">
				<div class="col-lg-2"></div>
		        <div class="col-lg-3">
		             <div class="form-group text-center">
		                 <div>
		                   	 <a href= "#" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在保存..." onclick="saveForm()">保存</a>                  
		                 </div>
		             </div>
		        </div>
		        <div class="col-lg-2"></div>
		        <div class="col-lg-3">
		        <div class="form-group text-center">
		                 <div>
		                 	<a href= "#" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交..." onclick="submitForm()">提交</a>                  
		                 
		     --%>            <%-- 	<button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提交</button>  
		                 </div>
		             </div>
		        </div>
		    </div>
		    --%>
		    <div class="row">
			  <c:if test="${not empty contractMain.id}">
					<div class="col-lg-2"></div>
			        <div class="col-lg-3">
			             <div class="form-group text-center">
			                 <div>
								<%-- <input id="btnSubmit2" class="btn  btn-danger btn-lg btn-parsley" type="submit" value="销毁" onclick="$('#flag').val('no')"/>&nbsp;							    
			                    <input type="button" id="button1" class="btn btn-danger btn-block btn-lg btn-parsley" onclick="$('#flag').val('no')" data-loading-text="正在处理..." value="销毁"></input>			                 
			                    --%>
			                    <button class="btn btn-danger btn-block btn-lg btn-parsley" data-loading-text="正在提交..." onclick="$('#flag').val('no')">销毁</button>   		                 
                             </div>
			             </div>
			        </div>
		     
		        <div class="col-lg-2"></div>
		        <div class="col-lg-3">
		        <div class="form-group text-center">
		                 <div>
		                  <%--<input id="btnSubmit" class="btn  btn-primary btn-lg btn-parsley" type="submit" value="提交" onclick="$('#flag').val('yes')"/>&nbsp;
		                  <input type="button" id="button2" class="btn btn-success btn-block btn-lg btn-parsley" onclick="$('#flag').val('yes')" data-loading-text="正在提交..." value="提交"></input>
		                  --%> 
		                	<button class="btn btn-success btn-block btn-lg btn-parsley" data-loading-text="正在提交..." onclick="$('#flag').val('yes')">提交</button>   		                 
		                 </div>
		             </div>
		        </div>
		      </c:if>
		      <c:if test="${empty contractMain.id}">
		        <div class="col-lg-4"></div>
		        <div class="col-lg-4">
		        <div class="form-group text-center">
		                 <div>
		                  <%--   
		                    <input id="btnSubmit" class="btn  btn-primary btn-lg btn-parsley" type="submit" value="提交" onclick="$('#flag').val('yes')"/>&nbsp;
		                 	<input type="button" id="button2" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="$('#flag').val('yes')" data-loading-text="正在提交..." value="提交"></input>
		                  --%> 
		                  	<button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交..." onclick="$('#flag').val('yes')">提交</button>   		                 
		                 </div>
		             </div>
		         </div>
		      </c:if>
		    </div>
		    
		</c:if>
		        <c:if test="${not empty contractMain.id}">
							<act:flowChart procInsId="${contractMain.act.procInsId}"/>
							<act:histoicFlow procInsId="${contractMain.act.procInsId}" />
				</c:if>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
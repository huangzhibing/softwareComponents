<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购合同管理管理</title>
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
			
	        $('#bdate').datetimepicker({
				// format: "YYYY-MM-DD"
	        	format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#advanceDate').datetimepicker({
				// format: "YYYY-MM-DD"
	        	format: "YYYY-MM-DD HH:mm:ss"
			});
	      $('#endDate').datetimepicker({
				// format: "YYYY-MM-DD"
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
					// format: "YYYY-MM-DD"
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
			
		
		function button_checkPass(){
			//按照设计文档，需等待审核意见输入完毕后才更改相关字段，保持事务一致性
			//$('#billStateFlag').val('E');

			//loading()函数阻塞主窗体，强制等待【审核意见输入窗体】返回
			jp.loading("正在处理...");
			
			var modelCode="CON";
            // alert(modelCode);
			//调用layer弹窗模块打开审核意见输入窗体，等待输入返回
			//功能未完成，目前只实现了界面，对记录的操作未添加
			//时间：2018年5月6日 17:51:13
			//关闭所有弹窗
			//jp.close();
			top.layer.open({
				type:2,
				title:"审核意见",
				btn: ['提交'],
				content:"${ctx}/contractmain/contractMainCheckComment/pass?id=${contractMain.id}",
				shadeClose:false,
				shade:false,
				area:['500px','180px'],				
				yes:function(index,layero){				
					var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
					var ifram= iframeWin.test();
					$("#nextUser").val(ifram[0]);
					top.layer.close(index);//关闭对话框。
					jp.close();
					$("#inputForm").submit();
				},
				cancel:function(index,layer){
					 top.layer.close(index);//关闭对话框。	
					 jp.close();
				}
			});

		}
		function button_checkFail(){
			//按照设计文档，需等待审核意见输入完毕后才更改相关字段，保持事务一致性
			//loading()函数阻塞主窗体，强制等待【审核意见输入窗体】返回
			jp.loading("正在处理……");
			var modelCode="CON";
			//调用layer弹窗模块打开审核意见输入窗体，等待输入返回
			//功能未完成，目前只实现了界面，对记录的操作未添加
			//时间：2018年5月6日 17:51:13
			var da=$("#inputForm").serializeJSON();
	
			top.layer.open({
				type:2,
				title:"审核意见",
				content:"${ctx}/contractmain/contractMainCheckComment/list?id=${contractMain.id}",
				shadeClose:false,
				btn: ['提交'],
				shade:false,
				area:['600px','380px'],
				yes:function(index,layero){				
					var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
					var ifram= iframeWin.test();
					$("#nextUser").val(ifram[0]);
					$("#justifyRemark").val(ifram[1]);			
				    $("#inputForm").attr('action','${ctx}/contractmain/contractMainCheck/unpassSave');    
					top.layer.close(index);//关闭对话框。
					jp.close();
					$("#inputForm").submit();
				},
				cancel:function(index,layer){
					 jp.close();
					 top.layer.close(index);//关闭对话框。	
					}
				});		    
		}
		//工作流调用
		function checkFail(){
			//按照设计文档，需等待审核意见输入完毕后才更改相关字段，保持事务一致性
			//loading()函数阻塞主窗体，强制等待【审核意见输入窗体】返回
			jp.loading("正在处理……");
			//调用layer弹窗模块打开审核意见输入窗体，等待输入返回
			top.layer.open({
				type:2,
				title:"审核意见",
				content:"${ctx}/contractmain/contractMainCheckComment/list?id=${contractMain.id}",
				shadeClose:false,
				btn: ['提交'],
				shade:false,
				area:['600px','380px'],
				yes:function(index,layero){				
					var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
					var ifram= iframeWin.test();
					//$("#nextUser").val(ifram[0]);
					$("#justifyRemark").val(ifram[1]);	
					$('#flag').val('no');
				    $("#inputForm").attr('action','${ctx}/contractmain/contractMainCheck/saveAudit');    
					top.layer.close(index);//关闭对话框。
					jp.close();
					$("#inputForm").submit();
				},
				cancel:function(index,layer){
					 jp.close();
					 top.layer.close(index);//关闭对话框。	
					}
				});		    
		}
		
		//工作流调用
		function checkPass(){
			//按照设计文档，需等待审核意见输入完毕后才更改相关字段，保持事务一致性
			//loading()函数阻塞主窗体，强制等待【审核意见输入窗体】返回
			jp.loading("正在处理……");
			//调用layer弹窗模块打开审核意见输入窗体，等待输入返回
			top.layer.open({
				type:2,
				title:"审核意见",
				content:"${ctx}/contractmain/contractMainCheckComment/list?id=${contractMain.id}",
				shadeClose:false,
				btn: ['提交'],
				shade:false,
				area:['600px','380px'],
				yes:function(index,layero){				
					var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
					var ifram= iframeWin.test();
					//$("#nextUser").val(ifram[0]);
					$("#justifyRemark").val(ifram[1]);	
					$('#flag').val('yes');
				    $("#inputForm").attr('action','${ctx}/contractmain/contractMainCheck/saveAudit');    
					top.layer.close(index);//关闭对话框。
					jp.close();
					$("#inputForm").submit();
				},
				cancel:function(index,layer){
					 jp.close();
					 top.layer.close(index);//关闭对话框。	
					}
				});		    
		}
		
		
		//在具体的使用中需替换'#applyDetailList', applyDetailRowIdx, applyDetailTpl这三个值
		function setOtherValue(items,obj,targetField,targetId,nam,labNam){
			var da=items;
			 for (var i=1; i<da.length; i++){
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
		
		function submitForm(){
		//	alert(2);
			//修改表单提交的action
		 //   $("#inputForm").attr('action','${ctx}/contractmain/contractMain/submit');    
		//	$("#inputForm").submit();
		$.post("${ctx}/contractmain/contractMainCheck/save",$('#inputForm').serialize(),function(data){			
			jp.success(data.msg);
		});
		}
		
		function setRatio(ratio){
		  var rowl=contractDetailRowIdx;
		 // alertr(ratio);
		  for(var i=1;i<rowl;i++){
			    var itemPrice=$("#contractDetailList"+i+"_itemPrice").val();
			    //修改含税单价
				$("#contractDetailList"+i+"_itemPriceTaxed").val(itemPrice*(1+1*ratio));
				//获取签订的合同量
				var itemQtyValue=$("#contractDetailList"+i+"_itemQty").val();
				//修改含税金额
			    $("#contractDetailList"+i+"_itemSumTaxed").val(itemPrice*(1+1*ratio)*itemQtyValue);
			    
		  }
		}
		function selectPlan(buyerId){}
		
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="#"  onclick="history.go(-1)"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="contractMain" action="${ctx}/contractmain/contractMainCheck/saveAudit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="nextUser"/>
		<form:hidden path="justifyRemark"/><%--存放审核不通过意见 ，在后台赋值给act.comment--%>
		    <form:hidden path="act.taskId"/>
			<form:hidden path="act.taskName"/>
			<form:hidden path="act.taskDefKey"/>
			<form:hidden path="act.procInsId"/>
			<form:hidden path="act.procDefId"/>
			<form:hidden id="flag" path="act.flag"/>	
		  
		<sys:message content="${message}"/>	
		        <div class="form-group">				   
					<label class="col-sm-2 control-label">合同号：</label>
					<div class="col-sm-3">
						<form:input path="billNum" htmlEscape="false"   readOnly="true"  class="form-control required"/>
					</div>
					<label class="col-sm-2 control-label">合同状态：</label>
				     <form:input path="billStateFlag" htmlEscape="false" type="hidden" readOnly="true"  class="form-control"/>					     
					<div class="col-sm-3">
						<form:select path="billStateFlag" class="form-control "  disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('ContractStateFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
		
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人编号：</label>
					<div class="col-sm-3">
					    <form:input path="user.id" htmlEscape="false" type="hidden" readOnly="true"  class="form-control"/>					    
						<form:input path="user.no" htmlEscape="false"  readOnly="true"  class="form-control required"/>
					</div>
					<label class="col-sm-2 control-label">制单人名称：</label>
					<div class="col-sm-3">
						<form:input path="makeEmpname" htmlEscape="false" readOnly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">供应商编号：</label>
					<div class="col-sm-3">
						        <form:input path="account.id" htmlEscape="false" readOnly="true"  class="form-control hidden"/>										            
								<form:input path="account.accountCode" htmlEscape="false" readOnly="true"  class="form-control required"/>					
				
					</div>
					<label class="col-sm-2 control-label">供应商名称：</label>
					<div class="col-sm-3">
						<form:input path="supName" htmlEscape="false"  readOnly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">供应商电话：</label>
					<div class="col-sm-3">
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
					<label class="col-sm-2 control-label">税率：</label>
					<div class="col-sm-3">
					   <%-- <sys:gridselect-purratio url="${ctx}/taxratio/taxRatio/data" id="taxRatio" name="taxRatio" value="${contractMain.taxRatio}" labelName="taxRatio" labelValue="${contractMain.taxRatio}"
							 title="选择税率" cssClass="form-control required" targetId="" targetField="" fieldLabels="税率编号|税率值" fieldKeys="taxratioId|taxRatio" searchLabels="税率编号|税率值" searchKeys="taxratioId|taxRatio" ></sys:gridselect-purratio>
						--%>
						 <input id="taxRatio" name="taxRatioNew" htmlEscape="false" readOnly="true"  class="form-control required" onchange="setRatioNew(this)" value="<fmt:formatNumber value="${contractMain.taxRatio}" maxIntegerDigits="4" type="percent"/>" />			    
					</div>
					
				   <%--
					<label class="col-sm-2 control-label">承送单号：</label>
					<div class="col-sm-3">
						<form:input path="transContractNum" htmlEscape="false"    class="form-control"/>
					</div>
				    --%>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">采购员编号：</label>
					<div class="col-sm-3">
				             <form:input path="groupBuyer.id" htmlEscape="false" readOnly="true"  class="form-control hidden"/>										            
						     <form:input path="groupBuyer.user.no" htmlEscape="false" readOnly="true"  class="form-control required"/>					
				    </div>
					<label class="col-sm-2 control-label">采购员名称：</label>
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
				<label class="col-sm-2 control-label">合同类型：</label>
					<div class="col-sm-3">
					            <form:input path="contractType.id" htmlEscape="false" readOnly="true"  class="form-control hidden"/>										            
								<form:input path="contractTypeId" htmlEscape="false" readOnly="true"  class="form-control hidden"/>										            								
								<form:input path="contypeName" htmlEscape="false" readOnly="true"  class="form-control required"/>					
					</div>
					<label class="col-sm-2 control-label">合同保证金：</label>
					<div class="col-sm-3">
						<input id="contractNeedSum" name="contractNeedSum" readOnly="true" class="form-control  number" value="<fmt:formatNumber value="${contractMain.contractNeedSum}" pattern="#.00"/>" />
					</div>					
				</div>
				<div class="form-group">
					
					<label class="col-sm-2 control-label">签订日期：</label>
					<div class="col-sm-3">						
							<div class='input-group form_datetime' id='bdate'>
			                    <input type='text'  name="bdate" class="form-control required"  readOnly="true" value="<fmt:formatDate value="${contractMain.bdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            			          
					</div>
					<label class="col-sm-2 control-label">付款方式：</label>
							<form:input path="payMode.id" htmlEscape="false" type="hidden" readOnly="true"  class="form-control"/>					     					
					<div class="col-sm-3">
						<form:select path="payMode.id" disabled="true" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${payList}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
					
				</div>
				<%-- 
				<div class="form-group">
					<label class="col-sm-2 control-label">付款说明：</label>
					<div class="col-sm-3">
						<form:input path="paymentNotes" htmlEscape="false"  readOnly="true"  class="form-control "/>
					</div>
					<label class="col-sm-2 control-label">预付额款：</label>
					<div class="col-sm-3">
						<input id="advancePay" name="advancePay"  readOnly="true"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.advancePay}" pattern="#.00"/>" />
						
					</div>
				</div>
				--%>
				<div class="form-group">
					<label class="col-sm-2 control-label">预付日期：</label>
					<div class="col-sm-3">
					<div class='input-group form_datetime' id='advanceDate'>
			                    <input type='text'  name="advanceDate" class="form-control" readOnly="true"  value="<fmt:formatDate value="${contractMain.advanceDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>	
					</div>
					<label class="col-sm-2 control-label">截止日期：</label>
					<div class="col-sm-3">
					<div class='input-group form_datetime' id='endDate'>
			                    <input type='text'  name="endDate" class="form-control" readOnly="true" value="<fmt:formatDate value="${contractMain.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>	
					</div>
				</div>
			<%-- 	<div class="form-group">
					<label class="col-sm-2 control-label">运输方式：</label>
						<form:input path="transType.id" htmlEscape="false" type="hidden" readOnly="true"  class="form-control"/>					     					
					<div class="col-sm-3">
						<form:select path="transType.id" disabled="true" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${transList}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
					<label class="col-sm-2 control-label">运费承担方：</label>
					<div class="col-sm-3">
						<form:input path="tranpricePayer" htmlEscape="false" readOnly="true"   class="form-control "/>
					</div>
				</div>
			
				<div class="form-group">
					
					<label class="col-sm-2 control-label">承送单号：</label>
					<div class="col-sm-3">
						<form:input path="transContractNum" htmlEscape="false"  readOnly="true"  class="form-control"/>
					</div>
				</div>
			--%>
				<div class="form-group">
					<label class="col-sm-2 control-label">含税总额：</label>
					<div class="col-sm-3">
						<input id="goodsSumTaxed" name="goodsSumTaxed"  readOnly="true"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.goodsSumTaxed}" pattern="#.00"/>" />
					<%-- 	<form:input path="goodsSumTaxed" htmlEscape="false"  readOnly="true"  class="form-control  number"/>
					--%>
					</div>
					<label class="col-sm-2 control-label">不含税总额：</label>
					<div class="col-sm-3">
					    <input id="goodsSum" name="goodsSum"  readOnly="true"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.goodsSum}" pattern="#.00"/>" />
						<%--<form:input path="goodsSum" htmlEscape="false"  readOnly="true"  class="form-control  number"/>
					     --%>
					</div>
				</div>
				
				<div class="form-group">	
					
					<label class="col-sm-2 control-label">运费总额：</label>
					<div class="col-sm-3">
						<input id="tranprice" name="tranprice"  readOnly="true"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.tranprice}" pattern="#.00"/>" />
					<%-- 	<form:input path="tranprice" htmlEscape="false"  readOnly="true"  class="form-control  number"/>
					 --%>
					</div>
					<label class="col-sm-2 control-label">单据说明：</label>
					<div class="col-sm-3">
						<form:input path="billNotes" htmlEscape="false" readOnly="true"   class="form-control "/>
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
		        <div class="form-group">
		             <label class="col-sm-2 control-label">审核意见：</label>
		             <div class="col-sm-3">
						  <form:textarea path="act.comment" class="form-control" rows="5" type="hidden" maxlength="20"/>
					</div>
		        </div>
		    --%>
		        <div id="popupFormDiv1" style="display:none;"></div> 
		        <div id="popupFormDiv2" style="display:none;"></div> 
		        
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
		<div class="table-responsive" style="max-height:500px">
			<table class="table table-striped table-bordered table-condensed"  style="min-width: 2630px">
				<thead>
					<tr>
						<th class="hide"></th>
						<th width="150px">编号</th>
						<th width="200px">物料编码</th>
						<th width="250px">物料名称</th>
						<th width="250px">物料规格</th>
						<th width="250px">物料材质</th>
						<th width="220px">计划到货日期</th>
						<th width="150px">签合同量</th>
						<th width="150px">无税单价</th>
						<th width="150px">无税总额</th>
						<th width="150px">含税单价</th>
						<th width="150px">含税金额</th>						
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
						<input id="contractDetailList{{idx}}_itemId" name="contractDetailList[{{idx}}].item.id" type="hidden" value="{{row.item.id}}"/>
                    </td>

					<td>
						<input id="contractDetailList{{idx}}_serialNum" name="contractDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"  readOnly="true"  class="form-control "/>
					</td>
					
					
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
					    <div class='input-group form_datetime' id="contractDetailList{{idx}}_arriveDate">
		                    <input type='text'  name="contractDetailList[{{idx}}].arriveDate" class="form-control required" readOnly="true" value="{{row.arriveDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>	
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
					var data = ${fns:toJson(contractMain.contractDetailList)};
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
			
			<div id="tab-2" class="tab-pane fade">			   
			       <br/>  
			       <div class="form-group">				   
					<label class="col-sm-2 control-label">合同模板编码：</label>
					<div class="col-sm-3">
					     	    <form:input path="contractNotesModel.id" htmlEscape="false" readOnly="true"  class="form-control hidden"/>										            
								<form:input path="contractNotesModel.contractId" htmlEscape="false" readOnly="true"  class="form-control"/>					
					
					</div>
					<label class="col-sm-2 control-label">合同模板名称：</label>
					<div class="col-sm-3">
						<form:input path="contractModelName" htmlEscape="false"  readOnly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">合同说明：</label>
					<div class="col-sm-8">
						<form:textarea path="dealNote" htmlEscape="false" rows="15" maxlength="2147483647"  readonly="true"  class="form-control "/>
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
                        <sys:ckfinder input="billType" type="files" selectMultiple="true" readonly="true" uploadPath="/contractmain/contractMain"/>                    					    					    
					  </div>
			       </div>			
			<script type="text/javascript">
				$(document).ready(function() {	
				});
			</script>
			</div>
			
		</div>
		</div>
		<c:if test="${fns:hasPermission('contractmaincheck:contractMain:edit') || isAdd}">
			              <div class="row">  
		                          <div class="col-lg-2"></div>
									<div class="col-lg-3">
										<div class="form-group text-center">
											<div>
											    <input id="btnSubmit1" class="btn btn-success btn-block btn-lg btn-parsley" type="button" value="同 意" onclick="checkPass()"/>&nbsp;
											 <%--  <input id="btnSubmit1" class="btn btn-success btn-block btn-lg btn-parsley" type="submit" value="同 意" onclick="$('#flag').val('yes')"/>&nbsp;
											 --%>  <%-- <button class="btn btn-success btn-block btn-lg btn-parsley" data-loading-text="正在提交..." onclick="$('#flag').val('yes')">同意</button>   		                 
											  --%>
											</div>
										</div>
									</div>
									<div class="col-lg-2"></div>
									<div class="col-lg-3">
										<div class="form-group text-center">
											<div>
											  <input id="btnSubmit2" class="btn btn-danger btn-block btn-lg btn-parsley" type="button" value="驳 回" onclick="checkFail()"/>&nbsp;
											<%--   <button class="btn btn-danger btn-block btn-lg btn-parsley" data-loading-text="正在提交..." onclick="checkFail()">驳回</button>   		                 
											--%>
											</div>
										</div>              
				                    </div>
				             </div>  
				             <%--    <div class="row">   
				                    <c:if test="${testAudit.act.taskDefKey ne 'purcontractclose'}">
											<input id="btnSubmit1" class="btn  btn-primary btn-lg btn-parsley" type="submit" value="同 意" onclick="$('#flag').val('yes')"/>&nbsp;
											<input id="btnSubmit2" class="btn  btn-danger btn-lg btn-parsley" type="button" value="驳 回" onclick="checkFail()"/>&nbsp;
									</c:if>
				                    <div class="col-lg-1"></div>
									<div class="col-lg-2">
										<div class="form-group text-center">
											<div>
												<a class="btn btn-primary btn-block btn-lg btn-parsley"
													href="${ctx}/contractmain/contractMainCheck"
													data-loading-text="正在返回...">返 回</a>
											</div>
										</div>
									</div>
								</div>	--%>
								
								
								
									        		        
		    <%--    <div class="col-lg-4"></div>
		        <div class="col-lg-4">
		        <div class="form-group text-center">
		                 <div>
		                 	<a href= "${ctx}/contractmain/contractMainCheck" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在保存...">返回</a>                  
		                 </div>
		             </div>
		        </div>
		        
		        
		                            <div class="col-lg-2"></div>
									<div class="col-lg-2">
										<div class="form-group text-center">
											<div>
											  <button class="btn btn-success btn-block btn-lg btn-parsley" data-loading-text="正在提交..." onclick="$('#flag').val('yes')">同意</button>   		                 
											</div>
										</div>
									</div>
									<div class="col-lg-1"></div>
									<div class="col-lg-2">
										<div class="form-group text-center">
											<div>
											  <button class="btn btn-danger btn-block btn-lg btn-parsley" data-loading-text="正在提交..." onclick="$('#flag').val('no')">驳回</button>   		                 
											</div>
										</div>              
				                    </div>
		        
		   --%>  
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
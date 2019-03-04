<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购到货生成</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
	var buyerIdforbeginSetPlan = null;

    function qtyCtrl(conId,itemCode){
        var flag = false;
        $.ajax({
            url:"${ctx}/purinvcheckmain/invCheckMain/getCurrentQty?conId="+conId+"&itemCode="+itemCode,
            type: "GET",
            cache:false,
            async:false,
            dataType: "text",
            success:function(data){
                var wcheckQty = $("#invCheckDetailList0_checkQty").val();
                console.log(1*wcheckQty - 1*rcheckQty - 1*data)
                if(1*wcheckQty - 1*rcheckQty - 1*data > 0){
                    jp.warning("总到货物料量超出合同量！");
                }else{
                    flag =  true;
                }
            },
            error:function(){
                jp.warning("读取物料合同量失败！");
            }
        });
        if(flag){return true;}else{return false;}
    }

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
			
			var iotype = $("#ioTypeNames").val();
			$("#ioType").val(iotype);
			
			var contractid = $("#contractIdNames").val();
			$("#contractId").val(contractid);
			
			var planid = $("#planIdNames").val();
			$("#planId").val(planid);
			
			var supid = $("#supNames").val();
			$("#supId").val(supid);
	    }
    
		$(document).ready(function() {
		//根据来源显示合同或计划子表
		var displayPlan= $("#displayPlan");
		var displayCon= $("#displayContract");		
 		displayPlan.css("display","none");
		displayCon.css("display","none"); 
		
		var itemSource=$("#orderOrContract");
		if($("#orderOrContract").val() == "contract"){
			displayCon.css("display","block");
		}else if($("#orderOrContract").val() == "plan"){
			displayPlan.css("display","block");
		}

		itemSource.change(function(){
		if( itemSource.val()== "contract"){
		    var con = $("#buyerName").val();
			if(con==""||con==null){
				jp.warning("请先填写采购员信息！");
			}else{
			//清空并隐藏计划信息
				$("#planIdNames").val("");
				$("#planId").val("");
				$("#planName").val("");
				displayCon.css("display","block");
				displayPlan.css("display","none");
			}
		}else if(itemSource.val()== "plan")
		{
			var pln = $("#buyerName").val();
			if(pln==""||pln==null){
				jp.warning("请先填写采购员信息！");
			}else{
			//清空并隐藏合同信息
				$("#contractIdNames").val("");
				$("#contractId").val("");
				$("#contractName").val("");
				displayPlan.css("display","block");
				displayCon.css("display","none");
			}
		}
		else{
			displayPlan.css("display","none");
			displayCon.css("display","none");
		}
	})
		
			$("#inputForm").validate({
				submitHandler: function(form){
				if(invCheckDetailRowIdx == 0){
					jp.warning("请输入物料子表信息！");
				}else{
                    var conId = $("#contractId").val();var itemCode = $("#invCheckDetailList0_itemId").val();
                    console.log(conId,itemCode);
                    var t = qtyCtrl(conId, itemCode);
                    if(t==true){
                        jp.loading();
                        form.submit();
                    }
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
                // jp.alert('只能选择一个物料！');

			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
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
		
		function selectPlan(planId){
			//buyerIdforbeginSetPlan = buyerId;
			var buyerId = $("#buyerIdNames").val();
			$.ajax({
				url:"${ctx}/purinvcheckmain/invCheckMain/plandatabyAjax?planId="+planId+"&buyerId="+buyerId,
				type: "GET",
				cache:false,
				dataType: "json",
				success:function(data){
					//清除原有的到货子表
				    var namebox = $("input[name^='boxs']");  //获取name值为boxs的所有input
                    for(i = 0; i < namebox.length; i++) {                      
                        $(namebox[i]).parent().parent().remove(); 
                        invCheckDetailRowIdx--;
                    }
				    //清除原有的计划子表
				    var namebox = $("input[name^='plnboxName']");  //获取name值为plnboxName的所有input
                    for(i = 0; i < namebox.length; i++) {                      
                        $(namebox[i]).parent().parent().remove(); 
                        purPlanDetailRowIdx--;
                    }
                    //清除原有的合同子表
				    var namebox = $("input[name^='conboxName']");  //获取name值为conboxName的所有input
                    for(i = 0; i < namebox.length; i++) {                      
                        $(namebox[i]).parent().parent().remove(); 
                        contractDetailRowIdx--;
                    }
                    
					 console.log(data.rows);
					 purPlanData=data;
					 purPlanDetailRowIdx = 0;
					 purPlanDetailTpl = $("#purPlanDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");					
						var planData=data.rows;
						for (var i=0; i<planData.length; i++){
						if(planData[i].checkQty == undefined || planData[i].checkQty == null || planData[i].checkQty == "") planData[i].checkQty = 0;
						    if(planData[i].checkQty < planData[i].planQty){
								addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, planData[i]);
								purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
							}
						}			
				}
			});
		}
		
		//下达给采购员的合同
		var contractDetailData = {};
		
		function selectContract(){
		contractIdforCn = $("#contractIdNames").val();
		console.log(contractIdforCn);
		$.ajax({
			url:"${ctx}/purinvcheckmain/invCheckMain/selectContract?contractId="+contractIdforCn,
			type: "GET",
			cache:false,
			dataType: "json",
			success:function(data){
					//清除原有的到货子表
				    var namebox = $("input[name^='boxs']");  //获取name值为boxs的所有input
                    for(i = 0; i < namebox.length; i++) {                      
                        $(namebox[i]).parent().parent().remove(); 
                        invCheckDetailRowIdx--;
                    }
			
			 		//清除原有的计划子表
				    var namebox = $("input[name^='plnboxName']");  //获取name值为plnboxName的所有input
                    for(i = 0; i < namebox.length; i++) {                      
                        $(namebox[i]).parent().parent().remove(); 
                        purPlanDetailRowIdx--;
                    }
					//清除原有的合同子表
				    var namebox = $("input[name^='conboxName']");  //获取name值为conboxName的所有input
                    for(i = 0; i < namebox.length; i++) {                      
                        $(namebox[i]).parent().parent().remove(); 
                        contractDetailRowIdx--;
                    }
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
						console.log(contractData[i]);
						if(contractData[i].checkQty == undefined || contractData[i].checkQty == null || contractData[i].checkQty == "") contractData[i].checkQty = 0;
						if(contractData[i].checkQty < contractData[i].itemQty){
							addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl, contractData[i]);
							contractDetailRowIdx = contractDetailRowIdx + 1;
						}
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
				   var namebox = $("input[name^='plnboxName']");  //获取name值为boxs的所有input
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
				url:"${ctx}/purinvcheckmain/invCheckMain/selectContract?buyerId="+$("#contractIdNames").val(),
				type: "GET",
				cache:false,
				dataType: "json",
				success:function(data){
					// alert(3);
					//console.log(data);
				     //清除原有的
				   var namebox = $("input[name^='conboxName']");  //获取name值为boxs的所有input
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
    function allcheckBoxcon(){
        //建议用name属性值操作，因为id属性在每一个页面中默认是唯一的，而name属性则可以取到属性值相同的所有
        var nn = $("#allboxName").is(":checked"); //判断th中的checkbox是否被选中，如果被选中则nn为true，反之为false
        if(nn == true) {
            var namebox = $("input[name^='conboxName']");  //获取name值为boxs的所有input
            for(i = 0; i < namebox.length; i++) {
                namebox[i].checked=true;    //js操作选中checkbox
            }
        }
        if(nn == false) {
            var namebox = $("input[name^='conboxName']");
            for(i = 0; i < namebox.length; i++) {
                namebox[i].checked=false;
            }
        }
    }

    //点击选择框则全选
    function allcheckBoxpln(){
        //建议用name属性值操作，因为id属性在每一个页面中默认是唯一的，而name属性则可以取到属性值相同的所有
        var nn = $("#allplnboxName").is(":checked"); //判断th中的checkbox是否被选中，如果被选中则nn为true，反之为false
        if(nn == true) {
            var namebox = $("input[name^='plnboxName']");  //获取name值为boxs的所有input
            for(i = 0; i < namebox.length; i++) {
                namebox[i].checked=true;    //js操作选中checkbox
            }
        }
        if(nn == false) {
            var namebox = $("input[name^='plnboxName']");
            for(i = 0; i < namebox.length; i++) {
                namebox[i].checked=false;
            }
        }
    }
		
		function addToCheck(){
		var flag = 0;
			var namebox = $("input[name^='plnboxName']");
				for(i = 0; i < namebox.length; i++) {
				   //已被选择
				   if(namebox[i].checked){
                       if(invCheckDetailRowIdx == 1 || invCheckDetailRowIdx > 1){
                           jp.alert('只能选择一个物料！');
                           return;
                       }

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
	                   var checkQty=$("#"+itId+"_checkQty").val();
	                   var itemPrice=$("#"+itId+"_planPrice").val();
	                   var itemSum=$("#"+itId+"_planSum").val();
	                   var itemSerialNum=$("#"+itId+"_serialNum").val();
	                   

/*  	                   var detaildata = ${fns:toJson(invCheckMain.invCheckDetailList)};
	                   for(j = 0; j < detaildata.length; j++) {
	                   	if(detaildata[j].frontSerialNum == itemSerialNum){
		                   	 jp.warning("该物料已从计划生成至到货!");
		                   	 flag = 1;
		                   	 break;
	                   	 }
	                   }
	                   if(flag == 1){
		                   break;
	                   } */
	                   //该条计划信息隐藏
	                   $(namebox[i]).parent().parent().hide();
	                   //填充到到货数据
	                   //添加一行空白的
					   addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl);
					   //计划的数据填充到到货
	                   var preID="#invCheckDetailList"+invCheckDetailRowIdx;
	                   $(preID+"_itemId").val(itemId);//物料id
	                   $(preID+"_itemCode").val(itemCode);//物料编号
	                   $(preID+"_itemName").val(itemName);//物料名称
	                   $(preID+"_itemSpecmodel").val(itemModel);//物料规格
	                   $(preID+"_unitCode").val(unitName);
	                   $(preID+"_itemTexture").val(itemTexture);
	                   $(preID+"_itemId").val(itemId);
	                   $(preID+"_checkQty").val(checkQty);
	                   $(preID+"_realPrice").val(itemPrice);
	                   $(preID+"_realSum").val(itemSum);
	                   $(preID+"_frontSerialNum").val(itemSerialNum);
	                   $(preID+"_frontSerialNum").val(itemSerialNum);
	                   
	                   
	                   var planBillNum=$("#"+itId+"_billNum").val();
	                   var planSerialNum=$("#"+itId+"_serialNum").val();
	                   $(preID+"_frontBillNum").val(planBillNum);//计划主表编号
	                   $(preID+"_frontSerialNum").val(planSerialNum);//计划子表序号
	                   if(checkQty==null||checkQty=="") checkQty=0;
                       $(preID+"_checkQty").val(planQty);
	                   //$(preID+"_checkQty").val(planQty-1*checkQty); $(preID+"_maxQty").val(planQty-1*checkQty);
	                   //priceCalculate();
	                   namebox[i].checked = false;
	                   
	                   invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
                 }
			   }
			   printInvCheckDetailIdx();  
		}
		//打印当前子表数量
		function printInvCheckDetailIdx()
		{	
			console.log("invCheckDetailRowIdx:"+invCheckDetailRowIdx);
		}
		
		function contractAddToCheck(){
			var namebox = $("input[name^='conboxName']");
				for(i = 0; i < namebox.length; i++) {   
				   //已被选择
				   console.log(namebox[i].checked);
				   if(namebox[i].checked){
                       if(invCheckDetailRowIdx == 1 || invCheckDetailRowIdx > 1){
                           jp.alert('只能选择一个物料！');
                           return;
                       }

					   //获取合同数据
					   var boxId=namebox[i].id;
					       itId=boxId.split("_")[0];
					      
	                   var itemCode=$("#"+itId+"_itemCode").val();
	                   var itemId=$("#"+itId+"_itemId").val();
	                   var itemName=$("#"+itId+"_itemName").val();
	                   var itemModel=$("#"+itId+"_itemModel").val();	                  
	                   var measUnit=$("#"+itId+"_measUnit").val();
	                   var itemPrice=$("#"+itId+"_itemPrice").val();
	                   var conQty=$("#"+itId+"_itemQty").val();
	                   var itemSum=$("#"+itId+"_itemSum").val();
	                   var checkQty = $("#"+itId+"_checkQty").val();
	                   if(checkQty==null||checkQty=="") checkQty=0;
	                   var conBillNum=$("#"+itId+"_billNum").val();//合同主表编号
	                   var conSerialNum=$("#"+itId+"_serialNum").val();////合同子表序号
	                   
	                   //contractDetailRowIdx = contractDetailRowIdx - 1;
/*  	                   var detaildata = ${fns:toJson(invCheckMain.invCheckDetailList)};
	                   for(j = 0; j < detaildata.length; j++) {
	                   	if(detaildata[j].frontSerialNum == conSerialNum){
		                   	 jp.warning("该物料已从合同生成至到货!");
		                   	 flag = 1;
		                   	 break;
	                   	 }
	                   }
	                   if(flag == 1){
		                   break;
	                   }  */
	                   	//该条合同信息隐藏
	                   $(namebox[i]).parent().parent().hide(); 
                       //填充到到货数据
                       //添加一行空白的
					   addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl);
					   //合同的数据填充到到货
	                   var preID="#invCheckDetailList"+invCheckDetailRowIdx;
	                   $(preID+"_itemId").val(itemId);//物料id
	                   $(preID+"_itemCode").val(itemCode);//物料编号
	                   $(preID+"_itemName").val(itemName);//物料名称
	                   $(preID+"_itemSpecmodel").val(itemModel);//物料规格
	                   $(preID+"_unitCode").val(measUnit);//单位
	                   $(preID+"_realPrice").val(itemPrice);
	                   $(preID+"_realSum").val(itemSum);
	                   $(preID+"_frontBillNum").val(conBillNum);
	                   $(preID+"_frontSerialNum").val(conSerialNum);
                       $(preID+"_checkQty").val(conQty);
	                   //$(preID+"_checkQty").val(conQty-1*checkQty); $(preID+"_maxQty").val(conQty-1*checkQty);
	                   
	                   //priceCalculate();
	                   
	                   namebox[i].checked = false;
	                   findQmsFlag(itemCode,preID);
	                   
	                   invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
                 }
			   }
			   printInvCheckDetailIdx(); 
		}
		
		function changeSubmit(){
			var billState = "录入完毕";
			$("#billStateFlag").val(billState);
			
			var newUrl = "${ctx}/purinvcheckmain/invCheckMain/changeStateSave"
			$("#inputForm").attr('action', newUrl).submit();//修改表单action
		}
		
		
		function findQmsFlag(itemCode,preID){
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
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/purinvcheckmain/invCheckMain/listCreat"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="invCheckMain" action="${ctx}/purinvcheckmain/invCheckMain/saveCreat" method="post" onsubmit="return inputResult()" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="col-sm-3" style="display:none">
		    <form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/> 
			<form:hidden path="wareId" htmlEscape="false"  class="form-control"/> 
			<form:hidden path="supCode" htmlEscape="false"    class="form-control "/>
			<form:hidden path="buyerId" htmlEscape="false"  class="form-control"/> 
			<form:hidden path="planId" htmlEscape="false"    class="form-control "/>
			<form:hidden path="contractId" htmlEscape="false"    class="form-control "/>
			
			
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
						<sys:gridselect-pursup url="${ctx}/account/account/data" id="sup" name="sup" value="${invCheckMain.supId}" 
						labelName="account.accountCode" labelValue="${invCheckMain.supId}"
							 title="选择供应商编号" cssClass="form-control required" 
							 fieldLabels="供应商编码|供应商名称"  
							 targetId="supName|supAddress"  targetField="accountName|accountAddr" 
							 fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" 
							 searchKeys="accountCode|accountName" ></sys:gridselect-pursup>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
					<div class="col-sm-3">
						<form:input path="supName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>税率：</label>
					<div class="col-sm-3">
						<input id="taxRatio" name="taxRatioNew" htmlEscape="false"    class="form-control required" value="<fmt:formatNumber value="${invCheckMain.taxRatio}" maxIntegerDigits="4" type="percent"/>"/>
					</div>
				</div>
				
				<div class="form-group">
				    <label class="col-sm-2 control-label"><font color="red">*</font>接收仓库编码：</label>
					<div class="col-sm-3">
					<sys:gridselect-pursup url="${ctx}/purinvcheckmain/invCheckMain/waredata" id="ware" name="ware" value="${invCheckMain.wareId}" 
					         labelName="wareID" labelValue="${invCheckMain.wareId}"
							 title="选择仓库" cssClass="form-control required" fieldLabels="仓库编号|仓库名称" 
							 fieldKeys="wareID|wareName"
							 searchLabels="仓库编号|仓库名称" searchKeys="wareID|wareName" 
							 targetId="wareName" targetField = "wareName"
							 ></sys:gridselect-pursup>
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
					<sys:gridselect-pursupforInvCheck url="${ctx}/group/groupQuery/buyersdata" id="buyerId" name="buyerId.user.id" value="${invCheckMain.buyerId}" 
					   labelName=".user.no" labelValue="${invCheckMain.buyerId}"
							 title="选择采购员编号" cssClass="form-control required" 
							 fieldLabels="采购员编码|采购员名称" fieldKeys="user.no|buyername" 
							 searchLabels="采购员编码|采购员名称" searchKeys="user.no|buyername" 
							 targetId="buyerName|remarks" targetField = "buyername|id">
					</sys:gridselect-pursupforInvCheck>
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
					<sys:gridselect-pursupforparam url="${ctx}/purinvcheckmain/invCheckMain/databilltype" id="ioType" name="billType.ioType" value="${purInvCheckMain.billType.ioType}" 
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
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>来源：</label>
					<div class="col-sm-3">
						<form:select path="orderOrContract" class="form-control required">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('itemSource')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				    </form:select>
					</div>
				</div>
				<div id = "displayPlan" class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计划编号：</label>
					<div class="col-sm-3">
					<sys:gridselect-pursupforplanitem url="${ctx}/purinvcheckmain/invCheckMain/plandatabyGroupbuyer" param1="buyerId" param1value="buyerIdNames"  id="planId" name="plan" value="${invCheckMain.planId}" 
					labelName="billNum" labelValue="${invCheckMain.planId}"
							 title="选择计划" cssClass="form-control required" fieldLabels="计划号|计划类型名称" fieldKeys="billNum|planTypeName" 
							 searchLabels="计划号|计划类型名称" searchKeys="billNum|planTypeName"  
							 targetId="planTypeName" targetField="planTypeName" ></sys:gridselect-pursupforplanitem>
						
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>计划类别：</label>
					<div class="col-sm-3">
					<form:input path="planTypeName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div id = "displayContract" class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>合同编号：</label>
					<div class="col-sm-3">
					<sys:gridselect-pursupforitem url="${ctx}/purinvcheckmain/invCheckMain/databyGroupbuyer" param1="buyerId" param1value="buyerIdNames" id="contractId" name="contract" value="${invCheckMain.contractId}" 
					labelName="billNum" labelValue="${invCheckMain.contractId}"
							 title="选择合同" cssClass="form-control required" fieldLabels="合同号|合同类型名称|采购员|供应商编号|供应商名称" fieldKeys="billNum|contypeName|buyerName|account.accountCode|supName" 
							 searchLabels="合同号|合同类型名称" searchKeys="billNum|contypeName"  
							 targetId="contractName|supName" targetField="contypeName|supName" ></sys:gridselect-pursupforitem>
						
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>合同类别：</label>
					<div class="col-sm-3">
					<form:input path="contractName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				
		<div class="tabs-container">
                <ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">到货信息</a>
	                </li>
					<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false" >合同信息</a>
	                </li>
					<li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false" >计划信息</a>
	                </li>
                </ul>
            
            <div class="tab-content">
			<div id="tab-1" class="tab-pane fade in  active">
			
			<a class="btn btn-white btn-sm" onclick="deleteRowBacktoConorPlan()" title="删除"><i class="fa fa-minus"></i> 删除</a>
			
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>
					    	<input id="allboxs" onclick="allcheck()" type="checkbox"/>
						</th>
						<th><font color="red">*</font>物料编号</th>
						<th><font color="red">*</font>物料名称</th>
						<th><font color="red">*</font>物料规格</th>
						<th><font color="red">*</font>计量单位</th>
						<th><font color="red">*</font>到货数量</th>
						<th><font color="red">*</font>质检与否</th>
						<th>说明</th>
						
					</tr>
				</thead>
				<tbody id="invCheckDetailList">
				</tbody>
			</table>
			
			<script type="text/template" id="invCheckDetailTpl">//<!--
				<tr id="invCheckDetailList{{idx}}">
					<td class="hide">
						<input id="invCheckDetailList{{idx}}_id" name="invCheckDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="invCheckDetailList{{idx}}_delFlag" name="invCheckDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
                        <input id="invCheckDetailList{{idx}}_itemId" name="invCheckDetailList[{{idx}}].item.id" type="hidden" value="{{row.item.id}}"/>
					    <input id="invCheckDetailList{{idx}}_frontBillNum" name="invCheckDetailList[{{idx}}].frontBillNum" type="hidden" value="{{row.frontBillNum}}"/>
                        <input id="invCheckDetailList{{idx}}_frontSerialNum" name="invCheckDetailList[{{idx}}].frontSerialNum" type="hidden" value="{{row.frontSerialNum}}"/>
                        <input id="invCheckDetailList{{idx}}_frontQty" name="invCheckDetailList[{{idx}}].frontQty" type="hidden" value="{{row.frontQty}}"/>
						<input id="invCheckDetailList{{idx}}_maxQty" name="invCheckDetailList[{{idx}}].maxQty" type="hidden" value="{{row.maxQty}}"/>


						<input id="invCheckDetailList{{idx}}_realPrice" name="invCheckDetailList[{{idx}}].realPrice" type="text" value="{{row.realPrice}}"    class="form-control required" readonly="true"/>
						<input id="invCheckDetailList{{idx}}_realSum" name="invCheckDetailList[{{idx}}].realSum" type="text" value="{{row.realSum}}"    class="form-control " readonly="true"/>
						<input id="invCheckDetailList{{idx}}_taxRatio" name="invCheckDetailList[{{idx}}].taxRatio" type="text" value="{{row.taxRatio}}"    class="form-control required"  onchange="priceCalculate()"/>
						<input id="invCheckDetailList{{idx}}_realPriceTaxed" name="invCheckDetailList[{{idx}}].realPriceTaxed" type="text" value="{{row.realPriceTaxed}}"    class="form-control " readonly="true" />
						<input id="invCheckDetailList{{idx}}_realSumTaxed" name="invCheckDetailList[{{idx}}].realSumTaxed" type="text" value="{{row.realSumTaxed}}"    class="form-control " readonly="true"/>
						<input id="invCheckDetailList{{idx}}_transSum" name="invCheckDetailList[{{idx}}].transSum" type="text" value="{{row.transSum}}"    class="form-control " onchange="priceCalculate()"/>
						<input id="invCheckDetailList{{idx}}_transRatio" name="invCheckDetailList[{{idx}}].transRatio" type="text" value="{{row.transRatio}}"    class="form-control " onchange="priceCalculate()"/>
						<input id="invCheckDetailList{{idx}}_transSumTaxed" name="invCheckDetailList[{{idx}}].transSumTaxed" type="text" value="{{row.transSumTaxed}}"    class="form-control " readonly="true" />
						<input id="invCheckDetailList{{idx}}_taxSum" name="invCheckDetailList[{{idx}}].taxSum" type="text" value="{{row.taxSum}}"    class="form-control " readonly="true"/>

                    </td>

					<td>
                        <input type="checkbox" name="boxs" id="invCheckDetailList{{idx}}_checkbox" value="${row.id}"/>
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
						<input id="invCheckDetailList{{idx}}_checkQty" name="invCheckDetailList[{{idx}}].checkQty" type="text" value="{{row.checkQty}}"    class="form-control required"/>
					</td>

					<td>
						<input id="invCheckDetailList{{idx}}_qmsFlag" name="invCheckDetailList[{{idx}}].qmsFlag" type="text" value="{{row.qmsFlag}}"    class="form-control " readonly="true"/>
					</td>

					
					<td>
						<input id="invCheckDetailList{{idx}}_itemNotes" name="invCheckDetailList[{{idx}}].itemNotes" type="text" value="{{row.itemNotes}}"    class="form-control "/>
					</td>
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var invCheckDetailRowIdx = 0, invCheckDetailTpl = $("#invCheckDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                var rcheckQty = '';
				$(document).ready(function() {
				$("#billStateFlag").val("正在修改");
				$("#billType").val("生成");
				
				$("")
				
				if(($("#planIdNames").val()!= "")&&($("#planIdNames").val()!= null)){
				var plnId = $("#planIdNames").val();
					selectPlanOnlyShowDetail(plnId);
				}else if(($("#contractIdNames").val()!= "")&&($("#contractIdNames").val()!= null)){
					selectContractOnlyShowDetail();
				}
				var data = ${fns:toJson(invCheckMain.invCheckDetailList)};
				rcheckQty = data[0].checkQty;
				console.log(rcheckQty);
				for (var i=0; i<data.length; i++){
				console.log(data);
					addRow('#invCheckDetailList', invCheckDetailRowIdx, invCheckDetailTpl, data[i]);
					data[i].frontQty = data[i].checkQty;
					invCheckDetailRowIdx = invCheckDetailRowIdx + 1;
				}
				});
		var contractIdforCn = null;		
		function selectContractOnlyShowDetail(){
		contractIdforCn = $("#contractIdNames").val();
		$.ajax({
			url:"${ctx}/purinvcheckmain/invCheckMain/selectContract?contractId="+contractIdforCn,
			type: "GET",
			cache:false,
			dataType: "json",
			success:function(data){
				contractDetailData = data;
				contractDetailRowIdx = 0;
				contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				    var contractData=data.rows;
					for (var i=0; i<contractData.length; i++){
						if(contractData[i].checkQty == undefined || contractData[i].checkQty == null || contractData[i].checkQty == "") contractData[i].checkQty = 0;
						if(contractData[i].checkQty < contractData[i].itemQty){
							addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl, contractData[i]);
							contractDetailRowIdx = contractDetailRowIdx + 1;
						}
					}			
					
			}
		})
		}
		
		function selectPlanOnlyShowDetail(planId){
			var buyerId = $("#buyerIdNames").val();
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
						for (var i=0; i<planData.length; i++){
						if(planData[i].checkQty == undefined || planData[i].checkQty == null || planData[i].checkQty == "") planData[i].checkQty = 0;
						    if(planData[i].checkQty < planData[i].planQty){
								addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, planData[i]);
								purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
							}
						}			
				}
			});
		}
		
				
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
				
				function deleteRowBacktoConorPlan(){
                    var namebox = $("input[name^='boxs']");  //获取name值为boxs的所有input
                    for(i = 0; i < namebox.length; i++) {
                        if(namebox[i].checked){    //js操作选中checkbox                    
                            var boxId=namebox[i].id;
                            boxId=boxId.split("_")[0];
                            $("#"+boxId+"_delFlag").val(1);
                            //获得addToContract之后到货的billNum,即frontBillNum
                            var billNum = $("#"+boxId+"_frontBillNum").val();
                            var billtype = $("#"+boxId+"_frontBillNum").val();
                            var serialNum = $("#"+boxId+"_frontSerialNum").val();
                                                     
                            printInvCheckDetailIdx();
                            $(namebox[i]).parent().parent().remove();
                            invCheckDetailRowIdx = invCheckDetailRowIdx - 1;
                            
                            //根据frontBillNum来决定放回合同或计划
                            billtype = billtype.substring(0,3);
                            console.log(billtype);
                            if(billtype=="con"){
                                var boxName = $("input[name^='conboxName']");  //获取name值为boxs的所有input
 
					            for(var j = 0; j < boxName.length; j++) {
								   //获取合同数据
								   var boxId=boxName[j].id;
								   var itsId=boxId.split("_")[0];
								   //获得每次循环的合同编号和序号
			   	                   var conBillNum=$("#"+itsId+"_billNum").val();	                   
			                	   var conSerialNum=$("#"+itsId+"_serialNum").val();
			                	   	                   
								   if(serialNum==conSerialNum){
								   		$(boxName[j]).parent().parent().show();
								   		//减去金额
										priceCalculate();
								   	}
								 }		   
		                   }else{
		                       var boxName = $("input[name^='plnboxName']");  //获取name值为boxs的所有input
		                       
					            for(var j = 0; j < boxName.length; j++) {  
								   //获取计划数据
								   var boxId=boxName[j].id;
								   var itsId=boxId.split("_")[0];
								   //获得每次循环的计划编号和序号
			   	                   var planBillNum=$("#"+itsId+"_billNum").val();	                   
			                	   var planSerialNum=$("#"+itsId+"_serialNum").val();
			                	   	                   
								   if(serialNum==planSerialNum){
								   		$(boxName[j]).parent().parent().show();
								   		priceCalculate();
								   	}
									  
								 }        	
		                   }
                        }
                    }
                    
				}
				
				//判断输入到货量是否大于可用最大到货量
				function checkQtyCount(idx){
					var billn = $("#invCheckDetailList"+idx+"_frontBillNum").val();
					var maxQty = parseFloat($("#invCheckDetailList"+idx+"_maxQty").val());
					var currentQty = parseFloat($("#invCheckDetailList"+idx+"_checkQty").val());
					console.log(billn+","+maxQty+","+currentQty);
					if(currentQty > maxQty){
						jp.warning("当前最大可用到货量为"+maxQty+", 请重新输入合理到货量！");
						$("#invCheckDetailList"+idx+"_checkQty").val(maxQty);
					}else if(currentQty == "" || currentQty == 0){
						jp.warning("请输入有效到货量！");
					}else{
						priceCalculate();
						//$("#invCheckDetailList"+idx+"_frontQty").val(currentQty);
					}			
				}
					
				    var sumTotal = 0;
				    var transSumTotal = 0;
				    
					function priceCalculate(){
			        sumTotal = 0;
			        transSumTotal = 0;
					var index = invCheckDetailRowIdx;
					for(var j = 0; j<= index; j++){
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
					  if(tagtaxRatio == undefined || tagtaxRatio == "") tagtaxRatio = 0;
					  
					  tagcheckQty = $("#invCheckDetailList"+j+"_checkQty").val();//数量
					  if(tagcheckQty == undefined || tagcheckQty == "") tagcheckQty = 0;
					  
					  tagrealPrice = $("#invCheckDetailList"+j+"_realPrice").val();//不含税单价
					  if(tagrealPrice == undefined || tagrealPrice == "") tagrealPrice = 0;
					  
					  tagtransSum = $("#invCheckDetailList"+j+"_transSum").val();//不含税运费
					  if(tagtransSum == undefined || tagtransSum == "") tagtransSum = 0;
					  
					  tagtransRatio = $("#invCheckDetailList"+j+"_transRatio").val();//运费税率
					  if(tagtransRatio == undefined || tagtransRatio == "") tagtransRatio = 0;
					  
					  /*金额计算*/
					  tagrealSum = (1*tagcheckQty) * (1*tagrealPrice);//不含税金额
					  tagrealPriceTaxed = (1+(1*tagtaxRatio))* (1*tagrealPrice);//含税单价
					  
					  tagrealSumTaxed = (1*tagcheckQty)*(1*tagrealPriceTaxed);//含税金额
				      tagtransSumTaxed = (1+(1*tagtransRatio))*(1*tagtransSum);//含税运费
					  tagtaxSum = 1*tagtransSumTaxed - 1*tagtransSum + 1*tagrealSumTaxed - 1*tagrealSum;//税额
					  /*表单赋值*/
					  $("#invCheckDetailList"+j+"_realSum").val(tagrealSum.toFixed(2));
					  $("#invCheckDetailList"+j+"_realPriceTaxed").val(tagrealPriceTaxed.toFixed(2));
					  $("#invCheckDetailList"+j+"_realSumTaxed").val(tagrealSumTaxed.toFixed(2));
					  $("#invCheckDetailList"+j+"_transSumTaxed").val(tagtransSumTaxed.toFixed(2));					  
					  $("#invCheckDetailList"+j+"_taxSum").val(tagtaxSum.toFixed(2));
					  console.log(1*tagrealSumTaxed);
					  sumTotal = sumTotal + 1*tagrealSumTaxed;//小计金额
					  transSumTotal = transSumTotal + 1*tagtransSumTaxed;//运费合计
					}
					var s = transSumTotal + sumTotal;//金额合计
					$("#priceSumSubtotal").val(sumTotal.toFixed(2));
					$("#tranpriceSum").val(transSumTotal.toFixed(2));
					$("#priceSum").val(s.toFixed(2));
					
					}
					
			</script>
			</div>
			
			<div id="tab-2" class="tab-pane fade">
			<a class="btn btn-white btn-sm" onclick="contractAddToCheck()" title="加入到货"><i class="fa fa-plus"></i> 加入到货</a>
			 <table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>
					    	<input id="allboxName" onclick="allcheckBoxcon()" type="checkbox"/>
						</th>
						<th>序号</th>
						<th>合同单号</th>
						<th>物料编号</th>
						<th>物料名称</th>
						<th>物料规格</th>
						<th>计量单位</th>
						<th>合同数量</th>
						
					</tr>
				</thead>
				<tbody id="contractDetailList">
				</tbody>
			</table>
			
			<script type="text/template" id="contractDetailTpl">//<!--
				<tr id="contractDetailList{{idx}}">
					<td class="hide">
						<input id="contractDetailList{{idx}}_id" name="contractDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="contractDetailList{{idx}}_delFlag" name="contractDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
						<input id="contractDetailList{{idx}}_itemId" name="contractDetailList[{{idx}}].item.id" type="hidden" value="{{row.item.id}}"/>
						<input id="contractDetailList{{idx}}_checkQty" name="contractDetailList[{{idx}}].checkQty" type="hidden" value="{{row.checkQty}}"/>


						<input id="contractDetailList{{idx}}_itemPrice" name="contractDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}" readOnly="true" onchange="setItemPrice(this)"  class="form-control number required"/>
						<input id="contractDetailList{{idx}}_itemSum" name="contractDetailList[{{idx}}].itemSum"  type="text" value="{{row.itemSum}}"  readOnly="true"  class="form-control number required"/>
						
					</td>

					
					<td>
                        <input type="checkbox" name="conboxName" id="contractDetailList{{idx}}_checkbox" value="${row.id}"/>
                    </td>
                    <td>
						<input id="contractDetailList{{idx}}_serialNum" name="contractDetailList[{{idx}}].serialNum" readOnly="true" type="text" value="{{row.serialNum}}"    class="form-control "/>
					</td>
					<td>
						<input id="contractDetailList{{idx}}_billNum" name="contractDetailList[{{idx}}].contractMain.billNum" type="text" readOnly="true" value="{{row.contractMain.billNum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="contractDetailList{{idx}}_itemCode" name="contractDetailList[{{idx}}].item.code" type="text" value="{{row.item.code}}"   readOnly="true" class="form-control required"/>

                    </td>					
					
					<td>
						<input id="contractDetailList{{idx}}_itemName" name="contractDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"  readOnly="true"  class="form-control required"/>
					</td>

	                <td>
						<input id="contractDetailList{{idx}}_itemModel" name="contractDetailList[{{idx}}].itemModel" type="text" value="{{row.itemModel}}"  readOnly="true"  class="form-control required"/>
					</td>
					
					<td>
						<input id="contractDetailList{{idx}}_measUnit" name="contractDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"  readOnly="true"  class="form-control "/>
					</td>

					<td>
						<input id="contractDetailList{{idx}}_itemQty" name="contractDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}" readOnly="true" onchange="setSumValue(this)"   class="form-control number required"/>
					</td>
		
				</tr>//-->
			</script>
			</div>
			
			<script type="text/javascript">			  
				var contractDetailRowIdx = 0, contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
				});
			</script>
			
			
			<div id="tab-3" class="tab-pane fade">
			<a class="btn btn-white btn-sm" onclick="addToCheck()" title="加入到货"><i class="fa fa-plus"></i> 加入到货</a>
			 <table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>
					    	<input id="allplnboxName" onclick="allcheckBoxpln()" type="checkbox"/>
						</th>
						<th>序号</th>
						<th>计划单号</th>
						<th>物料编号</th>
						<th>物料名称</th>
						<th>物料规格</th>
						<th>计量单位</th>
						<th>到货数量</th>
						<th>计划数量</th>	
						<th>需求日期</th>					
						<th>需求数量</th>
						
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

										
					
						<input id="purPlanDetailList{{idx}}_planPrice" name="purPlanDetailList[{{idx}}].planPrice" readOnly="true" type="text" value="{{row.planPrice}}"    class="form-control "/>
						<input id="purPlanDetailList{{idx}}_planSum" name="purPlanDetailList[{{idx}}].planSum" readOnly="true" type="text" value="{{row.planSum}}"    class="form-control "/>
					
					</td>

					<td>
                        <input type="checkbox" name="plnboxName" id="purPlanDetailList{{idx}}_checkbox" value="${row.id}"/>
                    </td>
                    <td>
						<input id="purPlanDetailList{{idx}}_serialNum" name="purPlanDetailList[{{idx}}].serialNum" readOnly="true" type="text" value="{{row.serialNum}}"    class="form-control "/>
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_billNum" name="purPlanDetailList[{{idx}}].billNum.billNum" type="text" readOnly="true" value="{{row.billNum.billNum}}"    class="form-control "/>
					</td>
					
					
					<td>
                        <input id="purPlanDetailList{{idx}}_itemCode" name="purPlanDetailList[{{idx}}].itemCode.code" type="text" readOnly="true" value="{{row.itemCode.code}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_itemName" name="purPlanDetailList[{{idx}}].itemName" type="text" readOnly="true" value="{{row.itemName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_itemSpecmodel" name="purPlanDetailList[{{idx}}].itemSpecmodel"  readOnly="true" type="text" value="{{row.itemSpecmodel}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_unitName" name="purPlanDetailList[{{idx}}].unitName" readOnly="true" type="text" value="{{row.unitName}}"    class="form-control "/>
					</td>
					
					<td>
						<input id="purPlanDetailList{{idx}}_checkQty" name="purPlanDetailList[{{idx}}].checkQty" readOnly="true" type="text" value="{{row.checkQty}}"    class="form-control "/>
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_planQty" name="purPlanDetailList[{{idx}}].planQty" readOnly="true" type="text" value="{{row.planQty}}"    class="form-control "/>
					</td>
														
					<td>
		                 <input type='text' id="purPlanDetailList{{idx}}_requestDate"  name="purPlanDetailList[{{idx}}].requestDate" readOnly="true" class="form-control "  value="{{row.requestDate}}"/>		                   					            
					</td>																				
					<td>
						<input id="purPlanDetailList{{idx}}_purQty" name="purPlanDetailList[{{idx}}].purQty" readOnly="true" type="text" value="{{row.purQty}}"    class="form-control "/>
					</td>									
				</tr>//-->
			</script>
			
			<script type="text/javascript">			  
				var purPlanDetailRowIdx = 0, purPlanDetailTpl = $("#purPlanDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {

				});
			</script>
		</div>
		</div>
		<c:if test="${fns:hasPermission('purinvcheckmain:invCheckMain:edit') || isAdd}">
				<div class="col-lg-1"></div>
		        <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                     <input type ="submit" name="Submit" class="btn btn-primary btn-block btn-lg btn-parsley" value = "保存" data-loading-text="保存采购到货生成单中..."/>
		                 </div>
		             </div>
		        </div>
		        <div class="col-lg-1"></div>
		        <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                     <input type ="submit" name="Submit" class="btn btn-primary btn-block btn-lg btn-parsley" value = "提交" onclick="changeSubmit()" data-loading-text="提交采购到货生成单中..."/>
		                 </div>
		             </div>
		        </div>
		         <div class="col-lg-1"></div>
		        <div class="col-lg-2">
		        <div class="form-group text-center">
		                 <div>
		                      <a href= "${ctx}/purinvcheckmain/invCheckMain/listCreat" class="btn btn-primary btn-block btn-lg btn-parsley">返回</a>
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
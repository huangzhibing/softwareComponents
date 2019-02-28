<%@ page contentType="text/html;charset=UTF-8" %>

<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购合同管理管理</title>
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
    <%@include file="/webpage/include/treeview.jsp" %>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					//alert(currentDetailRow);
					if(currentDetailRow<=0){
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
				 //format: "YYYY-MM-DD"
	        	format: "YYYY-MM-DD HH:mm:ss"
			});
	        $('#endDate').datetimepicker({
				 //format: "YYYY-MM-DD"
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
		/*	$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
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
		}
		
		
		function saveForm(){
			//修改表单提交的action
			   // console.log($('#inputForm').serializeJSON());
			   // jp.loading("正在保存...");
		        $("#inputForm").attr('action','${ctx}/contractmain/contractMainCreate/save');    
		    	$("#inputForm").submit();
		/*	$.post("${ctx}/contractmain/contractMainCreate/save",$('#inputForm').serialize(),function(data){			
				//console.log(data);
				alert(data.body.id);
				$("#id").val(data.body.id);
				jp.success(data.msg);
			});
		*/	
		}
		
		function submitForm(){
			//修改表单提交的action
			   // jp.loading("正在提交...");
		        $("#inputForm").attr('action','${ctx}/contractmain/contractMainCreate/submit');    
		    	$("#inputForm").submit();		
		}
		//修改税率时修改所有合同子表的含税金额
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
			  //var  ratio=obj.value;
			  //     ratio=(ratio.split('%')[0])/100.00;
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
		
		
		/**
		//选择采购员时获取计划信息
		function selectPlan(buyerId){
			//alert("buyerIdnew:"+buyerId);
			//获取供应商id
	 	 	var accountId=$("#accountId").val();
			$.ajax({
				url:"${ctx}/contractmain/contractMainCreate/selectPlan?buyerId="+buyerId+"&accountId="+accountId,
				type: "GET",
				cache:false,
				dataType: "json",
				success:function(data){
					console.log(data);
				}
			});
		
			jp.get("${ctx}/contractmain/contractMainCreate/selectPlanNew", function(data){
				console.log(data);
       	  	});
		}
	 */
	 function selectPlan(buyerId){
		    var accountId=$("#accountId").val();
	    	if(accountId==""){
	    		jp.warning("请先选择供应商！");
	    		//删除采购员信息，先选择供应商后再填采购员信息
	    		$("#groupBuyerNames").val("");
	    		$("#buyerName").val("");
	    	}else {
	    		selectPlanNext(buyerId);
	    	}
	 }
		//选择采购员时获取计划信息
		function selectPlanNext(buyerId){
			//alert("buyerIdnew:"+buyerId);
			//获取供应商id
	 	 	var accountId=$("#accountId").val();
			$.ajax({
				url:"${ctx}/contractmain/contractMainCreate/selectPlan?buyerId="+buyerId+"&accountId="+accountId,
				type: "GET",
				cache:false,
				dataType: "json",
				success:function(data){
					console.log(data);
			   //合同信息处理
					var curBuyerId=$("#buyerId").val();//获取页面加载时最初的采购员Id
					if(curBuyerId==buyerId){//采购员选择框选中的是最初始的采购员时
					    //删除原来的采购员的合同信息
					    var namebox = $("input[name^='boxs']");  //获取name值为boxs的所有input
	                    for(var i = 0; i < namebox.length; i++) {                 
	                    	 var boxId=namebox[i].id;
                             boxId=boxId.split("_")[0];
                             var delFlag = $("#"+boxId+"_delFlag");
                             if(delFlag.val()=="0"){
                            	 currentDetailRow=currentDetailRow-1;
                             }
 	                    	$(namebox[i]).parent().parent().remove();
                        } 
					    //添加最初始页面加载时该采购员的合同信息数据
					    contractDetailRowIdx=0;
	                    for (var i=0; i<dataCon.length; i++){
							addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl, dataCon[i]);
							contractDetailRowIdx = contractDetailRowIdx + 1;
							//合同的信息的条数加1
							currentDetailRow=currentDetailRow+1;
						}
				    }else{//采购员选择框选中的不是最初始的采购员时
				    	 contractDetailRowIdx=0;
				    	//删除原来的采购员的合同信息
					    var namebox = $("input[name^='boxs']");  //获取name值为boxs的所有input
	                    for(var i = 0; i < namebox.length; i++) {                 
	                    	 var boxId=namebox[i].id;
                             boxId=boxId.split("_")[0];
                             var delFlag = $("#"+boxId+"_delFlag");
                             if(delFlag.val()=="0"){
                            	 currentDetailRow=currentDetailRow-1;
                             }
                        	 $(namebox[i]).parent().parent().remove();                       	                            
                        } 
				    }
					
					
			  //计划信息处理
				    //清除原有的计划信息
				    var namebox = $("input[name^='boxName']");  //获取name值为boxs的所有input
                    for(var i = 0; i < namebox.length; i++) {                      
                        $(namebox[i]).parent().parent().remove(); 
                        purPlanDetailRowIdx--;
                    }
                     //重新加载计划信息数据
					 purPlanData=data;
					 
					 purPlanDetailRowIdx = 0;
					 purPlanDetailTpl = $("#purPlanDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");					
						var planData=data.rows ;
						for (var i=0; i<planData.length; i++){
							planData[i].requestDate=planData[i].requestDate.split(" ")[0];
							//添加一条计划信息，后再判断该条计划信息显示与否
							addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, planData[i]);
							purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
							
							//合同量等于采购量则该条计划信息隐藏
							var conQty=planData[i].conQty;
							var planQty=planData[i].planQty;
							if(conQty==planQty){
								var tag=$("#purPlanDetailList"+i+"_id");
								$(tag).parent().parent().hide(); 
							}
						    //已生成合同信息的计划则不显示
							var planBillNum=planData[i].billNum.billNum;//获取该条计划信息的单号
							var planSerialNum=planData[i].serialNum;  //获取该计划信息的序号
							//alert(planBillNum);
							//alert(planSerialNum);
							//遍历该合同信息，判断该计划是否已生成合同
							var namebox1 = $("input[name^='boxs']");  //获取name值为boxs的所有input
		                    var k = 0;
		                    while(k<namebox1.length) {                   	 
	                              var boxId=namebox1[k].id;
	                              boxId=boxId.split("_")[0];
	                              //获取生成合同的原计划单号和序号
	                              var billNum = $("#"+boxId+"_planBillNum").val();
	                              var serialNum = $("#"+boxId+"_planSerialNum").val();
	                              var delFlag = $("#"+boxId+"_delFlag").val();//用于判断该合同子表是否删除状态
	                              if(billNum==planBillNum&&serialNum==planSerialNum&&delFlag=="0"){
	                            		 var tag=$("#purPlanDetailList"+i+"_id");
	 									 $(tag).parent().parent().hide(); 
	                            		 break;
	                              } 
	                         k++;
	                      }
						    
						}
				}
			});
		}
		
	    //点击计划加入合同按钮时执行
	    function planCreateContrast(){
	    	var condate=$("#condate").val();//签订日期
	    	var accountId=$("#accountId").val();
	    	if(accountId==""){
	    		jp.warning("请选择供应商！");
	    	}else if(condate==""){
	    		jp.warning("请输入签订日期！");
	    	}else{
	    		addContrast();
	    	}
	    }
	  
		//计划生成合同时执行函数
		function addContrast(){			
			   var namebox = $("input[name^='boxName']");  //获取name值为boxs的所有input
			  
			   for(var i = 0; i < namebox.length; i++) {   
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
	                   var planBillNum=$("#"+itId+"_billNum").val();	                   
	                   var planSerialNum=$("#"+itId+"_serialNum").val();	                   
	                   var conQty=$("#"+itId+"_conQty").val();
	                   var planPrice=$("#"+itId+"_planPrice").val();
	                   //该条计划信息隐藏
	                   $(namebox[i]).parent().parent().hide(); 
	                     //填充到合同数据
	                     //添加一行空白的
	                    //遍历合同信息，判断是否已经存在该记录，存在则不添加
	                    var flag=0;
	                    var namebox1 = $("input[name^='boxs']");  //获取name值为boxs的所有input
	                    var k = 0;
	                    while(k<namebox1.length) {                   	
                           //js操作选中checkbox    
                            var boxId=namebox1[k].id;
                                boxId=boxId.split("_")[0];
                            var billNum = $("#"+boxId+"_planBillNum").val();
                            var serialNum = $("#"+boxId+"_planSerialNum").val();
                            var delFlag = $("#"+boxId+"_delFlag").val();
                            var id = $("#"+boxId+"_id").val();
                            if(billNum==planBillNum&&serialNum==planSerialNum){
                            	//alert(delFlag);
                            	if(delFlag=="0"){
                            		flag=1;
                            		break;
                            	}else{//页面初始加载存储的合同信息（删除时其实是改delFlag标志位1再隐藏）
                            		 if(delFlag=="1"){//改隐藏为显示，重新修改数据
                            			flag=2;
                            			$("#"+boxId+"_delFlag").val(0);
                            			var frontItemQty=$("#"+boxId+"_frontItemQty").val();
                            			if(frontItemQty==undefined) frontItemQty=0;
                            			$("#"+boxId+"_itemQty").val(planQty-1*conQty+1*frontItemQty);
                            			//$("#"+boxId+"_itemQty").val(planQty-1*conQty);
                            //修改无税单价    			
                            			
                            			//获取供应商id
	 	 				    	var accountId=$("#accountId").val();
	 	 					   //获取物料ID
	 	 					   var itemId=$("#"+boxId+"_itemId").val();
	 	 					   var itemNum=planQty-1*conQty+1*frontItemQty;
	 	 					   var condate=$("#condate").val();//签订日期
	 	 					   $.ajax({
	 	 							//url:"${ctx}/contractmain/contractMainCreate/getItemPrice?itemId="+itemId+"&itemNum="+itemNum+"&accountId="+accountId,
	 	 							//type: "GET",
	 	 							url:"${ctx}/contractmain/contractMainCreate/getItemPrice",
							        type:"POST",
	 	 							cache:false,
	 	 							dataType: "json",
	 	 							success:function(data){
	 	 								//console.log(data);
	 	 								/**var itemSumValue=data;
	 	 								$("#"+boxId+"_itemPrice").val(itemSumValue); //修改无税单价
	 	 								 $("#"+boxId+"_itemSum").val(((planQty-1*conQty+1*frontItemQty)*itemSumValue).toFixed(2));////修改无税总额
	 	 								var taxRatio=$("#taxRatio").val(); //获取税率
		 	 								taxRatio=(taxRatio.split('%')[0])/100.00;
									       //修改含税单价
									       $("#"+boxId+"_itemPriceTaxed").val((itemSumValue*(1+1*taxRatio)).toFixed(2));
									       //修改含税金额
								           $("#"+boxId+"_itemSumTaxed").val((itemSumValue*(1+1*taxRatio)*(planQty-1*conQty+1*frontItemQty)).toFixed(2));
								       */
	 	 								var itemSumValue=Number(data.body.sumPrice);//获取含税总额	
	 								    var itemUnitPrice=Number(data.body.supPrice);//获取含税单价，供应商价格维护中的价格是含税单价	

	 									if(0==itemSumValue){	
	 										var info=data.body.info;
	 										if(info!=undefined){
	 											if(info=='lack'){
			 									    $("#"+boxId+"_itemPriceSpan").html("<font color='red'>供应商价格数据缺失</font>");
	 											}else{
		 											jp.warning(data.body.info);
			 									    $("#"+boxId+"_itemPriceSpan").html("<font color='red'>价格数据获取失败</font>");
		 										}
	 										}								
	 									    $("#"+boxId+"_itemSum").val(0.00);
	 									    $("#"+boxId+"_itemPrice").val("");
	 									    $("#"+boxId+"_itemPriceTaxed").val(0.00);
	 									    $("#"+boxId+"_itemSumTaxed").val(0.00);
	 									}else{
	 									    $("#itemPriceSpan").html("");
	 										//var itemSumValue=data;//获取无税总额
	 										//填写无税总额
	 										var taxRatio=$("#taxRatio").val(); //获取税率
		 	 								taxRatio=(taxRatio.split('%')[0])/100.00;
	 										$("#"+boxId+"_itemSum").val(((itemUnitPrice/(1+taxRatio))*itemNum).toFixed(2));
	 										//填写无税单价
//	 										var avgItemPrice=(itemSumValue/(1*itemNum)).toFixed(2);
	 									//	var avgItemPrice=itemUnitPrice;
	 										$("#"+boxId+"_itemPrice").val(((itemUnitPrice/(1+taxRatio))).toFixed(2));
	 										
		 	 								//填写含税平均价格
									      //  var avgItemPriceTaxed=(avgItemPrice*(1+taxRatio)).toFixed(2);
									        //修改含税单价
										 //   $("#"+boxId+"_itemPriceTaxed").val(avgItemPriceTaxed);
									        $("#"+boxId+"_itemPriceTaxed").val(itemUnitPrice);
										    //修改含税金额
									        $("#"+boxId+"_itemSumTaxed").val((itemSumValue).toFixed(2));
									     
	 									}
	 									 //计算含税总额，不含税总额、运费总额
		 	 							 calculateSumPrice();
	 	 							  }
	 	 							
	 	 						});  
                            			
                            			
                            	//	   $("#"+boxId+"_itemPrice").val(planPrice); //修改无税单价
                            		   //修改无税总额
								 //      $("#"+boxId+"_itemSum").val(((planQty-1*conQty+1*frontItemQty)*planPrice).toFixed(2));
								       //获取税率的值
								     //  var taxRatio=$("#taxRatioNames").val();
								       $("#"+boxId+"_measUnit").val(unitName);
								       //运费和要求为空
								       $("#"+boxId+"_transPrice").val("");
								       $("#"+boxId+"_transSum").val("");
								       $("#"+boxId+"_massRequire").val("");
								       
                            			$(namebox1[k]).parent().parent().show(); 
             	 	                   currentDetailRow=currentDetailRow+1;
                            		}
                            	}
                            } 
                            k++;
                        }
	                   // if(frontItemQty==undefined) frontItemQty=0;
	                    if(flag==0&&(planQty-1*conQty)>0){
	                    	addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl);
	 					   //合同信息的条数加1
	 	                   currentDetailRow=currentDetailRow+1;
	 	                   //计划的数据填充到合同
	 	                   var preID="#contractDetailList"+contractDetailRowIdx;
	 	                   $(preID+"_itemCode").val(itemCode);
	 	                   $(preID+"_itemName").val(itemName);
	 	                   $(preID+"_itemModel").val(itemModel);
	 	                   $(preID+"_measUnit").val(unitName);
	 	                   $(preID+"_itemTexture").val(itemTexture);
	 	                   $(preID+"_itemId").val(itemId);
	 	                   $(preID+"_itemQty").val(planQty-1*conQty);
	 	                   $(preID+"_planBillNum").val(planBillNum);
	 	                   $(preID+"_planSerialNum").val(planSerialNum);
	 	                 //  $(preID+"_itemPrice").val(planPrice);

	 	                   
	 	                  //获取供应商id
	 	 					var accountId=$("#accountId").val();
	 	 					   //获取物料ID
	 	 					   var itemId=$(preID+"_itemId").val();
	 	 					   var itemNum=planQty-1*conQty;//签订数量
	 	 					   var condate=$("#condate").val();//签订日期
	 	 					   $.ajax({
	 	 							//url:"${ctx}/contractmain/contractMainCreate/getItemPrice?itemId="+itemId+"&itemNum="+itemNum+"&accountId="+accountId,
	 	 							//type: "GET",
	 	 							url:"${ctx}/contractmain/contractMainCreate/getItemPrice",
							        type:"POST",
	 	 							cache:false,
	 	 							dataType: "json",
	 								data: { "itemId": itemId, "itemNum": itemNum, "accountId": accountId, "condate": condate },
	 	 							success:function(data){
	 	 								//console.log(data);
	 	 							/**	var itemSumValue=data;
	 	 							    //修改无税单价
	 	 								$(preID+"_itemPrice").val(itemSumValue.toFixed(2));
	 	 								//修改无税总额
	 	 								$(preID+"_itemSum").val(((planQty-1*conQty)*itemSumValue).toFixed(2));
	 	 								var taxRatio=$("#taxRatio").val(); //获取税率
	 	 								taxRatio=(taxRatio.split('%')[0])/100.00;
	 	 								//var itemPriceTaxedValue=$("#"+preTagId+"_itemPriceTaxed").val();
	 	 							   //修改含税单价
	 	 								$(preID+"_itemPriceTaxed").val((itemSumValue*(1+1*taxRatio)).toFixed(2));
	 	 						       //修改含税金额
	 	 					            $(preID+"_itemSumTaxed").val((itemSumValue*(1+1*taxRatio)*(planQty-1*conQty)).toFixed(2));
	 	 						     */
	 	 								var itemSumValue=Number(data.body.sumPrice);//获取含税总额	
	 								    var itemUnitPrice=Number(data.body.supPrice);//获取含税单价，供应商价格维护中的价格是含税单价	

	 									if(0==itemSumValue){	
	 										var info=data.body.info;
	 										if(info!=undefined){
	 	 										if(info=='lack'){
	 	  									        $(preID+"_itemPriceSpan").html("<font color='red'>供应商价格数据缺失</font>");
	 	 										}
	 	 										else{
	 	 											jp.warning(data.body.info);
	 	 	 									    $(preID+"_itemPriceSpan").html("<font color='red'>价格数据获取失败</font>");
	 	 										}
	 										}
	 									    $(preID+"_itemSum").val(0.00);
	 									    $(preID+"_itemPrice").val("");
	 									    $(preID+"_itemPriceTaxed").val(0.00);
	 									    $(preID+"_itemSumTaxed").val(0.00);
	 									}else{
	 									    $("#itemPriceSpan").html("");
	 										//var itemSumValue=data;//获取无税总额
	 										var taxRatio=$("#taxRatio").val(); //获取税率
		 	 								taxRatio=(taxRatio.split('%')[0])/100.00;
	 										//填写无税总额
	 										$(preID+"_itemSum").val(((itemUnitPrice/(1+taxRatio))*itemNum).toFixed(2));
	 										//填写无税单价
	 										var avgItemPrice=((itemUnitPrice/(1+taxRatio))).toFixed(2);
	 										$(preID+"_itemPrice").val(avgItemPrice);
	 										
		 	 								//填写含税平均价格
									      //  var avgItemPriceTaxed=(avgItemPrice*(1+taxRatio)).toFixed(2);
									        //修改含税单价
										 //   $(preID+"_itemPriceTaxed").val(avgItemPriceTaxed);
										    //修改含税金额
									      //  $(preID+"_itemSumTaxed").val((avgItemPriceTaxed*itemNum).toFixed(2));
		 	 								$(preID+"_itemSumTaxed").val(itemSumValue.toFixed(2));
		 	 								 //修改含税单价
										    $(preID+"_itemPriceTaxed").val(itemUnitPrice);
	 									}
	 									//计算含税总额，不含税总额、运费总额
		 								 calculateSumPrice();
	 	 							}
	 								 
	 	 						});  
	 	                   
	 	                   
	 	               /*    //修改无税总额
					       $(preID+"_itemSum").val(((planQty-1*conQty)*planPrice).toFixed(2));
					       //获取税率的值
					       var taxRatio=$("#taxRatioNames").val();
					       //修改含税单价
					       $(preID+"_itemPriceTaxed").val((planPrice*(1+1*taxRatio)).toFixed(2));
					       //修改含税金额
				           $(preID+"_itemSumTaxed").val((planPrice*(1+1*taxRatio)*(planQty-1*conQty)).toFixed(2));
	 	                */   
	 	                   contractDetailRowIdx = contractDetailRowIdx + 1;
	                    }
	                //恢复没有被选中状态
	                namebox[i].checked=false;   
                 }
			   }  
			   
			 //计算含税总额，不含税总额、运费总额
		//	 calculateSumPrice();
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
				<a class="panelButton" href="#"  onclick="history.go(-1)"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="contractMain" action="${ctx}/contractmain/contractMainCreate/submit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/>
			<form:hidden path="act.taskId"/>
			<form:hidden path="act.taskName"/>
			<form:hidden path="act.taskDefKey"/>
			<form:hidden path="act.procInsId"/>
			<form:hidden path="act.procDefId"/>
			<form:hidden id="flag" path="act.flag"/>		
		<input id="buyerId" name="buyerId" type="hidden" value="${contractMain.groupBuyer.id}"/>
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
							 title="选择供应商编号" cssClass="form-control required" fieldLabels="供应商编码|供应商名称"  targetId="supName|supAddress|accountTelNum|accountFaxNum|accountLinkMam|taxRatio"  targetField="accountName|accountAddr|telNum|faxNum|accountLinkMam|taxRatio" fieldKeys="accountName|accountAddr" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" ></sys:gridselect-pursup>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
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
					<label class="col-sm-2 control-label"><font color="red">*</font>税率：</label>
				     	
				    <div class="col-sm-3">
					    <%-- <sys:gridselect-purratio url="${ctx}/taxratio/taxRatio/data" id="taxRatio" name="taxRatio" value="${contractMain.taxRatio}" labelName="taxRatio" labelValue="${contractMain.taxRatio}"
							 title="选择税率" cssClass="form-control required" targetId="" targetField="" fieldLabels="税率编号|税率值" fieldKeys="taxratioId|taxRatio" searchLabels="税率编号|税率值" searchKeys="taxratioId|taxRatio" ></sys:gridselect-purratio>
						--%>
						 <input id="taxRatio" name="taxRatioNew" htmlEscape="false"   class="form-control required" onchange="setRatioNew(this)" value="<fmt:formatNumber value="${contractMain.taxRatio}" maxIntegerDigits="4" type="percent"/>" />			    
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
							<input id="contractNeedSum" name="contractNeedSum"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.contractNeedSum}" pattern="0.00"/>" />
					<%-- 	<form:input path="contractNeedSum" htmlEscape="false"    class="form-control  number"/>
					--%>
					</div>
					
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>签订日期：</label>
					<div class="col-sm-3">						
							<div class='input-group form_datetime' id='bdate'>
			                    <input type='text' id="condate"  name="bdate" class="form-control required"  value="<fmt:formatDate value="${contractMain.bdate}" pattern="yyyy-MM-dd"/>"/>
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
			<%-- 	<div class="form-group">
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
					<label class="col-sm-2 control-label">含税总额：</label>
					<div class="col-sm-3">
						<input id="goodsSumTaxed" name="goodsSumTaxed"  class="form-control  number" value="<fmt:formatNumber value="${contractMain.goodsSumTaxed}" pattern="#.00"/>" />
					</div>
					<label class="col-sm-2 control-label">不含税总额：</label>
					<div class="col-sm-3">
					    <input id="goodsSum" name="goodsSum"   class="form-control  number" value="<fmt:formatNumber value="${contractMain.goodsSum}" pattern="#.00"/>" />
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
					<label class="col-sm-2 control-label">审核意见：</label>
					<div class="col-sm-3">
						<form:textarea path="act.comment" class="form-control" rows="5" readonly="true" maxlength="2147483647"/>
					</div>
				</div>
		    </c:if>
		    --%>
		<div class="tabs-container">
           <%--  <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">采购合同子表：</a>
                </li>
            </ul
           --%>  
            <ul class="nav nav-tabs">
                
				<li class=""><a data-toggle="tab" href="#tab-1" aria-expanded="true">合同信息</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">合同说明</a>
                </li>
				<li class="active"><a data-toggle="tab" href="#tab-3" aria-expanded="false">计划信息</a>
                </li>
            </ul>
        <div class="tab-content">
				<div id="tab-1" class="tab-pane fade ">
		  <%-- 	<a class="btn btn-white btn-sm" onclick="addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl);contractDetailRowIdx = contractDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a> --%>
			    <a class="btn btn-white btn-sm" onclick="deleteRow()" title="删除"><i class="fa fa-minus"></i> 删除</a>
			<div class="table-responsive" style="max-height:500px;min-height: 400px">
			<table class="table table-striped table-bordered table-condensed"  style="min-width: 2490px">
				<thead>
					<tr>
						<th class="hide"></th>
						<th width="10px">
					    	<input id="allboxs" onclick="allcheck()" type="checkbox"/>
						</th>	
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
					    <input id="contractDetailList{{idx}}_itemId" name="contractDetailList[{{idx}}].item.id" type="hidden" value="{{row.item.id}}"/>
                    	<input id="contractDetailList{{idx}}_planBillNum" name="contractDetailList[{{idx}}].planBillNum" type="hidden" value="{{row.planBillNum}}"/>
                        <input id="contractDetailList{{idx}}_planSerialNum" name="contractDetailList[{{idx}}].planSerialNum" type="hidden" value="{{row.planSerialNum}}"/>
                        <input id="contractDetailList{{idx}}_frontItemQty" name="contractDetailList[{{idx}}].frontItemQty" type="hidden" value="{{row.frontItemQty}}"/>
                        <input id="contractDetailList{{idx}}_frontItemQty" name="contractDetailList[{{idx}}].frontItemQty" type="hidden" value="{{row.frontItemQty}}"/>
                    </td>
                    <td>
                        <input type="checkbox" name="boxs" id="contractDetailList{{idx}}_checkbox" value="${row.id}"/>
                    </td>
					
					<td>
						<input id="contractDetailList{{idx}}_itemCode" name="contractDetailList[{{idx}}].item.code" type="text" value="{{row.item.code}}" readonly="true"   class="form-control required"/>

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
						<input id="contractDetailList{{idx}}_itemSumTaxed" name="contractDetailList[{{idx}}].itemSumTaxed" type="text" value="{{row.itemSumTaxed}}" readOnly="true"   class="form-control number required"/>
					</td>
														
					<td>
						<input id="contractDetailList{{idx}}_measUnit" name="contractDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"  readonly="true"  class="form-control "/>
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
					
					
				</tr>//-->
			</script>
			<script type="text/javascript">
			  //用于记录合同信息的条数，验证合同信息不为空
		        var currentDetailRow=0;
				var contractDetailRowIdx =0, contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");				
				var dataCon = ${fns:toJson(contractMain.contractDetailList)};				
				$(document).ready(function() {
					var data = ${fns:toJson(contractMain.contractDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl, data[i]);
						contractDetailRowIdx = contractDetailRowIdx + 1;
						//合同的信息的条数加1
						currentDetailRow=currentDetailRow+1;
					}					
				});
				//通过合同量录入修改无税金额、含税金额、运费金额、计划中的合同量
				function setSumValue(obj){
					//获取当前点击所在的行的信息
					var preTagId=obj.id.split("_")[0];
					//获取无税单价
					var itemPriceId=preTagId+"_itemPrice";
					//修改无税总额
					var itemSumId=preTagId+"_itemSum";
					
				/*	//获取无税单价
					var itemSumValue=$("#"+itemPriceId).val();
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
				*/
				    //修改计划中的合同量时，需验证输入量是否超过合同剩余量
					 var planBillNum = $("#"+preTagId+"_planBillNum").val();//获取计划编号
                     var planSerialNum = $("#"+preTagId+"_planSerialNum").val();//获取计划序号
                     var namePlan = $("input[name^='boxName']");  //获取name值为boxs的所有input
                     var len=namePlan.length;
                     var k=0;
                     while(k<len){
                   	   var boxIdPlan=namePlan[k].id;
                   	    boxIdPlan=boxIdPlan.split("_")[0];
                           var billNum = $("#"+boxIdPlan+"_billNum").val();
                           var serialNum = $("#"+boxIdPlan+"_serialNum").val();
                           //找到与合同信息对应的计划
                           if(billNum==planBillNum&&serialNum==planSerialNum){
                        	   var frontItemQty=$("#"+preTagId+"_frontItemQty").val();
                        	   if(frontItemQty==undefined||frontItemQty==""){
                        		   frontItemQty=0;
                        	   }
                        	   var conQty=$("#"+boxIdPlan+"_conQty").val();//当前合同总量（不含本项）
                        	   var planQty=$("#"+boxIdPlan+"_planQty").val(); 
                        	   var curQty=planQty-1*conQty+1*frontItemQty;
                        	   if(obj.value>curQty){
           						  jp.warning("当前计划可签合同量剩余"+curQty);
                            	   obj.value=curQty;
                        	   }else{
                        		  // $("#"+boxIdPlan+"_conQty").val(constractQty);
                        	   }   
                       	}
                   	   k++;
                      }  
                     
                     
                   //获取供应商id
 					var accountId=$("#accountId").val();
 					   //获取物料ID
 					   var itemId=$("#"+preTagId+"_itemId").val();
 					   var itemNum=obj.value;
 					   var condate=$("#condate").val();//签订日期
 					   $.ajax({
 							//url:"${ctx}/contractmain/contractMainCreate/getItemPrice?itemId="+itemId+"&itemNum="+itemNum+"&accountId="+accountId,
 							url:"${ctx}/contractmain/contractMainCreate/getItemPrice",
							type:"POST",
 							//type: "GET",
 							cache:false,
 							dataType: "json",
							data: { "itemId": itemId, "itemNum": itemNum, "accountId": accountId, "condate": condate },
 							success:function(data){
 								//console.log(data);
 								//var itemSumValue=data;
 							/**	$("#"+itemPriceId).val(itemSumValue.toFixed(2));
 								//修改无税总额
 								$("#"+itemSumId).val((obj.value*itemSumValue).toFixed(2));
 								//获取含税单价
 								var taxRatio=$("#taxRatio").val(); //获取税率
 								taxRatio=(taxRatio.split('%')[0])/100.00;
 								//var itemPriceTaxedValue=$("#"+preTagId+"_itemPriceTaxed").val();
 								var itemPriceTaxedValue=(itemSumValue*(1+1*taxRatio)).toFixed(2);
 								$("#"+preTagId+"_itemPriceTaxed").val(itemPriceTaxedValue);
 								//修改含税金额
 								$("#"+preTagId+"_itemSumTaxed").val((itemPriceTaxedValue*obj.value).toFixed(2));
 							*/								   
 							    var itemSumValue=Number(data.body.sumPrice);//获取含税总额	
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
									    $("#"+itemSumId).val(0.00);
									    $("#"+itemPriceId).val("");
									    $("#"+preTagId+"_itemPriceTaxed").val(0.00);
									    $("#"+preTagId+"_itemSumTaxed").val(0.00);
									}else{
									    $("#itemPriceSpan").html("");
										//var itemSumValue=data;//获取含税总额
										var taxRatio=$("#taxRatio").val(); //获取税率
	 	 								taxRatio=(taxRatio.split('%')[0])/100.00;
										//填写含税总额
										$("#"+preTagId+"_itemSumTaxed").val(itemSumValue.toFixed(2));
										//填写含税单价
										//var avgItemPrice=(itemSumValue/(1*itemNum)).toFixed(2);
										$("#"+preTagId+"_itemPriceTaxed").val(itemUnitPrice);
										
	 	 								//填写含税平均价格
								       // var avgItemPriceTaxed=(avgItemPrice*(1+taxRatio)).toFixed(2);
								        //修改无税单价
									    $("#"+itemPriceId).val((itemUnitPrice/(1+taxRatio)).toFixed(2));
									    //修改无税金额
								        $("#"+itemSumId).val((itemUnitPrice/(1+taxRatio)).toFixed(2));									    	 	 								
									}
 								//获取运费单价
 								var transPriceValue=$("#"+preTagId+"_transPrice").val();
 								//修改运费金额
 								$("#"+preTagId+"_transSum").val((transPriceValue*obj.value).toFixed(2));
 								
 								//计算含税总额，不含税总额、运费总额
 			 					calculateSumPrice();
 							}
 						});  
 					   
 					
                     
				}
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
					var taxRatio=$("#taxRatio").val(); //获取税率
					taxRatio=(taxRatio.split('%')[0])/100.00;
					//修改含税单价
					$("#"+preTagId+"_itemPriceTaxed").val((obj.value*(1+1*taxRatio)).toFixed(2));
					//修改含税金额
				    $("#"+preTagId+"_itemSumTaxed").val((obj.value*(1+1*taxRatio)*itemQtyValue).toFixed(2));
				    //
				    
				  //计算含税总额，不含税总额、运费总额
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
					var taxRatio=$("#taxRatio").val(); //获取税率
					taxRatio=(taxRatio.split('%')[0])/100.00;
					//修改无税单价
					$("#"+preTagId+"_itemPrice").val((obj.value/(1+1*taxRatio)).toFixed(2));
					//修改无税金额
					$("#"+preTagId+"_itemSum").val(((obj.value/(1+1*taxRatio))*itemQtyValue).toFixed(2));
					
					//计算含税总额，不含税总额、运费总额
					calculateSumPrice();
				}
				//通过运费单价修改运费金额
				function setTransPrice(obj){
					//获取当前点击所在的行的信息
					var preTagId=obj.id.split("_")[0];
					//获取签订的合同量
					var itemQtyValue=$("#"+preTagId+"_itemQty").val();
					$("#"+preTagId+"_transSum").val((obj.value*itemQtyValue).toFixed(2));
					
					//计算含税总额，不含税总额、运费总额
					calculateSumPrice();
				}
				
				//点击选择框则全选
				function allcheck(){
					//建议用name属性值操作，因为id属性在每一个页面中默认是唯一的，而name属性则可以取到属性值相同的所有
					var nn = $("#allboxs").is(":checked"); //判断th中的checkbox是否被选中，如果被选中则nn为true，反之为false
	                 if(nn == true) {
	                     var namebox = $("input[name^='boxs']");  //获取name值为boxs的所有input
	                     for(var i = 0; i < namebox.length; i++) {
	                         namebox[i].checked=true;    //js操作选中checkbox
	                     }
	                 }
	                 if(nn == false) {
	                     var namebox = $("input[name^='boxs']");
	                     for(var i = 0; i < namebox.length; i++) {
	                         namebox[i].checked=false;
	                     }
	                 }
				}
				//点击选择框则全选
				function allcheckBox(){
					//建议用name属性值操作，因为id属性在每一个页面中默认是唯一的，而name属性则可以取到属性值相同的所有
					var nn = $("#allboxName").is(":checked"); //判断th中的checkbox是否被选中，如果被选中则nn为true，反之为false
	                 if(nn == true) {
	                     var namebox = $("input[name^='boxName']");  //获取name值为boxs的所有input
	                     for(var i = 0; i < namebox.length; i++) {
	                         namebox[i].checked=true;    //js操作选中checkbox
	                     }
	                 }
	                 if(nn == false) {
	                     var namebox = $("input[name^='boxName']");
	                     for(var i = 0; i < namebox.length; i++) {
	                         namebox[i].checked=false;
	                     }
	                 }
				}
				
				function deleteRow(){
                    var namebox = $("input[name^='boxs']");  //获取name值为boxs的所有input
                    for(var i = 0; i < namebox.length; i++) {                   	
                        if(namebox[i].checked){    //js操作选中checkbox                    
                            var boxId=namebox[i].id;
                                boxId=boxId.split("_")[0];
                                var id = $("#"+boxId+"_id");
                                var planBillNum = $("#"+boxId+"_planBillNum").val();
                                var planSerialNum = $("#"+boxId+"_planSerialNum").val();
                              //  alert("planBillNum:"+planBillNum);
                                if (id.val() == ""){
                    				currentDetailRow=currentDetailRow-1;   				
                    				//$("#"+boxId+"_delFlag").val(1);
                    				$(namebox[i]).parent().parent().remove();
                    			}else{
                    				currentDetailRow=currentDetailRow-1;
                    				$("#"+boxId+"_delFlag").val(1);
                                    $(namebox[i]).parent().parent().hide();
                    			}
                                //计划子表合同量减除
                                // var curItemQty=$("#"+boxId+"_itemQty").val();
                                  //获取该合同信息的计划来源
                               
                                  //显示该合同对应的计划
                                  var namePlan = $("input[name^='boxName']");  //获取name值为boxs的所有input
                                  var len=namePlan.length;
                                  var k=0;
                                  while(k<len){
                                	    var boxIdPlan=namePlan[k].id;
			                    	    boxIdPlan=boxIdPlan.split("_")[0];
		                                var billNum = $("#"+boxIdPlan+"_billNum").val();
		                                var serialNum = $("#"+boxIdPlan+"_serialNum").val();
		                               //  alert("billNum.val():"+billNum);
		                               // alert("serialNum.val():"+serialNum);
		                               //  alert("planSerialNum:"+planSerialNum);
		                                if(billNum==planBillNum&&serialNum==planSerialNum){
		                                	//alert("k2:"+k);
	                                		$(namePlan[k]).parent().parent().show();
	                                	}
                                	   k++;
                                   } 
                        //恢复没有被选中状态
              	        namebox[i].checked=false;  
                        }
                    }
                    
                  //计算含税总额，不含税总额、运费总额
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
						
			<div id="tab-3" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="planCreateContrast()" title="加入合同"><i class="fa fa-plus"></i> 加入合同</a>
		<div class="table-responsive" style="max-height:500px">
			<table class="table table-striped table-bordered table-condensed"  style="min-width: 1850px">
				<thead>
					<tr>
						<th class="hide"></th>
						<th width="10px">
					    	<input id="allboxName" onclick="allcheckBox()" type="checkbox"/>
						</th>
						<th width="180px">计划单号</th>
						<th width="150px">编号</th>
						<th width="150px">物料编码</th>
						<th width="250px">物料名称</th>
						<th width="250px">物料规格</th>
		                <th width="100px">单位</th>
						<th width="150px">采购数量</th>
						<th width="150px">单价</th>
						<th width="150px">金额</th>
						<th width="150px">需求日期</th>
						<th width="150px">需求数量</th>						
						<th width="10px">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="purPlanDetailList">
				</tbody>
			</table>
	     </div>
			 
			 
			<script type="text/template" id="purPlanDetailTpl">//<!--
				<tr id="purPlanDetailList{{idx}}">
					<td class="hide">
						<input id="purPlanDetailList{{idx}}_id" name="purPlanDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purPlanDetailList{{idx}}_delFlag" name="purPlanDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
						<input id="purPlanDetailList{{idx}}_itemTexture" name="purPlanDetailList[{{idx}}].itemTexture" type="text" readOnly="true" value="{{row.itemTexture}}"    class="form-control "/>									
                    	<input id="purPlanDetailList{{idx}}_conQty" name="purPlanDetailList[{{idx}}].conQty" type="hidden" value="{{row.conQty}}"/>
						<input id="purPlanDetailList{{idx}}_conQtyOrion" name="purPlanDetailList[{{idx}}].conQty" type="hidden" value="{{row.conQty}}"/>
                    </td>

					<td>
                        <input type="checkbox" name="boxName" id="purPlanDetailList{{idx}}_checkbox" value="${row.id}"/>
                    </td>
                    
					<td>
						<input id="purPlanDetailList{{idx}}_billNum" name="purPlanDetailList[{{idx}}].billNum.billNum" type="text" readOnly="true" value="{{row.billNum.billNum}}"    class="form-control "/>
					</td>
					
					<td>
						<input id="purPlanDetailList{{idx}}_serialNum" name="purPlanDetailList[{{idx}}].serialNum" readOnly="true" type="text" value="{{row.serialNum}}"    class="form-control "/>
					</td>
					<td>
                        <input id="purPlanDetailList{{idx}}_itemCode" name="purPlanDetailList[{{idx}}].itemCode.code" type="text" readOnly="true" value="{{row.itemCode.code}}"    class="form-control "/>
					</td>
					
					<td class="hide">
                        <input id="purPlanDetailList{{idx}}_itemId" name="purPlanDetailList[{{idx}}].itemCode.id" type="text" readOnly="true" value="{{row.itemCode.id}}"    class="form-control "/>
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
						<input id="purPlanDetailList{{idx}}_planQty" name="purPlanDetailList[{{idx}}].planQty" readOnly="true" type="text" value="{{row.planQty}}"    class="form-control "/>
					</td>
										
					<td>
						<input id="purPlanDetailList{{idx}}_planPrice" name="purPlanDetailList[{{idx}}].planPrice" readOnly="true" type="text" value="{{row.planPrice}}"    class="form-control "/>
					</td>
	
					<td>
						<input id="purPlanDetailList{{idx}}_planSum" name="purPlanDetailList[{{idx}}].planSum" readOnly="true" type="text" value="{{row.planSum}}"    class="form-control "/>
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
				var purPlanData = ${fns:toJson(contractMain.purPlanDetailList)};
				$(document).ready(function() {
				//  var buyerId=$("#groupBuyer.id").val();
				var data = ${fns:toJson(contractMain.purPlanDetailList)};
					for (var i=0; i<data.length; i++){
						data[i].requestDate=data[i].requestDate.split(" ")[0];
						addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl, data[i]);
						purPlanDetailRowIdx = purPlanDetailRowIdx + 1;
						//合同量等于采购量则隐藏
						var conQty=data[i].conQty;
						var planQty=data[i].planQty;
						if(conQty==planQty){
							var tag=$("#purPlanDetailList"+i+"_id");
							 $(tag).parent().parent().hide(); 
						}
					  //console.log(data[i]);
					  //已生成合同信息的计划则不显示
						var planBillNum=data[i].billNum.billNum;
					    var planSerialNum=data[i].serialNum;
					    var namebox1=dataCon;//获取合同数据
					    var k = 0;					   
                      	while(k<namebox1.length) {                   	                         
                          var billNum = namebox1[k].planBillNum; 
                          var serialNum =namebox1[k].planSerialNum;
                          if(billNum==planBillNum&&serialNum==planSerialNum){
                        	    var tag=$("#purPlanDetailList"+i+"_id");
									$(tag).parent().parent().hide(); 
                        		break;
                          } 
                          k++;
                    }
					}
				});
			</script>
			</div>
						
		</div>
		</div>
		<c:if test="${fns:hasPermission('contractmain:contractMain:edit') || isAdd}">
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
							<act:histoicFlow procInsId="${contractMain.act.procInsId}"/>
				</c:if>
		 
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
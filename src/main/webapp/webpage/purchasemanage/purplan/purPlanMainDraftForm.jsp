<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>

	<title>采购计划制定</title>
	<meta name="decorator" content="ani"/>
	<%@include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
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
            if($('#billStateFlag').val()!="B"){
                $("#remark").hide();
            }
            if("${isAdd}"=="true"){
                $("#myTab a:last").tab('show');//打开第二个tab
            }

	        
	        
		});
		
		function button_save(){
            if($('#billStateFlag').val()!="B") {
                $('#billStateFlag').val('A');
            }
			jp.loading("正在处理...");
			form=$('#inputForm');
			jp.post("${ctx}/common/chkCode",{tableName:"pur_planmain",fieldName:"bill_num",fieldValue:$('#billNum').val()},function(data){if("${isAdd}"==""){form.submit()}else{if(data=='true'){form.submit();}else{jp.warning("问题处理报告单编号已存在！");return false;}}});
			jp.close();
		}
		function button_submit(){
			$('#billStateFlag').val('W');
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
		   // console.log(obj);
			var listId= obj.id.split("_")[0];
			var priceId=listId+"_planPrice";
			var sumId=listId+"_planSum";
			var planNum=$(obj).val();
			$('#'+sumId).val($('#'+priceId).val()*planNum);
			numberLog.set(listId+"_log",planNum);
            //从价格维护模块读取物料价格
            $.ajax({
                url:"${ctx}/purplan/purPlanMain/getItemPrice?itemId="+$("#"+listId+"_itemCodeId").val()+"&itemNum="+planNum+"&accountId="+$("#supIdId").val(),
                type: "GET",
                cache:false,
                dataType: "json",
                success:function(data){
                    if(data!=null){
                        $('#'+priceId).val(data);
                        console.log("从价格维护读取test");



                        var itemSumValue=Number(data.body.sumPrice);//获取总价格

                        if(0==itemSumValue){
                            var info=data.body.info;
                            if(info!=undefined){
                                if(info=='lack'){
                                    $("#"+listId+"_itemPriceSpan").html("<font color='red'>供应商梯度价格数据缺失</font>");
                                }
                                else{
                                    jp.warning(data.body.info);
                                    $("#"+listId+"_itemPriceSpan").html("<font color='red'>价格数据获取失败</font>");
                                }
                            }
                            // console.log(data);
                            $("#"+listId+"_planQty").val("");
                            $("#"+sumId).val("");
                            $("#"+priceId).val("");
                        }else{
                            $("#"+listId+"_itemPriceSpan").html("");
                            //var itemSumValue=data;//获取总额
                            //填写总额
                            $("#"+sumId).val(itemSumValue.toFixed(2));
                            //填写平均单价
                            var avgItemPrice=(itemSumValue/(1*planNum)).toFixed(2);
                            // console.log(avgItemPrice);
                            $("#"+priceId).val(avgItemPrice);

                        }



                    }
                    //$('#'+sumId).val($('#'+priceId).val()*planNum);
                    numberLog.set(listId+"_log",planNum);
                }
            });
			
		}
		function priceSum(obj){
			var listId= obj.id.split("_")[0];
			var qtyId=listId+"_planQty";
			var sumId=listId+"_planSum";
			$('#'+sumId).val($('#'+qtyId).val()*$(obj).val());
			
		}

		//加入计划事件
		function addPlan(){
		    if($("#supIdId").val()==""){
		        jp.warning("该计划还没有选择供应商！");
                $("#supIdNames").focus();
		        return false;
			}
		    var rollData=$("#rollPlanNewTable").bootstrapTable('getSelections');
		   // console.log(rollData);
            var num=0;
		    for(var i=0;i<rollData.length;i++){
		        if(isContain(rollData[i].billNum,rollData[i].serialNum)){
		            continue;
                }
                var prefix="#purPlanDetailList"+purPlanDetailRowIdx+"_";
		        addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl);
                if(rollData[i].itemCode!==undefined) { //防止滚动计划 物料编码为空
                    $(prefix + "itemCode").val(rollData[i].itemCode.code);
                    $(prefix + "itemCodeId").val(rollData[i].itemCode.id);
                }
                $(prefix+"itemName").val(rollData[i].itemName);
                $(prefix+"itemSpecmodel").val(rollData[i].itemSpecmodel);
                $(prefix+"itemTexture").val(rollData[i].itemTexture);
                if(rollData[i].requestDate!=null) {
                    $(prefix + "requestDate").val(jp.dateFormat(rollData[i].requestDate, "yyyy-MM-dd"));
                }
                $(prefix+"purQty").val(rollData[i].applyQty);
                var qty=rollData[i].applyQty-rollData[i].invQty+rollData[i].safetyQty<0?0:rollData[i].applyQty-rollData[i].invQty+rollData[i].safetyQty;
                $(prefix+"planQty").val(qty);
                qtySum($(prefix+"planQty")[0]);
                //$(prefix+"planPrice").val(rollData[i].costPrice);

                //从价格维护模块读取物料价格
                /*$.ajax({
                    url:"${ctx}/purplan/purPlanMain/getItemPrice?itemId="+rollData[i].itemCode.id+"&itemNum="+qty+"&accountId="+$("#supIdId").val(),
                    type: "GET",
                    cache:false,
                    dataType: "json",
					async:false,
                    success:function(data){
						if(data!=null){
                            $(prefix+"planPrice").val(data);
                            console.log("从价格维护读取Test");
                        }else{
                            $(prefix+"planPrice").val(rollData[i].costPrice);
                        }
                        priceSum($(prefix+"planPrice")[0]);
						//alert(prefix);
                    }
                });*/

                //priceSum($(prefix+"planPrice")[0]);
                $(prefix+"unitName").val(rollData[i].unitName);
               // $(prefix+"planPrice").val(rollData[i].planPrice);
                // $(prefix+"planSum").val(rollData[i].planSum);
                $(prefix+"invQty").val(rollData[i].invQty);
                $(prefix+"applyBillNum").val(rollData[i].billNum);
                $(prefix+"applySerialNum").val(rollData[i].serialNum);
                $(prefix+"applyId").val(rollData[i].id);
                purPlanDetailRowIdx++;
                num++;
            }
            if(num>0){  //勾选了1条以上需求记录，加入后跳转计划明细tab
                jp.success("已将"+num+"条需求记录加入到计划中！");
                $("#myTab a:first").tab('show');//打开第一个Tab
                $("#rollPlanNewTable").bootstrapTable("refresh");



            }
		}

        //点击选择框则全选
        function allCheck(){
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

        function deleteAll(){
            var nameboxs = $("input[name^='boxs']");  //获取name值为boxs的所有input
            var num=0;
			for(var i=0;i<nameboxs.length;i++){
			    if(nameboxs[i].checked==true){  //选出选中的checkbox
                    var boxId=nameboxs[i].id;
                    var rowId=boxId.split("_")[0];
                    var $id=$("#"+rowId+"_id");
                    //alert($id.val());
                    //alert(rowId);
                    if ($id.val() == "") {
                        $(nameboxs[i]).parent().parent().remove();
                    }else {

                        $("#"+rowId+"_delFlag").val(1);
                        $(nameboxs[i]).parent().parent().hide();
                    }
                    num++;
				}
			}
			if(num>0){//勾选了1条以上要删除的计划 刷新boostrapTablle
			    jp.success("已将"+num+"条计划记录删除！");
			    $("#rollPlanNewTable").bootstrapTable("refresh");
            }
		}


		//判断是否已添加到计划明细里面
        function isContain(billNum,serialNum){
            var $nameboxs = $("input[name^='boxs']");
            //alert("target："+billNum+" "+serialNum);
            for(var i=0;i<$nameboxs.length;++i){
                var idPre=$nameboxs[i].id.split("_")[0];
                //alert($("#"+idPre+"_applySerialNum").val()+"  "+$("#"+idPre+"_applyBillNum").val());
                if(serialNum==$("#"+idPre+"_applySerialNum").val()&&$("#"+idPre+"_applyBillNum").val()==billNum){
                    //alert($($nameboxs[i]).parent().parent().css("display"));
                    if($($nameboxs[i]).parent().parent().css("display")!='none') {
                        return true;
                    }
                }
            }
            return false;
        }
		
		/** 
		 * 获取格式化后的当前日期时间
		 * @param date 
		 * @returns {String} 
		 */
        function formatDateToString(){

            date = new Date();

            var year = date.getFullYear();
            var month = date.getMonth()+1;
            var day = date.getDate();
            var hh=date.getHours();
            var mm=date.getMinutes();
            var ss=date.getSeconds();
            if(month<10) month = "0"+month;
            if(day<10) day = "0"+day;
            if(hh<10) hh = "0"+hh;
            if(mm<10) mm = "0"+mm;
            if(ss<10) ss = "0"+ss;
            return year+month+day+"_"+hh+":"+mm+":"+ss;
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
				<a class="panelButton" onclick="history.go(-1);"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<%--@elvariable id="purPlanMain" type="PurPlanMain"--%>
		<form:form id="inputForm" modelAttribute="purPlanMain" action="${ctx}/purplan/purPlanMainDraft/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="billStateFlag" />
		<form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/>
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
			                    <input type='text'  name="planDate" class="form-control required"  value="<fmt:formatDate value="${purPlanMain.planDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
						<form:hidden path="makeEmpid.id" value="${fns:getUser().id}" />
						<form:input path="makeEmpid.no" htmlEscape="false"  readonly="true" value="${fns:getUser().no}"    class="form-control "/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人名称：</label>
					<div class="col-sm-3">
						<form:input path="makeEmpname" htmlEscape="false"  readonly="true"  value="${fns:getUser().name}"  class="form-control "/>
					</div>
					
				</div>
				
				<div class="form-group">
					
					<label class="col-sm-2 control-label"><font color="red">*</font>计划类型编码：</label>
					<div class="col-sm-3">
						<sys:gridselect-pursup url="${ctx}/plantype/planType/data" id="planTypeCode" name="planTypeCode.id" value="${purPlanMain.planTypeCode.id}" labelName="planTypeCode.plantypeId" labelValue="${purPlanMain.planTypeCode.plantypeId}"
							 title="选择计划类型编码" cssClass="form-control required" fieldLabels="计划类型编码|计划类型名称" fieldKeys="plantypeId|plantypename" searchLabels="计划类型编码|计划类型名称" searchKeys="plantypeId|plantypename" targetId="planTypeName" targetField="plantypename" ></sys:gridselect-pursup>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>计划类型名称：</label>
					<div class="col-sm-3">
						<form:input path="planTypeName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>供应商编码：</label>
					<div class="col-sm-3">
						<sys:gridselect-pursup url="${ctx}/account/account/data" id="supId" name="supId.id" value="${purPlanMain.supId.id}" labelName="supId.accountCode" labelValue="${purPlanMain.supId.accountCode}"
							title="选择供应商编号" cssClass="form-control  required" fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" targetId="supName|taxRatio" targetField="accountName|taxRatio" ></sys:gridselect-pursup>

					</div>

					<label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
					<div class="col-sm-3">
						<form:input path="supName" htmlEscape="false"  class="form-control required"/>
					</div>

				</div>


			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>税率：</label>
				<div class="col-sm-3">
					<%--<form:input path="taxRatio" htmlEscape="false"  class="form-control required"/>--%>
						<input id="taxRatio" name="taxRatioNew" htmlEscape="false"    class="form-control required" onchange="setRatioNew(this)" value="<fmt:formatNumber value="${purPlanMain.taxRatio}" maxIntegerDigits="4" type="percent"/>" />
				</div>

				<label class="col-sm-2 control-label">单据说明：</label>
					<div class="col-sm-3">
						<form:textarea path="makeNotes" htmlEscape="false"  rows="3"   class="form-control "/>
					</div>

			</div>


			<div class="form-group">
				<div id="remark">
					<label class="col-sm-2 control-label">审核不通过说明：</label>
					<div class="col-sm-3">
						<form:textarea path="justifyRemark" htmlEscape="false" rows="4" maxlength="2147483647"  readonly="true"  class="form-control "/>
					</div>
				</div>

			</div>




		<div class="tabs-container" >
            <ul id="myTab" class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">计划明细：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">需求明细：</a>
				</li>
            </ul>
            <div class="tab-content">
         <div id="tab-1" class="tab-pane fade in  active"  >
			<a class="btn btn-white btn-sm" onclick="deleteAll()" title="删除"><i class="fa fa-minus"></i> 删除</a>
					<%--<a class="btn btn-white btn-sm" onclick="addRow('#purPlanDetailList', purPlanDetailRowIdx, purPlanDetailTpl);purPlanDetailRowIdx = purPlanDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
          <div class="table-responsive">
			<table class="table table-striped table-bordered table-condensed " style="min-width: 3000px"   >

				<thead>
					<tr>
						<th class="hide"></th>
						<th>
							<input id="allboxs" onclick="allCheck()" type="checkbox"/>
						</th>
					<%--	<th>序号</th>--%>
						<th><font color="red">*</font>物料编号</th>
						<th><font color="red">*</font>物料名称</th>
						<th><font color="red">*</font>物料规格</th>
						<th>材质</th>
						<th><font color="red">*</font>需求日期</th>
						<th><font color="red">*</font>需求数量</th>
                        <th><font color="red">*</font>采购数量</th>
						<th>单位</th>
						<th>数量修改历史</th>
						<th><font color="red">*</font>价格</th>
						<th>总额</th>
						<th>库存数量</th>
						<%--<th>移动平均价</th>--%>
						<%--<th>安全库存量</th>--%>
						<%--<th>采购员编码</th>--%>
						<%--<th>采购员名称</th>--%>
						<%--<th>供应商编号</th>--%>
						<%--<th>供应商名称</th>--%>
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
						<input id="purPlanDetailList{{idx}}_buyerId" name="purPlanDetailList[{{idx}}].buyerId.id" type="hidden" value="${fns:getUser().id}" />
						<input id="purPlanDetailList{{idx}}_buyerName" name="purPlanDetailList[{{idx}}].buyerName" type="hidden" value="${fns:getUser().name}"  />
						<input id="purPlanDetailList{{idx}}_applyBillNum" name="purPlanDetailList[{{idx}}].applyBillNum" type="hidden" value="{{row.applyBillNum}}"/>
						<input id="purPlanDetailList{{idx}}_itemCodeId" name="purPlanDetailList[{{idx}}].itemCode.id" type="hidden" value="{{row.itemCode.id}}"/>
						<input id="purPlanDetailList{{idx}}_applySerialNum" name="purPlanDetailList[{{idx}}].applySerialNum" type="hidden"  value="{{row.applySerialNum}}"/>
						<input id="purPlanDetailList{{idx}}_applyId" name="purPlanDetailList[{{idx}}].applyId" type="hidden" value="{{row.applyId}}"/>
					</td>
					<td>
                        <input type="checkbox" name="boxs" id="purPlanDetailList{{idx}}_checkbox" data-value=""/>
                    </td>

						<%--<input id="purPlanDetailList{{idx}}_serialNum" name="purPlanDetailList[{{idx}}].serialNum"  type="hidden" value="{{idx}}"  style="width:50px"   class="form-control number"/>--%>

					
					<td>
						<input id="purPlanDetailList{{idx}}_itemCode" name="purPlanDetailList[{{idx}}].itemCode.code" type="text" readonly="true" value="{{row.itemCode.code}}"    class="form-control required "/>
					</td>

					
					
					<td>
						<input id="purPlanDetailList{{idx}}_itemName" name="purPlanDetailList[{{idx}}].itemName" type="text" readonly="true" value="{{row.itemName}}"    class="form-control required "/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_itemSpecmodel" name="purPlanDetailList[{{idx}}].itemSpecmodel" readonly="true" type="text" value="{{row.itemSpecmodel}}"    class="form-control required"/>
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_itemTexture" name="purPlanDetailList[{{idx}}].itemTexture" type="text" readonly="true" value="{{row.itemTexture}}"    class="form-control "/>
					</td>

					<td>
		                <input type='text' id="purPlanDetailList{{idx}}_requestDate" name="purPlanDetailList[{{idx}}].requestDate" class="form-control form_datetime required" readonly="true" value="{{row.requestDate}}"/>
					</td>

                    <td>
						<input id="purPlanDetailList{{idx}}_purQty" name="purPlanDetailList[{{idx}}].purQty" type="text" value="{{row.purQty}}"  readonly='true'   class="form-control required number"/>
					</td>

					<td>
						<input id="purPlanDetailList{{idx}}_planQty" name="purPlanDetailList[{{idx}}].planQty" type="text" value="{{row.planQty}}" onchange="qtySum(this)"   class="form-control required number isFloatGtZero"/>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_unitName" name="purPlanDetailList[{{idx}}].unitName" type="text" readonly="true" value="{{row.unitName}}"    class="form-control "/>
					</td>
					
					<td>
						<input id="purPlanDetailList{{idx}}_log" name="purPlanDetailList[{{idx}}].log" type="text" value="{{row.log}}" readonly="true"   class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_planPrice" name="purPlanDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"  onchange="priceSum(this)"  class="form-control required number isFloatGteZero"/>
						<span id="purPlanDetailList{{idx}}_itemPriceSpan"></span>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_planSum" name="purPlanDetailList[{{idx}}].planSum" type="text" value="{{row.planSum}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_invQty" name="purPlanDetailList[{{idx}}].invQty" type="text" value="{{row.invQty}}"    class="form-control number"/>
					</td>
					



					<%--
					<td>
						<sys:gridselect-pursup-new url="${ctx}/group/groupQuery/buyersdata" id="purPlanDetailList{{idx}}_buyerId" name="purPlanDetailList[{{idx}}].buyerId.id" value="{{row.buyerId.id}}" labelName="purPlanDetailList{{idx}}.buyerId.user.no" labelValue="{{row.buyerId.user.no}}"
							 title="选择采购员编码" cssClass="form-control  " fieldLabels="采购员编码|采购员名称" fieldKeys="user.no|buyername" searchLabels="采购员编码|采购员名称" searchKeys="user.no|buyername" targetId="buyerName" targetField="buyername" ></sys:gridselect-pursup-new>
					</td>


					<td>
						<input id="purPlanDetailList{{idx}}_buyerName" name="purPlanDetailList[{{idx}}].buyerName" type="text" value="{{row.buyerName}}"    class="form-control "/>
					</td>


					
					<td>
						<sys:gridselect-pursup-new url="${ctx}/account/account/data" id="purPlanDetailList{{idx}}_supId" name="purPlanDetailList[{{idx}}].supId.id" value="{{row.supId.id}}" labelName="purPlanDetailList{{idx}}.supId.accountCode" labelValue="{{row.supId.accountCode}}"
							 title="选择供应商编号" cssClass="form-control  " fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" targetId="supName" targetField="accountName" ></sys:gridselect-pursup-new>
					</td>
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_supName" name="purPlanDetailList[{{idx}}].supName" type="text" value="{{row.supName}}"    class="form-control "/>
					</td>
					--%>
					
					
					
					<td>
						<input id="purPlanDetailList{{idx}}_notes" name="purPlanDetailList[{{idx}}].notes" type="text" value="{{row.notes}}"    class="form-control "/>
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
						<%--<a class="btn btn-white btn-sm" onclick="addRow('#rollPlanNewList', rollPlanNewRowIdx, rollPlanNewTpl);rollPlanNewRowIdx = rollPlanNewRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
					<div id="toolbar"><a class="btn btn-white btn-sm" onclick="addPlan()" title="加入计划"><i class="fa fa-plus"></i> 加入计划</a></div>
					<table id="rollPlanNewTable" data-toolbar="#toolbar"></table>

					<script type="text/javascript">



                        $(document).ready(function() {
                            /*	var data = ;
                                for (var i=0; i<data.length; i++){
                                    addRow('#rollPlanNewList', rollPlanNewRowIdx, rollPlanNewTpl, data[i]);
                                    rollPlanNewRowIdx = rollPlanNewRowIdx + 1;
                                }*/

                            $('#rollPlanNewTable').bootstrapTable({

                                //请求方法
                                method: 'get',
                                //类型json
                                dataType: "json",
                                //显示刷新按钮
                                showRefresh: true,
                                //显示切换手机试图按钮
                                showToggle: true,
                                //显示 内容列下拉框
                                showColumns: true,
                                // 不显示到处按钮
                                showExport: false,
                                //显示切换分页按钮
                                showPaginationSwitch: true,
                                //最低显示2行
                                minimumCountColumns: 2,
                                //是否显示行间隔色
                                striped: true,
                                //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                                cache: false,
                                //是否显示分页（*）
                                pagination: true,
                                //排序方式
                                sortOrder: "asc",
                                //初始化加载第一页，默认第一页
                                pageNumber:1,
                                //每页的记录行数（*）
                                pageSize: 10,
                                //可供选择的每页的行数（*）
                                pageList: [10, 25, 50, 100],
                                //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
                                url: "${ctx}/rollplannewquery/rollPlanNew/dataForPlan",
                                //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
                                //queryParamsType:'',
                                ////查询参数,每次调用是会带上这个参数，可自定义
                                queryParams : function(params) {
                                    var searchParam={};//= $("#searchForm").serializeJSON();
									searchParam.planNum='${purPlanMain.billNum}';
                                    searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
                                    searchParam.pageSize = params.limit === undefined? -1 : params.limit;
                                    searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                                    return searchParam;
                                },
                                //分页方式：client客户端分页，server服务端分页（*）
                                sidePagination: "server",
                                contextMenuTrigger:"right",//pc端 按右键弹出菜单
                                contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。


                                onClickRow: function(row, $el){
                                },
                                columns: [{
                                    checkbox: true,
                                    formatter:function(value, row , index){
                                        if(isContain(row.billNum,row.serialNum)){
                                           // alert(row.billNum+row.serialNum);
                                            return {checked:true,disabled:true}
                                        }
                                        //alert(row.billNum+row.serialNum);
                                    }

                                }

                                    ,{
                                        field: 'itemCode.code',
                                        title: '物料编号',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'itemName',
                                        title: '物料名称',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'applyQty',
                                        title: '需求数量',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'requestDate',
                                        title: '需求日期',
                                        sortable: true,
                                        formatter:function(value, row , index){
                                           if(value!=null) {
                                               return value.split(" ")[0];
                                           }else{
                                               return value;
										   }

                                        }

                                    }
                                    ,{
                                        field: 'applyDept',
                                        title: '需求部门',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'billNum',
                                        title: '单据编号',
                                        sortable: true
                                    }
                                    ,{
                                        field: 'billType',
                                        title: '单据类型',
                                        sortable: true

                                    }
                                    /*,{
                                        field: 'serialNum',
                                        title: '序号',
                                        sortable: true

                                    }*/
                                    ,{
                                        field: 'itemSpecmodel',
                                        title: '规格型号',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'unitName',
                                        title: '单位',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'planNum',
                                        title: '计划号',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'batchLt',
                                        title: '提前期',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'costPrice',
                                        title: '平均价',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'planPrice',
                                        title: '计划价',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'planSum',
                                        title: '总金额',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'applyQtyNotes',
                                        title: '需求说明',
                                        sortable: true

                                    }
                                    ,{
                                        field: 'sourseFlag',
                                        title: '来源',
                                        sortable: true,
                                        formatter:function(value, row , index){
                                            return jp.getDictLabel(${fns:toJson(fns:getDictList('rollplan_source_flag'))}, value, "-");
                                        }


                                    }
                                    ,{
                                        field: 'opFlag',
                                        title: '状态',
                                        sortable: true,
                                        formatter:function(value, row , index){
                                            return jp.getDictLabel(${fns:toJson(fns:getDictList('rollplan_op_flag'))}, value, "未加入计划");
                                        }

                                    }
                                    /*
                                                ,{
                                                    field: 'planArrivedate',
                                                    title: '计划到达日期',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'notes',
                                                    title: '说明',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'massRequire',
                                                    title: '质量要求',
                                                    sortable: true

                                                }

                                                ,{
                                                    field: 'purStartDate',
                                                    title: '采购开始日期',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'purArriveDate',
                                                    title: '计划到货日期',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'purQty',
                                                    title: '采购数量',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'invQty',
                                                    title: '库存数量',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'safetyQty',
                                                    title: '安全库存',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'realQty',
                                                    title: '实际数量',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'roadQty',
                                                    title: '在途量',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'makeDate',
                                                    title: '制单日期',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'applyDeptId.code',
                                                    title: '需求部门编码',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'applyDept',
                                                    title: '需求部门名称',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'makeEmpid.no',
                                                    title: '制单人代码',
                                                    sortable: true

                                                }
                                                ,{
                                                    field: 'makeEmpname',
                                                    title: '制单人名称',
                                                    sortable: true

                                                }

                                                */

                                ]
                            });/*boostrapTable end*/

                        });
					</script>
				</div>
		</div>
		</div>
		<br><br><br>
		<c:choose>
		<c:when test="${isEdit||isAdd}">
				<div class="col-lg-2"></div>
		    <%--    <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                     <input type="button" id="save_btn" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="button_save()" data-loading-text="正在保存..." value="保 存"></input>
		                 </div>
		             </div>
		        </div>--%>
		        <div class="col-lg-1"></div>
		        <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                     <input type="button" id="submit_btn" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="button_submit()" data-loading-text="正在提交..." value="提 交"></input>
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
		                     <a  class="btn btn-primary btn-block btn-lg btn-parsley"  onclick="history.go(-1)" data-loading-text="正在返回..." >返 回</a>
		                 </div>
		             </div>
		        </div>
		</c:otherwise>
		</c:choose>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
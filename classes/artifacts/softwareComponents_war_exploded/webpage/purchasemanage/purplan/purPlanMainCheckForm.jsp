<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>采购计划审核</title>
<meta name="decorator" content="ani" />
<script type="text/javascript">

//判读点击了那个按钮 通过/1 不通过/2
var whichBtn;
//保存数量修改历史
		var numberLog=new Map();//记录数量是否修改 用于记录数量历史修改字段；MAP数据结构为  『修改字段id，数量}
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
                    jp.loading("正在处理...");
                    //处理修改数量历史字段
                    numberLog.forEach(function (item, key, mapObj) {
                        var str=$("#"+key).val()+" "+"${fns:getUser().name}"+"-"+formatDateToString()+"-"+item;
                        $("#"+key).val(str);
                        //alert("#"+key);
                    });
                    //执行打开审核通过或不通过页面
				    if(whichBtn=="1"){
				        //alert("pass");
				        button_checkPass(form);
					}else if(whichBtn=="2"){
				       // alert("notpass");
				        button_checkFail(form);
					}


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

//按钮点击事件，提交到jquery表单验证处理
		function button_click(button) {
			if(button.value=="审核通过"){
			    whichBtn="1";
			}else if(button.value=="审核不通过"){
			    whichBtn="2";
			}
			$("#inputForm").submit();
        }
		
function button_checkPass(form){
	//按照设计文档，需等待审核意见输入完毕后才更改相关字段，保持事务一致性
	//$('#billStateFlag').val('E');
	var cancelFlag=1;
	//loading()函数阻塞主窗体，强制等待【审核意见输入窗体】返回
	jp.loading("正在处理...");
												//
	top.layer.open({type:2,title:"指定下一个审核人",content:"${ctx}/purplan/purPlanMainCheckComment/pass?purPlanMainCheckId=${purPlanMainCheck.id}",
		shadeClose:false,shade:false,
		area:['500px','200px'],
		cancel:function(index,layer){jp.close();},
		btn:['提交'],
		yes:function(index,layero){
		   iframeWin= layero.find('iframe')[0].contentWindow;
		   if(iframeWin.getMySelect()=='0'){
			   $('#billStateFlag').val('E');form.submit()
		   }else {
			   form.submit();
		   }
			top.layer.close(index);
		}

		});
}


// 来自TQ,2018-5-14-16:43
function button_checkFail(form){
    //按照设计文档，需等待审核意见输入完毕后才更改相关字段，保持事务一致性

    //loading()函数阻塞主窗体，强制等待【审核意见输入窗体】返回
    jp.loading("正在处理……");
    //调用layer弹窗模块打开审核意见输入窗体，等待输入返回

    //获取子表项数据--form项数据
    var da=$("#inputForm").serializeJSON();

    top.layer.open({type:2,
        title:"审核意见",
        content:"${ctx}/purplan/purPlanMainCheckComment/unPass?purPlanMainCheckId=${purPlanMainCheck.id}",
        shadeClose:false,
        shade:false,
        area:['500px','370px'],

/*		这里的逻辑是：需要在父窗口监听弹窗（子窗口）中自定义按钮的ckick事件，难以直接做到，
	    由于子窗口里【提交按钮】和【右上角的关闭按钮】都会执行colse方法，进而触发end函数，导致窗体关闭，
        因此可以在end（）函数里进行判断到底子窗口中点击了哪个按钮，如果是按钮触发end函数，则提交子表，否则不进行任何处理*/

        btn:['提交'],
        yes:function(index,layero){
            iframeWin= layero.find('iframe')[0].contentWindow;
            if(iframeWin.getMySelect()=='origin'){
                $('#billStateFlag').val('B');form.submit();
            }else {
                form.submit();
            }1
            top.layer.close(index);

        },

        //cancel()方法，右上角关闭按钮触发的回调
        cancel:function(index,layero){
            jp.close();
        }
    });
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
				    addRow('#purPlanDetailCheckList', purPlanDetailCheckRowIdx, purPlanDetailCheckTpl);
				    addRowModify('#purPlanDetailCheckList', purPlanDetailCheckRowIdx, purPlanDetailCheckTpl, items[i],obj,targetField,targetId,nam,labNam);		
				    purPlanDetailCheckRowIdx = purPlanDetailCheckRowIdx + 1;
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
.bootstrap-datetimepicker-widget {
	position: absolute;
}
</style>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary">
					<div class="panel-heading">
						<h3 class="panel-title">
							<a class="panelButton" href="${ctx}/purplan/purPlanMainCheck"><i
								class="ti-angle-left"></i> 返回</a>
						</h3>
					</div>
					<div class="panel-body">
						<%--@elvariable id="purPlanMainCheck" type="com.hqu.modules.purchasemanage.purplan.entity.PurPlanMainCheck"--%>
						<form:form id="inputForm" modelAttribute="purPlanMainCheck"
							action="${ctx}/purplan/purPlanMainCheck/save" method="post"
							class="form-horizontal">
							<form:hidden path="id" />
							<form:hidden path="billStateFlag" />
							<sys:message content="${message}" />
							<div class="form-group">
								<label class="col-sm-2 control-label"><font color="red">*</font>采购计划编号：</label>
								<div class="col-sm-3">
									<form:input path="billNum" htmlEscape="false" disabled="true"
										value="${billNum}" class="form-control " />
								</div>

								<label class="col-sm-2 control-label"><font color="red">*</font>单据状态：</label>
								<div class="col-sm-3">
									<form:select path="billStateFlag" class="form-control "
										disabled="true">
										<form:option value="" label="" />
										<form:options items="${fns:getDictList('purplan_state_flag')}"
											itemLabel="label" itemValue="value" htmlEscape="false" />
									</form:select>
								</div>


							</div>

							<div class="form-group">

								<label class="col-sm-2 control-label"><font color="red">*</font>计划日期：</label>
								<div class="col-sm-3">
									<div class='input-group form_datetime' id='planDate'>
										<input type='text' name="planDate"
											class="form-control required" disabled="true"
											value="<fmt:formatDate value="${purPlanMainCheck.planDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
										<span class="input-group-addon"> <span
											class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>

								</div>

								<label class="col-sm-2 control-label"><font color="red">*</font>制单日期：</label>
								<div class="col-sm-3">
									<div class='input-group form_datetime' id='makeDate'>
										<input type='text' name="makeDate" class="form-control "
											cssClass="form-control required" disabled="true"
											value="<fmt:formatDate value="${purPlanMainCheck.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
										<span class="input-group-addon"> <span
											class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>

								</div>

							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><font color="red">*</font>制单人编号：</label>
								<div class="col-sm-3">
									<form:input path="makeEmpid.no" htmlEscape="false"
										 disabled="true"
										class="form-control " />
								</div>
								<label class="col-sm-2 control-label"><font color="red">*</font>制单人名称：</label>
								<div class="col-sm-3">
									<form:input path="makeEmpname" htmlEscape="false"
										disabled="true"
										class="form-control " />
								</div>

							</div>

							<div class="form-group">

								<label class="col-sm-2 control-label"><font color="red">*</font>计划类型编码：</label>
								<div class="col-sm-3">
									<fieldset disabled="true">
										<sys:gridselect-pursup url="${ctx}/plantype/planType/data"
											id="planTypeCode" name="planTypeCode.id"
											value="${purPlanMainCheck.planTypeCode.id}"
											labelName="planTypeCode.plantypeId"
											labelValue="${purPlanMainCheck.planTypeCode.plantypeId}"
											title="选择计划类型编码" cssClass="form-control required"
											fieldLabels="计划类型编码|计划类型名称"
											fieldKeys="plantypeId|plantypename"
											searchLabels="计划类型编码|计划类型名称"
											searchKeys="plantypeId|plantypename" targetId="planTypeName"
											targetField="plantypename" disabled="true"></sys:gridselect-pursup>
									</fieldset>
								</div>
								<label class="col-sm-2 control-label"><font color="red">*</font>计划类型名称：</label>
								<div class="col-sm-3">
									<form:input path="planTypeName" htmlEscape="false"
										class="form-control required" disabled="true" />
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label">单据说明：</label>
								<div class="col-sm-8">
									<form:input path="makeNotes" htmlEscape="false"
										class="form-control " disabled="true" />
								</div>

							</div>
							<div class="tabs-container">
								<ul class="nav nav-tabs">
									<li class="active"><a data-toggle="tab" href="#tab-1"
										aria-expanded="true">采购计划子表：</a></li>
								</ul>
								<div class="tab-content">
									<div id="tab-1" class="tab-pane fade in  active">
										<div style="width: 100%; overflow-x: scroll;">
											<br> <br>
											<table
												class="table table-striped table-bordered table-condensed"
												style="min-width: 200%">
												<thead>
													<tr>
														<th class="hide"></th>
														<th>序号</th>
														<th><font color="red">*</font>物料编号</th>
														<th><font color="red">*</font>物料名称</th>
														<th><font color="red">*</font>物料规格</th>
														<th><font color="red">*</font>需求日期</th>
														<th><font color="red">*</font>数量</th>
														<th>单位</th>
														<th>数量修改历史</th>
														<th><font color="red">*</font>价格</th>
														<th>总额</th>
														<th>库存数量</th>
														<th>移动平均价</th>
														<th>安全库存量</th>
														<th>采购员编码</th>
														<th>采购员名称</th>
														<th>供应商编号</th>
														<th>供应商名称</th>
														<th>备注</th>

													</tr>
												</thead>
												<tbody id="purPlanDetailCheckList">
												</tbody>
											</table>
										</div>
										<script type="text/template" id="purPlanDetailCheckTpl">//<!--
				<tr id="purPlanDetailCheckList{{idx}}">
					<td class="hide">
						<input id="purPlanDetailCheckList{{idx}}_id" name="purPlanDetailCheckList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purPlanDetailCheckList{{idx}}_delFlag" name="purPlanDetailCheckList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_serialNum" name="purPlanDetailCheckList[{{idx}}].serialNum"  value="{{row.serialNum}}" disabled="true" type="text"   style="width:50px"   class="form-control number"/>
					</td>
					
					
					<td>
						<fieldset disabled="true">
						<sys:gridselect url="${ctx}/item/item/data" id="purPlanDetailCheckList{{idx}}_itemCode" name="purPlanDetailCheckList[{{idx}}].itemCode.id" value="{{row.itemCode.id}}" labelName="purPlanDetailCheckList{{idx}}.itemCode.code" labelValue="{{row.itemCode.code}}"
							 title="选择物料编号" cssClass="form-control  " fieldLabels="物料编号|物料名称" fieldKeys="code|name" searchLabels="物料编号|物料名称" searchKeys="code|name" ></sys:gridselect>
						 </fieldset>
					</td>
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_itemName" name="purPlanDetailCheckList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control required " disabled="true" />
					</td>
					
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_itemSpecmodel" name="purPlanDetailCheckList[{{idx}}].itemSpecmodel" type="text" value="{{row.itemSpecmodel}}" disabled="true"    class="form-control required"/>
					</td>
					
					<td>
						<div class='input-group form_datetime' id="purPlanDetailCheckList{{idx}}_requestDate">
		                    <input type='text'  disabled="true" name="purPlanDetailCheckList[{{idx}}].requestDate" class="form-control required"  value="{{row.requestDate}}"/>
		                </div>						            
					</td>

					<td>
						<input id="purPlanDetailCheckList{{idx}}_planQty" name="purPlanDetailCheckList[{{idx}}].planQty" type="text" value="{{row.planQty}}" onchange="qtySum(this)"   class="form-control required number" />
					</td>
					
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_unitName" name="purPlanDetailCheckList[{{idx}}].unitName" type="text" value="{{row.unitName}}"    class="form-control " disabled="true" />
					</td>
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_log" name="purPlanDetailCheckList[{{idx}}].log" type="text" value="{{row.log}}" readonly="true"   class="form-control " readonly="true" />
					</td>
					
					
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_planPrice" name="purPlanDetailCheckList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"  onchange="priceSum(this)"  disabled="true" class="form-control required number"/>
					</td>
					
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_planSum" name="purPlanDetailCheckList[{{idx}}].planSum" type="text" value="{{row.planSum}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_invQty" name="purPlanDetailCheckList[{{idx}}].invQty" type="text" value="{{row.invQty}}" disabled="true"    class="form-control number"/>
					</td>
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_costPrice" name="purPlanDetailCheckList[{{idx}}].costPrice" type="text" value="{{row.costPrice}}"   disabled="true" class="form-control "/>
					</td>

					<td>
						<input id="purPlanDetailCheckList{{idx}}_safetyQtys" name="purPlanDetailCheckList[{{idx}}].safetyQtys" type="text" value="{{row.safetyQtys}}"  disabled="true"  class="form-control number"/>
					</td>

					<td>
						<fieldset disabled="true">
						<sys:gridselect-pursup-new url="${ctx}/group/groupQuery/buyersdata" id="purPlanDetailCheckList{{idx}}_buyerId" name="purPlanDetailCheckList[{{idx}}].buyerId.id" value="{{row.buyerId.id}}" labelName="purPlanDetailCheckList{{idx}}.buyerId.user.no" labelValue="{{row.buyerId.user.no}}"
							 title="选择采购员编码" cssClass="form-control  " fieldLabels="采购员编码|采购员名称" fieldKeys="user.no|buyername" searchLabels="采购员编码|采购员名称" searchKeys="user.no|buyername" targetId="buyerName" targetField="buyername" ></sys:gridselect-pursup-new>
						</fieldset>
					</td>
					
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_buyerName" name="purPlanDetailCheckList[{{idx}}].buyerName" type="text" value="{{row.buyerName}}"  disabled="true"  class="form-control "/>
					</td>
					

					
					<td>
						<fieldset disabled="true">
						<sys:gridselect-pursup-new url="${ctx}/account/account/data" id="purPlanDetailCheckList{{idx}}_supId" name="purPlanDetailCheckList[{{idx}}].supId.id" value="{{row.supId.id}}" labelName="purPlanDetailCheckList{{idx}}.supId.accountCode" labelValue="{{row.supId.accountCode}}"
							 title="选择供应商编号" cssClass="form-control  " fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName" searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName" targetId="supName" targetField="accountName" ></sys:gridselect-pursup-new>
						</fieldset>
					</td>
					
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_supName" name="purPlanDetailCheckList[{{idx}}].supName" type="text" value="{{row.supName}}"  disabled="true"  class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="purPlanDetailCheckList{{idx}}_notes" name="purPlanDetailCheckList[{{idx}}].notes" type="text" value="{{row.notes}}"   disabled="true" class="form-control "/>
					</td>
					
					
					

				</tr>//-->
			</script>
										<script type="text/javascript">
				var purPlanDetailCheckRowIdx = 0, purPlanDetailCheckTpl = $("#purPlanDetailCheckTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					
					var data = ${fns:toJson(purPlanMainCheck.purPlanDetailCheckList)};
					for (var i=0; i<data.length; i++){
						addRow('#purPlanDetailCheckList', purPlanDetailCheckRowIdx, purPlanDetailCheckTpl, data[i]);
						purPlanDetailCheckRowIdx = purPlanDetailCheckRowIdx + 1;
					}
				});
			</script>
									</div>
								</div>
							</div>
							<br>
							<br>
							<br>
							<c:choose>
								<c:when
									test="${fns:hasPermission('purplan:purPlanMainCheck:edit')||isAdd}">
									<div class="col-lg-2"></div>
									<div class="col-lg-2">
										<div class="form-group text-center">
											<div>
												<input type="button" id="checkPass_btn"
													class="btn btn-success btn-block btn-lg btn-parsley"
													onclick="button_click(this)" data-loading-text="正在提交审核..."
													value="审核通过"></input>
											</div>
										</div>
									</div>
									<div class="col-lg-1"></div>
									<div class="col-lg-2">
										<div class="form-group text-center">
											<div>
												<input type="button" id="checkFail_btn"
													class="btn btn-danger btn-block btn-lg btn-parsley"
													onclick="button_click(this)"
													data-loading-text="正在提交提交审核..." value="审核不通过"></input>
											</div>
										</div>

									</div>
									<div class="col-lg-1"></div>
									<div class="col-lg-2">
										<div class="form-group text-center">
											<div>
												<a class="btn btn-primary btn-block btn-lg btn-parsley"
													href="${ctx}/purplan/purPlanMainCheck"
													data-loading-text="正在返回...">返回</a>
											</div>
										</div>
									</div>
								</c:when>
								<c:otherwise>
									<div class="col-lg-4"></div>

									<div class="col-lg-4">
										<div class="form-group text-center">
											<div>
												<a class="btn btn-primary btn-block btn-lg btn-parsley"
													href="${ctx}/purplan/purPlanMainCheck"
													data-loading-text="正在返回...">返 回</a>
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
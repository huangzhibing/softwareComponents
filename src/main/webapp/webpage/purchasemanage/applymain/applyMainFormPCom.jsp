<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购需求管理</title>
	<meta name="decorator" content="ani"/>
	<style type="text/css">
        
      
    </style>
	<script type="text/javascript">

		$(document).ready(function() {
			//var tab = $('.tab');
			//var td = tab.find('td');
			//if(td.length * 300 > 800){ // 300是td的平均值，800是table外div的宽度，如果大于这个就给table一个宽度
			//    tab[0].width = td.length * 300;
			//}
			
		//	$("#tableTab").css("width","500%");
			
		
			
		
			
			flag = 1;
			$("#inputForm").validate({
				submitHandler: function(form){
					if(flag==1){
						mysave();
						flag=1;
					}
					if(flag==2){
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
			
	        $('#makeDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#applyDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#periodId').datetimepicker({
				 format: "YYYY-MM-DD"
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
					 format: "YYYY-MM-DD",
					 widgetPositioning:{vertical:"bottom"}
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
					addRow('#applyDetailList', applyDetailRowIdx, applyDetailTpl);	
				    addRowModify('#applyDetailList', applyDetailRowIdx, applyDetailTpl, items[i],obj,targetField,targetId,nam,labNam);		
					applyDetailRowIdx = applyDetailRowIdx + 1;
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
		
		
	/*	
		function setValue(data){				
			for (var i=0; i<data.length; i++){
				addRow('#applyDetailList', applyDetailRowIdx, applyDetailTpl, data[i]);
				applyDetailRowIdx = applyDetailRowIdx + 1;
			}
		}
	*/	
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
		当前步骤--[${applyMain.act.taskName}]
		<form:form id="inputForm" modelAttribute="applyMain" action="${ctx}/applymain/applyMain/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<form:hidden path="processInstanceId"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
					<div class="col-sm-3">
						<form:input path="billNum" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>单据状态：</label>
					<div class="col-sm-3">
						<form:input path="billStateFlag" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制定日期：</label>
					<div class="col-sm-3">
						<form:input path="makeDate" htmlEscape="false" value="${fns:getMyDate()}" readonly="true"  class="form-control required"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>业务日期：</label>
					<div class="col-sm-3">
							<div class='input-group form_datetime' id='applyDate'>
			                    <input type='text'  name="applyDate" class="form-control required" readonly="true" value="<fmt:formatDate value="${applyMain.applyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>需求类别编码：</label>
					<div class="col-sm-3">
						<sys:gridselect-modify url="${ctx}/applytype/applyType/data" id="applyType" name="applyType.id" value="${applyMain.applyType.id}" labelName="applyType.applytypeid" labelValue="${applyMain.applyType.applytypeid}"
							 title="选择需求类别编码" 
							 targetId="applyName" targetField="applytypename" 
							 cssClass="form-control required" fieldLabels="需求类别编码|需求类别名称" fieldKeys="applytypeid|applytypename" searchLabels="需求类别编码|需求类别名称" searchKeys="applytypeid|applytypename" ></sys:gridselect-modify>
					</div>

					<label class="col-sm-2 control-label"><font color="red">*</font>需求类别名称：</label>
					<div class="col-sm-3">
						<form:input path="applyName" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>

				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>需求部门编码：</label>
					<div class="col-sm-3">
						<input type='text' id='code' name="office.code" class="form-control " readonly="true" value="${fns:getUserOfficeCode()}"/>
			         	
					</div>
					<!-- 
					<div class="col-sm-10">
						<sys:treeselect id="office" name="office.id" value="${applyMain.office.id}" labelName="office.code" labelValue="${applyMain.office.code}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</div>
					
					-->
					<label class="col-sm-2 control-label"><font color="red">*</font>需求部门名称：</label>
					<div class="col-sm-3">
						<form:input path="applyDept" htmlEscape="false" readonly="true" value="${fns:getUserOfficeName()}"  class="form-control required"/>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>需求人员编码：</label>
					<div class="col-sm-3">
						<input type='text' id='no' name="user.no" class="form-control " readonly="true" value="${fns:getUserOfficeCode()}"/>
						</div>
					<!--  
					<div class="col-sm-10">
						<sys:userselect id="user" name="user.id" value="${applyMain.user.id}" labelName="user.no" labelValue="${applyMain.user.no}"
							    cssClass="form-control required"/>
					</div>
					-->
					<label class="col-sm-2 control-label"><font color="red">*</font>需求人员名称：</label>
					<div class="col-sm-3">
						<form:input path="makeEmpname" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">需求说明：</label>
					<div class="col-sm-8">
						<form:input path="makeNotes" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">单据类型：</label>
					<div class="col-sm-10">
						<form:input path="billType" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">核算期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='periodId'>
			                    <input type='text'  name="periodId" class="form-control "  value="<fmt:formatDate value="${applyMain.periodId}" pattern="yyyy-MM-dd"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">来源标志：</label>
					<div class="col-sm-10">
						<form:input path="sourceFlag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">数量合计：</label>
					<div class="col-sm-10">
						<form:input path="applyQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">金额合计：</label>
					<div class="col-sm-10">
						<form:input path="applySum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:textarea path="applyNote" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">公司编码：</label>
					<div class="col-sm-10">
						<form:input path="fbrNo" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">登陆人所在部门：</label>
					<div class="col-sm-10">
						<form:input path="userDeptCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				
				<c:if test="${applyMain.auditOpinion!=''&&applyMain.auditOpinion!=null}">  
					<div class="form-group">
						<label class="col-sm-2 control-label">审核不通过意见：</label>
						<div class="col-sm-10">
							<form:textarea path="auditOpinion" htmlEscape="false" rows="4" readonly="true"   class="form-control "/>
						</div>
					</div>
				</c:if>
				
				
		<div style="width:100%;  overflow-x:scroll; ">	
		<br><br><br>
		<div  class="tabs-container">
		
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">采购需求单：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			
			
			
			<table  class="table table-striped table-bordered table-condensed" style="min-width:200%;">
			
				<thead>
					<tr>
						<th class="hide" ></th>
						<!-- <th>编号</th> -->
						<th><font color="red">*</font>物料编号</th>
						<th><font color="red">*</font>物料名称</th>
						<th>规格型号</th>
						<th><font color="red">*</font>需求日期</th>
						<th><font color="red">*</font>需求数量</th>
						<th>库存量</th>
						<th>单位</th>
						<th>移动平均价</th>
						<th>金额</th>
						<th>数量修改历史</th>
						<th>材质</th>
						<th>说明</th>
						
						
						
					</tr>
				</thead>
				<tbody id="applyDetailList">
				</tbody>
				
			</table>
			</div>
			<!-- 
			
						<th>需求价格</th>
						<th>计划价格</th>
						<th>计划金额</th>
						<th>物料图号</th>
						<th>计划到货日期</th>
			
			
			<td width="70px">
						<div class='input-group form_datetime' id="applyDetailList{{idx}}_planArrivedate">
		                    <input type='text'  name="applyDetailList[{{idx}}].planArrivedate" class="form-control "  value="{{row.planArrivedate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>
					
					
					
					
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_applyPrice" name="applyDetailList[{{idx}}].applyPrice" type="text" value="{{row.applyPrice}}"    class="form-control "/>
					</td>
					<td width="70px">
						<input id="applyDetailList{{idx}}_planPrice" name="applyDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"    class="form-control "/>
					</td>
					<td width="70px">
						<input id="applyDetailList{{idx}}_planSum" name="applyDetailList[{{idx}}].planSum" type="text" value="{{row.planSum}}"    class="form-control "/>
					</td>
					<td width="70px">
						<input id="applyDetailList{{idx}}_itemPdn" name="applyDetailList[{{idx}}].itemPdn" type="text" value="{{row.itemPdn}}"    class="form-control "/>
					</td>
			 -->
			<script type="text/template" id="applyDetailTpl">//<!--
				<tr id="applyDetailList{{idx}}">
					<td class="hide">
						<input id="applyDetailList{{idx}}_id" name="applyDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="applyDetailList{{idx}}_delFlag" name="applyDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td width="70px" style="display:none">
						<input id="applyDetailList{{idx}}_serialNum" name="applyDetailList[{{idx}}].serialNum" type="text" value="{{idx}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_serialNum1" name="applyDetailList{{idx}}.item.code" type="text" value="{{row.item.code}}" readonly="true"   class="form-control "/>
					</td>
					
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_itemName" name="applyDetailList[{{idx}}].itemName" type="text" readonly="true" value="{{row.itemName}}"    class="form-control "/>
					</td>
					
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_itemSpecmodel" name="applyDetailList[{{idx}}].itemSpecmodel" type="text" readonly="true" value="{{row.itemSpecmodel}}"    class="form-control "/>
					</td>
					
					
					<td width="70px">
						<div class='input-group form_datetime' id="applyDetailList{{idx}}_requestDate">
		                    <input type='text'  name="applyDetailList[{{idx}}].requestDate" class="form-control required " readonly="true" value="{{row.requestDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>	
					</td>
					
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_applyQty" name="applyDetailList[{{idx}}].applyQty" type="text" value="{{row.applyQty}}"  onchange="applyQtyChange(this)" readonly="true" class="form-control  required number isFloatGteZero"/>
					</td>
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_nowSum" name="applyDetailList[{{idx}}].nowSum" type="text" value="{{row.nowSum}}"  readonly="true"  class="form-control "/>
					</td>
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_unitName" name="applyDetailList[{{idx}}].unitName" type="text" value="{{row.unitName}}"  readonly="true"  class="form-control "/>
					</td>

					<td width="70px">
						<input id="applyDetailList{{idx}}_costPrice" name="applyDetailList[{{idx}}].costPrice" type="text" value="{{row.costPrice}}" readonly="true" onchange="costPriceChange(this)"  class="form-control "/>
					</td>

					<td width="70px">
						<input id="applyDetailList{{idx}}_applySum" name="applyDetailList[{{idx}}].applySum" type="text" value="{{row.applySum}}"  readonly="true"  class="form-control number"/>
					</td>
					
					<td width="70px">
						<input id="applyDetailList{{idx}}_log1" name="applyDetailList[{{idx}}].log" type="text" readonly="true"  value="{{row.log}}" class="form-control "/>
					</td>

					<td width="70px">
						<input id="applyDetailList{{idx}}_itemTexture" name="applyDetailList[{{idx}}].itemTexture" type="text" value="{{row.itemTexture}}"  readonly="true"  class="form-control "/>
					</td>

					<td width="70px">
						<input id="applyDetailList{{idx}}_notes" name="applyDetailList[{{idx}}].notes" type="text" value="{{row.notes}}"  readonly="true"  class="form-control "/>
					</td>

					<td width="70px" style="display:none">
						<input id="applyDetailList{{idx}}_changeNum" name="applyDetailList[{{idx}}].changeNum" type="text"     class="form-control "/>
					</td>





					
					
					
					
					
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var applyDetailRowIdx = 0, applyDetailTpl = $("#applyDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(applyMain.applyDetailList)};
					for (var i=0; i<data.length; i++){
						console.log(data[i]);
						addRow('#applyDetailList', applyDetailRowIdx, applyDetailTpl, data[i]);
						applyDetailRowIdx = applyDetailRowIdx + 1;
					}
					
				});
				function applyQtyChange(obj){
					var value = obj.value;
					var idType = obj.id;
					idType = idType.split("_")[0];
					var costId = idType+"_costPrice";
					//alert(costId);
					var costV = $("#"+costId).val();
					var applySumId = idType+"_applySum";
					$("#"+applySumId).val(""+(value*costV).toFixed(0));
					
					//历史相关
					var changeNumId = idType+"_changeNum";
					$("#"+changeNumId).val(value);
				}
				function costPriceChange(obj){
					var costV = obj.value;
					var idType = obj.id;
					idType = idType.split("_")[0];
					var qId = idType+"_applyQty";
					var qV = $("#"+qId).val();
					var applySumId = idType+"_applySum";
					$("#"+applySumId).val(""+(costV*qV));
				}
				
				
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('applymain:applyMain:edit') || isAdd}">
				
		        
		        <div class="col-lg-6"></div>
		        <div class="col-lg-1">
		             <div class="form-group text-center">
		                 <div>
		                 <!--  
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                 -->
		                 
		                    <!--  <input type="button" class="btn btn-primary" value="保存" onclick="mysave()"> &nbsp;-->
		                 
		                 </div>
		             </div>
		        </div>
		</c:if>
		
		<script type="text/javascript">
		function myclose(){
			//alert("close");
			window.location ="${ctx}/applymain/applyMain/list";
			
		}
		
		
		
		
		
		function changeFlag(){
			
			flag=2;
		}
		
		function sub(){
			//保存office的id
			//var cdeptCodeValue = $('input[name="office.id"]').val();
			//$("#cdeptCode").val(cdeptCodeValue);
			
			changeFlag();
			var newUrl = "${ctx}/applymain/applyMain/save";    //设置新提交地址
	        $("form").attr('action',newUrl);    //通过jquery为action属性赋值
	        //$("form").submit();    //提交ID为myform的表单
		}
		
		function changeFlag1(){
			flag=1;
			
		}
		
		function mysave(){
			//alert($("#invCheckMain").val());
			//保存office的id
			//var cdeptCodeValue = $('input[name="office.id"]').val();
			//$("#cdeptCode").val(cdeptCodeValue);
			
			//alert("aaa");
			//$("#inputForm").form('validate');
			
			
			
			
			
			
			jp.loading();
			$.ajax({
				type:'POST',
				datatype:'text',
				url:"${ctx}/applymain/applyMain/jax",
				data:$("form").serializeArray(),
				contentType:"application/x-www-form-urlencoded",
				success:function(data){
					top.layer.msg("保存成功", {icon: "${ctype=='success'? 1:1}"});
				}
				
			})
			
			
			
		}
		
		
	
		
		
		
		
		
		</script>
		 <br><br><br><br><br>
		    <act:flowChart procInsId="${applyMain.act.procInsId}"/>
			<act:histoicFlow procInsId="${applyMain.act.procInsId}"/>
		
		
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购需求审批管理</title>
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
			
	        $('#makeDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#applyDate').datetimepicker({
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
						addRow('#applyDetailAuditList', applyDetailAuditRowIdx, applyDetailAuditTpl);	
					    addRowModify('#applyDetailAuditList', applyDetailAuditRowIdx, applyDetailAuditTpl, items[i],obj,targetField,targetId,nam,labNam);		
						applyDetailAuditRowIdx = applyDetailAuditRowIdx + 1;
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
		当前步骤--[${applyMainAudit.act.taskName}]
		<form:form id="inputForm" modelAttribute="applyMainAudit" action="${ctx}/applymainaudit/applyMainAudit/save" method="post" class="form-horizontal">
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
							<div class='input-group form_datetime' id='makeDate'>
			                    <input type='text'  name="makeDate" class="form-control required" readonly="true"  value="<fmt:formatDate value="${applyMainAudit.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>业务日期：</label>
					<div class="col-sm-3">
							<div class='input-group form_datetime' id='applyDate'>
			                    <input type='text'  name="applyDate" class="form-control required"   value="<fmt:formatDate value="${applyMainAudit.applyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
					</div>
				</div>
				

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>需求类别编码：</label>
					<div class="col-sm-3">
						<sys:gridselect-modify url="${ctx}/applytype/applyType/data" id="applyType" name="applyType.id" value="${applyMainAudit.applyType.applytypeid}" labelName="applyType.applytypeid" labelValue="${applyMainAudit.applyType.applytypeid}"
							 title="选择需求类别编码" 
							 targetId="applyName" targetField="applytypename" 
							 cssClass="form-control required" fieldLabels="需求类别编码|需求类别名称" fieldKeys="applytypeid|applytypename" searchLabels="需求类别编码|需求类别名称" searchKeys="applytypeid|applytypename" ></sys:gridselect-modify>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>需求类别名称：</label>
					<div class="col-sm-3">
						<form:input path="applyName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>需求部门编码：</label>
					<div class="col-sm-3">
						<input id="office" name="office.code" value="${applyMainAudit.office.code}"  htmlEscape="false"  readonly="true" class="form-control required"/>
					<!-- 	<form:input path="office" htmlEscape="false"  readonly="true" value="${applyMainAudit.office.code}" class="form-control required"/>
					 
						<sys:treeselect id="office" name="office.id" value="${applyMainAudit.office.id}" labelName="office.code" labelValue="${applyMainAudit.office.code}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					-->
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>需求部门名称：</label>
					<div class="col-sm-3">
						<form:input path="applyDept" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>需求人员编码：</label>
					<div class="col-sm-3">
						<form:input path="user" htmlEscape="false" value="${applyMainAudit.user.no}" readonly="true"  class="form-control required"/>
						<!-- 
						<sys:userselect id="user" name="user.id" value="${applyMainAudit.user.id}" labelName="user.no" labelValue="${applyMainAudit.user.no}"
							    cssClass="form-control required"/>
						 -->
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>需求人员名称：</label>
					<div class="col-sm-3>
						<form:input path="makeEmpname" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label">需求说明：</label>
					<div class="col-sm-8">
						<form:input path="makeNotes" htmlEscape="false"   class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">登陆人所在部门：</label>
					<div class="col-sm-10">
						<form:input path="userDeptCode" htmlEscape="false"  readonly="true"   class="form-control "/>
					</div>
				</div>
				
			</div>
				
				
		 <div style="width:100%;  overflow-x:scroll; ">		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">采购需求单查询：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
				
			<a class="btn btn-white btn-sm" onclick="addRow('#applyDetailAuditList', applyDetailAuditRowIdx, applyDetailAuditTpl);applyDetailAuditRowIdx = applyDetailAuditRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed" style="min-width:200%;">
				<thead>
					<tr>
						<th class="hide"></th>
						<!--  <th>序号</th>-->
						<th>物料编号</th>
						<th>物料名称</th>
						<th>规格型号</th>
						<th>需求日期</th>
						<th>需求数量</th>
						<th>库存量</th>
						<th>计量单位</th>
						<th>移动平均价</th>
						<th>金额</th>
						<th>数量修改历史</th>
						<th>物料材质</th>
						<th>说明</th>
					</tr>
				</thead>
				<tbody id="applyDetailAuditList">
				</tbody>
			</table>
			<script type="text/template" id="applyDetailAuditTpl">//<!--
				<tr id="applyDetailAuditList{{idx}}">
					<td class="hide">
						<input id="applyDetailAuditList{{idx}}_id" name="applyDetailAuditList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="applyDetailAuditList{{idx}}_delFlag" name="applyDetailAuditList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td style="display:none">
						<input id="applyDetailAuditList{{idx}}_serialNum" name="applyDetailAuditList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"  readonly="true"   class="form-control "/>
					</td>
					
					
				
					




					<td >
						<sys:gridselect-purmul url="${ctx}/applymain/applyMain/myData" id="applyDetailAuditList{{idx}}_item" name="applyDetailAuditList[{{idx}}].item.id" value="{{row.item.id}}" labelName="applyDetailAuditList{{idx}}.item.code" labelValue="{{row.item.code}}"
							 title="选择物料编号" cssClass="form-control required" 
							targetId="itemName|itemSpecmodel|nowSum|costPrice|unitName|itemTexture" targetField="name|specModel|nowSum|costPrice|unit|texture" 
							isMultiSelected="true" fieldLabels="物料编号|物料名称" fieldKeys="code|name" searchLabels="物料编号|物料名称" searchKeys="code|name" ></sys:gridselect-purmul>
					</td>


					
					<td>
						<input id="applyDetailAuditList{{idx}}_itemName" name="applyDetailAuditList[{{idx}}].itemName" type="text" value="{{row.itemName}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailAuditList{{idx}}_itemSpecmodel" name="applyDetailAuditList[{{idx}}].itemSpecmodel" type="text" value="{{row.itemSpecmodel}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<div class='input-group form_datetime' id="applyDetailAuditList{{idx}}_requestDate">
		                    <input type='text'  name="applyDetailAuditList[{{idx}}].requestDate" class="form-control  "  readonly="true" value="{{row.requestDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>
					
					
					<td>
						<input id="applyDetailAuditList{{idx}}_applyQty" name="applyDetailAuditList[{{idx}}].applyQty" type="text" value="{{row.applyQty}}"  onchange="applyQtyChange(this)"  class="form-control  isIntGteZero"/>
					</td>
					
					
					<td>
						<input id="applyDetailAuditList{{idx}}_nowSum" name="applyDetailAuditList[{{idx}}].nowSum" type="text" value="{{row.nowSum}}"  readonly="true"  class="form-control number"/>
					</td>
					
					
					<td>
						<input id="applyDetailAuditList{{idx}}_unitName" name="applyDetailAuditList[{{idx}}].unitName" type="text" value="{{row.unitName}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailAuditList{{idx}}_costPrice" name="applyDetailAuditList[{{idx}}].costPrice" type="text" value="{{row.costPrice}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailAuditList{{idx}}_applySum" name="applyDetailAuditList[{{idx}}].applySum" type="text" value="{{row.applySum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailAuditList{{idx}}_log" name="applyDetailAuditList[{{idx}}].log1" type="text" value="{{row.log}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailAuditList{{idx}}_itemTexture" name="applyDetailAuditList[{{idx}}].itemTexture" type="text" value="{{row.itemTexture}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="applyDetailAuditList{{idx}}_notes" name="applyDetailAuditList[{{idx}}].notes" type="text" value="{{row.notes}}"   class="form-control "/>
					</td>
					
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var applyDetailAuditRowIdx = 0, applyDetailAuditTpl = $("#applyDetailAuditTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(applyMainAudit.applyDetailAuditList)};
					for (var i=0; i<data.length; i++){
						addRow('#applyDetailAuditList', applyDetailAuditRowIdx, applyDetailAuditTpl, data[i]);
						applyDetailAuditRowIdx = applyDetailAuditRowIdx + 1;
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
		</div>
		
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                    
		                	 <input id="btnSubmit" class="btn btn-primary" type="submit" value="提交" onclick="$('#flag').val('yes')"/>&nbsp;
							<input id="btnSubmit" class="btn btn-inverse" type="submit" value="放弃" onclick="$('#flag').val('no')"/>&nbsp;
							
		                 </div>
		             </div>
		        </div>
		        
		        <script type="text/javascript">
		        //关闭
		        function myclose(){
					//alert("close");
					window.location ="${ctx}/applymainaudit/applyMainAudit";
					
				}
				
				
				//保存
				function save(){
					//alert($("#invCheckMain").val());
					//保存office的id
					//var cdeptCodeValue = $('input[name="office.id"]').val();
					//$("#cdeptCode").val(cdeptCodeValue);
					
					
					$.ajax({
						type:'POST',
						datatype:'text',
						url:"${ctx}/applymainaudit/applyMainAudit/aajax",
						data:$("form").serializeArray(),
						contentType:"application/x-www-form-urlencoded",
						success:function(data){
							top.layer.msg("保存成功", {icon: "${ctype=='success'? 1:1}"});
						}
						
					})
					
					 
					
				}
			
				//审核通过
				function sub(){
					//保存office的id
					//var cdeptCodeValue = $('input[name="office.id"]').val();
					//$("#cdeptCode").val(cdeptCodeValue);
					$.ajax({
						type:'POST',
						datatype:'text',
						url:"${ctx}/applymainaudit/applyMainAudit/aajax",
						data:$("form").serializeArray(),
						contentType:"application/x-www-form-urlencoded",
						success:function(data){
							var value = $("#id").val();
							var yes="yes"
							var flag =1;
							//alert(value);
							jp.open({type:2,title:"审核意见",content:"${ctx}/applymainaudit/applyMainAudit/checkList?id="+value+"&yes="+yes,shadeClose:false,shade:false,area:['600px','360px']
							,end:function(){
								
								if(flag==1){
									
									$("#inputForm").submit();
								}
							}
							,cancel:function(index,layer){
								flag=0;
								jp.close()}});
				
						}
						
					})
					
				
					
					//var newUrl = "${ctx}/purreport/purReport/subsaveAsse";    //设置新提交地址
			       // $("form").attr('action',newUrl);    //通过jquery为action属性赋值
			        //$("form").submit();    //提交ID为myform的表单
				}
		        //审核不通过
				function sub1(){
					//保存office的id
					//var cdeptCodeValue = $('input[name="office.id"]').val();
					//$("#cdeptCode").val(cdeptCodeValue);
					
					$.ajax({
						type:'POST',
						datatype:'text',
						url:"${ctx}/applymainaudit/applyMainAudit/aajax",
						data:$("form").serializeArray(),
						contentType:"application/x-www-form-urlencoded",
						success:function(data){
							var value = $("#id").val();
							var yes="no"
							var flag =1;
							//alert(value);
							jp.open({type:2,title:"审核意见",content:"${ctx}/applymainaudit/applyMainAudit/checkList?id="+value+"&yes="+yes,shadeClose:false,shade:false,area:['600px','360px']
							,end:function(){
								if(flag==1){
									$("#inputForm").submit();
								}
							}
							,cancel:function(index,layer){
								flag=0;
								jp.close()}});
				
						}
						
					})
					
					
					//var newUrl = "${ctx}/purreport/purReport/subsaveAsse";    //设置新提交地址
			       // $("form").attr('action',newUrl);    //通过jquery为action属性赋值
			        //$("form").submit();    //提交ID为myform的表单
				}
		        
		        
		        </script>
		        <br><br><br><br><br>
		    <act:flowChart procInsId="${applyMainAudit.act.procInsId}"/>
			<act:histoicFlow procInsId="${applyMainAudit.act.procInsId}"/>
		
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
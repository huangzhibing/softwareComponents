<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			fg=1;
			$("#inputForm").validate({
				submitHandler: function(form){
					//jp.loading();
					//form.submit();
					if(fg==1){
						save();
					}
					if(fg==2){
						sub();
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
			
	        $('#checkDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#justifyDate').datetimepicker({
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/purreport/purReport/asseList"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="purReport" action="${ctx}/purreport/purReport/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}" />	
		
		<form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/>
				
				
				
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验结论</label>
					<div class="col-sm-10">
						<form:input path="checkResult" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				
		
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验单编号：</label>
					<div class="col-sm-10">
						<form:input path="reportId" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
			
				
				
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">检验类型名称：</label>
					<div class="col-sm-10">
						<form:input path="checktName" htmlEscape="false"  value="装配" readonly="true" class="form-control "/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">全检/抽检：</label>
					<div class="col-sm-10">
						<form:input path="rndFul" htmlEscape="false"  value="全检" readonly="true" class="form-control "/>
					</div>
					
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">执行标准：</label>
					<div class="col-sm-10">
					<form:select path="qualityNorm" class="form-control "  >
							<form:option value="" label=""/>
							<form:options items="${qualityNormList}" itemLabel="qnormname" itemValue="qnormname" htmlEscape="false"/>
					</form:select>
					
					<%-- 
						//原内容
						<form:input path="qualityNorm" htmlEscape="false"    class="form-control "/>
					--%>
					</div>
				</div>
				
				
				
				
				
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">检验部门编码：</label>
					<div class="col-sm-10">
						<form:input path="cdeptCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				
				
				
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">检验部门名称：</label>
					<div class="col-sm-10">
						<sys:treeselect id="office" name="office.id" value="${purReport.office.id}" labelName="office.name" labelValue="${purReport.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				
				
				<%--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! --%>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">检验岗位编码：</label>
					<div class="col-sm-10">
						<form:input path="cpositionCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				
				
				
				<div class="form-group" >
					<label class="col-sm-2 control-label">检验岗位名称：</label>
					<div class="col-sm-10">
					
					<form:select path="cpositionName" class="form-control "  onchange="getChange()">
							<form:option value="质检管理员" label="质检管理员"/>
							<form:options items="${roleList}" itemLabel="name" itemValue="name" htmlEscape="false"/>
					</form:select>
					
					
					<%-- 	<form:input path="cpositionName" htmlEscape="false"    class="form-control "/>--%>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:textarea path="memo" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
				
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">检验日期：</label>
					<div class="col-sm-10">
						
							
							<%-- 
			                   
			                 <input type='text'  name="checkDate" class="form-control " readonly="true" value="<fmt:formatDate value="${purReport.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                    --%>
			        <input type='text' id='checkDate1' name="checkDate" class="form-control " readonly="true" value="${fns:getMyDate()}"/>
			                 
			                					            
			           
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验时间：</label>
					<div class="col-sm-10">
						<form:input path="checkTime" htmlEscape="false" readonly="true" value="${fns:getMyTime()}"  class="form-control "/>
					</div>
				</div>
				
				
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">检验人代码 ：</label>
					<div class="col-sm-10">
						<form:input path="cpersonCode" htmlEscape="false"  value='${fns:getUser().no}' readonly="true" class="form-control "/>
					</div>
				</div>
				
				
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">检验人名称：</label>
					<div class="col-sm-10">
						<form:input path="checkPerson" htmlEscape="false"  value='${fns:getUser().name}' readonly="true" class="form-control "/>
					</div>
				</div>
				
				
				<c:if test="${purReport.isAudit!='未审核'}">  
				<div class="form-group" >
					<label class="col-sm-2 control-label">审核结论：</label>
					<div class="col-sm-10">
						<form:input path="justifyResult" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				
				<div class="form-group" >
					<label class="col-sm-2 control-label">审核意见：</label>
					<div class="col-sm-10">
						<form:textarea path="justifyRemark" htmlEscape="false" rows="4" readonly="true"   class="form-control "/>
					</div>
				</div>
				
				
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核日期：</label>
					<div class="col-sm-10">
					<form:input path="justifyDate" htmlEscape="false"  readonly="true"  class="form-control "/>
					
						
					
					</div>
				</div>
				
				
				<div class="form-group" >
					<label class="col-sm-2 control-label">审核日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='justifyDate'>
			                    <input type='text'  name="justifyDate" class="form-control " readonly="true" value="<fmt:formatDate value="${purReport.justifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				
				
				
				<div class="form-group" >
					<label class="col-sm-2 control-label">审核人：</label>
					<div class="col-sm-10">
						<form:input path="justifyPerson" htmlEscape="false"  readonly="true"   class="form-control "/>
					</div>
				</div>
				
				
				
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核部门名称：</label>
					<div class="col-sm-10">
						<sys:treeselect id="deptOffice" name="deptOffice.id" value="${purReport.deptOffice.id}" labelName="deptOffice.name" labelValue="${purReport.deptOffice.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核岗位名称：</label>
					<div class="col-sm-10">
						<form:input path="jpositionName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='justifyDate'>
			                    <input type='text'  name="justifyDate" class="form-control "  value="<fmt:formatDate value="${purReport.justifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				</c:if>
				<div class="form-group" >
					<div class="col-sm-10">
						<form:hidden path="isAudit"/>
					</div>
				</div>
				
				
				
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-10">
						<form:select path="state" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
		<div style="width:100%;  overflow-x:scroll; ">
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">检验物品清单：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#purReportRSnList', purReportRSnRowIdx, purReportRSnTpl);purReportRSnRowIdx = purReportRSnRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed" style="min-width:200%;">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>检验对象序列号</th>
						<th><font color="red">*</font>检验对象编码</th>
						<th><font color="red">*</font>检验对象名称</th>
						<th><font color="red">*</font>检验结论</th>
						<%-- <th>问题类型编码</th> --%>
						<th><font color="red">*</font>问题类型名称</th>
						<th>检验日期</th>
						<th>检验时间</th>
						<th>检验人名称</th>
						<th>检验标准名称</th>
						<th>备注</th>
						
						<th>审核历史</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				
			
				<tbody id="purReportRSnList">
				</tbody>
			</table>
			<script type="text/template" id="purReportRSnTpl">//<!--
				<tr id="purReportRSnList{{idx}}">
					<td class="hide">
						<input id="purReportRSnList{{idx}}_id" name="purReportRSnList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purReportRSnList{{idx}}_delFlag" name="purReportRSnList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td style="width: 120px;">
						<input id="purReportRSnList{{idx}}_objSn" name="purReportRSnList[{{idx}}].objSn" type="text" value="{{row.objSn}}"  onchange="changeNum(this)"  class="form-control required"/>
					</td>
					
					
					<td style="width: 100px;">

						<input id="purReportRSnList{{idx}}_objectDef" name="purReportRSnList[{{idx}}].objCode"  type="text" value="{{row.objCode}}"    class="form-control required"/>

					</td>
					
					
					<td style="width: 100px;">
						<input id="purReportRSnList{{idx}}_objName" name="purReportRSnList[{{idx}}].objName"  type="text" value="{{row.objName}}"    class="form-control required"/>
					</td>
					
					
					<td style="width: 25px;">
						<select id="purReportRSnList{{idx}}_checkResult" name="purReportRSnList[{{idx}}].checkResult" data-value="{{row.checkResult}}" class="form-control required  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('isQualified')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>



	                <td style="display:none"> 
						<input id="purReportRSnList{{idx}}_matterCode" name="purReportRSnList[{{idx}}].matterCode" type="text" value="{{row.matterCode}}"    class="form-control "/>
					</td>
					
					
					<td style="width: 100px;">
						<sys:gridselect-modify url="${ctx}/mattertype/matterType/data" id="purReportRSnList{{idx}}_matterType" name="purReportRSnList[{{idx}}].matterType.id" value="{{row.matterType.id}}" labelName="purReportRSnList{{idx}}.matterType.mattername" labelValue="{{row.matterType.mattername}}"
							 title="选择问题类型名称" 
 							targetId="purReportRSnList{{idx}}_matterCode" targetField="mattercode" 
							cssClass="form-control  required" fieldLabels="问题类型编码|问题类型名称" fieldKeys="mattercode|mattername" searchLabels="问题类型编码|问题类型名称" searchKeys="mattercode|mattername" ></sys:gridselect-modify>
					</td>





					
					
					<td style="width: 40px;">
						

						<input id="purReportRSnList[{{idx}}].checkDate" name="purReportRSnList[{{idx}}].checkDate" readonly="true" type="text" value="${fns:getMyDate()}"    class="form-control "/>					            
					</td>
					
					
					<td style="width: 40px;">
						<input id="purReportRSnList{{idx}}_checkTime" name="purReportRSnList[{{idx}}].checkTime" readonly="true" type="text" value="${fns:getMyTime()}"    class="form-control "/>
					</td>
					
					
					<td style="width: 40px;">
						<input id="purReportRSnList{{idx}}_checkPerson" name="purReportRSnList[{{idx}}].checkPerson" readonly="true" value='${fns:getUser().name}' type="text" value="{{row.checkPerson}}"    class="form-control "/>
					</td>
					
					
					<td style="width: 100px;">
						<sys:gridselect url="${ctx}/qmsqcnorm/qCNorm/data" id="purReportRSnList{{idx}}_qCNorm" name="purReportRSnList[{{idx}}].qCNorm.id" value="{{row.qCNorm.id}}" labelName="purReportRSnList{{idx}}.qCNorm.qcnormName" labelValue="{{row.qCNorm.qcnormName}}"
							 title="选择检验标准名称" cssClass="form-control  " fieldLabels="检验标准号|检验标准名称" fieldKeys="qcnormId|qcnormName" searchLabels="检验标准号|检验标准名称" searchKeys="qcnormId|qcnormName" ></sys:gridselect>
					</td>

					<td style="width: 220px;">
						<input id="purReportRSnList{{idx}}_memo" name="purReportRSnList[{{idx}}].memo" type="text" value="{{row.memo}}"    class="form-control "/>
					</td>
					
					
					

					<td style="width: 200px;">
						<input id="purReportRSnList{{idx}}_justifyLog" name="purReportRSnList[{{idx}}].justifyLog1" type="text" value="{{row.justifyLog}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#purReportRSnList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purReportRSnRowIdx = 0, purReportRSnTpl = $("#purReportRSnTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(purReport.purReportRSnList)};
					for (var i=0; i<data.length; i++){
						data[i].checkDate = data[i].checkDate.split(" ")[0];
						
						addRow('#purReportRSnList', purReportRSnRowIdx, purReportRSnTpl, data[i]);
						purReportRSnRowIdx = purReportRSnRowIdx + 1;
					}
				});
				
function changeNum(obj){
					
					//包头不包尾
					var begin = 15;
					var end = 27;
					var id = obj.id;


					//purReportRSnList{{idx}}_objSn
					var value = $("#"+id).val();
					//判断是否已经存在，若存在清空并提示
					$.ajax({
						type:'POST',
						datatype:'text',
						url:"${ctx}/purreport/purReport/ajaxForObjNameCheck",
						data:"objSn="+value,
						contentType:"application/x-www-form-urlencoded",
						success:function(data){

							if(data=='1'){
								jp.alert("物料已检验");

								$("#"+id).val("");
							}


						}
					})
					value = "";
					//截取字符串
					if(value.length>=40){
						var objCode = value.substring(begin,end);
						$.ajax({
							type:'POST',
							datatype:'text',
							url:"${ctx}/purreport/purReport/ajaxForObjName",
							data:"objCode="+objCode,
							contentType:"application/x-www-form-urlencoded",
							success:function(data){

								//top.layer.msg(data, {icon: "${ctype=='success'? 1:1}"});
								$("#"+arr[0]+"_objName").val(data.split("|")[0]);
								$("#"+arr[0]+"_objectDef").val(objCode);
								
								
							}
						})
					}
					//alert($('input[name="office.id"]').val());
				}
			</script>
			</div>
		</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('purreport:purReport:edit') || isAdd}">
				<div class="col-lg-1"></div>
		        <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                     <%--
		                      <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                --%>
		                	 <input type="submit" class="btn btn-primary btn-block btn-lg btn-parsley" value="保存" onclick="changefg1()"> &nbsp;
		                 </div>
		             </div>
		        </div>
		        <div class="col-lg-1"></div>
		        <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                     <%--
		                      <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                --%>
							 
							<input type="submit" class="btn btn-primary btn-block btn-lg btn-parsley" value="完成并提交" onclick="changefg()"> &nbsp;
		                 </div>
		             </div>
		        </div>
		        <div class="col-lg-1"></div>
		        <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                     <%--
		                      <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                --%>
							<input type="button" class="btn btn-primary btn-block btn-lg btn-parsley" value="关闭" onclick="myclose()" >
		                 </div>
		             </div>
		        </div>
		</c:if>
		
		
		<script type="text/javascript">
		function myclose(){
			//alert("close");
			window.location ="${ctx}/purreport/purReport/asseList";
			
		}
		function changefg(){
			fg=2;
		}
		
		function changefg1(){
			fg=1;
		}
		
		function save(){
			//alert($("#invCheckMain").val());
			//保存office的id
			//var cdeptCodeValue = $('input[name="office.id"]').val();
			//$("#cdeptCode").val(cdeptCodeValue);
			
			
			$.ajax({
				type:'POST',
				datatype:'text',
				url:"${ctx}/purreport/purReport/aajaxAsse",
				data:$("form").serializeArray(),
				contentType:"application/x-www-form-urlencoded",
				success:function(data){
					top.layer.msg("保存成功", {icon: "${ctype=='success'? 1:1}"});
				}
				
			})
			
			 
			
		}
	
		
		function sub(){
			//保存office的id
			//var cdeptCodeValue = $('input[name="office.id"]').val();
			//$("#cdeptCode").val(cdeptCodeValue);
			
			
			var newUrl = "${ctx}/purreport/purReport/subsaveAsse";    //设置新提交地址
	        $("form").attr('action',newUrl);    //通过jquery为action属性赋值
	        $("form").submit();    //提交ID为myform的表单
		}
		
		
		
		</script>
		
		
		
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
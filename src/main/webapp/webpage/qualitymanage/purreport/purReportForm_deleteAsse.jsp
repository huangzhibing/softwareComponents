<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单管理</title>
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
			
	        $('#checkDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#justifyDate').datetimepicker({
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
						<form:select path="rndFul" class="form-control " readonly="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('checkType2')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">执行标准：</label>
					<div class="col-sm-10">
					<form:select path="qualityNorm" class="form-control "  readonly="true">
							<form:option value="" label=""/>
							<form:options items="${qualityNormList}" itemLabel="qnormname" itemValue="qnormname" htmlEscape="false"/>
					</form:select>
					
					<%-- 
						//原内容
						<form:input path="qualityNorm" htmlEscape="false"    class="form-control "/>
					
					
					<div class="col-sm-10">
					<form:select path="qualityNorm" class="form-control "  >
							<form:option value="" label=""/>
							<form:options items="${qualityNormList}" itemLabel="qnormname" itemValue="qnormname" htmlEscape="false"/>
					</form:select>
					
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
						<form:input path="checktName" value="${purReport.office.name}"  htmlEscape="false"  readonly="true" class="form-control "/>
					
						
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
					
					<form:select path="cpositionName" class="form-control "  onchange="getChange()" readonly="true">
							<form:option value="" label=""/>
							<form:options items="${roleList}" itemLabel="name" itemValue="name" htmlEscape="false"/>
					</form:select>
					
					
					<%-- 	<form:input path="cpositionName" htmlEscape="false"    class="form-control "/>--%>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:textarea path="memo" htmlEscape="false" rows="4"  readonly="true"  class="form-control "/>
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
			        <input type='text' id='checkDate1' name="checkDate" class="form-control " readonly="true" value="${fns:getMyDate()}" readonly="true"/>
			                 
			                					            
			           
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
				
				
				<div class="form-group" >
					<label class="col-sm-2 control-label">审核日期：</label>
					<div class="col-sm-10">
					<form:input path="justifyDate" htmlEscape="false"  readonly="true"  class="form-control "/>
					
						
					
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
				
				<div class="form-group" >
					<label class="col-sm-2 control-label">审核状态：</label>
					<div class="col-sm-10">
						<form:input path="isAudit" htmlEscape="false"  readonly="true"  class="form-control "/>
						
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
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">检验物品清单：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<%-- <a class="btn btn-white btn-sm" onclick="addRow('#purReportRSnList', purReportRSnRowIdx, purReportRSnTpl);purReportRSnRowIdx = purReportRSnRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			
			
			
						<sys:gridselect url="${ctx}/ObjectDef/objectDef/data" id="purReportRSnList{{idx}}_objectDef" name="purReportRSnList[{{idx}}].objectDef.id" value="{{row.objectDef.id}}" labelName="purReportRSnList{{idx}}.objectDef.objCode" labelValue="{{row.objectDef.objCode}}"
							 title="选择检验对象编码" cssClass="form-control  " fieldLabels="objCode" fieldKeys="objCode" searchLabels="objCode" searchKeys="objCode" ></sys:gridselect>
					
			
				<select id="purReportRSnList{{idx}}_checkResult" name="purReportRSnList[{{idx}}].checkResult" data-value="{{row.checkResult}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('isQualified')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
			<div class='input-group form_datetime' id="purReportRSnList{{idx}}_checkDate">
		                    <input type='text'  name="purReportRSnList[{{idx}}].checkDate" class="form-control "  value="{{row.checkDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>	
			
			<sys:gridselect url="${ctx}/qmsqcnorm/qcnorm/data" id="purReportRSnList{{idx}}_qCNorm" name="purReportRSnList[{{idx}}].qCNorm.id" value="{{row.qCNorm.id}}" labelName="purReportRSnList{{idx}}.qCNorm.qcnormname" labelValue="{{row.qCNorm.qcnormname}}"
							 title="选择检验标准名称" cssClass="form-control  " fieldLabels="检验标准名称" fieldKeys="qcnormname" searchLabels="检验标准名称" searchKeys="qcnormname" ></sys:gridselect>
					
			
			
			--%>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验对象序列号</th>
						<th>检验对象编码</th>
						<th>检验对象名称</th>
						<th>检验结论</th>
						<%-- <th>问题类型编码</th> --%>
						<th>问题类型名称</th>
						<th>检验日期</th>
						<th>检验时间</th>
						<th>检验人名称</th>
						<th>备注</th>
						<th>检验标准名称</th>
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
					
					<td>
						<input id="purReportRSnList{{idx}}_objSn" name="purReportRSnList[{{idx}}].objSn" readonly="true" type="text" value="{{row.objSn}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportRSnList{{idx}}_objCode" name="purReportRSnList{{idx}}.objectDef.objCode" readonly="true" type="text" value="{{row.objCode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportRSnList{{idx}}_objName" name="purReportRSnList[{{idx}}].objName" readonly="true" type="text" value="{{row.objName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportRSnList{{idx}}_checkResult" name="purReportRSnList[{{idx}}].checkResult" readonly="true" type="text" value="{{row.checkResult}}"    class="form-control "/>
					

					
					</td>




	                <td style="display:none">
						<input id="purReportRSnList{{idx}}_matterCode" name="purReportRSnList[{{idx}}].matterCode" type="text" value="{{row.matterCode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportRSnList{{idx}}_matterName" name="purReportRSnList[{{idx}}].matterType.id" value="{{row.matterType.mattername}}" readonly="true" type="text" value="{{row.matterName}}"    class="form-control "/>
					
					</td>






					
					
					<td>

						<input id="purReportRSnList{{idx}}_checkDate" name="purReportRSnList[{{idx}}].checkDate" readonly="true" type="text" value="{{row.checkDate}}"    class="form-control "/>
					
											            
					</td>
					
					
					<td>
						<input id="purReportRSnList{{idx}}_checkTime" name="purReportRSnList[{{idx}}].checkTime" readonly="true" type="text" value="{{row.checkTime}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportRSnList{{idx}}_checkPerson" name="purReportRSnList[{{idx}}].checkPerson" readonly="true" type="text" value="{{row.checkPerson}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportRSnList{{idx}}_memo" name="purReportRSnList[{{idx}}].memo" readonly="true" type="text" value="{{row.memo}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportRSnList{{idx}}_qCNorm" name="purReportRSnList[{{idx}}].qCNorm.qcnormName" readonly="true" type="text" value="{{row.qCNorm.qcnormName}}"    class="form-control "/>
					
						</td>
					
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purReportRSnRowIdx = 0, purReportRSnTpl = $("#purReportRSnTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(purReport.purReportRSnList)};
					for (var i=0; i<data.length; i++){
						addRow('#purReportRSnList', purReportRSnRowIdx, purReportRSnTpl, data[i]);
						purReportRSnRowIdx = purReportRSnRowIdx + 1;
					}
				});
				
				 function getIdSelections() {
				        return $.map($("#purReportTable").bootstrapTable('getSelections'), function (row) {
				            return row.id
				        });
				    }
				
				function myDelete(){
					jp.confirm('确认要删除该检验单记录吗？', function(){
						var id = $("#id").val();
						window.location = "${ctx}/purreport/purReport/mydeleteEleAsse?id=" + id;
			          	   
					})
				}
				
				
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('purreport:purReport:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                    		<input type="button" class="btn btn-primary btn-block btn-lg btn-parsley" value="删除" onclick="myDelete()" >
		                     <%--
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">删除</button>
		                 --%>
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
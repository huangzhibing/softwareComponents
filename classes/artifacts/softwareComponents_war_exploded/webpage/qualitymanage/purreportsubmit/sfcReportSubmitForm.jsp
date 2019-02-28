<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单发送</title>
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
				/*	if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				*/
				}
			});
			
	        $('#checkDate').datetimepicker({
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
		
		
		
		
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/purreport/sfcReportSubmit"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="purReport" action="${ctx}/purreport/sfcReportSubmit/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">				   
					<label class="col-sm-2  control-label">检验单编号：</label>
					<div class="col-sm-3 ">
						<form:input path="reportId" htmlEscape="false"    class="form-control" readOnly="true"/>
					</div>
					<label class="col-sm-2 control-label">完工入库通知单编号：</label>	
					<div class="col-sm-3">
					 	<form:input path="invCheckMain.billnum" htmlEscape="false"    class="form-control" readOnly="true"/>					
					</div>
				</div>
				<div class="form-group">
				   <label class="col-sm-2 control-label">检验类型名称：</label>
					<div class="col-sm-3">
						<form:input path="checktName" htmlEscape="false"    class="form-control " readOnly="true"/>
					</div>
					<label class="col-sm-2 control-label">全检/抽检：</label>
					<div class="col-sm-3">
						<form:input path="rndFul" htmlEscape="false"    class="form-control " readOnly="true"/>			
					</div>
					
				</div>
								
				
				<div class="form-group">
					<label class="col-sm-2 control-label">执行标准：</label>
					<div class="col-sm-3">
						<form:input path="qualityNorm" htmlEscape="false"    class="form-control " readOnly="true"/>
					</div>
					<label class="col-sm-2 control-label">检验部门名称：</label>
					<div class="col-sm-3">
					<%-- 	<sys:treeselect id="office" name="office.id" value="${purReport.office.id}" labelName="office.name" labelValue="${purReport.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					--%> 
					<form:input path="office.id" htmlEscape="false"  value="${purReport.office.name}"  class="form-control " readOnly="true"/>					
					 
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验岗位名称：</label>
					<div class="col-sm-3">
						<form:input path="cpositionName" htmlEscape="false"    class="form-control " readOnly="true"/>
					</div>
					<label class="col-sm-2 control-label">检验结论：</label>
					<div class="col-sm-3">
						<form:input path="checkResult" htmlEscape="false"    class="form-control " readOnly="true"/>
					</div>
				</div>
			
				<div class="form-group">
					<label class="col-sm-2 control-label">检验人：</label>
					<div class="col-sm-3">
						<form:input path="checkPerson" htmlEscape="false"    class="form-control " readOnly="true"/>
					</div>
					<label class="col-sm-2 control-label">检验日期：</label>
					<div class="col-sm-3">
					<!--  	<p class="input-group">
							<div class='input-group form_datetime' id='checkDate'>
			                    <input type='text'  name="checkDate" class="form-control "  value="<fmt:formatDate value="${purReport.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
			           <form:input path="checkDate" htmlEscape="false"   value="${purReport.checkDate}"  class="form-control " readOnly="true"/>			         
			         -->
			         <div class='form_datetime' id='checkDate'>
			                    <input type='text'  name="checkDate" class="form-control " readOnly="true" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${purReport.checkDate}" />"/>			                    
			                </div>	
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验时间：</label>
					<div class="col-sm-3">
						<form:input path="checkTime" htmlEscape="false"    class="form-control " readOnly="true"/>
					</div>
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-3">
					 	<form:input path="memo" htmlEscape="false"  readOnly="true"  class="form-control "/>				 
					</div>
					
				</div>
				<%--
				<div class="form-group">
					<label class="col-sm-2 control-label">审核岗位名称：</label>
					<div class="col-sm-3">
						<form:input path="jpositionName" htmlEscape="false"    class="form-control " readOnly="true"/>
					</div>
					<label class="col-sm-2 control-label">审核人：</label>
					<div class="col-sm-3">
						<form:input path="justifyPerson" htmlEscape="false"    class="form-control " readOnly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">审核日期：</label>
					<div class="col-sm-3">
					      <div class='form_datetime' id='checkDate'>
			                    <input type='text'  name="justifyDate" class="form-control " readOnly="true" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${purReport.justifyDate}" />"/>			                    
			                </div>
					   
					</div>
					<label class="col-sm-2 control-label">审核结论：</label>
					<div class="col-sm-3">
						<form:input path="justifyResult" htmlEscape="false"    class="form-control " readOnly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">审核部门名称：</label>
					<div class="col-sm-3">
						<form:input path="jdeptName" htmlEscape="false"    class="form-control hidden" readOnly="true"/>
					</div>
				</div>
				 --%>
				<%-- 工作流相关标签--%>
				<%-- 
				<form:hidden path="id"/>
			    <form:hidden path="act.taskId"/>
			    <form:hidden path="act.taskName"/>
			    <form:hidden path="act.taskDefKey"/>
			    <form:hidden path="act.procInsId"/>
			    <form:hidden path="act.procDefId"/>
			    <form:hidden id="flag" path="act.flag"/>
				--%>	
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">质量管理检验抽检表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
		<%-- 	<a class="btn btn-white btn-sm" onclick="addRow('#purReportRSnList', purReportRSnRowIdx, purReportRSnTpl);purReportRSnRowIdx = purReportRSnRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
		--%>	<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验对象序列号</th>
						<th>检验对象编码</th>
						<th>检验对象名称</th>
						<th>检验结论</th>
						<th>检验人名称</th>
						<th>检验日期</th>
						<th>检验时间</th>
						<th>检验标准</th>
						<th>备注</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="purReportRSnList">
				</tbody>
			</table>
			<script type="text/template" id="purReportRSnTpl">
				<tr id="purReportRSnList{{idx}}">
					<td class="hide">
						<input id="purReportRSnList{{idx}}_id" name="purReportRSnList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purReportRSnList{{idx}}_delFlag" name="purReportRSnList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td >
						<input id="purReportRSnList{{idx}}_objSn" name="purReportRSnList[{{idx}}].objSn" type="text" value="{{row.objSn}}"   readonly="true"  class="form-control "/>
					</td>
					<td>
						<input id="purReportRSnList{{idx}}_objCode" name="purReportRSnList[{{idx}}].objCode" type="text" value="{{row.objectDef.id}}"   readonly="true"  class="form-control "/>
					</td>
					<td>
						<input id="purReportRSnList{{idx}}_objName" name="purReportRSnList[{{idx}}].objName" type="text" value="{{row.objName}}"   readonly="true"  class="form-control "/>
					</td>
                    <td>
						<input id="purReportRSnList{{idx}}_checkResult" name="purReportRSnList[{{idx}}].checkResult" type="text" value="{{row.checkResult}}"   readonly="true"  class="form-control "/>
					</td>									
                    <td>
						<input id="purReportRSnList{{idx}}_checkPerson" name="purReportRSnList[{{idx}}].checkPerson" type="text" value="{{row.checkPerson}}"   readonly="true"  class="form-control "/>
					</td>
                    <td>
						<input id="purReportRSnList{{idx}}_checkDate" name="purReportRSnList[{{idx}}].checkDate" type="text" value="{{row.checkDate}}"   readonly="true"  class="form-control "/>
					</td>
                    <td>
						<input id="purReportRSnList{{idx}}_checkTime" name="purReportRSnList[{{idx}}].checkTime" type="text" value="{{row.checkTime}}"   readonly="true"  class="form-control "/>
					</td>
					<td>
						<input id="purReportRSnList{{idx}}_qcnormName" name="purReportRSnList[{{idx}}].qcnormName" type="text" value="{{row.qcnorm.qcnormName}}"   readonly="true"  class="form-control "/>
					</td>
					<td>
						<input id="purReportRSnList{{idx}}_memo" name="purReportRSnList[{{idx}}].memo" type="text" value="{{row.memo}}"   readonly="true"  class="form-control "/>
					</td>
										
				</tr>
			</script>
			<script type="text/javascript">
				var purReportRSnRowIdx = 0, purReportRSnTpl = $("#purReportRSnTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data =${fns:toJson(purReport.purReportRSnList)};					
					for (var i=0; i<data.length; i++){
						data[i].checkDate=data[i].checkDate.split(" ")[0];
						addRow('#purReportRSnList', purReportRSnRowIdx, purReportRSnTpl, data[i]);
						console.log(data[i]);
						purReportRSnRowIdx = purReportRSnRowIdx + 1;
					}
				});
									
			</script>
			</div>
		</div>
		</div>
		
	<%-- 	<c:if test="${fns:hasPermission('purreport:purReportSubmit:view') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在发送通知...">发送通知</button>		                
		                 </div>
		             </div>
		        </div>
		</c:if>
	 --%> <div class="col-lg-2"></div>
	      
		        <c:if test="${purReport.sendState=='1'}">   
				    <div class="col-lg-7">
			        <div class="form-group text-center">
			                 <div>
		                         <a href= "${ctx}/purreport/sfcReportSubmit" class="btn btn-primary btn-block btn-lg btn-parsley">关闭</a>
			                 </div>
			             </div>
			        </div>
		        </c:if>
		        
		        <c:if test="${purReport.sendState!='1'}">
		            <div class="col-lg-3">
			             <div class="form-group text-center">		               		   
			                  <div>
		                        <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在发送通知...">发送通知</button>		                
			                 </div> 
			                
			             </div>
			        </div>
			        <div class="col-lg-2"></div>
			        <div class="col-lg-3">
			             <div class="form-group text-center">
			                 <div>
		                      <a href= "${ctx}/purreport/sfcReportSubmit" class="btn btn-primary btn-block btn-lg btn-parsley">关闭</a>
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
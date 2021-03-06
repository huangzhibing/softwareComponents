<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>装配检验单审核管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
	$().ready(function(){
		var justifyResult=$("#justifyResult");
		var comment=$("#container-comment");
		comment.css("display","none");
		justifyResult.change(function(){
		
		if(justifyResult.val()=="未通过"){
		comment.css("display","block");  
		}else{
		comment.css("display","none")
		}
	})
})
	function inputResult(){
	//获取岗位编号和名称
		var jposition = $("#roleName").val();
		$("#jpositionName").val(jposition);
		var jpositionId = $("#roleId").val();
		$("#jpositionCode").val(jpositionId);
		
		if($("#jdeptName").val() == ""||$("#jpositionName").val() == ""||$("#justifyPerson").val() == ""||$("#justifyDate").val() == ""){
			alert("请输入审核必填项！");
			return false;
		}
		else if(!($("#justifyResult").val()=='通过'||$("#justifyResult").val()=='未通过')){
		    alert("请选择审核结果！");
		    return false;
		}
		else{
		    if($("textarea#JustifyRemark").val()==""&&$("#justifyResult").val()=='未通过'){
		    alert("请填写审核意见！");
			return false;
			}
			else{
			return true;
			}
	}
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/purreport/purReportAudit/listAssembleAudit"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<%-- action="${ctx}/purreport/purReport/save" method="post" --%>
		<form:form id="inputForm" modelAttribute="purReport" action="${ctx}/purreport/purReportAudit/AuditSubmit" method="post" onsubmit="return inputResult()" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		        <form:hidden path="invCheckMain.billnum" htmlEscape="false"    class="form-control required" readonly = "true"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验单编号：</label>
					<div class="col-md-3"> 
						<form:input path="reportId" htmlEscape="false"  class="form-control required" readonly = "true"/>
					</div>
					<label class="col-sm-2 control-label">检验类型名称：</label>
					<div class="col-md-3"> 
						<form:input path="checktName" htmlEscape="false"    class="form-control " readonly = "true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">全检/抽检：</label>
					<div class="col-md-3"> 
					<form:input path="rndFul" htmlEscape="false"   class="form-control " readonly = "true"/>
					</div>
					<label class="col-sm-2 control-label">执行标准：</label>
					<div class="col-md-3"> 
						<form:input path="qualityNorm" htmlEscape="false"    class="form-control " readonly = "true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-md-3"> 
						<form:input path="state" htmlEscape="false"    class="form-control " readonly = "true"/>
					</div>
					<label class="col-sm-2 control-label">检验部门名称：</label>
					<div class="col-md-3"> 
						<form:input path="office.name" htmlEscape="false"    class="form-control " readonly = "true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验岗位名称：</label>
					<div class="col-md-3"> 
						<form:input path="cpositionName" htmlEscape="false"    class="form-control " readonly = "true"/>
					</div>
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-md-3"> 
						<form:input path="memo" htmlEscape="false"    class="form-control " readonly = "true"/>
					</div>
				</div>
				<div class="tabs-container">
						<ul class="nav nav-tabs">
							<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">质量管理检验抽检表：</a>
							</li>
						</ul>
						<div class="tab-content">
							<div id="tab-1" class="tab-pane fade in  active">
						<table class="table table-striped table-bordered table-condensed">
							<thead>
								<tr>
									<th class="hide"></th>
									<th>检验对象序列号</th>
									<th>检验对象编码</th>
									<th>检验对象名称</th>
									<th>检验结论</th>
									<th>问题类型名称</th>
									<th>检验人名称</th>
									<th>检验日期</th>
									<th>检验时间</th>
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
									<input id="purReportRSnList{{idx}}_id" name="purReportRSnList[{{idx}}].id" type="text" value="{{row.id}}"/>
									<input id="purReportRSnList{{idx}}_delFlag" name="purReportRSnList[{{idx}}].delFlag" type="hidden" value="0"/>
								</td>
								
								<td>
									<input id="purReportRSnList{{idx}}_objSn" name="purReportRSnList[{{idx}}].objSn" type="text" value="{{row.objSn}}" class="form-control " readonly = "true"/>
								</td>
								
								<td>
										<input id="purReportRSnList{{idx}}_objCode" name="purReportRSnList[{{idx}}].objCode" type="text" value="{{row.objCode}}" class="form-control " readonly = "true"/>
								</td>

								<td>
										<input id="purReportRSnList{{idx}}_objName" name="purReportRSnList[{{idx}}].objName" type="text" value="{{row.objName}}" class="form-control " readonly = "true"/>
								</td>
								
								<td>
									    <input id="purReportRSnList{{idx}}_checkResult" name="purReportRSnList[{{idx}}].checkResult" type="text" value="{{row.checkResult}}" class="form-control " readonly = "true"/>
								</td>

								<td>
									    <input id="purReportRSnList{{idx}}_mattername" name="purReportRSnList[{{idx}}].mattername" type="text" value="{{row.matterType.mattername}}" class="form-control " readonly = "true"/>
							    </td>
								
								<td>
										<input id="purReportRSnList{{idx}}_checkPerson" name="purReportRSnList[{{idx}}].checkPerson" type="text" value="{{row.checkPerson}}"  class="form-control " readonly = "true"/>
							    </td>
								
								<td>
										<input id="purReportRSnList{{idx}}_checkDate" name="purReportRSnList[{{idx}}].checkDate" type="text"  class="form-control" value="{{row.checkDate}}"  readonly = "true"/>				            
								</td>
								
								
								<td>
									    <input id="purReportRSnList{{idx}}_checkTime" name="purReportRSnList[{{idx}}].checkTime" type="text" value="{{row.checkTime}}" class="form-control " readonly = "true"/>
								</td>

								<td>
									<input id="purReportRSnList{{idx}}_memo" name="purReportRSnList[{{idx}}].memo" type="text" value="{{row.memo}}"  class="form-control "  class="form-control " readonly = "true"/>
								</td>							
							
						</script>
						<script type="text/javascript">
						
							var purReportRSnRowIdx = 0, purReportRSnTpl = $("#purReportRSnTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
							$(document).ready(function() {
								var data = ${fns:toJson(purReport.purReportRSnList)};
								for (var i=0; i<data.length; i++){
								data[i].checkDate = data[i].checkDate.split(" ")[0];
								console.log(data[i].checkDate);
									addRow('#purReportRSnList', purReportRSnRowIdx, purReportRSnTpl, data[i]);
									purReportRSnRowIdx = purReportRSnRowIdx + 1;
								}
							});
						</script>
						</div>
					</div>
					</div>
			
			<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>审核部门名称：</label>
					<div class="col-sm-3">
						<form:input path="jdeptName" htmlEscape="false"    class="form-control required" readonly ="true"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>审核岗位名称：</label>
					<div class="col-sm-3">
					<sys:gridselect url="${ctx}/purreport/purReportAudit/getSysRole" id="role" name="role.id" value="${role.id}" 
						labelName="role.name" labelValue="${role.name}"
							 title="选择审核岗位" cssClass="form-control required" 
							 fieldLabels="审核岗位" fieldKeys="name" searchLabels="审核岗位名称" searchKeys="name" >
					</sys:gridselect>
					</div>
			</div>
			<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>审核人：</label>
					<div id="divId" class="col-sm-3">
					<form:input path="justifyPerson" htmlEscape="false"   class="form-control required" readonly ="true"/> 
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>审核日期：</label>
					<div class="col-sm-3">
						<input type="text" name="justifyDate" htmlEscape="false" class="form-control required" readonly ="true"
						value="<fmt:formatDate value="${purReport.justifyDate}" pattern="yyyy-MM-dd"/>"/>
					</div>
			</div>
			<div class="form-group">
			<label class="col-sm-2 control-label"><font color="red">*</font>审核结论：</label>
			<div class="col-sm-3">
				<form:select path="justifyResult" class="form-control ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('justifyResult')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div class="col-sm-3" style = "display:none">
			<form:input path="jpositionName" htmlEscape="false"  class="form-control"/> 
			<form:input path="jpositionCode" htmlEscape="false"  class="form-control"/> 
			</div>
			</div>
			<div id="container-comment" class="form-group">
			<label class="col-sm-2 control-label"><font color="red">*</font>审核意见：</label>
			<div class="col-sm-5" >
			<textarea id="JustifyRemark" name="justifyRemark" class="form-control" rows="3"></textarea>
			<!-- <input type="text" name="justifyDate" htmlEscape="false" class="form-control required"/> -->
			</div>
			</div>
			<div class="col-lg-2"></div>
		        <div class="col-lg-3">
		             <div class="form-group text-center">
		                 <div>
		                     <input type ="submit" name="Submit" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="提交审核中..."/>
		                 </div>
		             </div>
		        </div>
		        <div class="col-lg-2"></div>
		        <div class="col-lg-3">
		        <div class="form-group text-center">
		                 <div>
		                      <a href= "${ctx}/purreport/purReportAudit/listAssembleAudit" class="btn btn-primary btn-block btn-lg btn-parsley">关闭</a>
		                 </div>
		             </div>
		        </div>
		</div>
		</div>
		
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
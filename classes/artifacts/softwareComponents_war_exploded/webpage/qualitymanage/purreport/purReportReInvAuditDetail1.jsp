<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单审核管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
	$().ready(function(){
	var comment=$("#container-comment");
		comment.css("display","none");
	if($("#justifyResult").val()=="未通过"){
	comment.css("display","block");
	document.getElementById("justifyRemark").value = "${purReport.justifyRemark}";
	}
	})
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/purreport/purReportAudit/listReInvAudit"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<%-- action="${ctx}/purreport/purReport/save" method="post" --%>
		<form:form id="inputForm" modelAttribute="purReport"  action="${ctx}/purreport/purReportAudit/AuditSubmit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
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
				<div class="form-group">
				    <label class="col-sm-2 control-label">审核部门名称：</label>
					<div class="col-md-3"> 
						<form:input path="jdeptName" htmlEscape="false"    class="form-control " readonly = "true"/>
					</div>
					<label class="col-sm-2 control-label">审核岗位名称：</label>
					<div class="col-md-3"> 
						<form:input path="jpositionName" htmlEscape="false"    class="form-control " readonly = "true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">审核人：</label>
					<div class="col-md-3"> 
						<form:input path="justifyPerson" htmlEscape="false"    class="form-control " readonly = "true"/>
					</div>
					<label class="col-sm-2 control-label">审核日期：</label>
					<div class="col-md-3"> 
	                    <input type='text'  name="justifyDate" class="form-control "  value="<fmt:formatDate value="${purReport.justifyDate}" pattern="yyyy-MM-dd"/>"
	                    readonly = "true"/>
					</div>
				</div>
				<div class="form-group">
				    <label class="col-sm-2 control-label">审核结论：</label>
					<div class="col-md-3"> 
						<form:input path="justifyResult" htmlEscape="false"    class="form-control " readonly = "true"/>
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
						<div id="container-comment" class="form-group">
						<label class="col-sm-2 control-label"><font color="red">*</font>审核意见：</label>
						<div class="col-sm-5" >
						<textarea class="form-control" rows="3" id = "justifyRemark" readonly ="true"></textarea>
						</div>
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
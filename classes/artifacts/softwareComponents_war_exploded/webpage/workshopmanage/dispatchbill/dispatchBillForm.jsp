<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车间派工单管理</title>
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
				<a class="panelButton" href="${ctx}/dispatchbill/dispatchBill"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="dispatchBill" action="${ctx}/dispatchbill/dispatchBill/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>派工单号：</label>
					<div class="col-sm-10">
						<form:input path="dispatchNo" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>加工路线单号：</label>
					<div class="col-sm-10">
						<form:input path="routineBillNo" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>工艺号：</label>
					<div class="col-sm-10">
						<form:input path="routingCode" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计划工作中心：</label>
					<div class="col-sm-10">
						<form:input path="centerCode" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>实作工作中心：</label>
					<div class="col-sm-10">
						<form:input path="actualcenterCode" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>负责人：</label>
					<div class="col-sm-10">
						<form:input path="personInCharge" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计划班组：</label>
					<div class="col-sm-10">
						<sys:treeselect id="teamCode" name="teamCode" value="teamCode" labelName="teamCode" labelValue="${dispatchBill.teamCode}"
							title="部门" url="/sys/office/treeData" cssClass="form-control  required" allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>实作班组：</label>
					<div class="col-sm-10">
						<sys:treeselect id="actualTeamCode" name="actualTeamCode" value="actualTeamCode" labelName="actualTeamCode" labelValue="${dispatchBill.actualTeamCode}"
							title="部门" url="/sys/office/treeData" cssClass="form-control  required" allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>班次：</label>
					<div class="col-sm-10">
						<sys:treeselect id="shiftCode" name="shiftCode" value="shiftCode" labelName="shiftCode" labelValue="${dispatchBill.shiftCode}"
							title="班次" url="/sys/office/treeData" cssClass="form-control  required" allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计划工时：</label>
					<div class="col-sm-10">
						<form:input path="workHour" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>实作工时：</label>
					<div class="col-sm-10">
						<form:input path="actualWorkHour" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">车间派工单人员表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#dispatchBillPersonList', dispatchBillPersonRowIdx, dispatchBillPersonTpl);dispatchBillPersonRowIdx = dispatchBillPersonRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						
						<th>加工路线单号</th>
						<th>工艺号</th>
						<th><font color="red">*</font>计划执行工人</th>
						<th><font color="red">*</font>实际执行工人</th>
						<th><font color="red">*</font>实际执行工时</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="dispatchBillPersonList">
				</tbody>
			</table>
			<script type="text/template" id="dispatchBillPersonTpl">//<!--
				<tr id="dispatchBillPersonList{{idx}}">
					<td class="hide">
						<input id="dispatchBillPersonList{{idx}}_id" name="dispatchBillPersonList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="dispatchBillPersonList{{idx}}_delFlag" name="dispatchBillPersonList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					
					<td>
						<input id="dispatchBillPersonList{{idx}}_routineBillNo" name="dispatchBillPersonList[{{idx}}].routineBillNo" type="text" value="{{row.routineBillNo}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="dispatchBillPersonList{{idx}}_routingCode" name="dispatchBillPersonList[{{idx}}].routingCode" type="text" value="{{row.routingCode}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="dispatchBillPersonList{{idx}}_workerCode" name="dispatchBillPersonList[{{idx}}].workerCode" type="text" value="{{row.workerCode}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="dispatchBillPersonList{{idx}}_actualWorkerCode" name="dispatchBillPersonList[{{idx}}].actualWorkerCode" type="text" value="{{row.actualWorkerCode}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="dispatchBillPersonList{{idx}}_actualWorkHour" name="dispatchBillPersonList[{{idx}}].actualWorkHour" type="text" value="{{row.actualWorkHour}}"    class="form-control required"/>
					</td>

					<td>
						<textarea id="dispatchBillPersonList{{idx}}_remarks" name="dispatchBillPersonList[{{idx}}].remarks" rows="1"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#dispatchBillPersonList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var dispatchBillPersonRowIdx = 0, dispatchBillPersonTpl = $("#dispatchBillPersonTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(dispatchBill.dispatchBillPersonList)};
					for (var i=0; i<data.length; i++){
						addRow('#dispatchBillPersonList', dispatchBillPersonRowIdx, dispatchBillPersonTpl, data[i]);
						dispatchBillPersonRowIdx = dispatchBillPersonRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('dispatchbill:dispatchBill:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <c:if test="${isAdd}">
		                     	<button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在保存...">保存</button>
		                 	 </c:if>
		                 	 <c:if test="${!isAdd}">
		                 	 	<button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在保存...">保存</button>
		                 	 	<button type="button" class="btn btn-primary btn-lg btn-parsley" onclick="submitBill()">提交</button>
		                 	 </c:if>
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
<script>
function submitBill(){
	jp.loading("正在提交...");
	form=$('#inputForm');
	jp.post("${ctx}/dispatchbill/dispatchBill/submit",$('#inputForm').serialize(),function(data){
		form.submit();
    });
	jp.close();
}
</script>
</body>
</html>
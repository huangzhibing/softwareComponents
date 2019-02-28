<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车间加工路线管理管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
                    //提交提示
                    if($('#assignedState').val()=="C"){
                        jp.confirm("是否提交此加工路线单？",function () {
                            jp.loading();
                            form.submit();
                        },function () {
                            return false;
                        });
                    }else {
                        jp.loading();
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
			
	        $('#planBDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#planEDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#makeDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#confirmDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#deliveryDate').datetimepicker({
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






	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/processroutine/processBatch/queryList"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="processBatch" action="" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="assignedState"/>
		<sys:message content="${message}"/>	

				<div class="form-group">
					<label class="col-sm-2 control-label">车间作业计划号：</label>
					<div class="col-sm-3">
						<form:input path="processBillNo" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">批次序号：</label>
					<div class="col-sm-3">
						<form:input path="batchNo" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">产品编码：</label>
					<div class="col-sm-3">
						<form:input path="prodCode" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">产品名称：</label>
					<div class="col-sm-3">
						<form:input path="prodName" htmlEscape="false"   readonly="true" class="form-control "/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">计划生产数量：</label>
					<div class="col-sm-3">
						<form:input path="planQty" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">计划生产日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
						<div class='input-group form_datetime' id='planBDate'>
							<input type='text'  name="planBDate" class="form-control " readonly="true" value="<fmt:formatDate value="${processBatch.planBDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
						</div>
						</p>
					</div>
				</div>

				<div class="form-group">

                    <label class="col-sm-2 control-label">单据状态：</label>
                    <div class="col-sm-3">
                        <form:select path="assignedState" class="form-control "  disabled="true" >
                            <form:option value="" label=""/>
                            <form:options items="${fns:getDictList('processroutine_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select>
                    </div>

					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-3">
						<form:textarea path="remarks" htmlEscape="false" rows="2"  readonly="true"  class="form-control "/>
					</div>
				</div>

				

		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">车间加工路线表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#processRoutineList', processRoutineRowIdx, processRoutineTpl);processRoutineRowIdx = processRoutineRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
		<div class="table-responsive" style="min-height: 250px">
				<table class="table table-striped table-bordered table-condensed " style="min-width: 200%">
					<thead>
					<tr>
						<th class="hide"></th>
						<th>加工路线单号</th>
						<th>生产序号</th>
						<th>工艺编码</th>
						<th>工艺名称</th>
						<th><font color="red">*</font>工作中心代码</th>
						<th>计划数量</th>
						<th>生产日期</th>
						<th><font color="red">*</font>负责人</th>
						<th><font color="red">*</font>计划班组</th>
						<th><font color="red">*</font>班次</th>
						<th>计划工时</th>
						<th>备注信息</th>

					</tr>
					</thead>
					<tbody id="processRoutineList">
					</tbody>
				</table>
		</div>
				<script type="text/template" id="processRoutineTpl">//<!--
				<tr id="processRoutineList{{idx}}">
				



					<td>
						<input id="processRoutineList{{idx}}_routineBillNo" name="processRoutineList[{{idx}}].routineBillNo" readonly="true" type="text" value="{{row.routineBillNo}}"    class="form-control "/>
					</td>


					<td>
						<input id="processRoutineList{{idx}}_produceNo" name="processRoutineList[{{idx}}].produceNo" type="text" value="{{row.produceNo}}" readonly="true"   class="form-control "/>
					</td>


					<td>
						<input id="processRoutineList{{idx}}_routingCode" name="processRoutineList[{{idx}}].routingCode" type="text" value="{{row.routingCode}}"   readonly="true" class="form-control "/>
					</td>

					<td>
						<input id="processRoutineList{{idx}}_routingName" name="processRoutineList[{{idx}}].routingName" type="text" value="{{row.routingName}}"  readonly="true"  class="form-control "/>
					</td>




					<td>
						<%--<input id="processRoutineList{{idx}}_centerCode" name="processRoutineList[{{idx}}].centerCode" type="text" value="{{row.centerCode}}"    class="form-control "/>--%>
						<sys:gridselect url="${ctx}/workcenter/workCenter/data" id="processRoutineList{{idx}}_centerCode" name="processRoutineList[{{idx}}].centerId" value="{{row.centerCode}}" labelName="processRoutineList[{{idx}}].centerCode" labelValue="{{row.centerCode}}"
							 title="选择工作中心代码" cssClass="form-control required" fieldLabels="工作中心代码|工作中心名称" fieldKeys="centerCode|centerName" searchLabels="工作中心代码|工作中心名称" searchKeys="centerCode|centerName" ></sys:gridselect>


					</td>






					<td>
						<input id="processRoutineList{{idx}}_planQty" name="processRoutineList[{{idx}}].planQty" type="text" value="{{row.planQty}}"  readonly="true"  class="form-control "/>
					</td>


					<td>
						<div class='input-group form_datetime' id="processRoutineList{{idx}}_planBData">
		                    <input type='text'  name="processRoutineList[{{idx}}].planBData" class="form-control "  readonly="true" value="{{row.planBData}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>
					</td>










					<td>
						<%--<input id="processRoutineList{{idx}}_personIncharge" name="processRoutineList[{{idx}}].personIncharge" type="text" value="{{row.personIncharge}}"    class="form-control required"/>--%>
						<sys:userselect id="processRoutineList{{idx}}_personIncharge" name="processRoutineList[{{idx}}].personInchargeId" value="{{row.personIncharge}}" labelName="processRoutineList[{{idx}}].personIncharge" labelValue="{{row.personIncharge}}"
							    cssClass="form-control required"/>
					</td>Id


					<td>
						<%--<input id="processRoutineList{{idx}}_teamCode" name="processRoutineList[{{idx}}].teamCode" type="text" value="{{row.teamCode}}"    class="form-control required"/>--%>
						<sys:gridselect url="${ctx}/team/team/data" id="processRoutineList{{idx}}_teamCode" name="processRoutineList[{{idx}}].teamId" value="{{row.teamCode}}" labelName="processRoutineList[{{idx}}].teamCode" labelValue="{{row.teamCode}}"
							 title="选择计划班组" cssClass="form-control required" fieldLabels="班组代码|班组名称" fieldKeys="teamCode|teamName" searchLabels="班组代码|班组名称" searchKeys="teamCode|teamName" ></sys:gridselect>
					</td>





					<td>
						<%--<input id="processRoutineList{{idx}}_shiftName" name="processRoutineList[{{idx}}].shiftName" type="text" value="{{row.shiftName}}"    class="form-control required "/>--%>
							<sys:gridselect url="${ctx}/shiftdef/shiftDef/data" id="processRoutineList{{idx}}_shiftname"  value="{{row.shiftname}}" name="processRoutineList[{{idx}}].shiftid"   labelName="processRoutineList[{{idx}}].shiftname" labelValue="{{row.shiftname}}"
							 title="选择班次" cssClass="form-control required" fieldLabels="班次名称|开始时间|结束时间" fieldKeys="shiftname|begintime|endtime" searchLabels="班次名称|开始时间|结束时间" searchKeys="shiftname|begintime|endtime" ></sys:gridselect>

					</td>


					<td>
						<input id="processRoutineList{{idx}}_workhour" name="processRoutineList[{{idx}}].workhour" type="text" value="{{row.workhour}}"  readonly="true"  class="form-control "/>
					</td>




					<td>
						<textarea id="processRoutineList{{idx}}_remarks" name="processRoutineList[{{idx}}].remarks" rows="1"  readonly="true"  class="form-control ">{{row.remarks}}</textarea>
					</td>

				</tr>//-->
				</script>
			<script type="text/javascript">
				var processRoutineRowIdx = 0, processRoutineTpl = $("#processRoutineTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(processBatch.processRoutineList)};
					for (var i=0; i<data.length; i++){
						addRow('#processRoutineList', processRoutineRowIdx, processRoutineTpl, data[i]);
						processRoutineRowIdx = processRoutineRowIdx + 1;
					}
				});
			</script>
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
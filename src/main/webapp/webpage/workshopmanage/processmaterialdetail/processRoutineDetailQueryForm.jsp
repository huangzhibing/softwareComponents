<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车间物料安装明细查询</title>
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
			
	        $('#planBDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#realBDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#MakeDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#ConfirmDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#DeliveryDate').datetimepicker({
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
				<a class="panelButton" href="${ctx}/processmaterialdetail/processRoutineDetail/queryList"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="processRoutineDetail" action="${ctx}/processmaterialdetail/processRoutineDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	

				<div class="form-group">
					<label class="col-sm-2 control-label">加工路线单号：</label>
					<div class="col-sm-3">
						<form:input path="routinebillno" htmlEscape="false" readonly="true"   class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">单件序号：</label>
					<div class="col-sm-3">
						<form:input path="seqno" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>


				<div class="form-group">

					<label class="col-sm-2 control-label">机器序列号：</label>
					<div class="col-sm-3">
						<form:input path="mserialno" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<%--<label class="col-sm-2 control-label">产品编码：</label>
					<div class="col-sm-3">
						<form:input path="prodcode" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>--%>

					<label class="col-sm-2 control-label">产品名称：</label>
					<div class="col-sm-3">
						<form:input path="prodname" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工艺编码：</label>
					<div class="col-sm-3">
						<form:input path="routingcode" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">工艺名称：</label>
					<div class="col-sm-3">
						<form:input path="routingname" htmlEscape="false"   readonly="true" class="form-control "/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">工作中心代码：</label>
					<div class="col-sm-3">
						<form:input path="centercode.centerCode" htmlEscape="false"   readonly="true" class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">生产时间：</label>
					<div class="col-sm-3">
						<form:input path="workhour" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label">生产日期：</label>
					<div class="col-sm-3">

						<div class='input-group form_datetime' id='planBDate'>
							<input type='text'  name="planbdate" class="form-control " readonly="true" value="<fmt:formatDate value="${processRoutineDetail.planbdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
						</div>

					</div>

					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-3">
						<form:input path="remarks" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>

			<div class="form-group">

				<label class="col-sm-2 control-label">实际生产开始时间：</label>
				<div class="col-sm-3">
					<div class='input-group form_datetime' id='actualBDate'>
						<input type='text'  name="actualBDate" class="form-control " readonly="true"  value="<fmt:formatDate value="${processRoutineDetail.actualBDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
						<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
					</div>
				</div>

				<label class="col-sm-2 control-label">实际生产结束时间：</label>
				<div class="col-sm-3">
					<div class='input-group form_datetime' id='planedate'>
						<input type='text'  name="planedate" class="form-control " readonly="true"  value="<fmt:formatDate value="${processRoutineDetail.planedate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
						<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
					</div>
				</div>

			</div>

		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">车间物料安装明细：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#processMaterialDetailList', processMaterialDetailRowIdx, processMaterialDetailTpl);processMaterialDetailRowIdx = processMaterialDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
					<%--	<th><font color="red">*</font>单件序号</th>
						<th><font color="red">*</font>工艺编码</th>
						<th><font color="red">*</font>工艺名称</th>--%>
						<th><font color="red">*</font>物料编码</th>
						<th><font color="red">*</font>物料名称</th>
						<th><font color="red">*</font>物料二维码</th>
						<th><font color="red">*</font>是否有品质异常</th>
						<th>品质异常类型</th>
						<th>备注信息</th>
					</tr>
				</thead>
				<tbody id="processMaterialDetailList">
				</tbody>
			</table>
			<script type="text/template" id="processMaterialDetailTpl">//<!--
				<tr id="processMaterialDetailList{{idx}}">
					<td class="hide">
						<input id="processMaterialDetailList{{idx}}_id" name="processMaterialDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="processMaterialDetailList{{idx}}_delFlag" name="processMaterialDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					

					
					<%--
					<td>
						<input id="processMaterialDetailList{{idx}}_seqNo" name="processMaterialDetailList[{{idx}}].seqNo" type="text" value="{{row.seqNo}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="processMaterialDetailList{{idx}}_routingCode" name="processMaterialDetailList[{{idx}}].routingCode" type="text" value="{{row.routingCode}}"   readonly="true" class="form-control required"/>
					</td>
					
					
					<td>
						<input id="processMaterialDetailList{{idx}}_routingName" name="processMaterialDetailList[{{idx}}].routingName" type="text" value="{{row.routingName}}" readonly="true"   class="form-control required"/>
					</td>
					--%>
					
					<td>
						<input id="processMaterialDetailList{{idx}}_itemCode" name="processMaterialDetailList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="processMaterialDetailList{{idx}}_itemName" name="processMaterialDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="processMaterialDetailList{{idx}}_itemSN" name="processMaterialDetailList[{{idx}}].itemSN" readonly="true" type="text" value="{{row.itemSN}}"    class="form-control required"/>
					</td>
					

					<td style=" width: 80px; ">
						<select id="processMaterialDetailList{{idx}}_hasQualityPro" name="processMaterialDetailList[{{idx}}].hasQualityPro" data-value="{{row.hasQualityPro}}"  readonly="true"  class="form-control  required ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('itemFinish')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>

					<td>
						<input id="processMaterialDetailList{{idx}}_qualityProblem" name="processMaterialDetailList[{{idx}}].qualityProblem" type="text" readonly="true"  value="{{row.qualityProblem}}"    class="form-control"/>
					</td>

					
					
					<td>
						<textarea id="processMaterialDetailList{{idx}}_remarks" name="processMaterialDetailList[{{idx}}].remarks" rows="1"  readonly="true"  class="form-control ">{{row.remarks}}</textarea>
					</td>
					

				</tr>//-->
			</script>
			<script type="text/javascript">
				var processMaterialDetailRowIdx = 0, processMaterialDetailTpl = $("#processMaterialDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(processRoutineDetail.processMaterialDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#processMaterialDetailList', processMaterialDetailRowIdx, processMaterialDetailTpl, data[i]);
						processMaterialDetailRowIdx = processMaterialDetailRowIdx + 1;
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
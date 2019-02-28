<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>超期复验单管理</title>
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
				<a class="panelButton" href="${ctx}/reinvcheckmain/reinvCheckMain"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="reinvCheckMain" action="${ctx}/reinvcheckmain/reinvCheckMain/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据号：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
			<%--<div class="form-group">--%>
				<%--<label class="col-sm-2 control-label"><font color="red">*</font>核算期间：</label>--%>
				<%--<div class="col-sm-10">--%>
					<%--<form:input readonly="true" cssStyle="display: none" path="period.id" htmlEscape="false"    class="form-control "/>--%>
					<%--<input readonly class="form-control" value="${billMain.period.periodId}">--%>
				<%--</div>--%>
			<%--</div>--%>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='makeDate'>
			                    <input type='text'  name="makeDate" class="form-control "  value="<fmt:formatDate value="${reinvCheckMain.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人编码：</label>
					<div class="col-sm-10">
						<form:input path="makeEmpId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人姓名：</label>
					<div class="col-sm-10">
						<form:input path="makeEmpName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-10">
						<form:input path="billFlag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>库房号：</label>
				<div class="col-sm-10">
					<sys:gridselect-modify targetId="wareName" targetField="wareName" url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${billMain.ware.id}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
										   title="选择库房号" cssClass="form-control required" fieldLabels="库房号|库房名称" fieldKeys="wareID|wareName" searchLabels="库房号|库房名称" searchKeys="wareID|wareName" ></sys:gridselect-modify>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>库房名称：</label>
				<div class="col-sm-10">
					<form:input readonly="true" path="wareName" htmlEscape="false"    class="form-control required"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>库管员代码：</label>
				<div class="col-sm-10">
					<sys:gridselect-modify labelField="user.no" targetId="wareEmpName" targetField="empName" url="${ctx}/employee/employee/data" id="wareEmp" name="wareEmp.id" value="${billMain.wareEmp.id}" labelName="wareEmp.user.no" labelValue="${billMain.wareEmp.user.no}"
										   title="选择库管员代码" cssClass="form-control " fieldLabels="库管员代码|库管员名称" fieldKeys="user.no|empName" searchLabels="库管员代码|库管员名称" searchKeys="user.no|empName" ></sys:gridselect-modify>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>库管员名称：</label>
				<div class="col-sm-10">
					<form:input readonly="true" path="wareEmpName" htmlEscape="false"  value="${billMain.wareEmp.empName}"   class="form-control "/>
				</div>
			</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">复验员编码：</label>
					<div class="col-sm-10">
						<form:input path="checkEmpId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">复验员姓名：</label>
					<div class="col-sm-10">
						<form:input path="checkEmpName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">质检标志：</label>
					<div class="col-sm-10">
						<form:input path="qmsFlag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">超期复验子表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#reinvCheckDetailList', reinvCheckDetailRowIdx, reinvCheckDetailTpl);reinvCheckDetailRowIdx = reinvCheckDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>序号</th>
						<th><font color="red">*</font>物料编号</th>
						<th>物料名称</th>
						<th>物料规格</th>
						<th><font color="red">*</font>单位</th>
						<th><font color="red">*</font>数量</th>
						<th>实际价格</th>
						<th>实际金额</th>
						<th>批次</th>
						<th>货区代码</th>
						<th>货区名称</th>
						<th>货位编码</th>
						<th>货位名称</th>
						<th><font color="red">*</font>合格数量</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="reinvCheckDetailList">
				</tbody>
			</table>
			<script type="text/template" id="reinvCheckDetailTpl">//<!--
				<tr id="reinvCheckDetailList{{idx}}">
					<td class="hide">
						<input id="reinvCheckDetailList{{idx}}_id" name="reinvCheckDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="reinvCheckDetailList{{idx}}_delFlag" name="reinvCheckDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="reinvCheckDetailList{{idx}}_serialNum" name="reinvCheckDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="reinvCheckDetailList{{idx}}_itemCode" name="reinvCheckDetailList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="reinvCheckDetailList{{idx}}_itemSpec" name="reinvCheckDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="reinvCheckDetailList{{idx}}_itemName" name="reinvCheckDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="reinvCheckDetailList{{idx}}_measUnit" name="reinvCheckDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="reinvCheckDetailList{{idx}}_itemQty" name="reinvCheckDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="reinvCheckDetailList{{idx}}_itemPrice" name="reinvCheckDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="reinvCheckDetailList{{idx}}_itemSum" name="reinvCheckDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="reinvCheckDetailList{{idx}}_itemBatch" name="reinvCheckDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>
					
					
				<%--<td>--%>
						<%--<sys:gridselect-modify url="${ctx}/bin/bin/data" id="billDetailList{{idx}}_bin" name="billDetailList[{{idx}}].bin.id" value="{{row.bin.id}}" labelName="billDetailList{{idx}}.bin.binId" labelValue="{{row.bin.binId}}"--%>
							 <%--title="选择货区号" cssClass="form-control  " fieldLabels="货区编码|货区名称" fieldKeys="binId|binDesc" searchLabels="货区编码|货区名称" searchKeys="binId|binDesc"--%>
							 <%--targetId="billDetailList{{idx}}_binName" targetField="binDesc"></sys:gridselect-modify>--%>
					<%--</td>--%>


					<%--<td>--%>
						<%--<input id="billDetailList{{idx}}_binName" name="billDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"    class="form-control "/>--%>
					<%--</td>--%>

					
					
						<%--<td>--%>
						<%--<sys:gridselect-modify url="${ctx}/location/location/data" id="billDetailList{{idx}}_loc" name="billDetailList[{{idx}}].loc.id" value="{{row.loc.id}}" labelName="billDetailList{{idx}}.loc.locId" labelValue="{{row.loc.locId}}"--%>
							 <%--title="选择货位号" cssClass="form-control  " fieldLabels="货位编码|货位名称" fieldKeys="locId|locDesc" searchLabels="货位编码|货位名称" searchKeys="locId|locDesc"--%>
							 <%--targetId="billDetailList{{idx}}_locName" targetField="locDesc" ></sys:gridselect-modify>--%>
					<%--</td>--%>


					<%--<td>--%>
						<%--<input id="billDetailList{{idx}}_locName" name="billDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"    class="form-control "/>--%>
					<%--</td>--%>
					
					<td>
						<input id="reinvCheckDetailList{{idx}}_hgQty" name="reinvCheckDetailList[{{idx}}].hgQty" type="text" value="{{row.hgQty}}"    class="form-control required"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#reinvCheckDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var reinvCheckDetailRowIdx = 0, reinvCheckDetailTpl = $("#reinvCheckDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(reinvCheckMain.reinvCheckDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#reinvCheckDetailList', reinvCheckDetailRowIdx, reinvCheckDetailTpl, data[i]);
						reinvCheckDetailRowIdx = reinvCheckDetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('reinvcheckmain:reinvCheckMain:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
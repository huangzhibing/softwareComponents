<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>盘点数据管理</title>
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
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('.close').hide()
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
				<a class="panelButton" href="${ctx}/countdata/countMain"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="countMain" action="${ctx}/countdata/countMain/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>盘点单号：</label>
					<div class="col-sm-10">
						<form:input  readonly="true" path="billNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>仓库代码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disableClick="true" targetId="wareName" targetField="wareName" url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${countMain.ware.id}" labelName="ware.wareID" labelValue="${countMain.ware.wareID}"
							 title="选择仓库代码" cssClass="form-control required" fieldLabels="仓库代码|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库代码|仓库名称" searchKeys="wareID|wareName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>仓库名称：</label>
					<div class="col-sm-10">
						<form:input  readonly="true" path="wareName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">货区代码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disableClick="true" targetId="binName" targetField="binDesc" url="${ctx}/bin/bin/data" id="bin" name="bin.id" value="${countMain.bin.id}" labelName="bin.binId" labelValue="${countMain.bin.binId}"
							 title="选择货区代码" cssClass="form-control" fieldLabels="货区代码|货区名称" fieldKeys="binId|binDesc" searchLabels="货区代码|货区名称" searchKeys="binId|binDesc" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">货区名称：</label>
					<div class="col-sm-10">
						<form:input readonly="true" path="binName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">货位代码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disableClick="true" targetId="locName" targetField="locDesc" url="${ctx}/location/location/data" id="loc" name="loc.id" value="${countMain.loc.id}" labelName="loc.locId" labelValue="${countMain.loc.locId}"
							 title="选择货位代码" cssClass="form-control" fieldLabels="货位代码|货位名称" fieldKeys="locId|locDesc" searchLabels="货位代码|货位名称" searchKeys="locId|locDesc" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">货位名称：</label>
					<div class="col-sm-10">
						<form:input  readonly="true" path="locName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>盘点日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='checkDate'>
			                    <input readonly="true" type='text'  name="checkDate" class="form-control required"  value="<fmt:formatDate value="${countMain.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>盘点人：</label>
					<div class="col-sm-10">
						<sys:userselect id="checkEmpId" name="checkEmpId.id" value="${countMain.checkEmpId.id}" labelName="checkEmpId.name" labelValue="${countMain.checkEmpId.name}"
							    cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">调账人：</label>
					<div class="col-sm-10">
						<sys:userselect id="adjEmpId" name="adjEmpId.id" value="${countMain.adjEmpId.id}" labelName="adjEmpId.name" labelValue="${countMain.adjEmpId.name}"
							    cssClass="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">盘点单明细：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#countDetailList', countDetailRowIdx, countDetailTpl);countDetailRowIdx = countDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>物料编码</th>
						<th>物料名称</th>
                        <th>物料规格</th>
						<th>单位</th>
                        <th>账面数量</th>
                        <th><font color="red">*</font>实盘数量</th>
						<th>实际单价</th>
                        <th>批号</th>
					</tr>
				</thead>
				<tbody id="countDetailList">
				</tbody>
			</table>
			<script type="text/template" id="countDetailTpl">//<!--
				<tr id="countDetailList{{idx}}">
					<td class="hide">
						<input id="countDetailList{{idx}}_id" name="countDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="countDetailList{{idx}}_delFlag" name="countDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<sys:gridselect url="${ctx}/item/item/data" id="countDetailList{{idx}}_item" name="countDetailList[{{idx}}].item.id" value="{{row.item.id}}" labelName="countDetailList{{idx}}.item.code" labelValue="{{row.item.code}}"
							 title="选择物料编码" cssClass="form-control  required" fieldLabels="物料编码|物料名称|物料规格|物料图号" fieldKeys="code|name|specModel|drawNO" searchLabels="物料编码|物料名称|物料规格|物料图号" searchKeys="code|name|specModel|drawNO" ></sys:gridselect>
					</td>
					
					
					<td>
						<input id="countDetailList{{idx}}_itemName" readonly="true" name="countDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>


					<td>
						<input id="countDetailList{{idx}}_specModel" readonly="true" name="countDetailList[{{idx}}].specModel" type="text" value="{{row.specModel}}"    class="form-control "/>
					</td>
					
					
					<td>
						<sys:gridselect url="${ctx}/unit/unit/data" id="countDetailList{{idx}}_measUnit" name="countDetailList[{{idx}}].measUnit.id" value="{{row.measUnit.id}}" labelName="countDetailList{{idx}}.measUnit.unitName" labelValue="{{row.measUnit.unitName}}"
							 title="选择计量单位" cssClass="form-control  " fieldLabels="计量单位编码|计量单位名称" fieldKeys="unitCode|unitName" searchLabels="计量单位编码|计量单位名称" searchKeys="unitCode|unitName" ></sys:gridselect>
					</td>


					<td>
						<input id="countDetailList{{idx}}_itemQty" readonly name="countDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control "/>
					</td>


					<td>
						<input id="countDetailList{{idx}}_realQty" name="countDetailList[{{idx}}].realQty" type="text" value="{{row.realQty}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="countDetailList{{idx}}_planPrice" name="countDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"    class="form-control "/>
					</td>


					<td>
						<input id="countDetailList{{idx}}_batchId" name="countDetailList[{{idx}}].batchId" type="text" value="{{row.batchId}}"    class="form-control "/>
					</td>


				</tr>//-->
			</script>
			<script type="text/javascript">
				var countDetailRowIdx = 0, countDetailTpl = $("#countDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(countMain.countDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#countDetailList', countDetailRowIdx, countDetailTpl, data[i]);
						countDetailRowIdx = countDetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('countdata:countMain:edit') || isAdd}">
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
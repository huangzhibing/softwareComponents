<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>材料单据稽核管理</title>
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
		    
		    //使用字典将对应键值转换
			$("#billFlag").val(jp.getDictLabel(${fns:toJson(fns:getDictList('billFlag'))}, $("#billFlag").val(), "-"));
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/cosbillrecord/cosBillRecord/list2"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="billMain" action="${ctx}/cosbillrecord/cosBillRecord/saveCosBillRecord" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">单据编号：</label>
					<div class="col-sm-3">
						<form:input path="billNum" htmlEscape="false"   class="form-control " readonly="true"/>
					</div>
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-3">
						<form:input path="billFlag" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">创建日期：</label>
					<div class="col-sm-3">
						<input type="text" name="createDate"  htmlEscape="false"   class="form-control " readonly="true" value="<fmt:formatDate value="${billMain.createDate}" pattern="yyyy-MM-dd"/>"/>
					</div>
					<label class="col-sm-2 control-label">核算期间：</label>
					<div class="col-sm-3">
						<form:input path="period.periodId" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">仓库编码：</label>
					<div class="col-sm-3">
						<form:input path="ware.wareID" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
					<label class="col-sm-2 control-label">仓库名称：</label>
					<div class="col-sm-3">
						<form:input path="wareName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">部门编码：</label>
					<div class="col-sm-3">
						<form:input path="dept.code" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
					<label class="col-sm-2 control-label">部门名称：</label>
					<div class="col-sm-3">
						<form:input path="deptName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">经办人编码：</label>
					<div class="col-sm-3">
						<form:input path="billPerson.no" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
					<label class="col-sm-2 control-label">经办人名称：</label>
					<div class="col-sm-3">
						<form:input path="billPerson.loginName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库管员编码：</label>
					<div class="col-sm-3">
						<form:input path="wareEmp.user.no" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
					<label class="col-sm-2 control-label">库管员名称：</label>
					<div class="col-sm-3">
						<form:input path="wareEmpname" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">用途编码：</label>
					<div class="col-sm-3">
						<form:input path="invuse.useId" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
					<label class="col-sm-2 control-label">用途名称：</label>
					<div class="col-sm-3">
						<form:input path="useName" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">生产计划号：</label>
					<div class="col-sm-3">
						<form:input path="mpsPlanId" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
					<label class="col-sm-2 control-label">内部合同号：</label>
					<div class="col-sm-3">
						<form:input path="orderId" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
			<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">外购件出库单据子表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>序号</th>
						<th>物料代码</th>
						<th>物料名称</th>
						<th>物料规格</th>
						<th>计量单位</th>
						<th>数量</th>
						
						<th>货区号</th>
						<th>货区名称</th>
						<th>货位号</th>
						<th>货位名称</th>
						<th>计划单价</th>
						<th>实际单价</th>
						<th>实际金额</th>
					</tr>
				</thead>
				<tbody id="billDetailList">
				</tbody>
			</table>
			<script type="text/template" id="billDetailTpl">//<!--
				<tr id="billDetailList{{idx}}">
					<td class="hide">
						<input id="billDetailList{{idx}}_id" name="billDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="billDetailList{{idx}}_delFlag" name="billDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="billDetailList{{idx}}_serialNum" name="billDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"   class="form-control " readonly = "true"/>
					</td>
					
					<td>
						<input id="billDetailList{{idx}}_item.id" name="billDetailList[{{idx}}].item.id" type="text" value="{{row.item.id}}"   class="form-control " readonly = "true"/>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemName" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"   class="form-control " readonly = "true"/>
					</td>
					
					<td>
						<input id="billDetailList{{idx}}_itemSpec" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"   class="form-control " readonly = "true"/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_measUnit" name="billDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"   class="form-control " readonly = "true"/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemQty" name="billDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control " readonly = "true"/>
					</td>

					<td>
						<input id="billDetailList{{idx}}_.bin.binId" name="billDetailList[{{idx}}]..bin.binId" type="text" value="{{row..bin.binId}}"    class="form-control " readonly = "true"/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_binName" name="billDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"    class="form-control " readonly = "true"/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_.loc.locId" name="billDetailList[{{idx}}].loc.locId" type="text" value="{{row.loc.locId}}"    class="form-control " readonly = "true"/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_locName" name="billDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"    class="form-control " readonly = "true"/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_planPrice" name="billDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"    class="form-control " readonly = "true"/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemPrice" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"    class="form-control " readonly = "true"/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemSum" name="billDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control " readonly = "true"/>
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var billDetailRowIdx = 0, billDetailTpl = $("#billDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(billMain.billDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#billDetailList', billDetailRowIdx, billDetailTpl, data[i]);
						billDetailRowIdx = billDetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
				
			<div class="col-lg-3"></div>
	        <div class="col-lg-6">
	             <div class="form-group text-center">
	             	<div class="col-lg-2"></div>
	                 <div class="col-lg-3">
	                     <button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在稽核...">稽核</button>
	                     <a href= "${ctx}/cosbillrecord/cosBillRecord/list2" class="btn btn-primary btn-lg btn-parsley">返回</a>
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
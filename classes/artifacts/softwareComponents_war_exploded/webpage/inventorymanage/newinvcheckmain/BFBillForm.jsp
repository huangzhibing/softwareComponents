<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>单据管理</title>
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
			
	        $('#billDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#recDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#operDate').datetimepicker({
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
				<a class="panelButton" href="${ctx}/newinvcheckmain/newinvCheckMain/BFSearch"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="billMain" action="${ctx}/billmain/billMain/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据号：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据类型：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/billtype/billType/data" id="io" name="io.ioType" value="${billMain.io.ioType}" labelName="io.ioType" labelValue="${billMain.io.ioType}"
							 title="选择单据类型" cssClass="form-control required" fieldLabels="单据类型|单据名称" fieldKeys="ioType|ioDesc" searchLabels="单据类型|单据名称" searchKeys="ioType|ioDesc" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">经办人：</label>
					<div class="col-sm-10">
						<sys:userselect id="billPerson" name="billPerson.id" value="${billMain.billPerson.id}" labelName="billPerson.no" labelValue="${billMain.billPerson.no}"
							    cssClass="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">核算期间：</label>
					<div class="col-sm-10">
						<form:input path="period.id" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据说明：</label>
					<div class="col-sm-10">
						<form:input path="billDesc" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人编码：</label>
					<div class="col-sm-10">
						<sys:userselect id="billEmp" name="billEmp.no" value="${billMain.billEmp.no}" labelName="billEmp.no" labelValue="${billMain.billEmp.no}"
							    cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人：</label>
					<div class="col-sm-10">
						<form:input path="billEmpname" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
			<div class="tabs-container">
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">出入库单据子表：</a>
					</li>
					<li><a data-toggle="tab" href="#tab-2" aria-expanded="true">二维码信息表：</a>
					</li>
				</ul>
				<div class="tab-content">
					<div id="tab-1" class="tab-pane fade in  active">
						<a class="btn btn-white btn-sm"
						   onclick="addRow('#billDetailList', billDetailRowIdx, billDetailTpl);billDetailRowIdx = billDetailRowIdx + 1;"
						   title="新增"><i class="fa fa-plus"></i> 新增</a>
						<table class="table table-striped table-bordered table-condensed">
							<thead>
							<tr>
								<th class="hide"></th>
								<th><font color="red">*</font>序号</th>
								<th>物料名称</th>
								<th>物料规格</th>
								<th>数量</th>
								<th>实际单价</th>
								<th>实际金额</th>
								<th>物料批号</th>
								<th>计划单价</th>
								<th width="10">&nbsp;</th>
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
						<input id="billDetailList{{idx}}_serialNum" name="billDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>


					<td>
						<input id="billDetailList{{idx}}_itemName" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>


					<td>
						<input id="billDetailList{{idx}}_itemSpec" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>

					<td>
						<input id="billDetailList{{idx}}_itemQty" name="billDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control "/>
					</td>



					<td>
						<input id="billDetailList{{idx}}_itemPrice" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"    class="form-control "/>
					</td>


					<td>
						<input id="billDetailList{{idx}}_itemSum" name="billDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control "/>
					</td>


					<td>
						<input id="billDetailList{{idx}}_itemBatch" name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>




					<td>
						<input id="billDetailList{{idx}}_planPrice" name="billDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"    class="form-control "/>
					</td>

					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#billDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
						</script>
						<script type="text/javascript">
                            var billDetailRowIdx = 0,
                                billDetailTpl = $("#billDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
                            $(document).ready(function () {
                                var data = ${fns:toJson(billMain.billDetailList)};
                                for (var i = 0; i < data.length; i++) {
                                    addRow('#billDetailList', billDetailRowIdx, billDetailTpl, data[i]);
                                    billDetailRowIdx = billDetailRowIdx + 1;
                                }
                            });
						</script>
					</div>
					<div id="tab-2" class="tab-pane fade in ">
						<a class="btn btn-white btn-sm"
						   onclick="addRow('#billDetailCodeList', billDetailCodeRowIdx, billDetailCodeTpl);billDetailCodeRowIdx = billDetailCodeRowIdx + 1;"
						   title="新增"><i class="fa fa-plus"></i> 新增</a>
						<div class="table-responsive" style="max-height:500px">
							<table class="table table-striped table-bordered table-condensed"
								   style="min-width:1350px;">
								<thead>
								<tr>
									<th class="hide"></th>
									<th width="100px">序号</th>
									<th width="400px"><font color="red">*</font>二维码</th>
									<th width="200px">货区编号</th>
									<th width="200px">货区名称</th>
									<th width="200px">货位编号</th>
									<th width="200px">货位名称</th>
									<th width="10">&nbsp;</th>
								</tr>
								</thead>
								<tbody id="billDetailCodeList">
								</tbody>
							</table>
						</div>
						<script type="text/template" id="billDetailCodeTpl">//<!--
				<tr id="billDetailCodeList{{idx}}">
					<td class="hide">
						<input id="billDetailCodeList{{idx}}_id" name="billDetailCodeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="billDetailCodeList{{idx}}_delFlag" name="billDetailCodeList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<input readOnly="true" id="billDetailCodeList{{idx}}_serialNum" name="billDetailCodeList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control "/>
					</td>
					<td>
						<input  id="billDetailCodeList{{idx}}_itemBarcode" name="billDetailCodeList[{{idx}}].itemBarcode" type="text" value="{{row.itemBarcode}}"    class="form-control required"/>
					</td>
					<td>
						<sys:gridselect-modify url="${ctx}/bin/bin/data"  urlParamName="wareId" urlParamId="wareNames" id="billDetailCodeList{{idx}}_bin" name="billDetailCodeList[{{idx}}].bin.id" value="{{row.bin.id}}" labelName="billDetailCodeList{{idx}}.bin.binId" labelValue="{{row.bin.binId}}"
							 title="选择货区号" cssClass="form-control  " fieldLabels="货区编码|货区名称" fieldKeys="binId|binDesc" searchLabels="货区编码|货区名称" searchKeys="binId|binDesc"
							 targetId="billDetailCodeList{{idx}}_binName" targetField="binDesc" extraField="billDetailCodeList{{idx}}_bin:binId"></sys:gridselect-modify>
					</td>


					<td>
						<input id="billDetailCodeList{{idx}}_binName" name="billDetailCodeList[{{idx}}].binName" type="text" value="{{row.bin.binDesc}}"  readOnly="true"  class="form-control "/>
					</td>

                    <td style="display:none">
						<input style="" id="billDetailCodeList{{idx}}_bin" name="billDetailCodeList[{{idx}}].bin.binId" type="text" value="{{row.bin.binId}}"  readOnly="true"  class="form-control "/>
                    </td>

                    <td style="display:none">
						<input style="" id="billDetailCodeList{{idx}}_loc" name="billDetailCodeList[{{idx}}].loc.locId" type="text" value="{{row.loc.locId}}"  readOnly="true"  class="form-control "/>
                    </td>
					<td>
						<sys:gridselect-modify url="${ctx}/location/location/data" urlParamName="binId" urlParamId="billDetailCodeList{{idx}}_binNames" id="billDetailCodeList{{idx}}_loc" name="billDetailCodeList[{{idx}}].loc.id" value="{{row.loc.id}}" labelName="billDetailCodeList{{idx}}.loc.locId" labelValue="{{row.loc.id}}"
							 title="选择货位号" cssClass="form-control  " fieldLabels="货位编码|货位名称" fieldKeys="locId|locDesc" searchLabels="货位编码|货位名称" searchKeys="locId|locDesc"
							 targetId="billDetailCodeList{{idx}}_locName" targetField="locDesc" extraField="billDetailCodeList{{idx}}_loc:locId"></sys:gridselect-modify>
					</td>


					<td>
						<input id="billDetailCodeList{{idx}}_locName" name="billDetailCodeList[{{idx}}].locName" type="text" value="{{row.loc.locDesc   }}"  readOnly="true"   class="form-control "/>
					</td>
					<!--<td>
						<input readOnly="true" id="billDetailCodeList{{idx}}_bin.binId" name="billDetailCodeList[{{idx}}].bin.binId" type="text" value="{{row.bin.binId}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="true" id="billDetailCodeList{{idx}}_bin.binDesc" name="billDetailCodeList[{{idx}}].bin.binDesc" type="text" value="{{row.bin.binDesc}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="true" id="billDetailCodeList{{idx}}_loc.locId" name="billDetailCodeList[{{idx}}].loc.locId" type="text" value="{{row.loc.locId}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="true" id="billDetailCodeList{{idx}}_loc.locDesc" name="billDetailCodeList[{{idx}}].loc.locDesc" type="text" value="{{row.loc.locDesc}}"    class="form-control "/>
					</td>-->
						<td class="text-center" width="10">
							{{#delBtn}}<span class="close"
											 onclick="delRow(this, '#billDetailCodeList{{idx}}')"
											 title="删除">&times;</span>{{/delBtn}}
						</td>
						</tr>//-->
						</script>
						<script type="text/javascript">
                            var billDetailCodeRowIdx = 0,
                                billDetailCodeTpl = $("#billDetailCodeTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
                            $(document).ready(function () {
                                var data = ${fns:toJson(billMain.billDetailCodeList)};
                                console.log(data);
                                for (var i = 0; i < data.length; i++) {
                                    addRow('#billDetailCodeList', billDetailCodeRowIdx, billDetailCodeTpl, data[i]);
                                    billDetailCodeRowIdx = billDetailCodeRowIdx + 1;
                                }
                            });
						</script>
					</div>
				</div>
			</div>
			<c:if test="${fns:hasPermission('billmain:billMain:edit') || isAdd}">
				<div class="col-lg-3"></div>
				<div class="col-lg-6">
					<div class="form-group text-center">
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
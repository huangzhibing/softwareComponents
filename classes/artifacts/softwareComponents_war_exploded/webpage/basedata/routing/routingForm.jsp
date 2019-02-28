<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工艺路线管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
            $("#inputForm").validate({
                submitHandler: function(form){
                    var index=jp.loading();
                    form.submit()
                    var isAdd='${isAdd}';
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
<body>
<div class="wrapper wrapper-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">
						<a class="panelButton" href="${ctx}/routing/routing"><i class="ti-angle-left"></i> 返回</a>
					</h3>
				</div>
				<div class="panel-body">
					<form:form id="inputForm" modelAttribute="routing" action="${ctx}/routing/routing/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<sys:message content="${message}"/>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>产品代码：</label>
							<div class="col-sm-10">
								<c:if test="${not empty isAdd}">
									<sys:gridselect-item url="${ctx}/product/product/data" id="product" name="product.item.id" value="${routing.product.item.id}" labelName="product.item.code" labelValue="${routing.product.item.code}"
														 title="选择产品代码"  cssClass="form-control" fieldLabels="产品编码|产品名称" fieldKeys="item.code|itemNameRu" searchLabels="产品编码|产品名称" searchKeys="item.code|itemNameRu"
														 extraField="productNames:item.code;productName:itemNameRu" ></sys:gridselect-item>
								</c:if>
								<c:if test="${empty isAdd}">
								<sys:gridselect-item url="${ctx}/product/product/data" id="product" name="product.item.id" value="${routing.product.item.id}" labelName="product.item.code" labelValue="${routing.product.item.code}"
														title="选择产品代码" disabled="disabled" cssClass="form-control" fieldLabels="产品编码|产品名称" fieldKeys="item.code|itemNameRu" searchLabels="产品编码|产品名称" searchKeys="item.code|itemNameRu"
														extraField="productNames:item.code;productName:itemNameRu" ></sys:gridselect-item>
								</c:if>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>产品名称：</label>
							<div class="col-sm-10">
								<form:input path="productName" htmlEscape="false"    class="form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>工艺编码：</label>
							<div class="col-sm-10">
								<c:if test="${empty isAdd}">
									<form:input path="routingCode"  readOnly="readOnly" htmlEscape="false"    class="form-control required number"/>
								</c:if>
								<c:if test="${not empty isAdd}">
									<form:input path="routingCode" htmlEscape="false"    class="form-control required number"/>
								</c:if>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>工艺名称：</label>
							<div class="col-sm-10">
								<form:input path="routingName" htmlEscape="false"    class="form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">工艺序号：</label>
							<div class="col-sm-10">
								<form:input path="routingSeqNo" htmlEscape="false"    class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">工艺描述：</label>
							<div class="col-sm-10">
								<form:input path="routingDesc" htmlEscape="false"    class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>部门代码：</label>
							<div class="col-sm-10">
								<sys:treeselect-modify-code id="office" name="office.id" value="${routing.office.code}" labelName="office.code" labelValue="${routing.office.code}"
													   title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"
													   targetId="deptName" targetField="code"></sys:treeselect-modify-code>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>部门名称：</label>
							<div class="col-sm-10">
								<form:input path="deptName" htmlEscape="false"    class="form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>单位计件工资：</label>
							<div class="col-sm-10">
								<form:input path="unitWage" htmlEscape="false"    class="form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">是否为总装工艺：</label>
							<div class="col-sm-10">
								<form:select path="assembleflag"  class="form-control required">
									<form:option value="" label=""/>
									<form:option value="y" label="是"/>
									<form:option value="n" label="否"/>
								</form:select>
							</div>
						</div>

						<div class="tabs-container">
							<ul class="nav nav-tabs">
								<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">工序：</a>
								</li>
								<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">工艺路线物料关系表：</a>
								</li>
							</ul>
							<div class="tab-content">
								<div id="tab-1" class="tab-pane fade in  active">
									<a class="btn btn-white btn-sm" onclick="addRow('#routingWorkList', routingWorkRowIdx, routingWorkTpl);routingWorkRowIdx = routingWorkRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
									<table class="table table-striped table-bordered table-condensed">
										<thead>
										<tr>
											<th class="hide"></th>
											<th><font color="red">*</font>工序编码</th>
											<th><font color="red">*</font>工序名称</th>
											<th><font color="red">*</font>工序序号</th>
											<th><font color="red">*</font>负责人名称</th>
											<th>工序描述</th>
											<th><font color="red">*</font>定额工时</th>
											<th width="10">&nbsp;</th>
										</tr>
										</thead>
										<tbody id="routingWorkList">
										</tbody>
									</table>
									<script type="text/template" id="routingWorkTpl">//<!--
				<tr id="routingWorkList{{idx}}">
					<td class="hide">
						<input id="routingWorkList{{idx}}_id" name="routingWorkList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="routingWorkList{{idx}}_delFlag" name="routingWorkList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<sys:gridselect-item url="${ctx}/workprocedure/workProcedure/data" id="routingWorkList{{idx}}_workProcedure" name="routingWorkList[{{idx}}].workProcedure.id" value="{{row.workProcedure.workProcedureId}}" labelName="routingWorkList[{{idx}}].workProcedure.workProcedureId" labelValue="{{row.workProcedure.workProcedureId}}"
							 title="选择工序编码" cssClass="form-control  required" fieldLabels="工序编码|工序名称" fieldKeys="workProcedureId|workProcedureName" searchLabels="工序编码|工序名称" searchKeys="workProcedureId|workProcedureName"
							 targetId="" targetField=""
							 extraField="routingWorkList{{idx}}_workProcedureName:workProcedureName;routingWorkList{{idx}}_workPersonName:userNameRu;routingWorkList{{idx}}_workProcedureDesc:workProcedureDesc;routingWorkList{{idx}}_workTime:workTime"
							 limite="officeId:deptCode.id" ></sys:gridselect-item>
					</td>


					<td>
						<input readonly="readOnly" id="routingWorkList{{idx}}_workProcedureName" name="routingWorkList[{{idx}}].workProcedureName" type="text" value="{{row.workProcedureName}}"    class="form-control required"/>
					</td>

					<td>
						<input id="routingWorkList{{idx}}_workPorcedureSeqNo" name="routingWorkList[{{idx}}].workPorcedureSeqNo" type="text" value="{{row.workPorcedureSeqNo}}"    class="form-control required"/>
					</td>

					<td>
						<input readonly="readOnly" id="routingWorkList{{idx}}_workPersonName" name="routingWorkList[{{idx}}].workPersonName" type="text" value="{{row.workPersonName}}"    class="form-control required"/>
					</td>


					<td>
						<input readonly="readOnly" id="routingWorkList{{idx}}_workProcedureDesc" name="routingWorkList[{{idx}}].workProcedureDesc" type="text" value="{{row.workProcedureDesc}}"    class="form-control "/>
					</td>


					<td>
						<input readonly="readOnly" id="routingWorkList{{idx}}_workTime" name="routingWorkList[{{idx}}].workTime" type="text" value="{{row.workTime}}"    class="form-control required"/>
					</td>

					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#routingWorkList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
									</script>
									<script type="text/javascript">
                                        var routingWorkRowIdx = 0, routingWorkTpl = $("#routingWorkTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                                        $(document).ready(function() {
                                            var data = ${fns:toJson(routing.routingWorkList)};
                                            for (var i=0; i<data.length; i++){
                                                addRow('#routingWorkList', routingWorkRowIdx, routingWorkTpl, data[i]);
                                                routingWorkRowIdx = routingWorkRowIdx + 1;
                                            }
                                        });
									</script>
								</div>

								<div id="tab-2" class="tab-pane fade">
										<a class="btn btn-white btn-sm" onclick="addRow('#routingItemList', routingItemRowIdx, routingItemTpl);routingItemRowIdx = routingItemRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
										<table class="table table-striped table-bordered table-condensed">
											<thead>
											<tr>
												<th class="hide"></th>
												<th><font color="red">*</font>物料编码</th>
												<th><font color="red">*</font>物料名称</th>
												<th><font color="red">*</font>需求数量</th>
												<th>备注</th>
												<th width="10">&nbsp;</th>
											</tr>
											</thead>
											<tbody id="routingItemList">
											</tbody>
										</table>
										<script type="text/template" id="routingItemTpl">//<!--
				<tr id="routingItemList{{idx}}">
					<td class="hide">
						<input id="routingItemList{{idx}}_id" name="routingItemList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="routingItemList{{idx}}_delFlag" name="routingItemList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<sys:gridselect-item url="${ctx}/item/item/data3" id="routingItemList{{idx}}_item" name="routingItemList[{{idx}}].item.id" value="{{row.item.code}}" labelName="routingItemList[{{idx}}].item.code" labelValue="{{row.item.code}}"
							 title="选择物料代码" cssClass="form-control  required"
							  fieldLabels="物料代码|物料名称" fieldKeys="code|name" searchLabels="物料名称" searchKeys="name"
							  extraField="routingItemList{{idx}}_itemName:name"
							 ></sys:gridselect-item>
					</td>

					<td>
						<input readOnly="readOnly" id="routingItemList{{idx}}_itemName" name="routingItemList[{{idx}}].itemName" type="text" value="{{row.item.name}}"    class="form-control required"/>
					</td>

					<td>
						<input id="routingItemList{{idx}}_reqQty" name="routingItemList[{{idx}}].reqQty" type="text" value="{{row.reqQty}}"    class="form-control required"/>
					</td>


					<td>
						<input id="routingItemList{{idx}}_remark" name="routingItemList[{{idx}}].remark" type="text" value="{{row.remark}}"    class="form-control "/>
					</td>

					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#routingItemList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
				</script>
				<script type="text/javascript">
					var routingItemRowIdx = 0, routingItemTpl = $("#routingItemTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
					$(document).ready(function() {
						var data = ${fns:toJson(routing.routingItemList)};
						for (var i=0; i<data.length; i++){
							addRow('#routingItemList', routingItemRowIdx, routingItemTpl, data[i]);
							routingItemRowIdx = routingItemRowIdx + 1;
						}
					});
				</script>
							</div>
							</div>
						</div>
						<c:if test="${fns:hasPermission('routing:routing:edit') || isAdd}">
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
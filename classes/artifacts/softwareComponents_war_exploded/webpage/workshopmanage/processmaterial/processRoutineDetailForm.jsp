<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车间物料安装单管理</title>
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
				 format: "YYYY-MM-DD HH:mm:ss"
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
				<a class="panelButton" href="${ctx}/processmaterial/processRoutineDetail"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="processRoutineDetail" action="${ctx}/processmaterial/processRoutineDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	

				<div class="form-group">
					<label class="col-sm-2 control-label">加工路线单号：</label>
					<div class="col-sm-3">
						<form:input path="routinebillno" htmlEscape="false"  readonly="true"  class="form-control "/>
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
						<form:input path="prodname" htmlEscape="false" readonly="true"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工艺编码：</label>
					<div class="col-sm-3">
						<form:input path="routingcode" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">工艺名称：</label>
					<div class="col-sm-3">
						<form:input path="routingname" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">工作中心代码：</label>
					<div class="col-sm-3">
						<form:input path="centercode.centerCode" htmlEscape="false" readonly="true"   class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">生产日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
						<div class='input-group form_datetime' id='planBDate'>
							<input type='text'  name="planbdate" class="form-control " readonly="true" value="<fmt:formatDate value="${processRoutineDetail.planbdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
						</div>
						</p>
					</div>
				</div>







				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-8">
						<form:input path="remarks" htmlEscape="false" readonly="true"   class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">车间物料安装单：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#processMaterialList', processMaterialRowIdx, processMaterialTpl);processMaterialRowIdx = processMaterialRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>

						<th><font color="red">*</font>加工路线单号</th>
						<th><font color="red">*</font>单件序号</th>
						<th><font color="red">*</font>工艺编码</th>
						<th><font color="red">*</font>物料编码</th>
						<th><font color="red">*</font>物料名称</th>
						<th><font color="red">*</font>工作中心代码</th>
						<th><font color="red">*</font>需安装数量</th>

						<th>备注信息</th>
					</tr>
				</thead>
				<tbody id="processMaterialList">
				</tbody>
			</table>
			<script type="text/template" id="processMaterialTpl">//<!--
				<tr id="processMaterialList{{idx}}">
					<td class="hide">
						<input id="processMaterialList{{idx}}_id" name="processMaterialList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="processMaterialList{{idx}}_delFlag" name="processMaterialList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					

					
					
					<td>
						<input id="processMaterialList{{idx}}_routineBillNo" name="processMaterialList[{{idx}}].routineBillNo" type="text" readonly="true" value="{{row.routineBillNo}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="processMaterialList{{idx}}_seqNo" name="processMaterialList[{{idx}}].seqNo" type="text" value="{{row.seqNo}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="processMaterialList{{idx}}_routingCode" name="processMaterialList[{{idx}}].routingCode" type="text" value="{{row.routingCode}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="processMaterialList{{idx}}_itemCode" name="processMaterialList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"  readonly="true"  class="form-control required"/>
					</td>

					<td>
						<input id="processMaterialList{{idx}}_itemName" name="processMaterialList[{{idx}}].itemName" type="text" value="{{row.itemName}}"  readonly="true"  class="form-control required"/>
					</td>

					<td>
						<input id="processMaterialList{{idx}}_centerCode" name="processMaterialList[{{idx}}].centerCode" type="text" value="{{row.centerCode}}"  readonly="true"  class="form-control required"/>
					</td>

					<td>
						<input id="processMaterialList{{idx}}_assembleQty" name="processMaterialList[{{idx}}].assembleQty" type="text" value="{{row.assembleQty}}"  readonly="true"  class="form-control required"/>
					</td>
					
					


					<td>
						<textarea id="processMaterialList{{idx}}_remarks" name="processMaterialList[{{idx}}].remarks" rows="1"  readonly="true"  class="form-control ">{{row.remarks}}</textarea>
					</td>


				</tr>//-->
			</script>
			<script type="text/javascript">
				var processMaterialRowIdx = 0, processMaterialTpl = $("#processMaterialTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(processRoutineDetail.processMaterialList)};
					for (var i=0; i<data.length; i++){
						addRow('#processMaterialList', processMaterialRowIdx, processMaterialTpl, data[i]);
						processMaterialRowIdx = processMaterialRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<%--<c:if test="${fns:hasPermission('processmaterial:processRoutineDetail:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                 </div>
		             </div>
		        </div>
		</c:if>--%>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
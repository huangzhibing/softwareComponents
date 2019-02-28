<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>领料单编辑</title>
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
			
	        $('#editDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#operDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });


            $('#wareIdId').change(function () {
                $.ajax({
                    url:"${ctx}/inboundinput/inboundInput/getEmp",
                    data:{wareId:$('#wareId').val()},
                    dataType:'json',
                    success:function(re){
                        console.log("getWareEmpSuccess");
                        $('#wareEmpid').val(re.user.no);
                        $('#wareEmpname').val(re.empName);
                    }
                });
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


		function button_save(){
		    $("#billStateFlag").val("P");
		    $("#inputForm").submit();
		}
        function button_submit(){
			jp.confirm("确认提交？",function () {
                $("#billStateFlag").val("C");
			    $('#inputForm').submit();
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
				<a class="panelButton" href="${ctx}/materialorder/sfcMaterialOrder/editList"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="sfcMaterialOrder" action="${ctx}/materialorder/sfcMaterialOrder/editSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="billStateFlag"/>
		<sys:message content="${message}"/>

			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>单据号：</label>
				<div class="col-sm-3">
					<form:input path="materialOrder" htmlEscape="false"  readonly="true"  class="form-control required"/>
				</div>


				<label class="col-sm-2 control-label"><font color="red">*</font>制单人名称：</label>
				<div class="col-sm-3">
					<form:input path="editor" htmlEscape="false"  readonly="true"  class="form-control required"/>
				</div>

			</div>
			<%--<div class="form-group">
                <label class="col-sm-2 control-label">单据类型：</label>
                <div class="col-sm-10">
                    <form:input path="billType" htmlEscape="false"  readonly="true"  class="form-control "/>
                </div>
            </div>--%>


			<div class="form-group">

				<label class="col-sm-2 control-label"><font color="red">*</font>工作中心编码：</label>
				<div class="col-sm-3">
					<sys:gridselect-modify-code  url="${ctx}/workcenter/workCenter/data" id="operCode" name="operCode" value="${sfcMaterialOrder.operCode}" labelName=".centerCode" labelValue="${sfcMaterialOrder.operCode}"
										title="选择工作中心" cssClass="form-control required" fieldLabels="工作中心编号|工作中心名称" fieldKeys="centerCode|centerName" searchLabels="工作中心编号|工作中心名称" searchKeys="centerCode|centerName"
									extraField="operName:centerName;operCodeId:operCode"   targetField="" targetId=""></sys:gridselect-modify-code>

					<%--<form:input path="operCode" htmlEscape="false"    class="form-control required"/>--%>
				</div>

				<label class="col-sm-2 control-label"><font color="red">*</font>工作中心名称：</label>
				<div class="col-sm-3">
					<form:input path="operName" htmlEscape="false"   class="form-control required"/>
				</div>


			</div>
			<div class="form-group">

			</div>
			<div class="form-group">

				<%--<label class="col-sm-2 control-label"><font color="red">*</font>领料人代码：</label>
				<div class="col-sm-3">
					<form:input path="responser" htmlEscape="false"    class="form-control required"/>
				</div>--%>
				<label class="col-sm-2 control-label"><font color="red">*</font>领料人姓名：</label>
				<div class="col-sm-3">
					<form:input path="respName" htmlEscape="false"    class="form-control required"/>
				</div>

				<label class="col-sm-2 control-label"><font color="red">*</font>部门名称：</label>
				<div class="col-sm-3">
					<form:input path="shopName" htmlEscape="false"   class="form-control required"/>
				</div>
			</div>

			<div class="form-group">


				<label class="col-sm-2 control-label"><font color="red">*</font>领料日期：</label>
				<div class="col-sm-3">
					<p class="input-group">
					<div class='input-group form_datetime' id='operDate'>
						<input type='text'  name="operDate" class="form-control required"  value="<fmt:formatDate value="${sfcMaterialOrder.operDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
						<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
					</div>
					</p>
				</div>


				<label class="col-sm-2 control-label"><font color="red">*</font>制单日期：</label>
				<div class="col-sm-3">
					<p class="input-group">
					<div class='input-group form_datetime' id='editDate'>
						<input type='text'  name="editDate" class="form-control required"  value="<fmt:formatDate value="${sfcMaterialOrder.editDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
						<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
					</div>
					</p>
				</div>





			</div>




			<div class="form-group">


				<label class="col-sm-2 control-label"><font color="red">*</font>核算期：</label>
				<div class="col-sm-3">
					<form:input path="periodId" htmlEscape="false"   class="form-control required"/>
				</div>

				<label class="col-sm-2 control-label"><font color="red">*</font>库房名称：</label>
				<div class="col-sm-3">
					<sys:gridselect-label1  url="${ctx}/warehouse/wareHouse/data" id="wareId"  labelName="wareName" labelValue="${sfcMaterialOrder.wareName}"
										    title="选择库房" cssClass="form-control required" fieldLabels="库房号|库房名称" fieldKeys="wareID|wareName" searchLabels="库房号|库房名称" searchKeys="wareID|wareName"
										  targetField="wareID" targetId="wareId" allowInput="false" labelNameForValue="ware.wareName"></sys:gridselect-label1>

					<form:hidden path="wareId"></form:hidden>
					<input id="wareIdId" type="hidden" />
				<%--<form:input path="wareName" htmlEscape="false"  class="form-control required"/>--%>
				</div>

			</div>
			<div class="form-group">
				<%--<label class="col-sm-2 control-label"><font color="red">*</font>库房代码：</label>
				<div class="col-sm-3">
					<form:input path="wareId" htmlEscape="false"    class="form-control "/>
				</div>
--%>

			</div>
			<div class="form-group">
				<%--<label class="col-sm-2 control-label"><font color="red">*</font>库管员编码：</label>
				<div class="col-sm-3">
					<form:input path="wareEmpid" htmlEscape="false"   class="form-control "/>
				</div>--%>

				<label class="col-sm-2 control-label"> 单据类型：</label>
				<div class="col-sm-3">
					<form:select path="receiveType" class="form-control" readonly="true" >
						<form:option value="" label=""/>
						<form:option value="C" label="系统生成"/>
						<form:option value="M" label="手工录入"/>
					</form:select>
				</div>

				<label class="col-sm-2 control-label"><font color="red">*</font>库管员名称：</label>
				<div class="col-sm-3">
					<form:input path="wareEmpname" htmlEscape="false"    class="form-control "/>
				</div>
				<form:hidden path="wareEmpid"></form:hidden>
			</div>


			<div class="form-group">
				<label class="col-sm-2 control-label">备注信息：</label>
				<div class="col-sm-8">
					<form:textarea path="remarks" htmlEscape="false" rows="4"     class="form-control "/>
				</div>
			</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">领料单子表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#sfcMaterialOrderDetailList', sfcMaterialOrderDetailRowIdx, sfcMaterialOrderDetailTpl);sfcMaterialOrderDetailRowIdx = sfcMaterialOrderDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>

						<th>序号</th>
						<th>材料代码</th>
						<th>材料名称</th>
						<th>材料规格</th>
						<th>计量单位</th>
						<th>申领数量</th>
						<th>实际价格</th>
						<th>实际金额</th>
						<%--<th>批次</th>
						<th>货区代码</th>--%>
						<th>货区名称</th>
			<%--			<th>货位代码</th>--%>
						<th>货位名称</th>
						<th>备注信息</th>
					</tr>
				</thead>
				<tbody id="sfcMaterialOrderDetailList">
				</tbody>
			</table>
			<script type="text/template" id="sfcMaterialOrderDetailTpl">//<!--
				<tr id="sfcMaterialOrderDetailList{{idx}}">
					<td class="hide">
						<input id="sfcMaterialOrderDetailList{{idx}}_id" name="sfcMaterialOrderDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="sfcMaterialOrderDetailList{{idx}}_delFlag" name="sfcMaterialOrderDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					
							<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_sequenceId" name="sfcMaterialOrderDetailList[{{idx}}].sequenceId" readonly="true" type="text" value="{{row.sequenceId}}"    class="form-control "/>
					</td>


					<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_materialId" name="sfcMaterialOrderDetailList[{{idx}}].materialId" type="text" value="{{row.materialId}}"  readonly="true"  class="form-control "/>
					</td>


					<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_materialName" name="sfcMaterialOrderDetailList[{{idx}}].materialName" type="text" value="{{row.materialName}}"   readonly="true" class="form-control "/>
					</td>


					<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_materialSpec" name="sfcMaterialOrderDetailList[{{idx}}].materialSpec" type="text" value="{{row.materialSpec}}"  readonly="true"  class="form-control "/>
					</td>


					<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_numUnit" name="sfcMaterialOrderDetailList[{{idx}}].numUnit" type="text" value="{{row.numUnit}}"   class="form-control "/>
					</td>


					<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_requireNum" name="sfcMaterialOrderDetailList[{{idx}}].requireNum" type="text" value="{{row.requireNum}}"   class="form-control required isFloatGteZero"/>
					</td>

				

					<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_realPrice" name="sfcMaterialOrderDetailList[{{idx}}].realPrice" type="text" value="{{row.realPrice}}"  class="form-control "/>
					</td>


					<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_realSum" name="sfcMaterialOrderDetailList[{{idx}}].realSum" type="text" value="{{row.realSum}}"  class="form-control "/>
					</td>


				<%--	<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_itemBatch" name="sfcMaterialOrderDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"   class="form-control "/>
					</td>


					<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_binId" name="sfcMaterialOrderDetailList[{{idx}}].binId" type="text" value="{{row.binId}}"   class="form-control "/>
					</td>--%>


					<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_binName" name="sfcMaterialOrderDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"  class="form-control "/>
					</td>


					<%--<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_locId" name="sfcMaterialOrderDetailList[{{idx}}].locId" type="text" value="{{row.locId}}"   class="form-control "/>
					</td>--%>


					<td>
						<input id="sfcMaterialOrderDetailList{{idx}}_locName" name="sfcMaterialOrderDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"   class="form-control "/>
					</td>

					<td>
						<textarea id="sfcMaterialOrderDetailList{{idx}}_remarks" name="sfcMaterialOrderDetailList[{{idx}}].remarks" rows="1"   class="form-control ">{{row.remarks}}</textarea>
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var sfcMaterialOrderDetailRowIdx = 0, sfcMaterialOrderDetailTpl = $("#sfcMaterialOrderDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(sfcMaterialOrder.sfcMaterialOrderDetailList)};
					for (var i=0; i<data.length; i++){
					    if(data[i].returnNum == undefined){
					        data[i].returnNum = 0;
                        }
						addRow('#sfcMaterialOrderDetailList', sfcMaterialOrderDetailRowIdx, sfcMaterialOrderDetailTpl, data[i]);
						sfcMaterialOrderDetailRowIdx = sfcMaterialOrderDetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('materialorder:sfcMaterialOrder:edit') || isAdd}">
				<div class="col-lg-2"></div>
				 <div class="col-lg-3">
		             <div class="form-group text-center">
		                 <div>
							 <input type="button" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="button_save()" data-loading-text="保存" value="保存"></input>
		                 </div>
		             </div>
		        </div>
			<div class="col-lg-1"></div>
		        <div class="col-lg-3">
		             <div class="form-group text-center">
		                 <div>
							 <input type="button" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="button_submit()" data-loading-text="提交" value="提交"></input>
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
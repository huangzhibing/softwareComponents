<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>完工入库通知单管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
				    jp.confirm("确认提交完工入库通知单？",function () {
                        jp.loading();
                        form.submit();
                    });

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
	        $('#finishDate').datetimepicker({
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
				<a class="panelButton" href="${ctx}/sfcinvcheckmain/sfcInvCheckMain"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="sfcInvCheckMain" action="${ctx}/sfcinvcheckmain/sfcInvCheckMain/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
					<div class="col-sm-3">
						<form:input path="billNo" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>

					<label class="col-sm-2 control-label">制单日期：</label>
					<div class="col-sm-3">
						<div class='input-group form_datetime' id='makeDate'>
							<input type='text'  name="makeDate" class="form-control " readonly="true" value="<fmt:formatDate value="${sfcInvCheckMain.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
						</div>
					</div>
				</div>

			<%--
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人编号：</label>
					<div class="col-sm-10">
						<form:input path="makepId" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>--%>


				<div class="form-group">
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-3">
						<form:select path="billStateFlag" class="form-control " disabled="true">
							<form:option value="" label=""></form:option>
							<form:options items="${fns:getDictList('invcheckmain_bill_status')}"   itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>

					<label class="col-sm-2 control-label">制单人名称：</label>
					<div class="col-sm-3">
						<form:input path="makepName" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<%--<label class="col-sm-2 control-label">完工人编号：</label>
					<div class="col-sm-10">
						<form:input path="workerpId" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>--%>
						<label class="col-sm-2 control-label">完工人：</label>
						<div class="col-sm-3">
							<form:input path="workerpName" htmlEscape="false"  readonly="true"  class="form-control "/>
						</div>

						<label class="col-sm-2 control-label">完工日期：</label>
						<div class="col-sm-3">
							<div class='input-group form_datetime' id='finishDate'>
								<input type='text'  name="finishDate" class="form-control " readonly="true" value="<fmt:formatDate value="${sfcInvCheckMain.finishDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
								<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
							</div>
						</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">工作中心编号：</label>
					<div class="col-sm-3">
						<form:input path="shopId" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">工作中心名称：</label>
					<div class="col-sm-3">
						<form:input path="shopName" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>


			<%--
				<div class="form-group">
					<label class="col-sm-2 control-label">库管员编号：</label>
					<div class="col-sm-3">
						<form:input path="warepId" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>--%>


				<div class="form-group">
					<label class="col-sm-2 control-label">仓库编号：</label>
					<div class="col-sm-3">
						<form:input path="wareId" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">仓库名称：</label>
					<div class="col-sm-3">
						<form:input path="wareName" htmlEscape="false"  readonly="true"  class="form-control"/>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label">库管员：</label>
					<div class="col-sm-3">
						<form:input path="warepName" htmlEscape="false"  readonly="true"  class="form-control"/>
					</div>
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-3">
						<form:input path="notes" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<%--<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>质检标志：</label>
					<div class="col-sm-10">
						<form:input path="qmsFlag" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>--%>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">完工入库通知单子表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#sfcInvCheckDetailList', sfcInvCheckDetailRowIdx, sfcInvCheckDetailTpl);sfcInvCheckDetailRowIdx = sfcInvCheckDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>序号</th>
						<th>机器序列号</th>
						<th>物料二维码</th>
						<th>产品编号</th>
						<th>产品名称</th>
						<th>产品规格</th>
						<th>计量单位</th>
						<th>实际数量</th>
						<th>备注</th>
						

					</tr>
				</thead>
				<tbody id="sfcInvCheckDetailList">
				</tbody>
			</table>
			<script type="text/template" id="sfcInvCheckDetailTpl">//<!--
				<tr id="sfcInvCheckDetailList{{idx}}">
					<td class="hide">
						<input id="sfcInvCheckDetailList{{idx}}_id" name="sfcInvCheckDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="sfcInvCheckDetailList{{idx}}_delFlag" name="sfcInvCheckDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_serialNo" name="sfcInvCheckDetailList[{{idx}}].serialNo" type="text" value="{{row.serialNo}}" readonly="true"   class="form-control required"/>
					</td>

					<td>
						<input id="sfcInvCheckDetailList{{idx}}_mSerialNo" name="sfcInvCheckDetailList[{{idx}}].mSerialNo" type="text" value="{{row.mSerialNo}}" readonly="true"   class="form-control"/>
					</td>

					<td>
						<input id="sfcInvCheckDetailList{{idx}}_objNo" name="sfcInvCheckDetailList[{{idx}}].objSn" type="text" value="{{row.objSn}}" readonly="true"   class="form-control"/>
					</td>
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_itemCode" name="sfcInvCheckDetailList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"  readonly="true"   class="form-control required"/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_itemName" name="sfcInvCheckDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}" readonly="true"    class="form-control "/>
					</td>
					
					
					<%--<td>
						<input id="sfcInvCheckDetailList{{idx}}_itemBarcode" name="sfcInvCheckDetailList[{{idx}}].itemBarcode" type="text" value="{{row.itemBarcode}}" readonly="true"    class="form-control "/>
					</td>--%>


					<td>
						<input id="sfcInvCheckDetailList{{idx}}_itemSpecmodel" name="sfcInvCheckDetailList[{{idx}}].itemSpecmodel" type="text" value="{{row.itemSpecmodel}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_unitCode" name="sfcInvCheckDetailList[{{idx}}].unitCode" type="text" value="{{row.unitCode}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_realQty" name="sfcInvCheckDetailList[{{idx}}].realQty" type="text" value="{{row.realQty}}"  readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_notes" name="sfcInvCheckDetailList[{{idx}}].notes" type="text" value="{{row.notes}}" readonly="true"    class="form-control "/>
					</td>
					


				</tr>//-->
			</script>
			<script type="text/javascript">
				var sfcInvCheckDetailRowIdx = 0, sfcInvCheckDetailTpl = $("#sfcInvCheckDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(sfcInvCheckMain.sfcInvCheckDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#sfcInvCheckDetailList', sfcInvCheckDetailRowIdx, sfcInvCheckDetailTpl, data[i]);
						sfcInvCheckDetailRowIdx = sfcInvCheckDetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('sfcinvcheckmain:sfcInvCheckMain:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <%--<c:if test="${isAdd}">
		                     	<button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在保存...">保存</button>
		                 	 </c:if>
		                 	 <c:if test="${!isAdd}">
		                 	 	<button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在保存...">保存</button>
		                 	 	<button type="button" class="btn btn-primary btn-lg btn-parsley" onclick="submitBill()">提交</button>
		                 	 </c:if>--%>

								 <c:if test="${isAdd}">
									 <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
								 </c:if>
								 <c:if test="${!isAdd}">
									 <input type="button" onclick="jp.warning('当前单据状态不能提交！')" class="btn btn-primary btn-block btn-lg btn-parsley" value="提 交"></input>
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
	jp.post("${ctx}/sfcinvcheckmain/sfcInvCheckMain/submit",$('#inputForm').serialize(),function(data){
		//form.submit();
    });
	jp.close();
}
</script>
</body>
</html>
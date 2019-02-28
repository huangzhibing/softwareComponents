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
	        $('#finishDate').datetimepicker({
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
				<a class="panelButton" href="${ctx}/sfcinvcheckmain/sfcInvCheckMain"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="sfcInvCheckMain" action="${ctx}/sfcinvcheckmain/sfcInvCheckMain/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">单据编号：</label>
					<div class="col-sm-10">
						<form:input path="billNo" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据类型：</label>
					<div class="col-sm-10">
						<form:input path="billType" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='makeDate'>
			                    <input type='text'  name="makeDate" class="form-control "  value="<fmt:formatDate value="${sfcInvCheckMain.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人编号：</label>
					<div class="col-sm-10">
						<form:input path="makepId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人名称：</label>
					<div class="col-sm-10">
						<form:input path="makepName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人说明：</label>
					<div class="col-sm-10">
						<form:input path="makeNotes" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据标志：</label>
					<div class="col-sm-10">
						<form:input path="billStateFlag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">完工人编号：</label>
					<div class="col-sm-10">
						<form:input path="workerpId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">完工人名称：</label>
					<div class="col-sm-10">
						<form:input path="workerpName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">完工日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='finishDate'>
			                    <input type='text'  name="finishDate" class="form-control "  value="<fmt:formatDate value="${sfcInvCheckMain.finishDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">车间编号：</label>
					<div class="col-sm-10">
						<form:input path="shopId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">车间名称：</label>
					<div class="col-sm-10">
						<form:input path="shopName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库管员编号：</label>
					<div class="col-sm-10">
						<form:input path="warepId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库管员名称：</label>
					<div class="col-sm-10">
						<form:input path="warepName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">仓库编号：</label>
					<div class="col-sm-10">
						<form:input path="wareId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">仓库名称：</label>
					<div class="col-sm-10">
						<form:input path="wareName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input path="notes" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库存处理标志：</label>
					<div class="col-sm-10">
						<form:input path="invFlag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">质检标志：</label>
					<div class="col-sm-10">
						<form:input path="qmsFlag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">金额合计：</label>
					<div class="col-sm-10">
						<form:input path="priceSum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">入库类型：</label>
					<div class="col-sm-10">
						<form:input path="ioType" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">入库标志：</label>
					<div class="col-sm-10">
						<form:input path="ioFlag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">入库类型描述：</label>
					<div class="col-sm-10">
						<form:input path="ioDesc" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">完工入库通知单子表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#sfcInvCheckDetailList', sfcInvCheckDetailRowIdx, sfcInvCheckDetailTpl);sfcInvCheckDetailRowIdx = sfcInvCheckDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>序号</th>
						<th>物料编号</th>
						<th>物料名称</th>
						<th>质量要求</th>
						<th>检验对象序列号</th>
						<th>物料二维码</th>
						<th>实际数量</th>
						<th>实际单价</th>
						<th>实际金额</th>
						<th>含税单价</th>
						<th>含税金额</th>
						<th>税额</th>
						<th>作废标志</th>
						<th>备注</th>
						<th>结算说明</th>
						<th>结算标志</th>
						<th>对应单号</th>
						<th>对应序号</th>
						<th>退货标志</th>
						<th>物料规格</th>
						<th>计量单位</th>
						<th>物料图号</th>
						<th width="10">&nbsp;</th>
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
						<input id="sfcInvCheckDetailList{{idx}}_seriaNo" name="sfcInvCheckDetailList[{{idx}}].seriaNo" type="text" value="{{row.seriaNo}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_itemCode" name="sfcInvCheckDetailList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_itemName" name="sfcInvCheckDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_massRequire" name="sfcInvCheckDetailList[{{idx}}].massRequire" type="text" value="{{row.massRequire}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_objSn" name="sfcInvCheckDetailList[{{idx}}].objSn" type="text" value="{{row.objSn}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_itemBarcode" name="sfcInvCheckDetailList[{{idx}}].itemBarcode" type="text" value="{{row.itemBarcode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_realQty" name="sfcInvCheckDetailList[{{idx}}].realQty" type="text" value="{{row.realQty}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_realPrice" name="sfcInvCheckDetailList[{{idx}}].realPrice" type="text" value="{{row.realPrice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_realSum" name="sfcInvCheckDetailList[{{idx}}].realSum" type="text" value="{{row.realSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_realPriceTaxed" name="sfcInvCheckDetailList[{{idx}}].realPriceTaxed" type="text" value="{{row.realPriceTaxed}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_realSumTaxed" name="sfcInvCheckDetailList[{{idx}}].realSumTaxed" type="text" value="{{row.realSumTaxed}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_taxSum" name="sfcInvCheckDetailList[{{idx}}].taxSum" type="text" value="{{row.taxSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_checkState" name="sfcInvCheckDetailList[{{idx}}].checkState" type="text" value="{{row.checkState}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_notes" name="sfcInvCheckDetailList[{{idx}}].notes" type="text" value="{{row.notes}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_balanceNotes" name="sfcInvCheckDetailList[{{idx}}].balanceNotes" type="text" value="{{row.balanceNotes}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_balanceFlag" name="sfcInvCheckDetailList[{{idx}}].balanceFlag" type="text" value="{{row.balanceFlag}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_corBillnum" name="sfcInvCheckDetailList[{{idx}}].corBillnum" type="text" value="{{row.corBillnum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_corSerialnum" name="sfcInvCheckDetailList[{{idx}}].corSerialnum" type="text" value="{{row.corSerialnum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_backFlag" name="sfcInvCheckDetailList[{{idx}}].backFlag" type="text" value="{{row.backFlag}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_itemSpecmodel" name="sfcInvCheckDetailList[{{idx}}].itemSpecmodel" type="text" value="{{row.itemSpecmodel}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_unitCode" name="sfcInvCheckDetailList[{{idx}}].unitCode" type="text" value="{{row.unitCode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sfcInvCheckDetailList{{idx}}_itemPdn" name="sfcInvCheckDetailList[{{idx}}].itemPdn" type="text" value="{{row.itemPdn}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#sfcInvCheckDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
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
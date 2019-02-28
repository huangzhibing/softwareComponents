<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>客户档案维护管理</title>
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
				<a class="panelButton" href="${ctx}/accountfile/fileSalaccount"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="fileSalaccount" action="${ctx}/accountfile/fileSalaccount/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>客户编码：</label>
					<div class="col-sm-10">
						<form:input path="accountCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>客户名称：</label>
					<div class="col-sm-10">
						<form:input path="accountName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>所属地区编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify url="${ctx}/areadef/areaDef/data" id="area" name="areaDef.id" value="${fileSalaccount.areaDef.id}" labelName="areaDef.areaCode" labelValue="${fileSalaccount.areaDef.areaCode}"
											   title="选择地区" cssClass="form-control required" fieldLabels="地区编码|地区名称" fieldKeys="areaCode|areaName" searchLabels="地区编码|地区名称" searchKeys="areaCode|areaName"
											   targetId="areaName" targetField="areaName"></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font> 所属地区名称：</label>
					<div class="col-sm-10">
						<form:input path="areaName" htmlEscape="false"  readOnly="readonly" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">企业性质：</label>
					<div class="col-sm-10">
						<form:input path="accountProp" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">法人代表：</label>
					<div class="col-sm-10">
						<form:input path="accountMgr" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>客户类别编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify url="${ctx}/accounttype/accountType2/data" id="subType" name="accountType2.id" value="${fileSalaccount.accountType2.id}" labelName="accountType2.accTypeCode" labelValue="${fileSalaccount.accountType2.accTypeCode}"
											   title="选择客户类别" cssClass="form-control required" fieldLabels="客户类别编码|客户类别名称" fieldKeys="accTypeCode|accTypeName" searchLabels="客户类别编码|客户类别名称" searchKeys="accTypeCode|accTypeName"
											   targetId="subTypeName" targetField="accTypeName"></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>客户类别名称：</label>
					<div class="col-sm-10">
						<form:input path="subTypeName" htmlEscape="false" readOnly="readonly"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">上级部门：</label>
					<div class="col-sm-10">
						<form:input path="supHigherUp" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">联系地址：</label>
					<div class="col-sm-10">
						<form:input path="accountAddr" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">邮编：</label>
					<div class="col-sm-10">
						<form:input path="postCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">详细信息</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">联系人</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">合同信息</a>
				</li>
				<li class=""><a data-toggle="tab" href="#tab-4" aria-expanded="false">发货信息</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
					<div class="form-group">
						<label class="col-sm-2 control-label pull-left">电话：</label>
						<div class="col-sm-2">
							<form:input path="telNum" htmlEscape="false"    class="form-control "/>
						</div>
						<label class="col-sm-2 control-label pull-left">传真：</label>
						<div class="col-sm-2">
							<form:input path="faxNum" htmlEscape="false"    class="form-control "/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">开户银行：</label>
						<div class="col-sm-2">
							<form:input path="bankName" htmlEscape="false"    class="form-control "/>
						</div>
						<label class="col-sm-2 control-label">银行账号：</label>
						<div class="col-sm-2">
							<form:input path="bankAccount" htmlEscape="false"    class="form-control "/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">纳税登记号：</label>
						<div class="col-sm-2">
							<form:input path="taxCode" htmlEscape="false"    class="form-control "/>
						</div>
						<label class="col-sm-2 control-label">状态：</label>
						<div class="col-sm-2">
							<form:input path="state" htmlEscape="false"    class="form-control "/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">综合评估：</label>
						<div class="col-sm-2">
							<form:input path="supEvalation" htmlEscape="false"    class="form-control "/>
						</div>
						<label class="col-sm-2 control-label">说明：</label>
						<div class="col-sm-2">
							<form:input path="supNotes" htmlEscape="false"    class="form-control "/>
						</div>
					</div>






			</div>
				<div id="tab-2" class="tab-pane fade">
						<%--<a class="btn btn-white btn-sm" onclick="addRow('#fileLinkManList', fileLinkManRowIdx, fileLinkManTpl);fileLinkManRowIdx = fileLinkManRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
					<table class="table table-striped table-bordered table-condensed">
						<thead>
						<tr>
							<th class="hide"></th>
							<th>联系人编码</th>
							<th>联系人名称</th>
							<th>性别</th>
							<th>电话</th>
							<th>手机</th>
							<th>mail</th>
							<th>传真</th>
							<th>备注</th>
							<th width="10">&nbsp;</th>
						</tr>
						</thead>
						<tbody id="fileLinkManList">
						</tbody>
					</table>
					<script type="text/template" id="fileLinkManTpl">//<!--
				<tr id="fileLinkManList{{idx}}">
					<td class="hide">
						<input id="fileLinkManList{{idx}}_id" name="fileLinkManList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="fileLinkManList{{idx}}_delFlag" name="fileLinkManList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<input id="fileLinkManList{{idx}}_linkManCode" name="fileLinkManList[{{idx}}].linkManCode" type="text" value="{{row.linkManCode}}"    class="form-control required"/>
					</td>


					<td>
						<input id="fileLinkManList{{idx}}_linkManName" name="fileLinkManList[{{idx}}].linkManName" type="text" value="{{row.linkManName}}"    class="form-control required"/>
					</td>


					<td>
						<input id="fileLinkManList{{idx}}_linkManSex" name="fileLinkManList[{{idx}}].linkManSex" type="text" value="{{row.linkManSex}}"    class="form-control required"/>
					</td>


					<td>
						<input id="fileLinkManList{{idx}}_linkManTel" name="fileLinkManList[{{idx}}].linkManTel" type="text" value="{{row.linkManTel}}"    class="form-control "/>
					</td>


					<td>
						<input id="fileLinkManList{{idx}}_linkManMobile" name="fileLinkManList[{{idx}}].linkManMobile" type="text" value="{{row.linkManMobile}}"    class="form-control "/>
					</td>


					<td>
						<input id="fileLinkManList{{idx}}_linkManEmail" name="fileLinkManList[{{idx}}].linkManEmail" type="text" value="{{row.linkManEmail}}"    class="form-control "/>
					</td>


					<td>
						<input id="fileLinkManList{{idx}}_linkManFax" name="fileLinkManList[{{idx}}].linkManFax" type="text" value="{{row.linkManFax}}"    class="form-control "/>
					</td>


					<td>
						<input id="fileLinkManList{{idx}}_remark" name="fileLinkManList[{{idx}}].remark" type="text" value="{{row.remark}}"    class="form-control "/>
					</td>


				</tr>//-->
					</script>
					<script type="text/javascript">
                        var fileLinkManRowIdx = 0, fileLinkManTpl = $("#fileLinkManTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                        $(document).ready(function() {
                            var data = ${fns:toJson(fileSalaccount.fileLinkManList)};
                            for (var i=0; i<data.length; i++) {
                                addRow('#fileLinkManList', fileLinkManRowIdx, fileLinkManTpl, data[i]);
                                fileLinkManRowIdx = fileLinkManRowIdx + 1;
                            }
                            $(".table input").attr("readOnly",true)
                        });
					</script>
				</div>

				<div id="tab-3" class="tab-pane fade">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#fileContractList', fileContractRowIdx, fileContractTpl);fileContractRowIdx = fileContractRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>合同号</th>
						<th>签订日期</th>
						<th>合同类型</th>
						<th>客户名称</th>
						<th>销售人员</th>
						<th>合同状态</th>
						<th>结案人姓名</th>
						<th>运费总额</th>
						<th>不含税总额</th>
						<th>含税总额</th>
						<th>说明</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="fileContractList">
				</tbody>
			</table>
			<script type="text/template" id="fileContractTpl">//<!--
				<tr id="fileContractList{{idx}}">
					<td class="hide">
						<input id="fileContractList{{idx}}_id" name="fileContractList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="fileContractList{{idx}}_delFlag" name="fileContractList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="fileContractList{{idx}}_contractCode" name="fileContractList[{{idx}}].contractCode" type="text" value="{{row.contractCode}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<div class='input-group form_datetime' id="fileContractList{{idx}}_signDate">
		                    <input type='text'  name="fileContractList[{{idx}}].signDate" class="form-control "  value="{{row.signDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>
					
					
					<td>
						<input id="fileContractList{{idx}}_contractTypeName" name="fileContractList[{{idx}}].contractTypeName" type="text" value="{{row.contractTypeName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="fileContractList{{idx}}_accountName" name="fileContractList[{{idx}}].accountName" type="text" value="{{row.accountName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="fileContractList{{idx}}_personName" name="fileContractList[{{idx}}].personName" type="text" value="{{row.personName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="fileContractList{{idx}}_contractStat" name="fileContractList[{{idx}}].contractStat" type="text" value="{{row.contractStat}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="fileContractList{{idx}}_endPsnName" name="fileContractList[{{idx}}].endPsnName" type="text" value="{{row.endPsnName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="fileContractList{{idx}}_transfeeSum" name="fileContractList[{{idx}}].transfeeSum" type="text" value="{{row.transfeeSum}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<input id="fileContractList{{idx}}_goodsSum" name="fileContractList[{{idx}}].goodsSum" type="text" value="{{row.goodsSum}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<input id="fileContractList{{idx}}_goodsSumTaxed" name="fileContractList[{{idx}}].goodsSumTaxed" type="text" value="{{row.goodsSumTaxed}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<input id="fileContractList{{idx}}_praRemark" name="fileContractList[{{idx}}].praRemark" type="text" value="{{row.praRemark}}"    class="form-control "/>
					</td>
					

				</tr>//-->
			</script>
			<script type="text/javascript">
				var fileContractRowIdx = 0, fileContractTpl = $("#fileContractTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(fileSalaccount.fileContractList)};
					for (var i=0; i<data.length; i++){
						addRow('#fileContractList', fileContractRowIdx, fileContractTpl, data[i]);
						fileContractRowIdx = fileContractRowIdx + 1;
					}
                    $(".table input").attr("readOnly",true)
				});
			</script>
			</div>


				<div id="tab-4" class="tab-pane fade">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#filePickBillList', filePickBillRowIdx, filePickBillTpl);filePickBillRowIdx = filePickBillRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>发货单号</th>
						<th>制单人</th>
						<th>收货客户</th>
						<th>发货单状态</th>
						<th>承运人</th>
						<th>发货日期</th>
						<th>出库仓库</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="filePickBillList">
				</tbody>
			</table>
			<script type="text/template" id="filePickBillTpl">//<!--
				<tr id="filePickBillList{{idx}}">
					<td class="hide">
						<input id="filePickBillList{{idx}}_id" name="filePickBillList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="filePickBillList{{idx}}_delFlag" name="filePickBillList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="filePickBillList{{idx}}_pbillCode" name="filePickBillList[{{idx}}].pbillCode" type="text" value="{{row.pbillCode}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="filePickBillList{{idx}}_pbillPsnName" name="filePickBillList[{{idx}}].pbillPsnName" type="text" value="{{row.pbillPsnName}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="filePickBillList{{idx}}_rcvAccountName" name="filePickBillList[{{idx}}].rcvAccountName" type="text" value="{{row.rcvAccountName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<select id="filePickBillList{{idx}}_pbillStat" name="filePickBillList[{{idx}}].pbillStat" data-value="{{row.pbillStat}}" readOnly="true" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('pbillStat')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<input id="filePickBillList{{idx}}_transAccount" name="filePickBillList[{{idx}}].transAccount" type="text" value="{{row.transAccount}}"    class="form-control "/>
					</td>
					
					
					<td>
						<div class='input-group form_datetime' id="filePickBillList{{idx}}_pickDate">
		                    <input type='text'  name="filePickBillList[{{idx}}].pickDate" class="form-control "  value="{{row.pickDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>
					
					
					<td>
						<input id="filePickBillList{{idx}}_wareName" name="filePickBillList[{{idx}}].wareName" type="text" value="{{row.wareName}}"    class="form-control "/>
					</td>
					

				</tr>//-->
			</script>
			<script type="text/javascript">
				var filePickBillRowIdx = 0, filePickBillTpl = $("#filePickBillTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(fileSalaccount.filePickBillList)};
					for (var i=0; i<data.length; i++){
						addRow('#filePickBillList', filePickBillRowIdx, filePickBillTpl, data[i]);
						filePickBillRowIdx = filePickBillRowIdx + 1;
					}
                    $(".table input").attr("readOnly",true)
				});
			</script>
			</div>
			</div>
		</div>
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
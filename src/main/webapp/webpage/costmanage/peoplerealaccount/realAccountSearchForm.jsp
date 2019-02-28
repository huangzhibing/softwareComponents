<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>材料凭证单据查询</title>
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
			
	        $('#oriBillDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#makeDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#checkDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });

	        if($('#billNum').val() != ""){
	            $('#billNum').attr("readOnly",true);
			}

            $('input').attr("readOnly",true);

		});
		function initNo() {
			$("[id$='recNo']").each(function (index,element) {
				$(element).val(index+1);
            })
        }
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
			initNo();
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
			initNo();
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
				<a class="panelButton" href="${ctx}/peopleRealaccount/realAccount?flag=${flag}"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="realAccount" action="${ctx}/peopleRealaccount/realAccount/save?flag=${flag}" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">凭证号：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">会计期间：</label>
					<div class="col-sm-10">
						<form:input path="periodId" htmlEscape="false" readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">原始凭证号：</label>
					<div class="col-sm-10">
						<form:input path="oriBillId" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">原始单据日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
						<div class='input-group form_datetime' id='oriBillDate'>
							<input type='text'  name="oriBillDate" readonly="readonly" class="form-control "  value="<fmt:formatDate value="${realAccount.oriBillDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
						<form:input path="makeId" htmlEscape="false"  readonly="true" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人名称：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="makeName" htmlEscape="false" readonly="true" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='makeDate'>
			                    <input type='text' readonly="readonly" name="makeDate" class="form-control "  value="<fmt:formatDate value="${realAccount.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">制单方式：</label>
				<div class="col-sm-10">
					<form:select path="billMode" disabled="true" class="form-control ">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('billMode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">单据处理状态：</label>
				<div class="col-sm-10">
					<form:select path="cosBillNumStatus" disabled="true" class="form-control ">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('cosBillNumStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="tabs-container">
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">凭证附表：</a>
					</li>
					<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">人工工资：</a>
					</li>
				</ul>
				<div class="tab-content">
					<div id="tab-1" class="tab-pane fade in  active">
						<a class="btn btn-white btn-sm" onclick="addRow('#realAccountDetailList', realAccountDetailRowIdx, realAccountDetailTpl);realAccountDetailRowIdx = realAccountDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
						<div class="table-responsive" style="max-height:500px">
							<table class="table table-striped table-bordered table-condensed" style="min-width:1350px">
								<thead>
								<tr>
									<th class="hide"></th>
									<th>序号</th>
									<th><font color="red">*</font>科目代码</th>
									<th><font color="red">*</font>科目名称</th>
									<th><font color="red">*</font>科目金额</th>
									<th><font color="red">*</font>借贷方向</th>
									<th>摘要</th>
									<th>核算对象编号</th>
									<th>核算对象名称</th>
									<th>核算对象单位</th>
									<th>核算对象规格</th>
									<th>核算对象数量</th>
									<th width="10">&nbsp;</th>
								</tr>
								</thead>
								<tbody id="realAccountDetailList">
								</tbody>
							</table>
						</div>
						<script type="text/template" id="realAccountDetailTpl">//<!--
				<tr id="realAccountDetailList{{idx}}">
					<td class="hide">
						<input id="realAccountDetailList{{idx}}_id" name="realAccountDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="realAccountDetailList{{idx}}_delFlag" name="realAccountDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<input id="realAccountDetailList{{idx}}_recNo" name="realAccountDetailList[{{idx}}].recNo" type="text" value="{{row.recNo}}"  readOnly="true"  class="form-control "/>
					</td>


					<td>
                        <sys:gridselect-item title="选择科目" url="${ctx}/cositem/cosItem/data"  extraField="realAccountDetailList{{idx}}_itemName:itemName" id="realAccountDetailList{{idx}}_item_id" name="realAccountDetailList[{{idx}}].itemId.id" value="{{row.itemId.id}}" labelName="realAccountDetailList[{{idx}}].itemId.itemCode" labelValue="{{row.itemId.itemCode}}"
                                                                     cssClass="form-control" fieldKeys="itemCode|itemName" fieldLabels="科目编码|科目名称" searchKeys="itemCode|itemName" searchLabels="科目编码|科目名称"></sys:gridselect-item>

                    </td>


					<td>
						<input id="realAccountDetailList{{idx}}_itemName" name="realAccountDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control required"/>
					</td>


					<td>
						<input id="realAccountDetailList{{idx}}_itemSum" name="realAccountDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control required"/>
					</td>


					<td width="80px" >
						<select id="realAccountDetailList{{idx}}_blflag" name="realAccountDetailList[{{idx}}].blflag" data-value="{{row.blflag}}" readOnly="true" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('blflag')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>


					<td>
						<input id="realAccountDetailList{{idx}}_abst" name="realAccountDetailList[{{idx}}].abst" type="text" value="{{row.abst}}"    class="form-control "/>
					</td>


					<td>
                        <sys:gridselect-item title="选择核算对象" url="${ctx}/cosprodobject/cosProdObject/data"  extraField="realAccountDetailList{{idx}}_prodName:prodName;realAccountDetailList{{idx}}_prodUnit:unit;realAccountDetailList{{idx}}_prodSpec:specModel" id="realAccountDetailList{{idx}}_prod_id" name="realAccountDetailList[{{idx}}].cosProdObject.id" value="{{row.cosProdObject.id}}" labelName="realAccountDetailList[{{idx}}].cosProdObject.prodId" labelValue="{{row.cosProdObject.prodId}}"
                                                 cssClass="form-control" fieldKeys="prodId|prodName|prodNum|unit|specModel" fieldLabels="核算对象代码|核算对象名称|核算对象数量|单位|规格型号" searchKeys="prodId|prodName|prodNum|unit|specModel" searchLabels="核算对象代码|核算对象名称|核算对象数量|单位|规格型号"></sys:gridselect-item>
                    </td>


					<td>
						<input id="realAccountDetailList{{idx}}_prodName" name="realAccountDetailList[{{idx}}].prodName" readOnly="true" type="text" value="{{row.prodName}}"    class="form-control "/>
					</td>


					<td>
						<input id="realAccountDetailList{{idx}}_prodUnit" name="realAccountDetailList[{{idx}}].prodUnit" readOnly="true"  type="text" value="{{row.prodUnit}}"    class="form-control "/>
					</td>


					<td>
						<input id="realAccountDetailList{{idx}}_prodSpec" name="realAccountDetailList[{{idx}}].prodSpec"  readOnly="true" type="text" value="{{row.prodSpec}}"    class="form-control "/>
					</td>


					<td>
						<input id="realAccountDetailList{{idx}}_prodQty" name="realAccountDetailList[{{idx}}].prodQty" type="text" value="{{row.prodQty}}"    class="form-control "/>
					</td>

					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#realAccountDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
						</script>
						<script type="text/javascript">
                            var realAccountDetailRowIdx = 0, realAccountDetailTpl = $("#realAccountDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                            $(document).ready(function() {
                                var data = ${fns:toJson(realAccount.realAccountDetailList)};
                                for (var i=0; i<data.length; i++){
                                    addRow('#realAccountDetailList', realAccountDetailRowIdx, realAccountDetailTpl, data[i]);
                                    realAccountDetailRowIdx = realAccountDetailRowIdx + 1;
                                }
                            });
						</script>
					</div>
					<div id="tab-2" class="tab-pane fade">
							<%--<a class="btn btn-white btn-sm" onclick="addRow('#personWageList', personWageRowIdx, personWageTpl);personWageRowIdx = personWageRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
						<div class="table-responsive" style="max-height:500px">
							<table class="table table-striped table-bordered table-condensed" style="min-width:2400px">
								<thead>
								<tr>
									<th class="hide"></th>
									<th>工资单据号</th>
									<th>核算期</th>
									<th>工艺代码</th>
									<th>工艺名称</th>
									<th>班组代码</th>
									<th>班组名称</th>
									<th>产品代码</th>
									<th>产品名称</th>
									<th>单位计件工资</th>
									<th>工作量</th>
									<th>计件工资</th>
									<th>单位分配工资</th>
									<th>分配工资</th>
									<th>应付工资</th>
								</tr>
								</thead>
								<tbody id="personWageList">
								</tbody>
							</table>
						</div>
						<script type="text/template" id="personWageTpl">//<!--
				<tr id="personWageList{{idx}}">
					<td class="hide">
						<input id="personWageList{{idx}}_id" name="personWageList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="personWageList{{idx}}_delFlag" name="personWageList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td class="input-group">
						<input id="personWageList{{idx}}_billNum" name="personWageList[{{idx}}].billNum" type="text" value="{{row.billNum}}"  readOnly="false"  class="form-control "/>
						<span class="input-group-btn">
							<button type="button" id="jiagongDetail" class="btn btn-primary"><i class="fa fa-search"></i></button>
						</span>
					</td>

                    <td>
						<input id="personWageList{{idx}}_routing" name="personWageList[{{idx}}].routing.routingCode" type="text" value="{{row.periodId}}"    class="form-control "/>
					</td>

					<td>
						<input id="personWageList{{idx}}_routing" name="personWageList[{{idx}}].routing.routingCode" type="text" value="{{row.routing.routingCode}}"    class="form-control "/>
					</td>

					<td>
						<input id="personWageList{{idx}}_itemName" name="personWageList[{{idx}}].routing.routingName" type="text" value="{{row.routing.routingName}}"    class="form-control "/>
					</td>

					<td>
						<input id="personWageList{{idx}}_team" name="personWageList[{{idx}}].team.teamCode" type="text" value="{{row.team.teamCode}}"    class="form-control "/>
					</td>

					<td>
						<input id="personWageList{{idx}}_team" name="personWageList[{{idx}}].team.teamName" type="text" value="{{row.team.teamName}}"    class="form-control "/>
					</td>

					<td>
						<input id="personWageList{{idx}}_itemCode" name="personWageList[{{idx}}].itemCode" type="text" value="{{row.itemCode.item.code}}"    class="form-control "/>
					</td>

					<td>
						<input id="personWageList{{idx}}_itemCode" name="personWageList[{{idx}}].itemCode.itemName" type="text" value="{{row.itemCode.item.name}}"    class="form-control "/>
					</td>
					<td>
						<input id="personWageList{{idx}}_itemUnit" name="personWageList[{{idx}}].itemUnit" type="text" value="{{row.itemUnit}}"    class="form-control "/>
					</td>
					<td>
						<input id="personWageList{{idx}}_itemName" name="personWageList[{{idx}}].itemName" type="text" value="{{row.itemQty}}"    class="form-control "/>
					</td>
					<td>
						<input id="personWageList{{idx}}_wagePlan" name="personWageList[{{idx}}].wagePlan" type="text"    class="form-control "/>
					</td>
					<td>
						<input id="personWageList{{idx}}_wageUnit" name="personWageList[{{idx}}].wageUnit" type="text" value="{{row.wageUnit}}"    class="form-control"/>
					</td>
					<td>
						<input id="personWageList{{idx}}_wageAssign" name="personWageList[{{idx}}].wageAssign" type="text"    class="form-control "/>
					</td>
					<td>
						<input id="personWageList{{idx}}_wagePay" name="personWageList[{{idx}}].wagePay" type="text"    class="form-control "/>
					</td>

				</tr>//-->
						</script>
						<script type="text/javascript">
                            var personWageRowIdx = 0, personWageTpl = $("#personWageTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                            $(document).ready(function() {
                                var data = ${fns:toJson(realAccount.personWageList)};

                                for (var i=0; i<data.length; i++){
                                    addRow('#personWageList', personWageRowIdx, personWageTpl, data[i]);
                                    personWageRowIdx = personWageRowIdx + 1;
                                }
                                $('input').attr('readOnly',true);

                                $('#jiagongDetail').click(function () {
                                    top.layer.open({
                                        type:2,
                                        title:"加工路线单",
                                        content:"${ctx}/peopleRealaccount/realAccount/jiagong?billNum=${realAccount.personWageList[0].billNum}",
                                        shadeClose:false,
                                        shade:false,
                                        area:['70%','70%'],
                                        yes:function(index,layero){
                                            var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                                            var ifram= iframeWin.test();
                                            top.layer.close(index);//关闭对话框。
                                            jp.close();
                                        },
                                        cancel:function(index,layer){
                                            top.layer.close(index);//关闭对话框。
                                            jp.close();
                                        }
                                    });
                                })

                                var Qty = parseFloat("${realAccount.personWageList[0].itemQty}");
                                var Unit = "${realAccount.personWageList[0].itemUnit}";
                                var wUnit = "${realAccount.personWageList[0].wageUnit}";
                                var wPlan = Qty*Unit; var wAssign = Qty*wUnit;  var wPay = (Unit+wUnit)*Qty;
                                console.log($("input[id$='wagePlan']"));
                                $("input[id$='wagePlan']").val(wPlan.toFixed(2));
                                $("input[id$='wageAssign']").val(wAssign.toFixed(2));
                                $("input[id$='wagePay']").val(wPay.toFixed(2));
                                $("input[id$='wageUnit']").val((1*$("input[id$='wageUnit']").val()).toFixed(2));
                                // $("input[id$='wagePlan']").val(wPlan);
                                // $("input[id$='wageAssign']").val(wAssign);
                                // $("input[id$='wagePay']").val(wPay);
                            });
						</script>
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
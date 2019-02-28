<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>成品出库单据录入管理</title>
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
                        <a class="panelButton" href="${ctx}/outboundinput/outboundInput?type=Query"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="billMain" action="" method="post" class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message content="${message}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
                            <div class="col-sm-10">
                                <form:input path="billNum" htmlEscape="false"    class="form-control required"/>
                            </div>
                        </div>
                     <div class="form-group">
						<label class="col-sm-2 control-label"><font color="red">*</font>单据状态：</label>
						<div class="col-sm-10">
							<form:select path="billFlag" class="form-control required" disabled="true">
								<form:option value="" label=""/>
								<form:options items="${fns:getDictList('billFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
					</div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>出库日期：</label>
                            <div class="col-sm-10">
                                <p class="input-group">
                                <div class='input-group form_datetime' id='billDate'>
                                    <input type='text'  name="billDate" class="form-control required"  value="<fmt:formatDate value="${billMain.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
                                </div>
                                </p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>核算期间：</label>
                            <div class="col-sm-10">
                                <form:input path="period.periodId" htmlEscape="false"    class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                        <label class="col-sm-2 control-label"><font color="red">*</font>仓库编码：</label>
                        <div class="col-sm-10">
                            <sys:gridselect-modify url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${billMain.ware.id}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
                                            title="选择库房号" disabled="disabled" cssClass="form-control required" fieldLabels="库房号|库房名称" fieldKeys="wareID|wareName" searchLabels="库房号|库房名称" searchKeys="wareID|wareName"
                                            targetId="wareName" targetField="wareName"></sys:gridselect-modify>
                        </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>仓库名称：</label>
                            <div class="col-sm-10">
                                <form:input path="wareName" readonly="true" htmlEscape="false"    class="form-control required"/>
                            </div>
                        </div>
                         <div class="form-group">
							<label class="col-sm-2 control-label">客户编码：</label>
							<div class="col-sm-10">
								<sys:gridselect-item disabled="disabled"   extraField="accountId:accountCode;accountNames:accountCode;accountNamed:accountName"  url="${ctx}/account/account/data?accTypeNameRu=客户" id="account" name="account.id" value="${billMain.account.id}" labelName="" labelValue="${billMain.account.accountCode}"
									 title="选择客户编码" cssClass="form-control required" fieldLabels="客户编码|客户名称" fieldKeys="accountCode|accountName" searchLabels="客户编码|客户名称" searchKeys="accountCode|accountName" ></sys:gridselect-item>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">客户名称：</label>
							<div class="col-sm-10">
								<form:input id="accountNamed" readOnly="true" path="accountName" htmlEscape="false"    class="form-control "/>
							</div>
						</div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>部门编码：</label>
                            <div class="col-sm-10">
                                <sys:treeselect-modify id="dept" name="dept.id" value="${billMain.dept.id}" labelName="dept.code" labelValue="${billMain.dept.code}"
                                                title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"
                                                targetId="deptname" targetField="code" disabled="disabled"></sys:treeselect-modify>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>部门名称：</label>
                            <div class="col-sm-10">
                                <form:input id="deptname" readonly="true" path="deptName" htmlEscape="false"    class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>经办人：</label>
                            <div class="col-sm-10">
                                <sys:userselect id="billPerson" name="billPerson.id" value="${billMain.billPerson.id}" labelName="billPerson.no" labelValue="${billMain.billPerson.no}"
                                                cssClass="form-control " disabled="disabled"
                                                ></sys:userselect>
                            </div>
                        </div>
                         <div class="form-group">
							<label class="col-sm-2 control-label">库管员编码：</label>
							<div class="col-sm-10">
								<sys:gridselect-modify  labelField="user.no"  url="${ctx}/employee/employee/data" id="wareEmp" name="wareEmp.id" value="${billMain.wareEmp.id}" labelName="wareEmp.user.no" labelValue="${billMain.wareEmp.user.no}"
									 title="选择库管员代码" cssClass="form-control required" fieldLabels="库管员代码|库管员名称" fieldKeys="user.no|empName" searchLabels="库管员代码|库管员名称" searchKeys="user.no|empName" 
									 targetId="wareEmpname" targetField="empName" disabled="disabled"></sys:gridselect-modify>
							</div>
						</div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>库管员名称：</label>
                            <div class="col-sm-10">
                                <form:input path="wareEmpname" readonly="true"   class="form-control "/>
                            </div>
                        </div>
                         <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>收货地址：</label>
                            <div class="col-sm-10">
                                <form:input  path="rcvAddr"    class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>承运人：</label>
                            <div class="col-sm-10">
                                <form:input  path="transAccount"    class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>承运车号：</label>
                            <div class="col-sm-10">
                                <form:input  path="carNum"    class="form-control "/>
                            </div>
                        </div>
                        

                        <div class="tabs-container">
                            <ul class="nav nav-tabs">
                                <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">成品出库单据明细：</a>
                                </li>
                                <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="true">物料二维码：</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div id="tab-1" class="tab-pane fade in  active">
                                 <div class="table-responsive" style="max-height:500px">
                                    <table class="table table-striped table-bordered table-condensed" style="min-width:1350px">
                                        <thead>
                                        <tr>
                                            <th class="hide"></th>
                                            <th>编号</th>
                                            <th><font color="red">*</font>物料编码</th>
                                            <th><font color="red">*</font>物料名称</th>
                                            <th><font color="red">*</font>规格型号</th>
                                            <th><font color="red">*</font>数量</th>
                                            <th>单位</th>
                                            <th>货区编码</th>
                                            <th>货区名称</th>
                                            <th>货位编码</th>
                                            <th>货位名称</th>
                                            <th>批次</th>
                                            <th>计划单价</th>
                                            <th>实际单价</th>
                                            <th>实际金额</th>
                                            <th width="10">&nbsp;</th>
                                        </tr>
                                        </thead>
                                        <tbody id="billDetailList">
                                        </tbody>
                                    </table>
                                    </div>
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
						<sys:gridselect-modify url="${ctx}/item/item/data" id="billDetailList{{idx}}_item" name="billDetailList[{{idx}}].item.id" value="{{row.item.id}}" labelName="billDetailList{{idx}}.item.code" labelValue="{{row.item.id}}"
							 title="选择物料代码" cssClass="form-control  required"
							 targetId="billDetailList{{idx}}_itemName" targetField="name" fieldLabels="物料代码|物料名称|规格型号|单位" fieldKeys="code|name|specModel|unit" searchLabels="物料代码|物料名称|规格型号|单位" searchKeys="code|name|specModel|unit"
							 extraField="billDetailList{{idx}}_itemSpec:specModel;billDetailList{{idx}}_measUnit:unit" disabled="disabled"
							 ></sys:gridselect-modify>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemName" readonly="readonly" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemSpec" readonly="readonly" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>
					
					<td>
						<input id="billDetailList{{idx}}_itemQty" name="billDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control "/>
					</td>
					<td>
						<input id="billDetailList{{idx}}_measUnit" name="billDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"    class="form-control "/>
					</td>
					<td>
						<sys:gridselect-modify url="${ctx}/bin/bin/data" id="billDetailList{{idx}}_bin" name="billDetailList[{{idx}}].bin.id" value="{{row.bin.id}}" labelName="billDetailList{{idx}}.bin.binId" labelValue="{{row.bin.id}}"
							 title="选择货区号" cssClass="form-control  " fieldLabels="货区编码|货区名称" fieldKeys="binId|binDesc" searchLabels="货区编码|货区名称" searchKeys="binId|binDesc"
							 targetId="billDetailList{{idx}}_binName" targetField="binDesc" disabled="disabled"></sys:gridselect-modify>
					</td>


					<td>
						<input id="billDetailList{{idx}}_binName" readonly="readonly" name="billDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"    class="form-control "/>
					</td>


					<td>
						<sys:gridselect-modify url="${ctx}/location/location/data" id="billDetailList{{idx}}_loc" name="billDetailList[{{idx}}].loc.id" value="{{row.loc.id}}" labelName="billDetailList{{idx}}.loc.locId" labelValue="{{row.loc.id}}"
							 title="选择货位号" cssClass="form-control  " fieldLabels="货位编码|货位名称" fieldKeys="locId|locDesc" searchLabels="货位编码|货位名称" searchKeys="locId|locDesc"
							 targetId="billDetailList{{idx}}_locName" targetField="locDesc" disabled="disabled"></sys:gridselect-modify>
					</td>


					<td>
						<input id="billDetailList{{idx}}_locName" readonly="readonly" name="billDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"    class="form-control "/>
					</td>
                    <td>
						<input id="billDetailList{{idx}}_itemBatch" name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>
                    <td>
						<input id="billDetailList{{idx}}_planPrice" name="billDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"    class="form-control "/>
					</td>
                    <td>
						<input id="billDetailList{{idx}}_itemPrice" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"    class="form-control "/>
					</td>


					<td>
						<input id="billDetailList{{idx}}_itemSum" name="billDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control "/>
					</td>





					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#billDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
                                    </script>
                                    <script type="text/javascript">
                                        var billDetailRowIdx = 0, billDetailTpl = $("#billDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                                        $(document).ready(function() {
                                            var data = ${fns:toJson(billMain.billDetailList)};
                                            var data2 = ${fns:toJson(billMain.billDetailCodeList)};
                                            for (var i=0; i<data.length; i++){
                                                addRow('#billDetailList', billDetailRowIdx, billDetailTpl, data[i]);
                                                billDetailRowIdx = billDetailRowIdx + 1;
                                            }
                                            for (var i=0; i<data2.length; i++){
                                                addRow('#billDetailList2', billDetailRowIdx2, billDetailTpl2, data2[i]);
                                                billDetailRowIdx2 = billDetailRowIdx2 + 1;
                                            }
                                        });
                                    </script>
                                </div>
                                <div id="tab-2" class="tab-pane fade in">
                                    <a class="btn btn-white btn-sm" onclick="addRow('#billDetailList2', billDetailRowIdx2, billDetailTpl2);billDetailRowIdx2 = billDetailRowIdx2 + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
                                    <div class="table-responsive" style="max-height:500px">
                                        <table class="table table-striped table-bordered table-condensed" style="min-width:1350px">
                                            <thead>
                                            <tr>
                                                <th class="hide"></th>
                                                <th>序号</th>
                                                <th>二维码</th>
                                                <th>机台码</th>
                                                <th>机台激活码</th>
                                                <th width="10">&nbsp;</th>
                                            </tr>
                                            </thead>
                                            <tbody id="billDetailList2">
                                            </tbody>
                                        </table>
                                    </div>
                                    <script type="text/template" id="billDetailTpl2">
                                        //<!--
				<tr id="billDetailCodeList{{idx}}">
					<td class="hide">
						<input id="billDetailCodeList{{idx}}_id" name="billDetailCodeList[{{idx}}].id" type="hidden" value="{{}}"/>
						<input id="billDetailCodeList{{idx}}_delFlag" name="billDetailCodeList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<input id="billDetailCodeList{{idx}}_serialNum2" value="{{idx}}" readOnly="readOnly" name="billDetailCodeList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>

					<td>
						<input id="billDetailCodeList{{idx}}_itemBarcode" onchange="getCode(this,{{idx}})" name="billDetailCodeList[{{idx}}].itemBarcode" type="text" value="{{row.itemBarcode}}"    class="form-control "/>
					</td>
					<td>
						<input id="billDetailCodeList{{idx}}_machineBarcode" readonly="readonly" name="billDetailCodeList[{{idx}}].machineBarcode" type="text" value="{{row.machineBarcode}}"    class="form-control "/>
					</td>
					<td>
						<input id="billDetailCodeList{{idx}}_activationBarcode" readonly="readonly" name="billDetailCodeList[{{idx}}].activationBarcode" type="text" value="{{row.activationBarcode}}"    class="form-control "/>
					</td>
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#billDetailCodeList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
                                    </script>
                                    <script type="text/javascript">
                                        var billDetailRowIdx2 = 0,billDetailTpl2 = $("#billDetailTpl2").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                                    </script>
                                </div>
                            </div>
                        </div>
                    </form:form>
                    <div class="row">
                    	<div class="col-lg-3"></div>
                        <div class="col-lg-6">
							<a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/outboundinput/outboundInput?type=Query">返回</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
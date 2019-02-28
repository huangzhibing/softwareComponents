<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>成品退货单据管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        function getSum(i) {
            var a=Number($('#billDetailList'+i+'_itemQty').val())
            var b=Number($('#billDetailList'+i+'_itemPrice').val())
            $('#billDetailList'+i+'_itemSum').val(String(a*b))
        }
        $(document).ready(function() {
            if("p"=="${mtype}"){
                document.getElementById('inputForm').action="${ctx}/ocpth/Ocpth/post"
            }
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
            var id = '${billMain.billNum}';
            $.ajax({
                url:"${ctx}/item/item/getItemInfo",
                data:{billNum:id},
                dataType:'json',
                success:function (result) {
                    console.log(result);
                    var serId =0;
                    for(var i = 0;i<result.length;i++){
                        var list = "#billDetailCodeList";
                        result[i].serId = serId;
                        $(list).append(Mustache.render(billItemDetailTpl, {
                            idx: serId, delBtn: true, row: result[i]
                        }));
                        serId = serId+1;
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
                        <a class="panelButton" href="${ctx}/ocpth/Ocpth?mtype=${mtype}"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                        <form:form id="inputForm" modelAttribute="billMain"  action="${ctx}/ocpth/Ocpth/save" method="post" class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message content="${message}"/>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
                                <div class="col-sm-10">
                                    <form:input path="billNum" htmlEscape="false"  readonly="true"  class="form-control required"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>单据状态：</label>
                                <div class="col-sm-10">
                                    <form:select disabled="true" path="billFlag" class="form-control required">
                                        <form:option value="" label=""/>
                                        <form:options items="${fns:getDictList('billFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>入库日期：</label>
                                <div class="col-sm-10">
                                    <p class="input-group">
                                    <div class='input-group form_datetime' id='billDate'>
                                        <input type='text' readonly="readonly" name="billDate" class="form-control required"  value="<fmt:formatDate value="${billMain.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                        <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
                                    </div>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">选择到货单：</label>
                                <div class="col-sm-10 input-group">
                                    <form:input readonly="true" path="corBillNum"  htmlEscape="false"    class="form-control"/>
                                    <span class="input-group-btn">
                                        <button type="button"  class="btn   btn-primary  "><i class="fa fa-search"></i>
                                        </button>
                                   </span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>仓库编码：</label>
                                <div class="col-sm-10">
                                        <sys:gridselect-item  url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${billMain.ware.id}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
                                                              title="选择库房号" cssClass="form-control required" fieldLabels="库房号|库房名称" fieldKeys="wareID|wareName" searchLabels="库房号|库房名称" searchKeys="wareID|wareName"
                                                              targetId="wareName" targetField="wareName"></sys:gridselect-item>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>仓库名称：</label>
                                <div class="col-sm-10">
                                    <form:input path="wareName" readOnly="true"  htmlEscape="false"    class="form-control required"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>供应商编码：</label>
                                <div class="col-sm-10">
                                    <input id="accountNames" name="account.accountCode" readonly="readonly"
                                           type="text" value="${billMain.account.accountCode}" data-msg-required="" class="form-control required" style="" aria-required="true">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
                                <div class="col-sm-10">
                                    <form:input path="accountName" htmlEscape="false"  readonly="true"  class="form-control required"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>经办人：</label>
                                <div class="col-sm-10">
                                    <sys:userselect-modify targetId="aaa" disabled="disabled" id="billPerson" name="billPerson.no" value="${billMain.billPerson.no}" labelName="billPerson.loginName" labelValue="${billMain.billPerson.loginName}"
                                                           cssClass="form-control "/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>库管员编码：</label>
                                <div class="col-sm-10">
                                    <sys:gridselect-item  disabled="disabled"   url="${ctx}/employee/employee/data"  id="wareEmp" name="wareEmp.id" value="${billMain.wareEmp.id}" labelName="wareEmp.user.no" labelValue="${billMain.wareEmp.user.no}"
                                                          title="选择库管员代码" cssClass="form-control required" fieldLabels="库管员代码|库管员名称" fieldKeys="user.no|empName" searchLabels="库管员代码|库管员名称" searchKeys="user.no|empName"
                                                          targetId="wareEmpname" targetField="empName"></sys:gridselect-item>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>库管员名称：</label>
                                <div class="col-sm-10">
                                    <form:input  id="wareEmpname" path="wareEmpname" readOnly="true"   class="form-control required"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>核算期间：</label>
                                <div class="col-sm-10">
                                    <form:input readonly="true" cssStyle="display: none" path="period.id" htmlEscape="false"    class="form-control "/>
                                    <input readonly class="form-control" name="period.periodId" value="${billMain.period.periodId}">
                                </div>
                            </div>
                            <div class="tabs-container">
                                <ul class="nav nav-tabs">
                                    <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">入库信息：</a>
                                    </li>
                                    <li><a data-toggle="tab" href="#tab-2" aria-expanded="true">物料二维码：</a>
                                    </li>
                                </ul>
                                <div class="tab-content">
                                    <div id="tab-1" class="tab-pane fade in  active">
                                            <%--<a class="btn btn-white btn-sm" onclick="addRow('#billDetailList', billDetailRowIdx, billDetailTpl);billDetailRowIdx = billDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
                                        <div class="table-responsive" style="max-height:500px">
                                            <table class="table table-striped table-bordered table-condensed" style="min-width:1350px">
                                                <thead>
                                                <tr>
                                                    <th class="hide"></th>
                                                    <th>编号</th>
                                                    <th style="width: 190px;"><font color="red">*</font>物料编码</th>
                                                    <th style="width: 160px;"><font color="red">*</font>物料名称</th>
                                                    <th><font color="red">*</font>规格型号</th>
                                                    <th><font color="red">*</font>数量</th>
                                                    <th>单位</th>

                                                    <th style="width: 100px;">批次</th>
                                                        <%--<th>计划单价</th>--%>
                                                    <th>实际单价</th>
                                                    <th>实际金额</th>
                                                    <th>货区编码</th>
                                                    <th>货区名称</th>
                                                    <th>货位编码</th>
                                                    <th>货位名称</th>
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
						<input readOnly="true" id="billDetailList{{idx}}_serialNum" name="billDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>
                    <td>
						<sys:gridselect-item disabled="disabled" url="${ctx}/item/item/data" id="billDetailList{{idx}}_item" name="billDetailList[{{idx}}].item.id" value="{{row.item.id}}" labelName="billDetailList[{{idx}}].item.code" labelValue="{{row.item.id}}"
							 title="选择物料代码" cssClass="form-control  required"
							 targetId="billDetailList{{idx}}_itemName" targetField="name" fieldLabels="物料代码|物料名称|规格型号|单位" fieldKeys="code|name|specModel|unit" searchLabels="物料代码|物料名称|规格型号|单位" searchKeys="code|name|specModel|unit"
							 extraField="billDetailList{{idx}}_itemSpec:specModel;billDetailList{{idx}}_measUnit:unit"
							 ></sys:gridselect-item>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemName" readonly="readonly" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemSpec" readonly="readonly" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>

					<td>
						<input id="billDetailList{{idx}}_itemQty" onkeyup="getSum({{idx}})" readonly="readonly" name="billDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}" onkeyup="getItemSum('billDetailList{{idx}}')"  class="form-control "/>
					</td>

					<td>
						<input id="billDetailList{{idx}}_measUnit" name="billDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"  readonly="readonly"  class="form-control "/>
					</td>

                    <td>
						<input id="billDetailList{{idx}}_itemBatch" readonly="readonly" name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>

                    <td>
						<input id="billDetailList{{idx}}_itemPrice" readonly="readonly" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}" onkeyup="getItemSum('billDetailList{{idx}}')"   class="form-control required"/>
					</td>

					<td>
						<input id="billDetailList{{idx}}_itemSum" name="billDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}" readonly="readonly"   class="form-control "/>
					</td>

                    <td>
						<sys:gridselect-modify  url="${ctx}/bin/bin/data" id="billDetailList{{idx}}_bin" name="billDetailList[{{idx}}].bin.id" value="{{row.bin.id}}" labelName="billDetailList{{idx}}.bin.binId" labelValue="{{row.bin.binId}}"
							 title="选择货区号" cssClass="form-control  " fieldLabels="货区编码|货区名称" fieldKeys="binId|binDesc" searchLabels="货区编码|货区名称" searchKeys="binId|binDesc"
							 targetId="billDetailList{{idx}}_binName" targetField="binDesc"></sys:gridselect-modify>
					</td>

					<td>
						<input id="billDetailList{{idx}}_binName" readonly="readonly" name="billDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"    class="form-control "/>
					</td>

					<td>
						<sys:gridselect-modify  url="${ctx}/location/location/data" id="billDetailList{{idx}}_loc" name="billDetailList[{{idx}}].loc.id" value="{{row.loc.id}}" labelName="billDetailList{{idx}}.loc.locId" labelValue="{{row.loc.locId}}"
							 title="选择货位号" cssClass="form-control  " fieldLabels="货位编码|货位名称" fieldKeys="locId|locDesc" searchLabels="货位编码|货位名称" searchKeys="locId|locDesc"
							 targetId="billDetailList{{idx}}_locName" targetField="locDesc"></sys:gridselect-modify>
					</td>


					<td>
						<input id="billDetailList{{idx}}_locName" readonly="readonly" name="billDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"    class="form-control "/>
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
                                                for (var i=0; i<data.length; i++){
                                                    addRow('#billDetailList', billDetailRowIdx, billDetailTpl, data[i]);
                                                    billDetailRowIdx = billDetailRowIdx + 1;
                                                }
                                            });
                                        </script>
                                    </div>


                                    <div id="tab-2" class="tab-pane fade in ">
                                        <table class="table table-striped table-bordered table-condensed">
                                            <thead>
                                            <tr>
                                                <th class="hide"></th>
                                                <th width="100px">序号</th>
                                                <th width="400px"><font color="red">*</font>二维码</th>
                                                <th width="200px"><font color="red">*</font>货区编号</th>
                                                <th width="200px"><font color="red">*</font>货区名称</th>
                                                <th width="200px"><font color="red">*</font>货位编号</th>
                                                <th width="200px"><font color="red">*</font>货位名称</th>
                                                <th width="10">&nbsp;</th>
                                            </tr>
                                            </thead>
                                            <tbody id="billDetailCodeList">
                                            </tbody>
                                        </table>
                                        <script type="text/template" id="billItemDetailTpl">//<!--
                <tr id="billItemDetailList{{idx}}">
                    <td class="hide">
                            <input id="billDetailCodeList{{idx}}_id" name="billDetailCodeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
                            <input id="billDetailCodeList{{idx}}_delFlag" name="billDetailCodeList[{{idx}}].delFlag" type="hidden" value="0"/>
                            <input id="billDetailCodeList{{idx}}_itemCode" name="billDetailCodeList[{{idx}}].item.code" type="hidden" value="{{row.itemCode}}"/>

                        </td>


                        <td>
                            <input readOnly="true" id="billDetailCodeList{{idx}}_serialNum" name="billDetailCodeList[{{idx}}].serialNum" type="text" value="{{row.serId}}"    class="form-control required"/>
                        </td>
                        <td>
                            <input readOnly="true" id="billDetailCodeList{{idx}}_itemBarcode" name="billDetailCodeList[{{idx}}].itemBarcode" type="text" value="{{row.itemBarcode}}"    class="form-control required"/>
                        </td>
                        <td>
                            <input readOnly="true" id="billDetailCodeList{{idx}}_bin.binId" name="billDetailCodeList[{{idx}}].bin.binId" type="text" value="{{row.bin.binId}}"    class="form-control required"/>
                        </td>
                        <td>
                            <input readOnly="true" id="billDetailCodeList{{idx}}_bin.binDesc" name="billDetailCodeList[{{idx}}].bin.binDesc" type="text" value="{{row.bin.binDesc}}"    class="form-control"/>
                        </td>
                        <td>
                            <input readOnly="true" id="billDetailCodeList{{idx}}_loc.locId" name="billDetailCodeList[{{idx}}].loc.locId" type="text" value="{{row.loc.locId}}"    class="form-control required"/>
                        </td>
                        <td>
                            <input readOnly="true" id="billDetailCodeList{{idx}}_loc.locDesc" name="billDetailCodeList[{{idx}}].loc.locDesc" type="text" value="{{row.loc.locDesc}}"    class="form-control"/>
                        </td>


                    <td class="text-center" width="10">
                        {{#delBtn}}<span class="close" onclick="delRow(this, '#billItemDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
                    </td>
                </tr>//-->
                                        </script>
                                        <script type="text/javascript">
                                            var billItemDetailRowIdx = 0, billItemDetailTpl = $("#billItemDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                                            $(document).ready(function() {
                                                var data = ${fns:toJson(billMain.billDetailCodeList)};
                                                for (var i=0; i<data.length; i++){
                                                    addRow('#billDetailCodeList', billItemDetailRowIdx, billItemDetailTpl, data[i]);
                                                    billItemDetailRowIdx = billItemDetailRowIdx + 1;
                                                }
                                                if("p"=="${mtype}") {
                                                    $('input[id$="itemQty"]').attr("readonly", false)
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
                                        <div>
                                            <c:if test="${mtype=='i'}">
                                                <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">退货</button>
                                            </c:if>
                                            <c:if test="${mtype=='c'}">
                                            </c:if>
                                            <c:if test="${mtype=='p'}">
                                                <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">过账</button>
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
</body>
</html>
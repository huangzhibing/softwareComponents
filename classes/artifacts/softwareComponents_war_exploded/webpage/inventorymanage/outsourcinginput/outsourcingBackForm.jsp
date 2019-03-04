<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>外购件退货单据管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp"%>
    <script type="text/javascript">

        <%--function getItemSum(id) {
            var price = $("#"+id+"_itemPrice").val().trim();
            price = parseInt(price);
            var itemQty = $("#"+id+"_itemQty").val().trim();
            itemQty = parseInt(itemQty);
            if (itemQty>detail.checkQty)
            {
                $("#input_save_button").attr("disabled", true);
                jp.warning("退货数量大于到货数量，请重新输入!");
            }
            else {
                $("#input_save_button").attr("disabled", false);
                if($("#"+id+"_itemPrice").val() ==""||$("#"+id+"_itemQty").val() ==""){
                    $("#input_save_button").attr("disabled", false);
                    var itemSum = "";
                }else {
                    var itemSum = price * itemQty;
                }
                $("#"+id+"_itemSum").val(itemSum);
            }
        }

        function getItemBatch(id1,id2){
            var billNum = $("#"+id2).val();
            var serialNum = $("#"+id1+"_serialNum").val();
            var itemNum = $("#"+id1+"_itemNames").val();

            if($("#"+id1+"_serialNum").val() ==""||$("#"+id1+"_item").val() ==""){
                var itemBatch = "";
            }else {
                var itemBatch = billNum+itemNum+serialNum;
            }
            $("#"+id1+"_itemBatch").val(itemBatch);
        }--%>

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
            $('#wareNames').change(function(){
                $.ajax({
                    url:"${ctx}/outboundinput/outboundInput/getEmp",
                    data:{
                        wareID:$('#wareNames').val()
                    },
                    dataType:'json',
                    success:function(re){
                        console.log(re);
                        $('#wareEmpId').val(re.id);
                        $('#wareEmpNames').val(re.user.no);
                        $('#wareEmpname').val(re.empName);
                    }
                });
            });
            $('#corBillNum').click(function () {
                $('tr td').remove();
                top.layer.open({
                    type: 2,
                    area: ['800px', '500px'],
                    title:"选择到货单",
                    auto:true,
                    name:'friend',
                    content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/outsourcinginput/outsourcingInput/dataBackList")+"&fieldLabels="+encodeURIComponent("退货单号")+"&fieldKeys="+encodeURIComponent("billnum")+"&searchLabels="+encodeURIComponent("退货单号")+"&searchKeys="+encodeURIComponent("billnum")+"&isMultiSelected=false",
                    btn: ['确定', '关闭'],
                    yes: function(index, layero){
                        var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                        var items = iframeWin.getSelections();
                        if(items == ""){
                            jp.warning("必须选择一条数据!");
                            return;
                        }
                        item = items[0];
                        console.log(item);
                        var test=jp.loading("正在录入");
                        $('#corBillNum').val(item.billnum);
                        $('#accountNames').val(item.supId);
                        $('#accountName').val(item.supName);
                        $('#wareNames').val(item.wareId);
                        $('#wareName').val(item.wareName);
                        $.ajax({
                            url:"${ctx}/outboundinput/outboundInput/getEmp",
                            data:{
                                wareID:$('#wareNames').val()
                            },
                            dataType:'json',
                            success:function(re){
                                console.log(re);
                                $('#wareEmpId').val(re.id);
                                $('#wareEmpNames').val(re.user.no);
                                $('#wareEmpname').val(re.empName);
                            }
                        });
                        <%--$('#rcvAddr').val(item.rcvAddr);--%>
                        <%--$('#transAccount').val(item.transAccount);--%>
                        $.get("${ctx}/purinvcheckmain/invCheckMain/detail?id="+item.id, function(details){
                            console.log(details);
                            for(var i = 0;i < details.invCheckDetailList.length;i++){
                                detail=details.invCheckDetailList[i];
                                console.log(detail);
                                detail.id="";
                                detail.item=new Object();
                                detail.item.id = detail.itemId;
                                detail.item.code=detail.itemCode;
                                detail.itemPrice=detail.realPrice;
                                detail.itemSum=detail.realSum;
                                detail.itemSpec=detail.itemSpecmodel;
                                detail.itemQty=detail.checkQty;
                                detail.measUnit=detail.unitCode;
                                addRow('#billDetailList',i,billDetailTpl,detail);
                                $('#billDetailList'+i+'_itemQty').trigger('onkeyup');
                            }
                        });
                        jp.close(test);
                        top.layer.close(index);//关闭对话框。
                    },
                    cancel: function(index){
                    }
                });
            });
            $('#input_submit_button').click(function () {
               $('#billFlag').val("S");
               jp.close();
            });
            $('#input_save_button').click(function () {
                $('#billFlag').val("J");
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
        function initItemNum(){
            $("[id$='serialNum'][id^='billDetailList']").each(function(index,element){
                $(element).val(index+1);
            });

            $("[id$='serialNum'][id^='billDetailCodeList']").each(function(index,element){
                $(element).val(index+1);
            });

        }
        function auditNote() {
            layer.prompt({
                formType: 2,
                title: '请填写不通过意见,并确认',
                maxlength: 50
            }, function(value, index, elem){
                $('#note').val(value);
                $('#Audit').val("false");
                $('#inputForm').submit();
                layer.close(index);
                jp.close();
            });
        }

        function getItemBatch() {
            var date = new Date();
            var year = date.getFullYear().toString();
            var month = (date.getMonth()+1).toString();
            if(month>0&&month<10){
                month = "0"+month;
            }
            var day = date.getDate().toString();
            if(day>0&&day<10){
                day = "0"+day;
            }
            var time = year+month+day;
            $("[id$='itemBatch']").val(time);
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
            initItemNum();
            getItemBatch();
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
            initItemNum();
        }
        function getIdSelections() {
            return $.map($('#billDetailTpl').bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }


        function deleteAll(){

            jp.confirm('确认要删除该单据记录吗？', function(){
                jp.loading();
                jp.get("${ctx}/outsourcinginut/outsourcingInput/deleteAllDetail?ids=" + getIdSelections(), function(data){
                    if(data.success){
                        $('#billDetailTpl').bootstrapTable('refresh');
                        jp.success(data.msg);
                    }else{
                        jp.error(data.msg);
                    }
                })

            })
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
                        <a class="panelButton" href="${ctx}/outsourcinginput/outsourcingInput/Blist?type=${type}"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="billMain" action="${ctx}/outsourcinginput/outsourcingInput/BackSave?type=${type}" method="post" class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message-type content="${message}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
                            <div class="col-sm-10">
                                <form:input path="billFlag" htmlEscape="false" cssStyle="display: none;"/>
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
                            <label class="col-sm-2 control-label"><font color="red">*</font>退货日期：</label>
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
                            <label class="col-sm-2 control-label">选择退货单：</label>
                            <div class="col-sm-10 input-group">
                                <form:input readonly="true" path="corBillNum"  htmlEscape="false"    class="form-control"/>
                                    <span class="input-group-btn">
                                             <button type="button"  class="btn   btn-primary  "><i class="fa fa-search"></i>
                                             </button>
                                        </button>
                                        <button type="button"  class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
                                   </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">仓库编码：</label>
                            <div class="col-sm-10">
                                    <sys:gridselect-item  url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${billMain.ware.id}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
                                                          title="选择库房号" cssClass="form-control" fieldLabels="库房号|库房名称" fieldKeys="wareID|wareName" searchLabels="库房号|库房名称" searchKeys="wareID|wareName"
                                                          targetId="wareName" targetField="wareName"></sys:gridselect-item>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">仓库名称：</label>
                            <div class="col-sm-10">
                                <form:input path="wareName" readOnly="true"  htmlEscape="false"    class="form-control"/>
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
                        <%--<div id="Comment" class="form-group" style="display: none;">--%>
                            <%--<label class="col-sm-2 control-label"><font color="red">*</font>审批意见：</label>--%>
                            <%--<div class="col-sm-10">--%>
                                <%--<textarea id="auditComment" class="form-control required" name="auditComment" ng-value="${auditComment}"></textarea>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <form:input path="note" htmlEscape="false" cssStyle="display: none"/>
                        <input id="Audit" name="Audit" htmlEscape="false" style="display: none;"/>
                        <c:if test="${type eq 'BQuery'}">
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>审核不通过意见：</label>
                                <div class="col-sm-10">
                                    <form:textarea type="text" readonly="true" htmlEscape="false" class="form-control" path="note"></form:textarea>
                                </div>
                            </div>
                        </c:if>
                        <div class="tabs-container">
                                <ul class="nav nav-tabs">
                                    <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">退货信息：</a>
                                    </li>
                                    <li><a data-toggle="tab" href="#tab-2" aria-expanded="true">物料二维码：</a>
                                    </li>
                                </ul>
                            <div class="tab-content">
                                <div id="tab-1" class="tab-pane fade in  active">
                                    <a class="btn btn-white btn-sm" onclick="addRow('#billDetailList', billDetailRowIdx, billDetailTpl);billDetailRowIdx = billDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
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
                                            <th><font color="red">*</font>实际单价</th>
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
						<input id="billDetailList{{idx}}_serialNum" value="{{idx2}}" readOnly="readOnly" name="billDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>
                    <td>
						<sys:gridselect-item
							flag='1'
							limite="wareNames:ware.wareID"
							extraField="billDetailList0_itemId:item.id;billDetailList{{idx}}_measUnit:item.unitCode.unitName;billDetailList{{idx}}_itemNames:item.code;billDetailList{{idx}}_accountId:id;billDetailList{{idx}}_itemBatch:itemBatch;billDetailList{{idx}}_item:item.id;billDetailList{{idx}}_itemName:item.name;billDetailList{{idx}}_planPrice:item.planPrice;billDetailList{{idx}}_itemSpec:item.specModel;billDetailList{{idx}}_binId:bin.id;billDetailList{{idx}}_binNames:bin.binId;billDetailList{{idx}}_binName:bin.binDesc;billDetailList{{idx}}_locId:loc.id;billDetailList{{idx}}_locNames:loc.locId;billDetailList{{idx}}_locName:loc.locDesc;billDetailList{{idx}}_itemPrice:costPrice"
							url="${ctx}/invaccount/invAccount/data" id="billDetailList{{idx}}_item" name="billDetailList[{{idx}}].item.id" value="{{row.item.code}}" labelName="billDetailList[{{idx}}].item.code" labelValue="{{row.item.code}}"
							 title="选择物料代码" cssClass="form-control required " fieldLabels="物料代码|物料名称|物料规格|数量" fieldKeys="item.code|item.name|item.specModel|realQty" searchLabels="物料代码|物料名称|货区号|货区名|货位号|货位名" searchKeys="item.code|item.name|bin.binId|bin.binDesc|loc.locId|locDesc" ></sys:gridselect-item>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemName" readonly="readonly" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemSpec" readonly="readonly" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>

					<td>
						<input id="billDetailList{{idx}}_itemQty" name="billDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}" onkeyup="getItemSum('billDetailList{{idx}}')"  class="form-control "/>
					</td>

					<td>
						<input id="billDetailList{{idx}}_measUnit" name="billDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"  readonly="readonly"  class="form-control "/>
					</td>

                    <td>
						<input id="billDetailList{{idx}}_itemBatch"  name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>

                    <%--<td>--%>
						<%--<input id="billDetailList{{idx}}_planPrice" name="billDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"    class="form-control "/>--%>
					<%--</td>--%>

                    <td>
						<input id="billDetailList{{idx}}_itemPrice" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}" onkeyup="getItemSum('billDetailList{{idx}}')"   class="form-control required"/>
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
                                    <a class="btn btn-white btn-sm" onclick="addRow('#billDetailCodeList', billDetailCodeRowIdx, billDetailCodeTpl);billDetailCodeRowIdx = billDetailCodeRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
                                    <div class="table-responsive" style="max-height:500px">
                                        <table class="table table-striped table-bordered table-condensed" style="min-width:1350px;">
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
                                        {{#delBtn}}<span class="close" onclick="delRow(this, '#billDetailCodeList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
                                    </td>
                                    </tr>//-->
                                    </script>
                                    <script type="text/javascript">
                                        var billDetailCodeRowIdx = 0, billDetailCodeTpl = $("#billDetailCodeTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                                        $(document).ready(function() {
                                            var data = ${fns:toJson(billMain.billDetailCodeList)};
                                            console.log(data);
                                            for (var i=0; i<data.length; i++){
                                                addRow('#billDetailCodeList', billDetailCodeRowIdx, billDetailCodeTpl, data[i]);
                                                billDetailCodeRowIdx = billDetailCodeRowIdx + 1;
                                            }
                                        });
                                    </script>
                                </div>

                            </div>
                        </div>
                        <c:if test="${type eq 'BInput'}">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-3">

                                <div class="form-group text-center">
                                    <div>
                                        <button id="input_save_button" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在保存...">保 存</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-1"></div>
                            <div class="col-lg-3">
                                <div class="form-group text-center">
                                    <div>
                                        <button id="input_submit_button" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在保存...">提 交</button>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${type eq 'BQuery'}">
                            <div class="col-lg-3"></div>
                            <div class="col-lg-6">
                                <div class="form-group text-center">
                                    <div>
                                        <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/outsourcinginput/outsourcingInput/Blist?type=BQuery"><i class="ti-angle-left"></i> 返 回</a>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${type eq 'BPost'}">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-3">
                                <div class="form-group text-center">
                                    <div>
                                        <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在过账...">过 账</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-1"></div>
                            <div class="col-lg-3">
                                <div class="form-group text-center">
                                    <div>
                                        <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/outsourcinginput/outsourcingInput/Blist?type=BPost"><i class="ti-angle-left"></i> 返 回</a>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${type eq 'BAudit'}">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-3">
                                <div class="form-group text-center">
                                    <div>
                                        <button class="btn btn-primary btn-block btn-lg btn-parsley" type="submit" name="Audit" value="true" data-loading-text="审核通过...">审 核 通 过</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-1"></div>
                            <div class="col-lg-3">
                                <div class="form-group text-center">
                                    <div>
                                        <button id="input_audit_button" type="button" onclick="auditNote()" class="btn btn-danger btn-block btn-lg btn-parsley" data-loading-text="审核不通过...">审 核 不 通 过</button>
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

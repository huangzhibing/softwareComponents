 <%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>冲估单据管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp"%>
    <script type="text/javascript">
        function getItemSum(id) {
            var price = $("#"+id+"_itemPrice").val().trim();
            price = parseInt(price);
            var itemQty = $("#"+id+"_itemQty").val().trim();
            itemQty = parseInt(itemQty);
            if($("#"+id+"_itemPrice").val() ==""||$("#"+id+"_itemQty").val() ==""){
                var itemSum = "";
            }else {
                var itemSum = price * itemQty;
            }
            $("#"+id+"_itemSum").val(itemSum);
        }
        function getItemBatch(id1,id2){
            var billNum = $("#"+id2).val();
            var serialNum = $("#"+id1+"_serialNum").val();
            var itemNum = $("#"+id1+"_itemNames").val();

            if($("#"+id1+"_serialNum").val() ==""||$("#"+id1+"_item").val() ==""){
                var itemBatch = "";
            }else {
                var itemBatch = billNum+serialNum+itemNum;
            }
            $("#"+id1+"_itemBatch").val(itemBatch);
        }
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
            <%--if("${type}" === "query"){--%>
            $(".close").hide()
            $("#wareButton").hide()
            $("#deptButton").hide()
            $("#billPersonButton").hide()
            $("#wareEmpButton").hide()
//            }

            $('#billDate').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#recDate').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#operDate').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#corBillNum').click(function () {
                $('tr td').remove();
                top.layer.open({
                    type: 2,
                    area: ['800px', '500px'],
                    title:"选择入库单",
                    auto:true,
                    name:'friend',
                    content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/outsourcinginput/outsourcingInput/data")+"&fieldLabels="+encodeURIComponent("外购件入库单号|库房名称")+"&fieldKeys="+encodeURIComponent("billNum|wareName")+"&searchLabels="+encodeURIComponent("外购件入库单号|库房名称")+"&searchKeys="+encodeURIComponent("billNum|wareName")+"&isMultiSelected=false",
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
                        $('#itemId').val(item.id);
                        $('#corBillNum').val(item.billNum);
                        $('#wareId').val(item.ware.wareID);
                        $('#wareNames').val(item.ware.wareID);
                        $('#wareName').val(item.wareName);
                        $('#accountNames').val(item.account.id);
                        $('#accountName').val(item.accountName);
                        $.get("${ctx}/outsourcinginput/outsourcingInput/detail?id="+item.id, function(details){
                            console.log(details);
                            for(var i = 0;i < details.billDetailList.length;i++){
                                detail=details.billDetailList[i];
                                console.log(detail);
                                detail.id="";
                                addRow('#billDetailList',i + 1, billDetailTpl,detail);
                                $('#billDetailList'+i+'_itemQty').trigger('onkeyup');
                            }
                        });
                        $.ajax({
                            url:"${ctx}/impactestimationinput/impactestimationInput/getEmp",
                            data:{wareId:$('#wareId').val()},
                            dataType:'json',
                            success:function (request) {
                                $('#wareEmpId').val(request.id);
                                $('#wareEmpNames').val(request.user.no);
                                $('#wareEmpname').val(request.empName);
                            }
                        });
                        jp.close(test);
                        top.layer.close(index);//关闭对话框。
                    },
                    cancel: function(index){
                    }
                });
            })
            $('#wareId').change(function () {

            });
        });
        function initItemNum(){
            $("[id$='serialNum']").each(function(index,element){
                $(element).val(index+1);
            });
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
                jp.get("${ctx}/impactestimationinput/impactestimationInput/deleteAllDetail?ids=" + getIdSelections(), function(data){
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
                        <a class="panelButton" href="${ctx}/impactestimationinput/impactestimationInput?type=${type}"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="billMain" action="${ctx}/impactestimationinput/impactestimationInput/save?type=${type}" method="post" class="form-horizontal">
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
                                <form:select id="billFlag" disabled="true" path="billFlag" class="form-control required ">
                                    <form:option value="" label=""/>
                                    <form:options items="${fns:getDictList('billFlag')}" itemLabel="label"  itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>冲估日期：</label>
                            <div class="col-sm-10">
                                <p class="input-group">
                                <div class='input-group form_datetime' id='cgDate'>
                                    <input type='text'  name="billDate" class="form-control required" readonly="readonly" value="<fmt:formatDate value="${billMain.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
                                </div>
                                </p>
                            </div>
                        </div>
                        <c:if test="${displaynone == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>估价入库单号：</label>
                            <div class="col-sm-10">
                                <form:input readonly="true" path="corBillNum" htmlEscape="false"    class="form-control "/>
                            </div>
                        </div>
                        </c:if>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>库房号：</label>
                            <div class="col-sm-10">
                                <sys:gridselect-modify disableClick="true" targetId="wareName" targetField="wareName" url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.wareID" value="${billMain.ware.wareID}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
                                                       title="选择库房号" cssClass="form-control required" fieldLabels="库房号|库房名称" fieldKeys="wareID|wareName" searchLabels="库房号|库房名称" searchKeys="wareID|wareName" ></sys:gridselect-modify>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>库房名称：</label>
                            <div class="col-sm-10">
                                <form:input readonly="true" path="wareName" htmlEscape="false"    class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>供应商编码：</label>
                            <div class="col-sm-10">
                                <input id="accountNames" name="account.accountCode" readonly="readonly"
                                       type="text" value="${billMain.account.id}" data-msg-required="" class="form-control required" style="" aria-required="true">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
                            <div class="col-sm-10">
                                <form:input path="accountName" htmlEscape="false"  readonly="true"  class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>库管员代码：</label>
                            <div class="col-sm-10">
                                <sys:gridselect-modify disableClick="true" labelField="user.no" targetId="wareEmpname" targetField="empName" url="${ctx}/employee/employee/data" id="wareEmp" name="wareEmp.user.no" value="${billMain.wareEmp.user.no}" labelName="wareEmp.user.no" labelValue="${billMain.wareEmp.user.no}"
                                                       title="选择库管员代码" cssClass="form-control " fieldLabels="库管员代码|库管员名称" fieldKeys="user.no|empName" searchLabels="库管员代码|库管员名称" searchKeys="user.no|empName" ></sys:gridselect-modify>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>库管员名称：</label>
                            <div class="col-sm-10">
                                <form:input readonly="true" path="wareEmpname" htmlEscape="false"  value="${billMain.wareEmp.empName}"   class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>核算期间：</label>
                            <div class="col-sm-10">
                                <form:input readonly="true" cssStyle="display: none" path="period.id" htmlEscape="false"    class="form-control "/>
                                <input readonly class="form-control" value="${billMain.period.periodId}">
                            </div>
                        </div>
                        <div class="tabs-container">
                                <%--<ul class="nav nav-tabs">--%>
                                <%--<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">出入库单据子表：</a>--%>
                                <%--</li>--%>
                                <%--</ul>--%>
                                    <div class="tab-content">
                                        <div id="tab-1" class="tab-pane fade in  active">
                                                <%--<a class="btn btn-white btn-sm" onclick="addRow('#billDetailList', billDetailRowIdx, billDetailTpl);billDetailRowIdx = billDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
                                            <table class="table table-striped table-bordered table-condensed">
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
							limite="wareId:ware.id"
							extraField="billDetailList0_itemId:item.id;billDetailList{{idx}}_measUnit:item.unitCode.unitName;billDetailList{{idx}}_itemNames:item.id;billDetailList{{idx}}_accountId:id;billDetailList{{idx}}_itemBatch:itemBatch;billDetailList{{idx}}_item:item.id;billDetailList{{idx}}_itemName:item.name;billDetailList{{idx}}_planPrice:item.planPrice;billDetailList{{idx}}_itemSpec:item.specModel;billDetailList{{idx}}_binId:bin.id;billDetailList{{idx}}_binNames:bin.binId;billDetailList{{idx}}_binName:bin.binDesc;billDetailList{{idx}}_locId:loc.id;billDetailList{{idx}}_locNames:loc.locId;billDetailList{{idx}}_locName:loc.locDesc;billDetailList{{idx}}_itemPrice:costPrice"
							url="${ctx}/invaccount/invAccount/data" id="billDetailList{{idx}}_item" name="billDetailList[{{idx}}].item.id" value="{{row.itemId}}" labelName="billDetailList[{{idx}}].item.code" labelValue="{{row.item.code}}"
							 title="选择物料代码" cssClass="form-control required " fieldLabels="物料代码|物料名称|物料规格|数量" fieldKeys="item.code|item.name|item.specModel|realQty" searchLabels="物料代码|物料名称|货区号|货区名|货位号|货位名" searchKeys="item.code|item.name|bin.binId|bin.binDesc|loc.locId|locDesc" ></sys:gridselect-item>
			</td>
					<td>
						<input id="billDetailList{{idx}}_itemName" readonly="readonly" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemSpec" readonly="readonly" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>

					<td>
						<input id="billDetailList{{idx}}_itemQty" onkeyup="sum({{idx}})" name="billDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control required"/>
					</td>
					<td>
						<input id="billDetailList{{idx}}_measUnit" name="billDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"  readonly="readonly"  class="form-control "/>
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
                    <td>
						<input id="billDetailList{{idx}}_itemBatch"  name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>
                    <td>
						<input id="billDetailList{{idx}}_planPrice" name="billDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"    class="form-control "/>
					</td>
                    <td>
						<input id="billDetailList{{idx}}_itemPrice" readonly="readonly" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"  onkeyup="sum({{idx}})"  class="form-control "/>
					</td>


					<td>
						<input id="billDetailList{{idx}}_itemSum" readonly="readonly" name="billDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control required"/>
					</td>




                    <td>
						<input readOnly="true" id="billDetailList{{idx}}_estimation" name="billDetailList[{{idx}}].filePath" type="hidden" value="{{row.filePath}}" maxlength="200" class="form-control"/>
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
                                    </div>
                        </div>
                        <c:if test="${type eq 'Input'}">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-3">

                                <div class="form-group text-center">
                                    <div>
                                        <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在保存...">保 存</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-1"></div>
                            <div class="col-lg-3">
                                <div class="form-group text-center">
                                    <div>
                                        <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/impactestimationinput/impactestimationInput?type=Input"><i class="ti-angle-left"></i> 返 回</a>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${type eq 'Query'}">
                            <div class="col-lg-3"></div>
                            <div class="col-lg-6">
                                <div class="form-group text-center">
                                    <div>
                                        <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/impactestimationinput/impactestimationInput?type=Query"><i class="ti-angle-left"></i> 返 回</a>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${type eq 'Post'}">
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
                                        <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/impactestimationinput/impactestimationInput?type=Post"><i class="ti-angle-left"></i> 返 回</a>
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

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>成品入库单据录入管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp"%>
    <script type="text/javascript">
        function iniItemNum(){
            $("[id$='serialNum']").each(function(index,element){
                $(element).val(index+1);
            });
            $("[id$='serialNum2']").each(function(index,element){
                $(element).val(index+1);
            });
        }
        function getCode(code,index) {
            console.log(code)
            var url='http://localhost:8080/a/mpsplan/mSerialNo/data?objSn='+code.value
            $.ajax({
                url:url,
                success:function (data) {
                    try {
                        $('#billDetailCodeList' + index + '_machineBarcode').val(data.rows[0].mserialno)
                    }catch (e) {
                    }
                }
            })
        }
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
                var itemBatch = billNum+itemNum+serialNum;
            }
            $("#"+id1+"_itemBatch").val(itemBatch);
        }
        $(document).ready(function() {
            var item;
            $("#inputForm").validate({
                submitHandler: function(form){
                    jp.loading();
                    $('#billFlag').attr("disabled",false);
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
            // $(".close").hide()
            // $("#wareButton").hide()
            // $("#deptButton").hide()
            // $("#billPersonButton").hide()
            // $("#wareEmpButton").hide()
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
            $('#wareId').change(function () {
                $.ajax({
                    url:"${ctx}/inboundinput/inboundInput/getEmp",
                    data:{wareId:$('#wareId').val()},
                    dataType:'json',
                    success:function(re){
                        $('#wareEmpId').val(re.id);
                        $('#wareEmpNames').val(re.user.no);
                        $('#wareEmpname').val(re.empName);
                    }
                });
            });
            $('#wareName').change(function () {
                $.ajax({
                    url:"${ctx}/inboundinput/inboundInput/getBatchFlag",
                    data:{wareId:$('#wareId').val()},
                    dataType:'json',
                    success:function (request) {
                        alert("1111");
                        $("[id$='itemBatch']").each(function (index,element) {
                            if(request="否"){
                                $(element).val("");
                                alert("否");
                            }
                            else {
                                alert("是");
                                getItemBatch('billDetailList{{idx}}','billnum');
                            }
                        })
                    }
                });
            });
            // $('#').click(function () {
            //    top.layout.open({
            //        type: 2,
            //        area: ['800px','500px'],
            //        title:"选择通知单",
            //        auto: true,
            //        name:'friend',
            //        content: ""
            //        btn:['确定','关闭'],
            //        yes:function (index,layero) {
            //            var iframeValue = layero.find('iframe')[0].contentWindow; //得到iframe页面的窗口对象
            //            var items = iframeValue.getSelections();
            //            console.log(items);
            //            if(items == ""){
            //                jp.warning("必须选择一条数据!");
            //                return ;
            //            }
            //            item = items[0];
            //            console.log(item);
            //            jp.loading("正在录入");
            //
            //        }
            //
            //
            //    });
            // });

        });

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
            iniItemNum();
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
            iniItemNum();
        }
        function getIdSelections() {
            return $.map($('#billDetailTpl').bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }


        function deleteAll(){

            jp.confirm('确认要删除该单据记录吗？', function(){
                jp.loading();
                jp.get("${ctx}/inboundinput/inboundInput/deleteAllDetail?ids=" + getIdSelections(), function(data){
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
                        <a class="panelButton" href="${ctx}/inboundinput/inboundInput?type=${type}"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="billMain" action="${ctx}/inboundinput/inboundInput/save?type=${type}" method="post" class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message-type content="${message}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
                            <div class="col-sm-10">
                                <form:input id="billnum" path="billNum" htmlEscape="false" readonly="true"   class="form-control required"/>
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
                            <label class="col-sm-2 control-label"><font color="red">*</font>核算期间：</label>
                            <div class="col-sm-10">
                                <form:input readonly="true" cssStyle="display: none" path="period.id" htmlEscape="false"    class="form-control "/>
                                <input readonly class="form-control" value="${billMain.period.periodId}">
                            </div>
                        </div>
                        <div class="form-group">
                        <label class="col-sm-2 control-label"><font color="red">*</font>仓库编码：</label>
                        <div class="col-sm-10">
                            <sys:gridselect-ware url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.wareID" value="${billMain.ware.wareID}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
                                            title="选择仓库编码" cssClass="form-control required" fieldLabels="仓库编码|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库编码|仓库名称" searchKeys="wareID|wareName"
                                            targetId="wareName" targetField="wareName"></sys:gridselect-ware>
                        </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>仓库名称：</label>
                            <div class="col-sm-10">
                                <form:input path="wareName" htmlEscape="false"  readonly="true"  class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>部门编码：</label>
                            <div class="col-sm-10">
                                <sys:treeselect-modify disabled="disabled" targetId="deptname" targetField="code" id="dept" name="dept.code" value="${billMain.dept.code}" labelName="dept.codes" labelValue="${billMain.dept.code}"
                                                title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>

                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>部门名称：</label>
                            <div class="col-sm-10">
                                <form:input readonly="true"  id="deptname" path="deptName" htmlEscape="false"   class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>经办人：</label>
                            <div class="col-sm-10">
                                <sys:userselect disabled="disabled" id="billPerson" name="billPerson.no" value="${billMain.billPerson.no}" labelName="billPerson.no" labelValue="${billMain.billPerson.no}"
                                                cssClass="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>库管员编码：</label>
                            <div class="col-sm-10">
                                <sys:gridselect-modify  disabled="disabled" labelField="user.no"  url="${ctx}/employee/employee/data"  id="wareEmp" name="wareEmp.user.no" value="${billMain.wareEmp.user.no}" labelName="wareEmp.user.no" labelValue="${billMain.wareEmp.user.no}"
                                                        title="选择库管员代码" cssClass="form-control required" fieldLabels="库管员代码|库管员名称" fieldKeys="user.no|empName" searchLabels="库管员代码|库管员名称" searchKeys="user.no|empName"
                                                        targetId="wareEmpname" targetField="empName"></sys:gridselect-modify>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>库管员名称：</label>
                            <div class="col-sm-10">
                                <form:input path="wareEmpname" htmlEscape="false"  readonly="true" value="${billMain.wareEmp.empName}" class="form-control "/>
                            </div>
                        </div>

                        <div class="tabs-container">
                            <ul class="nav nav-tabs">
                                <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">成品入库单据明细：</a>
                                </li>
                                <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="true">物料二维码：</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div id="tab-1" class="tab-pane fade in  active">
                                    <a class="btn btn-white btn-sm" onclick="addRow('#billDetailList', billDetailRowIdx, billDetailTpl);billDetailRowIdx = billDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
                                    <%--<a class="btn btn-white btn-sm" id="remove" onclick="delRow(billDetailList, '#billDetailList{{idx}}')" title="删除"><i class="fa fa-minus"></i> 删除</a>--%>
                                    <div class="table-responsive" style="max-height:500px">
                                        <table class="table table-striped table-bordered table-condensed" style="min-width:1350px">
                                        <thead>
                                        <tr>
                                            <%--<th class="bs-checkbox" style="width: 36px;"data-field="0" tabindex="0">--%>
                                                <%--<div class="th-inner">--%>
                                                    <%--<input name="btSelectAll" type="checkbox">--%>
                                                <%--</div>--%>
                                                <%--<div class="fht-cell"></div>--%>
                                            <%--</th>--%>
                                            <th class="hide"></th>
                                            <th>编号</th>
                                            <th style="width:180px;"><font color="red">*</font>物料代码</th>
                                            <th style="width:180px;"><font color="red">*</font>物料名称</th>
                                            <th><font color="red">*</font>规格型号</th>
                                            <th><font color="red">*</font>数量</th>
                                            <th>单位</th>
                                            <th>货区编码</th>
                                            <th>货区名称</th>
                                            <th>货位编码</th>
                                            <th>货位名称</th>
                                            <th>批次</th>
                                            <th>计划单价</th>
                                            <th><font color="red">*</font>实际单价</th>
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
				    <%--<td class="bs-checkbox">--%>
				        <%--<input data-index={{idx}} name="btSelectItem" type="checkbox">--%>
				    <%--</td>--%>
					<td class="hide">
						<input id="billDetailList{{idx}}_id" name="billDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="billDetailList{{idx}}_delFlag" name="billDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<input id="billDetailList{{idx}}_serialNum" value="{{idx2}}" readOnly="readOnly" name="billDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>
                    <td>
						<sys:gridselect-item url="${ctx}/item/item/data?code=01" id="billDetailList{{idx}}_item" name="billDetailList[{{idx}}].item.id" value="{{row.item.id}}" labelName="billDetailList[{{idx}}].item.code" labelValue="{{row.item.id}}"
							 title="选择物料代码" cssClass="form-control  required"
							 targetId="billDetailList{{idx}}_itemName" targetField="name" fieldLabels="物料代码|物料名称|规格型号|单位|计划单价" fieldKeys="code|name|specModel|unit|planPrice" searchLabels="物料名称|规格型号|单位|计划单价" searchKeys="name|specModel|unit|planPrice"
							 extraField="billDetailList{{idx}}_itemSpec:specModel;billDetailList{{idx}}_measUnit:unit;billDetailList{{idx}}_planPrice:planPrice"
							 ></sys:gridselect-item>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemName" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"  readOnly="true"   class="form-control "/>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemSpec" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"  readOnly="true"   class="form-control "/>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemQty" name="billDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control "/>
					</td>
					<td>
						<input id="billDetailList{{idx}}_measUnit" name="billDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"  readOnly="true"   class="form-control "/>
					</td>
					<td>
						<sys:gridselect-modify url="${ctx}/bin/bin/data" urlParamName="wareId" urlParamId="wareNames" id="billDetailList{{idx}}_bin" name="billDetailList[{{idx}}].bin.id" value="{{row.bin.id}}" labelName="billDetailList[{{idx}}].bin.binId" labelValue="{{row.bin.binId}}"
							 title="选择货区号" cssClass="form-control  " fieldLabels="货区编码|货区名称" fieldKeys="binId|binDesc" searchLabels="货区编码|货区名称" searchKeys="binId|binDesc"
							 targetId="billDetailList{{idx}}_binName" targetField="binDesc"></sys:gridselect-modify>
					</td>


					<td>
						<input id="billDetailList{{idx}}_binName" name="billDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"  readOnly="true"  class="form-control "/>
					</td>


					<td>
						<sys:gridselect-modify url="${ctx}/location/location/data" urlParamName="binId" urlParamId="billDetailList{{idx}}_binNames" id="billDetailList{{idx}}_loc" name="billDetailList[{{idx}}].loc.id" value="{{row.loc.id}}" labelName="billDetailList[{{idx}}].loc.locId" labelValue="{{row.loc.locId}}"
							 title="选择货位号" cssClass="form-control  " fieldLabels="货位编码|货位名称" fieldKeys="locId|locDesc" searchLabels="货位编码|货位名称" searchKeys="locId|locDesc"
							 targetId="billDetailList{{idx}}_locName" targetField="locDesc" ></sys:gridselect-modify>
					</td>


					<td>
						<input id="billDetailList{{idx}}_locName" name="billDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"  readOnly="true"   class="form-control "/>
					</td>
                    <td>
						<input id="billDetailList{{idx}}_itemBatch" name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}" readOnly="true"   class="form-control "/>
					</td>
                    <td>
						<input id="billDetailList{{idx}}_planPrice" name="billDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"  readOnly="true"   class="form-control "/>
					</td>
                    <td>
						<input id="billDetailList{{idx}}_itemPrice" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}" onkeyup="getItemSum('billDetailList{{idx}}')"   class="form-control required"/>
					</td>
					<td>
						<input id="billDetailList{{idx}}_itemSum" name="billDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control "/>
					</td>





					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#billDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr> //-->
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
                                            // $(".input-group-btn").hide()

                                            if("${type}" === "Query"){

                                                $('input').attr('readonly',true)
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
						<input id="billDetailCodeList{{idx}}_id" name="billDetailCodeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
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
						<input id="billDetailCodeList{{idx}}_activationBarcode" name="billDetailCodeList[{{idx}}].activationBarcode" type="text" value="{{row.activationBarcode}}"    class="form-control "/>
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
                                        <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/inboundinput/inboundInput?type=Input"><i class="ti-angle-left"></i> 返 回</a>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${type eq 'Query'}">
                            <div class="col-lg-3"></div>
                            <div class="col-lg-6">
                                <div class="form-group text-center">
                                    <div>
                                        <a class="btn btn-primary btn-block btn-parsley" href="${ctx}/inboundinput/inboundInput?type=Query"><i class="ti-angle-left"></i> 返 回</a>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <%--<c:if test="${type eq 'Post'}">
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
                                        <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/inboundinput/inboundInput?type=Post"><i class="ti-angle-left"></i> 返 回</a>
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

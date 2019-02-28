<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>成品出库单据录入管理</title>
    <meta name="decorator" content="ani"/>
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
            var url='http://localhost:8080/a/invaccountcode/invAccountCode/data?itemBarcode='+code.value
            $.ajax({
                url:url,
                success:function (data) {
                    try {
                        $('#billDetailCodeList' + index + '_machineBarcode').val(data.rows[0].machineBarcode)
                        $('#billDetailCodeList' + index + '_activationBarcode').val(data.rows[0].activationBarcode)
                    }catch (e) {
                    }
                }
            })
        }
		function sum(id){
			var price=parseInt($("#billDetailList"+id+"_itemPrice").val());
			var mun=parseInt($("#billDetailList"+id+"_itemQty").val());
			if(price&&mun)
				$("#billDetailList"+id+"_itemSum").val((mun*price).toString());
			else $("#billDetailList"+id+"_itemSum").val('');
		}
        function getInv(index){
            var str=$('#period\\.periodId').val();
            $.ajax({
                url:"${ctx}/outboundinput/outboundInput/getInv",
                data:{
                    "ware\.wareID":$('#wareNames').val(),
                    year:str.substring(0,4),
                    period:str.substring(4,6),
                    "item\.code":$('#billDetailList'+index+'_itemNames').val(),
                },
                dataType:'json',
                success:function(data){
                    try {
                        $('#billDetailList' + index + '_itemId').val(data.item.id);
                        $('#billDetailList' + index + '_itemNames').val(data.item.code);
                        $('#billDetailList' + index + '_binId').val(data.bin.id);
                        $('#billDetailList' + index + '_binNames').val(data.bin.binId);
                        $('#billDetailList' + index + '_binName').val(data.bin.binDesc);
                        $('#billDetailList' + index + '_locId').val(data.loc.id);
                        $('#billDetailList' + index + '_locNames').val(data.loc.locId);
                        $('#billDetailList' + index + '_locName').val(data.loc.locDesc);
                    }catch (e) {
                        console.log(e)
                    }
                },
            });
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
						$('#wareEmpId').val(re.id);
						$('#wareEmp\\.user\\.no').val(re.user.no);
						$('#wareEmpname').val(re.empName);
					}				
	            });
			});
            $('#pickBillNames,#MyButton').click(function () {
            	$('tr td').remove();
                top.layer.open({
                    type: 2,
                    area: ['800px', '500px'],
                    title:"选择送货单",
                    auto:true,
                    name:'friend',
                    content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/pickbill/pickBill/data?pbillStat=E")+"&fieldLabels="+encodeURIComponent("发货单号")+"&fieldKeys="+encodeURIComponent("pbillCode")+"&searchLabels="+encodeURIComponent("发货单号")+"&searchKeys="+encodeURIComponent("pbillCode")+"&isMultiSelected=false",
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
                        $('#pickBillId').val(item.id);
                        $('#pickBillNames').val(item.pbillCode);
                       // $('#wareId').val(item.ware.id);
                       // $('#wareId').trigger('change');
                       	$("#accountId,#accountNames").val(item.rcvAccount.accountCode)
                       	$('#accountNamed').val(item.rcvAccountName)
                        //$('#wareName').val(item.wareName);
                        //$('#wareName').trigger('change');
                        $('#rcvAddr').val(item.rcvAddr);
                        $('#transAccount').val(item.transAccount);
                        $.get("${ctx}/pickbill/pickBill/detail?id="+item.id, function(details){
                            console.log(details);
                            for(var i = 0;i < details.pickBillItemList.length;i++){
                            	detail=details.pickBillItemList[i];
                            	console.log(detail);
                            	try{
	                            	detail.id="";
	                            	detail.item=new Object();
                                        detail.item.id = detail.prodCode.id;
                                    if('item' in detail.prodCode){
                                        detail.item.code = 'code' in detail.prodCode.item ? detail.prodCode.item.code : null;
                                    }
                                    detail.item.id = detail.item.code
	                            	detail.planPrice=detail.prodPrice;
	                            	detail.itemPrice=detail.prodPriceTaxed;
	                            	detail.itemName=detail.prodName;
	                            	detail.itemSpec=detail.prodSpec;
	                            	detail.itemQty=detail.pickQty;
	                                detail.measUnit=detail.unitName;
	                                addRow('#billDetailList',i,billDetailTpl,detail);
	                                $('#billDetailList'+i+'_itemQty').trigger('onkeyup');
	                            	getInv(i);
                            	}catch(err){
                            		console.log(err);
                            	}
                            }
                        });
                        jp.close(test);
                        top.layer.close(index);//关闭对话框。
                    },
                    cancel: function(index){
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
            iniItemNum();
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
    </script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <a class="panelButton" href="${ctx}/outboundinput/outboundInput?type=Input"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="billMain" action="${ctx}/outboundinput/outboundInput/save?type=Input" method="post" class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message content="${message}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>单据编号：</label>
                            <div class="col-sm-10">
                                <form:input path="billNum"  readOnly="true" htmlEscape="false"    class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>单据状态：</label>
                            <div class="col-sm-10">
                               <input value="新增" readOnly="ture" class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>出库日期：</label>
                            <div class="col-sm-10">
                                <p class="input-group">
                                <div class='input-group form_datetime' id='billDate'>
                                    <input type='text' readonly="true" name="billDate" class="form-control required"  value="<fmt:formatDate value="${billMain.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
								<form:input readOnly="true" path="period.periodId" htmlEscape="false"    class="form-control required"/>
							</div>
							<form:input style="display:none;" readOnly="true" path="period.id" htmlEscape="false"    class="form-control required"/>
						</div>
                       <div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>发货单：</label>
							<input id="pickBillId" value="${billMain.corBillNum}" type="hidden" name="saleId" value="" />
                           <div class="col-sm-10">
                                <div class="input-group">
                                    <input  id="pickBillNames" readOnly="readOnly"   class="form-control required"/>
                                   <span class="input-group-btn">
                                             <button type="button" id="MyButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
                                             </button>
                                       </button>
                                       <button type="button" id="MyDelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
                                   </span>
                                </div>
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
							<label class="col-sm-2 control-label"><font color="red">*</font>客户编码：</label>
							<div class="col-sm-10">
								<sys:gridselect-item   extraField="accountId:accountCode;accountNames:accountCode;accountNamed:accountName"  url="${ctx}/account/account/data?accTypeNameRu=客户" id="account" name="account.accountCode" value="${billMain.account.id}" labelName="" labelValue="${billMain.account.accountCode}"
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
                                <sys:treeselect-modify id="dept" name="dept.code" value="${billMain.dept.code}" labelName="dept.code" labelValue="${billMain.dept.code}"
                                                title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"
                                                targetId="deptname" targetField="code"></sys:treeselect-modify>
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
                                <form:hidden path="billPerson.no"/>
                                <form:input  readonly="true" path="billPerson.loginName" htmlEscape="false"    class="form-control required"/>
                            </div>
                        </div>
                       <div class="form-group">
							<label class="col-sm-2 control-label"><font color="red">*</font>库管员编码：</label>
							<div class="col-sm-10">
                                <form:input  readonly="true" path="wareEmp.user.no" htmlEscape="false"    class="form-control required"/>
							</div>
						</div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>库管员名称：</label>
                            <div class="col-sm-10">
                                <form:input  id="wareEmpname" path="wareEmpname" readOnly="true"   class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>收货地址：</label>
                            <div class="col-sm-10">
                                <form:input  path="rcvAddr"    class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>承运人：</label>
                            <div class="col-sm-10">
                                <form:input  path="transAccount"    class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>承运车号：</label>
                            <div class="col-sm-10">
                                <form:input  path="carNum"    class="form-control required"/>
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
                                    <%--<a class="btn btn-white btn-sm" onclick="addRow('#billDetailList', billDetailRowIdx, billDetailTpl);billDetailRowIdx = billDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
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
							url="${ctx}/invaccount/invAccount/data" id="billDetailList{{idx}}_item" name="billDetailList[{{idx}}].item.id" value="{{row.item.id}}" labelName="billDetailList[{{idx}}].item.code" labelValue="{{row.item.id}}"
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
						<sys:gridselect-item  url="${ctx}/bin/bin/data" id="billDetailList{{idx}}_bin" name="billDetailList[{{idx}}].bin.id" value="{{row.bin.id}}" labelName="billDetailList[{{idx}}].bin.binId" labelValue="{{row.bin.id}}"
							 title="选择货区号" cssClass="form-control  " fieldLabels="货区编码|货区名称" fieldKeys="binId|binDesc" searchLabels="货区编码|货区名称" searchKeys="binId|binDesc"
							 extraField="billDetailList0_binNames:binId;billDetailList{{idx}}_binName:binDesc"
							 limite="wareNames:wareId"></sys:gridselect-item>
					</td>


					<td>
						<input id="billDetailList{{idx}}_binName" readonly="readonly" name="billDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"    class="form-control "/>
					</td>


					<td>
						<sys:gridselect-item  url="${ctx}/location/location/data" id="billDetailList{{idx}}_loc" name="billDetailList[{{idx}}].loc.id" value="{{row.loc.id}}" labelName="billDetailList[{{idx}}].loc.locId" labelValue="{{row.loc.id}}"
							 title="选择货位号" cssClass="form-control  " fieldLabels="货位编码|货位名称" fieldKeys="locId|locDesc" searchLabels="货位编码|货位名称" searchKeys="locId|locDesc"
							 extraField="billDetailList{{idx}}_locNames:locId;billDetailList{{idx}}_locName:locDesc" targetField="locDesc"
							 limite="billDetailList{{idx}}_binNames:binId"></sys:gridselect-item>
					</td>


					<td>
						<input id="billDetailList{{idx}}_locName" readonly="readonly" name="billDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"    class="form-control "/>
					</td>
                    <td>
						<input id="billDetailList{{idx}}_itemBatch" readonly="readonly" name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>
                    <td>
						<input id="billDetailList{{idx}}_planPrice" readonly="readonly" name="billDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"    class="form-control "/>
					</td>
                    <td>
						<input id="billDetailList{{idx}}_itemPrice" readonly="readonly" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"  onkeyup="sum({{idx}})"  class="form-control "/>
					</td>


					<td>
						<input id="billDetailList{{idx}}_itemSum" readonly="readonly" name="billDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control required"/>
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
                        <c:if test="${fns:hasPermission('billmain:billMain:edit') || isAdd}">
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
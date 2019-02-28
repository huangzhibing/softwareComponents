<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>单据管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
		    var item;
			$("#inputForm").validate({
				submitHandler: function(form){
					jp.loading();
					$('#billFlag').attr("disabled", false);
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
			//     $(".close").hide()
			// 	$("#wareButton").hide()
			// 	$("#deptButton").hide()
			// 	$("#billPersonButton").hide()
			// 	$("#wareEmpButton").hide()
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
	            if("${type}" === 'query' || "${type}" === 'post'){
	                return false
                }
                top.layer.open({
                    type: 2,
                    area: ['800px', '500px'],
                    title:"选择盘点单",
                    auto:true,
                    name:'friend',
                    content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/countdata/countMain/data?procFlag=Y")+"&fieldLabels="+encodeURIComponent("盘点单号|库房名称")+"&fieldKeys="+encodeURIComponent("billNum|wareName")+"&searchLabels="+encodeURIComponent("盘点单号|库房名称")+"&searchKeys="+encodeURIComponent("billNum|wareName")+"&isMultiSelected=false",
                    btn: ['确定', '关闭'],
                    yes: function(index, layero){
                        var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                        var items = iframeWin.getSelections();
                        if(items == ""){
                            jp.warning("必须选择一条数据!");
                            return;
                        }
                        item = items[0]
                        console.log(item)
                        var loading = jp.loading("加载中");
                        $.get("${ctx}/countdata/countMain/detail?id="+item.id, function(countMain){
                            console.log(countMain)
							if(countMain) {
                                $('#wareName').val(countMain.wareName)
                                $('#wareNames').val(countMain.ware.wareID)
                                $('#wareId').val(countMain.ware.id)
                                $('#corBillNum').val(countMain.billNum)
                                $('#wareEmpId').val(countMain.checkEmpId.id)
                                $('#wareEmpNames').val(countMain.checkEmpId.no)
                                $('#wareEmpname').val(countMain.checkEmpId.name)
                                for (var i = 0; i < countMain.countDetailList.length; i++) {
                                    countMain.countDetailList[i].serialNum = i + 1
                                    countMain.countDetailList[i].itemSpec = countMain.countDetailList[i].specModel
									if(countMain.countDetailList[i].measUnit) {
                                        countMain.countDetailList[i].measUnit = countMain.countDetailList[i].measUnit.unitName
                                    }
                                    countMain.countDetailList[i].id = ""
                                    countMain.countDetailList[i].itemQty = countMain.countDetailList[i].realQty - countMain.countDetailList[i].itemQty
                                    console.log(countMain.countDetailList[i])
                                    addRow('#billDetailList', i + 1, billDetailTpl, countMain.countDetailList[i])
                                }
                            }
                            jp.close(loading)
                        })
						jp.close(loading);
                        top.layer.close(index);//关闭对话框。
                    },
                    cancel: function(index){
                    }
                });
            })
	        $('#corBillNumButton').click(function () {
	            if("${type}" === 'query' || "${type}" === 'post'){
	                return false
                }
                top.layer.open({
                    type: 2,
                    area: ['800px', '500px'],
                    title:"选择盘点单",
                    auto:true,
                    name:'friend',
                    content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/countdata/countMain/data?procFlag=Y")+"&fieldLabels="+encodeURIComponent("盘点单号|库房名称")+"&fieldKeys="+encodeURIComponent("billNum|wareName")+"&searchLabels="+encodeURIComponent("盘点单号|库房名称")+"&searchKeys="+encodeURIComponent("billNum|wareName")+"&isMultiSelected=false",
                    btn: ['确定', '关闭'],
                    yes: function(index, layero){
                        var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                        var items = iframeWin.getSelections();
                        if(items == ""){
                            jp.warning("必须选择一条数据!");
                            return;
                        }
                        item = items[0]
                        console.log(item)
                        var loading = jp.loading("加载中");
                        $.get("${ctx}/countdata/countMain/detail?id="+item.id, function(countMain){
                            console.log(countMain)
							if(countMain) {
                                $('#wareName').val(countMain.wareName)
                                $('#wareNames').val(countMain.ware.wareID)
                                $('#wareId').val(countMain.ware.id)
                                $('#corBillNum').val(countMain.billNum)
                                $('#wareEmpId').val(countMain.checkEmpId.id)
                                $('#wareEmpNames').val(countMain.checkEmpId.no)
                                $('#wareEmpname').val(countMain.checkEmpId.name)
                                for (var i = 0; i < countMain.countDetailList.length; i++) {
                                    countMain.countDetailList[i].serialNum = i + 1
                                    countMain.countDetailList[i].itemSpec = countMain.countDetailList[i].specModel
									if(countMain.countDetailList[i].measUnit) {
                                        countMain.countDetailList[i].measUnit = countMain.countDetailList[i].measUnit.unitName
                                    }
                                    countMain.countDetailList[i].id = ""
                                    countMain.countDetailList[i].itemQty = countMain.countDetailList[i].realQty - countMain.countDetailList[i].itemQty
                                    console.log(countMain.countDetailList[i])
                                    addRow('#billDetailList', i + 1, billDetailTpl, countMain.countDetailList[i])
                                }
                            }
                            jp.close(loading)
                        })
						jp.close(loading);
                        top.layer.close(index);//关闭对话框。
                    },
                    cancel: function(index){
                    }
                });
            })
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
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="billMain" action="${ctx}/inventoryprofit/inventoryProfit/save?type=${type}" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message-type content="${message}"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据号：</label>
					<div class="col-sm-10">
						<form:input readonly="true" path="billNum" htmlEscape="false"    class="form-control required"/>
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
					<label class="col-sm-2 control-label"><font color="red">*</font>盘点日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='billDate'>
			                    <input type='text'  readonly="true"  name="billDate" class="form-control required"  value="<fmt:formatDate value="${billMain.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>

                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>盘点单号：</label>
                    <div class="col-sm-10 input-group">
                        <form:input readonly="true" path="corBillNum" htmlEscape="false"    class="form-control "/>
						<span class="input-group-btn">
                                         <button type="button"  id="corBillNumButton" class="btn <c:if test="${fn:contains(cssClass, 'input-sm')}"> btn-sm </c:if><c:if test="${fn:contains(cssClass, 'input-lg')}"> btn-lg </c:if>  btn-primary ${disabled} ${hideBtn ? 'hide' : ''}"><i class="fa fa-search"></i>
                                         </button>
							 </span>
                    </div>
                </div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>部门编码：</label>
					<div class="col-sm-10">
						<sys:treeselect-modify targetId="aaa" targetField="nothing" disableClick="true" id="dept" name="dept.code" value="${billMain.dept.code}" labelName="dept.codes" labelValue="${billMain.dept.code}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>部门名称：</label>
					<div class="col-sm-10">
						<form:input  readonly="true" path="deptName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
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
					<label class="col-sm-2 control-label"><font color="red">*</font>经办人：</label>
					<div class="col-sm-10">
						<sys:userselect-modify targetId="aaa" disableClick="true" id="billPerson" name="billPerson.id" value="${billMain.billPerson.no}" labelName="billPerson.loginName" labelValue="${billMain.billPerson.loginName}"
							    cssClass="form-control "/>
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
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input path="note" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">出入库单据明细：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#billDetailList', billDetailRowIdx, billDetailTpl);billDetailRowIdx = billDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
				<div class="table-responsive" style="max-height:500px">
					<table class="table table-striped table-bordered table-condensed" style="min-width:1350px">                                        <thead>

					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>编号</th>
                        <th><font color="red">*</font>物料代码</th>
                        <th>物料名称</th>
                        <th>规格型号</th>
                        <th><font color="red">*</font>调整数量</th>
                        <th>计量单位</th>
						<th>货区编码</th>
						<th>货区名称</th>
						<th>货位编码</th>
						<th>货位名称</th>
                        <th>批次</th>
						<th><font color="red">*</font>实际单价</th>
						<th><font color="red">*</font>实际金额</th>
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
						<input id="billDetailList{{idx}}_serialNum" readonly="true" name="billDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>

					<td>
						<sys:gridselect-modify targetId="aaa" targetField="aaa" disableClick="true" url="${ctx}/item/item/data" id="billDetailList{{idx}}_item" name="billDetailList[{{idx}}].item.id" value="{{row.item.id}}" labelName="billDetailList{{idx}}.item.code" labelValue="{{row.item.code}}"
							 title="选择物料代码" cssClass="form-control  required" fieldLabels="物料代码|物料名称|规格型号" fieldKeys="code|name|specModel" searchLabels="物料代码|物料名称|规格型号" searchKeys="code|name|specModel" ></sys:gridselect-modify>
					</td>


					<td>
						<input id="billDetailList{{idx}}_itemName" readonly="true" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>


					<td>
						<input id="billDetailList{{idx}}_itemSpec" readonly="true" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>

					<td>
						<input id="billDetailList{{idx}}_itemQty" readonly="true" name="billDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control "/>
					</td>




					<td>
						<input id="billDetailList{{idx}}_measUnit" name="billDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"    class="form-control "/>
					</td>

					<td>
						<sys:gridselect-modify disableClick="true" targetId="billDetailList{{idx}}_binName" targetField="binDesc" url="${ctx}/bin/bin/data" id="billDetailList{{idx}}_bin" name="billDetailList[{{idx}}].bin.id" value="{{row.bin.id}}" labelName="billDetailList{{idx}}.bin.binId" labelValue="{{row.bin.binId}}"
							 title="选择货区号" cssClass="form-control  " fieldLabels="货区号|货区名称" fieldKeys="binId|binDesc" searchLabels="货区号|货区名称" searchKeys="binId|binDesc" ></sys:gridselect-modify>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_binName" readonly="true" name="billDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<sys:gridselect-modify disableClick="true"  targetId="billDetailList{{idx}}_locName" targetField="locDesc" url="${ctx}/location/location/data" id="billDetailList{{idx}}_loc" name="billDetailList[{{idx}}].loc.id" value="{{row.loc.id}}" labelName="billDetailList{{idx}}.loc.locId" labelValue="{{row.loc.locId}}"
							 title="选择货位号" cssClass="form-control  " fieldLabels="货位号|货位名称" fieldKeys="locId|locDesc" searchLabels="货位号|货位名称" searchKeys="locId|locDesc" ></sys:gridselect-modify>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_locName" readonly="true" name="billDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"    class="form-control "/>
					</td>
					
					


					<td>
						<input id="billDetailList{{idx}}_itemBatch" name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>

					

					
					<td>
						<input id="billDetailList{{idx}}_itemPrice" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="billDetailList{{idx}}_itemSum" name="billDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control required"/>
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
                    // $(".input-group-btn").hide()

                    if("${type}" === "query" || "${type}" === "post"){

						$('input').attr('readonly',true)
                    }
				});
			</script>
			</div>
		</div>
		</div>
		<%--<c:if test="${fns:hasPermission('billmain:billMain:edit') || isAdd}">--%>
			<c:if test="${type eq 'input'}">
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
							 <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/inventoryprofit/inventoryProfit?type=input"><i class="ti-angle-left"></i> 返 回</a>
						 </div>
		             </div>
		        </div>
			</c:if>
			<c:if test="${type eq 'query'}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
						 <div>
							 <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/inventoryprofit/inventoryProfit?type=query"><i class="ti-angle-left"></i> 返 回</a>
		                 </div>
		             </div>
		        </div>
			</c:if>
			<c:if test="${type eq 'post'}">
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
							 <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/inventoryprofit/inventoryProfit?type=post"><i class="ti-angle-left"></i> 返 回</a>
						 </div>
		             </div>
		        </div>
			</c:if>
		<%--</c:if>--%>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
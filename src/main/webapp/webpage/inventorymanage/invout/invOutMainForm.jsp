<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>放行单管理</title>
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
            $('#post').click(function(){
                $('#start').val('1')
            })

	        $('#makeDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#awayDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });

        if($('#corBillNum\\.id').val()){
            var loading = jp.loading("加载中");
            $.get("${ctx}/outboundinput/outboundInput/detail?id="+$('#corBillNum\\.id').val(), function(corBillNum){
                $('#corBillNum\\.billNum').val(corBillNum.billNum)
                console.log($('#corBillNum\\.id').val())
                $('#corBillNum').val(corBillNum.billNum)
                $('#wareEmpId').val(corBillNum.wareEmp.id)
                $('#wareEmpNames').val(corBillNum.wareEmp.id)
                $('#wareEmpName').val(corBillNum.wareEmpname)
                $('#wareNames').val(corBillNum.ware.wareID)
                $('#wareName').val(corBillNum.wareName)
                $('#wareId').val(corBillNum.ware.id)
//                 for(var i = 0;i < corBillNum.billDetailList.length;i++){
// //                            addRow('#invOutDetailList', invOutDetailRowIdx, invOutDetailTpl);
//                     corBillNum.billDetailList[i].id=""
//                     addRow('#invOutDetailList',i,invOutDetailTpl,corBillNum.billDetailList[i])
//                     $('invOutDetailList'+i+'_itemCode').val(corBillNum.billDetailList[i].itemCode)
//                     $('invOutDetailList'+i+'_itemSpec').val(corBillNum.billDetailList[i].itemSpec)
//                     $('invOutDetailList'+i+'_itemName').val(corBillNum.billDetailList[i].itemName)
//                     $('invOutDetailList'+i+'_measUnit').val(corBillNum.billDetailList[i].measUnit)
//                     $('invOutDetailList'+i+'_itemQty').val(corBillNum.billDetailList[i].itemQty)
//                 }
                jp.close(loading)
            }
            )
        }

        $('#corBillNum').click(function () {
            console.log('aaa')
            top.layer.open({
                type: 2,
                area: ['800px', '500px'],
                title:"选择出库单",
                auto:true,
                name:'friend',
                content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/outboundinput/outboundInput/data2?billFlag=T")+"&fieldLabels="+encodeURIComponent("出库单号")+"&fieldKeys="+encodeURIComponent("billNum")+"&searchLabels="+encodeURIComponent("出库单号")+"&searchKeys="+encodeURIComponent("billNum")+"&isMultiSelected=false",
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
                    $.get("${ctx}/outboundinput/outboundInput/detail?id="+item.id, function(corBillNum){
                        console.log(corBillNum)
                        $('#corBillNum\\.billNum').val(corBillNum.billNum)
                        console.log($('#corBillNum\\.id').val())
                        $('#corBillNum').val(corBillNum.billNum)
                        $('#wareEmpId').val(corBillNum.wareEmp.user.no)
                        $('#wareEmpNames').val(corBillNum.wareEmp.user.no)
                        $('#wareEmpName').val(corBillNum.wareEmpname)
                        $('#wareNames').val(corBillNum.ware.wareID)
                        $('#wareName').val(corBillNum.wareName)
                        $('#wareId').val(corBillNum.ware.id)
                        $('#deptId').val(corBillNum.dept.code)
                        $('#deptNames').val(corBillNum.deptName)
                        $('input[name="deptName"]').val(corBillNum.deptName)
                        $('#deptName').val(corBillNum.dept.code)
                        $('#carrierName').val(corBillNum.transAccount)
                        $('#carNum').val(corBillNum.carNum)
                        for(var i = 0;i < corBillNum.billDetailList.length;i++){
//                            addRow('#invOutDetailList', invOutDetailRowIdx, invOutDetailTpl);
							corBillNum.billDetailList[i].id=""
                            addRow('#invOutDetailList',i,invOutDetailTpl,corBillNum.billDetailList[i])
							console.log(corBillNum.billDetailList[i].item.code)
                            $('#invOutDetailList'+i+'_itemCode').val(corBillNum.billDetailList[i].item.id)
                            $('invOutDetailList'+i+'_itemSpec').val(corBillNum.billDetailList[i].itemSpec)
                            $('invOutDetailList'+i+'_itemName').val(corBillNum.billDetailList[i].itemName)
                            $('invOutDetailList'+i+'_measUnit').val(corBillNum.billDetailList[i].measUnit)
                            $('invOutDetailList'+i+'_itemQty').val(corBillNum.billDetailList[i].itemQty)
                        }

                        jp.close(loading)
                    })
                    jp.close(loading)
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
				<a class="panelButton" href="${ctx}/invout/invOutMain"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="invOutMain" action="${ctx}/invout/invOutMain/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
			<input hidden id="start" name="start" value="0" />
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据号：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>出库单号：</label>
					<div class="col-sm-10">
						<form:input readonly="true" cssStyle="display: none" path="corBillNum.billNum" htmlEscape="false"    class="form-control "/>
						<input type="text" readonly id="corBillNum" class="form-control required" value="${invOutMain.corBillNum.billNum}" placeholder="请点击选择出库单"/>

					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='makeDate'>
			                    <input type='text'  name="makeDate" class="form-control required"  value="<fmt:formatDate value="${invOutMain.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人编码：</label>
					<div class="col-sm-10">
						<sys:userselect-modify targetId="makeEmpName" id="makeEmp" name="makeEmp.no" value="${invOutMain.makeEmp.no}" labelName="makeEmp.no" labelValue="${invOutMain.makeEmp.no}"
							    cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人名称：</label>
					<div class="col-sm-10">
						<form:input path="makeEmpName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group" style="display: none">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据状态：</label>
					<div class="col-sm-10">
						<form:select path="billFlag" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('pbillStat')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>仓库编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify targetField="wareId" targetId="wareName" disableClick="true" url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.wareID" value="${invOutMain.ware.wareID}" labelName="ware.wareID" labelValue="${invOutMain.ware.wareID}"
							 title="选择仓库编码" cssClass="form-control required" fieldLabels="仓库编码|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库编码|仓库名称" searchKeys="wareID|wareName" ></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>仓库名称：</label>
					<div class="col-sm-10">
						<form:input path="wareName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库管员编码：</label>
					<div class="col-sm-10">
						<sys:userselect-modify targetId="wareEmpName" id="wareEmp" name="wareEmp.no" value="${invOutMain.wareEmp.no}" labelName="wareEmp.no" labelValue="${invOutMain.wareEmp.no}"
							    cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库管员姓名：</label>
					<div class="col-sm-10">
						<form:input path="wareEmpName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>部门编码：</label>
					<div class="col-sm-10">
						<sys:treeselect-modify targetField="aaa" targetId="aaa"  id="dept" name="dept.code" value="${invOutMain.dept.code}" labelName="dept.code" labelValue="${invOutMain.dept.code}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>部门名称：</label>
					<div class="col-sm-10">
						<form:input path="deptName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">承运商名称：</label>
					<div class="col-sm-10">
						<form:input path="carrierName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">承运车号：</label>
					<div class="col-sm-10">
						<form:input path="carNum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>出厂日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='awayDate'>
			                    <input type='text'  name="awayDate" class="form-control required"  value="<fmt:formatDate value="${invOutMain.awayDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">放行单明细：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#invOutDetailList', invOutDetailRowIdx, invOutDetailTpl);invOutDetailRowIdx = invOutDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>序号</th>
						<th><font color="red">*</font>物料编码</th>
						<th><font color="red">*</font>物料名称</th>
						<th><font color="red">*</font>物料规格</th>
						<th><font color="red">*</font>单位</th>
						<th><font color="red">*</font>数量</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="invOutDetailList">
				</tbody>
			</table>
			<script type="text/template" id="invOutDetailTpl">//<!--
				<tr id="invOutDetailList{{idx}}">
					<td class="hide">
						<input id="invOutDetailList{{idx}}_id" name="invOutDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="invOutDetailList{{idx}}_delFlag" name="invOutDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="invOutDetailList{{idx}}_serialNum" name="invOutDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="invOutDetailList{{idx}}_itemCode" name="invOutDetailList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"    class="form-control required"/>
					</td>
					

					
					<td>
						<input id="invOutDetailList{{idx}}_itemName" name="invOutDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control required"/>
					</td>

					<td>
						<input id="invOutDetailList{{idx}}_itemSpec" name="invOutDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control required"/>
					</td>
					
					<td>
						<input id="invOutDetailList{{idx}}_measUnit" name="invOutDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="invOutDetailList{{idx}}_itemQty" name="invOutDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control required"/>
					</td>
					

				</tr>//-->
			</script>
			<script type="text/javascript">
				var invOutDetailRowIdx = 0, invOutDetailTpl = $("#invOutDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(invOutMain.invOutDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#invOutDetailList', invOutDetailRowIdx, invOutDetailTpl, data[i]);
						invOutDetailRowIdx = invOutDetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('invout:invOutMain:edit') || isAdd}">
			<div class="col-lg-1"></div>
			<div class="col-lg-3">
				<div class="form-group text-center">
					<div>
						<button id="save" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在保存...">保 存</button>
					</div>
				</div>
			</div>

			<div class="col-lg-3"></div>
			<div class="col-lg-3">
				<div class="form-group text-center">
					<div>
						<button id="post" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>超期复验单管理</title>
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
			
	        $('#makeDate').datetimepicker({
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
        $(document).ready(function(){
            $('#wareId').on('change',function(){
                var index=jp.loading();
                $.ajax({
                    url:"${ctx}/outboundinput/outboundInput/getEmp",
                    data:{wareId:$('#wareId').val()},
                    dataType:'json',
                    success:function(re){
                        $('#wareEmpIdName').val(re.user.no);
                        $('#wareEmpName').val(re.user.name);
                        jp.close(index);
                    }
                });
                $.ajax({
                    url:"${ctx}/newinvcheckmain/newinvCheckMain/findcodelist",
                    data:{wareId:$('#wareId').val()},
                    dataType:'json',
                    success:function (request) {
                        if(request.rows.length==0){
                            jp.warning("该仓库没有超期物料")
                        }
                        for(var i=0;i<request.rows.length;i++){
                            //addRow('#newinvCheckDetailCodeList', newinvCheckDetailCodeRowIdx, newinvCheckDetailCodeTpl, data[i]);
                            addRow('#newinvCheckDetailCodeList',i,newinvCheckDetailCodeTpl,request.rows[i])
                            $('#newinvCheckDetailCodeList'+i+'_id').val("")
                            $('#newinvCheckDetailCodeList'+i+'_serialNum').val((i+1))
                            $('#newinvCheckDetailCodeList'+i+'_itemCode').val(request.rows[i].item.code)
                            $('#newinvCheckDetailCodeList'+i+'_itemBarcode').val(request.rows[i].itemBarcode)
                            $('#newinvCheckDetailCodeList'+i+'_supBarcode').val(request.rows[i].supBarcode)
                        }
                    }
                });
                $.ajax({
                    url:"${ctx}/newinvcheckmain/newinvCheckMain/findlist",
                    data:{wareId:$('#wareId').val()},
                    dataType:'json',
                    success:function (request) {
                        for(var i=0;i<request.rows.length;i++){
                            //addRow('#newinvCheckDetailList', newinvCheckDetailRowIdx, newinvCheckDetailTpl, data[i]);
                            addRow('#newinvCheckDetailList',i,newinvCheckDetailTpl,request.rows[i])
                            $('#newinvCheckDetailList'+i+'_id').val("")
                            $('#newinvCheckDetailList'+i+'_serialNum').val((i+1))
                            $('#newinvCheckDetailList'+i+'_itemCodeName').val(request.rows[i].item.id)
                            $('#newinvCheckDetailList'+i+'_itemName').val(request.rows[i].itemName)
                            $('#newinvCheckDetailList'+i+'_itemSpec').val(request.rows[i].item.specModel)
                            $('#newinvCheckDetailList'+i+'_measUnit').val(request.rows[i].item.unitCode.unitName)
                            $('#newinvCheckDetailList'+i+'_itemPrice').val(request.rows[i].item.planPrice)
                            $('#newinvCheckDetailList'+i+'_itemQty').val(request.rows[i].itemQty)
                            $('#newinvCheckDetailList'+i+'_itemSum').val(request.rows[i].nowSum)
                            //$('#newinvCheckDetailList'+i+'_itemBatch').val(request.rows[i].itemBatch)
                            //$('#newinvCheckDetailList'+i+'_binId').val(request.rows[i].bin)
                            //$('#newinvCheckDetailList'+i+'_binName').val(request.rows[i].binName)
                            //$('#newinvCheckDetailList'+i+'_locId').val(request.rows[i].loc)
                            //$('#newinvCheckDetailList'+i+'_locName').val(request.rows[i].locName)
                        }
                    }
                });
            });

        })
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/newinvcheckmain/newinvCheckMain/list"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="newinvCheckMain" action="${ctx}/newinvcheckmain/newinvCheckMain/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">单据号：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">核算期：</label>
					<div class="col-sm-10">
						<form:input path="periodId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='makeDate'>
			                    <input type='text'  name="makeDate" class="form-control "  value="<fmt:formatDate value="${newinvCheckMain.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
						<sys:userselect id="makeEmpid" name="makeEmpid.id" value="${newinvCheckMain.makeEmpid.id}" labelName="makeEmpid.no" labelValue="${newinvCheckMain.makeEmpid.no}"
							    cssClass="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人姓名：</label>
					<div class="col-sm-10">
						<form:input path="makeEmpName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-10">
						<form:select path="billFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('reviewbillflag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">仓库编码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify url="${ctx}/warehouse/wareHouse/data" id="ware" name="wareId.wareID" value="${newinvCheckMain.wareId.wareID}" labelName="wareId.wareID" labelValue="${newinvCheckMain.wareId.wareID}"
							 title="选择仓库编码" cssClass="form-control" fieldLabels="仓库编码|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库编码|仓库名称" searchKeys="wareID|wareName"
										targetId="wareName" targetField="wareName"></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">仓库名称：</label>
					<div class="col-sm-10">
						<form:input path="wareName" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库管员编码：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/employee/employee/data" id="wareEmpId" name="wareEmpId.id" value="${newinvCheckMain.wareEmpId.user.no}" labelName="wareEmpId.empID" labelValue="${newinvCheckMain.wareEmpId.user.no}"
							 title="选择库管员编码" cssClass="form-control" fieldLabels="库管员代码|库管员名称" fieldKeys="user.no|empName" searchLabels="库管员代码|库管员名称" searchKeys="user.no|empName" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库管员姓名：</label>
					<div class="col-sm-10">
						<form:input path="wareEmpName" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">复验员编码：</label>
					<div class="col-sm-10">
						<sys:userselect-modify targetId="checkEmpName" id="checkEmpId" name="checkEmpId.id" value="${newinvCheckMain.checkEmpId.id}" labelName="checkEmpId.no" labelValue="${newinvCheckMain.checkEmpId.no}"
											   cssClass="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">复验员姓名：</label>
					<div class="col-sm-10">
						<form:input path="checkEmpName" htmlEscape="false" readonly="true"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">质检标志：</label>
					<div class="col-sm-10">
						<form:input path="qmsFlag" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">超期复验子表：</a>
                </li>
				<li><a data-toggle="tab" href="#tab-2" aria-expanded="true">超期复验单扫码表：</a>
				</li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>序号</th>
						<th><font color="red">*</font>物料编号</th>
						<th>物料名称</th>
						<th>物料规格</th>
						<th><font color="red">*</font>单位</th>
						<th><font color="red">*</font>数量</th>
						<th>实际价格</th>
						<th>实际金额</th>
						<th>批次</th>
						<th>货区代码</th>
						<th>货区名称</th>
						<th>货位编码</th>
						<th>货位名称</th>
						<th>合格数量</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="newinvCheckDetailList">
				</tbody>
			</table>
			<script type="text/template" id="newinvCheckDetailTpl">//<!--
				<tr id="newinvCheckDetailList{{idx}}">
					<td class="hide">
						<input id="newinvCheckDetailList{{idx}}_id" name="newinvCheckDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="newinvCheckDetailList{{idx}}_delFlag" name="newinvCheckDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="newinvCheckDetailList{{idx}}_serialNum" name="newinvCheckDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<sys:gridselect url="${ctx}/InvAccount/invAccount/data" id="newinvCheckDetailList{{idx}}_itemCode" name="newinvCheckDetailList[{{idx}}].itemCode.id" value="{{row.itemCode.code}}" labelName="newinvCheckDetailList[{{idx}}].itemCode.code" labelValue="{{row.itemCode.code}}"
							 title="选择物料编号" cssClass="form-control  required" fieldLabels="" fieldKeys="" searchLabels="" searchKeys="" ></sys:gridselect>
					</td>

					<td>
						<input id="newinvCheckDetailList{{idx}}_itemName" name="newinvCheckDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>

					<td>
						<input id="newinvCheckDetailList{{idx}}_itemSpec" name="newinvCheckDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>
					
					<td>
						<input id="newinvCheckDetailList{{idx}}_measUnit" name="newinvCheckDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="newinvCheckDetailList{{idx}}_itemQty" name="newinvCheckDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="newinvCheckDetailList{{idx}}_itemPrice" name="newinvCheckDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="newinvCheckDetailList{{idx}}_itemSum" name="newinvCheckDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="newinvCheckDetailList{{idx}}_itemBatch" name="newinvCheckDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="newinvCheckDetailList{{idx}}_binId" name="newinvCheckDetailList[{{idx}}].binId" type="text" value="{{row.binId}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="newinvCheckDetailList{{idx}}_binName" name="newinvCheckDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="newinvCheckDetailList{{idx}}_locId" name="newinvCheckDetailList[{{idx}}].locId" type="text" value="{{row.locId}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="newinvCheckDetailList{{idx}}_locName" name="newinvCheckDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="newinvCheckDetailList{{idx}}_hgQty" name="newinvCheckDetailList[{{idx}}].hgQty" type="text" value="{{row.hgQty}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#newinvCheckDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var newinvCheckDetailRowIdx = 0, newinvCheckDetailTpl = $("#newinvCheckDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(newinvCheckMain.newinvCheckDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#newinvCheckDetailList', newinvCheckDetailRowIdx, newinvCheckDetailTpl, data[i]);
						newinvCheckDetailRowIdx = newinvCheckDetailRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-2" class="tab-pane fade">
					<a class="btn btn-white btn-sm" onclick="addRow('#newinvCheckDetailCodeList', newinvCheckDetailCodeRowIdx, newinvCheckDetailCodeTpl);newinvCheckDetailCodeRowIdx = newinvCheckDetailCodeRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
					<table class="table table-striped table-bordered table-condensed">
						<thead>
						<tr>
							<th class="hide"></th>
							<th>序号</th>
							<th>物料编号</th>
							<th>物料二维码</th>
							<th>供应商二维码</th>
							<th width="10">&nbsp;</th>
						</tr>
						</thead>
						<tbody id="newinvCheckDetailCodeList">
						</tbody>
					</table>
					<script type="text/template" id="newinvCheckDetailCodeTpl">//<!--
				<tr id="newinvCheckDetailCodeList{{idx}}">
					<td class="hide">
						<input id="newinvCheckDetailCodeList{{idx}}_id" name="newinvCheckDetailCodeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="newinvCheckDetailCodeList{{idx}}_delFlag" name="newinvCheckDetailCodeList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>


					<td>
						<input id="newinvCheckDetailCodeList{{idx}}_serialNum" name="newinvCheckDetailCodeList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control "/>
					</td>


					<td>
						<input id="newinvCheckDetailCodeList{{idx}}_itemCode" name="newinvCheckDetailCodeList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"    class="form-control "/>
					</td>


					<td>
						<input id="newinvCheckDetailCodeList{{idx}}_itemBarcode" name="newinvCheckDetailCodeList[{{idx}}].itemBarcode" type="text" value="{{row.itemBarcode}}"    class="form-control "/>
					</td>


					<td>
						<input id="newinvCheckDetailCodeList{{idx}}_supBarcode" name="newinvCheckDetailCodeList[{{idx}}].supBarcode" type="text" value="{{row.supBarcode}}"    class="form-control "/>
					</td>

					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#newinvCheckDetailCodeList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
					</script>
					<script type="text/javascript">
                        var newinvCheckDetailCodeRowIdx = 0, newinvCheckDetailCodeTpl = $("#newinvCheckDetailCodeTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                        $(document).ready(function() {
                            var data = ${fns:toJson(newinvCheckMain.newinvCheckDetailCodeList)};
                            for (var i=0; i<data.length; i++){
                                addRow('#newinvCheckDetailCodeList', newinvCheckDetailCodeRowIdx, newinvCheckDetailCodeTpl, data[i]);
                                newinvCheckDetailCodeRowIdx = newinvCheckDetailCodeRowIdx + 1;
                            }
                        });
					</script>
				</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('newinvcheckmain:newinvCheckMain:edit') || isAdd}">
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
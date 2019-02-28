<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>材料凭证人工制单</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
				    if(error == "false") {
                        jp.loading();
                        form.submit();
                    }else{
                    	error="false";
				        return false;
					}
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

		});

		function getItemByCode(id) {
			$.ajax({
				url: '${ctx}/realaccount/realAccount/getItemByCode',
				data: {
				    itemCode:$("#"+id+"_prod_idNames").val()
				},
				success: function (data) {
				    console.log(data);
					$("#"+id+"_prodUnit").val(data.unit);
					$("#"+id+"_prodSpec").val(data.specModel);
                }
			})
        }
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

        function balance(){
			var data = ${fns:toJson(realAccount.realAccountDetailList)};
			var bo = 0;
			var loan = 0;
			for (var i=0; i<= realAccountDetailRowIdx; i++){
				var blflag = $("#realAccountDetailList"+i+"_blflag").val();
				var sum = $("#realAccountDetailList"+i+"_itemSum").val();
				if(blflag == "B"){
					bo += 1*sum;
				}
				if(blflag == "L"){
					loan += 1*sum;
				}
			}
			if(bo != loan){
				jp.alert("借贷不平衡！");
				error="true";
			}else{
				if(bo == 0 || loan == 0){
	        		if(bo == 0){
	        			jp.alert("缺少借方,无法提交!");
						error="true";
	        		}
	        		if(loan == 0){
	        			jp.alert("缺少贷方,无法提交!");
						error="true";
	        		}
	        	}else{
	        		this.inputForm.submit();
					error="false";
	        	}
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
				<a class="panelButton" href="${ctx}/realaccount/realAccount?flag=${flag}"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="realAccount" action="${ctx}/realaccount/realAccount/save?flag=${flag}" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>凭证号：</label>
					<div class="col-sm-10">
						<form:input path="billNum" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>会计期间：</label>
					<div class="col-sm-10">
						<form:input path="periodId" htmlEscape="false" readonly="true"  class="form-control required"/>
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
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">凭证附表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#realAccountDetailList', realAccountDetailRowIdx, realAccountDetailTpl);realAccountDetailRowIdx = realAccountDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
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
						<input id="realAccountDetailList{{idx}}_itemName" name="realAccountDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}" readOnly="true"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="realAccountDetailList{{idx}}_itemSum" name="realAccountDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control required"/>
					</td>
					
					
					<td width="80px">
						<select id="realAccountDetailList{{idx}}_blflag" name="realAccountDetailList[{{idx}}].blflag" data-value="{{row.blflag}}" class="form-control m-b  required">
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
					
					<td>
						<input id="realAccountDetailList{{idx}}_prodName" name="realAccountDetailList[{{idx}}].prodName" type="text" value="{{row.prodName}}" onkeyup="getItemByCode('realAccountDetailList{{idx}}')"  readOnly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="realAccountDetailList{{idx}}_prodUnit" name="realAccountDetailList[{{idx}}].prodUnit" type="text" value="{{row.prodUnit}}" readOnly="true" class="form-control "/>
					</td>
					
					
					<td>
						<input id="realAccountDetailList{{idx}}_prodSpec" name="realAccountDetailList[{{idx}}].prodSpec" type="text" value="{{row.prodSpec}}"  readOnly="true"  class="form-control "/>
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
		</div>
		</div>
		<c:if test="${fns:hasPermission('realaccount:realAccount:edit') || isAdd}">
				<div class="col-lg-2"></div>
		        <div class="col-lg-3">
		             <div class="form-group text-center">
		                 <div>
		                     <button id="saveBtn" name="saveBtn" value="true" class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在保存...">保 存</button>
		                     <button  name="submitBtn" value="true" onclick = "balance()" class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
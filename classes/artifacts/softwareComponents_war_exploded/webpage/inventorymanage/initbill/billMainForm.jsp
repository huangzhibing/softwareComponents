<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>单据管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
	function iniItemNum(){
		$("[id$='serialNum']").each(function(index,element){
			$(element).val(index+1);
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
			//$(list+idx+"_serialNum").val(idx);
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
	<script type="text/javascript">
	
		var flag=1,rqty;//获得进去页面时的可用量
		$(document).ready(function(){
			if("${isAdd}"=="true"){
				flag=0;
			}else{
				
				//document.getElementById("iotype").disabled="true";
			}
		})
		

		
		
		function cal(id){
			var index=id.indexOf("_");
			var itemSum=id.substring(0,index)+"_itemSum";
			var itemqty=id.substring(0,index)+"_itemQty";
			var sum=document.getElementById(itemSum).value;
			var qty=document.getElementById(itemqty).value;
			if(sum!=null&&qty!=null&&qty!=0&&sum!=''&&qty!=''){
				var t=parseFloat(sum)/parseFloat(qty);
				console.log(t);
				document.getElementById(id.substring(0,index)+"_itemPrice").value=t;
			}
			
		}
		$(document).ready(function(){
			$('#wareId').on('change',function(){
				var index=jp.loading();
	        	$.ajax({
					url:"${ctx}/outboundinput/outboundInput/getEmp",
					data:{wareId:$('#wareId').val()},
					dataType:'json',
					success:function(re){console.log(re);
						$('#wareEmpId').val(re.id);
						$('#wareEmpNames').val(re.user.no);
						$('#wareEmpname').val(re.empName);
						$('#wareEmpdd').val(re.user.no);
						jp.close(index);
					}				
				});
	        	
	        });
		})
		  
   
</script>
</head>
<body >
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/initbill/initBill"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="billMain" action="${ctx}/initbill/initBill/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据号：</label>
					<div class="col-sm-10">
						<form:input readOnly="true" path="billNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>单据状态：</label>
					<div class="col-sm-10">
<%-- 						<form:select path="billFlag" class="form-control required" disabled="true">
 --%>							<input value="新增" readOnly="true"     class="form-control required"/>
								<form:input  style="display:none;" value="N" path="billFlag" htmlEscape="false"    class="form-control" />
						<%-- </form:select> --%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>出入库日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='billDate'>
			                    <input readOnly="true" type='text'  name="billDate" class="form-control required"  value="<fmt:formatDate value="${billMain.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>部门编码：</label>
					<div class="col-sm-10">
						<sys:treeselect-modify disabled="disabled" targetId="deptd" targetField="code" id="dept" name="dept.id" value="${billMain.dept.id}" labelName="dept.code" labelValue="${billMain.dept.code}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div style="display:none;">
				<form:input  id="code" path="dept.code" htmlEscape="false"    class="form-control required"/>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>部门名称：</label>
					<div class="col-sm-10">
						<form:input readonly="true" id="deptd" path="deptName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库房号：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify targetId="wared" targetField="wareName" 
						extraField="subFlag:subFlag;wareIDd:wareID"
						url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${billMain.ware.id}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
							 title="选择库房号" cssClass="form-control required" fieldLabels="库房号|库房名称" fieldKeys="wareID|wareName" searchLabels="库房号|库房名称" searchKeys="wareID|wareName" ></sys:gridselect-modify>
					</div>
				</div>
				<input style="display:none;" value="${billMain.ware.wareID }" id="wareIDd" name="ware.wareID"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库房名称：</label>
					<div class="col-sm-10">
						<form:input readonly="true" id="wared" path="wareName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<input id="subFlag" style="display:none;"/>
				 <%-- <div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>经办人编码：</label>
					<div class="col-sm-10">
						<sys:userselect-modify targetId="billPersond" id="billPerson" name="billPerson.id" value="${billMain.billPerson.id}" labelName="billPerson.no" labelValue="${billMain.billPerson.no}"
							    cssClass="form-control required"/>
						<form:input placeHolder="点击获取经办人" onclick="Test()" readOnly="true"  id="billPersonId"  value="${billMain.billPerson.no}" path="billPerson.no"
							    cssClass="form-control required"/>
					</div>
				</div> 
				<form:input path="billPerson.id" style="display:none;"  id="billPerson" name="billPerson.id" value="${billMain.billPerson.id}" labelName="billPerson.no" labelValue="${billMain.billPerson.no}"
							    cssClass="form-control required"/>
				 <div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>经办人名字：</label>
					<div class="col-sm-10">
						<form:input  readonly="true" id="billPersond" path="billPerson.name" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>  --%>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库管员代码：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify disabled="disabled" labelField="user.no" targetId="wareEmpname" targetField="empName" url="${ctx}/employee/employee/data" id="wareEmp" name="wareEmp.id" value="${billMain.wareEmp.id}" labelName="wareEmp.user.no" labelValue="${billMain.wareEmp.user.no}"
							 title="选择库管员代码" cssClass="form-control required" fieldLabels="库管员代码|库管员名称" fieldKeys="user.no|empName" searchLabels="库管员代码|库管员名称" searchKeys="user.no|empName" ></sys:gridselect-modify>
					</div>
				</div>
				<input id="wareEmpdd" name="wareEmp.user.no" value="${billMain.wareEmp.user.no }" style="display:none;"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库管员名称：</label>
					<div class="col-sm-10">
						<form:input id="wareEmpname" readonly="true" path="wareEmpname" htmlEscape="false"  value="${billMain.wareEmp.empName}"   class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人代码：</label>
					<div class="col-sm-10">
						<form:input id="billEmpNo" readOnly="true" path="billEmp.no" htmlEscape="false" value="${billMain.billEmp.no }"    class="form-control required"/>
					</div>
					<div class="col-sm-10" style="display:none;">
						<form:input id="billEmpId" readOnly="true" path="billEmp.id" htmlEscape="false" value="${billMain.billEmp.id }"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>制单人名称：</label>
					<div class="col-sm-10">
						<form:input id="billEmpname" readonly="true" path="billEmpname" htmlEscape="false"  value="${billMain.billEmpname}"   class="form-control required"/>
					</div>
				</div>
				
				
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>核算期间：</label>
					<div class="col-sm-10">
						<form:input id="periodId" readOnly="true" path="period.periodId" htmlEscape="false"    class="form-control required"/>
					</div>
					<input style="display:none;" readOnly="true" name="period.id" value="${billMain.period.periodId }"    class="form-control required"/>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input id="note"  path="note" htmlEscape="false"    class="form-control"/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">单据明细：</a>
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
						<th><font color="red">*</font>序号</th>
						<th style="width:180px;"><font color="red">*</font>物料代码</th>
						<th style="width:180px;"><font color="red">*</font>物料名称</th>
						<th>货区号</th>
						<th>货区名称</th>
						<th>货位号</th>
						<th>货位名称</th>
						
						<th><font color="red">*</font>物料规格</th>
						<th>批次号</th>
						
						<th>计量单位</th>
						<th><font color="red">*</font>实际单价</th>
						<th><font color="red">*</font>数量</th>
						
						<th><font color="red">*</font>实际金额</th>
						
						
						<th>计划单价</th>
						
						
						
						
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="billDetailList">
				</tbody>
			</table>
			</div>
			<script type="text/template" id="billDetailTpl">//<!--
				<tr id="billDetailList{{idx}}" >
					<td class="hide">
						<input id="billDetailList{{idx}}_id" name="billDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="billDetailList{{idx}}_delFlag" name="billDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input readOnly="true" id="billDetailList{{idx}}_serialNum" name="billDetailList[{{idx}}].serialNum" type="text" value="{{row.serialNum}}"    class="form-control required"/>
					</td>
					
					<td>
						<sys:gridselect-initbill
							targetId="" targetField="" 
							extraField="billDetailList{{idx}}_itemCoded:code;billDetailList{{idx}}_itemBatch:itemBatch;billDetailList{{idx}}_item:id;billDetailList{{idx}}_itemName:name;billDetailList{{idx}}_planPrice:planPrice;billDetailList{{idx}}_itemSpec:specModel;billDetailList{{idx}}_measUnit:unitCode.unitName" 
							url="${ctx}/item/item/data2" id="billDetailList{{idx}}_item" name="billDetailList{{idx}}.item.id" value="{{row.item.id}}" labelName="billDetailList{{idx}}.item.code" labelValue="{{row.item.id}}"
							 title="选择物料代码" cssClass="form-control required " fieldLabels="物料代码|物料名称|物料规格" fieldKeys="code|name|specModel" searchLabels="物料代码|物料名称" searchKeys="code|name" ></sys:gridselect-initbill>
					</td>
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_itemName" name="billDetailList[{{idx}}].itemName" type="text" value="{{row.itemName}}"    class="form-control "/>
					</td>
					<td style="display:none;">
					<input  id="billDetailList{{idx}}_itemCoded" name="billDetailList[{{idx}}].item.code" value="{{row.item.id}}"/>
					</td>
					<td style="display:none;">
						<input  readOnly="readOnly" id="billDetailList{{idx}}_accountId" name="billDetailList[{{idx}}].accountId" class="form-control  " value="{{row.accountId}}" />
					</td>
					<td>
						<sys:gridselect-modify url="${ctx}/bin/bin/data" urlParamName="wareId" urlParamId="wareNames" id="billDetailList{{idx}}_bin" name="billDetailList[{{idx}}].bin.id" value="{{row.bin.id}}" labelName="billDetailList{{idx}}.bin.binId" labelValue="{{row.bin.binId}}"
							 title="选择货区号" cssClass="form-control  " fieldLabels="货区编码|货区名称" fieldKeys="binId|binDesc" searchLabels="货区编码|货区名称" searchKeys="binId|binDesc"
							 targetId="billDetailList{{idx}}_binName" targetField="binDesc"></sys:gridselect-modify>
					</td>


					<td>
						<input id="billDetailList{{idx}}_binName" name="billDetailList[{{idx}}].binName" type="text" value="{{row.binName}}"  readOnly="true"  class="form-control "/>
					</td>


					<td>
						<sys:gridselect-modify url="${ctx}/location/location/data" urlParamName="binId" urlParamId="billDetailList{{idx}}_binNames" id="billDetailList{{idx}}_loc" name="billDetailList[{{idx}}].loc.id" value="{{row.loc.id}}" labelName="billDetailList{{idx}}.loc.locId" labelValue="{{row.loc.locId}}"
							 title="选择货位号" cssClass="form-control  " fieldLabels="货位编码|货位名称" fieldKeys="locId|locDesc" searchLabels="货位编码|货位名称" searchKeys="locId|locDesc"
							 targetId="billDetailList{{idx}}_locName" targetField="locDesc" ></sys:gridselect-modify>
					</td>


					<td>
						<input id="billDetailList{{idx}}_locName" name="billDetailList[{{idx}}].locName" type="text" value="{{row.locName}}"  readOnly="true"   class="form-control "/>
					</td>
					
					
					
					
					
					<td style="display:none;">
						<input readOnly="readOnly" id="billDetailList{{idx}}_item" name="billDetailList[{{idx}}].item.id" type="text" value="{{row.item.id}}"    class="form-control "/>
					</td>
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_itemSpec" name="billDetailList[{{idx}}].itemSpec" type="text" value="{{row.itemSpec}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_itemBatch" name="billDetailList[{{idx}}].itemBatch" type="text" value="{{row.itemBatch}}"    class="form-control "/>
					</td>
					
					
					
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_measUnit" name="billDetailList[{{idx}}].measUnit" type="text" value="{{row.measUnit}}"    class="form-control "/>
					</td>
					<td>
						<input readOnly="true" id="billDetailList{{idx}}_itemPrice" name="billDetailList[{{idx}}].itemPrice" type="text" value="{{row.itemPrice}}"    class="form-control required"/>
					</td>
					
					<td>
						<input onkeyup="cal(this.id)"  id="billDetailList{{idx}}_itemQty" name="billDetailList[{{idx}}].itemQty" type="text" value="{{row.itemQty}}"    class="form-control required"/>
					</td>
					
					<td>
						<input onkeyup="cal(this.id)" id="billDetailList{{idx}}_itemSum" name="billDetailList[{{idx}}].itemSum" type="text" value="{{row.itemSum}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input readOnly="readOnly" id="billDetailList{{idx}}_planPrice" name="billDetailList[{{idx}}].planPrice" type="text" value="{{row.planPrice}}"    class="form-control "/>
					</td>
					
					
					<td class="text-center" width="10">
						{{#delBtn}}<span  class="close" onclick="delRow(this, '#billDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
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
		<c:if test="${fns:hasPermission('billmain:billMain:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button id="submitBtn" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
<script type="text/javascript">
function Test(){
	top.layer.open({
	    type: 2, 
	    area: ['900px', '560px'],
	    title:"选择用户",
	    auto:true,
	    maxmin: true, //开启最大化最小化按钮
	    content: ctx+"/sys/user/userSelect?isMultiSelect="+false,
	    btn: ['确定', '关闭'],
	    yes: function(index, layero){
	    	var ids = layero.find("iframe")[0].contentWindow.getIdSelections();
	    	var names = layero.find("iframe")[0].contentWindow.getNameSelections();
	    	var loginNames = layero.find("iframe")[0].contentWindow.getLoginNameSelections();
	    	var nos = layero.find("iframe")[0].contentWindow.getNoSelections();
	        console.log(nos)
	    	if(ids.length ==0){
	    		
				jp.warning("请选择至少一个用户!");
				return;
			}
	        console.log(ids+" "+nos);
	    	// 执行保存
	    	$("#billPerson").val(ids.join(",").replace(/u_/ig,""));
	    	$("#billPersond").val(names.join(","));
	    	$("#billPersonId").val(nos.join(","));
	    	//yesFuc(ids.join(","), names.join(","),nos.join(","),loginNames.join(","));
	    	
	    	top.layer.close(index);
		  },
		  cancel: function(index){ 
			  //取消默认为空，如需要请自行扩展。
			  top.layer.close(index);
	        }
	}); 
}

</script>
</body>
</html>
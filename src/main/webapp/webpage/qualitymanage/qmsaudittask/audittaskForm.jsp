<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>稽核任务管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					//jp.loading();
					//form.submit();
					var index=jp.loading();
                    var isAdd='${isAdd}';
                    $.ajax({
                        url:'${ctx}/common/chkCode',
                        data:{
                            tableName:"qms_audittask",
                            fieldName:"auditt_id",
                            fieldValue:$('#audittId').val(),
                        },
                        success:function(res){
                            if(isAdd==''){
                                form.submit();
                            }else{
                                if(res=='true'){
                                    form.submit();
                                }else{
                                    jp.warning("稽核任务编号已存在!");
                                    return false;
                                }
                            }
                        }
                    });
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
			
	        $('#auditDate').datetimepicker({
				 format: "YYYY-MM-DD"
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
					 format: "YYYY-MM-DD" , 
					 widgetPositioning:{vertical :"bottom"}
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
		
		function setAuditTaskItem(code,row){
			$.ajax({
				url:"${ctx}/qmsaudittask/audittask/getAuditStd?code="+code,
				type: "GET",
				cache:false,
				dataType: "json",
				success:function(data){
					var dataItem=data.rows;
					if(dataItem.length>0){
						$(row+"_auditstdName").val(dataItem[0].auditpCode);
						$(row+"_auditstdId").val(dataItem[0].id);
						$(row+"_auditpName").val(dataItem[0].auditpName);
						$(row+"_auditItemCode").val(dataItem[0].auditItemCode);
						$(row+"_auditItemName").val(dataItem[0].auditItemName);
						$(row+"_auditDeptCode").val(dataItem[0].office.code);
						$(row+"_auditDeptName").val(dataItem[0].auditDeptName);
					for(var i=1;i<data.total;i++){
						addRow('#auditTaskItemList', auditTaskItemRowIdx, auditTaskItemTpl);
						$('#auditTaskItemList'+auditTaskItemRowIdx+"_auditstdName").val(dataItem[i].auditpCode);
						$('#auditTaskItemList'+auditTaskItemRowIdx+"_auditstdId").val(dataItem[i].id);
						$('#auditTaskItemList'+auditTaskItemRowIdx+"_auditpName").val(dataItem[i].auditpName);
						$('#auditTaskItemList'+auditTaskItemRowIdx+"_auditItemCode").val(dataItem[i].auditItemCode);
						$('#auditTaskItemList'+auditTaskItemRowIdx+"_auditItemName").val(dataItem[i].auditItemName);
						$('#auditTaskItemList'+auditTaskItemRowIdx+"_auditDeptCode").val(dataItem[i].office.code);
						$('#auditTaskItemList'+auditTaskItemRowIdx+"_auditDeptName").val(dataItem[i].auditDeptName);
						auditTaskItemRowIdx=auditTaskItemRowIdx+1;
					}
				  }		
				}
		});
		}
		
		//依据采购人员的编码设置采购的用户名值
		function setUsername(idx,value){
			var id=idx.split("_")[0];
			var obj=idx.split("_")[1];
			if(obj=='auditer'){
				var ids="#"+id+"_auditGmName";
				$(ids).val(value);
			}else if(obj=="tracker"){
				var ids="#"+id+"_trackpName";
				$(ids).val(value);
			}else{
				$("#auditGlName").val(value);
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
				<a class="panelButton" href="${ctx}/qmsaudittask/audittask"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="audittask" action="${ctx}/qmsaudittask/audittask/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				
				<div class="form-group">
				   <c:if test="${isAdd=='true'}">
						<label class="col-sm-2 control-label"><font color="red">*</font>稽核任务编号：</label>
						<div class="col-sm-3">
							<form:input path="audittId" htmlEscape="false"    class="form-control required"/>
						</div>
					</c:if>
					<c:if test="${isAdd!='true'}">
						<label class="col-sm-2 control-label"><font color="red">*</font>稽核任务编号：</label>
						<div class="col-sm-3">
							<form:input path="audittId" htmlEscape="false"  readonly="true"  class="form-control required"/>
						</div>
					</c:if>
					<label class="col-sm-2 control-label"><font color="red">*</font>稽核任务名称：</label>
					<div class="col-sm-3">
						<form:input path="audittName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>审核组长编号：</label>
					<div class="col-sm-3">
					     <sys:userselect-update id="auditGrouper" name="auditGrouper.id" value="${audittask.auditGrouper.id}" labelName="auditGrouper.no" labelValue="${audittask.auditGrouper.no}"
							    cssClass="form-control required"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>审核组长名称：</label>
					<div class="col-sm-3">
						<form:input path="auditGlName" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>审核完成时间：</label>
					<div class="col-sm-3">
							<div class='input-group form_datetime' id='auditDate'>
			                    <input type='text'  name="auditDate" class="form-control required"  value="<fmt:formatDate value="${audittask.auditDate}" pattern="yyyy-MM-dd"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>审核目的：</label>
					<div class="col-sm-3">
						<form:input path="auditGoal" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>审核范围：</label>
					<div class="col-sm-3">
						<form:input path="auditScope" htmlEscape="false"    class="form-control required"/>
					</div>
					<label class="col-sm-2 control-label"><font color="red">*</font>审核结果综述：</label>
					<div class="col-sm-3">
						<form:input path="auditResult" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">稽核任务项：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#auditTaskItemList', auditTaskItemRowIdx, auditTaskItemTpl);auditTaskItemRowIdx = auditTaskItemRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
		<div class="table-responsive" style="min-height: 350px">
			<table class="table table-striped table-bordered table-condensed" style="min-width: 2800px">
				<thead>
					<tr>
						<th class="hide"></th>
					<%--	<th><font color="red">*</font>稽核任务名称</th>   --%>
						<th width="150px"><font color="red">*</font>稽核流程ID</th>
						<th width="150px"><font color="red">*</font>稽核流程名称</th>
						<th width="150px"><font color="red">*</font>稽核标准要素编码</th>
						<th width="150px"><font color="red">*</font>稽核标准要素名称</th>
						<th width="150px"><font color="red">*</font>稽核部门编号</th>
						<th width="150px"><font color="red">*</font>稽核部门名称</th>
						<th width="150px"><font color="red">*</font>内审员编号</th>
						<th width="150px"><font color="red">*</font>内审员名称</th>
						<th width="150px"><font color="red">*</font>审核完成时间</th>
						<th width="120px"><font color="red">*</font>是否符合</th>
						<th width="150px"><font color="red">*</font>追踪人编号</th>
						<th width="150px"><font color="red">*</font>追踪人名称</th>
						<th width="120px"><font color="red">*</font>不符合项是否已纠正</th>
						<th width="200px">不符合项说明</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="auditTaskItemList">
				</tbody>
			</table>
		</div>
			<script type="text/template" id="auditTaskItemTpl">//<!--
				<tr id="auditTaskItemList{{idx}}">
					<td class="hide">
						<input id="auditTaskItemList{{idx}}_id" name="auditTaskItemList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="auditTaskItemList{{idx}}_delFlag" name="auditTaskItemList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
				
					<td>
						<sys:gridselect-auditTask url="${ctx}/qmsauditstd/auditstd/myData" id="auditTaskItemList{{idx}}_auditstd" name="auditTaskItemList[{{idx}}].auditstd.id" value="{{row.auditstd.id}}" labelName="auditTaskItemList{{idx}}.auditstd.auditpCode" labelValue="{{row.auditstd.auditpCode}}"
							 title="选择稽核流程ID" cssClass="form-control  required" fieldLabels="稽核流程ID|稽核流程名称" fieldKeys="auditpCode|auditpName" searchLabels="稽核流程ID|稽核流程名称" searchKeys="auditpCode|auditpName" ></sys:gridselect-auditTask>
					</td>
					
					
					<td>
						<input id="auditTaskItemList{{idx}}_auditpName" name="auditTaskItemList[{{idx}}].auditpName" type="text" value="{{row.auditpName}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="auditTaskItemList{{idx}}_auditItemCode" name="auditTaskItemList[{{idx}}].auditItemCode" type="text" value="{{row.auditItemCode}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="auditTaskItemList{{idx}}_auditItemName" name="auditTaskItemList[{{idx}}].auditItemName" type="text" value="{{row.auditItemName}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="auditTaskItemList{{idx}}_auditDeptCode" name="auditTaskItemList[{{idx}}].auditDeptCode" type="text" value="{{row.auditDeptCode}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="auditTaskItemList{{idx}}_auditDeptName" name="auditTaskItemList[{{idx}}].auditDeptName" type="text" value="{{row.auditDeptName}}"  readonly="true"   class="form-control required"/>
					</td>
					
					
					<td  class="max-width-250">
						<sys:userselect-update id="auditTaskItemList{{idx}}_auditer" name="auditTaskItemList[{{idx}}].auditer.id" value="{{row.auditer.id}}" labelName="auditTaskItemList{{idx}}.auditer.no" labelValue="{{row.auditer.no}}"
							    cssClass="form-control required"/>
					</td>
					
					
					<td>
						<input id="auditTaskItemList{{idx}}_auditGmName" name="auditTaskItemList[{{idx}}].auditGmName" type="text" value="{{row.auditGmName}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<div class='input-group form_datetime' id="auditTaskItemList{{idx}}_auditDate">
		                    <input type='text'  name="auditTaskItemList[{{idx}}].auditDate" class="form-control required"  value="{{row.auditDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>
					
					
					<td>
						<select id="auditTaskItemList{{idx}}_isMeet" name="auditTaskItemList[{{idx}}].isMeet" data-value="{{row.isMeet}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('auditCriterion')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					<td  class="max-width-250">
						<sys:userselect-update id="auditTaskItemList{{idx}}_tracker" name="auditTaskItemList[{{idx}}].tracker.id" value="{{row.tracker.id}}" labelName="auditTaskItemList{{idx}}.tracker.no" labelValue="{{row.tracker.no}}"
							    cssClass="form-control required"/>
					</td>
					
					
					<td>
						<input id="auditTaskItemList{{idx}}_trackpName" name="auditTaskItemList[{{idx}}].trackpName" type="text" value="{{row.trackpName}}" readonly="true"   class="form-control required"/>
					</td>

					<td>
						<select id="auditTaskItemList{{idx}}_isCorrect" name="auditTaskItemList[{{idx}}].isCorrect" data-value="{{row.isCorrect}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('auditCriterion')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
				    <td>
						<input id="auditTaskItemList{{idx}}_remark" name="auditTaskItemList[{{idx}}].remark" type="text" value="{{row.remark}}"    class="form-control "/>
					</td>
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#auditTaskItemList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var auditTaskItemRowIdx = 0, auditTaskItemTpl = $("#auditTaskItemTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(audittask.auditTaskItemList)};
					for (var i=0; i<data.length; i++){
						addRow('#auditTaskItemList', auditTaskItemRowIdx, auditTaskItemTpl, data[i]);
						auditTaskItemRowIdx = auditTaskItemRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('qmsaudittask:audittask:edit') || isAdd}">
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
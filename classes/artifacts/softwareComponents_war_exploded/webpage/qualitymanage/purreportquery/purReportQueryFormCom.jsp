<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单查询管理</title>
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
			
	        $('#checkDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#justifyDate').datetimepicker({
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
				<a class="panelButton" href="${ctx}/purreportquery/purReportQuery"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="purReportQuery" action="${ctx}/purreportquery/purReportQuery/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验单编号：</label>
					<div class="col-sm-10">
						<form:input path="reportId" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>完工入库单编号：</label>
					<div class="col-sm-10">
					<form:input path="sfcInvCheckMain.billNo" htmlEscape="false"  readonly="true"   class="form-control required"/>
					<%-- 
						<sys:gridselect url="${ctx}/purinvcheckmain/invCheckMain/data" id="invCheckMain" name="invCheckMain.id" value="${purReportQuery.invCheckMain.id}" labelName="invCheckMain.billnum" labelValue="${purReportQuery.invCheckMain.billnum}"
							 title="选择采购报检单编号" cssClass="form-control required" fieldLabels="采购报检单编号" fieldKeys="billnum" searchLabels="采购报检单编号" searchKeys="billnum" ></sys:gridselect>
					--%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验类型名称：</label>
					<div class="col-sm-10">
						<form:input path="checktName" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">全检/抽检：</label>
					<div class="col-sm-10">
					<form:input path="rndFul" htmlEscape="false"  readonly="true"   class="form-control required"/>
					<%-- 
						<form:select path="rndFul" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('checkType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
						--%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">执行标准：</label>
					<div class="col-sm-10">
						<form:input path="qualityNorm" htmlEscape="false"    readonly="true" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验部门名称：</label>
					<div class="col-sm-10">
					<form:input path="office.name" htmlEscape="false"  readonly="true"   class="form-control required"/>
					<%-- 
						<sys:treeselect id="office" name="office.id" value="${purReportQuery.office.id}" labelName="office.name" labelValue="${purReportQuery.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					--%>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">检验岗位编码：</label>
					<div class="col-sm-10">
						<form:input path="cpositionCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">检验岗位名称：</label>
					<div class="col-sm-10">
						<form:input path="cpositionName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验日期：</label>
					<div class="col-sm-10">
					<input type='text' id='checkDate1' name="checkDate" class="form-control " readonly="true" value="${fns:getMyDate()}"/>
			              
					<%-- 
						<p class="input-group">
							<div class='input-group form_datetime' id='checkDate'>
			                    <input type='text'  name="checkDate" class="form-control "  value="<fmt:formatDate value="${purReportQuery.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
			            --%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验时间：</label>
					<div class="col-sm-10">
						<form:input path="checkTime" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验人代码：</label>
					<div class="col-sm-10">
						<form:input path="cpersonCode" htmlEscape="false"  readonly="true"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验人名称：</label>
					<div class="col-sm-10">
						<form:input path="checkPerson" htmlEscape="false" readonly="true"   class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">检验结论：</label>
					<div class="col-sm-10">
						<form:input path="checkResult" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核部门编码：</label>
					<div class="col-sm-10">
						<form:input path="jdeptCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核部门名称：</label>
					<div class="col-sm-10">
						<sys:treeselect id="deptOffice" name="deptOffice.id" value="${purReportQuery.deptOffice.id}" labelName="deptOffice.name" labelValue="${purReportQuery.deptOffice.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核岗位编码：</label>
					<div class="col-sm-10">
						<form:input path="jpositionCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核岗位名称：</label>
					<div class="col-sm-10">
						<form:input path="jpositionName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='justifyDate'>
			                    <input type='text'  name="justifyDate" class="form-control "  value="<fmt:formatDate value="${purReportQuery.justifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核人代码：</label>
					<div class="col-sm-10">
						<form:input path="jpersonCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核人：</label>
					<div class="col-sm-10">
						<form:input path="justifyPerson" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核结论：</label>
					<div class="col-sm-10">
						<form:input path="justifyResult" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input path="memo" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group" >
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-10">
					<%-- 
						<form:select path="state" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
						--%>
						<form:input path="state" htmlEscape="false"  readonly="true"  class="form-control "/>
						
					</div>
				</div>
				<div class="form-group" style="display:none">
					<label class="col-sm-2 control-label">审核不通过意见 ：</label>
					<div class="col-sm-10">
						<form:input path="justifyRemark" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">检验物品清单：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验对象序列号</th>
						<th>检验对象编码</th>
						<th>检验对象名称</th>
						<th>检验结论</th>
						<%-- <th>问题类型编码</th> --%>
						<%--<th>问题类型名称</th>--%>
						<th>检验日期</th>
						<th>检验时间</th>
						<th>检验人名称</th>
						<th>备注</th>
						<th>检验标准名称</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<%--
				<sys:gridselect url="${ctx}/ObjectDef/objectDef/data" id="purReportRSnQueryList{{idx}}_objectDef" name="purReportRSnQueryList[{{idx}}].objectDef.id" value="{{row.objectDef.id}}" labelName="purReportRSnQueryList{{idx}}.objectDef.objCode" labelValue="{{row.objectDef.objCode}}"
							 title="选择检验对象编码" cssClass="form-control  " fieldLabels="检验对象编码" fieldKeys="objCode" searchLabels="检验对象编码" searchKeys="objCode" ></sys:gridselect>
				
				
						<select id="purReportRSnQueryList{{idx}}_checkResult" name="purReportRSnQueryList[{{idx}}].checkResult" data-value="{{row.checkResult}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('isQualified')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
						
						<sys:gridselect url="${ctx}/mattertype/matterType/data" id="purReportRSnQueryList{{idx}}_matterType" name="purReportRSnQueryList[{{idx}}].matterType.id" value="{{row.matterType.id}}" labelName="purReportRSnQueryList{{idx}}.matterType.mattername" labelValue="{{row.matterType.mattername}}"
							 title="选择问题类型名称" cssClass="form-control  " fieldLabels="问题类型编码|问题类型名称" fieldKeys="mattercode|mattername" searchLabels="问题类型编码|问题类型名称" searchKeys="mattercode|mattername" ></sys:gridselect>
					
					<div class='input-group form_datetime' id="purReportRSnQueryList{{idx}}_checkDate">
		                    <input type='text'  name="purReportRSnQueryList[{{idx}}].checkDate" class="form-control "  value="{{row.checkDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>	
					
				<sys:gridselect url="${ctx}/qcnorm/qCNorm/data" id="purReportRSnQueryList{{idx}}_qCNorm" name="purReportRSnQueryList[{{idx}}].qCNorm.id" value="{{row.qCNorm.id}}" labelName="purReportRSnQueryList{{idx}}.qCNorm.qcnormname" labelValue="{{row.qCNorm.qcnormname}}"
							 title="选择检验标准名称" cssClass="form-control  " fieldLabels="检验标准名称" fieldKeys="qcnormname" searchLabels="检验标准名称" searchKeys="qcnormname" ></sys:gridselect>
					
				
				
				
				 --%>
				<tbody id="purReportRSnQueryList">
				</tbody>
			</table>
			<script type="text/template" id="purReportRSnQueryTpl">//<!--
				<tr id="purReportRSnQueryList{{idx}}">
					<td class="hide">
						<input id="purReportRSnQueryList{{idx}}_id" name="purReportRSnQueryList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purReportRSnQueryList{{idx}}_delFlag" name="purReportRSnQueryList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="purReportRSnQueryList{{idx}}_objSn" name="purReportRSnQueryList[{{idx}}].objSn" readonly="true" type="text" value="{{row.objSn}}"    class="form-control "/>
					</td>
					
					
					<td>

							<input id="purReportRSnQueryList{{idx}}_objCode" name="purReportRSnQueryList{{idx}}.objectDef.objCode" readonly="true" type="text" value="{{row.objectDef.id}}"    class="form-control "/>
					
					</td>
					
					
					<td>
						<input id="purReportRSnQueryList{{idx}}_objName" name="purReportRSnQueryList[{{idx}}].objName" type="text" value="{{row.objName}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>

						<input id="purReportRSnList{{idx}}_checkResult" name="purReportRSnList[{{idx}}].checkResult" readonly="true" type="text" value="{{row.checkResult}}"    class="form-control "/>
					
					</td>
					
					
					<td style="display:none">
						<input id="purReportRSnQueryList{{idx}}_matterCode" name="purReportRSnQueryList[{{idx}}].matterCode" readonly="true" type="text" value="{{row.matterCode}}"    class="form-control "/>
					</td>
					
					
					<td style="display:none">

						<input id="purReportRSnList{{idx}}_matterName" name="purReportRSnList[{{idx}}].matterType.id" value="{{row.matterType.mattername}}" readonly="true" type="text" value="{{row.matterName}}"    class="form-control "/>
					
						</td>
					
					
					<td>

						<input id="purReportRSnList{{idx}}_checkDate" name="purReportRSnList[{{idx}}].checkDate" readonly="true" type="text" value="{{row.checkDate}}"    class="form-control "/>
					
											            
					</td>
					
					
					<td>
						<input id="purReportRSnQueryList{{idx}}_checkTime" name="purReportRSnQueryList[{{idx}}].checkTime" readonly="true" type="text" value="{{row.checkTime}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportRSnQueryList{{idx}}_checkPerson" name="purReportRSnQueryList[{{idx}}].checkPerson" readonly="true" type="text" value="{{row.checkPerson}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="purReportRSnQueryList{{idx}}_memo" name="purReportRSnQueryList[{{idx}}].memo" readonly="true" type="text" value="{{row.memo}}"    class="form-control "/>
					</td>
					
					
					<td>
						
					<input id="purReportRSnList{{idx}}_qCNorm" name="purReportRSnList[{{idx}}].qCNorm.qcnormName" readonly="true" type="text" value="{{row.qcnorm.qcnormName}}"    class="form-control "/>
					

					</td>
					
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purReportRSnQueryRowIdx = 0, purReportRSnQueryTpl = $("#purReportRSnQueryTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(purReportQuery.purReportRSnQueryList)};
					for (var i=0; i<data.length; i++){
						console.log(data[i]);
						addRow('#purReportRSnQueryList', purReportRSnQueryRowIdx, purReportRSnQueryTpl, data[i]);
						purReportRSnQueryRowIdx = purReportRSnQueryRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('purreportquery:purReportQuery:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div><%-- 
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">关闭</button>
		                 
		                 --%>
		                 <a href= "${ctx}/purreportquery/purReportQuery" class="btn btn-primary btn-block btn-lg btn-parsley">关闭</a>
		                 
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
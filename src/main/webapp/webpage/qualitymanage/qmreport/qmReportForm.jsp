<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>质量问题管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
				form.submit();
				
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					jp.close();
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
	        $('#qmDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
		
		function button_save(){
			$('#state').val('编辑中');
			jp.loading("正在保存...");
			form=$('#inputForm');
			jp.post("${ctx}/common/chkCode",{tableName:"qms_qmreport",fieldName:"qmreport_id",fieldValue:$('#qmreportId').val()},function(data){if("${isAdd}"==""){form.submit();jp.close();}else{if(data=='true'){form.submit();}else{jp.warning("问题处理报告单编号已存在！");jp.close();return false;}}});
		}
		
		function button_submit(){
			$('#state').val('已提交');
			jp.loading("正在提交...");
			form=$('#inputForm');
			jp.post("${ctx}/common/chkCode",{tableName:"qms_qmreport",fieldName:"qmreport_id",fieldValue:$('#qmreportId').val()},function(data){if("${isAdd}"==""){form.submit();jp.close();}else{if(data=='true'){form.submit();}else{jp.warning("问题处理报告单编号已存在！");jp.close();return false;}}});
		}
		
		function openImportDialog(){
		    /*if($("#rndFul").val()=="全检")
				jp.openDialog('检验单列表-全检', '${ctx}/qmreport/qmReport/purlist','1200px', '800px', qmwindow);
		    else if($("#rndFul").val()=="抽检")
		        jp.openDialog('检验单列表-抽检', '${ctx}/qmreport/qmReport/purlist','1200px', '800px', qmwindow);*/
            jp.openDialog('检验单列表', '${ctx}/qmreport/qmReport/purlist','1200px', '800px', qmwindow);
		}
		function importRow(row){
			addRow('#qmReportRSnList', qmReportRSnRowIdx, qmReportRSnTpl, row);
			qmReportRSnRowIdx = qmReportRSnRowIdx + 1;
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

		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var purId=$(prefix+"_purReportRSnid").val();
			
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){                       //当检验对象为此次新增时
				for(var i=0;i<qmReportRSnList.length;i++){
					if(purId==qmReportRSnList[i].purReportRSnId){
						qmReportRSnList.splice(i,1);
						break;
					}
				}
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
				<a class="panelButton" href="${ctx}/qmreport/qmReport"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<%--@elvariable id="qmReport" type="QmReport"--%>
		<form:form id="inputForm" modelAttribute="qmReport" action="${ctx}/qmreport/qmReport/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="userDeptCode" value="${fns:getUserOfficeCode()}"/>
		<sys:message content="${message}"/>	
				<div class=hide>
						<form:input id="state" path="state" htmlEscape="false"  />
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>问题处理报告单编号：</label>
					<div class="col-sm-3">
						<form:input path="qmreportId" htmlEscape="false" readonly="true"  class="form-control required"/>
					</div>



					<label class="col-sm-2 control-label"><font color="red">*</font>问题处理人：</label>
					<div class="col-sm-3">
						<sys:userselect id="qmPerson" name="qmPerson" value="${qmReport.qmPerson.id}" labelName="qmPersonName" labelValue="${qmReport.qmPerson.name}" disabled="disabled"
										cssClass="form-control required"/>
					</div>

				</div>

				
				<div class="form-group">

					<label class="col-sm-2 control-label"><font color="red">*</font>问题处理意见：</label>
					<div class="col-sm-3">
						<sys:gridselect url="${ctx}/matterhandling/matterHandling/data" id="mhandlingName" name="mhandlingName.id" value="${qmReport.mhandlingName.id}" labelName="mhandlingName.mhandlingname" labelValue="${qmReport.mhandlingName.mhandlingname}"
										title="选择问题处理意见" cssClass="form-control required" fieldLabels="处理意见|意见描述" fieldKeys="mhandlingname|mhandlingdes" searchLabels="处理意见|意见描述" searchKeys="mhandlingname|mhandlingdes" ></sys:gridselect>
					</div>


					<label class="col-sm-2 control-label">处理日期：</label>
					<div class="col-sm-3">
						<div class='input-group form_datetime' id='qmDate'>
							<input type='text'  name="qmDate" class="form-control"  readonly="true" value="<fmt:formatDate value="${qmReport.qmDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
						</div>

					</div>

				</div>

			<%--	<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验类型：</label>
				 	<div class="col-sm-3">
					<form:select path="rndFul" class="form-control ">
						<form:options items="${fns:getDictList('checkType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
					</div>
				</div>--%>


		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">不合格的检验对象：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="openImportDialog()" title="新增"><i class="fa fa-plus"></i> 新增</a>
		<div class="table-responsive">
			<table class="table table-striped table-bordered table-condensed" style="min-width: 150%">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验单编号</th>
						<th>采购报检单编号</th>
						<th>检验对象序列号</th>
						<th>检验对象编码</th>
						<th>检验对象名称</th>
						<th>问题类型</th>
						<th>检验结论</th>
						<th>检验日期</th>
						<th>检验时间</th>
						<th>检验人名称</th>
						<th>备注</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="qmReportRSnList">
				</tbody>
			</table>
		</div>
			<script type="text/template" id="qmReportRSnTpl">//<!--
				<tr id="qmReportRSnList{{idx}}">
					<td class="hide">
						<input id="qmReportRSnList{{idx}}_id" name="qmReportRSnList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="qmReportRSnList{{idx}}_delFlag" name="qmReportRSnList[{{idx}}].delFlag" type="hidden" value="0"/>
						
						<input id="qmReportRSnList{{idx}}_purReportRSnid" name="qmReportRSnList[{{idx}}].purReportRSnId" type="hidden" value="{{row.purReportRSnId}}">@author xianbang</input>

					</td>
					
					<td>
						<input id="qmReportRSnList{{idx}}_reportId" name="qmReportRSnList[{{idx}}].reportId" type="text" value="{{row.reportId}}"  readonly='true' style='width: 150px;'  class="form-control "/>
					</td>
					
					
					<td>
						<input id="qmReportRSnList{{idx}}_billNum" name="qmReportRSnList[{{idx}}].billNum" type="text" value="{{row.billNum}}"  readonly='true' style='width: 150px;'  class="form-control "/>
					</td>
					
					
					<td>
						<input id="qmReportRSnList{{idx}}_objSn" name="qmReportRSnList[{{idx}}].objSn" type="text" value="{{row.objSn}}"  readonly='true'  class="form-control "/>
					</td>
					
					
					<td>
						<input id="qmReportRSnList{{idx}}_objCode" name="qmReportRSnList[{{idx}}].objCode" type="text" value="{{row.objCode}}"  readonly='true'  class="form-control "/>
					</td>
					
					
					<td>
						<input id="qmReportRSnList{{idx}}_objName" name="qmReportRSnList[{{idx}}].objName" type="text" value="{{row.objName}}"  readonly='true'  class="form-control "/>
					</td>
					

					<td>
						<input id="qmReportRSnList{{idx}}_matterName" name="qmReportRSnList[{{idx}}].matterName" type="text" value="{{row.matterName}}" readonly='true'   class="form-control "/>
					</td>

					
					<td>
						<input id="qmReportRSnList{{idx}}_checkResult" name="qmReportRSnList[{{idx}}].checkResult" type="text" value="{{row.checkResult}}"  readonly='true'  class="form-control "/>
					</td>
					
					
					<td>
		                <input type='text'  name="qmReportRSnList[{{idx}}].checkDate" class="form-control " readonly='true' value="{{row.checkDate}}"/>
		              					            
					</td>
					
					
					<td>
						<input id="qmReportRSnList{{idx}}_checkTime" name="qmReportRSnList[{{idx}}].checkTime" type="text" value="{{row.checkTime}}"  readonly='true'  class="form-control "/>
					</td>
					
					
					<td>
						<input id="qmReportRSnList{{idx}}_checkPerson" name="qmReportRSnList[{{idx}}].checkPerson" type="text" value="{{row.checkPerson}}"  readonly='true'  class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="qmReportRSnList{{idx}}_memo" name="qmReportRSnList[{{idx}}].memo" type="text" value="{{row.memo}}"  readonly='true'  class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#qmReportRSnList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var qmReportRSnRowIdx = 0, qmReportRSnTpl = $("#qmReportRSnTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				var qmReportRSnList;
				var qmwindow=this;
				$(document).ready(function() {
					//if(${isAdd}){qmReport.qmreportId="${qmreportId}";}
					
					var data =qmReportRSnList= ${fns:toJson(qmReport.qmReportRSnList)};
					for (var i=0; i<data.length; i++){
						addRow('#qmReportRSnList', qmReportRSnRowIdx, qmReportRSnTpl, data[i]);
						qmReportRSnRowIdx = qmReportRSnRowIdx + 1;
					}
					
					
				});
			</script>
			</div>
		</div>
		</div>
	<div class="control-group" style="margin-top: 26px">
		<c:choose>
		<c:when test="${isEdit || isAdd}">
				<div class="col-lg-2"></div>
				<div class="col-lg-3">
		             <div class="form-group text-center">
		                 <div>
		                     <input type="button" id="save_button" onclick="button_save()" class="btn btn-primary btn-block btn-lg btn-parsley" value="保存" />
		                 </div>
		             </div>
		        </div>
		        <div class="col-lg-2"></div>
		        <div class="col-lg-3">
		             <div class="form-group text-center">
		                 <div>
		                     <input type="button" id="submit_button" onclick="button_submit()" class="btn btn-primary btn-block btn-lg btn-parsley" value="提交" />
		                 </div>
		             </div>
		        </div>
		</c:when>
		<c:otherwise>
		<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/qmreport/qmReport" data-loading-text="正在返回...">返回</a>
		                 </div>
		             </div>
		        </div>
		</c:otherwise>
		</c:choose>
	</div>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
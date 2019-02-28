<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>质量问题处理结果管理</title>
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
			
	        $('#qmDate').datetimepicker({
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
					 format: "YYYY-MM-DD"
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
				<a class="panelButton" href="${ctx}/qmreportquery/qmReportDeal"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<%--@elvariable id="qmReportQuery" type="QmReportQuery"--%>
		<form:form id="inputForm" modelAttribute="qmReportQuery" action="${ctx}/qmreportquery/qmReportDeal/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>问题处理报告单编号：</label>
					<div class="col-sm-10">
						<form:input path="qmreportId" htmlEscape="false"  readonly="true"   class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>问题处理人：</label>
					<div class="col-sm-10">
						<form:input path="qmPerson.name" htmlEscape="false"  readonly="true"   class="form-control required"/>
						
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>问题处理日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='qmDate'>
			                    <input type='text'  name="qmDate" class="form-control required" readonly="true" value="<fmt:formatDate value="${qmReportQuery.qmDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>问题处理意见：</label>
					<div class="col-sm-10">
					<form:input path="mhandlingName.mhandlingname" htmlEscape="false"  readonly="true"   class="form-control required"/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">不合格的检验对象：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<%--<a class="btn btn-white btn-sm" onclick="addRow('#qmReportRSnQueryList', qmReportRSnQueryRowIdx, qmReportRSnQueryTpl);qmReportRSnQueryRowIdx = qmReportRSnQueryRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
			<div class="table-responsive">
			  <table class="table table-striped table-bordered table-condensed" style="min-width:150%" >
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验单编号</th>
						<c:if test="${qmReportQuery.qmType=='采购'}">
						<th>采购报检单编号</th>
						</c:if>
						<th>检验对象序列号</th>
						<th>检验对象编码</th>
						<th>检验对象名称</th>
						<th>检验结论</th>
						<th>检验日期</th>
						<th>检验时间</th>
						<th>检验人名称</th>
						<th>问题类型名称</th>
						<th>备注</th>
						<th><font color="red">*</font>是否已处理</th>
						<th><font color="red">*</font>处理结果</th>
					</tr>
				</thead>
				<tbody id="qmReportRSnQueryList">
				</tbody>
			  </table>
			</div>
			<script type="text/template" id="qmReportRSnQueryTpl">//<!--
				<tr id="qmReportRSnQueryList{{idx}}">
					<td class="hide">
						<input id="qmReportRSnQueryList{{idx}}_id" name="qmReportRSnQueryList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="qmReportRSnQueryList{{idx}}_delFlag" name="qmReportRSnQueryList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="qmReportRSnQueryList{{idx}}_reportId" name="qmReportRSnQueryList[{{idx}}].reportId" type="text" value="{{row.reportId}}"  readonly='true'   class="form-control "/>
					</td>
					
					<c:if test="${qmReportQuery.qmType=='采购'}">
					<td>
						<input id="qmReportRSnQueryList{{idx}}_billNum" name="qmReportRSnQueryList[{{idx}}].billNum" type="text" value="{{row.billNum}}"  readonly='true'  class="form-control "/>
					</td>
					</c:if>
					
					<td>
						<input id="qmReportRSnQueryList{{idx}}_objSn" name="qmReportRSnQueryList[{{idx}}].objSn" type="text" value="{{row.objSn}}"  readonly='true'  class="form-control "/>
					</td>
					
					
					<td>
						<input id="qmReportRSnQueryList{{idx}}_objCode" name="qmReportRSnQueryList[{{idx}}].objCode" type="text" value="{{row.objCode}}"  readonly='true'  class="form-control "/>
					</td>
					
					
					<td>
						<input id="qmReportRSnQueryList{{idx}}_objName" name="qmReportRSnQueryList[{{idx}}].objName" type="text" value="{{row.objName}}" readonly='true'   class="form-control "/>
					</td>
					
					
					<td>
						<input id="qmReportRSnQueryList{{idx}}_checkResult" name="qmReportRSnQueryList[{{idx}}].checkResult" type="text" value="{{row.checkResult}}" readonly='true'   class="form-control "/>
					</td>
					
					
					<td>
						<div class='input-group form_datetime' id="qmReportRSnQueryList{{idx}}_checkDate" readonly='true'>
		                    <input type='text'  name="qmReportRSnQueryList[{{idx}}].checkDate" class="form-control " readonly='true' value="{{row.checkDate}}"/>

		                </div>						            
					</td>
					
					
					<td>
						<input id="qmReportRSnQueryList{{idx}}_checkTime" name="qmReportRSnQueryList[{{idx}}].checkTime" readonly='true' type="text" value="{{row.checkTime}}"    class="form-control "/>
					</td>


					<td>
						<input id="qmReportRSnQueryList{{idx}}_checkPerson" name="qmReportRSnQueryList[{{idx}}].checkPerson" readonly='true' type="text" value="{{row.checkPerson}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qmReportRSnQueryList{{idx}}_matterName" name="qmReportRSnQueryList[{{idx}}].matterName" readonly='true' type="text" value="{{row.matterName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qmReportRSnQueryList{{idx}}_memo" name="qmReportRSnQueryList[{{idx}}].memo" type="text" readonly='true' value="{{row.memo}}"    class="form-control "/>
					</td>

					<td width="100px">
						<select id="qmReportRSnQueryList{{idx}}_isDeal" name="qmReportRSnQueryList[{{idx}}].isDeal" data-value="{{row.isDeal}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('yes_no')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>


					<td>
						<input id="qmReportRSnQueryList{{idx}}_dealResult" name="qmReportRSnQueryList[{{idx}}].dealResult" type="text" value="{{row.dealResult}}"    class="form-control required"/>
					</td>
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var qmReportRSnQueryRowIdx = 0, qmReportRSnQueryTpl = $("#qmReportRSnQueryTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(qmReportQuery.qmReportRSnQueryList)};
					for (var i=0; i<data.length; i++){
						addRow('#qmReportRSnQueryList', qmReportRSnQueryRowIdx, qmReportRSnQueryTpl, data[i]);
						qmReportRSnQueryRowIdx = qmReportRSnQueryRowIdx + 1;
						if(data[i].isDeal!=undefined){
                            $("#btn-submit").text("处理结果已提交");
						    $("#btn-submit").attr("disabled",true);
                        }
					}
				});
			</script>
			</div>
		</div>
		</div>
		 <c:if test="${fns:hasPermission('qmreportquery:qmReportQuery:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button id="btn-submit" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
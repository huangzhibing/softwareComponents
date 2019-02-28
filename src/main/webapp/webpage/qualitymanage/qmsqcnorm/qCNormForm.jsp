<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验标准定义管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				 submitHandler: function (form) {
	                    var index = jp.loading();
	                    var isAdd = '${isAdd}'
	                    $.ajax({
	                        url: '${ctx}/common/chkCode',
	                        data: {
	                            tableName: "qms_qcnorm",
	                            fieldName: "qcnorm_id",
	                            fieldValue: $('#qcnormId').val()
	                        },
	                        success: function (res) {
	                            if (isAdd === '') {
	                                form.submit();
	                            } else {
	                                if (res === 'true') {
	                                    form.submit();
	                                } else {
	                                    jp.warning("检验标准号已存在");
	                                    return false;
	                                }
	                            }
	                        },
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
			
	        $('#compileDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#beginDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#endDate').datetimepicker({
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/qmsqcnorm/qCNorm"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="qCNorm" action="${ctx}/qmsqcnorm/qCNorm/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

			<c:if test="${isAdd!='true'}">
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验标准号：</label>
					<div class="col-sm-3">
						<form:input path="qcnormId" htmlEscape="false" maxlength="40"
									class="form-control required" readonly="true"/>
					</div>

                    <label class="col-sm-2 control-label"><font color="red">*</font>检验标准名称：</label>
                    <div class="col-sm-3">
                        <form:input path="qcnormName" htmlEscape="false" maxlength="40"    class="form-control required"/>
                    </div>
                </div>
			</c:if>

			<c:if test="${isAdd=='true'}">
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验标准号：</label>
					<div class="col-sm-3">
						<form:input path="qcnormId" htmlEscape="false" maxlength="40"    class="form-control required"/>
					</div>

                    <label class="col-sm-2 control-label"><font color="red">*</font>检验标准名称：</label>
                    <div class="col-sm-3">
                        <form:input path="qcnormName" htmlEscape="false" maxlength="40"    class="form-control required"/>
                    </div>
                </div>
			</c:if>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font> 检验对象编码：</label>
					<div class="col-sm-3">
						<sys:gridselect-modify url="${ctx}/objectdef/objectDef/data" id="qnobj" name="qnobj.id" value="${qCNorm.id}" labelName="qnobj.objCode" labelValue="${qCNorm.qnobj.objCode}"
							 title="选择检验对象编码" cssClass="form-control required" 
							 targetId="objNameRu" targetField="objName"
							 fieldLabels="检验对象编码|检验对象名称" fieldKeys="objCode|objName" searchLabels="检验对象编码|检验对象名称" searchKeys="objCode|objName" ></sys:gridselect-modify>
					</div>

					<label class="col-sm-2 control-label">检验对象名称：</label>
					<div class="col-sm-3">
						<form:input path="objNameRu" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">执行标准：</label>
					<div class="col-sm-3">
						<form:input path="qualityNorm" htmlEscape="false" maxlength="40"    class="form-control "/>					    
					</div>
					  <label class="col-sm-2 control-label">执行标准附件：</label>
					  <div class="col-sm-3">
					    <br>
                        <form:hidden path="qualityNormFile" htmlEscape="false" maxlength="150" class="input-xlarge"/>
                        <sys:ckfinder input="qualityNormFile" type="files" selectMultiple="true" readonly="false" uploadPath="/执行标准附件"/>                    					    					    
					  </div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">编制人：</label>
					<div class="col-sm-3">
						<form:input path="compilePerson" htmlEscape="false" maxlength="20"    class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">编制日期：</label>
					<div class="col-sm-3">
							<div class='input-group form_datetime' id='compileDate'>
			                    <input type='text'  name="compileDate" class="form-control "  value="<fmt:formatDate value="${qCNorm.compileDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">生效日期：</label>
					<div class="col-sm-3">
							<div class='input-group form_datetime' id='beginDate'>
			                    <input type='text'  name="beginDate" class="form-control "
                                       value="<fmt:formatDate
			                    value="${qCNorm.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
					</div>
					<label class="col-sm-2 control-label">失效日期：</label>
					<div class="col-sm-3">
							<div class='input-group form_datetime' id='endDate'>
			                    <input type='text'  name="endDate" class="form-control "  value="<fmt:formatDate value="${qCNorm.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">检验标准子表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#qCNormItemList', qCNormItemRowIdx, qCNormItemTpl);qCNormItemRowIdx = qCNormItemRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验标准号</th>
						<th>检验标准名称</th>
						<th>检验对象编码</th>
						<th>检验对象名称</th>
						<th>检验指标编码</th>
						<th>检验指标名称</th>
						<th>指标单位</th>
	<%-- 				<th width = "100px">数据类型</th>
						<th>数据精度</th>
						<th>检验方法</th>
						<th>检验仪器</th>
						<th>仪器精度</th>
						<th>精度单位</th>
						<th>最大值</th>    	--%>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="qCNormItemList">
				</tbody>
			</table>
			<script type="text/template" id="qCNormItemTpl">//<!--
				<tr id="qCNormItemList{{idx}}">
					<td class="hide">
						<input id="qCNormItemList{{idx}}_id" name="qCNormItemList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="qCNormItemList{{idx}}_delFlag" name="qCNormItemList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="qCNormItemList{{idx}}_qcnormId" name="qCNormItemList[{{idx}}].qcnormId" type="text" value="{{row.qcnormId}}" maxlength="40"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_qcnormName" name="qCNormItemList[{{idx}}].qcnormName" type="text" value="{{row.qcnormName}}" maxlength="40"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_objCode" name="qCNormItemList[{{idx}}].objCode" type="text" value="{{row.objCode}}" maxlength="40"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_objName" name="qCNormItemList[{{idx}}].objName" type="text" value="{{row.objName}}" maxlength="40"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_itemCode" name="qCNormItemList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}" maxlength="40"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_itemName" name="qCNormItemList[{{idx}}].itemName" type="text" value="{{row.itemName}}" maxlength="40"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_itemUnit" name="qCNormItemList[{{idx}}].itemUnit" type="text" value="{{row.itemUnit}}" maxlength="20"    class="form-control "/>
					</td>
					
					
<%-- 				<td>
						<select id="qCNormItemList{{idx}}_dataType" name="qCNormItemList[{{idx}}].dataType" data-value="{{row.dataType}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('dataType')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_dataLimit" name="qCNormItemList[{{idx}}].dataLimit" type="text" value="{{row.dataLimit}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_checkMethod" name="qCNormItemList[{{idx}}].checkMethod" type="text" value="{{row.checkMethod}}" maxlength="50"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_checkAppa" name="qCNormItemList[{{idx}}].checkAppa" type="text" value="{{row.checkAppa}}" maxlength="50"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_appaLimit" name="qCNormItemList[{{idx}}].appaLimit" type="text" value="{{row.appaLimit}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_appaUnit" name="qCNormItemList[{{idx}}].appaUnit" type="text" value="{{row.appaUnit}}" maxlength="8"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="qCNormItemList{{idx}}_maxValue" name="qCNormItemList[{{idx}}].maxValue" type="text" value="{{row.maxValue}}"    class="form-control "/>
					</td>    	--%>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#qCNormItemList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>    
				</tr>//-->
			</script>
	
			<script type="text/javascript">
			var qCNormItemRowIdx = 0, qCNormItemTpl = $("#qCNormItemTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		
			$(document).ready(function() {
				
				<%--var data = ${fns:toJson(qCNorm.qCNormItemList)};--%>
				<%--var data = ${QCNorm};--%>
                <%--debugger--%>
				<%--if(data){--%>

				    <%--var data = data.qCNormItemList;--%>
                    <%--for (var i=0; i<data.length; i++){--%>
                        <%--addRow('#qCNormItemList', qCNormItemRowIdx, qCNormItemTpl, data[i]);--%>
                        <%--qCNormItemRowIdx = qCNormItemRowIdx + 1;--%>
                    <%--}--%>
				<%--}--%>

			
				var data = ${fns:toJson(QCNorm.QCNormItemList)};
			
                  for (var i=0; i<data.length; i++){
                        addRow('#qCNormItemList', qCNormItemRowIdx, qCNormItemTpl, data[i]);
                        qCNormItemRowIdx = qCNormItemRowIdx + 1;
                    }
				
				
				
				

			});
		</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('qmsqcnorm:qCNorm:edit') || isAdd}">
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
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
			jp.post("${ctx}/common/chkCode",{tableName:"qms_qmreport",fieldName:"qmreport_id",fieldValue:$('#qmreportId').val()}
			,function(data){if("${isAdd}"==""){
			    form.submit();jp.close();
			}else{
			    if(data=='true'){
			        form.submit();jp.close();
			    }else{jp.warning("问题处理报告单编号已存在！");return false;}
			}});
		}
		
		function button_submit(){
			$('#state').val('已提交');
			jp.loading("正在提交...");
			form=$('#inputForm');
			jp.post("${ctx}/common/chkCode",{tableName:"qms_qmreport",fieldName:"qmreport_id",fieldValue:$('#qmreportId').val()},function(data){if("${isAdd}"==""){form.submit();jp.close();}else{if(data=='true'){form.submit();jp.close();}else{jp.warning("问题处理报告单编号已存在！");return false;}}});
		}
		
		function openImportDialog(listObj){
		    /*if($("#rndFul").val()=="全检")
				jp.openDialog('检验单列表-全检', '${ctx}/qmreport/qmReportMachine/purlist','1200px', '800px', qmwindow);
		    else if($("#rndFul").val()=="抽检")
		        jp.openDialog('检验单列表-抽检', '${ctx}/qmreport/qmReportMachine/purlist','1200px', '800px', qmwindow);*/
            jp.openDialog('检验单列表', '${ctx}/qmreport/qmReportMachine/purlist','1200px', '800px', qmwindow);
            currentPreId=listObj.id.split("_")[0];
		}
		function importRow(row){
			addRow('#qmReportRSnList', qmReportRSnRowIdx, qmReportRSnTpl, row);
			qmReportRSnRowIdx = qmReportRSnRowIdx + 1;
		}

		function importCurrentRow(row){
			//$("#"+currentPreId+"_purReportRSnid").val(row.purReportRSnId);
            //$("#"+currentPreId+"_objSn").val(row.objSn);
            //$("#"+currentPreId+"_objCode").val(row.objCode);
           // $("#"+currentPreId+"_objName").val(row.objName);
            //$("#"+currentPreId+"_reportId").val(row.reportId);
            $("#"+currentPreId+"_reportId").val(row.mserialno);
            console.log(row.mserialno);
            console.log(row);
            //$("#"+currentPreId+"_checkResult").val(row.checkResult);
            $("#"+currentPreId+"_checkDate").val(row.checkDate);
            $("#"+currentPreId+"_checkTime").val(row.checkTime);

            $("#"+currentPreId+"_checkPerson").val(row.checkPerson);
            $("#"+currentPreId+"_memo").val(row.memo);

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
			$(list+idx).find(".form_date").each(function(){
				 $(this).datetimepicker({
					 format: "YYYY-MM-DD"
			    });
			});
            $(list+idx).find(".form_time").each(function(){
                $(this).datetimepicker({
                    format: "HH:mm:ss"
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

        function changeNum(obj){

            var begin = 15;
            var end = 27;//取二维码前15到27位 为检验对象编码（物料编码）

            var idPre = obj.id.split("_")[0];
            if(obj.value.length==40){
            //截取字符串
				var objCode = obj.value.substring(begin,end);
				if(objCode.trim()!=""){
					jp.get("${ctx}/qmreport/qmReportMachine/getObjByObjCode?objCode="+objCode,function(data) {
						console.log(data);
						if (data.success) {
							$("#" + idPre + "_objName").attr("placeholder", "");
							$("#" + idPre + "_objName").val(data.body.objName);
							$("#" + idPre + "_objCodeIdNames").val(objCode);
						} else {
							$("#" + idPre + "_objName").val("");
							$("#" + idPre + "_objName").attr("placeholder", "自动填写失败！");
							$("#" + idPre + "_objCodeIdNames").val(objCode);
						}



					});
				}
			}
			else{
                $("#" + idPre + "_objName").val("");
                $("#" + idPre + "_objName").attr("placeholder", "二维码位数不正确！");
            }
            //alert($('input[name="office.id"]').val());
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
				<a class="panelButton" href="${ctx}/qmreport/qmReportMachine"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<%--@elvariable id="qmReport" type="QmReport"--%>
		<form:form id="inputForm" modelAttribute="qmReport" action="${ctx}/qmreport/qmReportMachine/save" method="post" class="form-horizontal">
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
						<sys:userselect id="qmPerson" name="qmPerson" value="${qmReport.qmPerson.id}" disabled="disabled" labelName="qmPersonName" labelValue="${qmReport.qmPerson.name}"
										cssClass="form-control required"/>
					</div>

				</div>

				
				<div class="form-group">

					<label class="col-sm-2 control-label"><font color="red">*</font>问题处理意见：</label>
					<div class="col-sm-3">
						<sys:gridselect url="${ctx}/matterhandling/matterHandling/data" id="mhandlingName" name="mhandlingName.id" value="${qmReport.mhandlingName.id}" labelName="mhandlingName.mhandlingname" labelValue="${qmReport.mhandlingName.mhandlingname}"
										title="选择问题处理意见" cssClass="form-control required" fieldLabels="处理意见|意见描述" fieldKeys="mhandlingname|mhandlingdes" searchLabels="处理意见|意见描述" searchKeys="mhandlingname|mhandlingdes" ></sys:gridselect>
					</div>


					<label class="col-sm-2 control-label"><font color="red">*</font>处理日期：</label>
					<div class="col-sm-3">
						<div class='input-group form_datetime' id='qmDate'>
							<input type='text'  name="qmDate" class="form-control required" readonly="true" value="<fmt:formatDate value="${qmReport.qmDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
			<%--<a class="btn btn-white btn-sm" onclick="openImportDialog()" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>

				<a class="btn btn-white btn-sm" onclick="addRow('#qmReportRSnList', qmReportRSnRowIdx, qmReportRSnTpl);qmReportRSnRowIdx=qmReportRSnRowIdx+1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<div class="table-responsive">
				<table class="table table-striped table-bordered table-condensed" style="min-width: 150%">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验对象序列号</th>
						<th><font color="red">*</font>检验对象编码</th>
						<th><font color="red">*</font>检验对象名称</th>
                        <th><font color="red">*</font>检验项目类型</th>
						<th><font color="red">*</font>问题类型</th>
						<th>检验结论</th>
						<th><font color="red">*</font>机器序列号</th>
						<th><font color="red">*</font>检验日期</th>
						<th><font color="red">*</font>检验时间</th>
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
						<input id="qmReportRSnList{{idx}}_objSn" name="qmReportRSnList[{{idx}}].objSn" type="text" value="{{row.objSn}}"  onchange="changeNum(this)"   class="form-control "/>
					</td>
					
					
					<td>
					<sys:gridselect-label url="${ctx}/objectdef/objectDef/data" id="qmReportRSnList{{idx}}_objCodeId" allowInput="true"
                                            labelName="qmReportRSnList[{{idx}}].objCode"
                                            labelValue="{{row.objCode}}"
                                            title="选择检验对象编码" cssClass="form-control required"
                                            targetId="qmReportRSnList{{idx}}_objName" targetField="objName"
                                            fieldLabels="检验对象编码|检验对象名称" fieldKeys="objCode|objName" searchLabels="检验对象编码|检验对象名称"
                                            searchKeys="objCode|objName"></sys:gridselect-label>
						<%--<input id="qmReportRSnList{{idx}}_objCode" name="qmReportRSnList[{{idx}}].objCode" type="text" value="{{row.objCode}}"    class="form-control required"/>--%>
					</td>
					
					
					<td>
						<input id="qmReportRSnList{{idx}}_objName" name="qmReportRSnList[{{idx}}].objName" type="text" value="{{row.objName}}"   class="form-control required"/>
					</td>

					<td style="width: 160px;">
						<select id="qmReportRSnList{{idx}}_matterType" name="qmReportRSnList[{{idx}}].matterType" data-value="{{row.matterType}}" class="form-control required  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('qms_matter_type')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>


					<td width="250px">
					<%--<select id="qmReportRSnList{{idx}}_matterName" name="qmReportRSnList[{{idx}}].matterName" class="form-control required"  data-value="{{row.matterName}}"  >
							<c:forEach items="${fns:getDictList('CheckPrj')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
					</select>--%>
						<%--<input id="qmReportRSnList{{idx}}_matterName" name="qmReportRSnList[{{idx}}].matterName" type="text" value="{{row.matterName}}" readonly='true'   class="form-control "/>--%>
					<sys:gridselect-label1 url="${ctx}/mattertype/matterType/data" id="purReportRSnList{{idx}}_matterNameId" labelNameForValue="item.mattername" labelName="qmReportRSnList[{{idx}}].matterName" labelValue="{{row.matterName}}" allowInput="false" targetField="" targetId=""
							 title="选择问题类型名称"
							cssClass="form-control  required" fieldLabels="问题类型编码|问题类型名称" fieldKeys="mattercode|mattername" searchLabels="问题类型编码|问题类型名称" searchKeys="mattercode|mattername" ></sys:gridselect-label1>
					</td>

					<td>
						<input id="qmReportRSnList{{idx}}_checkResult" name="qmReportRSnList[{{idx}}].checkResult" type="text" value="不合格"  readonly='true'  class="form-control "/>
					</td>



					<td>
					<div class="input-group" style="width: 100%">
						<input id="qmReportRSnList{{idx}}_reportId" name="qmReportRSnList[{{idx}}].reportId" type="text" value="{{row.reportId}}"    style='width: 150px;'  class="form-control " />
							<span class="input-group-btn">
	       		 				<button type="button"  onclick="openImportDialog(this)" id="qmReportRSnList{{idx}}_reportIdButton" class="btn btn-primary"><i class="fa fa-search"></i></button>
	             			</span>

					</div>
					</td>

					

					
					
					<td>
		                <input type='text' id="qmReportRSnList{{idx}}_checkDate" name="qmReportRSnList[{{idx}}].checkDate" class="form-control form_date required"  value="{{row.checkDate}}"/>
		              					            
					</td>
					
					
					<td>
						<input id="qmReportRSnList{{idx}}_checkTime" name="qmReportRSnList[{{idx}}].checkTime" type="text" value="{{row.checkTime}}"   class="form-control form_time required"/>
					</td>
					
					
					<td>
						<input id="qmReportRSnList{{idx}}_checkPerson" name="qmReportRSnList[{{idx}}].checkPerson" type="text" value="{{row.checkPerson}}"    class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="qmReportRSnList{{idx}}_memo" name="qmReportRSnList[{{idx}}].memo" type="text" value="{{row.memo}}"   class="form-control "/>
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
				var currentPreId;
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
	<div style="margin-top: 20px">
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
		                     <a class="btn btn-primary btn-block btn-lg btn-parsley" href="${ctx}/qmreport/qmReportMachine" data-loading-text="正在返回...">返回</a>
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
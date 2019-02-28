<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>联系人维护管理</title>
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
			
		});
		
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("select option").each(function(){
				if(row!=null){
					if($(this).val()==row.linkManSex)
						$(this).attr("selected","selected");
				}
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
		function iniItemNum(){
			$("[id$='linkManCode']").each(function(index,element){
				$(element).val(index+1);
			});

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
				<a class="panelButton" href="${ctx}/linkman/salaccount"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="salaccount" action="${ctx}/linkman/salaccount/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">客户编码：</label>
					<div class="col-sm-10">
						<form:input path="accountCode" htmlEscape="false"   readOnly="readOnly"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">客户名称：</label>
					<div class="col-sm-10">
						<form:input path="accountName" htmlEscape="false"  readOnly="readOnly"   class="form-control "/>
					</div>
				</div>
				 <div class="form-group">
                  	 <label class="col-sm-2 control-label"><font color="red">*</font>所属地区编码：</label>
                   <div class="col-sm-10">
                    <sys:gridselect-modify url="${ctx}/areadef/areaDef/data" id="area" name="areaCode.id" value="${salaccount.areaCode.id}" labelName="areaCode.areaCode" labelValue="${salaccount.areaCode.areaCode}"
                                    title="选择地区" cssClass="form-control required" fieldLabels="地区编码|地区名称" fieldKeys="areaCode|areaName" searchLabels="地区编码|地区名称" searchKeys="areaCode|areaName"
                                    targetId="areaName" targetField="areaName"></sys:gridselect-modify>
                    </div>
                </div>
				<div class="form-group">
					<label class="col-sm-2 control-label">所属地区名称：</label>
					<div class="col-sm-10">
						<form:input path="areaName" htmlEscape="false"  readOnly="readonly" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">客户类别编码：</label>
					<div class="col-sm-10">
						 <sys:gridselect-modify url="${ctx}/accounttype/accountType2/data" id="subType" name="subTypeCode.id" value="${salaccount.subTypeCode.id}" labelName="subTypeCode.accTypeCode" labelValue="${salaccount.subTypeCode.accTypeCode}"
                                    title="选择客户类别" cssClass="form-control required" fieldLabels="客户类别编码|客户类别名称" fieldKeys="accTypeCode|accTypeName" searchLabels="客户类别编码|客户类别名称" searchKeys="accTypeCode|accTypeName"
                                    targetId="subTypeName" targetField="accTypeName"></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">客户类别名称：</label>
					<div class="col-sm-10">
						<form:input path="subTypeName" htmlEscape="false" readOnly="readonly"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">企业性质：</label>
					<div class="col-sm-10">
						<form:input path="accountProp" htmlEscape="false"   readOnly="readOnly"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">法人代表：</label>
					<div class="col-sm-10">
						<form:input path="accountMgr" htmlEscape="false"   readOnly="readOnly"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">地址：</label>
					<div class="col-sm-10">
						<form:input path="accountAddr" htmlEscape="false"  readOnly="readOnly"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">邮编：</label>
					<div class="col-sm-10">
						<form:input path="postCode" htmlEscape="false"  readOnly="readOnly"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">上级部门：</label>
					<div class="col-sm-10">
						<form:input path="supHigherUp" htmlEscape="false"   readOnly="readOnly"  class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">联系人维护：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#linkManList', linkManRowIdx, linkManTpl);linkManRowIdx = linkManRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>联系人编码</th>
						<th><font color="red">*</font>联系人名称</th>
						<th>联系人性别</th>
						<th>联系人电话</th>
						<th>联系人手机</th>
						<th>联系人电子邮件</th>
						<th>联系人传真</th>
						<th>备注</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="linkManList">
				</tbody>
			</table>
			<script type="text/template" id="linkManTpl">//<!--
				<tr id="linkManList{{idx}}">
					<td class="hide">
						<input id="linkManList{{idx}}_id" name="linkManList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="linkManList{{idx}}_delFlag" name="linkManList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="linkManList{{idx}}_linkManCode" name="linkManList[{{idx}}].linkManCode" readonly="readonly" type="text" value="{{row.linkManCode}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="linkManList{{idx}}_linkManName" name="linkManList[{{idx}}].linkManName" type="text" value="{{row.linkManName}}"    class="form-control required"/>
					</td>
					
					<td>
						<select id="linkManList{{idx}}_linkManSex"  name="linkManList[{{idx}}].linkManSex"    class="form-control">
							<option value="男">男</option>	
							<option value="女">女</option>
						</select>
					</td>
					
					<td>
						<input id="linkManList{{idx}}_linkManTel" name="linkManList[{{idx}}].linkManTel"  type="text" value="{{row.linkManTel}}"    class="form-control digits"/>
					</td>
					
					
					<td>
						<input id="linkManList{{idx}}_linkManMobile" name="linkManList[{{idx}}].linkManMobile" type="text" value="{{row.linkManMobile}}"    class="form-control digits"/>
					</td>
					
					
					<td>
						<input id="linkManList{{idx}}_linkManEmail" name="linkManList[{{idx}}].linkManEmail" type="text" value="{{row.linkManEmail}}"    class="form-control email"/>
					</td>
					
					
					<td>
						<input id="linkManList{{idx}}_linkManFax" name="linkManList[{{idx}}].linkManFax" type="text" value="{{row.linkManFax}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="linkManList{{idx}}_remark" name="linkManList[{{idx}}].remark" type="text" value="{{row.remark}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#linkManList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var linkManRowIdx = 0, linkManTpl = $("#linkManTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(salaccount.linkManList)};
					console.log(data);
					for (var i=0; i<data.length; i++){
						addRow('#linkManList', linkManRowIdx, linkManTpl, data[i]);
						linkManRowIdx = linkManRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('linkman:salaccount:edit') || isAdd}">
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>班组管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
                submitHandler: function(form){
					jp.loading();
					var isAdd = '${isAdd}'
					$.ajax({
						url: '${ctx}/common/chkCode',
						data: {
							tableName: "mdm_team",
							fieldName: "team_code",
							fieldValue: $('#teamCode').val()
						},
						success: function (res) {
							if (isAdd === '') {
								form.submit();
							} else {
								if (res === 'true') {
									form.submit();
								} else {
									jp.warning("班组编号已存在");
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
		function changeInfo(value){
			var p = document.getElementById("teamPersonList"+value+"_personCodeId").value;
            $.ajax({
                url: '${ctx}/team/team/changeInfo',
                data: {
                    personWorkId: p
                },
                success: function (res) {
                	document.getElementById("teamPersonList"+value+"_personCodeNames").value = res.user.no;
                	document.getElementById("teamPersonList"+value+"_personCode").value = res.user.no;
                	document.getElementById("teamPersonList"+value+"_personName").value = res.user.name;
                	document.getElementById("teamPersonList"+value+"_workTypeCode").value = res.workTypeCode.workTypeCode;
                	document.getElementById("teamPersonList"+value+"_workTypeName").value = res.workTypeName;
                },
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
				<a class="panelButton" href="${ctx}/team/team"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="team" action="${ctx}/team/team/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>班组编码：</label>
					<div class="col-sm-10">
						<form:input path="teamCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>班组名称：</label>
					<div class="col-sm-10" >
						<form:input path="teamName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>所属部门编码：</label>
					<div class="col-sm-10">
						<sys:treeselect-modify targetId="deptCode" id="deptCode" targetField="code" name="deptCode.id" value="${team.deptCode.id}" labelName="deptCode.name" labelValue="${team.deptCode.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required"  notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>所属部门名称：</label>
					<div class="col-sm-10">
						<form:input path="deptName" htmlEscape="false"   id ="deptCode" class="form-control required"  disabled="true" />
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">班组人员关系表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#teamPersonList', teamPersonRowIdx, teamPersonTpl);teamPersonRowIdx = teamPersonRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>人员编码</th>
						<th>人员名称</th>
						<th>工种编码</th>
						<th>工种名称</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="teamPersonList">
				</tbody>
			</table>
			<script type="text/template" id="teamPersonTpl">//<!--
				<tr id="teamPersonList{{idx}}">
					<td class="hide">
						<input id="teamPersonList{{idx}}_id" name="teamPersonList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="teamPersonList{{idx}}_delFlag" name="teamPersonList[{{idx}}].delFlag" type="hidden" value="0"/>
						<input id="teamPersonList{{idx}}_personCode" name="teamPersonList[{{idx}}].personCode" type="hidden" value="{{row.personCode}}"/>
					</td>
					
					<td  onchange="changeInfo(this.id)" id="{{idx}}">
						<sys:gridselect-modify targetId="teamPersonList{{idx}}_personName"  targetField="teamPersonList{{idx}}.personCode.user.id" url="${ctx}/personwork/personWork/data" id="teamPersonList{{idx}}_personCode" name="teamPersonList[{{idx}}].personCode.id" value="{{row.personCode}}" labelName="teamPersonList{{idx}}.personCode.id" labelValue="{{row.personCode.id}}"
							 title="选择人员编码" cssClass="form-control"  fieldLabels="人员编码|人员名称|工种名称" fieldKeys="user.no|user.name|workTypeName" searchLabels="人员编码|人员名称|工种名称" searchKeys="user.id|user.name|workTypeName"></sys:gridselect-modify>
					</td>
					
					<td>
						<input id="teamPersonList{{idx}}_personName"  name="teamPersonList[{{idx}}].personName" type="text" value="{{row.personName}}"    class="form-control " readonly/>
					</td>
					
					
					<td>
						<input id="teamPersonList{{idx}}_workTypeCode" name="teamPersonList[{{idx}}].workTypeCode" type="text" value="{{row.workTypeCode}}"    class="form-control " readonly/>
					</td>
					
					
					<td>
						<input id="teamPersonList{{idx}}_workTypeName" name="teamPersonList[{{idx}}].workTypeName" type="text" value="{{row.workTypeName}}"    class="form-control " readonly/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#teamPersonList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var teamPersonRowIdx = 0, teamPersonTpl = $("#teamPersonTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(team.teamPersonList)};
					for (var i=0; i<data.length; i++){
						addRow('#teamPersonList', teamPersonRowIdx, teamPersonTpl, data[i]);
						$("#teamPersonList"+i+"_personCodeNames").val(data[i].personCode);
						teamPersonRowIdx = teamPersonRowIdx + 1;
					}
					$("#deptCodeName").val("${team.deptCode.code}");
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('team:team:edit') || isAdd}">
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
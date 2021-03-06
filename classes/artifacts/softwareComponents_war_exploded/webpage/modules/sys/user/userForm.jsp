<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
	var validateForm;
	var $table; // 父页面table表格id
	var $topIndex;//弹出窗口的 index
	function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
	  if(validateForm.form()){
		  $table = table;
		  $topIndex = index;
		  jp.loading();
		  $("#inputForm").submit();
		  return true;
	  }

	  return false;
	}
	$(document).ready(function() {
		$("#no").focus();
		$("#searchRole").on('input',function () {
            var str = $("#searchRole").val().trim();
            var span = $(".table-bordered span");
            for (var i = 4; i < span.length; i++) {
            	if(span[i].textContent.indexOf(str) == -1){
            		span[i].hidden = true;
            	}else{
            		span[i].hidden = false;
            	}
            }
            
        })
		validateForm = $("#inputForm").validate({
			rules: {
					loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
				},
			messages: {
					loginName: {remote: "用户登录名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
			submitHandler : function(form) {
				jp.post("${ctx}/sys/user/save",$('#inputForm').serialize(),function(data){
	                    if(data.success){
	                    	$table.bootstrapTable('refresh');
	                    	jp.success(data.msg);
            	  			
	                    }else{
            	  			jp.error(data.msg);
	                    }
	                    
	                    jp.close($topIndex);//关闭dialog
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
        $('#birthday').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
	});

	
	</script>
</head>
<body class="bg-white">
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
		      <tr>
		         <td class="width-15 active">	<label class="pull-right"><font color="red">*</font>头像：</label></td>
		         <td class="width-35"><form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="input-xlarge"/>
						<sys:ckfinder input="nameImage" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/></td>
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>归属公司:</label></td>
		         <td class="width-35"><sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
						title="公司" url="/sys/office/treeData?type=1" allowClear="true" cssClass="form-control required"/></td>
		      </tr>
		      
		      <tr>
		         <td class="active"><label class="pull-right"><font color="red">*</font>归属部门:</label></td>
		         <td><sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
					 allowClear="true" title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" notAllowSelectParent="true"/></td>
		         <td class="active"><label class="pull-right"><font color="red">*</font>工号:</label></td>
		         <td><form:input path="no" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		      </tr>
		      
		      <tr>
		         <td class="active"><label class="pull-right"><font color="red">*</font>姓名:</label></td>
		         <td><form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		         <td class="active"><label class="pull-right"><font color="red">*</font>登录名:</label></td>
		         <td><input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
					 <form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control required userName"/></td>
		      </tr>
		      
		      
		      <tr>
		         <td class="active"><label class="pull-right"><c:if test="${empty user.id}"><font color="red">*</font></c:if>密码:</label></td>
		         <td><input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="8" class="form-control isPwd ${empty user.id?'required':''}"/>
					<c:if test="${not empty user.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if></td>
		         <td class="active"><label class="pull-right"><c:if test="${empty user.id}"><font color="red">*</font></c:if>确认密码:</label></td>
		         <td><input id="confirmNewPassword" name="confirmNewPassword" type="password"  class="form-control isPwd ${empty user.id?'required':''}" value="" maxlength="50" minlength="8" equalTo="#newPassword"/></td>
		      </tr>
		      
		       <tr>
		         <td class="active"><label class="pull-right">邮箱:</label></td>
		         <td><form:input path="email" htmlEscape="false" maxlength="100" class="form-control email"/></td>
		         <td class="active"><label class="pull-right">电话:</label></td>
		         <td><form:input path="phone" htmlEscape="false" maxlength="100" class="form-control"/></td>
		      </tr>
			  <tr>
				  <td class="active"><label class="pull-right">身份证:</label></td>
				  <td><form:input path="idcard" htmlEscape="false" maxlength="21" class="form-control"/></td>
				  <td class="active"><label class="pull-right">出生年月:</label></td>
				  <td>
					  <div class='input-group date' id='birthday' >
						  <input type='text'  name="birthday" class="form-control" <fmt:formatDate value="${user.birthday}" type="both" dateStyle="full"/> type="both" dateStyle="full"/>
						  <span class="input-group-addon">
							   <span class="glyphicon glyphicon-calendar"></span>
						   </span>
					  </div>
				  </td>

			  </tr>
		      
		      <tr>
		         <td class="active"><label class="pull-right">手机:</label></td>
		         <td><form:input path="mobile" htmlEscape="false" maxlength="100" class="form-control"/></td>
		         <td class="active"><label class="pull-right">是否允许登录:</label></td>
		         <td><form:select path="loginFlag"  class="form-control">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select></td>
		      </tr>
			  <tr>
				  <td class="active"><label class="pull-right">性别:</label></td>
				  <td><form:select path="sex"  class="form-control">
					  <form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				  </form:select></td>
				  <td class="active"><label class="pull-right">是否采购员:</label></td>
				  <td><form:select path="personFlag"  class="form-control">
					  <form:options items="${fns:getDictList('person_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				  </form:select></td>
			  </tr>
			  <tr>
				  <td class="active"><label class="pull-right">财务编码:</label></td>
				  <td><form:input path="fncId" htmlEscape="false" maxlength="50" class="form-control"/></td>
				  <td class="active"><label class="pull-right">财务编码:</label></td>
				  <td><form:input path="fncId1" htmlEscape="false" maxlength="50" class="form-control"/></td>

			  </tr>
		      
		      <tr>
		         <td class="active"><label class="pull-right"><font color="red">*</font>用户角色:</label></td>
		         <td>
		            <input id="searchRole" name="searchRole" class="form-control" type="text" placeholder="搜索角色">
		            <br/>
		         	<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" cssClass="i-checks required"/>
		         	<label id="roleIdList-error" class="error" for="roleIdList"></label>
		         </td>
		         <td class="active"><label class="pull-right">备注:</label></td>
		         <td><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/></td>
		      </tr>
		      
		      <c:if test="${not empty user.id}">
		       <tr>
		         <td class=""><label class="pull-right">创建时间:</label></td>
		         <td><span class="lbl"><fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/></span></td>
		         <td class=""><label class="pull-right">最后登陆:</label></td>
		         <td><span class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></span></td>
		      </tr>
		     </c:if>
		      </tbody>
		      </table>
	</form:form>
</body>
</html>
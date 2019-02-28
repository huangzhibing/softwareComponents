<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>科目定义管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		var validateForm;
		var $cosItemLeftTreeTable; // 父页面table表格id
		var $topIndex;//openDialog的 dialog index
		function doSubmit(treeTable, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $cosItemLeftTreeTable = treeTable;
			  $topIndex = index;
			  jp.loading();
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
						jp.post("${ctx}/cositem/cosItemLeft/save",$('#inputForm').serialize(),function(data){
		                    if(data.success){
		                    	$cosItemLeftTreeTable.jstree("refresh");
		                    	jp.success(data.msg);
		                    }else {
	            	  			jp.error(data.msg);
		                    }
		                    
		                        jp.close($topIndex);//关闭dialog
		            });;
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
			if($("#itemCode").val()==""){
				var id = "0";
				autoNumbering(id);
			}
			
		});
	
		function autoNumbering(id){
			$.ajax({
				url:"${ctx}/cositem/cosItem/autoNumbering?id="+id,
				type: "GET",
				cache: false,
				dataType: "text",
				async:false,
				success:function(data){
					$("#itemCode").val(data);
				}
			});
		}
		
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="cosItemLeft" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
		   <form:hidden path="parent.id" htmlEscape="false"    class="form-control required"/>
		   		<tr>
					<td class="width-15 active"><label class="pull-right">科目编号：</label></td>
					<td class="width-35">
						<form:input path="itemCode" htmlEscape="false"    class="form-control " readonly = "true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>科目名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
<%-- 					<td class="width-15 active"><label class="pull-right">父级编号：</label></td>
					<td class="width-35">
						<sys:treeselect id="parent" name="parent.id" value="${cosItemLeft.parent.id}" labelName="parent.name" labelValue="${cosItemLeft.parent.name}"
						title="父级编号" url="/cositem/cosItemLeft/treeData" extId="${cosItemLeft.id}" cssClass="form-control " allowClear="true"/>
					</td> --%>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>排序：</label></td>
					<td class="width-35">
						<form:input path="sort" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>

		 	</tbody>
		</table>
		</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>核算对象定义管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		var validateForm;
		var $cosProdObjectLeftTreeTable; // 父页面table表格id
		var $topIndex;//openDialog的 dialog index
		function doSubmit(treeTable, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $cosProdObjectLeftTreeTable = treeTable;
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
						jp.post("${ctx}/cosprodobject/cosProdObjectLeft/save",$('#inputForm').serialize(),function(data){
		                    if(data.success){
		                    	$cosProdObjectLeftTreeTable.jstree("refresh");
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
			
		});
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="cosProdObjectLeft" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
						<form:hidden path="parent.id" htmlEscape="false"    class="form-control "/>
<%-- 						<sys:treeselect id="parent" name="parent.id" value="${cosProdObjectLeft.parent.id}" labelName="parent.name" labelValue="${cosProdObjectLeft.parent.name}"
						title="父级编号" url="/cosprodobject/cosProdObjectLeft/treeData" extId="${cosProdObjectLeft.id}" cssClass="form-control " allowClear="true"/> --%>
					<td class="width-15 active"><label class="pull-right">核算对象编码：</label></td>
					<td class="width-35">
					<sys:gridselect-allsuitable url="${ctx}/purinvcheckmain/invCheckMain/dataRequest" id="prodId" name="prodId" value="${cosProdObjectLeft.prodId}" labelName="item.code" labelValue="${cosProdObjectLeft.prodId}"
							 title="选择物料编号" cssClass="form-control required " targetId="name" targetField="name"  fieldLabels="物料编码|物料名称|物料类别" isMultiSelected="true" fieldKeys="code|name|className" searchLabels="物料编码|物料名称|物料类别" searchKeys="code|name|className" ></sys:gridselect-allsuitable>
						<%-- <form:hidden path="prodId" htmlEscape="false"    class="form-control "/> --%>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>核算对象名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required" readonly = "true"/>
					</td>
				</tr>
				
				<tr>
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
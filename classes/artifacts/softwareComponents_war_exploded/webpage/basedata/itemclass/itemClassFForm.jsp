<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>物料附表管理</title>
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
		$(document).ready(function(){
			$('#Name').on('change',function(){
				
				var index=jp.loading();
				var Name = $("#Name").val();
				var Names = Name.split(" ");
				Name = Names[0];
				console.log(Name);
	        	$.ajax({
					url:"${ctx}/itemclass/itemClassF/getClassCode",
					data:{Name:Name},
					
					success:function(re){console.log(re);
						$("#classCode").val(Name+re);
						jp.close(index);
					}				
				});
	        	
	        });
		})
	</script>
	<script type="text/javascript">
		/* $(document).ready(function(){
			if('${isAdd}'=='true'){
				document.getElementById("classCode").disabled="";
			}else{
				document.getElementById("classCode").disabled="disabled";
			}
		}) */
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/itemclass/itemClassF"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="itemClassF" action="${ctx}/itemclass/itemClassF/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>物料类型编码：</label>
					<div class="col-sm-10">
						<form:input readOnly="true"  id="classCode" onblur="chk1(this.value)"  path="classCode" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>物料类型名称：</label>
					<div class="col-sm-10">
						<form:input  onblur="chk(this.value)" path="className" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
				<label class="col-sm-2 control-label">父物料类编码：</label>
			<div class="col-sm-10">
				
				<sys:treeselect-modify id="fatherClassCode" name="fatherClassCode.id" value="${itemClassF.fatherClassCode.id}" labelName="fatherClassCode.classId" labelValue=" ${itemClassF.fatherClassCode.classId}"
					title="父物料类名称" targetId="Name" targetField="classId" url="/itemclass/itemClass/treeData"  extId="${itemClassF.id}" cssClass="form-control " allowClear="true"/>
			</div>
			</div>
			<div  class="form-group">
			 <label class="col-sm-2 control-label">父物料类型名称：</label>
					<div class="col-sm-10">
						<form:input disabled="true" path="fatherClassCode.name" id="Name" htmlEscape="false"    class="form-control"/>
					</div> 
			</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>系统标识：</label>
					<div class="col-sm-10">
						<sys:checkbox id="systemSign" name="systemSign" items="${fns:getDictList('systemSign')}" values="${itemClassF.systemSign}" cssClass="i-checks required"/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('itemclass:itemClassF:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button id="submitbtn" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
<script type="text/javascript">
	/* var flag=1;
	$(document).ready(function(){
		if("${isAdd}"=="true"){
			flag=0;
		}else{
			document.getElementById("centerCode").disabled="true";
		}
	}) */
	function chk(a){
		//if(flag==0){
			var tableName="mdm_itemclass_f";
			var fieldName="class_name";
			var tableValue=a
			var par={tableName:tableName,fieldName:fieldName,fieldValue:tableValue};
			var url="${ctx}/common/chkCode";
			$.post(url,par,function(data){console.log(data);
				if(data=="false"){
					layer.alert("物料名已存在")
					document.getElementById("submitbtn").disabled="disabled";
				}else{
					document.getElementById("submitbtn").disabled="";
				}
			})
		//}
		
	}
	function chk1(a){
		//if('${isAdd}'!='true'){
			var tableName="mdm_itemclass_f";
			var fieldName="class_code";
			var tableValue=a
			var par={tableName:tableName,fieldName:fieldName,fieldValue:tableValue};
			var url="${ctx}/common/chkCode";
			$.post(url,par,function(data){console.log(data);
				if(data=="false"){
					layer.alert("物料代码已存在")
					document.getElementById("submitbtn").disabled="disabled";
				}else{
					document.getElementById("submitbtn").disabled="";
				}
			})
		//}
		
	}
</script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>生成集中领料单</title>
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
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){

				    jp.post("${ctx}/materialorder/sfcMaterialOrder/isExist",$('#inputForm').serialize(),function(data){
						if(data.success){
						    if(data.errorCode=="0"){
						        jp.confirm("已存在该领料日期的领料单，确实重新生成并覆盖当天的领料单？",function () {
									submit();
                                },function () {
									return false;
                                });
							}else if(data.errorCode=="-1"){
                                jp.confirm("已存在该领料日期审核通过的领料单，强制覆盖可能会造成重复领料，仍然强制覆盖当天领料单？",function () {
                                    submit();
                                },function () {
                                    return false;
                                });
							}else{
						        submit();
							}
						}else{
                            jp.error(data.msg);
                        }
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
			
	        $('#operDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
		});

		function submit() {
		    jp.loading("正在执行。。。")
            jp.post("${ctx}/materialorder/sfcMaterialOrder/genMaterialOrder",$('#inputForm').serialize(),function(data){
                if(data.success){
                    $table.bootstrapTable('refresh');
                    jp.success(data.msg);
                    jp.close($topIndex);//关闭dialog
                }else{
                    jp.error(data.msg);
                }
            });
        }
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="sfcMaterialOrder" action="${ctx}/materialorder/sfcMaterialOrder/genMaterialOrder" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>

				<tr>

					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>请填写领料日期：</label></td>
					<td class="width-35">
							<div class='input-group form_datetime' id='operDate'>
			                    <input type='text'  name="operDate" class="form-control required"  value="<fmt:formatDate value="${sfcMaterialOrder.operDate}" pattern="yyyy-MM-dd"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
					</td>
				</tr>

		 	</tbody>
		</table>
	</form:form>
</body>
</html>
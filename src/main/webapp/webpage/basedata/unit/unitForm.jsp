<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>计量单位定义管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
        $(document).ready(function(){
            $('#standUnitCode').change(function(){
                var s=$(this).children('option:selected').val();//这就是selected的值
                // var standunitname = document.getElementById("standunitname");
                if($("#standUnitCode").value == ""){
                    $("#standUnitName").value = "";
				}
                standunitname.value = $(this).children('option:selected').attr("sid");

            })
        })

        $(document).ready(function() {
            $("#inputForm").validate({
                submitHandler: function(form){
                    var index=jp.loading();
                    var isAdd='${isAdd}';
                    $.ajax({
                        url:'${ctx}/common/chkCode',
                        data:{
                            tableName:"mdm_unit",
                            fieldName:"unit_code",
                            fieldValue:$('#unitCode').val(),
                        },
                        success:function(res){
                            if(isAdd===''){
                                form.submit();
                            }else{
                                if(res==='true'){
                                    form.submit();
                                }else{
                                    jp.warning("计量单位编码已存在!");
                                    return false;
                                }
                            }
                        }
                    })
                },
                submitHandler: function(form){
                    var index=jp.loading();
                    var isAdd='${isAdd}';
                    $.ajax({
                        url:'${ctx}/common/chkCode',
                        data:{
                            tableName:"mdm_unit",
                            fieldName:"unit_name",
                            fieldValue:$('#unitName').val(),
                        },
                        success:function(res){
                            if(isAdd===''){
                                form.submit();
                            }else{
                                if(res==='true'){
                                    form.submit();
                                }else{
                                    jp.warning("计量单位名称已存在!");
                                    return false;
                                }
                            }
                        }
                    })
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
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/unit/unit"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="unit" action="${ctx}/unit/unit/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计量单位代码：</label>
					<div class="col-sm-10">
						<form:input path="unitCode" htmlEscape="false" maxlength="4" minlength="4" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计量单位名称：</label>
					<div class="col-sm-10">
						<form:input path="unitName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font> 计量单位类型名称：</label>
					<div class="col-sm-10">
						<sys:gridselect-modify url="${ctx}/unittype/unitType/data" id="unittype" name="unittype.id" targetId="unittype.unitTypeCode" targetField="unittype.unitTypeCode" value="${unit.unittype.id}" labelName="unittype.unitTypeName" labelValue="${unit.unittype.unitTypeName}"
							 title="选择计量单位类型" cssClass="form-control required" fieldLabels="计量单位类别编码|计量单位类别名称" fieldKeys="unitTypeCode|unitTypeName" searchLabels="计量单位类别编码|计量单位类别名称" searchKeys="unitTypeCode|unitTypeName"></sys:gridselect-modify>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>是否标准：</label>
					<div class="col-sm-10">
						<form:select path="isStand" class="form-control required">
							<form:option value="是" label="是"/>
							<form:option value="否" label="否"/>
							<form:options items="${fns:getDictList('isStand')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">标准计量单位编码：</label>
					<div class="col-sm-10">
						<form:select path="standUnitCode" htmlEscape="false"    class="form-control ">
							<option value="" selected></option>
							<c:forEach items="${standUnitType}" var="var">
								<form:option value="${var.unitCode}" sid="${var.unitName}">${var.unitCode}</form:option>
							</c:forEach>
						</form:select>
						<%--<form:input path="standUnitCode" htmlEscape="false"    class="form-control "/>--%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">标准计量单位名称：</label>
					<div class="col-sm-10">
						<form:input path="standUnitName" htmlEscape="false"  id="standunitname"  class="form-control " />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单位换算关系：</label>
					<div class="col-sm-10">
						<form:input path="conversion" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('unit:unit:edit') || isAdd}">
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
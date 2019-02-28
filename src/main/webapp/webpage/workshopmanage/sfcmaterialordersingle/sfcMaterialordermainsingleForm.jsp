<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>原单行领料单管理</title>
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
			
	        $('#editdate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#operdate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
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
				<a class="panelButton" href="${ctx}/sfcmaterialordersingle/sfcMaterialordermainsingle"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="sfcMaterialordermainsingle" action="${ctx}/sfcmaterialordersingle/sfcMaterialordermainsingle/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">单据号：</label>
					<div class="col-sm-10">
						<form:input path="materialorder" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">领料类型：</label>
					<div class="col-sm-10">
						<form:input path="receivetype" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">出入库标记：</label>
					<div class="col-sm-10">
						<form:input path="iotype" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据类型：</label>
					<div class="col-sm-10">
						<form:select path="billtype" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('sfc_material_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">出库标志：</label>
					<div class="col-sm-10">
						<form:input path="outflag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">物料类型：</label>
					<div class="col-sm-10">
						<form:input path="materialtype" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库房代码：</label>
					<div class="col-sm-10">
						<form:input path="wareid" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库房名称：</label>
					<div class="col-sm-10">
						<form:input path="warename" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">核算期：</label>
					<div class="col-sm-10">
						<form:input path="periodid" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">领料类型：</label>
					<div class="col-sm-10">
						<form:input path="itemtype" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">部门代码：</label>
					<div class="col-sm-10">
						<form:input path="shopid" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">部门名称：</label>
					<div class="col-sm-10">
						<form:input path="shopname" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">领料人代码：</label>
					<div class="col-sm-10">
						<form:input path="responser" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">领料人姓名：</label>
					<div class="col-sm-10">
						<form:input path="respname" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人名称：</label>
					<div class="col-sm-10">
						<form:input path="editor" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='editdate'>
			                    <input type='text'  name="editdate" class="form-control"  value="<fmt:formatDate value="${sfcMaterialordermainsingle.editdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">用途代码：</label>
					<div class="col-sm-10">
						<form:input path="useid" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">用途名称：</label>
					<div class="col-sm-10">
						<form:input path="usedesc" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制单人编码：</label>
					<div class="col-sm-10">
						<form:input path="editorid" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-10">
						<form:input path="billstateflag" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">业务日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='operdate'>
			                    <input type='text'  name="operdate" class="form-control"  value="<fmt:formatDate value="${sfcMaterialordermainsingle.operdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库管员编码：</label>
					<div class="col-sm-10">
						<form:input path="wareempid" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库管员名称：</label>
					<div class="col-sm-10">
						<form:input path="wareempname" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工位编码：</label>
					<div class="col-sm-10">
						<form:input path="opercode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工位名称：</label>
					<div class="col-sm-10">
						<form:input path="opername" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">加工路线单号：</label>
					<div class="col-sm-10">
						<form:input path="routinebillno" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工艺编码：</label>
					<div class="col-sm-10">
						<form:input path="routingcode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">出库单号：</label>
					<div class="col-sm-10">
						<form:input path="invbillno" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">退料对应领料单号：</label>
					<div class="col-sm-10">
						<form:input path="materialorderinreturn" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('sfcmaterialordersingle:sfcMaterialordermainsingle:edit') || isAdd}">
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
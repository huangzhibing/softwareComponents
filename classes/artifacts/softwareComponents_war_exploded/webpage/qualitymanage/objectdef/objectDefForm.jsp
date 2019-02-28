<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验对象管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
                    var index = jp.loading();
                    var isAdd = '${isAdd}';
                    $.ajax({
                        url:'${ctx}/objectdef/objectDef/chkCode?accCode='+$('#objCode').val(),
                        success:function(res){
                            console.log(res)
                            if(isAdd === ''){
                                form.submit()
                            } else {
                                if (res === 'true') {
                                    form.submit();
                                } else {
                                    jp.warning("企业编号已存在");
                                    return false;
                                }
                            }
                        },
                        complete:function(){
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



            $('#objCode').click(function () {
                top.layer.open({
                    type: 2,
                    area: ['800px', '500px'],
                    title:"选择检验对象",
                    auto:true,
                    name:'friend',
                    content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/item/item/data2")+"&fieldLabels="+encodeURIComponent("物料编码|物料名称")+"&fieldKeys="+encodeURIComponent("code|name")+"&searchLabels="+encodeURIComponent("物料编码|物料名称")+"&searchKeys="+encodeURIComponent("code|name")+"&isMultiSelected=false",
                    btn: ['确定', '关闭'],
                    yes: function(index, layero){
                        var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                        var items = iframeWin.getSelections();
                        if(items == ""){
                            jp.warning("必须选择一条数据!");
                            return;
                        }
                        item = items[0]
                        console.log(item)
                        var loading = jp.loading("加载中");
                        $('#objName').val(item.name)
                        $('#objId').val(item.id)
                        $('#objCode').val(item.code)
                        jp.close(loading)

                        top.layer.close(index);//关闭对话框。
                    },
                    cancel: function(index){
                    }
                });
            })
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
				<a class="panelButton" href="${ctx}/objectdef/objectDef"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="objectDef" action="${ctx}/objectdef/objectDef/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>检验对象编码：</label>
				<div class="col-sm-10">
					<form:input readonly="true" cssStyle="display: none" path="objId" htmlEscape="false"    class="form-control "/>
					<form:input readonly="true" path="objCode" htmlEscape="false" placeholder="请点击选择检验对象"
								class="form-control required "/>
				</div>
			</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验对象名称：</label>
					<div class="col-sm-10">
						<form:input path="objName" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>检验对象类型编码：</label>
					<div class="col-sm-10">
                        <sys:gridselect-modify url="${ctx}/qmsobjecttype/objectType/data" id="objt"
                                            name="objt.objtcode" value="${objectDef.objt.id}"
                                            labelName="objt.objtcode"
                                            labelValue="${objectDef.objt.objtcode}"
                                            title="选择检验对象类型编码" cssClass="form-control required"
                                            targetId="objtNameRu" targetField="objtname"
                                            fieldLabels="检验对象类型编码|检验对象类型名称|检验对象类型描述" fieldKeys="objtcode|objtname|objtdes" searchLabels="检验对象类型编码|检验对象类型名称|检验对象类型描述"
                                            searchKeys="objtcode|objtname|objtdes"></sys:gridselect-modify>
                    </div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验对象类型名称：</label>
					<div class="col-sm-10">
						<form:input readonly="true" path="objtNameRu" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">采购/制造：</label>
					<div class="col-sm-10">
						<form:select path="mpFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('MP_Flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">是否销售：</label>
					<div class="col-sm-10">
						<form:select path="isSale" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('Is_Sale')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">执行标准：</label>
					<div class="col-sm-10">
						<form:select path="qualityNorm" htmlEscape="false"    class="form-control ">
                            <form:options items="${qualityNormList}" itemLabel="qnormname" itemValue="qnormname" htmlEscape="false"/>
                        </form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">抽样标准：</label>
					<div class="col-sm-10">
						<form:select path="sampNorm" htmlEscape="false"    class="form-control ">
                            <form:option value="GBT 2828.1-2012">GBT 2828.1-2012</form:option>
                        </form:select>
                        
                        
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">检验方法：</label>
					<div class="col-sm-10">
						<form:textarea path="checkMethod" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">采样方法：</label>
					<div class="col-sm-10">
						<form:select path="sampMethod" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('Samp_Method')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>质检与否：</label>
					<div class="col-sm-10">
						<form:select path="qmsFlag" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('qmsFlagforObjdef')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
		<c:if test="${fns:hasPermission('objectdef:objectDef:edit') || isAdd}">
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
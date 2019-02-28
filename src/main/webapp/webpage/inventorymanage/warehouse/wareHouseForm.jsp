<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">


        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if(validateForm.form()){
                $table = table;
                $topIndex = index;
                $("#inputForm").submit();
                return true;
            }

            return false;
        }

        $(document).ready(function() {
            validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					jp.loading();
//					form.submit();

                    $.post("${ctx}/warehouse/wareHouse/save",$('#inputForm').serialize(),function(data){

                        if(data.success){
                            $table.bootstrapTable('refresh');

                            var pro=parent.$("iframe[src$='/softwareComponents/a/warehouse/wareHouse']")[0]
//                            console.log(pro)
                            pro.contentWindow.refresh()
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
            $('#currPeriod').click(function () {
                top.layer.open({
                    type: 2,
                    area: ['600px', '400px'],
                    title:"选择核算期",
                    auto:true,
                    name:'friend',
                    content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/period/period/data?")+"&fieldLabels="+encodeURIComponent("核算期编码")+"&fieldKeys="+encodeURIComponent("periodId")+"&searchLabels="+encodeURIComponent("核算期编码")+"&searchKeys="+encodeURIComponent("periodId")+"&isMultiSelected=false",
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
						$('#currPeriod').val(item.periodId);
						jp.close(loading);
                        top.layer.close(index);//关闭对话框。
                    },
                    cancel: function(index){
                    }
                });
            })
            $('#lastCarrdate').datetimepicker({
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
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="wareHouse" action="${ctx}/warehouse/wareHouse/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库房号：</label>
					<div class="col-sm-10">
						<form:input readonly="true" path="wareID" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>库房名称：</label>
					<div class="col-sm-10">
						<form:input path="wareName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">库房地址：</label>
					<div class="col-sm-10">
						<form:input path="wareAddress" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划资金：</label>
					<div class="col-sm-10">
						<form:input path="planCost" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>管理标识：</label>
					<div class="col-sm-10">
						<form:select path="adminMode" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('adminMode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>价格设置：</label>
					<div class="col-sm-10">
						<form:select path="priceMode" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('priceMode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>当前结算期：</label>
					<div class="col-sm-10">
						<form:input path="currPeriod" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">最近结转日期：</label>
					<div class="col-sm-10">
						<%--<form:input path="lastCarrdate" htmlEscape="false"    class="form-control "/>--%>
						<div class="col-sm-10">
							<p class="input-group">
							<div class='input-group form_datetime' id='lastCarrdate'>
								<input type='text'  name="lastCarrdate" class="form-control required"  value="<fmt:formatDate value="${wareHouse.lastCarrdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
								<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
							</div>
							</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-10">
						<form:input path="note" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>货物标志：</label>
					<div class="col-sm-10">
						<form:select path="itemType" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('itemType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">是否允许库存为负标志：</label>
					<div class="col-sm-10">
						<form:select path="subFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('subFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">是否为含税价入库标志：</label>
					<div class="col-sm-10">
						<form:select path="taxFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('taxFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">批次管理标志：</label>
					<div class="col-sm-10">
						<form:select path="batchFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('batchFlag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><font color="red">*</font>是否为自动仓库：</label>
						<div class="col-sm-10">
							<form:select path="autoFlag" class="form-control required">

								<form:option  label="是" value="Y" htmlEscape="false"/>
								<form:option  label="否" value="N" htmlEscape="false"/>
							</form:select>
						</div>
					</div>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
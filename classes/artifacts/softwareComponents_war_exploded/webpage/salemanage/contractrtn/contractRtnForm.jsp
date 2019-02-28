<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>销售回款单据管理</title>
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
            $('#contractCode').click(function () {
                $('tr td').remove();
                top.layer.open({
                    type: 2,
                    area: ['800px', '500px'],
                    title:"选择入库单",
                    auto:true,
                    name:'friend',
                    content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/salcontract/contract/data?status=E")+"&fieldLabels="+encodeURIComponent("合同单号|合同签订日期")+"&fieldKeys="+encodeURIComponent("contractCode|signDate")+"&searchLabels="+encodeURIComponent("合同单号|合同签订日期")+"&searchKeys="+encodeURIComponent("contractCode|signDate")+"&isMultiSelected=false",
                    btn: ['确定', '关闭'],
                    yes: function(index, layero){
                        var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                        var items = iframeWin.getSelections();
                        if(items == ""){
                            jp.warning("必须选择一条数据!");
                            return;
                        }
                        item = items[0];
                        console.log(item);
                        var test=jp.loading("正在录入");
                        $('#itemId').val(item.id);
                        $('#contractCode').val(item.contractCode);
                        $('#accountNames').val(item.account.id);
                        $('#accountName').val(item.accountName);
                        jp.close(test);
                        top.layer.close(index);//关闭对话框。
                    },
                    cancel: function(index){
                    }
                });
            });
	        $('#inputDate').datetimepicker({
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
				<a class="panelButton" href="${ctx}/contractrtn/contractRtn"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="contractRtn" action="${ctx}/contractrtn/contractRtn/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>回款单编码：</label>
					<div class="col-sm-10">
						<form:input path="rtnBillCode" readonly="true" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">合同单据：</label>
				<div class="col-sm-10 input-group">
					<form:input  path="contractCode" readonly="true" htmlEscape="false"   class="form-control "/>
					<span class="input-group-btn">
                                         <button type="button"  id="${id}Button" class="btn <c:if test="${fn:contains(cssClass, 'input-sm')}"> btn-sm </c:if><c:if test="${fn:contains(cssClass, 'input-lg')}"> btn-lg </c:if>  btn-primary ${disabled} ${hideBtn ? 'hide' : ''}"><i class="fa fa-search"></i>
                                         </button>
                                           <button type="button" id="${id}DelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
                                     </span>
				</div>
			</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>回款金额：</label>
					<div class="col-sm-10">
						<form:input path="rtnSum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>客户编码：</label>
				<div class="col-sm-10">
					<input id="accountNames" name="account.accountCode" readonly="readonly"
						   type="text" value="${contractRtn.account.id}" data-msg-required="" class="form-control required" style="" aria-required="true">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>客户名称：</label>
				<div class="col-sm-10">
					<form:input path="accountName" htmlEscape="false"  readonly="true"  class="form-control required"/>
				</div>
			</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">录入日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='inputDate'>
			                    <input type='text'  name="inputDate" class="form-control"  value="<fmt:formatDate value="${contractRtn.inputDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('contractrtn:contractRtn:edit') || isAdd}">
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
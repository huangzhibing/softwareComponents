<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>关系企业管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">

        $(document).ready(function () {
            $("#accountCode").attr("maxlength","64")
            var isAdd = '${isAdd}'
            if(isAdd === ''){
                $('#accountCode').attr('readOnly',true)
            }
            $("#inputForm").validate({
                submitHandler: function (form) {
                    var index = jp.loading();
                    var isAdd = '${isAdd}'
                    $.ajax({
                        url: '${ctx}/common/chkCode',
                        data: {
                            tableName: "mdm_account",
                            fieldName: "account_code",
                            fieldValue: $('#accountCode').val()
                        },
                        success: function (res) {
                            if (isAdd === '') {
                                form.submit();
                            } else {
                                if (res === 'true') {
                                    form.submit();
                                } else {
                                    jp.warning("企业编号已存在");
                                    return false;
                                }
                            }
                        },
                    });
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
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
                        <a class="panelButton" href="${ctx}/account/account"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="account" action="${ctx}/account/account/save"
                               method="post" class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message content="${message}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>企业编码：</label>
                            <div class="col-sm-10">
                                <form:input path="accountCode" htmlEscape="false" class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>企业名称：</label>
                            <div class="col-sm-10">
                                <form:input path="accountName" htmlEscape="false" class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>企业类型编码：</label>
                            <div class="col-sm-10">
                                <sys:gridselect-modify url="${ctx}/accounttype/accountType/data" id="accTypeCode"
                                                    name="accType.accTypeCode" value="${account.accType.accTypeCode}"
                                                    labelName="accType.accTypeCode"
                                                    labelValue="${account.accType.accTypeCode}"
                                                    title="选择企业类型编码" cssClass="form-control required"
                                                    targetId="accTypeNameRu" targetField="accTypeName"
                                                    fieldLabels="企业类型编码|企业类型名称" fieldKeys="accTypeCode|accTypeName" searchLabels="企业类型编码|企业类型名称"
                                                    searchKeys="accTypeCode|accTypeName"></sys:gridselect-modify>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">企业类型名称：</label>
                            <div class="col-sm-10">
                                <form:input readonly="true" path="accTypeNameRu" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">法人：</label>
                            <div class="col-sm-10">
                                <form:input path="accountMgr" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">联系地址：</label>
                            <div class="col-sm-10">
                                <form:input path="accountAddr" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">邮编：</label>
                            <div class="col-sm-10">
                                <form:input path="postCode" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">性质：</label>
                            <div class="col-sm-10">
                                <form:input path="accountProp" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>税率：</label>
                            <div class="col-sm-10">
                            	<input id="taxRatioNew" name = "taxRatioNew"  htmlEscape="false"   class="form-control required"  value="<fmt:formatNumber value="${account.taxRatio}" maxIntegerDigits="4" type="percent"/>"/>百分号格式
                                <%--  <form:input path="taxRatio" htmlEscape="false" class="form-control required"/>   --%>
                            </div>
                        </div>
                        <c:if test="${fns:hasPermission('account:account:edit') || isAdd}">
                            <div class="col-lg-3"></div>
                            <div class="col-lg-6">
                                <div class="form-group text-center">
                                    <div>
                                        <button class="btn btn-primary btn-block btn-lg btn-parsley"
                                                data-loading-text="正在提交...">提 交
                                        </button>
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
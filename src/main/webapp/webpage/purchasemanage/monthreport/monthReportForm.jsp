<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>进料不合格统计表管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">

        $(document).ready(function () {
            $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.loading();
                    form.submit();
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

            $('#chkDate').datetimepicker({
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
                        <a class="panelButton" href="${ctx}/monthreport/monthReport"><i class="ti-angle-left"></i>
                            返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="monthReport" action="${ctx}/monthreport/monthReport/save"
                               method="post" class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message content="${message}"/>





                            <div class="form-group">
                                <label class="col-sm-2 control-label">物料名称：</label>
                                <div class="col-sm-3">
                                    <sys:gridselect-pursup url="${ctx}/applymain/applyMain/itemData" id="item"
                                                       name="item.id"
                                                           value="${monthReport.item.id}" labelName="item.name"
                                                           labelValue="${monthReport.item.name}"
                                                           title="选择物料" cssClass="form-control"
                                                           targetId="specModel|itemNameNew" targetField="specModel|name"
                                                           fieldLabels="物料名称|规格型号"
                                                           fieldKeys="name|specModel" searchLabels="物料名称|规格型号"
                                                           searchKeys="name|specModel"></sys:gridselect-pursup>
                                </div>
                                <form:input path="itemNameNew" htmlEscape="false" class="form-control hidden "/>


                                <label class="col-sm-2 control-label">物料规格：</label>
                                <div class="col-sm-3">
                                    <form:input path="specModel" htmlEscape="false" class="form-control " readonly="true"/>
                                </div>
                            </div>

                        <div class="form-group">

                            <label class="col-sm-2 control-label" ><font color="red">*</font>报检单编号：</label>
                            <div class="col-sm-8">

                                <sys:gridselect-pursup url="${ctx}/monthreport/monthReport/purReportNew"
                                                       id="purReportNew" name="purReportNew.id"
                                                       value="${monthReport.purReportNew.id}"
                                                       labelName="purReportNew.reportId"
                                                       labelValue="${monthReport.purReportNew.reportId}"
                                                       title="选择IQC来料报检单编号"  cssClass="form-control required "
                                                       fieldLabels="报检单编号|供应商编号|供应商名称"
                                                       fieldKeys="reportId|supId|supname1"
                                                       searchLabels="报检单编号|供应商编号|供应商名称"
                                                       searchKeys="reportId|supId|supname1"
                                                       targetId="supName|supCode|purQty|chkRes"
                                                       targetField="supname1|supId|itemCount|checkResult"></sys:gridselect-pursup>
                            </div>
                            </div>




                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>供应商编号：</label>
                            <%--<div class="col-sm-3">--%>
                                <%--<sys:gridselect-pursup url="${ctx}/account/account/data" id="supId" name="supId.id"--%>
                                                       <%--value="${monthReport.supId.id}" labelName="supId.accountCode"--%>
                                                         <%--labelValue="${monthReport.supId.accountCode}"--%>
                                                       <%--title="选择供应商编号"  cssClass="form-control"--%>
                                                       <%--fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName"--%>
                                                       <%--searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName"--%>
                                                       <%--targetId="supName"--%>
                                                       <%--targetField="accountName"></sys:gridselect-pursup>--%>
                            <%--</div>--%>

                            <div class="col-sm-3">
                                <form:input path="supCode" htmlEscape="false" class="form-control  required "
                                            readonly="true"/>
                            </div>



                            <label class="col-sm-2 control-label"><font color="red">*</font>供应商名称：</label>
                            <div class="col-sm-3">
                                <form:input path="supName" htmlEscape="false" class="form-control required" readonly="true"/>
                            </div>
                        </div>


                        <%--<div class="form-group">--%>
                        <%--<label class="col-sm-2 control-label">物料名称：</label>--%>
                        <%--<div class="col-sm-3">--%>
                        <%--<form:input path="name" htmlEscape="false" class="form-control "/>--%>
                        <%--</div>--%>

                        <%--<label class="col-sm-2 control-label">物料规格：</label>--%>
                        <%--<div class="col-sm-3">--%>
                        <%--<form:input path="specModel" htmlEscape="false" class="form-control "/>--%>
                        <%--</div>--%>
                        <%--</div>--%>




                        <%--<td>--%>
                            <%--<sys:gridselect-pursup url="${ctx}/applymain/applyMain/itemData" id="item" name="item.id"--%>
                                                   <%--value="${monthreport.item.id}" labelName="item.code"--%>
                                                   <%--labelValue="${monthreport.item.code}"--%>
                                                   <%--title="选择物料代码"--%>
                                                   <%--targetId="itemName|itemSpecmodel" targetField="name|specModel"--%>
                                                   <%--cssClass="form-control required" fieldLabels="物料名称|规格型号"--%>
                                                   <%--fieldKeys="name|specModel" searchLabels="物料名称|规格型号"--%>
                                                   <%--searchKeys="name|specModel"></sys:gridselect-pursup>--%>
                        <%--</td>--%>


                        <div class="form-group">
                            <label class="col-sm-2 control-label">物料类别：</label>
                            <div class="col-sm-8">
                                <form:input path="className" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-2 control-label">来料数量：</label>
                            <div class="col-sm-3">
                                <form:input path="purQty" htmlEscape="false" class="form-control  isFloatGtZero"/>
                            </div>

                            <label class="col-sm-2 control-label">抽检数量：</label>
                            <div class="col-sm-3">
                                <form:input path="chkQty" htmlEscape="false" class="form-control  isFloatGtZero"/>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-2 control-label">不良数量：</label>
                            <div class="col-sm-3">
                                <form:input path="failQty" htmlEscape="false" class="form-control  isFloatGteZero"/>
                            </div>

                            <label class="col-sm-2 control-label">不良率：</label>
                            <div class="col-sm-3">
                                <form:input path="ratio" htmlEscape="false" class="form-control  isFloatGteZero"/>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-2 control-label">检验结果：</label>
                            <div class="col-sm-3">
                                <form:input path="chkRes" htmlEscape="false" class="form-control "/>
                            </div>

                            <label class="col-sm-2 control-label">检验日期：</label>
                            <div class="col-sm-3">
                                <p class="input-group">
                                <div class='input-group form_datetime' id='chkDate'>
                                    <input type='text' name="chkDate" class="form-control"
                                           value="<fmt:formatDate value="${monthReport.chkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
                                </div>
                                </p>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-2 control-label">录入人编号：</label>
                            <div class="col-sm-3">
                                <form:hidden path="makeEmpid.id" value="${fns:getUser().id}"/>
                                <form:input path="makeEmpid.no" htmlEscape="false" readonly="true"
                                            value="${fns:getUser().no}" class="form-control "/>
                            </div>

                            <label class="col-sm-2 control-label">录入人名称：</label>
                            <div class="col-sm-3">
                                <form:input path="makeEmpName" htmlEscape="false" readonly="true"
                                            value="${fns:getUser().name}" class="form-control "/>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-2 control-label">备注信息：</label>
                            <div class="col-sm-8">
                                <form:textarea path="remarks" htmlEscape="false" rows="4" class="form-control "/>
                            </div>
                        </div>
                        <c:if test="${fns:hasPermission('monthreport:monthReport:edit') || isAdd}">
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
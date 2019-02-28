<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@ include file="/webpage/include/bootstraptable.jsp"%>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#inputForm").validate({
                submitHandler: function(form){
                    var index=jp.loading();
                    form.submit();
                    //var isAdd='${isAdd}';
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
    <title>其它工资分配计算</title>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">
                <a class="panelButton" href="${ctx}/cospersonother/cosPersonOther?flag=fenpei"><i class="ti-angle-left"></i> 返回</a>
            </h3>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-sm-12 col-md-12 animated fadeInRight">
                    <form:form id="inputForm"  modelAttribute="cosPersonOther" action="${ctx}/cospersonother/cosPersonOther/distribution" method="post" class="form-horizontal">
                        <sys:message content="${message}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>当前日期：</label>
                            <div class="col-sm-10">
                                <input name="nowDate"  value="${nowDate}" readonly="readonly"   class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>当前核算期：</label>
                            <div class="col-sm-10">
                                <input name="nowPeriod" value="${nowPeriod}"  readonly="readonly"  class="form-control required"/>
                            </div>
                        </div>
                        </div>
                        <div class="col-lg-3"></div>
                        <div class="col-lg-6">
                            <div class="form-group text-center">
                                <div>
                                    <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">分配计算</button>
                                </div>
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
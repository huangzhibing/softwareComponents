<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>进料不合格统计表管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@include file="monthReportList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">进料不合格统计表列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="monthReport"
                                   class="form form-horizontal well clearfix">


                            <%--<div class="col-xs-12 col-sm-6 col-md-4">--%>
                                <%--<label class="label-item single-overflow pull-left">报检单编号：</label>--%>
                                <%--<form:input path="monthReport.purReportNew.reportId" htmlEscape="false" maxlength="64"--%>
                                            <%--class=" form-control"/>--%>
                            <%--</div>--%>

                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left">供应商编号</label>
                                <form:input path="supCode" htmlEscape="false" maxlength="64" class=" form-control"/>
                            </div>

                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left">供应商名称</label>
                                <form:input path="supName" htmlEscape="false" maxlength="64" class=" form-control"/>
                            </div>


                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left">物料名称</label>
                                <form:input path="name" htmlEscape="false" maxlength="64" class=" form-control"/>
                            </div>


                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <div style="margin-top:26px">
                                    <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                            class="fa fa-search"></i> 查询</a>
                                    <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                            class="fa fa-refresh"></i> 重置</a>
                                </div>
                            </div>
                        </form:form>
                        <div id="printContent">
                        </div>
                    </div>
                </div>
                <%--&lt;%&ndash;&ndash;%&gt;--%>
                <!-- 工具栏 -->
                <%--<div id="printContent">--%>
                    <div id="toolbar">
                        <shiro:hasPermission name="monthreport:monthReport:add">
                            <a id="add" class="btn btn-primary" href="${ctx}/monthreport/monthReport/form"
                               title="进料不合格统计表"><i class="glyphicon glyphicon-plus"></i> 新建</a>
                        </shiro:hasPermission>


                        <shiro:hasPermission name="monthreport:monthReport:list">
                            <button id="print" class="btn btn-success" onclick="print()">
                                <i class="glyphicon glyphicon-view"></i> 打印
                            </button>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="monthreport:monthReport:edit">
                            <button id="edit" class="btn btn-success" disabled onclick="edit()">
                                <i class="glyphicon glyphicon-edit"></i> 修改
                            </button>

                        </shiro:hasPermission>


                        <shiro:hasPermission name="monthreport:monthReport:del">
                            <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
                                <i class="glyphicon glyphicon-remove"></i> 删除
                            </button>
                        </shiro:hasPermission>
                        <%--<shiro:hasPermission name="monthreport:monthReport:import">--%>
                        <%--<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>--%>
                        <%--<div id="importBox" class="hide">--%>
                        <%--<form id="importForm" action="${ctx}/monthreport/monthReport/import" method="post" enctype="multipart/form-data"--%>
                        <%--style="padding-left:20px;text-align:center;" ><br/>--%>
                        <%--<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　--%>
                        <%----%>
                        <%----%>
                        <%--</form>--%>
                        <%--</div>--%>
                        <%--</shiro:hasPermission>--%>
                        <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2"
                           href="#collapseTwo">
                            <i class="fa fa-search"></i> 检索
                        </a>
                    </div>

                    <!-- 表格 -->
                    <table id="monthReportTable" data-toolbar="#toolbar"></table>

                    <!-- 创建一个空的iframe，因为如果每次请求都生成PDF，那么是不必要的。 -->
                    <iframe style="display:none" id="printIframe"></iframe>

                    <!-- context menu -->
                    <ul id="context-menu" class="dropdown-menu">
                        <shiro:hasPermission name="monthreport:monthReport:edit">
                            <li data-item="edit"><a>编辑</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="monthreport:monthReport:del">
                            <li data-item="delete"><a>删除</a></li>
                        </shiro:hasPermission>
                        <li data-item="action1"><a>取消</a></li>
                    </ul>
                </div>
            </div>
        </div>
</body>
</html>
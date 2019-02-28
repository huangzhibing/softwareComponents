<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>产成品出库表</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp"%>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@include file="billMain.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">产成品出库表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>

            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="billMain" class="form form-horizontal well clearfix">
                            <div class="col-xs-12 col-sm-6 col-md-3">
                                <label class="label-item single-overflow pull-left" title="收货单位：">收货单位：</label>
                                <input name="corName" htmlEscape="false" maxlength="20"  class=" form-control"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-3">
                                <label class="label-item single-overflow pull-left" title="订单号：">订单号：</label>
                                <input name="orderId" htmlEscape="false" maxlength="20"  class=" form-control"/>
                            </div>
                            <%--<div class="col-xs-12 col-sm-6 col-md-3">--%>
                                <%--<label class="label-item single-overflow pull-left" title="物料代码：">物料代码：</label>--%>
                                <%--<sys:gridselect-item url="${ctx}/item/item/data2" id="item" name="itemCode" value="${billDetail.item.id}" labelName="item.code" labelValue="${billDetail.item.code}"--%>
                                                     <%--title="选择物料代码" cssClass="form-control  required" fieldLabels="物料代码|物料名称|规格型号" fieldKeys="code|name|specModel" searchLabels="物料代码|物料名称|规格型号" searchKeys="code|name|specModel"--%>
                                                     <%--targetId="itemName" targetField="name" extraField="itemSpec:specModel">--%>
                                <%--</sys:gridselect-item>--%>
                                    <%--&lt;%&ndash;<input type='text' name="item.id" class=" form-control"/>&ndash;%&gt;--%>
                            <%--</div>--%>
                            <div class="col-xs-12 col-sm-6 col-md-3">
                                <label class="label-item single-overflow pull-left" title="产品名称：">产品名称：</label>
                                <input type='text' id="itemName" name="item.name"   class=" form-control"/>
                            </div>
                            <%--<div class="col-xs-12 col-sm-6 col-md-3">--%>
                                <%--<label class="label-item single-overflow pull-left" title="规格型号：">规格型号：</label>--%>
                                <%--<input type='text' id="itemSpec" name="itemSpec"   class=" form-control"/>--%>
                            <%--</div>--%>
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 col-md-6">
                                    <div class="form-group">
                                        <label class="label-item single-overflow pull-left" title="出库日期：">&nbsp;出库日期：</label>
                                        <div class="col-xs-12">
                                            <div class="col-xs-12 col-sm-6 col-md-5">
                                                <div class='input-group date' id='beginBillDate' style="left: 0px;" >
                                                    <input type='text'  name="beginBillDate" class="form-control"  />
                                                    <span class="input-group-addon">
													   <span class="glyphicon glyphicon-calendar"></span>
												   </span>
                                                </div>
                                            </div>
                                            <div class="col-xs-12 col-sm-6 col-sm-1">
                                                ~
                                            </div>
                                            <div class="col-xs-12 col-sm-6 col-md-5">
                                                <div class='input-group date' id='endBillDate' style="left: -10px;" >
                                                    <input type='text'  name="endBillDate" class="form-control" />
                                                    <span class="input-group-addon">
													   <span class="glyphicon glyphicon-calendar"></span>
												   </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            <div class="col-xs-12 col-sm-6 col-md-3">
                                <div style="margin-top:26px">
                                    <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
                                    <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>

            <!-- 工具栏 -->
            <div id="toolbar">
                <%--<shiro:hasPermission name="billmain:billMain:add">--%>
                    <%--<a id="add" class="btn btn-primary" href="${ctx}/outboundinput/outboundInput/form?type=Input" title="单据"><i class="glyphicon glyphicon-plus"></i> 新建</a>--%>
                <%--</shiro:hasPermission>--%>
                <%--<button id="edit" class="btn btn-success" onclick="print()">--%>
                    <%--<i class="glyphicon glyphicon-edit"></i>  打印--%>
                <%--</button>--%>
                <%--<shiro:hasPermission name="billmain:billMain:del">--%>
                    <%--<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">--%>
                        <%--<i class="glyphicon glyphicon-remove"></i> 删除--%>
                    <%--</button>--%>
                <%--</shiro:hasPermission>--%>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>

            <!-- 表格 -->
            <table id="billMainTable"   data-toolbar="#toolbar"></table>
            <iframe style="display:none" id="printIframe"></iframe>
            <!-- context menu -->
            <ul id="context-menu" class="dropdown-menu">
                <shiro:hasPermission name="billmain:billMain:edit">
                    <li data-item="edit"><a>编辑</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="billmain:billMain:del">
                    <li data-item="delete"><a>删除</a></li>
                </shiro:hasPermission>
                <li data-item="action1"><a>取消</a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
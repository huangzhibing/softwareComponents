<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>库存盘点表</title>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp"%>
    <%@include file="/webpage/include/treeview.jsp"%>
    <%@include file="inventoryCheckList.js"%>
</head>
<body>
    <div class="wrapper warpper-content">
        <div class="panel panel-primary">
            <div class="panel-body">
                <sys:message content="${message}"/>

                <!-- 搜索 -->
                <div class="accordion-group">
                    <div id="collapseTwo" class="accordion-body collapse">
                        <form:form id="searchForm" modelAttribute="inventoryCheck" class="form form-horizontal well clearfix">
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="库房号：">仓库编码：</label>
                                <sys:gridselect-modify url="${ctx}/warehouse/wareHouse/data" id="ware" name="ware.id" value="${billMain.ware.id}" labelName="ware.wareID" labelValue="${billMain.ware.wareID}"
                                                       title="选择仓库编码" cssClass="form-control required" fieldLabels="仓库编码|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库编码|仓库名称" searchKeys="wareID|wareName"
                                                       targetId="wareName" targetField="wareName" ></sys:gridselect-modify>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="库房名称：">仓库名称：</label>
                                <input id="wareName" name="wareName" htmlEscape="false" maxlength="100" class=" form-control"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="核算期：">会计年度：</label>
                                <input id="year" name="year" htmlEscape="false" maxlength="100" class=" form-control"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="核算期：">会计期间：</label>
                                <input id="period" name="period" htmlEscape="false" maxlength="100" class=" form-control"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="laber-item pull-left single-overflow" title="物料类编码：">物料类编码：</label>
                                <sys:treeselect-modify targetId="className" targetField="classId" id="classCode" name="classCode.id" value="${item.classCode.id}" labelName="classCode.classId" labelValue="${item.classCode.classId}"
                                                       title="物料类型编码" url="/item/itemClassNew/treeData"  cssClass="form-control required" allowClear="true" />
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4" >
                                <label class="label-item single-overflow pull-left" title="物料类名称：">物料类名称：</label>
                                <input name="className" htmlEscape="false"   id="className" class="form-control " />
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="物料代码：">物料代码：</label>
                                <sys:gridselect-modify url="${ctx}/item/item/data2" id="item" name="itemCode" value="${billDetail.item.id}" labelName="item.code" labelValue="${billDetail.item.code}"
                                                       title="选择物料代码" cssClass="form-control  required" fieldLabels="物料代码|物料名称|规格型号" fieldKeys="code|name|specModel" searchLabels="物料代码|物料名称|规格型号" searchKeys="code|name|specModel"
                                                       targetId="itemName" targetField="name" extraField="itemSpec:specModel">
                                </sys:gridselect-modify>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="物料名称：">物料名称：</label>
                                <input type='text' id="itemName" name="itemName"   class=" form-control"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="规格型号：">规格型号：</label>
                                <input type='text' id="itemSpec" name="itemSpec"   class=" form-control"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <div style="...">
                                    <a id="search" class="btn btn-primary btn-rounded btn-bordered btn-sm">
                                        <i class="fa fa-search"></i>查询
                                    </a>
                                    <a id="reset" class="btn btn-primary btn-rounded btn-bordered btn-sm">
                                        <i class="fa fa-refresh"></i>重置
                                    </a>
                                </div>
                            </div>
                        </form:form>

                    </div>
                </div>

            <!--工具栏-->
            <div id="toolbar">
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i>检索
                </a>
            </div>

            <!--表格-->
            <table id="inventoryCheckTable" data-toolbar="#toolbar"></table>
        </div>
        </div>
    </div>

</body>
</html>

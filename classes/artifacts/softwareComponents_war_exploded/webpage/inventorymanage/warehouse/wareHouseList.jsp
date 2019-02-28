<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="wareHouseList.js" %>
    <%@include file="wareHouseTreeList.js" %>
    <%@include file="binList.js" %>
    <%@include file="locationList.js" %>

</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">仓库列表</h3>
	</div>
	<div class="panel-body">
        <div class="row">
            <%--树--%>
            <div class="col-sm-4 col-md-3">
                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-10">
                            <div class="input-search">
                                <button type="submit" class="input-search-btn">
                                    <i class="fa fa-search" aria-hidden="true"></i></button>
                                <input id="search_q" type="text" class="form-control input-sm" name=""
                                       placeholder="查找...">

                            </div>
                        </div>
                    </div>
                </div>
                <div id="wareHouseJsTree"></div>
            </div>

            <div id="wareHouseTab" class="col-sm-8 col-md-9 animated fadeInRight">

                <sys:message content="${message}"/>

                <!-- 搜索 -->
                <div class="accordion-group">
                    <div id="collapseTwo" class="accordion-body collapse">
                        <div class="accordion-inner">
                            <form:form id="searchForm" modelAttribute="wareHouse" class="form form-horizontal well clearfix">
                                <div class="col-xs-12 col-sm-6 col-md-4">
                                    <label class="label-item single-overflow pull-left" title="库房号：">库房号：</label>
                                    <form:input path="wareID" htmlEscape="false" maxlength="4"  class=" form-control"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-4">
                                    <label class="label-item single-overflow pull-left" title="库房名称：">库房名称：</label>
                                    <form:input path="wareName" htmlEscape="false" maxlength="20"  class=" form-control"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-4">
                                    <label class="label-item single-overflow pull-left" title="货物标志：">货物标志：</label>
                                    <form:select path="itemType"  class="form-control m-b">
                                        <form:option value="" label=""/>
                                        <form:options items="${fns:getDictList('itemType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-4">
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
                    <shiro:hasPermission name="warehouse:wareHouse:add">
                        <a id="add" class="btn btn-primary" onclick="wareHouseForm()" href="javascript:void(0)" title="仓库"><i class="glyphicon glyphicon-plus"></i> 新建</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:edit">
                        <button id="edit" class="btn btn-success" disabled onclick="edit()">
                            <i class="glyphicon glyphicon-edit"></i> 修改
                        </button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:del">
                        <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
                            <i class="glyphicon glyphicon-remove"></i> 删除
                        </button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:import">
                        <button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
                        <div id="importBox" class="hide">
                            <form id="importForm" action="${ctx}/warehouse/wareHouse/import" method="post" enctype="multipart/form-data"
                                  style="padding-left:20px;text-align:center;" ><br/>
                                <input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　


                            </form>
                        </div>
                    </shiro:hasPermission>
                    <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                        <i class="fa fa-search"></i> 检索
                    </a>
                </div>

                <!-- 表格 -->
                <table id="wareHouseTable"   data-toolbar="#toolbar"></table>

                <!-- context menu -->
                <ul id="context-menu" class="dropdown-menu">
                    <shiro:hasPermission name="warehouse:wareHouse:edit">
                        <li data-item="edit"><a>编辑</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:del">
                        <li data-item="delete"><a>删除</a></li>
                    </shiro:hasPermission>
                    <li data-item="action1"><a>取消</a></li>
                </ul>
            </div>
            <%--binList--%>
            <div id="binTab" class="col-sm-8 col-md-9 animated fadeInRight" style="display: none;" >


                <!-- 搜索 -->
                <div class="accordion-group">
                    <div id="collapseTwo-bin" class="accordion-body collapse">
                        <div class="accordion-inner">
                            <form:form id="searchForm-bin" modelAttribute="bin" class="form form-horizontal well clearfix">
                                <div class="col-xs-12 col-sm-6 col-md-4">
                                    <label class="label-item single-overflow pull-left" title="库房号：">库房号：</label>
                                    <form:input path="wareId" htmlEscape="false" maxlength="4"  class=" form-control"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-4">
                                    <div style="margin-top:26px">
                                        <a  id="search-bin" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
                                        <a  id="reset-bin" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
                                    </div>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>

                <!-- 工具栏 -->
                <div id="toolbar-bin">
                    <shiro:hasPermission name="warehouse:wareHouse:add">
                        <a id="add-bin" class="btn btn-primary" onclick="wareHouseForm('bin')" href="javascript:void(0)" title="货区"><i class="glyphicon glyphicon-plus"></i> 新建</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:edit">
                        <button id="edit-bin" class="btn btn-success" disabled onclick="editBin()">
                            <i class="glyphicon glyphicon-edit"></i> 修改
                        </button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:del">
                        <button id="remove-bin" class="btn btn-danger" disabled onclick="deleteAllBin()">
                            <i class="glyphicon glyphicon-remove"></i> 删除
                        </button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:import">
                        <button id="btnImport-bin" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
                        <div id="importBox-bin" class="hide">
                            <form id="importForm-bin" action="${ctx}/bin/bin/import" method="post" enctype="multipart/form-data"
                                  style="padding-left:20px;text-align:center;" ><br/>
                                <input id="uploadFile-bin" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　


                            </form>
                        </div>
                    </shiro:hasPermission>
                    <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo-bin">
                        <i class="fa fa-search"></i> 检索
                    </a>
                </div>

                <!-- 表格 -->
                <table id="binTable"   data-toolbar="#toolbar-bin"></table>

                <!-- context menu -->
                <ul id="context-menu-bin" class="dropdown-menu">
                    <shiro:hasPermission name="warehouse:wareHouse:edit">
                        <li data-item="edit"><a>编辑</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:del">
                        <li data-item="delete"><a>删除</a></li>
                    </shiro:hasPermission>
                    <li data-item="action1"><a>取消</a></li>
                </ul>
            </div>
            <%--locationList--%>
            <div id="locationTab" class="col-sm-8 col-md-9 animated fadeInRight" style="display: none;">
                <!-- 搜索 -->
                <div class="accordion-group">
                    <div id="collapseTwo-location" class="accordion-body collapse">
                        <div class="accordion-inner">
                            <form:form id="searchForm-location" modelAttribute="location" class="form form-horizontal well clearfix">
                                <div class="col-xs-12 col-sm-6 col-md-4">
                                    <label class="label-item single-overflow pull-left" title="货区号：">货区号：</label>
                                    <form:input path="binId" htmlEscape="false" maxlength="64"  class=" form-control"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-4">
                                    <div style="margin-top:26px">
                                        <a  id="search-location" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
                                        <a  id="reset-location" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
                                    </div>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>

                <!-- 工具栏 -->
                <div id="toolbar-location">
                    <shiro:hasPermission name="warehouse:wareHouse:add">
                        <a id="add-location" class="btn btn-primary" onclick="wareHouseForm('location')" href="javascript:void(0)" title="货位"><i class="glyphicon glyphicon-plus"></i> 新建</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:edit">
                        <button id="edit-location" class="btn btn-success" disabled onclick="editLocation()">
                            <i class="glyphicon glyphicon-edit"></i> 修改
                        </button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:del">
                        <button id="remove-location" class="btn btn-danger" disabled onclick="deleteAllLocation()">
                            <i class="glyphicon glyphicon-remove"></i> 删除
                        </button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:import">
                        <button id="btnImport-location" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
                        <div id="importBox-location" class="hide">
                            <form id="importForm-location" action="${ctx}/location/location/import" method="post" enctype="multipart/form-data"
                                  style="padding-left:20px;text-align:center;" ><br/>
                                <input id="uploadFile-location" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　


                            </form>
                        </div>
                    </shiro:hasPermission>
                    <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo-location">
                        <i class="fa fa-search"></i> 检索
                    </a>
                </div>

                <!-- 表格 -->
                <table id="locationTable"   data-toolbar="#toolbar-location"></table>

                <!-- context menu -->
                <ul id="context-menu-location" class="dropdown-menu">
                    <shiro:hasPermission name="warehouse:wareHouse:edit">
                        <li data-item="edit"><a>编辑</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="warehouse:wareHouse:del">
                        <li data-item="delete"><a>删除</a></li>
                    </shiro:hasPermission>
                    <li data-item="action1"><a>取消</a></li>
                </ul>
            </div>
        </div>
	</div>
	</div>
	</div>
</body>
</html>
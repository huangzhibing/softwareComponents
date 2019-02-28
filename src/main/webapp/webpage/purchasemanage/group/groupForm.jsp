<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>采购组管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">

        $(document).ready(function () {
            $("#inputForm").validate({
                submitHandler: function (form) {
                    var index = jp.loading();
                    var isAdd = '${isAdd}';
                    $.ajax({
                        url: '${ctx}/common/chkCode',
                        data: {
                            tableName: "pur_group",
                            fieldName: "groupid",
                            fieldValue: $('#groupid').val(),
                        },
                        success: function (res) {
                            if (isAdd) {
                                if (res === 'true') {
                                    form.submit();
                                } else {
                                    jp.warning("采购组编码已存在!");
                                    return false;
                                }
                            } else {
                                form.submit();
                            }
                        }

                    })
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

        function addRow(list, idx, tpl, row) {
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function () {
                $(this).val($(this).attr("data-value"));
            });
            $(list + idx).find("input[type='checkbox'], input[type='radio']").each(function () {
                var ss = $(this).attr("data-value").split(',');
                for (var i = 0; i < ss.length; i++) {
                    if ($(this).val() == ss[i]) {
                        $(this).attr("checked", "checked");
                    }
                }
            });
            $(list + idx).find(".form_datetime").each(function () {
                $(this).datetimepicker({
                    format: "YYYY-MM-DD HH:mm:ss"
                });
            });
        }

        function delRow(obj, prefix) {
            var id = $(prefix + "_id");
            var delFlag = $(prefix + "_delFlag");
            if (id.val() == "") {
                $(obj).parent().parent().remove();
            } else if (delFlag.val() == "0") {
                delFlag.val("1");
                $(obj).html("&divide;").attr("title", "撤销删除");
                $(obj).parent().parent().addClass("error");
            } else if (delFlag.val() == "1") {
                delFlag.val("0");
                $(obj).html("&times;").attr("title", "删除");
                $(obj).parent().parent().removeClass("error");
            }
        }

        //依据采购人员的编码设置采购的用户名值
        function setUsername(idx, value) {
            var id = idx.split("_")[0];
            var ids = "#" + id + "_buyername";
            $(ids).val(value);
        }

        //在具体的使用中需替换'#applyDetailList', applyDetailRowIdx, applyDetailTpl这三个值
        function setOtherValue(items, obj, targetField, targetId, nam, labNam) {
            var da = items;
            for (var i = 1; i < da.length; i++) {
                //增加一行
                addRow('#groupItemClassList', groupItemClassRowIdx, groupItemClassTpl);
                //为所增加的行进行赋值
                addRowModify('#groupItemClassList', groupItemClassRowIdx, groupItemClassTpl, da[i], obj, targetField, targetId, nam, labNam);
                groupItemClassRowIdx = groupItemClassRowIdx + 1;
            }
        }

        function addRowModify(list, idx, tpl, row, obj, targetField, targetId, nam, labNam) {
            //给gridselect-modify1标签的显示input标签赋值，后台所传显示
            $(list + idx + "_" + obj + "Names").val(row[labNam]);
            $(list + idx + "_" + obj + "Names").trigger("change");
            //为gridselect-modify1隐含的标签赋值,提交时传给后台
            $(list + idx + "_" + obj + "Id").val(row[nam]);
            $(list + idx + "_" + obj + "Id").trigger("change");
            //table标签的其他字段赋值
            //给各标签赋值
            for (var i = 0; i < targetField.length; i++) {
                //获取标签id
                var ind = targetField[i];
                //获取对象所填充的属性
                var tId = targetId[i];
                $(list + idx + "_" + tId).val(row[ind]);
                $(list + idx + "_" + tId).trigger("change");
            }
        }


    </script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <a class="panelButton" href="${ctx}/group/group"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="group" action="${ctx}/group/group/save" method="post"
                               class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message content="${message}"/>


                        <c:if test="${isAdd!='true'}">
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>采购组代码：</label>
                                <div class="col-sm-10">
                                    <form:input path="groupid" htmlEscape="false" class="form-control required"
                                                readonly="true"/>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${isAdd=='true'}">
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>采购组代码：</label>
                                <div class="col-sm-10">
                                    <form:input path="groupid" htmlEscape="false" class="form-control required"/>
                                </div>
                            </div>
                        </c:if>

                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>采购组名称：</label>
                            <div class="col-sm-10">
                                <form:input path="groupname" htmlEscape="false" class="form-control required"/>
                            </div>
                        </div>


                        <div class="tabs-container">
                            <ul class="nav nav-tabs">
                                <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">采购员：</a>
                                </li>
                                <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">物料类：</a>
                                </li>
                                <li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">部门：</a>
                                </li>
                                <li class=""><a data-toggle="tab" href="#tab-4" aria-expanded="false">供应商：</a>
                                </li>
                                <li class=""><a data-toggle="tab" href="#tab-5" aria-expanded="false">仓库：</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div id="tab-1" class="tab-pane fade in  active">
                                    <a class="btn btn-white btn-sm"
                                       onclick="addRow('#groupBuyerList', groupBuyerRowIdx, groupBuyerTpl);groupBuyerRowIdx = groupBuyerRowIdx + 1;"
                                       title="新增"><i class="fa fa-plus"></i> 新增</a>
                                    <table class="table table-striped table-bordered table-condensed">
                                        <thead>
                                        <tr>
                                            <th class="hide"></th>
                                            <th>采购员编码</th>
                                            <th>采购员名称</th>
                                            <th>采购员电话</th>
                                            <th>采购员传真</th>
                                            <th>送货地址</th>
                                            <th>采购员级别</th>
                                            <th width="10">&nbsp;</th>
                                        </tr>
                                        </thead>
                                        <tbody id="groupBuyerList">
                                        </tbody>
                                    </table>
                                    <script type="text/template" id="groupBuyerTpl">//<!--
				<tr id="groupBuyerList{{idx}}">
					<td class="hide">
						<input id="groupBuyerList{{idx}}_id" name="groupBuyerList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="groupBuyerList{{idx}}_delFlag" name="groupBuyerList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td  class="max-width-250">
						<sys:userselect-update id="groupBuyerList{{idx}}_user" name="groupBuyerList[{{idx}}].user.id" value="{{row.user.id}}" labelName="groupBuyerList{{idx}}.user.no" labelValue="{{row.user.no}}"
							    cssClass="form-control "/>
					</td>
					
					
					<td>
						<input id="groupBuyerList{{idx}}_buyername" name="groupBuyerList[{{idx}}].buyername"
						type="text" value="{{row.buyername}}"    class="form-control" readonly="true" />
					</td>
					<td>
						<input id="groupBuyerList{{idx}}_buyerPhoneNum" name="groupBuyerList[{{idx}}].buyerphonenum" type="text" value="{{row.buyerphonenum}}"    class="form-control "/>
					</td>
					<td>
						<input id="groupBuyerList{{idx}}_buyerFaxNum" name="groupBuyerList[{{idx}}].buyerfaxnum" type="text" value="{{row.buyerfaxnum}}"    class="form-control "/>
					</td>
                    <td>
						<input id="groupBuyerList{{idx}}_deliveryAddress" name="groupBuyerList[{{idx}}].deliveryaddress" type="text" value="{{row.deliveryaddress}}"    class="form-control "/>
					</td>
					<td>
						<select id="groupBuyerList{{idx}}_buyerlevel" name="groupBuyerList[{{idx}}].buyerlevel" data-value="{{row.buyerlevel}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('buyerLevel')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#groupBuyerList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
                                    </script>
                                    <script type="text/javascript">
                                        var groupBuyerRowIdx = 0,
                                            groupBuyerTpl = $("#groupBuyerTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
                                        $(document).ready(function () {
                                            var data = ${fns:toJson(group.groupBuyerList)};
                                            for (var i = 0; i < data.length; i++) {
                                                //console.log(data[i]);
                                                addRow('#groupBuyerList', groupBuyerRowIdx, groupBuyerTpl, data[i]);
                                                groupBuyerRowIdx = groupBuyerRowIdx + 1;
                                            }
                                        });
                                    </script>
                                </div>
                                <div id="tab-2" class="tab-pane fade">
                                    <a class="btn btn-white btn-sm"
                                       onclick="addRow('#groupItemClassList', groupItemClassRowIdx, groupItemClassTpl);groupItemClassRowIdx = groupItemClassRowIdx + 1;"
                                       title="新增"><i class="fa fa-plus"></i> 新增</a>
                                    <table class="table table-striped table-bordered table-condensed">
                                        <thead>
                                        <tr>
                                            <th class="hide"></th>
                                            <th>物料类代码</th>
                                            <th>物料类名称</th>
                                            <th width="10">&nbsp;</th>
                                        </tr>
                                        </thead>
                                        <tbody id="groupItemClassList">
                                        </tbody>
                                    </table>
                                    <script type="text/template" id="groupItemClassTpl">//<!--
				<tr id="groupItemClassList{{idx}}">
					<td class="hide">
						<input id="groupItemClassList{{idx}}_id" name="groupItemClassList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="groupItemClassList{{idx}}_delFlag" name="groupItemClassList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<sys:gridselect-purmul url="${ctx}/item/item/data2" id="groupItemClassList{{idx}}_item"
						name="groupItemClassList[{{idx}}].item.id" value="{{row.item.id}}" labelName="groupItemClassList{{idx}}.item.code" labelValue="{{row.item.code}}"
							 title="选择物料类代码" cssClass="form-control  " targetId="itemclassname" targetField="name" fieldLabels="物料类代码|物料类名称" isMultiSelected="true" fieldKeys="code|name" searchLabels="物料类代码|物料类名称" searchKeys="code|name" ></sys:gridselect-purmul>
					</td>
					
					
					<td>
						<input id="groupItemClassList{{idx}}_itemclassname" name="groupItemClassList[{{idx}}].itemclassname" type="text" value="{{row.itemclassname}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#groupItemClassList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
                                    </script>
                                    <script type="text/javascript">
                                        var groupItemClassRowIdx = 0,
                                            groupItemClassTpl = $("#groupItemClassTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
                                        $(document).ready(function () {
                                            var data = ${fns:toJson(group.groupItemClassList)};
                                            for (var i = 0; i < data.length; i++) {
                                                //console.log(data[i]);
                                                addRow('#groupItemClassList', groupItemClassRowIdx, groupItemClassTpl, data[i]);
                                                groupItemClassRowIdx = groupItemClassRowIdx + 1;
                                            }
                                        });
                                    </script>
                                </div>
                                <div id="tab-3" class="tab-pane fade">
                                    <a class="btn btn-white btn-sm"
                                       onclick="addRow('#groupOrgzList', groupOrgzRowIdx, groupOrgzTpl);groupOrgzRowIdx = groupOrgzRowIdx + 1;"
                                       title="新增"><i class="fa fa-plus"></i> 新增</a>
                                    <table class="table table-striped table-bordered table-condensed">
                                        <thead>
                                        <tr>
                                            <th class="hide"></th>
                                            <th>部门代码</th>
                                            <th>部门名称</th>
                                            <th width="10">&nbsp;</th>
                                        </tr>
                                        </thead>
                                        <tbody id="groupOrgzList">
                                        </tbody>
                                    </table>
                                    <script type="text/template" id="groupOrgzTpl">//<!--
				<tr id="groupOrgzList{{idx}}">
					<td class="hide">
						<input id="groupOrgzList{{idx}}_id" name="groupOrgzList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="groupOrgzList{{idx}}_delFlag" name="groupOrgzList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td  class="max-width-250">
						<sys:treeselect-update targetId="groupOrgzList{{idx}}_orgzname" targetField="code" id="groupOrgzList{{idx}}_office" name="groupOrgzList[{{idx}}].office.id" 
							value="{{row.office.id}}" labelName="groupOrgzList{{idx}}.office.code " labelValue="{{row.office.code}}" title="部门" url="/sys/office/treeData?type=2" cssClass="form-control  " 
							allowClear="true" notAllowSelectParent="true"/>

					</td>
					
					
					<td>
						<input id="groupOrgzList{{idx}}_orgzname" name="groupOrgzList[{{idx}}].orgzname" type="text" value="{{row.orgzname}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#groupOrgzList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
                                    </script>
                                    <script type="text/javascript">
                                        var groupOrgzRowIdx = 0,
                                            groupOrgzTpl = $("#groupOrgzTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
                                        $(document).ready(function () {
                                            var data = ${fns:toJson(group.groupOrgzList)};
                                            for (var i = 0; i < data.length; i++) {
                                                //console.log(data[i]);
                                                addRow('#groupOrgzList', groupOrgzRowIdx, groupOrgzTpl, data[i]);
                                                groupOrgzRowIdx = groupOrgzRowIdx + 1;
                                            }
                                        });
                                    </script>
                                </div>
                                <div id="tab-4" class="tab-pane fade">
                                    <a class="btn btn-white btn-sm"
                                       onclick="addRow('#groupSupList', groupSupRowIdx, groupSupTpl);groupSupRowIdx = groupSupRowIdx + 1;"
                                       title="新增"><i class="fa fa-plus"></i> 新增</a>
                                    <table class="table table-striped table-bordered table-condensed">
                                        <thead>
                                        <tr>
                                            <th class="hide"></th>
                                            <th><font color="red">*</font>供应商代码</th>
                                            <th>供应商</th>
                                            <th width="10">&nbsp;</th>
                                        </tr>
                                        </thead>
                                        <tbody id="groupSupList">
                                        </tbody>
                                    </table>
                                    <script type="text/template" id="groupSupTpl">//<!--
				<tr id="groupSupList{{idx}}">
					<td class="hide">
						<input id="groupSupList{{idx}}_id" name="groupSupList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="groupSupList{{idx}}_delFlag" name="groupSupList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<%--<td>--%>
						<%--<sys:gridselect-modify url="${ctx}/suptype/supType/data" id="groupSupList{{idx}}_supid" name="groupSupList[{{idx}}].supid.id" value="{{row.supid.id}}" labelName="groupSupList{{idx}}.supid.suptypeId" labelValue="{{row.supid.suptypeId}}"--%>
							 <%--title="选择供应商代码" cssClass="form-control  required" targetId="groupSupList{{idx}}_supname" targetField="suptypeName" fieldLabels="供应商代码|供应商名称" fieldKeys="suptypeId|suptypeName" searchLabels="供应商代码|供应商名称" searchKeys="suptypeId|suptypeName" ></sys:gridselect-modify>--%>
					<%--</td>--%>

										<%--<td>--%>
						<%--<sys:gridselect-modify url="${ctx}/suptype/supType/data" id="groupSupList{{idx}}_supid" name="groupSupList[{{idx}}].supid.id" value="{{row.supid.id}}" labelName="groupSupList{{idx}}.supid.suptypeId" labelValue="{{row.supid.suptypeId}}"--%>
							 <%--title="选择供应商代码" cssClass="form-control  required" targetId="groupSupList{{idx}}_supname" targetField="suptypeName" fieldLabels="供应商代码|供应商名称" fieldKeys="suptypeId|suptypeName" searchLabels="供应商代码|供应商名称" searchKeys="suptypeId|suptypeName" ></sys:gridselect-modify>--%>
					<%--</td>--%>
                    <td>
					<sys:gridselect-pursup url="${ctx}/account/account/data" id="groupSupList{{idx}}_account"
					name="groupSupList[{{idx}}].account.id" value="{{row.account.id}}"
					labelName="groupSupList{{idx}}.account.id" labelValue="{{row.account.accountCode}}"
										   title="选择供应商编号" cssClass="form-control required "
										   fieldLabels="供应商编码|供应商名称" fieldKeys="accountCode|accountName"
										   searchLabels="供应商编码|供应商名称" searchKeys="accountCode|accountName"
										   targetId="groupSupList{{idx}}_supname" targetField="accountName"
										   ></sys:gridselect-pursup>
                        </td>















					
					<td>
						<input id="groupSupList{{idx}}_supname" name="groupSupList[{{idx}}].supname" type="text" value="{{row.supname}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#groupSupList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
                                    </script>
                                    <script type="text/javascript">
                                        var groupSupRowIdx = 0,
                                            groupSupTpl = $("#groupSupTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
                                        $(document).ready(function () {
                                            var data = ${fns:toJson(group.groupSupList)};
                                            for (var i = 0; i < data.length; i++) {
                                                addRow('#groupSupList', groupSupRowIdx, groupSupTpl, data[i]);
                                                groupSupRowIdx = groupSupRowIdx + 1;
                                            }
                                        });
                                    </script>
                                </div>
                                <div id="tab-5" class="tab-pane fade">
                                    <a class="btn btn-white btn-sm"
                                       onclick="addRow('#groupWareList', groupWareRowIdx, groupWareTpl);groupWareRowIdx = groupWareRowIdx + 1;"
                                       title="新增"><i class="fa fa-plus"></i> 新增</a>
                                    <table class="table table-striped table-bordered table-condensed">
                                        <thead>
                                        <tr>
                                            <th class="hide"></th>
                                            <th><font color="red">*</font>仓库代码</th>
                                            <th>仓库名称</th>
                                            <th width="10">&nbsp;</th>
                                        </tr>
                                        </thead>
                                        <tbody id="groupWareList">
                                        </tbody>
                                    </table>
                                    <script type="text/template" id="groupWareTpl">//<!--
				<tr id="groupWareList{{idx}}">
					<td class="hide">
						<input id="groupWareList{{idx}}_id" name="groupWareList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="groupWareList{{idx}}_delFlag" name="groupWareList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<sys:gridselect-modify url="${ctx}/warehouse/wareHouse/data" id="groupWareList{{idx}}_warehouse" name="groupWareList[{{idx}}].warehouse.id" value="{{row.warehouse.id}}" labelName="groupWareList{{idx}}.warehouse.wareID" labelValue="{{row.warehouse.wareID}}"
							 title="选择仓库代码" cssClass="form-control  required" targetId="groupWareList{{idx}}_warename" targetField="wareName" fieldLabels="仓库代码|仓库名称" fieldKeys="wareID|wareName" searchLabels="仓库代码|仓库名称" searchKeys="wareID|wareName" ></sys:gridselect-modify>
					</td>
					
					
					<td>
						<input id="groupWareList{{idx}}_warename" name="groupWareList[{{idx}}].warename" type="text" value="{{row.warename}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#groupWareList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
                                    </script>
                                    <script type="text/javascript">
                                        var groupWareRowIdx = 0,
                                            groupWareTpl = $("#groupWareTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
                                        $(document).ready(function () {
                                            var data = ${fns:toJson(group.groupWareList)};
                                            for (var i = 0; i < data.length; i++) {
                                                addRow('#groupWareList', groupWareRowIdx, groupWareTpl, data[i]);
                                                groupWareRowIdx = groupWareRowIdx + 1;
                                            }
                                        });
                                    </script>
                                </div>
                            </div>
                        </div>
                        <c:if test="${fns:hasPermission('group:group:edit') || isAdd}">
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
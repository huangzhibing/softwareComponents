<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>供应商联系人管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">

        $(document).ready(function () {
            $("#inputForm").validate({
                submitHandler: function (form) {

                 //changed by ckw at 2018-7-31 11:22
                    var tmpCount=0;

                    //统计状态为“生效状态”、“未删除”的记录个数

                    for (var i = 0; i < linkManRowIdx; i++) {

                        //遍历获取子表的state，delflag属性
                       var row_id_state= "#linkManList"+i+"_state";
                       var row_id_defflag="#linkManList"+i+"_delFlag";
                       var row_id_state_value=$(row_id_state).val();
                       var row_id_defflag_value=$(row_id_defflag).val();


                       if(row_id_state_value==="生效状态" && row_id_defflag_value==="0"){
                           tmpCount=tmpCount+1;
                       }
                    }

                    //最多允许子表中存在一个生效的联系人记录
                    if(tmpCount>1){
                        jp.alert("最多只能有一个联系人处于生效状态！");
                    }else {
                     // 提交数据
                        jp.loading();
                       form.submit();
                    }
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
    </script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <a class="panelButton" href="${ctx}/linkman/paccount"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="paccount" action="${ctx}/linkman/paccount/save"
                               method="post" class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message content="${message}"/>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">企业编码：</label>
                            <div class="col-sm-3">
                                <form:input path="accountCode" htmlEscape="false" readonly="true"
                                            class="form-control "/>
                            </div>

                            <label class="col-sm-2 control-label">企业名称：</label>
                            <div class="col-sm-3">
                                <form:input path="accountName" htmlEscape="false" readonly="true"
                                            class="form-control "/>
                            </div>
                        </div>






                        <div class="form-group">
                            <label class="col-sm-2 control-label">地区编码：</label>
                            <div class="col-sm-3">
                                <form:input path="areaCode" htmlEscape="false" readonly="true"
                                            class="form-control "/>
                            </div>
                            <label class="col-sm-2 control-label">地区名称：</label>
                            <div class="col-sm-3">
                                <form:input path="areaName" htmlEscape="false" readonly="true" class="form-control "/>
                            </div>
                        </div>





                        <div class="form-group">
                            <label class="col-sm-2 control-label">性质：</label>
                            <div class="col-sm-3">
                                <form:input path="accountProp" htmlEscape="false" readonly="true"
                                            class="form-control "/>
                            </div>

                            <label class="col-sm-2 control-label">法人：</label>
                            <div class="col-sm-3">
                                <form:input path="accountMgr" htmlEscape="false" readonly="true" class="form-control "/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">类别编码：</label>
                            <div class="col-sm-3">
                                <form:input path="subTypeCode" htmlEscape="false" readonly="true"
                                            class="form-control "/>
                            </div>

                            <label class="col-sm-2 control-label">类别名称：</label>
                            <div class="col-sm-3">
                                <form:input path="subTypeName" htmlEscape="false" readonly="true"
                                            class="form-control "/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">联系地址：</label>
                            <div class="col-sm-3">
                                <form:input path="accountAddr" htmlEscape="false" readonly="true"
                                            class="form-control "/>
                            </div>

                            <label class="col-sm-2 control-label">邮编：</label>
                            <div class="col-sm-3">
                                <form:input path="postCode" htmlEscape="false" readonly="true" class="form-control "/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">上级部门：</label>
                            <div class="col-sm-8">
                                <form:input path="supHigherUp" htmlEscape="false" readonly="true"
                                            class="form-control "/>
                            </div>
                        </div>

                        <div class="tabs-container">
                            <ul class="nav nav-tabs">
                                <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">联系人档案：</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div id="tab-1" class="tab-pane fade in  active">
                                    <a class="btn btn-white btn-sm"
                                       onclick="addRow('#linkManList', linkManRowIdx, linkManTpl);linkManRowIdx = linkManRowIdx + 1;"
                                       title="新增"><i class="fa fa-plus"></i> 新增</a>
                                    <table class="table table-striped table-bordered table-condensed">
                                        <thead>
                                        <tr>
                                            <th class="hide"></th>
                                            <th><font color="red">*</font>联系人编号</th>
                                            <th><font color="red">*</font>联系人姓名</th>
                                            <th>性别</th>
                                            <th>级别</th>
                                            <th>职位</th>
                                            <th>电话</th>
                                            <th>手机</th>
                                            <th>mail</th>
                                            <th><font color="red">*</font> 状态</th>
                                            <th width="10">&nbsp;</th>
                                        </tr>
                                        </thead>
                                        <tbody id="linkManList">
                                        </tbody>
                                    </table>
                                    <script type="text/template" id="linkManTpl">//<!--
				<tr id="linkManList{{idx}}">
					<td class="hide">
						<input id="linkManList{{idx}}_id" name="linkManList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="linkManList{{idx}}_delFlag" name="linkManList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="linkManList{{idx}}_linkCode" name="linkManList[{{idx}}].linkCode" type="text" value="{{row.linkCode}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="linkManList{{idx}}_linkName" name="linkManList[{{idx}}].linkName" type="text" value="{{row.linkName}}"    class="form-control required"/>
					</td>
					
					
					<td width="80">
						<select id="linkManList{{idx}}_linkSex" name="linkManList[{{idx}}].linkSex" data-value="{{row.linkSex}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('sex')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<input id="linkManList{{idx}}_linkLevel" name="linkManList[{{idx}}].linkLevel" type="text" value="{{row.linkLevel}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="linkManList{{idx}}_linkPosition" name="linkManList[{{idx}}].linkPosition" type="text" value="{{row.linkPosition}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="linkManList{{idx}}_linkTel" name="linkManList[{{idx}}].linkTel" type="text" value="{{row.linkTel}}"    class="form-control  isPhone"/>
					</td>
					
					
					<td>
						<input id="linkManList{{idx}}_linkPhone" name="linkManList[{{idx}}].linkPhone" type="text" value="{{row.linkPhone}}"    class="form-control  isMobile"/>
					</td>
					
					
					<td>
						<input id="linkManList{{idx}}_linkMail" name="linkManList[{{idx}}].linkMail" type="text" value="{{row.linkMail}}"    class="form-control  email"/>
					</td>
					
					
					<td width="150">
					<select id="linkManList{{idx}}_state" name="linkManList[{{idx}}].state"
					data-value="{{row.state}}" class="form-control required">

							<c:forEach items="${fns:getDictList('isVariable')}" var="varFlag">
								<option value="${varFlag.value}">${varFlag.label}</option>
							</c:forEach>

					</select>
					</td>



					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#linkManList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
                                    </script>
                                    <script type="text/javascript">
                                        var linkManRowIdx = 0,
                                            linkManTpl = $("#linkManTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
                                        $(document).ready(function () {
                                            var data = ${fns:toJson(paccount.linkManList)};
                                            for (var i = 0; i < data.length; i++) {
                                                addRow('#linkManList', linkManRowIdx, linkManTpl, data[i]);
                                                linkManRowIdx = linkManRowIdx + 1;
                                            }
                                        });
                                    </script>
                                </div>
                            </div>
                        </div>
                        <c:if test="${fns:hasPermission('linkman:paccount:edit') || isAdd}">
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
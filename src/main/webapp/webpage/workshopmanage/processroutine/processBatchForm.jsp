<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车间加工路线管理管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
                    //提交提示
                    if($('#assignedState').val()=="C"){
                        jp.confirm("是否提交此加工路线单？",function () {
                            jp.loading();
                            form.submit();
                        },function () {
                            return false;
                        });
                    }else {
                        jp.loading();
                        form.submit();
                    }
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
			
	        $('#planBDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#planEDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#makeDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#confirmDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#deliveryDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
		});
		
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
			$(list+idx).find(".form_datetime").each(function(){
				 $(this).datetimepicker({
					 format: "YYYY-MM-DD"
			    });
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}


        /*
       *保存按钮事件
       */
        function button_save() {
            $('#assignedState').val('P');//修改SfcProcessBatch表中assignedState状态

            /* 修改ProcessRoutine表字段*/
            $('input[name$=".billState"]').each(function(){
                $(this).val('P');//更改工艺路线表状态
            });


            $('#inputForm').submit();//提交表单
        }

        /**
         * 提交按钮事件
         */
        function button_submit() {
            $('#assignedState').val('C');//修改SfcProcessBatch表中assignedState状态

            /* 修改ProcessRoutine表字段*/
            $('input[name$=".billState"]').each(function(){
                $(this).val('C');//更改工艺路线表状态
            });

            $('#inputForm').submit();//提交表单
        }





        /**
         *  批量设置工作中心按钮事件
         */
        function button_center(){
            console.log(getSelectionsPre());
            if(getSelectionsPre().length<1){
                jp.warning("请勾选需要批量设置的加工路线！");
                return;
            }
            var idsPre=getSelectionsPre();
            top.layer.open({
                type: 2,
                area: ['800px', '500px'],
                title:"选择工作中心",
                auto:true,
                name:'friend',
                content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/workcenter/workCenter/data")+"&fieldLabels="+encodeURIComponent("工作中心代码|工作中心名称")+"&fieldKeys="+encodeURIComponent("centerCode|centerName")+"&searchLabels="+encodeURIComponent("工作中心代码|工作中心名称")+"&searchKeys="+encodeURIComponent("centerCode|centerName")+"&isMultiSelected=false",
                btn: ['确定', '关闭'],
                yes: function(index, layero){
                    var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    var items = iframeWin.getSelections();
                    if(items == ""){
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    console.log(items);
                    for(var i=0;i<idsPre.length;i++){
                        $(idsPre[i]+"_centerCodeName").val(items[0].centerCode);
                    }

                    top.layer.close(index);//关闭对话框。
                    jp.success("批量设置成功！");
                },
                cancel: function(index){
                }
            });


        }

        /**
		 * 批量设置负责人按钮事件
         */
        function button_charge(){
            if(getSelectionsPre().length<1){
                jp.warning("请勾选需要批量设置的加工路线！");
                return;
            }
            var idsPre=getSelectionsPre();

            jp.openUserSelectDialog(false,function(ids, names){
                for(var i=0;i<idsPre.length;i++) {
                    $(idsPre[i] + "_personInchargeId").val(ids.replace(/u_/ig, ""));
                    $(idsPre[i] + "_personInchargeName").val(names);
                    //$(idsPre[i] + "_personInchargeName").focus();
                }
            });
            jp.success("批量设置成功！");
		}


        /**
         *  批量设置计划班组按钮事件
         */
        function button_team(){
            console.log(getSelectionsPre());
            if(getSelectionsPre().length<1){
                jp.warning("请勾选需要批量设置的加工路线！");
                return;
            }
            var idsPre=getSelectionsPre();
            top.layer.open({
                type: 2,
                area: ['800px', '500px'],
                title:"选择计划班组",
                auto:true,
                name:'friend',
                content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/team/team/data")+"&fieldLabels="+encodeURIComponent("班组代码|班组名称")+"&fieldKeys="+encodeURIComponent("teamCode|teamName")+"&searchLabels="+encodeURIComponent("班组代码|班组名称")+"&searchKeys="+encodeURIComponent("teamCode|teamName")+"&isMultiSelected=false",
                btn: ['确定', '关闭'],
                yes: function(index, layero){
                    var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    var items = iframeWin.getSelections();
                    if(items == ""){
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    console.log(items);
                    for(var i=0;i<idsPre.length;i++){
                        $(idsPre[i]+"_teamCodeName").val(items[0].teamCode);
                    }

                    top.layer.close(index);//关闭对话框。
                    jp.success("批量设置成功！");
                },
                cancel: function(index){
                }
            });


        }

        /**
         *  批量设置班次按钮事件
         */
        function button_shiftname(){
            console.log(getSelectionsPre());
            if(getSelectionsPre().length<1){
                jp.warning("请勾选需要批量设置的加工路线！");
                return;
            }
            var idsPre=getSelectionsPre();
            top.layer.open({
                type: 2,
                area: ['800px', '500px'],
                title:"选择班次",
                auto:true,
                name:'friend',
                content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/shiftdef/shiftDef/data")+"&fieldLabels="+encodeURIComponent("班次名称|开始时间|结束时间")+"&fieldKeys="+encodeURIComponent("shiftname|begintime|endtime")+"&searchLabels="+encodeURIComponent("班次名称|开始时间|结束时间")+"&searchKeys="+encodeURIComponent("shiftname|begintime|endtime")+"&isMultiSelected=false",
                btn: ['确定', '关闭'],
                yes: function(index, layero){
                    var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    var items = iframeWin.getSelections();
                    if(items == ""){
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    console.log(items);
                    for(var i=0;i<idsPre.length;i++){
                        $(idsPre[i]+"_shiftnameName").val(items[0].shiftname);
                    }

                    top.layer.close(index);//关闭对话框。
                    jp.success("批量设置成功！");
                },
                cancel: function(index){
                }
            });


        }




        //点击左上角checkbox选择框全选事件
        function allCheck(){
            //建议用name属性值操作，因为id属性在每一个页面中默认是唯一的，而name属性则可以取到属性值相同的所有
            var nn = $("#allboxs").is(":checked"); //判断th中的checkbox是否被选中，如果被选中则nn为true，反之为false
            if(nn == true) {
                var namebox = $("input[name^='boxs']");  //获取name值为boxs的所有input
                for(i = 0; i < namebox.length; i++) {
                    namebox[i].checked=true;    //js操作选中checkbox
                }
            }
            if(nn == false) {
                var namebox = $("input[name^='boxs']");
                for(i = 0; i < namebox.length; i++) {
                    namebox[i].checked=false;
                }
            }
        }
        //获取选中行的ID前缀（包含了#）
        function getSelectionsPre() {
            var list=new Array();
            var $allBoxes=$("input[name='boxs']");
            for(var i=0;i<$allBoxes.length;i++){
                if($allBoxes[i].checked==true){ //筛选出选中的box
                    list.push("#"+$allBoxes[i].id.split("_")[0])
                }
            }
            return list;
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
				<a class="panelButton" href="${ctx}/processroutine/processBatch"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="processBatch" action="${ctx}/processroutine/processBatch/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="assignedState"/>
		<sys:message content="${message}"/>	

				<div class="form-group">
					<label class="col-sm-2 control-label">车间作业计划号：</label>
					<div class="col-sm-3">
						<form:input path="processBillNo" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">批次序号：</label>
					<div class="col-sm-3">
						<form:input path="batchNo" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">产品编码：</label>
					<div class="col-sm-3">
						<form:input path="prodCode" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">产品名称：</label>
					<div class="col-sm-3">
						<form:input path="prodName" htmlEscape="false"   readonly="true" class="form-control "/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">计划生产数量：</label>
					<div class="col-sm-3">
						<form:input path="planQty" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">计划生产日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
						<div class='input-group form_datetime' id='planBDate'>
							<input type='text'  name="planBDate" class="form-control " readonly="true" value="<fmt:formatDate value="${processBatch.planBDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
						</div>
						</p>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-8">
						<form:textarea path="remarks" htmlEscape="false" rows="2"  readonly="true"  class="form-control "/>
					</div>
				</div>
<%--
				<div class="form-group">
					<label class="col-sm-2 control-label">实际生产数量：</label>
					<div class="col-sm-10">
						<form:input path="realQty" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">实际生产日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='planEDate'>
			                    <input type='text'  name="planEDate" class="form-control "  value="<fmt:formatDate value="${processBatch.planEDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-10">
						<form:input path="billState" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">派工状态：</label>
					<div class="col-sm-10">
						<form:input path="assignedState" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制定人：</label>
					<div class="col-sm-10">
						<form:input path="makeID" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">制定日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='makeDate'>
			                    <input type='text'  name="makeDate" class="form-control "  value="<fmt:formatDate value="${processBatch.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">提交人：</label>
					<div class="col-sm-10">
						<form:input path="confirmPid" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">提交日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='confirmDate'>
			                    <input type='text'  name="confirmDate" class="form-control "  value="<fmt:formatDate value="${processBatch.confirmDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">下达人：</label>
					<div class="col-sm-10">
						<form:input path="deliveryPid" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">下达日期：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='deliveryDate'>
			                    <input type='text'  name="deliveryDate" class="form-control "  value="<fmt:formatDate value="${processBatch.deliveryDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
--%>

		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">车间加工路线表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
					<div style=" padding-left: 1%; ">批量设置： <a class="btn btn-white btn-sm" onclick="button_center()" title="工作中心"><i class="fa fa-share"></i> 工作中心</a> <a class="btn btn-white btn-sm" onclick="button_charge()" title="负责人"><i class="fa fa-share"></i> 负责人</a> <a class="btn btn-white btn-sm" onclick="button_team()" title="班组"><i class="fa fa-share"></i> 计划班组</a> <a class="btn btn-white btn-sm" onclick="button_shiftname()" title="班次"><i class="fa fa-share"></i> 班次</a></div>
		<div class="table-responsive" style="min-height: 250px">
				<table class="table table-striped table-bordered table-condensed " style="min-width: 200%">
					<thead>
					<tr>
						<th class="hide"></th>
						<th>
							<input id="allboxs" onclick="allCheck()" type="checkbox"/>
						</th>
						<th>加工路线单号</th>
						<th>生产序号</th>
						<th>工艺编码</th>
						<th>工艺名称</th>
						<th><font color="red">*</font>工作中心代码</th>
						<th>计划数量</th>
						<th>生产日期</th>
						<th><font color="red">*</font>负责人</th>
						<th><font color="red">*</font>计划班组</th>
						<th><font color="red">*</font>班次</th>
						<th>计划工时</th>
						<th>备注信息</th>

					</tr>
					</thead>
					<tbody id="processRoutineList">
					</tbody>
				</table>
		</div>
				<script type="text/template" id="processRoutineTpl">//<!--
				<tr id="processRoutineList{{idx}}">
					<td class="hide">
						<input id="processRoutineList{{idx}}_id" name="processRoutineList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="processRoutineList{{idx}}_delFlag" name="processRoutineList[{{idx}}].delFlag" type="hidden" value="0"/>

						<input id="processRoutineList{{idx}}_billState" name="processRoutineList[{{idx}}].billState" type="hidden" value="{{row.billState}}" />
					</td>

					<td>
                        <input type="checkbox" name="boxs" id="processRoutineList{{idx}}_checkbox" data-value=""/>
                    </td>

					<td>
						<input id="processRoutineList{{idx}}_routineBillNo" name="processRoutineList[{{idx}}].routineBillNo" readonly="true" type="text" value="{{row.routineBillNo}}"    class="form-control "/>
					</td>


					<td>
						<input id="processRoutineList{{idx}}_produceNo" name="processRoutineList[{{idx}}].produceNo" type="text" value="{{row.produceNo}}" readonly="true"   class="form-control "/>
					</td>


					<td>
						<input id="processRoutineList{{idx}}_routingCode" name="processRoutineList[{{idx}}].routingCode" type="text" value="{{row.routingCode}}"   readonly="true" class="form-control "/>
					</td>

					<td>
						<input id="processRoutineList{{idx}}_routingName" name="processRoutineList[{{idx}}].routingName" type="text" value="{{row.routingName}}"  readonly="true"  class="form-control "/>
					</td>




					<td>
						<%--<input id="processRoutineList{{idx}}_centerCode" name="processRoutineList[{{idx}}].centerCode" type="text" value="{{row.centerCode}}"    class="form-control "/>--%>
						<sys:gridselect url="${ctx}/workcenter/workCenter/data" id="processRoutineList{{idx}}_centerCode" name="processRoutineList[{{idx}}].centerId" value="{{row.centerCode}}" labelName="processRoutineList[{{idx}}].centerCode" labelValue="{{row.centerCode}}"
							 title="选择工作中心代码" cssClass="form-control required" fieldLabels="工作中心代码|工作中心名称" fieldKeys="centerCode|centerName" searchLabels="工作中心代码|工作中心名称" searchKeys="centerCode|centerName" ></sys:gridselect>


					</td>






					<td>
						<input id="processRoutineList{{idx}}_planQty" name="processRoutineList[{{idx}}].planQty" type="text" value="{{row.planQty}}"  readonly="true"  class="form-control "/>
					</td>


					<td>
						<div class='input-group form_datetime' id="processRoutineList{{idx}}_planBData">
		                    <input type='text'  name="processRoutineList[{{idx}}].planBData" class="form-control "  readonly="true" value="{{row.planBData}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>
					</td>










					<td>
						<%--<input id="processRoutineList{{idx}}_personIncharge" name="processRoutineList[{{idx}}].personIncharge" type="text" value="{{row.personIncharge}}"    class="form-control required"/>--%>
						<sys:userselect id="processRoutineList{{idx}}_personIncharge" name="processRoutineList[{{idx}}].personInchargeId" value="{{row.personIncharge}}" labelName="processRoutineList[{{idx}}].personIncharge" labelValue="{{row.personIncharge}}"
							    cssClass="form-control required"/>
					</td>Id


					<td>
						<%--<input id="processRoutineList{{idx}}_teamCode" name="processRoutineList[{{idx}}].teamCode" type="text" value="{{row.teamCode}}"    class="form-control required"/>--%>
						<sys:gridselect url="${ctx}/team/team/data" id="processRoutineList{{idx}}_teamCode" name="processRoutineList[{{idx}}].teamId" value="{{row.teamCode}}" labelName="processRoutineList[{{idx}}].teamCode" labelValue="{{row.teamCode}}"
							 title="选择计划班组" cssClass="form-control required" fieldLabels="班组代码|班组名称" fieldKeys="teamCode|teamName" searchLabels="班组代码|班组名称" searchKeys="teamCode|teamName" ></sys:gridselect>
					</td>





					<td>
						<%--<input id="processRoutineList{{idx}}_shiftName" name="processRoutineList[{{idx}}].shiftName" type="text" value="{{row.shiftName}}"    class="form-control required "/>--%>
							<sys:gridselect url="${ctx}/shiftdef/shiftDef/data" id="processRoutineList{{idx}}_shiftname"  value="{{row.shiftname}}" name="processRoutineList[{{idx}}].shiftid"   labelName="processRoutineList[{{idx}}].shiftname" labelValue="{{row.shiftname}}"
							 title="选择班次" cssClass="form-control required" fieldLabels="班次名称|开始时间|结束时间" fieldKeys="shiftname|begintime|endtime" searchLabels="班次名称|开始时间|结束时间" searchKeys="shiftname|begintime|endtime" ></sys:gridselect>

					</td>


					<td>
						<input id="processRoutineList{{idx}}_workhour" name="processRoutineList[{{idx}}].workhour" type="text" value="{{row.workhour}}"    class="form-control "/>
					</td>




					<td>
						<textarea id="processRoutineList{{idx}}_remarks" name="processRoutineList[{{idx}}].remarks" rows="1"    class="form-control ">{{row.remarks}}</textarea>
					</td>

				</tr>//-->
				</script>
			<script type="text/javascript">
				var processRoutineRowIdx = 0, processRoutineTpl = $("#processRoutineTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(processBatch.processRoutineList)};
					for (var i=0; i<data.length; i++){
						addRow('#processRoutineList', processRoutineRowIdx, processRoutineTpl, data[i]);
						processRoutineRowIdx = processRoutineRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('processroutine:processBatch:edit') || isAdd}">
			<div class="col-lg-3"></div>
			<div class="col-lg-2">
				<div class="form-group text-center">
					<div>
						<input type="button" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="button_save()" data-loading-text="正在保存..." value="保 存"></input>

					</div>
				</div>
			</div>
			<div class="col-lg-2"></div>
			<div class="col-lg-2">
				<div class="form-group text-center">
					<div>
						<input type="button" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="button_submit()" data-loading-text="正在提交..." value="提 交"></input>
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
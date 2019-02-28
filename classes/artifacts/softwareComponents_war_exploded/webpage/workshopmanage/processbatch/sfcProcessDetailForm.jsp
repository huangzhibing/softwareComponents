<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车间作业计划分批管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){


                    //提交提示
                    if($('#assignedState').val()=="C"){
                        jp.confirm("是否提交此车间作业计划对应的分批计划？",function () {
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
			
	        $('#planBdate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#planEdate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });

		});


        /*
         *保存按钮事件
         */
        function button_save() {
            $('#assignedState').val('P');//修改SfcProcessDetail表中assignedState状态

            /* 修改ProcessBatch表字段*/
            $('input[name$=".billState"]').each(function(){
                $(this).val('P');//更改分批表状态
            });
            $('input[name$=".assignedState"]').each(function(){
                $(this).val('P');//更改分批计划计划派工状态
            });
            var nowTime=jp.dateFormat(new Date(),"YYYY-MM-dd hh:mm:ss");
            $('input[name$=".makeDate"]').each(function() {
                $(this).val(nowTime);  //修改制单日期时间
            });
            $('input[name$=".makeID"]').each(function() {
                $(this).val('${fns:getUser().id}');//修改制单人
            });
            $('input[name$=".confirmDate"]').each(function() {
                $(this).attr("disabled","true");//禁用提交日期字段（默认为空串""，日期插入""后台会报错）;
            });
            $('input[name$=".confirmPid"]').each(function() {
                $(this).attr("disabled","true");//禁用提交人字段
            });

            $('#inputForm').submit();//提交表单
        }

        /**
         * 提交按钮事件
         */
        function button_submit() {
            var number=0;
            $('input[name$=".planQty"]').each(function(){
                number=Number($(this).val())+Number(number);
            });
            if(number!=${sfcProcessDetail.planQty}){
                jp.warning("已分批的数量总和不等于车间作业计划总数量，不能提交!");
                console.log(number);
                return false;
            }

            $('#assignedState').val('C');//修sfcprocessdetail表中assignedState状态

            /* 修改ProcessBatch表字段*/
            $('input[name$=".billState"]').each(function(){
                $(this).val('C');//更改分批计划状态
            });
            $('input[name$=".assignedState"]').each(function(){
                $(this).val('P');//分批表计划派工状态
            });
            var nowTime=jp.dateFormat(new Date(),"YYYY-MM-dd hh:mm:ss");
            /*$('input[name$=".makeDate"]').each(function() {
                $(this).val(nowTime);  //修改制单日期时间
            });
            $('input[name$=".makePID"]').each(function() {
                $(this).val('${fns:getUser().id}');//修改制单人
            });*/
            $('input[name$=".confirmDate"]').each(function() {
                $(this).val(nowTime);
                //$(this).attr("disabled","true");//禁用提交日期字段（默认为空串""，日期插入""后台会报错）;
            });
            $('input[name$=".confirmPid"]').each(function() {
                $(this).val('${fns:getUser().id}');
                //$(this).attr("disabled","true");//禁用提交人字段
            });

            $('#inputForm').submit();//提交表单
        }

		
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

            if(row==undefined){
                $(list+idx+"_prodCode").val("${sfcProcessDetail.prodCode}");
                $(list+idx+"_prodName").val("${sfcProcessDetail.prodName}");
                $(list+idx+"_planBDateInput").val("${fns:formatDateTime(sfcProcessDetail.planBdate)}".substr(0,10));
                $(list+idx+"_planQty").val("1");
                //console.log($(list+idx+"_planBDate"));
                //console.log("${fns:formatDateTime(sfcProcessDetail.planBdate)}".substr(0,10));
            }

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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/processbatch/sfcProcessDetail"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="sfcProcessDetail" action="${ctx}/processbatch/sfcProcessDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
        <form:hidden path="assignedState"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">作业计划编号：</label>
					<div class="col-sm-3">
						<form:input path="processBillNo" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">计划数量：</label>
					<div class="col-sm-3">
						<form:input path="planQty" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">产品编码：</label>
					<div class="col-sm-3">
						<form:input path="prodCode" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">产品名称：</label>
					<div class="col-sm-3">
						<form:input path="prodName" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">计划生产日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
							<div class='input-group form_datetime' id='planBdate'>
			                    <input type='text'  name="planBdate" class="form-control " readonly="true" value="<fmt:formatDate value="${sfcProcessDetail.planBdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>

					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-3">
						<form:textarea path="remarks" htmlEscape="false" rows="2"  readonly="true"  class="form-control "/>
					</div>

				</div>


		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">车间作业计划分批表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#processBatchList', processBatchRowIdx, processBatchTpl);processBatchRowIdx = processBatchRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>批次序号</th>
						<th>产品编码</th>
						<th>产品名称</th>
						<th>计划生产日期</th>
						<th><font color="red">*</font>计划生产数量</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="processBatchList">
				</tbody>
			</table>
			<script type="text/template" id="processBatchTpl">//<!--
				<tr id="processBatchList{{idx}}">
					<td class="hide">
						<input id="processBatchList{{idx}}_id" name="processBatchList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="processBatchList{{idx}}_delFlag" name="processBatchList[{{idx}}].delFlag" type="hidden" value="0"/>
						<input id="processBatchList{{idx}}_assignedState" name="processBatchList[{{idx}}].assignedState" type="hidden" value="{{row.assignedState}}"/>
						<input id="processBatchList{{idx}}_billState" name="processBatchList[{{idx}}].billState" type="hidden" value="{{row.billState}}" />
						<input id="processBatchList{{idx}}_makeID" name="processBatchList[{{idx}}].makeID" type="hidden" value="{{row.makeID}}" />
		                <input id="processBatchList{{idx}}_makeDate" name="processBatchList[{{idx}}].makeDate" type="hidden" value="{{row.makeDate}}"/>
						<input id="processBatchList{{idx}}_confirmPid" name="processBatchList[{{idx}}].confirmPid" type="hidden" value="{{row.confirmPid}}"  />
		                <input id="processBatchList{{idx}}_confirmDate"  name="processBatchList[{{idx}}].confirmDate" type="hidden"  value="{{row.confirmDate}}"/>

					</td>
					

					

					
					
					<td>
						<input id="processBatchList{{idx}}_batchNo" name="processBatchList[{{idx}}].batchNo" type="text" value="{{idx}}" readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="processBatchList{{idx}}_prodCode" name="processBatchList[{{idx}}].prodCode" type="text" value="{{row.prodCode}}"  readonly="true"   class="form-control "/>
					</td>
					<td>
						<input id="processBatchList{{idx}}_prodName" name="processBatchList[{{idx}}].prodName" type="text" value="{{row.prodName}}"   readonly="true"  class="form-control "/>
					</td>

					<td>
						<div class='input-group form_datetime' id="processBatchList{{idx}}_planBDate">
		                    <input type='text' id="processBatchList{{idx}}_planBDateInput" name="processBatchList[{{idx}}].planBDate" class="form-control " readonly="true"  value="{{row.planBDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>
					</td>

					<td>
						<input id="processBatchList{{idx}}_planQty" name="processBatchList[{{idx}}].planQty" type="text" value="{{row.planQty}}"    class="form-control number isFloatGtZero"/>
					</td>

					<td>
						<textarea id="processBatchList{{idx}}_remarks" name="processBatchList[{{idx}}].remarks" rows="1"    class="form-control ">{{row.remarks}}</textarea>
					</td>

					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#processBatchList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var processBatchRowIdx = 1, processBatchTpl = $("#processBatchTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(sfcProcessDetail.processBatchList)};
					for (var i=0; i<data.length; i++){
						addRow('#processBatchList', processBatchRowIdx, processBatchTpl, data[i]);
						processBatchRowIdx = processBatchRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('processbatch:sfcProcessDetail:edit') || isAdd}">
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
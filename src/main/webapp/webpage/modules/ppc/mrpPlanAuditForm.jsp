<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>MRP计划审核</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					jp.loading();
					form.submit();
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


        /*
		通过按钮事件
       */
        function button_pass(){
            jp.confirm("确认审核通过？",function () {

                $('#MRPDealStatus').val('S');//修改MPS表中MRPDeal状态

                /* 修改MRP表字段*/
                $('input[name$=".planStatus"]').each(function() {
                    $(this).val('S');//更改MRP计划状态
                    console.log(this.id);
                });
                var nowTime=jp.dateFormat(new Date(),"YYYY-MM-dd hh:mm:ss");
                $('input[name$=".auditDate"]').each(function() {
                    $(this).val(nowTime);  //修改审批日期时间
                });
                $('input[name$=".auditPID"]').each(function() {
                    $(this).val('${fns:getUser().id}');//修改审批人
                });

                $('#inputForm').submit();//提交表单

            });

        }

        /*
         不通过按钮事件
         */
        function button_no_pass(){
            jp.confirm("确认审核不通过？",function () {

                $('#MRPDealStatus').val('U');//修改MPS表中MRPDeal状态

                /* 修改MRP表字段*/
                $('input[name$=".planStatus"]').each(function() {
                    $(this).val('U');
                });//更改MRP计划状态
                var nowTime=jp.dateFormat(new Date(),"YYYY-MM-dd hh:mm:ss");
                $('input[name$=".auditDate"]').each(function() {
                    if($(this).val()=="")
                        $(this).val(nowTime);  //修改审批日期时间
                });
                $('input[name$=".auditPID"]').each(function() {
                    if($(this).val()=="")
                        $(this).val('${fns:getUser().id}');//修改审批人
                });
                $('#inputForm').submit();//提交表单

            });

        }

		/*
		初始化MRP子表函数
		 */
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


	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/ppc/mrpPlan/auditList"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="mpsPlan" action="${ctx}/ppc/mrpPlan/auditSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="MRPDealStatus"/>
		<sys:message content="${message}"/>
			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>MPS计划号：</label>
				<div class="col-sm-3">
					<form:input path="mpsPlanid" htmlEscape="false" readonly="true" class="form-control required"/>
				</div>

				<label class="col-sm-2 control-label"><font color="red">*</font>产品编码：</label>
				<div class="col-sm-3">
					<form:input path="prodCode" htmlEscape="false"  readonly="true"  class="form-control "/>
				</div>
			</div>


			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>产品名称：</label>
				<div class="col-sm-3">
					<form:input path="prodName" htmlEscape="false"  readonly="true"  class="form-control "/>
				</div>

				<label class="col-sm-2 control-label"><font color="red">*</font>计划数量：</label>
				<div class="col-sm-3">
					<form:input path="planQty" htmlEscape="false"  readonly="true"  class="form-control required number"/>
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-2 control-label"><font color="red">*</font>计划开始日期：</label>
				<div class="col-sm-3">
					<div class='input-group form_datetime' id='planBDate'>
						<input type='text' id='planBDateInput' name="planBDate" class="form-control" readonly="true"  value="<fmt:formatDate value="${mpsPlan.planBDate}" pattern="yyyy-MM-dd"/>"/>
						<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
					</div>
				</div>

				<label class="col-sm-2 control-label"><font color="red">*</font>计划结束日期：</label>
				<div class="col-sm-3">
					<div class='input-group form_datetime' id='planEDate'>
						<input type='text' id='planEDateInput' name="planEDate" class="form-control" readonly="true"  value="<fmt:formatDate value="${mpsPlan.planEDate}" pattern="yyyy-MM-dd"/>"/>
						<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
					</div>
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-2 control-label">备注信息：</label>
				<div class="col-sm-8">
					<form:textarea path="remarks" htmlEscape="false"  readonly="true" class="form-control "/>
				</div>
			</div>




			<div class="tabs-container">
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">MRP计划表：</a>
					</li>
				</ul>
				<div class="tab-content">
					<div id="tab-1" class="tab-pane fade in  active">
						<%--<a class="btn btn-white btn-sm" onclick="addRow('#mrpPlanList', mrpPlanRowIdx, mrpPlanTpl);mrpPlanRowIdx = mrpPlanRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>--%>
						<table class="table table-striped table-bordered table-condensed">
							<thead>
							<tr>
								<th class="hide"></th>
								<th><font color="red"></font>审批意见</th>
								<th><font color="red">*</font>MRP计划号</th>
								<th><font color="red">*</font>物料编码</th>
								<th><font color="red">*</font>物料名称</th>
								<th><font color="red">*</font>计划数量</th>
								<th><font color="red">*</font>计划开始日期</th>
								<th><font color="red">*</font>计划结束日期</th>
								<th>备注</th>
							</tr>
							</thead>
							<tbody id="mrpPlanList">
							</tbody>
						</table>
						<script type="text/template" id="mrpPlanTpl">//<!--
				<tr id="mrpPlanList{{idx}}">
					<td class="hide">
						<input id="mrpPlanList{{idx}}_id" name="mrpPlanList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="mrpPlanList{{idx}}_auditDate" name="mrpPlanList[{{idx}}].auditDate" type="hidden" value="{{row.auditDate}}" />
						<input id="mrpPlanList{{idx}}_planStatus" name="mrpPlanList[{{idx}}].planStatus" type="hidden" value="{{row.planStatus}}" />
						<input id="mrpPlanList{{idx}}_auditPID" name="mrpPlanList[{{idx}}].auditPID" type="hidden" value="{{row.auditPID}}" />
						<input id="mrpPlanList{{idx}}_delFlag" name="mrpPlanList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					
					<td>
						<input id="mrpPlanList{{idx}}_auditComment" name="mrpPlanList[{{idx}}].auditComment" type="text" value="{{row.auditComment}}" class="form-control"/>
					</td>
					
					<td>
						<input id="mrpPlanList{{idx}}_MRPPlanID" name="mrpPlanList[{{idx}}].mrpplanID" type="text" value="{{row.mrpplanID}}"  readonly="true"  class="form-control "/>
					</td>


					<td>
						<input id="mrpPlanList{{idx}}_itemCode" name="mrpPlanList[{{idx}}].itemCode" type="text" value="{{row.itemCode}}"  readonly="true"  class="form-control "/>
					</td>


					<td>
						<input id="mrpPlanList{{idx}}_itemName" name="mrpPlanList[{{idx}}].itemName"  type="text" value="{{row.itemName}}"  readonly="true"  class="form-control "/>
					</td>


					<td>
						<input id="mrpPlanList{{idx}}_planQty" name="mrpPlanList[{{idx}}].planQty" readonly="true" type="text" value="{{row.planQty}}"    class="form-control required isFloatGtZero"/>
					</td>



					<td>
						<div class='input-group form_datetime' id="mrpPlanList{{idx}}_planBdate">
		                    <input type='text'  id=mrpPlanList{{idx}}_planBdate_input name="mrpPlanList[{{idx}}].planBdate" class="form-control required" readonly="true" value="{{row.planBdate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>
					</td>


					<td>
						<div class='input-group form_datetime' id="mrpPlanList{{idx}}_planEDate">
		                    <input type='text' id="mrpPlanList{{idx}}_planEDate_input" name="mrpPlanList[{{idx}}].planEDate" class="form-control required" readonly="true" value="{{row.planEDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>
					</td>

					<td>
						<input id="mrpPlanList{{idx}}_remarks" name="mrpPlanList[{{idx}}].remarks" type="text" value="{{row.remarks}}"  readonly="true"  class="form-control "/>
					</td>

				</tr>//-->
						</script>
						<script type="text/javascript">
                            var mrpPlanRowIdx = 0, mrpPlanTpl = $("#mrpPlanTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                            $(document).ready(function() {
                                var data = ${fns:toJson(mpsPlan.mrpPlanList)};
                                for (var i=0; i<data.length; i++){
                                    addRow('#mrpPlanList', mrpPlanRowIdx, mrpPlanTpl, data[i]);
                                    mrpPlanRowIdx = mrpPlanRowIdx + 1;
                                    console.log(data[i]);
                                }
                            });
						</script>
					</div>
				</div>
			</div>






		<c:if test="${fns:hasPermission('ppc:mrpPlan:audit')}">
			<div class="col-lg-3"></div>
			<div class="col-lg-2">
				<div class="form-group text-center">
					<div>
						<input type="button" class="btn btn-success btn-block btn-lg btn-parsley" onclick="button_pass()" data-loading-text="正在处理..." value="审批通过"></input>

					</div>
				</div>
			</div>
			<div class="col-lg-2"></div>
			<div class="col-lg-2">
				<div class="form-group text-center">
					<div>
						<input type="button" class="btn btn-danger btn-block btn-lg btn-parsley" onclick="button_no_pass()" data-loading-text="正在处理..." value="审批不通过"></input>
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
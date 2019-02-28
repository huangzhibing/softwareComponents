<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车间作业计划下达</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
                    //下达提示

                        jp.confirm("是否下达此主生产计划对应的车间作业计划？",function () {
                            jp.loading();
                            form.submit();
                        },function () {
							return false;
                        });

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
		});


        /**
		 * 提交按钮事件
         */
        function button_submit() {


            $('#SFCDealStatus').val('S');//修改MPS表中SFCDealStatus状态

            /* 修改SfcProcessDetail表字段*/
            $('input[name$=".billState"]').each(function(){
                $(this).val('S');//更改车间作业计划状态
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
					 //,defaultDate:"${mpsPlan.planBDate}"
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/process/sfcProcessMain/orderList"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
			<%--@elvariable id="mpsPlan" type="com.hqu.modules.workshopmanage.ppc.entity.MpsPlan"--%>
		<form:form id="inputForm" modelAttribute="mpsPlan" action="${ctx}/process/sfcProcessMain/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="SFCDealStatus"/>
		<sys:message content="${message}"/>	
				
				<div class="form-group">
					<label class="col-sm-2 control-label">主生产计划号：</label>
					<div class="col-sm-3">
						<form:input path="mpsPlanid" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">产品编码：</label>
					<div class="col-sm-3">
						<form:input path="prodCode" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label">产品名称：</label>
					<div class="col-sm-3">
						<form:input path="prodName" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>

					<label class="col-sm-2 control-label">计划数量：</label>
					<div class="col-sm-3">
						<form:input path="planQty" htmlEscape="false"  readonly="true"  class="form-control "/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">计划开始日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
							<div class='input-group form_datetime' id='planBDate'>
			                    <input type='text'  name="planBDate" class="form-control " readonly="true" value="<fmt:formatDate value="${mpsPlan.planBDate}" pattern="yyyy-MM-dd "/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
			            </p>
					</div>

					<label class="col-sm-2 control-label">计划结束日期：</label>
					<div class="col-sm-3">
						<p class="input-group">
						<div class='input-group form_datetime' id='planEDate'>
							<input type='text'  name="planEDate" class="form-control " readonly="true" value="<fmt:formatDate value="${mpsPlan.planEDate}" pattern="yyyy-MM-dd"/>"/>
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
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">车间作业计划表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">

			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						
						<th><font color="red">*</font>作业计划编号</th>
						<th><font color="red">*</font>产品编码</th>
						<th><font color="red">*</font>产品名称</th>
						<th><font color="red">*</font>生产日期</th>
						<th><font color="red">*</font>计划数量</th>
						<th>备注信息</th>
					</tr>
				</thead>
				<tbody id="sfcProcessDetailList">
				</tbody>
			</table>
			<script type="text/template" id="sfcProcessDetailTpl">//<!--
				<tr id="sfcProcessDetailList{{idx}}">
					<td class="hide">
						<input id="sfcProcessDetailList{{idx}}_id" name="sfcProcessDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="sfcProcessDetailList{{idx}}_delFlag" name="sfcProcessDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
						<input id="sfcProcessDetailList{{idx}}_planStatus" name="sfcProcessDetailList[{{idx}}].billState" type="hidden" value="{{row.billState}}" />
						<input id="sfcProcessDetailList{{idx}}_assignedState" name="sfcProcessDetailList[{{idx}}].assignedState" type="hidden" value="{{row.assignedState}}" />
						<input id="sfcProcessDetailList{{idx}}_makePID" name="sfcProcessDetailList[{{idx}}].makePID" type="hidden" value="{{row.makePID}}" />
						<input id="sfcProcessDetailList{{idx}}_makeDate" name="sfcProcessDetailList[{{idx}}].makeDate" type="hidden" value="{{row.makeDate}}" />
						<input id="sfcProcessDetailList{{idx}}_confirmPID" name="sfcProcessDetailList[{{idx}}].confirmPID" type="hidden" value="{{row.confirmPID}}" />
						<input id="sfcProcessDetailList{{idx}}_confirmDate" name="sfcProcessDetailList[{{idx}}].confirmDate" type="hidden" value="{{row.confirmDate}}" />
					</td>
					

					<td>
						<input id="sfcProcessDetailList{{idx}}_processBillNo" name="sfcProcessDetailList[{{idx}}].processBillNo" type="text"  value="{{row.processBillNo}}"  readonly="true"  class="form-control "/>
					</td>


					
					<td >
						<input id="sfcProcessDetailList{{idx}}_prodCode" name="sfcProcessDetailList[{{idx}}].prodCode" value="{{row.prodCode}}" readonly="true" class="form-control required"/>
					</td>



					<td >
						<input id="sfcProcessDetailList{{idx}}_prodName" name="sfcProcessDetailList[{{idx}}].prodName" value="{{row.prodName}}" readonly="true" class="form-control required"/>
					</td>

					<td>
						<div class='input-group form_datetime' id="sfcProcessDetailList{{idx}}_planBDate">
		                    <input type='text' id="sfcProcessDetailList{{idx}}_planBDateInput" name="sfcProcessDetailList[{{idx}}].planBdate"  readonly="true"  class="form-control required"  value="{{row.planBdate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>
					</td>
					
					<td>
						<input id="sfcProcessDetailList{{idx}}_planQty" name="sfcProcessDetailList[{{idx}}].planQty" type="text" value="{{row.planQty}}"  readonly="true"   class="form-control number required isFloatGtZero"/>
					</td>
					


					<td>
						<textarea id="sfcProcessDetailList{{idx}}_remarks" name="sfcProcessDetailList[{{idx}}].remarks" rows="1"   readonly="true"   class="form-control ">{{row.remarks}}</textarea>
					</td>
					

				</tr>//-->
			</script>
			<script type="text/javascript">
				var sfcProcessDetailRowIdx = 1, sfcProcessDetailTpl = $("#sfcProcessDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(mpsPlan.sfcProcessDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#sfcProcessDetailList', sfcProcessDetailRowIdx, sfcProcessDetailTpl, data[i]);
						sfcProcessDetailRowIdx = sfcProcessDetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('process:sfcProcessMain:edit') || isAdd}">
				<%--<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <c:if test="${isAdd}">
		                     	<button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在保存...">保存</button>
		                 	 </c:if>
		                 	 <c:if test="${!isAdd}">
		                 	 	<button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在保存...">保存</button>
		                 	 	<button type="button" class="btn btn-primary btn-lg btn-parsley" onclick="submitBill()">提交</button>
		                 	 </c:if>
		                 </div>
		             </div>
		        </div>--%>
			<div class="col-lg-3"></div>

			<div class="col-lg-5">
				<div class="form-group text-center">
					<div>
						<input type="button" class="btn btn-primary btn-block btn-lg btn-parsley" onclick="button_submit()" data-loading-text="正在下达..." value="下 达"></input>
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
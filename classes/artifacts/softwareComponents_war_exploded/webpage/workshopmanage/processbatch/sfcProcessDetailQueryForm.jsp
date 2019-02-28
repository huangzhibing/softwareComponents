<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车间作业计划分批查询</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			
			
	        $('#planBdate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#planEdate').datetimepicker({
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

            if(row==undefined){
                $(list+idx+"_prodCode").val("${sfcProcessDetail.prodCode}");
                $(list+idx+"_prodName").val("${sfcProcessDetail.prodName}");
                $(list+idx+"_planBDateInput").val("${fns:formatDateTime(sfcProcessDetail.planBdate)}".substr(0,10));
                $(list+idx+"_planQty").val("1");
                //console.log($(list+idx+"_planBDate"));
                //console.log("${fns:formatDateTime(sfcProcessDetail.planBdate)}".substr(0,10));
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
				<a class="panelButton" href="${ctx}/processbatch/sfcProcessDetail/queryList"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="sfcProcessDetail" action="" method="post" class="form-horizontal">
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


					<label class="col-sm-2 control-label">单据状态：</label>
					<div class="col-sm-3">
						<form:select path="assignedState" class="form-control "  disabled="true" >
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('processbatch_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
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
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">车间作业计划分批表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			
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
						<input id="processBatchList{{idx}}_planQty" name="processBatchList[{{idx}}].planQty" type="text" value="{{row.planQty}}"  readonly="true"    class="form-control number isFloatGtZero"/>
					</td>

					<td>
						<textarea id="processBatchList{{idx}}_remarks" name="processBatchList[{{idx}}].remarks" rows="1"  readonly="true"    class="form-control ">{{row.remarks}}</textarea>
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

		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
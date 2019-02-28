<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>检验单管理</title>
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>

	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					//jp.loading();
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
			
	        $('#checkDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#justifyDate').datetimepicker({
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
					 format: "YYYY-MM-DD HH:mm:ss"
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
				<a class="panelButton" href="${ctx}/purreport/purReport/completelist"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm"  action="${ctx}/purreport/purReport/save" method="post" class="form-horizontal">
		
		
			
				
		<div style="width:100%;  overflow-x:scroll; ">		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">检验物品清单：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			
			<table id="subTab" class="table table-striped table-bordered table-condensed" style="min-width:200%;">
				
				<thead>
					<tr>
						<th class="hide"></th>
						<th>检验单编号</th>
						<th>检验对象序列号</th>
						<th>检验对象编码</th>
						<th>检验对象名称</th>
						<%-- <th>问题类型编码</th> --%>
						<th>规则ID</th>
						<th>测试项目</th>
						<th>检验标准号</th>
						<th>检验标准名称</th>
						<th>岗位名称</th>
						<th>是否检验合格</th>
					</tr>
				</thead>
				
				
				<tbody id="verifyQCNormList">
				</tbody>
			</table>
			
			
			
			
			
			
			<script type="text/template" id="purReportRSnTpl">//<!--
				<tr id="verifyQCNormList{{idx}}">

					<td class="hide">
						<input id="verifyQCNormList{{idx}}_id" name="verifyQCNormList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="verifyQCNormList{{idx}}_delFlag" name="verifyQCNormList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td style="width: 120px;">
						<input id="verifyQCNormList{{idx}}_reportId" name="verifyQCNormList[{{idx}}].reportId" type="text" value="{{row.reportId}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td style="width: 160px;">
						<input id="verifyQCNormList{{idx}}_objSn" name="verifyQCNormList[{{idx}}].objSn" type="text" value="{{row.objSn}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td style="width: 100px;">
						<input id="verifyQCNormList{{idx}}_objCode" name="verifyQCNormList[{{idx}}].objCode"  type="text" value="{{row.objCode}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td style="width: 100px;">
						<input id="verifyQCNormList{{idx}}_objName" name="verifyQCNormList[{{idx}}].objName"  type="text" value="{{row.objName}}"  readonly="true"  class="form-control "/>
					</td>


					<td style="width: 100px;">
						<input id="verifyQCNormList{{idx}}_ruleId" name="verifyQCNormList[{{idx}}].ruleId"  type="text" value="{{row.ruleId}}"  readonly="true"  class="form-control "/>
					</td>


					<td style="width: 100px;">
						<input id="verifyQCNormList{{idx}}_checkprj" name="verifyQCNormList[{{idx}}].checkprj"  type="text" value="{{row.checkprj}}"  readonly="true"  class="form-control "/>
					</td>


					<td style="width: 100px;">
						<input id="verifyQCNormList{{idx}}_qcnormId" name="verifyQCNormList[{{idx}}].qcnormId"  type="text" value="{{row.qcnormId}}"  readonly="true"  class="form-control "/>
					</td>


					<td style="width: 100px;">
						<input id="verifyQCNormList{{idx}}_qcnormName" name="verifyQCNormList[{{idx}}].qcnormName"  type="text" value="{{row.qcnormName}}"  readonly="true"  class="form-control "/>
					</td>

					

					<td style="width: 100px;">
						<input id="verifyQCNormList{{idx}}_roleName" name="verifyQCNormList[{{idx}}].roleName"  type="text" value="{{row.roleName}}"  readonly="true"  class="form-control "/>
					</td>


					<td style="width: 120px;">
						<select id="verifyQCNormList{{idx}}_isQuality" name="verifyQCNormList[{{idx}}].isQuality" data-value="{{row.isQuality}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('TrueORFalse')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>


					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purReportRSnRowIdx = 0, purReportRSnTpl = $("#purReportRSnTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(verifyQCNormList)};
					for (var i=0; i<data.length; i++){
						console.log(data[i]);
						addRow('#verifyQCNormList', purReportRSnRowIdx, purReportRSnTpl, data[i]);
						purReportRSnRowIdx = purReportRSnRowIdx + 1;
					}
				});
				
				
				
			</script>
			</div>
		</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('purreport:purReport:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <%--
		                      <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                --%>
		                	 <input type="button" class="btn btn-primary" value="保存" onclick="save()"> &nbsp;
							 
							<input type="button" class="btn btn-primary" value="提交" onclick="sub()"> &nbsp;
							<input type="button" class="btn btn-primary" value="关闭" onclick="myclose()" >
		                 </div>
		             </div>
		        </div>
		</c:if>
		
		
		<script type="text/javascript">
		
		
		
		
		function setSFC(id){
			//alert("setSFC");
			$.ajax({
				type:'get',
				datatype:'json',
				url:"${ctx}/purreport/purReport/findDate?str="+id,
				//data:$("form").serializeArray(),
				contentType:"application/x-www-form-urlencoded",
				success:function(data){
				//	alert("sus");
					
					//var data1 = ${fns:toJson(data)};
					console.log(data);
					getValue(data);
				}
				
			});
			
			
		}
		
		
		function myclose(){
			//alert("close");
			window.location ="${ctx}/purreport/purReport/completelist";
			
		}
		
		
		
		function save(){
			//alert($("#invCheckMain").val());
			//保存office的id
			//var cdeptCodeValue = $('input[name="office.id"]').val();
			//$("#cdeptCode").val(cdeptCodeValue);
			
	
			$.ajax({
				type:'POST',
				datatype:'text',
				url:"${ctx}/purreport/purReport/ajaxCom",
				data:$("form").serializeArray(),
				contentType:"application/x-www-form-urlencoded",
				success:function(data){
					top.layer.msg("保存成功", {icon: "${ctype=='success'? 1:1}"});
				}
				
			})
			
			 
			
		}
	
		
		function sub(){
			//保存office的id
			//var cdeptCodeValue = $('input[name="office.id"]').val();
			//$("#cdeptCode").val(cdeptCodeValue);
			var len=  purReportRSnRowIdx;
			var count=0;
			for(var k=0;k<len;k++){
				var val=$("#verifyQCNormList"+k+"_isQuality").val();
			
				if(val=="f"){
					count=count+1;
					//top.layer.msg("不能提交", {icon: "${ctype=='success'? 2:1}"});
					
				}
				if(val==""){
					count=count+1;
					//top.layer.msg("不能提交", {icon: "${ctype=='success'? 2:1}"});
					
				}
			}
			if(count==0&&len!=0){
				var newUrl = "${ctx}/purreport/purReport/saveCom";    //设置新提交地址
		        $("form").attr('action',newUrl);    //通过jquery为action属性赋值
		        $("form").submit();    //提交ID为myform的表单
			}else{
				
				jp.warning("不能提交");
			}
			
		
		}
		
		
		
		</script>
		
		
	
		
		
		
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
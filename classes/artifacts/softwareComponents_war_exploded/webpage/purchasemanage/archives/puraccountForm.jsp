<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>供应商档案管理</title>
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
		//地区编码grid选择 by  ltq 20180804
		function setAreaCode(ojb){
			$("#areaCode").val(ojb.areacode);
		}

		//类别编码grid选择 by ckw 20180804
        function setSupTypeCode(ojb){
            $("#subTypeCode").val(ojb.suptypeId);
        }

        // setSupTypeCode(items[0]);
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/archives/puraccount"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="puraccount" action="${ctx}/archives/puraccount/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">企业编码：</label>
					<div class="col-sm-3">
						<form:input path="accountCode" htmlEscape="false"    class="form-control " readonly="true"/>
					</div>
					<label class="col-sm-2 control-label">企业名称：</label>
					<div class="col-sm-3">
						<form:input path="accountName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">地区编码：</label>
					<div class="col-sm-3">
						<form:input path="areaCode" htmlEscape="false"    class="form-control hidden"/>						
						<sys:gridselect-purarea url="${ctx}/areacode/areaCode/myAreaCode" id="areaCode"
												name="areaCode.areacode" value="${puraccount.areaCode}"
                                                labelName="areaCode.areacode" labelValue="${puraccount.areaCode}"
                                                title="选择地区编码" cssClass="form-control required"
                                               targetId="areaName"  targetField="areaname"
												fieldLabels="地区编码|地区名称"    fieldKeys="areacode|areaname"
												searchLabels="地区编码|地区名称" searchKeys="areacode|areaname" >
						</sys:gridselect-purarea>
					</div>
					<label class="col-sm-2 control-label">地区名称：</label>
					<div class="col-sm-3">
						<form:input path="areaName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>

			<%--puraccount.java--%>
			<%--private String subTypeCode;        // 类别编码--%>
			<%--private String subTypeName;        // 类别名称--%>

			<%--suptype.java--%>
			<%--private String suptypeId;		// 供应商类别编码--%>
			<%--private String suptypeName;		// 供应商类别名称--%>
				<div class="form-group">
					<label class="col-sm-2 control-label">类别编码：</label>
					<div class="col-sm-3">
						<form:input path="subTypeCode" htmlEscape="false"    class="form-control hidden"/>
						<sys:gridselect-pursuptype url="${ctx}/suptype/supType/data" id="supType"
												name="supType.suptypeId" value="${puraccount.subTypeCode}"
												labelName="supType.suptypeId" labelValue="${puraccount.subTypeCode}"
												title="选择类型编码" cssClass="form-control required"
												targetId="subTypeName"  targetField="suptypeName"
												fieldLabels="类型编码|类型名称"    fieldKeys="suptypeId|suptypeName"
												searchLabels="类型编码|类型名称" searchKeys="suptypeId|suptypeName" >
						</sys:gridselect-pursuptype>
					</div>
					<label class="col-sm-2 control-label">类别名称：</label>
					<div class="col-sm-3">
						<form:input path="subTypeName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">性质：</label>
					<div class="col-sm-3">
						<form:input path="accountProp" htmlEscape="false"    class="form-control "/>
					</div>
					<label class="col-sm-2 control-label">法人：</label>
					<div class="col-sm-3">
						<form:input path="accountMgr" htmlEscape="false"    class="form-control "/>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label">联系地址：</label>
					<div class="col-sm-3">
						<form:input path="accountAddr" htmlEscape="false"    class="form-control "/>
					</div>
					<label class="col-sm-2 control-label">邮编：</label>
					<div class="col-sm-3">
						<form:input path="postCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label">电话：</label>
					<div class="col-sm-3">
						<form:input path="telNum" htmlEscape="false"    class="form-control  isPhone"/>
					</div>
					<label class="col-sm-2 control-label">传真：</label>
					<div class="col-sm-3">
						<form:input path="faxNum" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">开户行：</label>
					<div class="col-sm-3">
						<form:input path="bankName" htmlEscape="false"    class="form-control "/>
					</div>
					<label class="col-sm-2 control-label">银行账户：</label>
					<div class="col-sm-3">
						<form:input path="bankAccount" htmlEscape="false"    class="form-control "/>
					</div>
				</div>

			<div class="form-group">
				<label class="col-sm-2 control-label">上级部门：</label>
				<div class="col-sm-3">
					<form:input path="supHigherUp" htmlEscape="false"    class="form-control "/>
				</div>
					<label class="col-sm-2 control-label">纳税号：</label>
					<div class="col-sm-3">
						<form:input path="taxCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">供货商状态：</label>
					<div class="col-sm-3">
						<form:input path="state" htmlEscape="false"    class="form-control "/>
					</div>
					<label class="col-sm-2 control-label">综合评估：</label>
					<div class="col-sm-3">
						<form:input path="supEvalation" htmlEscape="false"    class="form-control "/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-8">
						<form:input path="supNotes" htmlEscape="false"    class="form-control "/>
					</div>
															
				</div>
               
               <div class="form-group">
                    
                    <label class="col-sm-2 control-label">供应商附件：</label>
					  <div class="col-sm-3">
					    <br>
                        <form:hidden path="accountFile" htmlEscape="false" maxlength="150" class="input-xlarge"/>
                        <sys:ckfinder input="accountFile" type="files" selectMultiple="true" readonly="false" uploadPath="/供应商附件"/>                    					    					    
					  </div>
                    
               </div>
               
               
		<div class="tabs-container">
            <ul class="nav nav-tabs">
                </li>
				<li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">联系人</a>
                </li>
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">合同信息</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">到货信息</a>

            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
	<!--子表新增按钮 	<a class="btn btn-white btn-sm" onclick="addRow('#purContractMainList', purContractMainRowIdx, purContractMainTpl);purContractMainRowIdx = purContractMainRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a> -->	
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>日期</th>
						<th><font color="red">*</font>合同号</th>
						<th><font color="red">*</font>合同状态</th>
						<th>合同类型</th>
						<th><font color="red">*</font>供应商名称</th>
						<th><font color="red">*</font>签订人</th>
						
						<th><font color="red">*</font>采购员</th>
						
						<th>总额（不含税）</th>
						<th>总额（含税）</th>
						<th>运费总额</th>
						<th>履约保证金</th>
						<th>说明</th>
						
						
		<!--空白		<th width="10">&nbsp;</th> 	-->	
					</tr>
				</thead>
				<tbody id="purContractMainList">
				</tbody>
			</table>
			<script type="text/template" id="purContractMainTpl">//<!--
				<tr id="purContractMainList{{idx}}">
					<td class="hide">
						<input id="purContractMainList{{idx}}_id" name="purContractMainList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purContractMainList{{idx}}_delFlag" name="purContractMainList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<div class='input-group form_datetime' id="purContractMainList{{idx}}_makeDate">
		                    <input type='text'  name="purContractMainList[{{idx}}].makeDate" class="form-control " readonly="true" value="{{row.makeDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>					

					<td>
						<input id="purContractMainList{{idx}}_billNum" name="purContractMainList[{{idx}}].billNum" type="text" value="{{row.billNum}}"  readonly="true"  class="form-control required"/>
					</td>
					
					<td>
						<input id="purContractMainList{{idx}}_contrState" name="purContractMainList[{{idx}}].contrState" type="text" value="{{row.contrState}}"  readonly="true"  class="form-control required"/>
					</td>
					
					<td>
						<input id="purContractMainList{{idx}}_billType" name="purContractMainList[{{idx}}].contypeName" type="text" value="{{row.billType}}" readonly="true"   class="form-control "/>
					</td>
					
					<td>
						<input id="purContractMainList{{idx}}_supName" name="purContractMainList[{{idx}}].supName" type="text" value="{{row.supName}}"  readonly="true"  class="form-control required"/>
					</td>
					
					<td>
						<input id="purContractMainList{{idx}}_makeEmpname" name="purContractMainList[{{idx}}].makeEmpname" type="text" value="{{row.makeEmpname}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					
					
					<td>
						<input id="purContractMainList{{idx}}_buyerName" name="purContractMainList[{{idx}}].buyerName" type="text" value="{{row.buyerName}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="purContractMainList{{idx}}_tranprice" name="purContractMainList[{{idx}}].tranprice" type="text" value="{{row.tranprice}}"  readonly="true"  class="form-control  number"/>
					</td>
					
					
					<td>
						<input id="purContractMainList{{idx}}_goodsSum" name="purContractMainList[{{idx}}].goodsSum" type="text" value="{{row.goodsSum}}"   readonly="true" class="form-control  number"/>
					</td>
					
					
					<td>
						<input id="purContractMainList{{idx}}_goodsSumTaxed" name="purContractMainList[{{idx}}].goodsSumTaxed" type="text" value="{{row.goodsSumTaxed}}"  readonly="true"  class="form-control  number"/>
					</td>
					
					<td>
						<input id="purContractMainList{{idx}}_contractNeedSum" name="purContractMainList[{{idx}}].contractNeedSum" type="text" value="{{row.contractNeedSum}}" readonly="true"   class="form-control  number"/>
					</td>
					
					<td>
						<input id="purContractMainList{{idx}}_dealNote" name="purContractMainList[{{idx}}].dealNote" type="text" value="{{row.dealNote}}" readonly="true"   class="form-control "/>
					</td>
					
					
					
					

					
		<!--删除		<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#purContractMainList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
		-->
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purContractMainRowIdx = 0, purContractMainTpl = $("#purContractMainTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(puraccount.purContractMainList)};
					for (var i=0; i<data.length; i++){
						addRow('#purContractMainList', purContractMainRowIdx, purContractMainTpl, data[i]);
						purContractMainRowIdx = purContractMainRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-2" class="tab-pane fade">
<!--新增按钮 			<a class="btn btn-white btn-sm" onclick="addRow('#pinvCheckMainList', pinvCheckMainRowIdx, pinvCheckMainTpl);pinvCheckMainRowIdx = pinvCheckMainRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>    -->
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>日期</th>
						<th>到货单号</th>
						<th>到货状态</th>
						<th>单据类型</th>
						<th>供应商名称</th>
						<th>采购员名称</th>
						<th>接收仓库</th>
						<th>估价标记</th>
						<th>制单人</th>
						<th>小计金额</th>
					
						<th>运费合计</th>
						<th>金额合计</th>
			
			<!-- 		<th width="10">&nbsp;</th>	-->
					</tr>
				</thead>
				<tbody id="pinvCheckMainList">
				</tbody>
			</table>
			<script type="text/template" id="pinvCheckMainTpl">//<!--
				<tr id="pinvCheckMainList{{idx}}">
					<td class="hide">
						<input id="pinvCheckMainList{{idx}}_id" name="pinvCheckMainList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="pinvCheckMainList{{idx}}_delFlag" name="pinvCheckMainList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<div class='input-group form_datetime' id="pinvCheckMainList{{idx}}_makeDate">
		                    <input type='text'  name="pinvCheckMainList[{{idx}}].makeDate" class="form-control " readonly="true" value="{{row.makeDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>
					
					<td>
						<input id="pinvCheckMainList{{idx}}_billnum" name="pinvCheckMainList[{{idx}}].billnum" type="text" value="{{row.billnum}}"  readonly="true"  class="form-control "/>
					</td>
					
					<td>
						<input id="pinvCheckMainList{{idx}}_thFlag" name="pinvCheckMainList[{{idx}}].thFlag" type="text" value="{{row.thFlag}}"   readonly="true" class="form-control "/>
					</td>
					
					<td>
						<input id="pinvCheckMainList{{idx}}_billType" name="pinvCheckMainList[{{idx}}].billType" type="text" value="{{row.billType}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="pinvCheckMainList{{idx}}_supName" name="pinvCheckMainList[{{idx}}].supName" type="text" value="{{row.supName}}"  readonly="true"  class="form-control "/>
					</td>

					<td>
						<input id="pinvCheckMainList{{idx}}_buyerName" name="pinvCheckMainList[{{idx}}].buyerName" type="text" value="{{row.buyerName}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="pinvCheckMainList{{idx}}_wareName" name="pinvCheckMainList[{{idx}}].wareName" type="text" value="{{row.wareName}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="pinvCheckMainList{{idx}}_invFlag" name="pinvCheckMainList[{{idx}}].invFlag" type="text" value="{{row.invFlag}}"  readonly="true"  class="form-control "/>
					</td>

					<td>
						<input id="pinvCheckMainList{{idx}}_makeEmpname" name="pinvCheckMainList[{{idx}}].makeEmpname" type="text" value="{{row.makeEmpname}}"   readonly="true" class="form-control "/>
					</td>
					
					
					
					<td>
						<input id="pinvCheckMainList{{idx}}_priceSumSubtotal" name="pinvCheckMainList[{{idx}}].priceSumSubtotal" type="text" value="{{row.priceSumSubtotal}}"   readonly="true" class="form-control "/>
					</td>
					
					
					<td>
						<input id="pinvCheckMainList{{idx}}_tranpriceSum" name="pinvCheckMainList[{{idx}}].tranpriceSum" type="text" value="{{row.tranpriceSum}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="pinvCheckMainList{{idx}}_priceSum" name="pinvCheckMainList[{{idx}}].priceSum" type="text" value="{{row.priceSum}}"  readonly="true"  class="form-control "/>
					</td>
					
					
			<!--		<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#pinvCheckMainList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>	-->
				</tr>//-->
			</script>
			<script type="text/javascript">
				var pinvCheckMainRowIdx = 0, pinvCheckMainTpl = $("#pinvCheckMainTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(puraccount.pinvCheckMainList)};
					for (var i=0; i<data.length; i++){
						addRow('#pinvCheckMainList', pinvCheckMainRowIdx, pinvCheckMainTpl, data[i]);
						pinvCheckMainRowIdx = pinvCheckMainRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-3" class="tab-pane fade">
<!-- 新增按钮		<a class="btn btn-white btn-sm" onclick="addRow('#purLinkManList', purLinkManRowIdx, purLinkManTpl);purLinkManRowIdx = purLinkManRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>   -->
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>联系人编号</th>
						<th><font color="red">*</font>联系人姓名</th>
						<th width="80">性别</th>
						<th>级别</th>
						<th>职位</th>
						<th>电话</th>
						<th>手机</th>
						<th>mail</th>
						<th>状态</th>
				<!--空格 	<th width="10">&nbsp;</th> -->	
					</tr>
				</thead>
				<tbody id="purLinkManList">
				</tbody>
			</table>
			<script type="text/template" id="purLinkManTpl">//<!--
				<tr id="purLinkManList{{idx}}">
					<td class="hide">
						<input id="purLinkManList{{idx}}_id" name="purLinkManList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="purLinkManList{{idx}}_delFlag" name="purLinkManList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="purLinkManList{{idx}}_linkCode" name="purLinkManList[{{idx}}].linkCode" type="text" value="{{row.linkCode}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td>
						<input id="purLinkManList{{idx}}_linkName" name="purLinkManList[{{idx}}].linkName" type="text" value="{{row.linkName}}"  readonly="true"  class="form-control required"/>
					</td>
					
					
					<td width="80">
						<select id="purLinkManList{{idx}}_linkSex" name="purLinkManList[{{idx}}].linkSex" data-value="{{row.linkSex}}" disabled="disabled" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('sex')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<input id="purLinkManList{{idx}}_linkLevel" name="purLinkManList[{{idx}}].linkLevel" type="text" value="{{row.linkLevel}}" readonly="true"   class="form-control "/>
					</td>
					
					
					<td>
						<input id="purLinkManList{{idx}}_linkPosition" name="purLinkManList[{{idx}}].linkPosition" type="text" value="{{row.linkPosition}}"  readonly="true"  class="form-control "/>
					</td>
					
					
					<td>
						<input id="purLinkManList{{idx}}_linkTel" name="purLinkManList[{{idx}}].linkTel" type="text" value="{{row.linkTel}}"  readonly="true"  class="form-control  isPhone"/>
					</td>
					
					
					<td>
						<input id="purLinkManList{{idx}}_linkPhone" name="purLinkManList[{{idx}}].linkPhone" type="text" value="{{row.linkPhone}}" readonly="true"  class="form-control  isMobile"/>
					</td>
					
					
					<td>
						<input id="purLinkManList{{idx}}_linkMail" name="purLinkManList[{{idx}}].linkMail" type="text" value="{{row.linkMail}}"  readonly="true"  class="form-control  email"/>
					</td>
					
					
					<td>
						<input id="purLinkManList{{idx}}_linkRemarks" name="purLinkManList[{{idx}}].linkRemarks" type="text" value="{{row.linkRemarks}}" readonly="true"   class="form-control "/>
					</td>
					
		<!--		<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#purLinkManList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>    -->
				</tr>//-->
			</script>
			<script type="text/javascript">
				var purLinkManRowIdx = 0, purLinkManTpl = $("#purLinkManTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(puraccount.purLinkManList)};
					for (var i=0; i<data.length; i++){
						addRow('#purLinkManList', purLinkManRowIdx, purLinkManTpl, data[i]);
						purLinkManRowIdx = purLinkManRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${fns:hasPermission('archives:puraccount:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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
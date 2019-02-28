<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#puraccountTable').bootstrapTable({
		 
		  //请求方法
               method: 'get',
               //类型json
               dataType: "json",
               //显示刷新按钮
               showRefresh: true,
               //显示切换手机试图按钮
               showToggle: true,
               //显示 内容列下拉框
    	       showColumns: true,
    	       //显示到处按钮
    	       showExport: true,
    	       //显示切换分页按钮
    	       showPaginationSwitch: true,
    	       //显示详情按钮
    	       detailView: true,
    	       	//显示详细内容函数
	           detailFormatter: "detailFormatter",
    	       //最低显示2行
    	       minimumCountColumns: 2,
               //是否显示行间隔色
               striped: true,
               //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）     
               cache: false,    
               //是否显示分页（*）  
               pagination: true,   
                //排序方式 
               sortOrder: "asc",  
               //初始化加载第一页，默认第一页
               pageNumber:1,   
               //每页的记录行数（*）   
               pageSize: 10,  
               //可供选择的每页的行数（*）    
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/archives/puraccount/data",
               //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
               //queryParamsType:'',   
               ////查询参数,每次调用是会带上这个参数，可自定义                         
               queryParams : function(params) {
               	var searchParam = $("#searchForm").serializeJSON();
               	searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
               	searchParam.pageSize = params.limit === undefined? -1 : params.limit;
               	searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                   return searchParam;
               },
               //分页方式：client客户端分页，server服务端分页（*）
               sidePagination: "server",
               contextMenuTrigger:"right",//pc端 按右键弹出菜单
               contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
               contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	window.location = "${ctx}/archives/puraccount/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该供应商档案记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/archives/puraccount/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#puraccountTable').bootstrapTable('refresh');
                   	  			jp.success(data.msg);
                   	  		}else{
                   	  			jp.error(data.msg);
                   	  		}
                   	  	})
                   	   
                   	});
                      
                   } 
               },
              
               onClickRow: function(row, $el){
               },
               columns: [{
		        checkbox: true
		       
		    }
			,{
		        field: 'accountCode',
		        title: '企业编码',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/archives/puraccount/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'accountName',
		        title: '企业名称',
		        sortable: true
		       
		    }
			,{
		        field: 'areaCode',
		        title: '地区编码',
		        sortable: true
		       
		    }
			,{
		        field: 'areaName',
		        title: '地区名称',
		        sortable: true
		       
		    }
			,{
		        field: 'accountProp',
		        title: '性质',
		        sortable: true
		       
		    }
			,{
		        field: 'accountMgr',
		        title: '法人',
		        sortable: true
		       
		    }
			,{
		        field: 'subTypeCode',
		        title: '类别编码',
		        sortable: true
		       
		    }
			,{
		        field: 'subTypeName',
		        title: '类别名称',
		        sortable: true
		       
		    }
			,{
		        field: 'supHigherUp',
		        title: '上级部门',
		        sortable: true
		       
		    }
			,{
		        field: 'accountAddr',
		        title: '联系地址',
		        sortable: true
		       
		    }
			,{
		        field: 'postCode',
		        title: '邮编',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#puraccountTable').bootstrapTable("toggleView");
		}
	  
	  $('#puraccountTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#puraccountTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#puraccountTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/archives/puraccount/import/template';
				  },
			    btn2: function(index, layero){
				        var inputForm =top.$("#importForm");
				        var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				        inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				        inputForm.onsubmit = function(){
				        	jp.loading('  正在导入，请稍等...');
				        }
				        inputForm.submit();
					    jp.close(index);
				  },
				 
				  btn3: function(index){ 
					  jp.close(index);
	    	       }
			}); 
		});
		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#puraccountTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#puraccountTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#puraccountTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该供应商档案记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/archives/puraccount/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#puraccountTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/archives/puraccount/form?id=" + getIdSelections();
  }
  
  



  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#puraccountChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/archives/puraccount/detail?id="+row.id, function(puraccount){
			
    	var puraccountChild1RowIdx = 0, puraccountChild1Tpl = $("#puraccountChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  puraccount.purContractMainList;

		if(!jQuery.isEmptyObject(data1)){
			for (var i=0; i<data1.length; i++){
                var billStateFlag = data1[i].billStateFlag;

                if (billStateFlag == "A") {
                    data1[i].billStateFlag = "正在录入/修改";
                } else if (billStateFlag == "W") {
                    data1[i].billStateFlag = "录入完毕";

                } else if (billStateFlag == "E") {
                    data1[i].billStateFlag = "审批通过";

                } else if (billStateFlag == "B") {
                    data1[i].billStateFlag = "审批未通过";
                } else if (billStateFlag == "V") {
                    data1[i].billStateFlag = "作废";
                } else if (billStateFlag == "O") {
                    data1[i].billStateFlag = "已结案";
                } else if (billStateFlag == "D") {
                    data1[i].billStateFlag = "冻结";
                }
				addRow('#puraccountChild-'+row.id+'-1-List', puraccountChild1RowIdx, puraccountChild1Tpl, data1[i]);
				puraccountChild1RowIdx = puraccountChild1RowIdx + 1;
			}
		}		
    	var puraccountChild2RowIdx = 0, puraccountChild2Tpl = $("#puraccountChild2Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data2 =  puraccount.pinvCheckMainList;
		if(!jQuery.isEmptyObject(data2)){
			for (var i=0; i<data2.length; i++){
				addRow('#puraccountChild-'+row.id+'-2-List', puraccountChild2RowIdx, puraccountChild2Tpl, data2[i]);
				puraccountChild2RowIdx = puraccountChild2RowIdx + 1;
			}
		}					
    	var puraccountChild3RowIdx = 0, puraccountChild3Tpl = $("#puraccountChild3Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data3 =  puraccount.purLinkManList;	
		if(!jQuery.isEmptyObject(data3)){
			for (var i=0; i<data3.length; i++){
				addRow('#puraccountChild-'+row.id+'-3-List', puraccountChild3RowIdx, puraccountChild3Tpl, data3[i]);
				puraccountChild3RowIdx = puraccountChild3RowIdx + 1;
			}
		}	  			
      })
     
        return html;
    }
  
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}
			
</script>
<script type="text/template" id="puraccountChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
		
		<!--		<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">采购合同父表</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-2" aria-expanded="true">入库通知单</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-3" aria-expanded="true">联系人档案</a></li>
		-->
				
				<li><a data-toggle="tab" href="#tab-{{idx}}-3" aria-expanded="true">联系人</a></li>
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">合同信息</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-2" aria-expanded="true">到货信息</a></li>
				
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
					<!--
								<th>单据号</th>
								<th>单据类型</th>
								<th>制单日期</th>
								<th>制单人名称</th>
								<th>供应商名称</th>
								<th>采购员名称</th>
								<th>运费总额</th>
								<th>不含税总额</th>
								<th>含税总额</th>
								<th>合同说明</th>
								<th>合同状态</th>
								<th>合同保证金</th>
						-->
								<th>日期</th>
								<th>合同号</th>
								<th>合同状态</th>
								<th>合同类型</th>
								<th>供应商名称</th>
								<th>签订人</th>
								<th>采购员</th>
								<th>总额（不含税）</th>
								<th>总额（含税）</th>
								<th>运费总额</th>
								<th>履约保证金</th>
								<th>说明</th>
								
							</tr>
						</thead>
						<tbody id="puraccountChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-2" class="tab-pane fade">
					<table class="ani table">
						<thead>
				<!--		<tr>
								<th>单据编号</th>
								<th>单据类型</th>
								<th>制单日期</th>
								<th>制单人名称</th>
								<th>采购员名称</th>
								<th>供应商名称</th>
								<th>仓库名称</th>
								<th>仓库处理标志</th>
								<th>估价标记</th>
								<th>运费合计</th>
								<th>金额合计</th>
								<th>小计金额</th>
    							<th>运费合计</th>
							</tr>	-->
							<tr>
								<th>日期</th>
								<th>到货单号</th>
								<th>到货状态</th>
								<th>单据类型</th>
								<th>供应商名称</th>
								<th>采购员</th>
						
								<th>接收仓库</th>
								<th>估价标志</th>
								<th>制单人</th>
								<th>小计金额</th>
								
								

								<th>金额合计</th>
								
							</tr>
						</thead>
						<tbody id="puraccountChild-{{idx}}-2-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-3" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>联系人编号</th>
								<th>联系人姓名</th>
								<th width="80">性别</th>
								<th>级别</th>
								<th>职位</th>
								<th>电话</th>
								<th>手机</th>
								<th>mail</th>
								<th>状态</th>
							</tr>
						</thead>
						<tbody id="puraccountChild-{{idx}}-3-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="puraccountChild1Tpl">//<!--
				<tr>
				<!--
					<td>
						{{row.billNum}}
					</td>
					<td>
						{{row.billType}}
					</td>
					<td>
						{{row.makeDate}}
					</td>
					<td>
						{{row.makeEmpname}}
					</td>
					<td>
						{{row.supName}}
					</td>
					<td>
						{{row.buyerName}}
					</td>
					<td>
						{{row.tranprice}}
					</td>
					<td>
						{{row.goodsSum}}
					</td>
					<td>
						{{row.goodsSumTaxed}}
					</td>
					<td>
						{{row.dealNote}}
					</td>
					<td>
						{{row.contrState}}
					</td>
					<td>
						{{row.contractNeedSum}}
					</td>
				-->

				<td>
					{{row.makeDate}}
				</td>
				
				<td>
					{{row.billNum}}
				</td>
				
				<td>
					{{row.billStateFlag}}
				</td>
				
				<td>
					{{row.contypeName}}
				</td>
				
				<td>
					{{row.supName}}
				</td>

				<td>
					{{row.makeEmpname}}
				</td>

				<td>
					{{row.buyerName}}
				</td>
				

				<td>
					{{row.goodsSum}}
				</td>
				<td>
					{{row.goodsSumTaxed}}
				</td>
				
				<td>
					{{row.tranprice}}
				</td>
				
				<td>
					{{row.contractNeedSum}}
				</td>
				
				<td>
					{{row.dealNote}}
				</td>

			
				</tr>//-->
	</script>
	<script type="text/template" id="puraccountChild2Tpl">//<!--
		<!--		<tr>
					<td>
						{{row.billnum}}
					</td>
					<td>
						{{row.billType}}
					</td>
					<td>
						{{row.makeDate}}
					</td>
					<td>
						{{row.makeEmpname}}
					</td>
					<td>
						{{row.buyerName}}
					</td>
					<td>
						{{row.supName}}
					</td>
					<td>
						{{row.wareName}}
					</td>
					<td>
						{{row.invFlag}}
					</td>
					<td>
						{{row.thFlag}}
					</td>
					<td>
						{{row.tranpriceSum}}
					</td>
					<td>
						{{row.priceSum}}
					</td>
					<td>
						{{row.priceSumSubtotal}}
					</td>
				</tr>		-->
				
				<tr>
					<td>
						{{row.makeDate}}
					</td>
			
					<td>
						{{row.billnum}}
					</td>
					
					<td>
						{{row.invFlag}}
					</td>
					
					<td>
						{{row.billType}}
					</td>
					
					<td>
						{{row.supName}}
					</td>
					
					<td>
						{{row.buyerName}}
					</td>
					
				<td>
					{{row.wareName}}
				</td>

				<td>
					{{row.thFlag}}
				</td>
				
				<td>
					{{row.makeEmpname}}
				</td>
				<td>
					{{row.priceSumSubtotal}}
				</td>


				<td>
					{{row.tranpriceSum}}
				</td>

				<td>
					{{row.priceSum}}
				</td>
				
			</tr>
				
				//-->
	</script>
	<script type="text/template" id="puraccountChild3Tpl">//<!--
				<tr>
					<td>
						{{row.linkCode}}
					</td>
					<td>
						{{row.linkName}}
					</td>
					<td width="80">
						{{row.linkSex}}
					</td>
					<td>
						{{row.linkLevel}}
					</td>
					<td>
						{{row.linkPosition}}
					</td>
					<td>
						{{row.linkTel}}
					</td>
					<td>
						{{row.linkPhone}}
					</td>
					<td>
						{{row.linkMail}}
					</td>
					<td>
						{{row.linkRemarks}}
					</td>
				</tr>//-->
	</script>

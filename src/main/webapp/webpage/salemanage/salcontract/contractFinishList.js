<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#contractTable').bootstrapTable({
		 
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
    	       //detailView: true,
    	       	//显示详细内容函数
	          // detailFormatter: "detailFormatter",
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
               url: "${ctx}/salcontract/contract/data",
               //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
               //queryParamsType:'',   
               ////查询参数,每次调用是会带上这个参数，可自定义                         
               queryParams : function(params) {
               	var searchParam = $("#searchForm").serializeJSON();
               	searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
               	searchParam.pageSize = params.limit === undefined? -1 : params.limit;
               	searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
               	searchParam.status="O";
                   return searchParam;
               },
               //分页方式：client客户端分页，server服务端分页（*）
               sidePagination: "server",
               contextMenuTrigger:"right",//pc端 按右键弹出菜单
               contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
               contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	window.location = "${ctx}/salcontract/contract/formSearch?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该销售合同记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/salcontract/contract/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#contractTable').bootstrapTable('refresh');
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
		        field: 'contractCode',
		        title: '合同编码',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/salcontract/contract/formFinish?id="+row.id+"'>"+value+"</a>";
		         }
		    }
			,{
		        field: 'signDate',
		        title: '签订日期',
		        sortable: true
		       
		    }
			,{
		        field: 'contractType.ctrTypeCode',
		        title: '合同类型编码',
		        sortable: true
		       
		    }
			,{
		        field: 'contractType.ctrTypeName',
		        title: '合同类型名称',
		        sortable: true
		       
		    }
			,{
		        field: 'account.accountCode',
		        title: '客户编码',
		        sortable: true
		       
		    }
			,{
		        field: 'account.accountName',
		        title: '客户名称',
		        sortable: true
		       
		    }
			,{
		        field: 'blncType.blncTypeCode',
		        title: '结算方式编码',
		        sortable: true
		       
		    }
			,{
		        field: 'blncType.blncTypeName',
		        title: '结算方式名称',
		        sortable: true
		       
		    }
			,{
		        field: 'person.no',
		        title: '销售人员编码',
		        sortable: true
		       
		    }
			,{
		        field: 'person.name',
		        title: '销售人员姓名',
		        sortable: true
		       
		    }
			,{
		        field: 'deliveryDate',
		        title: '交货日期',
		        sortable: true
		       
		    }
			,{
		        field: 'inputDate',
		        title: '制单日期',
		        sortable: true
		       
		    }
			,{
		        field: 'inputPerson.no',
		        title: '制单人编码',
		        sortable: true
		       
		    },{
		        field: 'inputPerson.name',
		        title: '制单人名称',
		        sortable: true
		       
		    }
			,{
		        field: 'contractStat',
		        title: '合同状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('contractStat'))}, value, "-");
		        }
		    }
			,{
		        field: 'endReason',
		        title: '结案原因',
		        sortable: true
		       
		    }
			,{
		        field: 'endPerson.no',
		        title: '结案人编码',
		        sortable: true
		       
		    }
			,{
		        field: 'endPerson.name',
		        title: '结案人姓名',
		        sortable: true
		       
		    }
			,{
		        field: 'endDate',
		        title: '结案日期',
		        sortable: true
		       
		    }
			,{
		        field: 'payType.typeCode',
		        title: '付款方式编码',
		        sortable: true
		       
		    }
			,{
		        field: 'payType.typeName',
		        title: '付款方式名称',
		        sortable: true
		       
		    }
			,{
		        field: 'saleTaxRatio.salTaxRatio',
		        title: '税率',
		        sortable: true
		       
		    }
			,{
		        field: 'tranpricePayer',
		        title: '运费承担方',
		        sortable: true
		       
		    }
			,{
		        field: 'transfeeSum',
		        title: '运费总额',
		        sortable: true
		       
		    }
			,{
		        field: 'goodsSum',
		        title: '不含税总额',
		        sortable: true
		       
		    }
			,{
		        field: 'goodsSumTaxed',
		        title: '含税总额',
		        sortable: true
		       
		    }
			,{
		        field: 'itemContents',
		        title: '合同文本',
		        sortable: true
		       
		    }
			,{
		        field: 'paperCtrCode',
		        title: '纸制合同号',
		        sortable: true
		       
		    }
			,{
		        field: 'praRemark',
		        title: '备注',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#contractTable').bootstrapTable("toggleView");
		}
	  
	  $('#contractTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#contractTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#contractTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/salcontract/contract/import/template';
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
		  $('#contractTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#contractTable').bootstrapTable('refresh');
		});
		
				$('#beginSignDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endSignDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#contractTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该销售合同记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/salcontract/contract/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#contractTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/salcontract/contract/formSearch?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#contractChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/salcontract/contract/detail?id="+row.id, function(contract){
    	var contractChild1RowIdx = 0, contractChild1Tpl = $("#contractChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  contract.ctrFIleList;
		for (var i=0; i<data1.length; i++){
			addRow('#contractChild-'+row.id+'-1-List', contractChild1RowIdx, contractChild1Tpl, data1[i]);
			contractChild1RowIdx = contractChild1RowIdx + 1;
		}
				
    	var contractChild2RowIdx = 0, contractChild2Tpl = $("#contractChild2Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data2 =  contract.ctrItemList;
		for (var i=0; i<data2.length; i++){
			addRow('#contractChild-'+row.id+'-2-List', contractChild2RowIdx, contractChild2Tpl, data2[i]);
			contractChild2RowIdx = contractChild2RowIdx + 1;
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
<script type="text/template" id="contractChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">合同附件</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-2" aria-expanded="true">销售合同子表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>合同编码</th>
								<th>附件编号</th>
								<th>附件名称</th>
								<th>附件路径</th>
							</tr>
						</thead>
						<tbody id="contractChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-2" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>合同编码</th>
								<th>订单编号</th>
								<th>产品编码</th>
								<th>产品名称</th>
								<th>规格型号</th>
								<th>单位名称</th>
								<th>签订量</th>
								<th>无税单价</th>
								<th>无税总额</th>
								<th>含税单价	</th>
								<th>含税金额</th>
								<th>发货量</th>
								<th>运费价格</th>
								<th>运费金额</th>
								<th>订单期间</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="contractChild-{{idx}}-2-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="contractChild1Tpl">//<!--
				<tr>
					<td>
						{{row.contract.id}}
					</td>
					<td>
						{{row.fileCode}}
					</td>
					<td>
						{{row.fileName}}
					</td>
					<td>
						{{row.filePath}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="contractChild2Tpl">//<!--
				<tr>
					<td>
						{{row.contract.id}}
					</td>
					<td>
						{{row.itemCode}}
					</td>
					<td>
						{{row.prodCode}}
					</td>
					<td>
						{{row.prodName}}
					</td>
					<td>
						{{row.prodSpec}}
					</td>
					<td>
						{{row.unitName}}
					</td>
					<td>
						{{row.prodQty}}
					</td>
					<td>
						{{row.prodPrice}}
					</td>
					<td>
						{{row.prodSum}}
					</td>
					<td>
						{{row.prodPriceTaxed}}
					</td>
					<td>
						{{row.prodSumTaxed}}
					</td>
					<td>
						{{row.pickQty}}
					</td>
					<td>
						{{row.transPrice}}
					</td>
					<td>
						{{row.transSum}}
					</td>
					<td>
						{{row.periodId}}
					</td>
					<td>
						{{row.remark}}
					</td>
				</tr>//-->
	</script>

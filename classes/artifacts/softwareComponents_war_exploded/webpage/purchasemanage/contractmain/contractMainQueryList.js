<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#contractMainTable').bootstrapTable({
		 
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
               url: "${ctx}/contractmain/contractMainQuery/data",
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
         //      contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	window.location = "${ctx}/contractmain/contractMainQuery/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该采购合同管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/contractmain/contractMain/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#contractMainTable').bootstrapTable('refresh');
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
		        field: 'billNum',
		        title: '合同号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/contractmain/contractMainQuery/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'bdate',
		        title: '签订日期',
		        sortable: true,
		        formatter:function(value, row , index){
				       	if(value==undefined) {
				       		return "";}
				       	else{
				       	 return value.split(" ")[0];}
			    }
		       
		    }
		/*	,{
		        field: 'contrState',
		        title: '合同状态',
		        sortable: true,
		        formatter:function(value, row , index){
			      	return jp.getDictLabel(${fns:toJson(fns:getDictList('contrState'))}, value, "-");
			      }
		    }*/
			,{
		        field: 'billStateFlag',
		        title: '合同状态',
		        sortable: true,
		        formatter:function(value, row , index){
			      	return jp.getDictLabel(${fns:toJson(fns:getDictList('ContractStateFlag'))}, value, "-");
			      }
		    }
			,{
		        field: 'contypeName',
		        title: '合同类型名称',
		        sortable: true,
		     //   formatter:function(value, row , index){
		     //   	return jp.getDictLabel(${fns:toJson(fns:getDictList('ContypeName'))}, value, "-");
		     //   }
		       
		    }
			
			,{
		        field: 'user.no',
		        title: '制单人编号',
		        sortable: true
		       
		    }
			,{
		        field: 'makeEmpname',
		        title: '制单人名称',
		        sortable: true
		       
		    }
			,{
		        field: 'account.accountCode',
		        title: '供应商编号',
		        sortable: true
		       
		    }
			,{
		        field: 'supName',
		        title: '供应商名称',
		        sortable: true
		       
		    }
			,{
		        field: 'supAddress',
		        title: '供应商地址',
		        sortable: true
		       
		    }
		/*	,{
		        field: 'billType',
		        title: '单据类型',
		        sortable: true
		       
		    }*/
			,{
		        field: 'contrState',
		        title: '单据类型',
		        sortable: true,
		        formatter:function(value, row , index){
			      	return jp.getDictLabel(${fns:toJson(fns:getDictList('contrState'))}, value, "-");
			      }
		    }
			,{
		        field: 'taxRatio',
		        title: '税率',
		        sortable: true,
		        formatter:function(value, row , index){
		        	value=value*100+"%";
		        	return value;
		    }
		       
		    }
			,{
		        field: 'groupBuyer.user.no',
		        title: '采购员编号',
		        sortable: true
		       
		    }
			,{
		        field: 'buyerName',
		        title: '采购员名称',
		        sortable: true
		       
		    }
			,{
		        field: 'paymentNotes',
		        title: '付款说明',
		        sortable: true
		       
		    }
			,{
		        field: 'advancePay',
		        title: '预付额款',
		        sortable: true
		       
		    }
			,{
		        field: 'advanceDate',
		        title: '预付日期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	if(value==undefined) {
			       		return "";}
			       	else{
			       	 return value.split(" ")[0];}
		    }
		       
		    }
			,{
		        field: 'endDate',
		        title: '截止日期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	if(value==undefined) {
			       		return "";}
			       	else{
			       	 return value.split(" ")[0];}
		    }
		       
		    }
			,{
		        field: 'tranmodeName',
		        title: '运输方式名称',
		        sortable: true		        
		       
		    }
			,{
		        field: 'tranpricePayer',
		        title: '运费承担方',
		        sortable: true
		       
		    }
			,{
		        field: 'tranprice',
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
			/*,{
		        field: 'dealNote',
		        title: '合同说明',
		        sortable: true
		       
		    }*/
			,{
		        field: 'transContractNum',
		        title: '运输协议号',
		        sortable: true
		       
		    }
			,{
		        field: 'billNotes',
		        title: '单据说明',
		        sortable: true
		       
		    }
			
			,{
		        field: 'paymodeName',
		        title: '付款方式名称',
		        sortable: true		      
		    }
			
			,{
		        field: 'contractNeedSum',
		        title: '合同保证金',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#contractMainTable').bootstrapTable("toggleView");
		}
	  
	  $('#contractMainTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#contractMainTable').bootstrapTable('getSelections').length);
            $('#detail').prop('disabled', $('#contractMainTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/contractmain/contractMain/import/template';
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
		  $('#contractMainTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#contractMainTable').bootstrapTable('refresh');
		});
		
				$('#beginBdate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#endBdate').datetimepicker({
					 format: "YYYY-MM-DD"
				});				
	});
		
  function getIdSelections() {
        return $.map($("#contractMainTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该采购合同管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/contractmain/contractMain/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#contractMainTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function detail(){
	  
	  window.location = "${ctx}/contractmain/contractMainQuery/form?id=" + getIdSelections();
  }
  
  //采购员标签内函数
  function selectPlan(buyerId){		
  }
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#contractMainChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/contractmain/contractMainQuery/detail?id="+row.id, function(contractMain){
    	var contractMainChild1RowIdx = 0, contractMainChild1Tpl = $("#contractMainChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  contractMain.contractDetailList;
		for (var i=0; i<data1.length; i++){
			addRow('#contractMainChild-'+row.id+'-1-List', contractMainChild1RowIdx, contractMainChild1Tpl, data1[i]);
			contractMainChild1RowIdx = contractMainChild1RowIdx + 1;
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
<script type="text/template" id="contractMainChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">采购合同子表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>序号</th>
								<th>物料编号</th>
								<th>物料名称</th>
								<th>物料规格</th>
								<th>物料材质</th>
								<th>数量</th>
								<th>无税单价</th>
								<th>无税总额</th>
								<th>含税单价</th>
								<th>含税金额</th>
								<th>单位</th>
								<th>运费价格</th>
								<th>运费金额</th>
								<th>质量要求</th>	
							</tr>
						</thead>
						<tbody id="contractMainChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="contractMainChild1Tpl">//<!--
				<tr>
				    
				    <td>
						{{row.serialNum}}
					</td>
					<td>
						{{row.item.code}}
					</td>
					<td>
						{{row.itemName}}
					</td>
					<td>
					    {{row.itemModel}}
				    </td>
				    <td>
				     	{{row.itemTexture}}
				    </td>
					<td>
						{{row.itemQty}}
					</td>
					<td>
						{{row.itemPrice}}
					</td>
					<td>
						{{row.itemSum}}
					</td>
					<td>
						{{row.itemPriceTaxed}}
					</td>
					<td>
						{{row.itemSumTaxed}}
					</td>
					<td>
						{{row.measUnit}}
					</td>				
					<td>
						{{row.transPrice}}
					</td>
					<td>
						{{row.transSum}}
					</td>
					<td>
					    {{row.massRequire}}
				    </td>
				</tr>//-->
	</script>

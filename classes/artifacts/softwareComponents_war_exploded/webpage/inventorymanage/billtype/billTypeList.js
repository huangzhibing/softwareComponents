<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#billTypeTable').bootstrapTable({
		 
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
               url: "${ctx}/billtype/billType/data",
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
                   	window.location = "${ctx}/billtype/billType/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该单据类型定义记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/billtype/billType/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#billTypeTable').bootstrapTable('refresh');
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
		        field: 'ioType',
		        title: '出入库类型',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/billtype/billType/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'ioFlag',
		        title: '出入库标识',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billType'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'ioDesc',
		        title: '描述',
		        sortable: true
		       
		    }
			,{
		        field: 'currQty',
		        title: '影响现有量',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'currTotalQty',
		        title: '影响总现有量',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'beginQty',
		        title: '影响期初量',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'inQty',
		        title: '影响累计入库',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'outQty',
		        title: '影响累计出库',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'wasteQty',
		        title: '影响废品量',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'waitQty',
		        title: '影响待验量',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'assignQty',
		        title: '影响已分配量',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'wshopQty',
		        title: '影响车间任务',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'wshopUse',
		        title: '影响车间领用',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'wshopCost',
		        title: '影响生产成本',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'purAssign',
		        title: '影响采购任务',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'sellOrder',
		        title: '影响销售订单',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'costPrice',
		        title: '影响移动平均价',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('influenceCostPrice'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'accountId',
		        title: '科目代码',
		        sortable: true
		       
		    }
			,{
		        field: 'accountName',
		        title: '科目名称',
		        sortable: true
		       
		    }
			,{
		        field: 'dinQty',
		        title: '影响调拨入库',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'doutQty',
		        title: '影响调拨出库',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'pinQty',
		        title: '影响盘盈入库',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'poutQty',
		        title: '影响盘亏出库',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'tinQty',
		        title: '影响调整入库',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'toutQty',
		        title: '影响调整出库',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billInfluence'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'note',
		        title: '备注',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#billTypeTable').bootstrapTable("toggleView");
		}
	  
	  $('#billTypeTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#billTypeTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#billTypeTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/billtype/billType/import/template';
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
		  $('#billTypeTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#billTypeTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#billTypeTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该单据类型定义记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/billtype/billType/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#billTypeTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/billtype/billType/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#billTypeChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/billtype/billType/detail?id="+row.id, function(billType){
    	var billTypeChild1RowIdx = 0, billTypeChild1Tpl = $("#billTypeChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  billType.billTypeWareHouseList;
		for (var i=0; i<data1.length; i++){
			addRow('#billTypeChild-'+row.id+'-1-List', billTypeChild1RowIdx, billTypeChild1Tpl, data1[i]);
			billTypeChild1RowIdx = billTypeChild1RowIdx + 1;
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
<script type="text/template" id="billTypeChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">单据类型与仓库关系表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>库房号</th>
								<th>库房名称</th>
								<th>管理标识</th>
							</tr>
						</thead>
						<tbody id="billTypeChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="billTypeChild1Tpl">//<!--
				<tr>
					<td>
						{{row.wareHouse.wareID}}
					</td>
					<td>
						{{row.wareName}}
					</td>
					<td>
						{{row.adminMode}}
					</td>
				</tr>//-->
	</script>

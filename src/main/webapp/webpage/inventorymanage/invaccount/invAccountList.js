<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#invAccountTable').bootstrapTable({
		 
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
               url: "${ctx}/invaccount/invAccount/data",
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
                   	window.location = "${ctx}/invaccount/invAccount/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该库存帐记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/invaccount/invAccount/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#invAccountTable').bootstrapTable('refresh');
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
		        field: 'ware.wareID',
		        title: '仓库编号',
		        sortable: true
		        ,formatter:function(value, row , index){
		            if(value == null){
		            	return "<a href='${ctx}/invaccount/invAccount/form?id="+row.id+"'>-</a>";
		            }else{
		            	return "<a href='${ctx}/invaccount/invAccount/form?id="+row.id+"'>"+value+"</a>";
		            }
		        }
		       
		    }
			,{
		        field: 'item.code',
		        title: '物料编号',
		        sortable: true
		       
		    }
			   ,{
				   field: 'item.name',
				   title: '物料名称',
				   sortable: true

			   }
			,{
		        field: 'year',
		        title: '会计年度',
		        sortable: true
		       
		    }
			,{
		        field: 'period',
		        title: '会计期间',
		        sortable: true
		       
		    }
			,{
		        field: 'beginQty',
		        title: '期初数量',
		        sortable: true
		       
		    }
			,{
		        field: 'beginSum',
		        title: '期初金额',
		        sortable: true
		        ,formatter:function(value, row , index){
                	if(value!=null){
                		console.log(value);
                		return value.toFixed(2);
                	}
		         }
		       
		    }
			,{
		        field: 'currInQty',
		        title: '本期入库量',
		        sortable: true
		        
		       
		    }
			,{
		        field: 'currInSum',
		        title: '入库金额',
		        sortable: true
		        ,formatter:function(value, row , index){
                	if(value!=null){
                		console.log(value);
                		return value.toFixed(2);
                	}
		         }
		       
		    }
			,{
		        field: 'currOutQty',
		        title: '本期出库量',
		        sortable: true
		       
		    }
			,{
		        field: 'currOutSum',
		        title: '出库金额',
		        sortable: true
		        ,formatter:function(value, row , index){
                	if(value!=null){
                		console.log(value);
                		return value.toFixed(2);
                	}
		         }
		       
		    }
			,{
		        field: 'nowQty',
		        title: '现有量',
		        sortable: true
		       
		    }
			,{
		        field: 'nowSum',
		        title: '现有金额',
		        sortable: true
		        ,formatter:function(value, row , index){
                	if(value!=null){
                		console.log(value);
                		return value.toFixed(2);
                	}
		         }
		       
		    }
			,{
		        field: 'costPrice',
		        title: '单价',
		        sortable: true
		        ,formatter:function(value, row , index){
                	if(value!=null){
                		console.log(value);
                		return value.toFixed(2);
                	}
		         }
		       
		    }
			,{
		        field: 'costTax',
		        title: '差异率',
		        sortable: true
		       
		    }
			,{
		        field: 'beginSub',
		        title: '期初差异',
		        sortable: true
		       
		    }
			,{
		        field: 'currInSub',
		        title: '收入差异',
		        sortable: true
		       
		    }
			,{
		        field: 'currOutSub',
		        title: '发出差异',
		        sortable: true
		       
		    }
			,{
		        field: 'nowSub',
		        title: '结存差异',
		        sortable: true
		       
		    }
            // ,{
		    //     field: 'minCost',
		    //     title: '最小入库单价',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'maxCost',
		    //     title: '最大入库单价',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'companyCode',
		    //     title: '公司机构类码',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'tinSum',
		    //     title: '调整入库',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'toutSum',
		    //     title: '调整出库',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'dinQty',
		    //     title: '调拨入库数量',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'dinSum',
		    //     title: '调拨入库金额',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'doutQty',
		    //     title: '调拨出库数量',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'doutSum',
		    //     title: '调拨出库金额',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'pinQty',
		    //     title: '盘点入库数量',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'pinSum',
		    //     title: '盘点入库金额',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'poutQty',
		    //     title: '盘点出库数量',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'poutSum',
		    //     title: '盘点出库金额',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'qinQty',
		    //     title: '其他入库数量',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'qinSum',
		    //     title: '其他入库金额',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'qoutQty',
		    //     title: '其他出库数量',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'qoutSum',
		    //     title: '其他出库金额',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'checkFlag',
		    //     title: '盘点标志',
		    //     sortable: true,
		    //     formatter:function(value, row , index){
		    //     	return jp.getDictLabel(${fns:toJson(fns:getDictList('checkFlag'))}, value, "-");
		    //     }
		    //
		    // }
            // ,{
		    //     field: 'toolFlag',
		    //     title: '工具标志',
		    //     sortable: true,
		    //     formatter:function(value, row , index){
		    //     	return jp.getDictLabel(${fns:toJson(fns:getDictList('toolFlag'))}, value, "-");
		    //     }
		    //
		    // }
			,{
		        field: 'bin.binId',
		        title: '货区编号',
		        sortable: true
		       
		    }
			,{
		        field: 'loc.locId',
		        title: '货位编号',
		        sortable: true
		       
		    }
			,{
		        field: 'itemBatch',
		        title: '批次号',
		        sortable: true
		       
		    }
            // ,{
		    //     field: 'corId',
		    //     title: '供应商编号',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'deptId',
		    //     title: '部门编号',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'applyCode',
		    //     title: '需求号',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'orderCode',
		    //     title: '订单号',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'operNo',
		    //     title: '工序号',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'sourceFlag',
		    //     title: '自制外协标记',
		    //     sortable: true,
		    //     formatter:function(value, row , index){
		    //     	return jp.getDictLabel(${fns:toJson(fns:getDictList('sourceFlag'))}, value, "-");
		    //     }
		    //
		    // }
			,{
		        field: 'salQty',
		        title: '待发货数量',
		        sortable: true
		       
		    }
			,{
		        field: 'purQty',
		        title: '待入库数量',
		        sortable: true
		       
		    }
			,{
		        field: 'dinTranQty',
		        title: '调拨在途数量',
		        sortable: true
		       
		    }
			,{
		        field: 'doutTranQty',
		        title: '调拨待发数量',
		        sortable: true
		       
		    }
			,{
		        field: 'sfcQty',
		        title: '计划备料数量',
		        sortable: true
		       
		    }
			// ,{
		     //    field: 'qmsQty',
		     //    title: '不合格数量',
		     //    sortable: true
		     //
		    // }
			,{
		        field: 'realQty',
		        title: '可用数量',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#invAccountTable').bootstrapTable("toggleView");
		}
	  
	  $('#invAccountTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#invAccountTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#invAccountTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/invaccount/invAccount/import/template';
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
		  $('#invAccountTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#invAccountTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#invAccountTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该库存帐记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/invaccount/invAccount/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#invAccountTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/invaccount/invAccount/form?id=" + getIdSelections();
  }
  
</script>
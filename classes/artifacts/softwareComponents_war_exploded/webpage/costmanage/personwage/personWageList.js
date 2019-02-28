<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#personWageTable').bootstrapTable({
		 
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
               url: "${ctx}/personwage/personWage/data?flag=${flag}",
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
                   	window.location = "${ctx}/personwage/personWage/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该人工工资录入等记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/personwage/personWage/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#personWageTable').bootstrapTable('refresh');
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
		        field: 'workCode.workProcedureId',
		        title: '工序代码',
		        sortable: true
                       ,formatter:function(value, row , index){
                           return "<a href='${ctx}/personwage/personWage/form?flag=${flag}&id="+row.id+"'>"+value+"</a>";
                       }

                   }
                   ,{
                       field: 'workCode.workProcedureName',
                       title: '工序名称',
                       sortable: true

                   }
            ,{
                 field: 'personCode.no',
                 title: '人员代码',
                 sortable: true

			}
            ,{
                  field: 'personCode.name',
                  title: '人员名称',
                  sortable: true

            }
			,{
		        field: 'itemCode.item.code',
		        title: '产品代码',
		        sortable: true
		       
		    }
                   ,{
                       field: 'itemCode.item.name',
                       title: '产品代码',
                       sortable: true

                   }
			,{
		        field: 'itemUnit',
		        title: '单位计件工资',
		        sortable: true
		       
		    }
			,{
		        field: 'itemQty',
		        title: '工作量',
		        sortable: true
		       
		    }
                   ,{
                       field: '',
                       title: '计件工资',
                       sortable: true
					   ,formatter:function (value,row,index) {
						   return row.itemUnit*row.itemQty;
                       }
                   }
			,{
		        field: 'wageUnit',
		        title: '单位分配工资',
		        sortable: true
		       
		    }
                   ,{
                       field: '',
                       title: '分配工资',
                       sortable: true
                       ,formatter:function (value,row,index) {
                           return row.wageUnit*row.itemQty;
                       }

                   }
                   ,{
                       field: '',
                       title: '应付工资',
                       sortable: true
                       ,formatter:function (value,row,index) {
                           return (row.wageUnit*row.itemQty)+(row.itemUnit*row.itemQty);
                       }

                   }
                   ,{
                       field: 'periodId',
                       title: '核算期',
                       sortable: true

                   }
            // ,{
		    //     field: 'billStatus',
		    //     title: '单据状态',
		    //     sortable: true,
		    //     formatter:function(value, row , index){
		    //     	return jp.getDictLabel(${fns:toJson(fns:getDictList('billStatus'))}, value, "-");
		    //     }
		    //
		    // }
            // ,{
		    //     field: 'billMode',
		    //     title: '单据类型',
		    //     sortable: true,
		    //     formatter:function(value, row , index){
		    //     	return jp.getDictLabel(${fns:toJson(fns:getDictList('billMode'))}, value, "-");
		    //     }
		    //
		    // }
            // ,{
		    //     field: 'checkId',
		    //     title: '稽核人代码',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'checkDate',
		    //     title: '稽核日期',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'checkName',
		    //     title: '稽核人姓名',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'makeId',
		    //     title: '录入人代码',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'makeDate',
		    //     title: '录入日期',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'makeName',
		    //     title: '录入人姓名',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'cosBillNum',
		    //     title: '成本单据号',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'corBillNum',
		    //     title: '对应单据号',
		    //     sortable: true
		    //
		    // }
            // ,{
		    //     field: 'corSeqNo',
		    //     title: '对应的序号',
		    //     sortable: true
		    //
		    // }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#personWageTable').bootstrapTable("toggleView");
		}
	  
	  $('#personWageTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#personWageTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#personWageTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/personwage/personWage/import/template';
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
		  $('#personWageTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#personWageTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#personWageTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该人工工资录入等记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/personwage/personWage/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#personWageTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(flag){
	  window.location = "${ctx}/personwage/personWage/form?id=" + getIdSelections()+"&flag="+flag;
  }
  
</script>
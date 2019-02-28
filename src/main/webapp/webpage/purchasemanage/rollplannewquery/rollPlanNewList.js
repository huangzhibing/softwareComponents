<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#rollPlanNewTable').bootstrapTable({
		 
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
               url: "${ctx}/rollplannewquery/rollPlanNew/data",
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
                   	window.location = "${ctx}/rollplannewquery/rollPlanNew/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该滚动计划记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/rollplannewquery/rollPlanNew/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#rollPlanNewTable').bootstrapTable('refresh');
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
		        field: 'itemCode.code',
		        title: '物料编号',
		        sortable: true
		       
		    }
			,{
		        field: 'itemName',
		        title: '物料名称',
		        sortable: true
		       
		    }
            ,{
                field: 'applyTypeName',
                title: '需求类别',
                sortable: true

            }
		   ,{
			   field: 'applyQty',
			   title: '需求数量',
			   sortable: true

		   }
           ,{
               field: 'unitName',
               title: '单位',
               sortable: true

           }
           ,{
               field: 'itemSpecmodel',
               title: '规格型号',
               sortable: true

           }
		   ,{
			   field: 'requestDate',
			   title: '需求日期',
			   sortable: true,
               formatter(value,row,index){
			     if(value!=null)
			        return jp.dateFormat(value,"yyyy-MM-dd");
               }
		   }
		   ,{
			   field: 'applyDept',
			   title: '需求部门',
			   sortable: true

		   }
		   ,{
			   field: 'billNum',
			   title: '单据编号',
			   sortable: true
		   }
		   ,{
			   field: 'billType',
			   title: '单据类型',
			   sortable: true

		   }
		   /*,{
			   field: 'serialNum',
			   title: '序号',
			   sortable: true

		   }*/

		   ,{
			   field: 'planNum',
			   title: '计划号',
			   sortable: true

		   }
		   ,{
			   field: 'batchLt',
			   title: '提前期',
			   sortable: true

		   }
		   ,{
			   field: 'costPrice',
			   title: '平均价',
			   sortable: true

		   }
	/*	   ,{
			   field: 'planPrice',
			   title: '计划价',
			   sortable: true

		   }*/
		   ,{
			   field: 'planSum',
			   title: '总金额',
			   sortable: true

		   }
		    ,{
			   field: 'sourseFlag',
			   title: '来源',
			   sortable: true,
               formatter:function(value, row , index){
                   return jp.getDictLabel(${fns:toJson(fns:getDictList('rollplan_source_flag'))}, value, "-");
               }

		    }
		   ,{
			   field: 'opFlag',
			   title: '状态',
			   sortable: true,
               formatter:function(value, row , index){
                   return jp.getDictLabel(${fns:toJson(fns:getDictList('rollplan_op_flag'))}, value, "未加入计划");
               }
		   }
           ,{
               field: 'applyQtyNotes',
               title: '需求说明',
               sortable: true

           }
/*
			,{
		        field: 'planArrivedate',
		        title: '计划到达日期',
		        sortable: true
		       
		    }
			,{
		        field: 'notes',
		        title: '说明',
		        sortable: true
		       
		    }
			,{
		        field: 'massRequire',
		        title: '质量要求',
		        sortable: true
		       
		    }

			,{
		        field: 'purStartDate',
		        title: '采购开始日期',
		        sortable: true
		       
		    }
			,{
		        field: 'purArriveDate',
		        title: '计划到货日期',
		        sortable: true
		       
		    }
			,{
		        field: 'purQty',
		        title: '采购数量',
		        sortable: true
		       
		    }
			,{
		        field: 'invQty',
		        title: '库存数量',
		        sortable: true
		       
		    }
			,{
		        field: 'safetyQty',
		        title: '安全库存',
		        sortable: true
		       
		    }
			,{
		        field: 'realQty',
		        title: '实际数量',
		        sortable: true
		       
		    }
			,{
		        field: 'roadQty',
		        title: '在途量',
		        sortable: true
		       
		    }
			,{
		        field: 'makeDate',
		        title: '制单日期',
		        sortable: true
		       
		    }
			,{
		        field: 'applyDeptId.code',
		        title: '需求部门编码',
		        sortable: true
		       
		    }
			,{
		        field: 'applyDept',
		        title: '需求部门名称',
		        sortable: true
		       
		    }
			,{
		        field: 'makeEmpid.no',
		        title: '制单人代码',
		        sortable: true
		       
		    }
			,{
		        field: 'makeEmpname',
		        title: '制单人名称',
		        sortable: true
		       
		    }

			*/

		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#rollPlanNewTable').bootstrapTable("toggleView");
		}
	  
	  $('#rollPlanNewTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#rollPlanNewTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#rollPlanNewTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/rollplannewquery/rollPlanNew/import/template';
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
		  $('#rollPlanNewTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#rollPlanNewTable').bootstrapTable('refresh');
		});
		
		$('#beginRequestDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endRequestDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#beginPlanArrivedate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endPlanArrivedate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#beginPurStartDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endPurStartDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#beginPurArriveDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endPurArriveDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#beginMakeDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endMakeDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		
	});
		
  function getIdSelections() {
        return $.map($("#rollPlanNewTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该滚动计划记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/rollplannewquery/rollPlanNew/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#rollPlanNewTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/rollplannewquery/rollPlanNew/form?id=" + getIdSelections();
  }
  
</script>
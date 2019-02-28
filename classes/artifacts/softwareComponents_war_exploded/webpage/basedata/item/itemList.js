<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#itemTable').bootstrapTable({
		 
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
               url: "${ctx}/item/item/data",
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
                   	window.location = "${ctx}/item/item/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该物料定义记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/item/item/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#itemTable').bootstrapTable('refresh');
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
		        field: 'code',
		        title: '物料编码',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/item/item/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'name',
		        title: '物料名称',
		        sortable: true
		       
		    }
			,{
		        field: 'classCode.classId',
		        title: '物料类型编码',
		        sortable: true
		       
		    }
			,{
		        field: 'className',
		        title: '物料类型名称',
		        sortable: true
		       
		    }
			,{
		        field: 'specModel',
		        title: '规格型号',
		        sortable: true
		       
		    }
			,{
		        field: 'texture',
		        title: '材质',
		        sortable: true
		       
		    }
			,{
		        field: 'gb',
		        title: '国标码',
		        sortable: true
		       
		    }
			,{
		        field: 'drawNO',
		        title: '图号',
		        sortable: true
		       
		    }
			,{
		        field: 'unitCode.unitCode',
		        title: '计量单位代码',
		        sortable: true
		       
		    }
			,{
		        field: 'unit',
		        title: '计量单位名称',
		        sortable: true
		       
		    }
			,{
		        field: 'safetyQty',
		        title: '安全库存量',
		        sortable: true
		       
		    }
			,{
		        field: 'leadTime',
		        title: '采购提前期',
		        sortable: true
		       
		    }
			,{
		        field: 'maxStorage',
		        title: '最大库存',
		        sortable: true
		       
		    }
			,{
		        field: 'minStorage',
		        title: '最小库存',
		        sortable: true
		       
		    }
			,{
		        field: 'planPrice',
		        title: '计划价格',
		        sortable: true
		       
		    }
			,{
		        field: 'systemSign',
		        title: '系统标识',
		        sortable: true,
		        formatter:function(value, row , index){
		        	var valueArray = value.split(",");
		        	var labelArray = [];
		        	for(var i =0 ; i<valueArray.length-1; i++){
		        		labelArray[i] = jp.getDictLabel(${fns:toJson(fns:getDictList('systemSign'))}, valueArray[i], "-");
		        	}
		        	return labelArray.join(",");
		        }
		       
		    }
			,{
		        field: 'isKey',
		        title: '关键件标识',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('is_key'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'itemBatch',
		        title: '采购批量',
		        sortable: true
		       
		    }
			,{
		        field: 'isInMotor',
		        title: '整机标识',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('is_in_motor'))}, value, "-");
		        }
		       
		    },{
                field: 'cycleTime',
                title: '库存检验周期',
                sortable: true

            },{
			   field: 'isPart',
			   title: '半成品标识',
			   sortable: true,
			   formatter:function(value, row , index){
			   		switch (value){
						case 'y':
							return '是';
							break;
						case 'n':
							return '否'
							break;
						default:
							return ''
					}
               }

		   }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#itemTable').bootstrapTable("toggleView");
		}
	  
	  $('#itemTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#itemTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#itemTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/item/item/import/template';
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
		  $('#itemTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#itemTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#itemTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该物料定义记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/item/item/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#itemTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/item/item/form?id=" + getIdSelections();
  }
  
</script>
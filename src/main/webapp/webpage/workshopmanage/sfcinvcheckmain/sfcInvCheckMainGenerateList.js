<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#processRoutineDetailTable').bootstrapTable({
		 
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
               url: "${ctx}/sfcinvcheckmain/sfcInvCheckMain/generateData",
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
               //contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	window.location = "${ctx}/processroutinedetail/processRoutineDetail/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该加工路线单明细记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/processroutinedetail/processRoutineDetail/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#processRoutineDetailTable').bootstrapTable('refresh');
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
               columns: [


			{
		        field: 'mserialno',
		        title: '机器序列号',
		        sortable: true,
                formatter:function(value, row , index){
       					 return "<a href='${ctx}/sfcinvcheckmain/sfcInvCheckMain/generateForm?id="+row.id+"'>"+value+"</a>";
		        }
		       
		    }
		   ,{
			   field:'processbillno',
			   title:'车间计划作业号',
			   sortable:true
		   }
			,{
		        field: 'routinebillno',
		        title: '加工路线单号',
		        sortable: true
		       
		    }
			,{
		        field: 'seqno',
		        title: '单件序号',
		        sortable: true

		    }
			,{
		        field: 'produceno',
		        title: '生产序号',
		        sortable: true
		       
		    }
			,{
		        field: 'prodcode',
		        title: '产品编码',
		        sortable: true

		    }
			,{
		        field: 'prodname',
		        title: '产品名称',
		        sortable: true

		    }
			,{
		        field: 'routingcode',
		        title: '工艺编码',
		        sortable: true
		       
		    }
			,{
		        field: 'routingname',
		        title: '工艺名称',
		        sortable: true

		    }
			/*,{
		        field: 'workprocedurecode',
		        title: '工序编码',
		        sortable: true
		       
		    }
			,{
		        field: 'centercode.centerCode',
		        title: '工作中心代码',
		        sortable: true
		       
		    }
			,{
		        field: 'islastrouting',
		        title: '末道工艺标志',
		        sortable: true
		       
		    }*/
			,{
		        field: 'planbdate',
		        title: '计划生产日期',
		        sortable: true
		       
		    }
			,{
		        field: 'realqty',
		        title: '实际生产数量',
		        sortable: true
		       
		    }

			,{
		        field: 'personincharge',
		        title: '负责人',
		        sortable: true
		       
		    }
			,{
		        field: 'teamcode.teamCode',
		        title: '计划班组',
		        sortable: true
		       
		    }

			,{
		        field: 'shiftname',
		        title: '班次',
		        sortable: true
		       
		    }
			,{
		        field: 'workhour',
		        title: '计划工时',
		        sortable: true
		       
		    }

			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#processRoutineDetailTable').bootstrapTable("toggleView");
		}
	  
	  $('#processRoutineDetailTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#processRoutineDetailTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#processRoutineDetailTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/processroutinedetail/processRoutineDetail/import/template';
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
		  $('#processRoutineDetailTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#processRoutineDetailTable').bootstrapTable('refresh');
		});
		
		$('#beginPlanbdate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endPlanbdate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#beginPlanedate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endPlanedate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#beginMakedate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endMakedate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#beginConfirmdate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endConfirmdate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#beginDeliverydate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endDeliverydate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});


    /**
     * 产品弹框选择按钮事件绑定
     */
    $("#prodButton").click(function(){
        top.layer.open({
            type: 2,
            area: ['800px', '500px'],
            title:"选择产品名称",
            auto:true,
            name:'friend',
            content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/product/product/data")+"&fieldLabels="+encodeURIComponent("产品名称")+"&fieldKeys="+encodeURIComponent("itemNameRu")+"&searchLabels="+encodeURIComponent("产品名称")+"&searchKeys="+encodeURIComponent("itemNameRu")+"&isMultiSelected=false",
            btn: ['确定', '关闭'],
            yes: function(index, layero){
                var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                var items = iframeWin.getSelections();
                if(items == ""){
                    jp.warning("必须选择一条数据!");
                    return;
                }
                var item=items[0];
                console.log(item);
                //$("#aprodName").val(item.itemNameRu);
                $("#prodname").val(item.item.name);
                top.layer.close(index);//关闭对话框。
            },
            cancel: function(index){
            }
        });
    });
    $("#prodDelButton").click(function(){
        // 清除
        $("#prodname").val("");
        $("#prodname").focus();

    });



});
		
  function getIdSelections() {
        return $.map($("#processRoutineDetailTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该加工路线单明细记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/processroutinedetail/processRoutineDetail/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#processRoutineDetailTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/processroutinedetail/processRoutineDetail/form?id=" + getIdSelections();
  }
  
</script>
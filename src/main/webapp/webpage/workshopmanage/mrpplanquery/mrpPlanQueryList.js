<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#mrpPlanQueryTable').bootstrapTable({
		 
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
               url: "${ctx}/mrpplanquery/mrpPlanQuery/data",
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
                   	window.location = "${ctx}/mrpplanquery/mrpPlanQuery/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该物料需求计划记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/mrpplanquery/mrpPlanQuery/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#mrpPlanQueryTable').bootstrapTable('refresh');
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
		        field: 'mrpplanID',
		        title: 'MRP计划号',
		        sortable: true
		        /*,formatter:function(value, row , index){
		        	return "<a href='${ctx}/mrpplanquery/mrpPlanQuery/form?id="+row.id+"'>"+value+"</a>";
		         }*/
		       
		    }
			,{
		        field: 'itemCode',
		        title: '物料编码',
		        sortable: true
		       
		    }
			,{
		        field: 'itemName',
		        title: '物料名称',
		        sortable: true
		       
		    }
			,{
		        field: 'planQty',
		        title: '计划数量',
		        sortable: true
		       
		    }

			,{
		        field: 'mpsplanid',
		        title: 'mps计划号',
		        sortable: true
		       
		    }
			,{
		        field: 'planBdate',
		        title: '计划开始日期',
		        sortable: true
		       
		    }
			,{
		        field: 'planEDate',
		        title: '计划结束日期',
		        sortable: true
		       
		    }

			,{
		        field: 'planStatus',
		        title: '计划状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('mps_planstatus'))}, value, "-");
		        }
		       
		    }

			,{
		        field: 'makeName',
		        title: '计划制定人',
		        sortable: true
		       
		    }

			,{
		        field: 'makeDate',
		        title: '计划制定日期',
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

		 
		  $('#mrpPlanQueryTable').bootstrapTable("toggleView");
		}
	  
	  $('#mrpPlanQueryTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#mrpPlanQueryTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#mrpPlanQueryTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/mrpplanquery/mrpPlanQuery/import/template';
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
		  $('#mrpPlanQueryTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#mrpPlanQueryTable').bootstrapTable('refresh');
		});
		
		$('#beginPlanBdate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endPlanBdate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#beginPlanEDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endPlanEDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#beginMakeDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endMakeDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#beginConfirmDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endConfirmDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#beginAuditDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endAuditDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#beginDeliveryDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endDeliveryDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#beginCloseDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endCloseDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});




    /**
     * 物料弹框选择按钮事件绑定
     */
    $("#prodButton").click(function(){
        top.layer.open({
            type: 2,
            area: ['800px', '500px'],
            title:"选择物料名称",
            auto:true,
            name:'friend',
            content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/item/item/data2")+"&fieldLabels="+encodeURIComponent("物料编码|物料名称")+"&fieldKeys="+encodeURIComponent("code|name")+"&searchLabels="+encodeURIComponent("物料编码|物料名称")+"&searchKeys="+encodeURIComponent("code|name")+"&isMultiSelected=false",
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
                $("#itemName").val(item.name);
                top.layer.close(index);//关闭对话框。
            },
            cancel: function(index){
            }
        });
    });
    $("#prodDelButton").click(function(){
        // 清除
        $("#itemName").val("");
        $("#itemName").focus();

    });

		
	});
		
  function getIdSelections() {
        return $.map($("#mrpPlanQueryTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该物料需求计划记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/mrpplanquery/mrpPlanQuery/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#mrpPlanQueryTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/mrpplanquery/mrpPlanQuery/form?id=" + getIdSelections();
  }
  
</script>
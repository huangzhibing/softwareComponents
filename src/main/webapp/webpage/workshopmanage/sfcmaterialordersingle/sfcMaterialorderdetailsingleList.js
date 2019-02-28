<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#sfcMaterialorderdetailsingleTable').bootstrapTable({
		 
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
               url: "${ctx}/sfcmaterialordersingle/sfcMaterialorderdetailsingle/data",
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
                   	window.location = "${ctx}/sfcmaterialordersingle/sfcMaterialorderdetailsingle/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该原单行领料单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/sfcmaterialordersingle/sfcMaterialorderdetailsingle/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#sfcMaterialorderdetailsingleTable').bootstrapTable('refresh');
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
		        field: 'updateDate',
		        title: '更新时间',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/sfcmaterialordersingle/sfcMaterialorderdetailsingle/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true
		       
		    }
			,{
		        field: 'materialorder',
		        title: '领料单号',
		        sortable: true
		       
		    }
			,{
		        field: 'sequenceid',
		        title: '序号',
		        sortable: true
		       
		    }
			,{
		        field: 'materialid',
		        title: '材料代码',
		        sortable: true
		       
		    }
			,{
		        field: 'materialname',
		        title: '材料名称',
		        sortable: true
		       
		    }
			,{
		        field: 'numunit',
		        title: '计量单位',
		        sortable: true
		       
		    }
			,{
		        field: 'materialspec',
		        title: '材料规格',
		        sortable: true
		       
		    }
			,{
		        field: 'mpsid',
		        title: 'MPS号',
		        sortable: true
		       
		    }
			,{
		        field: 'projid',
		        title: '工程号',
		        sortable: true
		       
		    }
			,{
		        field: 'orderid',
		        title: '令单号',
		        sortable: true
		       
		    }
			,{
		        field: 'taskid',
		        title: '任务号',
		        sortable: true
		       
		    }
			,{
		        field: 'batchid',
		        title: '批号',
		        sortable: true
		       
		    }
			,{
		        field: 'itemcode',
		        title: '零件代码',
		        sortable: true
		       
		    }
			,{
		        field: 'itemname',
		        title: '零件名称',
		        sortable: true
		       
		    }
			,{
		        field: 'operno',
		        title: '工序号',
		        sortable: true
		       
		    }
			,{
		        field: 'opercode',
		        title: '工序代码',
		        sortable: true
		       
		    }
			,{
		        field: 'itembatch',
		        title: '批次',
		        sortable: true
		       
		    }
			,{
		        field: 'binid',
		        title: '货区代码',
		        sortable: true
		       
		    }
			,{
		        field: 'binname',
		        title: '货区名称',
		        sortable: true
		       
		    }
			,{
		        field: 'locid',
		        title: '货位代码',
		        sortable: true
		       
		    }
			,{
		        field: 'locname',
		        title: '货位名称',
		        sortable: true
		       
		    }
			,{
		        field: 'materialorderdetail',
		        title: '领料单号',
		        sortable: true
		       
		    }
			,{
		        field: 'islack',
		        title: 'Y缺料N不缺料',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('sfc_is_lack'))}, value, "-");
		        }
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#sfcMaterialorderdetailsingleTable').bootstrapTable("toggleView");
		}
	  
	  $('#sfcMaterialorderdetailsingleTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#sfcMaterialorderdetailsingleTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#sfcMaterialorderdetailsingleTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/sfcmaterialordersingle/sfcMaterialorderdetailsingle/import/template';
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
		  $('#sfcMaterialorderdetailsingleTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#sfcMaterialorderdetailsingleTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#sfcMaterialorderdetailsingleTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该原单行领料单记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/sfcmaterialordersingle/sfcMaterialorderdetailsingle/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#sfcMaterialorderdetailsingleTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/sfcmaterialordersingle/sfcMaterialorderdetailsingle/form?id=" + getIdSelections();
  }
  
</script>
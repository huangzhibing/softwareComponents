<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#purSupBackDetailTable').bootstrapTable({
		 
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
               url: "${ctx}/pursupbackdetail/purSupBackDetail/data",
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
                   	window.location = "${ctx}/pursupbackdetail/purSupBackDetail/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该供应商退货明细记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/pursupbackdetail/purSupBackDetail/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#purSupBackDetailTable').bootstrapTable('refresh');
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
		        field: 'serialNum',
		        title: '序号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return index + 1;
		         }
		       
		    }
			,{
		        field: 'backDate',
		        title: '退货日期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return value.split(" ")[0];
		        }
		    }
			,{
		        field: 'billNum',
		        title: '退货号码',
		        sortable: true
		       
		    }
			,{
		        field: 'supName',
		        title: '供应商名称',
		        sortable: true
		       
		    }
			,{
		        field: 'itemCode',
		        title: '物料代码',
		        sortable: true
		       
		    }
			,{
		        field: 'itemName',
		        title: '物料名称',
		        sortable: true
		       
		    }
			,{
		        field: 'itemSpecModel',
		        title: '物料规格',
		        sortable: true
		       
		    }
			,{
		        field: 'backQty',
		        title: '退货数量',
		        sortable: true
		       
		    }
			,{
		        field: 'unitCode',
		        title: '单位',
		        sortable: true
		       
		    }
			,{
		        field: 'realPriceTaxed',
		        title: '单价',
		        sortable: true
		       
		    }
			,{
		        field: 'realSumTaxed',
		        title: '总金额',
		        sortable: true
		       
		    }
			,{
		        field: 'conBillNum',
		        title: '采购单号',
		        sortable: true
		       
		    }
			,{
		        field: 'backReason',
		        title: '退货原因',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#purSupBackDetailTable').bootstrapTable("toggleView");
		}
	  
	  $('#purSupBackDetailTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#purSupBackDetailTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#purSupBackDetailTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/pursupbackdetail/purSupBackDetail/import/template';
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
		  $('#purSupBackDetailTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#purSupBackDetailTable').bootstrapTable('refresh');
		});
		
		$('#beginBackDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endBackDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		
	});


	function print(){
	  	var data = document.getElementById("purSupBackDetailTable").innerHTML;
	  	$("#printContent").html("");
	  	var thead = "<p align= 'center'>价格供应商退货明细</p>";
		$("#printContent").html("<style>table,table th,table td{border:1px solid}</style>"+thead+"<table style='font-size:12px;width:100%;border-collapse:collapse;margin:auto'>"+data+"</table>");
		$("#printContent").jqprint({
			debug:false,
			importCSS:true,
			printContainer:true,
			operaSupport: true
		});
		$("#printContent").html("");
	}

  function getIdSelections() {
        return $.map($("#purSupBackDetailTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该供应商退货明细记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/pursupbackdetail/purSupBackDetail/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#purSupBackDetailTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/pursupbackdetail/purSupBackDetail/form?id=" + getIdSelections();
  }
  
</script>
<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#dispatchBillTable').bootstrapTable({
		 
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
               url: "${ctx}/dispatchbill/dispatchBill/data",
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
                   	window.location = "${ctx}/dispatchbill/dispatchBill/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该车间派工单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/dispatchbill/dispatchBill/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#dispatchBillTable').bootstrapTable('refresh');
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
               columns: [//{
		        //checkbox: true
		       
		    //}
			
			//,
            {
 		        field: 'dispatchNo',
 		        title: '派工单号',
 		        sortable: true
 		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/dispatchbill/dispatchBill/form?id="+row.id+"'>"+value+"</a>";
		         }
 		    }
 			,{
		        field: 'routineBillNo',
		        title: '加工路线单号',
		        sortable: true
		        
		    }
			,{
		        field: 'routingCode',
		        title: '工艺号',
		        sortable: true
		       
		    }
			,{
		        field: 'centerCode',
		        title: '计划工作中心',
		        sortable: true
		       
		    }
			,{
		        field: 'actualcenterCode',
		        title: '实作工作中心',
		        sortable: true
		       
		    }
			,{
		        field: 'personInCharge',
		        title: '负责人',
		        sortable: true
		       
		    }
			,{
		        field: 'teamCode',
		        title: '计划班组',
		        sortable: true
		       
		    }
			,{
		        field: 'actualTeamCode',
		        title: '实作班组',
		        sortable: true
		       
		    }
			,{
		        field: 'shiftCode',
		        title: '班次',
		        sortable: true
		       
		    }
			,{
		        field: 'workHour',
		        title: '计划工时',
		        sortable: true
		       
		    }
			,{
		        field: 'actualWorkHour',
		        title: '实作工时',
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

		 
		  $('#dispatchBillTable').bootstrapTable("toggleView");
		}
	  
	  $('#dispatchBillTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#dispatchBillTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#dispatchBillTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/dispatchbill/dispatchBill/import/template';
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
		  $('#dispatchBillTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#dispatchBillTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#dispatchBillTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该车间派工单记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/dispatchbill/dispatchBill/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#dispatchBillTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/dispatchbill/dispatchBill/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#dispatchBillChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/dispatchbill/dispatchBill/detail?id="+row.id, function(dispatchBill){
    	var dispatchBillChild1RowIdx = 0, dispatchBillChild1Tpl = $("#dispatchBillChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  dispatchBill.dispatchBillPersonList;
		for (var i=0; i<data1.length; i++){
			addRow('#dispatchBillChild-'+row.id+'-1-List', dispatchBillChild1RowIdx, dispatchBillChild1Tpl, data1[i]);
			dispatchBillChild1RowIdx = dispatchBillChild1RowIdx + 1;
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
<script type="text/template" id="dispatchBillChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">车间派工单人员表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>派工单号</th>
								<th>加工路线单号</th>
								<th>工艺号</th>
								<th>计划执行工人</th>
								<th>实际执行工人</th>
								<th>实际执行工时</th>
								<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="dispatchBillChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="dispatchBillChild1Tpl">//<!--
				<tr>
					<td>
						{{row.dispatchNo}}
					</td>
					<td>
						{{row.routineBillNo}}
					</td>
					<td>
						{{row.routingCode}}
					</td>
					<td>
						{{row.workerCode}}
					</td>
					<td>
						{{row.actualWorkerCode}}
					</td>
					<td>
						{{row.actualWorkHour}}
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
	</script>

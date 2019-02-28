<%@ page contentType="text/html;charset=UTF-8" %>
<script>
//依据采购人员的编码设置采购的用户名值
function setUsername(idx,value){
	$("#auditGlName").val(value);
}
$(document).ready(function() {	
	$('#audittaskTable').bootstrapTable({
		 
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
               url: "${ctx}/qmsaudittask/audittask/data",
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
                   	window.location = "${ctx}/qmsaudittask/audittask/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该稽核任务记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/qmsaudittask/audittask/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#audittaskTable').bootstrapTable('refresh');
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
		        field: 'audittId',
		        title: '稽核任务编号',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return "<a href='${ctx}/qmsaudittask/audittask/form?id="+row.id+"'>"+value+"</a>";
		         }
		    }
			,{
		        field: 'audittName',
		        title: '稽核任务名称',
		        sortable: true
		       
		    }
			,{
		        field: 'auditGrouper.no',
		        title: '审核组长编号',
		        sortable: true
		       
		    }
			,{
		        field: 'auditGlName',
		        title: '审核组长名称',
		        sortable: true
		       
		    }
			,{
		        field: 'auditDate',
		        title: '审核完成时间',
		        sortable: true,
		        formatter:function(value, row , index){
		        	if(value==undefined) {
			       		return "";}
			       	else{
			       	 return value.split(" ")[0];}
			    }
		       
		    }
			,{
		        field: 'auditGoal',
		        title: '审核目的',
		        sortable: true
		       
		    }
			,{
		        field: 'auditScope',
		        title: '审核范围',
		        sortable: true
		       
		    }
			,{
		        field: 'auditResult',
		        title: '审核结果综述',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#audittaskTable').bootstrapTable("toggleView");
		}
	  
	  $('#audittaskTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#audittaskTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#audittaskTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/qmsaudittask/audittask/import/template';
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
		  $('#audittaskTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#audittaskTable').bootstrapTable('refresh');
		});
		
				$('#beginAuditDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#endAuditDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#audittaskTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该稽核任务记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/qmsaudittask/audittask/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#audittaskTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/qmsaudittask/audittask/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#audittaskChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/qmsaudittask/audittask/detail?id="+row.id, function(audittask){
    	var audittaskChild1RowIdx = 0, audittaskChild1Tpl = $("#audittaskChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  audittask.auditTaskItemList;
		for (var i=0; i<data1.length; i++){
			if(data1[i].isMeet=='1'){
				data1[i].isMeet="是";
			}else{
				data1[i].isMeet="否";
			}
            if(data1[i].isCorrect=='1'){
            	data1[i].isCorrect="是";
			}else{
				data1[i].isCorrect="否";
			}
            data1[i].auditDate=data1[i].auditDate.split(" ")[0];
			addRow('#audittaskChild-'+row.id+'-1-List', audittaskChild1RowIdx, audittaskChild1Tpl, data1[i]);
			audittaskChild1RowIdx = audittaskChild1RowIdx + 1;
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
<script type="text/template" id="audittaskChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">稽核任务项</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
				 
						<table class="ani table"  style="min-width: 2800px">
						<thead>
							<tr>															
								<th width='100px'>稽核流程ID</th>
								<th width='150px'>稽核流程名称</th>
								<th width='100px'>稽核标准要素编码</th>
								<th width='150px'>稽核标准要素名称</th>
								<th width='100px'>稽核部门编号</th>
								<th width='100px'>稽核部门名称</th>
								<th width='100px'>内审员编号</th>
								<th width='100px'>内审员名称</th>
								<th width='150px'>审核完成时间</th>
								<th width='50px'>是否符合</th>
								<th width='100px'>追踪人编号</th>
								<th width='100px'>追踪人名称</th>
								<th width='100px'>不符合项是否已纠正</th>
								<th width='150px'>不符合项说明</th>
							</tr>
						</thead>
						<tbody id="audittaskChild-{{idx}}-1-List">
						</tbody>
					</table>
				 
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="audittaskChild1Tpl">//<!--
				<tr>										
					<td>
						{{row.auditstd.auditpCode}}
					</td>
					<td>
						{{row.auditpName}}
					</td>
					<td>
						{{row.auditItemCode}}
					</td>
					<td>
						{{row.auditItemName}}
					</td>
					<td>
						{{row.auditDeptCode}}
					</td>
					<td>
						{{row.auditDeptName}}
					</td>
					<td>
						{{row.auditer.no}}
					</td>
					<td>
						{{row.auditGmName}}
					</td>
					<td>
						{{row.auditDate}}
					</td>
					<td>
						{{row.isMeet}}
					</td>
					
					<td>
						{{row.tracker.no}}
					</td>
					<td>
						{{row.trackpName}}
					</td>
					<td>
						{{row.isCorrect}}
					</td>
					<td>
					    {{row.remark}}
				    </td>
				</tr>//-->
	</script>

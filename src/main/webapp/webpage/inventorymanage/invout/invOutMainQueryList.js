<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#invOutMainTable').bootstrapTable({
		 
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
               url: "${ctx}/invout/invOutMain/data",
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
                   	window.location = "${ctx}/invout/invOutMain/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该放行单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/invout/invOutMain/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#invOutMainTable').bootstrapTable('refresh');
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
		        field: 'billNum',
		        title: '单据号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/invout/invOutMain/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'makeDate',
		        title: '制单日期',
		        sortable: true
		       
		    }
			,{
		        field: 'makeEmp.no',
		        title: '制单人编码',
		        sortable: true
		       
		    }
			,{
		        field: 'makeEmpName',
		        title: '制单人名称',
		        sortable: true
		       
		    }
			,{
		        field: 'billFlag',
		        title: '单据状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('pbillStat'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'ware.wareID',
		        title: '仓库编码',
		        sortable: true
		       
		    }
			,{
		        field: 'wareName',
		        title: '仓库名称',
		        sortable: true
		       
		    }
			,{
		        field: 'wareEmp.no',
		        title: '库管员编码',
		        sortable: true
		       
		    }
			,{
		        field: 'wareEmpName',
		        title: '库管员姓名',
		        sortable: true
		       
		    }
			,{
		        field: 'dept.code',
		        title: '部门编码',
		        sortable: true
		       
		    }
			,{
		        field: 'deptName',
		        title: '部门名称',
		        sortable: true
		       
		    }
			,{
		        field: 'carrierName',
		        title: '承运商名称',
		        sortable: true
		       
		    }
			,{
		        field: 'carNum',
		        title: '承运车号',
		        sortable: true
		       
		    }
			,{
		        field: 'awayDate',
		        title: '出厂日期',
		        sortable: true
		       
		    }
			,{
		        field: 'corBillNum.billNum',
		        title: '对应单号',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#invOutMainTable').bootstrapTable("toggleView");
		}
	  
	  $('#invOutMainTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#invOutMainTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#invOutMainTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/invout/invOutMain/import/template';
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
		  $('#invOutMainTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#invOutMainTable').bootstrapTable('refresh');
		});
		
				$('#beginMakeDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endMakeDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#invOutMainTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该放行单记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/invout/invOutMain/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#invOutMainTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/invout/invOutMain/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#invOutMainChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/invout/invOutMain/detail?id="+row.id, function(invOutMain){
    	var invOutMainChild1RowIdx = 0, invOutMainChild1Tpl = $("#invOutMainChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  invOutMain.invOutDetailList;
		for (var i=0; i<data1.length; i++){
			addRow('#invOutMainChild-'+row.id+'-1-List', invOutMainChild1RowIdx, invOutMainChild1Tpl, data1[i]);
			invOutMainChild1RowIdx = invOutMainChild1RowIdx + 1;
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
<script type="text/template" id="invOutMainChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">放行单明细</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>单据号</th>
								<th>序号</th>
								<th>物料编码</th>
								<th>物料名称</th>
								<th>物料规格</th>
								<th>单位</th>
								<th>数量</th>
							</tr>
						</thead>
						<tbody id="invOutMainChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="invOutMainChild1Tpl">//<!--
				<tr>
					<td>
						{{row.billNum}}
					</td>
					<td>
						{{row.serialNum}}
					</td>
					<td>
						{{row.itemCode}}
					</td>
					<td>
						{{row.itemSpec}}
					</td>
					<td>
						{{row.itemName}}
					</td>
					<td>
						{{row.measUnit}}
					</td>
					<td>
						{{row.itemQty}}
					</td>
				</tr>//-->
	</script>

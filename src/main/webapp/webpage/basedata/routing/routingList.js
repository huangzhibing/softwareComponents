<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#routingTable').bootstrapTable({
		 
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
               url: "${ctx}/routing/routing/data",
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
                   	window.location = "${ctx}/routing/routing/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该工艺路线记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/routing/routing/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#routingTable').bootstrapTable('refresh');
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
				   field: 'product.item.code',
				   title: '产品代码',
				   sortable: true
				   ,formatter:function(value, row , index){
					   return "<a href='${ctx}/routing/routing/form?id="+row.id+"'>"+value+"</a>";
				   }

			   }
			   ,{
				   field: 'productName',
				   title: '产品名称',
				   sortable: true

			   }
			,{
		        field: 'routingCode',
		        title: '工艺编码',
		        sortable: true

		       
		    }
			,{
		        field: 'routingName',
		        title: '工艺名称',
		        sortable: true
		       
		    }
		   ,{
			   field: 'routingSeqNo',
			   title: '工艺序号',
			   sortable: true

		   }
			,{
		        field: 'routingDesc',
		        title: '工艺描述',
		        sortable: true
		       
		    }
			,{
		        field: 'office.code',
		        title: '部门代码',
		        sortable: true
		       
		    }
			,{
		        field: 'deptName',
		        title: '部门名称',
		        sortable: true
		       
		    }
		    ,{
              	field:'unitWage',
				title:'单位计件工资',
				sortable:true
			},{
			   field: 'assembleflag',
			   title: '是否为总装工艺',
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

		 
		  $('#routingTable').bootstrapTable("toggleView");
		}
	  
	  $('#routingTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#routingTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#routingTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/routing/routing/import/template';
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
		  $('#routingTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#routingTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#routingTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该工艺路线记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/routing/routing/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#routingTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/routing/routing/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#routingChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/routing/routing/detail?id="+row.id, function(routing){
    	var routingChild1RowIdx = 0, routingChild1Tpl = $("#routingChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  routing.routingWorkList;
		for (var i=0; i<data1.length; i++){
			addRow('#routingChild-'+row.id+'-1-List', routingChild1RowIdx, routingChild1Tpl, data1[i]);
			routingChild1RowIdx = routingChild1RowIdx + 1;
		}
          var routingChild2RowIdx = 0, routingChild2Tpl = $("#routingChild2Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
          var data2 =  routing.routingItemList;
          console.log(data2)
          for (var i=0; i<data2.length; i++){
              addRow('#routingChild-'+row.id+'-2-List', routingChild2RowIdx, routingChild2Tpl, data2[i]);
              routingChild2RowIdx = routingChild2RowIdx + 1;
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
<script type="text/template" id="routingChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">工序</a></li>
<li><a data-toggle="tab" href="#tab-{{idx}}-2" aria-expanded="true">工艺路线物料关系表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>工序编码</th>
								<th>工序名称</th>
								<th>工序序号</th>
								<th>负责人名称</th>
								<th>工序职能说明</th>
								<th>定额工时</th>
							</tr>
						</thead>
						<tbody id="routingChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
    <div id="tab-{{idx}}-2" class="tab-pane fade">
    <table class="ani table">
    <thead>
	<tr>
		<th>物料编码</th>
		<th>物料名称</th>
		<th>需求数量</th>
		<th>备注</th>
    </tr>
    </thead>
    <tbody id="routingChild-{{idx}}-2-List">
    </tbody>
    </table>
    </div>

	</script>
	<script type="text/template" id="routingChild1Tpl">//<!--
				<tr>
					<td>
						{{row.workProcedure.workProcedureId}}
					</td>
					<td>
						{{row.workProcedureName}}
					</td>
					<td>
						{{row.workPorcedureSeqNo}}
					</td>
					<td>
						{{row.workPersonName}}
					</td>
					<td>
						{{row.workProcedureDesc}}
					</td>
					<td>
						{{row.workTime}}
					</td>
				</tr>//-->
	</script>
<script type="text/template" id="routingChild2Tpl">//<!--
    <tr>
		<td>
			{{row.item.code}}
		</td>
		<td>
			{{row.item.name}}
		</td>
		<td>
			{{row.reqQty}}
		</td>
		<td>
			{{row.remark}}
		</td>
	</tr>//-->
</script>

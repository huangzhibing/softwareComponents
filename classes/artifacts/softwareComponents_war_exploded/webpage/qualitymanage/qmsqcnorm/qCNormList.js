<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#qCNormTable').bootstrapTable({
		 
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
               url: "${ctx}/qmsqcnorm/qCNorm/data",
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
                   	window.location = "${ctx}/qmsqcnorm/qCNorm/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该检验标准定义记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/qmsqcnorm/qCNorm/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#qCNormTable').bootstrapTable('refresh');
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
		        field: 'qcnormId',
		        title: '检验标准号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/qmsqcnorm/qCNorm/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'qcnormName',
		        title: '检验标准名称',
		        sortable: true
		       
		    }
			,{
		        field: 'qnobj.objCode',
		        title: '检验对象编码',
		        sortable: true
		       
		    }
			,{
		        field: 'objNameRu',
		        title: '检验对象名称',
		        sortable: true
		       
		    }
			,{
		        field: 'qualityNorm',
		        title: '执行标准',
		        sortable: true
		       
		    }
			,{
		        field: 'compilePerson',
		        title: '编制人',
		        sortable: true
		       
		    }
			,{
		        field: 'compileDate',
		        title: '编制日期',
		        sortable: true
		       
		    }
			,{
		        field: 'beginDate',
		        title: '生效日期',
		        sortable: true
		       
		    }
			,{
		        field: 'endDate',
		        title: '失效日期',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#qCNormTable').bootstrapTable("toggleView");
		}
	  
	  $('#qCNormTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#qCNormTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#qCNormTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/qmsqcnorm/qCNorm/import/template';
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
		  
		  $('#qCNormTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#qCNormTable').bootstrapTable('refresh');
		});
		
				$('#compileDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginBeginDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endBeginDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#qCNormTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该检验标准定义记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/qmsqcnorm/qCNorm/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#qCNormTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/qmsqcnorm/qCNorm/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  
	  var htmltpl =  $("#qCNormChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/qmsqcnorm/qCNorm/detail?id="+row.id, function(qCNorm){
    	var qCNormChild1RowIdx = 0, qCNormChild1Tpl = $("#qCNormChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  qCNorm.qcnormItemList;
		console.log(qCNorm);
		for (var i=0; i<data1.length; i++){
			
			addRow('#qCNormChild-'+row.id+'-1-List', qCNormChild1RowIdx, qCNormChild1Tpl, data1[i]);
			qCNormChild1RowIdx = qCNormChild1RowIdx + 1;
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
<script type="text/template" id="qCNormChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">检验标准子表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>检验标准号</th>
								<th>检验标准名称</th>
								<th>检验对象编码</th>
								<th>检验对象名称</th>
								<th>检验指标编码</th>
								<th>检验指标名称</th>
								<th>指标单位</th>
					<%--		<th>数据类型</th>
								<th>数据精度</th>
								<th>检验方法</th>
								<th>检验仪器</th>
								<th>仪器精度</th>
								<th>精度单位</th>
								<th>最大值</th>  		--%>
							</tr>
						</thead>
						<tbody id="qCNormChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="qCNormChild1Tpl">//<!--
				<tr>
					<td>
						{{row.qcnormId}}
					</td>
					<td>
						{{row.qcnormName}}
					</td>
					<td>
						{{row.objCode}}
					</td>
					<td>
						{{row.objName}}
					</td>
					<td>
						{{row.itemCode}}
					</td>
					<td>
						{{row.itemName}}
					</td>
					<td>
						{{row.itemUnit}}
					</td>
		<%--		<td>
						{{dict.dataType}}
					</td>
					<td>
						{{row.dataLimit}}
					</td>
					<td>
						{{row.checkMethod}}
					</td>
					<td>
						{{row.checkAppa}}
					</td>
					<td>
						{{row.appaLimit}}
					</td>
					<td>
						{{row.appaUnit}}
					</td>
					<td>
						{{row.maxValue}}
					</td>    --%>
				</tr>//-->
	</script>

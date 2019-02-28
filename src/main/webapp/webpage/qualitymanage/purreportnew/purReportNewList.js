<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#purReportNewTable').bootstrapTable({
		 
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
    	       detailView: false,
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
               url: "${ctx}/purreportnew/purReportNew/data",
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
                   	window.location = "${ctx}/purreportnew/purReportNew/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该IQC来料检验报告记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/purreportnew/purReportNew/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#purReportNewTable').bootstrapTable('refresh');
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
		        field: 'reportId',
		        title: '报告单编号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/purreportnew/purReportNew/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'inv.billnum',
		        title: '采购单号',
		        sortable: true
		       
		    }
			,{
		        field: 'icode',
		        title: '料号',
		        sortable: true
		       
		    }
			,{
		        field: 'itemName',
		        title: '物料名称',
		        sortable: true
		       
		    }
			,{
		        field: 'supname1',
		        title: '供应商名称',
		        sortable: true
		       
		    }
			,{
		        field: 'itemCount',
		        title: '来料数量',
		        sortable: true
		       
		    }
			,{
		        field: 'inspectionNum',
		        title: '送检编号',
		        sortable: true
		       
		    }
			,{
		        field: 'itemArriveDate',
		        title: '来料日期',
		        sortable: true
		       
		    }
			,{
		        field: 'checkDate',
		        title: '检验日期',
		        sortable: true
		       
		    }
			,{
		        field: 'user.name',
		        title: '检验员',
		        sortable: true
		       
		    }
			,{
		        field: 'rndFul',
		        title: '全检抽检',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('checkType'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'aql',
		        title: '收货标准',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('AQL'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'crAc',
		        title: '致命缺陷允收数量',
		        sortable: true
		       
		    }
			,{
		        field: 'majAc',
		        title: '严重缺陷允收数量',
		        sortable: true
		       
		    }
			,{
		        field: 'minAc',
		        title: '轻微缺陷允收数量',
		        sortable: true
		       
		    }
			,{
		        field: 'crRe',
		        title: '致命缺陷拒收数量',
		        sortable: true
		       
		    }
			,{
		        field: 'majRe',
		        title: '严重缺陷拒收数量',
		        sortable: true
		       
		    }
			,{
		        field: 'minRe',
		        title: '轻微缺陷拒收数量',
		        sortable: true
		       
		    }
			,{
		        field: 'checkResult',
		        title: '检验结果',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('checkResultNew'))}, value, "-");
		        }
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#purReportNewTable').bootstrapTable("toggleView");
		}
	  
	  $('#purReportNewTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#purReportNewTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#purReportNewTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/purreportnew/purReportNew/import/template';
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
		  $('#purReportNewTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#purReportNewTable').bootstrapTable('refresh');
		});
		
				$('#beginItemArriveDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endItemArriveDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginCheckDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endCheckDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#purReportNewTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该IQC来料检验报告记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/purreportnew/purReportNew/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#purReportNewTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/purreportnew/purReportNew/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#purReportNewChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/purreportnew/purReportNew/detail?id="+row.id, function(purReportNew){
    	var purReportNewChild1RowIdx = 0, purReportNewChild1Tpl = $("#purReportNewChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  purReportNew.purreportfundetailList;
		for (var i=0; i<data1.length; i++){
			addRow('#purReportNewChild-'+row.id+'-1-List', purReportNewChild1RowIdx, purReportNewChild1Tpl, data1[i]);
			purReportNewChild1RowIdx = purReportNewChild1RowIdx + 1;
		}
				
    	var purReportNewChild2RowIdx = 0, purReportNewChild2Tpl = $("#purReportNewChild2Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data2 =  purReportNew.purreportnewsnList;
		for (var i=0; i<data2.length; i++){
			addRow('#purReportNewChild-'+row.id+'-2-List', purReportNewChild2RowIdx, purReportNewChild2Tpl, data2[i]);
			purReportNewChild2RowIdx = purReportNewChild2RowIdx + 1;
		}
				
    	var purReportNewChild3RowIdx = 0, purReportNewChild3Tpl = $("#purReportNewChild3Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data3 =  purReportNew.purreportsizedetailList;
		for (var i=0; i<data3.length; i++){
			addRow('#purReportNewChild-'+row.id+'-3-List', purReportNewChild3RowIdx, purReportNewChild3Tpl, data3[i]);
			purReportNewChild3RowIdx = purReportNewChild3RowIdx + 1;
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
<script type="text/template" id="purReportNewChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">IQC来料检验报告功能检查缺陷描述</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-2" aria-expanded="true">IQC来料检验报告外观检查缺陷描述</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-3" aria-expanded="true">IQC来料检验报告产品尺寸缺陷描述</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>报告单主键</th>
								<th>产品功能缺陷描述</th>
								<th>致命缺陷产品数量</th>
								<th>严重缺陷产品数量</th>
								<th>轻微缺陷产品数量</th>
								<th>不良品数量</th>
							</tr>
						</thead>
						<tbody id="purReportNewChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-2" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>报告单主键</th>
								<th>产品外观缺陷描述</th>
								<th>致命缺陷产品数量</th>
								<th>严重缺陷产品数量</th>
								<th>轻微缺陷产品数量</th>
								<th>不良品数量</th>
							</tr>
						</thead>
						<tbody id="purReportNewChild-{{idx}}-2-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-3" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>报告单主键</th>
								<th>产品尺寸缺陷描述</th>
								<th>致命缺陷产品数量</th>
								<th>严重缺陷产品数量</th>
								<th>轻微缺陷产品数量</th>
								<th>不良品数量</th>
							</tr>
						</thead>
						<tbody id="purReportNewChild-{{idx}}-3-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="purReportNewChild1Tpl">//<!--
				<tr>
					<td>
						{{row.reportId.id}}
					</td>
					<td>
						{{row.matterType.mattername}}
					</td>
					<td>
						{{row.crNum}}
					</td>
					<td>
						{{row.majNum}}
					</td>
					<td>
						{{row.minNum}}
					</td>
					<td>
						{{row.sum}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="purReportNewChild2Tpl">//<!--
				<tr>
					<td>
						{{row.reportId.id}}
					</td>
					<td>
						{{row.matterType.mattername}}
					</td>
					<td>
						{{row.crNum}}
					</td>
					<td>
						{{row.majNum}}
					</td>
					<td>
						{{row.minNum}}
					</td>
					<td>
						{{row.sum}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="purReportNewChild3Tpl">//<!--
				<tr>
					<td>
						{{row.reportId.id}}
					</td>
					<td>
						{{row.matterType.mattername}}
					</td>
					<td>
						{{row.crNum}}
					</td>
					<td>
						{{row.majNum}}
					</td>
					<td>
						{{row.minNum}}
					</td>
					<td>
						{{row.sum}}
					</td>
				</tr>//-->
	</script>

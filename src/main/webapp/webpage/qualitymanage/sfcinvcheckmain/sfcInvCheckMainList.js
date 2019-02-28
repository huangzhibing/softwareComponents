<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#sfcInvCheckMainTable').bootstrapTable({
		 
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
               url: "${ctx}/sfcinvcheckmain/sfcInvCheckMain/data",
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
                   	window.location = "${ctx}/sfcinvcheckmain/sfcInvCheckMain/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该完工入库通知单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/sfcinvcheckmain/sfcInvCheckMain/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#sfcInvCheckMainTable').bootstrapTable('refresh');
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
		        field: 'billNo',
		        title: '单据编号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/sfcinvcheckmain/sfcInvCheckMain/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'billType',
		        title: '单据类型',
		        sortable: true
		       
		    }
			,{
		        field: 'makeDate',
		        title: '制单日期',
		        sortable: true
		       
		    }
			,{
		        field: 'makepId',
		        title: '制单人编号',
		        sortable: true
		       
		    }
			,{
		        field: 'makepName',
		        title: '制单人名称',
		        sortable: true
		       
		    }
			,{
		        field: 'makeNotes',
		        title: '制单人说明',
		        sortable: true
		       
		    }
			,{
		        field: 'billStateFlag',
		        title: '单据标志',
		        sortable: true
		       
		    }
			,{
		        field: 'workerpId',
		        title: '完工人编号',
		        sortable: true
		       
		    }
			,{
		        field: 'workerpName',
		        title: '完工人名称',
		        sortable: true
		       
		    }
			,{
		        field: 'finishDate',
		        title: '完工日期',
		        sortable: true
		       
		    }
			,{
		        field: 'shopId',
		        title: '车间编号',
		        sortable: true
		       
		    }
			,{
		        field: 'shopName',
		        title: '车间名称',
		        sortable: true
		       
		    }
			,{
		        field: 'warepId',
		        title: '库管员编号',
		        sortable: true
		       
		    }
			,{
		        field: 'warepName',
		        title: '库管员名称',
		        sortable: true
		       
		    }
			,{
		        field: 'wareId',
		        title: '仓库编号',
		        sortable: true
		       
		    }
			,{
		        field: 'wareName',
		        title: '仓库名称',
		        sortable: true
		       
		    }
			,{
		        field: 'notes',
		        title: '备注',
		        sortable: true
		       
		    }
			,{
		        field: 'invFlag',
		        title: '库存处理标志',
		        sortable: true
		       
		    }
			,{
		        field: 'qmsFlag',
		        title: '质检标志',
		        sortable: true
		       
		    }
			,{
		        field: 'priceSum',
		        title: '金额合计',
		        sortable: true
		       
		    }
			,{
		        field: 'ioType',
		        title: '入库类型',
		        sortable: true
		       
		    }
			,{
		        field: 'ioFlag',
		        title: '入库标志',
		        sortable: true
		       
		    }
			,{
		        field: 'ioDesc',
		        title: '入库类型描述',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#sfcInvCheckMainTable').bootstrapTable("toggleView");
		}
	  
	  $('#sfcInvCheckMainTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#sfcInvCheckMainTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#sfcInvCheckMainTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/sfcinvcheckmain/sfcInvCheckMain/import/template';
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
		  $('#sfcInvCheckMainTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#sfcInvCheckMainTable').bootstrapTable('refresh');
		});
		
				$('#makeDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#finishDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#sfcInvCheckMainTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该完工入库通知单记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/sfcinvcheckmain/sfcInvCheckMain/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#sfcInvCheckMainTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/sfcinvcheckmain/sfcInvCheckMain/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#sfcInvCheckMainChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/sfcinvcheckmain/sfcInvCheckMain/detail?id="+row.id, function(sfcInvCheckMain){
    	var sfcInvCheckMainChild1RowIdx = 0, sfcInvCheckMainChild1Tpl = $("#sfcInvCheckMainChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  sfcInvCheckMain.sfcInvCheckDetailList;
		for (var i=0; i<data1.length; i++){
			addRow('#sfcInvCheckMainChild-'+row.id+'-1-List', sfcInvCheckMainChild1RowIdx, sfcInvCheckMainChild1Tpl, data1[i]);
			sfcInvCheckMainChild1RowIdx = sfcInvCheckMainChild1RowIdx + 1;
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
<script type="text/template" id="sfcInvCheckMainChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">完工入库通知单子表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>单据编号</th>
								<th>序号</th>
								<th>物料编号</th>
								<th>物料名称</th>
								<th>质量要求</th>
								<th>检验对象序列号</th>
								<th>物料二维码</th>
								<th>实际数量</th>
								<th>实际单价</th>
								<th>实际金额</th>
								<th>含税单价</th>
								<th>含税金额</th>
								<th>税额</th>
								<th>作废标志</th>
								<th>备注</th>
								<th>结算说明</th>
								<th>结算标志</th>
								<th>对应单号</th>
								<th>对应序号</th>
								<th>退货标志</th>
								<th>物料规格</th>
								<th>计量单位</th>
								<th>物料图号</th>
							</tr>
						</thead>
						<tbody id="sfcInvCheckMainChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="sfcInvCheckMainChild1Tpl">//<!--
				<tr>
					<td>
						{{row.sfcInvCheckMain.id}}
					</td>
					<td>
						{{row.seriaNo}}
					</td>
					<td>
						{{row.itemCode}}
					</td>
					<td>
						{{row.itemName}}
					</td>
					<td>
						{{row.massRequire}}
					</td>
					<td>
						{{row.objSn}}
					</td>
					<td>
						{{row.itemBarcode}}
					</td>
					<td>
						{{row.realQty}}
					</td>
					<td>
						{{row.realPrice}}
					</td>
					<td>
						{{row.realSum}}
					</td>
					<td>
						{{row.realPriceTaxed}}
					</td>
					<td>
						{{row.realSumTaxed}}
					</td>
					<td>
						{{row.taxSum}}
					</td>
					<td>
						{{row.checkState}}
					</td>
					<td>
						{{row.notes}}
					</td>
					<td>
						{{row.balanceNotes}}
					</td>
					<td>
						{{row.balanceFlag}}
					</td>
					<td>
						{{row.corBillnum}}
					</td>
					<td>
						{{row.corSerialnum}}
					</td>
					<td>
						{{row.backFlag}}
					</td>
					<td>
						{{row.itemSpecmodel}}
					</td>
					<td>
						{{row.unitCode}}
					</td>
					<td>
						{{row.itemPdn}}
					</td>
				</tr>//-->
	</script>

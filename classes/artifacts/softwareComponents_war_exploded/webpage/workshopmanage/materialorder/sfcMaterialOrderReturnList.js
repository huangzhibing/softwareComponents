<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#sfcMaterialOrderTable').bootstrapTable({
		 
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
               url: "${ctx}/materialorder/sfcMaterialOrder/data",
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
               //contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	window.location = "${ctx}/materialorder/sfcMaterialOrder/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该领料单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/materialorder/sfcMaterialOrder/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#sfcMaterialOrderTable').bootstrapTable('refresh');
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
		        radio: true
		       
		    }
			
			,{
		        field: 'materialOrder',
		        title: '单据号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/materialorder/sfcMaterialOrder/returnForm?id="+row.id+"'>"+value+"</a>";
		         }
		    }

		   ,{
			   field: 'receiveType',
			   title: '单据类型',
			   sortable: true
			   ,formatter:function(value, row , index){
				   if(value=='C')
					   return "系统生成";
				   else
					   return "手工录入";
			   }
		   }

		   ,{
			   field: 'operCode',
			   title: '工作中心编码',
			   sortable: true

		   }
		   ,{
			   field: 'operName',
			   title: '工作中心名称',
			   sortable: true

		   }
		   /*,{
			   field: 'responser',
			   title: '领料人代码',
			   sortable: true

		   }*/
		   ,{
			   field: 'respName',
			   title: '领料人姓名',
			   sortable: true

		   }

			/*,{
		        field: 'shopId',
		        title: '部门代码',
		        sortable: true
		       
		    }*/
			,{
		        field: 'shopName',
		        title: '部门名称',
		        sortable: true
		       
		    }

			,{
		        field: 'editor',
		        title: '制单人名称',
		        sortable: true
		       
		    }
			,{
		        field: 'editDate',
		        title: '制单日期',
		        sortable: true
		       
		    }
			/*,{
		        field: 'useId',
		        title: '用途代码',
		        sortable: true
		       
		    }
			,{
		        field: 'useDesc',
		        title: '用途名称',
		        sortable: true
		       
		    }*/
			/*,{
		        field: 'editorId',
		        title: '制单人编码',
		        sortable: true
		       
		    }*/
			,{
		        field: 'billType',
		        title: '领料单类型',
		        sortable: true,
			    formatter:function(value, row , index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('sfc_material_type'))}, value, "-");
                }
		    }
		   ,{
			   field: 'qualityFlag',
			   title: '良品标识',
			   sortable: true
		   }
			,{
		        field: 'operDate',
		        title: '领料日期',
		        sortable: true
		       
		    }
			,{
		        field: 'wareEmpid',
		        title: '库管员编码',
		        sortable: true
		       
		    }
			,{
		        field: 'wareEmpname',
		        title: '库管员名称',
		        sortable: true
		       
		    }
		   ,{
			   field: 'wareId',
			   title: '库房代码',
			   sortable: true

		   }
		   ,{
			   field: 'wareName',
			   title: '库房名称',
			   sortable: true

		   }
		   ,{
			   field: 'periodId',
			   title: '核算期',
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

		 
		  $('#sfcMaterialOrderTable').bootstrapTable("toggleView");
		}
	  
	  $('#sfcMaterialOrderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#sfcMaterialOrderTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#sfcMaterialOrderTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/materialorder/sfcMaterialOrder/import/template';
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
		  $('#sfcMaterialOrderTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#sfcMaterialOrderTable').bootstrapTable('refresh');
		});
		
				$('#editDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginOperDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#endOperDate').datetimepicker({
					format: "YYYY-MM-DD"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#sfcMaterialOrderTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该领料单记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/materialorder/sfcMaterialOrder/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#sfcMaterialOrderTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/materialorder/sfcMaterialOrder/returnForm?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#sfcMaterialOrderChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/materialorder/sfcMaterialOrder/detail?id="+row.id, function(sfcMaterialOrder){
    	var sfcMaterialOrderChild1RowIdx = 0, sfcMaterialOrderChild1Tpl = $("#sfcMaterialOrderChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  sfcMaterialOrder.sfcMaterialOrderDetailList;
		for (var i=0; i<data1.length; i++){
			addRow('#sfcMaterialOrderChild-'+row.id+'-1-List', sfcMaterialOrderChild1RowIdx, sfcMaterialOrderChild1Tpl, data1[i]);
			sfcMaterialOrderChild1RowIdx = sfcMaterialOrderChild1RowIdx + 1;
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
<script type="text/template" id="sfcMaterialOrderChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">领料单子表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								
								<th>序号</th>
								<th>材料代码</th>
								<th>材料名称</th>
								<th>计量单位</th>
								<th>材料规格</th>
								<th>申领数量</th>
								<th>实发数量</th>
								<th>计划价格</th>
								<th>计划金额</th>
								<th>实际价格</th>
								<th>实际金额</th>
								<th>MPS号</th>
								<th>令单号</th>
								<th>批次</th>
								<th>货区代码</th>
								<th>货区名称</th>
								<th>货位代码</th>
								<th>货位名称</th>
								<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="sfcMaterialOrderChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="sfcMaterialOrderChild1Tpl">//<!--
				<tr>
					
					<td>
						{{row.sequenceId}}
					</td>
					<td>
						{{row.materialId}}
					</td>
					<td>
						{{row.materialName}}
					</td>
					<td>
						{{row.numUnit}}
					</td>
					<td>
						{{row.materialSpec}}
					</td>
					<td>
						{{row.requireNum}}
					</td>
					<td>
						{{row.receiveNum}}
					</td>
					<td>
						{{row.unitPrice}}
					</td>
					<td>
						{{row.sumPrice}}
					</td>
					<td>
						{{row.realPrice}}
					</td>
					<td>
						{{row.realSum}}
					</td>
					<td>
						{{row.mpsId}}
					</td>
					<td>
						{{row.orderId}}
					</td>
					<td>
						{{row.itemBatch}}
					</td>
					<td>
						{{row.binId}}
					</td>
					<td>
						{{row.binName}}
					</td>
					<td>
						{{row.locId}}
					</td>
					<td>
						{{row.locName}}
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
	</script>

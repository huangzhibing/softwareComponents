<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#salOrderTable').bootstrapTable({
		 
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
               url: "${ctx}/salorder/salOrder/data",
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
                   	window.location = "${ctx}/salorder/salOrder/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该内部订单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/salorder/salOrder/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#salOrderTable').bootstrapTable('refresh');
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
		        field: 'contract.contractCode',
		        title: '合同编码',
		        sortable: true
		        ,formatter:function(value, row , index){
		            if(value == null){
		            	return "<a href='${ctx}/salorder/salOrder/formSearch?id="+row.id+"'>-</a>";
		            }else{
		            	return "<a href='${ctx}/salorder/salOrder/formSearch?id="+row.id+"'>"+value+"</a>";
		            }
		        	
		        }
		       
		    }
			,{
		        field: 'orderCode',
		        title: '订单编码',
		        sortable: true
		       
		    }
			,{
		        field: 'signDate',
		        title: '签订日期',
		        sortable: true
		       
		    }
			,{
		        field: 'accountCode',
		        title: '客户编码',
		        sortable: true
		       
		    }
			,{
		        field: 'accountName',
		        title: '客户名称',
		        sortable: true
		       
		    }
			,{
		        field: 'inputDate',
		        title: '制单日期',
		        sortable: true
		       
		    }
			,{
		        field: 'inputPerson.no',
		        title: '制单人编码',
		        sortable: true
		       
		    }
			,{
		        field: 'inputPerson.name',
		        title: '制单人姓名',
		        sortable: true
		       
		    }
			,{
		        field: 'orderStat',
		        title: '订单状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('orderStat'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'sendDate',
		        title: '交货日期',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#salOrderTable').bootstrapTable("toggleView");
		}
	  
	  $('#salOrderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#salOrderTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#salOrderTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/salorder/salOrder/import/template';
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
		  $('#salOrderTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#salOrderTable').bootstrapTable('refresh');
		});
		
				$('#beginSignDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endSignDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginSendDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endSendDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#salOrderTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该内部订单记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/salorder/salOrder/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#salOrderTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/salorder/salOrder/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#salOrderChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/salorder/salOrder/detail?id="+row.id, function(salOrder){
    	var salOrderChild1RowIdx = 0, salOrderChild1Tpl = $("#salOrderChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  salOrder.salOrderAuditList;
		if(data1!=null){
			for (var i=0; i<data1.length; i++){
			addRow('#salOrderChild-'+row.id+'-1-List', salOrderChild1RowIdx, salOrderChild1Tpl, data1[i]);
			salOrderChild1RowIdx = salOrderChild1RowIdx + 1;
		}
		}
		
				
    	var salOrderChild2RowIdx = 0, salOrderChild2Tpl = $("#salOrderChild2Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data2 =  salOrder.salOrderItemList;
		if(data2!=null){
			for (var i=0; i<data2.length; i++){
			addRow('#salOrderChild-'+row.id+'-2-List', salOrderChild2RowIdx, salOrderChild2Tpl, data2[i]);
			salOrderChild2RowIdx = salOrderChild2RowIdx + 1;
		}
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
<script type="text/template" id="salOrderChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">内部定单会签表</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-2" aria-expanded="true">内部订单子表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								
								<th>会签编码</th>
								<th>会签人</th>
								<th>会签日期</th>
								<th>会签意见</th>
								<th>状态</th>
							</tr>
						</thead>
						<tbody id="salOrderChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-2" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>序号</th>
								<th>产品编码</th>
								<th>产品名称</th>
								<th>规格型号</th>
								<th>单位名称</th>
								<th>签订量</th>
								<th>无税单价</th>
								<th>无税总额</th>
								<th>含税单价</th>
								<th>含税金额</th>
							</tr>
						</thead>
						<tbody id="salOrderChild-{{idx}}-2-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="salOrderChild1Tpl">//<!--
				<tr>
					
					<td>
						{{row.auditCode}}
					</td>
					<td>
						{{row.auditPerson}}
					</td>
					<td>
						{{row.auditDate}}
					</td>
					<td>
						{{row.auditNote}}
					</td>
					<td>
					{{row.auditNote}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="salOrderChild2Tpl">//<!--
				<tr>
					<td>
						{{row.seqId}}
					</td>
					<td>
						{{row.prodCode.id}}
					</td>
					<td>
						{{row.prodName}}
					</td>
					<td>
						{{row.prodSpec}}
					</td>
					<td>
						{{row.unitName}}
					</td>
					<td>
						{{row.prodQty}}
					</td>
					<td>
						{{row.prodPrice}}
					</td>
					<td>
						{{row.prodSum}}
					</td>
					<td>
						{{row.prodPriceTaxed}}
					</td>
					<td>
						{{row.prodSumTaxed}}
					</td>
				</tr>//-->
	</script>

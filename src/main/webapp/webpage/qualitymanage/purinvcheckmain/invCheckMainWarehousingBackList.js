<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#invCheckMainTable').bootstrapTable({
		 
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
               url: "${ctx}/purinvcheckmain/invCheckMain/selectBack",
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
                   	window.location = "${ctx}/purinvcheckmain/invCheckMain/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该采购到货管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/purinvcheckmain/invCheckMain/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#invCheckMainTable').bootstrapTable('refresh');
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
		        field: 'billnum',
		        title: '单据编号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/purinvcheckmain/invCheckMain/Backform?id="+row.id+"'>"+value+"</a>";
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
		        sortable: true,
		        formatter:function(value, row , index){
		        	return value.split(" ")[0];
		        }
		       
		    }
			,{
		        field: 'makeEmpid',
		        title: '制单人编号',
		        sortable: true
		       
		    }
			,{
		        field: 'makeEmpname',
		        title: '制单人名称',
		        sortable: true
		       
		    }
			,{
		        field: 'billStateFlag',
		        title: '单据标志',
		        sortable: true
		    }
			,{
		        field: 'buyerId',
		        title: '采购员编号',
		        sortable: true
		       
		    }
			,{
		        field: 'buyerName',
		        title: '采购员名称',
		        sortable: true
		       
		    }
			,{
		        field: 'arriveDate',
		        title: '到货日期',
		        sortable: true

		    }
			,{
		        field: 'supId',
		        title: '供应商编号',
		        sortable: true
		       
		    }
			,{
		        field: 'supName',
		        title: '供应商名称',
		        sortable: true
		       
		    }
			,{
		        field: 'invoiceNum',
		        title: '发票号',
		        sortable: true
		       
		    }
			,{
		        field: 'thFlag',
		        title: '估价标记',
		        sortable: true
		       
		    }
			,{
		        field: 'carrierName',
		        title: '承运商名称',
		        sortable: true
		       
		    }
			,{
		        field: 'invoiceNumCar',
		        title: '承运发票号',
		        sortable: true
		       
		    }
			,{
		        field: 'awayDate',
		        title: '发货日期',
		        sortable: true

		    }
			,{
		        field: 'tranpriceSum',
		        title: '运费合计',
		        sortable: true
		       
		    }
			,{
		        field: 'priceSum',
		        title: '金额合计',
		        sortable: true
		       
		    }
			,{
		        field: 'priceSumSubtotal',
		        title: '小计金额',
		        sortable: true
		       
		    }
			,{
		        field: 'qmsFlag',
		        title: '质检标志',
		        sortable: true
		       
		    }
			,{
		        field: 'ioType',
		        title: '入库类型',
		        sortable: true
		       
		    }
			,{
		        field: 'bdate',
		        title: '业务日期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return value.split(" ")[0];
		        }

		    }
			,{
		        field: 'billNotes',
		        title: '单据说明',
		        sortable: true
		       
		    }
			
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#invCheckMainTable').bootstrapTable("toggleView");
		}
	  
	  $('#invCheckMainTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#invCheckMainTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#invCheckMainTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/purinvcheckmain/invCheckMain/import/template';
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
		  $('#invCheckMainTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#invCheckMainTable').bootstrapTable('refresh');
		});
		
				$('#beginMakeDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#endMakeDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#beginBdate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#endBdate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#invCheckMainTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该采购到货管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/purinvcheckmain/invCheckMain/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#invCheckMainTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/purinvcheckmain/invCheckMain/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#invCheckMainChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/purinvcheckmain/invCheckMain/detail?id="+row.id, function(invCheckMain){
    	var invCheckMainChild1RowIdx = 0, invCheckMainChild1Tpl = $("#invCheckMainChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  invCheckMain.invCheckDetailList;
		for (var i=0; i<data1.length; i++){
			addRow('#invCheckMainChild-'+row.id+'-1-List', invCheckMainChild1RowIdx, invCheckMainChild1Tpl, data1[i]);
			invCheckMainChild1RowIdx = invCheckMainChild1RowIdx + 1;
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
<script type="text/template" id="invCheckMainChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">入库通知单子表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>序号</th>
								<th>物料编号</th>
								<th>物料名称</th>
								<th>到货数量</th>
								<th>实际单价</th>
								<th>实际金额</th>
								<th>含税单价</th>
								<th>含税金额</th>
								<th>不含税运费</th>
								<th>含税运费</th>
								<th>税率</th>
								<th>运费税率</th>
								<th>物料规格</th>
								<th>计量单位</th>
								<th>说明</th>
							</tr>
						</thead>
						<tbody id="invCheckMainChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="invCheckMainChild1Tpl">//<!--
				<tr>
					<td>
						{{row.serialnum}}
					</td>
					<td>
						{{row.item.code}}
					</td>
					<td>
						{{row.itemName}}
					</td>
					<td>
						{{row.checkQty}}
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
						{{row.transSum}}
					</td>
					<td>
						{{row.transSumTaxed}}
					</td>
					<td>
						{{row.taxRatio}}
					</td>
					<td>
						{{row.transRatio}}
					</td>
					<td>
						{{row.itemSpecmodel}}
					</td>
					<td>
						{{row.unitCode}}
					</td>
					<td>
						{{row.itemNotes}}
					</td>
				</tr>//-->
	</script>

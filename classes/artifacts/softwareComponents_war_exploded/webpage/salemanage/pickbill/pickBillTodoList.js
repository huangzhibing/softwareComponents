<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#pickBillTable').bootstrapTable({
		 
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
               url: "${ctx}/pickbill/pickBill/todo/data",
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
              
               onClickRow: function(row, $el){
               },
               columns: [
			{
			   field: 'pbillCode',
			   title: '发货单编码',
			   sortable: true
			   , formatter: function (value, row, index) {
				   if(row['temp']['task.assignee'] == undefined || row['temp']['task.assignee'] == ""){
                       return "<a href='${ctx}/pickbill/pickBill/form?id=" + row.id + "'>" + value + "</a>";
				   }else{
                       return '<a href="${ctx}/act/task/form?taskId='+row['temp']['task.id']+'&taskName='+encodeURI(row['temp']['task.name'])+'&taskDefKey='+row['temp']['task.taskDefinitionKey']+'&procInsId='+row['temp']['task.processInstanceId']+'&procDefId='+row['temp']['task.processDefinitionId']+'&status='+row.status+'">'+ value +'</a>';
				   };
			   }

		   }
		   , {
			   field: 'pbillStat',
			   title: '单据状态',
			   sortable: true,
			   formatter: function (value, row, index) {
				   return jp.getDictLabel(${fns:toJson(fns:getDictList('pbillStat'))}, value, "-");
			   }

		   }
		   , {
			   field: 'pbillDate',
			   title: '制单日期',
			   sortable: true

		   }

		   , {
			   field: 'pickDate',
			   title: '发货日期',
			   sortable: true

		   }
		   , {
			   field: 'rcvAccount.accountCode',
			   title: '客户编码',
			   sortable: true

		   }
		   , {
			   field: 'rcvAccountName',
			   title: '客户名称',
			   sortable: true

		   }

		   , {
			   field: 'contract.contractCode',
			   title: '合同号',
			   sortable: true

		   }

		   , {
			   field: 'ware.id',
			   title: '接收仓库编号',
			   sortable: true

		   }
		   , {
			   field: 'wareName',
			   title: '接收仓库名称',
			   sortable: true

		   }
		   , {
			   field: 'io.id',
			   title: '出库类型',
			   sortable: true

		   }
		   , {
			   field: 'iodesc',
			   title: '出库类型描述',
			   sortable: true

		   }
		   , {
			   field: 'pbillPerson.no',
			   title: '制单人编码',
			   sortable: true

		   }
		   , {
			   field: 'pbillPsnName',
			   title: '制单人姓名',
			   sortable: true

		   }
		   , {
			   field: 'rcvAddr',
			   title: '收货地址',
			   sortable: true

		   }
		   , {
			   field: 'rcvPerson',
			   title: '收货人',
			   sortable: true

		   }
		   , {
			   field: 'rcvTel',
			   title: '收货人电话',
			   sortable: true

		   }

		   , {
			   field: 'transAccount',
			   title: '承运人',
			   sortable: true

		   }
		   , {
			   field: 'qmsFlag',
			   title: '质检标志',
			   sortable: true,
			   formatter: function (value, row, index) {
				   return jp.getDictLabel(${fns:toJson(fns:getDictList('qmsFlagSal'))}, value, "-");
			   }

		   }

			, {
                       field: 'operate',
                       title: '操作',
                       align: 'center',
                       formatter: function operateFormatter(value, row, index) {
                           var arra = new Array();
                           if(row['temp']['task.assignee'] == undefined || row['temp']['task.assignee'] == ""){
                               arra.push('<a href="javascript:claim(\''+row['temp']['task.id']+'\');">【签收任务】</a>');
                           }else{
                               arra.push('<a href="${ctx}/act/task/form?taskId='+row['temp']['task.id']+'&taskName='+encodeURI(row['temp']['task.name'])+'&taskDefKey='+row['temp']['task.taskDefinitionKey']+'&procInsId='+row['temp']['task.processInstanceId']+'&procDefId='+row['temp']['task.processDefinitionId']+'&status='+row.status+'">【任务办理】</a>');

                           };
                           arra.push('<a  href="javascript:jp.openDialogView(\'跟踪\',\'${ctx}/act/task/trace/photo/'+row['temp']['task.processDefinitionId']+'/'+row['temp']['task.executionId']+'\',\'1000px\', \'600px\')">【跟踪】</a>');

                           return arra.join("");

                       }
                   }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#pickBillTable').bootstrapTable("toggleView");
		}
	  
	  $('#pickBillTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#pickBillTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#pickBillTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/pickbill/pickBill/import/template';
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
		  $('#pickBillTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#pickBillTable').bootstrapTable('refresh');
		});
		
				$('#beginPbillDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endPbillDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginPickDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endPickDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#pickBillTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该单据记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/pickbill/pickBill/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#pickBillTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/pickbill/pickBill/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#pickBillChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/pickbill/pickBill/detail?id="+row.id, function(pickBill){
    	var pickBillChild1RowIdx = 0, pickBillChild1Tpl = $("#pickBillChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  pickBill.pickBillItemList;
		for (var i=0; i<data1.length; i++){
			addRow('#pickBillChild-'+row.id+'-1-List', pickBillChild1RowIdx, pickBillChild1Tpl, data1[i]);
			pickBillChild1RowIdx = pickBillChild1RowIdx + 1;
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
<script type="text/template" id="pickBillChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">销售发货单明细</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>序号</th>
								<th>订单编号</th>
								<th>产品编码</th>
								<th>产品名称</th>
								<th>产品规格</th>
								<th>单位</th>
								<th>发货量</th>
								<th>税率</th>
								<th>无税单价</th>
								<th>无税总额</th>
								<th>含税单价</th>
								<th>含税总额</th>
								<th>运费价格</th>
								<th>运费总额</th>
							</tr>
						</thead>
						<tbody id="pickBillChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="pickBillChild1Tpl">//<!--
				<tr>
					<td>
						{{row.seqId}}
					</td>
					<td>
						{{row.itemCode}}
					</td>
					<td>
						{{row.prodCode.item.code}}
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
						{{row.pickQty}}
					</td>
					<td>
						{{row.taxRatio}}
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
					<td>
						{{row.transPrice}}
					</td>
					<td>
						{{row.transSum}}
					</td>
				</tr>//-->
	</script>

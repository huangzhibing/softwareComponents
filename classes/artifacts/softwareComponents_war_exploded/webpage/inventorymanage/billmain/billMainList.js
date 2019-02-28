<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#billMainTable').bootstrapTable({
		 
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
               url: "${ctx}/billmain/billMain/data",
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
                   	window.location = "${ctx}/billmain/billMain/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该单据记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/billmain/billMain/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#billMainTable').bootstrapTable('refresh');
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
		        	return "<a href='${ctx}/billmain/billMain/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'billDate',
		        title: '出入库日期',
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
		        field: 'io.ioType',
		        title: '单据类型',
		        sortable: true
		       
		    }
			,{
		        field: 'ioDesc',
		        title: '单据名称',
		        sortable: true
		       
		    }
			,{
		        field: 'ioFlag',
		        title: '单据标记',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('ioFlag'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'ware.wareID',
		        title: '库房号',
		        sortable: true
		       
		    }
			,{
		        field: 'wareName',
		        title: '库房名称',
		        sortable: true
		       
		    }
			,{
		        field: 'billPerson.no',
		        title: '经办人',
		        sortable: true
		       
		    }
			,{
		        field: 'wareEmp.empID',
		        title: '库管员代码',
		        sortable: true
		       
		    }
			,{
		        field: 'wareEmpname',
		        title: '库管员名称',
		        sortable: true
		       
		    }
			,{
		        field: 'corBillNum',
		        title: '对应单据号',
		        sortable: true
		       
		    }
			,{
		        field: 'corId',
		        title: '来往单位代码',
		        sortable: true
		       
		    }
			,{
		        field: 'corName',
		        title: '来往单位名称',
		        sortable: true
		       
		    }
			,{
		        field: 'invuse.useId',
		        title: '用途/车间班组代码',
		        sortable: true
		       
		    }
			,{
		        field: 'useName',
		        title: '用途/车间班组名称',
		        sortable: true
		       
		    }
			,{
		        field: 'billFlag',
		        title: '过账标志',
		        sortable: true,
		        formatter:function(value, row , index){
		        	console.log(${fns:toJson(fns:getDictList('billFlag'))});
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billFlag'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'estimateFlag',
		        title: '估价标志',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('estimateFlag'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'period.id',
		        title: '核算期间',
		        sortable: true
		       
		    }
			,{
		        field: 'billDesc',
		        title: '单据说明',
		        sortable: true
		       
		    }
			,{
		        field: 'recEmp.no',
		        title: '记账人编码',
		        sortable: true
		       
		    }
			,{
		        field: 'recEmpname',
		        title: '记账人',
		        sortable: true
		       
		    }
			,{
		        field: 'recDate',
		        title: '记账日期',
		        sortable: true
		       
		    }
			,{
		        field: 'billEmp.no',
		        title: '制单人编码',
		        sortable: true
		       
		    }
			,{
		        field: 'billEmpname',
		        title: '制单人',
		        sortable: true
		       
		    }
			,{
		        field: 'note',
		        title: '备注',
		        sortable: true
		       
		    }
			,{
		        field: 'orderCode',
		        title: '接口序号',
		        sortable: true
		       
		    }
			,{
		        field: 'taxRatio',
		        title: '税率',
		        sortable: true
		       
		    }
			,{
		        field: 'inoId',
		        title: '财务凭证号',
		        sortable: true
		       
		    }
			,{
		        field: 'classId',
		        title: '科目代码',
		        sortable: true
		       
		    }
			,{
		        field: 'classDesc',
		        title: '科目名称',
		        sortable: true
		       
		    }
			,{
		        field: 'cflag',
		        title: '传财务标志',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cflag'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'fnumber',
		        title: '发票号',
		        sortable: true
		       
		    }
			,{
		        field: 'finvoiceID',
		        title: '发票内码',
		        sortable: true
		       
		    }
			,{
		        field: 'billType',
		        title: '冲估类型',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cgType'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'redFlag',
		        title: '红单标志',
		        sortable: true
		       
		    }
			,{
		        field: 'cgFlag',
		        title: '冲估标志',
		        sortable: true
		       
		    }
			,{
		        field: 'totalSum',
		        title: '金额合计',
		        sortable: true
		       
		    }
			,{
		        field: 'operDate',
		        title: '业务日期',
		        sortable: true
		       
		    }
			,{
		        field: 'billSource',
		        title: '单据来源',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('billSource'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'cgNum',
		        title: '冲估单号',
		        sortable: true
		       
		    }
			,{
		        field: 'workPoscode',
		        title: '工位代码',
		        sortable: true
		       
		    }
			,{
		        field: 'wordPosname',
		        title: '工位名称',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#billMainTable').bootstrapTable("toggleView");
		}
	  
	  $('#billMainTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#billMainTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#billMainTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/billmain/billMain/import/template';
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
		  $('#billMainTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#billMainTable').bootstrapTable('refresh');
		});
		
				$('#beginBillDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endBillDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginRecDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endRecDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#billMainTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该单据记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/billmain/billMain/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#billMainTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/billmain/billMain/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#billMainChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/billmain/billMain/detail?id="+row.id, function(billMain){
    	var billMainChild1RowIdx = 0, billMainChild1Tpl = $("#billMainChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  billMain.billDetailList;
		for (var i=0; i<data1.length; i++){
			addRow('#billMainChild-'+row.id+'-1-List', billMainChild1RowIdx, billMainChild1Tpl, data1[i]);
			billMainChild1RowIdx = billMainChild1RowIdx + 1;
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
<script type="text/template" id="billMainChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">出入库单据子表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>单据号</th>
								<th>序号</th>
								<th>货区号</th>
								<th>货区名称</th>
								<th>货位号</th>
								<th>货位名称</th>
								<th>物料代码</th>
								<th>物料名称</th>
								<th>物料规格</th>
								<th>物料图号</th>
								<th>物料条码</th>
								<th>定单号</th>
								<th>计划号</th>
								<th>计量单位</th>
								<th>数量</th>
								<th>请领数量</th>
								<th>实际单价</th>
								<th>实际金额</th>
								<th>物料批号</th>
								<th>父项代码</th>
								<th>父项订单号</th>
								<th>工程号</th>
								<th>采购入库通知单号</th>
								<th>提货标志</th>
								<th>估价标志</th>
								<th>备注</th>
								<th>MRP标志</th>
								<th>计划单价</th>
								<th>税额</th>
								<th>销售传来的车号</th>
								<th>销售传来的到站</th>
								<th>销售传来的客户</th>
								<th>成本标志位</th>
								<th>接口序号</th>
								<th>运费</th>
								<th>工序号</th>
								<th>自制外协标记</th>
								<th>工序名称</th>
								<th>计划金额</th>
								<th>对应序号</th>
								<th>保留单价</th>
								<th>冲估数量</th>
								<th>冲估金额</th>
								<th>成本差额</th>
								<th>开票数量</th>
								<th>开票金额</th>
								<th>开票差额</th>
								<th>不含税金额</th>
								<th>含税金额</th>
								<th>税率</th>
							</tr>
						</thead>
						<tbody id="billMainChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="billMainChild1Tpl">//<!--
				<tr>
					<td>
						{{row.billNum}}
					</td>
					<td>
						{{row.serialNum}}
					</td>
					<td>
						{{row.bin.binId}}
					</td>
					<td>
						{{row.binName}}
					</td>
					<td>
						{{row.loc.locId}}
					</td>
					<td>
						{{row.locName}}
					</td>
					<td>
						{{row.item.code}}
					</td>
					<td>
						{{row.itemName}}
					</td>
					<td>
						{{row.itemSpec}}
					</td>
					<td>
						{{row.itemPdn}}
					</td>
					<td>
						{{row.barCode}}
					</td>
					<td>
						{{row.orderNum}}
					</td>
					<td>
						{{row.planNum}}
					</td>
					<td>
						{{row.measUnit}}
					</td>
					<td>
						{{row.itemQty}}
					</td>
					<td>
						{{row.itemRqty}}
					</td>
					<td>
						{{row.itemPrice}}
					</td>
					<td>
						{{row.itemSum}}
					</td>
					<td>
						{{row.itemBatch}}
					</td>
					<td>
						{{row.fitemCode}}
					</td>
					<td>
						{{row.forderNum}}
					</td>
					<td>
						{{row.contractNum}}
					</td>
					<td>
						{{row.corBillNum}}
					</td>
					<td>
						{{dict.pickFlag}}
					</td>
					<td>
						{{dict.estimateFlag}}
					</td>
					<td>
						{{row.note}}
					</td>
					<td>
						{{row.mrpFlag}}
					</td>
					<td>
						{{row.planPrice}}
					</td>
					<td>
						{{row.taxSum}}
					</td>
					<td>
						{{row.vehicleCode}}
					</td>
					<td>
						{{row.revStation}}
					</td>
					<td>
						{{row.revAccount}}
					</td>
					<td>
						{{dict.cosFlag}}
					</td>
					<td>
						{{row.orderCodes}}
					</td>
					<td>
						{{row.trafficCost}}
					</td>
					<td>
						{{row.operNo}}
					</td>
					<td>
						{{dict.sourceFlag}}
					</td>
					<td>
						{{row.operName}}
					</td>
					<td>
						{{row.planSum}}
					</td>
					<td>
						{{row.corSerialnum}}
					</td>
					<td>
						{{row.oldPrice}}
					</td>
					<td>
						{{row.cgQty}}
					</td>
					<td>
						{{row.cgSum}}
					</td>
					<td>
						{{row.subSum}}
					</td>
					<td>
						{{row.vouchQty}}
					</td>
					<td>
						{{row.vouchSum}}
					</td>
					<td>
						{{row.vouchSub}}
					</td>
					<td>
						{{row.realSum}}
					</td>
					<td>
						{{row.realSumTaxed}}
					</td>
					<td>
						{{row.taxRatio}}
					</td>
				</tr>//-->
	</script>

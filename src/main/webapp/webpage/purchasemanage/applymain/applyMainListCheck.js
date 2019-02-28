<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#applyMainTableCheck').bootstrapTable({
		 
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
               url: "${ctx}/applymain/applyMain/checkData",
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
                   	window.location = "${ctx}/applymain/applyMain/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该采购需求记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/applymain/applyMain/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#applyMainTableCheck').bootstrapTable('refresh');
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
		        field: 'billNum',
		        title: '单据编号',
		        sortable: true
		        
		       
		    }
			,{
		        field: 'makeDate',
		        title: '制定日期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return value.split(" ")[0];
		        }
		       
		    }
			,{
		        field: 'applyDate',
		        title: '业务日期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return value.split(" ")[0];
		        }
		       
		    }
			,{
		        field: 'applyType.applytypeid',
		        title: '需求类别编码',
		        sortable: true
		       
		    }
			,{
		        field: 'applyName',
		        title: '需求类别名称',
		        sortable: true
		       
		    }
			,{
		        field: 'billStateFlag',
		        title: '单据状态',
		        sortable: true
		       
		    }
			,{
		        field: 'office.code',
		        title: '需求部门编码',
		        sortable: true
		       
		    }
			,{
		        field: 'applyDept',
		        title: '需求部门名称',
		        sortable: true
		       
		    }
			,{
		        field: 'user.no',
		        title: '需求人员编码',
		        sortable: true
		       
		    }
			,{
		        field: 'makeEmpname',
		        title: '需求人员名称',
		        sortable: true
		       
		    }
			,{
		        field: 'makeNotes',
		        title: '需求说明',
		        sortable: true
		       
		    }
			
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#applyMainTableCheck').bootstrapTable("toggleView");
		}
	  
	  $('#applyMainTableCheck').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#applyMainTableCheck').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#applyMainTableCheck').bootstrapTable('getSelections').length!=1);
            $('#detail').prop('disabled', $('#applyMainTableCheck').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/applymain/applyMain/import/template';
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
		  $('#applyMainTableCheck').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#applyMainTableCheck').bootstrapTable('refresh');
		});
		
				$('#makeDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#applyDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#beginPeriodId').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#endPeriodId').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#beginMakeDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#endMakeDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#beginApplyDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#endApplyDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#applyMainTableCheck").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该采购需求记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/applymain/applyMain/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#applyMainTableCheck').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function deleteA(){
	  window.location = "${ctx}/applymain/applyMain/deleteA?id=" + getIdSelections();
  }
  function edit(){
	  window.location = "${ctx}/applymain/applyMain/form?id=" + getIdSelections();
  }
  
  function detail1(){
		//alert("close");
	  	//alert("detail按钮");
	  	
		//window.location ="${ctx}/applymain/applyMain/detail_1";
		window.location = "${ctx}/applymain/applyMain/detail_1?id=" + getIdSelections();
  }
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#applyMainChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/applymain/applyMain/detail?id="+row.id, function(applyMain){
    	var applyMainChild1RowIdx = 0, applyMainChild1Tpl = $("#applyMainChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  applyMain.applyDetailList;
		for (var i=0; i<data1.length; i++){
			addRow('#applyMainChild-'+row.id+'-1-List', applyMainChild1RowIdx, applyMainChild1Tpl, data1[i]);
			applyMainChild1RowIdx = applyMainChild1RowIdx + 1;
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
<script type="text/template" id="applyMainChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">采购需求单</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>序号</th>
								<th>物料编号</th>
								<th>物料名称</th>
								<th>规格型号</th>
								<th>需求日期</th>
								<th>需求数量</th>
								<th>计量单位</th>
								<th>物料材质</th>
								<th>需求金额</th>
								<th>说明</th>
								<th>数量修改历史</th>
								<th>库存量</th>
								<th>移动平均价</th>
							</tr>
						</thead>
						<tbody id="applyMainChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="applyMainChild1Tpl">//<!--
				<tr>
					
					<td>
						{{row.serialNum}}
					</td>
					<td>
						{{row.item.code}}
					</td>
					<td>
						{{row.itemName}}
					</td>
					<td>
						{{row.itemSpecmodel}}
					</td>
					<td>
						{{row.requestDate}}
					</td>
					<td>
						{{row.applyQty}}
					</td>
					<td>
						{{row.unitName}}
					</td>
					
					<td>
						{{row.itemTexture}}
					</td>
					
					<td>
						{{row.applySum}}
					</td>
					
					
					<td>
						{{row.notes}}
					</td>
					<td>
						{{row.log}}
					</td>
					
					<td>
						{{row.nowSum}}
					</td>
					<td>
						{{row.costPrice}}
					</td>
				</tr>//-->
	</script>

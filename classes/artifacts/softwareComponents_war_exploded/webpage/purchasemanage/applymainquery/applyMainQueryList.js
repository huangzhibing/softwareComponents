<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#applyMainQueryTable').bootstrapTable({
		 
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
               url: "${ctx}/applymainquery/applyMainQuery/data",
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
              
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	window.location = "${ctx}/applymainquery/applyMainQuery/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该采购需求查询记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/applymainquery/applyMainQuery/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#applyMainQueryTable').bootstrapTable('refresh');
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
			,{
		        field: 'userDeptCode',
		        title: '登陆人所在部门',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#applyMainQueryTable').bootstrapTable("toggleView");
		}
	  
	  $('#applyMainQueryTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#applyMainQueryTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#applyMainQueryTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/applymainquery/applyMainQuery/import/template';
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
		  $('#applyMainQueryTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#applyMainQueryTable').bootstrapTable('refresh');
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
        return $.map($("#applyMainQueryTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该采购需求查询记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/applymainquery/applyMainQuery/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#applyMainQueryTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/applymainquery/applyMainQuery/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#applyMainQueryChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/applymainquery/applyMainQuery/detail?id="+row.id, function(applyMainQuery){
    	var applyMainQueryChild1RowIdx = 0, applyMainQueryChild1Tpl = $("#applyMainQueryChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  applyMainQuery.applyDetailQueryList;
		for (var i=0; i<data1.length; i++){
			addRow('#applyMainQueryChild-'+row.id+'-1-List', applyMainQueryChild1RowIdx, applyMainQueryChild1Tpl, data1[i]);
			applyMainQueryChild1RowIdx = applyMainQueryChild1RowIdx + 1;
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
<script type="text/template" id="applyMainQueryChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">采购需求单查询子表</a></li>
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
								<th>库存量</th>
								<th>计量单位</th>
								<th>移动平均价</th>
								<th>金额</th>
								<th>数量修改历史</th>
								<th>物料材质</th>
								<th>说明</th>
							</tr>
						</thead>
						<tbody id="applyMainQueryChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="applyMainQueryChild1Tpl">//<!--
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
						{{row.nowSum}}
					</td>
					<td>
						{{row.unitName}}
					</td>
					<td>
						{{row.costPrice}}
					</td>
					<td>
						{{row.applySum}}
					</td>
					<td>
						{{row.log}}
					</td>
					<td>
						{{row.itemTexture}}
					</td>
					<td>
						{{row.notes}}
					</td>
				</tr>//-->
	</script>

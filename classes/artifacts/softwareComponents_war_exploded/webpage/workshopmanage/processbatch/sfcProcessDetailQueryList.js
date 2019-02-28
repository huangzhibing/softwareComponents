<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#sfcProcessDetailTable').bootstrapTable({
		 
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
               url: "${ctx}/processbatch/sfcProcessDetail/queryData",
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
              
              
               onClickRow: function(row, $el){
               },
               columns: [
			{
		        field: 'processBillNo',
		        title: '车间作业计划编号',
		        sortable: true
			   ,formatter:function(value, row , index){
				   return "<a href='${ctx}/processbatch/sfcProcessDetail/queryForm?id="+row.id+"'>"+value+"</a>";
			   }
		       
		    }
			,{
		        field: 'prodCode',
		        title: '产品编码',
		        sortable: true
		       
		    }
			,{
		        field: 'prodName',
		        title: '产品名称',
		        sortable: true
		       
		    }
		   ,{
			   field: 'planQty',
			   title: '计划生产数量',
			   sortable: true

		   }
			,{
		        field: 'planBdate',
		        title: '计划生产日期',
		        sortable: true
		       
		    }
		   ,{
			   field: 'assignedState',
			   title: '单据状态',
			   sortable: true,
			   formatter:function(value, row , index) {
				   return jp.getDictLabel(${fns:toJson(fns:getDictList('processbatch_status'))}, value, "-");
			   }

		   }
		   ,{
			   field: 'remarks',
			   title: '备注信息',
			   sortable: true

		   }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#sfcProcessDetailTable').bootstrapTable("toggleView");
		}
	  
	  $('#sfcProcessDetailTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#sfcProcessDetailTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#sfcProcessDetailTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/processbatch/sfcProcessDetail/import/template';
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
		  $('#sfcProcessDetailTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#sfcProcessDetailTable').bootstrapTable('refresh');
		});
		
				$('#beginPlanBdate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endPlanBdate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginPlanEdate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endPlanEdate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginMakeDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endMakeDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginConfirmDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endConfirmDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginDeliveryDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endDeliveryDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});




    /**
     * 产品弹框选择按钮事件绑定
     */
    $("#prodButton").click(function(){
        top.layer.open({
            type: 2,
            area: ['800px', '500px'],
            title:"选择产品名称",
            auto:true,
            name:'friend',
            content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/product/product/data")+"&fieldLabels="+encodeURIComponent("产品编码|产品名称")+"&fieldKeys="+encodeURIComponent("item.code|itemNameRu")+"&searchLabels="+encodeURIComponent("产品编码|产品名称")+"&searchKeys="+encodeURIComponent("item.code|itemNameRu")+"&isMultiSelected=false",
            btn: ['确定', '关闭'],
            yes: function(index, layero){
                var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                var items = iframeWin.getSelections();
                if(items == ""){
                    jp.warning("必须选择一条数据!");
                    return;
                }
                var item=items[0];
                console.log(item);
                //$("#aprodName").val(item.itemNameRu);
                $("#prodName").val(item.item.name);
                top.layer.close(index);//关闭对话框。
            },
            cancel: function(index){
            }
        });
    });
    $("#prodDelButton").click(function(){
        // 清除
        $("#prodName").val("");
        $("#prodName").focus();

    });
		
	});
		
  function getIdSelections() {
        return $.map($("#sfcProcessDetailTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
 
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#sfcProcessDetailChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/processbatch/sfcProcessDetail/detail?id="+row.id, function(sfcProcessDetail){
    	var sfcProcessDetailChild1RowIdx = 0, sfcProcessDetailChild1Tpl = $("#sfcProcessDetailChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  sfcProcessDetail.processBatchList;
		for (var i=0; i<data1.length; i++){
			addRow('#sfcProcessDetailChild-'+row.id+'-1-List', sfcProcessDetailChild1RowIdx, sfcProcessDetailChild1Tpl, data1[i]);
			sfcProcessDetailChild1RowIdx = sfcProcessDetailChild1RowIdx + 1;
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
<script type="text/template" id="sfcProcessDetailChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">车间作业计划分批表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>批次序号</th>
								<th>产品编码</th>
   							 	<th>产品名称</th>
								<th>计划生产数量</th>
								<th>计划生产日期</th>
    							<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="sfcProcessDetailChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="sfcProcessDetailChild1Tpl">//<!--
				<tr>
					<td>
						{{row.batchNo}}
					</td>
					<td>
						{{row.prodCode}}
					</td>
					<td>
						{{row.prodName}}
					</td>
					<td>
						{{row.planQty}}
					</td>
					<td>
						{{row.planBDate}}
					</td>
					<td>
						{{row.remarks}}
					</td>

				</tr>//-->
	</script>

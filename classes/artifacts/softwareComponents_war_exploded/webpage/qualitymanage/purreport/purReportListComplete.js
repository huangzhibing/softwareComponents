<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#purReportTable').bootstrapTable({
		 
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
               url: "${ctx}/purreport/purReport/myDataComplete",
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
             //  contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	window.location = "${ctx}/purreport/purReport/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该检验单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/purreport/purReport/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#purReportTable').bootstrapTable('refresh');
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
		        field: 'reportId',
		        title: '检验单编号',
		        sortable: true
		       
		       
		    }
			,{
		        field: 'sfcInvCheckMain.billNo',
		        title: '完工入库单编号',
		        sortable: true
		       
		    }
			,{
		        field: 'checktName',
		        title: '检验类型名称',
		        sortable: true
		       
		    }
			,{
		        field: 'rndFul',
		        title: '全检/抽检',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('checkType'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'qualityNorm',
		        title: '执行标准',
		        sortable: true
		       
		    }
			,{
		        field: 'office.name',
		        title: '检验部门名称',
		        sortable: true
		       
		    }
			,{
				field: 'cpositionName',
		        title: '检验岗位名称',
		        sortable: true
		       
		    }
			,{
		        field: 'checkResult',
		        title: '检验结论',
		        sortable: true
		       
		    }
			,{
		        field: 'checkDate',
		        title: '检验日期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return value.split(" ")[0];
		        }
		    }
			,{
		        field: 'checkTime',
		        title: '检验时间',
		        sortable: true
		       
		    }
			,{
		        field: 'checkPerson',
		        title: '检验人名称',
		        sortable: true
		       
		    }
			
			
			,{
		        field: 'memo',
		        title: '备注',
		        sortable: true
		       
		    }
			
		     ],
               onLoadSuccess:function(data){
		        	console.log(data);
		        }

		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#purReportTable').bootstrapTable("toggleView");
		}
	  
	  $('#purReportTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#purReportTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#purReportTable').bootstrapTable('getSelections').length!=1);
            $('#detail').prop('disabled', $('#purReportTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/purreport/purReport/import/template';
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
		  $('#purReportTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#purReportTable').bootstrapTable('refresh');
		});
		
				$('#checkDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
				$('#justifyDate').datetimepicker({
					 format: "YYYY-MM-DD"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#purReportTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该检验单记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/purreport/purReport/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#purReportTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/purreport/purReport/myForm?id=" + getIdSelections();
  }
  function detail(){

	  window.location = "${ctx}/purreport/purReport/MydetailCom?id=" + getIdSelections();
  }
  
  function detailCheck(){

	  window.location = "${ctx}/purreport/purReport/MydetailComCheck?id=" + getIdSelections();
  }
  
  function mydelete(){
	  window.location = "${ctx}/purreport/purReport/mydelete?id=" + getIdSelections();
  }
  
  function mydeleteCom(){
	  window.location = "${ctx}/purreport/purReport/mydeleteCom?id=" + getIdSelections();
  }
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#purReportChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/purreport/purReport/detail?id="+row.id, function(purReport){
    	var purReportChild1RowIdx = 0, purReportChild1Tpl = $("#purReportChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  purReport.purReportRSnList;
		for (var i=0; i<data1.length; i++){
			console.log(data1[i]);
			addRow('#purReportChild-'+row.id+'-1-List', purReportChild1RowIdx, purReportChild1Tpl, data1[i]);
			purReportChild1RowIdx = purReportChild1RowIdx + 1;
			console.log(data1[i]);
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
<script type="text/template" id="purReportChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">质量管理检验抽检表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>检验对象序列号</th>
								<th>检验对象编码</th>
								<th>检验对象名称</th>
								<th>检验结论</th>
								
								<th>检验日期</th>
								<th>检验时间</th>
								<th>检验人名称</th>
								<th>备注</th>
								<th>检验标准名称</th>
							</tr>
						</thead>
						<tbody id="purReportChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="purReportChild1Tpl">//<!--
				<tr>
					<td>
						{{row.objSn}}
					</td>
					<td>
						{{row.objCode}}
					</td>
					<td>
						{{row.objName}}
					</td>
					<td>
						{{row.checkResult}}
					</td>
					
					
					<td>
						{{row.checkDate}}
					</td>
					<td>
						{{row.checkTime}}
					</td>
					<td>
						{{row.checkPerson}}
					</td>
					<td>
						{{row.memo}}
					</td>
					<td>
						{{row.qcnorm.qcnormName}}
					</td>
				</tr>//-->
	</script>

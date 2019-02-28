<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#paccountTable').bootstrapTable({
		 
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
               url: "${ctx}/linkman/paccount/data",
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
                   	window.location = "${ctx}/linkman/paccount/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该供应商联系人记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/linkman/paccount/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#paccountTable').bootstrapTable('refresh');
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
		        field: 'accountCode',
		        title: '企业编码',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/linkman/paccount/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'accountName',
		        title: '企业名称',
		        sortable: true
		       
		    }
			,{
		        field: 'areaCode',
		        title: '地区编码',
		        sortable: true
		       
		    }
			,{
		        field: 'areaName',
		        title: '地区名称',
		        sortable: true
		       
		    }
			,{
		        field: 'accountProp',
		        title: '性质',
		        sortable: true
		       
		    }
			,{
		        field: 'accountMgr',
		        title: '法人',
		        sortable: true
		       
		    }
			,{
		        field: 'subTypeCode',
		        title: '类别编码',
		        sortable: true
		       
		    }
			,{
		        field: 'subTypeName',
		        title: '类别名称',
		        sortable: true
		       
		    }
			,{
		        field: 'supHigherUp',
		        title: '上级部门',
		        sortable: true
		       
		    }
			,{
		        field: 'accountAddr',
		        title: '联系地址',
		        sortable: true
		       
		    }
			,{
		        field: 'postCode',
		        title: '邮编',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#paccountTable').bootstrapTable("toggleView");
		}
	  
	  $('#paccountTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#paccountTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#paccountTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/linkman/paccount/import/template';
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
		  $('#paccountTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#paccountTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#paccountTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该供应商联系人记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/linkman/paccount/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#paccountTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/linkman/paccount/form?id=" + getIdSelections();
  }
  function details(){
	  window.location = "${ctx}/linkman/paccount/details?id=" + getIdSelections();
  }
  
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#paccountChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/linkman/paccount/detail?id="+row.id, function(paccount){
    	var paccountChild1RowIdx = 0, paccountChild1Tpl = $("#paccountChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  paccount.linkManList;
		for (var i=0; i<data1.length; i++){
			addRow('#paccountChild-'+row.id+'-1-List', paccountChild1RowIdx, paccountChild1Tpl, data1[i]);
			paccountChild1RowIdx = paccountChild1RowIdx + 1;
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
<script type="text/template" id="paccountChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">联系人档案</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>联系人编号</th>
								<th>联系人姓名</th>
								<th>性别</th>
								<th>级别</th>
								<th>职位</th>
								<th>电话</th>
								<th>手机</th>
								<th>mail</th>
								<th>状态</th>
							</tr>
						</thead>
						<tbody id="paccountChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="paccountChild1Tpl">//<!--
				<tr>
					<td>
						{{row.linkCode}}
					</td>
					<td>
						{{row.linkName}}
					</td>
					<td>
						{{row.linkSex}}
					</td>
					<td>
						{{row.linkLevel}}
					</td>
					<td>
						{{row.linkPosition}}
					</td>
					<td>
						{{row.linkTel}}
					</td>
					<td>
						{{row.linkPhone}}
					</td>
					<td>
						{{row.linkMail}}
					</td>
					<td>
						{{row.state}}
					</td>
				</tr>//-->
	</script>

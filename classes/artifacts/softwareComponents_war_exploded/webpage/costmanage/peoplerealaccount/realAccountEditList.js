<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#realAccountEditTable').bootstrapTable({
		 
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
	           detailFormatter: "detailFormatter2",
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
               url: "${ctx}/peopleRealaccount/realAccount/data?flag=${flag}",
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
                   if($el.data("item") == "xiugai"){
                   	window.location = "${ctx}/peopleRealaccount/realAccount/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该材料凭证单据管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/peopleRealaccount/realAccount/deleteBack?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#realAccountEditTable').bootstrapTable('refresh');
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
		        title: '凭证号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/peopleRealaccount/realAccount/form?flag=${flag}&id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
            ,{
                field: 'periodId',
				title: '会计期间',
                sortable: true

            }
			,{
		        field: 'oriBillId',
		        title: '原始凭证号',
		        sortable: true
		       
		    }
			,{
		        field: 'oriBillDate',
		        title: '原始单据日期',
		        sortable: true
		       
		    }

			,{
		        field: 'makeId',
		        title: '制单人编码',
		        sortable: true
		       
		    }
			,{
		        field: 'makeName',
		        title: '制单人名称',
		        sortable: true
		       
		    }
                   ,{
                       field: 'makeDate',
                       title: '制单日期',
                       sortable: true

                   }

		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#realAccountEditTable').bootstrapTable("toggleView");
		}
	  
	  $('#realAccountEditTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#realAccountEditTable').bootstrapTable('getSelections').length);
            $('#xiugai').prop('disabled', $('#realAccountEditTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/peopleRealaccount/realAccount/import/template';
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
		  $('#realAccountEditTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#realAccountEditTable').bootstrapTable('refresh');
		});
		
				$('#oriBillDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});

  function getIdSelectionsbyxiugai() {
        return $.map($("#realAccountEditTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  

  function xiugai(flag){
	  window.location = "${ctx}/peopleRealaccount/realAccount/form?id=" + getIdSelectionsbyxiugai()+"&flag="+flag;
  }
  
  function deleteAllbyxiugai(){

		jp.confirm('确认要删除该人工凭证单据管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/peopleRealaccount/realAccount/deleteAllBill?ids=" + getIdSelectionsbyxiugai(), function(data){
       	  		if(data.success){
       	  			$('#realAccountEditTable').bootstrapTable('refresh');
       	  			jp.success(data.msg);
       	  		}else{
       	  			jp.error(data.msg);
       	  		}
       	  	})
        	   
		})
  }
  
		   
  function detailFormatter2(index, row) {
	  var htmltpl =  $("#realAccountChildrenTpl2").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/peopleRealaccount/realAccount/detail?id="+row.id, function(realAccount){
    	var realAccountChild1RowIdx = 0, realAccountChild1Tpl = $("#realAccountChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  realAccount.realAccountDetailList;
		for (var i=0; i<data1.length; i++){
			addRow('#realAccountChild-'+row.id+'-1-List', realAccountChild1RowIdx, realAccountChild1Tpl, data1[i]);
			realAccountChild1RowIdx = realAccountChild1RowIdx + 1;
		}
		var realAccountChild2RowIdx = 0,realAccountChild2Tpl = $("#realAccountChild2Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data2 = realAccount.personWageList;
		for(var i=0;i<data2.length; i++){
            addRow('#realAccountChild-'+row.id+'-2-List', realAccountChild2RowIdx, realAccountChild2Tpl, data2[i]);
            realAccountChild2RowIdx = realAccountChild2RowIdx + 1;
		}
		console.log(row)
      })
     
        return html;
    }
  
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}
			
</script>
<script type="text/template" id="realAccountChildrenTpl2">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">凭证附表</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-2" aria-expanded="true">人工工资</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>序号</th>
								<th>科目代码</th>
								<th>科目名称</th>
								<th>科目金额</th>
								<th>借贷方向</th>
								<th>摘要</th>
								<th>核算对象编号</th>
								<th>核算对象名称</th>
								<th>核算对象单位</th>
								<th>核算对象规格</th>
								<th>核算对象数量</th>
							</tr>
						</thead>
						<tbody id="realAccountChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
    <div id="tab-{{idx}}-2" class="tab-pane fade">
    <table class="ani table">
    <thead>
    <tr>
    <th>人工单据号</th>
	<th>核算期</th>
    <th>工艺代码</th>
    <th>工艺名称</th>
    <th>班组代码</th>
    <th>班组名称</th>
    <th>产品代码</th>
    <th>产品名称</th>
    <th>单位计件工资</th>
    <th>单位分配工资</th>
	<th>工作量</th>
    </tr>
    </thead>
    <tbody id="realAccountChild-{{idx}}-2-List">
    </tbody>
    </table>
    </div>

	</script>
	<script type="text/template" id="realAccountChild1Tpl">//<!--
				<tr>
					<td>
						{{row.recNo}}
					</td>
					<td>
						{{row.itemId.itemCode}}
					</td>
					<td>
						{{row.itemName}}
					</td>
					<td>
						{{row.itemSum}}
					</td>
					<td>
						{{row.blflag}}
					</td>
					<td>
						{{row.abst}}
					</td>
					<td>
						{{row.cosProdObject.prodId}}
					</td>
					<td>
						{{row.prodName}}
					</td>
					<td>
						{{row.prodUnit}}
					</td>
					<td>
						{{row.prodSpec}}
					</td>
					<td>
						{{row.prodQty}}
					</td>
				</tr>//-->
	</script>
<script type="text/template" id="realAccountChild2Tpl">//<!--
    <tr>
    <td>
    {{row.billNum}}
</td>
<td>
{{row.periodId}}
</td>
<td>
{{row.routing.routingCode}}
</td>
<td>
{{row.routing.routingName}}
</td>
<td>
{{row.team.teamCode}}
</td>
<td>
{{row.team.teamName}}
</td>
<td>
{{row.itemCode.item.code}}
</td>
<td>
{{row.itemCode.item.name}}
</td>
<td>
{{row.itemUnit}}
</td>
<td>
{{row.wageUnit}}
</td>
<td>
{{row.itemQty}}
</td>
</tr>//-->
</script>

<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#groupTable').bootstrapTable({
		 
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
               url: "${ctx}/group/group/data",
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
                   	window.location = "${ctx}/group/group/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该采购组记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/group/group/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#groupTable').bootstrapTable('refresh');
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
		        field: 'groupid',
		        title: '采购组代码',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/group/group/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'groupname',
		        title: '采购组名称',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#groupTable').bootstrapTable("toggleView");
		}
	  
	  $('#groupTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#groupTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#groupTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/group/group/import/template';
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
		  $('#groupTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#groupTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#groupTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该采购组记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/group/group/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#groupTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/group/group/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#groupChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/group/group/detail?id="+row.id, function(group){
    	var groupChild1RowIdx = 0, groupChild1Tpl = $("#groupChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  group.groupBuyerList;
		if(data1!=undefined) {
            for (var i = 0; i < data1.length; i++) {
                console.log("=============================>>>>" + data1[i]);
                addRow('#groupChild-' + row.id + '-1-List', groupChild1RowIdx, groupChild1Tpl, data1[i]);
                groupChild1RowIdx = groupChild1RowIdx + 1;
            }
        }
				
    	var groupChild2RowIdx = 0, groupChild2Tpl = $("#groupChild2Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data2 =  group.groupItemClassList;
		if(data2!=undefined) {
            for (var i = 0; i < data2.length; i++) {
                addRow('#groupChild-' + row.id + '-2-List', groupChild2RowIdx, groupChild2Tpl, data2[i]);
                groupChild2RowIdx = groupChild2RowIdx + 1;
            }
        }
				
        var groupChild3RowIdx = 0, groupChild3Tpl = $("#groupChild3Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data3 =  group.groupOrgzList;

		if(data3!=undefined) {
            for (var i = 0; i < data3.length; i++) {
                console.log("=============================>>>>");
                //   console.log(data3[i]);
                addRow('#groupChild-' + row.id + '-3-List', groupChild3RowIdx, groupChild3Tpl, data3[i]);
                groupChild3RowIdx = groupChild3RowIdx + 1;
            }
        }

    	var groupChild4RowIdx = 0, groupChild4Tpl = $("#groupChild4Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data4 =  group.groupSupList;
          // alert(data4);
		  if(data4!=undefined) {
              for (var i = 0; i < data4.length; i++) {
                  addRow('#groupChild-' + row.id + '-4-List', groupChild4RowIdx, groupChild4Tpl, data4[i]);
                  groupChild4RowIdx = groupChild4RowIdx + 1;
              }
          }
				
    	var groupChild5RowIdx = 0, groupChild5Tpl = $("#groupChild5Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data5 =  group.groupWareList;
		if(data5!=undefined) {
            for (var i = 0; i < data5.length; i++) {

                addRow('#groupChild-' + row.id + '-5-List', groupChild5RowIdx, groupChild5Tpl, data5[i]);
                groupChild5RowIdx = groupChild5RowIdx + 1;
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
<script type="text/template" id="groupChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">采购员</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-2" aria-expanded="true">物料类</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-3" aria-expanded="true">部门</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-4" aria-expanded="true">供应商</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-5" aria-expanded="true">仓库</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>采购员编码</th>
								<th>采购员名称</th>
								<th>采购员电话</th>
                                <th>采购员传真</th>
                                <th>送货地址</th>
							</tr>
						</thead>
						<tbody id="groupChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-2" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>物料类代码</th>
								<th>物料类名称</th>
							</tr>
						</thead>
						<tbody id="groupChild-{{idx}}-2-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-3" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>部门代码</th>
								<th>部门名称</th>
							</tr>
						</thead>
						<tbody id="groupChild-{{idx}}-3-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-4" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>供应商代码</th>
								<th>供应商名称</th>
							</tr>
						</thead>
						<tbody id="groupChild-{{idx}}-4-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-5" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>								
								<th>仓库代码</th>
								<th>仓库名称</th>
							</tr>
						</thead>
						<tbody id="groupChild-{{idx}}-5-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="groupChild1Tpl">//<!--
				<tr>
					<td>
						{{row.user.no}}
					</td>
					<td>
						{{row.buyername}}
					</td>
            <td>{{row.buyerphonenum}}</td>
            <td>{{row.buyerfaxnum}}</td>
            <td>{{row.deliveryaddress}}</td>
				</tr>//-->
	</script>
	<script type="text/template" id="groupChild2Tpl">//<!--
				<tr>
					<td>
						{{row.item.code}}
					</td>
					<td>
						{{row.itemclassname}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="groupChild3Tpl">//<!--
				<tr>
					<td>
						{{row.office.code}}
					</td>
					<td>
						{{row.orgzname}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="groupChild4Tpl">//<!--
				<tr>
					<td>
						{{row.account.accountCode}}
					</td>
					<td>
						{{row.supname}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="groupChild5Tpl">//<!--
				<tr>
					<td>
						{{row.warehouse.wareID}}
					</td>
					<td>
						{{row.warename}}
					</td>
				</tr>//-->
	</script>

<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#fileSalaccountTable').bootstrapTable({
		 
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
               url: "${ctx}/accountfile/fileSalaccount/data?accTypeNameRu=客户",
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
                   	window.location = "${ctx}/accountfile/fileSalaccount/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该客户档案维护记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/accountfile/fileSalaccount/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#fileSalaccountTable').bootstrapTable('refresh');
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
		        title: '客户编码',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/accountfile/fileSalaccount/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'accountName',
		        title: '客户名称',
		        sortable: true
		       
		    }
			,{
		        field: 'areaDef.areaCode',
		        title: '所属地区编码',
		        sortable: true
		       
		    }
			,{
		        field: 'areaName',
		        title: '所属地区名称',
		        sortable: true
		       
		    }
			,{
		        field: 'accountProp',
		        title: '企业性质',
		        sortable: true
		       
		    }
			,{
		        field: 'accountMgr',
		        title: '法人代表',
		        sortable: true
		       
		    }
			,{
		        field: 'accountType2.accTypeCode',
		        title: '客户类别编码',
		        sortable: true
		       
		    }
			,{
		        field: 'subTypeName',
		        title: '客户类别名称',
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

		 
		  $('#fileSalaccountTable').bootstrapTable("toggleView");
		}
	  
	  $('#fileSalaccountTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#fileSalaccountTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#fileSalaccountTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/accountfile/fileSalaccount/import/template';
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
		  $('#fileSalaccountTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#fileSalaccountTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#fileSalaccountTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该客户档案维护记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/accountfile/fileSalaccount/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#fileSalaccountTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/accountfile/fileSalaccount/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#fileSalaccountChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/accountfile/fileSalaccount/detail?id="+row.id, function(fileSalaccount){
    	var fileSalaccountChild1RowIdx = 0, fileSalaccountChild1Tpl = $("#fileSalaccountChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  fileSalaccount.fileContractList;
		for (var i=0; i<data1.length; i++){
			addRow('#fileSalaccountChild-'+row.id+'-1-List', fileSalaccountChild1RowIdx, fileSalaccountChild1Tpl, data1[i]);
			fileSalaccountChild1RowIdx = fileSalaccountChild1RowIdx + 1;
		}
				
    	var fileSalaccountChild2RowIdx = 0, fileSalaccountChild2Tpl = $("#fileSalaccountChild2Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data2 =  fileSalaccount.fileLinkManList;
		for (var i=0; i<data2.length; i++){
			addRow('#fileSalaccountChild-'+row.id+'-2-List', fileSalaccountChild2RowIdx, fileSalaccountChild2Tpl, data2[i]);
			fileSalaccountChild2RowIdx = fileSalaccountChild2RowIdx + 1;
		}
				
    	var fileSalaccountChild3RowIdx = 0, fileSalaccountChild3Tpl = $("#fileSalaccountChild3Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data3 =  fileSalaccount.filePickBillList;
		for (var i=0; i<data3.length; i++){
			addRow('#fileSalaccountChild-'+row.id+'-3-List', fileSalaccountChild3RowIdx, fileSalaccountChild3Tpl, data3[i]);
			fileSalaccountChild3RowIdx = fileSalaccountChild3RowIdx + 1;
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
<script type="text/template" id="fileSalaccountChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">销售合同主表</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-2" aria-expanded="true">联系人维护</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-3" aria-expanded="true">销售发货单主表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>合同编码</th>
								<th>签订日期</th>
								<th>合同类型名称</th>
								<th>客户名称</th>
								<th>销售人员姓名</th>
								<th>合同状态</th>
								<th>结案人姓名</th>
								<th>运费总额</th>
								<th>不含税总额</th>
								<th>含税总额</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="fileSalaccountChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-2" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>联系人编码</th>
								<th>联系人名称</th>
								<th>联系人性别</th>
								<th>联系人电话</th>
								<th>联系人手机</th>
								<th>联系人电子邮件</th>
								<th>联系人传真</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="fileSalaccountChild-{{idx}}-2-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-3" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>发货单编码</th>
								<th>制单人姓名</th>
								<th>收货客户名称</th>
								<th>发货单状态</th>
								<th>承运人</th>
								<th>发货日期</th>
								<th>仓库名称</th>
							</tr>
						</thead>
						<tbody id="fileSalaccountChild-{{idx}}-3-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="fileSalaccountChild1Tpl">//<!--
				<tr>
					<td>
						{{row.contractCode}}
					</td>
					<td>
						{{row.signDate}}
					</td>
					<td>
						{{row.contractTypeName}}
					</td>
					<td>
						{{row.accountName}}
					</td>
					<td>
						{{row.personName}}
					</td>
					<td>
						{{row.contractStat}}
					</td>
					<td>
						{{row.endPsnName}}
					</td>
					<td>
						{{row.transfeeSum}}
					</td>
					<td>
						{{row.goodsSum}}
					</td>
					<td>
						{{row.goodsSumTaxed}}
					</td>
					<td>
						{{row.praRemark}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="fileSalaccountChild2Tpl">//<!--
				<tr>
					<td>
						{{row.linkManCode}}
					</td>
					<td>
						{{row.linkManName}}
					</td>
					<td>
						{{row.linkManSex}}
					</td>
					<td>
						{{row.linkManTel}}
					</td>
					<td>
						{{row.linkManMobile}}
					</td>
					<td>
						{{row.linkManEmail}}
					</td>
					<td>
						{{row.linkManFax}}
					</td>
					<td>
						{{row.remark}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="fileSalaccountChild3Tpl">//<!--
				<tr>
					<td>
						{{row.pbillCode}}
					</td>
					<td>
						{{row.pbillPsnName}}
					</td>
					<td>
						{{row.rcvAccountName}}
					</td>
					<td>
						{{dict.pbillStat}}
					</td>
					<td>
						{{row.transAccount}}
					</td>
					<td>
						{{row.pickDate}}
					</td>
					<td>
						{{row.wareName}}
					</td>
				</tr>//-->
	</script>

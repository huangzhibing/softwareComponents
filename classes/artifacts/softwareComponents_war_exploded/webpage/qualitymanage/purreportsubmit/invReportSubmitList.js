<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	
 //  var url="${ctx}/purreport/purReportSubmit/data";
	
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
    	       detailView: true,
    	       	//显示详细内容函数
	           detailFormatter: "detailFormatter",
    	       //最低显示2行
    	       minimumCountColumns: 2,
               //是否显示行间隔色
               striped: true,
               //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）                   
               cache: false,   
               //单选
               singleSelect: true,
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
               url: "${ctx}/purreport/invReportSubmit/data",
          //     url: "${ctx}/purreport/purReportSubmit/search",  
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
           //    contextMenu: '#context-menu',
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
		        checkbox: true
		       
		    }
			,{
		        field: 'reportId',
		        title: '检验单编号',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return "<a href='${ctx}/purreport/invReportSubmit/form?id="+row.id+"'>"+value+"</a>";
		          }
		       
		    }
			,{
		        field: 'reinvCheckMain.billNum',
		        title: '超期复验单编号',
		        sortable: true,
		       /* formatter:function(value, row , index){
			        	return "<a href='${ctx}/purreport/sfcReportSubmit/form?id="+row.id+"'>"+value+"</a>";
		        	//return "<span>"+"</span>"
			        }
			   */     
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
		/*	,{
		        field: 'qualityNorm',
		        title: '执行标准',
		        sortable: true
		       
		    }
		*/	,{
		        field: 'office.name',
		        title: '检验部门名称',
		        sortable: true
		       
		    }
			,{
		        field: 'cpositionName',
		        title: '检验岗位名称',
		        sortable: true
		       
		    }
			/*	,{
		        field: 'checkDate',
		        title: '检验日期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	//alert(value.split(" ")[0]);
		        	var date=value.split(" ")[0];
		        	return date;
		        }
		       },{
		        field: 'checkTime',
		        title: '检验时间',
		        sortable: true,
		        
		    }
			,{
		        field: 'checkPerson',
		        title: '检验人名称',
		        sortable: true
		       
		    }*/
			,{
		        field: 'memo',
		        title: '备注',
		        sortable: true
		       
		    }
		     ]
		
		});
		
	  //如果是移动端  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){ 
		  $('#purReportTable').bootstrapTable("toggleView");
		}
	  //选中则激活按钮
	  $('#purReportTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
           // $('#remove').prop('disabled', ! $('#purReportTable').bootstrapTable('getSelections').length);
           // $('#edit').prop('disabled', $('#purReportTable').bootstrapTable('getSelections').length!=1);
            $('#view').prop('disabled', $('#purReportTable').bootstrapTable('getSelections').length!=1);
        });
		  
		
	   // 绑定查询按扭    
	  $("#search").click("click", function() {
		//  url="${ctx}/purreport/purReportSubmit/search";
		//  alert(url);
		  $('#purReportTable').bootstrapTable('refresh');
		});
	   // 绑定重置按扭
	 $("#reset").click("click", function() {
		  $("#searchForm  input").val("");
		//  $("#searchForm  select").val("");
		 // $("#checkResult").val("");
		  $("#rndFul").val("");
		  $("#sendState").val("");
		  $("#searchForm  .select-item").html("");
		  $('#purReportTable').bootstrapTable('refresh');
		});
		//起始日期控件
		$('#beginCheckDate').datetimepicker({
			 format: "YYYY-MM-DD",
			// startDate:new Date()
		});
	
	/*	
		$("#beginCheckDate").datetimepicker({  
		  //  language : 'zh-CN',  
		  //  weekStart : 1,  
		  //  todayBtn : 1,  
		  //  autoclose : 1,  
		  //  todayHighlight : 1,  
		 //   startView : 2,  
		 //   minView: "month",  
		    format: 'YYYY-MM-DD',  
		//    forceParse : 0,  
		    endDate : "2018-4-14"  
		}).on('hide', function(event) {  
		    event.preventDefault();  
		    event.stopPropagation();  
		    var startTime = event.date;  
		    $('#endCheckDate').datetimepicker('setStartDate',startTime);  
		    $('#endCheckDate').val("");  
		});  
		
		
		$("#endCheckDate").datetimepicker({  
		    language : 'zh-CN',  
		    weekStart : 1,  
		    todayBtn : 1,  
		    autoclose : 1,  
		    todayHighlight : 1,  
		    startView : 2,  
		    minView: "month",  
		    format: 'yyyy-mm-dd',  
		    forceParse : 0  
		}).on('hide', function(event) {  
		    event.preventDefault();  
		    event.stopPropagation();  
		    var endTime = event.date;  
		    $("#beginCheckDate").datetimepicker('setEndDate',endTime);  
		});  
		*/	
		//终止日期控件
		$('#endCheckDate').datetimepicker({
			 format: "YYYY-MM-DD",
			// startDate:new Date()
	    });	
		/*	
		 $("#beginCheckDate").datetimepicker({
			    format: 'yyyy-mm-dd',
			//    minView:'month',
			///    language: 'zh-CN',
			//    autoclose:true,
			//    startDate:new Date()
			  }).on("click",function(){
			    $("#beginCheckDate").datetimepicker("setEndDate",$("#endCheckDate").val())
			  });
			  $("#endCheckDate").datetimepicker({
			    format: 'yyyy-mm-dd',
			//    minView:'month',
			//    language: 'zh-CN',
			//    autoclose:true,
			//    startDate:new Date()
			  }).on("click",function(){
			    $("#endCheckDate").datetimepicker("setStartDate",$("#beginCheckDate".val()))
			  });
		*/
		
	});
		
  function getIdSelections() {
        return $.map($("#purReportTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该检验单记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/purreport/invReportSubmit/deleteAll?ids=" + getIdSelections(), function(data){
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
	  window.location = "${ctx}/purreport/invReportSubmit/form?id=" + getIdSelections();
  }
  
  		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#purReportChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/purreport/invReportSubmit/detail?id="+row.id, function(purReport){
    	var purReportChild1RowIdx = 0, purReportChild1Tpl = $("#purReportChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  purReport.purReportRSnList;
		for (var i=0; i<data1.length; i++){
			data1[i].checkDate=data1[i].checkDate.split(" ")[0];
			addRow('#purReportChild-'+row.id+'-1-List', purReportChild1RowIdx, purReportChild1Tpl, data1[i]);
			purReportChild1RowIdx = purReportChild1RowIdx + 1;
		}
				
      	  			
      })
     
        return html;
    }
  
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}
	//点击详情按钮事件
	function view(){
		  window.location = "${ctx}/purreport/invReportSubmit/form?id=" + getIdSelections();
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
								<th>检验结论</th>
								<th>检验日期</th>
								<th>检验时间</th>
								<th>检验人名称</th>
								<th>备注</th>
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
				</tr>//-->
	</script>

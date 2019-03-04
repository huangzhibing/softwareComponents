<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#qmReportTable1').bootstrapTable({
		 
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
               url: "${ctx}/mserialnoprint/mSerialNoPrint/formPZdata?mCode=" +getMCode() ,
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
                   	window.location = "${ctx}/qmreport/qmReportMachine/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该质量问题记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/qmreport/qmReportMachine/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#qmReportTable1').bootstrapTable('refresh');
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
		        field: 'qmreportId',
		        title: '问题处理报告单编号',
		        sortable: true
		       
		    }
			,{
		        field: 'qmPerson.name',
		        title: '问题处理人',
		        sortable: true
		       
		    }
			,{
		        field: 'qmDate',
		        title: '问题处理日期',
		        sortable: true
		       
		    }
			,{
		        field: 'mhandlingName.mhandlingname',
		        title: '问题处理意见',
		        sortable: true
		       
		    }
			,{
		        field: 'state',
		        title: '单据状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('done_or_not'))}, value, "-");
		        }
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#qmReportTable1').bootstrapTable("toggleView");
		}
	  
	  $('#qmReportTable1').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#qmReportTable1').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#qmReportTable1').bootstrapTable('getSelections').length!=1);
            $('#item_detail').prop('disabled', $('#qmReportTable1').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/qmreport/qmReportMachine/import/template';
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
		  $('#qmReportTable1').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#qmReportTable1').bootstrapTable('refresh');
		});
		
	  $('#beginQmDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
	  });
	  $('#endQmDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
	  });
	  
	 // updateNotProcDisplay();
		
	});

{/*  function updateNotProcDisplay(){
		jp.get("${ctx}/purreport/purQmReport/dataNotProcessNum", function(data){
 	  		if(data!=null){
 	  			var notSubmitNum=data.body.total-data.body.submitNum;
 	  			var resultStr="当前有<span style='color: red;'>"+notSubmitNum+"</span>个未合格的采购检验单未处理！";
 	  			if(data.body.editNum>0){
 	  				resultStr+="其中<span style='color: green;'>"+data.body.editNum+"</span>个未合格的采购检验单被编辑中。";
 	  			}
 	  			$('#purReportRemain').html(resultStr);
 	  		}
 	  		
 	  	});
  }*/}


  function getIdSelections() {
        return $.map($("#qmReportTable1").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该质量问题记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/qmreport/qmReportMachine/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#qmReportTable1').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  			updateNotProcDisplay();
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	});
          	   
		});
		
  }


	function getMCode(){
		/*alert("${mCode}");*/
		return "${mCode}";
	}
  function edit(){
	  window.location = "${ctx}/qmreport/qmReportMachine/form?id=" + getIdSelections();
  }
  
  function itemDetail(){
	  window.location = "${ctx}/qmreport/qmReportMachine/form?id=" + getIdSelections()+"&justSee=true";
  }
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#qmReportChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/qmreport/qmReportMachine/detail?id="+row.id, function(qmReport){
    	var qmReportChild1RowIdx = 0, qmReportChild1Tpl = $("#qmReportChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  qmReport.qmReportRSnList;
		for (var i=0; i<data1.length; i++){
			addRow('#qmReportChild-'+row.id+'-1-List', qmReportChild1RowIdx, qmReportChild1Tpl, data1[i]);
			qmReportChild1RowIdx = qmReportChild1RowIdx + 1;
		}
				
      	  			
      })
     
        return html;
    }
  
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
		console.log(row);
	}
			
</script>
<script type="text/template" id="qmReportChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">不合格的检验对象</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>检验对象序列号</th>
								<th>检验对象编码</th>
								<th>检验对象名称</th>
								<th>问题类型</th>
								<th>检验结论</th>
                                <th>机器序列号</th>
								<th>检验日期</th>
								<th>检验时间</th>
								<th>检验人名称</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="qmReportChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="qmReportChild1Tpl">//<!--
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
					{{row.matterName}}
				    </td>
					<td>
						{{row.checkResult}}
					</td>
					<td>
						{{row.reportId}}
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

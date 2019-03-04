<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#materialReplaceLogTable').bootstrapTable({
		 
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
               url: "${ctx}/replacelog/materialReplaceLog/data",
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
              // contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	window.location = "${ctx}/replacelog/materialReplaceLog/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该换件记录记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/replacelog/materialReplaceLog/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#materialReplaceLogTable').bootstrapTable('refresh');
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
			/*
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true
		       
		    }*/
		   ,{
			   field: 'serialno',
			   title: '机器序列号',
			   sortable: true

		   }
			,{
		        field: 'routinebillno',
		        title: '加工路线单号',
		        sortable: true
		       
		    }
			,{
		        field: 'seqno',
		        title: '单件序号',
		        sortable: true
		       
		    }
			,{
		        field: 'routingcode',
		        title: '工艺编码',
		        sortable: true
		       
		    }



			,{
		        field: 'routingname',
		        title: '工艺名称',
		        sortable: true
		       
		    }
		   ,{
			   field: 'itemcode',
			   title: '物料编码',
			   sortable: true

		   }
			,{
		        field: 'itemname',
		        title: '物料名称',
		        sortable: true
		       
		    }
		   ,{
			   field: 'itemsn',
			   title: '物料二维码',
			   sortable: true

		   }
			/*,{
		        field: 'assembleqty',
		        title: '数量',
		        sortable: true
		       
		    }
			,{
		        field: 'finishedqty',
		        title: '已安装数量',
		        sortable: true
		       
		    }*/
		   ,{
			   field: 'centercode',
			   title: '工作中心代码',
			   sortable: true

		   }
			,{
		        field: 'centername',
		        title: '工作中心名称',
		        sortable: true
		       
		    }
			/*,{
		        field: 'assemblepersonid',
		        title: '装配人员ID',
		        sortable: true
		       
		    }*/
			,{
		        field: 'assemblepersonname',
		        title: '装配人员',
		        sortable: true
		       
		    }
			,{
		        field: 'assembletime',
		        title: '装配时间',
		        sortable: true
		       
		    }
		  /* ,{
			   field: 'updateDate',
			   title: '更新时间',
			   sortable: true
			   /!* ,formatter:function(value, row , index){
					return "<a href='${ctx}/replacelog/materialReplaceLog/form?id="+row.id+"'>"+value+"</a>";
				 }*!/

		   }*/
			/*,{
		        field: 'actualteamcode',
		        title: '实作班组',
		        sortable: true
		       
		    }
			,{
		        field: 'hasqualitypro',
		        title: '是否有品质异常问题',
		        sortable: true
		       
		    }
			,{
		        field: 'qualityproblem',
		        title: '异常类型',
		        sortable: true
		       
		    }*/
			,{
		        field: 'originalid',
		        title: '原表中的ID',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#materialReplaceLogTable').bootstrapTable("toggleView");
		}
	  
	  $('#materialReplaceLogTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#materialReplaceLogTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#materialReplaceLogTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/replacelog/materialReplaceLog/import/template';
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
		  $('#materialReplaceLogTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#materialReplaceLogTable').bootstrapTable('refresh');
		});
		
		$('#beginAssembletime').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endAssembletime').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		
	});
		
  function getIdSelections() {
        return $.map($("#materialReplaceLogTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该换件记录记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/replacelog/materialReplaceLog/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#materialReplaceLogTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/replacelog/materialReplaceLog/form?id=" + getIdSelections();
  }
  
</script>
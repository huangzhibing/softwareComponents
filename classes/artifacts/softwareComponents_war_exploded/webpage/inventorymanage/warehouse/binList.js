<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#binTable').bootstrapTable({

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
               url: "${ctx}/bin/bin/data",
               //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
               //queryParamsType:'',
               ////查询参数,每次调用是会带上这个参数，可自定义
               queryParams : function(params) {
               	var searchParam = $("#searchForm-bin").serializeJSON();
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
                       jp.openDialog('编辑货区',"${ctx}/bin/bin/form?id=" + row.id,'800px','500px',$('#binTable'))
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该货区记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/bin/bin/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#binTable').bootstrapTable('refresh');
                                refreshTree()
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
                   field: 'binId',
                   title: '货区号',
                   sortable: true,
                   formatter:function(value, row , index){
                       // return "<a href='${ctx}/bin/bin/form?id="+row.id+"'>"+value+"</a>";
                       return "<a onclick='openView(\"bin\",\""+ row.id+ "\")' href='javascript:void(0)'>"+value+"</a>";
                   }
               }
			,{
		        field: 'wareId',
		        title: '库房号',
		        sortable: true

		    }
			,{
		        field: 'binDesc',
		        title: '货区名称',
		        sortable: true

		    }
			,{
		        field: 'note',
		        title: '备注',
		        sortable: true

		    }
		     ]

		});


	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


		  $('#binTable').bootstrapTable("toggleView");
		}

	  $('#binTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove-bin').prop('disabled', ! $('#binTable').bootstrapTable('getSelections').length);
            $('#edit-bin').prop('disabled', $('#binTable').bootstrapTable('getSelections').length!=1);
        });

		$("#btnImport").click(function(){
			jp.open({
			    type: 1,
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/bin/bin/import/template';
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

	  $("#search-bin").click("click", function() {// 绑定查询按扭
		  $('#binTable').bootstrapTable('refresh');
		});

	 $("#reset-bin").click("click", function() {// 绑定查询按扭
		  $("#searchForm-bin  input").val("");
		  $("#searchForm-bin  select").val("");
		  $("#searchForm-bin  .select-item").html("");
		  $('#binTable').bootstrapTable('refresh');
		});


	});

  function getIdSelectionsBin() {
        return $.map($("#binTable").bootstrapTable('getSelections'), function (row) {
            // console.log(row.id)
            return row.id
        });
    }

  function deleteAllBin(){

		jp.confirm('确认要删除该货区记录吗？', function(){
			jp.loading();
			jp.get("${ctx}/bin/bin/deleteAll?ids=" + getIdSelectionsBin(), function(data){
         	  		if(data.success){
         	  			$('#binTable').bootstrapTable('refresh');
                        refreshTree()
                        jp.success(data.msg);
         	  		}else{
                        refreshTree()
                        jp.error(data.msg);
         	  		}
         	  	})

		})
  }
  function editBin(){
	  // window.location = "${ctx}/bin/bin/form?id=" + getIdSelectionsBin();

      jp.openDialog('编辑货区',"${ctx}/bin/bin/form?id=" + getIdSelectionsBin(),'800px','500px',$('#binTable'))
  }

</script>
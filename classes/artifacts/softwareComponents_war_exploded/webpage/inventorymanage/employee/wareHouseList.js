<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#wareHouseTable').bootstrapTable({

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
               url: "${ctx}/warehouse/wareHouse/data",
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
                   	window.location = "${ctx}/warehouse/wareHouse/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该仓库记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/warehouse/wareHouse/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#wareHouseTable').bootstrapTable('refresh');
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
		        field: 'wareID',
		        title: '库房号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a onclick='openView(\"\",\""+ row.id+ "\")' href='javascript:void(0)'>"+value+"</a>";
		         }

		    }
			,{
		        field: 'wareName',
		        title: '库房名称',
		        sortable: true

		    }
			,{
		        field: 'wareAddress',
		        title: '库房地址',
		        sortable: true

		    }
			,{
		        field: 'planCost',
		        title: '计划资金',
		        sortable: true

		    }
			,{
		        field: 'adminMode',
		        title: '管理标识',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('adminMode'))}, value, "-");
		        }

		    }
			,{
		        field: 'priceMode',
		        title: '价格设置',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('priceMode'))}, value, "-");
		        }

		    }
			,{
		        field: 'currPeriod',
		        title: '当前结算期',
		        sortable: true

		    }
			,{
		        field: 'lastCarrdate',
		        title: '最近结转日期',
		        sortable: true

		    }
			,{
		        field: 'note',
		        title: '备注',
		        sortable: true

		    }
			,{
		        field: 'itemType',
		        title: '货物标志',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('itemType'))}, value, "-");
		        }

		    }
			,{
		        field: 'subFlag',
		        title: '是否允许库存为负标志',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('subFlag'))}, value, "-");
		        }

		    }
			,{
		        field: 'taxFlag',
		        title: '是否为含税价入库标志',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('taxFlag'))}, value, "-");
		        }

		    }
			,{
		        field: 'batchFlag',
		        title: '批次管理标志',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('batchFlag'))}, value, "-");
		        }

		    }
		     ]

		});


	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


		  $('#wareHouseTable').bootstrapTable("toggleView");
		}

	  $('#wareHouseTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#wareHouseTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#wareHouseTable').bootstrapTable('getSelections').length!=1);
        });

		$("#btnImport").click(function(){
			jp.open({
			    type: 1,
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/warehouse/wareHouse/import/template';
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
		  $('#wareHouseTable').bootstrapTable('refresh');
		});

	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#wareHouseTable').bootstrapTable('refresh');
		});


	});

  function getIdSelections() {
        return $.map($("#wareHouseTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

  function deleteAll(){

		jp.confirm('确认要删除该仓库记录吗？', function(){
			jp.loading();
			jp.get("${ctx}/warehouse/wareHouse/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#wareHouseTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})

		})
  }
  function edit(){
	  window.location = "${ctx}/warehouse/wareHouse/form?id=" + getIdSelections();
  }
  function openView(type,id){
      if(type === 'bin'){
          url = "${ctx}/bin/bin/form?id=" + id
      } else if(type === 'location'){
          url = "${ctx}/location/location/form?id=" + id
      } else {
          url = "${ctx}/warehouse/wareHouse/form?id=" + id
      }
      jp.openDialogView("查看",url,'800px','500px')
  }

  function wareHouseForm(type){
      var url = ''
      var wareId = ''
      var tableName = 'wareHouseTable'
      console.log(type)
      if(type === 'bin'){
          var loading = jp.loading('加载中')
          url = $('#add-bin').attr('url')
          wareId = $('#wareId').val()
          tableName = 'binTable'
          // jp.get("${ctx}/warehouse/wareHouse/checkAddable?wareId="+ wareId + "&type=bin",function(res){
          //   console.log(res)
          // })
          $.ajax({
              url:"${ctx}/warehouse/wareHouse/checkAddable?wareId="+ wareId + "&type=bin",
              success:function(res){
                  console.log(res)
                  if(!res){

                      jp.warning('该仓库不允许添加该类型结点')
                      return false
                  } else {
                      jp.openDialog('新建', url, '800px', '500px', $('#'+tableName))

                  }
              },
              complete:function(){
                  jp.close(loading)
              }
          })
      } else if(type === 'location'){
          var loading = jp.loading('加载中')
          url = $('#add-location').attr('url')
          wareId = $('#binId').val().substring(0,4)
          tableName = 'locationTable'
          $.ajax({
              url:"${ctx}/warehouse/wareHouse/checkAddable?wareId="+ wareId + "&type=location",
              success:function(res){
                  console.log(res)
                  if(!res){
                      jp.warning('该仓库不允许添加该类型结点')
                      return false
                  } else {
                      jp.openDialog('新建', url, '800px', '500px', $('#'+tableName))
                  }
              },
              complete:function(){
                  jp.close(loading)
              }
          })
      } else {
          url = $('#add').attr('url')
          // jp.openDialog('新建', url, '800px', '500px', $('#wareHouseTable'))
          jp.openDialog('新建', '${ctx}/warehouse/wareHouse/form' ,'800px', '500px',$('#wareHouseTable'))

      }
      // if(!url){
      //     jp.openDialog('新建', '${ctx}/warehouse/wareHouse/form' ,'800px', '500px',$('#wareHouseTable'))
      // } else {
      //     jp.openDialog('新建', url, '800px', '500px', $('#wareHouseTable'))
      // }
  }

  function refreshTree(){
      refresh()
  }

</script>
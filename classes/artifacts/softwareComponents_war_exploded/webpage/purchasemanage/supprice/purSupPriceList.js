<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#purSupPriceTable').bootstrapTable({

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
               url: "${ctx}/supprice/purSupPrice/data",
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
                   	window.location = "${ctx}/supprice/purSupPrice/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该供应商价格维护记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/supprice/purSupPrice/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#purSupPriceTable').bootstrapTable('refresh');
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
		        field: 'account.accountCode',
		        title: '供应商代码',
		        sortable: true
		        ,formatter:function(value, row , index){
		            if(value == null){
		            	return "<a href='${ctx}/supprice/purSupPrice/form?id="+row.id+"'>-</a>";
		            }else{
		            	return "<a href='${ctx}/supprice/purSupPrice/form?id="+row.id+"'>"+value+"</a>";
		            }

		        }

		    }
			,{
		        field: 'supName',
		        title: '供应商名称',
		        sortable: true

		    }
			,{
		        field: 'areaCode',
		        title: '所属地区编码',
		        sortable: true

		    }
			,{
		        field: 'areaName',
		        title: '所属地区名称',
		        sortable: true

		    }
			,{
		        field: 'supType.suptypeId',
		        title: '供应商类别编码',
		        sortable: true

		    }
			,{
		        field: 'supTypeName',
		        title: '供应商类别名称',
		        sortable: true

		    }
			,{
		        field: 'prop',
		        title: '企业性质',
		        sortable: true

		    }
			,{
		        field: 'address',
		        title: '地址',
		        sortable: true

		    }
		     ]

		});


	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


		  $('#purSupPriceTable').bootstrapTable("toggleView");
		}

	  $('#purSupPriceTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#purSupPriceTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#purSupPriceTable').bootstrapTable('getSelections').length!=1);
        });

		$("#btnImport").click(function(){
			jp.open({
			    type: 1,
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/supprice/purSupPrice/import/template';
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
		  $('#purSupPriceTable').bootstrapTable('refresh');
		});

	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#purSupPriceTable').bootstrapTable('refresh');
		});
	 $('#date').datetimepicker({
		 format: "YYYY-MM-DD HH:mm:ss"
	});

	});

  function getIdSelections() {
        return $.map($("#purSupPriceTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

  function deleteAll(){

		jp.confirm('确认要删除该供应商价格维护记录吗？', function(){
			jp.loading();
			jp.get("${ctx}/supprice/purSupPrice/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#purSupPriceTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})

		})
  }
  function edit(){
	  window.location = "${ctx}/supprice/purSupPrice/form?id=" + getIdSelections();
  }





  function detailFormatter(index, row) {
	  var htmltpl =  $("#purSupPriceChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/supprice/purSupPrice/detail?id="+row.id, function(purSupPrice){
    	var purSupPriceChild1RowIdx = 0, purSupPriceChild1Tpl = $("#purSupPriceChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  purSupPrice.purSupPriceDetailList;
		for (var i=0; i<data1.length; i++){
			addRow('#purSupPriceChild-'+row.id+'-1-List', purSupPriceChild1RowIdx, purSupPriceChild1Tpl, data1[i]);
			purSupPriceChild1RowIdx = purSupPriceChild1RowIdx + 1;
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
<script type="text/template" id="purSupPriceChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">供应商价格维护明细</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>物料编码</th>
								<th>物料名称</th>
    <th>物料规格</th>
    <th>物料单位</th>
								<th>开始时间</th>
								<th>结束时间</th>
								<th>最小数量</th>
								<th>最大数量</th>
								<th>价格</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="purSupPriceChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="purSupPriceChild1Tpl">//<!--
				<tr>
					<td>
						{{row.item.code}}
					</td>
					<td>
						{{row.itemName}}
					</td>
                <td>
                       {{row.itemSpecmodel}}
               </td>
              <td>
                      {{row.unitName}}
             </td>
					<td>
						{{row.startDate}}
					</td>
					<td>
						{{row.endDate}}
					</td>
					<td>
						{{row.minQty}}
					</td>
					<td>
						{{row.maxQty}}
					</td>
					<td>
						{{row.supPrice}}
					</td>
					<td>
						{{row.notes}}
					</td>
				</tr>//-->
	</script>

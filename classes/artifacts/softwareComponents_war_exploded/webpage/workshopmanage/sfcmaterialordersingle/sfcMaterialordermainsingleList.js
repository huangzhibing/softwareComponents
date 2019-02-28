<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#sfcMaterialordermainsingleTable').bootstrapTable({
		 
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
               url: "${ctx}/sfcmaterialordersingle/sfcMaterialordermainsingle/data",
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
                   	window.location = "${ctx}/sfcmaterialordersingle/sfcMaterialordermainsingle/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该原单行领料单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/sfcmaterialordersingle/sfcMaterialordermainsingle/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#sfcMaterialordermainsingleTable').bootstrapTable('refresh');
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
		        field: 'updateDate',
		        title: '更新时间',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/sfcmaterialordersingle/sfcMaterialordermainsingle/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true
		       
		    }
			,{
		        field: 'materialorder',
		        title: '单据号',
		        sortable: true
		       
		    }
			,{
		        field: 'receivetype',
		        title: '领料类型',
		        sortable: true
		       
		    }
			,{
		        field: 'iotype',
		        title: '出入库标记',
		        sortable: true
		       
		    }
			,{
		        field: 'billtype',
		        title: '单据类型',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('sfc_material_type'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'outflag',
		        title: '出库标志',
		        sortable: true
		       
		    }
			,{
		        field: 'materialtype',
		        title: '物料类型',
		        sortable: true
		       
		    }
			,{
		        field: 'wareid',
		        title: '库房代码',
		        sortable: true
		       
		    }
			,{
		        field: 'warename',
		        title: '库房名称',
		        sortable: true
		       
		    }
			,{
		        field: 'periodid',
		        title: '核算期',
		        sortable: true
		       
		    }
			,{
		        field: 'itemtype',
		        title: '领料类型',
		        sortable: true
		       
		    }
			,{
		        field: 'shopid',
		        title: '部门代码',
		        sortable: true
		       
		    }
			,{
		        field: 'shopname',
		        title: '部门名称',
		        sortable: true
		       
		    }
			,{
		        field: 'responser',
		        title: '领料人代码',
		        sortable: true
		       
		    }
			,{
		        field: 'respname',
		        title: '领料人姓名',
		        sortable: true
		       
		    }
			,{
		        field: 'editor',
		        title: '制单人名称',
		        sortable: true
		       
		    }
			,{
		        field: 'editdate',
		        title: '制单日期',
		        sortable: true
		       
		    }
			,{
		        field: 'useid',
		        title: '用途代码',
		        sortable: true
		       
		    }
			,{
		        field: 'usedesc',
		        title: '用途名称',
		        sortable: true
		       
		    }
			,{
		        field: 'editorid',
		        title: '制单人编码',
		        sortable: true
		       
		    }
			,{
		        field: 'billstateflag',
		        title: '单据状态',
		        sortable: true
		       
		    }
			,{
		        field: 'operdate',
		        title: '业务日期',
		        sortable: true
		       
		    }
			,{
		        field: 'wareempid',
		        title: '库管员编码',
		        sortable: true
		       
		    }
			,{
		        field: 'wareempname',
		        title: '库管员名称',
		        sortable: true
		       
		    }
			,{
		        field: 'opercode',
		        title: '工位编码',
		        sortable: true
		       
		    }
			,{
		        field: 'opername',
		        title: '工位名称',
		        sortable: true
		       
		    }
			,{
		        field: 'routinebillno',
		        title: '加工路线单号',
		        sortable: true
		       
		    }
			,{
		        field: 'routingcode',
		        title: '工艺编码',
		        sortable: true
		       
		    }
			,{
		        field: 'invbillno',
		        title: '出库单号',
		        sortable: true
		       
		    }
			,{
		        field: 'materialorderinreturn',
		        title: '退料对应领料单号',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#sfcMaterialordermainsingleTable').bootstrapTable("toggleView");
		}
	  
	  $('#sfcMaterialordermainsingleTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#sfcMaterialordermainsingleTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#sfcMaterialordermainsingleTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/sfcmaterialordersingle/sfcMaterialordermainsingle/import/template';
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
		  $('#sfcMaterialordermainsingleTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#sfcMaterialordermainsingleTable').bootstrapTable('refresh');
		});
		
		$('#beginEditdate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endEditdate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#beginOperdate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endOperdate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		
	});
		
  function getIdSelections() {
        return $.map($("#sfcMaterialordermainsingleTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该原单行领料单记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/sfcmaterialordersingle/sfcMaterialordermainsingle/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#sfcMaterialordermainsingleTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/sfcmaterialordersingle/sfcMaterialordermainsingle/form?id=" + getIdSelections();
  }
  
</script>
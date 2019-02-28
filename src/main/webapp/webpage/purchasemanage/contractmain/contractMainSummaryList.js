<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#contractMainTable').bootstrapTable({
		 
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
    	    //   detailView: true,
    	       	//显示详细内容函数
	       //    detailFormatter: "detailFormatter",
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
               url: "${ctx}/contractmain/contractMainSummary/data",
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
               columns: [  {
		       // checkbox: true
            	    title: '序号',//标题  可不加
					formatter: function (value, row, index) {
						return index+1;
					}
		    }			
			,{
		        field: 'madeDate',
		        title: '下单日期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	if(value==undefined) {
			       		return "";}
			       	else{
			       	// return value.split(" ")[0];
			       		return value;
			       	}
			    }
		       
		    }		
			,{
		        field: 'accountName',
		        title: '供应商名称',
		        sortable: true
		       
		    }
			,{
		        field: 'itemName',
		        title: '物料名称',
		        sortable: true
		       
		    }
			,{
		        field: 'itemModel',
		        title: '物料规格',
		        sortable: true
		       
		    }
			,{
		        field: 'unit',
		        title: '单位',
		        sortable: true		        
		    }
			,{
		        field: 'orderTimes',
		        title: '下单次数',
		        sortable: true
		       
		    }
			,{
		        field: 'orderNum',
		        title: '下单数量',
		        sortable: true
		       
		    }
			,{
		        field: 'itemPriceTaxed',
		        title: '含税单价'
		     // sortable: true
		       
		    }
			,{
		        field: 'itemSumTaxed',
		        title: '总额',
		        sortable: true
		       
		    }
			,{
		        field: 'notes',
		        title: '备注',
		        sortable: true,
		        formatter:function(value, row , index){
		           if(value==undefined) {
		       		  return "";
		       	   }
		        }
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#contractMainTable').bootstrapTable("toggleView");
		}
	  
	  $('#contractMainTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#contractMainTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#contractMainTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/contractmain/contractMain/import/template';
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
		  $('#contractMainTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#contractMainTable').bootstrapTable('refresh');
		});
		
	 $('#createBeginDate').datetimepicker({
					 format: "YYYY-MM-DD"
	 });
	 
	 $('#createEndDate').datetimepicker({
					 format: "YYYY-MM-DD"
	 });
	 
	});
		
  function getIdSelections() {
        return $.map($("#contractMainTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该采购合同管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/contractmain/contractMain/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#contractMainTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
 
  
  //采购员标签内函数
  function selectPlan(buyerId){		
  }
  
//点击打印按钮，触发事件
	function print(){	
	    var src = $("#printIframe").attr("src");
	    if(src==undefined||src==''){
	           $("#printIframe").attr("src","${ctx}/contractmain/contractMainSummary/list");
	           $("#printIframe").load(function(){
	              $("#printIframe")[0].contentWindow.print();
	           });
	     }else{
	           $("#printIframe")[0].contentWindow.print();
	     }
	}
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#contractMainChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/contractmain/contractMain/detail?id="+row.id, function(contractMain){
    	var contractMainChild1RowIdx = 0, contractMainChild1Tpl = $("#contractMainChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  contractMain.contractDetailList;
		for (var i=0; i<data1.length; i++){
			addRow('#contractMainChild-'+row.id+'-1-List', contractMainChild1RowIdx, contractMainChild1Tpl, data1[i]);
			contractMainChild1RowIdx = contractMainChild1RowIdx + 1;
		}      	  			
      });
        return html;
    }
  
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}
	
	function queryParam(){
		
	}	
	//点击打印按钮，触发事件
	/*function print(){	
	    var src = $("#printIframe").attr("src");
	    if(src==undefined||src==''){
	           $("#printIframe").attr("src","${ctx}/contractmain/contractMainSummary/print");
	           $("#printIframe").load(function(){
	              $("#printIframe")[0].contentWindow.print();
	           });
	     }else{
	           $("#printIframe")[0].contentWindow.print();
	     }
	}*/
	//点击打印按钮，触发事件
	function print(){		  
		var data = document.getElementById("contractMainTable").innerHTML;
	  	$("#printContent").html("");
	  	var thead = "<p align= 'center'>采购订单汇总表</p>";
		$("#printContent").html("<style>table,table th,table td{border:1px solid}</style>"+thead+"<table style='font-size:12px; border-collapse:collapse;;margin:auto'>"+data+"</table>");
		$("#printContent").jqprint({
			debug:false,
			importCSS:true,
			printContainer:true,
			operaSupport: true
		});
		$("#printContent").html("");
	}
	
</script>


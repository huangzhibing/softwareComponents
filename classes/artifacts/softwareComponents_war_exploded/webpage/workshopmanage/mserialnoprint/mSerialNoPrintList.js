<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#mSerialNoPrintTable1').bootstrapTable({
		 
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
               url: "${ctx}/mserialnoprint/mSerialNoPrint/data",
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
              //contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	window.location = "${ctx}/mserialnoprint/mSerialNoPrint/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该机器序列号打印记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/mserialnoprint/mSerialNoPrint/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#mSerialNoPrintTable1').bootstrapTable('refresh');
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
		        field: 'mserialno',
		        title: '机器序列号',
		        sortable: true
		       
		    }
			,{
		        field: 'mpsplanid',
		        title: '主生产计划号',
		        sortable: true
		       
		    }
		/*	,{
		        field: 'processbillno',
		        title: '车间作业计划号',
		        sortable: true
		       
		    }*/
			/*,{
		        field: 'batchno',
		        title: '车间作业计划分批号',
		        sortable: true
		       
		    }*/
			/*,{
		        field: 'routinebillno',
		        title: '加工路线单号',
		        sortable: true
		       
		    }*/
			/*,{
		        field: 'routingcode',
		        title: '工艺编码',
		        sortable: true
		       
		    }
			,{
		        field: 'seqno',
		        title: '加工路线单单件序号',
		        sortable: true
		       
		    }
			,{
		        field: 'isassigned',
		        title: '已分配标志',
		        sortable: true
		       
		    }*/
			,{
		        field: 'prodcode',
		        title: '产品编码',
		        sortable: true
		       
		    }
			,{
		        field: 'prodname',
		        title: '产品名称',
		        sortable: true
		       
		    }
		  /* ,{
			   field: 'objsn',
			   title: '整机二维码',
			   sortable: true

		   }*/
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#mSerialNoPrintTable1').bootstrapTable("toggleView");
		}
	  
	  $('#mSerialNoPrintTable1').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#mSerialNoPrintTable1').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled',! $('#mSerialNoPrintTable1').bootstrapTable('getSelections').length);
          $('#edit1').prop('disabled',! $('#mSerialNoPrintTable1').bootstrapTable('getSelections').length);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/mserialnoprint/mSerialNoPrint/import/template';
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
		  $('#mSerialNoPrintTable1').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#mSerialNoPrintTable1').bootstrapTable('refresh');
		});


    /**
     * 产品弹框选择按钮事件绑定
     */
    $("#prodButton").click(function(){
        top.layer.open({
            type: 2,
            area: ['800px', '500px'],
            title:"选择产品名称",
            auto:true,
            name:'friend',
            content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/product/product/data")+"&fieldLabels="+encodeURIComponent("产品名称|产品编码")+"&fieldKeys="+encodeURIComponent("itemNameRu|item.code")+"&searchLabels="+encodeURIComponent("产品名称|产品编码")+"&searchKeys="+encodeURIComponent("itemNameRu|item.code")+"&isMultiSelected=false",
            btn: ['确定', '关闭'],
            yes: function(index, layero){
                var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                var items = iframeWin.getSelections();
                if(items == ""){
                    jp.warning("必须选择一条数据!");
                    return;
                }
                var item=items[0];
                $("#prodname").val(item.item.name);
                top.layer.close(index);//关闭对话框。
            },
            cancel: function(index){
            }
        });
    })
    $("#prodDelButton").click(function(){
        // 清除
        $("#prodname").val("");
        $("#prodname").focus();

    });

		
	});
		
  function getIdSelections() {
        return $.map($("#mSerialNoPrintTable1").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
/*
  function deleteAll(){

		jp.confirm('确认要删除该机器序列号打印记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/mserialnoprint/mSerialNoPrint/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#mSerialNoPrintTable1').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }*/
  function edit(){
      var serialnos = $.map($("#mSerialNoPrintTable1").bootstrapTable('getSelections'), function (row) {
          return row.mserialno
      });
      var codenames = $.map($("#mSerialNoPrintTable1").bootstrapTable('getSelections'), function (row) {
          return row.prodname
      });
      var specs = $.map($("#mSerialNoPrintTable1").bootstrapTable('getSelections'), function (row) {
          return row.prodspec
      });
      $.ajax({
          type: "POST",
          url: '${ctx}/tools/TwoDimensionCodeController/createBatchTwoDimensionCode',
          data: {encoderContent:serialnos.join(","),tm:new Date().getTime()},
          dataType:'json',
          cache: false,
          success: function(data){
              if(data.success){
                  $("#printContent").html("");
                  var names = data.body.names.split(",");
                  var item = '<table style="border-collapse:separate; border-spacing:10px;">';
                  for(var i = 0;i<names.length;i++){
                      var file = data.body.fartherFilePath+names[i]+".png";
                      var itemname = codenames[i];
                      var itemspec = specs[i];
                      item += '<tr><td><label style="font-size: 10px;">'+itemname+'</label><br/> ' +
                          '<label style="font-size: 8px;">型号：'+itemspec+'<br/>' +
                          '<label style="font-size: 8px;">S/N：'+serialnos[i]+'<br/>' +
                          '<label style="font-size: 8px;">技术支持：400-838-6658<br/></td>' +
                          '<td><img cache="false"  width="65px" height="65px"  class="block" style="vertical-align: middle" src="'+file+'"/></td></tr>'
                  }
                  document.getElementById("printContent").style.visibility="visible";
                  $("#printContent").html(item+"</table>");
                  $("#printContent").jqprint();
                  $("#printContent").html("");
                  document.getElementById("printContent").style.visibility="hidden";
              }else{
                  jp.alert('后台读取出错！');
                  return;
              }
          }
      })

	  //window.location = "${ctx}/mserialnoprint/mSerialNoPrint/form?id=" + getIdSelections();
  }

function edit1(){
    var serialnos = $.map($("#mSerialNoPrintTable1").bootstrapTable('getSelections'), function (row) {
        if(row.objsn!="N/A")
    		return row.objsn;
    });
    var codenames = $.map($("#mSerialNoPrintTable1").bootstrapTable('getSelections'), function (row) {
        return row.prodname
    });
    $.ajax({
        type: "POST",
        url: '${ctx}/tools/TwoDimensionCodeController/createBatchTwoDimensionCode',
        data: {encoderContent:serialnos.join(","),tm:new Date().getTime()},
        dataType:'json',
        cache: false,
        success: function(data){
            if(data.success){
                $("#printContent").html("");
                var names = data.body.names.split(",");
                var item = '<table style="border-collapse:separate; border-spacing:10px;">';
                for(var i = 0;i<names.length;i++){
                    var file = data.body.fartherFilePath+names[i]+".png";
                    var itemname = codenames[i];
                    item += '<tr><td><label style="font-size: 10px;">'+itemname+'</label><br/> ' +
                        '<label style="font-size: 8px;">型号：<br/>' +
                        '<label style="font-size: 8px;">S/N：'+serialnos[i]+'<br/>' +
                        '<label style="font-size: 8px;">技术支持：400-838-6658<br/></td>' +
                        '<td><img cache="false"  width="65px" height="65px"  class="block" style="vertical-align: middle" src="'+file+'"/></td></tr>'

                    //item += '<tr><td><label style="font-size: 1px;">'+itemname.substring(0,5)+'</label><br/><img cache="false"  width="65px" height="65px"  class="block" style="vertical-align: middle" src="'+file+'"/></td></tr>'
                }
                document.getElementById("printContent").style.visibility="visible";
                $("#printContent").html(item+"</table>");
                $("#printContent").jqprint();
                $("#printContent").html("");
                document.getElementById("printContent").style.visibility="hidden";
            }else{
                jp.alert('后台读取出错！');
                return;
            }
        }
    })

    //window.location = "${ctx}/mserialnoprint/mSerialNoPrint/form?id=" + getIdSelections();
}
  
</script>
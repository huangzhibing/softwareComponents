<%@ page contentType="text/html;charset=UTF-8" %>
<script>
function doSubmit(qmformFrame, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
  var $childTables=$(".childTable");
  var selectItems=[];
  var count=0;
  var repeat=0;
  $.map($($childTables),function($childTable){
	  var selects=$($childTable).bootstrapTable('getSelections');
	  if(JSON.stringify(selects) !== '[]'){
		  selectItems=selectItems.concat(selects);//把每个子表中选择的row添加到selectItems里
	  }
  });
  var rSnList=qmformFrame.qmReportRSnList;
  var rSnIdList= new Array();
  
  for(var i=0;i<rSnList.length;i++){
	  rSnIdList.push(rSnList[i].purReportRSnId);
  }

	  
  for(var i=0;i<selectItems.length;i++){
	  var rsnrow={};
	  rsnrow.reportId=selectItems[i].reportId;
	  rsnrow.billNum=selectItems[i].billNum;
	  rsnrow.objSn=selectItems[i].objSn;
	  if(selectItems[i].objectDef!==undefined){  //防止purReport子表中  检验对象序列号 字段为空
		  rsnrow.objCode=selectItems[i].objectDef.id;
	  }
	  rsnrow.objName=selectItems[i].objName;
	  if(selectItems[i].matterType!==undefined){  //防止purReport子表中  问题类型名称  字段为空
		  rsnrow.matterName=selectItems[i].matterType.mattername;
	  }
	  rsnrow.matterCode=selectItems[i].matterCode;
	  rsnrow.checkResult=selectItems[i].checkResult;
	  rsnrow.checkDate=selectItems[i].checkDate.split(" ")[0];//将DataTime格式的字符串中截取出日期  如在2018-04-12 00：00：00截取出2018-04-12
	  rsnrow.checkTime=selectItems[i].checkTime;
	  rsnrow.checkPerson=selectItems[i].checkPerson;
	  rsnrow.memo=selectItems[i].memo;
	  rsnrow.purReportRSnId=selectItems[i].id;
	  var dumplicate = rSnIdList.indexOf(rsnrow.purReportRSnId);
      if(dumplicate < '0'){
    	  qmformFrame.qmReportRSnList.push(rsnrow);
    	 qmformFrame.importRow(rsnrow);
    	 count++;
      }else{
    	  repeat++;
      }
	  
	  
  }
  var resultStr="";
  if(count!=0){
	  resultStr="已新增"+count+"条记录！";
  }
  if(repeat!=0){
	  resultStr=resultStr+"有"+repeat+"条记录已存在！";
  }
  jp.success(resultStr);
  jp.close(index);
  
  
  /*if(getIdSelections()){
	 
		  jp.loading();  	
		  jp.post("${ctx}/test/purchase/purcPlan/manualImport",{ids:getIdSelections()+"",pher:$("#pher").val()}, function(data){
         	  		if(data.success){
         	  			table.bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  			jp.close(index);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	}) 
         	
		  
		  
	 // }
  }else{
	  jp.info("请选择要添加的检验对象记录!");
  }*/
  
}

$(document).ready(function() {
	$('#purReportTable').bootstrapTable({
		 
		  //请求方法
               method: 'get',
               //类型json
               dataType: "json",
               //显示刷新按钮
               showRefresh: true,
               //显示切换手机试图按钮
               showToggle: false,
               //显示 内容列下拉框
    	       showColumns: false,
    	       //显示到处按钮
    	       showExport: false,
    	       //显示切换分页按钮
    	       showPaginationSwitch: false,
    	       //显示详情按钮
    	       detailView: true,
    	       	//显示详细内容函数
    	       onExpandRow: function (index, row, $detail) {
                   detailFormatter(index, row, $detail);
               },
    	       //最低显示2行
    	       minimumCountColumns: 2,
               //是否显示行间隔色
               striped: false,
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
               url: "${ctx}/purreport/purQmReport/dataAudited",
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
                   	window.location = "${ctx}/purreportc/purReport/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该检验单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/purreportc/purReport/delete?id="+row.id, function(data){
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
               columns: [
			{
		        field: 'reportId',
		        title: '检验单编号',
		        sortable: true
		       
		    }
			,{
		        field: 'invCheckMain.billnum',
		        title: '采购报检单编号',
		        sortable: true
		       
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
			,{
		        field: 'qualityNorm',
		        title: '执行标准',
		        sortable: true
		       
		    }
			,{
		        field: 'office.name',
		        title: '检验部门名称',
		        sortable: true
		       
		    }
			,{
				field:'checkResult',
				title:'检验结论',
				sortable:true
			}
			,{
		        field: 'checkDate',
		        title: '检验日期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	//alert(value.split(" ")[0]);
                    if(value!=null) {
                        var date = value.split(" ")[0];
                        return date;
                    }
		        }
		       
		    }
			,{
		        field: 'checkTime',
		        title: '检验时间',
		        sortable: true
		       
		    }
			,{
		        field: 'checkPerson',
		        title: '检验人名称',
		        sortable: true
		       
		    }
			,{
		        field: 'jdeptName',
		        title: '审核部门名称',
		        sortable: true
		       
		    }
			,{
		        field: 'justifyDate',
		        title: '审核日期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	//alert(value.split(" ")[0]);
                    if(value!=null) {
                        var date = value.split(" ")[0];
                        return date;
                    }
		        }
		       
		    }
			,{
		        field: 'justifyPerson',
		        title: '审核人',
		        sortable: true
		       
		    }
			,{
		        field: 'justifyResult',
		        title: '审核结论',
		        sortable: true
		       
		    }
			,{
		        field: 'memo',
		        title: '备注',
		        sortable: true
		       
		    }
			,{
		        field: 'state',
		        title: '单据状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('state'))}, value, "-");
		        }
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#purReportTable').bootstrapTable("toggleView");
		}
	  
	  $('#purReportTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#purReportTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#purReportTable').bootstrapTable('getSelections').length!=1);
        });
	
	
		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#purReportTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#purReportTable').bootstrapTable('refresh');
		});
		
				$('#checkDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#justifyDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});
		
/*  function getIdSelections() {
        return $.map($("#purReportTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  */

  
  
  
  
		   
  function detailFormatter(index, row,$detail) {
	  $.get("${ctx}/purreport/purQmReport/detailForQm?id="+row.id, function(purReport){
		  
		  var data1 = purReport.purReportRSnList;
		  if(typeof(data1)=="undefined"){ 

			  $detail.html("<div class='text-center'>该检验单没有记录不合格的检验对象数据！</div>"); 

		  }
		  else{
			for(var i=0;i<data1.length;i++){
				data1[i].reportId=row.reportId;
				if(row.invCheckMain!=null) {
                    data1[i].billNum = row.invCheckMain.billnum;
                }
				if(data1[i].isQmatter=="2") {
                    data1.splice(i, 1);//该不合格对象已被别的质量报告提交后，就不再显示。
                    i--;
                }
			}
			var childtable=$detail.html("<table class='childTable'></table>").find("table");
			$(childtable).bootstrapTable({
	            data:data1,
	            dataType: "json",
	            clickToSelect: true,
	            pageSize: 10,
	            pageList: [10, 25, 50, 100],
	            columns: [{
	                checkbox: true,
			        formatter: function (value,row,index) {            // 每次加载 checkbox 时判断当前 row 的 id 是否已经存在全局 Set() 里
		                if(row.isQmatter=="1")   
		                	return {disabled:true};             // 已处理就把checkbox去掉
		            }
	            }, {
	                field: 'objSn',
	                title: '检验对象序列号'
	            }, {
	                field: 'objectDef.id',
	                title: '检验对象编码'
	            }, {
	                field: 'objName',
	                title: '检验对象名称'
	            }, {
	                field: 'checkResult',
	                title: '检验结论'
	            },{
	                field: 'matterCode',
	                title: '问题类型编码',
	                visible:false
	            },{
	                field: 'matterType.mattername',
	                title: '问题类型名称'
	            },{
	                field: 'checkDate',
	                title: '检验日期',
	                formatter:function(value, row , index){
			        	//alert(value.split(" ")[0]);
			        	var date=value.split(" ")[0];
			        	return date;
			        }
	            },{
	                field: 'checkTime',
	                title: '检验时间'
	            },{
	                field: 'checkPerson',
	                title: '检验人名称'
	            },{
	                field: 'memo',
	                title: '备注'
	            },{
	            	field:'qcnorm.qcnormName',
	            	title:'检验标准名称'
	            },{
	                field: 'reportId',
	                title: 'reportId',
	                visible:false
	            },{
	                field: 'billNum',
	                title: 'billNum',
	                visible:false
	            }]
	        });
		  }
		  
	  });
     
    }
  

			
</script>

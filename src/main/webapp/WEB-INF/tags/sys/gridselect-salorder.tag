<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="fieldLabels" type="java.lang.String" required="true" description="表格Th里显示的名字"%>
<%@ attribute name="fieldKeys" type="java.lang.String" required="true" description="表格Td里显示的值"%>
<%@ attribute name="searchLabels" type="java.lang.String" required="true" description="表格Td里显示的值"%>
<%@ attribute name="searchKeys" type="java.lang.String" required="true" description="表格Td里显示的值"%>
<%@ attribute name="targetId" type="java.lang.String" required="true" description="额外赋值目标Id"%>
<%@ attribute name="targetField" type="java.lang.String" required="true" description="额外赋值字段"%>
<%@ attribute name="labelField" type="java.lang.String" required="false" description="额外赋值字段"%>
<%@ attribute name="extraField" type="java.lang.String" required="false" description="额外赋值字段"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="数据地址"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="isMultiSelected" type="java.lang.Boolean" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="urlParamName" type="java.lang.String" required="false" description="参数" %>
<%@ attribute name="urlParamId" type="java.lang.String" required="false" description="参数" %>
<%@ attribute name="disableClick" type="java.lang.Boolean" required="false" %>
	<input id="${id}Id" name="${name}"  type="hidden" value="${value}"/>
	<div class="input-group" style="width: 100%">
		<input id="${id}Names"  name="${labelName}s" ${allowInput?'':'readonly="readonly"'}  type="text" value="${labelValue}" data-msg-required="${dataMsgRequired}"
		class="${cssClass}" style="${cssStyle}"/>
       		 <span class="input-group-btn">
	       		 <button type="button"  id="${id}Button" class="btn <c:if test="${fn:contains(cssClass, 'input-sm')}"> btn-sm </c:if><c:if test="${fn:contains(cssClass, 'input-lg')}"> btn-lg </c:if>  btn-primary ${disabled} ${hideBtn ? 'hide' : ''}"><i class="fa fa-search"></i>
	             </button> 
	               <button type="button" id="${id}DelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
       		 </span>
       		
    </div>
	 <label id="${id}Name-error" class="error" for="${id}Names" style="display:none"></label>
<script type="text/javascript">
$(document).ready(function(){
	$("#${id}Button, #${id}Names").click(function(){
	    var disableClick = ${disableClick ? true : false}
	    if(disableClick){
	        return false;
        }
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}


        var param = $('#${urlParamId}').val();
        if(param){
            var urlParams = "?${urlParamName}="+param
            console.log(urlParams)
        } else {
            var urlParams = ''
        }
		top.layer.open({
		    type: 2,  
		    area: ['800px', '500px'],
		    title:"${title}",
		    auto:true,
		    name:'friend',
		    content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${url}")+urlParams+"&fieldLabels="+encodeURIComponent("${fieldLabels}")+"&fieldKeys="+encodeURIComponent("${fieldKeys}")+"&searchLabels="+encodeURIComponent("${searchLabels}")+"&searchKeys="+encodeURIComponent("${searchKeys}")+"&isMultiSelected=${isMultiSelected? true:false}",
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		    	 var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
		    	 var items = iframeWin.getSelections();
		    	 if(items == ""){
			    	jp.warning("必须选择一条数据!");
			    	return;
		    	 }
		    	 console.log(items)
                var extra = '${extraField}';
                var fieldArr;
                if (extra) {
                    fieldArr = extra.split(';');
                }
		    	 var ids = [];
		    	 var names = [];
		    	 var targetValue = [];
		    	 var fieldValue = [];
		    	 for(var i=0; i<items.length; i++){
		    		 var item = items[i];
		    		 console.log(item)
		    		 ids.push(item.id);
		    		 
		    		 names.push(item${fn:substring(labelName, fns:lastIndexOf(labelName, '.'), fn:length(labelName))})
                     targetValue.push(item['${targetField}'])
					  if (fieldArr) {
                         for(var j = 0;j<fieldArr.length;j++) {
                             $('#' + fieldArr[j].substring(0, fieldArr[j].indexOf(':'))).val(item[fieldArr[j].substring(fieldArr[j].indexOf(':')+1)])
                             var tempArr=fieldArr[j].substring(fieldArr[j].indexOf(':')+1).split(".");
                             var t,tempValue=item;
                             for(var k=0;k<tempArr.length;k++){
                            	 console.log(tempArr[k]+"-");
                            	 tempValue=tempValue[tempArr[k]];
                        
                             }
                             console.log(tempValue);
                             $('#' + fieldArr[j].substring(0, fieldArr[j].indexOf(':'))).val(tempValue);
                         }
                     }
		    	 }
		    	 var urlex="${ctx}/salorder/salOrder/form";
		    	 var parameter={id:items[0].id};
		    	 $('#contractChild-2-List').html("");
		    	 $.get("${ctx}/salcontract/contract/detail?id="+items[0].id, function(contract){
		    	   	
		    	    	var contractChild2RowIdx = 0, contractChild2Tpl = $("#contractChild2Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		    			var data2 =  contract.ctrItemList;
		    			console.log(data2);
		    			for (var i=0; i<data2.length; i++){
		    				addRow('#contractChild-'+'2-List', contractChild2RowIdx, contractChild2Tpl, data2[i]);
		    				contractChild2RowIdx = contractChild2RowIdx + 1;
		    			}
		    					
		    	      	  			
		    	      })
		    	 console.log(ids)
				 console.log(names)
				 console.log(fieldValue);
		    	 $("#${id}Id").val(ids.join(","));
		    	 $("#${id}Id").trigger("change");
				 $("#${targetId}").val(targetValue.join(","));
		    	 $("#${id}Names").val(names.join(","));
		    	 if('${labelField}'){
                     $("#${id}Names").val(fieldValue.join(","));
                 }
				 top.layer.close(index);//关闭对话框。
			  },
			  cancel: function(index){ 
		       }
			  
		}); 
	})
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}
	$("#${id}DelButton").click(function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		// 清除	
		$("#${id}Id").val("");
		$("#${id}Names").val("");
		$("#${id}Names").focus();

	});
})
</script>
<script type="text/template" id="contractChild2Tpl">//<!--
				<tr id="salOrderItemList{{idx}}">
					<td class="hide">
						<input id="salOrderItemList{{idx}}_id" name="salOrderItemList[{{idx}}].id" type="hidden" value=""/>
						<input id="salOrderItemList{{idx}}_delFlag" name="salOrderItemList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="salOrderItemList{{idx}}_seqId" name="salOrderItemList[{{idx}}].seqId" type="text" value="{{row.itemCode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="salOrderItemList{{idx}}_prodCode" name="salOrderItemList[{{idx}}].prodCode.id" type="text" value="{{row.productCode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="salOrderItemList{{idx}}_prodName" name="salOrderItemList[{{idx}}].prodName" type="text" value="{{row.prodName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="salOrderItemList{{idx}}_prodSpec" name="salOrderItemList[{{idx}}].prodSpec" type="text" value="{{row.prodSpec}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="salOrderItemList{{idx}}_unitName" name="salOrderItemList[{{idx}}].unitName" type="text" value="{{row.unitName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="salOrderItemList{{idx}}_prodQty" name="salOrderItemList[{{idx}}].prodQty" type="text" value="{{row.prodQty}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="salOrderItemList{{idx}}_prodPrice" name="salOrderItemList[{{idx}}].prodPrice" type="text" value="{{row.prodPrice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="salOrderItemList{{idx}}_prodSum" name="salOrderItemList[{{idx}}].prodSum" type="text" value="{{row.prodSum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="salOrderItemList{{idx}}_prodPriceTaxed" name="salOrderItemList[{{idx}}].prodPriceTaxed" type="text" value="{{row.prodPriceTaxed}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="salOrderItemList{{idx}}_prodSumTaxed" name="salOrderItemList[{{idx}}].prodSumTaxed" type="text" value="{{row.prodSumTaxed}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#salOrderItemList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>
				
	</script>

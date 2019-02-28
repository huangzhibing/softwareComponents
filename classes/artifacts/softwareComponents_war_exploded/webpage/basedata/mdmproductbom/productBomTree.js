<%@ page contentType="text/html;charset=UTF-8" %>
	<script>
		$(document).ready(function() {
			var to = false;
			var hrefAttr = $('#add').attr('href');
			//记录add默认访问路径
			$('#search_q').keyup(function () {
				if(to) { clearTimeout(to); }
				to = setTimeout(function () {
					var v = $('#search_q').val();
					$('#productBomJsTree').jstree(true).search(v);
				}, 250);
			});
			var  tree =$('#productBomJsTree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/mdmproductbom/productBom/treeData",
						"dataType" : "json" 
					}
				},
				"conditionalselect" : function (node, event) {
					return false;
				},
				'plugins': [ 'types', 'wholerow', "search"],
				"types":{
					'default' : { 'icon' : 'fa fa-file-text-o' },
					'1' : {'icon' : 'fa fa-home'},
					'2' : {'icon' : 'fa fa-umbrella' },
					'3' : { 'icon' : 'fa fa-group'},
					'4' : { 'icon' : 'fa fa-file-text-o' }
				}

			}).bind("activate_node.jstree", function (obj, e) {
				var node = $('#productBomJsTree').jstree(true).get_selected(true)[0];
                if(node.text === "产品"){
                    $("#searchForm  input").val("");
                    $("#searchForm  select").val("");
                    $("#searchForm  .select-item").html("");
                    $('#productBomTable').bootstrapTable('refresh');
                    return false
                }
				//设置查询条件
				var opt = {
					silent: true,
					query:{
						'accType.id':node.id
					}
				};
                // console.log(node)
				//设置页面查询元素
				if(node.data === 'p') {
                    $("#searchForm  input").val("");
                    $("#searchForm  select").val("");
                    $("#searchForm  .select-item").html("");
                    // $("#productNames").val(node.data);
                    $("#productItemName").val(node.text);
                } else if(node.data === 'n'){
                    $("#searchForm  input").val("");
                    $("#searchForm  select").val("");
                    $("#searchForm  .select-item").html("");
                    // $("#productNames").val(node.data);
                    $("#itemName").val(node.text);

				}
				 console.log(node.children_d)
                $("#id").val(node.children_d);
                if(node.children_d.length == 0) {
                   $("#id").val(node.id);
                }
                $('#productBomTable').bootstrapTable('refresh',opt);
				if(node.data === 'p') {
                    $('#add').attr('href', hrefAttr + '?pid=' + node.id);
                } else if(node.data === 'n') {
					var temp = $('#productBomJsTree').jstree(true).get_node(node.parent)
                    while(temp.parent.length >= 32){
                        temp = $('#productBomJsTree').jstree(true).get_node(temp.parent)
                    }
                    $('#add').attr('href', hrefAttr + '?nid=' + node.id + '&pid=' + temp.id);
				}
				//拼装add路径
			}).on('loaded.jstree', function() {
				$("#productBomJsTree").jstree('open_all');
			});
		});
	</script>
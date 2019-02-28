<%@ page contentType="text/html;charset=UTF-8" %>
	<script>
		$(document).ready(function() {
			var to = false;
			$('#search_q').keyup(function () {
				if(to) { clearTimeout(to); }
				to = setTimeout(function () {
					var v = $('#search_q').val();
					$('#productBomFatherClazzjsTree').jstree(true).search(v);
				}, 250);
			});
			$('#productBomFatherClazzjsTree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/productbomtree/productBomFatherClazz/treeData",
						"dataType" : "json" 
					}
				},
				"conditionalselect" : function (node, event) {
					return false;
				},
				'plugins': ["contextmenu", 'types', 'wholerow', "search"],
				"contextmenu": {
					"items": function (node) {
						var tmp = $.jstree.defaults.contextmenu.items();
						delete tmp.create.action;
						delete tmp.rename.action;
						tmp.rename = null;
						tmp.create = {
							"label": "添加下级物料树结构",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								jp.openDialog('添加下级物料树结构', '${ctx}/productbomtree/productBomFatherClazz/form?parent.id=' + obj.id + "&parent.name=" + obj.text, '800px', '500px', $('#productBomFatherClazzjsTree'));
							}
						};
						tmp.remove = {
							"label": "删除物料树结构",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								jp.confirm('确认要删除物料树结构吗？', function(){
									jp.loading();
									$.get("${ctx}/productbomtree/productBomFatherClazz/delete?id="+obj.id, function(data){
										if(data.success){
											$('#productBomFatherClazzjsTree').jstree("refresh");
											jp.success(data.msg);
										}else{
											jp.error(data.msg);
										}
									})

								});
							}
						}
						tmp.ccp = {
							"label": "编辑物料树结构",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								var parentId = inst.get_parent(data.reference);
								var parent = inst.get_node(parentId);
								jp.openDialog('编辑物料树结构', '${ctx}/productbomtree/productBomFatherClazz/form?id=' + obj.id, '800px', '500px', $('#productBomFatherClazzjsTree'));
							}
						}
						return tmp;
					}

				},
				"types":{
					'default' : { 'icon' : 'fa fa-file-text-o' },
					'1' : {'icon' : 'fa fa-home'},
					'2' : {'icon' : 'fa fa-umbrella' },
					'3' : { 'icon' : 'fa fa-group'},
					'4' : { 'icon' : 'fa fa-file-text-o' }
				}

			}).bind("activate_node.jstree", function (obj, e) {
				var node = $('#productBomFatherClazzjsTree').jstree(true).get_selected(true)[0];
				var opt = {
					silent: true,
					query:{
						'productType.id':node.id
					}
				};
				$("#productTypeId").val(node.id);
				$("#productTypeName").val(node.text);
				$('#productBomTable').bootstrapTable('refresh',opt);
			}).on('loaded.jstree', function() {
				$("#productBomFatherClazzjsTree").jstree('open_all');
			});
		});
	</script>
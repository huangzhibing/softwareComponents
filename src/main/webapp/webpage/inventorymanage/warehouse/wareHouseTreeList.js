<%@ page contentType="text/html;charset=UTF-8" %>
	<script>
        function refresh(){
            $('#wareHouseJsTree').jstree(true).destroy(false);
            var to = false;
            var hrefAttr = $('#add').attr('href');
            //记录add默认访问路径
            $('#search_q').keyup(function () {
                if(to) { clearTimeout(to); }
                to = setTimeout(function () {
                    var v = $('#search_q').val();
                    $('#wareHouseJsTree').jstree(true).search(v);
                }, 250);
            });
            $('#wareHouseJsTree').jstree({
                'core' : {
                    "multiple" : false,
                    "animation" : 0,
                    "themes" : { "variant" : "large", "icons":true , "stripes":true},
                    'data' : {
                        "url" : "${ctx}/warehouse/wareHouse/treeData",
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
                var node = $('#wareHouseJsTree').jstree(true).get_selected(true)[0];
                var opt = {
                    silent: true,
                    query:{
                        // 'accType.id':node.id
                    }
                };
                //设置查询条件
                //设置页面查询元素
                // if(node.text != '总结点'){
                //     console.log(node.id)
                //     $("#wareId").val(node.id);
                //     $("#wareName").val(node.text);
                // } else {
                //     $("#wareId").val('');
                //     $("#wareName").val('');
                //
                // }
                // $('#wareHouseTable').bootstrapTable('refresh',opt);
                console.log(node.id.length)
                var add = $('#add')
                var addBin = $('#add-bin')
                var addLoc = $('#add-location')
                var wareHouseTab = $('#wareHouseTab')
                var binTab = $('#binTab')
                var locationTab = $('#locationTab')
                if(node.id.length === 4 && node.text !== '总结点'){
                    addBin.attr('url','${ctx}/bin/bin/form?&wareId='+node.id + '&adminMode='+node.data);
                    // add.attr("disabled",false);
                    $("#wareId").val(node.id);
                    wareHouseTab.hide()
                    binTab.show()
                    locationTab.hide()
                    $('#binTable').bootstrapTable('refresh',opt);


                } else if (node.id.length === 6){
                    addLoc.attr('url','${ctx}/location/location/form?&binId='+node.id+ '&adminMode='+node.data);
                    addLoc.attr("disabled",false);
                    $("#binId").val(node.id);

                    wareHouseTab.hide()
                    binTab.hide()
                    locationTab.show()
                    $('#locationTable').bootstrapTable('refresh',opt);


                } else if (node.id.length === 8){
                    // add.attr('url','${ctx}/location/location/form?&binId='+node.id);
                    addLoc.attr("disabled",true);

                    wareHouseTab.hide()
                    binTab.hide()
                    locationTab.show()
                } else {
                    add.attr('url','${ctx}/warehouse/wareHouse/form');
                    $("#wareId").val('');
                    $("#wareName").val('');
                    $('#wareHouseTable').bootstrapTable('refresh',opt);
                    wareHouseTab.show()
                    binTab.hide()
                    locationTab.hide()

                }
                //拼装add路径
            }).on('loaded.jstree', function() {
                $("#wareHouseJsTree").jstree('close_all');
            });
        }
		$(document).ready(function() {
			var to = false;
			$('#search_q').keyup(function () {
				if(to) { clearTimeout(to); }
				to = setTimeout(function () {
					var v = $('#search_q').val();
					$('#wareHouseJsTree').jstree(true).search(v);
				}, 250);
			});
			$('#wareHouseJsTree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/warehouse/wareHouse/treeData",
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
				var node = $('#wareHouseJsTree').jstree(true).get_selected(true)[0];
				//设置查询条件
				var opt = {
					silent: true,
					query:{
						// 'accType.id':node.id
					}
				};
                //设置页面查询元素
                // if(node.text != '总结点'){
                //     console.log(node.id)
                //     $("#wareId").val(node.id);
                //     $("#wareName").val(node.text);
                // } else {
                //     $("#wareId").val('');
                //     $("#wareName").val('');
                //
                // }
                // $('#wareHouseTable').bootstrapTable('refresh',opt);
                console.log(node.id.length)
                var add = $('#add')
                var addBin = $('#add-bin')
                var addLoc = $('#add-location')
                var edit = $('#edit')
                var editBin = $('#edit-bin')
                var editLoc = $('#edit-location')
                var wareHouseTab = $('#wareHouseTab')
                var binTab = $('#binTab')
                var locationTab = $('#locationTab')
                if(node.id.length === 4 && node.text !== '总结点'){
                    addBin.attr('url','${ctx}/bin/bin/form?&wareId='+node.id + '&adminMode='+node.data);
                    // add.attr("disabled",false);
                    $("#wareId").val(node.id);
                    wareHouseTab.hide()
                    binTab.show()
                    locationTab.hide()
                    $('#binTable').bootstrapTable('refresh',opt);


                } else if (node.id.length === 6){
                    addLoc.attr('url','${ctx}/location/location/form?&binId='+node.id+ '&adminMode='+node.data);
                    addLoc.attr("disabled",false);
                    $("#binId").val(node.id);

                    wareHouseTab.hide()
                    binTab.hide()
                    locationTab.show()
                    $('#locationTable').bootstrapTable('refresh',opt);


                } else if (node.id.length === 8){
                    // add.attr('url','${ctx}/location/location/form?&binId='+node.id);
                    addLoc.attr("disabled",true);

                    wareHouseTab.hide()
                    binTab.hide()
                    locationTab.show()
                } else {
                    add.attr('url','${ctx}/warehouse/wareHouse/form');
                    $("#wareId").val('');
                    $("#wareName").val('');
                    $('#wareHouseTable').bootstrapTable('refresh',opt);
                    wareHouseTab.show()
                    binTab.hide()
                    locationTab.hide()

                }
			}).on('loaded.jstree', function() {
				$("#wareHouseJsTree").jstree('close_all');
			});
		});
	</script>
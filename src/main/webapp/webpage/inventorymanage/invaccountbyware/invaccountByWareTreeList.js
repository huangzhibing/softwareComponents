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
                    $('#wareHouseJsTree').jstree(true).search(v);
                }, 250);
            });
            $('#wareHouseJsTree').jstree({
                'core' : {
                    "multiple" : false,
                    "animation" : 0,
                    "themes" : { "variant" : "large", "icons":true , "stripes":true},
                    'data' : {
                        "url" : "${ctx}/invaccountbyware/invaccountByWare/treeData",
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
                    }
                };
                console.log(node.original.code);
                console.log(node.data);
                console.log(node.text);
                console.log(node.children.length);
                if(node.original.code.length == 4 &&node.text != '总结点'){

                    $("#wareNames").val(node.original.code);
                    console.log(node.original.code);
                    $('#invaccountByWareTable').bootstrapTable('refresh',opt);

                } else if (node.original.code.length == 6){

                    $("#binNames").val(node.original.code);
                    console.log(node.original.code);
                    $('#invaccountByWareTable').bootstrapTable('refresh',opt);

                } else if (node.original.code.length == 8){

                    $("#locNames").val(node.original.code);
                    console.log(node.original.code);
                    $('#invaccountByWareTable').bootstrapTable('refresh',opt);

                } else {
                    $("#wareNames").val("");
                    $("#binNames").val("");
                    $("#locNames").val("");
                    console.log(node.original.code);
                    $('#invaccountByWareTable').bootstrapTable('refresh',opt);
                }
                }).on('loaded.jstree',function() {
                    $("#wareHouseJsTree").jstree('close_all');
            });
})

	</script>
/**
 * 通用方法封装处理
 * Copyright (c) 2019
 */
$(function() {
	// select2复选框事件绑定
	if ($.fn.select2 !== undefined) {
		$("select.form-control:not(.noselect2)").each(function () {
			$(this).select2();
		})
	}
	// checkbox 事件绑定
	if ($(".check-box").length > 0) {
	    $(".check-box").iCheck({
	    	checkboxClass: 'icheckbox-blue',
			radioClass: 'iradio-blue',
	    })
	}
	// radio 事件绑定
	if ($(".radio-box").length > 0) {
	    $(".radio-box").iCheck({
	    	checkboxClass: 'icheckbox-blue',
			radioClass: 'iradio-blue',
	    })
	}
	// laydate 时间控件绑定
	if ($(".select-time").length > 0) {
		layui.use('laydate', function() {
		    var laydate = layui.laydate;
		    var startDate = laydate.render({
		        elem: '#startTime',
		        max: $('#endTime').val(),
		        theme: 'molv',
		        trigger: 'click',
		        done: function(value, date) {
		            // 结束时间大于开始时间
		            if (value !== '') {
		                endDate.config.min.year = date.year;
		                endDate.config.min.month = date.month - 1;
		                endDate.config.min.date = date.date;
		            } else {
		                endDate.config.min.year = '';
		                endDate.config.min.month = '';
		                endDate.config.min.date = '';
		            }
		        }
		    });
		    var endDate = laydate.render({
		        elem: '#endTime',
		        min: $('#startTime').val(),
		        theme: 'molv',
		        trigger: 'click',
		        done: function(value, date) {
		            // 开始时间小于结束时间
		            if (value !== '') {
		                startDate.config.max.year = date.year;
		                startDate.config.max.month = date.month - 1;
		                startDate.config.max.date = date.date;
		            } else {
		                startDate.config.max.year = '';
		                startDate.config.max.month = '';
		                startDate.config.max.date = '';
		            }
		        }
		    });
		});
	}
    // laydate time-input 时间控件绑定
    if ($(".time-input").length > 0) {
        layui.use('laydate', function() {
            var laydate = layui.laydate;
            $(".time-input").each(function () {
                var time = $(this)[0];
                laydate.render({
                    elem: time,
                    theme: 'molv',
                    trigger: 'click',
                    done: function(value, date) {
                        $(time).trigger("change");
                    }
                });
            });
        });
    }
    if ($(".clock-input").length > 0) {
        layui.use('laydate', function() {
            var laydate = layui.laydate;
            $(".clock-input").each(function () {
                var time = $(this)[0];
                laydate.render({
                    elem: time,
                    theme: 'molv',
                    trigger: 'click',
                    format: 'HH:mm:ss',
                    type:"time",
                    done: function(value, date) {
                        $(time).trigger("change");
                    }
                });
            });
        });
    }
    if ($(".month-input").length > 0) {
        layui.use('laydate', function() {
            var laydate = layui.laydate;
            $(".month-input").each(function () {
                var time = $(this)[0];
                laydate.render({
                    elem: time,
                    theme: 'molv',
                    trigger: 'click',
                    format: 'yyyy-MM',
                    type:"month",
                    showBottom:"false",
                    done: function(value, date) {
                        $(time).val(value).trigger("change");
                    }
                });
            });
        });
    }
    if ($(".year-input").length > 0) {
        layui.use('laydate', function() {
            var laydate = layui.laydate;
            $(".year-input").each(function () {
                var time = $(this)[0];
                laydate.render({
                    elem: time,
                    theme: 'molv',
                    trigger: 'click',
                    format: 'yyyy',
                    type:"year",
                    showBottom:"false",
                    done: function(value, date) {
                        $(time).val(value).trigger("change");
                    }
                });
            });
        });
    }
	// tree 关键字搜索绑定
	if ($("#keyword").length > 0) {
		$("#keyword").bind("focus", function focusKey(e) {
		    if ($("#keyword").hasClass("empty")) {
		        $("#keyword").removeClass("empty");
		    }
		}).bind("blur", function blurKey(e) {
		    if ($("#keyword").val() === "") {
		        $("#keyword").addClass("empty");
		    }
		    $.tree.searchNode(e);
		}).bind("input propertychange", $.tree.searchNode);
	}
	// 复选框后按钮样式状态变更
	$("#bootstrap-table").on("check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table", function () {
		var ids = $("#bootstrap-table").bootstrapTable("getSelections");
		$('#toolbar .btn-del').toggleClass('disabled', !ids.length);
		$('#toolbar .btn-edit').toggleClass('disabled', ids.length!=1);
    });
	// tree表格树 展开/折叠
	var expandFlag = false;
	$("#expandAllBtn").click(function() {
	    if (expandFlag) {
	        $('#bootstrap-tree-table').bootstrapTreeTable('expandAll');
	    } else {
	        $('#bootstrap-tree-table').bootstrapTreeTable('collapseAll');
	    }
	    expandFlag = expandFlag ? false: true;
	})
    if($('.summernote').length==1){
        var summerTextId = $('.summernote').attr("sn-data-id");
        if($.common.isNotEmpty(summerTextId)){
            $('.summernote').summernote({
                height : '220px',
                lang : 'zh-CN',
                callbacks: {
                    onImageUpload: function (files) {
                        summernoteUploadImg(files[0], this);
                    },
                    onBlur: function() {
                        //焦点离开对input进行赋值
                        $("#"+summerTextId).val($('.summernote').summernote('code'));
                    }
                }
            });
            //初始化根据input对summernote赋值
            var summerText = $("#"+summerTextId).val();
            if($.common.isNotEmpty(summerText)){
                $('.summernote').summernote('code',summerText);
            }
        }
        var summerShowId = $('.summernote').attr("sn-show-id");
        if($.common.isNotEmpty(summerShowId)){
            $('.summernote').html($("#"+summerShowId).val());
        }
    }
});

/** 刷新选项卡 */
var refreshItem = function(){
    var topWindow = $(window.parent.document);
	var currentId = $('.page-tabs-content', topWindow).find('.active').attr('data-id');
	var target = $('.RuoYi_iframe[data-id="' + currentId + '"]', topWindow);
    var url = target.attr('src');
    target.attr('src', url).ready();
}

/** 创建选项卡 */
function createMenuItem(dataUrl, menuName) {
    dataIndex = $.common.random(1,100),
    flag = true;
    if (dataUrl == undefined || $.trim(dataUrl).length == 0) return false;
    var topWindow = $(window.parent.document);
    // 选项卡菜单已存在
    $('.menuTab', topWindow).each(function() {
        if ($(this).data('id') == dataUrl) {
            if (!$(this).hasClass('active')) {
                $(this).addClass('active').siblings('.menuTab').removeClass('active');
                $('.page-tabs-content').animate({ marginLeft: ""}, "fast");
                // 显示tab对应的内容区
                $('.mainContent .RuoYi_iframe', topWindow).each(function() {
                    if ($(this).data('id') == dataUrl) {
                        $(this).show().siblings('.RuoYi_iframe').hide();
                        return false;
                    }
                });
            }
            flag = false;
            return false;
        }
    });
    // 选项卡菜单不存在
    if (flag) {
        var str = '<a href="javascript:;" class="active menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
        $('.menuTab', topWindow).removeClass('active');

        // 添加选项卡对应的iframe
        var str1 = '<iframe class="RuoYi_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
        $('.mainContent', topWindow).find('iframe.RuoYi_iframe').hide().parents('.mainContent').append(str1);

        // 添加选项卡
        $('.menuTabs .page-tabs-content', topWindow).append(str);
    }
    return false;
}

//日志打印封装处理
var log = {
    log: function (msg) {
    	console.log(msg);
    },
    info: function(msg) {
    	console.info(msg);
    },
    warn: function(msg) {
    	console.warn(msg);
    },
    error: function(msg) {
    	console.error(msg);
    }
};

/** 设置全局ajax处理 */
$.ajaxSetup({
    complete: function(XMLHttpRequest, textStatus) {
        if (textStatus == 'timeout') {
            $.modal.alertWarning("服务器超时，请稍后再试！");
            $.modal.closeLoading();
        } else if (textStatus == "parsererror") {
            $.modal.alertWarning("服务器错误，请联系管理员！");
            $.modal.closeLoading();
        }
    }
});
layer.config({
    extend: 'moon/style.css',
    skin: 'layer-ext-moon'
});

// 上传文件
function summernoteUploadImg(file, obj) {
    var data = new FormData();
    data.append("file", file);
    $.ajax({
        type: "POST",
        url: ctx + "common/upload",
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        dataType: 'json',
        success: function(result) {
            if (result.code == web_status.SUCCESS) {
                $(obj).summernote('editor.insertImage', result.url, result.fileName);
            } else {
                $.modal.alertError(result.msg);
            }
        },
        error: function(error) {
            $.modal.alertWarning("图片上传失败。");
        }
    });
}

//date对象增加增加format方法（yyyy-MM-dd hh:mm:ss）
Date.prototype.format = function(format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}

function queryByStatus(status,statusId,formId) {
    if($.common.isEmpty(statusId)){
        statusId = "status";
    }
    $("#"+statusId).val(status);
    $.table.search(formId);
}

function showTimeStr(d1,d2,status){
    var millseconds = 0;
    if(status == 0){
        millseconds = new Date().getTime()-new Date(d1).getTime();
    }else{
        millseconds = new Date(d2).getTime()-new Date(d1).getTime();
    }
    return convertMillSeconds(millseconds);
}
function convertMillSeconds(millseconds) {
    if(millseconds<60*1000){
        return (millseconds/1000).toFixed(1)+"秒";
    }
    if(millseconds<60*60*1000){
        return (millseconds/60000).toFixed(1)+"分";
    }
    if(millseconds<24*60*60*1000){
        return (millseconds/3600000).toFixed(1)+"时";
    }
    return (millseconds/(24*3600000)).toFixed(1)+"天";
}

$(function() {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function() {
		login();
    }
});

function login() {
	$.modal.loading($("#btnSubmit").data("loading"));
	var loginName = $.common.trim($("#loginName").val());
	var question = $.common.trim($("#question").val());
	var answer = $.common.trim($("#answer").val());
	var password = $.common.trim($("#password").val());
	var emailCode = $.common.trim($("#emailCode").val());
    $.ajax({
        type: "put",
        url: ctx + "forgetPwd",
        data: {
            "loginName": loginName,
            "question": question,
            "answer": answer,
            "password": password
        },
        success: function(r) {
            if (r.code == 0) {
                $.modal.msg("密码重置成功！即将跳转登录页面！")
                setTimeout(function () {
                    location.href = ctx + 'login';
                },2500);
            } else {
            	$.modal.closeLoading();
                $("#emailCode").val("");
            	$.modal.msg(r.msg);
            }
        }
    });
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            loginName: {
                required: true,
                isUserName:true,
                remote: {
                    url: ctx + "getSecretProtect",
                    type: "post",

                    data: {
                        name: function () {
                            return $.common.trim($("#loginName").val());
                        }
                    },
                    dataFilter: function (data, type) {
                        data = eval(data);
                        var arr = [];
                        arr.push('<option value="">选择密保问题</option>');
                        for(var i=0;i<data.length;i++){
                            arr.push('<option value="'+data[i].id+'">'+data[i].question+'</option>')
                        }
                        $("#question").html(arr.join(""));
                        if(data.length==0){
                            return false;
                        }
                        return true;
                    }
                }
            },
            password: {
                required: true,
                minlength: 5,
                maxlength: 20
            },
            confirm: {
                required: true,
                equalTo: "#password"
            }
        },
        messages: {
            loginName: {
                required: icon + "请输入学工号",
                remote : icon + "学工号不存在"
            },
            password: {
                required: "请输入新密码",
                minlength: "密码不能小于6个字符",
                maxlength: "密码不能大于20个字符"
            },
            confirm: {
                required: "请再次输入新密码",
                equalTo: "两次密码输入不一致"
            }
        }
    })
}
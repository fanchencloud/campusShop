const baseProductURL = "/product";
const baseProductImageURL = "/productImage";

// server 上存在的缩略图图片
let serverThumbnail = [];
// 本地存储的图片数据
// 保存商品缩略图的数据
let localProductThumbnail = [];
// 本地保存的商品缩略图的地址
let localProductThumbnailPath = [];

let userId;

// 在页面加载完成执行函数
$(function () {
    console.log("页面加载完成自动执行！");
    // 获取用户信息并填充数据
    $.get("/user/getUserInfo", function (response) {
        console.log("成功接收到来自服务器数据");
        if (response.status === 200) {
            const user = response.data;
            // 用户昵称
            $("#nickname").val(user.name);
            // 性别 将性别下拉列表选中为后台数据
            $("#sex").val(user.gender);
            // 电话
            $("#phone").val(user.phone);
            // 邮箱
            $("#mailbox").val(user.email);
            userId = user.id;

            // 填充用户头像
            let test = user.headPortrait;
            if (!validateParameterRequired(test)) {
                const thumbnail = '/user/getUserImage';
                const productThumbnailHtml = '<li class="weui-uploader__file" style="background-image:url('
                    + thumbnail + ')" onclick="showProductThumbnailPictureImage()"></li>';
                serverThumbnail.push(getUrlParam("productId"));
                $("#uploadProductThumbnail").append(productThumbnailHtml);
            }
            // 修改显示
            $("#productThumbnailSize").html(serverThumbnail.length + "/1");
        } else {
            $.alert("后台数据错误！请联系管理员！");
        }
    });
})
;

/**
 * 展示商品详情图片
 * @param uuid 图片的uuid
 * @param dataSource 图片的来源
 */
const showProductDetailPictureImage = function (uuid, dataSource) {
    let picturePath, deletePath;
    // 判断数据来源是本地还是数据服务器上的 1、表示的是数据服务器上的 2、表示的是本地的数据
    if (dataSource === 1) {
        // 拼接图片地址
        picturePath = baseProductImageURL + '/getProductImage?id=' + uuid;
        deletePath = baseProductImageURL + "/delete?id=" + uuid;
    } else if (dataSource === 2) {
        picturePath = localProductDetailsPicturePath.get(uuid);
    }
    // 展示图片
    showImage(picturePath);
    let flag = true;
    // 为删除按钮添加事件
    $("#galleryHref").bind("click", function () {
        if (flag === true) {
            flag = false;
            // 删除图片数据
            if (dataSource === 1) {
                // 如果图片数据来自数据服务器，请求服务器删除数据
                $.get(deletePath, function (responseData) {
                    if (responseData.status === 200) {
                        // 删除本地数据
                        serverProductImage.remove(uuid);
                        // 显示删除结果
                        $.toptip(responseData.msg, 'success');
                        // 重新刷新布局
                        changeUploadProductDetailsPicture();
                    }
                });
            } else if (dataSource === 2) {
                // 数据来自本地数据、删除数据集合中的数据
                localProductDetailsPicture.delete(uuid);
                // 释放URL 对象
                URL.revokeObjectURL(localProductDetailsPicturePath.get(uuid));
                // 删除本地数据
                localProductDetailsPicturePath.delete(uuid);
                // 重新刷新布局
                changeUploadProductDetailsPicture();
            }
            // 隐藏画廊
            $("#gallery").fadeOut("slow");
            flag = true;
        }
    });
};

/**
 * 展示商品缩略图片
 * 1、如果在服务器上有数据，就展示服务器上的数据
 * 2、如果数据在本地，就展示本地的数据
 */
const showProductThumbnailPictureImage = function () {
    let picturePath, deletePath, dataSource = 0;
    // 判断数据来源是本地还是数据服务器上的 1、表示的是数据服务器上的 2、表示的是本地的数据
    if (serverThumbnail.length > 0) {
        dataSource = 1;
        // 拼接图片地址
        picturePath = "/user/getUserImage";
        deletePath = "/person/deleteHeadImage";
    } else if (localProductThumbnailPath.size !== 0) {
        dataSource = 2;
        picturePath = localProductThumbnailPath[0];
    }
    // 展示图片
    showImage(picturePath);
    let flag = true;
    // 为删除按钮添加事件
    $("#galleryHref").bind("click", function () {
        if (flag === true) {
            flag = false;
            // 删除图片数据
            if (dataSource === 1) {
                // 如果图片数据来自数据服务器，请求服务器删除数据
                $.get(deletePath, function (responseData) {
                    if (responseData.status === 200) {
                        // 删除本地数据
                        serverProductImage = [];
                        // 显示删除结果
                        $.toptip(responseData.msg, 'success');
                    }
                });
            } else if (dataSource === 2) {
                // 数据来自本地数据、删除数据集合中的数据
                localProductThumbnail = [];
                for (let i = 0; i < localProductThumbnailPath.length; i++) {
                    URL.revokeObjectURL(localProductThumbnailPath[i]);
                }
                // 删除本地数据
                localProductThumbnailPath = [];
                // 显示删除结果
                $.toptip("删除成功", 'success');
            }
            // 重新刷新布局
            changeProductThumbnail();
            // 隐藏画廊
            $("#gallery").fadeOut("slow");
            flag = true;
        }
    });
};

/**
 * 以画廊展示图片
 * @param imageUrl 展示图片的路径
 */
const showImage = function (imageUrl) {
    // 设置图片
    const fileInputContainer = document.getElementById("gallerySpan");
    fileInputContainer.style.backgroundImage = "url(" + imageUrl + ")";
    // 添加span的点击事件
    fileInputContainer.onclick = function () {
        $("#gallery").fadeOut("slow");
    };
    $("#gallery").fadeIn("slow");
};

/**
 * 刷新商品详情图片
 */
const changeUploadProductDetailsPicture = function () {
    console.log("触发改变商品详情图片事件!");
    // language=JQuery-CSS
    let $uploadProductDetailsPictureList = $("#uploadProductDetailsPictureList");
    // 先清空原始网页数据
    removeAllChild("uploadProductDetailsPictureList");
    console.log("商品详情图片：服务器数据：" + serverProductImage.length + " 本地数据：" + localProductDetailsPicture.size);
    // 数据数量
    let pictureNumber = 0;
    // 填充服务器数据
    let productImageHtml = '';
    for (let i = 0; i < serverProductImage.length; i++) {
        const picturePath = baseProductImageURL + '/getProductImage?id=' + serverProductImage[i];
        productImageHtml += '<li class="weui-uploader__file" style="background-image:url('
            + picturePath + ')" onclick="showProductDetailPictureImage(' + '\'' + picturePath + '\'' + ', 1)"></li>';
        pictureNumber++;
    }
    $uploadProductDetailsPictureList.append(productImageHtml);
    // 填充本地数据
    productImageHtml = '';
    localProductDetailsPicturePath.forEach(function (value, key, map) {
        productImageHtml += '<li class="weui-uploader__file" style="background-image:url('
            + value + ')" onclick="showProductDetailPictureImage(' + '\'' + key + '\'' + ', 2)"></li>';
        pictureNumber++;
    });
    $uploadProductDetailsPictureList.append(productImageHtml);
    // 修改显示
    $("#ProductDetailsPictureSize").html(pictureNumber + "/5");
};

/**
 * 刷新商品缩略图
 */
const changeProductThumbnail = function () {
    console.log("触发刷新商品缩略图的事件!");
    removeAllChild("uploadProductThumbnail");
    let $uploadProductThumbnail = $("#uploadProductThumbnail");
    let productThumbnailLength = 0;
    console.log("缩略图：服务器数据：" + serverThumbnail.length + " 本地数据：" + localProductThumbnail.length);
    // 图片数据来自服务器
    if (serverThumbnail.length > 0) {
        // 填充商品缩略图
        const thumbnail = baseProductURL + '/getProductThumbnail?productId=' + getUrlParam("productId");
        const productThumbnailHtml = '<li class="weui-uploader__file" style="background-image:url('
            + thumbnail + ')" onclick="showProductThumbnailPictureImage()"></li>';
        $uploadProductThumbnail.append(productThumbnailHtml);
        productThumbnailLength = 1;
    } else if (localProductThumbnailPath.length > 0) {
        // 取本地保存的数据进行展示
        let imageLi = '<li class="weui-uploader__file" style="background-image:url('
            + localProductThumbnailPath[0] + ')" onclick="showProductThumbnailPictureImage()"></li>';
        $uploadProductThumbnail.append(imageLi);
        productThumbnailLength = 1;
    }
    // 修改显示
    $("#productThumbnailSize").html(productThumbnailLength + "/1");
};

/**
 * 响应选择缩略图的函数
 */
const uploadProductThumbnailInput = function () {
    // 清空原数据
    localProductThumbnail = [];
    for (let i = 0; i < localProductThumbnailPath.length; i++) {
        URL.revokeObjectURL(localProductThumbnailPath[i]);
    }
    localProductThumbnailPath = [];
    const list = this.files;
    for (let i = 0; i < list.length; i++) {
        console.log(list[i].name);
        localProductThumbnail.push(list[i]);
        let objectURL = getObjectURL(list[i]);
        localProductThumbnailPath.push(objectURL);
    }
    changeProductThumbnail();
    console.log("file number = " + list.length);
};

/**
 * 响应选择商品详情图片的函数
 */
const uploadProductDetailsPictureInput = function () {
    let size = serverProductImage.length + localProductDetailsPicture.size;
    const list = this.files;
    size = size + list.length;
    if (size > 5) {
        $.toast("详情图片超过限制", "forbidden");
    } else {
        for (let i = 0; i < list.length; i++) {
            const str = list[i].name;
            const pos = str.lastIndexOf(".");
            const lastName = str.substring(pos, str.length);
            let uuid = generateUUID();
            localProductDetailsPicture.set(uuid, list[i]);
            let objectURL = getObjectURL(list[i]);
            localProductDetailsPicturePath.set(uuid, objectURL);
            console.log(str + "   -- 后缀名为：" + lastName);
        }
        changeUploadProductDetailsPicture();
        console.log("file number = " + list.length);
    }
};

/**
 * 上传商品详情图
 */
const uploadProductDetailsPicture = function () {
    // 检查上传的详情图片数量
    let uploadProductThumbnailList = document.getElementById("uploadProductDetailsPictureList");
    // 获取详情图片的数量
    let size = uploadProductThumbnailList.childElementCount;
    console.log(size);
    if (size < 5) {
        // 允许上传
        $("#uploadProductDetailsPictureInput").click();
    } else {
        // 数量过多
        $.toast("详情图片超过限制", "forbidden");
    }
};

/**
 * 上传商品缩略图
 */
const uploadProductThumbnail = function () {
    // 检查上传的图片数量
    let uploadProductThumbnailList = document.getElementById("uploadProductThumbnail");
    // 获取缩略图的数量
    // var size = document.getElementById("uploadProductThumbnail").childElementCount;
    let size = uploadProductThumbnailList.childElementCount;
    console.log(size);
    if (size >= 1) {
        $.toast("超过上传数量", "forbidden");
        return false;
    } else {
        // 清空文件框的数据
        const test = document.getElementById('uploadProductThumbnailInput');
        //虽然test的value不能设为有字符的值，但是可以设置为空值
        test.value = '';
        // 上传照片
        $("#uploadProductThumbnailInput").click();
    }
};
let submitFlag = true;
/**
 * 提交商品修改数据
 */
const submitFunction = function () {
    if (submitFlag === true) {
        console.log("进入提交修改数据函数");
        submitFlag = false;
        // 将上传按钮设置位不可用状态
        const checkButton = $("#registerShopSubmit");
        checkButton.addClass("weui-btn_disabled");

        // 检查上传参数
        // 用户昵称
        const nickname = $("#nickname").val();
        if (validateRequired(nickname, "用户昵称不能为空")) {
            removeDisable(checkButton);
            return false;
        }

        // 性别
        const sex = $("#sex").val();
        if (validateRequired(sex, "性别不能为空")) {
            removeDisable(checkButton);
            return false;
        }

        // 电话
        const phone = $("#phone").val();
        if (validateRequired(phone, "电话不能为空")) {
            removeDisable(checkButton);
            return false;
        }
        // 检查上传的详情图片数量
        let uploadProductThumbnailList = document.getElementById("uploadProductThumbnail");
        // 获取详情图片的数量
        let size = uploadProductThumbnailList.childElementCount;
        // 验证缩略图
        if (size < 1) {
            $.toptip('请至少添加一张商品缩略图', 'error');
            removeDisable(checkButton);
            return false;
        }


        // 验证码
        const verifyCode = $("#verifyCode").val();
        if (validateRequired(verifyCode, "验证码不能为空")) {
            removeDisable(checkButton);
            return false;
        }

        let user = {};
        // 用户昵称
        user.name = nickname;
        // 性别
        user.gender = sex;
        // 电话
        user.phone = phone;
        // 邮箱
        user.email = mailbox;
        // 用户id
        user.id = userId;
        let formData = new FormData();
        // 处理缩略图
        if (localProductThumbnail.length > 0) {
            const file = localProductThumbnail[0];
            const str = file.name;
            const pos = str.lastIndexOf(".");
            const suffixName = str.substring(pos, str.length);
            // 创建新文件对象
            const newThumbnail = new File([file], "thumbnail" + suffixName, {type: "image/*"});
            formData.append("thumbnail", newThumbnail);
        }


        formData.append("user", JSON.stringify(user));
        formData.append("verifyCode", verifyCode);
        $.ajax({
            type: "POST",
            url: "/person/modifyUser",
            dataType: "json",
            cache: false,         //不设置缓存
            processData: false,  // 不处理数据
            contentType: false, // 不设置内容类型
            data: formData,
            success: function (response) {
                console.log(response);
                if (response.status === 200) {
                    console.log("修改成功！");
                    $.toast(response.msg);
                    setTimeout(" window.location.href = \"/home\";", 2000);
                } else {
                    console.log("请求失败");
                }
            },
            error: function (xhr) {
                console.log("错误提示： " + xhr + " ---- " + xhr.status + " " + xhr.statusText);
                checkButtonFunction();
            },
            //请求完成后回调函数 (请求成功或失败之后均调用)。参数： XMLHttpRequest 对象和一个描述成功请求类型的字符串
            complete: function (XMLHttpRequest, textStatus) {
                console.log("函数调用完成，将按钮设置为可用状态");
                // 请求完成，将按钮重置为可用
                checkButtonFunction();
            }
        });
    }
};

const callBackFunction = function () {
    console.log("修改商品完成");
};

const checkButtonFunction = function () {
    // 将上传按钮设置位不可用状态
    const checkButton = $("#registerShopSubmit");
    removeDisable(checkButton);
};
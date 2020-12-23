//根据传递过来的参数name获取对应的值
// 传过来的是key 的名称
function getParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
    var r = location.search.substr(1).match(reg);
    // 返回对应的值
    if (r!=null)
        return (r[2]);
    return null;
}
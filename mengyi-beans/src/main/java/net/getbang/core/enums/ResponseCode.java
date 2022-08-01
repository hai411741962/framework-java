
package net.getbang.core.enums;

public enum ResponseCode {
    @Deprecated
    success(200, "请求成功"),
    SUCCESS(200, "请求成功"),
    fail(400, "缺少必要的请求参数或者参数格式错误"),
    PARAM_ERROR(400, "缺少必要的请求参数或者参数格式错误"),
    PARAM_TYPE_ERROR(400 ,"请求参数类型错误"),
    unauthorized(401, "权限校验未通过"),
    invalideTokenFormat(402, "token格式错误"),
    permissionDeny(403, "权限拒绝"),
    invalideParam(404, "参数校验未通过"),
    connectTimeout(405, "请求超时"),
    notFoundService(406, "服务未找到"),
    authenFailed(407, "用户授权失败"),
    userNotExist(408, "用户不存在"),
    userPasswordError(411, "用户密码不正确"),
    pushSuccess(409, "websocket推送消息成功"),
    pushFailedNoUser(410, "websocket推送消息失败，用户不存在"),
    invalideToken(412, "token已失效"),
    nullToken(413, "token不能为空"),
    httpClientParamError(414, "httpClient 请求参数格式异常"),
    httpClientError(415, "httpClient 请求出错"),
    companyExist(416, "该公司已经注册过"),
    accountExist(417, "该账号已存在"),
    mobileExist(418, "该手机号码已存在"),
    emailExist(418, "该邮箱已存在"),
    userRegisterFailed(419, "注册账号失败"),
    signatureFailed(420, "请求验签不通过"),
    logout(421, "用户退出登录成功"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    excelTemplateAliaError(600, "文件错误,表头不一致"),
    fileNotFound(601, "未检测到文件"),
    fileNotMatchTemplate(602, "文件未匹配到模板"),
    NotSupportFileType(603, "不支持的文件类型"),
    fileIsNotExcel(604, "文件不是Excel文件"),
    invalideFilePath(605, "请求的文件路径无效"),
    fileNotExist(606, "请求的文件不存在"),
    readFileFailure(607, "文件读取失败"),
    NotSupportImageType(608, "不支持的图片类型"),
    serviceTypeEmpty(609, "服务类型serviceType不能为空"),
    exceedSize(610, "文件大小超过限制"),
    fileNotChose(611, "文件未选中，请选择文件上传"),
    fileNameIllegal(612, "文件名中不得包含【空格 # ? & * 】等特殊字符"),
    readHeaderFailure(613, "Excel表头读取失败"),
    oneFileOnly(614, "上传的文件数大于1，仅支持单文件上传"),
    saveFileFailure(615, "接口发生异常，文件保存失败"),
    filePermissionError(616, "权限错误或文件不存在"),
    fileServiceTypeError(617, "文件上传服务类型错误"),
    fileUploadBatchLimitError(618, "文件批量上传单次文件个数超过限制"),
    error(500, "服务端错误"),
    remoteCallError(501, "远程调用异常"),
    serviceFuse(700, "请求熔断,稍后重试"),
    serviceFlow(701, "请求限流,稍后重试"),
    serviceHotspot(702, "请求热点参数限流,稍后重试"),
    serviceSystem(703, "请求触发系统保护规则,稍后重试"),
    serviceRules(704, "请求Sentinel授权规则不通过,稍后重试"),
    unkown(999, "未知类型"),
    DOUBLE_BOOKING(20001, "已购,无需重复下单"),
    DOUBLE_PAID(20002, "已支付,无需重复支付"),
    BILL_DUPLICATE_DEDUCTION(20003, "订单审核出现异常,请确认订单正确性！"),
    ORDER_VIOLATION_OPERATION(20004, "订单审核出现异常,订单违法操作！"),
    NOT_ORDER_OPERATION(20005, "用户无权操作订单!"),
    PRODUCT_NOT_EXIST(20006, "没有此产品!"),
    PROJECT_ID_NOT_EXIST(20007, "租户未配置projectId"),
    ;

    private int code;
    private String desc;

    private ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ResponseCode getByCode(int code) {
        ResponseCode[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ResponseCode responseCode = var1[var3];
            if (responseCode.code() == code) {
                return responseCode;
            }
        }

        return unkown;
    }

    public int code() {
        return this.code;
    }

    public String desc() {
        return this.desc;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

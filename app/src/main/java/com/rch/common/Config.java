package com.rch.common;

/**
 * Created by Administrator on 2018/3/27.
 */

public class Config {


         /**生产**/
//        static final String IP="https://manage.ubuyche.com/rch-front-manage/v1_2_6";//生产服务
//        static final String APPVERSIONIP = "https://manage.ubuyche.com/rch-front-manage";//获取版本单独拎出来不需要后面加版本
//        static final String H5IP = "https://wx.ubuyche.com";//h5生产服务


        /**测试**/
         static final String IP = "http://192.168.2.244:8080/v1_2_6";//测试服务
         static final String APPVERSIONIP = "http://192.168.2.244:8080";//获取版本单独拎出来不需要后面加版本
         static final String H5IP = "http://192.168.2.244";//h5测试地址

       /**预发布**/
//      static final String IP = "https://prelaunch.ubuyche.com/v1_2_6";//预发布服务
//      static final String APPVERSIONIP = "https://prelaunch.ubuyche.com";//获取版本单独拎出来不需要后面加版本
//      static final String H5IP = "https://prelaunchh5.ubuyche.com";//h 5预发布测试地址



        /**开发239 或者本地服务**/
//      static final String IP="http://192.168.2.239:8080/rch-front-manage/v1_2_6";//开发huanjing
//      static final String APPVERSIONIP="http://192.168.2.239:8080/rch-front-manage";//开发huanjing
//      static final String IP="http://192.168.4.226:8089/rch-front-manage";//海龙服务
//      static final String IP="http://192.168.4.245:8080/rch-front-manage";//王杭磊
//      static final String H5IP = "http://192.168.4.248:8081";//h5熊阳




    static final String key = "545b3dadb9e0b0e48aa3d21d";   //签名秘钥

    //登录
    public static final String LOGIN = Config.IP + "/app/userLogin.do";

    //我的信息
    public static final String MYINFO = Config.IP + "/app/user/getUserInfo.do";

    //获取历史预约
    public static final String LISTORDERBYUSER = Config.IP + "/app/user/getListOrderByUser.do";

    //（10）	订单管理-预约订单列表
    public static final String LISTORDERBYENT = Config.IP + "/app/user/getListOrderByEnt.do";

    //车辆定制
    public static final String SAVEVEHICLEQUEST = Config.IP + "/app/vehicleQuest/saveVehicleQuest.do";

    //意见反馈
    public static final String SAVEFEEDBACK = Config.IP + "/app/feedback/saveFeedBack.do";

    //用户模块-发送短信验证码
    public static final String CODE = Config.IP + "/app/sendBuyVehicle.do";

    public static final String YZM = Config.IP + "/app/sendBuyVehicle.do";


    //获取手机发送短信次数
    public static final String SENDNUMBER = Config.IP + "/app/getMobileSendNumberByType.do";


    //提交企业/个人认证
    public static final String SUBMITREGISTER = Config.IP + "/app/user/submitAuthentication.do";

    //上传证件照片
    public static final String UPLOAD = Config.IP + "/app/upload/uploadUserLicenseImg.do";

    //刷新/获取用户基本信息
    public static final String REFRESHUSERRESULT = Config.IP + "/app/user/refreshUserResult.do";

    //签到
    public static final String USERSIGN = Config.IP + "/app/user/userSign.do";

    //初始化用户认证信息
    public static final String REFRESHCERTIFIEDINFO = Config.IP + "/app/user/initAuthentication.do";

    //获取商品列表
    public static final String CARLIST = Config.IP + "/app/vehicle/getVehicleList.do";

    //获取首页数据
    public static final String HOMEINFO = Config.IP + "/app/getIndexParam.do";

    //获取品牌列表
    public static final String BRANDSLIST = Config.IP + "/app/vehicle/getBrandList.do";

    //获取城市列表
    public static final String CITYLIST = Config.IP + "/app/vehicle/getVehicleCityList.do";

    //获取商品详情
    public static final String SHOPDETAILINFO = Config.IP + "/app/vehicle/getVehicleDetail.do";

    //预约看车-发送验证码
    public static final String LOOKCARCODE = Config.IP + "/app/order/sendMobileOrderCaptcha.do";

    //预约看车
    public static final String PRELOOKCAR = Config.IP + "/app/order/saveOrder.do";

    //搜索页-模糊匹配车辆名称
    public static final String SEARCH = Config.IP + "/app/vehicle/searchBrandSeries.do";

    //搜索页-模糊匹配车辆名称
    public static final String LOAN = Config.IP + "/app/loan.do";

    //生成分享日志
    public static final String SHARELOG = Config.IP + "/app/user/vehicleShare.do";
    //取消收藏
    public static final String CANCLE_COLLECTION = Config.IP + "/app/owner/cancelCollect.do";

    public static final String APPVERSION = Config.APPVERSIONIP + "/app/getAppVersion.do";

    public static final String CARSERIES = Config.IP + "/app/vehicle/getSeriesList.do";//获取车系

    public static final String VTYPELIST = Config.IP + "/app/vehicle/getModelList.do";//获取车型

    //汽车检测
    public static final String CARJC = Config.IP + "/app/appCheckVehicle.do";

    //我要卖车问题查询
    public static final String SELLCARPROBLEM = Config.IP + "/web/owner/queryBuyVehicvleAns.do";

    //我要卖车
    public static final String SELLCAR = Config.IP + "/app/buyVehicle.do";

    //我的收藏列表
    public static final String COLLECTVEHICLE_URL = Config.IP + "/app/user/queryVehicleCollection.do";


    public static final String YHXI = Config.H5IP + "/agreement?type=app";//用户协议/rch/#
    public static final String ABOUT = Config.H5IP + "/about?type=app";//关于我们/rch/#
    public static final String TASKURL = Config.H5IP + "/distributor?type=app";//任务中心/rch/#
    public static final String NEWCARMOR_URL = Config.H5IP + "/paramsNew?id=";//新车详情更多的界面
    public static final String CAR_AGREEMENT_URL = Config.H5IP + "/vehicleAgreement";//发布车辆协议
    public static final String NOTES_DETAIL_URL = Config.H5IP + "/newDetail?id=";//发布车辆协议

    public static final String KFURL = "http://chat56.live800.com/live800/chatClient/chatbox.jsp?companyID=998798&configID=170297&jid=2611795639";

//    public static final String SINGIN=Config.H5IP+"/signIn?backUrl=my";

//    public static final String SINGIN=Config.H5IP+"/signIn?type=app";
//    public static final String SINGIN="http://192.168.2.244:8081/signIn?backUrl=my&type=app";

    //    public static final String SINGIN=Config.H5IP+"/signIn?type=app";
    public static final String SINGIN = "http://192.168.2.244/signIn?type=app";

    //预约单详情
    public static final String ORDERDETAIL_URL = Config.IP + "/web/user/getOrderDetail.do";




    /*---------------------------------------------------------------------------------------------*/


    //用户找回密码-发送找回密码验证码
    public static final String SENDCODE = Config.IP + "/app/sendFindPasswordCaptcha.do";

    //用户找回密码-验证找回密码验证码
    public static final String CHECKCODE = Config.IP + "/app/checkFindPasswordCaptcha.do";

    //用户找回密码-重置密码
    public static final String RESETPWD = Config.IP + "/app/resetPassword.do";


    //用户注册-点击下一步验证
    public static final String NEXTVERIFIC = Config.IP + "/app/checkRegistCaptchaAndInviteCode.do";

    //编辑新增发布二手车
    public static final String FB_URL = Config.IP + "/app/old/addOrEditOldCar.do";

    //获取枚举
    public static final String DOWNENUM_URL = Config.IP + "/app/downEnum.do";

//    public static final java.lang.String TEST ="http://www.baidu.com";

    public static final String MYCOLLECT_URL = Config.IP + "/app/myCollect.do";

    public static final String QUERYOWNERUSER = Config.IP + "/app/newuser/queryUserByEntId.do";//员工管理 v1.2.1

    //我的收藏分享
    public static final String SHURE = Config.IP + "/app/myCollect.do";

    //员工添加
    public static final String SHURE_YGTJ = Config.IP + "/app/myCollect.do";
    //删除员工
    public static final String OPERUSER = Config.IP + "/app/newuser/operUser.do";

    public static final String INVITEUSER = Config.IP + "/app/newuser/inviteUser.do";

    public static final String QUERTOWNERVEHICLE = Config.IP + "/app/owner/quertOwnerVehicle.do";


    public static final String AUDITORDER = Config.IP + "/app/user/getListAuditOrderByEnt.do";

    //预约单审核预约详情
    public static final String AUDITORDERDETAILBYENT = Config.IP + "/app/user/getAuditOrderDetailByEnt.do";

    public static final String GETORDERDETAILBYENT = Config.IP + "/app/user/getOrderDetailByEnt.do";

    public static final String CANCELVEHICLE_URL = Config.IP + "/app/user/cancelVehicle.do";

    public static final String CONFIRMSEEVEHICLE = Config.IP + "/app/user/confirmSeeVehicle.do";

    public static final String AUDITVEHICLE = Config.IP + "/app/user/auditVehicle.do";

    public static final String QUERTAPPPUBLISHVEHICLE = Config.IP + "/app/owner/quertAppPublishVehicle.do";

    //获取车辆管理商品详情
    public static final String APPPUBLISHVEHICLEDETAIL = Config.IP + "/app/owner/quertAppPublishVehicleDetail.do";

    //获取车辆商品详情
    public static final String GETVEHICLEDETAIL_URL = Config.IP + "/app/vehicle/getVehicleDetail.do";


    public static final String SAVEQUESTVEHICLE = Config.IP + "/app/questVehicle/saveQuestVehicle.do";

    //客户管理列表
    public static final String CUSTOMER_MANAGER_LIST = Config.IP + "/app/newuser/queryCustomer.do";
    //客户详情
    public static final String CUSTOMER_DETAIL = Config.IP + "/app/newuser/queryUserDeatil.do";
    //写跟进
    public static final String WRITE_FOLLOW = Config.IP + "/app/order/followUser.do";
    //新增、编辑新车辆
    public static final String ADD_OR_EDIT_NEW_CAR = Config.IP + "/app/new/addOrEditNewCar.do";
    //删除/下架新车辆
    public static final String DELETE_NEW_CAR = Config.IP + "/app/new/delOrDownNewCar.do";
    //车辆收藏取消收藏
    public static final String COLLECTION_URL = Config.IP + "/app/user/vehicleCollection.do";
    //预约操作
    public static final String OPER_ORDER_URL = Config.IP + "/app/order/operAdvanceOrder.do";
    //询价处理
    public static final String INQUIRY_OPER_URL = Config.IP + "/app/order/operInquery.do";

    public static final String SUBMITVEHICLEINQUIRY_URL = Config.IP + "/app/vehicleInquiry/submitVehicleInquiry.do";

    public static final String SUBMITAUTHENTICATION_URL = Config.IP + "/app/user/submitAuthentication.do";


    //新车管理列表
    public static final String NEWCARGULI_URL = Config.IP + "/app/new/queryNewCarList.do";
    //二手车管理列表
    public static final String OLDCARGULB_URL = Config.IP + "/app/old/queryOldCarList.do";
    //新车管理详情
    public static final String NEWCARDEATILE_URL = Config.IP + "/app/new/queryNewCarDetail.do";
    //新车管理列表
    public static final String OLECARDATILE_URL = Config.IP + "/app/old/queryOldCarDetail.do";
    //获取车辆规格列表
    public static final String STANDARDLIST_URL = Config.IP + "/app/vehicle/getVehicleStandardList.do";
    //明星车型列表
    public static final String STARTCAR_URL = Config.IP + "/app/vehicle/getShowiconVehicleList.do";
    //生意圈列表
    public static final String GETCIRCLELIST_URL =Config.IP + "/app/circle/getCircleList.do";
    //生意圈查看电话
    public static final String GETCIRCLEMOBILE_URL = Config.IP + "/app/circle/getCircleMobile.do";
    //用于进入发布生意圈页面时获取用户初始手机号码
    public static final String INITCIRCLE_URL =Config.IP + "/app/circle/initCircle.do";
    public static final String SAVECIRCLE_URL = Config.IP + "/app/circle/saveCircle.do";

    public static String DELETE_OLD_CAR = Config.IP + "/app/old/delOrDownOldCar.do";
    //反馈列表
    public static String FEEDBACK_LIST = Config.IP +"/app/feedback/getFeedBackList.do";
    //新闻列表
    public static String NOTES_LIST = Config.IP + "/app/rchNews/getRchNewsList.do";
}

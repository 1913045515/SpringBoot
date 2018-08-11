package com.example.demo.utils;

/**
 * 常量定义
 * @author wangjun
 *
 */
public class Constants {

	/**
	 * 用户信息KEY
	 */
	public static final String USER_INFO = "USER_INFO";
	
    /**
     * 记录登陆前地址
     */
    public static final String LOGIN_UP_URL = "LOGIN_UP_URL";

	/**
	 * 分页值
	 */
	public static final int PAGE_SIZE = 20;
	
	/**
	 * 密码方式登录
	 */
	public static final String LOGIN_TYPE_PW="1";
	
	/**
	 * 0:表示用户状态为未激活
	 */
	public static final int USER_STATUS_STOP=0;
	
	/**
	 * 1:表示用户状态为激活
	 */
	public static final int USER_STATUS_OPEN=1;

	/**
	 * 用户选择收货地址
	 */
	public static final String USER_EXPRESS_ADD="USER_EXPRESS_ADD";

	/**
	 * 快递
	 */
	public static final String LOGISTICS_TYPE_EXPRESS ="1";

	/**
	 * EMS
	 */
	public static final String LOGISTICS_TYPE_EMS ="2";

	/**
	 * 平邮
	 */
	public static final String LOGISTICS_TYPE_GENERAL ="3";

	/**
	 * 供应商
	 */
	public static final String LOGISTICS_REF_TYPE_SUPP ="1";

	/**
	 * C店
	 */
	public static final String LOGISTICS_REF_TYPE_SHOP ="2";

	/**
	 * 线下店
	 */
	public static final String LOGISTICS_REF_TYPE_STORE ="3";

	/**
	 * 配送按件数
	 */
	public static final String COST_TYP_NUM ="2";

	/**
	 * 配送按体积
	 */
	public static final String COST_TYP_VOLUNE ="4";

	/**
	 * 配送按重量
	 */
	public static final String COST_TYP_WEIGHT="1";

	/**
	 * 商城前端域名
	 */
	public static final String APP_DOMAIN = "appDomain";

	/**
	 * 文件域名
	 */
	public static final String FILE_DOMAIN = "fileDomain";

	/**
	 * 用户对象缓存(redis)KEY前缀
	 */
	public static final String REDIS_USER="REDIS_USER";

	/**
	 * 购物车缓存KEY前缀
	 */
	public static final String REDIS_CART = "REDIS_CART";

	/**
	 * 订单列表缓存KEY前缀
	 */
	public static final String REDIS_ORDER = "REDIS_ORDER";

	/**
	 * 子订单列表缓存KEY前缀
	 */
	public static final String REDIS_ORDER_ITEM = "REDIS_ORDER_ITEM";

	/**
	 * 商品对象缓存KEY前缀
	 */
	public static final String REDIS_GOODS = "REDIS_GOODS";

	/**
	 * 订单申请状态名称
	 */
	public static final String[] STATUS_VALUES = {"申请中", "待审核", "待修改", "待报价",
			"已报价", "确认报价", "待付款", "已付款", "办理中", "已完成", "已取消", "草稿",
			"合同签署"};

	/**
	 * 邮箱发送原因
	 */
	public static final String[] EMAIL_VALUES = {"注册","找回密码"};

	/**
	 * 订单审核文件类型
	 */
	public static final int AUDIT_TYPE = 1;


	/**
	 * 订单合同
	 */
	public static final int CONTRACT_TYPE = 2;


	/**
	 * 订单补充资料
	 */
	public static final int AUDIT_EXPAND_TYPE = 3;


	/**
	 * 百度地图AK
	 */
	private static final String BAIDU_MAP_AK = "BwC8VFGOGGACUBt32bn5hS2aFSgB0vqt";

	/**
	 * 是否加入黑名单（0未加入）
	 */
	public static final int USER_BLACK_STATUS_ZERO = 0;

	/**
	 * 是否加入黑名单（1已加入）
	 */
	public static final int USER_BLACK_STATUS_ONE = 1;

	/**
	 * token expires -1（永久）
	 */
	public static final int USER_TOKEN_EXPIRES_FONE = -1;

	/**
	 * token状态用户存在异常！请联系客服
	 */
	public static final int USER_TOKEN_TWO = 2;

	/**
	 * hashMap类型
	 */
	public static final String JAVA_UTIL_HASHMAP = "java.util.HashMap";
	/**
	 * model 类型
	 */
	public static final String JAVA_MODEL = "model";




	/**
	 * 老项目搬过来的常量
	 */
	public static String ROLE_ADMIN = "ROLE_ADMIN";
	public static String ROLE_MC = "ROLE_MC";
	public static String ROLE_COMMUNITY = "ROLE_COMMUNITY";
	public static String ROLE_MERCHANT = "ROLE_MERCHANT";
	public static String ROLE_STORE = "ROLE_STORE";
	public static String YP_MENU_ROOT = "SORT_MU_ROOT";
	public static String YP_MENU_SORT_ITEM = "SORT_MU_ITEM";
	public static String YP_MENU_COMMTIY_ITEM = "COMUTIY_MU_ITEM";
	public static String DEFAULT_COMMTIY_ID = "-1";
	public static String WX_INDEX = "http://www.baidu.com?agentId={agentId}";
	public static String WX_OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx520c15f417810387&redirect_uri=http://www.baidu.com?agentId={agentId}&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
	public static String SESSION_KEY = "CURRENT_USER";
	public static String USER_SESSION = "LOGIN_USER_INFO";
	public static String QUEUE_ONE_KEY = "queue_one_key";
	public static String GENERATOR_ROOT_PATH = System.getProperty("user.dir");
	public static String USER_MENU = "LOGIN_USER_MENU";
	public static String REPOSITORY_PATH_KEY = "file.default.upload.path";
	public static String REPOSITORY_PATH_TYPE_KEY = "file.default.upload.type";
	public static String USER_TOKEN = "LOGIN_USER_INFO_TOKEN";
	public static String ACTIVE_START_BATCH_JOB = "activeStartBatchJob";
	public static String ACTIVE_END_BATCH_JOB = "activeEndBatchJob";
	//public static String DEFULT_PIC = "http://infuneye-img.oss-cn-shenzhen.aliyuncs.com/2018/06/14/width_750_height_1334/0d746cc4-c295-41a4-918c-345e123eb9a4.png";
	public static String DEFULT_PIC = "http://infuneye-img.oss-cn-shenzhen.aliyuncs.com/2018/06/28/width_750_height_1206/e8d819e8-1e13-4057-a7a0-390988444016.png";



	/**
	 * 定时任务cron表达式，默认每秒跑一次
	 */
	public static final String DEFAULT_CRON_VALUE = "0/1 * * * * ?";

	/**
	 * 积分兑换颜值比例
	 */
	public static final int EXCHANGE_SCORE_SCALE = 100;

	public static final String EXCHANGE_END_DATE = "2018-08-01 00:00:00";

	public static final String PUSH_SYS_DESC = "系统推送消息";

	/**
	 * 活动定时任务默认执行时间间隔：5分钟
	 */
	public static final int DEFAULT_REPEAT_INTERVAL = 2 * 60;

	/**
	 * elasticsearch操作成功状态标识
	 */
	public static final String ELASTICSEARCH_SUCCESS="OK";
}

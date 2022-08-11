package vn.nextpay.nextshop.constant;

import java.util.Base64;

public class Constant {

	public static final String APP_NAME = System.getenv("APP_NAME");
	public static final String APP_ID = System.getenv("APP_ID");
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String REDIS_TOKEN_PREFIX = "SUPPER_APP_TOKEN_";
	public static final String PUBLIC_KEY = new String(Base64.getDecoder().decode(System.getenv("PUBLIC_KEY")));
	public static final String PREFIX_USER_ID = "USER_ID";
	public static final String PREFIX_MERCHANT_ID = "MERCHANT_ID";
	public static final String PREFIX_APP_PARENT_ID = "APP_PARENT_ID";
	public static final String PREFIX_APP_ID = "APP_ID";
	public static final String TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJBUFBfUEFSRU5UX0lEIjoiNWVmZWYyMGM0NTIxZmQzYWFhYmY0MTk3IiwiUEVSTUlTU0lPTlMiOltdLCJDSEFOTkVMIjoiV0VCIiwiVVNFUl9JRCI6IjVmYjc1NzkxNDA0NDNiMDAxMjE3MzdkMCIsImV4cCI6MTY1MjE2NTAyOSwiaWF0IjoxNjIwNjI5MDI5LCJNRVJDSEFOVF9JRCI6IjVmYjc3YmIwZGQyMWQwMDAxMTM3ZWUxZCJ9.psZ6fPix1AGqbnYEd_XuxdmCKHSD8_bsLZvf-n0RLjmenE3_bQ_nu8fqZY6HpB-QN1BXeip3_ZX4SQyNg62IjInPQDvgHtr0FE1bJmPMT1gHe8SMmS90zxgwXktSF2ASG0TJ0jNKwNjr47v0stfn4Il5Pcq3risA7v-0qvk4z1A";
	public static final String WARD_LANG = "Phường ";
	public static final String DISTRICT_LANG = "Quận ";
	public static final String CITY_LANG = "Thành phố ";
    public static final String PREFIX_CHANNEL = "CHANNEL";
	public static final String PREFIX_PERMISSIONS = "PERMISSIONS";

}

/**
 *
 */
package com.ais.sys.utils;

/**
 * Define all constant for whole application
 *
 * @author AIS
 */
public class ConstantUtil {

	private ConstantUtil() {
	  
	}
	 
    //contant yes or no
    public static final String CONTANT_YES = "Y";
    public static final String CONTANT_NO = "N";

    //product status
    public static final String PRODUCT_STATUS_APPROVED = "APPROVED";


    // error  type
    public static final String ERROR_TYPE_DANGER = "danger";
    public static final String ERROR_TYPE_SUCCESS = "success";


    public static final String RETURN_CONFIRMED = "RETURN_CONFIRMED";
    public static final String WAIT_RETURN = "WAIT_RETURN";
    public static final String CANCELLED = "CANCELLED";
    public static final String NEW = "NEW";
    public static final String PICKED = "PICKED";
    public static final String SHIPPED = "SHIPPED";
    public static final String DELIVERY_FAILURE = "DELIVERY_FAILURE";
    public static final String DELIVERY_CONFIRMED = "DELIVERY_CONFIRMED";
    public static final String DELIVERY = "DELIVERY";
    public static final String COMPLETED = "COMPLETED";


    // version
    public static final String VERSION_STAGING="STAGING";
    public static final String VERSION_ONLINE="ONLINE";
    

    //user for _BARCODE or IMAGE
    //PRODUCT STATUS APPROVED_IN_RETEK--A UNAPPROVED_IN_RETEK--F
    public static final String PRODUCT_STATUS_APPROVED_IN_RETEK="APPROVED_IN_RETEK";
    public static final String PRODUCT_STATUS_UNAPPROVED_IN_RETEK="UNAPPROVED_IN_RETEK";
    //A Accepted; F Failed
    public static final String PRODUCT_STATUS_RETEK_A="A";
    public static final String PRODUCT_STATUS_RETEK_F="F";

    public static final String PRODUCT_IMAGE_TYPE="PRODUCT_IMAGE";
    //swatch 
    public static final String SWATCH_IMAGE_TYPE="SWATCH_IMAGE";

    public static final String JOB_USER_NAME="JOB";

    public static final String LOGIN_FAILURE_TYPE="LOGIN_FAILURE_TYPE";
    public static final String LOGIN_FAILURE_TYPE_DISABLED="DISABLED";
    public static final String LOGIN_FAILURE_TYPE_BADCREDENTIALS="BADCREDENTIALS";
    
    public static final String ARCHIVE    = "archive";
    public static final String ERROR    = "error";
    public static final String SUPPLIER_DIRECT_DELIVERY    = "SUPPLIER_DIRECT_DELIVERY";
    public static final String CONSIGNMENT_VIA_WAREHOUSE    = "CONSIGNMENT_VIA_WAREHOUSE";
    public static final String CONSIGNMENT   = "CONSIGNMENT";
    
    public static final String LOGIN_FAILURE_NAME="LOGIN_FAILURE_NAME";
    
    public static final String QUERY_IP_URL = "url.ip"; 
    
    public interface PropertiesKeys {
    	String IMAGE_HOST = "imageHost";
    	String IMAGE_PROTOCOL="imageProtocol";
    	String IMAGE_ROOT_PATH="img.rootPath";
    }
    
    public interface ImageSize {
		public int MIN_SIZE = 80;
		public int MEDIUM_SIZE = 400;
		public int MAX_SIZE = 1024;
	}
	
	public interface ImageSubfix {
		public String MIN_SUBFIX = "_S"; // small
		public String MEDIUM_SUBFIX = "_M"; //middle
		public String MAX_SUBFIX = "_L"; //large
	}

}

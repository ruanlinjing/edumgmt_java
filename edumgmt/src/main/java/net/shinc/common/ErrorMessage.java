package net.shinc.common;

/**
 * 错误提示信息枚
 * @author wang_hw
 */
public enum ErrorMessage {

	SUCCESS("SUCCESS"),
	ERROR_DEFAULT("ERROR_DEFAULT"),
	RESULT_EMPTY("RESULT_EMPTY"),
	
	ADD_SUCCESS("ADD_SUCCESS"),
	ADD_FAILED("ADD_FAILED"),
	DELETE_SUCCESS("DELETE_SUCCESS"),
	DELETE_FAILED("DELETE_FAILED"),
	UPDATE_SUCCESS("UPDATE_SUCCESS"),
	UPDATE_FAILED("UPDATE_FAILED"),
	LOGIN_SUCCESS("LOGIN_SUCCESS"),
	LOGIN_FAILED("LOGIN_FAILED"),
	
	NEED_LOGIN("NEED_LOGIN"),
	PASSWORD_WRONG("PASSWORD_WRONG"),
	NEWPASSWORD_WRONG("NEWPASSWORD_WRONG"),
	NICKNAME_EXIST("NICKNAME_EXIST"),
	COURSE_EXIST("COURSE_EXIST"),
	
	KNOWLEDGEPOINT_EXIST("KNOWLEDGEPOINT_EXIST"),
	KNOWLEDGE_USED("KNOWLEDGE_USED"),
	
	USER_NONE("USER_NONE"),
	USER_UNABLE("USER_UNABLE"),
	
	ERROR_PARAM_ERROR("ERROR_PARAM_ERROR"),
	
	KEYWORD_EXIST("KEYWORD_EXIST"),
	KEYWORD_ISUSED("KEYWORD_ISUSED");
	
	private String code;
	ErrorMessage(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public static void main(String[] args) {
		System.out.println(ErrorMessage.SUCCESS.getCode());
	}
	
	
}

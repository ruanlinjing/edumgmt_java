package net.shinc.orm.mybatis.bean;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/** 
 * @ClassName Lecture 
 * @Description 视频讲解人
 * @author wangzhiying 
 * @date 2015年7月31日 下午7:51:49  
 */
public class Lecture implements Serializable{

	private static final long serialVersionUID = -1973504308864577176L;

	private Integer id;
	
	@NotEmpty(message="{lecture.name.not.empty}")
    private String name;

    private String level;

    public Lecture() {
	}
    
	public Lecture(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }
}

package com.example.demo.entity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
/**
 * @author linzhiqiang
 * @create 2018/08/01
 */
@Document(collection = "test")
public class TestVO{

    /**
     * 消息标题
     */
    @Field("id")
    private String id;

    /**
     * 消息标题
     */
    @Field("title")
    private String title;
    /**
     * 消息描述
     */
    @Field("description")
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TestVO(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.example.demo.controller;
import com.example.demo.entity.TestVO;
import com.example.demo.util.MongoUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试的控制类
 * @author linzhiqiang
 * @date 2018/7/29
 */
@RestController
@RequestMapping("mongodb")
public class MongoDBController {
    @RequestMapping("save")
    public boolean save(){
        TestVO testVO =new TestVO();
        testVO.setId("1");
        testVO.setTitle("I am title");
        testVO.setDescription("I am description");
        MongoUtil.getInstance().save(testVO);
        return true;
    }

    @RequestMapping("get")
    public List get(){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id","1");
        List list = MongoUtil.getInstance().listByParamMap(paramMap,TestVO.class);
        return list;
    }

    @RequestMapping("update")
    public boolean update(){
        TestVO testVO =new TestVO();
        testVO.setId("1");
        testVO.setDescription("11111111111111111");
        MongoUtil.getInstance().update(testVO,TestVO.class);
        return true;
    }

    @RequestMapping("delete")
    public boolean delete(){
        MongoUtil.getInstance().remove("1",TestVO.class);
        return true;
    }
}

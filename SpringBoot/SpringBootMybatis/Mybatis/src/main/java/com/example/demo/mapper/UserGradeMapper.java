package com.example.demo.mapper;

import com.example.demo.model.UserGrade;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserGradeMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserGrade record);

    UserGrade selectByPrimaryKey(String id);

    List<UserGrade> selectAll();

    int updateByPrimaryKey(UserGrade record);
}
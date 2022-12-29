package com.example.dowitter.mybatis;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MybatisRepository {
    List<MemberVO> getMemberList();
}

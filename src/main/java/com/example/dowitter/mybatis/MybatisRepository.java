package com.example.dowitter.mybatis;

import com.example.dowitter.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MybatisRepository {
    List<MemberVO> getMemberList();
}

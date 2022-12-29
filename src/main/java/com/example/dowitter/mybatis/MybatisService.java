package com.example.dowitter.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MybatisService {

    private final MybatisRepository mybatisRepository;

    @Autowired
    public MybatisService(MybatisRepository mybatisRepository) {
        this.mybatisRepository = mybatisRepository;
    }

    public List<MemberVO> findMemberList() {
        return mybatisRepository.getMemberList();
    }
}

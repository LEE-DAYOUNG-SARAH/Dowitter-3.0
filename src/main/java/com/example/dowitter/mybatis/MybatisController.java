package com.example.dowitter.mybatis;

import com.example.dowitter.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
public class MybatisController {

    private final MybatisService mybatisService;

    @Autowired
    public MybatisController(MybatisService mybatisService) {
        this.mybatisService = mybatisService;
    }

    @RequestMapping("/mybatis/test")
    public String test() {
        List<MemberVO> memberList = mybatisService.findMemberList();
        log.debug("memberList => {}", memberList);
        return "test";
    }

}

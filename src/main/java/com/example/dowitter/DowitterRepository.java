package com.example.dowitter;

import com.example.dowitter.Form.DocForm;
import com.example.dowitter.Form.JoinForm;
import com.example.dowitter.Form.LoginForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DowitterRepository {
    List<DocForm> findDocList();

    MemberVO findMember(LoginForm loginForm);

    int insertMember(JoinForm joinForm);
}

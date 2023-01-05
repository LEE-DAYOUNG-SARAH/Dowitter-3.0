package com.example.dowitter;

import com.example.dowitter.Form.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DowitterRepository {
    List<DocForm> findDocList();

    MemberVO findMemberForLogin(LoginForm loginForm);

    int insertMember(JoinForm joinForm);

    MemberVO findMemberByUid(Long uid);

    int updateMember(ModifyMemberForm modifyMemberForm);

    int insertDoc(WriteForm writeForm);

    int updateDoc(ModifyDocForm modifyDocForm);

    int deleteDoc(Long uid);

    int deleteMember(Long uid);
}

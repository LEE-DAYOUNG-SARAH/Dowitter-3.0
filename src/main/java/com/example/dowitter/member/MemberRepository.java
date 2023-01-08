package com.example.dowitter.member;

import com.example.dowitter.form.JoinForm;
import com.example.dowitter.form.LoginForm;
import com.example.dowitter.form.ModifyMemberForm;
import org.apache.ibatis.annotations.Mapper;

/**
 * Member repository.
 */
@Mapper
public interface MemberRepository {

    /**
     * 로그인을 위한 회원 조회
     *
     * @param loginForm
     * @return MemberVO
     */
    MemberVO findMemberForLogin(LoginForm loginForm);

    /**
     * uid를 통한 회원조회
     *
     * @param uid
     * @return MemberVO
     */
    MemberVO findMemberByUid(Long uid);

    /**
     * 회원 추가
     *
     * @param joinForm
     * @return int
     */
    int insertMember(JoinForm joinForm);

    /**
     * 회원 수정
     *
     * @param modifyMemberForm
     * @return int
     */
    int updateMember(ModifyMemberForm modifyMemberForm);

    /**
     * 회원 삭제
     *
     * @param uid
     * @return int
     */
    int deleteMember(Long uid);

}

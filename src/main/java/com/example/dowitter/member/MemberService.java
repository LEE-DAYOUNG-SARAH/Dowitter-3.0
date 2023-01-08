package com.example.dowitter.member;

import com.example.dowitter.form.JoinForm;
import com.example.dowitter.form.LoginForm;
import com.example.dowitter.form.ModifyMemberForm;
import com.example.dowitter.common.exception.UnMatchPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 로그인 정보에 부합하는 회원 조회
     *
     * @param loginForm
     * @return MemberVO
     */
    public MemberVO getMemberForLogin(LoginForm loginForm) {
        return memberRepository.findMemberForLogin(loginForm);
    }

    /**
     * uid를 통한 회원 조회
     *
     * @param uid
     * @return MemberVO
     */
    public MemberVO getMemberByUid(Long uid) {
        return memberRepository.findMemberByUid(uid);
    }

    /**
     * 회원가입
     * (비밀번호, 확인용 비빌번호가 동일한지 확인 후 회원추가)
     *
     * @param joinForm
     */
    @Transactional
    public void joinMember(JoinForm joinForm) {
        // 비밀번호 확인
        String password = joinForm.getPassword();
        String rePassword = joinForm.getRePassword();

        // 동일하지 않을 경우, error
        if( !password.equals(rePassword) ) {
            throw new UnMatchPasswordException();
        }

        // 회원가입
        int countOfRInsertRow = memberRepository.insertMember(joinForm);
        if( countOfRInsertRow != 1 ) {
            throw new RuntimeException();
        }
    }

    /**
     * 회원 수정
     * (비밀번호, 확인용 비밀번호가 동일한지 확인 후 회원 수정)
     *
     * @param modifyMemberForm
     */
@Transactional
    public void modifyMember(ModifyMemberForm modifyMemberForm) {
        // 비밀번호 유효성 검증
        String password = modifyMemberForm.getPassword();
        String rePassword = modifyMemberForm.getRePassword();

        if( !password.equals(rePassword) ) {
            throw new UnMatchPasswordException();
        }

        int countOfUpdateRow = memberRepository.updateMember(modifyMemberForm);
        if( countOfUpdateRow != 1 ) {
            throw new RuntimeException();
        }
    }

    /**
     * 회원 탈퇴
     *
     * @param uid
     */
public void withdrawal(Long uid) {
        int countOfDeleteRow = memberRepository.deleteMember(uid);
        if( countOfDeleteRow != 1 ) {
            throw new RuntimeException();
        }
    }

}

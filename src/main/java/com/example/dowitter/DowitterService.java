package com.example.dowitter;

import com.example.dowitter.Form.DocForm;
import com.example.dowitter.Form.JoinForm;
import com.example.dowitter.Form.LoginForm;
import com.example.dowitter.common.exception.UnMatchPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class DowitterService {
    private final DowitterRepository dowitterRepository;

    public List<DocForm> getDocList() {
        return dowitterRepository.findDocList();
    }

    public MemberVO getMember(LoginForm loginForm) {
        return dowitterRepository.findMember(loginForm);
    }

    public void joinMember(JoinForm joinForm) {
        // 비밀번호 확인
        String password = joinForm.getPassword();
        String rePassword = joinForm.getRePassword();

        // 동일하지 않을 경우, error
        if( !password.equals(rePassword) ) {
            throw new UnMatchPasswordException();
        }

        // 회원가입
        int countOfRInsertRow = dowitterRepository.insertMember(joinForm);
        if( countOfRInsertRow != 1 ) {
            throw new RuntimeException();
        }

    }

}

package com.example.dowitter;

import com.example.dowitter.Form.*;
import com.example.dowitter.common.exception.UnMatchPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class DowitterService {
    private final DowitterRepository dowitterRepository;

    public List<DocForm> getDocList() {
        return dowitterRepository.findDocList();
    }

    public MemberVO getMemberForLogin(LoginForm loginForm) {
        return dowitterRepository.findMemberForLogin(loginForm);
    }

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
        int countOfRInsertRow = dowitterRepository.insertMember(joinForm);
        if( countOfRInsertRow != 1 ) {
            throw new RuntimeException();
        }

    }

    public List<DocForm> getFeedList(Long uid) {
        return getDocList().stream()
                .filter(docForm -> docForm.getMemberUid().equals(uid))
                .collect(Collectors.toList());
    }

    public MemberVO getMemberByUid(Long uid) {
        return dowitterRepository.findMemberByUid(uid);
    }

    @Transactional
    public void modifyMember(ModifyMemberForm modifyMemberForm) {
        // 비밀번호 유효성 검증
        String password = modifyMemberForm.getPassword();
        String rePassword = modifyMemberForm.getRePassword();

        if( !password.equals(rePassword) ) {
            throw new UnMatchPasswordException();
        }

        int countOfUpdateRow = dowitterRepository.updateMember(modifyMemberForm);
        if( countOfUpdateRow != 1 ) {
            throw new RuntimeException();
        }
    }

    public void writeDoc(WriteForm writeForm) {
        int countOfInsertRow = dowitterRepository.insertDoc(writeForm);
        if( countOfInsertRow != 1 ) {
            throw new RuntimeException();
        }
    }

    public void modifyDoc(ModifyDocForm modifyDocForm) {
        int countOfUpdateRow = dowitterRepository.updateDoc(modifyDocForm);
        if( countOfUpdateRow != 1 ) {
            throw new RuntimeException();
        }
    }

    public void deleteDoc(Long uid) {
        int countOfDeleteRow = dowitterRepository.deleteDoc(uid);
        if( countOfDeleteRow != 1 ) {
            throw new RuntimeException();
        }
    }

    public void withdrawal(Long uid) {
        int countOfDeleteRow = dowitterRepository.deleteMember(uid);
        if( countOfDeleteRow != 1 ) {
            throw new RuntimeException();
        }
    }
}

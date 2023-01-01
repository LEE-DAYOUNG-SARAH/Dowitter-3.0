package com.example.dowitter;

import com.example.dowitter.Form.DocForm;
import com.example.dowitter.Form.LoginForm;
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

}

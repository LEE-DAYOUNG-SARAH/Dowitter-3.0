package com.example.dowitter;

import com.example.dowitter.Form.DocForm;
import com.example.dowitter.Form.LoginForm;
import com.example.dowitter.common.constants.SessionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class DowitterController {

    private final DowitterService dowitterService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "dowitter/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpSession session) {
        // 로그인 유효성 검증
        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info(fieldError.getField());
                log.info(fieldError.getDefaultMessage());
            }
            return "dowitter/login";
        }

        // 회원 조회
        MemberVO memberVO = dowitterService.getMember(loginForm);
        // 조회된 회원이 없는 경우
        if( memberVO == null ) {
            return "dowitter/login";
        }

        // 조회된 회원이 있는 경우, session에 member 정보 저장
        session.setAttribute(SessionConstant.LOGIN_MEMBER, memberVO);

        return "redirect:/timeline";
    }

    @RequestMapping("/timeline")
    public String timeline(Model model) {

        // 게시글 리스트 조회
        List<DocForm> docList = dowitterService.getDocList();
        log.info("######## docList => {}", docList);

        model.addAttribute("docList", docList);
        return "dowitter/timeline";
    }
}

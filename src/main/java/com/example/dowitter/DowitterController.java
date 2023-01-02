package com.example.dowitter;

import com.example.dowitter.Form.*;
import com.example.dowitter.common.constants.SessionConstant;
import com.example.dowitter.common.exception.UnMatchPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class DowitterController {
    private final DowitterService dowitterService;

    @GetMapping("/")
    public String home() {
        // 인터셉터에서 로그인 정보가 없으면 로그인 화면으로 보냄
        // 여기까지 타고 들어온것은 로그인 정보는 있는것. 타임라인으로 보내겠음
        return "redirect:/timeline";
    }

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
            log.info("error => {}", bindingResult);
            return "dowitter/login";
        }

        // 회원 조회
        MemberVO memberVO = dowitterService.getMemberForLogin(loginForm);
        // 조회된 회원이 없는 경우
        if( memberVO == null ) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "dowitter/login";
        }

        // 조회된 회원이 있는 경우, session에 member 정보 저장
        session.setAttribute(SessionConstant.LOGIN_MEMBER, memberVO);

        return "redirect:/timeline";
    }

    @GetMapping("/timeline")
    public String timeline(@ModelAttribute WriteForm writeForm,
                           Model model) {

        // 게시글 리스트 조회
        List<DocForm> docList = dowitterService.getDocList();
        log.info("######## docList => {}", docList);

        model.addAttribute("docList", docList);
        return "dowitter/timeline";
    }

    @GetMapping("/join")
    public String joinForm(@ModelAttribute JoinForm joinForm) {
        return "dowitter/join";
    }

    @PostMapping("/join")
    public String join(@Validated @ModelAttribute JoinForm joinForm,
                       BindingResult bindingResult) {
        if( bindingResult.hasErrors()) {
            log.info("error => {}", bindingResult);
            return "dowitter/join";
        }

        try {
            // 회원가입
            dowitterService.joinMember(joinForm);
        } catch (UnMatchPasswordException e) {
            bindingResult.reject("unMatchPassword", "비밀번호가 동일하지 않습니다.");
            return "dowitter/join";
        } catch (Exception e) {
            bindingResult.reject("error", "회원가입 도중 오류가 발생하였습니다.<br>다시 시도해주세요.");
            return "dowitter/join";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 날림
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/feed/{feedUid}")
    public String feedForm(@PathVariable("feedUid") Long feedUid,
                           Model model) {

        // 피드 회원 조회
        MemberVO feedMember = dowitterService.getMemberByUid(feedUid);
        feedMember.setPassword(null);
        model.addAttribute("feedMember", feedMember);

        // 피드 리스트 조회
        List<DocForm> feedList = dowitterService.getFeedList(feedUid);
        model.addAttribute("feedList", feedList);
        return "dowitter/feed";
    }

    @GetMapping("/modifyMember")
    public String modifyMemberForm(@ModelAttribute ModifyMemberForm modifyMemberForm,
                                   HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute(SessionConstant.LOGIN_MEMBER);
        Long uid = memberVO.getUid();

        // 회원정보 조회
        MemberVO memberByUid = dowitterService.getMemberByUid(uid);
        if( memberByUid == null ) {
            return "error";
        }

        modifyMemberForm.setUid(memberByUid.getUid());
        modifyMemberForm.setUserId(memberByUid.getUserId());
        return "dowitter/modifyMember";
    }

    @PostMapping("/modifyMember")
    public String modifyMember(@Validated @ModelAttribute ModifyMemberForm modifyMemberForm,
                               BindingResult bindingResult,
                               HttpSession session) {
        if( bindingResult.hasErrors() ) {
            log.info("error => {}", bindingResult);
            return "dowitter/modifyMember";
        }

        try {
            dowitterService.modifyMember(modifyMemberForm);
        } catch (UnMatchPasswordException e) {
            bindingResult.reject("unMatchPassword", "비밀번호가 동일하지 않습니다.");
            return "dowitter/modifyMember";
        } catch (Exception e) {
            return "error";
        }

        MemberVO memberVO = (MemberVO) session.getAttribute(SessionConstant.LOGIN_MEMBER);
        Long uid = memberVO.getUid();

        return "redirect:/feed/" + uid;
    }

    @PostMapping("/timeline/write")
    public String writeDoc(@Validated @ModelAttribute WriteForm writeForm,
                           BindingResult bindingResult) {
        if( bindingResult.hasErrors() ) {
            log.info("error => {}", bindingResult);
            return "dowitter/timeline";
        }

        // doc 저장
        try {
            dowitterService.writeDoc(writeForm);
        } catch (Exception e) {
            return "error";
        }

        return "redirect:/timeline";
    }
}

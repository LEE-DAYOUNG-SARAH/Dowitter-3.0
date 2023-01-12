package com.example.dowitter.member;

import com.example.dowitter.form.JoinForm;
import com.example.dowitter.form.LoginForm;
import com.example.dowitter.form.ModifyMemberForm;
import com.example.dowitter.common.constants.SessionConstant;
import com.example.dowitter.common.exception.UnMatchPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**``
 * The type Member controller.
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 로그인 화면
     *
     * @param loginForm
     * @return 로그인 화면
     */
    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "member/login";
    }

    /**
     * 로그인 처리
     *
     * @param loginForm
     * @param bindingResult
     * @param session
     * @return (로그인 실패시) 로그인 화면
     *         (로그인 성공시) 타임라인 화면
     */
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpSession session) {
        // 로그인 유효성 검증
        if(bindingResult.hasErrors()) {
            log.info("error => {}", bindingResult);
            return "member/login";
        }

        // 회원 조회
        MemberVO memberVO = memberService.getMemberForLogin(loginForm);
        // 조회된 회원이 없는 경우
        if( memberVO == null ) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "member/login";
        }

        // 조회된 회원이 있는 경우, session에 member 정보 저장
        session.setAttribute(SessionConstant.LOGIN_MEMBER, memberVO);

        return "redirect:/timeline";
    }

    /**
     * 회원가입 화면
     *
     * @param joinForm
     * @return 회원가입 화면
     */
    @GetMapping("/join")
    public String joinForm(@ModelAttribute JoinForm joinForm) {
        return "member/join";
    }

    /**
     * 회원가입 처리
     *
     * @param joinForm
     * @param bindingResult
     * @return (회원가입 실패시) 회원가입 화면
     *         (회원가입 성공시) 로그인 화면
     */
    @PostMapping("/join")
    public String join(@Validated @ModelAttribute JoinForm joinForm,
                       BindingResult bindingResult) {
        if( bindingResult.hasErrors()) {
            log.info("error => {}", bindingResult);
            return "member/join";
        }

        try {
            // 회원가입
            memberService.joinMember(joinForm);
        } catch (UnMatchPasswordException e) {
            bindingResult.reject("unMatchPassword", "비밀번호가 동일하지 않습니다.");
            return "member/join";
        } catch (Exception e) {
            bindingResult.reject("error", "회원가입 도중 오류가 발생하였습니다.<br>다시 시도해주세요.");
            return "member/join";
        }
        return "redirect:/login";
    }

    /**
     * 로그아웃 처리
     *
     * @param session
     * @return 로그인 화면
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 날림
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * 회원정보 수정 화면
     *
     * @param modifyMemberForm
     * @param session
     * @return 회원정보 수정 화면
     */
    @GetMapping("/member/modify")
    public String modifyMemberForm(@ModelAttribute ModifyMemberForm modifyMemberForm,
                                   HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute(SessionConstant.LOGIN_MEMBER);
        Long uid = memberVO.getUid();

        // 회원정보 조회
        MemberVO memberByUid = memberService.getMemberByUid(uid);
        if( memberByUid == null ) {
            return "error";
        }

        modifyMemberForm.setUid(memberByUid.getUid());
        modifyMemberForm.setUserId(memberByUid.getUserId());
        return "member/modify";
    }

    /**
     * 회원정보 수정 처리
     *
     * @param modifyMemberForm
     * @param bindingResult
     * @param session
     * @return (회원정보 수정 실패시) 회원정보 수정 화면
     *         (회원정보 수정 성공시) 개인 피드 화면
     */
    @PostMapping("/member/modify")
    public String modifyMember(@Validated @ModelAttribute ModifyMemberForm modifyMemberForm,
                               BindingResult bindingResult,
                               HttpSession session) {
        if( bindingResult.hasErrors() ) {
            log.info("error => {}", bindingResult);
            return "member/modify";
        }

        try {
            memberService.modifyMember(modifyMemberForm);
        } catch (UnMatchPasswordException e) {
            bindingResult.reject("unMatchPassword", "비밀번호가 동일하지 않습니다.");
            return "member/modify";
        } catch (Exception e) {
            return "error";
        }

        MemberVO memberVO = (MemberVO) session.getAttribute(SessionConstant.LOGIN_MEMBER);
        Long uid = memberVO.getUid();

        return "redirect:/feed/" + uid;
    }

    /**
     * 회원탈퇴 처리
     *
     * @param session
     * @return (회원탈퇴 실패시) 에러화면
     *         (회원탈퇴 성공시) 로그인 화면
     */
    @GetMapping("/withdrawal")
    public String withdrawal(HttpSession session) {

        try {
            MemberVO member = (MemberVO) session.getAttribute(SessionConstant.LOGIN_MEMBER);
            Long uid = member.getUid();

            // 회원탈퇴
            memberService.withdrawal(uid);

            session.invalidate();   // 세션 날
        } catch (Exception e) {
            return "error";
        }

        return "redirect:/login";
    }

}

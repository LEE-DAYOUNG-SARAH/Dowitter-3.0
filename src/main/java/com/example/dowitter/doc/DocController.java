package com.example.dowitter.doc;

import com.example.dowitter.Form.DocForm;
import com.example.dowitter.Form.ModifyDocForm;
import com.example.dowitter.Form.WriteForm;
import com.example.dowitter.member.MemberVO;
import com.example.dowitter.common.constants.SessionConstant;
import com.example.dowitter.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DocController {
    private final DocService docService;
    private final MemberService memberService;

    /**
     * 메인화면
     *
     * @return 타임라인
     */
    @GetMapping("/")
    public String home() {
        // 인터셉터에서 로그인 정보가 없으면 로그인 화면으로 보냄
        // 여기까지 타고 들어온것은 로그인 정보는 있는것. 타임라인으로 보내겠음
        return "redirect:/timeline";
    }

    /**
     * 타임라인 화면
     *
     * @param writeForm
     * @param model
     * @return 타임라인 화면
     */
    @GetMapping("/timeline")
    public String timeline(@ModelAttribute WriteForm writeForm,
                           Model model) {
        // 게시글 리스트 조회
        List<DocForm> docList = docService.getDocList();
        log.info("######## docList => {}", docList);

        model.addAttribute("docList", docList);
        return "doc/timeline";
    }

    /**
     * 개인 피드 화면
     *
     * @param feedUid
     * @param writeForm
     * @param model
     * @return 개인 피드 화면
     */
    @GetMapping("/feed/{feedUid}")
    public String feedForm(@PathVariable("feedUid") Long feedUid,
                           @ModelAttribute WriteForm writeForm,
                           Model model) {
        // 피드 회원 조회
        MemberVO feedMember = memberService.getMemberByUid(feedUid);
        feedMember.setPassword(null);
        model.addAttribute("feedMember", feedMember);

        // 피드 리스트 조회
        List<DocForm> feedList = docService.getFeedList(feedUid);
        model.addAttribute("feedList", feedList);
        return "doc/feed";
    }

    /**
     * 문서 작성 처리
     *
     * @param form
     * @param writeForm
     * @param bindingResult
     * @return (문서 작성 실패시) 게시글 리스트 화면
     *         (문서 작성 성공시) 게시글 리스트 화면
     */
    @PostMapping("/{form}/doc/write")
    public String writeDocForFeed(@PathVariable("form") String form,
                                  @Validated @ModelAttribute WriteForm writeForm,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        String returnUrl = "/" + form;
        if( form.equals("feed") ) {
            Long memberUid = writeForm.getMemberUid();
            returnUrl = returnUrl + "/" + memberUid;
        }

        if( bindingResult.hasErrors() ) {
            return "redirect:" + returnUrl;
        }

        // doc 저장
        try {
            docService.writeDoc(writeForm);
        } catch (Exception e) {
            return "error";
        }

        return "redirect:" + returnUrl;
    }

    /**
     * 문서 수정 화면
     *
     * @param uid
     * @return 문서 수정 화면
     */
    @PostMapping("/modifyDocForm")
    @ResponseBody
    public DocForm modifyDocForm(Long uid) {
        log.info("uid => {}", uid);

        // 게시글 조회
        DocForm doc = docService.getDocList().stream()
                .filter(docForm -> docForm.getUid().equals(uid))
                .findFirst()
                .orElseThrow();
        return doc;
    }

    /**
     * 문서 수정 처리
     *
     * @param form
     * @param modifyDocForm
     * @param bindingResult
     * @param session
     * @return (문서 수정 실패시) 에러화면
     *         (문서 수정 성공시) 게시글 리스트 화면
     */
    @PostMapping("/{form}/doc/modify")
    public String modifyDocForFeed(@PathVariable("form") String form,
                                   @Validated @ModelAttribute ModifyDocForm modifyDocForm,
                                   BindingResult bindingResult,
                                   HttpSession session) {
        if( bindingResult.hasErrors() ) {
            return "error";
        }

        try {
            // 게시글 수정
            docService.modifyDoc(modifyDocForm);
        } catch (Exception e) {
            return "error";
        }

        String returnUrl = "/" + form;
        if( form.equals("feed") ) {
            MemberVO member = (MemberVO) session.getAttribute(SessionConstant.LOGIN_MEMBER);
            Long uid = member.getUid();

            returnUrl = returnUrl + "/" + uid;
        }


        return "redirect:" + returnUrl;
    }

    /**
     * 문서 삭제 처리
     *
     * @param form
     * @param uid
     * @return (문서 삭제 실패시) 에러화면
     *         (문서 삭제 성공시) 게시글 리스트 화면
     */
    @GetMapping("/{form}/doc/delete")
    public String deleteDoc(@PathVariable("form")String form,
                            Long uid,
                            HttpSession session) {
        try {
            docService.deleteDoc(uid);
        } catch (Exception e) {
            log.info("{}", e);
            return "error";
        }

        String returnUrl = "/" +form;
        if( form.equals("feed") ) {
            MemberVO member = (MemberVO) session.getAttribute(SessionConstant.LOGIN_MEMBER);
            returnUrl = returnUrl + "/" + member.getUid();
        }

        return "redirect:" + returnUrl;
    }
}

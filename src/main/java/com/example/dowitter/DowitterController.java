package com.example.dowitter;

import com.example.dowitter.Form.DocForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
public class DowitterController {

    private final DowitterService dowitterService;

    public DowitterController(DowitterService dowitterService) {
        this.dowitterService = dowitterService;
    }

    @RequestMapping("/login")
    public String login() {
        return "dowitter/login";
    }

    @RequestMapping("/timeline")
    public String timeline(Model model) {
        List<DocForm> docList = dowitterService.getDocList();
        log.info("######## docList => {}", docList);

        model.addAttribute("docList", docList);
        return "dowitter/timeline";
    }
}

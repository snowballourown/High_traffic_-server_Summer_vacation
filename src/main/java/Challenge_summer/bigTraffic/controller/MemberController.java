package Challenge_summer.bigTraffic.controller;


import Challenge_summer.bigTraffic.domain.Member;
import Challenge_summer.bigTraffic.dto.MemberCreateRequest;
import Challenge_summer.bigTraffic.service.MemberService;
import lombok.AllArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    @ResponseBody
    public List<Member> memberAllFind() {
        return memberService.findByAll();
    }


    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest memberDto) {
        memberService.createMember(memberDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
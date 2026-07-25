package Challenge_summer.bigTraffic.controller;


import Challenge_summer.bigTraffic.domain.Member;
import Challenge_summer.bigTraffic.dto.member.MemberRequest;
import Challenge_summer.bigTraffic.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;


//    DTO로 바꾸짜 ->####
    @GetMapping("/members")
    @ResponseBody
    public List<Member> memberAllFind() {
        return memberService.findByAll();
    }


    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest memberDto) {
        memberService.createMember(memberDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
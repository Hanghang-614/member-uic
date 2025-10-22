package com.wch.member.controller;

import com.wch.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    
    @Autowired
    private MemberService memberService;
    
    @PostMapping("/{memberId}/session")
    public String createSession(@PathVariable String memberId) {
        memberService.createMemberSession(memberId);
        return "Session created for member: " + memberId;
    }
    
    @DeleteMapping("/{memberId}/session")
    public String closeSession(@PathVariable String memberId) {
        memberService.closeMemberSession(memberId);
        return "Session closed for member: " + memberId;
    }
    
    @PostMapping("/events")
    public String publishEvent(@RequestBody String eventData) {
        memberService.processEvent(eventData);
        return "Event published: " + eventData;
    }
    
    @GetMapping("/stats")
    public String getStats() {
        return String.format("Active sessions: %d, Registered listeners: %d", 
            memberService.getActiveSessionCount(), 
            memberService.getListenerCount());
    }
}
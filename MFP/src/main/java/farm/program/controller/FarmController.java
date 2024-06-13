package farm.program.controller;

import farm.program.domain.CustomerInfo;
import farm.program.domain.FarmInfo;
import farm.program.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/program")
public class FarmController {

    @Autowired
    private FarmService farmService;

    // 고객이 작물 선택시 해당 작물재배 가능한 농장 목록을 반환
    @GetMapping("/farms")
    public List<FarmInfo> getFarmsByCrop(@RequestParam String crop) {
        return farmService.getFarmsByCrop(crop);
    }

    // 고객이 농장을 선택하고 신청을 처리
    @PostMapping("/apply")
    public CustomerInfo applyForProgram(@RequestBody CustomerInfo customer) {
        return farmService.applyForProgram(customer);
    }

    // 농장주가 신청을 승인
    @PostMapping("/approve/{customerId}")
    public Optional<CustomerInfo> approve(@PathVariable Long customerId) {
        return farmService.updateStatus(customerId, "저희 서비스를 이용해 주셔서 감사합니다!");
    }

    // 농장주가 신청을 거부
    @PostMapping("/reject/{customerId}")
    public Optional<CustomerInfo> reject(@PathVariable Long customerId) {
        return farmService.updateStatus(customerId, "아쉽게도 현재 이용 불가능한 농장입니다.");
    }
}
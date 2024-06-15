package farm.program.service;

import farm.member.domain.Role;
import farm.program.domain.Crops;
import farm.program.domain.CustomerInfo;
import farm.program.domain.FarmInfo;
import farm.program.repository.CropsRepository;
import farm.program.repository.CustomerInfoRepository;
import farm.program.repository.FarmInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class FarmService {

    private final FarmInfoRepository farmInfoRepository;

    private final CustomerInfoRepository customerInfoRepository;

    private final CropsRepository cropsRepository;

    @Autowired
    public FarmService(FarmInfoRepository farmInfoRepository, CustomerInfoRepository customerInfoRepository, CropsRepository cropsRepository) {
        this.farmInfoRepository = farmInfoRepository;
        this.customerInfoRepository = customerInfoRepository;
        this.cropsRepository = cropsRepository;
    }

    public ResponseEntity<List<Crops>> getAllCrops(){
        List<Crops> crops = cropsRepository.findAll();
        return ResponseEntity.ok(crops);
    }

    // 고객이 선택한 작물에 따라 농장 목록을 반환하는 메서드
    public List<FarmInfo> getFarmsByCrop(List<String> crops) {
        List<FarmInfo> farmInfos = new ArrayList<>();
        for(String crop : crops){
            FarmInfo farmCrops = farmInfoRepository.findByName(crop);
            farmInfos.add(farmCrops);
        }
        return farmInfos;
    }

    // 고객의 신청을 처리하는 메서드
    public CustomerInfo applyForProgram(CustomerInfo customer) {
        return customerInfoRepository.save(customer);
    }

    // 고객 신청 상태를 업데이트하는 메서드
    // Optional<CustomerInfo> : 반환타입, CustomerInfo 객체가 존재할 수도 있고, 존재하지 않을 수도 있음을 나타냄
    public Optional<CustomerInfo> updateStatus(Long customerId, String status) {

        // customerId를 사용하여 데이터베이스에서 CustomerInfo 객체를 조회
        // 조회 결과는 Optional<CustomerInfo>로 반환, 조회된 객체가 있을 수도 있고 없을 수도 있음을 의미
        Optional<CustomerInfo> customerOpt = customerInfoRepository.findById(customerId);
        // Optional 객체에 CustomerInfo 객체가 존재하는지 확인 존재하면 true, 없으면 false 리턴
        if (customerOpt.isPresent()) {
            // Optional 객체에서 CustomerInfo 객체를 가져옴, 이 시점에서 customer 는 조회된 CustomerInfo 객체를 참조
            CustomerInfo customer = customerOpt.get();
            // CustomerInfo 객체의 status 필드를 새로운 status 로 업데이트
            customer.setStatus(status);
            // 변경된 객체 저장
            customerInfoRepository.save(customer);
        }
        // 업데이트된 CustomerInfo 객체를 포함한 Optional 객체를 반환 customerOpt 가 비어있으면 빈 Optional 객체를 반환
        return customerOpt;
    }

    public List<FarmInfo> getFarmerFarmInfos(){
        return farmInfoRepository.findByMemberRole(Role.ROLE_FARMER);
    }

    public List<CustomerInfo> getCustomerInfos(){
        return customerInfoRepository.findByMemberRole(Role.ROLE_CUSTOMER);
    }
}
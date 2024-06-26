package farm.program.dto;

import farm.program.domain.FarmCrops;

public class FarmCropsDto {
    private Long id;
    private String crops;
    private Long farmId;
    private String farmName;

    // 기본 생성자
    public FarmCropsDto() {}

    // 커스텀 생성자
    public FarmCropsDto(Long id, String crops, Long farmId, String farmName) {
        this.id = id;
        this.crops = crops;
        this.farmId = farmId;
        this.farmName = farmName;
    }

    // 엔티티를 DTO로 변환하는 변환 생성자
    public FarmCropsDto(FarmCrops entity) {
        this.id = entity.getId();
        this.crops = entity.getCrops();
        if (entity.getFarmInfo() != null) {
            this.farmId = entity.getFarmInfo().getId();
            this.farmName = entity.getFarmInfo().getName();
        }
    }

    // 엔티티를 DTO로 변환하는 정적 메서드
    public static FarmCropsDto fromEntity(FarmCrops entity) {
        return new FarmCropsDto(entity);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrops() {
        return crops;
    }

    public void setCrops(String crops) {
        this.crops = crops;
    }

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }
}

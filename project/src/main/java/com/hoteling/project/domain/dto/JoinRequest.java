package com.hoteling.project.domain.dto;

import java.util.ArrayList;
import java.util.List;

import com.hoteling.project.domain.entity.UserRole;
import com.hoteling.project.domain.entity.DogInfo;
import com.hoteling.project.domain.entity.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "아이디를 확인해 주세요.")
    private String uId;

    @NotBlank(message = "비밀번호를 확인해 주세요.")
    private String uPw;

    @NotBlank(message = "입력하신 비밀번호를 확인해 주세요.")
    private String uPwCheck;

    @NotBlank(message = "이름을 확인해 주세요.")
    private String uName;

    @NotBlank(message = "생년월일을 확인해 주세요.")
    private String uSsn;

    @NotBlank(message = "전화번호를 확인해 주세요.")
    private String uPhoneNum;

    @NotBlank(message = "이메일을 확인해 주세요.")
    private String uEmail;

    public User toUserEntity() {
        return User.builder()
                .uId(this.uId)
                .uPw(this.uPw)
                .uName(this.uName)
                .uSsn(this.uSsn)
                .uPhoneNum(this.uPhoneNum)
                .uEmail(this.uEmail)
                .uRole(UserRole.USER)
                .build();
    }
    
 private List<DogInfoRequest> dogInfos = new ArrayList<>();
    
    public List<DogInfo> toDogInfoEntities(User owner) {
        List<DogInfo> dogInfoList = new ArrayList<>();
        for (DogInfoRequest dogInfoRequest : dogInfos) {
            // 강아지 이름이 비어있다면 이 강아지 정보를 무시
            if (dogInfoRequest.getDogName() == null || dogInfoRequest.getDogName().isEmpty()) {
                continue;
            }
            DogInfo dogInfo = new DogInfo();
            dogInfo.setDogName(dogInfoRequest.getDogName());
            dogInfo.setDogGender(dogInfoRequest.getDogGender());
            dogInfo.setDogBirth(dogInfoRequest.getDogBirth());
            dogInfo.setNeutered(dogInfoRequest.getNeutered());
            dogInfo.setAdditionalInfo(dogInfoRequest.getAdditionalInfo());
            dogInfo.setOwner(owner);
            dogInfoList.add(dogInfo);
        }
        return dogInfoList;
    }
}

package com.example.hello_there.apratment;

import com.example.hello_there.apratment.dto.PatchApartmentReq;
import com.example.hello_there.apratment.dto.PostApartmentReq;
import com.example.hello_there.exception.BaseException;
import com.example.hello_there.exception.BaseResponse;
import com.example.hello_there.login.jwt.JwtService;
import com.example.hello_there.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartment")
public class ApartmentController {
    private final ApartmentService apartmentService;
    private final UtilService utilService;
    private final JwtService jwtService;

    /** 아파트 자동등록 **/
    @PostMapping("/auto-register")
    public BaseResponse<String> registerApartment() {
        try {
            // 인천시 부평구 아파트
            List<PostApartmentReq> postApartmentReqList = apartmentService.getApartmentInfoBupyeong();

            // 서울시 동작구 아파트
            postApartmentReqList.addAll(apartmentService.getApartmentInfoDongjak());

            // 서울시 관악구 아파트
            postApartmentReqList.addAll(apartmentService.getApartmentInfoGwanak());

            // 서울시 송파구 아파트
            postApartmentReqList.addAll(apartmentService.getApartmentInfoSongpa());

            for(PostApartmentReq postApartmentReq : postApartmentReqList) {
                apartmentService.createApartment(postApartmentReq); // 아파트 자동 등록
            }
            return new BaseResponse<>("인천시 부평구, 서울시 동작구, 서울시 관악구, 서울시 송파구 아파트를 DB에 등록합니다.");
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 유저의 아파트 정보 설정하기
     */
    @PatchMapping("/update")
    public BaseResponse<String> modifyApartmentInfo(@RequestBody PatchApartmentReq patchApartmentReq) {
        // PostMan에서 Headers에 Authorization필드를 추가하고, 로그인할 때 받은 jwt 토큰을 입력해야 실행이 됩니다.
        try {
            Long userId = jwtService.getUserIdx();
            return new BaseResponse<>(apartmentService.modifyApartment(patchApartmentReq, userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저의 아파트 등록하기
     */
    @PostMapping("/register")
    public BaseResponse<String> createApartment(@RequestBody PostApartmentReq postApartmentReq) {
        Apartment apartment = apartmentService.createApartment(postApartmentReq);
        String city = apartment.getCity();
        String district = apartment.getDistrict();
        String apartmentName = apartment.getApartmentName();
        return new BaseResponse<>(city + " " + district + apartmentName + " 등록이 완료되었습니다.");
    }
}
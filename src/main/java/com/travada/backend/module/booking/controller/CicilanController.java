package com.travada.backend.module.booking.controller;

import com.travada.backend.module.booking.service.CicilanService;
import com.travada.backend.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cicilan")
public class CicilanController {
    @Autowired
    private CicilanService cicilanService;

    @GetMapping("/{idDestinasi}/{orang}")
    private BaseResponse getCicilan(@PathVariable Long idDestinasi, @PathVariable int orang){
        BaseResponse baseResponse = new BaseResponse();

        baseResponse.setStatus(HttpStatus.CREATED);
        baseResponse.setData(cicilanService.viewCicilan(idDestinasi,orang));
        baseResponse.setMessage("sukses");
        return baseResponse;
    }

    @PutMapping("/{id}")
    private BaseResponse updateCicilan(@PathVariable Long id, @RequestBody String status){

        BaseResponse baseResponse = new BaseResponse();

        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(cicilanService.updateCicilanById(id,status));
        baseResponse.setMessage("status sukses diupdate");
        return baseResponse;
    }
}

package schwarz.jobs.interview.coupon.web.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@Controller
@RequestMapping("/api/coupons")
@Slf4j
public class CouponResource {

    @Autowired
    private CouponService couponService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody @Valid final CouponDTO couponDTO) {

        final Coupon coupon = couponService.createCoupon(couponDTO);

        if (null == coupon) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/")
    public ResponseEntity<List<Coupon>> getCoupons(@RequestBody @Valid final CouponRequestDTO couponRequestDTO) {
        return ResponseEntity.ok(couponService.getCoupons(couponRequestDTO));
    }
}

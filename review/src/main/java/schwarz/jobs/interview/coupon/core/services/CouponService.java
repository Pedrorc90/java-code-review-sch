package schwarz.jobs.interview.coupon.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@Service
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Optional<Coupon> getCoupon(final String code) {
        return couponRepository.findByCode(code);
    }

    public Coupon createCoupon(final CouponDTO couponDTO) {

        try {
            Coupon coupon = Coupon.builder()
                .code(couponDTO.getCode().toLowerCase())
                .discount(couponDTO.getDiscount())
                .minBasketValue(couponDTO.getMinBasketValue())
                .build();
            return couponRepository.save(coupon);
        } catch (final NullPointerException e) {
            log.error("Request should contain value for code");
            return null;
        }
    }

    public List<Coupon> getCoupons(final CouponRequestDTO couponRequestDTO) {

        final Optional<List<Coupon>> foundCoupons = couponRepository.findByCodeList(couponRequestDTO.getCodes());
        return foundCoupons.orElse(new ArrayList<>());

    }
}

package schwarz.jobs.interview.coupon.core.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@ExtendWith(SpringExtension.class)
public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Test
    public void createCoupon() {
        CouponDTO dto = CouponDTO.builder()
            .code("12345")
            .discount(BigDecimal.TEN)
            .minBasketValue(BigDecimal.valueOf(50))
            .build();

        couponService.createCoupon(dto);

        verify(couponRepository, times(1)).save(any());
    }



    @Test
    public void should_test_get_Coupons() {

        // Given
        CouponRequestDTO dto = CouponRequestDTO.builder()
            .codes(Arrays.asList("1111", "1234"))
            .build();

        Coupon coupon1 = Coupon.builder()
                .code("1111").discount(BigDecimal.TEN).minBasketValue(BigDecimal.valueOf(50))
                .build();
        Coupon coupon2 = Coupon.builder()
                .code("1234").discount(BigDecimal.TEN).minBasketValue(BigDecimal.valueOf(50))
                .build();

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(coupon1);
        coupons.add(coupon2);

        when(couponRepository.findByCodeList(anyList())).thenReturn(Optional.of(coupons));


        // When
        List<Coupon> returnedCoupons = couponService.getCoupons(dto);

        // Then
        assertThat(returnedCoupons.get(0).getCode()).isEqualTo("1111");
        assertThat(returnedCoupons.get(1).getCode()).isEqualTo("1234");
    }
}

package schwarz.jobs.interview.coupon.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import schwarz.jobs.interview.coupon.core.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(final String code);

    @Query("FROM Coupon c where c.code in :codes")
    Optional<List<Coupon>> findByCodeList(final List<String> codes);

}

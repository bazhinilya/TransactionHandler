package transaction.test.repository;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import transaction.test.entity.ExchengeRate;

public interface ExchengeRatesRepository extends JpaRepository<ExchengeRate, Long> {

    @Query(nativeQuery = true, value = "SELECT kzt_to_usd * :sum FROM exchenge_rate WHERE created_date < :current_date ORDER BY datetime DESC LIMIT 1")
    BigDecimal getConvertedKZTSumByActualUSDRate(@Param("sum") BigDecimal sum,
            @Param("current_date") Date currentDate);

    @Query(nativeQuery = true, value = "SELECT rub_to_usd * :sum FROM exchenge_rate WHERE created_date < :current_date ORDER BY datetime DESC LIMIT 1")
    BigDecimal getConvertedRUBSumByActualUSDRate(@Param("sum") BigDecimal sum,
            @Param("current_date") Date currentDate);
}
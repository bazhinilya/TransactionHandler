package transaction.test.repository;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import transaction.test.entity.Limit;

public interface LimitRepository extends JpaRepository<Limit, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM \"limit\" WHERE limit_datetime < :current_date AND limit_category = :current_category ORDER BY limit_datetime DESC LIMIT 1")
    Limit findNearestLimitByCurrentDate(@Param("current_date") Date currentDate,
            @Param("current_category") String currentCategory);
}
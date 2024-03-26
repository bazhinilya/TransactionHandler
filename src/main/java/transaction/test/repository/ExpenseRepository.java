package transaction.test.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import transaction.test.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM expense WHERE datetime < :current_date AND expense_category = :current_category ORDER BY datetime DESC LIMIT 1")
    Expense findNearestRemainingLimitByCurrentDate(@Param("current_date") Date currentDate,
            @Param("current_category") String currentCategory);

    @Query(nativeQuery = true, value = "SELECT * FROM expense WHERE limit_exceeded = true")
    List<Expense> findAllExceededLimit();
}
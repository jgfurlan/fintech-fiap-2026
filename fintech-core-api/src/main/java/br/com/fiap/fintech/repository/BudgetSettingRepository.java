package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.entity.BudgetSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BudgetSettingRepository extends JpaRepository<BudgetSettingEntity, Long> {
    List<BudgetSettingEntity> findByUserProfileUserId(String userId);
}

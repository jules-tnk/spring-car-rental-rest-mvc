package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.user_management.Agent;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AgentRepository extends JpaRepository<Agent, Long> {

    List<Agent> findAllById(Long id, Sort sort);

}

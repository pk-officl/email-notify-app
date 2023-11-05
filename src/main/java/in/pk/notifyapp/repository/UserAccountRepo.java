package in.pk.notifyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.pk.notifyapp.entity.UserAccountTO;

public interface UserAccountRepo extends JpaRepository<UserAccountTO, Long> {

}

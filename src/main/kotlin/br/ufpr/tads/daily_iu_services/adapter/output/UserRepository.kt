package br.ufpr.tads.daily_iu_services.adapter.output

import br.ufpr.tads.daily_iu_services.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

}
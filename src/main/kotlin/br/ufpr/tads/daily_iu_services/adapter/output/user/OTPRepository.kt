package br.ufpr.tads.daily_iu_services.adapter.output.user

import br.ufpr.tads.daily_iu_services.domain.entity.user.OTP
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OTPRepository : JpaRepository<OTP, Long> {
    fun findByUserIdAndUsedFalse(userId: Long): OTP?
}
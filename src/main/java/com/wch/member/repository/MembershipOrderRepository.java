package com.wch.member.repository;

import com.wch.member.entity.MembershipOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipOrderRepository extends JpaRepository<MembershipOrder, Long> {
}
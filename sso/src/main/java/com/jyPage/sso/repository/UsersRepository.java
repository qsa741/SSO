package com.jyPage.sso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jyPage.sso.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

	// id 중복여부 확인
	long countByIdIs(String id);

	// email 중복여부 확인
	long countByEmailIs(String email);

	// phone 중복여부 확인
	long countByPhoneIs(String phone);

	// id, email 매칭여부
	long countByIdIsAndEmailIs(String id, String email);

}

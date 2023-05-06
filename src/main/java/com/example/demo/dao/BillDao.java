package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.POJO.Bill;

public interface BillDao extends JpaRepository<Bill, Integer>{

	List<Bill> getAllBills();

	List<Bill> getBillByUsername(@Param("username") String username);

}

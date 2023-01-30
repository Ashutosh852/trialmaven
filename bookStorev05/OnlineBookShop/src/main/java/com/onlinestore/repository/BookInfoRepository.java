package com.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.onlinestore.entity.BookInfo;

@Repository
public interface BookInfoRepository extends JpaRepository<BookInfo, Long>, JpaSpecificationExecutor<BookInfo> {

}

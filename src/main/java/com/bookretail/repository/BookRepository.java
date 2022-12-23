package com.bookretail.repository;

import com.bookretail.model.Book;
import com.bookretail.model.OneSignalNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<OneSignalNotification> {


}
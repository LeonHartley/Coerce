package io.coerce.services.messaging.server.persistence;

import io.coerce.persistence.dao.Dao;
import io.coerce.persistence.dao.annotations.Field;
import io.coerce.persistence.dao.annotations.Transaction;

import java.util.List;

public interface LogDao extends Dao {
    @Transaction(query = "INSERT into `logs` (`category`, `data`) VALUES(:category, :logString);")
    LogBean create(final String category, final String logString);

    @Transaction(query = "UPDATE `logs` SET `category` = :bean.category, `data` = :bean.data WHERE ")
    void save(LogBean bean);

    @Transaction(query = "SELECT * FROM `logs` WHERE `category` = :category")
    List<LogBean> getLogsByCategory(final String category);

    class LogBean {
        @Field(name = "id")
        private long id;

        @Field(name = "category")
        private String category;

        @Field(name = "data")
        private String data;

        public LogBean(long id, String category, String data) {
            this.id = id;
            this.category = category;
            this.data = data;
        }

        public String getCategory() {
            return category;
        }

        public String getData() {
            return data;
        }
    }
}

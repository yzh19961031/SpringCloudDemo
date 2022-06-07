package com.yzh.server1.dao;

import com.yzh.server1.bean.Account;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yuanzhihao
 * @since 2022/1/27
 */
@Repository
public interface AccountDao {
    @Select("SELECT id, name, balance FROM tbl_account")
    List<Account> queryList();

    @Select("SELECT id, name, balance FROM tbl_account WHERE id = #{id}")
    Account queryById(int id);

    @Insert("INSERT INTO tbl_account(name, balance) VALUES (#{name}, #{balance})")
    int insert(Account account);

    @Delete("DELETE FROM tbl_account WHERE id = #{id}")
    int deleteById(int id);

    @Update("UPDATE tbl_account SET name = #{name}, balance = #{balance} WHERE id = #{id}")
    void update(Account account);
}

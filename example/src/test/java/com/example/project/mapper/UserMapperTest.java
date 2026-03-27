package com.example.project.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.example.project.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserMapper 单元测试
 *
 * @author CodeGenerator
 */
@MybatisPlusTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("测试根据ID查询")
    void testSelectById() {
        // 注意：此测试需要真实的数据库连接
        // 在集成测试环境中执行
        User user = userMapper.selectById(1L);
        // 验证结果（根据实际数据调整）
        if (user != null) {
            assertNotNull(user.getUsername());
        }
    }

    @Test
    @DisplayName("测试插入用户")
    void testInsert() {
        User user = new User();
        user.setUsername("test_user_" + System.currentTimeMillis());
        user.setPassword("password123");
        user.setRealName("测试用户");
        user.setEmail("test@example.com");
        user.setPhone("13900000000");
        user.setGender(1);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());

        int result = userMapper.insert(user);

        assertEquals(1, result);
        assertNotNull(user.getId());
    }

    @Test
    @DisplayName("测试根据用户名查询")
    void testSelectByUsername() {
        // 使用MyBatis Plus的查询方法
        User user = userMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getUsername, "admin")
        );

        if (user != null) {
            assertEquals("admin", user.getUsername());
        }
    }

    @Test
    @DisplayName("测试查询所有用户")
    void testSelectList() {
        var wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .orderByDesc(User::getCreateTime)
                .last("LIMIT 10");

        var users = userMapper.selectList(wrapper);

        assertNotNull(users);
    }

    @Test
    @DisplayName("测试更新用户")
    void testUpdateById() {
        // 先查询一个用户
        User user = userMapper.selectById(1L);
        if (user != null) {
            // 更新用户名
            String newRealName = "更新后的" + user.getRealName();
            user.setRealName(newRealName);

            int result = userMapper.updateById(user);
            assertEquals(1, result);

            // 验证更新结果
            User updated = userMapper.selectById(1L);
            assertEquals(newRealName, updated.getRealName());
        }
    }

    @Test
    @DisplayName("测试删除用户")
    void testDeleteById() {
        // 先插入一条数据
        User user = new User();
        user.setUsername("delete_test_" + System.currentTimeMillis());
        user.setPassword("password123");
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);

        Long userId = user.getId();
        assertNotNull(userId);

        // 删除
        int result = userMapper.deleteById(userId);
        assertEquals(1, result);

        // 验证删除结果
        User deleted = userMapper.selectById(userId);
        assertNull(deleted);
    }
}

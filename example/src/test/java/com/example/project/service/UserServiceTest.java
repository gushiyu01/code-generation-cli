package com.example.project.service;

import com.example.project.dto.UserQueryDTO;
import com.example.project.entity.User;
import com.example.project.mapper.UserMapper;
import com.example.project.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UserService 单元测试
 *
 * @author CodeGenerator
 */
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserQueryDTO queryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("admin");
        testUser.setPassword("password123");
        testUser.setRealName("管理员");
        testUser.setEmail("admin@example.com");
        testUser.setPhone("13800138000");
        testUser.setGender(1);
        testUser.setStatus(1);
        testUser.setCreateTime(LocalDateTime.now());

        queryDTO = new UserQueryDTO();
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(10);
    }

    @Test
    @DisplayName("测试分页查询用户列表")
    void testList() {
        List<User> users = Arrays.asList(testUser);
        when(userMapper.selectList(any())).thenReturn(users);

        List<User> result = userService.list(queryDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).getUsername());
        verify(userMapper, times(1)).selectList(any());
    }

    @Test
    @DisplayName("测试根据ID查询用户")
    void testGetById() {
        when(userMapper.selectById(1L)).thenReturn(testUser);

        User result = userService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("admin", result.getUsername());
        verify(userMapper, times(1)).selectById(1L);
    }

    @Test
    @DisplayName("测试根据ID查询用户-不存在")
    void testGetByIdNotFound() {
        when(userMapper.selectById(999L)).thenReturn(null);

        User result = userService.getById(999L);

        assertNull(result);
        verify(userMapper, times(1)).selectById(999L);
    }

    @Test
    @DisplayName("测试新增用户")
    void testSave() {
        when(userMapper.insert(any(User.class))).thenReturn(1);

        boolean result = userService.save(testUser);

        assertTrue(result);
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    @DisplayName("测试新增用户失败")
    void testSaveFailed() {
        when(userMapper.insert(any(User.class))).thenReturn(0);

        boolean result = userService.save(testUser);

        assertFalse(result);
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    @DisplayName("测试更新用户")
    void testUpdateById() {
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        boolean result = userService.updateById(testUser);

        assertTrue(result);
        verify(userMapper, times(1)).updateById(any(User.class));
    }

    @Test
    @DisplayName("测试删除用户")
    void testDeleteById() {
        when(userMapper.deleteById(1L)).thenReturn(1);

        boolean result = userService.deleteById(1L);

        assertTrue(result);
        verify(userMapper, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("测试批量删除用户")
    void testDeleteBatchIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(userMapper.deleteBatchIds(ids)).thenReturn(3L);

        boolean result = userService.deleteBatchIds(ids);

        assertTrue(result);
        verify(userMapper, times(1)).deleteBatchIds(ids);
    }

    @Test
    @DisplayName("测试查询条件为空时查询所有")
    void testListWithEmptyQuery() {
        List<User> users = Arrays.asList(testUser);
        when(userMapper.selectList(any())).thenReturn(users);

        UserQueryDTO emptyQuery = new UserQueryDTO();
        List<User> result = userService.list(emptyQuery);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("测试模糊查询条件")
    void testListWithLikeQuery() {
        queryDTO.setUsername("admin");
        List<User> users = Arrays.asList(testUser);
        when(userMapper.selectList(any())).thenReturn(users);

        List<User> result = userService.list(queryDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}

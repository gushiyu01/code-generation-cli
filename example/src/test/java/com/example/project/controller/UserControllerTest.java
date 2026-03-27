package com.example.project.controller;

import com.example.project.common.PageResult;
import com.example.project.common.Result;
import com.example.project.entity.User;
import com.example.project.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController 单元测试
 *
 * @author CodeGenerator
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IUserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    @DisplayName("测试分页查询用户列表")
    void testList() throws Exception {
        when(userService.list(any())).thenReturn(Arrays.asList(testUser));

        mockMvc.perform(post("/user/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pageNum\":1,\"pageSize\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.data.list").isArray());
    }

    @Test
    @DisplayName("测试根据ID查询用户")
    void testGetById() throws Exception {
        when(userService.getById(1L)).thenReturn(testUser);

        mockMvc.perform(get("/user/get")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }

    @Test
    @DisplayName("测试根据ID查询用户-不存在")
    void testGetByIdNotFound() throws Exception {
        when(userService.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/user/get")
                        .param("id", "999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.msg").value("用户不存在"));
    }

    @Test
    @DisplayName("测试新增用户")
    void testSave() throws Exception {
        when(userService.save(any(User.class))).thenReturn(true);

        mockMvc.perform(post("/user/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("新增成功"));
    }

    @Test
    @DisplayName("测试更新用户")
    void testUpdate() throws Exception {
        when(userService.updateById(any(User.class))).thenReturn(true);

        testUser.setRealName("更新后的管理员");
        mockMvc.perform(post("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("更新成功"));
    }

    @Test
    @DisplayName("测试删除用户")
    void testDelete() throws Exception {
        when(userService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(get("/user/delete")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("删除成功"));
    }

    @Test
    @DisplayName("测试批量删除用户")
    void testBatchDelete() throws Exception {
        when(userService.deleteBatchIds(any())).thenReturn(true);

        mockMvc.perform(post("/user/batchDelete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1, 2, 3]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("批量删除成功"));
    }

    @Test
    @DisplayName("测试缺少ID参数查询")
    void testGetByIdMissingParam() throws Exception {
        mockMvc.perform(get("/user/get"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("测试新增用户参数校验失败")
    void testSaveValidationFail() throws Exception {
        testUser.setUsername(""); // 用户名为空
        testUser.setEmail("invalid-email"); // 邮箱格式错误

        mockMvc.perform(post("/user/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());
    }
}

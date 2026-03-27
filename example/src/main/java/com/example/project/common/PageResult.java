package com.example.project.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 *
 * @author CodeGenerator
 */
@Data
@Schema(description = "分页结果")
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "数据列表")
    private List<T> list;

    @Schema(description = "总数")
    private long total;

    @Schema(description = "页码")
    private int pageNum;

    @Schema(description = "每页条数")
    private int pageSize;

    public PageResult() {
    }

    public PageResult(List<T> list, long total, int pageNum, int pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * 构建成功响应
     */
    public static <T> Result<PageResult<T>> success(List<T> list, long total, int pageNum, int pageSize) {
        PageResult<T> pageResult = new PageResult<>(list, total, pageNum, pageSize);
        return Result.success(pageResult);
    }
}

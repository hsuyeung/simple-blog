package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.file.FileInfoVO;
import com.hsuyeung.blog.service.IFileService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 文件上传下载 api
 *
 * @author hsuyeung
 * @date 2022/06/27
 */
@Api(tags = "文件上传下载相关接口")
@RestController
public class FileApi implements IBaseWebResponse {
    @Resource
    private IFileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("/api/file/upload")
    public WebResponse<String> upload(@ApiParam("文件") @RequestParam("file") MultipartFile file) throws IOException {
        return ok(fileService.upload(file));
    }

    @ApiOperation("文件读取")
    @GetMapping("/file/{year}/{month}/{day}/{fileName}")
    public void readFile(
            @ApiParam("年") @PathVariable("year") String year,
            @ApiParam("月") @PathVariable("month") String month,
            @ApiParam("日") @PathVariable("day") String day,
            @ApiParam("文件名") @PathVariable("fileName") String fileName,
            HttpServletResponse response) throws UnsupportedEncodingException {
        fileService.readFile(year, month, day, fileName, response);
    }

    /**
     * 查询文件上传记录分页列表
     *
     * @param url            文件访问路径，全模糊
     * @param startTimestamp 开始时间戳，大于等于
     * @param endTimestamp   结束时间戳，小于等于
     * @param pageNum        页码
     * @param pageSize       每页数量
     * @return 文件上传记录分页列表
     */
    @ApiOperation("查询文件上传记录分页列表")
    @GetMapping("/api/file/page")
    public WebResponse<PageVO<FileInfoVO>> getFilePage(
            @ApiParam("文件 url") @RequestParam(value = "url", required = false) String url,
            @ApiParam("开始时间戳") @RequestParam(value = "startTimestamp", required = false) Long startTimestamp,
            @ApiParam("结束时间戳") @RequestParam(value = "endTimestamp", required = false) Long endTimestamp,
            @ApiParam("页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam("每页数量") @RequestParam("pageSize") Integer pageSize) {
        return ok(fileService.getFilePage(url, pageNum, startTimestamp, endTimestamp, pageSize));
    }
}

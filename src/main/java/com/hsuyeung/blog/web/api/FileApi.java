package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.file.FileSearchDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.file.FileInfoVO;
import com.hsuyeung.blog.service.IFileService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequiredArgsConstructor
public class FileApi implements IBaseWebResponse {
    private final IFileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("/api/file/actions/upload")
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
     * @param pageSearchParam 文件分页搜索条件
     * @return 文件上传记录分页列表
     */
    @ApiOperation("查询文件上传记录分页列表")
    @PostMapping("/api/file/actions/page")
    public WebResponse<PageVO<FileInfoVO>> getFilePage(
            @ApiParam("文件分页搜索条件") @RequestBody PageSearchDTO<FileSearchDTO> pageSearchParam) {
        return ok(fileService.getFilePage(pageSearchParam));
    }
}
